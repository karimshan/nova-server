package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class Bosses extends MatrixDialogue {

	public Bosses() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Boss Teleports",
				"Godwars: Bandos Room", "Godwars: Zamorak Room", "Godwars: Saradomin Room",
				"Godwars: Armadyl Room", "Next page");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {

				player.getControllerManager().startController("GodWars");
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2870, 5363, 2));
			}
			else if (componentId == 2){

				player.getControllerManager().startController("GodWars");
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2925, 5330, 2));
			}
			else if (componentId == 3){

				player.getControllerManager().startController("GodWars");
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2901, 5264, 0));
				}
			else if (componentId == 4){

				player.getControllerManager().startController("GodWars");
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2838, 5297, 2));
			}
			else if (componentId == 5) {
				stage = 32;
				sendDialogue(SEND_5_OPTIONS, "Boss teleports",
						"Zaros God: Nex", "Tormented Demons", "King Black Dragon",
						"Corporeal Beast", "Next page");
			}
	} else if (stage == 32) {
		if (componentId == 1) 	{
				player.getControllerManager().startController("GodWars");
			TeleportManager.SpiritTeleport(player, 0, 0, new Location(2905, 5203, 0));
	}
		else if (componentId == 2)
			TeleportManager.SpiritTeleport(player, 0, 0, new Location(2562,
					5739, 0));
		else if (componentId == 3)
			TeleportManager.SpiritTeleport(player, 0, 0, new Location(2285,
					4694, 0));
		else if (componentId == 4)
			TeleportManager.SpiritTeleport(player, 0, 0, new Location(2975,4386,2));
		else if (componentId == 5) {
			stage = 35;
			sendDialogue(SEND_5_OPTIONS, "Boss teleports",
					"Bork", "Glacors", "Kalphite Hive",
					"Skeletal Horror's Cave", "Nevermind");
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
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2807,
						10105, 0));
			else if (componentId == 5) {
				player.interfaces().closeChatBoxInterface();
			}
	}
		}

	public void finish() {
	}

	private int npcId;
}
