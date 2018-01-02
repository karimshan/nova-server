package org.nova.game.npc.combat.impl;

import java.util.ArrayList;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.utility.misc.Misc;


public class GluttonousBehemothCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Gluttonous behemoth" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int size = npc.getSize();
		ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
		boolean stomp = false;
		for (Entity t : possibleTargets) {
			int distanceX = t.getX() - npc.getX();
			int distanceY = t.getY() - npc.getY();
			if (distanceX < size && distanceX > -1 && distanceY < size
					&& distanceY > -1) {
				stomp = true;
				delayHit(
						npc,
						0,
						t,
						getRegularHit(
								npc,
								getRandomMaxHit(npc, defs.getMaxHit(),
										NPCCombatDefinitions.MELEE, t)));
			}
		}
		if (stomp) {
			npc.setNextAnimation(new Animation(13718));
			return defs.getAttackDelay();
		}
		int attackStyle = Misc.getRandom(2);
		if (attackStyle == 2) {
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			if (!(distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1))
				attackStyle = Misc.getRandom(1);
			else {
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
		if (attackStyle == 0) {
			npc.setNextAnimation(new Animation(13719));
			Game.sendProjectile(npc, target, 2612, 41, 16, 41, 35, 16, 0);
			int damage = getRandomMaxHit(npc, defs.getMaxHit(),
					NPCCombatDefinitions.MAGE, target);
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			if (damage != 0) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						target.setNextGraphics(new Graphics(2613));
					}
				}, 1);
			}
		} else if (attackStyle == 1) {
			npc.setNextAnimation(new Animation(13722));
			Game.sendProjectile(npc, target, 2611, 41, 16, 41, 35, 16, 0);
			delayHit(
					npc,
					2,
					target,
					getRangeHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.RANGE, target)));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					target.setNextGraphics(new Graphics(2611));
				}
			}, 1);
		}
		return defs.getAttackDelay();
	}
}
