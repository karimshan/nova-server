package org.nova.game.player.content.minigames.clanwars;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.game.player.controlers.Controller;
import org.nova.game.player.controlers.Wilderness;

/**
 * Handles the FFA Clan Wars zone.
 * @author Emperor
 *
 */
public final class FfaZone extends Controller {

	/**
	 * If the FFA zone is the risk zone.
	 */
	private boolean risk;
	
	/**
	 * If the player was in the ffa pvp area.
	 */
	private transient boolean wasInArea;
	
	@Override
	public void start() {
		if (getArguments() == null || getArguments().length < 1) {
			this.risk = player.getX() >= 2948 && player.getY() >= 5508 && player.getX() <= 3071 && player.getY() <= 5631;
		} else {
			this.risk = (Boolean) getArguments()[0];
		}
		player.interfaces().sendTab(player.interfaces().isFullScreen() ? 10 : 19, 789);
		if (inSafe(player))
			player.interfaces().sendTab(player.interfaces().isFullScreen() ? 10 : 19, 790);
		moved();
	}
	
	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.packets().sendMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					if (risk) {
						Player killer = player.getMostDamageReceivedSourcePlayer();
						if (killer != null) {
							killer.removeDamage(player);
							if (!inSafe(player))
							player.sendItemsOnDeath(killer);
						}
						player.getEquipment().init();
						player.getInventory().init();
					}
					player.setLocation(new Location(2993, 9679, 0));
					player.getControllerManager().startController("clan_wars_request");
					player.reset();
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
	public void magicTeleported(int type) {
		player.getControllerManager().forceStop();
	}
	
	@Override
	public boolean processObjectClick1(GlobalObject object) {
		switch (object.getId()) {
		case 38700:
			player.setLocation(new Location(2993, 9679, 0));
			player.getControllerManager().startController("clan_wars_request");
			return false;
		}
		return true;
	}
	
	@Override
	public void moved() {
		boolean inArea = inPvpArea(player);
		if (inArea && !wasInArea) {
			player.packets().sendPlayerOption("Attack", 1, true);
			player.setCanPvp(true);
			wasInArea = true;
			if (risk) {
				player.setWildernessSkull();
			}
			Wilderness.checkBoosts(player);
		} else if (!inArea && wasInArea) {
			player.packets().sendPlayerOption("null", 1, true);
			player.setCanPvp(false);
			wasInArea = false;
		}
	}
	
	@Override
	public boolean keepCombating(Entity victim) {
		if (!(victim instanceof Player)) {
			return true;
		}
		return player.isCanPvp() && ((Player) victim).isCanPvp();
	}
	
	@Override
	public void forceClose() {
		player.setCanPvp(false);
		player.packets().sendPlayerOption("null", 1, true);
		boolean resized = player.interfaces().isFullScreen();
		player.packets().closeInterface(resized ? 746 : 548, resized ? 11 : 27);
		
	}
	
	@Override
	public boolean logout() {
		return false;
	}
	
	/**
	 * Checks if the location is in a ffa pvp zone.
	 * @param t The world tile.
	 * @return {@code True} if so.
	 */
	public static boolean inPvpArea(Location t) {
		return (t.getX() >= 2948 && t.getY() >= 5512 && t.getX() <= 3071 && t.getY() <= 5631)		//Risk area.
				|| (t.getX() >= 2756 && t.getY() >= 5512 && t.getX() <= 2879 && t.getY() <= 5631);	//Safe area.
	}
	public static boolean inSafe(Location t) {
		return (t.getX() >= 2756 && t.getY() >= 5512 && t.getX() <= 2879 && t.getY() <= 5631);
	}
	
	/**
	 * Checks if the location is in a ffa zone.
	 * @param t The world tile.
	 * @return {@code True} if so.
	 */
	public static boolean inArea(Location t) {
		return (t.getX() >= 2948 && t.getY() >= 5508 && t.getX() <= 3071 && t.getY() <= 5631)		//Risk area.
				|| (t.getX() >= 2756 && t.getY() >= 5508 && t.getX() <= 2879 && t.getY() <= 5631);	//Safe area.
	}

	/**
	 * Checks if a player's overload effect is changed (due to being in the risk ffa zone, in pvp)
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	public static boolean isOverloadChanged(Player player) {
		if (!(player.getControllerManager().getControler() instanceof FfaZone)) {
			return false;
		}
		return player.isCanPvp() && ((FfaZone) player.getControllerManager().getControler()).risk;
	}
}