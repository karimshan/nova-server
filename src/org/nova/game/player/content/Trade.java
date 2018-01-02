package org.nova.game.player.content;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.nova.game.item.Item;
import org.nova.game.item.ItemsContainer;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.utility.loading.playerutils.SFiles;
import org.nova.utility.misc.Misc;


public class Trade implements Serializable {

	private static final long serialVersionUID = 4297266601674588915L;
	private final Player trader, partner;
	private TradeState currentState = TradeState.STATE_ONE;// thats how my old
															// one started
	private ItemsContainer<Item> traderItemsOffered = new ItemsContainer<Item>(
			28, false);
	private ItemsContainer<Item> partnerItemsOffered = new ItemsContainer<Item>(
			28, false);
	private boolean traderDidAccept, partnerDidAccept;

	/**
	 * Constructor
	 * 
	 * @param trader
	 * @param partner
	 */
	public Trade(Player trader, Player partner) {
		this.trader = trader;
		this.partner = partner;
		trader.getTemporaryAttributtes().put("didRequestTrade", Boolean.FALSE);
		partner.getTemporaryAttributtes().put("didRequestTrade", Boolean.FALSE);
		trader.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
				tradeFailed();

			}

		});
		partner.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
				tradeFailed();

			}

		});
	}

	public void start() {
		trader.faceEntity(partner);
		partner.faceEntity(partner);
//		if (trader.getSession().getIP().equalsIgnoreCase(partner.getSession().getIP())) {
//			trader.getPacketsEncoder().sendMessage("You cannot trade yourself.");
//			partner.getPacketsEncoder().sendMessage("You cannot trade yourself.");
//			finish();
//			return;
//		}
		if (trader.getLocation().getX() == partner.getLocation().getX() && trader.getLocation().getY() == partner.getLocation().getY()) {
			trader.sendMessage("You can't trade in this position.");
			partner.sendMessage("You can't trade in this position.");
			finish();
			return;
		}
		if (!trader.withinDistance(new Location(partner.getX(), partner.getY(), partner.getZ()), 1)) {
			finish();
			return;
		}
		refreshScreen();
		openFirstTradeScreen(trader);
		openFirstTradeScreen(partner);
		refreshScreen();
		if(!trader.getData().getCachedTradeItems().isEmpty())
			trader.getData().getCachedTradeItems().clear();
		if(!partner.getData().getCachedTradeItems().isEmpty())
			partner.getData().getCachedTradeItems().clear();
	}

	/**
	 * 
	 * @return partner
	 */
	public Player getPartner() {
		return partner;
	}

	/**
	 * Sends Options
	 * 
	 * @param p
	 */
	public static void sendOptions(Player p) {
		Object[] tparams1 = new Object[] { "", "", "", "Value<col=FF9040>",
				"Remove-X", "Remove-All", "Remove-10", "Remove-5", "Remove",
				-1, 0, 7, 4, 90, 335 << 16 | 31 };
		p.packets().sendRunScript(150, tparams1);
		p.packets().sendIComponentSettings(335, 31, 0, 27, 1150); // Access
		Object[] tparams3 = new Object[] { "", "", "", "", "", "", "", "",
				"Value<col=FF9040>", -1, 0, 7, 4, 90, 335 << 16 | 34 };
		p.packets().sendRunScript(695, tparams3);
		p.packets().sendIComponentSettings(335, 34, 0, 27, 1026); // Access
		Object[] tparams2 = new Object[] { "", "", "Lend", "Value<col=FF9040>",
				"Offer-X", "Offer-All", "Offer-10", "Offer-5", "Offer", -1, 0,
				7, 4, 93, 336 << 16 };
		p.packets().sendRunScript(150, tparams2);
		p.packets().sendIComponentSettings(336, 0, 0, 27, 1278); // Access
		// refreshScreen();

	}

	/**
	 * Opens first screen
	 * 
	 * @param p
	 */
	public void openFirstTradeScreen(Player p) {
		sendOptions(p);
		p.interfaces().sendInterface(335);
		p.interfaces().sendInventoryInterface(336);
		p.packets().sendItems(90, false, traderItemsOffered);
		p.packets().sendItems(90, true, partnerItemsOffered);
		p.packets().sendIComponentText(335, 37, "");
		String name = p.equals(trader) ? partner.getUsername() : trader
				.getUsername();
		p.packets().sendIComponentText(335, 15,
				"Trading with: " + Misc.formatPlayerNameForDisplay(name));
		p.packets().sendIComponentText(335, 22,
				Misc.formatPlayerNameForDisplay(name));
		refreshScreen();
		if (trader.getLocation().getX() == partner.getLocation().getX() && trader.getLocation().getY() == partner.getLocation().getY()) {
			trader.sendMessage("Cant trade in this position");
			trader.getTrade().endSession();
			partner.sendMessage("Cant trade in this position");
			partner.getTrade().endSession();
			return;
		}
	}

	/**
	 * Opens second screen
	 * 
	 * @param p
	 */
	public void openSecondTradeScreen(Player p) {
		currentState = TradeState.STATE_TWO;
		partnerDidAccept = false;
		traderDidAccept = false;
		p.interfaces().sendInterface(334);
		p.packets().sendIComponentText(334, 54,
				(Misc.formatPlayerNameForDisplay(trader.getUsername())));
		p.packets().sendIComponentText(334, 34,
				"Are you sure you want to make this trade?");
		if (trader.getLocation().getX() == partner.getLocation().getX() && trader.getLocation().getY() == partner.getLocation().getY()) {
			trader.sendMessage("Cant trade in this position");
			trader.getTrade().endSession();
			partner.sendMessage("Cant trade in this position");
			partner.getTrade().endSession();
			return;
		}
	}

	/**
	 * Add's item to inter.
	 * 
	 * @param player
	 * @param slot
	 * @param amount
	 */
	public void addItem(Player player, int slot, int amount) {
		if (currentState == TradeState.STATE_ONE) {
			Item item = player.getInventory().getItem(slot);
			if (item == null)
				return;
			if (!ItemConstants.isTradeable(item)) {
				player.packets().sendMessage(
					"That item isn't tradeable.");
						return;						
	}
//			for (String i : Constants.UNTRADEABLE_ITEMS) {
//				if (item.getName().toLowerCase().contains(i)) {
//						{player.packets().sendMessage("That item isn't tradeable.");
//					return;
//				}
//				}
//			} // TODO
	
			Item itemsBefore[] = getOffer(player).getItemsCopy();
			int maxAmount = player.getInventory().getItems().getNumberOf(item);
			if (amount < maxAmount)
				item = new Item(item.getId(), amount);
			else
				item = new Item(item.getId(), maxAmount);
			getOffer(player).add(item);
			player.getInventory().deleteItem(slot, item);
			refreshItems(player, itemsBefore);
			resetAccept();
			player.getData().getCachedTradeItems().add(item);
			SFiles.savePlayer(player);
		}
		if (currentState == TradeState.STATE_TWO) {
			return;
		}
	}

	/**
	 * saves Logs TODO
	 * 
	 * @param player
	 * @param i
	 */
	@SuppressWarnings("unused")
	private void saveLog(final Player player, final int i) {
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(
					"tradelog.txt", true));
			bf.write("[" + DateFormat.getDateTimeInstance().format(new Date())
					+ " "
					+ Calendar.getInstance().getTimeZone().getDisplayName()
					+ "] " + player.getUsername() + ": " + partnerItemsOffered);
			bf.newLine();
			bf.flush();
			bf.close();
		} catch (IOException ignored) {
		}
	}

	/**
	 * Shows trade icons (IE the red exclamation)
	 * 
	 * @param player
	 * @param slot
	 */
	public static void TradeIcons(Player player, int slot) {
		Object[] tparams4 = new Object[] { slot, 7, 4, 21954593 };
		player.packets().sendRunScript(143, tparams4);
	}

	/**
	 * getOffer for items.
	 * 
	 * @param p
	 * @return
	 */
	private ItemsContainer<Item> getOffer(Player p) {
		return p != partner ? traderItemsOffered : partnerItemsOffered;
	}

	/**
	 * Gets other offer
	 * 
	 * @param other
	 * @return
	 */
	private Player getOther(Player other) {
		return other != partner ? partner : other;
	}

	/**
	 * Removes Item from interface
	 * 
	 * @param player
	 * @param slotId
	 * @param amount
	 */
	public void removeItem(Player player, int slotId, int amount) {
		if (currentState != TradeState.STATE_TWO) {
			Item item = getOffer(player).get(slotId);
			if (item == null)
				return;
			Item itemsBefore[] = getOffer(player).getItemsCopy();
			int maxAmount = getOffer(player).getNumberOf(item);
			if (amount < maxAmount)
				item = new Item(item.getId(), amount);
			else
				item = new Item(item.getId(), maxAmount);
			getOffer(player).remove(slotId, item);
			player.getInventory().addItem(item);
			refreshItems(player, itemsBefore);
			// TradeIcons(partner, slotId);
			// TradeIcons(trader, slotId);
			TradeIcons(getOther(player), slotId);
			resetAccept();
			player.getData().getCachedTradeItems().remove(item);
			SFiles.savePlayer(player);
		}
	}

	/**
	 * refreshes items on inter
	 * 
	 * @param player
	 * @param itemsBefore
	 */
	private void refreshItems(Player player, Item itemsBefore[]) {
		int changedSlots[] = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			Item item = getOffer(player).getItems()[index];
			if (itemsBefore[index] != item)
				changedSlots[count++] = index;
		}
		int finalChangedSlots[] = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refreshScreen();
	}

	/**
	 * Refreshes screen
	 */
	private void refreshScreen() {
		trader.packets().sendItems(90, false, traderItemsOffered);
		trader.packets().sendItems(90, true, partnerItemsOffered);
		partner.packets().sendItems(90, false, partnerItemsOffered);
		partner.packets().sendItems(90, true, traderItemsOffered);
		String name = trader.getUsername();
		partner.packets().sendIComponentText(335, 22,
				Misc.formatPlayerNameForDisplay(name));
		String name1 = partner.getUsername();
		trader.packets().sendIComponentText(335, 22,
				Misc.formatPlayerNameForDisplay(name1));
		trader.packets().sendIComponentText(
				335,
				21,
				" has " + partner.getInventory().getFreeSlots()
						+ " free inventory slots.");
		partner.packets().sendIComponentText(
				335,
				21,
				" has " + trader.getInventory().getFreeSlots()
						+ " free inventory slots.");
		trader.packets().sendGlobalConfig(729, getTradersItemsValue());
		trader.packets().sendGlobalConfig(697, getPartnersItemsValue());
		partner.packets().sendGlobalConfig(729, getPartnersItemsValue());
		partner.packets().sendGlobalConfig(697, getTradersItemsValue());
	}

	/**
	 * Gets trade value
	 * 
	 * @return returns initial price
	 */
	private int getTradersItemsValue() {
		int initialPrice = 0;
		for (Item item : traderItemsOffered.toArray()) {
			if (item != null) {
				initialPrice += item.defs().getValue()
						* item.getAmount();
			}
		}
		return initialPrice;
	}

	/**
	 * gets Value of partner
	 * 
	 * @return regular price
	 */
	private int getPartnersItemsValue() {
		int initialPrice = 0;
		for (Item item : partnerItemsOffered.toArray()) {
			if (item != null) {
				initialPrice += item.defs().getValue()
						* item.getAmount();
			}
		}
		return initialPrice;
	}

	/**
	 * Sets accepted pressed.
	 * 
	 * @param pl
	 */
	public void acceptPressed(Player pl) {
		if (!traderDidAccept && pl.equals(trader)) {
			traderDidAccept = true;
		} else if (!partnerDidAccept && pl.equals(partner)) {
			partnerDidAccept = true;
		}
		switch (currentState) {
		case STATE_ONE:
			if (pl.equals(trader)) {
				if (partnerDidAccept && traderDidAccept) {
					if (!trader.getInventory().getItems()
							.hasSpaceFor(partnerItemsOffered)) {
						partner.packets()
								.sendMessage(
										"Other player does not have enough space in their inventory.");
						trader.packets()
								.sendMessage(
										"You do not have enough space in your inventory.");
						tradeFailed();
						return;
					} else if (!partner.getInventory().getItems()
							.hasSpaceFor(traderItemsOffered)) {
						trader.packets()
								.sendMessage(
										"Other player does not have enough space in their inventory.");
						partner.packets()
								.sendMessage(
										"You do not have enough space in your inventory.");
						tradeFailed();
						return;
					} else if (partner.getInventory().getItems()
							.goesOverAmount(traderItemsOffered)) {
						partner.packets().sendMessage(
								"You'll go over max of an item!");
						trader.packets().sendMessage(
								"They'll go over max of an item!");
						tradeFailed();
						return;
					} else if (trader.getInventory().getItems()
							.goesOverAmount(partnerItemsOffered)) {
						trader.packets().sendMessage(
								"You'll go over max of an item!");
						partner.packets().sendMessage(
								"They'll go over max of an item!");
						tradeFailed();
						return;
					}
					openSecondTradeScreen(trader);
					openSecondTradeScreen(partner);
				} else {
					trader.packets().sendIComponentText(335, 37,
							"Waiting for other player...");
					partner.packets().sendIComponentText(335, 37,
							"Other player has accepted");
				}
			} else if (pl.equals(partner)) {
				if (partnerDidAccept && traderDidAccept) {
					if (!trader.getInventory().getItems()
							.hasSpaceFor(partnerItemsOffered)) {
						partner.packets()
								.sendMessage(
										"Other player does not have enough space in their inventory.");
						trader.packets()
								.sendMessage(
										"You do not have enough space in your inventory.");
						tradeFailed();
						return;
					} else if (!partner.getInventory().getItems()
							.hasSpaceFor(traderItemsOffered)) {
						trader.packets()
								.sendMessage(
										"Other player does not have enough space in their inventory.");
						partner.packets()
								.sendMessage(
										"You do not have enough space in your inventory.");
						tradeFailed();
						return;
					} else if (partner.getInventory().getItems()
							.goesOverAmount(traderItemsOffered)) {
						partner.packets().sendMessage(
								"You'll go over max of an item!");
						trader.packets().sendMessage(
								"They'll go over max of an item!");
						tradeFailed();
						return;
					} else if (trader.getInventory().getItems()
							.goesOverAmount(partnerItemsOffered)) {
						trader.packets().sendMessage(
								"You'll go over max of an item!");
						partner.packets().sendMessage(
								"They'll go over max of an item!");
						tradeFailed();
						return;
					}
					openSecondTradeScreen(trader);
					openSecondTradeScreen(partner);
				} else {
					partner.packets().sendIComponentText(335, 37,
							"Waiting for other player...");
					trader.packets().sendIComponentText(335, 37,
							"Other player has accepted");
				}
			}
			break;

		case STATE_TWO:
			if (pl.equals(trader)) {
				if (partnerDidAccept && traderDidAccept) {
					giveItems();
				} else {
					trader.packets().sendIComponentText(334, 34,
							"Waiting for other player...");
					partner.packets().sendIComponentText(334, 34,
							"Other player has accepted");
				}
			} else if (pl.equals(partner)) {
				if (partnerDidAccept && traderDidAccept) {
					giveItems();
				} else {
					partner.packets().sendIComponentText(334, 34,
							"Waiting for other player...");
					trader.packets().sendIComponentText(334, 34,
							"Other player has accepted");
				}
			}
			break;
		}

	}

	/**
	 * Ends Session of trade
	 */
	public void endSession() {
		trader.interfaces().closeScreenInterface();
		trader.interfaces().closeInventoryInterface();
		partner.interfaces().closeScreenInterface();
		partner.interfaces().closeInventoryInterface();
		traderItemsOffered = null;
		partnerItemsOffered = null;
		trader.setCloseInterfacesEvent(null);
		partner.setCloseInterfacesEvent(null);
		trader.setTrade(null);
		partner.setTrade(null);
		trader.getInventory().refresh();
		partner.getInventory().refresh();
		trader.getTemporaryAttributtes().remove("tradingWith");
		partner.getTemporaryAttributtes().remove("tradingWith");
		trader.getTemporaryAttributtes().remove("receivedTradeRequest");
		partner.getTemporaryAttributtes().remove("receivedTradeRequest");
		trader.getData().getCachedTradeItems().clear();
		partner.getData().getCachedTradeItems().clear();
	}

	/**
	 * WHat happens when tradeFailed
	 */
	public void tradeFailed() {
		trader.getInventory().getItems().addAll(traderItemsOffered);
		partner.getInventory().getItems().addAll(partnerItemsOffered);
		trader.packets().sendMessage("Trade declined.");
		partner.packets().sendMessage("Trade declined.");
		traderItemsOffered = null;
		partnerItemsOffered = null;
		trader.interfaces().closeScreenInterface();
		partner.interfaces().closeScreenInterface();
		trader.interfaces().closeInventoryInterface();
		partner.interfaces().closeInventoryInterface();
		trader.setTrade(null);
		partner.setTrade(null);
		endSession();
		trader.getTemporaryAttributtes().remove("tradingWith");
		partner.getTemporaryAttributtes().remove("tradingWith");
		trader.getTemporaryAttributtes().remove("receivedTradeRequest");
		partner.getTemporaryAttributtes().remove("receivedTradeRequest");
		trader.getInventory().refresh(trader.getInventory().getItems());
		partner.getInventory().refresh(partner.getInventory().getItems());
		trader.getData().getCachedTradeItems().clear();
		partner.getData().getCachedTradeItems().clear();
	}
	
	public void finish() {
		trader.getInventory().getItems().addAll(traderItemsOffered);
		partner.getInventory().getItems().addAll(partnerItemsOffered);
		traderItemsOffered = null;
		partnerItemsOffered = null;
		trader.setCloseInterfacesEvent(null);
		partner.setCloseInterfacesEvent(null);
		trader.interfaces().closeInventoryInterface();
		partner.interfaces().closeInventoryInterface();
		endSession();
		trader.setTrade(null);
		partner.setTrade(null);
		trader.getTemporaryAttributtes().remove("tradingWith");
		partner.getTemporaryAttributtes().remove("tradingWith");
		trader.getTemporaryAttributtes().remove("receivedTradeRequest");
		partner.getTemporaryAttributtes().remove("receivedTradeRequest");
		trader.getInventory().refresh(trader.getInventory().getItems());
		partner.getInventory().refresh(partner.getInventory().getItems());
		trader.getData().getCachedTradeItems().clear();
		partner.getData().getCachedTradeItems().clear();
	}

	/**
	 * Gives items to partner / other
	 */
	private void giveItems() {
		try {
			if (!trader.getInventory().getItems()
					.hasSpaceFor(partnerItemsOffered)) {
				partner.packets()
						.sendMessage(
								"Other player does not have enough space in their inventory.");
				trader.packets().sendMessage(
						"You do not have enough space in your inventory.");
				tradeFailed();
				return;
			} else if (!partner.getInventory().getItems()
					.hasSpaceFor(traderItemsOffered)) {
				trader.packets()
						.sendMessage(
								"Other player does not have enough space in their inventory.");
				partner.packets().sendMessage(
						"You do not have enough space in your inventory.");
				tradeFailed();
				return;
			}
			for (Item itemAtIndex : traderItemsOffered.toArray()) {
				if (itemAtIndex != null) {
					partner.getInventory().addItem(itemAtIndex.getId(),
							itemAtIndex.getAmount());
				}
			}
			for (Item itemAtIndex : partnerItemsOffered.toArray()) {
				if (itemAtIndex != null) {
					trader.getInventory().addItem(itemAtIndex.getId(),
							itemAtIndex.getAmount());
				}
			}
			endSession();
			partner.getInventory().refresh();
			partner.sendMessage("Accepted trade.");
			trader.getInventory().refresh();
			trader.sendMessage("Accepted trade.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * gets items offered
	 * 
	 * @param p
	 * @return
	 */
	public ItemsContainer<Item> getPlayerItemsOffered(Player p) {
		return (p.equals(trader) ? traderItemsOffered : partnerItemsOffered);
	}

	public enum TradeState {
		STATE_ONE, STATE_TWO
	}

	/**
	 * resets the accept trade.
	 */
	public void resetAccept() {
		partnerDidAccept = traderDidAccept = false;
		switch (currentState) {
		case STATE_ONE:
			partner.packets().sendIComponentText(335, 37, "");
			trader.packets().sendIComponentText(335, 37, "");
			break;
		case STATE_TWO:
			partner.packets().sendIComponentText(334, 34, "");
			trader.packets().sendIComponentText(334, 34, "");
			break;
		}
	}

	/**
	 * Gets the state of trade
	 * 
	 * @return state
	 */
	public TradeState getState() {// Where? Just havent implemented, I got
									// everything needed tho XD Nice :P Fuck, I
									// really dont feel like adding more then 3
									// pets -____-
		return currentState;
	}
}