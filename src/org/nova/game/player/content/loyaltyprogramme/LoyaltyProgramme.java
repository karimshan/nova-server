package org.nova.game.player.content.loyaltyprogramme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

public class LoyaltyProgramme {

	public LoyaltyProgramme(Player player) {
		this.player = player;
	}
	/**
	 * An array of available loyalty shop categories
	 */
	public static final String[] CATEGORIES = { "auras", "emotes", "outfits",
			"titles", "special-offers", "limmited-edition", "recolor",
			"effects" };
	/**
	 * The list of loyalty items
	 */
	private static final HashMap<Integer, LoyaltyItem> ITEMS = new HashMap<Integer, LoyaltyItem>();

	/**
	 * The serial UID
	 */
	private static final long serialVersionUID = -111881367666488484L;

	/**
	 * The loyalty shop interface
	 */
	public static final int INTERFACE_ID = 1143;

	/**
	 * The tab switch config
	 */
	public static final int TAB_CONFIG = 2226;
	/**
	 * Opens the loyalty shop interface
	 */
	public void openShop() {
		player.interfaces().sendScreenInterface(INTERFACE_ID,
				INTERFACE_ID);
		player.packets().sendConfig(TAB_CONFIG, -1);
		currentTab = -1;
		player.packets().sendIComponentText(INTERFACE_ID, 164,
				"" + Misc.formatTypicalInteger(player.getLoyaltyPoints()));
		for (int i = 0; i < 500; i++)
			player.packets().sendIComponentSettings(INTERFACE_ID, i, 0, 300,
					2150);
		player.packets().sendHideIComponent(1143, 117, true);
		for (int i = 0; i < 300; i++) {
			player.packets().sendUnlockIComponentOptionSlots(1143, i, i, i, i);
		}
	}

	/**
	 * The current tab
	 */
	public int currentTab;
	private Player player;
	
	/**
	 * Opens a tab on the loyalty interface
	 * 
	 * @param tab
	 *            The tab to open
	 */
	public void openTab(String tab) {
		switch (tab.toLowerCase().toString()) {
		case "home":
		//	player.getPackets().sendConfig(TAB_CONFIG, -1);
			currentTab = -1;
			player.packets().sendWindowsPane(1143, 0);
			break;
		case "auras":
			//player.getPackets().sendConfig(TAB_CONFIG, 1);
			currentTab = 1;
			player.packets().sendWindowsPane(1143, 0);
			break;
		case "emotes":
			player.packets().sendConfig(TAB_CONFIG, 2);
			currentTab = 2;
			player.packets().sendWindowsPane(1143, 0);
			break;
		case "outfits":
			player.packets().sendConfig(TAB_CONFIG, 3);
			currentTab = 3;
			player.packets().sendWindowsPane(1143, 0);
			break;
		case "titles":
			player.packets().sendConfig(TAB_CONFIG, 4);
			currentTab = 4;
			player.packets().sendWindowsPane(1143, 0);
			break;
		case "recolor":
			player.packets().sendConfig(TAB_CONFIG, 5);
			currentTab = 5;
			player.packets().sendWindowsPane(1143, 0);
			break;
		case "special-offers":
			player.packets().sendConfig(TAB_CONFIG, 6);
			currentTab = 6;
			player.packets().sendWindowsPane(1143, 0);
			break;
		case "limmited-edition":
			player.packets().sendConfig(TAB_CONFIG, 7);
			currentTab = 7;
			player.packets().sendWindowsPane(1143, 0);
			break;
		case "favorites":
			player.packets().sendConfig(TAB_CONFIG, 8);
			currentTab = 8;
			player.packets().sendWindowsPane(1143, 0);
			break;
		case "effects":
			player.packets().sendConfig(TAB_CONFIG, 9);
			currentTab = 9;
			player.packets().sendWindowsPane(1143, 0);
			break;
		default:
			player.packets().sendMessage(
					"This tab is currently un-available"
							+ (player.getRights() >= 2 ? ": " + "\"" + tab
									+ "\"" : "."));
		}
	}
	public void handleButtons(Player player, int componentId, int slotId) {
		switch (componentId) {
		case 109:
			player.packets().sendWindowsPane(
					player.interfaces().isFullScreen() ? 746
							: 548, 0);
			player.getAppearance().generateAppearanceData();
			break;
		case 66:
		case 67:
		case 68:
		case 69:
		case 70:
		case 71:
			openConfirmPurchaseInterface(slotId);
			break;
		case 59:
			resetOverlayBox(true);
			break;
		case 138:
			openPurchaseResultsInterface("outfits", slotId);
			break;
		case 169:// Buy
			//buyItem(slotId, "auras");
			openPurchaseResultsInterface("auras", slotId);// will use attributes.
			break;
		case 103:
			player.interfaces().closeScreenInterface();
			player.closeInterfaces();
			player.packets().closeInterface(INTERFACE_ID);
			player.packets().sendWindowsPane(
					player.interfaces().isFullScreen() ? 746
							: 548, 0);
			player.getAppearance().generateAppearanceData();
			break;
		case 117:
			openTab("favorites");
			break;
		case 111:
			openTab("home");
			break;
		case 132:
			openTab("auras");
			break;
		case 8:
			openTab("effects");
			break;
		case 137:
			openTab("emotes");
			break;
		//case 138:
			//openTab("outfits");
			//break;
		case 143:
			openTab("titles");
			break;
		case 148:
			openTab("recolor");
			break;
		case 153:
			openTab("special-offers");
			break;
		}
	}

