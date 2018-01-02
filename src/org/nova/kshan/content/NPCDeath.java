package org.nova.kshan.content;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.map.GlobalObject;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.kshan.content.areas.c_o_r.C_O_R_Data;
import org.nova.kshan.content.skills.slayer.SlayerTask;

/**
 * A class that contains instructions as to what happens when an NPC dies.
 * 
 * @author Shan
 *
 */
public class NPCDeath {
	
	/**
	 * 
	 * @param npc
	 * @param player
	 */
	public static void onDeath(NPC npc, Player player) {
		// Slayer Task
		if(player.slayerTask().amountLeft() > 0)
			if (npc.defs().name.equals(player.slayerTask().monsterName()))
				SlayerTask.sendDeath(player, npc);
		// For Cavern of Remembrance
		for(GlobalObject o : C_O_R_Data.getBossObjects())
			if(o.getName().toLowerCase().contains(npc.getName().toLowerCase()))
				player.getCavernOfRemembrance().addBossKilled(npc);
		// Individual NPC IDs
		switch(npc.getId()) {
			case 1265: // Rock Crab
				for(GlobalObject o : Game.getLocalObjects(player, 10)) {
					if(o.getName().toLowerCase().contains("soul")) {
						Game.sendProjectile(npc, npc, o, 2263, 
							30, 10, 24, 0, 5, 0);
						Game.submit(new GameTick(1.8) {
							public void run() {
								Game.sendGraphics(npc, new Graphics(388), o);
								stop();
							}
						});
					}
				}
				break;
			default:
				break;
		}
	}

}
