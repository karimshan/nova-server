package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class Dungeons extends MatrixDialogue {

	public Dungeons() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Spirit Tree: Dungeon Teleports", "Chaos Tunnels", "Revenant Dungeon (Wilderness)", "Taverly Dungeon", "Fire Giant Dungeon", "Next page");
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
				stage = 39;
				sendDialogue(SEND_5_OPTIONS, "Spirit Tree: Dungeon Teleports",
						"Brimhaven Dungeon", "Ancient Cavern", "Asgarnian Ice Caves",
						"Waterbirth Island Dungeon", "Next page");
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
				
				else if (componentId == 5) { //rev
					stage = 41;
					sendDialogue(SEND_5_OPTIONS, "Spirit Tree: Dungeon Teleports",
							"Revenants Dungeon (Wilderness)", "Chaos Dwarf Battlefield", "Mage Bank",
							"None", "First page");
				}
		} 
	
	else if (stage == 41) {
		if (componentId == 1) {

			player.getControllerManager().startController("Wilderness");
			TeleportManager.SpiritTeleport(player, 0, 0, new Location(3069,
					3648, 0));
		}
	else if (componentId == 2) 
			TeleportManager.SpiritTeleport
			(player, 0, 0, new Location(1518,
					4703, 0));
			
	else if (componentId == 3) 
		TeleportManager.SpiritTeleport(player, 0, 0, new Location(2539,
				4715, 0));
	
		else if (componentId == 4) 
			player.interfaces().closeChatBoxInterface();
		
		else if (componentId == 5) {	
			sendDialogue(SEND_5_OPTIONS, "Spirit Tree: Dungeon Teleports", "Chaos Tunnels", "Revenant Dungeon (Wilderness)", "Taverly Dungeon", "Fire Giant Dungeon", "Next page");
		stage = 2;
		}
} 
		
		}

	public void finish() {
	}

	private int npcId;
}
