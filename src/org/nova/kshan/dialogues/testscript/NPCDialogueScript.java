package org.nova.kshan.dialogues.testscript;

import org.nova.game.npc.NPC;

/**
 * 
 * @author K-Shan
 *
 */
public class NPCDialogueScript extends DialogueScript {
	
	private NPC npc;
	
	public NPCDialogueScript(NPC npc, String[] lines) {
		this.npc = npc;
		super.lines = lines;
	}
	
	public NPC getNPC() {
		return npc;
	}
	
}
