package org.nova.game.player.content.cities;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class Miscellania {
	
	public static void enter(Player player) {
		if (player.isCamelotKnight()) {
			/*
			 * Teleport action
			 */
		} else {
			player.sm("You cannot enter here, you must complete battle quest: Knight of Camelot.");
			player.interfaces().closeChatBoxInterface();
			
		}
	}

}
