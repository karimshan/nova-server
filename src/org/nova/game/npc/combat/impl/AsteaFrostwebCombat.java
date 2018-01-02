package org.nova.game.npc.combat.impl;

import java.util.ArrayList;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.utility.misc.Misc;


public class AsteaFrostwebCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Astea Frostweb" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
	//	if (Utils.getRandom(10) == 0) {
		//	AsteaFrostweb boss = (AsteaFrostweb) npc;
		//	boss.spawnSpider();
	//	}
		if (Misc.getRandom(10) == 0) { // spikes
			ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			for (Entity t : possibleTargets)
				delayHit(npc, 1, t,
						new Hit(npc, Misc.getRandom(defs.getMaxHit()),
								HitLook.REGULAR_DAMAGE));
			return defs.getAttackDelay();
		} else {
			int attackStyle = Misc.getRandom(1);
			if (attackStyle == 1) { // check melee
				if (Misc.getDistance(npc.getX(), npc.getY(), target.getX(),
						target.getY()) > 1)
					attackStyle = 0; // set mage
				else { // melee
					npc.setNextAnimation(new Animation(defs.getAttackEmote()));
					delayHit(
							npc,
							0,
							target,
							getMeleeHit(
									npc,
									getRandomMaxHit(npc, defs.getMaxHit(),
											NPCCombatDefinitions.MELEE, target)));
					return defs.getAttackDelay();
				}
			}
			if (attackStyle == 0) { // mage
				npc.setNextAnimation(new Animation(defs.getAttackEmote()));
				ArrayList<Entity> possibleTargets = npc.getPossibleTargets();

				int d = getRandomMaxHit(npc, defs.getMaxHit(),
						NPCCombatDefinitions.MAGE, target);
				delayHit(npc, 1, target, getMagicHit(npc, d));
				if (d != 0) {
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							if (target.getFreezeDelay() >= System
									.currentTimeMillis())
								target.setNextGraphics(new Graphics(1677, 0,
										100));
							else {
								target.setNextGraphics(new Graphics(369));
								target.addFreezeDelay(10000);
							}
						}
					}, 1);
					for (final Entity t : possibleTargets) {
						if (t != target && t.withinDistance(target, 2)) {
							int damage = getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MAGE, t);
							delayHit(npc, 1, t, getMagicHit(npc, damage));
							if (damage != 0) {
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										if (t.getFreezeDelay() >= System
												.currentTimeMillis())
											t.setNextGraphics(new Graphics(
													1677, 0, 100));
										else {
											t.setNextGraphics(new Graphics(369));
											t.addFreezeDelay(10000);
										}
									}
								}, 1);
							}

						}
					}
				}
				if (Misc.getDistance(npc.getX(), npc.getY(), target.getX(),
						target.getY()) <= 1) { // lure
												// after
												// freeze
					npc.resetWalkSteps();
					npc.addWalkSteps(target.getX() + Misc.getRandom(2),
							target.getY() + Misc.getRandom(2));
				}
			}
		}
		return defs.getAttackDelay();
	}
}
