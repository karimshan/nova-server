package org.nova.game.player.content.handlers;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth Skype: NexonLTD
 *
 */
public class SpiritTree {

	private static int teleportType;

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 147:
			player.closeInterfaces();
			player.interfaces().closeChatBoxInterface();
			break;
		case 11:
			switch (teleportType) {
			case 1:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("Dungeons");
				break;
			case 2:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("Slayer");
				break;
			case 3:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("Forests");
				break;
			case 4:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("Islands");
				break;
			case 5:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("Bosses");
				break;
			case 6:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("Game");
				break;
			case 7:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("Cities");
				break;
			}
			break;

		case 28:
			setTeleportType(0);
			break;
		case 41:
			setTeleportType(1);
			break;
		case 55:
			setTeleportType(2);
			break;
		case 69:
			setTeleportType(3);
			break;
		case 83:
			setTeleportType(4);
			break;
		case 97:
			setTeleportType(5);
			break;
		case 111:
			setTeleportType(6);
			break;
		case 125:
			setTeleportType(7);
			break;

		}
	}

	public static void forceCloseAll(Player player) {
		player.interfaces().closeChatBoxInterface();
		player.closeInterfaces();
	}

	public static void sendTeleports(Player player) {
		int SCREEN = 797;
		player.packets().sendIComponentText(SCREEN, 30, "None");
		player.packets().sendIComponentText(SCREEN, 128, "Cities");
		player.packets().sendIComponentText(SCREEN, 114, "Minigames");
		player.packets().sendIComponentText(SCREEN, 100, "Bosses");
		player.packets().sendIComponentText(SCREEN, 86, "Islands");
		player.packets().sendIComponentText(SCREEN, 71, "Forests");
		player.packets().sendIComponentText(SCREEN, 58, "Slayer");
		player.packets().sendIComponentText(SCREEN, 44, "Dungeons");
		player.packets().sendIComponentText(SCREEN, 30, "None");
		/*
		 * Those was titles
		 */
		player.packets().sendIComponentText(SCREEN, 18,
				"Select a location where you would like to travel.");
		player.packets()
				.sendIComponentText(SCREEN, 153, "Pick a Teleportation");
		player.interfaces().sendInterface(SCREEN);
		player.sm("Select an location where you want to travel.");
	}

	public int getTeleportType() {
		return teleportType;
	}

	public static void setTeleportType(int teleportType) {
		SpiritTree.teleportType = teleportType;
	}
}
