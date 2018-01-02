package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class GameTeles extends MatrixDialogue {

	public GameTeles() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Minigame teleports",
				"Al-Kharid Duel Arena", "Clan Wars", "Barrows Brothers",
				"None", "Nevermind");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3367,
						3274, 0));	
			}
			else if (componentId == 2) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2970,
						9679, 0));	
			}
			else if (componentId == 3) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3566,
						3288, 0));	
			}
			else if (componentId == 4) {
			player.interfaces().closeChatBoxInterface();
			}
			else if (componentId == 5) {
				stage = 39;
				sendDialogue(SEND_5_OPTIONS, "Spirit Tree: Dungeon Teleports",
						"Brimhaven Dungeon", "Ancient Cavern", "Asgarnian Ice Caves",
						"Waterbirth Island Dungeon", "Nevermind");
			}
		}
			
		}

	public void finish() {
	}

}
