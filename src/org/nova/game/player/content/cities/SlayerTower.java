package org.nova.game.player.content.cities;

import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
/**
 * 
 * @author Fuzen Seth
 *
 */
public class SlayerTower {

	public static final int FIRST_FLOOR = 1;
	public static final int SECOND_FLOOR = 2;
	public static final int THIRD_FLOOR = 3;
	static Location FLOOR_ONE = new Location(1,1,1);
	static Location FLOOR_TWO = new Location(1,1,1);
	static Location FLOOR_THREE = new Location(1,1,1);

	public static boolean processObjectActions(Player player, GlobalObject object) {
		switch (object.defs().name.toString()) {
		case "Spikey chain":
			if (player.isCompletedHoarfrost())  {
				player.sendMessage("You must have completed battle quest Hoarfrost Hollow in order of climbing up.");
			return false;
			}
			if (player.getZ() == 0 || player.getZ() == 1 || player.getZ() == 2)
			player.setLocation(new Location(player.getX(),player.getY(), player.getZ() + 1));
			return true;
		}
		switch (object.getId()) {
		case FIRST_FLOOR:
		if (player.isCompletedHoarfrost())  {
			player.sendMessage("You must have completed battle quest Hoarfrost Hollow in order of climbing up.");
		return false;
		}
		player.setLocation(FLOOR_ONE);
			return true;
		case SECOND_FLOOR:
			player.setLocation(FLOOR_TWO);
			return true;
		case THIRD_FLOOR:
			player.setLocation(FLOOR_THREE);
			return true;
		}
		return false;
	}
}
