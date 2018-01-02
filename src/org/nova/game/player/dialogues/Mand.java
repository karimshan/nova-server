package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.itemactions.Artifacts;

public class Mand extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hello there, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Who are you?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "My name is Mandrith, "+player.getDisplayName()+".",
								"I have a collection of statuettes,",
								"if you have any, feel free to give me.",
								"You will be rewarded with some pure gold!"}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Hold on, I must think a while..." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} 
		switch (stage) {
		case 3:
			   sendDialogue(SEND_2_OPTIONS, "What would you like to do?",
	                    "I have statuettes that I want to exchange.", "I will be coming back another time!");
			   stage = 4;
			break;
		case 4:
			switch (componentId) {
			case 1:
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "I have statuettes that I want to exchange." },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 5;
				break;
			case 2:
				player.interfaces().closeChatBoxInterface();
				break;
			}
			break;
		case 5:
			end();
			player.interfaces().closeChatBoxInterface();
			Artifacts.exchangeArtifacts(player);
			break;
		}
	}

	@Override
	public void finish() {

	}
}