package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.newuser.CharacterCreation;

public class Dotmatrix extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"I have been watching you all the time."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Why have you been watching me, Dotmatrix?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
				"To see how you make it, you seem to be ready...",
					"To start your adventure in lands of Nova.",
					"But... there's one thing that I want to show you still."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "What you would like to show me, Dotmatrix?"},
					IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"I will show you around Nova, after that...", "you will know the basics, and you're free to go!"
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			CharacterCreation.startIntroduction(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}