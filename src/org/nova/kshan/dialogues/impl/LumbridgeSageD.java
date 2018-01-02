package org.nova.kshan.dialogues.impl;

import org.nova.game.npc.NPC;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class LumbridgeSageD extends Dialogue {
	
	NPC npc;

	@Override
	public void start() {
		npc = (NPC) data[0];
		sendNPCDialogue(npc.getId(), npc.getName(), false,
			"Greetings, adventurer. How may I help you?");
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		if(stage == 0) {
			sendOptions("Choose an Option:", new String[] { 
				"Who are you?", "Tell me about the town of Lumbridge.", "I'm fine for now, thanks." });
			stage = 1;
		} else if(stage == 1) {
			if(buttonId == OPTION1_OTHER) {
				sendNPCDialogue(npc.getId(), npc.getName(), false, 
					"I am Phileas, the Lumbridge Sage. In times past, people",
					"came from all around to ask me for advice. My renown",
					"seems to have diminished somewhat in recent years,",
					"though. Can I help you with anything?" );
				stage = 2;
			} else if(buttonId == OPTION2_OTHER) {
				sendNPCDialogue(npc.getId(), npc.getName(), false, 
					"Lumbridge is one of the older towns in the human-",
					"controlled kingdoms. It was founded over two hundred",
					"years ago towards the end of the Fourth Age. It's called",
					"Lumbridge because of this bridge built over the River Lum." );
				stage = 4;
			} else if(buttonId == OPTION3_OTHER) {
				end();
				if(!player.tasks().hasFinished(player.tasks().ldTasks(), 
						(byte) player.tasks().indexFromName("TalkToSage")))
					player.tasks().taskCompletion("TalkToSage");
			}
		} else if(stage == 2) {
			sendOptions("Select an Option", new String[] { 
				"Tell me about the town of Lumbridge.", "I'm fine for now, thanks." });
			stage = 3;
		} else if(stage == 3) {
			if(buttonId == OPTION1) {
				sendNPCDialogue(npc.getId(), npc.getName(), false, 
					"Lumbridge is one of the older towns in the human-",
					"controlled kingdoms. It was founded over two hundred",
					"years ago towards the end of the Fourth Age. It's called",
					"Lumbridge because of this bridge built over the River Lum." );
				stage = 4;
			} else {
				if(!player.tasks().hasFinished(player.tasks().ldTasks(), 
						(byte) player.tasks().indexFromName("TalkToSage")))
					player.tasks().taskCompletion("TalkToSage");
				end();
			}
		} else if(stage == 4) {
			sendNPCDialogue(npc.getId(), npc.getName(), false, 
				"The town is governed by Duke Horacio, who is a good",
				"friend of our monach, King Roald of Misthalin." );
			stage = 5;
		} else if(stage == 5) {
			sendNPCDialogue(npc.getId(), npc.getName(), false, 
				"Recently, however, there have been great changes due to",
				"the Battle of Lumbridge." );
			stage = 6;
		} else if(stage == 6) {
			sendOptions("Select an Option", new String[] { 
				"Who are you?", "What about the battle?", "Goodbye." });
			stage = 7;
		} else if(stage == 7) {
			if(buttonId == OPTION1_OTHER) {
				sendNPCDialogue(npc.getId(), npc.getName(), false, 
					"I am Phileas, the Lumbridge Sage. In times past, people",
					"came from all around to ask me for advice. My renown",
					"seems to have diminished somewhat in recent years,",
					"though. Can I help you with anything?" );
				stage = 2;
			} else if(buttonId == OPTION2_OTHER) {
				sendNPCDialogue(npc.getId(), npc.getName(), false, 
					"Indeed, not long ago there was a great fight between",
					"Saradomin and Zamorak on the battlefield west of",
					"the castle." );
				stage = 9;
			} else {
				sendNPCDialogue(npc.getId(), npc.getName(), false, 
					"Good adventuring, traveller." );
				stage = 8;
			}
		} else if(stage == 8) {
			end();
			if(!player.tasks().hasFinished(player.tasks().ldTasks(), 
					(byte) player.tasks().indexFromName("TalkToSage")))
				player.tasks().taskCompletion("TalkToSage");
		} else if(stage == 9) {
			sendNPCDialogue(npc.getId(), npc.getName(), false, 
				"Titanic forces were unleashed as neither side could gain",
				"the upper hand. Each side sought advantages, but it was",
				"close until the end." );
			stage = 10;
		} else if(stage == 10) {
			sendNPCDialogue(npc.getId(), npc.getName(), false, 
				"The battle lasted for months, but in the end the forces of", 
				"the holy Saradomin were triumphant. Zamorak was",
				"defeated... but..." );
			stage = 11;
		} else if(stage == 11) {
			sendNPCDialogue(npc.getId(), npc.getName(), false, 
				"Before Saradomin could complete his victory, Moia, the",
				"general of Zamorak's forces, transported him away." );
			stage = 12;
		} else if(stage == 12) {
			sendNPCDialogue(npc.getId(), npc.getName(), false, 
				"Now, the battlefield lies empty save for a single",
				"Saradominist devotee." );
			stage = 13;
		} else if(stage == 13) {
			sendOptions("Select an Option", new String[] { "Who are you?", "Goodbye." });
			stage = 14;
		} else if(stage == 14) {
			if(buttonId == OPTION1) {
				sendNPCDialogue(npc.getId(), npc.getName(), false, 
					"I am Phileas, the Lumbridge Sage. In times past, people",
					"came from all around to ask me for advice. My renown",
					"seems to have diminished somewhat in recent years,",
					"though. Can I help you with anything?" );
				stage = 2;
			} else {
				sendNPCDialogue(npc.getId(), npc.getName(), false, 
				"Good adventuring, traveller." );
				stage = 8;
			}
		}
		else
			end();
	}

	@Override
	public void finish() {
		
	}
	

}
