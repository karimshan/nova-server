package org.nova.game.player.content.itemactions;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information Handles the teleportation tabs.
 */
public class TeleportTabs {
	
	/**
	 * @author Fuzen Seth
	 */
	public enum TeleportTablets {
		FALADOR(8009, new Location(2965,3379,0)),
		VARROCK(8007, new Location(3212,3424,0)),
		LUMBRIDGE(8008, new Location(3222,3218,0));
		
		private int itemId;
		private Location destination;
		
		private TeleportTablets(int itemId, Location destination)  {
			this.itemId = itemId;
			this.destination = destination;
		}

		public int getItemId() {
			return itemId;
		}
		public Location getDestination() {
			return destination;
		}

	}
	
	/**
	 * Uses a teleportation tab.
	 * @param player
	 * @param destination
	 * TODO alot shit here
	 */
	public static final void useTab(final Player player) {
		for (final TeleportTablets tabs : TeleportTablets.values()) {
			final Location destination = new Location(tabs.getDestination());
			
			player.getInventory().deleteItem(tabs.getItemId(), 1);
			if (!player.getControllerManager().processItemTeleport(destination))
			    return;
			if (player.isTeleport()) {
			player.sendMessage("You cannot teleport anywhere.");
				return;
			}
			player.lock();
			player.setNextAnimation(new Animation(9597));
			player.setNextGraphics(new Graphics(1680));
			WorldTasksManager.schedule(new WorldTask() {
			    int stage;

			    @Override
			    public void run() {
				if (stage == 0) {
				    player.setNextAnimation(new Animation(4731));
				    stage = 1;
				}
				else if (stage == 2) {
					player.setNextAnimation(new Animation(9598));
				}
				else if (stage == 3) {
				    Location teledestination = destination;
				    // attemps to randomize destination by 4x4 area
				    for (int trycount = 0; trycount < 10; trycount++) {
					teledestination = new Location(destination, 2);
					teledestination = destination;
				    }
				    player.setLocation(tabs.getDestination());
				    player.faceLocation(new Location(teledestination.getX(), teledestination.getY() - 1, teledestination.getZ()));
				    player.setDirection(6);
				    player.setNextAnimation(new Animation(-1));
				    stage = 2;
				} else if (stage == 4) {
				    player.resetReceivedDamage();
				    player.unlock();
				    stop();
				}

			    }
			}, 2, 1);
		}
	}
}
