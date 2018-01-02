package org.nova.game.player.content.itemactions;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class GodPreach {

	public GodPreach() {
		setPlayer(player);
	}
	
	private String currentBook;
	
	
	public void preachBook() {
		
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getCurrentBook() {
		return currentBook;
	}

	public void setCurrentBook(String currentBook) {
		this.currentBook = currentBook;
	}
	private Player player;
}
