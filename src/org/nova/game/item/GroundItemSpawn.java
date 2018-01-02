package org.nova.game.item;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class GroundItemSpawn {

	/**
	 * Loads the ground items.
	 */
	public static final void load() {
		
	}
	/**
	 * Adds a spawned ground item.
	 * @param id
	 * @param x
	 * @param y
	 * @param plane
	 */
	public static final void addSpawnedGroundItem(int id, int x, int y, int plane) {

	}
	
	public static void removeAll(Player player) {
		for (int i = 0; i < 21000; i++) {
		final FloorItem floorItem = new FloorItem(i);
			//	Item floorItem = new Item(i);
		player.packets().sendRemoveGroundItem(floorItem);
	}
	}
}
