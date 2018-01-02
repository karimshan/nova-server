package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.DuellistHats;
import org.nova.game.player.content.handlers.SpiritTreeHandler;

public class Escotada extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, 
				"Hey there "+player.getDisplayName()+", how may I help you?"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Are you the guy that gives the hats?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Yes, you have currently "+player.duelWins+" duel wins."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 2) {
			 SpiritTreeHandler.sendTeleports(player);
			 end();
		} else if (stage == 3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"The more you got duel kills, the more you get caps.", "But I can give you now your current duellist cap."
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			end();
			DuellistHats.addHat(player);
			} else
			end();
	}

	@Override
	public void finish() {

	}
}