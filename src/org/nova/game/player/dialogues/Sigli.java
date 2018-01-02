package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.CamelotKnight;

public class Sigli extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hello."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Hey Sigli, I need a permission", "for King Arthur." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Before I give you any permissions, you must bring me",
					"5 cooked chickens, I'm starving!."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			player.setCamelotknightStage(2);
			CamelotKnight.ShowBind(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}