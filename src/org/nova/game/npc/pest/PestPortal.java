package org.nova.game.npc.pest;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.content.minigames.pestcontrol.PestControl;
import org.nova.utility.misc.Misc;


@SuppressWarnings("serial")
public class PestPortal extends NPC {

    boolean isLocked;
    PestControl control;
    int ticks;

    public PestPortal(int id, boolean canbeAttackedOutOfArea, Location tile, PestControl control) {
	super(id, tile, -1, canbeAttackedOutOfArea, true);
	this.control = control;
	setCantFollowUnderCombat(true);
	setForceMultiArea(true);
	setCapDamage(400);
	isLocked = true;
    }

    public boolean isLocked() {
	return isLocked;
    }

    public void unlock() {
	if (getId() >= 6146) {
	    setNextNPCTransformation(getId() - 4);
	    control.sendTeamMessage(getStringForId() + " portal shield has been dropped!");
	}
	this.isLocked = false;
    }

    private String getStringForId() {
	switch (getId()) {
	    case 6142:
		return "The purple, western";
	    case 6143:
		return "The blue, eastern";
	    case 6144:
		return "The yellow, south-eastern";
	    case 6145:
		return "The red, south-western";
	}
	return "THIS SHOULDN'T EVER HAPPEN.";
    }

    private int getIndexForId() {
	switch (getId()) {
	    case 6146:
	    case 6142:
		return 0;
	    case 6147:
	    case 6143:
		return 1;
	    case 6148:
	    case 6144:
		return 2;
	    case 6149:
	    case 6145:
		return 3;
	    case 3782:
	    case 3784:
	    case 3785:
		return 4;
	}
	return -1;
    }

    @Override
    public void processNPC() {
	super.processNPC();
	ticks++;
	if (ticks % 15 == 0) {
	    if (control.createPestNPC(getIndexForId())) {
		if (Misc.random(5) == 0) // double spawn xD
		    control.createPestNPC(getIndexForId());
	    }
	}
	if (isDead() || isLocked)
	    return;
	cancelFaceEntityNoCheck();
    }

    @Override
    public void sendDeath(Entity source) {
	final NPCCombatDefinitions defs = getCombatDefinitions();
	resetWalkSteps();
	setNextAnimation(null);
	WorldTasksManager.schedule(new WorldTask() {
	    int loop;

	    @Override
	    public void run() {
		if (loop == 0) {
		    setNextAnimation(new Animation(defs.getDeathEmote()));
		} else if (loop >= defs.getDeathDelay()) {
		    if (getIndexForId() != 4) {
			control.unlockPortal();
			control.getKnight().heal(500);
		    }
		    finish();
		    stop();
		}
		loop++;
	    }
	}, 0, 1);
    }
}