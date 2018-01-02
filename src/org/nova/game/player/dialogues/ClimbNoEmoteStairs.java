package org.nova.game.player.dialogues;

import org.nova.game.map.Location;

public class ClimbNoEmoteStairs extends MatrixDialogue {

	private Location upTile;
	private Location downTile;

	// uptile, downtile, climbup message, climbdown message, emoteid
	@Override
	public void start() {
		upTile = (Location) parameters[0];
		downTile = (Location) parameters[1];
		sendDialogue(SEND_3_LARGE_OPTIONS, "What would you like to do?",
				(String) parameters[2], (String) parameters[3], "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_3_LARGE_OPTIONS && componentId == 2) {
			player.useStairs(-1, upTile, 0, 1);
		} else if (interfaceId == SEND_3_LARGE_OPTIONS && componentId == 3)
			player.useStairs(-1, downTile, 0, 1);
		end();
	}

	@Override
	public void finish() {

	}

}
