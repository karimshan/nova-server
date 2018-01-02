package org.nova.game.player.content.itemactions;

import org.nova.cache.definition.ItemDefinition;
import org.nova.cache.stream.InputStream;
import org.nova.game.item.Item;
import org.nova.game.masks.ForceTalk;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

public class test {

	private static InputStream stream;
	
	public test(InputStream stream) {
		this.setStream(stream);
	}
	
	public static void generateRandomItem(Player player, Item itemToAlch) {
		int toSlot = getStream().readShortLE128();
		int randomItem = Misc.getRandom(3);
		switch (randomItem) {
		case 0:
			itemToAlch.setId(4151);
			player.sm("Item set: "+itemToAlch.getName()+".");
			break;
		case 1:
			itemToAlch.setId(6585);
			player.sm("Item set: "+itemToAlch.getName()+".");
			break;
		case 2:
			itemToAlch.setId(5698);
			player.sm("Item set: "+itemToAlch.getName()+".");
			break;
		case 3:
			itemToAlch.setId(11283);
			player.sm("Item set: "+itemToAlch.getName()+".");
			break;
		}
	}
	public static void testalch(Player player) {
		int toSlot = getStream().readShortLE128();
		int hash1 = getStream().readInt();
		int hash2 = getStream().readInt();
		Item itemToAlch = player.getInventory().getItem(toSlot);
		ItemDefinition def = ItemDefinition.get(itemToAlch.getId());
		generateRandomItem(player, itemToAlch);
		player.getInventory().deleteItem(itemToAlch.getId(), 1);
		player.getInventory().addItem(995, def.getValue());
		player.setNextForceTalk(new ForceTalk("I have attempted to ALCH ITEM: "+itemToAlch.getName()+"!"));
		player.sm("Test: You succesfully alch " +itemToAlch.getName()+ ", coins received: " + def.getValue() +".");
	}

	public static InputStream getStream() {
		return stream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}
}
