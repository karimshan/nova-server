package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.UzerRuins;

public class SirOwen extends MatrixDialogue {

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
			new String[] { player.getDisplayName(), "Hey Sir Owen, I have been told that I must come here and", "Speak with you, great sir." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"The Al-Kharid secret army is needing you help.",
					"There's an secret monster inside staircase."
					,"Before going there, you must visit at varrock.",
					"There's Reldo at Varrock castle library..."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "You have to speak with him, Reldo will give you", "knowledge and more details about the monster."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Best of luck, adventurer!"
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			player.setRuinsofuzerStage(1);
			UzerRuins.ShowBind(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}