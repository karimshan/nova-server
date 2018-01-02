package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.DeanVellio;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class VellioLow extends MatrixDialogue {

	public VellioLow() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Vellio's Low level teleports", "Al-Kharid guards", "Edgeville Hill giants", "Rock crabs", "Barbarian Village Barbarians", "Nevermind, I don't want to go anywhere!");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(3293, 3163, 0));
			}
			else if (componentId == 2) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(3117,9850,0));
			}
			else if (componentId == 3) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(2419,3847,0));
			}
			else if (componentId == 4) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(3082,3416,0));
			}
			else if (componentId == 5) {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleNPCMessage", DeanVellio.VELLIO, parameters, "Get lost then, "+player.getDisplayName()+"!");
				player.sendMessage("You didn't want to go anywhere, you leave Dean Vellio to it's own peace.");
			}
		}
		
		
		}

	public void finish() {
	}

	private int npcId;
}
