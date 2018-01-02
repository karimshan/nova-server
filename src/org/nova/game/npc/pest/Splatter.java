package org.nova.game.npc.pest;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.npc.others.PestMonsters;
import org.nova.game.player.content.minigames.pestcontrol.PestControl;
import org.nova.utility.misc.Misc;

@SuppressWarnings("serial")
public class Splatter extends PestMonsters {

    public Splatter(int id, Location tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, int index, PestControl manager) {
	super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned, index, manager);
    }

    @Override
    public void processNPC() {
	super.processNPC();
    }

    private void sendExplosion() {
	final Splatter splatter = this;
	setNextAnimation(new Animation(3888));
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		setNextAnimation(new Animation(3889));
		setNextGraphics(new Graphics(649 + (getId() - 3727)));
		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			finish();
			for (Entity e : getPossibleTargets())
			    if (e.withinDistance(splatter, 2))
				e.applyHit(new Hit(splatter, Misc.getRandom(400), HitLook.REGULAR_DAMAGE));
		    }
		});
	    }
	});
    }

    @Override
    public void sendDeath(Entity source) {
	final NPCCombatDefinitions defs = getCombatDefinitions();
	resetWalkSteps();
	getCombat().removeTarget();
	setNextAnimation(null);
	WorldTasksManager.schedule(new WorldTask() {
	    int loop;

	    @Override
	    public void run() {
		if (loop == 0)
		    sendExplosion();
		else if (loop >= defs.getDeathDelay()) {
		    reset();
		    stop();
		}
		loop++;
	    }
	}, 0, 1);
    }
}
