package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class Karim extends MatrixDialogue {

	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, 
				"Hello, "+player.getDisplayName()+". How may I help you?"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "I'm hungry, what do you have for sale?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"The Frog Princess sent for you."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {

			stage = 39;
			sendDialogue(SEND_2_OPTIONS, "Select a option",
					"I would like to buy some kebab.", "Nevermind.");
			stage = 3;
		}
		else if (stage == 5) {
		player.interfaces().closeChatBoxInterface();
		if (!player.getInventory().containsItem(995, 10)) {
			player.sendMessage("You must have 10 gold coins to buy kebab.");
			return;
		}
		player.sendMessage("");
		}
		else if (stage == 3) {
			if (componentId == 1) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { player.getDisplayName(), "I would like to buy some kebab." },
				IS_PLAYER, player.getIndex(), 9827);
			}
			else if (componentId == 2) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Nevermind."},
			IS_PLAYER, player.getIndex(), 9827);
			stage = 5;
			}
			} else
			end();
	}

	@Override
	public void finish() {

	}
}