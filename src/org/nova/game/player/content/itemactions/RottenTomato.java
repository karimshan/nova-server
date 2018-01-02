package org.nova.game.player.content.itemactions;

import org.nova.game.entity.Entity;
import org.nova.game.map.GlobalObject;
import org.nova.game.player.Player;
import org.nova.utility.ShopsHandler;

/**
 * 
 * @author Fuzen Seth
 * @since 1.2.2014
 * @information Represents a crate where people can buy tomatoes.
 */
public class RottenTomato {
	
	/**
	 * We handle the crates.
	 * @return
	 */
	public static final boolean handleCrates(Player player, GlobalObject gameObject) {
		if (!gameObject.defs().name.equals("Crate") && !gameObject.defs().containsOption("Buy")) {
		return false;
		}
		
		ShopsHandler.openShop(player, 1);
		return false;
	}
	
	public static final void throwTomato(Player player, Entity target) {
	
	}
}
