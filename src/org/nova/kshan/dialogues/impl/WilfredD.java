package org.nova.kshan.dialogues.impl;

import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class WilfredD extends Dialogue {
	
	NPC npc;
	
	@Override
	public void start() {
		npc = (NPC) data[0];
		sendOptions(TITLE, new String[] { 
			"Who are you?", "What is that cape you're wearing?", "Ask about something else." });
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		if(stage == 0) {
			if(buttonId == OPTION1_OTHER) {
				sendPlayerDialogue(false, "Who are you?");
				stage = 1;
			} else if(buttonId == OPTION2_OTHER) {
				sendPlayerDialogue(false, "What is that cape you're wearing?");
				stage = 3;
			} else if(buttonId == OPTION3_OTHER) {
				sendOptions("Can you tell me about...", new String[] { "Tell me about Magic trees.", 
					"Can I have a hatchet?", "Nevermind." });
				stage = 4;
			}
		} else if(stage == 1) {
			sendNPCDialogue(npc.getId(), npc.getName(), false,
				"My name is Wilfred and I'm the best woodsman in",
				"Asgarnia! I've spent my life studying the best methods for", 
				"woodcutting. That's why I have this cape, the Skillcape of", 
				"Woodcutting.");
			stage = 10;
		} else if(stage == 3) {
			sendNPCDialogue(npc.getId(), npc.getName(), false, 
				"This is a Skillcape of Woodcutting. Only a person who has",
				"achieved the highest possible level in a skill can wear one.");
			stage = 10;
		} else if(stage == 4) {
			if(buttonId == OPTION1_OTHER) {
				sendNPCDialogue(npc.getId(), npc.getName(), false, 
					"Magic trees require level 75 woodcutting to chop", 
					"and give you the best woodcutting experience.", 
					"Magic trees can be found all around the world,", 
					"and can easily be chopped down for money as well!");
				stage = 10;
			} else if(buttonId == OPTION2_OTHER) {
				if(player.getInventory().containsItem(1351, 1) || player.getBank().containsItem(1351, 1)) {
					sendNPCDialogue(npc.getId(), npc.getName(), false, 
						"It looks like you already have a bronze hatchet!", 
						"Please check your inventory or bank for a", 
						"bronze hatchet.");
					stage = 10;
				} else {
					sendNPCDialogue(npc.getId(), npc.getName(), false, 
						"Why yes, you may certainly have one!", 
						"I will give you a bronze hatchet to start", 
						"out, but over time you will find out that", 
						"higher level hatchets get you more logs!");
					stage = 5;
				}
			} else
				end();
		} else if(stage == 5) {
			sendItemDialogue(new Item(1351, 1), "Wilfred hands you a bronze hatchet!");
			if(!player.getInventory().hasFreeSlots()) {
				Game.addGroundItem(new Item(1351, 1), new Location(player), player, false, 180, true);
				player.sm("Your bronze hatchet has been placed on the ground.");
			} else
				player.getInventory().addItem(new Item(1351, 1));
			stage = 10;
		}
		else
			end();
	}

	@Override
	public void finish() {

	}

}
