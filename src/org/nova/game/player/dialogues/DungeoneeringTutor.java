package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class DungeoneeringTutor extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Greetings, adventurer."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(),
					"Hello, will you teach me the basics of ",
					"Dungeoneering? What is this place?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"This is Daemonheim. Where you can do Dungeoneering.",
					"By completing dungeons you will receive", 
					"Dungeoneering XP and tokens, with the tokens..."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
					"You can buy many kind of items, also you can",
					"unlock some special and very powerful prayers.",
					"I know it took a long time to travel here, so...",
					"I have an solution, I'll give you Ring of Kinship."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			player.getInventory().addItem(15707,1);
			player.getInventory().refresh();
			player.interfaces().closeChatBoxInterface();
			player.lock(3);
			player.sm("You have received Ring of Kinship from the Dungeoneering Tutor.");
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { "",
			"The dungeoneering tutor has given you Ring of Kinship." },
			IS_ITEM, 15707, 1);
			stage = 5;
		} else if (stage == 5) {
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}