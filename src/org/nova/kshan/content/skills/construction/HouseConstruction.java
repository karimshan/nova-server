package org.nova.kshan.content.skills.construction;

import java.util.List;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.kshan.utilities.FormatUtils;

/**
 * Handles any construction related things. (building objects, etc);
 * 
 * @author K-Shan
 *
 */
public class HouseConstruction {
	
	private transient Player player;
	
	public HouseConstruction(Player player) {
		this.player = player;
	}
	
	/**
	 * Sends the build interface for the current build object.
	 * @param object
	 */
	public void sendBuild(GlobalObject object) {
		List<RoomObject> houseObjects = RoomObjects.getBuildablesFromBuild(object);
		if(houseObjects == null) {
			if(player.getRights() > 1)
				player.sm("["+object.getId()+" - "+object.getName()+"] has no house objects to build.");
			return;
		}
		if(houseObjects.size() < 1 || houseObjects.size() > 7) {
			if(player.getRights() > 1)
				player.sm("["+object.getId()+" - "+object.getName()+"] house object size mismatches.");
			player.sm("You cannot build anything here. If you feel this is a mistake, please report it.");
			return;
		}
		int interfaceId = houseObjects.size() <= 3 ? 394 : 396;
		int[] slots = { 0, 2, 4, 6, 1, 3, 5 };
		player.interfaces().sendInterface(interfaceId);
		player.packets().sendInterSetItemsOptionsScript(interfaceId, 11, 8, interfaceId == 394 ? 1 : 2, 4, "Build");
		player.packets().sendUnlockIComponentOptionSlots(interfaceId, 11, 0, interfaceId == 394 ? 3 : 7, 0);
		Item[] itemIds = new Item[7];
		for(int i = 0; i < houseObjects.size(); i++)
			itemIds[interfaceId == 394 ? i : slots[i]] = new Item(houseObjects.get(i).getItemId(), 1);
		player.packets().sendItems(8, itemIds);
		for(int count = 14; count <= (interfaceId == 394 ? 28 : 48); count++)
			player.packets().sendString("", interfaceId, count);
		int[] levels = new int[houseObjects.size()];
		for(int i = 0; i < houseObjects.size(); i++) {
			player.packets().sendConfig(1485 + i, player.getSkills().getLevel(Skills.CONSTRUCTION) >= 
				houseObjects.get(i).getLevelRequired() && player.getInventory().containsItems(
					houseObjects.get(i).getItemsRequired()) ? 1 : 0);
			levels[i] = houseObjects.get(i).getLevelRequired();
			player.packets().sendString("Level: "+levels[i], interfaceId, (interfaceId == 394 ? 32 + i : 56 + i));
			player.packets().sendString(itemIds[interfaceId == 394 ? i : slots[i]].getName(), interfaceId, 14 + (5 * i));
			for(int j = 0; j < 4; j++)
				player.packets().sendString(houseObjects.get(i).getItemsRequired().length <= j ? "" : 
					houseObjects.get(i).getItemsRequired()[j].getName()+": "+houseObjects.get(i).getItemsRequired()[j].getAmount(), 
						interfaceId, 15 + j + (5 * i));
		}
		player.getTemporaryAttributtes().put("buildObject", object);
	}
	
