package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class RottenPotatoD extends MatrixDialogue {

	public RottenPotatoD() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Select a option", "Log me out.", "Teleport me to somewhere.", "Let's get maxed!", "Mute a target.", "Ban a target.");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {
				player.logout();
				player.interfaces().closeChatBoxInterface();
			}
			else if (componentId == 2) {
				player.setLocation(new Location(2833, 3860, 0));
				player.interfaces().closeChatBoxInterface();
			}
			else if (componentId == 3) {
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2884,
						9799, 0));	
				player.interfaces().closeChatBoxInterface();
			}
			else if (componentId == 4) {
				/**
				 * TODO
				 */
			player.interfaces().closeChatBoxInterface();
			}
			else if (componentId == 5) {
				/**
				 * TODO
				 * 
				 */
				player.interfaces().closeChatBoxInterface();
			}
		}
		
		}

	public void finish() {
	}

	private int npcId;
}
