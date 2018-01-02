package org.nova.game.npc.combat.impl;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.utility.misc.Misc;

public class JadCombat extends CombatScript {

	public Object[] getKeys() {
		return new Object[] { Integer.valueOf(2745) };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Misc.random(3);
		if (attackStyle == 2) { // melee
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			int size = npc.getSize();
			if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)
				attackStyle = Misc.random(2); // set mage
			else {
				npc.setNextAnimation(new Animation(9277));
				delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
				return defs.getAttackDelay();
			}
		}
		if (attackStyle == 1) { // range
			npc.setNextAnimation(new Animation(9276));
			npc.setNextGraphics(new Graphics(1625));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					target.setNextGraphics(new Graphics(1628));
					delayHit(npc, 1, target, getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit() - 1, NPCCombatDefinitions.RANGE, target)));
				}
			}, 2);
		} else { // Magic
			npc.setNextAnimation(new Animation(9278));
			npc.setNextGraphics(new Graphics(1626));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					Game.sendProjectile(new Location(npc.getX() + 1, npc.getY() + 1, npc.getZ()), target, 1627, 80, 30, 40, 20, 5, 0);
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							target.setNextGraphics(new Graphics(2741, 0, 100));
							delayHit(npc, 0, target, getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit() - 1, NPCCombatDefinitions.MAGE, target)));
						}

					}, 1);
				}
			}, 2);
		}
		return defs.getAttackDelay() + 2;
	}

}