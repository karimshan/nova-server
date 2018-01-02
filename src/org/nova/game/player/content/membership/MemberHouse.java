package org.nova.game.player.content.membership;

import org.nova.game.player.Player;

/**
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 */
public class MemberHouse {
	
	public MemberHouse(Player player) {
		this.player = player;
	}
	
	private transient Player player;
	
	public void enterHouse() {
		if (player.isDonator()) {
			player.teleportPlayer(2162, 5115, 1);
			player.sendMessage("You have succesfully entered to mysterious house.");
		} else {
			player.sendMessage("You cannot enter to this house, you must be a member.");
		}
	}
	
}
