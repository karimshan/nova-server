package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class SimpleNPCMessage extends MatrixDialogue {

	private int npcId;
	private String message;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		message = (String) parameters[1];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						message }, IS_NPC, npcId, 9827);
		//this is the gayest shit i have ever seen
	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}

	@Override
	public void finish() {

	}

}
