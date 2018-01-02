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

public class MossTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7330, 7329 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage = 0;
		if (usingSpecial) {// priority over regular attack
			npc.setNextAnimation(new Animation(8223));
			npc.setNextGraphics(new Graphics(1460));
			delayHit(
					npc,
					1,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 160,
									NPCCombatDefinitions.MAGE, target)));
			for (Entity targets : npc.getPossibleTargets()) {
				Game.sendProjectile(npc, targets, 1462, 34, 16, 30, 35, 16, 0);
				if (Misc.getRandom(3) == 0)// 1/3 chance of being poisioned
					targets.getPoison().makePoisoned(58);
			}
		} else {
			damage = getRandomMaxHit(npc, 160, NPCCombatDefinitions.MELEE,
					target);
			npc.setNextAnimation(new Animation(8222));
			delayHit(npc, 1, target, getMeleeHit(npc, damage));
		}
		return defs.getAttackDelay();
	}

}
