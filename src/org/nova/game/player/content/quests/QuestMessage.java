package org.nova.game.player.content.quests;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information
 */
public class QuestMessage {
	/**
	 * Sends a new quest message.
	 * @param player
	 * @param message
	 */
	
	public QuestMessage(Player player, String message) {
		 QuestManager.getQuestManager().displayQuestPoints(player);
		player.packets().sendIComponentText(1024, 7, message);
		player.packets().sendHideIComponent(1024, 5, true);
		player.interfaces().sendBindingTab();
	
	}
	
	/**
	 * Sets a quest title.
	 * @param player
	 * @param title
	 */
	public static void setTitle(Player player, String title) {
		 QuestManager.getQuestManager().displayQuestPoints(player);
		player.packets().sendIComponentText(1024, 82, title);
		player.packets().sendHideIComponent(1024, 5, true);
		player.interfaces().sendBindingTab();
		
	}
}
