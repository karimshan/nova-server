package org.nova.game.npc.combat.impl;

import org.nova.game.entity.Entity;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;

public class BarricadeCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		// TODO Auto-generated method stub
		return new Object[] { "Barricade" };
	}

	/*
	 * empty
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		return 0;
	}

}
