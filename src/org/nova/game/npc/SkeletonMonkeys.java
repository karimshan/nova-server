package org.nova.game.npc;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @since 29.12.2013
 * @information This needs more work. Used to handle the skeleton monkeys.
 */
public class SkeletonMonkeys {
	/**
	 * The monkeys to respawn.
	 */
	public static final int[] SKELETON_MONKEYS = {};
	/**
	 * Respawns the skeleton monkeys.
	 * @param player
	 * @param skeletonMonkey
	 */
	public static final void respawn(final Player player, NPC skeletonMonkey) {
		
final NPC Skeleton = new NPC(2474, new Location(player.getX() + 1, player.getY(), player.getZ()),-1, true);
	Game.addNPC(Skeleton);
	Game.addNPC(Skeleton);
	WorldTasksManager.schedule(new WorldTask() {
		int loop;
		
		@Override
		public void run() {
		switch (loop) {
		case 0:
			break;
		case 50:
			/**
			 * We remove the 2 spawns if they still exist.
			 */
			Game.removeNPC(Skeleton);
			Game.removeNPC(Skeleton);
			stop();
			break;
			}
			loop++;
			}
		}, 0, 1);
	}
	
}
