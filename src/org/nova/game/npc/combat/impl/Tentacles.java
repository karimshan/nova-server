package org.nova.game.npc.combat.impl;

import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;

public class Tentacles extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 15209, 15210 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			npc.setNextForceTalk(new ForceTalk("GLARGHHHHHHHH"));
		target.setNextGraphics(new Graphics(337));
		delayHit(
				npc,
				0,
				target,
				getMagicHit(
						npc,
						getRandomMaxHit(npc, defs.getMaxHit(),
								defs.getAttackStyle(), target)));
		return defs.getAttackDelay();
	}
}
