package org.nova.kshan.content.skills.construction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.nova.Constants;
import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.map.Region;
import org.nova.game.map.RegionBuilder;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.content.Magic;
import org.nova.kshan.content.skills.construction.RoomChunk.Roof;

/**
 * 
 * @author K-Shan
 *
 */
public class PlayerHouse implements Serializable {

	private static final long serialVersionUID = -7059592314241248454L;
	
	private Player player;
	
	private transient HouseConstruction construction;
	private transient HouseData houseData;

	private boolean buildingMode = true;
	private boolean arrivingAtPortal = true;
	private boolean privateHouse;
	private boolean purchasedHouse;
	private String houseLocationName;
	
	/**
	 * Used so guests can't enter while region is still loading
	 */
	private transient boolean loadedHouse;
	private transient boolean atHouse;
	private transient boolean cheatMode;
	private transient boolean buildingObject;

	/**
	 * 0 is Basic wood
	 * 1 is Basic stone
	 * 2 is Whitewashed stone
	 * 3 is Fremennik-style wood
	 */
	private int style = 0;
	
	/**
	 * Alternative styles:
	 * 0 is Tropical wood
	 * 1 is Fancy stone
	 * 2 is Zenevivia's dark stone
	 * 3 is Fremennik-style wood
	 */
	private boolean usingAlternativeStyle;

	private transient int[] mapChunks;
	transient int baseX;
	transient int baseY;
	
	private transient List<RoomChunk> temporaryRooms;
	private transient List<NPC> temporaryNpcs;
	private transient List<GlobalObject> temporaryObjects;
	private transient List<Player> cachedGuests;
	
	private List<RoomChunk> savedRooms;
	private List<HouseNPC> savedNpcs;
	private List<HouseObject> savedObjects;
	
	public PlayerHouse(Player player) {
		this.player = player;
		this.savedRooms = new ArrayList<RoomChunk>();
		this.savedNpcs = new ArrayList<HouseNPC>();
		this.savedObjects = new ArrayList<HouseObject>();
	}
	
	/**
	 * Enters the player's house. Should only be calling this on login
	 * @param buildingMode
	 */
	public void enterHouse(boolean buildingMode) {
		this.buildingMode = buildingMode;
		if(construction == null)
			construction = new HouseConstruction(player);
		if(savedRooms.isEmpty())
			houseData.handleConstructionData();
		setDefaults();
		Game.submit(new GameTick(1.052) {
			int ticks = 0;
			@Override
			public void run() {
				if(player.hasFinished() || !Game.containsPlayer(player.getUsername()) 
						|| player.getRandomEvent().getCurrent() == null) {
					stop();
					destroyHouse();
					player.setCoords(getHouseLocation());
					return;
				}
				if(ticks == 0)
					parseLandscape();
				else if(ticks == 2) { // Increased duration of load time (2 secs more)
					player.refreshDynamicMap();
					stop();
				}
				ticks++;
			}
		});
	}
	
	/**
	 * Sets the constructors for the classes that contain info relating to construction.
	 */
	public void setConstructors() {
		this.construction = new HouseConstruction(player);
		this.houseData = new HouseData(player);
	}
	
	/**
	 * Resets the variables.
	 */
	public void resetVariables() {
		Region r = Game.getRegion(player.getRegionId(), true);
		temporaryRooms.clear();
		for(NPC npc : temporaryNpcs)
			npc.finish();
		temporaryNpcs.clear();
		for(GlobalObject o : temporaryObjects)
			Game.removeObject(o, true);
		temporaryObjects.clear();
		for(GlobalObject o : r.getSpawnedObjects())
			Game.removeObject(o, true);
		r.getSpawnedObjects().clear();
		r.getRemovedObjects().clear();
	}
	
	/**
	 * Sets the default variables.
	 */
	public void setDefaults() {
		if(temporaryRooms != null)
			temporaryRooms.clear();
		else
			temporaryRooms = new ArrayList<RoomChunk>();
		if(cachedGuests != null)
			cachedGuests.clear();
		else
			cachedGuests = new ArrayList<Player>();
		if(temporaryNpcs != null)
			temporaryNpcs.clear();
		else
			temporaryNpcs = new ArrayList<NPC>();
		if(temporaryObjects != null)
			temporaryObjects.clear();
		else
			temporaryObjects = new ArrayList<GlobalObject>();
	}

