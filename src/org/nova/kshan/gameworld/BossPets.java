package org.nova.kshan.gameworld;

import org.nova.cache.Cache;
import org.nova.cache.definition.NPCDefinition;

/**
 * Purpose: To generate sizes for boss pets
 * Note: This is just a test, this does not work, so don't use this
 * 
 * @author K-Shan
 *
 */
public class BossPets {
	
	public static void main(String[] args) {
		Cache.load();
		int npcId = 13447;
		NPCDefinition def = NPCDefinition.get(npcId);
		int size = def.size;
		String name = def.name;
		int realSize = 128 * size;
		double modded = ((realSize) * ((1.0 / 5.12)));
		System.out.println(npcId+" - "+name+" | Size: "+size+" | Size for pet: "+(realSize)+" "+modded);
	}

}
