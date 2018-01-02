package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.handlers.SpiritTreeHandler;

public class SpiritTree extends MatrixDialogue {

	private int npcId = 3636;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"What do you want, "+player.getDisplayName()+"?"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "I'm looking a teleportation.", "Could you teleport me to somewhere?"},
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"As you wish, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			 SpiritTreeHandler.sendTeleports(player);
			} else
			end();
	}

	@Override
	public void finish() {

	}
}