	/**
	 * Destroys the current map that was generated for the player.
	 */
	public void destroyHouse() {
		if(temporaryRooms != null)
			temporaryRooms.clear();
		if(temporaryNpcs != null) {
			for(NPC npc : temporaryNpcs)
				npc.finish();
			temporaryNpcs.clear();
		}
		if(temporaryObjects != null) {
			for(GlobalObject o : temporaryObjects)
				Game.removeObject(o, true);
			temporaryObjects.clear();
		}
		Region r = Game.getRegion(player.getRegionId());
		if(r != null) {
			if(r.getSpawnedObjects() != null) {
				for(GlobalObject o : r.getSpawnedObjects())
					if(o != null)
						Game.removeObject(o, true);
				r.getSpawnedObjects().clear();
			}
		}
		setDefaults();
		if(getMapChunks() != null)
			RegionBuilder.destroyMap(getMapChunks()[0], getMapChunks()[1], 8, 8);
		mapChunks = null;
		if(getCachedGuests() != null && !getCachedGuests().isEmpty())
			getCachedGuests().clear();
	}
	
	/**
	 * Sets the player's location in the first garden room.
	 */
	public void teleportToHouse() {
		player.setLocation(new Location((getBaseX() + 37), (getBaseY() + 35), 0));
	}
	
	/**
	 * Creates the house
	 */
	public void parseLandscape() {
		parseLandscape(true);
	}
	
	/**
	 * Creates the house.
	 */
	public void parseLandscape(boolean refresh) {
		setLoadedHouse(false);
		if(temporaryRooms.isEmpty())
			houseData.addRooms();
		Object[][][][] landscapeData = new Object[4][8][8][];
		if(getMapChunks() == null)
			mapChunks = RegionBuilder.findEmptyChunkBound(8, 8);
		baseX = (getMapChunks()[0] << 3);
		baseY = (getMapChunks()[1] << 3);
		for (RoomChunk roomsList : temporaryRooms) {
			landscapeData[roomsList.getZ()][roomsList.getX()][roomsList.getY()] = new Object[] {
					roomsList.getRoom().getChunkX(), roomsList.getRoom().getChunkY(),
					roomsList.getRotation(), roomsList.getRoom().isShowingRoof() };
		}
		if(!inBuildingMode()) { // Makes the roofs if the player isn't in building mode
			for (int x = 1; x < 7; x++) {
				skipY: for (int y = 1; y < 7; y++) {
					for (int plane = 2; plane >= 0; plane--) {
						if (landscapeData[plane][x][y] != null) {
							boolean hasRoof = (boolean) landscapeData[plane][x][y][3];
							if (hasRoof) {
								byte rotation = (byte) landscapeData[plane][x][y][2];
								landscapeData[plane + 1][x][y] = new Object[] {
										Roof.ROOF1.getChunkX(), Roof.ROOF1.getChunkY(),
										rotation, true };
								continue skipY;
							}
						}
					}
				}
			}
		}
		for (int plane = 0; plane < landscapeData.length; plane++) {
			for (int x = 0; x < landscapeData[plane].length; x++) {
				for (int y = 0; y < landscapeData[plane][x].length; y++) {
					if (landscapeData[plane][x][y] != null) // This copies the room data onto the land data.
						RegionBuilder.copyChunk((int) landscapeData[plane][x][y][0] + (isUsingAlternativeStyle() && style != 3 ? 8 : 0), 
							(int) landscapeData[plane][x][y][1], style,
							getMapChunks()[0] + x, getMapChunks()[1] + y, plane, (byte) landscapeData[plane][x][y][2]);
					else if (plane == 0) // If the rooms have been set, it'll continue to make the rest of the region land.
						RegionBuilder.copyChunk(RoomChunk.GRASS[0], RoomChunk.GRASS[1],
							(style == 2 && isUsingAlternativeStyle() ? 0 : style), 
							getMapChunks()[0] + x, getMapChunks()[1] + y, plane, 0);
				}
			}
		}
		if(refresh)
			houseData.refreshSpawnedData();
		if(Game.containsPlayer(player.getUsername()) && player.hasStarted())
			player.refreshMap();
	}

