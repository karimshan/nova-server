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
public class SeaTrollCombat extends CombatScript {

	  public Object[] getKeys()
	    {
	        return (new Object[] {
	            Integer.valueOf(3847)
	        });
	    }
	public int attack(NPC npc, Entity target) {
		int jadAttack = Misc.getRandom(2);
		int jadHit = Misc.getRandom(200);
        NPCCombatDefinitions defs = npc.getCombatDefinitions();
      
	
			switch(jadAttack) {
			case 1:
				npc.setNextAnimation(new Animation(2992));
				delayHit(npc, 1, target, new Hit[] {
						getMagicHit(npc, jadHit)
				});
				break;
			case 2:
				npc.setNextAnimation(new Animation(2992));
				delayHit(npc, 1, target, new Hit[] {
						getRangeHit(npc, jadHit)
				});
				break;
			}
		
		return defs.getAttackDelay();
	}

}