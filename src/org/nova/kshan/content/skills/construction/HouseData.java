package org.nova.kshan.content.skills.construction;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.map.DynamicRegion;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.map.MapUtils;
import org.nova.game.map.Region;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.utility.loading.Logger;

/**
 * Stores methods pertaining to data in a player's house.
 * 
 * @author K-Shan
 *
 */
public class HouseData {
	
	private transient Player player;
	private transient PlayerHouse house;
	
	public HouseData(Player player) {
		this.player = player;
		this.house = player.getHouse();
	}
	
	/**
	 * Refreshes house.getObjects() and npcs with almost no delay.
	 */
	public void refreshDataNoDelay() {
		int[] mapChunks = house.getMapChunks();
		List<NPC> npcs = house.getTemporaryNPCs();
		List<RoomChunk> rooms = house.getTemporaryRooms();
		Region region = Game.getRegion(MapUtils.encode(MapUtils.Structure.REGION, 
				MapUtils.convert(MapUtils.Structure.CHUNK, MapUtils.Structure.REGION, mapChunks)), true);
		if (npcs != null) {
			for (NPC npc : npcs)
				npc.finish();
			npcs.clear();
		}
		if (house.getTemporaryObjects() != null || !house.getTemporaryObjects().isEmpty()) {
			for (GlobalObject o : house.getTemporaryObjects())
				Game.removeObject(o, true);
			house.getTemporaryObjects().clear();
		}
		final Region r = region;
		if (r.getSpawnedObjects() != null) {
			for (GlobalObject o : r.getSpawnedObjects())
				Game.removeObject(o, true);
			r.getSpawnedObjects().clear();
		}
		if(r.getRemovedObjects() != null)
			r.getRemovedObjects().clear();
		if(r.getFloorItems() != null)
			r.getFloorItems().clear();
		house.refreshConfigs();
		for(final RoomChunk roomChunk : rooms) {
			for(GlobalObject spawning : getObjectsFromRoom(roomChunk, true, false, true)) {
				if(spawning.defs().containsOption(4, "Build") 
						&& !spawning.defs().name.toLowerCase().contains("window")) {
					if(house.inBuildingMode())
						Game.spawnRemoveObject(spawning, true);
					else
						Game.removeObject(spawning, true);
				} else if(spawning.defs().name.toLowerCase().contains("window")) {
					spawning = new GlobalObject(house.getWindowFromStyle(), spawning.getType(), 
						spawning.getRotation(), spawning);
					Game.spawnRemoveObject(spawning, true);
				}
			}
		}
		spawnHouseObjects();
		spawnHouseNPCs();
		house.refreshConfigs();
		if (house.getTemporaryObjects() != null)
			for (GlobalObject obj : house.getTemporaryObjects())
				if(obj != null)
					player.packets().sendSpawnedObject(obj);
	}
	
