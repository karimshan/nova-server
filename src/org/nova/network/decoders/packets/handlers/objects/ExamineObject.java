package org.nova.network.decoders.packets.handlers.objects;

import org.nova.game.map.GlobalObject;
import org.nova.game.player.Player;

/**
 * 
 * @author Karimshan Nawaz
 *
 */
public class ExamineObject {
	
	/**
	 * 
	 * @param player
	 * @param object
	 */
	public static void examine(Player player, GlobalObject object) {
		if(player.isOwner())
			player.sm(object.getInformation());
		player.packets().sendMessage(object.getExamine());
	}

}
