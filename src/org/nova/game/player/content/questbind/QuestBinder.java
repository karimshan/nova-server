package org.nova.game.player.content.questbind;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 * Reworked Quest binder. 7.11.2013
 */
public class QuestBinder {

	public QuestBinder() {
		
	}
	/**
	 * Interface id of the quest binder.
	 */
	public static final int BINDER_ID = -1;
	/**
	 * SendICText position.
	 */
	public static final int TEXT_POSITION = 1;
	
	/**
	 * Sends a quest message.
	 * @param message
	 */
	public void sendBindMessage(String message) {
	new QuestMessage(message);
		update();
	}
	/**
	 * Quest stage, default 0
	 * @param currentStage
	 * @return
	 */
	public int getStage(int currentStage)  {
		return 0; 
	}
	/**
	 * Updates the binder.
	 */
	private void update() {
		
	}
	

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	private Player player;
}
