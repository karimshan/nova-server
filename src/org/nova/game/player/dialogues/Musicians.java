package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.actions.MusicanRest;
import org.nova.utility.misc.Misc;

public class Musicians extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, 
				"Good day, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case 1:	
			sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { player.getDisplayName(), "Hey there, it's a shiny day." },
				IS_PLAYER, player.getIndex(), 9827);
				stage = 2;
			break;
		case 2:	
			sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, 
				"For sure it is, awesome day to play some music!"}, IS_NPC, npcId, 9827);
		stage = 3;
			break;
		case 3:
			sendDialogue(SEND_5_OPTIONS,"Choose a option",
					"Could you play me some music?", "Why are you here?", "Nevermind, I had nothing to say actually.", "", "");
			stage = 4;
			break;
		case 4:
			switch (componentId) {
			case 1:
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "Could you play me some music?" },
						IS_PLAYER, player.getIndex(), 9827);
						stage = 10;
				break;
			case 2:	
				sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Why are you here?" },
					IS_PLAYER, player.getIndex(), 9827);
					stage = 14;
				break;
			case 3:
				player.interfaces().closeChatBoxInterface();
				break;
			case 4:
				player.interfaces().closeChatBoxInterface();
				break;
			case 5:
				player.interfaces().closeChatBoxInterface();
				break;
			}
			break;
		case 10:
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Of course, "+player.getDisplayName()+". Let's begin!"}, IS_NPC, npcId, 9827);
			stage = 22;
		break;
		case 14:
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,  "It's a nice day, i'm here to play music." },
					IS_PLAYER, player.getIndex(), 9827);
					stage = 3;
			break;
		
		
		
		
		//Ends for the rest.
		case 22:
			long currentTime = Misc.currentTimeMillis();
			if (player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
				player.packets().sendMessage(
						"You can't rest while perfoming an emote.");
				return;
			}
			if (player.getStopDelay() >= currentTime) {
				player.packets().sendMessage(
						"You can't rest while perfoming an action.");
				return;
			}
			player.stopAll();
			player.getActionManager().setSkill(new MusicanRest());
			break;
		}
	}

	@Override
	public void finish() {

	}
}