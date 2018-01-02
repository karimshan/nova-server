package org.nova.game.npc.combat.impl;

import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.utility.misc.Misc;

public class CommanderZilyanaCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6247 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Misc.getRandom(4) == 0) {
			switch (Misc.getRandom(9)) {
			case 0:
				npc.setNextForceTalk(new ForceTalk(
						"Death to the enemies of the light!"));
				npc.playSound(3247, 2);
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("Slay the evil ones!"));
				npc.playSound(3242, 2);
				break;
			case 2:
				npc.setNextForceTalk(new ForceTalk(
						"Saradomin lend me strength!"));
				npc.playSound(3263, 2);
				break;
			case 3:
				npc.setNextForceTalk(new ForceTalk("By the power of Saradomin!"));
				npc.playSound(3262, 2);
				break;
			case 4:
				npc.setNextForceTalk(new ForceTalk("May Saradomin be my sword."));
				npc.playSound(3251, 2);
				break;
			case 5:
				npc.setNextForceTalk(new ForceTalk("Good will always triumph!"));
				npc.playSound(3260, 2);
				break;
			case 6:
				npc.setNextForceTalk(new ForceTalk(
						"Forward! Our allies are with us!"));
				npc.playSound(3245, 2);
				break;
			case 7:
				npc.setNextForceTalk(new ForceTalk("Saradomin is with us!"));
				npc.playSound(3266, 2);
				break;
			case 8:
				npc.setNextForceTalk(new ForceTalk("In the name of Saradomin!"));
				npc.playSound(3250, 2);
				break;
			case 9:
				npc.setNextForceTalk(new ForceTalk("Attack! Find the Godsword!"));
				npc.playSound(3258, 2);
				break;
			}
		}
		if (Misc.getRandom(1) == 0) { // mage magical attack
			npc.setNextAnimation(new Animation(6967));
			for (Entity t : npc.getPossibleTargets()) {
				if (!t.withinDistance(npc, 3))
					continue;
				int damage = getRandomMaxHit(npc, defs.getMaxHit(),
						NPCCombatDefinitions.MAGE, t);
				if (damage > 0) {
					delayHit(npc, 1, t, getMagicHit(npc, damage));
					t.setNextGraphics(new Graphics(1194));
				}
			}

		} else { // melee attack
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}