	/**
	 * Refreshes house.getObjects() and npcs.
	 * @param player
	 */
	public void refreshSpawnedData() {
		int[] mapChunks = house.getMapChunks();
		List<NPC> npcs = house.getTemporaryNPCs();
		List<RoomChunk> rooms = house.getTemporaryRooms();
		int[] regionPos = MapUtils.convert(MapUtils.Structure.CHUNK, MapUtils.Structure.REGION, mapChunks);
		final Region region = Game.getRegion(MapUtils.encode(MapUtils.Structure.REGION, regionPos), true);
		if (npcs != null) {
			for (NPC npc : npcs)
				npc.finish();
			npcs.clear();
		}
		if (house.getTemporaryObjects() != null) {
			for (GlobalObject o : house.getTemporaryObjects())
				Game.removeObject(o, true);
			house.getTemporaryObjects().clear();
		}
		final Region r = region;
		if (r != null) {
			if (r.getSpawnedObjects() != null) {
				for (GlobalObject o : r.getSpawnedObjects())
					Game.removeObject(o, true);
				r.getSpawnedObjects().clear();
			}
			if(region.getRemovedObjects() != null)
				region.getRemovedObjects().clear();
			if(region.getFloorItems() != null)
				region.getFloorItems().clear();
		}
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				if (region.getLoadMapStage() != 2)
					return;
				((TimerTask) this).cancel();
				house.refreshConfigs();
				for(final RoomChunk roomChunk : rooms) {
					for(GlobalObject spawning : getObjectsFromRoom(roomChunk, true, false, true)) {
						if(spawning.defs().containsOption("Build")
								&& !spawning.defs().name.toLowerCase().contains("window")) {
							if(house.inBuildingMode())
								Game.spawnRemoveObject(spawning, true);
							else
								Game.removeObject(spawning, true);
						} else if(spawning.defs().name.toLowerCase().contains("window")) {
							spawning = new GlobalObject(house.getWindowFromStyle(), spawning.getType(), 
								spawning.getRotation(), spawning);
							Game.spawnRemoveObject(spawning, true);
						}
					}
				}
				spawnHouseObjects();
				spawnHouseNPCs();
				house.refreshConfigs();
				if (house.getTemporaryObjects() != null)
					for (GlobalObject obj : house.getTemporaryObjects())
						if(obj != null)
							player.packets().sendSpawnedObject(obj);
				house.setLoadedHouse(true);
			}
		}, 2400, 600);
	}
	
	/**
	 * Spawns the house npcs.
	 */
	private void spawnHouseNPCs() {
		house.getTemporaryNPCs().clear();
		for(int i = 0; i < house.getSavedNpcs().size(); i++) {
			HouseNPC houseNPC = house.getSavedNpcs().get(i);
			NPC npc = new NPC(houseNPC.getId(), 
				new Location(house.getBaseX() + houseNPC.getOffsetX(), 
					house.getBaseY() + houseNPC.getOffsetY(), houseNPC.getZ()), 
						houseNPC.hasRandomWalk(), false, houseNPC.getSpawnDirection(), houseNPC.getCustomName());
			house.getTemporaryNPCs().add(npc);
			npc.spawnNPC();
		}
	}
	
	/**
	 * Spawns the objects.
	 */
	private void spawnHouseObjects() {
		house.getTemporaryObjects().clear();
		for(int i = 0; i < house.getSavedObjects().size(); i++) {
			HouseObject ho = house.getSavedObjects().get(i);
			GlobalObject o = new GlobalObject(ho.getId(), ho.getType(), ho.getRotation(),
				new Location(house.getBaseX() + ho.getOffsetX(), house.getBaseY() + ho.getOffsetY(),
					ho.getZ()));
			house.getTemporaryObjects().add(o);
			Game.spawnRemoveObject(o, true);
		}
	}
	
	/**
	 * Adds the rooms.
	 */
	public void addRooms() {
		house.getTemporaryRooms().clear();
		for(int i = 0; i < house.getSavedRooms().size(); i++)
			house.getTemporaryRooms().add(house.getSavedRooms().get(i));
	}
	
	/**
	 * Creates a new construction folder for the player when they go to their house for the first time.
	 */
	public void handleConstructionData() {
		house.getSavedObjects().add(new HouseObject(13405, 0, 10, 35, 35, 0));
		house.getSavedRooms().add(new RoomChunk(RoomList.getRoom("Garden"), 4, 4, 0, 0));
		house.getSavedRooms().add(new RoomChunk(RoomList.getRoom("Parlour"), 4, 5, 0, 0));
		house.setHouseLocationName("Rimmington"); // Rimmington because starter house
		Logger.log("Construction", player.getDisplayName()+" has just purchased a house!");
	}
	
	/**
	 * Writes out the values of the {@value Object} to the corresponding file.
	 * @param fileName
	 * @param data
	 */
	public void addData(Object data) {
		if(data instanceof NPC) {
			NPC npc = (NPC) data;
			HouseNPC hn = new HouseNPC(npc.getId(), 
				house.getOffsetX(npc.getX()), house.getOffsetY(npc.getY()), npc.getZ(), 
					npc.hasRandomWalk(), npc.getFaceDir(), npc.getStoredCustomName());
			if(!house.getSavedNpcs().contains(hn))
				house.getSavedNpcs().add(hn);
			if(!house.getTemporaryNPCs().contains(npc))
				house.getTemporaryNPCs().add(npc);
			npc.spawnNPC();
			npc.changeDirection(npc.getFaceDir());
		} else if(data instanceof GlobalObject) {
			GlobalObject object = (GlobalObject) data;
			HouseObject ho = new HouseObject(object.getId(), object.getRotation(), object.getType(),
				house.getOffsetX(object.getX()), house.getOffsetY(object.getY()), object.getZ());
			if(!house.getSavedObjects().contains(ho))
				house.getSavedObjects().add(ho);
			if(!house.getTemporaryObjects().contains(object))
				house.getTemporaryObjects().add(object);
			house.refreshRoomObjects(object, true);
		} else if(data instanceof RoomChunk) {
			RoomChunk room = (RoomChunk) data;
			if(!house.getSavedRooms().contains(room))
				house.getSavedRooms().add(room);
			if(!house.getTemporaryRooms().contains(room))
				house.getTemporaryRooms().add(room);
		}
	}

	/**
	 * Removes an object from the player's cached object list.
	 * @param obj
	 */
	public void removeData(Object data, boolean... extra) {
		if(data instanceof GlobalObject) {
			GlobalObject obj = (GlobalObject) data;
			HouseObject ho = new HouseObject(obj.getId(), obj.getRotation(), obj.getType(),
				obj.getX() - house.getBaseX(), obj.getY() - house.getBaseY(), obj.getZ());
			house.getTemporaryObjects().remove(obj);
			HouseObject toRemove = null;
			for(HouseObject hobj : house.getSavedObjects())
				if(hobj.matchesCoords(ho))
					toRemove = hobj;
			house.getSavedObjects().remove(toRemove);
			if(extra.length == 1 && extra[0])
				Game.removeObject(obj, true);
			else if(extra.length == 1)
				house.refreshRoomObjects(obj, false);
		} else if(data instanceof NPC) {
			NPC npc = (NPC) data;
			HouseNPC hn = new HouseNPC(npc.getId(), 
				npc.getX() - house.getBaseX(), npc.getY() - house.getBaseY(), npc.getZ(),
					npc.hasRandomWalk(), npc.getFaceDir(), npc.getStoredCustomName());
			HouseNPC toRemove = null;
			for(HouseNPC hnpc : house.getSavedNpcs())
				if(hnpc.matchesCoords(hn))
					toRemove = hnpc;
			house.getSavedNpcs().remove(toRemove);
			house.getTemporaryNPCs().remove(npc);
			npc.finish();
		} else if(data instanceof RoomChunk) {
			RoomChunk r = (RoomChunk) data;
			RoomChunk toRemove = null;
			house.getTemporaryRooms().remove(r);
			for(RoomChunk rc : house.getSavedRooms())
				if(rc.matchesCoords(r))
					toRemove = rc;
			house.getSavedRooms().remove(toRemove);
		}
	}
	
	/**
	 * Returns if th
	 * @param roomChunk
	 * @param moveLocation
	 * @param spawn
	 * @return
	 */
	public List<GlobalObject> getObjectsFromRoom(RoomChunk roomChunk, boolean moveLocation) {
		return getObjectsFromRoom(roomChunk, moveLocation, false, false);
	}
	
	/**
	 * Returns the objects from a room.
	 * @param roomChunk
	 * @param moveLocation
	 * @param viewingRoom
	 * @param keepAllObjects
	 * @return
	 */
	public List<GlobalObject> getObjectsFromRoom(RoomChunk roomChunk, boolean moveLocation, 
			boolean viewingRoom, boolean keepAllObjects) {
		List<GlobalObject> list = new ArrayList<GlobalObject>();
		int boundX = house.getMapChunks()[0] * 8 + roomChunk.getX() * 8;
		int boundY = house.getMapChunks()[1] * 8 + roomChunk.getY() * 8;
		int realChunkX = roomChunk.getRoom().getChunkX();
		int realChunkY = roomChunk.getRoom().getChunkY();
		Region original = Game.getRegion(MapUtils.encode(MapUtils.Structure.REGION, realChunkX / 8, realChunkY / 8));
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				GlobalObject[] objects = original.getObjects(roomChunk.getZ(), (realChunkX & 0x7) * 8 + x, (realChunkY & 0x7) * 8 + y);
				if (objects != null) {
					for (GlobalObject object : objects) {
						if (object == null)
							continue;
						if(!keepAllObjects && !object.defs().containsOption("Build") && viewingRoom)
							continue;
						else if(!keepAllObjects && !viewingRoom && !object.defs().containsOption("Build") && 
							!object.getName().toLowerCase().contains("window"))
							continue;
						if(moveLocation) {
							GlobalObject spawning = new GlobalObject(object);
							int[] coords = DynamicRegion.translate(x, y, roomChunk.getRotation(), 
								object.defs().sizeX, object.defs().sizeY, object.getRotation());
							spawning.setCoords(new Location(boundX + coords[0], boundY + coords[1], roomChunk.getZ()));
							spawning.setRotation((spawning.getRotation() + roomChunk.getRotation()) & 0x3);
							list.add(spawning);
						} else
							list.add(object);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * Returns the 'build' object that corresponds with this object.
	 * @param object
	 * @return
	 */
	public GlobalObject getBuildObject(GlobalObject object) {
		for(GlobalObject regionObject : getObjectsFromRoom(house.getCurrentRoom(object), true, false, true))
			if(regionObject.matchesObject(object) && regionObject.getId() != object.getId())
				return regionObject;
		return null;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
