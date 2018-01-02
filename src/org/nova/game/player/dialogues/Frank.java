package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.QuestBind;

public class Frank extends MatrixDialogue {

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
			new String[] { player.getDisplayName(),
					"So you're that Frank...",
					"The man, who will give me passport for ship?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"I know, Ned has told me that someone named "+player.getUsername()+"",
					"will come here and ask for the passport.", 
					"Now you must find Ned..."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
					"Ned will be at port Sarim,",
					"show the passport to Ned, also get prepared for a fight.",
					"You and Ned are going to take a boat to Crandor.",
					"This fight will not be easy, food will be needed."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"This will not be an easy fight, goodluck friend!"
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			player.getInventory().addItem(9083, 1);
			player.getInventory().refresh();
			player.sm("Game: Received item: Ship Passport (Seal of Passage).");
			player.setHoarfrostStage(1);
			QuestBind.ShowBindforHoarfrost(player);
			player.interfaces().closeChatBoxInterface();
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}