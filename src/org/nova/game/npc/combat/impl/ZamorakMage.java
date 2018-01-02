package org.nova.game.npc.combat.impl;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 * 
 */
public class ZamorakMage extends CombatScript {

	  public Object[] getKeys()
	    {
	        return (new Object[] {
	            Integer.valueOf(6371)
	        });
	    }
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = Misc.getRandom(defs.getMaxHit());
	
		if(target.withinDistance(npc, 1)) {
			npc.setNextGraphics(new Graphics(2728));
			npc.setNextAnimation(new Animation(14223));
			Game.sendProjectile(npc, target, 2731, 18, 18, 50, 50, 0, 0);
			delayHit(npc, 1, target, new Hit[] {
	                getMagicHit(npc, hit)
	            });
			
		}
		return defs.getAttackDelay();
	}

}