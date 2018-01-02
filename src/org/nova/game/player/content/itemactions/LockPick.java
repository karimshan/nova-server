package org.nova.game.player.content.itemactions;

import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class LockPick {
	
	public static final int LOCK_PICK = 1523;
	
	
	
	public static boolean handleLockPickDoors(Player player, GlobalObject object) {
		switch (object.getId()) {
		
		}
		return false;
	}
	
	/**
	 * Sends a walk to specific coordinate.
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void sendWalk(Player player, int x, int y) {
		player.addWalkSteps(x, y, -1, false);	
	}
	/**
	 * Does the player contain a special tile?
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static boolean playerContainsTile(Player player, int x, int y, int z) {
		return player.getLocation().equals(new Location(x,y,z));
	}
	
	/**
	 * Does the player have a lock pick?
	 * @param player
	 * @return
	 */
	public static boolean containsLockPick(Player player) {
		return player.getInventory().containsItem(LOCK_PICK, 1);
	}
}
