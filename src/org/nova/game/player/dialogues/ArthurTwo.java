package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.cities.BarbarianOutpost;
import org.nova.game.player.content.quests.CamelotKnight;

public class ArthurTwo extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"So you have the returned, and got permission!"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "That's right King Arthurt!", "What do I need to do next?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"You will be jumping into water...", " Sea troll queen lives underwater."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Alright, let's being I am ready!" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Mhmm, i'll teleport you to the bridge where you find", "the sea troll queen. Goodluck my friend."
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			player.getInventory().deleteItem(769, 1);
		BarbarianOutpost.processDiveJump(player);
			player.setCamelotknightStage(4);
			CamelotKnight.ShowBind(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}