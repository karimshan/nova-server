package org.nova.game.player.content.cities;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */

public class GraveStone {

	private Player player;
	private Location tile;
	private int count;

	public GraveStone(Player player, Location tile, int count) {
		player(player).tile(tile).count(count).start().process();
	}

	public GraveStone start() {
		player.sendMessage("You have 3 minutes to pickup your loot.");
		return this;
	}

	public boolean process() {
		count(count() - 1);
		int minutes = (int) count() / 60;
		int seconds = (int) count() - (minutes * 60);
		String time = (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);

		if (count() == 0) {
			return finish();
		} else {

			if (count() == 60)
				player.sendMessage("You have 1 minute left to pickup your loot, so hurry up!");

			if (player.interfaces().containsInterface(548)) {
				player.packets().sendHideIComponent(548, 21, false);
				player.packets().sendHideIComponent(548, 22, false);
				player.packets().sendIComponentText(548, 22, "" + time);
			} else {
				player.packets().sendHideIComponent(746, 187, false);
				player.packets().sendHideIComponent(746, 188, false);
				player.packets().sendIComponentText(746, 188, "" + time);
			}

		}
		return true;
	}

	public boolean finish() {
		GlobalObject object = Game.getObject(tile(), 10);
		if (object != null) {
			Game.destroySpawnedObject(object, true);
		}
		player().hints().removeUnsavedHintIcon();
		player.packets().sendHideIComponent(548, 21, true);
		player.packets().sendHideIComponent(548, 22, true);
		player.packets().sendHideIComponent(746, 187, true);
		player.packets().sendHideIComponent(746, 188, true);
		player.sendMessage("Your gravestone has collapsed.");
		return false;
	}

	public Player player() {
		return player;
	}

	public GraveStone player(Player player) {
		this.player = player;
		return this;
	}

	public Location tile() {
		return tile;
	}

	public GraveStone tile(Location tile) {
		this.tile = tile;
		return this;
	}

	public int count() {
		return count;
	}

	public GraveStone count(int count) {
		this.count = count;
		return this;
	}

}
