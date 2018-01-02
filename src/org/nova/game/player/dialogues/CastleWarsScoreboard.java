package org.nova.game.player.dialogues;

import org.nova.game.player.content.minigames.CastleWars;

public class CastleWarsScoreboard extends MatrixDialogue {

	@Override
	public void start() {
		CastleWars.viewScoreBoard(player);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();

	}

	@Override
	public void finish() {

	}

}
