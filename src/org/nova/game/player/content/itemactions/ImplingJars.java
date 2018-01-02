package org.nova.game.player.content.itemactions;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class ImplingJars {

	private static final int[] jarLoots = {1,1,1};
	
	public static void openjar(Player player, Item item) {
		player.getInventory().deleteItem(item.getId(), 1);
		player.addStopDelay(2);
		player.closeInterfaces();
		player.interfaces().closeChatBoxInterface();
		player.getInventory().addItem(getJarloots().length,1);
	}

	public static int[] getJarloots() {
		return jarLoots;
	}
}
