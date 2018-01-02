package org.nova.kshan.dialogues.impl;

import java.util.Random;

import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.input.type.integer.IntegerInput;

/**
 * 
 * @author K-Shan
 *
 */
public class Gambler extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (int) data[0];
		npc(npcId, "Hey there " + player.getDisplayName()
				+ ", are you feeling lucky today?");
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		if (stage == 0) {
			sendOptions(TITLE, "Of course!", "No, not really!");
			stage = 1;
		} else if (stage == 1) {
			if (buttonId == OPTION1) {
				npc(npcId,
						"You can bet me tax bags! Choose how many you'd",
						"like to bet!");
				stage = 2;
			} else
				end();
		} else if (stage == 2) {
			end();
			player.getTemporaryAttributtes().put("betting", 0);
			player.getInputEvent().run(new IntegerInput() {

				@Override
				public void process(int input) {
					int bet = input;
					if (bet <= 0 || bet > 5) {
						player.sm("Your bet can only be up to 5 tax bags.");
						return;
					}
					if (bet > player.getInventory().getAmountOf(5607)) {
						player.sm("You don't have that many tax bags in your inventory.");
						return;
					}
					if (player.getInventory().getFreeSlots() < 10) {
						player.sm("You need at least 10 free inventory spaces before betting.");
						return;
					}
					int random = new Random().nextInt(100); // Set value of 100
					if (random <= 30) { // 30%
						player.sm("Congratulations, you've won the bet!");
						player.getInventory().addItem(5607, (bet * 2));
					} else {
						player.getInventory().deleteItem(5607, bet);
						player.sm("Unfortunately, you've lost the bet.");
					}
				}

				@Override
				public void whileTyping(int key, char keyChar, boolean shiftHeld) {
					
				}
			}, "Choose how many tax bags you'd like to bet (Max 5):");
		}
	}

	@Override
	public void finish() {

	}

}
