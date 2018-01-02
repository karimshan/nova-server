package org.nova.game.player.content.playertask;

import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 *  Player's Task shop reward handling.
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class TaskRewards {
	int rewardInterface = 741,
		rewardTitle = -1;
	
	/*
	 * Current reward
	 */
	String currentReward = null;
	
	/**
	 * Handles the task system rewards.
	 * @param player
	 */
	public static void handleRewards(Player player) {
		int reward = Misc.random(7); 
		switch (reward) {
		/*
		 * Gives random amount of points
		 */
		case 0:
			if (player.isBoostedtasks() == true) {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints +=30;
				player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+". (Boosted)");
			} else {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints +=22;
				player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+".");
			}
			break;
		case 1:
			if (player.isBoostedtasks() == true) {

				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 35;
				player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+". (Boosted)");
			} else {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 15;
			player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+".");
			}
			break;
		case 2:
			if (player.isBoostedtasks() == true) {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 33;
				player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+". (Boosted)");
			} else {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 18;
			player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+".");
			}
			break;
		case 3:
			if (player.isBoostedtasks() == true) {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 21;
				player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+". (Boosted)");
			} else {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 13;
			player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+".");
			}
			break;
		case 4:
			if (player.isBoostedtasks() == true) {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 28;
				player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+". (Boosted)");
			} else {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 19;
				player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+".");
			}
			break;
		case 5:
			if (player.isBoostedtasks() == true) {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 21;
				player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+". (Boosted)");
			} else {
				player.taskpoints +=9;
				player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+".");
			}	break;
		case 6:
			if (player.isBoostedtasks() == true) {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 26;
			player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+". (Boosted)");
		} else {
			player.interfaces().closeChatBoxInterface();
			player.taskName="None";
			player.taskpoints += 17;
		player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+".");
		}
			break;
		case 7:
			if (player.isBoostedtasks() == true) {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 33;
				player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+". (Boosted)");
			} else {
				player.interfaces().closeChatBoxInterface();
				player.taskName="None";
				player.taskpoints += 15;
				player.sm("<col=ff0033>Your task has been completed, current task points: "+player.taskpoints+".");
			}
			break;
			
		}
	}

}
