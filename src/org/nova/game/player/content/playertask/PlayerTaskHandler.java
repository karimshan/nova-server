package org.nova.game.player.content.playertask;

import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 *  Advanced player task system
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class PlayerTaskHandler {
	
	public void checkforCompletedTask(Player player) {
		if (player.isCompletedTask() == true) {
			player.interfaces().closeChatBoxInterface();
			PlayerTaskHandler.cleanStages(player);
			player.burned=0;
			player.fished=0;
			player.cooked=0;
			player.treescutted=0;
			player.logscutted=0;
			player.smithed=0;
			TaskRewards.handleRewards(player);
		} else {
			player.interfaces().closeChatBoxInterface();
			player.sm("<col=ff0033>Task System: You haven't any completed tasks, current task: "+player.taskName+".");
		}
	}
	
	/**
	 * This method just simply checks for tasks, and can be used pretty much anywhere.
	 * *
	 * @return 
	 */
	public static void searchforTask(Player player) {
		
			player.interfaces().closeChatBoxInterface();
			tasks(player); //Starts finding a task. 
	
}
	public void scanCurrentTask(Player player) {
		
	}
	public static void tasks(Player player) {
		int task = 0;
		task = Misc.getRandom(8);
	switch (task) {
	case 5:
		player.taskName = "mine 200 ores";
		resetTaskStages(player);
		sendTaskMessage(player, "<col=ff0033>Task System: Your current task is: "+player.taskName+".");
		player.sm("<col=ff0033>Task System: Your current task is: "+player.taskName+".");
		break;
		//Current task
	case 1:
		player.taskName = "cut 150 logs";
		resetTaskStages(player);
		sendTaskMessage(player, "Your current task is: "+player.taskName+".");
		player.sm("<col=ff0033>Task System: Your current task is: "+player.taskName+".");
		break;
	case 2:
		player.taskName = "fish 225 fishes";
		resetTaskStages(player);
		sendTaskMessage(player, "Your current task is: "+player.taskName+".");
		player.sm("<col=ff0033>Task System: Your current task is: "+player.taskName+".");
		break;
	case 3:
		player.taskName = "burn 130 logs";
		resetTaskStages(player);
		sendTaskMessage(player, "Your current task is: "+player.taskName+".");
		player.sm("<col=ff0033>Task System: Your current task is: "+player.taskName+".");
		break;
	case 4:
		player.taskName = "smith 125 times";
		resetTaskStages(player);
		sendTaskMessage(player, "Your current task is: "+player.taskName+".");
		player.sm("<col=ff0033>Task System: Your current task is: "+player.taskName+".");
		break;
	case 6:
		player.taskName = "cook 325 food";
		resetTaskStages(player);
		sendTaskMessage(player, "Your current task is: "+player.taskName+".");
		player.sm("<col=ff0033>Task System: Your current task is: "+player.taskName+".");
		break;
}
}
	public static void cleanStages(Player player) {
		resetTaskStages(player);
	}
	/**
	 * This method resets all other task stages.
	 * @param player
	 */
	private static void resetTaskStages(Player player) {
		// TODO
	}


	/**
	 * Sends an "Task" Message once task is searched.
	 * @param player
	 * @param MSG
	 */
	public static void sendTaskMessage(Player player, String MSG) {
		player.getMatrixDialogues().startDialogue(
				"SimpleMessage", MSG);
	}

	
	
	
}
