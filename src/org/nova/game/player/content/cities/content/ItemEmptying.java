package org.nova.game.player.content.cities.content;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class ItemEmptying {
	
	private static final int EMPTY_BUCKET = 1925;
	
	private static final int EMPTY_VIAL = 229;
	
	public static boolean emptyingItem(Player player, Item item) {
	if (item.getName().equals("Vial of water pack"))
	return false;
		 if (item.getName().contains("potion") || item.getName().contains("Overload") ||
				 (item.getName().contains("Super") && !item.getName().contains("Superior")) || item.getName().contains("brew") || 
				 item.getName().contains("Vial")) {
				player.getInventory().deleteItem(item.getId(), 1);
				player.getInventory().addItem(EMPTY_VIAL, 1);
				player.sendMessage("You empty the " + item.getName().toLowerCase() + ".");
			 return true;
		 }
		 else if (item.getName().contains("Bucket of")) {
				player.getInventory().deleteItem(item.getId(), 1);
				player.getInventory().addItem(EMPTY_BUCKET, 1);
				player.sendMessage("You empty the " + item.getName().toLowerCase() + ".");
			 return true;
		 }
		 	return false;
	}
}
