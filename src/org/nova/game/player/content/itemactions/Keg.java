package org.nova.game.player.content.itemactions;

import org.nova.game.item.Item;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @since 16.2.2014
 * @information Represents the Keg item.
 */
public class Keg {
	
	
	/**
	 * Handles the keg.
	 */
	public static final void handleKeg(Player player, Item item) {
		int itemId = item.getId();
		int drinkenKegs = 0;
		if (itemId == 3801) { //KEG
			if (drinkenKegs >= 2) 
			drinkenKegs++;
			player.packets().sendMessage("You start to feel dizzy!");
			player.getInventory().deleteItem(3801, 1);
			player.getInventory().addItem(3712, 1);
			player.setNextAnimation(new Animation(1329));
			player.getAppearance().setRenderEmote(290);
			player.packets().sendSound(4580, 0, 1);
			}
	}
	/**
	 * Sets the player drunk.
	 */
	public static final void setDrunk() {
	int drinkenKegs;
	drinkenKegs = 0;
	
	}

}
