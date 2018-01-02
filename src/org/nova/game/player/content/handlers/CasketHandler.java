package org.nova.game.player.content.handlers;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;
/**
 * @author Stacx / Fuzen Seth
 *
 */
public class CasketHandler {
	
	private static final int[] caskets = {405/*TODO more*/};
	
	
	/**
	 * 
	 * @param player
	 */
	public static boolean openCaskets(Player player, int itemId) {
		int casket = 0;
		for(int casketIndex : caskets) {
			if(player.getInventory().containsItem(casketIndex, 1)) {
				casket = casketIndex;
				break;
			}
		}
		if(casket == 0 || casket != itemId) {
			return false;
		}
		casketAction(player, casket);
		return true;
	}
	/**
	 * 
	 * @param player
	 */

	public static void casketAction(final Player player, final int casket) {
		boolean openingCasket = false;
		if (openingCasket) {
			return;
		}
		openingCasket = true;
		player.getInventory().deleteItem(casket, 1);
		int[] reward = { 25000, 40000, 50000, 60000, 80000 };
		final int won = reward[Misc.random(reward.length)];
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			boolean openingCasket;
			@Override
			public void run() {
				player.addStopDelay(33);
				player.interfaces().closeChatBoxInterface();
				switch (loop) {
				case 1:
					player.lock();
					player.sm("You are opening the casket...");
					break;
				case 2:
					player.addStopDelay(1);
				player.getInventory().addItem(995, won);
				player.unlock();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You have opened the casket.");
				openingCasket = false;
				player.getInventory().refresh();
				stop();	
					break;
				}
				loop++;
				}
			}, 0, 1);
	}
	/**
	 * 
	 * @return
	 */
	public static int[] getCaskets() {
		return caskets;
	}
	/**
	 * 
	 * @param caskets
	 */
	public static void setCaskets(int[] caskets) {
		//CasketHandler.caskets = caskets;
	}
}
