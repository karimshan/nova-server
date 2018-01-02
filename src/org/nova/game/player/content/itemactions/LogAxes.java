package org.nova.game.player.content.itemactions;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the world logs which has an axe.
 * @since 25.5.2014
 */
public class LogAxes {
	/**
	 * Holds the pick up animation.
	 */
	private static Animation PICK_ANIM = new Animation(832);
	
	/**
	 * Handles the world logs which has an axe.
	 * @param player
	 */
	public static final boolean handleLogs(Player player, GlobalObject gameObject) {
		for (Logs logs : Logs.values()) {
		player.setNextAnimation(PICK_ANIM);
		player.sendMessage("You pick the hatchet from the log.");
		//Adds the new empty log.
		Game.spawnTemporaryObject(
				new GlobalObject(logs.getLogId(), gameObject.getType(),
						gameObject.getRotation(), gameObject.getX(), gameObject.getY(), gameObject
								.getZ()), 600 * 2);
		player.getInventory().addItem(logs.getHatchetId(), 1);
		}
		return false;
	}
	
	/**
	 * 
	 * @author Fuzen Seth
	 * This enum represents the object ids, outcoming ids and the item ids.
	 */
	public enum Logs {
		LUMBRIDGE_CHICKENS(36974, 36975, 1351);
		
		private int logId;
		private int replaceLogId;
		private int hatchetId;
		
		private Logs(int logId, int replaceLogId, int hatchetId) {
			this.logId = logId;
			this.replaceLogId = replaceLogId;
			this.hatchetId = hatchetId;
		}
		
		public int getLogId() {
			return logId;
		}
		
		public int getReplaceLogId() {
			return replaceLogId;
		}
		
		public int getHatchetId() {
			return hatchetId;
		}
	}
	
}
