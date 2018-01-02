package org.nova.kshan.randoms.dependencies;

import java.awt.Point;

import org.nova.game.map.Location;
import org.nova.game.map.RegionBuilder;
import org.nova.game.player.Player;
import org.nova.kshan.content.areas.Area;
import org.nova.kshan.content.areas.Area.AreaType;

/**
 * 
 * @author K-Shan
 *
 */
public class DynamicMap {
	
	private int[] mapChunks;
	private String name;
	private int baseX, baseY, chunkX, chunkY, sizeX, sizeY;
	
	// Exclusive to custom maps.
	private boolean custom;
	private int chunkOffsetX, chunkOffsetY;
	
	public DynamicMap(int chunkX, int chunkY, int sizeX, int sizeY) {
		setChunkX(chunkX);
		setChunkY(chunkY);
		setSizeX(sizeX);
		setSizeY(sizeY);
		setMapChunks(RegionBuilder.findEmptyChunkBound(getSizeX(), getSizeY()));
		setBaseX(getMapChunks()[0] << 3);
		setBaseY(getMapChunks()[1] << 3);
		setName("DEFAULT");
	}
	
	public DynamicMap(int chunkX, int chunkY) {
		setChunkX(chunkX);
		setChunkY(chunkY);
		setSizeX(1);
		setSizeY(1);
		setMapChunks(RegionBuilder.findEmptyChunkBound(getSizeX(), getSizeY()));
		setBaseX(getMapChunks()[0] << 3);
		setBaseY(getMapChunks()[1] << 3);
		setName("DEFAULT");
	}
	
	public DynamicMap(String name, int chunkX, int chunkY, int sizeX, int sizeY) {
		setChunkX(chunkX);
		setChunkY(chunkY);
		setSizeX(sizeX);
		setSizeY(sizeY);
		setMapChunks(RegionBuilder.findEmptyChunkBound(getSizeX(), getSizeY()));
		setBaseX(getMapChunks()[0] << 3);
		setBaseY(getMapChunks()[1] << 3);
		setName(name);
	}

	/**
	 * Copies one chunk of a real map onto an empty region and copies all of the chunks 
	 * above and to the right of the base chunk by however large sizeX and sizeY are.
	 */
	public void createCopiedMap() {
		RegionBuilder.copyAllPlanesMap(getChunkX(), getChunkY(), getMapChunks()[0], getMapChunks()[1], getSizeX(), getSizeY());
		addMap();
	}
	
	/**
	 * Creates a rectangular 8x8 map which has a true chunk area of (sizeX * sizeY). The map is populated 
	 * with only one chunk from a real map at first (such as the grass floor chunk from construction), 
	 * and then any number of chunks from different maps can be put on the "floor" that was populated 
	 * with the original chunk.
	 */
	public void createCustomMap(int floorChunkX, int floorChunkY, int floorSizeX, int floorSizeY, int offsetX, int offsetY) {
		for(int x = 0; x < floorSizeX; x++)
			for(int y = 0; y < floorSizeY; y++)
		RegionBuilder.copyChunk(floorChunkX, floorChunkY, 0, 
			getMapChunks()[0] + x, getMapChunks()[1] + y, 0, 0);
		for(int x = 0; x < getSizeX(); x++)
			for(int y = 0; y < getSizeY(); y++)
				for(int z = 0; z < 4; z++)
		RegionBuilder.copyChunk(getChunkX() + x, getChunkY() + y, z, 
			getMapChunks()[0] + x + offsetX, getMapChunks()[1] + y + offsetY, z, 0);
		setChunkOffsetX(offsetX);
		setChunkOffsetY(offsetY);
		setCustom(true);
		addMap();
	}
	
	/**
	 * Destroys the map.
	 */
	public void destroyMap(boolean removeFromList) {
		RegionBuilder.destroyMap(getMapChunks()[0], getMapChunks()[1], getSizeX(), getSizeY());
		if(removeFromList)
			DynamicMapList.MAPS.remove(this);
	}
	
	/**
	 * Destroys the map and removes from the dynamic map list.
	 */
	public void destroyMap() {
		destroyMap(true);
	}
	
	/**
	 * Adds a map to the dynamic map regions list.
	 */
	public void addMap() {
		if(!DynamicMapList.MAPS.contains(this))
			DynamicMapList.MAPS.add(this);
		else
			System.out.println("[ERROR]: The map \""+name+"\" is already in the dynamic map list.");
	}
	
	@Override
	public String toString() {
		return "["+name+"]: Base: ["+baseX+", "+baseY+"] Chunk: ["+chunkX+", "+chunkY+"] Extended: ["+
			sizeX+", "+sizeY+"] Center: ["+getCenter().getX()+", "+getCenter().getY()+"] "+(isCustom() ? 
				"Offsets: ["+getChunkOffsetX()+", "+getChunkOffsetY()+"]" : "");
	}
	
	/**
	 * Returns the center of the class's dynamic map
	 * @return area
	 */
	public Location getCenter() {
		Area area = new Area(new Point(isCustom() ? getCustomBaseX() : getBaseX(), 
			isCustom() ? getCustomBaseY() : getBaseY()), 
				new Point((isCustom() ? getCustomBaseX() : getBaseX()) + (getSizeX() * 8), 
					(isCustom() ? getCustomBaseY() : getBaseY()) + (getSizeY() * 8)), 
						(byte) 0, getName(), AreaType.MEMBERS_ONLY);
		return area.getCenter();
	}
	
	/**
	 * Returns the center of any dynamic map.
	 * @param baseX
	 * @param baseY
	 * @param sizeX
	 * @param sizeY
	 * @return
	 */
	public static Location getCenter(int baseX, int baseY, int sizeX, int sizeY) {
		Area area = new Area(new Point(baseX, baseY), 
			new Point(baseX + (sizeX * 8), baseY + (sizeY * 8)), 
				(byte) 0, "DEFAULT", AreaType.MEMBERS_ONLY);
		return area.getCenter();
	}
	
	public static boolean inDynamicMap(Player p) {
		
		return false;
	}
	
	public int[] getMapChunks() {
		return mapChunks;
	}

	public void setMapChunks(int[] mapChunks) {
		this.mapChunks = mapChunks;
	}

	public int getBaseX() {
		return baseX;
	}

	public void setBaseX(int baseX) {
		this.baseX = baseX;
	}

	public int getBaseY() {
		return baseY;
	}

	public void setBaseY(int baseY) {
		this.baseY = baseY;
	}
	
	public int getChunkX() {
		return chunkX;
	}

	public void setChunkX(int chunkX) {
		this.chunkX = chunkX;
	}

	public int getChunkY() {
		return chunkY;
	}

	public void setChunkY(int chunkY) {
		this.chunkY = chunkY;
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}

	public int getChunkOffsetX() {
		return chunkOffsetX;
	}

	public void setChunkOffsetX(int customMapOffsetX) {
		this.chunkOffsetX = customMapOffsetX;
	}

	public int getChunkOffsetY() {
		return chunkOffsetY;
	}

	public void setChunkOffsetY(int customMapOffsetY) {
		this.chunkOffsetY = customMapOffsetY;
	}
	
	/**
	 * Also exclusive to custom maps
	 * @return
	 */
	public int getCustomBaseX() {
		return (getMapChunks()[0] << 3) + (getChunkOffsetX() * 8);
	}
	
	/**
	 * Also exclusive to custom maps
	 * @return
	 */
	public int getCustomBaseY() {
		return (getMapChunks()[1] << 3) + (getChunkOffsetY() * 8);
	}

}
