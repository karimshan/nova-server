package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class FrogD extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, 
				"Welcome to the Land of the Frogs."}, IS_NPC, npcId, 9827);
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
					"The Frog Princess sent for you."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {

			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Who is the Frog Princess?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"She is the frog with the crown. Make sure you speak to", "her, not the other frogs, or she'll be offended."
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