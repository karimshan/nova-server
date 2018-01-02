package org.nova.game.player.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class Shortcuts {

	
	public static final boolean handleGameObject(Player player, GlobalObject gameObject) {
		switch (gameObject.getId()) {
		case 9311:
			
			return true;
		case 9312:
			passGrandExchangeShortcut(player, 3138, 3516,0);
			return true;
		}
		return false;
	}
	
	private static void passGrandExchangeShortcut(final Player player, final int x, final int y, final int z) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {	
				player.addStopDelay(3);
				boolean active = false;
				if (active) {
					stop();
				return;
				}
				active = true;
				switch (loop) {
				case 0:
					break;
				case 1:
					player.setNextAnimation(new Animation(2589));
					if (player.getX() == 3143 && player.getY() == 3513 && player.getZ() == 0)
					player.setLocation(new Location(3143, 3514,0));
					break;
				case 3:
					player.setNextAnimation(new Animation(2590));
					break;
					case 4:
						player.setLocation(new Location(x,y,z));
						break;
				case 5:
					player.setLocation(new Location(x,y,z));
					break;
				case 7:
					player.setNextAnimation(new Animation(2591));
					active = false;	
					stop();
					break;
				}
				loop++;
				}
			}, 0, 1);
	}
}
