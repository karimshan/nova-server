package org.nova.game.player.content;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @since 18.1.2014
 * @information World pickables for players.
 */
public class Pickables {

	public static final String[] PICKABLES = {"onion", "wheat", "potato", "cabbage", "roses"};
	/**
	 * Picks a goodie from ground.
	 * @param player
	 * @param gameObject
	 */
	public static final boolean pick(Player player, GlobalObject gameObject) {
		for (String s : PICKABLES) 
			if (gameObject.defs().name.toLowerCase().equals(s)) {
				if (!player.getInventory().hasFreeSlots()) {
					player.sendMessage("You don't have enough space in your inventory.");
					return false;
				}
				player.addStopDelay(3);
				player.setNextAnimation(new Animation(827));
				Game.removeTemporaryObject(gameObject, 50000, true);
				switch (gameObject.defs().name.toLowerCase()) {
				case "potato":
					player.getInventory().addItem(1942, 1);
					return true;
				case "wheat":
					player.getInventory().addItem(1947, 1);
					return true;
				case "cabbage":
					player.getInventory().addItem(1965, 1);
					return true;
				case "roses":
					switch (gameObject.getId()) {
					case 9262: //White rose seed.
						return player.getInventory().addItem(6453, 1); 
					case 9261:
						return player.getInventory().addItem(6455, 1);
					case 9260:
						return player.getInventory().addItem(6454, 1);
					}
					return true;
				}
			}
		return false;

	}

	/**
	 * Takes a banana from a tree.
	 * @param player
	 * @param id
	 * @return
	 */
	public static boolean pickBananas(Player player, int id) {
		switch (id) {
		case 2073:
		case 2072:
		case 2071:
		case 2074:
			if (player.getInventory().hasFreeSlots()) {
				player.addStopDelay(3);
				player.getInventory().addItem(1963,1);
				player.sendMessage("You pick a banana from the tree.");
				player.setNextAnimation(new Animation(834));
			}
			break;
		}
		return false;
	}
}
