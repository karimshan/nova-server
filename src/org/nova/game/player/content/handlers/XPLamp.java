package org.nova.game.player.content.handlers;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @since 5.3.2013
 * @information Represents the XP lamps in-game.
 * @updated 9.2.2014
 */
public class XPLamp {
	
	public static int LAMP_INTER = 1139, LAMP = 2528;

	public static boolean sendLamp(Player player) {
		if(!player.getInventory().containsItem(LAMP, 1)) {
			return false;
		}
		player.interfaces().sendInterface(LAMP_INTER);
		return true;
	}
	
	public static boolean handleButtons(Player player, int buttonId) {
		if(buttonId - 30 < 0 || buttonId - 30 > 24) {
			return false;
		}
		int skillPicked = buttonId - 30;
		player.getTemporaryAttributtes().put("xpLampSkill", new Integer(skillPicked));
		return true;
	}
	
	public static void appendExperience(Player player) {
		if(player.getTemporaryAttributtes().get("xpLampSkill") == null || !(player.getTemporaryAttributtes().get("xpLampSkill") instanceof Integer)) {
			return;
		}
		player.getInventory().deleteItem(LAMP, 1);
		player.getInventory().refresh();
		final double experience = calculateXp(player, (Integer) player.getTemporaryAttributtes().get("xpLampSkill"));
		player.getSkills().addXp((Integer) player.getTemporaryAttributtes().get("xpLampSkill"), experience);
		player.closeInterfaces();
		player.getMatrixDialogues().startDialogue("SimpleMessage", "Your wish has been granted.");
	}
	
	public static double calculateXp(Player player, int skillId) {
		int x = player.getSkills().getLevel(skillId);
		double il = 663.796486 + 14.4355241 * Math.pow(x + 5.8695973 * x, 2);
		double negativeType = Math.pow(0.119998437 * x, 3)
				+ Math.pow(0.00142607098 * x, 4);
		double xp = il - negativeType + (3 * 9);
		return 7500;
		//return xp; <-- need fix since fucked math
	}

}
