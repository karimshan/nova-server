/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nova.game.npc.combat.lrc;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;

/**
 *
 * @author Owner
 */
public class LivingRockStrikerCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{8833};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (npc.withinDistance(target, 10)) { // range magical attack
            npc.setNextAnimation(new Animation(1296));
            for (Entity t : npc.getPossibleTargets()) {
                delayHit(
                        npc,
                        1,
                        t,
                        getRangeHit(
                        npc,
                        getRandomMaxHit(npc, 140,
                        NPCCombatDefinitions.RANGE, t)));
                Game.sendProjectile(npc, t, 1197, 41, 16, 41, 35, 16, 0);
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
