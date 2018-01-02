package org.nova.game.player.dialogues;


// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class ClanWarsViewing extends MatrixDialogue {

	public ClanWarsViewing() {
	}

	public void start() {
		sendDialogue(SEND_3_OPTIONS, "Select an option",
				"I want to watch a friend's clan war.", "Show me a battle - any battle.", "Oh, forget it.");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		end();
		player.packets().sendMessage("There are no clan wars going on currently.");
		
		}

	public void finish() {
	
	}
}
