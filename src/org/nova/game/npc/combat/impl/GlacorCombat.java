package org.nova.game.npc.combat.impl;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Hit;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * @author 'Corey 2010
 */

public class GlacorCombat extends CombatScript{

	public Object[] getKeys() {
		return (new Object[]{Integer.valueOf(14301)});
	}

	public boolean spawnedMinions = false;
	public NPC Glacor;
	
	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = Misc.getRandom(294);
		int specialHit = Misc.getRandom(695);
		int attackStyle = Misc.getRandom(3);
        int distanceX = target.getX() - npc.getX();
        int distanceY = target.getY() - npc.getY();
		Player player = (Player) target;
        if (npc.getHitpoints() <= 2500 && !spawnedMinions){
        	//for you TODO Correctly spawn minions.
        	spawnedMinions = true;
        }
        if (distanceX < -1 || distanceY < -1){
        	npc.setNextAnimation(new Animation(412));
			Game.sendProjectile(npc, target, 1887, 34, 16, 40, 35, 16, 0); 
			delayHit(npc, 1, target, new Hit[] {
					getMagicHit(npc, specialHit)
			});
        }else{
			switch(attackStyle){
			case 2:
				npc.setNextAnimation(new Animation(9955));
				delayHit(npc, 0, target, new Hit[] {
						getMeleeHit(npc, hit)
				});
				break;
			case 3:
				npc.setNextAnimation(new Animation(412));
				Game.sendProjectile(npc, target, 1887, 34, 16, 40, 35, 16, 0); 
				delayHit(npc, 1, target, new Hit[] {
						getMagicHit(npc, specialHit)
				});
				break;
			case 0:
				npc.setNextAnimation(new Animation(9968));
				delayHit(npc, 1, target, new Hit[] {
						getRangeHit(npc, hit)
				});
				break;
			case 1:
				npc.setNextAnimation(new Animation(9968));
				delayHit(npc, 1, target, new Hit[] {
						getRangeHit(npc, hit)
				});
				break;
			}
		}
		return defs.getAttackDelay();
	}
}