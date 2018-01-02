package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.MiscellaniaThrone;

public class KingBolrenTwo extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Have you found out whos the king at Etceteria?"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Yes I have, sir Bolren!","the king's name is Percival.", "They need money aswell, that's why they have", "a war against you." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Interesting... How could we claim peace?",
					"We really need stop this war, too much is"
					,"too much. What you think?",
					"What should we offer him?"}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Give him some gold? What you want?","I really have no idea." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Gold, of course. Give him this contract."
					}, IS_NPC, npcId, 9827);
			player.getInventory().addItem(3710, 1);
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