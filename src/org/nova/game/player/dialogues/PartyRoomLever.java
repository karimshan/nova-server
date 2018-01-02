package org.nova.game.player.dialogues;

import org.nova.game.player.content.PartyRoom;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class PartyRoomLever extends MatrixDialogue {

	public PartyRoomLever() {
	}

	public void start() {
		sendDialogue(SEND_3_OPTIONS, "Select a Option",
				 "Balloon Bonanza (1000 coins).", "Nightly Dance (500 coins).", "No action.");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {
				 PartyRoom.purchase(player, true);
			}
			else if (componentId == 2) {  
				PartyRoom.purchase(player, false);
			}
			else if (componentId == 3) {
			end();
			
			}
			}
		}

	public void finish() {
	}

	private int npcId;
}
