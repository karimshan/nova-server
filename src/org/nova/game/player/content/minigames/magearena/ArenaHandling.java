package org.nova.game.player.content.minigames.magearena;

import java.util.concurrent.TimeUnit;

import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.map.RegionBuilder;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.controlers.Controller;

/**
 * 
 * @author Josh
 * @author Fuzen Seth (Improved)
 *
 */
public class ArenaHandling extends Controller {

	/**
	 * An Enumeration room bound chunk stages
	 */
	private static enum Stages {
		LOADING,
		RUNNING,
		DESTROYING
	}
	
	/**
	 * The re-spawn/magic bank lobby world tiles
	 */
	private Location LOBBY = new Location(2538, 4715, 0);
	
	/**
	 * The room chunks
	 */
	private static int[] boundChunks;
	
	/**
	 * The Controller stages
	 */
	private static Stages stage;
	
	/**
	 * Handles the start of the controller
	 */
	@Override
	public void start() {
		loadArena();		
	}
	
	/**
	 * Handles the login
	 */
	@Override
	public boolean login() {
		return false;
	}
	
	/**
	 * Handles the sending of death inside the arena
	 * Singling out death events
	 */
	public void sendDeathInsideArena() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.packets().sendMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					player.packets().sendMessage("You have failed Kolodions task!");
					player.setLocation(new Location(2538, 4715, 0));
				} else if (loop == 4) {
					player.packets().sendMusicEffect(90);
					destroyArena();
					player.reset();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	public static void handleHits(Player player, Hit hit) {
		if (hit.getLook() == HitLook.RANGE_DAMAGE 
				|| hit.getLook() == HitLook.MELEE_DAMAGE) {
		hit.setDamage(0);
		player.packets().sendMessage("You can only use Magic against Kolodion!");
		}
	}
	
	public void addKolodion(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.lock(1);
					Game.spawnNPC(907, new Location(player.getX() + 2, player.getY(), player.getZ()), -1, true, true);//2nd stage
					for (NPC npc : Game.getNPCs()) {
						if (npc != null && npc.getId() == 907) {
							npc.setNextForceTalk(new ForceTalk("You must prove yourself... Now!"));
 							player.hints().addHintIcon(npc, 0, false, false);
							npc.setNextGraphics(new Graphics(188));
							player.faceEntity(npc);
						}
					}
				} else if (loop == 1) {
					for (NPC npc : Game.getNPCs()) {
						if (npc != null && npc.getId() == 907) {
							npc.setTarget(player);
						}
					}
					player.unlock();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	/**
	 * Loads a new magic arena in random bound chunks..
	 */
	public void loadArena() {
		stage = Stages.LOADING;
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				boundChunks = RegionBuilder.findEmptyChunkBound(2, 2); 
				RegionBuilder.copyMap(385, 489, boundChunks[0], boundChunks[1], 7, 5, new int[1], new int[1]);
				player.stopAll();
				player.setLocation(new Location(boundChunks[0] * 8 + 24, boundChunks[1] * 8 + 20, 0));
				player.setNextAnimation(new Animation(-1));
				player.packets().sendMessage("You have been teleported into the mages arena.");
				WorldTasksManager.schedule(new WorldTask()  {
					@Override
					public void run() {
						stage = Stages.RUNNING;
						addKolodion(player);
					}
				}, 1);
			}
		});
	}
	
	public void finishTask(Player player) {
		destroyArena();
		player.finishedKolodionTask = true;
		player.packets().sendMessage("You've finished Kolodions task!");
		removeControler();
		player.setLocation(new Location(2538, 4715, 0));
	}
	/**
	 * Handles the log out
	 */
	@Override
	public boolean logout() {
		player.setLocation(LOBBY);
		destroyArena();
		return false;
	}
	
	/**
	 * Stops the player from using magic transporting
	 */
	@Override
	public boolean processMagicTeleport(Location toTile) {
		return false;
	}

	/**
	 * Stops the player from using item transporting
	 */
	@Override
	public boolean processItemTeleport(Location toTile) {
		return false;
	}
	
	/**
	 * Handles the destroying of the map
	 */
	public static void destroyArena() {
		if(stage != Stages.RUNNING) 
			return;
		stage = Stages.DESTROYING;
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(boundChunks[0], boundChunks[1], 8, 8);
			}
		}, 1200, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Handles force closing
	 */
	@Override
	public void forceClose() {
		player.setLocation(LOBBY);
		destroyArena();
	}
}