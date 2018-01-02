package org.nova.game.player.content.cities.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;

/**
 * Remove this shit
 * @author K-Shan
 *
 */
public class ZanarisHouse {

	public ZanarisHouse(Player player) {
		this.player = player;
	}

	private transient Player player;

	/**
	 * Opens the house.
	 * 
	 * @param player
	 */
	public void openHouse() {
		if (!hasDramenStaff())
			return;
		if (player.getLocation().equals(new Location(3201, 3169, 0)))
			fairyTeleport(player, 2452, 4472, 0);
	}

	/**
	 * Does the player have dramen staff?
	 * 
	 * @param player
	 * @return
	 */
	public boolean hasDramenStaff() {
		return (player.getEquipment().getWeaponId() == 772);
	}

	/**
	 * Teleports a player to zanaris, with player whos wearing a dramen staff.
	 * 
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 */
	private static void fairyTeleport(final Player player, final int x,
			final int y, final int z) {
		player.lock(3);
		player.setNextAnimation(new Animation(3254));
		player.setNextGraphics(new Graphics(2670));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextGraphics(new Graphics(2671));
				player.setNextAnimation(new Animation(3255));
				player.setLocation(new Location(x, y, z));
				player.stopAll();
			}

		}, 3);
	}

	public GlobalObject getObject() {
		return player.object;
	}
}
