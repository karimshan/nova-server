package org.nova.game.npc.combat.impl;

import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;
import org.nova.game.player.content.Combat;
import org.nova.utility.misc.Misc;

public class LeatherDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Green dragon", "Blue dragon", "Red dragon",
				"Black dragon", 742, 14548 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		if (distanceX > size || distanceX < -1 || distanceY > size
				|| distanceY < -1)
			return 0;
		if (Misc.getRandom(3) != 0) {
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MELEE, target)));
		} else {
			int damage = Misc.getRandom(650);
			npc.setNextAnimation(new Animation(12259));
			npc.setNextGraphics(new Graphics(1, 0, 100));
			final Player player = target instanceof Player ? (Player) target
					: null;
			if (Combat.hasAntiDragProtection(target)
					|| (player != null && (player.getPrayer()
							.usingPrayer(0, 17) || player.getPrayer()
							.usingPrayer(1, 7)))) {
				damage = 0;
				player.packets()
						.sendMessage(
								"Your "
										+ (Combat.hasAntiDragProtection(target) ? "shield"
												: "prayer")
										+ " absorb's most of the dragon's breath!",
								true);
			}
			if (player != null
					&& player.getFireImmune() > Misc.currentTimeMillis()) {
				if (damage != 0)
					damage = Misc.getRandom(50);
			} else if (damage == 0)
				damage = Misc.getRandom(50);
			else if (player != null)
				player.packets().sendMessage(
						"You are hit by the dragon's fiery breath!", true);
			delayHit(npc, 1, target, getRegularHit(npc, damage));
		}
		return defs.getAttackDelay();
	}
}
