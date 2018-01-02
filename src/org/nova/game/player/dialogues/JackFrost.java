package org.nova.game.player.dialogues;

import org.nova.game.player.content.handlers.SpiritTreeHandler;

public class JackFrost extends MatrixDialogue {
	@Override
	public void start() {
		sendDialogue(SEND_4_OPTIONS, "Choose a option",
				"I want to restore my special attack.", "I would like to open my bank.", "I want to view the teleports.", "Nevermind, I don't want to do anything."); //Change options maybe?
	}
	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_4_OPTIONS){
			if (componentId == 1) {
				player.getDonatorHouse().addAction("Restore special");
		} else if (componentId == 2){
			player.getDonatorHouse().addAction("Open bank");
		} else if (componentId == 3){
			player.interfaces().closeChatBoxInterface();
			SpiritTreeHandler.sendTeleports(player);
			}  else if (componentId == 4){
	player.closeInterfaces();
	player.interfaces().closeChatBoxInterface();
		} 
		}
	}
	@Override
	public void finish() {

	}

}