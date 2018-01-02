package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class Forests extends MatrixDialogue {

	public Forests() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Spirit Tree: Forest Teleports", "Feldip Hills", "Morytania", "Haunted Woods", "Tree Gnome Stronghold", "Nevermind");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2579,
						2970, 0));	
			}
			else if (componentId == 2) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3456,
						3432, 0));	
			}
			else if (componentId == 3) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3562,
						3480, 0));	
			}
			else if (componentId == 4) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2459,
						3458, 0));	
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
