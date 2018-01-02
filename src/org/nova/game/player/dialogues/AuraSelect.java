package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class AuraSelect extends MatrixDialogue {

	public AuraSelect() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Diango's Auras",
				"Sharpshooter", "Lumberjack", "Quarrymaster",
				"Five-finger discount", "Reverence");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
	
		}


	public void finish() {
	}

	private int npcId;
}
