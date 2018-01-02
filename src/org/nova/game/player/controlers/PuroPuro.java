package org.nova.game.player.controlers;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceMovement;
import org.nova.game.player.Player;
import org.nova.game.player.actions.Hunter.FlyingEntities;
import org.nova.game.player.content.Magic;
import org.nova.utility.misc.Misc;
public class PuroPuro extends Controller {

    private static final Item[][] REQUIRED = { { new Item(11238, 3), new Item(11240, 2), new Item(11242, 1) }, { new Item(11242, 3), new Item(11244, 2), new Item(11246, 1) }, { new Item(11246, 3), new Item(11248, 2), new Item(11250, 1) }, { null } };

    private static final Item[] REWARD = { new Item(11262, 1), new Item(11259, 1), new Item(11258, 1), new Item(11260, 3) };

    @Override
    public void start() {
	player.packets().sendBlackOut(2);
    }

    @Override
    public void forceClose() {
	player.packets().sendBlackOut(0);
    }

    @Override
    public void magicTeleported(int type) {
	player.getControllerManager().forceStop();
    }

    @Override
    public boolean logout() {
	return false;
    }

    @Override
    public boolean login() {
	start();
	return false;
    }

    @Override
    public boolean processObjectClick1(GlobalObject object) {
	switch (object.getId()) {
	    case 25014:
		player.getControllerManager().forceStop();
		Magic.sendTeleportSpell(player, 6601, -1, 1118, -1, 0, 0, new Location(2427, 4446, 0), 9, false, Magic.OBJECT_TELEPORT);
		return true;
	}
	return true;
    }

    public static void pushThrough(final Player player, GlobalObject object) {
	int objectX = object.getX();
	int objectY = object.getY();
	int direction = 0;
	if (player.getX() == objectX && player.getY() < objectY) {
	    objectY = objectY + 1;
	    direction = ForceMovement.NORTH;
	} else if (player.getX() == objectX && player.getY() > objectY) {
	    objectY = objectY - 1;
	    direction = ForceMovement.SOUTH;
	} else if (player.getY() == objectY && player.getX() < objectX) {
	    objectX = objectX + 1;
	    direction = ForceMovement.EAST;
	} else if (player.getY() == objectY && player.getX() > objectX) {
	    objectX = objectX - 1;
	    direction = ForceMovement.WEST;
	} else {
	    objectY = objectY - 1;
	    objectX = objectX + 1;
	    direction = ForceMovement.SOUTH | ForceMovement.EAST;
	}
	player.packets().sendMessage(Misc.getRandom(2) == 0 ? "You use your strength to push through the wheat in the most efficient fashion." : "You use your strength to push through the wheat.");
	player.faceLocation(object);
	player.setNextAnimation(new Animation(6594));
	player.lock();
	final Location tile = new Location(objectX, objectY, 0);
	player.faceLocation(object);
	player.setNextForceMovement(new ForceMovement(tile, 6, direction));
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		player.unlock();
		player.setLocation(tile);
	    }
	}, 6);
    }

    public static void initPuroImplings() {
	for (int i = 0; i < 5; i++) {
	    for (int index = 0; index < 11; index++) {
		if (i > 2) {
		    if (Misc.getRandom(1) == 0)
			continue;
		}
		Game.spawnNPC(FlyingEntities.values()[index].getNpcId(), new Location(Misc.random(2558 + 3, 2626 - 3), Misc.random(4285 + 3, 4354 - 3), 0), -1, false);
	    }
	}
    }

    public static void openPuroInterface(final Player player) {
	player.interfaces().sendInterface(540); // puro puro
	for (int component = 60; component < 64; component++)
	    player.packets().sendHideIComponent(540, component, false);
	player.setCloseInterfacesEvent(new Runnable() {

	    @Override
	    public void run() {
		player.getTemporaryAttributtes().remove("puro_slot");
	    }
	});
    }

    public static void handlePuroInterface(Player player, int componentId) {
	player.getTemporaryAttributtes().put("puro_slot", (componentId - 20) / 2);
    }

    public static void confirmPuroSelection(Player player) {
	if (player.getTemporaryAttributtes().get("puro_slot") == null)
	    return;
	int slot = (int) player.getTemporaryAttributtes().get("puro_slot");
	Item exchangedItem = REWARD[slot];
	Item[] requriedItems = REQUIRED[slot];
	if (slot == 3) {
	    requriedItems = null;
	    for (Item item : player.getInventory().getItems().getItems()) {
		if (item == null || FlyingEntities.forItem((short) item.getId()) == null)
		    continue;
		requriedItems = new Item[] { item };
	    }
	}
	if (requriedItems == null || !player.getInventory().containsItems(requriedItems)) {
	    player.packets().sendMessage("You don't have the required items.");
	    return;
	}
	if (player.getInventory().addItem(exchangedItem.getId(), exchangedItem.getAmount())) {
	    player.getInventory().removeItems(requriedItems);
	    player.closeInterfaces();
	    player.packets().sendMessage("You exchange the required items for: " + exchangedItem.getName().toLowerCase() + ".");
	}
    }
}
