package org.nova.game.player.content;

import org.nova.game.player.Player;

/**
 * @author Fuzen Seth
 * @information Represents the 'urn' -item in-game.
 * @since 23.3.2014
 */
public class Urns {

	public static final int[] WOODCUTTING_URNS = {20307,20308,20309,20310,20311};
	
	/**
	 * Is the player holding a urn?
	 * @param player
	 * @return
	 */
	public static final boolean checkforUrns(Player player, int skillId) {
		for (int i : WOODCUTTING_URNS) 
			if (player.getInventory().containsItem(i, 1)) {
				handleUrns(player, skillId);
			} 
		return false;
	}
	
	/**
	 * Handles the all urns.
	 * @param player
	 * @param skillId
	 */
	private static final void handleUrns(Player player, int skillId) {
	player.setUrnStage(player.getUrnStage() + 1);
	
	addBonus(player, skillId);
	if (player.getUrnStage() > 50)
			//player.getInventory().deleteItem(item);
		for (int i = 20307; i < 20319; i++)
			
		player.getInventory().deleteItem(i, 1);
	player.getInventory().addItem(getCrackedUrn(player), 1);
	player.setUrnStage(0);
	player.sendMessage("Your urn has cracked, which means it will no longer give bonus experience.");
	if (player.getDisplayName().equals("Nexon"))
		player.sendMessage("Urn stage: "+player.getUrnStage()+".");
	
	}
	
	/**
	 * Gets the cracked urn.
	 */
	public static int getCrackedUrn(Player player) {
		for (int i : WOODCUTTING_URNS)
			if (player.getInventory().containsItem(i, 1))
				return 20299;
		
		return getCrackedUrn(player);
	}
	
	/**
	 * Adds the bonus XP.
	 */
	private static final void addBonus(Player player, int skillId) {
		double bonus;
//		if (Constants.DOUBLE_XP_WEEKEND)
//			bonus = Constants.URN_BONUS_EXPERIENCE * 2;
//		else
		bonus = 2000;
		player.getSkills().addXp(skillId, bonus);
	}

}
