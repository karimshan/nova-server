package org.nova.game.player.content.cities;

import org.nova.game.map.Location;
import org.nova.game.player.Player;

/**
 * @author Fuzen Seth
 */
public class Karamja {

	public static boolean handleObjects(Player player, int id) {
		switch (id) {
		case 492: //Entering to dungeon.
			player.setLocation(new Location(2856,9570,0));
			return true;
		case 1764: //Leaving the dungeon.
			player.setLocation(new Location(2856,3167,0));
			return true;
		case 31284: //Tzhaar entrance.
			player.setLocation(new Location(2480,5175,0));
			return true;
		case 9359: //Tzhaar entrance.
			player.setLocation(new Location(2866,9571,0));
			return true;
		case 25161:
			player.sendMessage("You cannot enter here currently.");
			return true;
		case 25213:
			player.sendMessage("I'm too scared to climb it, I don't know what would wait for me!");
			return true;
		case 2606: //Wall to go Lesser demons.
			if (!player.isCompletedHoarfrost()) {
				player.sendMessage("You must have completed Hoarfrost Hollow -battle quest to enter here.");
				return false;
			}
			if (player.getLocation().equals(new Location(2836,9599,0))) {
				player.setLocation(new Location(2836,9600,0));
				player.sendMessage("You succesfully pass the obstacle.");
			}
			if (player.getLocation().equals(new Location(2836,9600,0))) {
				player.sendMessage("You succesfully pass the obstacle.");
				player.setLocation(new Location(2836,9599,0));
			}
			return true;
		}
		
		return false;
 	}
}
