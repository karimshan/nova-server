package org.nova.game.player.content.cities.content;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @since 26.1.2014
 * @information This file represents the world travelling.
 */
public class Travelling {

	public static final int SHIP_INTERFACE = -1;
	
	private Player player;
	
	/**
	 * This enum holds the travelling data for destinations.
	 * @author Fuzen Seth
	 *
	 */
	public static enum Destinations {
		PORT_SARIM(2, 1, "Port Sarim");
		/**
		 * Datas
		 */
		private int price;
		private int configId;
		private String destination;
		/**
		 * Constructor
		 * @param price
		 * @param configId
		 */
		private Destinations(int price, int configId, String destination) {
			this.price = price;
			this.configId = configId;
			this.destination = destination;
		}
	}
	
	/**
	 * We travel.
	 */
	public void travel() {
		
	}
	/**
	 * Finishes the travel 'session'.
	 * @param interfaceRemove
	 */
	public void finish(boolean interfaceRemove) {
		if (interfaceRemove)
			player.interfaces().closeChatBoxInterface();
	}
}
