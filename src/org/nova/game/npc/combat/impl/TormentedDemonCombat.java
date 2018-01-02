package org.nova.game.npc.combat.impl;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.npc.others.TormentedDemon;
import org.nova.utility.misc.Misc;

public class TormentedDemonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Tormented demon" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		TormentedDemon torm = (TormentedDemon) npc;
		int hit = 0;
		int attackStyle = torm.getFixedAmount() == 0 ? Misc.getRandom(2)
				: torm.getFixedCombatType();
		if (torm.getFixedAmount() == 0)
			torm.setFixedCombatType(attackStyle);
		switch (attackStyle) {
		case 0:
			if (npc.withinDistance(target, 3)) {
				hit = getRandomMaxHit(npc, 189, NPCCombatDefinitions.MELEE,
						target);
				npc.setNextAnimation(new Animation(10922));
				npc.setNextGraphics(new Graphics(1886));
				delayHit(npc, 1, target, getMeleeHit(npc, hit));
			}
			return defs.getAttackDelay();
		case 1:
			hit = getRandomMaxHit(npc, 270, NPCCombatDefinitions.MAGE, target);
			npc.setNextAnimation(new Animation(10918));
			npc.setNextGraphics(new Graphics(1883, 0, 96 << 16));
			Game.sendProjectile(npc, target, 1884, 34, 16, 30, 35, 16, 0);
			delayHit(npc, 1, target, getMagicHit(npc, hit));
			break;
		case 2:
			hit = getRandomMaxHit(npc, 270, NPCCombatDefinitions.RANGE, target);
			npc.setNextAnimation(new Animation(10919));
			npc.setNextGraphics(new Graphics(1888));
			Game.sendProjectile(npc, target, 1887, 34, 16, 30, 35, 16, 0);
			delayHit(npc, 1, target, getRangeHit(npc, hit));
			break;
		}
		torm.setFixedAmount(torm.getFixedAmount() + 1);
		return defs.getAttackDelay();
	}
}
