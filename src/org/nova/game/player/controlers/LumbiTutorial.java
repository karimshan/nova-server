package org.nova.game.player.controlers;

import org.nova.Constants;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.content.Magic;

/**
 * Lumbridge starter tutorial.
 * @author Raghav
 *
 */
public class LumbiTutorial extends Controller {

	private static final int OZAN_NPC_ID = 7888;
	public static final int TELETAB = 8013;

	@Override
	public void start() {
		sendInterfaces();
	}

	@Override
	public void sendInterfaces() {
		player.interfaces().replaceRealChatBoxInterface(372);
		for (int i = 0; i < 6; i++)
			player.packets().sendIComponentText(372, i, "");
		player.packets().sendIComponentText(372, 0, "Welcome to " + Constants.SERVER_NAME);
		player.packets().sendIComponentText(372, 3, "Talk to Ozan!");
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		if (npc.getId() == OZAN_NPC_ID)
			player.getMatrixDialogues().startDialogue("OzanD", OZAN_NPC_ID, 0);
		return false;
	}

	@Override
	public boolean login() {
		start();
		return false;
	}
	
	@Override
	public boolean logout() {
		return false;
	}
	
	@Override
	public boolean canPlayerOption1(Player target) {
		return false;
	}
	
	@Override
	public boolean canHit(Entity entity) {
		return false;
	}

	@Override
	public boolean processObjectClick1(GlobalObject object) {
		return false;
	}

	@Override
	public boolean processNPCClick2(NPC npc) {
		return false;
	}

	@Override
	public boolean processObjectClick2(GlobalObject object) {
		return false;
	}

	@Override
	public boolean processObjectClick3(GlobalObject object) {
		return false;
	}
	
	@Override
	public boolean sendDeath() {
		return false;
	}

	@Override
	public boolean canAttack(Entity target) {
		return false;
	}

	@Override
	public boolean processMagicTeleport(Location toTile) {
		player.packets().sendMessage("You can't do this at this moment.");
		return false;
	}

	@Override
	public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
		if ((Boolean) player.getTemporaryAttributtes().get("cantmove") != null) {
			return false;
		}
		return true;
	}

	@Override
	public void forceClose() {
		player.getInventory().addItem(1856, 1);
		player.getInventory().addItem(10362, 1);
		player.getInventory().addItem(3867, 1);
		player.getTemporaryAttributtes().remove("cantmove");
		player.interfaces().closeReplacedRealChatBoxInterface();
	}

	@Override
	public boolean handleItemOption1(Player player, int slotId, int itemId, Item item) {
		if (itemId == TELETAB) {
			final Player p = player;
			p.getTemporaryAttributtes().put("cantmove", true);
			p.packets().sendIComponentText(372, 3, "");
			p.getInventory().deleteItem(itemId, 1);
			WorldTasksManager.schedule(new WorldTask() {
				int loop = 0;

				@Override
				public void run() {
					switch (loop) {
					case 0:
						Magic.useTeleTab(p, new Location(3368, 3268, 0));
						break;
					case 1:
						p.getMatrixDialogues().startDialogue("OzanD", OZAN_NPC_ID, 1);
						break;
					case 2:
						Magic.useTeleTab(p, new Location(2442, 3090, 0));
						break;
					case 3:
						p.getMatrixDialogues().startDialogue("OzanD", OZAN_NPC_ID, 2);
						break;
					case 4:
						Magic.useTeleTab(p, new Location(3365, 3083, 0));
						break;
					case 5:
						p.getMatrixDialogues().startDialogue("OzanD", OZAN_NPC_ID, 3);
						break;
					case 6:
						Magic.useTeleTab(p, new Location(3566, 3289, 0));
						break;
					case 7:
						p.getMatrixDialogues().startDialogue("OzanD", OZAN_NPC_ID, 4);
						break;
					case 8:
						Magic.useTeleTab(p, new Location(3223, 3219, 0));
						break;
					case 9:
						p.getMatrixDialogues().startDialogue("OzanD", OZAN_NPC_ID, 5);
						break;
					case 10:
						p.getMatrixDialogues().startDialogue("OzanD", OZAN_NPC_ID, 6);
						break;
					}
					loop++;
				}
			}, 0, 5);
		}
		return true;
	}

}
