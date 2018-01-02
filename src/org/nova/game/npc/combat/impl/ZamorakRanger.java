package org.nova.game.npc.combat.impl;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Hit;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 * @information Combat script written by Fuzen Seth.
 * @since 23.6.2014 
 */
public class ZamorakRanger extends CombatScript {

	  public Object[] getKeys()
	    {
	        return (new Object[] {
	            Integer.valueOf(6365)
	        });
	    }
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = Misc.getRandom(defs.getMaxHit());
	
		if(target.withinDistance(npc, 4)) {	
			npc.setNextAnimation(new Animation(426));
		Game.sendProjectile(npc, target,12, 41, 36,
				20, 35, 16, 0);
			delayHit(npc, 1, target, new Hit[] {
	                getRangeHit(npc, hit)
	            });
			
		}
		return defs.getAttackDelay();
	}

}