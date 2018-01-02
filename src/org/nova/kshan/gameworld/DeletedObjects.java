package org.nova.kshan.gameworld;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;

/**
 * 
 * @author K-Shan
 *
 */
public class DeletedObjects {

	/**
	 * Restricts the specified map tiles from spawning objects.
	 */
	public static final void delete() {
		
		// Some map tiles at boss area.
		addTile(80, 220, 0);
		addTile(79, 221, 0);
		addTile(77, 223, 0);
		addTile(76, 218, 0);
		
	}
	
	/**
	 * Spawns a blank object on the specified coordinates.
	 * @param x
	 * @param y
	 * @param z
	 */
	private static void addTile(int x, int y, int z) {
		Game.spawnNewObject(new GlobalObject(0, 10, 0, x, y, z));
	}

}
