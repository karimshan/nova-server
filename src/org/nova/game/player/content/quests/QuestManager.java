package org.nova.game.player.content.quests;

import org.nova.game.player.Player;

/**
 * @author Fuzen Seth
 */
public class QuestManager {
	/**
	 * Quest manager singleton.
	 */
	private static QuestManager questManager = new QuestManager();
	/**
	 * Displays the quest points.
	 * @param player
	 */
	public void displayQuestPoints(Player player) {
		player.packets().sendIComponentText(1024, 5, "Quest points: "+player.questpoints);
		player.interfaces().sendBindingTab();
	}
	
	/**
	 * Sends the 'Quest complete!' -interface.
	 * @param questTitle
	 * @param messages
	 * @param lines
	 */
	public final void sendQuestCompleted(String questTitle, String messages, int lines) {
		
	}
	
	/**
	 * 
	 * @author Fuzen Seth
	 *
	 */
	public static enum QuestType {
		NOT_STARTED,
		STARTED,
		PROGRESSING,
		FINISHED;
	}
	
	/**
	 * Has the player completed all the quests?
	 * @param player
	 * @return
	 */
	public final boolean completedQuests(Player player) {
		return player.isCompletedHoarfrost() && player.isCompletedHoarfrostDepths() && player.isCompletedRuinsofUzer() && player.isCompletedWanted();
	}

	public static QuestManager getQuestManager() {
		return questManager;
	}

	public static void setQuestManager(QuestManager questManager) {
		QuestManager.questManager = questManager;
	}
	
}
