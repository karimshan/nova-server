package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class KingFrogD extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_3_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, 
				""+player.getDisplayName()+", you must help me! I have been turned", "into a frog by a well-meaning wizard who suffers from", "an unfortunate obsession with frogs."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "What am I doing here?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"The only thing that will restore my true form is a kiss."
					}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Honestly, people ask me for the strangest favours!", "Okay, I suppose I could be persuaded..." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 5) {
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}