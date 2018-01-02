package org.nova.game.npc.combat;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;

public class Default extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Default" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = defs.getAttackStyle();
		if (attackStyle == NPCCombatDefinitions.MELEE) {
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(), attackStyle,
									target)));
		} else {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), attackStyle,
					target);
			delayHit(
					npc,
					2,
					target,
					attackStyle == NPCCombatDefinitions.RANGE ? getRangeHit(
							npc, damage) : getMagicHit(npc, damage));
			if (defs.getAttackProjectile() != -1)
				Game.sendProjectile(npc, target, defs.getAttackProjectile(),
						41, 16, 41, 35, 16, 0);
		}
		if (defs.getAttackGfx() != -1)
			npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		return defs.getAttackDelay();
	}
}