	/**
	 * Refreshes the configs.
	 */
	public void refreshConfigs() {
		player.packets().sendConfigByFile(6450, isArrivingAtPortal() ? 1 : 0);
		player.packets().sendConfigByFile(2176, inBuildingMode() ? 1 : 0);
		player.packets().sendGlobalConfig(944, getSavedRooms().size());
	}
	
	/**
	 * Gets the room from the list of rooms
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public RoomChunk findRoom(int x, int y, int z) {
		for(RoomChunk r : temporaryRooms)
			if(r != null)
				if(r.getX() == x && r.getY() == y && r.getZ() == z)
					return r;
		return null;
	}
	
	/**
	 * Returns the difference of the player's x to the base's x.
	 * @param currentX
	 * @return
	 */
	public int getOffsetX(int currentX) {
		return (currentX - getBaseX());
	}
	
	/**
	 * Returns the difference of the player's y to the bases' y.
	 * @param currentY
	 * @return
	 */
	public int getOffsetY(int currentY) {
		return (currentY - getBaseY());
	}
	
	/**
	 * Refreshes objects for another player.
	 * @param other
	 */
	public void refreshData(final Player other) {
		for(GlobalObject ob : player.getHouse().getTemporaryObjects())
			other.packets().sendSpawnedObject(ob);
	}
	
	/**
	 * Returns the objects in a room chunk
	 * @param chunk
	 * @return
	 */
	public List<GlobalObject> getObjectsInRoom(RoomChunk chunk) {
		List<GlobalObject> toReturn = new ArrayList<GlobalObject>();
		for(GlobalObject obj : Game.getLocalObjects(player, 50))
			if(getCurrentRoom(obj) == chunk)
				toReturn.add(obj);
		return toReturn;
	}
	
	/**
	 * 
	 * @param roomChunk
	 * @param remove
	 */
	public void viewRoom(RoomChunk roomChunk, boolean remove) {
		for(GlobalObject object : houseData.getObjectsFromRoom(roomChunk, true, true, false))
			if (remove) {
				Game.removeObject(object, true);
				player.getRegion().getRemovedObjects().remove(object);
			} else
				Game.spawnRemoveObject(object, true);
	}
	
	/**
	 * 
	 * @param toAdd
	 */
	public void addPlayer(final Player toAdd) {
		addPlayer(toAdd, false);
	}
	
	/**
	 * Adds a player to the original {@code House}
	 * @param toAdd
	 */
	public void addPlayer(final Player toAdd, boolean loggingIn) {
		if(!loggingIn) {
			Magic.telePlayer(toAdd, getMiddleLocation(), true);
			Game.submit(new GameTick(2.41) {
				
				@Override
				public void run() { // Gotta have all these checks to avoid bugs :)
					if(toAdd.hasFinished() || !Game.containsPlayer(toAdd.getUsername())) {
						stop();
						toAdd.setCoords(getHouseLocation());
						toAdd.getRandomEvent().fullyStop();
						toAdd.getHouse().setAtHouse(false);
						return;
					}
					if(player.getHouse().inBuildingMode()) {
						toAdd.sm(player.getDisplayName()+"'s house has just gone under construction.");
						toAdd.getRandomEvent().fullyStop();
						toAdd.setLocation(player.getHouse().getHouseLocation());
						toAdd.getHouse().setAtHouse(false);
						stop();
						return;
					}
					refreshData(toAdd);
					if(!toAdd.getUsername().equals(player.getUsername())) {
						getCachedGuests().add(toAdd);
						if(Game.containsPlayer(player.getUsername()))
							player.sm(toAdd.getDisplayName()+" has just entered your house!");
					} else
						player.sm("Welcome back home.");
					toAdd.getHouse().setAtHouse(true);
					stop();
				}
			});
		} else {
			toAdd.packets().sendWindowsPane(399, 0);
			Game.submit(new GameTick(2.3) {
				
				@Override
				public void run() {
					if(toAdd.hasFinished() || !Game.containsPlayer(toAdd.getUsername())) {
						toAdd.setCoords(getHouseLocation());
						toAdd.getRandomEvent().fullyStop();
						stop();
						return;
					}
					if(player.getHouse().inBuildingMode()) {
						toAdd.sm(player.getDisplayName()+"'s house has just gone under construction.");
						toAdd.getRandomEvent().fullyStop();
						toAdd.setLocation(player.getHouse().getHouseLocation());
						stop();
						return;
					}
					HashMap<String, Object> data = toAdd.getRandomEvent().getData();
					int[] lastLocationOffset = (int[]) data.get("lastLocationOffset");
					if(lastLocationOffset == null) {
						toAdd.setLocation(getMiddleLocation());
						return;
					}
					int offsetX = lastLocationOffset[0];
					int offsetY = lastLocationOffset[1];
					int z = lastLocationOffset[2];
					Location previousLocation = new Location((getBaseX() + offsetX), (getBaseY() + offsetY), z);
					toAdd.setLocation(previousLocation);
					toAdd.interfaces().sendDefaultPane();
					refreshData(toAdd);
					if(!toAdd.getUsername().equals(player.getUsername())) {
						getCachedGuests().add(toAdd);
						if(Game.containsPlayer(player.getUsername()))
							player.sm(toAdd.getDisplayName()+" has just entered your house!");
					} else {
						player.sm("Welcome back home.");
						player.getHouse().setLoadedHouse(true);
					}
					stop();
				}
			});
		}
	}
	
