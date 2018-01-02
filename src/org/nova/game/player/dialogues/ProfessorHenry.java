package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class ProfessorHenry extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Welcome to player safety programme, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Hello professor Henry.", "What did you mean with the 'player safety programme'?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"It's simple and easy, no worries.",
					"I am going to quiz you right now,"
					,"you will be granted with two experience books.",
					}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "If you answer wrong, you can always start again."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"But let's begin now, let's start the quiz, "+player.getDisplayName()+"!"
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}