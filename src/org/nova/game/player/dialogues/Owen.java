package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.DraynorMyth;

public class Owen extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hi, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
		}
	

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Hello, Sir Owen." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendDialogue(SEND_5_OPTIONS, "Select an Option",
					"I would like to exchange random event points.", "Summoning Actions", "",
					"", "");
			stage = 2;
		} else if (stage == 2) {

			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Alright, Aggie. I'll start searching." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
		end();
		DraynorMyth.setStage1(true);
		DraynorMyth.ShowBindforDraynorMyths(player);
			} else
			end();
	}

	@Override
	public void finish() {

	}
}