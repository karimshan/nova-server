package org.nova.game.player.dialogues;


// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class ResourcesManagerD extends MatrixDialogue {

	public ResourcesManagerD() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Resources Management",
				"Start a new resource", "I want to loot current resource incomes.", "Nevermind.",
				"", "");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {

		}

	private Object WildernessHats() {
		// TODO Auto-generated method stub
		return null;
	}

	public void finish() {
	}

	private int npcId;
}
