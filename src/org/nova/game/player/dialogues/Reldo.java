package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.UzerRuins;

public class Reldo extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Aha! Another adverturer, how may I help you?"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
			new String[] { player.getDisplayName(), "You're nice! Sir Owen has told me to come here", "so you can tell me about this unkown creature,", "which is inside the Ruins of Uzer!" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"I've heard rumours about it, i'm not sure is those true...",
					"I think theres an book inside my Library, you won't"
					,"Understand the language tho from the book but...",
					"I can of course tell you what it says."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "But sorry I cannot help you yet, i'm busy currently.", "Could you please find the book..."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Somewhere inside the library bookcases please."
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
		player.setRuinsofuzerStage(2);
			UzerRuins.ShowBind(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}