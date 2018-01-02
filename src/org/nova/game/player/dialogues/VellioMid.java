package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.DeanVellio;

public class VellioMid extends MatrixDialogue {

	public VellioMid() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Vellio's Mid level teleports", "Fire Giants dungeon", "Rock crabs", "Karamja Lesser demons", "Castlewars Ogres", "Nevermind, I don't want to go anywhere!");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(2577,9889,0));
			}
			else if (componentId == 2) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(2419,3847,0));
			}
			else if (componentId == 3) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(2839,9558,0));
			}
			else if (componentId == 4) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(2491,3086,0));
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
