package org.nova.game.player.content;

import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class CrystalChest {
	/**
	 * Player instance
	 */
	private transient Player player;

	private static final Object[] CHEST_REWARDS = {getCHEST_REWARD1(), getCHEST_REWARD2(), getCHEST_REWARD3(), getCHEST_REWARD4(), getCHEST_REWARD5(), getCHEST_REWARD6(), getCHEST_REWARD7(), getCHEST_REWARD8(), getCHEST_REWARD9(), getCHEST_REWARD10(), getCHEST_REWARD11(), getCHEST_REWARD12() };
	
	public static final int[] KEY_HALVES = { 985, 987 };
	
	public static final int KEY = 989;
	
	public static final int Animation = 881;
	
	 private static int[][] CHEST_REWARD1 = { {1631, 1}, {1969, 1}, {995, 2000} };
	 
	private static int[][] CHEST_REWARD2 = { {1631, 1} };
	
	private static int[][] CHEST_REWARD3 = { {1631, 1}, {554, 50}, {555, 50}, {556, 50}, {557, 50}, {558, 50}, {559, 50}, {560, 10}, {561, 10}, {562, 10}, {563, 10}, {564, 10}  };

	private static int[][] CHEST_REWARD4 = { {1631, 1}, {2363, 3} };
	
	private static int[][] CHEST_REWARD5 = { {1631, 1}, {454, 100} };
	
	private static int[][] CHEST_REWARD6 = { {1631, 1}, {441, 150} };
	
	private static int[][] CHEST_REWARD7 = { {1631, 1}, {1603, 2}, {1601, 2} };
	
	private static int[][] CHEST_REWARD8 = { {1631, 1}, {372, 5}, {995, 1000} };
	
	private static int[][] CHEST_REWARD9 = { {1631, 1}, {987, 1}, {995, 750} };
	
	private static int[][] CHEST_REWARD10 = { {1631, 1}, {985, 1}, {995, 750} };
	
	private static int[][] CHEST_REWARD11 = { {1631, 1}, {1183, 1} };
	
	private static int[][] CHEST_REWARD12 = { {1631, 1}, { 1079, 1 } };
	
	public void makeKey(Player p){
		if (p.getInventory().containsItem(toothHalf(), 1) 
				&& p.getInventory().containsItem(loopHalf(), 1)){
			p.getInventory().deleteItem(toothHalf(), 1);
			p.getInventory().deleteItem(loopHalf(), 1);
			p.getInventory().addItem(KEY, 1);
			p.packets().sendMessage("You succesfully make a crytal key.");
		}
	}
	
	public void openChest() {
		if (!player.getInventory().containsItem(989, 1)) {
			player.packets().sendMessage("The chest is securely locked.");
			return;
		}
		else if (player.getInventory().containsItem(989, 1)) {
	player.getInventory().deleteItem(989, 1);
	player.setNextAnimation(new Animation(881));
	player.packets().sendMessage("You unlock the chest with your key.");
	player.getInventory().addItem(995, Misc.random(8230));
	player.getInventory().addItem((Integer) CHEST_REWARDS[Misc.random(getLength() - 1)], 1);
	player.getInventory().refresh();
	}
}

	public static int getLength() {
		return CHEST_REWARDS.length;
	}
	
	/**
	 * Represents the toothHalf of the key.
	 */
	public static int toothHalf(){
		return KEY_HALVES[0];
	}
	
	/**
	 * Represent the loop half of the key.
	 */
	public static int loopHalf(){
		return KEY_HALVES[1];
	}

	public static int[][] getCHEST_REWARD2() {
		return CHEST_REWARD2;
	}

	public static void setCHEST_REWARD2(int[][] cHEST_REWARD2) {
		CHEST_REWARD2 = cHEST_REWARD2;
	}

	public static int[][] getCHEST_REWARD3() {
		return CHEST_REWARD3;
	}

	public static void setCHEST_REWARD3(int[][] cHEST_REWARD3) {
		CHEST_REWARD3 = cHEST_REWARD3;
	}

	public static int[][] getCHEST_REWARD4() {
		return CHEST_REWARD4;
	}

	public static void setCHEST_REWARD4(int[][] cHEST_REWARD4) {
		CHEST_REWARD4 = cHEST_REWARD4;
	}

	public static int[][] getCHEST_REWARD6() {
		return CHEST_REWARD6;
	}

	public static void setCHEST_REWARD6(int[][] cHEST_REWARD6) {
		CHEST_REWARD6 = cHEST_REWARD6;
	}

	public static int[][] getCHEST_REWARD5() {
		return CHEST_REWARD5;
	}

	public static void setCHEST_REWARD5(int[][] cHEST_REWARD5) {
		CHEST_REWARD5 = cHEST_REWARD5;
	}

	public static int[][] getCHEST_REWARD7() {
		return CHEST_REWARD7;
	}

	public static void setCHEST_REWARD7(int[][] cHEST_REWARD7) {
		CHEST_REWARD7 = cHEST_REWARD7;
	}

	public static int[][] getCHEST_REWARD8() {
		return CHEST_REWARD8;
	}

	public static void setCHEST_REWARD8(int[][] cHEST_REWARD8) {
		CHEST_REWARD8 = cHEST_REWARD8;
	}

	public static int[][] getCHEST_REWARD9() {
		return CHEST_REWARD9;
	}

	public static void setCHEST_REWARD9(int[][] cHEST_REWARD9) {
		CHEST_REWARD9 = cHEST_REWARD9;
	}

	public static int[][] getCHEST_REWARD10() {
		return CHEST_REWARD10;
	}

	public static void setCHEST_REWARD10(int[][] cHEST_REWARD10) {
		CHEST_REWARD10 = cHEST_REWARD10;
	}

	public static int[][] getCHEST_REWARD11() {
		return CHEST_REWARD11;
	}

	public static void setCHEST_REWARD11(int[][] cHEST_REWARD11) {
		CHEST_REWARD11 = cHEST_REWARD11;
	}

	public static int[][] getCHEST_REWARD12() {
		return CHEST_REWARD12;
	}

	public static void setCHEST_REWARD12(int[][] cHEST_REWARD12) {
		CHEST_REWARD12 = cHEST_REWARD12;
	}

	public static int[][] getCHEST_REWARD1() {
		return CHEST_REWARD1;
	}

	public static void setCHEST_REWARD1(int[][] cHEST_REWARD1) {
		CHEST_REWARD1 = cHEST_REWARD1;
	}
}
