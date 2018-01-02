package org.nova.game.player.content;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * Repairs degraded / broken items such as Nex items.
 * @author JazzyYaYaYa
 *
 */
public class ItemRepair {

	public static void checkItems(Player player, Item item) {
		if (player.getInventory().hasItems(item.getName().equalsIgnoreCase("Torva full helm"), 1)) {
			player.getInventory().deleteItem(item.getId(), 1);
		}
		else if (player.getInventory().hasItems(item.getName().equalsIgnoreCase("Torva platebody"), 1)) {
			player.getInventory().deleteItem(item.getId(), 1);
		}
	}
}
