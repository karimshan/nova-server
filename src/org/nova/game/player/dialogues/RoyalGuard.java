package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.utility.misc.Misc;

public class RoyalGuard extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if(Misc.random(3) == 0)
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"'Ello " + player.getDisplayName() + "!",
						"Lovely day in Nova!"}, IS_NPC, npcId, 9850);
		else if(Misc.random(3) == 1)
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
							"'Ello " + player.getDisplayName() + "!",
							"Could you tell the King to import some tea, please?"}, IS_NPC, npcId, 9850);
		else if(Misc.random(3) == 2)
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
							"'Ello " + player.getDisplayName() + "!",
							"Not a single intruder today! They must be scared of us."}, IS_NPC, npcId, 9850);
		else if(Misc.random(3) == 3)
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
							"'Ello " + player.getDisplayName() + "!",
							"Glad to see you out and about after your.. accident."}, IS_NPC, npcId, 9850);
		else
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
							"'Ello " + player.getDisplayName() + "!",
							"Glad to see you out and about after your.. accident."}, IS_NPC, npcId, 9850);
			
	}

	@Override
	public void run(int interfaceId, int componentId) {
			end();
    }

    @Override
    public void finish() {
    }
}


