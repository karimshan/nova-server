package org.nova.game.npc.familiar;

import java.io.Serializable;

import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.item.ItemsContainer;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.game.player.content.ItemConstants;
import org.nova.utility.loading.items.ItemSetsKeyGenerator;


public class BeastOfBurden implements Serializable {

	private static final int ITEMS_KEY = ItemSetsKeyGenerator.generateKey();

	/**
	 * 
	 */
	private static final long serialVersionUID = -2090871604834210257L;

	private transient Player player;
	private transient Familiar familiar;

	private ItemsContainer<Item> beastItems;

	public BeastOfBurden(int size) {
		beastItems = new ItemsContainer<Item>(size, false);
	}

	public void setEntitys(Player player, Familiar familiar) {
		this.player = player;
		this.familiar = familiar;
	}

	public void open() {
		player.interfaces().sendInterface(671);
		player.interfaces().sendInventoryInterface(665);
		sendInterItems();
		sendOptions();
	}

	public void dropBob() {
		int size = familiar.getSize();
		Location Location = new Location(familiar.getCoordFaceX(size),
				familiar.getCoordFaceY(size), familiar.getZ());
		for (int i = 0; i < beastItems.getSize(); i++) {
			Item item = beastItems.get(i);
			if (item != null)
				Game.addGroundItem(item, Location, player, false, -1, false);
		}
		beastItems.reset();
	}

	public void takeBob() {
		Item[] itemsBefore = beastItems.getItemsCopy();
		for (int i = 0; i < beastItems.getSize(); i++) {
			Item item = beastItems.get(i);
			if (item != null) {
				if (!player.getInventory().addItem(item))
					break;
				beastItems.remove(i, item);
			}
		}
		beastItems.shift();
		refreshItems(itemsBefore);
	}

	public void removeItem(int slot, int amount) {
		Item item = beastItems.get(slot);
		if (item == null)
			return;
		Item[] itemsBefore = beastItems.getItemsCopy();
		int maxAmount = beastItems.getNumberOf(item);
		if (amount < maxAmount)
			item = new Item(item.getId(), amount);
		else
			item = new Item(item.getId(), maxAmount);
		int freeSpace = player.getInventory().getFreeSlots();
		if (!item.defs().isStackable()) {
			if (freeSpace == 0) {
				player.packets().sendMessage(
						"Not enough space in your inventory.");
				return;
			}
			if (freeSpace < item.getAmount()) {
				item.setAmount(freeSpace);
				player.packets().sendMessage(
						"Not enough space in your inventory.");
			}
		} else {
			if (freeSpace == 0
					&& !player.getInventory().containsItem(item.getId(), 1)) {
				player.packets().sendMessage(
						"Not enough space in your inventory.");
				return;
			}
		}
		beastItems.remove(slot, item);
		beastItems.shift();
		player.getInventory().addItem(item);
		refreshItems(itemsBefore);
	}

	public void addItem(int slot, int amount) {
		Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)
				|| item.getId() == 4049
				|| (familiar.canStoreEssOnly() && item.getId() != 1436 && item
						.getId() != 7936)
				|| item.defs().getValue() > 50000) {
			player.packets().sendMessage("You cannot store this item.");
			return;
		}
		Item[] itemsBefore = beastItems.getItemsCopy();
		int maxAmount = player.getInventory().getItems().getNumberOf(item);
		if (amount < maxAmount)
			item = new Item(item.getId(), amount);
		else
			item = new Item(item.getId(), maxAmount);
		int freeSpace = beastItems.getFreeSlots();
		if (!item.defs().isStackable()) {
			if (freeSpace == 0) {
				player.packets().sendMessage(
						"Not enough space in your Familiar Inventory.");
				return;
			}

			if (freeSpace < item.getAmount()) {
				item.setAmount(freeSpace);
				player.packets().sendMessage(
						"Not enough space in your Familiar Inventory.");
			}
		} else {
			if (freeSpace == 0 && !beastItems.containsOne(item)) {
				player.packets().sendMessage(
						"Not enough space in your Familiar Inventory.");
				return;
			}
		}
		beastItems.add(item);
		beastItems.shift();
		player.getInventory().deleteItem(slot, item);
		refreshItems(itemsBefore);
	}

	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			Item item = beastItems.getItems()[index];
			if (itemsBefore[index] != item) {
				changedSlots[count++] = index;
			}

		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	public void refresh(int... slots) {
		player.packets().sendUpdateItems(ITEMS_KEY, beastItems, slots);
	}

	public void sendOptions() {
		player.packets().sendUnlockIComponentOptionSlots(665, 0, 0, 27, 0,
				1, 2, 3, 4, 5);
		player.packets().sendInterSetItemsOptionsScript(665, 0, 93, 4, 7,
				"Store", "Store-5", "Store-10", "Store-All", "Store-X",
				"Examine");
		player.packets().sendUnlockIComponentOptionSlots(671, 27, 0,
				ITEMS_KEY, 0, 1, 2, 3, 4, 5);
		player.packets().sendInterSetItemsOptionsScript(671, 27, ITEMS_KEY,
				6, 5, "Withdraw", "Withdraw-5", "Withdraw-10", "Withdraw-All",
				"Withdraw-X", "Examine");
	}

	public void sendInterItems() {
		player.packets().sendItems(ITEMS_KEY, beastItems);
		player.packets().sendItems(93, player.getInventory().getItems());
	}

	public ItemsContainer<Item> getBeastItems() {
		return beastItems;
	}
}
