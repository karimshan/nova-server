package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.map.Location;

public class MosolRei extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, 
				"Welcome to the Shilo village, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Thanks, Mosol Rei. I have a question...", "Could you let me pass the gate?" },
			IS_PLAYER, player.getIndex(), 9827);
		if (player.isCompletedRuinsofUzer()) {
			stage = 1;
		} else {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"I don't know who you are.", "Hahaha let the Zombies eat you!"}, IS_NPC, npcId, 9827);
			stage = 7;
		}
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"For sure, "+player.getDisplayName()+"!"
					}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			player.setLocation(new Location(2867,2952,0));
		end();
		player.getMatrixDialogues().startDialogue("SimpleMessage", "Mosol Rei has let you in to the Shilo village.");
			}
		 else if (stage == 7) {
			end();
			player.setLocation(new Location(2877,2951,0));
				}
		 else
			end();
	}

	@Override
	public void finish() {

	}
}