package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.MiscellaniaThrone;

public class KingBolrenThird extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hey again!"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Bolren... Percival's castle is so poor that","they cannot afford even food." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Really...? Wow he should come to me and",
					"tell me his problem instead of starting"
					,"an insane war, well anyways...",
					"Give him this contract, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Give him some gold? What you want?","I really have no idea." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Umm, go ask him what he wants to be in peace."
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
		player.setMiscellaniaThrone(4);
		MiscellaniaThrone.ShowBind(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}