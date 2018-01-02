package org.nova.game.player.content.itemactions;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * @author Fuzen Seth
 */
public class MagicBox {

	
	public static void bankItems(Player player, Item item) {
	if (player.bankDelay >= 0) {
		player.sendMessage("You have to wait "+player.bankDelay+" seconds to bank a item again.");
		return;
	}
	player.getInventory().deleteItem(item.getId(), item.getAmount());
	player.sendMessage("You have succesfully banked "+item.getName()+".");
	}
}