	/**
	 * Handles the clicking of buttons when the build interface is open
	 * @param obj
	 */
	public void handleBuildButtons(int interfaceId, int slotId) {
		int[] slots = { 0, 2, 4, 6, 1, 3, 5 };
		if(interfaceId != 394)
			for(int count = 0; count < slots.length; count++)
				if (slotId == slots[count]) {
					slotId = count;
					break;
				}
		final GlobalObject object = (GlobalObject) player.getTemporaryAttributtes().get("buildObject");
		List<RoomObject> houseObjects = RoomObjects.getBuildablesFromBuild(object);
		if(object == null || houseObjects == null) {
				player.sm("An error has occurred while trying to build an object.");
			return;
		}
		final RoomObject houseObject = houseObjects.get(slotId);
		if(houseObject == null) {
			if(player.getRights() > 1)
				player.sm("There was something wrong with the house object in this build.");
			return;
		}
		if(houseObject.getLevelRequired() > player.getSkills().getLevel(Skills.CONSTRUCTION) && !player.getHouse().hasCheatMode()) {
			player.sm("You need a construction level of "+houseObject.getLevelRequired()+" in order to build this.");
			return;
		}
		if(!player.getInventory().containsItems(houseObject.getItemsRequired()) && !player.getHouse().hasCheatMode()) {
			player.sm("You don't have the required materials needed to build this.");
			return;
		}
		for(Item item : houseObject.getItemsRequired()) {
			if(item.getName().toLowerCase().contains("plank")) {
				if(!player.getInventory().containsItems(new Item[] { new Item(8794, 1), new Item(2347, 1) })
						&& !player.getHouse().hasCheatMode()) {
					player.sm("You need a saw and hammer in order to build this.");
					return;
				}
			}
		}
		player.interfaces().closeScreenInterface();
		player.animate(898);
		player.getHouse().setBuildingObject(true);
		player.getTemporaryAttributtes().put("tempConObject", houseObject.getName().toLowerCase());
		player.setCantMove(true);
		Game.submit(new GameTick(1.6) {
			@Override
			public void run() {
				player.setCantMove(false);
				if(houseObject.getObjectIds().length == 1)
					player.getHouse().getHouseData().addData(new GlobalObject(houseObject.getObjectIds()[0], object.getType(), 
						object.getRotation(), object, true));
				else
					handleMultipleObjectsBuild(object, houseObject);
				player.sm("You make "+FormatUtils.formatForExamine(houseObject.getName().toLowerCase(), false));
				stop();
				player.getHouse().setBuildingObject(false);
				player.getTemporaryAttributtes().remove("tempConObject");
				for(int i = 0; i < houseObject.getItemsRequired().length; i++)
					player.getInventory().deleteItem(houseObject.getItemsRequired()[i]);
				player.getSkills().addXp(Skills.CONSTRUCTION, houseObject.getXp());
			}
			
			@Override
			public boolean isStopped() {
				player.setCantMove(false);
				player.getHouse().setBuildingObject(false);
				player.getTemporaryAttributtes().remove("tempConObject");
				return isStopped;
			}
			
		});
		player.getTemporaryAttributtes().remove("buildObject");
	}
	
	/**
	 * Handles the building of a room
	 * @param object
	 */
	public void buildRoom(GlobalObject object) {
		player.getDialogue().start(new RoomDeletion(), player.getHouse().getCurrentRoom(player), object);
	}

	/**
	 * Deletes the given room.
	 * @param chunk
	 */
	public void deleteRoom(RoomChunk room) {
		player.packets().sendWindowsPane(399, 0);
		for(GlobalObject obj : player.getRegion().getSpawnedObjects())
			if(player.getHouse().getCurrentRoom(obj) == room)
				Game.removeObject(obj, true);
		for(GlobalObject obj : player.getHouse().getTemporaryObjects())
			if(player.getHouse().getCurrentRoom(obj) == room)
				player.getHouse().getHouseData().removeData(obj, true);
		for(NPC n : player.getHouse().getTemporaryNPCs())
			if(player.getHouse().getCurrentRoom(n) == room)
				player.getHouse().getHouseData().removeData(n);
		player.getHouse().getHouseData().removeData(room);
		player.getHouse().resetVariables();
		player.getHouse().setDefaults();
		player.getHouse().parseLandscape();
		Game.submit(new GameTick(2.4) {
			public void run() {
				stop();
				player.interfaces().sendDefaultPane();
				player.getHouse().refreshConfigs();
			}
		});
	}
	
	/**
	 * Handles the creation of rooms.
	 * @param b
	 */
	public void handleRoomCreationButtons(int b) {
		int nextX = player.getXInChunk() == 0 ? player.getHouse().getCurrentRoom(player).getX() - 1 : 
			player.getXInChunk() == 7 ? player.getHouse().getCurrentRoom(player).getX() + 1 : 
				player.getHouse().getCurrentRoom(player).getX();
		int nextY = player.getYInChunk() == 0 ? player.getHouse().getCurrentRoom(player).getY() - 1 : 
			player.getYInChunk() == 7 ? player.getHouse().getCurrentRoom(player).getY() + 1 : 
				player.getHouse().getCurrentRoom(player).getY();
		int z = player.getZ();
		int count = 0;
		for(HouseRoom r : RoomList.getRooms()) {
			if(r.getCost() > 0) {
				if(b == (count + 93)) {
					if(player.getSkills().getLevel(Skills.CONSTRUCTION) < r.getLevelRequired() && !player.getHouse().hasCheatMode()) {
						player.sm("You need a Construction level of "+r.getLevelRequired()+" in order to build this room.");
						return;
					}
					if(!player.getInventory().containsItem(995, r.getCost()) && !player.getHouse().hasCheatMode()) {
						player.sm("You need "+r.getCost()+" coins to build this room.");
						return;
					}
					RoomChunk nextRoom = new RoomChunk(r, nextX, nextY, z, 0); // Default rotation is 0
					player.getHouse().viewRoom(nextRoom, false);
					player.getDialogue().start(new RoomCreation(), nextRoom);
					player.interfaces().closeScreenInterface();
					return;
				}
			}
			if(r.getCost() > 0)
				count++;
		}
	}
	
