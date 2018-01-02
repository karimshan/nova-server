package org.nova.game.player.content;

import java.util.ArrayList;
import java.util.List;

import org.nova.game.Game;
import org.nova.game.item.FloorItem;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * 
 */
public class GroundItemsSpawning {

	private static ArrayList<Item> groundItems;
	
	public static final void load() {
	//	addItem(new Item(1050), 3200, 3200, 0);
	//	addItem(new Item(1050), 3201, 3200, 0);
		//addItem(new Item(1050), 3202, 3200, 0);
	}
	/**
	 * Adds a ground item to given location.
	 */
	public static final void addItem(Item itemId, int x, int y, int plane) {
		itemId.getId();
		Game.addTimedGroundItem(itemId, new Location(x,y,plane));
	}

	public static void refreshSpawnedItems(Player player) {
		for (int regionId : player.getMapRegionsIds()) {
			List<FloorItem> floorItems = Game.getRegion(regionId)
					.getFloorItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave())
						&& player != item.getOwner()
						|| item.getLocation().getZ() != player.getZ())
					continue;
				player.packets().sendRemoveGroundItem(item);
			}
		}
		for (int regionId : player.getMapRegionsIds()) {
			List<FloorItem> floorItems = Game.getRegion(regionId)
					.getFloorItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave())
						&& player != item.getOwner()
						|| item.getLocation().getZ() != player.getZ())
					continue;
				player.packets().sendGroundItem(item);
				player.packets().sendRemoveGroundItem(item);
			}
		}
		load();
	}
	
	public static ArrayList<Item> getGroundItems() {
		return groundItems;
	}

	public static void setGroundItems(ArrayList<Item> groundItems) {
		GroundItemsSpawning.groundItems = groundItems;
	}
}
