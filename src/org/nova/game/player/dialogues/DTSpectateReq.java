package org.nova.game.player.dialogues;

public class DTSpectateReq extends MatrixDialogue {

	@Override
	public void start() {
		sendDialogue(
				SEND_2_TEXT_INFO,
				"You don't have the requirements to play this content, but you can",
				"spectate some of the matches taking place if you would like.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		player.getDominionTower().openSpectate();
		end();
	}

	@Override
	public void finish() {

	}

}
