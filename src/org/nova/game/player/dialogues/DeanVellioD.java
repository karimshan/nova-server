package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.DeanVellio;

public class DeanVellioD extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"I'm an experienced adventurer, "+player.getDisplayName()+".", "I can take you to perfect train locations for your level."}, IS_NPC, npcId, 9785);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Sounds great, where could you take me?" },
			IS_PLAYER, player.getIndex(), 9785);
			stage = 1;
		} else if (stage == 1) {
		DeanVellio.getDeanVellio().locate(player);
		} 
	}

	@Override
	public void finish() {

	}
}