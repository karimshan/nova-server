package org.nova.game.player.content.itemactions;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class SpiritShard {

	public static boolean openShards(Player player, Item item) {
		switch (item.getId()) {
		case 15262:
			player.getInventory().deleteItem(15262, 1);
			player.getInventory().addItem(12183,5000);
			player.sendMessage("You open the " + item.getName() + ". You receive 5,000 Spirit shards from the pack.");
			return true;
		}
		return false;
	}
	
	public static void exchangeShards(Player player, Item item) {
		
	int currentPacks = item.getAmount();
	
		if (player.getInventory().containsItem(15262, item.getAmount())) {
		
	}
	}
}
