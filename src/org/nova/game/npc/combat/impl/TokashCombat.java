package org.nova.game.npc.combat.impl;

import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.utility.misc.Misc;


public class TokashCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "To'Kash the Bloodchiller" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int size = npc.getSize();
		   int attack = Misc.getRandom(3);
	        int hit = Misc.getRandom(400+npc.getCombatLevel());
	            switch (attack) {
	                case 2:
	                case 3:
	                case 0:
	                    npc.setNextAnimation(new Animation(14392));
	                    delayHit(npc, 2, target, new Hit[]{
	                                getMeleeHit(npc, hit)
	                            });
	                    break;
	                case 1:
	                    npc.setNextAnimation(new Animation(14525));
	                    npc.setNextGraphics(new Graphics(3003));
	                    target.setNextGraphics(new Graphics(3005));
	                    delayHit(npc, 2, target, new Hit[]{
	                                getMagicHit(npc, hit+100)
	                            });
	                    break;
	            }
	        return defs.getAttackDelay();
	    }
}