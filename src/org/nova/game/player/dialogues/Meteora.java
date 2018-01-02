package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.HoarfrostDepths;

public class Meteora extends MatrixDialogue {

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
			new String[] { player.getDisplayName(), "Osman has told me to come here, and speak with you.", "Is there something that I need to know?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Hey, I am Meteora.",
					"I'm not sure what is actually happening here."
					,"I've heard some really weird noises around the Lunar Isle.",
					"I believe it's coming from a cave."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "Can you please check out the cave?", "Something must be really wrong in there."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"If you find anything, please tell me!"
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			player.setHoarfrostdepthsStage(2);
			HoarfrostDepths.ShowBindforHoarfrostDepths(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}