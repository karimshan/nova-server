package org.nova.game.player.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
1648 - cranking wheel ectophuntus
1649 - putting bones into machine
1651 - ectophuntus pray emote
1652 - shake vial
 */
public class Ectophial {

	Player player;
	
	public Ectophial(Player player) {
		this.player = player;
	}

	private int FULL_ECTOPHIAL = 4251, EMPTY_ECTOPHIAL = 4252;
	/**
	 * Teleport Landing Coords
	 */
	public final int LANDING_X = 3659;
	public final int LANDING_Y = 3517;

	/**
	 *  This is for Worship, this will refill ectohpial
	 * @param player
	 */
	public void refillEctophial(Player player) {
	if (player.getInventory().containsItem(EMPTY_ECTOPHIAL, 1)) {
		player.addStopDelay(2);
		player.getInventory().deleteItem(EMPTY_ECTOPHIAL,1);
		player.sendMessage("You refill the ectophial.");
		player.setNextAnimation(new Animation (1649));
		player.getInventory().addItem(FULL_ECTOPHIAL,1);
	} else {
		player.sendMessage("You cannot worship ectofuntus.");
	}
}
	
	public void ProcessTeleportation(final Player player) {
			WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				player.interfaces().closeChatBoxInterface();			
				if (loop == 0) {
                    player.setNextAnimation(new Animation(9609));
				player.setNextGraphics(new Graphics(1688));
				} else if (loop == 2) {
					player.getInventory().deleteItem(FULL_ECTOPHIAL,1);
                    player.setNextAnimation(new Animation(8939));
				} else if (loop == 4) {
					player.getInventory().addItem(EMPTY_ECTOPHIAL,1);
					player.setLocation(new Location (LANDING_X, LANDING_Y, 0));
				player.setNextAnimation(new Animation(8941));
				player.closeInterfaces();
				stop();
				}
				loop++;
				}
			}, 0, 1);
		}
	
}
