package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class ArtisanDia extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hey there."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Hey, "+NPCDefinition.get(npcId).name+".", "What are you doing here?"},
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"I'm here to make sure everything goes well,",
					"Your job here is to create armours for the"
					,"dwarven army so I think that you should get to work."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}