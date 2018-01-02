package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class Minigames extends MatrixDialogue {

	public Minigames() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Minigames",
				"Al-Kharid Duel Arena", "Dominion Tower", "Barrows Brothers",
				"Fight Pits", "Nevermind");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3258,
						5450, 0));	
			}
			else if (componentId == 2) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3065,
						3650, 0));	
			}
			else if (componentId == 3) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2884,
						9799, 0));
			}
			else if (componentId == 4) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2577,
						9888, 0));	
			}
			else if (componentId == 5) {
				player.interfaces().closeChatBoxInterface();
			}
		}
			else if (stage == 39) {
				if (componentId == 1) 
					TeleportManager.SpiritTeleport(player, 0, 0, new Location(2710,
							9485, 0));		
			else if (componentId == 2) 
					TeleportManager.SpiritTeleport
					(player, 0, 0, new Location(1746,
							5325, 0));
					
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