	private void resetOverlayBox(boolean b) {
		player.packets().sendWindowsPane(
				player.interfaces().isFullScreen() ? 746
						: 548, 0);
		player.getAppearance().generateAppearanceData();
	}

	/**
	 * Favorites an item
	 * 
	 * @param value
	 *            The item to favorite
	 */
	public void favorite(int value) {
		player.packets().sendConfig(2391, value);
	}
	
	public void updateInterfaceSettings(String category) {
		int hash = 0;
		int power = 2;
		boolean[] items = null;
		if (category != "auras")
			items = new boolean[player.getPurchasedLoyaltyItems(category).length];
		final ArrayList<Integer> auras = player.getPurchasedLoyaltyAuras();
		if (category != "auras") {
			for (int i = 1; i < items.length; i++) {
				if (items[i]) {
					hash += power;
				}
				power *= 2;
			}
			if (items[0])
				hash += 1;
			player.packets().sendConfig(2232, hash);
		} else {
			hash = 0;
			if (auras.contains(0))
				hash += 1;
			player.packets().sendConfig(getBuyConfig(category), hash);
			player.packets().sendConfig(TAB_CONFIG, currentTab);
		}
		player.packets().sendIComponentText(INTERFACE_ID, 127,
				"" + Misc.formatTypicalInteger(player.getLoyaltyPoints()));
		player.packets().sendConfig(TAB_CONFIG, currentTab);
	}

	/**
	 * Buys a loyalty item
	 * 
	 * @param slot
	 *            The slot being clicked on
	 * @param category
	 *            The category for this item
	 */
	public void buyItem(int slot, String category) {
		for (Entry<Integer, LoyaltyItem> stock : ITEMS.entrySet()) {
			if (stock.getKey() != slot)
				continue;
			if (player.getLoyaltyPoints() < stock.getValue().price) {
				player.packets().sendMessage(
						"You cannot afford this loyalty item.");
				return;
			}
			if (category != "auras"
					&& player.getPurchasedLoyaltyItems(category)[slot]
					|| category == "auras"
					&& player.getPurchasedLoyaltyAuras().contains(slot))
				return;
			player.decreaseLoyaltyPoints(stock.getValue().price);
			claim(slot, category);
			switch (category) {
			case "titles":
				player.getAppearance().setTitle(slot);
				player.getAppearance().generateAppearanceData();
				break;
			case "auras":
				if (!player.getInventory().addItem(stock.getValue().id, 1))
					player.getBank().addItem(stock.getValue().id, 1, true);
				break;
			case "outfits":
				for (Item item : getOutfit(slot)) {
					if (!player.getInventory().addItem(item))
						player.getBank().addItem(item.getId(), 1, true);
				}
				break;
			case "emotes":
				break;
			}
		}
	}
	/**
	 * Claims an item
	 * 
	 * @param value
	 *            The item to claim
	 */
	public void claim(int value, String category) {
		player.purchaseLoyaltyItem(value, category);
		updateInterfaceSettings(category);// 33 34
	}

