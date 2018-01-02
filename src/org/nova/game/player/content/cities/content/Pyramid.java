package org.nova.game.player.content.cities.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 * Al-Kharid pyramid stuff is here.
 *
 */
public class Pyramid {
	
	private boolean insidePyramid;

	public static void setInPyramid(Player player) {
	/*
	 * Actual teleport action over here.
	 */
	}
	public static void constructEffects(Player player) {
		player.interfaces().sendOverlay(118, false);
		player.setTeleport(true);
	}
		
	
	public static void processTunnelClimbAction(final Player player) {
		final int loop = 0;
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
			switch (loop) {
			case 1:
				player.sm("You start looking on the tunnel carefully...");
				break;
			case 4:
				break;
			case 7:
				player.unlock();
				break;
			}
			}
		}, 1);
	}
	public boolean isInsidePyramid() {
		return insidePyramid;
	}
	public void setInsidePyramid(boolean insidePyramid) {
		this.insidePyramid = insidePyramid;
	}
}

