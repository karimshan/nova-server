package org.nova.game.player.controlers;

import org.nova.game.map.Location;
import org.nova.game.player.Player;

public class Kalaboss extends Controller {

    private boolean showingOption;

    @Override
    public void start() {
	setInviteOption(true);
    }

    @SuppressWarnings("unused")
    @Override
    public boolean canPlayerOption1(Player target) {
	if (true) {
	    return true;
	}
	player.faceLocation(target);
	player.packets().sendMessage("You can't do that right now.");
	return false;
    }

    @Override
    public boolean login() {
	moved();
	return false;
    }

    @Override
    public boolean sendDeath() {
	setInviteOption(false);
	removeControler();
	return true;
    }

    @Override
    public boolean logout() {
	return false; // so doesnt remove script
    }

    @Override
    public void forceClose() {
	setInviteOption(false);
    }

    @Override
    public void moved() {
	if ((player.getX() == 3385 || player.getX() == 3384) && player.getY() == 3615) {
	    setInviteOption(false);
	    removeControler();
	    player.getControllerManager().startController("Wilderness");
	} else {
	    if (!isAtKalaboss(player)) {
		setInviteOption(false);
		removeControler();
	    } else
		setInviteOption(true);
	}
    }

    public static boolean isAtKalaboss(Location tile) {
	return tile.getX() >= 3385 && tile.getX() <= 3513 && tile.getY() >= 3605 && tile.getY() <= 3794;
    }

    public void setInviteOption(boolean show) {
	if (show == showingOption)
	    return;
	showingOption = show;
	player.packets().sendPlayerOption(show ? "Invite" : "null", 1, false);
    }
}
