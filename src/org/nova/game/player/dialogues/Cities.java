package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class Cities extends MatrixDialogue {

	public Cities() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "City teleports",
				"Varrock Centre", "Camelot", "Lumbridge",
				"Edgeville", "Next page");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3213,
						3423, 0));	
			}
			else if (componentId == 2) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2757,
						3478, 0));	
			}
			else if (componentId == 3) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3221,
						3219, 0));	
			}
			else if (componentId == 4) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3087,
						3495, 0));	
			}
			else if (componentId == 5) {
				stage = 39;
				sendDialogue(SEND_5_OPTIONS, "City teleports",
						"Relleka", "Canifis", "Falador",
						"East Ardougne", "Nevermind");
			}
		
			switch (stage) {
			case 39:
				switch (componentId) {
				case 1:
					TeleportManager.SpiritTeleport(player, 0, 0, new Location(2645,
							3674, 0));	
					break;
				case 2:
					TeleportManager.SpiritTeleport(player, 0, 0, new Location(3494,
							3483, 0));	
					break;
				case 3:

					TeleportManager.SpiritTeleport(player, 0, 0, new Location(2971,
							3342, 0));	
					break;
				case 4:

					TeleportManager.SpiritTeleport(player, 0, 0, new Location(2607,
							3297, 0));	
					break;
				case 5:
					player.interfaces().closeChatBoxInterface();
					break;
				}
				break;
			}
		}
		}

	public void finish() {
	}

	private int npcId;
}
