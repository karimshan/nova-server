package org.nova.game.player.content.cities.content;

import java.util.ArrayList;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class MiscellaniaResource {
	/**
	 * Constructor for resources.
	 * @param player
	 */
	public MiscellaniaResource(Player player) {
		this.player = player;
	}
	/**
	 * The resource interface ID.
	 */
	public static final int INTERFACE = -1;
	/**
	 * The rewards that player can get.
	 */
	private static final int[] REWARDS = {};
	/**
	 * Starts a new resource.
	 */
	public void start() {
	if (player.isActiveResource()) {
		player.sendMessage("You have already a active resource running, you cannot start another.");
		return;
	}
	addResourceUser();
	player.sendMessage("Your resource at Miscellania has been started.");
	}
	/**
	 * Adds a player to the resources.
	 */
	private void addResourceUser() {
		resourceUsers.add(player);
	}
	/**
	 * Cancels a resource. (Force Stop)
	 */
	public void cancel() {
		
	}
	/**
	 * Finishes a resource.
	 */
	public void finish() {
		
	}
	
	private int calculateActualAmount() {
		return 0;
	}
	/**
	 * Refresh everything.
	 */
	public void refreshAll() {
		
	}
	/**
	 * Refresh a resource.
	 */
	public void refresh() {
		
	}
	/**
	 * Sends resource interface with configs.
	 */
	public void sendInterface() {
		
	}
	
	
	/**
	 * Player instance.
	 */
	private Player player;
	/**
	 * Users in resource.
	 */
	private ArrayList<Player> resourceUsers;
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	public ArrayList<Player> getResourceUsers() {
		return resourceUsers;
	}
	public void setResourceUsers(ArrayList<Player> resourceUsers) {
		this.resourceUsers = resourceUsers;
	}

}
