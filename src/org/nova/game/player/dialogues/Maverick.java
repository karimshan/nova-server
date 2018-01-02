package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.newuser.CharacterCreation;

public class Maverick extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
		new String[] { player.getDisplayName(), "Hello Maverick, I have been told to come over here." },
		IS_PLAYER, player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"That's right, you're doing well, "+player.getDisplayName()+"!",
					"Next i'm going to give you a gem and chisel,",
					"you must craft the gems, once you have done that",
					"speak to me again for more instructions."}, IS_NPC, npcId, 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Alright, i'll get to work, Maverick!" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 2;
		} else if (stage == 2) {
		CharacterCreation.addGems(player);
		player.setSpeakedwithMaverick(true); 
		end();
		}else {
			end();
		}
	}

	@Override
	public void finish() {

	}
}