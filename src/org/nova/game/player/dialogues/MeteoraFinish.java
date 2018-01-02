package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.HoarfrostDepths;
import org.nova.game.player.content.quests.QuestBind;

public class MeteoraFinish extends MatrixDialogue {

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
					"I got the book, I even defeated the Ice Elemental.", 
					"What will now happen?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Thanks for the book, I will give it to some of the great magicians",
					"in which, they will use a holy spell of Saradomin"
					,"to make the Ice Elemental disappear. Thanks for your help.",
					"You have helped us a lot, and now I will reward you."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			HoarfrostDepths.sendReward(player);
			QuestBind.CleanTexts(player);
			end();
		} 
	}

	@Override
	public void finish() {

	}
}