package org.nova.game.map;

import java.io.Serializable;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.utility.misc.Misc;

public class Location implements Serializable {

	private static final long serialVersionUID = -6567346497259686765L;

	public static final int[] VIEWPORT_SIZES = { 104, 120, 136, 168 };

	protected short x;

	protected short y;
	protected byte z;

	public Location(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (byte) plane;
	}
	
	public Location getLocation() {
		return new Location(x, y, z);
	}
	
	public Location(Location tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.z = tile.z;
	}
	
	public boolean withinArea(int a, int b, int c, int d) {
		return getX() >= a && getY() >= b && getX() <= c && getY() <= d;
	}
	  
	public Location(Location tile, int randomize) {
		this.x = (short) (tile.x + Misc.getRandom(randomize * 2) - randomize);
		this.y = (short) (tile.y + Misc.getRandom(randomize * 2) - randomize);
		this.z = tile.z;
	}

	public void moveLocation(int xOffset, int yOffset, int planeOffset) {
		x += xOffset;
		y += yOffset;
		z += planeOffset;
	}

	public final void setCoords(Location tile) {
		setCoords(tile.x, tile.y, tile.z);
	}

	public final void setCoords(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (byte) plane;
	}

	public int getX() {
		return x;
	}
	
	public int getXInChunk() {
		return x & 0x7;
    }

    public int getYInChunk() {
    	return y & 0x7;
    }

	public int getXInRegion() {
		return x & 0x3F;
	}

	public int getYInRegion() {
		return y & 0x3F;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		if (z > 3)
			return 3;
		return z;
	}

	public int getChunkX() {
		return (x >> 3);
	}

	public int getChunkY() {
		return (y >> 3);
	}

	public int getRegionX() {
		return (x >> 6);
	}

	public int getRegionY() {
		return (y >> 6);
	}

	public int getRegionId() {
		return ((getRegionX() << 8) + getRegionY());
	}

	public int getLocalX(Location tile, int mapSize) {
		return x - 8 * (tile.getChunkX() - (VIEWPORT_SIZES[mapSize] >> 4));
	}

	public int getLocalY(Location tile, int mapSize) {
		return y - 8 * (tile.getChunkY() - (VIEWPORT_SIZES[mapSize] >> 4));
	}

	public int getLocalX(Location tile) {
		return getLocalX(tile, 0);
	}

	public int getLocalY(Location tile) {
		return getLocalY(tile, 0);
	}

	public int getLocalX() {
		return getLocalX(this);
	}

	public int getLocalY() {
		return getLocalY(this);
	}

	public int get18BitsLocationHash() {
		return getRegionY() + (getRegionX() << 8) + (z << 16);
	}

	public int get30BitsLocationHash() {
		return y + (x << 14) + (z << 28);
	}

	public boolean withinDistance(Location tile, int distance) {
		if (tile.z != z)
			return false;
		int deltaX = tile.x - x, deltaY = tile.y - y;
		return deltaX <= distance && deltaX >= -distance && deltaY <= distance
				&& deltaY >= -distance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + z;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (z != other.z)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public boolean withinDistance(Location tile) {
		if (tile.z != z)
			return false;
		 return Math.abs(tile.x - x) <= 15 && Math.abs(tile.y - y) <= 15;
	}

	public int getCoordFaceX(int sizeX) {
		return getCoordFaceX(sizeX, -1, -1);
	}

	public static final int getCoordFaceX(int x, int sizeX, int sizeY,
			int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public static final int getCoordFaceY(int y, int sizeX, int sizeY,
			int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	public int getCoordFaceX(int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public int getCoordFaceY(int sizeY) {
		return getCoordFaceY(-1, sizeY, -1);
	}

	public int getCoordFaceY(int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}
	
	public Location transform(int x, int y, int plane) {
		return new Location(this.x + x, this.y + y, this.z + plane);
	}
	
    public int getTileHash() {
    	return y + (x << 14) + (z << 28);
    }

    public static Location findEmptyLocation(Entity entity, int bounds) {
		int x = entity.getX();
		int y = entity.getY();
		int z = entity.getZ();
		int size = entity.getSize();
		if(Game.canMoveNPC(z, x - bounds, y - bounds, size))
			return new Location(x - bounds, y - bounds, z);
		else if(Game.canMoveNPC(z, x + bounds, y + bounds, size))
			return new Location(x + bounds, y + bounds, z);
		else if(Game.canMoveNPC(z, x - bounds, y + bounds, size))
			return new Location(x - bounds, y + bounds, z);
		else if(Game.canMoveNPC(z, x + bounds, y - bounds, size))
			return new Location(x + bounds, y - bounds, z);
		else if(Game.canMoveNPC(z, x + bounds, y, size))
			return new Location(x + bounds, y, z);
		else if(Game.canMoveNPC(z, x - bounds, y, size))
			return new Location(x - bounds, y, z);
		else if(Game.canMoveNPC(z, x, y + bounds, size))
			return new Location(x, y + bounds, z);
		else if(Game.canMoveNPC(z, x, y - bounds, size))
			return new Location(x, y - bounds, z);
		else
			return new Location(x + bounds, y + bounds, z);
	}
    
    public boolean matches(Location other) {
    	return other.getX() == getX() && other.getY() == getY() && getZ() == other.getZ();
	}

    @Override
	public String toString() {
		return "["+x+", "+y+", "+z+"]";
	}
    
    public Region getRegion() {
    	return Game.getRegion(getRegionId(), true);
    }

	public static Location create(Location l) {
		return new Location(l.getX(), l.getY(), l.getZ());
	}
	
	public static Location create(int x, int y, int z) {
		return new Location(x, y, z);
	}

}
