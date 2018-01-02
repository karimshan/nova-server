package org.nova.game.player.content;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class WildernessHelmets {

	/*
	 * Hat tiers
	 */
public static int TIER_1 =20801, TIER_2 = 20802,
			TIER_3 = 20803, TIER_4 = 20804,
			TIER_5 = 20805, TIER_6 = 20806;
	
private static final Object[] HATS = {getHatIndex()};

private static int[][] hatIndex = { {20801, 1}, {20802, 1}, {20803, 1}, {20804, 1} ,{20805, 1} , {20806, 1} };

	public static void resetProgress(Player player) {
		
	}
	public static void giveFirstTimeHat(Player player) {
		if (!player.isReceivedwildyhat()) {
		player.getInventory().addItem(20801, 1);
		player.setReceivedwildyhat(true);
		player.setTier1(true);
		player.sm("<col=ff0033>You have received Wildstaker helmet from Mr Ex.");
		} else {
			player.sm("You have already hat. Ask for a new one if you have lost it.");
		}
	}
	
	public static void checkLostHat(Player player) {
	if (player.isTier1()) {
		player.sm("You have received Wildstaker helmet. (Tier 1)");
		player.getInventory().addItem(TIER_1,1);
	}
	else if (player.isTier2()) {
		player.sm("You have received Wildstaker helmet. (Tier 2)");
		player.getInventory().addItem(TIER_2,1);
	}
	else if (player.isTier3()) {
		player.sm("You have received Wildstaker helmet. (Tier 3)");
		player.getInventory().addItem(TIER_3,1);
	}
	else if (player.isTier4()) {
		player.sm("You have received Wildstaker helmet. (Tier 4)");
		player.getInventory().addItem(TIER_4,1);
	}
	else if (player.isTier5()) {
		player.sm("You have received Wildstaker helmet. (Tier 5)");
		player.getInventory().addItem(TIER_5,1);
	}
	else if (player.isTier6()) {
		player.sm("You have received Wildstaker helmet. (Tier 6)");
		player.getInventory().addItem(TIER_6,1);
	}
} 
		
	
	
	public static void openStatistics(Player player) {
		player.interfaces().closeChatBoxInterface();
		
	}
	
	public static void scanHat(Player player) {
		
	}
	
	public static void removeHats(Player player) {
		player.sm("<col=ff0033>Your Wildstaker helmet has been updated.");
		player.getInventory().deleteItem(TIER_1, 1);
		player.getInventory().deleteItem(TIER_2, 1);
		player.getInventory().deleteItem(TIER_3, 1);
		player.getInventory().deleteItem(TIER_4, 1);
		player.getInventory().deleteItem(TIER_5, 1);
		player.getInventory().deleteItem(TIER_6, 1);
	}
	
	public static void removeTiers(Player player) {
		player.setTier1(false);
		player.setTier2(false);
		player.setTier3(false);
		player.setTier4(false);
		player.setTier5(false);
		player.setTier6(false);
	}
	public static int[][] getHatIndex() {
		return hatIndex;
	}
	public static void setHatIndex(int[][] hatIndex) {
		WildernessHelmets.hatIndex = hatIndex;
	}
	public static Object[] getHats() {
		return HATS;
	}

	public static int getLength() {
		return hatIndex.length;
	}
}
