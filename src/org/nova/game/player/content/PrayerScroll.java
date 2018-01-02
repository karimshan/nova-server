package org.nova.game.player.content;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information Represents prayer scroll handling.
 * @since 20.6.2014
 */
public class PrayerScroll {
	
	/**
	 * Static singleton.
	 */
	private static final PrayerScroll singleton = new PrayerScroll();
	
	/**
	 * 
	 * @author Fuzen Seth
	 * @information Holds data from the scrolls.
	 */
	public enum Scroll {
		
		RIGOUR(18839, "Rigour"),
		AUGURY(18344, "Augury");
		private int itemId;
		private String prayerName;
		
		private Scroll(int itemId, String prayerName) {
			this.itemId = itemId;
			this.prayerName = prayerName;
		}

		public String getPrayerName() {
			return prayerName;
		}

		public void setPrayerName(String prayerName) {
			this.prayerName = prayerName;
		}

		public int getItemId() {
			return itemId;
		}

		public void setItemId(int itemId) {
			this.itemId = itemId;
		}
	}
	
	/**
	 * Unlocks a prayer.
	 * @param player
	 * @param item
	 */
	public boolean unlockPrayer(Player player, Item item) {
		switch (item.getId()) {
		case 18344:
			if (player.getLearnedPrayer()[1]) {
			player.sendMessage("You have already learned the Augury prayer.");
				return false;
			}
			player.getLearnedPrayer()[1] = (true);
		return true;
		case 18839:
			if (player.getLearnedPrayer()[0]) {
			player.sendMessage("You have already learned the Rigour prayer.");
				return false;
			}
			player.getLearnedPrayer()[0] = (true);
		return true;
		
		}

		return false;
	}
	
	/**
	 * Player learns the new prayer.
	 * @param player
	 * @param item
	 * @return 
	 */
	public final boolean learnPrayer(Player player, Item item) {
		for (Scroll scrolls : Scroll.values()) {
			if (!(item.getId() == scrolls.getItemId()))
				return false;
			if (!unlockPrayer(player, item))
				return false;
			player.getInventory().deleteItem(item.getId(), 1);
			player.sendMessage("Congratulations, you have learned the "+scrolls.getPrayerName()+" prayer!");
			
		}
		return false;
	}
	/**
	 * Loads PrayerScroll by static.
	 * @return
	 */
	public static PrayerScroll getSingleton() {
		return singleton;
	}
	
}
