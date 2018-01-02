package org.nova.game.player.content;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.player.Player;

public final class SkillsDialogue {

	public static final int MAKE = 0, MAKE_SETS = 1, COOK = 2, ROAST = 3,
			OFFER = 4, SELL = 5, BAKE = 6, CUT = 7, DEPOSIT = 8,
			MAKE_NO_ALL_NO_CUSTOM = 9, TELEPORT = 10, SELECT = 11, TAKE = 13;

	public static interface ItemNameFilter {

		public String rename(String name);
	}
	   private static int[] items;

	    public static void setItem(int... item) {
	        items = item;
	    }

	    public static int getItem(int slot) {
	        return items[slot];
	    }
	    
	public static void sendSkillsDialogue(Player player, int option,
			String explanation, int maxQuantity, int[] items,
			ItemNameFilter filter) {
		player.interfaces().sendChatBoxInterface(905);
		player.packets().sendInterface(true, 905, 4, 916);
		if (option != MAKE_SETS && option != MAKE_NO_ALL_NO_CUSTOM)
			player.packets().sendUnlockIComponentOptionSlots(916, 8, -1, 0,
					0); // unlocks all option
		player.packets().sendIComponentText(916, 1, explanation);
		player.packets().sendGlobalConfig(754, option);
		for (int i = 0; i < 10; i++) {
			if (i >= items.length) {
				player.packets().sendGlobalConfig(
						i >= 6 ? (1139 + i - 6) : 755 + i, -1);
				continue;
			}
			player.packets().sendGlobalConfig(
					i >= 6 ? (1139 + i - 6) : 755 + i, items[i]);
			String name = ItemDefinition.get(items[i])
					.getName();
			if (filter != null)
				name = filter.rename(name);
			player.packets().sendGlobalString(
					i >= 6 ? (280 + i - 6) : 132 + i, name);
		}
		setMaxQuantity(player, maxQuantity);
		setQuantity(player, maxQuantity);  
		setItem(items);
	}

	public static void handleSetQuantityButtons(Player player, int componentId) {
		if (componentId == 5)
			setQuantity(player, 1, false);
		else if (componentId == 6)
			setQuantity(player, 5, false);
		else if (componentId == 7)
			setQuantity(player, 10, false);
		else if (componentId == 8)
			setQuantity(player, getMaxQuantity(player), false);
		else if (componentId == 19)
			setQuantity(player, getQuantity(player) + 1, false);
		else if (componentId == 20)
			setQuantity(player, getQuantity(player) - 1, false);
	}

	public static void setMaxQuantity(Player player, int maxQuantity) {
		player.getTemporaryAttributtes().put("SkillsDialogueMaxQuantity",
				maxQuantity);
		player.packets().sendConfigByFile(8094, maxQuantity);
	}

	public static void setQuantity(Player player, int quantity) {
		setQuantity(player, quantity, true);
	}

	public static void setQuantity(Player player, int quantity, boolean refresh) {
		int maxQuantity = getMaxQuantity(player);
		if (quantity > maxQuantity)
			quantity = maxQuantity;
		else if (quantity < 0)
			quantity = 0;
		player.getTemporaryAttributtes()
				.put("SkillsDialogueQuantity", quantity);
		if (refresh)
			player.packets().sendConfigByFile(8095, quantity);
	}

	public static int getMaxQuantity(Player player) {
		Integer maxQuantity = (Integer) player.getTemporaryAttributtes().get(
				"SkillsDialogueMaxQuantity");
		if (maxQuantity == null)
			return 0;
		return maxQuantity;
	}

	public static int getQuantity(Player player) {
		Integer quantity = (Integer) player.getTemporaryAttributtes().get(
				"SkillsDialogueQuantity");
		if (quantity == null)
			return 0;
		return quantity;
	}

	public static int getItemSlot(int componentId) {
		if (componentId < 14)
			return 0;
		return componentId - 14;
	}

	private SkillsDialogue() {

	}
}
