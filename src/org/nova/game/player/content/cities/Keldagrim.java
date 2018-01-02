package org.nova.game.player.content.cities;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.content.quests.Wanted;
/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class Keldagrim {
	
	
	/**
	 * Keldagrim's NPC actions.
	 * @param player
	 * @param npc
	 */
	public static void handleDialogueActions(Player player, NPC npc) {
		switch (npc.getId()) {
		case 1:
			break;
	}
}
	/**
	 * Keldagrim's object actions.
	 * @param player
	 * @param object
	 */
	public static void handleObjectActions(Player player, GlobalObject object) {
	switch (object.getId()) {
	case 1:
		
	break;
	}
	}
	
	public static void shipTeleport(final Player player, int i, int j, final Location tile) {
		if (!player.getControllerManager().processItemTeleport(tile))
			return;
			player.addStopDelay(6);
		player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 5883, "I will be taking you to Keldagrim in a cart.", "Enjoy the journey, "+player.getDisplayName()+".");
			WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
			Location teleTile = tile;
			teleTile = new Location(tile, 2);
			teleTile = tile;						
				if (loop == 0) {
				player.interfaces().sendInterface(120);
				} else if (loop == 2) {
				player.setLocation(teleTile);
				} else if (loop == 3) {
				player.closeInterfaces();
				player.sendMessage("You have succesfully reached Keldagrim.");
				Wanted.ShowBind(player);
				player.setWantedStage(5);
				stop();
				}
				loop++;
				}
			}, 0, 1);
		}
	
	public static void keldagrimTeleport(final Player player, int i, int j, final Location tile) {
		if (!player.getControllerManager().processItemTeleport(tile))
			return;
			player.addStopDelay(6);
			WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
			player.interfaces().closeChatBoxInterface();
			Location teleTile = tile;
			teleTile = new Location(tile, 2);
			teleTile = tile;						
				if (loop == 0) {
				player.interfaces().sendInterface(120);
				} else if (loop == 2) {
				player.setLocation(teleTile);
				} else if (loop == 3) {
				player.closeInterfaces();
				stop();
				}
				loop++;
				}
			}, 0, 1);
		}
	public static final boolean handleObjectInteractions(Player player, GlobalObject gameObject) {
		switch (gameObject.getId()) {
		case 56990: //Leaving Keldagrim.
			player.setLocation(new Location(2877,10119,0));
			return true;
		case 57139:
			if (!player.isCompletedWanted()) {
				player.sendMessage("It's too scary to enter in this tunnel, maybe I should get someone to travel me in to Keldagrim.");
			return false;
			}
			player.setLocation(new Location(2939,10197,0));
			return true;
		case 6108:
			player.sendMessage("This door seems to be locked.");
			return true;
		}
		return false;
	}
	public static void enter(Player player) {
		if (player.isCompletedWanted()) {
			player.setLocation(new Location(2773, 10162,0));
			player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 1841, "Welcome to Keldagrim, "+player.getDisplayName()+".");
		}
			if (player.getWantedStage() >= 2)  {
			player.setLocation(new Location(2773, 10162,0));
			player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 1841, "Find a route to Keldagrim next.");
			player.setWantedStage(4);
		}
	}
}
