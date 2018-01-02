package org.nova.game.player.content.playertask;

import org.nova.game.player.Player;

public class TaskManager {
static int taskInterface = 1027;

public static void checkCookingTask(Player player) {
	
}

public static void checkSmithingTask(Player player) {
	
}

public static void checkFishingTask(Player player) {
	
}
public static void checkFiremakingTask(Player player) {
	
}

	public static void searhforCompletedTask(Player player) {
		
	}
	
	/*
	 * Removes an active / Completed task
	 */
	public static void taskComplete(Player player) {
		
	}
	
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 27:
			player.closeInterfaces();
			player.teleportPlayer(3135, 3516, 0);
			break;
		}
	}
	
	private static void sendTexts(Player player) {
		player.packets()
		.sendIComponentText(taskInterface, 21,
				"Task Completed!");
		player.packets()
		.sendIComponentText(taskInterface, 24,
				"You have completed your current task, you can teleport straight to Elof which will be checking out the current task that you have done.");
		player.packets()
		.sendIComponentText(taskInterface, 27,
				"Teleport");
	}
	
	public static void removeTask(Player player) {
		player.setCompletedTask(false); //<-- Just incase
		PlayerTaskHandler.cleanStages(player);
		player.sm("<col=ff0033>Task System: Your task has been cancelled.");
	}
	
	public static void checkMiningTask(Player player) {
		
	}
	
	public static void checkWoodcuttingTask(Player player) {
		
	}
	
}
