package org.nova.game.player.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

public class Dicing {

    public static void handleRoll(final Player player, int itemId, boolean friends) {
	if (friends) {
	    switch (itemId) {
		case 15086:
		    friendsRoll(player, itemId, 2072, 1, 6);
		    break;
		case 15088:
		    friendsRoll(player, itemId, 2074, 1, 12);
		    break;
		case 15090:
		    friendsRoll(player, itemId, 2071, 1, 8);
		    break;
		case 15092:
		    friendsRoll(player, itemId, 2070, 1, 10);
		    break;
		case 15094:
		    friendsRoll(player, itemId, 2073, 1, 12);
		    break;
		case 15096:
		    friendsRoll(player, itemId, 2068, 1, 20);
		    break;
		case 15098:
		    friendsRoll(player, itemId, 2075, 1, 100);
		    break;
		case 15100:
		    friendsRoll(player, itemId, 2069, 1, 4);
		    break;
	    }
	} else {
	    switch (itemId) {
		case 15086:
		    privateRoll(player, itemId, 2072, 1, 6);
		    break;
		case 15088:
		    privateRoll(player, itemId, 2074, 1, 12);
		    break;
		case 15090:
		    privateRoll(player, itemId, 2071, 1, 8);
		    break;
		case 15092:
		    privateRoll(player, itemId, 2070, 1, 10);
		    break;
		case 15094:
		    privateRoll(player, itemId, 2073, 1, 12);
		    break;
		case 15096:
		    privateRoll(player, itemId, 2068, 1, 20);
		    break;
		case 15098:
		    privateRoll(player, itemId, 2075, 1, 100);
		    break;
		case 15100:
		    privateRoll(player, itemId, 2069, 1, 4);
		    break;
	    }
	}
    }

    public static void privateRoll(final Player player, final int itemId, int graphic, final int lowest, final int highest) {
	player.packets().sendMessage("Rolling...", true);
	player.addStopDelay(2);
	player.setNextAnimation(new Animation(11900));
	player.setNextGraphics(new Graphics(graphic));
	WorldTasksManager.schedule(new WorldTask() {
	    @Override
	    public void run() {
		player.packets().sendMessage("You rolled <col=db3535>" + getRandom(lowest, highest) + "</col> on " + diceText(itemId) + " die.", true);
	    }
	}, 1);
    }

    public static void friendsRoll(final Player player, final int itemId, int graphic, final int lowest, final int highest) {
	final FriendChatsManager chat = player.getCurrentFriendChat();
	if (chat == null) {
	    player.packets().sendMessage("You need to be in a friend chat to use this option.");
	    return;
	}
	player.addStopDelay(2);
	player.packets().sendMessage("Rolling...");
	player.setNextAnimation(new Animation(11900));
	player.setNextGraphics(new Graphics(graphic));
	WorldTasksManager.schedule(new WorldTask() {
	    @Override
	    public void run() {
		chat.sendDiceMessage(player, "Friends Chat channel-mate <col=db3535>" + player.getDisplayName() + "</col> rolled <col=db3535>" + getRandom(lowest, highest) + "</col> on " + diceText(itemId) + " die.");
	    }
	}, 1);
    }

    public static int getRandom(int lowest, int highest) {
	return Misc.random((highest - lowest) + 1) + lowest;
    }

    public static String diceText(int id) {
	switch (id) {
	    case 15086:
		return "a six-sided";
	    case 15088:
		return "two six-sided";
	    case 15090:
		return "an eight-sided";
	    case 15092:
		return "a ten-sided";
	    case 15094:
		return "a twelve-sided";
	    case 15096:
		return "a a twenty-sided";
	    case 15098:
		return "the percentile";
	    case 15100:
		return "a four-sided";
	}
	return "";
    }

    public static int getGraphic(int id) {
	return 0;
    }
}
