package org.nova.game.player.dialogues;


// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class LanderD extends MatrixDialogue {

	public LanderD() {
	}

	public void start() {
		sendDialogue(SEND_2_LARGE_OPTIONS, "Are you sure you would like to leave the lander?",
				"Yes, get me out of here!", "No, I want to stay.");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			switch (componentId) {
			case 1:
				player.interfaces().closeChatBoxInterface();
				player.getControllerManager().forceStop();
				break;
			case 2:
				player.interfaces().closeChatBoxInterface();
				break;
			}
		}
		}

	public void finish() {
	}

	private int npcId;
}
