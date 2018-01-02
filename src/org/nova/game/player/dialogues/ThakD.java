package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.Wanted;

public class ThakD extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Good day, how can I help you?"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
	switch (stage) {
	case -1:
				sendEntityDialogue(SEND_2_TEXT_CHAT,
						new String[] { player.getDisplayName(), "Oh hey Thak, I have been told to come over here.", "Is there anything I can do for you?" },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 2;
		break;
	case 2:
		case 1://Takes to keldagrim.
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
							"Hmm let's think..."}, IS_NPC, npcId, 9827);
			stage = 3;
			break;
	
		case 3:
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
							"hmm..... *hic*"}, IS_NPC, npcId, 9827);
			stage = 4;
		break;
		case 4:
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
							"I want you to buy me a beer, " + player.getDisplayName() + "", "After drinking a beer I can think again."}, IS_NPC, npcId, 9827);
			stage = 5;
			break;
		case 5:
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Oh okay, i'll do it Thak."},IS_PLAYER, player.getIndex(), 9827);
		stage = 6;
			break;
		case 6:
			player.setWantedStage(6);
			Wanted.ShowBind(player);
			break;
		}
	}
	@Override
	public void finish() {

	}
	
}
