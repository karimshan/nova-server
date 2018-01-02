package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.newuser.CharacterCreation;

public class AvalaniD extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Welcome to Nova, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Hey wizard, Avalani!", "I'm just wondering what am I doing over here?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"You're new in Nova, i'm going to teach you",
					"pretty much everything that you need."
					,"You can also contact me by clicking the '?' -button.",
					"Also, you can contact me anywhere, at anytime!"}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "For your first task, I will be giving you some", "raw shrimps that you must cook. When you're in battle", "it's always very important to be prepared well."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Here is your shrimps, simply use logs on fire and...", "after that you can cook, use shrimps on the fire."
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			CharacterCreation.addAvalaniSupplies(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}