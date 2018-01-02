package org.nova.game.npc.pest;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.NPC;
import org.nova.game.npc.others.PestMonsters;
import org.nova.game.player.Player;
import org.nova.game.player.content.minigames.pestcontrol.PestControl;

@SuppressWarnings("serial")
public class Spinner extends PestMonsters {

    private byte healTicks;

    public Spinner(int id, Location tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, int index, PestControl manager) {
	super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned, index, manager);
    }

    @Override
    public void processNPC() {
	PestPortal portal = manager.getPortals()[portalIndex];
	if (portal.isDead()) {
	    explode();
	    return;
	}
	if (!portal.isLocked) {
	    healTicks++;
	    if (!withinDistance(portal, 1))
		this.addWalkSteps(portal.getX(), portal.getY());
	    else if (healTicks % 6 == 0)
		healPortal(portal);
	}
    }

    private void healPortal(final PestPortal portal) {
	setNextFaceEntity(portal);
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		setNextAnimation(new Animation(3911));
		setNextGraphics(new Graphics(658, 0, 96 << 16));
		if (portal.getHitpoints() != 0)
		    portal.heal((portal.getMaxHitpoints() / portal.getHitpoints()) * 45);
		healTicks = 0; /* Saves memory in the long run. Meh */
	    }
	});
    }

    private void explode() {
	final NPC npc = this;
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		for (Player player : manager.getPlayers()) {
		    if (!withinDistance(player, 7))
			continue;
		    player.getPoison().makePoisoned(50);
		    player.applyHit(new Hit(npc, 50, HitLook.REGULAR_DAMAGE));
		    npc.reset();
		    npc.finish();
		}
	    }
	}, 1);
    }
}
