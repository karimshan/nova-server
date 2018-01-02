package org.nova.game.player.content.questbind;

import org.nova.game.player.Player;
/**
 * 
 * @author Fuzen Seth
 * @since 5.1.2014
 * @information Represents a quest message.
 */
public class QuestMessage {
	/**
	 * The actual message.
	 */
	private String message;
	/**
	 * The player.
	 */
	private Player player;
	public static int QUEST_BIND = 1024,MAIN_TITLE = 82,MAIN_TEXT = 7,
	QUEST_POINTS = 5,QUEST_INTERFACE = 72,BIND_TAB = 174
	;
	/**
	 * Adds a quest message.
	 * @param message
	 */
	public QuestMessage(String message) {
		this.message = message;
		player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT, message);
		/**
		 * TODO Sendtab (for update)
		 */
	}
}
