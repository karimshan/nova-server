package org.nova.game.player.content.cities;

import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
/**
 * 
 * @author Fuzen Seth
 *
 */
public class PortPhasmatys {

	private static final int ECTO_TOKENS = 4278;
	
	public static void handlePort(Player player, GlobalObject object) {
	if (!player.getInventory().containsItem(ECTO_TOKENS, 2)) {
	player.sendMessage("You need 2 ecto-tokens to pass the gate.");
		return;	
	}
	if (currentPosition(player, 3658,3508,0) || 
			currentPosition(player,3659,3509,0) ||
			currentPosition(player,3660,3509,0)) {
		player.getInventory().deleteItem(ECTO_TOKENS, 2);
		player.addStopDelay(4);
		player.addWalkSteps(3659, 3506, -1, false);
		//player.addWalkSteps(3659, 3506, 5);
	 if (currentPosition(player, -1,-1,1)) {
			
		}
		player.sendMessage("You succesfully pass the barrier.");
	}
}
	public static boolean currentPosition(Player player, int x, int y, int z) {
		return player.getLocation().equals(new Location(x,y,z));
	}
	public static boolean processObjects(Player player, GlobalObject object) {
		switch (object.getId()) {
		case 5259:
			handlePort(player, object);
			return true;
		}
		return false;
	}
}
