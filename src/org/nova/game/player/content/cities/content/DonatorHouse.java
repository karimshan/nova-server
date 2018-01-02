package org.nova.game.player.content.cities.content;

import org.nova.game.map.Location;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class DonatorHouse {
	
	public DonatorHouse(Player player) {
		this.player = player;
	}
	private transient Player player;
	
	private String action;
	/**
	 * Listens the incoming action.
	 * @param player
	 * @return 
	 */
	public boolean grabActions(Player player) {
		switch (getAction().toString()) {
		case "Restore special":
			player.getCombatDefinitions().refreshSpecialAttackPercentage();
			player.sendMessage("Your special attack has been restored.");
			return true;
		case "Open bank":
			player.interfaces().closeChatBoxInterface();
			player.closeInterfaces();
			player.getBank().openBank();
			return true;
		case "Restore prayer":
			player.getPrayer().refresh();
			player.sendMessage("Your prayers has been healed.");
			return true;
		case "Leave house":
			player.setLocation(new Location(3164,3488,0));
			player.interfaces().closeChatBoxInterface();
			return true;
		case "Enter house":
			player.closeInterfaces();
			player.interfaces().closeChatBoxInterface();
			if (!player.isDonator()) {
				player.sendMessage("You are currently are not a member, you cannot enter here.");
				return false;
			}
			player.teleportPlayer(2162, 5115, 1);
			player.sendMessage("You succesfully enter to the donator house.");
			player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 8517, "Welcome to the donator's house, " + player.getDisplayName() + "!");
			return true;
		}
		return false;
	}
	/**
	 * Refreshes the action list.
	 * @param player
	 */
	public void refreshActionsList(Player player) {
		addAction("none");
	}
	/**
	 * Gets an action.
	 * @return
	 */
	public String getAction() {
		return action;
	}
	/**
	 * Adds an action.
	 * @param action
	 */
	public void addAction(String action) {
		this.action = action;
		grabActions(player);
	}
}
