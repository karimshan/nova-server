package org.nova.kshan;

import java.util.Arrays;

import org.nova.cache.Cache;
import org.nova.kshan.content.skills.construction.RoomObjects;
import org.nova.kshan.content.skills.construction.RoomObject;

/**
 * Class for testing arrays.
 * 
 * @author K-Shan
 *
 */
public class ArraysTest {
	
	public static void main(String[] args) {
		try {
			Cache.load();
			RoomObjects.loadAll();
			RoomObject o = RoomObjects.getRoomObject("Test");
			System.out.println("Object IDs Array for "+o.getName()+": "+Arrays.toString(o.getObjectIds()));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
