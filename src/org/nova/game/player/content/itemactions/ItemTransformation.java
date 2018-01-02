package org.nova.game.player.content.itemactions;

import org.nova.game.item.Item;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 * @since 18.1.2014
 * @information Represents a item that can transform player.
 */
public class ItemTransformation {

	/**
	 * Is it a invalid area?
	 * @param player
	 * @return
	 */
	public static boolean invalidArea(Player player) {
	//	return SpecialAreaHandler.invalidCannonLocations(player) || SpecialAreaHandler.isInApeToll(player) || SpecialAreaHandler.isInApeTollDungeon(player);
	return false;
	}
	
	public static final boolean handleTransformations(Player player, Item item) {
		long currentTime = Misc.currentTimeMillis();
		if (player.getAttackedByDelay() + 10000 > currentTime) {
			player.packets()
					.sendMessage(
							"You can't transform until 10 seconds after the end of combat.");
			return false;
		}
		if (invalidArea(player)) {
			player.sendMessage("You cannot transform at this location.");
			return false;
		}
		switch (item.getId()) {
		
		}
		return false;
	}
	
	/**
	 * Transforms the player to a form.
	 */
	public static final void transform(Player player, int transformNPC) {
		switch (transformNPC) {
		case 2719:
		case 2720:
		case 2721:
		case 3092:
		if	(!player.getAppearance().isMale())
			player.getAppearance().transformIntoNPC(Integer.valueOf(transformNPC));
			break;
		}
	}
	/**
	 * Removes a transform, if you want to not use GFX just set 0 to gfxId 
	 * also message as 'null' if you don't want to send message or empty.
	 * @param player
	 * @param message
	 * @param gfxId
	 */
	public static void removeTransform(Player player, String message, int animation, int gfxId) {
		player.getAppearance().transformIntoNPC(-1);
		if (!(gfxId == 0)) 
			player.setNextGraphics(new Graphics(gfxId));
		if (!(animation == 0))
			player.setNextAnimation(new Animation(animation));
		if (!message.isEmpty()) 
				player.sendMessage(message);
			
	}
	
	
}
