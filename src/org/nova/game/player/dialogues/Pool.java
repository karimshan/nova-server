package org.nova.game.player.dialogues;

import org.nova.game.map.Location;

public class Pool extends MatrixDialogue {

	@Override
	public void start() {
		sendDialogue(SEND_2_OPTIONS, "Would you like to step inside the pool?",
				"Yes, please, I'd love to go.", "No thanks"); //Change options maybe?
	}

	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_2_OPTIONS && componentId == 1) {
			player.setLocation(new Location(2509, 4689, 0));
				sendDialogue(SEND_2_TEXT_CHAT, "",
						"*You Step into the pool...", //You can change txt
						"You feel powerful, as you teleports you!");
			}
		}

	@Override
	public void finish() {

	}

}