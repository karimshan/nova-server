package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.Magic;

/**
*
*@author Fuzen Seth
*@since 29.9.2013
*@information Represents the teleportation crystal.
*
*/
public class TeleportCrystalD extends MatrixDialogue {

	@Override
	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Teleportation Crystal",
				"Save my current location to the crystal.", "Teleport to the last saved location.", "Teleport to the Grand Exchange.", "Teleport to Miscellania.",
				"Nowhere.");
	}

	public void run(int interfaceId, int componentId) {
		if (componentId == 1) {
			player.saveLocation();
			//	ItemEffects.savecurrentLocation(player);
		} else if (componentId == 2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Location(
					player.getCrystalX(), player.getCrystalY(), player.getCrystalPlane()));
			
		} else if (componentId == 3) {
			Magic.sendNormalTeleportSpell(player, 0, 0,
						new Location(3164, 3483, 0));
			player.interfaces().closeChatBoxInterface();
		} else if (componentId == 4) {
		Magic.sendNormalTeleportSpell(player, 0, 0,
				new Location(2537, 3865, 0));
		player.interfaces().closeChatBoxInterface();
		} else if (componentId == 5) {
		player.interfaces().closeChatBoxInterface();
		} else {
			end();
	}
	}

	@Override
	public void finish() {

	}
}
