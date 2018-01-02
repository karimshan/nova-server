package org.nova.game.player.content.tutorial;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class Start {
	/**
	 * Starts the tutorial.
	 * @param player
	 */
	public static void startTutorial(Player player) {
		addLocationTask(player);
		player.teleportPlayer(3678, 4939, 0);
//	player.setTeleport(true); TODO - Enable upon release.int org.nova.game.
		player.interfaces().sendInterface(897);
		if (player.interfaces().isFullScreen() == true)
		player.getMatrixDialogues().startDialogue("SimpleMessage", "Rotate the camera using the arrow keys until you can see", "the kinght, Sir Vant.", "Right-click on him and select <col=ff0033>'Talk-to'</col> to get started.");
		locateHintIcons(player);
	}
	
	public static void handleButtons(final Player player, int componentId) {
		switch (componentId) {
		case 0:
			player.interfaces().closeChatBoxInterface();
			player.sendMessage("Goodjob, you can speak to Sir Vant now.");
			break;
	}
}

	public static void addLocationTask(final Player player) { 
		if (player.getLocation().equals(new Location(3680,4944,0)) || player.getLocation().equals(new Location(3680,4946,0)) || player.getLocation().equals(new Location(3680,4945,0))) 
		player.addStopDelay(5);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				 if (player.getLocation().equals(new Location(3680,4944,0)) || player.getLocation().equals(new Location(3680,4946,0)) || player.getLocation().equals(new Location(3680,4945,0))) {
					player.stopAll();	
					 player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 8850, "Hey, get back here! I need your help still.");
						player.addWalkSteps(3680, 4942, -1, false);
					}
				loop++;
				}
			}, 0, 1);
}
	
	public static final boolean outsideTiles(Location tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return  (destX >= 3676 && destX <= 3683 && destY >= 4945 && destY <= 3690); //home
	}
	
	public static void locateHintIcons(Player player) {
		
	}
	
	
	public static void searchBag(Player player) {
		player.hints().removeAll();
		player.setNextAnimation(new Animation(832));
		
	}
}
