package org.nova.game.player.content.cities;

import org.nova.game.item.Item;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class ApeToll {

	public static final int[] GREEGREES = {4024,4025,4026,4027,4028,4029,4030,4031};
	
	//0 - Ninja, 1 - Gorilla, 2 - Regular monkey, 3 - Small zombie monkey
	public static final int[] MONKEYS = {1481, 1482, 1487, 1485};
	/**
	 * Handled items (Greegrees)
	 * @param player
	 * @param item
	 * @return
	 */
	public static final boolean handleItems(Player player, Item item) {
		if (player.isTransformed()) {
		long currentTime = Misc.currentTimeMillis();
		if (player.getAttackedByDelay() + 10000 > currentTime) {
			player.packets()
					.sendMessage(
							"You can't transform until 10 seconds after the end of combat.");
			return false;
		}
		}			switch (item.getId()) {
			case 4024:
			case 4025:
			case 4026:
			case 4027:
			case 4028:
			case 4029:
			case 4030:
			case 4031:
				player.getEquipment().refresh(3);
				transformPlayer(player,item);
				return true;
			}
		return false;
	}
	
	public static final void transformPlayer(Player player, Item item) {
	/**
	 * Re add after testing done
	 */
//	if (!SpecialAreaHandler.isInApeToll(player)) {
//		player.sendMessage("" + item.getName() + " has no power in this area.");
//		return;
//	}
	if (player.isTransformed())
		untransformPlayer(player);
	else
	getActualNPC(player);
	}
	/**
	 * Remove a transform from the player.
	 * @param player
	 */
	public static final void untransformPlayer(Player player) {
		player.setTransformed(false);
		player.getAppearance().transformIntoNPC(-1);
		//player.sendMessage("You feel again the human power in you.");
		player.setNextGraphics(new Graphics(184));
	}
	
	private static void getActualNPC(Player player) {
		player.setTransformed(true);
		if (player.getInventory().containsItem(GREEGREES[0], 1) || player.getInventory().containsItem(GREEGREES[1], 1)) 
			player.getAppearance().transformIntoNPC(MONKEYS[0]);
		
			else if (player.getInventory().containsItem(GREEGREES[2], 1) || player.getInventory().containsItem(GREEGREES[3], 1)) 
				player.getAppearance().transformIntoNPC(MONKEYS[1]);
			
			else if (player.getInventory().containsItem(GREEGREES[5], 1)) 
				player.getAppearance().transformIntoNPC(MONKEYS[3]);
			
	}
}
