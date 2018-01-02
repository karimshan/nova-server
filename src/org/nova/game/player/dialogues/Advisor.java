package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class Advisor extends MatrixDialogue {

	private int npcId = 6307;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (!player.isMetAdvisor()) {
			player.setMetAdvisor(true);
		sendEntityDialogue(SEND_4_TEXT_CHAT,
				new String[] { NPCDefinition.get(6307).name,
						"Hey there, "+player.getDisplayName()+".",
						"I'm going to help you all around Nova.",
						"Please click the '?' button to contact me anywhere.", 
						"I will be giving you very helpful information."}, IS_NPC, 6307, 9827);
		} else {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(6307).name,
							"Good day, "+player.getDisplayName()+". Glad to see you again!", 
							"What would you like to talk about?"}, IS_NPC, 6307, 9827);
		}			
	}

	@Override
	public void run(int interfaceId, int componentId) {
	switch (stage) {
	case -1:
		sendDialogue(SEND_5_OPTIONS, "Select a option",
				"I want to discuss about making money.", "I want to know where to teleport around the world.", "What's task programme, Advisor?",
				"How can I start a quest right now?", "Nevermind, I had nothing to say.");
		stage = 2;
			break;
	case 3:			
		sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { NPCDefinition.get(6307).name,
			  "Once you have some bigger combat stats,",
			 "Killing bosses is a good way of getting big cash pile."}, IS_NPC, 6307, 9827);
		stage = -1;
		break;
	case 2:
		switch (componentId) {
		case 1:
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(6307).name,
		"The raw-resources are boosted such as:",
		"Ores, woods, planks, bars and so on.", 
		"Training you skills is great way of making money."}, IS_NPC, 6307, 9827);
			stage = 3;
			break;
		case 2:
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(6307).name,
		"You can teleport around the world, by using",
		"The 'Spirit tree' which is located in the Grand Exchange.", 
		"If you also have completed quests you can talk to Mason.", 
		"Mason can teleport you to unique areas in Nova."}, IS_NPC, 6307, 9827);
			stage = -1;
			break;
		case 3:
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(6307).name,
		"With the task programme you can earn task points.",
		"Task points can be spent in the store.", 
		"You can access there by clicking the ",
		"'My character' -button in the tasks tab, "+player.getDisplayName()+"."}, IS_NPC, 6307, 9827);
			stage = -1;	
			break;
		case 4:
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(6307).name,
		"You can start a quest by clicking the 'Quests log' -button.",
		"Inside the tasks tab. Select a quest you want to", 
		"complete and quest tab will appear with instructions."}, IS_NPC, 6307, 9827);
			stage = -1;	
			break;
		case 5:
			end();
			player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 6307,"See you again soon, "+player.getDisplayName()+"!");
			break;
		}
	}
}

	@Override
	public void finish() {

	}
}