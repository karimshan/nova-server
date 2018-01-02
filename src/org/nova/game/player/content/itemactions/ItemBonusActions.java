package org.nova.game.player.content.itemactions;

import java.util.HashMap;
import java.util.Map;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class ItemBonusActions {
	
	public static enum Items {
		HATI_HEAD(20178),
		HATI_PAWS(20177);

		private int itemId;

		public int getItemId() {
			return itemId;
		}	
		
		private	Items(int itemId) {
			this.itemId = itemId;
		}
		private static Map<Integer, Items> items = new HashMap<Integer, Items>();
		
		static {
			for (final Items item : Items.values()) {
				items.put(item.itemId, item);
			}
	}
	}
	
	public ItemBonusActions(Player player) {
		this.player = player;
	}
	private transient Player player;
	
	public boolean hasHatiPaws() {
		return player.getEquipment().getGlovesId() ==  20178;
	}
	public boolean hasHatiHead() {
		return player.getEquipment().getHatId() == 20177;
	}
}
