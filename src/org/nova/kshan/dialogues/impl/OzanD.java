package org.nova.kshan.dialogues.impl;

import org.nova.game.npc.NPC;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class OzanD extends Dialogue {
	
	NPC npc;
	
	public void start() {
		npc = (NPC) data[0];
		sendNPCDialogue(npc.getId(), npc.defs().name, false,
			"Hello there, "+player.getDisplayName()+", are you after a quest?");
	}
	
	public void process(int i, int b) { 
		if(stage == 0)
			sendOptions("What would you like to say?", new String[] { 
					"What quest do you have for me?", "I had another question", "Why do you have a bow?", "Nevermind." });
		else if(stage == 1) {
			switch(b) {
				case OPTION1:
					sendPlayerDialogue(false, "What quest do you have for me?");
					stage = 2;
					break;
				case OPTION2:
					sendPlayerDialogue(false, "What is your job here?");
					stage = 3;
					break;
				case OPTION3:
					sendPlayerDialogue(false, "Why do you have a bow?");
					stage = 4;
					break;
				case OPTION4:
					sendPlayerDialogue(false, "Nevermind.");
					stage = 10;
					break;
			}
		} 
		else
			end();
	}
	
	public void finish() {
		
	}
	
}
