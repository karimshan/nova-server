package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.masks.Animation;
import org.nova.game.player.content.quests.AwakingSaradomin;

public class CuratorD extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, 
				"Welcome to the Varrock's Museum, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Hey, Curator. I have heard that you need help?", "How may I help you sir?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"We must help the Saradomin's god.",
					"Shes in some serious danger, "+player.getDisplayName()+"",
					"And only can get us the cure."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {

			sendEntityDialogue(SEND_3_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Sounds challengsive, well my kind of", "adventurers are always ready for a", "challengsive tasks!" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
			player.setNextAnimation(new Animation(829));
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Drink this bottle, you will fall asleep.", "You will see what happened to Saradomin.", "Once you wake up get back and talk to me again."
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			end();
			player.interfaces().closeChatBoxInterface();
			AwakingSaradomin.introduceQuest(player);
			} else
			end();
	}

	@Override
	public void finish() {

	}
}