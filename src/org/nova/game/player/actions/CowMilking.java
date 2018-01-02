package org.nova.game.player.actions;

import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

public class CowMilking extends Action {

	@Override
	public boolean start(Player player) {
		if (!player.getInventory().containsItem(1924,1)) {
			player.sendMessage("You must have a bucket to take milk from the dairy cow.");
		return false;
		}
		
		return true;
	}

	@Override
	public boolean process(Player player) {
if (!player.getInventory().containsItem(1924,1)) 
	return false;
	
	
		return false;
	}

	@Override
	public int processWithDelay(Player player) {
		if (player.getInventory().containsItem(1924, 1)) {
			player.setNextAnimation(new Animation(2305));
			player.sendMessage("You fill the bucket with some milk.");
			player.getInventory().deleteItem(1924,1);
			player.getInventory().addItem(1927,1);
		}
		return 1;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
		
	}

}
