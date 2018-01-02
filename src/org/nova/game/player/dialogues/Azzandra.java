package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.UzerRuins;

public class Azzandra extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hmm... What you want i'm kinda busy."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "MUSPAH!" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Oh, I know why you came here! Let's begin.",
					"The Lucien is an holy creature, which is using strong and"
					,"very powerful attacks. He's located in the cave at ",
					"Ruins of Uzer."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "This battle will be not easy, but I think that", "you can make it, only with the power of Saradomin."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"May the Saradomin be with you, friend."
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			player.setRuinsofuzerStage(5);
			UzerRuins.ShowBind(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}