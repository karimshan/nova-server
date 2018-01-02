package org.nova.game.player.dialogues;

import org.nova.game.player.content.playertask.PlayerTaskHandler;
import org.nova.game.player.content.playertask.PlayerTaskShop;
import org.nova.game.player.content.playertask.TaskManager;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class Elof extends MatrixDialogue {
	
	private int npcId;


	public void start() {
		sendDialogue(SEND_3_OPTIONS, "Select a Option", "I want to talk abut the tasks system.","I want to have my quest cape.", "Nevermind.");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {
				stage = 39;
				sendDialogue(SEND_5_OPTIONS, "Tasks System",
						"I want to start a new task.", "I want to view the tasks store.", "I have completed my current task.", "Nevermind.", "", "");
			}
			else if (componentId == 2) {
				
			}
			else if (componentId == 3) {
			player.interfaces().closeChatBoxInterface();
			}
			else if (componentId == 4) {
			}
			else if (componentId == 5) {
			}
		}
			else if (stage == 39) {
			if (componentId == 1) 
			PlayerTaskHandler.searchforTask(player);
			else if (componentId == 2) 
			PlayerTaskShop.openStore(player);	
			else if (componentId == 3) 
				TaskManager.searhforCompletedTask(player);
				else if (componentId == 4) 
				player.interfaces().closeChatBoxInterface();
		} 
	}

	public void finish() {
	}

}
