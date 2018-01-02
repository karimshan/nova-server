package org.nova.game.player.content.itemactions;

import java.util.Random;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class CrystalKeyConventer {

	
	public static final int[] KEY_REWARDS = {14664,590,1925,1305,4587,6585,1712,1725,11118};
	
	public static final void exchangeKey(Player player) {
	player.getInventory().deleteItem(989,1);
	
	}
	
	public static final void fixKeys(Player player, Item item) {
		
	}
	
	public static void sendRandomReward(Player player) {
		Random random = new Random();
		player.getInventory().addItem(KEY_REWARDS[random.nextInt(8)],1);
	}
}
