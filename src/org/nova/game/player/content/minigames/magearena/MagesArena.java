package org.nova.game.player.content.minigames.magearena;

import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
/**
 * 
 * @author Fuzen Seth
 *
 */
public class MagesArena {

	/**
	 * The lever animation
	 */
	public Animation LEVER = new Animation(2140);
	
	/**
	 * The private instance of player
	 */
	private static Player player;
	
	/**
	 * The spawning location of Kolodion stages
	 */
	public Location KOLODION_SPAWN = new Location(3200, 3200, 0);

	/**
	 * The kolodion stages NPC id's
	 */
	public int[] KOLODIAN_STAGES = { 907/*stage 1, man*/, 11301, /*stage 2, ogre*/ 11302, /*stage 3, dark beast*/ 911 /*stage 4, dark demon*/};

	/**
	 * Handles the spawning of the next Kolodion stage
	 * @param player 
	 * 		The player fighting Kolodion
	 * @param npc 
	 * 		The npc being spawned
	 */
	
	public static void finish() {
		player.packets().sendMessage("You've finished Kolodion's Task!");
		player.finishedKolodionTask = true;
		player.setLocation(new Location(2538, 4715, 0));
	}
}