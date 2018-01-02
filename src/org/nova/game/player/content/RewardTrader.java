package org.nova.game.player.content;

import org.nova.game.player.Player;
import org.nova.game.player.content.TitleManager.Titles;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the rewards trader at edgeville.
 * @since 22.6.2014
 */
public class RewardTrader {
	/**
	 * Opens the vote shop.
	 * @param player
	 */
	public static final void openVoteShop(Player player) {
		player.getTitleManager().setTitle(player, Titles.ARCHON);
	}
	
	public static void addEXPBoost(Player player) {
		if (player.getVotePoints() > 99)
		player.interfaces().closeChatBoxInterface();
		//player.addXPBoost(60);
		player.sendMessage("Your Combat EXP boost is currently active!");
	}
	
	public static final void openRandomEventStore() {
		
	}
	
	public void openAchievementStore() {
		
	}
}
