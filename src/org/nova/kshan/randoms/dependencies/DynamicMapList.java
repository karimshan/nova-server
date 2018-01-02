package org.nova.kshan.randoms.dependencies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nova.game.map.RegionBuilder;

/**
 * This class includes both dynamic region objects of the DynamicMap clas and dynamic regions
 * independent of the DynamicMap class.
 * 
 * @author K-Shan
 *
 */
public class DynamicMapList {
	
	public static List<DynamicMap> MAPS = new ArrayList<DynamicMap>();

	public static DynamicMap getMap(String mapName) {
		for(DynamicMap map : MAPS) {
			if(map != null)
				if(map.getName().equalsIgnoreCase(mapName))
					return map;
		}
		return null;
	}
	
	/**
	 * These are the dynamic region maps that don't use the DynamicMap class.
	 * (These are basically the maps that aren't temporary, such as dynamic region maps)
	 * 
	 */
	public static Map<String, int[]> EXCLUSIVE_MAPS = new HashMap<String, int[]>();

	/**
	 * Removes an exclusive map for any reason. (NOT RECOMMENDED)
	 * @param name
	 */
	public static void removeExclusiveMap(String name) {
		int regionChunkX = EXCLUSIVE_MAPS.get(name)[0];
		int regionChunkY = EXCLUSIVE_MAPS.get(name)[1];
		int sizeX = EXCLUSIVE_MAPS.get(name)[2];
		int sizeY = EXCLUSIVE_MAPS.get(name)[3];
		RegionBuilder.destroyMap(regionChunkX, regionChunkY, sizeX, sizeY);
		EXCLUSIVE_MAPS.remove(name);
	}
	
	/**
	 * Adds a dynamic region map to the exclusive map list.
	 * @param name
	 * @param parameters
	 *
	 */
	public static void addExclusiveMap(String name, int sizeX, int sizeY, int chunkX, int chunkY) {
		int[] newRegionChunks = RegionBuilder.findEmptyChunkBound(sizeX, sizeY);
		int newRegionChunkX = newRegionChunks[0];
		int newRegionChunkY = newRegionChunks[1];
		int baseX = newRegionChunkX << 3;
		int baseY = newRegionChunkY << 3;
		RegionBuilder.copyAllPlanesMap(chunkX, chunkY, newRegionChunkX, newRegionChunkY, sizeX, sizeY);
		EXCLUSIVE_MAPS.put(name, new int[] { newRegionChunkX, newRegionChunkY, sizeX, sizeY, chunkX, chunkY, baseX, baseY });
	}

}