	/**
	 * Handles the building of more than one object.
	 * @param object	
	 * @param housOobject
	 */
	public void handleMultipleObjectsBuild(GlobalObject object, RoomObject roomObject) {
		switch(object.getName()) {
			case "Rug space":
				int[][] rugIds = { { 15415, 15414, 15413 }, { 15266, 15265, 15264 }, { 15389, 15388, 15387 } };
				for(GlobalObject o : object.getRegion().getSpawnedObjects()) {
					if(player.getHouse().getCurrentRoom(o) == (player.getHouse().getCurrentRoom(player))) {
						for(int i = 0; i < rugIds.length; i++) {
							for(int j = 0; j < rugIds[i].length; j++) {
								if(o.getId() == rugIds[i][j]) {
									player.getHouse().getHouseData().addData(new GlobalObject(roomObject.getObjectIds()[j], 
										o.getType(), o.getRotation(), o, true));
								}
							}
						}
					}
				}
				break;
			case "Habitat space":
				int[] habitatIds = new int[66];
				for(int i = 0; i < 66; i++)
					habitatIds[i] = 44843 + i;
				for(GlobalObject o : player.getHouse().getHouseData().getObjectsFromRoom(
					player.getHouse().getCurrentRoom(player), true, false, true)) {
					if(player.getHouse().getCurrentRoom(o) == player.getHouse().getCurrentRoom(player)) {
						for(int i = 0; i < habitatIds.length; i++) {
							if(o.getId() == habitatIds[i]) {
								player.getHouse().getHouseData().addData(new GlobalObject(roomObject.getObjectIds()[i], 
									o.getType(), o.getRotation(), o, true));
							}
						}
					}
				}
				break;
			case "Combat ring space":
				int[] ids = { 15277, 15278, 15279, 15280, 15281, 15282, 15286, 15287, 
						15289, 15290, 15294, 15295, 15288, 15291, 15293, 15292 };
				for(GlobalObject o : player.getHouse().getHouseData().getObjectsFromRoom(
						player.getHouse().getCurrentRoom(player), true, false, true)) {
						if(player.getHouse().getCurrentRoom(o) == player.getHouse().getCurrentRoom(player)) {
							for(int i = 0; i < ids.length; i++) {
								if(o.getId() == ids[i]) {
									player.getHouse().getHouseData().addData(new GlobalObject(roomObject.getObjectIds()[i], 
										o.getType(), o.getRotation(), o, true));
								}
							}
						}
					}
				break;
		}
	}
	
	/**
	 * Handles the clicking of objects.
	 * @param object
	 * @param myHouse
	 */
	public static void handleObjectClick(Player player, final GlobalObject object, final Player host, final boolean myHouse) {
		switch(object.getId()) {
			case 13405:
				player.getDialogue().start(new LeaveHouse(), host, myHouse);
				break;
		}
	}
	
	/**
	 * Removes multiple house objects
	 * @param o
	 */
	public void removeMultipleObjects(GlobalObject o) {
		String[] objects = { "boxing" };
		for(GlobalObject obj : player.getRegion().getSpawnedObjects()) {
			for(int i = 0; i < objects.length; i++) {
				if((obj.getName().toLowerCase().startsWith(objects[i]) && o.getName().toLowerCase().startsWith(objects[i]))
						&& player.getHouse().getCurrentRoom(o) == player.getHouse().getCurrentRoom(obj)) {
					player.getHouse().getHouseData().removeData(obj, false);
				} else if(o.getName().toLowerCase().startsWith(obj.getName().toLowerCase())
						&& player.getHouse().getCurrentRoom(o) == player.getHouse().getCurrentRoom(obj)) {
					player.getHouse().getHouseData().removeData(obj, false);
				}
			}
		}
	}
	
	/**
	 * Removes the house object or multiple house objects
	 * @param o
	 */
	public void removeObject(GlobalObject o) {
		List<RoomObject> hObjects = null;
		boolean multiple = false;
		hObjects = RoomObjects.getBuildablesFromBuild(player.getHouse().getHouseData().getBuildObject(o));
		for(RoomObject obj : hObjects) {
			for(int i = 0; i < obj.getObjectIds().length; i++) {
				if(obj.getObjectIds().length == 1)
					multiple = false;
				else if(obj.getObjectIds().length > 1 && o.getId() == obj.getObjectIds()[i])
					multiple = true;
			}
		}
		if(multiple)
			removeMultipleObjects(o);
		else
			player.getHouse().getHouseData().removeData(o, false);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
