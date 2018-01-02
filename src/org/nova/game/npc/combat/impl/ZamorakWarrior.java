package org.nova.game.npc.combat.impl;

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
 *
 */
public class ZamorakWarrior extends CombatScript {

	  public Object[] getKeys()
	    {
	        return (new Object[] {
	            Integer.valueOf(6363)
	        });
	    }
	//Melee Emote-12696
	//Mage Emote-9300
	//Range Gfx-451
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = Misc.getRandom(defs.getMaxHit());
	
		if(target.withinDistance(npc, 1)) {
			npc.setNextAnimation(new Animation(12311));
			delayHit(npc, 1, target, new Hit[] {
	                getMeleeHit(npc, hit)
	            });
		}
		return defs.getAttackDelay();
	}

}