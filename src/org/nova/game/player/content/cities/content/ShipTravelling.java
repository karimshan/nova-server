package org.nova.game.player.content.cities.content;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * 
 */
public class ShipTravelling {
	/**
	 * Constructor, we set player.
	 */
	public ShipTravelling() {
		setPlayer(player);
	}
	/**
	 * What's the destination?
	 */
	private String destination;
	
	private int travelId;
	/**
	 * Travel ids
	 */
	private static final int KARAMJA = 0;
	private static final int PORT_SARIM = 1;
	private static final int PORT_KHAZARD = 2;
	private static final int ENTRANA = 3;
	private static final int FELDIP_HILLS = 4;
	private static final int APE_A_TOLL = 5;
	private static final int CRANDOR = 6;
	
	/**
	 * We get automaticly the correct config.
	 * @return 
	 */
	public ShippingPolicy sendCorrectConfig() {
	switch (getDestination().toString()) {
	case "Karamja":
		return ShippingPolicy.KARAMJA;
	case "Port Sarim":
		return ShippingPolicy.PORT_SARIM;
	case "Port Khazard":
		return ShippingPolicy.PORT_KHAZARD;
	case "Feldip Hills":
		return ShippingPolicy.FELDIP_HILLS;
	case "Ape a Toll":
		return ShippingPolicy.APE_A_TOLL;
	case "Crandor":
		return ShippingPolicy.CRANDOR;
	case "Entrana":
		return ShippingPolicy.ENTRANA;
	}
	return null;
	}
	public void checkAll() {
		if (!player.getInventory().containsItem(995, getShippingFee()))
				return;
	}
	/**
	 * We get the prices for the ship.
	 * @return
	 */
	public int getShippingFee() {
		switch(travelId) {
		case KARAMJA: 
			return 30;
		case PORT_SARIM:
			return -1;
		case ENTRANA:
			return -1;
		case PORT_KHAZARD:
			return -1;
		case CRANDOR:
			return -1;
		}
		return travelId;
	}
	/**
	 *Player Instance
	 */
	private Player player;
	/**
	 * Ship Configs
	 * @author Fuzen Seth
	 *
	 */
	public enum ShippingPolicy {
	KARAMJA(5),
	ENTRANA(-1),
	FELDIP_HILLS(-1),
	APE_A_TOLL(-1),
	CRANDOR(-1),
	ASGARNIA(-1),
	PORT_SARIM(-1),
	PORT_KHAZARD(-1);

		private int configId;
		
		private ShippingPolicy(int configId) {
			this.configId = configId;
		}

		public int getConfigId() {
			return configId;
		}
	}
	
	/**
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}
	/**
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getTravelId() {
		return travelId;
	}

	public void setTravelId(int travelId) {
		this.travelId = travelId;
	}
	
}
