package org.nova.game.player.content;

import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class PkPointsHandler {

	/**
	 * Displays the pking points to player.
	 * @param player
	 */
	public static final void displayPKpoints(Player player) {
	player.getMatrixDialogues().startDialogue("SimpleMessage", "You have currently "+player.getPKPoints()+" PK points.");
	}
	
	/**
	 * Increases PK points.
	 */
	public static final void increasePKpoints(Player player) {
		final int randomAmount = Misc.getRandom(300);
		if (!player.isDonator())
		player.setPKPoints(player.getPKPoints() + randomAmount);
		else 
			player.setPKPoints(player.getPKPoints() + randomAmount * 2);
		
		player.sendMessage("You have received "+randomAmount+" PKing points.");
	}
	
}
