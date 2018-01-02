package org.nova.game.player;

import org.nova.game.map.Location;

public final class CoordsEvent {

	private Location tile;
	private Runnable event;
	private int sizeX;
	private int sizeY;

	public CoordsEvent(Location tile, Runnable event, int sizeX, int sizeY,
			int rotation) {
		this.tile = tile;
		this.event = event;
		if (rotation == 1 || rotation == 3) {
			this.sizeX = sizeY;
			this.sizeY = sizeX;
		} else {
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}
	}

	public CoordsEvent(Location tile, Runnable event, int sizeX, int sizeY) {
		this(tile, event, sizeX, sizeY, -1);
	}

	public CoordsEvent(Location tile, Runnable event, int size) {
		this(tile, event, size, size);
	}

	/*
	 * returns if done
	 */
	public boolean processEvent(Player player) {
		if (player.getZ() != tile.getZ())
			return true;
		int distanceX = player.getX() - tile.getX();
		int distanceY = player.getY() - tile.getY();
		if (distanceX > sizeX || distanceX < -1 || distanceY > sizeY
				|| distanceY < -1)
			return cantReach(player);
		if (player.hasWalkSteps())
			player.resetWalkSteps();
		event.run();
		return true;
	}

	public boolean cantReach(Player player) {
		if (!player.hasWalkSteps() && player.getNextWalkDirection() == -1) {
			player.packets().sendMessage("You can't reach that.");
			return true;
		}
		return false;
	}
}