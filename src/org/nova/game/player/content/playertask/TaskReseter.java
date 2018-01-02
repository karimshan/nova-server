package org.nova.game.player.content.playertask;

import org.nova.game.player.Player;

/**
 *@author Fuzen Seth / Nexon
 *Resets carefully playertasks
 */
public class TaskReseter {

	private static transient Player player;

	public static void handle(Player player) {
		deleteProgress();
	}
	/**
	 * This method completes full progress reset, sets all to 0.
	 */
	private static void deleteProgress() {
		PlayerTaskHandler.cleanStages(player);
		player.burned=0;
		player.fished=0;
		player.cooked=0;
		player.treescutted=0;
		player.logscutted=0;
		player.smithed=0;
	}
	

}
