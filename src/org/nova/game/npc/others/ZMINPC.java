
package org.nova.game.npc.others;

import java.util.ArrayList;
import java.util.List;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 *
 * @author Fuzen Seth
 * @Sets the ZMI Npcs agressive.
 */
public class ZMINPC extends NPC {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4449037259247161546L;

	public ZMINPC(int id, Location tile, int mapAreaNameHash,
            boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
    }
    
    @Override
    public ArrayList<Entity> getPossibleTargets() {
        ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
        for (int regionId : getMapRegionsIds()) {
            List<Integer> playerIndexes = Game.getRegion(regionId).getPlayerIndexes();
            if (playerIndexes != null) {
                for (int npcIndex : playerIndexes) {
                    Player player = Game.getPlayers().get(npcIndex);
                    if (player == null
                            || player.isDead()
                            || player.hasFinished()
                            || !player.isRunning()
                            || !player.withinDistance(this, 64)
                            || ((!isAtMultiArea() || !player.isAtMultiArea())
                            && player.getAttackedBy() != this && player.getAttackedByDelay() > System.currentTimeMillis())
                            || !clipedProjectile(player, false)) {
                        continue;
                    }
                    possibleTarget.add(player);
                }
            }
        }
        return possibleTarget;
    }

}