	/**
	 * Returns an outfit corresponding to the slot id
	 * 
	 * @param slot
	 *            The slot ID of the outfit
	 * @return The outfit items for the slot ID
	 */
	private Item[] getOutfit(int slot) {
		return null;
	}
	/**
	 * Returns the buy config for a specific category
	 * 
	 * @param category
	 *            The category to get the buy config for
	 * @return The config ID
	 */
	public int getBuyConfig(String category) {
		return (category == "titles" ? 2232 : category == "emotes" ? 2230
				: category == "outfits" ? 2231 : category == "auras" ? 2229
						: -1);
	}

	/**
	 * Opens the comfirm purchase hidden component
	 * 
	 * @param slotId
	 *            The slot id
	 */
	public void openConfirmPurchaseInterface(int slotId) {
		
		player.packets().sendHideIComponent(INTERFACE_ID, 16, false);
		player.packets().sendHideIComponent(INTERFACE_ID, 56, false);
		player.packets().sendIComponentText(INTERFACE_ID, 45,
				"My Points: "+player.getLoyaltyPoints()+"");
		player.packets().sendIComponentText(INTERFACE_ID, 47,
				"Item Cost: 500");
		player.packets().sendIComponentText(INTERFACE_ID, 48, "");
	}

	/**
	 * Opens the "join loyalty programme" hidden component
	 */
	public void openJoinLoyaltyProgrammeInterface() {
		player.packets().sendHideIComponent(INTERFACE_ID, 16, false);
		player.packets().sendHideIComponent(INTERFACE_ID, 57, false);
	}

	/**
	 * Opens the "Error occured while purchasing" hidden component
	 */
	public void openErrorOccuredInterface() {
		player.packets().sendHideIComponent(INTERFACE_ID, 16, false);
		player.packets().sendHideIComponent(INTERFACE_ID, 58, false);
	}

	/**
	 * Opens the purchase result interface
	 * 
	 * @param category
	 *            The item category
	 * @param slot
	 *            The slot
	 */
	public void openPurchaseResultsInterface(String category, int slotId) {
		try {
			player.packets().sendHideIComponent(INTERFACE_ID, 16, false);
			player.packets().sendHideIComponent(INTERFACE_ID, 58, false);
			player.packets().sendIComponentText(INTERFACE_ID, 161,
					"Your purchase was successful!");
			switch (category) {
			case "emotes":
				player.packets().sendIComponentText(INTERFACE_ID, 162,
						"Xuan teaches you the emote: (emoteNameHere)");
				break;
			case "outfits":
				player.packets().sendIComponentText(INTERFACE_ID, 162,
						"Xuan hands you the costume you purchased.");
				break;
			case "auras":
				player.packets().sendIComponentText(
						INTERFACE_ID,
						162,
						"You recieved the aura: TODO");
				break;
			case "titles":
				player.packets().sendIComponentText(INTERFACE_ID, 162,
						"You have unlocked the title: (titleNameHere)");
				break;
			}
			player.packets().sendIComponentText(INTERFACE_ID, 162,
					"\n \n Click 'Ok' to return to the shop.");
		} catch (Exception e) {
			openErrorOccuredInterface();
		}
	}
}
