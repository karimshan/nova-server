package org.nova.game.player.content;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class QuestBinder {
	/**
	 * -1 - Unidentified
	 */
	private static final int INTERFACE = -1;
	
	
	public static final void update(Player player) {
		updateBinder(player, null);
	}

	public static void updateBinder(Player player, String text) {
		player.packets().sendIComponentText(INTERFACE, -1, text);
	}
	
	public static final void constructQuestBinder(Player player) {
		hideIQuestComponent(player, 1);
		updateBinder(player, ""); //TODO
	}
	
	private static void hideIQuestComponent(Player player, int componentId) {
		player.packets().sendHideIComponent(INTERFACE, componentId, true);
	}

}
