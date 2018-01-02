package org.nova.kshan.content.interfaces;

import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public class InterfaceConstants {
	
	/**
	 * 
	 * @param player
	 * @param title
	 * @param lines
	 */
	public static void sendQuestLines(Player player, String title, String... lines) {
		player.interfaces().sendInterface(275);
		int number = 0;
		for (int i = 0; i < 316; i++)
			player.packets().sendString("", 275, i);
		player.packets().sendString(title, 275, 2);
		for (String s : lines) {
			if (s == null)
				continue;
			number++;
			player.packets().sendString(s, 275, 15 + number);
		}
		player.packets().sendRunScript(1207, number);
	}

}
