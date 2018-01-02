package org.nova.game.player.content;

import org.nova.game.Game;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class MaxoutPlayer {

	
	public static void scanGameProgress(Player player) {
	if (!isMaxed(player)) {
	player.getMatrixDialogues().startDialogue("SimpleMessage", "You don't have all skills 99.");
		return;
	}
	player.getInventory().addItem(20767, 1);
	player.getInventory().addItem(20768,1);
	if (!player.isFirstMaxCape()) {
		player.setFirstMaxCape(true);
		Game.sendMessage("<col=ff0033>News: "+player.getDisplayName()+" was awarded with his very first Max cape!", false);
	}
	if (!isCompletionist(player))
		return;
		if (!player.isFirstCompCape()) {
			player.setFirstCompCape(true);
			Game.sendMessage("<col=ff0033>News: "+player.getDisplayName()+" was awarded with his very first Completionist cape!", false);
		}
		player.getInventory().addItem(20771,1);
		player.getInventory().addItem(20772,1);
	}
	
	
	/**
	 *Has the player completed all the quests?
	 */
	public static boolean completedQuests(Player player) {
		return player.isCompletedHoarfrost() && player.isCompletedRuinsofUzer() && player.isCompletedWanted() && player.isCompletedHoarfrostDepths() && player.isCompletedThrone();
		
	}
	/**
	 * This is temporary will be deleted once all skills is done, ignore messy code.
	 * @param player
	 * @return
	 */
	public static boolean isMaxed(Player player) {
		return player.getSkills().getLevel(Skills.ATTACK) >= 98 && player.getSkills().getLevel(Skills.STRENGTH) >= 98 && player.getSkills().getLevel(Skills.DEFENCE) >= 98 && player.getSkills().getLevel(Skills.HITPOINTS) >= 98 && player.getSkills().getLevel(Skills.RANGE) >= 98 && player.getSkills().getLevel(Skills.MAGIC) >= 98
				&& player.getSkills().getLevel(Skills.PRAYER) >= 98&& player.getSkills().getLevel(Skills.SUMMONING) >= 98&& player.getSkills().getLevel(Skills.WOODCUTTING) >= 98&& player.getSkills().getLevel(Skills.FLETCHING) >= 98&& player.getSkills().getLevel(Skills.FISHING) >= 98
				&& player.getSkills().getLevel(Skills.COOKING) >= 98&& player.getSkills().getLevel(Skills.HERBLORE) >= 98&& player.getSkills().getLevel(Skills.DUNGEONEERING) >= 98&& player.getSkills().getLevel(Skills.FARMING) >= 98&& player.getSkills().getLevel(Skills.FIREMAKING) >= 98
				&& player.getSkills().getLevel(Skills.MINING) >= 98&& player.getSkills().getLevel(Skills.SMITHING) >= 98&& player.getSkills().getLevel(Skills.RUNECRAFTING) >= 98&& player.getSkills().getLevel(Skills.HUNTER) >= 98;
	}
	
	public static boolean isCompletionist(Player player) {
		return player.getSkills().getLevel(Skills.ATTACK) >= 98 && player.getSkills().getLevel(Skills.STRENGTH) >= 98 && player.getSkills().getLevel(Skills.DEFENCE) >= 98 && player.getSkills().getLevel(Skills.HITPOINTS) >= 98 && player.getSkills().getLevel(Skills.RANGE) >= 98 && player.getSkills().getLevel(Skills.MAGIC) >= 98
				&& player.getSkills().getLevel(Skills.PRAYER) >= 98&& player.getSkills().getLevel(Skills.SUMMONING) >= 98&& player.getSkills().getLevel(Skills.WOODCUTTING) >= 98&& player.getSkills().getLevel(Skills.FLETCHING) >= 98&& player.getSkills().getLevel(Skills.FISHING) >= 98
				&& player.getSkills().getLevel(Skills.COOKING) >= 98&& player.getSkills().getLevel(Skills.HERBLORE) >= 98&& player.getSkills().getLevel(Skills.DUNGEONEERING) >= 119&& player.getSkills().getLevel(Skills.FARMING) >= 98&& player.getSkills().getLevel(Skills.FIREMAKING) >= 98
				&& player.getSkills().getLevel(Skills.MINING) >= 98&& player.getSkills().getLevel(Skills.SMITHING) >= 98&& player.getSkills().getLevel(Skills.RUNECRAFTING) >= 98&& player.getSkills().getLevel(Skills.HUNTER) >= 98;
	}
}
