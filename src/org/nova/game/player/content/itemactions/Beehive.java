package org.nova.game.player.content.itemactions;

import org.nova.game.map.GlobalObject;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
/**
 * 
 * @author Fuzen Seth
 * @information this represents the camelot beehives.
 * @since 4.4.2014
 */
public class Beehive {
	
	private static final int EMPTY_BUCKET = 1925;
	private static final int EMPTY_BOWL = 1923;
	
	private static final Animation PICK_ANIMATION = new Animation(833);
	
	public static final boolean take(Player player, GlobalObject object, boolean honey) {
	player.addStopDelay(4);
	if (!honey) {
	if (!player.getInventory().containsItem(EMPTY_BUCKET, 1)) {
		player.sendMessage("You must have a bucket to take some wax from the " + object.defs().name.toLowerCase() + ".");
	return false;
	}
	if (!player.getInventory().containsItem(28, 1)) {
		player.sendMessage("You must have a Insect repellent to take some wax from the the " + object.defs().name.toLowerCase() + ".");
		return false;
	}
	player.setNextAnimation(PICK_ANIMATION);
	player.getInventory().deleteItem(1925,1);
	player.getInventory().addItem(30, 1);
	player.sendMessage("You take some wax from the " + object.defs().name.toLowerCase() + ".");

	}
	if (honey)  {
	if (!player.getInventory().containsItem(EMPTY_BOWL, 1)) {
		player.sendMessage("You must have a empty bowl to take some honey.");
		return false;
	}	
	player.setNextAnimation(PICK_ANIMATION);
	player.getInventory().deleteItem(EMPTY_BOWL, 1);
	player.getInventory().addItem(13573,1);
	player.sendMessage("You have taken some honey from the beehive.");
	return false;
	}
	return honey;
	}
}
