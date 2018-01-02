package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.map.Location;
import org.nova.game.player.content.HoarfrostHollow;
import org.nova.game.player.content.quests.QuestBind;

public class NedShip extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_3_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"I see that you got the Seal of Passage! Good job!",
						"We are going to Crandor, in crandor you must",
						"find an cave entrance and find Ice Demon..."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(),
					"Isn't that dangerous, Ned?", 
					"I have bad feeling about this." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Honestly, i'm not going to lie and gonna say straight that...", 
					"Yes, it definitely is hard, the Demon is...", 
					"Very powerful, you need food to kill him.",
					"As I said before, this battle will NOT be easy."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
					"But back to business, let's continue.",
					"I will take you to Crandor right now."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Best of luck, my very good friend."
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			HoarfrostHollow.HoarfrostTeleport(player, 1, 1, new Location(2849, 3235, 0));
			player.setHoarfrostStage(3);
			QuestBind.ShowBindforHoarfrost(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}