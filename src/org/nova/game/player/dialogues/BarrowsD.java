package org.nova.game.player.dialogues;

import org.nova.game.map.Location;

public class BarrowsD extends MatrixDialogue {

	@Override
	public void start() {
		sendDialogue(SEND_1_TEXT_CHAT, "",
				"You've found a hidden tunnel, do you want to enter?");
	}

	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue(SEND_2_LARGE_OPTIONS, SEND_DEFAULT_OPTIONS_TITLE,
					"Yes, I'm fearless.", "No way, that looks scary!");
		} else if (stage == 0) {
			if (componentId == 2) {
				player.setLocation(new Location(3551, 9692, 0));
				player.getTemporaryAttributtes().put("lootedChest",
						Boolean.FALSE);
			}
			end();
		}
	}

	@Override
	public void finish() {

	}

}
