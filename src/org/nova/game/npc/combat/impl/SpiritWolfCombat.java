package org.nova.game.npc.combat.impl;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.npc.familiar.Familiar;
import org.nova.game.player.Player;

public class SpiritWolfCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6829, 6828 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			familiar.submitSpecial(familiar.getOwner());
			npc.setNextAnimation(new Animation(8293));
			npc.setNextGraphics(new Graphics(1334));
			Game.sendProjectile(npc, target, 1333, 34, 16, 30, 35, 16, 0);
			if (target instanceof NPC) {
				if (!(((NPC) target).getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.SPECIAL))
					target.setAttackedByDelay(3000);// three seconds
				else
					familiar.getOwner()
							.packets()
							.sendMessage(
									"Your familiar cannot scare that monster.");
			} else if (target instanceof Player)
				familiar.getOwner()
						.packets()
						.sendMessage("Your familiar cannot scare a player.");
			else if (target instanceof Familiar)
				familiar.getOwner()
						.packets()
						.sendMessage(
								"Your familiar cannot scare other familiars.");
		} else {
			npc.setNextAnimation(new Animation(6829));
			delayHit(
					npc,
					1,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 40, NPCCombatDefinitions.MAGE,
									target)));
		}
		return defs.getAttackDelay();
	}

}
