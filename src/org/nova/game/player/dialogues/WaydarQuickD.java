package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;

public class WaydarQuickD extends MatrixDialogue {
	@Override
	public void start() {
		sendDialogue(SEND_2_OPTIONS, SEND_DEFAULT_OPTIONS_TITLE,
				"Travel back to Crash Island.", "No, I don't want to travel anywhere.");
	}
	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_2_OPTIONS){
			if (componentId == 1) {
				TeleportManager.WaydarTeleport(player, 0, 0, new Location(2892, 2725, 0));
		} else if (componentId == 2){
			player.interfaces().closeChatBoxInterface();
		} 
		}
	}
	@Override
	public void finish() {

	}

}