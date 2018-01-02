package org.nova.game.player.content.cities.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class PhasmatysHopper {

	private static final int BUCKET_OF_SLIME = 4286;
	
	private static final int BONES = 562;
	
	/**
	 * Runs hopper walking and emotes.
	 */
	public static void startHopperScript(Player player, GlobalObject object) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
		
				switch (loop)  {
				case 0:
					break;
				case 1:
					break;
				}
				loop++;
				}
			}, 0, 1);
		}
	
	public static void getCurrentStatus(Player player) {
	if (!player.isStoredBones()) {
		player.sendMessage("There is no bones stored in to the hopper.");
	} else {
		player.sendMessage("You have stored the bones in hopper, next you have to grind them.");
	}
	}
	
	public static void setBones(Player player) {
		player.getInventory().deleteItem(BONES, 1);
		player.setStoredBones(true);
		player.sendMessage("You put some bones in the grinder's hopper.");
	}
	
	public static void handleBin(Player player) {
	if (!player.isStoredBones()) {
		player.sendMessage("You haven't grinded any bones from the hopper.");
		return;
	}
}
	/**
	 * Processes object clicks in hopper.
	 * @param player
	 * @param object
	 * @return
	 */
	public static boolean processHopperClicking(Player player, GlobalObject object) {
	
		switch (object.getId()) {
		case 1:
			if (!player.isStoredBones()) {
				player.sendMessage("You can't grind any bones, the hopper doesn't have any!");
				return false;
			}
			startHopperScript(player,object);
			return true;
		}
		return false;
	}
}
