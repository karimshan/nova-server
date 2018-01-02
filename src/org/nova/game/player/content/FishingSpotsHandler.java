package org.nova.game.player.content;

import java.util.HashMap;

import org.nova.game.map.Location;
import org.nova.game.npc.NPC;


public class FishingSpotsHandler {

	public static final HashMap<Location, Location> moveSpots = new HashMap<Location, Location>();

	public static void init() {
		moveSpots.clear();
		moveSpots.put(new Location(2836, 3431, 0),
				new Location(2845, 3429, 0));
		moveSpots.put(new Location(2853, 3423, 0),
				new Location(2860, 3426, 0));
		moveSpots.put(new Location(3110, 3432, 0),
				new Location(3104, 3423, 0));
		moveSpots.put(new Location(3104, 3424, 0),
				new Location(3110, 3433, 0));
	}

	public static boolean moveSpot(NPC npc) {
		Location key = new Location(npc);
		Location spot = moveSpots.get(key);
		if (spot == null && moveSpots.containsValue(key)) {
			for (Location k : moveSpots.keySet()) {
				Location v = moveSpots.get(k);
				if (v.getX() == key.getY() && v.getY() == key.getX()
						&& v.getZ() == key.getZ()) {
					spot = k;
					break;
				}
			}
		}
		if (spot == null)
			return false;
		npc.setLocation(spot);
		return true;
	}

}
