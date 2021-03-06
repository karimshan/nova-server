package org.nova.game.player.dialogues;

public class DTClaimRewards extends MatrixDialogue {

	@Override
	public void start() {
		sendDialogue(SEND_1_TEXT_INFO, "You have a Dominion Factor of "
				+ player.getDominionTower().getDominionFactor() + ".");

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue(SEND_2_OPTIONS,
					"If you claim your rewards your progress will be reset.",
					"Claim Rewards", "Cancel");
		} else if (stage == 0) {
			if (componentId == 1)
				player.getDominionTower().openRewardsChest();
			end();
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
