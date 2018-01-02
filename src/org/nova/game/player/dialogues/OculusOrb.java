package org.nova.game.player.dialogues;

import org.nova.game.player.content.cities.Underwater;
import org.nova.game.player.content.quests.CamelotKnight;

public class OculusOrb extends MatrixDialogue {

	@Override
	public void start() {
		sendDialogue(SEND_2_OPTIONS, "Select a Option",
				"Teleport and finish the battle quest.", "I want to stay here."); //Change options maybe?
	}

	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_2_OPTIONS){
			if (componentId == 1) {
	Underwater.leaveUnderwater(player);
	CamelotKnight.sendReward(player);
			}  else if 	(componentId == 2) {
				player.interfaces().closeChatBoxInterface();
				player.sm("You should finish the tutorial, here is nothing to do.");
		}
		}
		}

	@Override
	public void finish() {

	}

}