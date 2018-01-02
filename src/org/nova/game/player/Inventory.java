package org.nova.game.player;

import java.io.Serializable;

import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.item.ItemsContainer;
import org.nova.game.map.Location;
import org.nova.utility.misc.Misc;

public final class Inventory implements Serializable {

	private static final long serialVersionUID = 8842800123753277093L;

	private ItemsContainer<Item> items;

	private transient Player player;

	public static final int INVENTORY_INTERFACE = 679;
	

	public Inventory() {
		items = new ItemsContainer<Item>(28, false);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void init() {
		player.packets().sendItems(93, items);
	}

	public void unlockInventoryOptions() {
		player.packets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 0, 27,
				4554126);
		player.packets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 28, 55,
				2097152);
	}

	public void reset() {
		items.reset();
		init(); // as all slots reseted better just send all again
	}

	public void refresh(int... slots) {
		player.packets().sendUpdateItems(93, items, slots);
	}

	public void addAll(ItemsContainer<Item> items) {
		if (items != null) {
			for (int i = 0; i < items.getSize(); i++) {
				if (items.get(i) != null)
					this.items.add(items.get(i));
			}
		}
	}

	public void recolour(Player player, int originalItem, int colouredItem) {
		player.interfaces().closeChatBoxInterface();
		player.closeInterfaces();
		if (!hasFreeSlots())
			return;
		player.getInventory().deleteItem(new Item(originalItem, 1));
		player.getInventory().addItem(colouredItem, 1);
		player.getInventory().refresh();
	}

	public void refresh(ItemsContainer<Item> items) {
		if (items != null && player != null)
			player.packets().sendItems(93, items);
	}

	public boolean addItem(int itemId, int amount) {
		if (itemId < 0
				|| amount < 0
				|| itemId >= Misc.getItemsSize()
				|| !player.getControllerManager().canAddInventoryItem(itemId,
						amount))
			return false;
		if(!player.getRandomEvent().canAddInventoryItem(itemId, amount))
			return false;
		Item[] itemsBefore = items.getItemsCopy();
		if (!items.add(new Item(itemId, amount))) {
			items.add(new Item(itemId, items.getFreeSlots()));
			player.packets().sendMessage("Not enough space in your inventory.");
			refreshItems(itemsBefore);
			return false;
		}
		refreshItems(itemsBefore);
		return true;
	}

	public boolean addItem(Item item) {
		if (item.getId() < 0
				|| item.getAmount() < 0
				|| item.getId() >= Misc.getItemsSize()
				|| !player.getControllerManager().canAddInventoryItem(
						item.getId(), item.getAmount()))
			return false;
		if(!player.getRandomEvent().canAddInventoryItem(item.getId(), item.getAmount()))
			return false;
		Item[] itemsBefore = items.getItemsCopy();
		if (!items.add(item)) {
			items.add(new Item(item.getId(), items.getFreeSlots()));
			player.packets().sendMessage("Not enough space in your inventory.");
			refreshItems(itemsBefore);
			return false;
		}
		refreshItems(itemsBefore);
		return true;
	}

	public void deleteItem(int slot, Item item) {
		if (!player.getControllerManager().canDeleteInventoryItem(item.getId(),
				item.getAmount()))
			return;
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(slot, item);
		refreshItems(itemsBefore);
	}

	public boolean removeItems(Item... list) {
		if (list == null || list.length == 0) {
			return false;
		}
		for (Item item : list) {
			if (item != null)
				deleteItem(items.getThisItemSlot(item), item);
		}
		refresh();
		return true;
	}

	public void deleteItem(int itemId, int amount) {
		if (!player.getControllerManager()
				.canDeleteInventoryItem(itemId, amount))
			return;
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}

	public void deleteItem(Item item) {
		if (!player.getControllerManager().canDeleteInventoryItem(item.getId(),
				item.getAmount()))
			return;
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(item);
		refreshItems(itemsBefore);
	}

	/*
	 * No refresh needed its client to who does it :p
	 */
	public void switchItem(int fromSlot, int toSlot) {
		Item[] itemsBefore = items.getItemsCopy();
		Item fromItem = items.get(fromSlot);
		Item toItem = items.get(toSlot);
		items.set(fromSlot, toItem);
		items.set(toSlot, fromItem);
		refreshItems(itemsBefore);
	}

	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index])
				changedSlots[count++] = index;
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	public ItemsContainer<Item> getItems() {
		return items;
	}

	public boolean hasFreeSlots() {
		return items.getFreeSlot() != -1;
	}

	public int getFreeSlots() {
		return items.getFreeSlots();
	}

	public Item getItem(int slot) {
		return items.get(slot);
	}

	public int getItemsContainerSize() {
		return items.getSize();
	}

	public boolean containsItems(Item[] item) {
		for (int i = 0; i < item.length; i++)
			if (!items.contains(item[i]))
				return false;
		return true;
	}

	public boolean containsItems(int[] itemIds, int[] ammounts) {
		int size = itemIds.length > ammounts.length ? ammounts.length
				: itemIds.length;
		for (int i = 0; i < size; i++)
			if (!items.contains(new Item(itemIds[i], ammounts[i])))
				return false;
		return true;
	}

	public boolean containsItem(int itemId, int ammount) {
		return items.contains(new Item(itemId, ammount));
	}

	public boolean hasItems(boolean b, int ammount) {
		return items.contains(new Item(ammount));
	}

	public boolean containsOneItem(int... itemIds) {
		for (int itemId : itemIds) {
			if (items.containsOne(new Item(itemId, 1)))
				return true;
		}
		return false;
	}

	public int numberOf(int id) {
		return items.getNumberOf(new Item(id, 1));
	}

	public void sendExamine(int slotId) {
		if (slotId >= getItemsContainerSize())
			return;
		Item item = items.get(slotId);
		if (item == null)
			return;
		if(player.isOwner())
			player.sm("["+item.getId()+" (x"+item.getAmount()+") - "+item.getName()+"] Equip: "+item.getEquipId());
		player.sm(item.getExamine());
	}

	public void replaceItem(int id, int amount, int slot) {
		Item item = items.get(slot);
		if (item == null)
			return;
		item.setId(id);
		item.setAmount(amount);
		refresh(slot);
	}

	public int getAmountOf(int itemId) {
		return items.getNumberOf(itemId);
	}

	public boolean addItemDrop(int itemId, int amount, Location tile) {
		if (itemId < 0
				|| amount < 0
				|| !player.getControllerManager().canAddInventoryItem(itemId,
						amount))
			return false;

		Item[] itemsBefore = items.getItemsCopy();
		if (!items.add(new Item(itemId, amount)))
			Game.addGroundItem(new Item(itemId, amount), tile, player, true,
					180);
		else
			refreshItems(itemsBefore);
		return true;
	}

	public boolean containsItems() {
		return items.getItems() != null;
	}

	public boolean containsItems(int... itemIds) {
		for(int i : itemIds)
			if(!items.containsOne(new Item(i, 1)))
				return false;
		return true;
	}

	public void add(int i, int j) {
		addItem(i, j);
		
	}

	public int lookupSlot(int id) {
		return items.lookupSlot(id);
	}
}