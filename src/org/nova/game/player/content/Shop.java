package org.nova.game.player.content;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.item.Item;
import org.nova.game.player.CombatDefinitions;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.utility.EconomyPrices;
import org.nova.utility.loading.items.ItemBonuses;
import org.nova.utility.loading.items.ItemExamines;
import org.nova.utility.loading.items.ItemSetsKeyGenerator;


public class Shop {

	private static final int MAIN_STOCK_ITEMS_KEY = ItemSetsKeyGenerator
			.generateKey();

	private static final int MAX_SHOP_ITEMS = 40;
	public static final int COINS = 995;
	public static final int SANTA_HAT = 1050;
	public int id = 0;

	private String name;
	private Item[] mainStock;
	private int[] defaultQuantity;
	private Item[] generalStock;
	private int money;
	private CopyOnWriteArrayList<Player> viewingPlayers;

	public Shop(String name, int money, Item[] mainStock, boolean isGeneralStore, int id) {
		viewingPlayers = new CopyOnWriteArrayList<Player>();
		this.name = name;
		this.money = money;
		this.mainStock = mainStock;
		this.id = id;
		defaultQuantity = new int[mainStock.length];
		for (int i = 0; i < defaultQuantity.length; i++)
			defaultQuantity[i] = mainStock[i].getAmount();
		if (isGeneralStore && mainStock.length < MAX_SHOP_ITEMS)
			generalStock = new Item[MAX_SHOP_ITEMS - mainStock.length];
	}

	public boolean isGeneralStore() {
		return generalStock != null;
	}
	
	public void buyDung(Player player, int clickSlot, int quantity) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		if (item.getAmount() == 0) {
			player.packets().sendMessage(
					"There is no stock of that item at the moment.");
			return;
		}
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getDungPrice(item, dq);
		int amountCoins = player.toks;
		int maxQuantity = amountCoins / price;
		int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();

