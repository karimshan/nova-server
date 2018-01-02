package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class SirVant extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { player.getDisplayName(), "Hello!" },
				IS_PLAYER, player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "Wait a moment - there's a dragon coming!"}, IS_NPC, npcId, 9827);
			stage = 1;
			break;
		case 1:
			break;
		}
	}

	@Override
	public void finish() {

	}
}