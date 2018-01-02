package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class Repair extends MatrixDialogue {

	private int npcId;

	
	
 @Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hello, I can your broken equipment,",
						" would you like to?" }, IS_NPC, npcId, 9827);
	}

 @Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "um, Hellz yea!" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendDialogue(SEND_5_OPTIONS,"What would you like to repair?", "Torva full helm",
					"Torva platebody.", "Torva platelegs.", "Torva gloves.", "More Options...");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 1) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(20138, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(20138, 1);
				player.getInventory().addItem(20135, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Torva full helm to repair this.");
			}
			}
			player.closeInterfaces();
			} else if (componentId == 2) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(20142, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(20142, 1);
				player.getInventory().addItem(20139, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Torva platebody to repair this.");
			}
			}
			player.closeInterfaces();
			} else if (componentId == 3) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(20146, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(20146, 1);
				player.getInventory().addItem(20143, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Torva platelegs to repair this.");
			}
			}
			player.closeInterfaces();
			} else if (componentId == 4) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(24979, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(24979, 1);
				player.getInventory().addItem(24977, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Torva gloves to repair this.");
			}
			}
			player.closeInterfaces();
			} else if (componentId == 5) {
				stage = 3;
				sendDialogue(SEND_5_OPTIONS,"What would you like to repair?",
						"Torva boots.", "Pernix cowl.", "Pernix body.",
						"Pernix chaps", "More Options...");
			}
		} else if (stage == 3) {
			if (componentId == 1) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(24985, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(24985, 1);
				player.getInventory().addItem(24983, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Torva boots to repair this.");
			}
			}
			player.closeInterfaces();
			} else if (componentId == 2) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(20150, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(20150, 1);
				player.getInventory().addItem(20147, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Pernix cowl to repair this.");
			}
			}
			player.closeInterfaces();
			} else if (componentId == 3) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(20154, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(20154, 1);
				player.getInventory().addItem(20151, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Pernix body to repair this.");
			}
			}
			player.closeInterfaces();
			} else if (componentId == 4) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(20158, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(20158, 1);
				player.getInventory().addItem(20155, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Pernix chaps to repair this.");
			}
			}
			player.closeInterfaces();
			} else if (componentId == 5) {
				sendDialogue(SEND_5_OPTIONS,"What would you like to repair?", "Pernix gloves",
					"Pernix boots.", "Virtus mask.", "Virtus robe top.", "More Options...");
				stage = 4;
			}
		} else if (stage == 4) {
			if (componentId == 1) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(24976, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(24976, 1);
				player.getInventory().addItem(24974, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Pernix gloves to repair this.");
			}
			}
			} else if (componentId == 2) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(24991, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(24991, 1);
				player.getInventory().addItem(24989, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Pernix boots to repair this.");
			}
			}
			} else if (componentId == 3)
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(20162, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(20162, 1);
				player.getInventory().addItem(20159, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Virtus mask to repair this.");
			}
			}
			else if (componentId == 4) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(20166, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(20166, 1);
				player.getInventory().addItem(20163, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Virtus robe top to repair this.");
			}
			}
			} else if (componentId == 5) {
				stage = 5;
				sendDialogue(SEND_5_OPTIONS,"What would you like to repair?", "Virtus robe legs",
					"Virtus gloves.", "Virtus boots.", "Zaryte bow.", "More Options...");
			}
		} else if (stage == 5) {
			if (componentId == 1) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(20170, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(20170, 1);
				player.getInventory().addItem(20167, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Virtus robe legs to repair this.");
			}
			}
			} else if (componentId == 2) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(24982, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(24982, 1);
				player.getInventory().addItem(24980, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Virtus gloves to repair this.");
			}
			}
			} else if (componentId == 3) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(24988, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(24988, 1);
				player.getInventory().addItem(24986, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Virtus boots to repair this.");
			}
			}
			} else if (componentId == 4) {
			if (player.getInventory().containsItem(995, 10000000)) {
			if (player.getInventory().containsItem(20174, 1)) {
				player.getInventory().deleteItem(995, 10000000);
				player.getInventory().deleteItem(20174, 1);
				player.getInventory().addItem(20171, 1);
			} else {
			player.packets().sendMessage("You need 10 million gold and a broken Zaryte bow to repair this.");
			}
			}
			} else if (componentId == 5) {
				stage = 2;
			sendDialogue(SEND_5_OPTIONS,"What would you like to repair?", "Torva full helm",
					"Torva platebody.", "Torva platelegs.", "Torva gloves.", "More Options...");
			}
		}
	}

 @Override
	public void finish() {

	}
}