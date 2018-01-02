package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.HoarfrostDepths;
import org.nova.game.player.content.quests.QuestBind;

public class Osman extends MatrixDialogue {

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
					"Hello Osman. I have been told to come speak with you.", 
					"Is there anything important that I need to know?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Yes, the Al-Kharid guards are not strong enough.",
					"We will need a strong and powerful warrior like you,"
					,"to complete this big fight. But are you sure",
					"that you can handle it all and kill him only yourself?"}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
					"This does not sound easy, because it isn't!", 
					"I have my very good old friend at Lunar Isle, speak with him!"}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			end();
			QuestBind.CleanTexts(player);
			player.setHoarfrostdepthsStage(1);
			HoarfrostDepths.ShowBindforHoarfrostDepths(player);
			} else
			end();
	}

	@Override
	public void finish() {

	}
}