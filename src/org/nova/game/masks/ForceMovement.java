package org.nova.game.masks;

import org.nova.game.map.Location;
import org.nova.utility.misc.Misc;

public class ForceMovement {

	public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;
	
	private Location toFirstTile;
	private Location toSecondTile;
	private int firstTileTicketDelay;
	private int secondTileTicketDelay;
	protected int direction;

	/*
	 * USE: moves to firsttile firstTileTicketDelay: the delay in game tickets
	 * between your tile and first tile the direction
	 */
	public ForceMovement(Location toFirstTile, int firstTileTicketDelay,
			int direction) {
		this(toFirstTile, firstTileTicketDelay, null, 0, direction);
	}
	/*
	 * USE: moves to firsttile and from first tile to second tile
	 * firstTileTicketDelay: the delay in game tickets between your tile and
	 * first tile secondTileTicketDelay: the delay in game tickets between first
	 * tile and second tile the direction
	 */
	public ForceMovement(Location toFirstTile, int firstTileTicketDelay,
			Location toSecondTile, int secondTileTicketDelay, int direction) {
		this.toFirstTile = toFirstTile;
		this.firstTileTicketDelay = firstTileTicketDelay;
		this.toSecondTile = toSecondTile;
		this.secondTileTicketDelay = secondTileTicketDelay;
		this.direction = direction;
	}
	
	public int getDirection() {
		switch(direction) {
		case NORTH:
			return Misc.getFaceDirection(0, 1);
		case EAST:
			return Misc.getFaceDirection(1, 0);
		case SOUTH:
			return Misc.getFaceDirection(0, -1);
		case WEST:
		default:
			return Misc.getFaceDirection(-1, 0);
		}
	}

	public Location getToFirstTile() {
		return toFirstTile;
	}

	public Location getToSecondTile() {
		return toSecondTile;
	}

	public int getFirstTileTicketDelay() {
		return firstTileTicketDelay;
	}

	public int getSecondTileTicketDelay() {
		return secondTileTicketDelay;
	}

}
