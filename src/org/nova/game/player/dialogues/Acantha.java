package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;
import org.nova.utility.ShopsHandler;

public class Acantha extends MatrixDialogue {
	@Override
	public void start() {
		sendDialogue(SEND_4_OPTIONS, "The mighty Duke",
				"Teleport to Taverly Summoning cave", "View charms store", "Skillcapes store", "Enter Donator's House"); //Change options maybe?
	}
	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_4_OPTIONS){
			if (componentId == 1) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2207, 5346, 0));	
		} else if (componentId == 2){
			ShopsHandler.openShop(player, 30);

			player.interfaces().closeChatBoxInterface();
		} else if (componentId == 3){
			player.interfaces().closeChatBoxInterface();
			ShopsHandler.openShop(player, 32);
		}  else if (componentId == 4){
		player.getDonatorHouse().addAction("Enter house");
		} 
		}
	}
	@Override
	public void finish() {

	}

}