package org.nova.game.player.content.cities;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class Trapdoor {


	/**
	 * All handled trapdoors.
	 */
	public static boolean handleTrapdoors(Player player, GlobalObject object) {
		switch (object.getId()) {
		case 26933:
			if (containsObjectPosition(object, 3097,3468,0)) {
				
			}
			return true;
		}
		return false;
	}
	/**
	 * Player opens the actual trapdoor.
	 * @param player
	 * @param object
	 */
	public static final void openTrapdoor(Player player, GlobalObject object) {
		
	}
	
	public static void updateTrapdoor(GlobalObject object) {
		Game.spawnObject(object, true);
	}
	/**
	 * Does a object contain a given position?
	 * @param object
	 * @param x
	 * @param y
	 * @param h
	 * @return
	 */
	public static boolean containsObjectPosition(GlobalObject object, int x, int y, int h) {
		return (object.getX() == y && object.getY() == y && object.getZ() == h);
	}
}
