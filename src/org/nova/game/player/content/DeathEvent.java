package org.nova.game.player.content;

import org.nova.game.map.Location;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the death event.
 * @since 14.3.2014
 */
public class DeathEvent {
	/**
	 * Edgeville's hub id.
	 */
	public static final int EDGEVILLE = 0;
	/**
	 * Lumbridge's hub id.
	 */
	public static final int LUMBRIDGE = 1;
	/**
	 * Falador's hub id.
	 */
	public static final int FALADOR = 2;
	/**
	 * Death hubs
	 * 0 - Edgeville
	 * 1 - Lumbridge
	 * 2 - Falador
	 */
	public static final Location[]HUBS = {new Location(3102,3493,0), new Location(3222,3219,0),new Location(2971,3349,0)};

	/**
	 * Switches a death hub.
	 * @param player
	 * @param deathHub
	 */
	public static final void switchDeathHub(Player player, int deathHub) {
		player.closeInterfaces();
		switch (deathHub) {
		case EDGEVILLE:
			player.setDeathHubType(EDGEVILLE);
			break;
		case LUMBRIDGE:
			player.setDeathHubType(LUMBRIDGE);
			break;
		case FALADOR:
			player.setDeathHubType(FALADOR);
			break;
		}
		sendMessage(player);
	}
	/**
	 * Gets the message.
	 * @param player
	 * @return
	 */
	private static String sendMessage(Player player) {
		switch (player.getDeathHubType()) {
		case LUMBRIDGE:
		player.sendMessage("You have changed your death hub to Lumbridge.");
			break;
		case FALADOR:
			player.sendMessage("You have changed your death hub to Falador.");
			break;
		case EDGEVILLE:
			player.sendMessage("You have changed your death hub to Edgeville.");
			break;
		}
		player.interfaces().closeChatBoxInterface();
		return null;
	}
	/**
	 * Handles the death hubs.
	 * @param player
	 * @return
	 */
	public static final Location handleDeathHub(Player player) {
	switch (player.getDeathHubType()) {
	case 0:
		return HUBS[0];	
		case 1:
			return HUBS[1];	
			case 2:
				return HUBS[2];
	}
	/**
	 * If death hub isn't set, we will by default send to Edgeville.
	 */
	return new Location(HUBS[0]);
	}
	
	/**
	 * Sets a death hub.
	 * @param hubId
	 */
	public static void setDeathHub(Player player, int hubId) {
		player.setDeathHubType(hubId);
	}
	
}
