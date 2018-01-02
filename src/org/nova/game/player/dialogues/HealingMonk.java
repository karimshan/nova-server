package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.DuellistHats;
import org.nova.game.player.content.cities.Monastery;
import org.nova.game.player.content.handlers.SpiritTreeHandler;

public class HealingMonk extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, 
				"Greetings, traveller."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Can you heal me? I'm injured." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Ok."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 2) {
			 SpiritTreeHandler.sendTeleports(player);
			 end();
		} else if (stage == 3) {
			end();
		Monastery.sendMonkHeal(player);
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