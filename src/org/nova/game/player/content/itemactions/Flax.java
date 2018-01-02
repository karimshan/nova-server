package org.nova.game.player.content.itemactions;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class Flax {
	/**
	 * The picking animation.
	 */
	private static Animation flaxAnimation = new Animation(827);
	
	/**
	 * We pick the flax.
	 */
	public static final void pickFlax(Player player, GlobalObject gameObject) {
		if (!player.getInventory().hasFreeSlots()) {
			player.packets().sendMessage("You don't have enough space in your inventory.");
			return;
		}
		int random = Misc.getRandom(10);
		gameObject.defs();
		gameObject.getId();
		player.setNextAnimation(flaxAnimation);
		player.sendMessage("You pick some flax.");
		player.addStopDelay(3);
		switch (random) {
		case 2:
		case 1:
		case 3:
		case 4:
		case 5:
		case 7:
			Game.removeTemporaryObject(gameObject, 50000, true);
			break;
		}
			player.getInventory().addItem(1779, 1);
		}
}
