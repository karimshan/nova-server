package org.nova.cache.loaders;

import java.util.HashMap;

import org.nova.cache.definition.ItemDefinition;
import org.nova.utility.misc.Misc;

public final class ItemEquipIds {

	private static final HashMap<Integer, Integer> equipIds = new HashMap<Integer, Integer>();

	public static int getEquipId(int itemId) {
		Integer equipId = equipIds.get(itemId);
		if (equipId == null)
			return -1;
		return equipId;

	}

	public static final void init() {
		int equipId = 0;
		for (int itemId = 0; itemId < Misc.getItemsSize(); itemId++) {
			ItemDefinition itemDefinitions = ItemDefinition.get(itemId);
			if (itemDefinitions.getMaleWornModelId1() >= 0
					|| itemDefinitions.getFemaleWornModelId1() >= 0) {
				equipIds.put(itemId, equipId++);
			}
		}
	}

	private ItemEquipIds() {

	}
}
