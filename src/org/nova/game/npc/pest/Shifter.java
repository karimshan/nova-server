package org.nova.game.npc.pest;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.others.PestMonsters;
import org.nova.game.player.content.minigames.pestcontrol.PestControl;
import org.nova.utility.misc.Misc;

@SuppressWarnings("serial")
public class Shifter extends PestMonsters {

    public Shifter(int id, Location tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, int index, PestControl manager) {
	super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned, index, manager);
    }

    @Override
    public void processNPC() {
	super.processNPC();
	Entity target = this.getPossibleTargets().get(0);
	if (this.getCombat().process() && !this.withinDistance(target, 10) || Misc.random(15) == 0)
	    teleportSpinner(target);
    }

    private void teleportSpinner(Location tile) { // def 3902, death 3903
	setLocation(tile);
	setNextAnimation(new Animation(3904));
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		setNextGraphics(new Graphics(654));// 1502
	    }
	});
    }
}
