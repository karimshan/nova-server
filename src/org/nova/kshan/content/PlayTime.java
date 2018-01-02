package org.nova.kshan.content;

import java.io.Serializable;

import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public class PlayTime implements Serializable {

	/**
	 * @serialField
	 */
	private static final long serialVersionUID = 8707269660046357289L;
	
	/**
	 * Represents the {@code Player}
	 */
	protected Player player;
	
	/**
	 * The time vars {@value Seconds, Minutes, Hours, Days}
	 */
	public static final byte SECONDS = 0, MINUTES = 1, HOURS = 2, DAYS = 3;
	
	/**
	 * The play time array that holds the time vars.
	 */
	private int[] playTime = new int[4];
	
	/**
	 * Sets the player.
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * New instance.
	 */
	public PlayTime() {

	}
	
	/**
	 * Processes the play time as a tick at every {@value 1 second.}
	 */
	public void processPlayTime() {
		if(getPlayTime(HOURS) == 24) {
			playTime[HOURS] = 0;
			playTime[DAYS]++;
		} else if(getPlayTime(MINUTES) == 59) {
			playTime[MINUTES] = 0;
			playTime[HOURS]++;
		} else if(getPlayTime(SECONDS) == 59) {
			playTime[SECONDS] = 0;
			playTime[MINUTES]++;
		} else if(getPlayTime(SECONDS) < 60)
			playTime[SECONDS]++;
	}
	
	/**
	 * Sets the playing time.
	 * @param index
	 * @param amount
	 */
	public void setPlayTime(int index, int amount) {
		playTime[index] = amount;
	}

	/**
	 * Returns the play time of any of the time vars.
	 * @param index
	 * @return
	 */
	public int getPlayTime(int index) {
		return playTime[index];
	}

	public void reset() {
		for(int i = 0; i <= 3; i++)
			setPlayTime(i, 0);
	}
}
