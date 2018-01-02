package org.nova.kshan.content.skills.construction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author K-Shan
 *
 */
public class RoomList {
	
	/**
	 * Represents the list of rooms.
	 */
	private static final List<HouseRoom> roomList = new ArrayList<HouseRoom>();
	
	/**
	 * Loads the list of rooms.
	 */
	public static final void loadRooms() {
		roomList.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader("data/playerdata/construction/roomList.txt"));
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith("//") || line.equals(""))
					continue;
				String[] tokens = line.split(" : ");
				String roomName = tokens[0];
				String[] roomTokens = tokens[1].split(", ");
				int chunkX = Integer.parseInt(roomTokens[0]);
				int chunkY = Integer.parseInt(roomTokens[1]);
				int levelRequired = Integer.parseInt(roomTokens[2]);
				int cost = Integer.parseInt(roomTokens[3]);
				HouseRoom room = new HouseRoom(roomName, chunkX, chunkY, levelRequired, cost);
				roomList.add(room);
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the room from the file.
	 * @param name
	 * @return
	 */
	public static HouseRoom getRoom(String name) {
		HouseRoom room = null;
		for(HouseRoom r : getRooms())
			if(r.getName().toLowerCase().equals(name.toLowerCase()))
				room = r;
		return room;
	}
	
	/**
	 * Returns the list of rooms from the file.
	 * @return
	 */
	public static List<HouseRoom> getRooms() {
		return roomList;
	}
	

}
