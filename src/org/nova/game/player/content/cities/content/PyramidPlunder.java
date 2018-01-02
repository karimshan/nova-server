package org.nova.game.player.content.cities.content;

import org.nova.game.map.GlobalObject;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the Pyramid Plunder minigame.
 * @since 10.4.2014
 */
public class PyramidPlunder {
	
	public static final int INTERFACE = 428;
	/**
	 * Gets the pyramid singleton.
	 */
	private static PyramidPlunder pyramidPlunder;
	/**
	 * What room is in the player currently?
	 */
	private int room;
	/**
	 * What level is the player on?
	 */
	private int level;
	
	private int progress;
	
	/**
	 * Holds the id of the unique object that process to the next room.
	 */
	private int correctObject;
	
	public void start(Player player) {
		setRoom(1);
		setLevel(21);
	}
	
	/**
	 * Adds the interface - boolean remove.
	 * @param player
	 * @param needRemove
	 */
	public void updateOverlay(Player player, boolean needRemove) {
		player.packets().sendIComponentText(INTERFACE, 2, "Room: "+getRoom()+" / 8");
		player.packets().sendIComponentText(INTERFACE, 4, "Level: "+getLevel()+"");
	}
	/**
	 * Handles game object.
	 * @param player
	 * @param gameObject
	 * @return
	 */
	public boolean handleGameObject(Player player, GlobalObject gameObject) {
		return false;
	}
	/**
	 * Cancels the minigame tasks & events.
	 * @param player
	 */
	public void cancel(Player player) {
		setRoom(-1);
		setLevel(-1);
	}

	public int getRoom() {
		return room;
	}

	public void setRoom(int room) {
		this.room = room;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public static PyramidPlunder getPyramidPlunder() {
		return pyramidPlunder;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

}
