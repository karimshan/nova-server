package org.nova.game.player.content;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.item.Item;
import org.nova.game.player.Player;

public class GodPath {

	public GodPath(Player player) {
		this.player = player;
	}

	private transient Player player;
	
	private String god;
	
	private static int[] godCapes = {2412,2413,2414};
	
	public void refundItems(int inventorySlot) {
	if (!checkAll())
		return;
	final Item item = player.getInventory().getItem(inventorySlot);
	final ItemDefinition itemDef = new ItemDefinition(item.getId());
	
	player.sendMessage("You succesfully have received back the "+itemDef.getName()+".");
}
	public boolean checkAll() {
		player.closeInterfaces();
		player.interfaces().closeChatBoxInterface();
		if (player.getInventory().containsItem(GodPath.getGodCapes().length+1, 1)) {
			player.sendMessage("You have already a cape.");
			return false;
		}
		
		return true;
	}
	public void selectPath() {
		player.interfaces().closeChatBoxInterface();
		player.closeInterfaces();
		if (player.getInventory().hasFreeSlots()) {
			player.sendMessage("You don't have enough space in your inventory.");
			return;
		}
		
		switch (getGod().toString()) {
		
		
		}
		player.sendMessage("You have selected a lord to believe, you believe to "+getGod()+".");
	}
	
	public String getGod() {
		return god;
	}
	
	public void setGod(String god) {
		this.god = god;
		selectPath();
	}
	public static int[] getGodCapes() {
		return godCapes;
	}
	public static void setGodCapes(int[] godCapes) {
		GodPath.godCapes = godCapes;
	}
}
