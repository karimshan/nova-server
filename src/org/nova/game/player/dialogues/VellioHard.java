package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.DeanVellio;

public class VellioHard extends MatrixDialogue {

	public VellioHard() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Vellio's High level teleports", "Living rock cavern creatures", "Waterbirth Island Dungeon", "Kalphite Hive", "Chaos tunnels", "Nevermind, I don't want to go anywhere!");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(3663,5118,0));
			}
			else if (componentId == 2) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(2542,10163,0));
			}
			else if (componentId == 3) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(3486,9510,2));
			}
			else if (componentId == 4) {
				DeanVellio.getDeanVellio().vellioTeleport(player, new Location(3294,5478,0));
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
