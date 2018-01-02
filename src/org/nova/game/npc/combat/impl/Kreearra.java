package org.nova.game.npc.combat.impl;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.utility.misc.Misc;

public class Kreearra extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6222 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (!npc.isUnderCombat()) {
			npc.setNextAnimation(new Animation(6997));
			delayHit(
					npc,
					1,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, 260,
									NPCCombatDefinitions.MELEE, target)));
			return defs.getAttackDelay();
		}
		npc.setNextAnimation(new Animation(6976));
		for (Entity t : npc.getPossibleTargets()) {
			if (Misc.getRandom(2) == 0)
				sendMagicAttack(npc, t);
			else {
				delayHit(
						npc,
						1,
						t,
						getRangeHit(
								npc,
								getRandomMaxHit(npc, 720,
										NPCCombatDefinitions.RANGE, t)));
				Game.sendProjectile(npc, t, 1197, 41, 16, 41, 35, 16, 0);
				Location teleTile = t;
				for (int trycount = 0; trycount < 10; trycount++) {
					teleTile = new Location(t, 2);
					if (Game.canMoveNPC(t.getZ(), teleTile.getX(),
							teleTile.getY(), t.getSize()))
						break;
				}
				t.setLocation(teleTile);
			}
		}
		return defs.getAttackDelay();
	}

	private void sendMagicAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(6976));
		for (Entity t : npc.getPossibleTargets()) {
			delayHit(
					npc,
					1,
					t,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 210,
									NPCCombatDefinitions.MAGE, t)));
			Game.sendProjectile(npc, t, 1198, 41, 16, 41, 35, 16, 0);
			target.setNextGraphics(new Graphics(1196));
		}
	}
}
