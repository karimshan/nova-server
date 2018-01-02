package org.nova.game.player.content;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.npc.NPC;

/**
 * 
 * @author Fuzen Seth
 * @information Represents Ape a toll dungeon skeletons.
 * @since 22.3.2014
 */
public class MonkeySkeleton {
	
	public static int skeletonMonkey = 1471;
	
	/**
	 * Handles the death.
	 */
	public static final void handleDeath(NPC npc) {
		if (!(npc.getId() == skeletonMonkey))
			return;
		addRespawnTask(npc);
	}

	private static final void addRespawnTask(final NPC npc) {
				WorldTasksManager.schedule(new WorldTask() {
				int loop;
				
				@Override
				public void run() {				
			switch (loop) {
			case 1:
			Game.sendMessage("ENSO: Added two skeleton monkeys.", false);
			Game.spawnNPC(npc.getId(), npc.getLastLocation(), -1, false);
			Game.spawnNPC(npc.getId(), npc.getLastLocation(), -1, false);
			break;
			case 15:
				Game.sendMessage("ENSO: Removed skeleton monkeys.", false);
			
				break;
			}
					loop++;
					}
				}, 0, 1);
	    }	
	
}
