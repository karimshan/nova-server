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
 * @author Tyler
 *
 */
public class NomadCombat extends CombatScript {

	  public Object[] getKeys()
	    {
	        return (new Object[] {
	            Integer.valueOf(8528)
	        });
	    }
	//Melee Emote-12696
	//Mage Emote-9300
	//Range Gfx-451
	public int attack(NPC npc, Entity target) {
		int nomadAttack = Misc.getRandom(2);
		int nomadHit = Misc.getRandom(500);
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if(target.withinDistance(npc, 1)) {
			npc.setNextAnimation(new Animation(12696));
			delayHit(npc, 1, target, new Hit[] {
	                getMeleeHit(npc, nomadHit)
	            });
		}
		else {
			switch(nomadAttack) {
			case 1:
				npc.setNextAnimation(new Animation(12697));
				delayHit(npc, 1, target, new Hit[] {
						getMagicHit(npc, nomadHit)
				});
				break;
			}
		}
		return defs.getAttackDelay();
	}

}