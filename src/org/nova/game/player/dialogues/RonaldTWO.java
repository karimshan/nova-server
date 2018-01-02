package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class RonaldTWO extends MatrixDialogue {

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
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Ronald, I got the cake for you!"},
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Amazing job, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "I will give you access to Keldagrim,", "you can now enter there. Go under the entrance and...",
					"You will receive more instructions."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Alright, thanks Ronald."},
					IS_PLAYER, player.getIndex(), 9827);
			stage = 5;
		} else if (stage == 5) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { NPCDefinition.get(npcId).name, "No problem, the dwarven army will need you.", "You have show me that you're trustworthy."}, IS_NPC, npcId, 9827);
			player.setWantedStage(2);
			if (player.getInventory().containsItem(1891, 1))
				player.getInventory().deleteItem(1891,1);
			else if (player.getInventory().containsItem(1892, 1))
				player.getInventory().deleteItem(1891,1);
		stage = 6;
		}
		else if (stage == 6) {	
			player.setWantedStage(3);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}