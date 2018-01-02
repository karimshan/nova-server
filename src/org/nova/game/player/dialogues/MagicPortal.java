package org.nova.game.player.dialogues;

import org.nova.game.map.Location;

public class MagicPortal extends MatrixDialogue {

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1)
			stage = 0;
		if (stage == 0) {
			if (componentId == 2) {
				player.packets().sendMessage("You enter the portal...");
				player.useStairs(10584, new Location(3233, 9315, 0), 2, 3,
						"..and are transported to an altar.");
				player.addWalkSteps(1599, 4515, -1, false);
				end();
			}
			if (componentId == 3) {
				player.packets().sendMessage("You enter the portal...");
				player.useStairs(10584, new Location(2152, 3868, 0), 2, 3,
						"..and are transported to an altar.");
				player.addWalkSteps(1600, 4514, -1, false);
				end();
			} else
				end();
		}
	}

	@Override
	public void start() {
		sendDialogue(SEND_3_LARGE_OPTIONS, "Select an Option",
				"Ancient Magic Altar", "Lunar Magic Altar", "Never Mind");
	}

}
