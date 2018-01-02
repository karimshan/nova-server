package org.nova.game.player.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.Item;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;
/**
 * 
 * @author Fuzen Seth
 *
 */
public class SheepShearing {

    public static final Animation SHEARING = new Animation(893);

    public static void shearAttempt(final Player player, final NPC npc) {
    	if (!player.getInventory().hasFreeSlots()) {
    	player.sendMessage("You don't have enough space in your inventory.");
    	
    		return;
    	}
    	if (!player.getInventory().containsItem(1735, 1)) {
	    player.packets().sendMessage("You need a pair of shears in order to sheer the sheep.");
	    return;
	}
		final boolean isBlack = npc.getId() == 8876;
	player.lock(3);
	npc.addFreezeDelay(3000);
	player.setNextAnimation(SHEARING);
	if (Misc.random(5) != 0) {
	    WorldTasksManager.schedule(new WorldTask() {

		@Override
		public void run() {
		    npc.faceEntity(player);
		    player.getInventory().addItem(new Item(isBlack ? 15415 : 1737, 1));
		    npc.setNextNPCTransformation(isBlack ? 8877 : npc.getId() == 43 ? 42 : 5152);
		    player.packets().sendMessage("You get some wool.");
		    setRespawnTask(npc.getId(), npc);
		}
	    }, 2);
	} else {
	    player.packets().sendMessage("The sheep manages to get away from you.");
	    npc.addWalkSteps(player.getX() - 5, player.getX() - 5);
	    if (Misc.random(2) == 0)
		npc.setNextForceTalk(new ForceTalk("Baaa"));
	}
    }
    
    public static void setRespawnTask(final int npcId, final NPC npc) {
			WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {				
		switch (loop) {
		case 35:
			if (npcId == 8877) 
	    		npc.transformIntoNPC(8876);
	    	else
	    		if (npcId== 42  || npc.getName().equals("Sheep")) 
	    		npc.transformIntoNPC(43);
			stop();
			break;
		}
				loop++;
				}
			}, 0, 1);
    }	
    
}
