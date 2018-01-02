package org.nova.game.npc.combat.impl;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.npc.familiar.Familiar;
import org.nova.utility.misc.Misc;

public class DreadFowlCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6825, 6824 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			npc.setNextAnimation(new Animation(7810));
			npc.setNextGraphics(new Graphics(1318));
			delayHit(
					npc,
					1,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 40, NPCCombatDefinitions.MAGE,
									target)));
			Game.sendProjectile(npc, target, 1376, 34, 16, 30, 35, 16, 0);
		} else {
			if (Misc.getRandom(10) == 0) {// 1/10 chance of random special
											// (weaker)
				npc.setNextAnimation(new Animation(7810));
				npc.setNextGraphics(new Graphics(1318));
				delayHit(
						npc,
						1,
						target,
						getMagicHit(
								npc,
								getRandomMaxHit(npc, 30,
										NPCCombatDefinitions.MAGE, target)));
				Game.sendProjectile(npc, target, 1376, 34, 16, 30, 35, 16, 0);
			} else {
				npc.setNextAnimation(new Animation(7810));
				delayHit(
						npc,
						1,
						target,
						getMeleeHit(
								npc,
								getRandomMaxHit(npc, 30,
										NPCCombatDefinitions.MELEE, target)));
			}
		}
		return defs.getAttackDelay();
	}
}
