package org.nova.game.player.controlers;

import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.content.minigames.pestcontrol.Lander;
import org.nova.utility.misc.Misc;

public final class PestControlLobby extends Controller {

    private int landerId;

    @Override
    public void start() {
	this.landerId = (Integer) getArguments()[0];
    }

    @Override
    public void sendInterfaces() {
	player.packets().sendIComponentText(407, 3, Misc.fixChatMessage(Lander.getLanders()[landerId].toString()));
	int minutesLeft = (Lander.getLanders()[landerId].getTimer().getMinutes());
	player.packets().sendIComponentText(407, 13, "Next Departure: " + minutesLeft + " minutes " + (!(minutesLeft % 2 == 0) ? " 30 seconds" : ""));
	player.packets().sendIComponentText(407, 14, "Player's Ready: " + Lander.getLanders()[landerId].getPlayers().size());
	player.packets().sendIComponentText(407, 16, "Commendations: " + player.getPestPoints());
	  player.interfaces().sendTab(player.interfaces().isFullScreen() ? 10 : 19, 407);
    }

    @Override
    public void magicTeleported(int teleType) {
	player.getControllerManager().forceStop();
    }

    @Override
    public boolean processMagicTeleport(Location toTile) {
	player.getControllerManager().forceStop();
	return true;
    }

    @Override
    public boolean processItemTeleport(Location toTile) {
	player.getControllerManager().forceStop();
	return true;
    }

    @Override
    public void forceClose() {
    	  player.packets().closeInterface(player.interfaces().isFullScreen() ? 10 : 19);
	Lander.getLanders()[landerId].exitLander(player);
    }

    @Override
    public boolean logout() {
	Lander.getLanders()[landerId].remove(player);//to stop the timer in the lander and prevent future errors
	return false;
    }

    @Override
    public boolean canSummonFamiliar() {
	player.packets().sendMessage("You feel it's best to keep your Familiar away during this game.");
	return false;
    }

    @Override
    public boolean processObjectClick1(GlobalObject object) {
	switch (object.getId()) {
	    case 14314:
	    case 25629:
	    case 25630:
		player.getMatrixDialogues().startDialogue("LanderD");
		return true;
	}
	return true;
    }
}
