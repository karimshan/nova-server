package org.nova.game.npc.combat.impl;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.npc.familiar.Familiar;

public class ThornySnailCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6807, 6806 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			npc.setNextAnimation(new Animation(8148));
			npc.setNextGraphics(new Graphics(1385));
			Game.sendProjectile(npc, target, 1386, 34, 16, 30, 35, 16, 0);
			delayHit(
					npc,
					1,
					target,
					getRangeHit(
							npc,
							getRandomMaxHit(npc, 80,
									NPCCombatDefinitions.RANGE, target)));
			npc.setNextGraphics(new Graphics(1387));
		} else {
			npc.setNextAnimation(new Animation(8143));
			delayHit(
					npc,
					1,
					target,
					getRangeHit(
							npc,
							getRandomMaxHit(npc, 40,
									NPCCombatDefinitions.RANGE, target)));
		}
		return defs.getAttackDelay();
	}

}
