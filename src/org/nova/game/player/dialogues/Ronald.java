package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class Ronald extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Ahoy! Hey, "+player.getDisplayName()+"!"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Hey there, Ronald. I have been told to come over here.", "I was asked speak with you, is there anything I need to do?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 2;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Hello friend, I am Meteora.",
					"I'm not sure what is actually happening here."
					,"I've heard some really weird noises around the Lunar Isle.",
					"I believe it's coming from an cave."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "The dwarfs are in need of your help, "+player.getDisplayName()+"!", "But first... I have a task for you."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "I'm ready to help you, what's that?"},
					IS_PLAYER, player.getIndex(), 9827);
			stage = 5;
		} else if (stage == 5) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { NPCDefinition.get(npcId).name, "I want a cake because I'm hungry, "+player.getDisplayName()+"!", "Actually, bring me a chocolate cake!"}, IS_NPC, npcId, 9827);
		stage = 6;
		}
		else if (stage == 6) {
			player.setWantedStage(1);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}