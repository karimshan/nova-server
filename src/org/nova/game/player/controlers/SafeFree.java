package org.nova.game.player.controlers;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

public class SafeFree extends Controller {


	@Override
	public void start() {
		player.setCanPvp(true);
		sendInterfaces();
	}
	
	@Override
	public void sendInterfaces() {
		player.interfaces().sendTab(player.interfaces().isFullScreen() ? 10 : 19, 789);
	}

	@Override
	public boolean login() {
		removeControler();
		return false;
	}

	@Override
	public boolean sendDeath() {
		player.addStopDelay(7);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.packets().sendMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					player.getEquipment().init();
					player.getInventory().init();
					player.reset();
					player.setCanPvp(false);
					teleportPlayer(player);
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.packets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean logout() {
		player.setCoords(new Location(3000, 9676, 0));
		return true;
	}

	@Override
	public void forceClose() {
		player.setCanPvp(false);
	}

	@Override
	public void moved() {
			if (!isInClanSafe(player)) {
				player.setCanPvp(false);
			} else 
				player.setCanPvp(true);
	}
	
	private void teleportPlayer(Player player) {
			player.setLocation(new Location(3000, 9676, 0));
	}

	public static boolean isInClanSafe(Player player) {
		return player.getX() >= 2756 && player.getY() >= 5512
				&& player.getX() <= 2878 && player.getY() <= 5630;
	}
}