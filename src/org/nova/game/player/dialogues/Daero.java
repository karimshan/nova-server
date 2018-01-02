package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;

public class Daero extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { player.getDisplayName(), "I need to return to Crash Island." },
				IS_PLAYER, player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
	switch (stage) {
	case -1:
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, "You know the routine..."}, IS_NPC, npcId, 9827);
		stage = 0;
		break;
	case 0:
		TeleportManager.DaeroTeleport(player, 0, 0, new Location(2391,9886,0));
		break;
		
	}
	}

	@Override
	public void finish() {

	}
}