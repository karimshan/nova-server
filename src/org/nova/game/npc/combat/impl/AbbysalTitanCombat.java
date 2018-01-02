package org.nova.game.npc.combat.impl;

import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;

public class AbbysalTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7350, 7349 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int damage = 0;
		damage = getRandomMaxHit(npc, 140, NPCCombatDefinitions.MELEE, target);
		npc.setNextAnimation(new Animation(7980));
		npc.setNextGraphics(new Graphics(1490));

		if (target instanceof Player) { // cjay failed dragonkk saved the day
			Player player = (Player) target;
			if (damage > 0 && player.getPrayer().getPrayerpoints() > 0)
				player.getPrayer().drainPrayer(damage / 2);
		}
		delayHit(npc, 1, target, getMeleeHit(npc, damage));
		return defs.getAttackDelay();
	}
}
