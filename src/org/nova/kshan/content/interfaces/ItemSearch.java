package org.nova.kshan.content.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.kshan.content.keystroke.KeyStrokeData;
import org.nova.kshan.input.type.string.StringInput;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class ItemSearch {
	
	/**
	 * Searches for an item in the item search pane
	 * @param player
	 * @param bId
	 */
	public static void searchItem(Player player, int bId, int slotId, int packetId) {
		if(player.getRights() < 2 && !player.isHiddenOwner())
			return;
		switch(bId) {
			case 0: // The Container
				@SuppressWarnings("unchecked")
				List<Integer> data = (List<Integer>) player.getData().getRuntimeData().get("item_search");
				int id = data.get(slotId);
				switch(packetId) {
					case 61: // Spawn 1
						player.getInventory().addItem(id, 1);
						break;
					case 64: // Spawn 5
						player.getInventory().addItem(id, 5);
						break;
					case 4: // Spawn 10
						player.getInventory().addItem(id, 10);
						break;
					case 52: // Spawn 10,000
						if(ItemDefinition.get(id).isStackable() && player.getInventory().hasFreeSlots())
							player.getInventory().addItem(id, 10000);
						else {
							player.getBank().addItem(id, 10000, true);
							player.sm(new Item(id).getName()+" x10,000 has been added to your bank.");
						}
						break;
					case 81: // Examine item
						player.sm(new Item(id).getExamine());
						break;
				}

				break;
			case 2: // Exit Search
				if(player.getData().getRuntimeData().get("item_search") != null)
					player.packets().sendRunScriptBlank(138);
				player.getData().getRuntimeData().remove("item_search");
				player.interfaces().sendItemSearchTab();
				break;
			case 3: // Search
				player.packets().sendString("<col=ffffff>Results: ", 1150, 4);
				player.getData().getRuntimeData().remove("item_search");
				player.getInputEvent().run(new StringInput() {
					
					private String result = "";

					@Override
					public void process(String input) {
						
					}		

					@Override
					public void whileTyping(int key, char keyChar, boolean shiftHeld) {
						if(KeyStrokeData.getKey(key).contains("space") && result.length() < 1)
							result = "";
						else if(KeyStrokeData.getKey(key).toLowerCase().contains("backspace"))
							result = result.substring(0, result.length() - 1);
						else if(KeyStrokeData.getKey(key).toLowerCase().contains("space"))
							result += " ";
						else if(KeyStrokeData.getKey(key).length() == 1)
							result += ""+keyChar;
						player.packets().sendString("<col=ffffff>Results: <col=ff0000><shad=000000>"+result, 1150, 4);
						int count = 0;
						boolean loaded = false;
						if (!loaded)
							for (int i = 0; i < Misc.getItemsSize(); i++) {
								ItemDefinition.get(i);
								loaded = true;
							}
						List<Integer> ids = new ArrayList<Integer>();
						for (ItemDefinition definition : ItemDefinition.itemDefinitions.values()) {
							String output = definition.name.toLowerCase();
							int itemId = definition.getId();
							ItemDefinition defs = ItemDefinition.get(itemId);
							if (output.contains(result.toLowerCase()) && !defs.isNoted()) {
								count++;
								if (count > 160)
									break;
								ids.add(itemId);
							}
						}
						player.getData().getRuntimeData().put("item_search", ids);
						if(ids.size() == 0)
							player.interfaces().sendItemSearchTab();
						else {
							Item[] items = new Item[ids.size()];
							int index = 0;
							for(int i : ids) {
								items[index] = new Item(i, 1);
								index++;
							}
							player.packets().sendItems(90, false, items);
							player.packets().sendInterSetItemsOptionsScript(1150, 0, 90, 5, 32, "Spawn", "Spawn 5", "Spawn 10", "Spawn 10,000", "Examine");
							player.packets().sendUnlockIComponentOptionSlots(1150, 0, 0, 160, 0, 1, 2, 3, 4, 5);
						}
						if(result.equals(""))
							player.interfaces().sendItemSearchTab();
					}
					
				}, "Enter the name of the item you would like to find:");
				break;
		}
	}

}
