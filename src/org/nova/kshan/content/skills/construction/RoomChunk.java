package org.nova.kshan.content.skills.construction;

import java.io.Serializable;

import org.nova.game.map.RegionBuilder;

/**
 * 
 * @author K-Shan
 *
 */
public class RoomChunk implements Serializable {
	
	/**
	 * @serialField
	 */
	private static final long serialVersionUID = -2188165559151937317L;
	
	private HouseRoom room;
	private byte x, y, z, rotation;
	
	public RoomChunk(HouseRoom room, int x, int y, int z, int rotation) {
		this.room = room;
		this.x = (byte) x;
		this.y = (byte) y;
		this.z = (byte) z;
		this.rotation = (byte) rotation;
	}
	
	/**
	 * List of all the floor chunks
	 */
	public static int[] TZHAAR_FLOOR = { 300, 636 }, 
		GRASS = { 233, 632 }, 
		DESERT = { 407, 385 },
		SNOW = { 362, 486 };

	public HouseRoom getRoom() {
		return room;
	}
	
	public void setRoom(HouseRoom room) {
		this.room = room;
	}

	public byte getX() {
		return x;
	}

	public byte getY() {
		return y;
	}

	public byte getZ() {
		return z;
	}

	public byte getRotation() {
		return rotation;
	}
	
	public void setRotation(int i) {
		this.rotation = (byte) i;
	}

	public enum Roof {
		ROOF1(233, 634, RegionBuilder.NORTH, RegionBuilder.SOUTH), 
		ROOF2(235, 634, RegionBuilder.NORTH, RegionBuilder.EAST, RegionBuilder.SOUTH), 
		ROOF3(235, 634, RegionBuilder.NORTH, RegionBuilder.EAST, RegionBuilder.SOUTH, RegionBuilder.NORTH);

		private int chunkX, chunkY;
		private int[] dirs;

		private Roof(int chunkX, int chunkY, int... dirs) {
			this.chunkX = chunkX;
			this.chunkY = chunkY;
			this.dirs = dirs;
		}

		public int getChunkX() {
			return chunkX;
		}

		public int getChunkY() {
			return chunkY;
		}

		public int[] getDirs() {
			return dirs;
		}

	}

	public boolean matchesCoords(RoomChunk r) {
		return r.getX() == x && r.getY() == y && r.getZ() == z;
	}
	
}
