package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class FirstVant extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_3_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hey there, "+player.getDisplayName()+".",
						"I'm going to give you a set for fighting.", 
						"You won't receive any XP inside this tutorial."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Who are you!?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "My name is Sir Vant.",
								"I am one of the most powerful and greatest fighters.",
								"Remember to use some food and potions during this battle.",
								"Food will be healing also your lifepoints."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
					"I will give you also some potions for this battle, ",
								"potions will be boosting your skills up.",
								"Let's begin now. "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
			stage = 7;
		} else if (stage == 7) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
				"And now i'm going to give you...",
				"Fighting equipment,",
				"some food of course,",
				"and Potions, to boost your skills!"}, IS_NPC, npcId, 9827);
			stage = 4;
		} else if (stage == 4) {
			stage = 5;
			
	
		} else if (stage == 5) {
			//controler.updateProgress();
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}