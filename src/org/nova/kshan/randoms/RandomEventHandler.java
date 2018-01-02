package org.nova.kshan.randoms;

import java.io.Serializable;
import java.util.HashMap;

import org.nova.Constants;
import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.kshan.randoms.dependencies.DynamicMap;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class RandomEventHandler implements Serializable {

	private static final long serialVersionUID = -6613726458724404782L;

	private transient Player player;
	private transient RandomEvent event;
	private transient boolean started;
	private String latterRandomEvent;
	private HashMap<String, Object> savedData;

	public RandomEventHandler() {
		latterRandomEvent = "";
	}

	public void set(Player player) {
		this.player = player;
	}

	public RandomEvent getCurrent() {
		return event;
	}

	public void start(Object obj, Object... temporaryData) {
		if (event != null) {
			if (player.getRights() > 1)
				player.sm("You're already in a random event.");
			return;
		}
		event = (RandomEvent) (obj instanceof RandomEvent ? obj
				: RandomEvents.getRandomEvent(obj));
		if (event == null)
			return;
		event.set(player);
		event.temporaryData = temporaryData;
		if (obj instanceof RandomEvent) {
			String name = ((RandomEvent) obj).getClass().getSimpleName();
			latterRandomEvent = name;
		} else
			latterRandomEvent = (String) obj;
		if (savedData == null)
			savedData = new HashMap<String, Object>();
		if (event.isTraditional())
			event.invokeFirst();
		else
			event.startEvent();
		setStarted(true);
	}

	public void login() {
		if (latterRandomEvent == null || latterRandomEvent == "") {
			return;
		}
		event = RandomEvents.getRandomEvent(latterRandomEvent);
		if (event == null) {
			latterRandomEvent = "";
			return;
		}
		event.set(player);
		if (player == null)
			return;
	
		if (event.getDynamicMapVariables() != null) { // Creates a dynamic map for the player if the variables array isn't empty.
			player.setLocation(0, 0, 0);
			Game.submit(new GameTick(.53) {

				@Override
				public void run() {
					stop();
					event.setDynamicMap(new DynamicMap(event
							.getDynamicMapVariables()[0], event
							.getDynamicMapVariables()[1], event
							.getDynamicMapVariables()[2], event
							.getDynamicMapVariables()[3]));
					event.baseX = event.getDynamicMap().getBaseX();
					event.baseY = event.getDynamicMap().getBaseY();
					event.getDynamicMap().createCopiedMap();
					if (player.getRights() > 1)
						player.sm(event.getDynamicMap().toString());
					if (event.isTraditional()) {
						int[] previousLocationArray = (int[]) getData().get(
								"previousLocation");
						int offsetX = previousLocationArray[0];
						int offsetY = previousLocationArray[1];
						int z = previousLocationArray[2];
						Location previousLocation = new Location(event.baseX
								+ offsetX, event.baseY + offsetY, z);
						player.setLocation(previousLocation);
						player.refreshMap();
						if (getData().get("interactionStage") != null)
							event.interactionStage = (short) getData().get("interactionStage");
						if (getData().get("toWrongLocation") != null)
							event.toWrongLocation = (boolean) getData().get("toWrongLocation");
						if (event.checkLogin()) {
							fullyStop(event.isTraditional() ? true : false);
							return;
						} else
							setStarted(true);
						if (event.hasHiddenMiniMap())
							player.packets().sendBlackOut(2);
						if (event.getTabsRemoved() != null && event.getTabsRemoved().length > 0)
							event.removeTabs();
					}
				}
				
			});

		} else {
			if (getData().get("interactionStage") != null)
				event.interactionStage = (short) getData().get("interactionStage");
			if (getData().get("toWrongLocation") != null)
				event.toWrongLocation = (boolean) getData().get("toWrongLocation");
			if (event.checkLogin()) {
				fullyStop(event.isTraditional() ? true : false);
				return;
			} else
				setStarted(true);
			if (event.hasHiddenMiniMap())
				player.packets().sendBlackOut(2);
			if (event.getTabsRemoved() != null && event.getTabsRemoved().length > 0)
				event.removeTabs();
		}
	}

	public void logout() {
		if (event == null)
			return;
		if (event.getDynamicMap() != null) {
			event.getDynamicMap().destroyMap(); // Gotta destroy it every time or else regions stack
			if (event.isTraditional())
				getData().put("previousLocation",
					new int[] { (player.getX() - event.baseX), (player.getY() - event.baseY), player.getZ() });
		}
		getData().put("interactionStage", event.interactionStage);
		getData().put("toWrongLocation", event.toWrongLocation);
		if(event.checkLogout())
			fullyStop();
	}

	public void fullyStop() {
		fullyStop(true);
	}

	public void fullyStop(boolean teleBack) {
		if (event == null)
			return;
		if (event.getDynamicMap() != null)
			event.getDynamicMap().destroyMap(); // Gotta destroy it every time or else regions stack
		if(teleBack) {
			if (event != null && event.toWrongLocation
					&& getData().get("originalLocation") != null) {
				player.setLocation(RandomEvent.WRONG_LOCATIONS[Misc
						.random(RandomEvent.WRONG_LOCATIONS.length - 1)]);
			} else {
				if(getData() == null)
					player.setLocation(Constants.START_PLAYER_LOCATION);
				else
					player.setLocation((Location) getData().get("originalLocation"));
			}
		}
		if (event.forceStop())
			event.forceStop();
		event = null;
		latterRandomEvent = null;
		setStarted(false);
		if(getData() != null)
			getData().clear();
		player.packets().sendBlackOut(0);
		player.interfaces().refreshAllTabs();
		player.setTimeUntilWrongLocation(-1);
		player.packets().sendResetCamera();
		player.interfaces().setKeepChatboxOpen(false);
		player.setRandomEventTimer(-1);
		player.unlock();
		player.setCantMove(false);
		player.stopAll();
		player.packets().sendRemoveOverlay();
	}

	public boolean hasStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean hasNPCOption1(NPC npc) {
		return event == null || !started ? true : event.hasNPCOption1(npc);
	}

	public boolean hasNPCOption2(NPC npc) {
		return event == null || !started ? true : event.hasNPCOption2(npc);
	}

	public boolean hasNPCOption3(NPC npc) {
		return event == null || !started ? true : event.hasNPCOption3(npc);
	}

	public boolean hasObjectOption1(GlobalObject object) {
		return event == null || !started ? true : event
				.hasObjectOption1(object);
	}

	public boolean hasObjectOption2(GlobalObject object) {
		return event == null || !started ? true : event
				.hasObjectOption2(object);
	}

	public boolean hasObjectOption3(GlobalObject object) {
		return event == null || !started ? true : event.hasObjectOption3(object);
	}

	public boolean hasObjectOption4(GlobalObject object) {
		return event == null || !started ? true : event.hasObjectOption4(object);
	}
	
	public boolean hasObjectOption5(GlobalObject object) {
		return event == null || !started ? true : event.hasObjectOption5(object);
	}

	public boolean canAttackEntity(Entity entity) {
		return event == null || !started ? true : event.canAttackEntity(entity);
	}

	public boolean canClickButtons(int interfaceId, int buttonId, int slotId,
			int packetId) {
		return event == null || !started ? true : event.canClickButtons(
				interfaceId, buttonId, slotId, packetId);
	}

	public boolean canTeleport() {
		return event == null || !started ? true : event.canTeleport();
	}

	public boolean canDropItem(Item item) {
		return event == null || !started ? true : event.canDropItem(item);
	}

	public boolean canPickupItem(Item item) {
		return event == null || !started ? true : event.canPickupItem(item);
	}
	
	public boolean canWearItem(Item item) {
		return event == null || !started ? true : event.canWearItem(item);
	}
	
	public boolean canUseInterfaceOnNPC(NPC npc, int interfaceSlot, int interfaceId, int buttonId) {
		return event == null || !started ? true : event.canUseInterfaceOnNPC(npc, interfaceSlot, interfaceId, buttonId);
	}
	
	public boolean canUseInterfaceOnPlayer(Player usingOn, int interfaceId, int buttonId, int itemId) {
		return event == null || !started ? true : event.canUseInterfaceOnPlayer(usingOn, interfaceId, buttonId, itemId);
	}
	
	public boolean canUseItemOnNPC(NPC npc, Item item) {
		return event == null || !started ? true : event.canUseItemOnNPC(npc, item);
	}

	public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
		return event == null || !started ? true : event.canUseItemOnItem(itemUsed, usedWith);
	}

	public boolean canUseItemOnObject(Item item, GlobalObject object) {
		return event == null || !started ? true : event.canUseItemOnObject(item, object);
	}

	public boolean canUseItemOnPlayer(Player usedOn, int itemId) {
		return event == null || !started ? true : event.canUseItemOnPlayer(usedOn, itemId);
	}
	
	public boolean canAddInventoryItem(int itemId, int amount) {
		return event == null || !started ? true : event.canAddInventoryItem(itemId, amount);
	}

	public boolean canChangeLocation(Location nextLocation) {
		return event == null || !started ? true : event.canChangeLocation(nextLocation);
	}

	public boolean canWalk() {
		return event == null || !started ? true : event.canWalk();
	}
	
	public boolean canLogout() {
		return event == null || !started ? true : event.canLogout();
	}
	
	public void onNPCDeath(NPC npc) {
		if (event == null || !started)
			return;
		else
			event.onNPCDeath(npc);
	}
	
	public void processMovement() {
		if (event == null || !started)
			return;
		else
			event.processMovement();
	}

	public void processEvent() {
		if (event == null || !started)
			return;
		else
			event.processEvent();
	}

	public HashMap<String, Object> getData() {
		return savedData;
	}

}
