package org.nova.game.map;

/**
 * 
 * @author K-Shan
 *
 */
public enum Directions {
	
	NORTH(0, 1),
	NORTHEAST(1, 1),
	EAST(1, 0),
	SOUTHEAST(1, -1),
	SOUTH(0, -1),
	SOUTHWEST(-1, -1),
	WEST(-1, 0),
	NORTHWEST(-1, 1);
	
	private int offsetX;
	private int offsetY;
	
	private Directions(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}
}
