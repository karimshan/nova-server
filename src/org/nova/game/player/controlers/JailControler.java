package org.nova.game.player.controlers;

import org.nova.Constants;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

public class JailControler extends Controller {

	@Override
	public void start() {
		if (player.getJailed() > Misc.currentTimeMillis())
			player.sendRandomJail(player);
	}

	@Override
	public void process() {
		if (player.getJailed() <= Misc.currentTimeMillis()) {
			player.getControllerManager().getControler().removeControler();
			player.packets().sendMessage(
					"Your account has been unmuted.", true);
			player.setLocation(Constants.RESPAWN_PLAYER_LOCATION);
		}
	}

	public static void stopControler(Player p) {
		p.getControllerManager().getControler().removeControler();
	}

	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.stopAll();
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.packets().sendMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(-1));
					player.reset();
					player.setCanPvp(false);
					player.sendRandomJail(player);
					player.resetStopDelay();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean login() {

		return false;
	}

	@Override
	public boolean logout() {

		return false;
	}

	@Override
	public boolean processMagicTeleport(Location toTile) {
		player.packets().sendMessage(
				"You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processItemTeleport(Location toTile) {
		player.packets().sendMessage(
				"You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processObjectClick1(GlobalObject object) {
		player.packets().sendMessage(
				"You cannot do any activities while being jailed.");
		return false;
	}

}
