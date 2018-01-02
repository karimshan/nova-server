package org.nova.kshan.dialogues.impl;

import org.nova.game.map.Location;
import org.nova.game.player.content.minigames.ZarosGodwars;
import org.nova.kshan.dialogues.Dialogue;

public final class NexEntrance extends Dialogue {

	@Override
	public void start() {
		sendLines("The room beyond this point is a prison!",
			"There is no way out other than death or teleport.",
				"Only those who endure dangerous encounters should proceed.");
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		if (stage == 0) {
			stage = 1;
			sendOptions("There are currently "+
				ZarosGodwars.getPlayersCount()+" people fighting.<br>Do you wish to join them?",
					"Climb down.", "Stay here.");
		} else if (stage == 1) {
			if (buttonId == 1) {
				player.setLocation(new Location(2911, 5204, 0));
				player.getControllerManager().startController("ZGDControler");
			}
			end();
		}

	}

	@Override
	public void finish() {
		
	}

}
