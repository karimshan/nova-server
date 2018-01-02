package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.CamelotKnight;

public class Arthur extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hey there."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Hey sir Arthur. I've been told to come here.","Can I help you somehow?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Yes, finally an adverturer comes here!",
					"You can, we need help of killing sea troll, a mysterious"
					,"monster underwater, it's not easy to kill.",
					"But since Relleka doesn't belong in to Camelot"}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "We need permission from Sigli the Huntsman,", "once you can proof me that he have accepted us", "to defeat sea troll queen, we can move to action."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Move in to Relleka and find Sigli the Huntsman."
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			player.setCamelotknightStage(1);
			CamelotKnight.ShowBind(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}