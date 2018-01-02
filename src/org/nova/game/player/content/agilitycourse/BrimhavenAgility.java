package org.nova.game.player.content.agilitycourse;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceMovement;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.controlers.Controller;
import org.nova.utility.misc.Misc;

public final class BrimhavenAgility extends Controller {

    private static final List<Player> players = new ArrayList<Player>();
    private static PlayingGame currentGame;
    private static BladesManager bladesManager;

    private static void removePlayer(Player player) {
	synchronized (players) {
	    players.remove(player);
	    if (player.getSize() == 0)
		cancelGame();
	}
	player.hints().removeUnsavedHintIcon();
	player.packets().closeInterface(player.interfaces().isFullScreen() ? 1 : 11);
    }

    private void addPlayer(Player player) {
	synchronized (players) {
	    players.add(player);
	    if (players.size() == 1)
		startGame();
	    else
		PlayingGame.addIcon(player);
	}
	sendInterfaces();
    }

    private static void startGame() {
	// starts at 0 so that it selects a taggedDispenser
	CoresManager.fastExecutor.scheduleAtFixedRate(currentGame = new PlayingGame(), 0, 60000);
	CoresManager.fastExecutor.scheduleAtFixedRate(bladesManager = new BladesManager(), 5000, 5000); // TODO
													// right
													// time
													// atm
													// they
													// move
													// each
													// 5seconds
    }

    private static void cancelGame() {
	currentGame.cancel();
	bladesManager.cancel();
	PlayingGame.taggedDispenser = null;
	currentGame = null;
	bladesManager = null;
    }

    private static class BladesManager extends TimerTask {

	@Override
	public void run() {
	    /*
	     * for (Player target : players) { for (WorldObject object:
	     * World.getRegion(target.getRegionId()).getObjects()) { if
	     * (object.getId() == 3569 || object.getId() == 3568) {
	     * target.getPackets().sendObjectAnimation(object, new
	     * Animation(1)); } } }
	     */
	}
    }

    private static class PlayingGame extends TimerTask {

	private static Location taggedDispenser;

	private static Location getNextDispenser() {
	    while (true) {
		Location tile = new Location(2761 + 11 * Misc.random(5), 9546 + 11 * Misc.random(5), 3);
		if (!(tile.getX() == 2805 && tile.getY() == 9590) && !(taggedDispenser != null && tile.equals(taggedDispenser)))
		    return tile;
	    }
	}

	private static void addIcon(Player player) {
	    Integer stage = (Integer) player.getTemporaryAttributtes().get("BrimhavenAgility");
	    if (stage != null)
		if (stage == -1) {
		    player.getTemporaryAttributtes().remove("BrimhavenAgility"); // didnt
										 // click
		  } else
		    player.getTemporaryAttributtes().put("BrimhavenAgility", -1); // clicked
	    if (taggedDispenser == null)
		return;
	    player.hints().addHintIcon(taggedDispenser.getX(), taggedDispenser.getY(), taggedDispenser.getZ(), 65, 2, 0, false, false);
	}

	@Override
	public void run() { // selects dispenser
	    taggedDispenser = getNextDispenser();
	    synchronized (players) {
		for (Player player : players)
		    addIcon(player);
	    }
	}

    }

    @Override
    public boolean processObjectClick1(final GlobalObject object) {
	if (object.getId() == 3581 || object.getId() == 3608) {
	    if (PlayingGame.taggedDispenser == null || PlayingGame.taggedDispenser.getTileHash() != object.getTileHash()) {
		return false;
	    }
	    Integer stage = (Integer) player.getTemporaryAttributtes().get("BrimhavenAgility");
	    if (stage == null) {
		player.getTemporaryAttributtes().put("BrimhavenAgility", 0); // clicked
		player.packets().sendMessage("You get tickets by tagging more than one pillar in a row. Tag the next pillar!");
	    } else if (stage == 0) {
		player.packets().sendMessage("You have already tagged this pillar, wait until the arrow moves again.");
	    } else {
		if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsOneItem(2996)) {
		    player.packets().sendMessage("Not enough space in your inventory.");
		    return false;
		}
		player.getTemporaryAttributtes().put("BrimhavenAgility", 0); // clicked
		player.getInventory().addItem(2996, 1);
	    }
	    return false;
	} else if (object.getId() == 3583) {
	    final int rotationY = object.getY() == 9559 ? -1 : 1;
	    player.lock();
	    player.faceLocation(new Location(player.getX(), player.getY() - 1, 3));
	    player.setNextAnimation(new Animation(1121));
	    WorldTasksManager.schedule(new WorldTask() {
		int index = 0;

		@Override
		public void run() {
		    if (index++ >= 7) {
			player.unlock();
			player.getSkills().addXp(Skills.AGILITY, 13.5);
			player.setNextAnimation(new Animation(-1));
			this.stop();
			return;
		    }
		    player.setNextAnimation(new Animation(1122));
		    final Location tile = new Location(player.getX(), player.getY() - rotationY, player.getZ());
		    player.setNextForceMovement(new ForceMovement(tile, 1, 1));
		    WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
			    player.setLocation(new Location(tile));
			    this.stop();
			    return;
			}
		    }, 0, 1);
		}
	    }, 1, 1);
	    return false;
	} else if (object.getId() == 3553) {
	    player.getAppearance().setRenderEmote(155);
	    final Location tile = new Location(player.getX(), player.getY() - object.getRotation(), player.getZ());
	    player.setNextForceMovement(new ForceMovement(tile, 1, 1));
	    return false;
	} else if (object.getId() == 3551) {
	    player.getAppearance().setRenderEmote(155);
	    WorldTasksManager.schedule(new WorldTask() {
		int index = 0;

		@Override
		public void run() {
		    if (index++ >= 7) {
			player.unlock();
			player.getSkills().addXp(Skills.AGILITY, 5);
			player.getAppearance().setRenderEmote(-1);
			this.stop();
			return;
		    }
		    final Location tile = new Location(player.getX(), player.getY() - object.getRotation(), player.getZ());
		    player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
		}
	    }, 0, 0);
	    return false;
	}
	return true;
    }

    @Override
    public void start() {
	addPlayer(player);
    }

    @Override
    public boolean logout() {
	removePlayer(player);
	return false;
    }

    @Override
    public boolean login() {
	addPlayer(player);
	return false; // so doesnt remove script
    }

    @Override
    public void magicTeleported(int type) {
	removePlayer(player);
	removeControler();
    }

    @Override
    public void forceClose() {
	removePlayer(player);
    }

    @Override
    public boolean sendDeath() {
	removePlayer(player);
	removeControler();
	return true;
    }

    @Override
    public void sendInterfaces() {
	player.interfaces().sendTab(player.interfaces().isFullScreen() ? 1 : 11, 5);
    }
}