	/**
	 * Returns the map chunks
	 * @return
	 */
	public int[] getMapChunks() {
		return mapChunks;
	}

	/**
	 * The base X coord.
	 * @return
	 */
	public int getBaseX() {
		return baseX;
	}
	
	/**
	 * The Base Y coord.
	 * @return
	 */
	public int getBaseY() {
		return baseY;
	}
	
	/**
	 * Returns the location in the middle.
	 * @return
	 */
	public Location getMiddleLocation() {
		if(mapChunks != null)
			return new Location(getBaseX() + 37, getBaseY() + 35, player.getZ());
		else
			return getHouseLocation();
	}

	/**
	 * Returns the number of portals in the player's house.
	 * @return
	 */
	public int getPortalCount() {
		int count = 0;
		for(GlobalObject o : getTemporaryObjects())
			if(o.getId() == 13405)
				count++;
		return count;
	}

	/**
	 * Returns the spawned npcs.
	 * @return
	 */
	public List<NPC> getTemporaryNPCs() {
		return temporaryNpcs;
	}

	/**
	 * Returns the spawned objects
	 * @return
	 */
	public List<GlobalObject> getTemporaryObjects() {
		return temporaryObjects;
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void setObjects(List<GlobalObject> obj) {
		this.temporaryObjects = obj;
	}

	/**
	 * Returns if the player is in building mode.
	 * @return
	 */
	public boolean inBuildingMode() {
		return buildingMode;
	}
	
	/**
	 * 
	 * @param b
	 */
	public void setInBuildingMode(boolean b) {
		this.buildingMode = b;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isArrivingAtPortal() {
		return arrivingAtPortal;
	}

	/**
	 * 
	 * @param arrivingAtPortal
	 */
	public void setArrivingAtPortal(boolean arrivingAtPortal) {
		this.arrivingAtPortal = arrivingAtPortal;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getStyle() {
		return style;
	}
	
	/**
	 * Returns the name of the style from the style
	 * @return
	 */
	public String getStyleName() {
		switch(getStyle()) {
			case 0:
				return isUsingAlternativeStyle() ? "Tropical wood" : "Basic wood";
			case 1:
				return isUsingAlternativeStyle() ? "Fancy stone" : "Basic stone";
			case 2:
				return isUsingAlternativeStyle() ? "Zenevivia's dark stone" : "Whitewashed stone";
			case 3:
				return "Fremennik-style wood";
		}
		return "Unknown";
	}
	
	public boolean hasCheatMode() {
		return cheatMode;
	}

	public void setCheatMode(boolean cheatMode) {
		this.cheatMode = cheatMode;
	}

	/**
	 * Returns the rooms in the house.
	 * @return
	 */
	public List<RoomChunk> getTemporaryRooms() {
		return temporaryRooms;
	}

	/**
	 * Returns the portal location.
	 * @return
	 */
	public String getHouseLocationName() {
		if(houseLocationName == null)
			houseLocationName = "Rimmington";
		return houseLocationName;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setHouseLocationName(String name) {
		this.houseLocationName = name;
	}
	
	/**
	 * Grabs the portal where your house is at.
	 * @return
	 */
	public Location getHouseLocation() {
		if(getHouseLocationName() == null)
			setHouseLocationName("Rimmington");
		switch(getHouseLocationName()) {
			case "Rimmington": 
				return new Location(2954, 3224, 0);
			case "Taverly":
				return new Location(2895, 3465, 0);
			case "Pollniveach":
				return new Location(3340, 3004, 0);
			case "Rellekka":
				return new Location(2670, 3633, 0);
			case "Brimhaven":
				return new Location(2760, 3178, 0);
			case "Yanille":
				return new Location(2544, 3093, 0);
		}
		return Constants.START_PLAYER_LOCATION;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isPrivateHouse() {
		return privateHouse;
	}

	/**
	 * 
	 * @param privateHouse
	 */
	public void setPrivateHouse(boolean privateHouse) {
		this.privateHouse = privateHouse;
	}
	
	/**
	 * Returns the room X
	 * @param o
	 * @return
	 */
	public int getRoomX(int chunkX) {
		return chunkX - getMapChunks()[0];
	}
	
	/**
	 * Returns the room Y
	 * @param o
	 * @return
	 */
	public int getRoomY(int chunkY) {
		return chunkY - getMapChunks()[1];
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Player> getCachedGuests() {
		return cachedGuests;
	}

	/**
	 * Returns whether the person has guests or not.
	 * @return
	 */
	public boolean hasGuests() {
		return getCachedGuests() != null && !getCachedGuests().isEmpty();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isAtHouse() {
		return atHouse;
	}

	/**
	 * 
	 * @param atHouse
	 */
	public void setAtHouse(boolean atHouse) {
		this.atHouse = atHouse;
	}

	/**
	 * Used so guests can't enter while region is still loading
	 * @return
	 */
	public boolean hasLoadedHouse() {
		return loadedHouse;
	}
	
	/**
	 * 
	 * @param style
	 */
	public void setStyle(int style) {
		this.style = style;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isUsingAlternativeStyle() {
		return usingAlternativeStyle;
	}

	/**
	 * 
	 * @param usingAlternativeStyle
	 */
	public void setUsingAlternativeStyle(boolean usingAlternativeStyle) {
		this.usingAlternativeStyle = usingAlternativeStyle;
	}

	/**
	 * 
	 * @param loadedHouse
	 */
	public void setLoadedHouse(boolean loadedHouse) {
		this.loadedHouse = loadedHouse;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasPurchasedHouse() {
		return purchasedHouse;
	}

	/**
	 * 
	 * @param purchasedHouse
	 */
	public void setPurchasedHouse(boolean purchasedHouse) {
		this.purchasedHouse = purchasedHouse;
	}
	
	public boolean isBuildingObject() {
		return buildingObject;
	}

	public void setBuildingObject(boolean buildingObject) {
		this.buildingObject = buildingObject;
	}
	
	/**
	 * Returns the window id to use with the style of the house.
	 * @return
	 */
	public int getWindowFromStyle() {
		switch(getStyle()) {
			case 0:
				return isUsingAlternativeStyle() ? 13733 : 13730;
			case 1:
				return isUsingAlternativeStyle() ? 13731 : 13728;
			case 2:
				return isUsingAlternativeStyle() ? 7101 : 13732;
			case 3:
				return 13729;
		}
		return inBuildingMode() ? 13830 : 13117; // If somehow the themes glitch, then it'll use the default windows
	}
	
	public HouseConstruction getConstruction() {
		return construction;
	}

	public HouseData getHouseData() {
		return houseData;
	}

	public void setHouseData(HouseData houseData) {
		this.houseData = houseData;
	}

	public void setConstruction(HouseConstruction construction) {
		this.construction = construction;
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void refreshRoomObjects(GlobalObject obj, boolean spawn) {
		if(spawn) {
			Game.spawnRemoveObject(obj, true);
			getTemporaryObjects().add(obj);
			return;
		}
		Game.spawnRemoveObject(houseData.getBuildObject(obj), true);
	}
	
	/**
	 * Returns the current room anything that has a location is in.
	 */
	public RoomChunk getCurrentRoom(Location location) {
		for(RoomChunk c : temporaryRooms)
			if(c.getX() == getRoomX(location.getChunkX()) && c.getY() == getRoomY(location.getChunkY()))
				return c;
		return null;
	}

	public List<RoomChunk> getSavedRooms() {
		return savedRooms;
	}

	public List<HouseNPC> getSavedNpcs() {
		return savedNpcs;
	}

	public List<HouseObject> getSavedObjects() {
		return savedObjects;
	}
	
}