package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.PyramidMystery;

public class MariusGiste extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hey there adventurer!"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Hello Marius, i've been told to come over here." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Yes I know that. I know everything pretty much.",
					"You must be looking for an adventure so I have"
					,"one for you.",
					"Pyramid's Mystery."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "You must find out what's in the pyramid", "which is located deep in the Al-Kharid deserts."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Alright. I'll start working on it!" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 5;
		} else if (stage == 5) {
			PyramidMystery.setStage1(true);
			PyramidMystery.ShowBind(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}