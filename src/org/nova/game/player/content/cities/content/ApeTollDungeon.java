package org.nova.game.player.content.cities.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;
/**
 * 
 * @author Fuzen Seth
 *
 */
public class ApeTollDungeon {
	/**
	 * Player enters to the dungeon.
	 * @param player
	 */
	public static void enter(Player player) {
	player.setLocation(new Location(2764,9103,0));
	processDungeon(player);
	}
	public static boolean progressObjectInteract(Player player, GlobalObject object) {
		switch (object.getId()) {
		case 4780:
			enter(player);
			return true;
		case 4781:
			player.setLocation(new Location(2763,2702,0));
			player.applyHit(new Hit(player, Misc.getRandom(15), HitLook.REGULAR_DAMAGE));
			player.sendMessage("The sunlight hurts your eyes.");
			break;
		}
		return false;
	}
	/**
	 * Hit's player in the tunnels.
	 * @param player
	 */
	private static void addHitTask(Player player) {
		player.applyHit(new Hit(player, Misc.getRandom(110), HitLook.REGULAR_DAMAGE));
	}
	/**
	 * The invalid positions in the tunnel.
	 * @param player
	 * @return
	 */
	private static final boolean containsInvalidPosition(Player player) {
		return player.getX() == 2746 && player.getY() == 9122 && player.getZ() == 0
		|| player.getX() == 2745 && player.getY() == 9122 && player.getZ() == 0
		|| player.getX() == 2745 && player.getY() == 9120 && player.getZ() == 0
		|| player.getX() == 2717 && player.getY() == 9128 && player.getZ() == 0
		|| player.getX() == 2716 && player.getY() == 9128 && player.getZ() == 0
		|| player.getX() == 2805 && player.getY() == 9142 && player.getZ() == 0
		|| player.getX() == 2805 && player.getY() == 9142 && player.getZ() == 0
				;
	}
	/**
	 * Is the player currently in the dungeon?
	 * @param player
	 * @return
	 */
	@SuppressWarnings("unused")
	private static final boolean isInDungeon(Player player) {
		return false;
	}
	/**
	 * Adds the camera shake. true : false 
	 * @param player
	 * @param action
	 */
	public static void addCameraShake(Player player, boolean action) {
		if (action) 
			player.packets().sendCameraShake(1, 12, 9, 1, 0);
		 else
			player.packets().sendStopCameraShake();
	}
	/**
	 * Progressing the whole dungeon.
	 * @param player
	 */
	public static final void processDungeon(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
	
			@Override
			public void run() {
				switch (loop) {
				case 0:
					break;
				case 23:
					if (player.isTransformed()) 
						return;
//					if (!SpecialAreaHandler.isInApeTollDungeon(player)) {
//						stop(); //We quit the task if player isn't in dungeon.
//						return;
//					}
					if (containsInvalidPosition(player))
						return;
					addCameraShake(player, true);
					break;
				case 25:
					addHitTask(player);
					break;
				case 28:
					addCameraShake(player, false);
					break;
				case 29: 
					loop = 1;
					break;
				}
				loop++;
				}
			}, 0, 1);
	}
	
}
