package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.MiscellaniaThrone;

public class KingPercivalTwo extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						""+player.getDisplayName()+" You truly are an hero!"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Haha thanks Percival. Glad to see that","you both can be in peace now." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"We will remember you always as a.",
					"real hero. Thank you so much, "+player.getDisplayName()+"!"
					,"our citizens must eat something. ",
					"This is an big problem and..."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Okay. I go figure out this with king Bolren.","We will try to find a solution for this." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 5;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Thank you very much, "+player.getDisplayName()+"!"
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			player.getInventory().deleteItem(3710,1);
		MiscellaniaThrone.sendReward(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}