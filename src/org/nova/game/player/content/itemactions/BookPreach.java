package org.nova.game.player.content.itemactions;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @since 25.5.2014
 * @represents the god book preaching.
 */
public class BookPreach {

	String message;
	
	/**
	 * Singleton
	 */
	private static final BookPreach singleton = new BookPreach();
	
	
	public static final void Preach(Player player, Item item) {
		switch (item.getId()) {
		case 3844:
			
			break;
		}
	}

	/**
	 * Inits the singleton.
	 * @return
	 */
	public static BookPreach getSingleton() {
		return singleton;
	}
}
