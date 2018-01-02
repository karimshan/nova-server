package org.nova.game.player.content.itemactions;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.player.Player;

/**
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 * Dwarven Rock Cakes
 */
public class DwarvenRockCake {

	private Player player;
	
	private boolean eating;
	
	public DwarvenRockCake(Player player) {
		this.player = player;
	}
	
	public void eat() {
		if (player.getHitpoints() <= 30) {
			player.sendMessage("You don't think it wise to try that again soon!");
			return;
		}
		if (eating) {
			player.sendMessage("You are eating already a cake.");
			return;
		}
		eating = true;
		WorldTasksManager.schedule(new WorldTask() {
			int count = 0;
			@Override
			public void run() {
			switch (count) {
			case 0:
				if (player.getHitpoints() <= 30) //I've added another check because if player is in combat it would be #annihilation.
					return;
				player.setNextAnimation(new Animation(829));			
				player.applyHit(new Hit(player, 10, HitLook.REGULAR_DAMAGE));
				player.setNextForceTalk(new ForceTalk("Ow! I nearly broke a tooth!"));
				eating = false;
				break;
			}
			stop();
			}

		}, 0, 0);
	}
	
	
	public boolean eatCake(Player player, int itemId){
	switch (itemId)  {
	case 7509:
	case 7510:
	case 2379:
		eat();
		return true;
	}
		return false;
}
	
}
