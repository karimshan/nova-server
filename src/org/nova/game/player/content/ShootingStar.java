package org.nova.game.player.content;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.utility.misc.Misc;

public class ShootingStar {
	
	/**
	 * @author Jake | Santa Hat @Rune-Server
	 */
	
	/**
	 * The Player's Constructor
	 */
private transient Player player;
	
	public ShootingStar(Player player) {
		this.player = player;
	}
	
	/**
	 * Total Star Dust Mined
	 */
	public static short stardustMined;
	
	/**
	 * Used For Getting The Star's Stage.
	 */
	public static byte stage = 8;
	
	/**
	 * Increases The Star's Stage
	 */
	public static int starSize = 38661;
	
	/**
	 * Shooting star crash locations.
	 */
	public final static Location[] LOCATION =  { 
		new Location(3031, 3347, 0), // Behind Falador Bank
		new Location(2974, 3238, 0), // Rimmington Mine
		new Location(3245, 3509, 0) // Varrock Lodestone
		};
	
	/**
	 * Used To Save The Star's Location.
	 */
	private static Location lastTile = LOCATION[Misc.random(0, 3)];
	
	/**
	 * Increases The X Position Of The Star.
	 */
	private int starX = lastTile.getX();
	
	/**
	 * Spawn Random Crashed Star
	 */
	public static void spawnRandomStar() {
		Game.spawnObject(new GlobalObject(38660, 10, 0 , lastTile), true);
		Game.sendMessage("<img=7><col=ff0000>News: A Shooting Star has just crashed!", false);
	}
	
	/**
	 * Check's If The Player Can Mine.
	 */
	public boolean mineCrashedStar() {
		if (player.getSkills().getLevel(Skills.MINING) < stage * 10) {
			player.packets().sendMessage("You need a mining level of at least 10 times the star's stage.");
			return false;
		} else {
			processMining();
		}
		return true;
	}
	/**
	 * Spawns The Shooting Star Every 1200 Seconds.
	 */
	public static void spawnStar() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 12) {
					ShootingStar.stage = 8;
					ShootingStar.stardustMined = 0;
					ShootingStar.spawnRandomStar();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	/**
	 * Removes The Star Sprite After 50 Seconds.
	 */
	public static void removeStarSprite(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 50) {
					for (NPC n : Game.getNPCs()) {
						if (n == null || n.getId() != 8091)
							continue;
						n.sendDeath(n); //Removes the Star Sprite.
						spawnStar(); //Spawns Another Star.
					}
				}
				loop++;
			}
		}, 0, 1);
	}
	/**
	 * Check's Who Was The First Person To Find The Crashed Star.
	 */
	public void checkIfFirst() {
		for (Player players : Game.getPlayers()) {
			if (player.taggedStar == false) {
				player.getSkills().addXp(Skills.MINING, player.getSkills().getLevel(Skills.MINING) * 75);
				player.packets().sendMessage("Congratulations, You were the first to reach the shooting star!");
				players.taggedStar = true;
			}
		}
	}
	
	/**
	 * Increases The Crashed Star's Stage.
	 */
	public void processMining() {
			if (stardustMined == 33 || stardustMined == 50 || stardustMined == 68
					|| stardustMined == 86 || stardustMined == 116 || stardustMined == 147
						|| stardustMined == 160) {
				starSize++;
				stage--;
				Game.spawnObject(new GlobalObject(-1, 10, 0 , starX, lastTile.getY(), lastTile.getZ()), true); // Deletes The Pre-Existing Star
				starX++;
				Game.spawnObject(new GlobalObject(starSize, 10, 0 , starX, lastTile.getY(), lastTile.getZ()), true); // Spawns A New Star.
				return;
			} else if (stardustMined >= 200) { //Fully Mined
				starX++;
				Game.spawnObject(new GlobalObject(-1, 10, 0 , starX, lastTile.getY() + 9, lastTile.getZ()), true); //Delete's The Last Star
				starSize = 38661; // Object ID
				stage = 8; // The Size Of The Star (Stage)
				player.stopAll();
				player.canTalkToSprite = true; // So Player's Can Only Get Reward Once.
				Game.spawnNPC(8091, lastTile, -1, true, true);
				/*
				 * Resets The Tag
				 */
				for (Player players : Game.getPlayers()) {
					players.taggedStar = false;
				}
				return;
			}
		}

}