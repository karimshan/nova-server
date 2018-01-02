package org.nova.game.player.content.cities.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

public class Crates {
	
	public Crates(Player player) {
		this.player = player;
	}
	
	private Player player;
	
	public void findCrateLoot() {
		
		player.addStopDelay(4);
		player.closeInterfaces();
	int randomItem = Misc.getRandom(8);
	switch (randomItem) {
	case 0:
		player.sendMessage("You have found nothing interesting.");
		break;
	case 1:
		player.getInventory().addItem(1277,1);
		player.sendMessage("You have found a wooden sword!");
		break;
	case 2: 
		player.getInventory().addItem(1173, 17);
		player.sendMessage("You have found a wooden shield!");
		break;
	case 3: 
		player.getInventory().addItem(995, 27);
		player.sendMessage("You have found some coins.");
		break;
	case 4: 
		player.sendMessage("You have found nothing interesting.");
		break;
	case 5: 
		player.getInventory().addItem(995, 12);
		player.sendMessage("You have found some coins.");
		break;
	case 6: 
		player.getInventory().addItem(995, 17);
		player.sendMessage("You have found some coins.");
		break;
	case 7: 
		player.getInventory().addItem(995, 17);
		player.sendMessage("You have found some coins.");
		break;
	case 8: 
		player.sendMessage("You have found nothing interesting.");
		break;
	}
}
	
	public void searchCrates(final Player player) {
	
		WorldTasksManager.schedule(new WorldTask() {
			boolean run;
			int loop;
			@Override
			public void run() {
			if (run == true)
				stop();
			if (loop == 2)
				run = true;
			player.sendMessage("You search the crate...");
			if (loop == 2) {
				findCrateLoot();
				run = false;
			}
			loop++;
			}
			}, 0, 1);
		}
	
}
