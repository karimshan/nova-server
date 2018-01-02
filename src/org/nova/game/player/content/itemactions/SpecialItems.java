package org.nova.game.player.content.itemactions;

import org.nova.game.map.GlobalObject;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class SpecialItems {

	
	public static boolean handleItemOnObject(Player player, int itemId, GlobalObject object) {
		if (itemId == 11286 && object.defs().name.toLowerCase().contains("Anvil")) {
			if (player.getSkills().getLevel(Skills.SMITHING) >= 89) {
				player.sendMessage("You must have atleast Smithing level of 90.");
				return false;
			}
			createDragonFireShield(player);
			return true;
		}
		return false;
	}
	
	public static void createDragonFireShield(Player player) {
		if (!hasDFSRequirements(player)) {
			player.sendMessage("You don't have the required items to create dragon fire shield.");
	return;
		}
		player.addStopDelay(3);
		player.setNextAnimation(new Animation(898));
		player.getInventory().deleteItem(11286,1);
		player.getInventory().deleteItem(1540,1);
		player.getInventory().addItem(11283,1);
		player.sendMessage("You succesfully create a Dragonfire shield.");
	}
	/**
	 * Does the player have requirements for DFS?
	 * @param player
	 * @param object
	 * @return
	 */
	public static boolean hasDFSRequirements(Player player) {
		return player.getInventory().containsItem(1540, 1) && player.getInventory().containsItem(2347, 1) && player.getInventory().containsItem(11286, 1);
	}
}
