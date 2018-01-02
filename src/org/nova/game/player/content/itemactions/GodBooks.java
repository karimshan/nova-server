package org.nova.game.player.content.itemactions;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class GodBooks {

	public GodBooks(Player player) {
		this.player = player;
	}
	public static final int SARADOMIN_PAGES[] = {1};
	
	public static final int GUTHIX_PAGES[] = {1};
	
	public static final int ZAMORAK_PAGES[] = {1};
	
	public static final int ZAMORAK_BOOK = -1;
	
	public static final int SARADOMIN_BOOK= 0;
	
	public static final int GUTHIX_BOOK = 0;
	
	/**
	 * Adds a page.
	 */
	public void addPages() {
		
	}

	public void itemUsedOn(Player player, int id, int secondId) {
		
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	private Player player;
	
}
