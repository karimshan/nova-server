package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.CamelotKnight;

public class SigliTwo extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		player.getInventory().deleteItem(2140,5);
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Thanks for the chickens."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Alright, may I have the permission now?", "I have given you the chickens." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Yes you can."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { player.getDisplayName(), "You are very rude, why you speak like that?", "Have I done something wrong?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"I am a huntsman, what do you expect!"
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			player.getInventory().addItem(769, 1); //This is the Cerficate.
			player.setCamelotknightStage(3);
			CamelotKnight.ShowBind(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}