package org.nova.game.player.content.minigames.pestcontrol;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.controlers.Controller;
import org.nova.utility.misc.Misc;

public class PestControlGame extends Controller {

    private PestControl control;
    private double points;

    @Override
    public void start() {
	control = (PestControl) getArguments()[0];
	setArguments(null);
	setPoints(0.0D);
	sendInterfaces();
	player.setForceMultiArea(true);
    }

    @Override
    public void sendInterfaces() {
	updatePestPoints();
	  player.interfaces().sendTab(player.interfaces().isFullScreen() ? 10 : 19, 408);
    }

    private void updatePestPoints() {
	boolean isGreen = getPoints() > 750;
	player.packets().sendIComponentText(408, 11, (isGreen ? "<col=75AE49>" : "") + (int) getPoints() + "</col>");
    }

    @Override
    public void forceClose() {
	if (control != null) {
	    if (control.getPortalCount() != 0) {
		if (control.getPlayers().contains(player))
		    control.getPlayers().remove(player);
	    }
	    player.useStairs(-1, Lander.getLanders()[control.getPestData().ordinal()].getLanderRequierment().getExitTile(), 1, 2);
	} else
	    player.useStairs(-1, new Location(2657, 2639, 0), 1, 2);
	player.setForceMultiArea(false);
	 player.packets().closeInterface(player.interfaces().isFullScreen() ? 10 : 19);
	player.reset();
    }

    @Override
    public void magicTeleported(int teleType) {
	player.getControllerManager().forceStop();
    }

    @Override
    public boolean processMagicTeleport(Location toTile) {
	player.getMatrixDialogues().startDialogue("SimpleMessage", "You can't leave the pest control area like this.");
	return false;
    }

    @Override
    public boolean processItemTeleport(Location toTile) {
	player.getMatrixDialogues().startDialogue("SimpleMessage", "You can't leave the pest control area like this.");
	return false;
    }

    @Override
    public boolean canMove(int dir) {
	Location toTile = new Location(player.getX() + Misc.DIRECTION_DELTA_X[dir], player.getY() + Misc.DIRECTION_DELTA_Y[dir], player.getZ());
	return !control.isBrawlerAt(toTile);
    }

    @Override
    public boolean login() {
	return true;
    }

    @Override
    public boolean logout() {
	if (control != null)
	    control.getPlayers().remove(player);
	return false;
    }

    @Override
    public boolean canSummonFamiliar() {
	player.packets().sendMessage("You feel it's best to keep your Familiar away during this game.");
	return false;
    }

    @Override
    public boolean sendDeath() {
	WorldTasksManager.schedule(new WorldTask() {
	    int loop;

	    @Override
	    public void run() {
		if (loop == 0) {
		    player.setNextAnimation(new Animation(836));
		} else if (loop == 1) {
		    player.packets().sendMessage("Oh dear, you have died.");
		} else if (loop == 3) {
		    player.reset();
		    player.setLocation(control.getLocation(35 - Misc.random(4), 54 - (Misc.random(3))));
		    player.setNextAnimation(new Animation(-1));
		} else if (loop == 4) {
		    player.packets().sendMusicEffect(90);
		    stop();
		}
		loop++;
	    }
	}, 0, 1);
	return false;
    }

    @Override
    public void trackXP(int skillId, int addedXp) {
	if (skillId == 3) // hp
	    setPoints(getPoints() + ((addedXp / 20) * 2.5));
	updatePestPoints();
    }

    public double getPoints() {
	return points;
    }

    public void setPoints(double points) {
	this.points = points;
    }
}
