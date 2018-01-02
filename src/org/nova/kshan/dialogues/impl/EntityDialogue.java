package org.nova.kshan.dialogues.impl;

import org.nova.cache.definition.NPCDefinition;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class EntityDialogue extends Dialogue {

	private int npcId;
	private String[] lines;
	private boolean noContinue;
	private int anim;

	@Override
	public void start() {
		npcId = (Integer) data[0];
		lines = (String[]) data[1];
		if(data.length >= 3)
			noContinue = (Boolean) data[2];
		else
			noContinue = false;
		if(data.length >= 4)
			anim = (Integer) data[3];
		else
			anim = NORMAL_TALKING;
		String name = NPCDefinition.get(npcId).name;
		sendEntityDialogue(npcId == -1 ? PLAYER : NPC, anim, npcId, npcId == -1 ? player.getDisplayName() : name, noContinue, lines);
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		end();
	}

	@Override
	public void finish() {

	}

}
