package org.nova.game.npc.combat.impl;

import org.nova.game.entity.Entity;
import org.nova.game.masks.Animation;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.utility.misc.Misc;
/**
 * 
 * @author Fuzen Seth
 *
 */
public class AgritNaNa extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 14544 };
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        int attackStyle = 0;
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
                                getRandomMaxHit(npc, 250,
                                        NPCCombatDefinitions.MELEE, target)));
                npc.setNextAnimation(new Animation(defs.getAttackEmote()));
                return defs.getAttackDelay();
            }
        
        }
        return defs.getAttackDelay();
    }
}
