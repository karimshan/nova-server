package org.nova.game.player.content.itemactions;

import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;

/**
 * 
 * @author Fuzen Seth
 * @information Rotten potato is a developer item.
 * @since 19.4.2014
 */
public class RottenPotato {

	private String action;
	
	private static RottenPotato singleton = new RottenPotato();
	
	/**
	 * Handles a given action.
	 * @param player
	 */
	public void handleAction(Player player) {
		if (getAction().isEmpty()) {
			System.out.println("This should never happen.");
			return;
		}
		
	switch (getAction().toString()) {
	case "heal":
		player.heal(player.getSkills().getLevel(Skills.HITPOINTS) * 2);
		player.sendMessage("Rotten potato heals you to full hitpoints.");
		return;
	case "send_commands":
		break;
	case "open_cm_tool":
		player.getMatrixDialogues().startDialogue("RottenPotatoD");
		break;
	case "eat":
	case "open":
		player.setNextAnimation(new Animation(829));
		player.getMatrixDialogues().startDialogue("RottenPotatoD");
	return;
	
	case "maxout":
		for (int skill = 0; skill < 25; skill++)
			player.getSkills().addXp(skill, 150000000);
		break;
	}
	/**
	 * Gfx for using potato.
	 */
	if (!getAction().equals("eat") || !getAction().equals("open")
			|| !getAction().equals("open_cm")) {
		player.closeInterfaces();	
		player.interfaces().closeChatBoxInterface();
	}
	
	player.setNextGraphics(new Graphics(1930));
	action = "null";
}

	public  String getAction() {
		return action;
	}
	/**
	 * Adds a action to player.
	 * @param player 
	 * @param actionName
	 * @return
	 */
	public static String addPotatoAction(Player player, String actionName) {
		RottenPotato.getSingleton().addAction(player, actionName);
		return actionName;
	}
	
	public void addAction(Player player, String action) {
		this.action = action;
		handleAction(player);
	}

	public static RottenPotato getSingleton() {
		return singleton;
	}

	public static void setSingleton(RottenPotato singleton) {
		RottenPotato.singleton = singleton;
	}

}
