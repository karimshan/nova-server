package org.nova.game.player.content.cities;

import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.game.player.content.dungeoneering.DungeonPartyManager;
/**
 *@author Fuzen Seth 
 */
public class Daemonheim {
	
	public static boolean startDungeoneering(Player player, Item item) {
	if (!player.getEquipment().wearingArmour() || player.getInventory().containsItem(item.getId(), 1)) {
		player.sendMessage("You are carrying items with you, please bank all your items before you enter to dungeon.");
		if (player.getInventory().containsItem(15707, 1) || player.getEquipment().getRingId() == 15707) {
			/**
			 * Player can still continue if he got Ring of kinship. But only that is acceptable.
			 */
			return true;
		}
	}
	new DungeonPartyManager(player);
	player.dungtime = 800;
	return false;
	}
}
