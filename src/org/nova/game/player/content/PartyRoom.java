package org.nova.game.player.content;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.item.Item;
import org.nova.game.item.ItemsContainer;
import org.nova.game.player.Player;
import org.nova.utility.loading.items.ItemSetsKeyGenerator;

public class PartyRoom {

    public static int PARTY_CHEST_INTERFACE = 647;
    public static int INVENTORY_INTERFACE = 336;
    private static ItemsContainer<Item> items = new ItemsContainer<Item>(100, false);
    private static final int CHEST_INTERFACE_ITEMS_KEY = ItemSetsKeyGenerator.generateKey();

    public static void openPartyChest(final Player player) {
	player.getTemporaryAttributtes().put("PartyRoomInventory", Boolean.TRUE);
	player.interfaces().sendInterface(PARTY_CHEST_INTERFACE);
	player.interfaces().sendInventoryInterface(INVENTORY_INTERFACE);
	sendOptions(player);
	player.setCloseInterfacesEvent(new Runnable() {
	    @Override
	    public void run() {
		player.getTemporaryAttributtes().remove("PartyRoomInventory");
	    }
	});
    }

    private static void sendOptions(final Player player) {
	player.packets().sendInterSetItemsOptionsScript(INVENTORY_INTERFACE, 0, 93, 4, 7, "Deposit", "Deposit-5", "Deposit-10", "Deposit-All", "Deposit-X");
	player.packets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 0, 27, 1278);
	player.packets().sendInterSetItemsOptionsScript(INVENTORY_INTERFACE, 30, CHEST_INTERFACE_ITEMS_KEY, 4, 7, "Value");
	player.packets().sendIComponentSettings(PARTY_CHEST_INTERFACE, 30, 0, 27, 1150);
	player.packets().sendInterSetItemsOptionsScript(PARTY_CHEST_INTERFACE, 33, CHEST_INTERFACE_ITEMS_KEY, 4, 7, "Examine");
	player.packets().sendIComponentSettings(PARTY_CHEST_INTERFACE, 33, 0, 27, 1026);

    }

    public static int getTotalCoins() {
	int price = 0;
	for (Item item : items.getItems()) {
	    if (item == null)
		continue;
	    price += ItemDefinition.get(item.getId()).getValue(item.getId());
	}
	return price;
    }

    public static void purchase(final Player player, boolean balloons) {
	if (balloons) {
	    if (player.getInventory().containsItem(995, 1000)) {
		// startParty(player);
	    } else {
		player.getMatrixDialogues().startDialogue("SimpleMessage", "Balloon Bonanza costs 1000 coins.");
	    }
	} else {
	    if (player.getInventory().containsItem(995, 500)) {
		startDancingKnights();
	    } else {
		player.getMatrixDialogues().startDialogue("SimpleMessage", "Nightly Dance costs 500 coins.");
	    }
	}
    }

    public static void startDancingKnights() {
	// TODO
    }
}
