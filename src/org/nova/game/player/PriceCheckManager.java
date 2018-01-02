package org.nova.game.player;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.item.Item;
import org.nova.game.item.ItemsContainer;
import org.nova.game.player.content.ItemConstants;

public class PriceCheckManager {

	private Player player;
	private ItemsContainer<Item> pcInv;

	public PriceCheckManager(Player player) {
		this.player = player;
		pcInv = new ItemsContainer<Item>(28, false);
	}

	public void initPriceCheck() {
		player.interfaces().sendInterface(206);
		player.interfaces().sendInventoryInterface(207);
		sendInterItems();
		sendOptions();
		player.packets().sendGlobalConfig(728, 0);
		for (int i = 0; i < pcInv.getSize(); i++)
			player.packets().sendGlobalConfig(700 + i, 0);
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				player.getInventory().getItems().addAll(pcInv);
				player.getInventory().init();
				pcInv.clear();
			}
		});
	}

	public int getSlotId(int clickSlotId) {
		return clickSlotId / 2;
	}

	public void removeItem(int clickSlotId, int amount) {
		int slot = getSlotId(clickSlotId);
		Item item = pcInv.get(slot);
		if (item == null)
			return;
		Item[] itemsBefore = pcInv.getItemsCopy();
		int maxAmount = pcInv.getNumberOf(item);
		if (amount < maxAmount)
			item = new Item(item.getId(), amount);
		else
			item = new Item(item.getId(), maxAmount);
		pcInv.remove(slot, item);
		player.getInventory().addItem(item);
		refreshItems(itemsBefore);
	}

	public void addItem(int slot, int amount) {
		Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)) {
			player.packets().sendMessage("That item isn't tradeable.");
			return;
		}
		Item[] itemsBefore = pcInv.getItemsCopy();
		int maxAmount = player.getInventory().getItems().getNumberOf(item);
		if (amount < maxAmount)
			item = new Item(item.getId(), amount);
		else
			item = new Item(item.getId(), maxAmount);
		pcInv.add(item);
		player.getInventory().deleteItem(slot, item);
		refreshItems(itemsBefore);
	}

	public void refreshItems(Item[] itemsBefore) {
		int totalPrice = 0;
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			Item item = pcInv.getItems()[index];
			if (item != null)
				totalPrice += ItemDefinition.get(item.getId()).getValue(item.getId())
						* item.getAmount();
			if (itemsBefore[index] != item) {
				changedSlots[count++] = index;
				player.packets()
						.sendGlobalConfig(
								700 + index,
								item == null ? 0 : ItemDefinition.get(item.getId()).getValue(item.getId()));
			}

		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
		player.packets().sendGlobalConfig(728, totalPrice);
	}

	public void refresh(int... slots) {
		player.packets().sendUpdateItems(90, pcInv, slots);
	}

	public void sendOptions() {
		player.packets().sendUnlockIComponentOptionSlots(206, 15, 0, 54, 0,
				1, 2, 3, 4, 5, 6);
		player.packets().sendUnlockIComponentOptionSlots(207, 0, 0, 27, 0,
				1, 2, 3, 4, 5);
		player.packets().sendInterSetItemsOptionsScript(207, 0, 93, 4, 7,
				"Add", "Add-5", "Add-10", "Add-All", "Add-X", "Examine");
	}

	public void sendInterItems() {
		player.packets().sendItems(531, pcInv);
		player.packets().sendItems(93, player.getInventory().getItems());
	}

}
