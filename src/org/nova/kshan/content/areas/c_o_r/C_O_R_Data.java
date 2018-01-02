package org.nova.kshan.content.areas.c_o_r;

import java.util.ArrayList;
import java.util.List;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.kshan.content.areas.Area;

/**
 * Contains data for the Cavern of Remembrance.
 * 
 * @author K-Shan
 *
 */
public final class C_O_R_Data {
	
	/**
	 * The tile the player will land on when the player teleports to the Cavern.
	 */
	public static final Location TELEPORT_TILE = new Location(110, 192, 0);
	
	/**
	 * Represents the list of boss objects.
	 */
	private static final List<GlobalObject> BOSS_OBJECTS = new ArrayList<GlobalObject>();
	
	/**
	 * Returns all of the boss global objects in the area.
	 * @return
	 */
	public static List<GlobalObject> getBossObjects() {
		Area area = Area.get("Cavern of Remembrance");
		for(GlobalObject objectsInArea : Game.getLocalObjects(area.getCenter(), 200)) {
			if(objectsInArea.defs().getWidth() < 128 || objectsInArea.defs().getHeight() < 128) {
				if(!BOSS_OBJECTS.contains(objectsInArea)) {
					BOSS_OBJECTS.add(objectsInArea);
				}
			}
		}
		return BOSS_OBJECTS;
	}
	
	/**
	 * Sends an interface if the player clicks the Cavern of Remembrance
	 * option in the book tab interface.
	 * @param player
	 */
	public static void handleBookTabAction(final Player player) {
		player.getDialogue().open(new C_O_R_BookTabDialogue());
	}

	/**
	 * Returns the amount of kills per boss needed to unlock 
	 * its barrier at the Cavern of Remembrance.
	 * @param boss
	 * @return
	 */
	public static int getKillsNeeded(String boss) {
		switch(boss) {
			case "Corporeal Beast":
				return 15;
			case "Nex":
				return 10;
			case "General Graardor":
			case "Commander Zilyana":
			case "K'ril Tsutsaroth":
			case "Kree'arra":
			case "Nomad":
			case "Tormented demon":
				return 25;
			case "TzTok-Jad":
			case "King Black Dragon":
				return 30;
			case "Avatar of Creation":
			case "Avatar of Destruction":
				return 35;
		}
		return -1;
	}

}
