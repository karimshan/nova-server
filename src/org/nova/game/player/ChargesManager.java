package org.nova.game.player;

import java.io.Serializable;
import java.util.HashMap;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.item.Item;
import org.nova.game.player.content.ItemConstants;
import org.nova.utility.misc.Misc;


public class ChargesManager implements Serializable {

	private static final long serialVersionUID = -5978513415281726450L;

	private transient Player player;

	private HashMap<Integer, Integer> charges;

	public ChargesManager() {
		charges = new HashMap<Integer, Integer>();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void process() {
		Item[] items = player.getEquipment().getItems().getItems();
		for (int slot = 0; slot < items.length; slot++) {
			Item item = items[slot];
			if (item == null)
				continue;
			if (player.getAttackedByDelay() > Misc.currentTimeMillis()) {
				int newId = ItemConstants.getDegradeItemWhenCombating(item
						.getId());
				if (newId != -1) {
					item.setId(newId);
					player.getEquipment().refresh(slot);
					player.getAppearance().generateAppearanceData();
					player.packets().sendMessage(
							"Your " + item.defs().getName()
									+ " degraded.");
				}
			}
			int defaultCharges = ItemConstants.getItemDefaultCharges(item
					.getId());
			if (defaultCharges == -1)
				continue;
			if (ItemConstants.itemDegradesWhileWearing(item.getId()))
				degrade(item.getId(), defaultCharges, slot);
			else if (player.getAttackedByDelay() > Misc.currentTimeMillis())
				degrade(item.getId(), defaultCharges, slot);
		}
	}

	public void die() {
		Item[] equipItems = player.getEquipment().getItems().getItems();
		for (int slot = 0; slot < equipItems.length; slot++) {
			if (equipItems[slot] != null && degradeCompletly(equipItems[slot]))
				player.getEquipment().getItems().set(slot, null);
		}
		Item[] invItems = player.getInventory().getItems().getItems();
		for (int slot = 0; slot < invItems.length; slot++) {
			if (invItems[slot] != null && degradeCompletly(invItems[slot]))
				player.getInventory().getItems().set(slot, null);
		}
	}

	/*
	 * return disapear;
	 */
	public boolean degradeCompletly(Item item) {
		int defaultCharges = ItemConstants.getItemDefaultCharges(item.getId());
		if (defaultCharges == -1)
			return false;
		while (true) {
			if (ItemConstants.itemDegradesWhileWearing(item.getId())
					|| ItemConstants.itemDegradesWhileCombating(item.getId())) {
				charges.remove(item.getId());
				int newId = ItemConstants.getItemDegrade(item.getId());
				if (newId == -1)
					return ItemConstants.getItemDefaultCharges(item.getId()) == -1 ? false
							: true;
				item.setId(newId);
			} else {
				int newId = ItemConstants.getItemDegrade(item.getId());
				if (newId != -1) {
					charges.remove(item.getId());
					item.setId(newId);
				}
				break;
			}
		}
		return false;
	}

	public void wear(int slot) {
		Item item = player.getEquipment().getItems().get(slot);
		if (item == null)
			return;
		int newId = ItemConstants.getDegradeItemWhenWear(item.getId());
		if (newId == -1)
			return;
		player.getEquipment().getItems().set(slot, new Item(newId, 1));
		player.getEquipment().refresh(slot);
		player.getAppearance().generateAppearanceData();
		player.packets().sendMessage(
				"Your " + item.defs().getName() + " degraded.");
	}

	private void degrade(int itemId, int defaultCharges, int slot) {
		Integer c = charges.remove(itemId);
		if (c == null)
			c = defaultCharges;
		else {
			c--;
			if (c == 0) {
				int newId = ItemConstants.getItemDegrade(itemId);
				player.getEquipment().getItems()
						.set(slot, newId != -1 ? new Item(newId, 1) : null);
				if (newId == -1)
					player.packets().sendMessage(
							"Your "
									+ ItemDefinition
											.get(itemId)
											.getName() + " became into dust.");
				else
					player.packets().sendMessage(
							"Your "
									+ ItemDefinition
											.get(itemId)
											.getName() + " degraded.");
				player.getEquipment().refresh(slot);
				player.getAppearance().generateAppearanceData();
				return;
			}
		}
		charges.put(itemId, c);
	}

	  /*
     * -1 inv
     */
    public void addCharges(int id, int amount, int wearSlot) {
	int maxCharges = ItemConstants.getItemDefaultCharges(id);
	if (maxCharges == -1) {
	    System.out.println("This item cant get charges atm " + id);
	    return;
	}
	Integer c = charges.get(id);
	int amt = c == null ? amount : amount + c;
	if (amt > maxCharges)
	    amt = maxCharges;
	if (amt <= 0) {
	    int newId = ItemConstants.getItemDegrade(id);
	    if (newId == -1) {
		if (wearSlot == -1)
		    player.getInventory().deleteItem(id, 1);
		else
		    player.getEquipment().getItems().set(wearSlot, null);
	    } else if (wearSlot == -1) {
		player.getInventory().deleteItem(id, 1);
		player.getInventory().addItem(newId, 1);
	    } else {
		Item item = player.getEquipment().getItem(wearSlot);
		if (item == null)
		    return;
		item.setId(newId);
		player.getEquipment().refresh(wearSlot);
		player.getAppearance().generateAppearanceData();
	    }
	    resetCharges(id);
	} else
	    charges.put(id, amt);
    }
    public void resetCharges(int id) {
    	charges.remove(id);
        }
}
