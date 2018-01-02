package org.nova.game.player.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.game.player.content.quests.QuestBind;

/**
 * 
 * @author JazzyYaYaYa  | Nexon | Fuzen Seth
 *
 */
public class HoarfrostHollow {
	/*
	 * Interface-sided teleportation
	 */
	
	public static void tunnelEntrance(Player player) {
		
	}
	
	
	public static void HoarfrostTeleport(final Player player, int i, int j, final Location tile) {
		if (!player.getControllerManager().processItemTeleport(tile))
			return;
			WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
				public void run() {
	player.addStopDelay(3);
	player.interfaces().sendOverlay(117, false);
			Location teleTile = tile;
			teleTile = new Location(tile, 2);
			teleTile = tile;						
				if (loop == 0) {

				} else if (loop == 5) {
				player.setLocation(teleTile);
				} else if (loop == 6) {
				player.interfaces().closeChatBoxInterface();
				player.interfaces().closeScreenInterface();
				player.closeInterfaces();
				player.sm("You have arrived to Crandor...");
				player.unlock();
				stop();
				}
				loop++;
				}
			}, 0, 1);
		}

	public static void finish(Player player) {
		if (player.getInventory().containsItem(10973, 1)) {
			player.teleportPlayer(3164, 3480, 0);
			QuestBind.CleanTexts(player);
			player.questpoints = +16;
			player.setCompletedHoarfrost();
			player.sm("<col=ff0033>Quest System: Battle Quest Hoarfrost Hollow has been completed.");
			player.packets().sendIComponentText(277, 3,
					"Hoarfrost Hollow Completed!");
			player.packets().sendIComponentText(277, 4,
					"Reward(s):");
			player.packets().sendIComponentText(277, 9,
					"Korasi's Sword");
			player.packets().sendIComponentText(277, 10,
					"Ability to change spellbook.");
			player.packets().sendIComponentText(277, 11,
					"Ability to change prayer book.");
			player.packets().sendIComponentText(277, 12,
					"");
			player.packets().sendIComponentText(277,13,
					"");
			player.packets().sendIComponentText(277, 14,
					"");
			player.packets().sendIComponentText(277, 6,
					"Quest Points; "+player.questpoints+"/350");
			player.packets().sendIComponentText(277, 7,
					"");
			player.packets().sendIComponentText(277, 15,
					"");
			player.packets().sendIComponentText(277, 16,
					"");
			player.packets().sendIComponentText(277, 17,
					"");
			player.getInventory().addItem(19784, 1);
			player.interfaces().sendInterface(277);
			
		} else {
			player.sm("Defeat the ice demon and get an light orb from him to continue.");
		}
	}
	
	public static void enterBossRoom(Player player) {
		if (!player.isCompletedHoarfrost()) {
		player.teleportPlayer(3413, 4369, 0);
		player.sm("Defeat the ice demon and once you get magical orb move in to tunnel.");
	}
	}
}
