package org.nova.game.player.controlers;

import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.player.content.itemactions.LightSource;
import org.nova.utility.misc.Misc;

public class UnderGroundDungeon extends Controller {

    transient int ticks;
    boolean hasStench, requiresLightSource;
    transient boolean initial;

    @Override
    public void start() {
	init();
	setArguments(null);
    }

    private void init() {
	if (this.getArguments() != null) {
	    hasStench = (boolean) this.getArguments()[0];
	    requiresLightSource = (boolean) this.getArguments()[1];
	}
	ticks = 0;
	initial = true;
	sendInterfaces();
    }

    @Override
    public void sendInterfaces() {
	if (requiresLightSource) {
	    boolean hasLight = LightSource.hasLightSource(player);
	    player.interfaces().sendOverlay(hasLight ? (LightSource.hasExplosiveSource(player) ? 98 : 97) : 96, true);
	    if (!hasLight)
		player.packets().sendBlackOut(2);
	    else
		player.packets().sendBlackOut(0);
	}
    }

    private void checkRequriments() {
	boolean lastInitial = initial;
	  
	if (requiresLightSource) {
	    if (!LightSource.hasLightSource(player)) {
		if (initial)
		    player.packets().sendMessage("You hear tiny insects skittering over the ground...");
		initial = false;
	    } else
		initial = true;
	}
	if (lastInitial != initial)
	    sendInterfaces();
    }

    @Override
    public void process() {
	checkRequriments();
	if (initial)
	    return;
	ticks++;
	if (hasStench) {
	    if (ticks % 12 == 0) {
		player.packets().sendMessage("The strench of the monsters burns your innards.");
		player.applyHit(new Hit(player, 200, HitLook.REGULAR_DAMAGE));
	    }
	}
	if (requiresLightSource) {
	    if (ticks % 2 == 0) {
		if (!LightSource.hasLightSource(player)) {
		    if (!player.isLocked())
			player.applyHit(new Hit(player, Misc.random(10, 100), HitLook.REGULAR_DAMAGE));
		}
	    }
	}
    }

    @Override
    public boolean processObjectClick1(final GlobalObject object) {
	if (object.getId() == 31316) {
	    player.useStairs(-1, new Location(3360, 2971, 0), 1, 2);
	    player.getControllerManager().forceStop();
	    return false;
	} else if (object.getId() == 5946) {
	    player.useStairs(828, new Location(3168, 3171, 0), 1, 2);
	    player.getControllerManager().forceStop();
	    return false;
	} else if (object.getId() == 32944) {
	    player.useStairs(-1, new Location(3219, 9532, 2), 1, 2);
	    player.getControllerManager().forceStop();
	    return false;
	} else if (object.getId() == 31435) {
	    return false;
	} else if (object.getId() == 15811) {
	    player.useStairs(-1, new Location(3749, 2973, 0), 1, 2);
	    return false;
	} else if (object.getId() == 15790) {
	    if (object.getX() == 3829)
		player.useStairs(-1, new Location(3831, 3062, 0), 1, 2);
	    if (object.getX() == 3814)
		player.useStairs(-1, new Location(3816, 3062, 0), 1, 2);
	    player.getControllerManager().forceStop();
	    return false;
	} else if (object.getId() == 15812) {
	    player.useStairs(-1, new Location(3749, 2973, 0), 1, 2);
	    player.getControllerManager().forceStop();
	    return false;
	} else if (object.getId() == 6912) {
	    player.setNextAnimation(new Animation(10578));
	    player.useStairs(-1, object, 1, 2);
	    player.useStairs(10579, new Location(player.getX(), player.getY() == 9601 ? player.getY() + 2 : player.getY() - 2, 0), 1, 2);
	    return false;
	} else if (object.getId() == 6899) {
	    player.setNextAnimation(new Animation(10578));
	    player.useStairs(-1, object, 1, 2);
	    player.useStairs(10579, new Location(3219, 9618, 0), 1, 2);
	    player.getControllerManager().forceStop();
	    player.packets().sendMessage("You squeeze through the hole.");
	    return false;
	} else if (object.getId() == 6439) {
	    player.useStairs(828, new Location(3310, 2961, 0), 1, 2);
	    player.getControllerManager().forceStop();
	    return false;
	} else if (object.getId() == 31390) {
	    player.useStairs(-1, new Location(3318, 9355, 0), 1, 2, "You tumble into the darkness, arriving on a different cave level.");
	    return false;
	} else if (object.getId() == 31367) {
	    player.useStairs(-1, new Location(3338, 9350, 0), 1, 2, "You tumble into the darkness, arriving on a different cave level.");
	    return false;
	}
	return true;
    }

    @Override
    public void magicTeleported(int type) {
	player.getControllerManager().forceStop();
    }

    @Override
    public void forceClose() {
	player.packets().sendBlackOut(0);
	player.interfaces().closeOverlay(true);
    }

    @Override
    public boolean login() {
	init();
	return false;
    }

    @Override
    public boolean logout() {
	return false;
    }
}
