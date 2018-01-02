package org.nova.game.player.content.cities;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class Monastery {

	
	public static void sendMonkHeal(Player player) {
		player.interfaces().closeChatBoxInterface();
		player.heal((int) (player.getMaxHitpoints() * 0.02));
		player.getMatrixDialogues().startDialogue("SimpleMessage", "Abbot Langey places his hand on your head. You feel a little better.");
	}
}
