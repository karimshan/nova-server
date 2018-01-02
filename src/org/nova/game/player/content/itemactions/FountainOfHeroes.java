package org.nova.game.player.content.itemactions;

import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @since 16.2.2014
 * @information Represents the Fountain of Heroes,
 *  located in the heroe's guild.
 */
public class FountainOfHeroes {

	/**
	 * We handle the fountain interaction.
	 * @param player
	 * @param gameObject
	 * @return
	 */
	public static final boolean handleFountain(Player player, Item item, GlobalObject gameObject) {
		if (item.getId() == 1712 && gameObject.getId() == 1) {
			player.setNextAnimation(new Animation(-1));
		}
		return false;
	}
}
