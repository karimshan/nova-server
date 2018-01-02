package org.nova.kshan.content.skills.construction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.kshan.utilities.ArrayUtils;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class RoomObjects {
	
	public static void loadAll() {
		loadRoomObjects();
		loadBuildables();
	}
	
	private static List<RoomObject> ROOM_OBJECTS = new ArrayList<RoomObject>();

	public static List<RoomObject> getRoomObjects() {
		return ROOM_OBJECTS;
	}
	
	public static RoomObject getRoomObject(String name) {
		for(RoomObject obj : getRoomObjects())
			if(obj.getName().toLowerCase().equals(name.toLowerCase()))
				return obj;
		return null;
	}
	
	public static void loadRoomObjects() {
		ROOM_OBJECTS.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader("data/playerdata/construction/houseObjects.txt"));
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith("#") || line.equals(""))
					continue;
				String[] tokens = line.split(" - ");
				String name = tokens[0];
				int itemId = Integer.parseInt(tokens[1]);
				byte levelRequired = Byte.parseByte(tokens[2]);
				double xp = Double.parseDouble(tokens[3]);
				String[] objectIdTokens = tokens[4].split(", ");
 				int[] objectIds = new int[objectIdTokens.length];
 				boolean hasMultipleObjectsMarker = false;
 				boolean hasDuplicateMarker = false;
 				ArrayList<Integer[]> multipleObjects = new ArrayList<Integer[]>();
 				Map<Integer, Integer> duplicateObjects = new HashMap<Integer, Integer>();
				for(int i = 0; i < objectIds.length; i++) {
					try {
						objectIds[i] = Integer.parseInt(objectIdTokens[i]);
					} catch(NumberFormatException e) {
						if(objectIdTokens[i].contains("x")) {
							hasDuplicateMarker = true;
							String[] duplicateAmounts = objectIdTokens[i].split("x");
							int objectId = Integer.parseInt(duplicateAmounts[0]);	
							int amount = Integer.parseInt(duplicateAmounts[1]);
							duplicateObjects.put((objectId + 100000), amount);
							objectIds[i] = (objectId + 100000);
						} 
						if(objectIdTokens[i].contains(":")) {
							hasMultipleObjectsMarker = true;
							String[] multipleObjectTokens = objectIdTokens[i].split(":");
							int startNum = Integer.parseInt(multipleObjectTokens[0]);	
							int endNum = Integer.parseInt(multipleObjectTokens[1]);
							int multipleObjectsLength = (endNum - startNum) + 1;
							int[] newObjectIds = new int[multipleObjectsLength];
							for(int k = 0; k < newObjectIds.length; k++)
								newObjectIds[k] = startNum + k;
							if(!multipleObjects.contains(newObjectIds))
								multipleObjects.add(ArrayUtils.toObject(newObjectIds));
						}
					}
				}
				if(hasDuplicateMarker) {
					for(int objId : duplicateObjects.keySet()) {
						int amount = duplicateObjects.get(objId);
						amount = amount - 1;
						for(int j = 0; j < amount; j++) {
							objectIds = ArrayUtils.depositElement(objectIds, 
								(ArrayUtils.indexOf(objectIds, (objId)) + 1), objId - 100000);
						}
						objectIds = ArrayUtils.depositElements(new int[] { (objId - 100000)  }, 
							objectIds, objId);
					}
				}
				if(hasMultipleObjectsMarker) {
					boolean looped = false;
					int[] newObjectIds = objectIds;
					for(int j = 0; j < multipleObjects.size(); j++) {
						if(!looped) {
							for(int i = 0; i < objectIds.length; i++) {
								if(objectIds[i] == 0) {
									int[] newArray = ArrayUtils.toPrimitive(multipleObjects.get(j++));
									newObjectIds = ArrayUtils.depositElements(newArray, newObjectIds, 0);
								}
								if(i == objectIds.length - 1)
									looped = true;
							}
						}
					}
					objectIds = newObjectIds;
				}
				String[] iRTokens = tokens[5].split(", ");
				Item[] itemsRequired = new Item[iRTokens.length / 2];
				int count = 0;
				for (int i = 0; i < itemsRequired.length; i++)
					itemsRequired[i] = new Item(getItemIdFromName(iRTokens[count++]), Integer.parseInt(iRTokens[count++]));
				RoomObject newInstance = new RoomObject(name, itemId, levelRequired, xp, objectIds, itemsRequired);
				ROOM_OBJECTS.add(newInstance);
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Map<Object, List<RoomObject>> BUILDABLES = new HashMap<Object, List<RoomObject>>();
	
	public static Map<Object, List<RoomObject>> getBuildables() {
		return BUILDABLES;
	}
	
	public static List<RoomObject> getBuildablesFromBuild(GlobalObject object) {
		List<RoomObject> roomObjects = getBuildables().get(object.getId());
		if(roomObjects == null)
			roomObjects = getBuildables().get(object.getName());
		return roomObjects;
	}
	
	public static void loadBuildables() {
		BUILDABLES.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader("data/playerdata/construction/buildObjects.txt"));
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith("#") || line.equals(""))
					continue;
				String[] tokens = line.split(" - ");
				String[] objectTokens = tokens[1].split(", ");
				String objectName = null;
				int objectId = -1;
				try {
					objectId = Integer.parseInt(tokens[0]);
				} catch(NumberFormatException e) {
					objectName = tokens[0];
				}
				List<RoomObject> rObjects = new ArrayList<RoomObject>();
				for(int i = 0; i < objectTokens.length; i++)
					rObjects.add(getRoomObject(objectTokens[i]));
				BUILDABLES.put(objectId == -1 ? objectName : objectId, rObjects);
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getItemIdFromName(String name) {
		for (int i = 0; i < Misc.getItemsSize(); i++)
			ItemDefinition.get(i);
		for(ItemDefinition def : ItemDefinition.itemDefinitions.values())
			if(def != null)
				if(def.getName().toLowerCase().equals(name.toLowerCase()))
					return def.getId();
		try {
			return Integer.parseInt(name);
		} catch(NumberFormatException e) { // Just so no errors get thrown
			return -1;
		}
	}
	
}
