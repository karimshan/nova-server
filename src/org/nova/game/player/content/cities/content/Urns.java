package org.nova.game.player.content.cities.content;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class Urns {

	public Urns() {
		setPlayer(player);
	}
	/**
	 * The urn's data
	 * @author Fuzen Seth
	 *
	 */
	public static enum UrnDefinitions {
	STRONG_WOODCUTTING_URN(20308, 60);
	
	private int itemId;
	private int level;
	private UrnDefinitions(int itemId, int level) {
		this.itemId = itemId;
		this.level = level;
		
	}
}
	public void processUrn() {
		
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	private Player player;
	
}
