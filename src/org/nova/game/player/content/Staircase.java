package org.nova.game.player.content;

import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Player;


public class Staircase {
	
	public static enum Staircases {
		VARROCK_STAIRCASE(24350, new Location(3203, 3496, 1)),
		LUMBRIDGE_STAIRCASE(36773, new Location(3205, 3209, 1)),
		SLAYER_TOWER_SPIKEY_CHAIN(9319, new Location(3422, 3549,1)),
		SLAYER_TOWER_STAIRCASE(4495, new Location(3417, 3541, 2));
		
		private int objectId;
		private Location nextTile;
		
		private Staircases(int objectId, Location nextTile) {
			this.objectId = objectId;
			this.nextTile = nextTile;
		}

		public int getObjectId() {
			return objectId;
		}

		public Location getNextTile() {
			return nextTile;
		}
	}

	public static final boolean handleGameObject(Player player, GlobalObject object) { 
	for (Staircases stairs : Staircases.values()) {
		if (object.getId() == stairs.getObjectId()) {
			player.setLocation(stairs.getNextTile());
			return false;
		}	
		if (object.defs().name.toLowerCase().contains("spikey chain"))
				player.sendMessage("You climb up the "+object.defs().name.toLowerCase()+".");
		return false;
		}
	return false;
	}
	
}
