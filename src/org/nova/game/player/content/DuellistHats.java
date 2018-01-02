package org.nova.game.player.content;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class DuellistHats {
	
	public static void addHat(Player player) {
		player.getInventory().addItem(20795,1);
		player.interfaces().closeChatBoxInterface();
		player.closeInterfaces();
		if (player.duelWins >= 19) {
			player.getInventory().addItem(20796,1);
		}
		if (player.duelWins >= 49) {
			player.getInventory().addItem(20797,1);
		}
		if (player.duelWins >= 74) {
			player.getInventory().addItem(20798,1);
		}
		 if (player.duelWins >= 99) {
			player.getInventory().addItem(20799,1);
		}
		 if (player.duelWins >= 149) {
			player.getInventory().addItem(20800,1);
		}
	}

}
