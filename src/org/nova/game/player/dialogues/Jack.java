package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.handlers.SpiritTreeHandler;

public class Jack extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, 
				"Hey there "+player.getDisplayName()+", how may I help you?"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Oh hey Jack, i'm looking for a teleportation!" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"As you wish, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			 SpiritTreeHandler.sendTeleports(player);
			 end();
		} else if (stage == 3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"She is the frog with the crown. Make sure you speak to", "her, not the other frogs, or she'll be offended."
					}, IS_NPC, npcId, 9827);
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