package org.nova.game.player.content;

import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;
/**
 * @author Fuzen Seth
 * @information Represents the caskets.
 */
public class Caskets {
	
	/**
	 * Casket rewards.
	 */
	private static final int[] ITEM_REWARDS = {1712, 1215, 4587, 1305, 1079, 1127, };
	
	/**
	 * Open a casket.
	 */
	public static final boolean lootCasket(Player player, Item item) {
		if (player.isDead())
			return false;
		if (item.getName().equals("Casket")) {
			player.getInventory().deleteItem(item.getId(), 1);
			player.addStopDelay(1);
			player.sendMessage("You loot the casket!");
			switch (Misc.getRandom(1)) {
			case 0: // The gold coin reward.
				if (!player.isDonator())
				player.getInventory().addItem(995, Misc.random(50000, 150000));
				else
					player.getInventory().addItem(995, Misc.random(75000, 190000));
				return true;
			case 1: //The item reward.
				player.getInventory().addItem(ITEM_REWARDS[Misc.getRandom(ITEM_REWARDS.length - 1)], 1);
				return true;
			}
		}
		return false;
	}
	
}
