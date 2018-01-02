package org.nova.game.player.content.cities;

import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author JazzyYaYaYa | Nexon  | Fuzen Seth
 *
 */
public class Underwater {

	public static void leaveUnderwater(Player player) {
		processRandomLocation(player);
		player.sm("You have left the underwater.");
		removeUnderwaterEffects(player);
	}
	
	/**2199
	 *  This does the random teleportation from underwater
	 * @param player
	 */
	private static void processRandomLocation(Player player) {
	int randomloc = 0;
	randomloc = Misc.random(3);
	if (randomloc == 1) {
		player.teleportPlayer(2714, 3514, 0);
	} else if (randomloc == 2) {
		player.teleportPlayer(2674, 3564, 0);
	} else if (randomloc == 3 ) {
		player.teleportPlayer(2663, 3552, 0);
	}
	
		
	}
	public static void constructUnderwaterEffects(Player player) {
		player.interfaces().sendOverlay(775, true);
		player.interfaces().sendOverlay(775, false);
		player.sm("You are in underwater!");
		player.setRun(false);
		player.getAppearance().setRenderEmote(267);
	}
	
	public static void removeUnderwaterEffects(Player player) {
		forceClose(player);
		player.sm("You have left from underwater.");
	}
	
	public static void forceClose(Player player) {
		player.getAppearance().setRenderEmote(-1);
		player.interfaces().closeChatBoxInterface();
		player.interfaces().closeScreenInterface();
		player.closeInterfaces();
		player.setTeleport(false);
		player.interfaces().closeOverlay(true);
		player.interfaces().closeOverlay(false);
	}
	
}
