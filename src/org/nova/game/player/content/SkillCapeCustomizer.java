package org.nova.game.player.content;

import java.util.Arrays;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.player.Player;


public final class SkillCapeCustomizer {

	private SkillCapeCustomizer() {

	}

	public static void resetSkillCapes(Player player) {
		player.setMaxedCapeCustomized(Arrays.copyOf(
				ItemDefinition.get(20767).originalModelColors,
				4));
		player.setCompletionistCapeCustomized(Arrays.copyOf(
				ItemDefinition.get(20769).originalModelColors,
				4));
	}

	public static void startCustomizing(Player player, int itemId) {
		player.getTemporaryAttributtes().put("SkillcapeCustomizeId", itemId);
		int[] skillCape = itemId == 20767 ? player.getMaxedCapeCustomized()
				: player.getCompletionistCapeCustomized();
		player.interfaces().sendInterface(20);
		for (int i = 0; i < 4; i++)
			player.packets().sendConfigByFile(9254 + i, skillCape[i]);
		player.packets().sendIComponentModel(
				20,
				55,
				player.getAppearance().isMale() ? ItemDefinition
						.get(itemId).getMaleWornModelId1()
						: ItemDefinition.get(itemId)
								.getFemaleWornModelId1());
	}

	public static int getCapeId(Player player) {
		Integer id = (Integer) player.getTemporaryAttributtes().get(
				"SkillcapeCustomizeId");
		if (id == null)
			return -1;
		return id;
	}

	public static void handleSkillCapeCustomizerColor(Player player, int colorId) {
		int capeId = getCapeId(player);
		if (capeId == -1)
			return;
		Integer part = (Integer) player.getTemporaryAttributtes().get(
				"SkillcapeCustomize");
		if (part == null)
			return;
		int[] skillCape = capeId == 20767 ? player.getMaxedCapeCustomized()
				: player.getCompletionistCapeCustomized();
		skillCape[part] = colorId;
		player.packets().sendConfigByFile(9254 + part, colorId);
		player.interfaces().sendInterface(20);
	}

	public static void handleSkillCapeCustomizer(Player player, int buttonId) {
		int capeId = getCapeId(player);
		if (capeId == -1)
			return;
		int[] skillCape = capeId == 20767 ? player.getMaxedCapeCustomized()
				: player.getCompletionistCapeCustomized();
		if (buttonId == 58) { // reset
			if (capeId == 20767)
				player.setMaxedCapeCustomized(Arrays.copyOf(
						ItemDefinition.get(capeId).originalModelColors,
						4));
			else
				player.setCompletionistCapeCustomized(Arrays.copyOf(
						ItemDefinition.get(capeId).originalModelColors,
						4));
			for (int i = 0; i < 4; i++)
				player.packets().sendConfigByFile(9254 + i, skillCape[i]);
		} else if (buttonId == 34) { // detail top
			player.getTemporaryAttributtes().put("SkillcapeCustomize", 0);
			player.interfaces().sendInterface(19);
			player.packets().sendConfig(2174, skillCape[0]);
		} else if (buttonId == 71) { // background top
			player.getTemporaryAttributtes().put("SkillcapeCustomize", 1);
			player.interfaces().sendInterface(19);
			player.packets().sendConfig(2174, skillCape[1]);
		} else if (buttonId == 83) { // detail button
			player.getTemporaryAttributtes().put("SkillcapeCustomize", 2);
			player.interfaces().sendInterface(19);
			player.packets().sendConfig(2174, skillCape[2]);
		} else if (buttonId == 95) { // background button
			player.getTemporaryAttributtes().put("SkillcapeCustomize", 3);
			player.interfaces().sendInterface(19);
			player.packets().sendConfig(2174, skillCape[3]);
		} else if (buttonId == 114 || buttonId == 142) { // done / close
			player.getAppearance().generateAppearanceData();
			player.closeInterfaces();
		}
	}
}
