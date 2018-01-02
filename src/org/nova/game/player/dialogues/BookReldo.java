package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.UzerRuins;

public class BookReldo extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"So you have found the book!"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "That's right Reldo, I did!", "Let's get into book reading now." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Alright, the book says: Inside ruin of uzer...",
					"There was born an wierd creature, which was named:"
					,"Lucien.... Cough... The book is corrupt!",
					"I cannot see the next line!"}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {

			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Hmm, what should we do now Reldo?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Since we know now the creature's name we can study more about it.",
					"I think you should speak to Azzandra, who is inside",
					"the Varrock Museum. Give him this security code: MUFPAH."
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			player.getInventory().deleteItem(757, 1);
			player.getInventory().refresh();
			player.setRuinsofuzerStage(3);
			UzerRuins.ShowBind(player);
			UzerRuins.setFoundbook(true);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}