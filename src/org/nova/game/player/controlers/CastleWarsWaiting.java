package org.nova.game.player.controlers;

import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Equipment;
import org.nova.game.player.content.minigames.CastleWars;

public class CastleWarsWaiting extends Controller {

	private int team;

	@Override
	public void start() {
		team = (int) getArguments()[0];
		sendInterfaces();
	}

	// You can't leave just like that!

	public void leave() {
		player.packets().closeInterface(
				player.interfaces().isFullScreen() ? 10 : 8);
		CastleWars.removeWaitingPlayer(player, team);
	}

	@Override
	public void sendInterfaces() {
		player.interfaces().sendTab(
				player.interfaces().isFullScreen() ? 10 : 8, 57);
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (interfaceId == 387 && componentId == 9) {
			player.packets().sendMessage(
					"You can't remove your team's colours.");
			return false;
		}
		return true;
	}

	public boolean canEquip(int slotId, int itemId) {
		if (slotId == Equipment.SLOT_CAPE || slotId == Equipment.SLOT_HAT) {
			player.packets().sendMessage(
					"You can't remove your team's colours.");
			return false;
		}
		return true;
	}

	@Override
	public boolean sendDeath() {
		removeControler();
		leave();
		return true;
	}

	@Override
	public boolean logout() {
		player.setCoords(new Location(CastleWars.LOBBY, 2));
		return true;
	}

	@Override
	public boolean processMagicTeleport(Location toTile) {
		player.getMatrixDialogues().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processItemTeleport(Location toTile) {
		player.getMatrixDialogues().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectTeleport(Location toTile) {
		player.getMatrixDialogues().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectClick1(GlobalObject object) {
		int id = object.getId();
		if (id == 4389 || id == 4390) {
			removeControler();
			leave();
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		removeControler();
		leave();
	}

	@Override
	public void forceClose() {
		leave();
	}
}
