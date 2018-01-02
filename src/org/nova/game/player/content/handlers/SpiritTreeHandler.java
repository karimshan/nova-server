package org.nova.game.player.content.handlers;

import org.nova.game.player.Player;

/**
 *  Spirit Tree Teleportation system
 * @author JazzyYaYaYa | Nexon  | Fuzen Seth
 *
 */
public class SpiritTreeHandler {

public static void handleButtons(Player player, int componentId) {
	switch (componentId) {
	/*
	 * Quit button
	 */
	case 147:
		refreshAll(player);
		player.closeInterfaces();
		player.interfaces().closeChatBoxInterface();
		break;
	/*
	 * Confirm Button
	 */
	case 11:
		Integer location = (Integer) player.getTemporaryAttributtes().get("teleportLocation");
		switch(location) {
		case 1:
			forceCloseAll(player);
			player.getMatrixDialogues().startDialogue("Dungeons");
			break;
		case 2:
		forceCloseAll(player);
		player.getMatrixDialogues().startDialogue("Slayer");
			break;
		case 3:
			forceCloseAll(player);
			player.getMatrixDialogues().startDialogue("Forests");
			break;
		case 4:
			forceCloseAll(player);
			player.getMatrixDialogues().startDialogue("Islands");
			break;
		case 5:
			forceCloseAll(player);
			player.getMatrixDialogues().startDialogue("Bosses");
		break;
		case 6:
			forceCloseAll(player);
			player.getMatrixDialogues().startDialogue("Minigames");
			break;
		case 7:
			forceCloseAll(player);
			player.getMatrixDialogues().startDialogue("Cities");
			break;
		}
		break;
	case 41:
		refreshAll(player);
		player.getTemporaryAttributtes().put("teleportLocation", new Integer(1));
		break;
	case 55:
		player.getTemporaryAttributtes().put("teleportLocation", new Integer(2));
		break;
	case 69:
		player.getTemporaryAttributtes().put("teleportLocation", new Integer(3));
		break;
	case 83:
		player.getTemporaryAttributtes().put("teleportLocation", new Integer(4));
		break;
	case 111:
		player.getTemporaryAttributtes().put("teleportLocation", new Integer(5));
		break;
	case 115:
		player.getTemporaryAttributtes().put("teleportLocation", new Integer(6));
		break;
	case 125:
		player.getTemporaryAttributtes().put("teleportLocation", new Integer(7));
		break;
	}
}

	public static void refreshAll(Player player) {
		player.getTemporaryAttributtes().remove("teleportLocation");
	}

	public static void forceCloseAll(Player player) {
		player.interfaces().closeChatBoxInterface();
		player.closeInterfaces();
	}




	public static void sendTeleports(Player player) {
		int SCREEN = 797;
		player.packets().sendIComponentText(SCREEN, 30,
				"None");
		player.packets().sendIComponentText(SCREEN, 128,
				"Cities");
		player.packets().sendIComponentText(SCREEN, 114,
				"Minigames");
		player.packets().sendIComponentText(SCREEN, 100,
				"Bosses");
		player.packets().sendIComponentText(SCREEN, 86,
				"Islands");
		player.packets().sendIComponentText(SCREEN, 71,
				"Forests");
		player.packets().sendIComponentText(SCREEN,58,
				"Slayer");
		player.packets().sendIComponentText(SCREEN, 44,
				"Dungeons");
		player.packets().sendIComponentText(SCREEN, 30,
				"None");
		/*
		 * Those was titles
		 */
		player.packets().sendIComponentText(SCREEN, 18,
				"Select and travelling location where you want to go.");
		player.packets().sendIComponentText(SCREEN, 153,
				"Spirt Tree Teleportation");
	player.interfaces().sendInterface(SCREEN);
	player.sm("Select an location where you want to travel.");
}
}
