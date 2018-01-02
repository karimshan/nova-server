package org.nova.game.npc.others;

import java.util.ArrayList;
import java.util.List;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.content.minigames.pestcontrol.PestControl;
import org.nova.utility.misc.Misc;

@SuppressWarnings("serial")
public class PestMonsters extends NPC {

    protected PestControl manager;
    protected int portalIndex;

    public PestMonsters(int id, Location tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, int index, PestControl manager) {
	super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	this.manager = manager;
	this.portalIndex = index;
	setForceMultiArea(true);
	setForceAgressive(true);
	setForceTargetDistance(70);
    }

    @Override
    public void processNPC() {
	super.processNPC();
	if (!getCombat().underCombat())
	    checkAgressivity();
    }

    @Override
    public ArrayList<Entity> getPossibleTargets() {
	ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
	List<Integer> playerIndexes = Game.getRegion(getRegionId()).getPlayerIndexes();
	if (playerIndexes != null) {
	    for (int playerIndex : playerIndexes) {
		Player player = Game.getPlayers().get(playerIndex);
		if (player == null || player.isDead() || player.hasFinished() || !player.isRunning() || !player.withinDistance(this, 10))
		    continue;
		possibleTarget.add(player);
	    }
	}
	if (possibleTarget.isEmpty() || Misc.random(3) == 0) {
	    possibleTarget.clear();
	    possibleTarget.add(manager.getKnight());
	}
	return possibleTarget;
    }

    @Override
    public void sendDeath(Entity source) {
	super.sendDeath(source);
	manager.getPestCounts()[portalIndex]--;
    }
}
