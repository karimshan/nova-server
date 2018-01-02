package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;
import org.nova.game.player.content.WildernessHelmets;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class WildyHats extends MatrixDialogue {

	public WildyHats() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Select a Option",
				"Show my wilderness statistics.", "I want to reset my wilderness statistics.", "Nevermind.",
				"", "");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1)
				WildernessHelmets.openStatistics(player);
			else if (componentId == 2)
				WildernessHelmets.resetProgress(player);
			else if (componentId == 3)
				player.interfaces().closeChatBoxInterface();
			else if (componentId == 4)
				player.interfaces().closeChatBoxInterface();
			else if (componentId == 5) {
				player.interfaces().closeChatBoxInterface();
			}
	} else if (stage == 32) {
		if (componentId == 1)
			TeleportManager.SpiritTeleport(player, 0, 0, new Location(2905, 5203, 0));
		else if (componentId == 2)
			TeleportManager.SpiritTeleport(player, 0, 0, new Location(2562,
					5739, 0));
		else if (componentId == 3)
			TeleportManager.SpiritTeleport(player, 0, 0, new Location(2285,
					4694, 0));
		else if (componentId == 4)
			TeleportManager.SpiritTeleport(player, 0, 0, new Location(2966,
					4383, 0));
		else if (componentId == 5) {
			stage = 35;
			sendDialogue(SEND_5_OPTIONS, "Boss teleports",
					"Bork", "Glacors", "Kalphite Hive",
					"None", "Nevermind");
		}
	}
		else if (stage == 35) {
			if (componentId == 1)
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3112, 5531, 0));
			else if (componentId == 2)
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(4188,
						5718, 0));
			else if (componentId == 3)
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(3471,
						9497, 0));
			else if (componentId == 4)
				player.interfaces().closeChatBoxInterface();
			else if (componentId == 5) {
				player.interfaces().closeChatBoxInterface();
			}
	}
		}

	private Object WildernessHats() {
		// TODO Auto-generated method stub
		return null;
	}

	public void finish() {
	}

	private int npcId;
}
