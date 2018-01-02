package org.nova.game.player.actions;

import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

public class Fillables {

	/**
	 * Player Instance
	 */
	private transient Player player;
	
	/**
	 *  This will complete the filling.
	 * @param player
	 */
	public void startFill(Player player) {
	if (this.getPlayer().getInventory().containsItem(1925, 1)) {
		player.setNextAnimation(new Animation (832));
		player.lock(2);
 		player.getInventory().deleteItem(1925, 1);
		player.getInventory().addItem(1929, 1);
		player.sm("You fill the bucket with some water.");
	}

}


	public Player getPlayer() {
		return player;
	}
	
}
