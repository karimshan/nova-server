package org.nova.game.npc.combat.impl;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;
import org.nova.game.player.content.Combat;
import org.nova.utility.misc.Misc;

public class BlinkCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] {12866};
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Misc.getRandom(5);
		int size = npc.getSize();

		if (attackStyle == 0) {
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			if (distanceX > size || distanceX < -1 || distanceY > size
					|| distanceY < -1)
				attackStyle = Misc.getRandom(4) + 1;
			else {
				delayHit(
						npc,
						0,
						target,
						getMeleeHit(
								npc,
								getRandomMaxHit(npc, defs.getMaxHit(),
										NPCCombatDefinitions.MELEE, target)));
				npc.setNextAnimation(new Animation(defs.getAttackEmote()));
				return defs.getAttackDelay();
			}
		} else if (attackStyle == 1 || attackStyle == 2) {
			int damage = Misc.getRandom(0);
			final Player player = target instanceof Player ? (Player) target
					: null;
			if (Combat.hasAntiDragProtection(target)
					|| (player != null && (player.getPrayer()
							.usingPrayer(0, 17) || player.getPrayer()
							.usingPrayer(1, 7))))
				damage = 0;
			if (player != null
					&& player.getFireImmune() > Misc.currentTimeMillis()) {
				if (damage != 0)
					damage = Misc.getRandom(164);
			} else if (damage == 0)
				damage = Misc.getRandom(0);
			else if (player != null)
				player.packets().sendMessage(
						"Your sword protects you from the dark magic casted by Blink.", true);
			delayHit(npc, 2, target, getRegularHit(npc, damage));
			Game.sendProjectile(npc, target, 1935, 34, 16, 30, 35, 16, 0);
			target.setNextGraphics(new org.nova.game.masks.Graphics(1935));
			npc.setNextAnimation(new Animation(811));

		} else if (attackStyle == 3) {
			int damage;
			final Player player = target instanceof Player ? (Player) target
					: null;
			if (Combat.hasAntiDragProtection(target)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.packets()
							.sendMessage(
									"Your sword protects you from the dark magic casted by Blink.",
									true);
			} else if (player != null
					&& (player.getPrayer().usingPrayer(0, 17) || player
							.getPrayer().usingPrayer(1, 7))) {
				damage = getRandomMaxHit(npc, 0, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.packets()
							.sendMessage(
									"Your sword protects you from the dark magic casted by Blink.",
									true);
			} else {
				damage = Misc.getRandom(0);
				if (player != null)
					player.packets().sendMessage(
							"Your sword protects you from the dark magic casted by Blink.",
							true);
			}
			if (Misc.getRandom(2) == 0)
			delayHit(npc, 2, target, getRegularHit(npc, damage));
			Game.sendProjectile(npc, target, 1935, 34, 16, 30, 35, 16, 0);
			npc.setNextAnimation(new Animation(811));
			target.setNextGraphics(new org.nova.game.masks.Graphics(1935));
		} else if (attackStyle == 4) {
			int damage;
			final Player player = target instanceof Player ? (Player) target
					: null;
			if (Combat.hasAntiDragProtection(target)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.packets()
							.sendMessage(
									"Your sword protects you from the dark magic casted by Blink.",
									true);
			} else if (player != null
					&& (player.getPrayer().usingPrayer(0, 17) || player
							.getPrayer().usingPrayer(1, 7))) {
				damage = getRandomMaxHit(npc, 1, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.packets()
							.sendMessage(
									"Your sword protects you from the dark magic casted by Blink.",
									true);
			} else {
				damage = Misc.getRandom(0);
				if (player != null)
					player.packets().sendMessage(
							"Your sword protects you from the dark magic casted by Blink.",
							true);
			}
			if (Misc.getRandom(2) == 0)
				target.addFreezeDelay(15000);
			delayHit(npc, 2, target, getRegularHit(npc, damage));
			Game.sendProjectile(npc, target, 1935, 34, 16, 30, 35, 16, 0);
			target.setNextGraphics(new org.nova.game.masks.Graphics(1935));
			npc.setNextAnimation(new Animation(811));
		} else {
			int damage;
			final Player player = target instanceof Player ? (Player) target
					: null;
			if (Combat.hasAntiDragProtection(target)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.packets()
							.sendMessage(
									"Your sword protects you from the dark magic casted by Blink.",
									true);
			} else if (player != null
					&& (player.getPrayer().usingPrayer(0, 17) || player
							.getPrayer().usingPrayer(1, 7))) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.packets()
							.sendMessage(
									"Your sword protects you from the dark magic casted by Blink.",
									true);
			} else {
				damage = Misc.getRandom(0);
				if (player != null)
					player.packets().sendMessage(
							"Your sword protects you from the dark magic casted by Blink.",
							true);
			}
			delayHit(npc, 2, target, getRegularHit(npc, damage));
			target.setNextGraphics(new org.nova.game.masks.Graphics(1935));
			Game.sendProjectile(npc, target, 1935, 34, 16, 30, 35, 16, 0);
			npc.setNextAnimation(new Animation(811));
		}
		return defs.getAttackDelay();
	}
}