		boolean enoughCoins = maxQuantity >= buyQ;
		if (!enoughCoins) {
			player.packets().sendMessage("You don't have enough Tokens, you have "+player.toks+" tokens.");
			buyQ = maxQuantity;
		} else if (quantity > buyQ)
			player.packets().sendMessage(
					"The shop has run out of stock.");
		if (item.defs().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.packets().sendMessage(
						"Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.packets().sendMessage(
						"Not enough space in your inventory.");
			}
		}
		if (buyQ != 0) {
			int totalPrice = price * buyQ;
			player.toks -= totalPrice;
			player.getInventory().addItem(item.getId(), buyQ);
			refreshShop();
			sendInventory(player);
		}
	}
	public void buyVote(Player player, int clickSlot, int quantity) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		if (item.getAmount() == 0) {
			player.packets().sendMessage(
					"There is no stock of that item at the moment.");
			return;
		}
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getVotePrice(item, dq);
		int amountCoins = player.VotePoints;
		int maxQuantity = amountCoins / price;
		int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();

		boolean enoughCoins = maxQuantity >= buyQ;
		if (!enoughCoins) {
			player.packets().sendMessage("You don't have enough points, you have "+player.VotePoints+" points.");
			buyQ = maxQuantity;
		} else if (quantity > buyQ)
			player.packets().sendMessage(
					"The shop has run out of stock.");
		if (item.defs().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.packets().sendMessage(
						"Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.packets().sendMessage(
						"Not enough space in your inventory.");
			}
		}
		if (buyQ != 0) {
			int totalPrice = price * buyQ;
			player.VotePoints -= totalPrice;
			player.getInventory().addItem(item.getId(), buyQ);
			refreshShop();
			sendInventory(player);
		}
	}
	
	public void addPlayer(final Player player) {
		viewingPlayers.add(player);
		player.getTemporaryAttributtes().put("Shop", this);
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				viewingPlayers.remove(player);
				player.getTemporaryAttributtes().remove("Shop");
			}
		});
		player.packets().sendConfig(118, MAIN_STOCK_ITEMS_KEY); // sets
																	// mainstock
																	// items set
		player.packets().sendConfig(1496, -1); // sets samples items set
		player.packets().sendConfig(532, money);
		sendStore(player);
		player.packets().sendGlobalConfig(199, -1);// unknown
		player.interfaces().sendInterface(620); // opens shop
		for (int i = 0; i < MAX_SHOP_ITEMS; i++)
			player.packets().sendGlobalConfig(
					946 + i,
					i < defaultQuantity.length ? defaultQuantity[i]
							: generalStock != null ? 0 : -1);// prices
		player.packets().sendGlobalConfig(1241, 16750848);// unknown
		player.packets().sendGlobalConfig(1242, 15439903);// unknown
		player.packets().sendGlobalConfig(741, -1);// unknown
		player.packets().sendGlobalConfig(743, -1);// unknown
		player.packets().sendGlobalConfig(744, 0);// unknown
		if (generalStock != null)
			player.packets().sendHideIComponent(620, 19, false); // unlocks
																	// general
																	// store
																	// icon
		player.packets().sendIComponentSettings(620, 25, 0,
				getStoreSize() * 6, 1150); // unlocks stock slots
		sendInventory(player);
		player.packets().sendIComponentText(620, 20, name);
	}

	public void sendInventory(Player player) {
		player.interfaces().sendInventoryInterface(621);
		player.packets().sendItems(93, player.getInventory().getItems());
		player.packets().sendUnlockIComponentOptionSlots(621, 0, 0, 27, 0,
				1, 2, 3, 4, 5);
		player.packets().sendInterSetItemsOptionsScript(621, 0, 93, 4, 7,
				"Value", "Sell 1", "Sell 5", "Sell 10", "Sell 500", "Examine");
	}

	public int getSlotId(int clickSlotId) {
		return clickSlotId / 6;
	}

	public void buy(Player player, int clickSlot, int quantity) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if(item == null) {
			player.sm("This item is no longer in stock.");
			return;
		}
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
		int amountCoins = player.getInventory().getItems().getNumberOf(money);
		int maxQuantity = amountCoins / price;
		int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();

		boolean enoughCoins = maxQuantity >= buyQ;
		if (!enoughCoins) {
			player.packets().sendMessage("You don't have enough coins.");
			buyQ = maxQuantity;
		} else if (quantity > buyQ)
			player.packets().sendMessage(
					"The shop has run out of stock.");
		if (item.defs().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.packets().sendMessage(
						"Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.packets().sendMessage(
						"Not enough space in your inventory.");
			}
		}
		if (buyQ != 0) {
			int totalPrice = price * buyQ;
			player.getInventory().deleteItem(money, totalPrice);
			player.getInventory().addItem(item.getId(), buyQ);
			item.setAmount(item.getAmount() - buyQ);
			if (item.getAmount() <= 0 && slotId >= mainStock.length)
				generalStock[slotId - mainStock.length] = null;
			refreshShop();
			sendInventory(player);
		}
	}

	public void restoreItems() {
		boolean needRefresh = false;
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i].getAmount() < defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + 1);
				needRefresh = true;
			} else if (mainStock[i].getAmount() > defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + -1);
				needRefresh = true;
			}
		}
		if (generalStock != null) {
			for (int i = 0; i < generalStock.length; i++) {
				Item item = generalStock[i];
				if (item == null)
					continue;
				item.setAmount(item.getAmount() - 1);
				if (item.getAmount() <= 0)
					generalStock[i] = null;
				needRefresh = true;
			}
		}
		if (needRefresh)
			refreshShop();
	}

	private boolean addItem(int itemId, int quantity) {
		for (Item item : mainStock) {
			if (item.getId() == itemId) {
				item.setAmount(item.getAmount() + quantity);
				refreshShop();
				return true;
			}
		}
		if (generalStock != null) {
			for (Item item : generalStock) {
				if (item == null)
					continue;
				if (item.getId() == itemId) {
					item.setAmount(item.getAmount() + quantity);
					refreshShop();
					return true;
				}
			}
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null) {
					generalStock[i] = new Item(itemId, quantity);
					refreshShop();
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean isSellable(int id){
		int[] nosell = {18347, 18349, 18351, 18353, 18355, 18357, 18359, 
				18361, 18363, 18365, 18367, 18369, 18371, 18373, 
				18333, 18334, 18335, 18337, 19893, 19669, 4084, 18746, 18745, 18744,
				15704, 15703, 15702, 15701, 15444, 15443, 15442, 15441, 21999, 21989,
				21979, 21969, 23952, 23942, 23932, 23922, 23912, 23673, 20929, 22985,
				23805, 10404, 1057, 1055, 1053};
		for (int j: nosell){
			if (j != id){
				continue;
			}else if (j == id){
			return true;
			}
		}
		return false;
	}

	public void sell(Player player, int slotId, int quantity) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)	
			return;
		if (isSellable(item.getId())){
			player.sm("You cannot sell this item to the shop.");
			return;
			}
//		for (String s : Constants.UNTRADEABLE_ITEMS) 
//		if (item.getName().toLowerCase().contains(s)) {
//		player.sendMessage("You cannot sell "+item.getName().toLowerCase()+" to a shop.");
//			return;
//		} // TODO
		int originalId = item.getId();
		if (item.defs().isNoted())
			item = new Item(item.defs().getCertId(), item.getAmount());
		if (item.defs().isDestroyItem()
				|| ItemConstants.getItemDefaultCharges(item.getId()) != -1
				|| !ItemConstants.isTradeable(item) || item.getId() == money) {
			player.packets().sendMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == 0 && generalStock == null) {
			player.packets().sendMessage(
					"You can't sell this item to this shop.");
			return;
		}
		int price = ItemDefinition.get(item.getId()).getValue(item.getId());
	//	int price = ItemDefinitions.getItemDefinitions(item.getId()).getValue(item.getId()) / 2;
		int numberOff = player.getInventory().getItems()
				.getNumberOf(originalId);
		if (quantity > numberOff)
			quantity = numberOff;
		if (!addItem(item.getId(), quantity)) {
			player.packets().sendMessage("Shop is currently full.");
			return;
		}
		player.getInventory().deleteItem(originalId, quantity);
		player.getInventory().addItem(money, price * quantity);
	}

	public void sendValue(Player player, int slotId) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
	
		if (item.defs().isNoted())
			item = new Item(item.defs().getCertId(), item.getAmount());
		if (item.defs().isNoted() || !ItemConstants.isTradeable(item)
				|| item.getId() == money) {
			player.packets().sendMessage("You can't sell this item.");
			return;
		}
		if (isSellable(item.getId())){
			player.sm("You cant sell this item to the shop!");
				return;
			}
		int dq = getDefaultQuantity(item.getId());
		if (dq == 0 && generalStock == null) {
			player.packets().sendMessage(
					"You can't sell this item to this shop.");
			return;
		}
		int price = ItemDefinition.get(item.getId()).getValue(item.getId()) / 4;
		player.packets().sendMessage(
				item.defs().getName()
						+ ": shop will buy for: "
						+ price
						+ " "
						+ ItemDefinition.get(money).getName()
								.toLowerCase()
						+ ". Right-click the item to sell.");
	}

	public int getDefaultQuantity(int itemId) {
		for (int i = 0; i < mainStock.length; i++)
			if (mainStock[i].getId() == itemId)
				return defaultQuantity[i];
		return 0;
	}

	public void sendInfo(Player player, int clickSlot) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
	
		player.getTemporaryAttributtes().put("ShopSelectedSlot", clickSlot);
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
		player.packets().sendMessage(
				item.defs().getName()
						+ ": current costs "
						+ price
						+ " "
						+ ItemDefinition.get(money).getName()
								.toLowerCase() + ".");
		player.interfaces().sendInventoryInterface(449);
		player.packets().sendGlobalConfig(741, item.getId());
		player.packets().sendGlobalConfig(743, money);
		player.packets().sendUnlockIComponentOptionSlots(449, 15, -1, 0, 0,
				1, 2, 3, 4); // unlocks buy
		player.packets().sendGlobalConfig(744, price);
		player.packets().sendGlobalConfig(745, 0);
		player.packets().sendGlobalConfig(746, -1);
		player.packets().sendGlobalConfig(168, 98);
		player.packets().sendGlobalString(25, ItemExamines.getExamine(item));
		player.packets().sendGlobalString(34, ""); // quest id for some items
		int[] bonuses = ItemBonuses.getItemBonuses(item.getId());
		if (bonuses != null) {
			HashMap<Integer, Integer> requiriments = item.defs()
					.getWearingSkillRequirements();
			if (requiriments != null && !requiriments.isEmpty()) {
				String reqsText = "";
				for (int skillId : requiriments.keySet()) {
					if (skillId > 24 || skillId < 0)
						continue;
					int level = requiriments.get(skillId);
					if (level < 0 || level > 120)
						continue;
					boolean hasReq = player.getSkills().getLevelFromXP(skillId) >= level;
					reqsText += "<br>"
							+ (hasReq ? "<col=00ff00>" : "<col=ff0000>")
							+ "Level " + level + " "
							+ Skills.SKILL_NAME[skillId];
				}
				player.packets().sendGlobalString(26,
						"<br>Worn on yourself, requiring: " + reqsText);
			} else
				player.packets()
						.sendGlobalString(26, "<br>Worn on yourself");
			player.packets().sendGlobalString(
					35,
					"<br>Attack<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_ATTACK]
							+ "<br><col=ffff00>---" + "<br>Strength"
							+ "<br>Ranged Strength" + "<br>Magic Damage"
							+ "<br>Absorb Melee" + "<br>Absorb Magic"
							+ "<br>Absorb Ranged" + "<br>Prayer Bonus");
			player.packets()
					.sendGlobalString(36,
							"<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning");
			player.packets().sendGlobalString(
					52,
					"<<br>Defence<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SUMMONING_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STRENGTH_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.RANGED_STR_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.MAGIC_DAMAGE]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORB_MELEE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORB_MAGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORB_RANGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.PRAYER_BONUS]);
		} else
			player.packets().sendGlobalString(26, "");

	}

	public void sendVoteInfo(Player player, int clickSlot) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		player.getTemporaryAttributtes().put("ShopSelectedSlot", clickSlot);
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getVotePrice(item, dq);
		player.packets().sendMessage(
				item.defs().getName()
						+ ": current costs "
						+ price
						+ " Vote Points.");
		player.interfaces().sendInventoryInterface(449);
		player.packets().sendGlobalConfig(741, item.getId());
		player.packets().sendGlobalConfig(743, money);
		player.packets().sendUnlockIComponentOptionSlots(449, 15, -1, 0, 0,
				1, 2, 3, 4); // unlocks buy
		player.packets().sendGlobalConfig(744, price);
		player.packets().sendGlobalConfig(745, 0);
		player.packets().sendGlobalConfig(746, -1);
		player.packets().sendGlobalConfig(168, 98);
		player.packets().sendGlobalString(25, ItemExamines.getExamine(item));
		player.packets().sendGlobalString(34, ""); // quest id for some items
		int[] bonuses = ItemBonuses.getItemBonuses(item.getId());
		if (bonuses != null) {
			HashMap<Integer, Integer> requiriments = item.defs()
					.getWearingSkillRequirements();
			if (requiriments != null && !requiriments.isEmpty()) {
				String reqsText = "";
				for (int skillId : requiriments.keySet()) {
					if (skillId > 24 || skillId < 0)
						continue;
					int level = requiriments.get(skillId);
					if (level < 0 || level > 120)
						continue;
					boolean hasReq = player.getSkills().getLevelFromXP(skillId) >= level;
					reqsText += "<br>"
							+ (hasReq ? "<col=00ff00>" : "<col=ff0000>")
							+ "Level " + level + " "
							+ Skills.SKILL_NAME[skillId];
				}
				player.packets().sendGlobalString(26,
						"<br>Worn on yourself, requiring: " + reqsText);
			} else
				player.packets()
						.sendGlobalString(26, "<br>Worn on yourself");
			player.packets().sendGlobalString(
					35,
					"<br>Attack<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_ATTACK]
							+ "<br><col=ffff00>---" + "<br>Strength"
							+ "<br>Ranged Strength" + "<br>Magic Damage"
							+ "<br>Absorb Melee" + "<br>Absorb Magic"
							+ "<br>Absorb Ranged" + "<br>Prayer Bonus");
			player.packets()
					.sendGlobalString(36,
							"<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning");
			player.packets().sendGlobalString(
					52,
					"<<br>Defence<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SUMMONING_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STRENGTH_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.RANGED_STR_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.MAGIC_DAMAGE]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORB_MELEE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORB_MAGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORB_RANGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.PRAYER_BONUS]);
		} else
			player.packets().sendGlobalString(26, "");
	}

	public void sendDungInfo(Player player, int clickSlot) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		player.getTemporaryAttributtes().put("ShopSelectedSlot", clickSlot);
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getDungPrice(item, dq);
		player.packets().sendMessage(
				item.defs().getName()
						+ ": current costs "
						+ price
						+ " Dung Tokens.");
		player.interfaces().sendInventoryInterface(449);
		player.packets().sendGlobalConfig(741, item.getId());
		player.packets().sendGlobalConfig(743, money);
		player.packets().sendUnlockIComponentOptionSlots(449, 15, -1, 0, 0,
				1, 2, 3, 4); // unlocks buy
		player.packets().sendGlobalConfig(744, price);
		player.packets().sendGlobalConfig(745, 0);
		player.packets().sendGlobalConfig(746, -1);
		player.packets().sendGlobalConfig(168, 98);
		player.packets().sendGlobalString(25, ItemExamines.getExamine(item));
		player.packets().sendGlobalString(34, ""); // quest id for some items
		int[] bonuses = ItemBonuses.getItemBonuses(item.getId());
		if (bonuses != null) {
			HashMap<Integer, Integer> requiriments = item.defs()
					.getWearingSkillRequirements();
			if (requiriments != null && !requiriments.isEmpty()) {
				String reqsText = "";
				for (int skillId : requiriments.keySet()) {
					if (skillId > 24 || skillId < 0)
						continue;
					int level = requiriments.get(skillId);
					if (level < 0 || level > 120)
						continue;
					boolean hasReq = player.getSkills().getLevelFromXP(skillId) >= level;
					reqsText += "<br>"
							+ (hasReq ? "<col=00ff00>" : "<col=ff0000>")
							+ "Level " + level + " "
							+ Skills.SKILL_NAME[skillId];
				}
				player.packets().sendGlobalString(26,
						"<br>Worn on yourself, requiring: " + reqsText);
			} else
				player.packets()
						.sendGlobalString(26, "<br>Worn on yourself");
			player.packets().sendGlobalString(
					35,
					"<br>Attack<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_ATTACK]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_ATTACK]
							+ "<br><col=ffff00>---" + "<br>Strength"
							+ "<br>Ranged Strength" + "<br>Magic Damage"
							+ "<br>Absorb Melee" + "<br>Absorb Magic"
							+ "<br>Absorb Ranged" + "<br>Prayer Bonus");
			player.packets()
					.sendGlobalString(36,
							"<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning");
			player.packets().sendGlobalString(
					52,
					"<<br>Defence<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STAB_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SUMMONING_DEF]
							+ "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.STRENGTH_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.RANGED_STR_BONUS]
							+ "<br><col=ffff00>"
							+ bonuses[CombatDefinitions.MAGIC_DAMAGE]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORB_MELEE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORB_MAGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.ABSORB_RANGE_BONUS]
							+ "%<br><col=ffff00>"
							+ bonuses[CombatDefinitions.PRAYER_BONUS]);
		} else
			player.packets().sendGlobalString(26, "");
	}
	  public int getVotePrice(Item item, int dq) {
		  switch(item.getId()) {
		 case 1053:
			 return 115;
		 case 1055:
			 return 115;
		 case 1057:
			 return 115;
		 case 10404:
			 return 15;
		 case 23805:
			 return 2;
		 case 22985:
			 return 5;
		 case 20929:
			 return 21;
		 case 23673:
			 return 28;
		 case 23912:
		 case 23922:
		 case 23932:
		 case 23942:
		 case 23952:
		 case 21969:
		 case 21979:
		 case 21989:
		 case 21999:
			 return 5;
		 case 15441:
		 case 15442:
		 case 15443:
		 case 15444:
		 case 15701:
		 case 15702:
		 case 15703:
		 case 15704:
			 return 7;
		 case 18744:
		 case 18745:
		 case 18746:
			 return 10;
		 case 4084:
			return 49;
		}
             
		return -1;
	}
	  
	  public int getDungPrice(Item item, int dq) {
		  switch(item.getId()) {
		/*Chaotic*/
		case 18349:
		return 200000;
		case 18351:
		return 200000;
		case 18353:
		return 200000;
		case 18355:
		return 200000;
		case 18357:
		return 200000;
		case 18359:
		return 200000;
		case 18361:
		return 200000;
		case 18363:
		return 200000;
		/*Chaotic*/
		
		/*Arcane Knecklaces*/
		case 18333:
		return 20000;
		case 18334:
		return 40000;
		case 18335:
		return 60000;
		/*Arcane Knecklaces*/
		
		
		/*Other Knecklaces*/
		case 19887:
		return 70000;
		case 18337:
		return 45000;
		/*Other Knecklaces*/
		
		
		/*Gravite*/
		case 18365:
		return 75000;
		case 18367:
		return 75000;
		case 18369:
		return 75000;
		case 18371:
		return 75000;
		case 18373:
		return 75000;
		/*Gravite*/
		
		
		/*Other Items*/
		case 19893:
		return 125000;
		case 18347:
		return 70000;
		case 19669:
		return 100000;
		/**/
		/**/
		/**/
		/**/
		/**/
		/**/
		}
             
		return -1;
	}

	  public int getBuyPrice(Item item, int dq) {
            return ItemDefinition.get(item.getId()).getValue(item.getId());
	}

	public int getSellPrice(Item item, int dq) {
		//return (ItemDefinitions.getItemDefinitions(item.getId()).getValue(item.getId())* (3/4));
		return (EconomyPrices.getPrice(item.getId()* (3/4)));
	}

	public void sendExamine(Player player, int clickSlot) {
		int slotId = getSlotId(clickSlot);
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		player.packets().sendMessage(ItemExamines.getExamine(item));
	}

	public void refreshShop() {
		for (Player player : viewingPlayers) {
			sendStore(player);
			player.packets().sendIComponentSettings(620, 25, 0,
					getStoreSize() * 6, 1150);
		}
	}

	public int getStoreSize() {
		return mainStock.length
				+ (generalStock != null ? generalStock.length : 0);
	}

	public void sendStore(Player player) {
		Item[] stock = new Item[mainStock.length
				+ (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(mainStock, 0, stock, 0, mainStock.length);
		if (generalStock != null)
			System.arraycopy(generalStock, 0, stock, mainStock.length,
					generalStock.length);
		player.packets().sendItems(MAIN_STOCK_ITEMS_KEY, stock);
	}

}
