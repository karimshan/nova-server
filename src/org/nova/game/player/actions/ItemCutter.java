package org.nova.game.player.actions;

import org.nova.game.item.Item;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class ItemCutter {

	public final static int KNIFE = 946;
	
	public static final Animation CUT_ANIM = new Animation(1);
	
	public static boolean cutItems(Player player, Item item, int itemUsedId, int itemUsedWithId) {
	if (player.isDueling() || player.isDead())
		return false;
	if (useItem(KNIFE, 1963)) {
		player.getInventory().deleteItem(itemUsedWithId, 1);
		player.sendMessage("You have sliced some " + item.getName() + ". ");
		player.getInventory().addItem(3162,1);
	}
		return false;
	}
	//0x3b2
	public static boolean useItem(int itemUsedId, int itemUsedWithId) {
		return itemUsedId == itemUsedId && itemUsedWithId == itemUsedWithId;
	}
}
