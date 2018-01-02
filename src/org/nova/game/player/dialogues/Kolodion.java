package org.nova.game.player.dialogues;

import org.nova.game.player.Skills;

/**
 * 
 * @author Josh'
 *
 */
public class Kolodion extends MatrixDialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendPlayerDialogue(9827, "Hello there. What is this place?");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendNPCDialogue(npcId, 9827, "I am the great Kolodion, master of battle magic, and",
					"this is my battle arena. Top wizards travel from all over",
					"RuneScape to fight here.");
			stage = 1;
		}
		else if (stage == 1) {
			sendOptionsDialogue("Select an Option", "Can I fight here?", "What's the point of that?", "That's barbaric!");
			stage = 2;
		}
		else if (stage == 2) {
			if (componentId == OPTION_1) {
				sendPlayerDialogue(9827, "Can I fight here?");
				stage = 3;
			}
			else if (componentId == OPTION_2) {
				sendPlayerDialogue(9827, "What's the point of that?");
				stage = 14;
			}
			else if (componentId == OPTION_3) {
				sendPlayerDialogue(9827, "That's barbaric!");
				stage = 16;
			}
		}
		else if (stage == 3) {
			sendNPCDialogue(npcId, 9827, "If you're sure you want in?");
			stage = 4;
		}
		else if (stage == 4) {
			sendOptionsDialogue("Select an Option", "Yes, I do.", "No I don't.");
			stage = 5;
		}
		else if (stage == 5) {
			if (componentId == OPTION_1) {
				sendPlayerDialogue(9827, "Yes, I do.");
				stage = 6;
			}
			else if (componentId == OPTION_2) {
				sendPlayerDialogue(9827, "No I don't.");
				stage = 17;
			}
		}
		else if (stage == 6) {
			sendNPCDialogue(npcId, 9827, "Good, good. You have a healthy sense of competition.");
			stage = 7;
		}
		else if (stage == 7) {
			sendNPCDialogue(npcId, 9827, "Remember, traveller - in my arena, hand-to-hand", 
				"combat is useless. Your strength will dimish if you",
				"enter the arena, but the spells you can learn are",
				"amongst the most powerful in all of RuneScape.");
			stage = 8;
		}
		else if (stage == 8) {
			sendOptionsDialogue("Select an Option", "Okay, let's fight.", "No thanks.");
			stage = 9;
		}
		else if (stage == 9) {
			if (componentId == OPTION_1) {
				sendPlayerDialogue(9827, "Okay, let's fight.");
				stage = 10;
			}
			else if (componentId == OPTION_2) {
				sendPlayerDialogue(9827, "No thanks.");
				stage = 17;
			}
		}
		else if (stage == 10) {
			sendNPCDialogue(npcId, 9827, "I must first check that you are up to scratch.");
			stage = 11;
		}
		else if (stage == 11) {
			sendPlayerDialogue(9827, "You don't need to worry about that.");
			stage = 12;
		}
		else if (stage == 12) {
			sendNPCDialogue(npcId, 9827, "Not just any magician can enter - only the most",
				"powerful and most feared. Before you can use the",
				"power of this arena, you must prove yourself against",
				"me.");	
			stage = 13;
		}
		else if (stage == 13) {
			if (player.getCombatDefinitions().getSpellBook() != 0) {
				sendNPCDialogue(npcId, 9827, "You need to be on Normal Magics to be able to enter the feared magicians guild.");
			}
			if (player.getSkills().getLevel(Skills.MAGIC) > 60) {
				player.getControllerManager().startController("ArenaHandling");
				end();
			} else {
				sendNPCDialogue(npcId, 9827, "I'm sorry "+player.getDisplayName()+", but you need atleast level 60",
						"in magic to enter the feared magicians guild.");
				stage = 99;
			}
		}
		else if (stage == 99) {
			player.getMatrixDialogues().finishDialogue();
		}
		else if (stage == 14) {
			sendNPCDialogue(npcId, 9827, "Whats the point in it? Well, the aim of this minigame is to learn new magic spells",
					"which will help you along the world of rune-scape, in combat.");
			stage = 15;
		}
		else if (stage == 15) {
			sendNPCDialogue(npcId, 9827, "As you progress you can choose a team, your options are; Saradomin, Guthix or Zamorak.",
					"Whatever you choose, you can go back but you will have to complete tasks with that god",
					"you will need to cast 100 magic spells of that type wearing the god cape of your chosen commander.");
			stage = 99;
		}
		else if (stage == 16) {
			sendNPCDialogue(npcId, 9827, "If you don't think this is real, then don't come along with me, but im trying to help you here..");
			stage = 99;
		}
		else if (stage == 17) {
			sendNPCDialogue(npcId, 9827, "If at any time, you feel like you wan't to just pop back and see me!");
			stage = 99;
		}
	}
	
	@Override
	public void finish() {
		
	}
}
