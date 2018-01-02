package org.nova.game.player.content.itemactions;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class HelmsEnchant {

	public HelmsEnchant() {
	setPlayer(player);	
	}
	
	public enum Enchantables {
		
	}
	
	private Player player;
	
	public void enchant(Player player, Item item) {
	if (player.isCompletedHoarfrost()) {  
	player.sendMessage("You must have completed Hoarfrost Hollow -quest to enchant items.");
		return;
	}
		if (!player.getInventory().containsItem(1/* TODO load it from enum*/, 1))  {
			player.sendMessage("You don't have a helm to enchant.");
				return;
		}
}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	
}
