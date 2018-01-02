package org.nova.game.player.dialogues;

public class Selene extends MatrixDialogue {
	@Override
	public void start() {
		sendDialogue(SEND_4_OPTIONS, "Selena's Services",
				"May I have a teleport crystal?", "May I have a moonclan set?", "Nevermind.", ""); //Change options maybe?
	}
	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_4_OPTIONS){
			if (componentId == 1) {
				if (player.getInventory().containsItem(995, 1250000)) {
					player.sm("You have bought teleportation crystal for 1.25 million coins.");
					player.getInventory().deleteItem(995, 1250000);
					player.getInventory().addItem(6099, 1);
					player.getMatrixDialogues().startDialogue(
							"SimpleMessage", "You have purchased a teleportation crystal.");
				} else {
					player.sm("Teleportation crystal costs 1.25 million gold coins to purchase.");
				}
		} else if (componentId == 2){
		if (player.getInventory().hasFreeSlots()) {
			player.getInventory().addItem(9068, 1);
			player.getInventory().addItem(9069, 1);
			player.getInventory().addItem(9071, 1);
			player.getInventory().addItem(9072, 1);
			player.getInventory().addItem(9073, 1);
			player.getInventory().addItem(9074, 1);
			player.getInventory().addItem(9070, 1);
			player.sm("Selene has given you moonclan set for free.");
		} else {
			player.interfaces().closeChatBoxInterface();
			player.sm("You must have 7 free slots to receive this armour.");
		}
		} else if (componentId == 3){
			player.interfaces().closeChatBoxInterface();
			
		}  else if (componentId == 4){
			player.interfaces().closeChatBoxInterface();
		} 
		}
	}
	@Override
	public void finish() {

	}

}