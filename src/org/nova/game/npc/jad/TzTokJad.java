/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nova.game.npc.jad;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;
import org.nova.game.player.content.Magic;

/**
 *
 * @author Owner
 */
public class TzTokJad extends NPC {

    /**
	 * 
	 */
	private static final long serialVersionUID = -661323148915736102L;

	public TzTokJad(int id, Location tile, int mapAreaNameHash,
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

    /*
     * gotta override else setRespawnTask override doesnt work
     */
    @Override
    public void sendDeath(Entity source) {
	if(!(source instanceof Player)) {
		 final NPCCombatDefinitions defs = getCombatDefinitions();
		 WorldTasksManager.schedule(new WorldTask() {

	            int loop;

	            @Override
	            public void run() {
	                if (loop == 0) {
	                    setNextAnimation(new Animation(defs.getDeathEmote()));
	                } else if (loop >= defs.getDeathDelay()) { 
	                    reset();
	                    setCoords(getRespawnTile());
	                    finish();
	                    setRespawnTask();
	                    stop();
	                }
	                loop++;
	            }
	        }, 0, 1);
		 return;
	}
	Player killer = (Player) source;
	
	 final NPCCombatDefinitions defs = getCombatDefinitions();
		
        resetWalkSteps();
        getCombat().removeTarget();
        setNextAnimation(null);
		killer.getInventory().addItem(6570, 1);
		final NPC n = this;
		Magic.sendNormalTeleportSpell(killer, 0, 0, new Location(2438,5173, 0));                               
        WorldTasksManager.schedule(new WorldTask() {

            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setNextAnimation(new Animation(defs.getDeathEmote()));
                } else if (loop >= defs.getDeathDelay()) { 
                    drop(n);
                    reset();
                    setCoords(getRespawnTile());
                    finish();
                    setRespawnTask();
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }
    
    @Override
    public void setRespawnTask() {
        if (!hasFinished()) {
            reset();
            setCoords(getRespawnTile());
            finish();
        }
        final NPC npc = this;
        CoresManager.slowExecutor.schedule(new Runnable() {

            @Override
            public void run() {
                setFinished(false);
                Game.addNPC(npc);
                npc.setLastRegionId(0);
                Game.updateEntityRegion(npc);
                loadMapRegions();
                checkMultiArea();
            }
        }, getCombatDefinitions().getRespawnDelay() * 1200,
                TimeUnit.MILLISECONDS);
    }
}

