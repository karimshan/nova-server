package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class Islands extends MatrixDialogue {

	public Islands() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Spirit Tree: Island Teleports", "Brimhaven","Karamja", "Ape Atoll", "Miscellania", "Nevermind");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3258,
						5450, 0));	
			}
			else if (componentId == 2) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2906,
						3187, 0));	
			}
			else if (componentId == 3) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2784,
						2786, 0));	
			}
			else if (componentId == 4) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2540,
						3865, 0));	
			}
			else if (componentId == 5) {
				stage = 39;
				sendDialogue(SEND_5_OPTIONS, "Spirit Tree: Dungeon Teleports",
						"Brimhaven Dungeon", "Ancient Cavern", "Asgarnian Ice Caves",
						"Waterbirth Island Dungeon", "Nevermind");
			}
		}
			else if (stage == 39) {
				if (componentId == 1) 
					TeleportManager.SpiritTeleport(player, 0, 0, new Location(2710,
							9485, 0));		
			else if (componentId == 2) 
					TeleportManager.SpiritTeleport
					(player, 0, 0, new Location(1737,
							5312, 0));
					
			else if (componentId == 3) 
					TeleportManager.SpiritTeleport(player, 0, 0, new Location(3005,
							9548, 0)); 
			
				else if (componentId == 4) 
					TeleportManager.SpiritTeleport(player, 0, 0, new Location(2548,
							10151, 0));
				
				else if (componentId == 5) {
				player.interfaces().closeChatBoxInterface();
				}
		} 
		}

	public void finish() {
	}

	private int npcId;
}
