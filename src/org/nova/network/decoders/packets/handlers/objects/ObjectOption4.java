package org.nova.network.decoders.packets.handlers.objects;

import org.nova.game.map.GlobalObject;
import org.nova.game.player.Player;

/**
 * 
 * @author Karimshan Nawaz
 *
 */
public class ObjectOption4 {
	
	/**
	 * 
	 * @param player
	 * @param object
	 */
	public static void process(final Player player, final GlobalObject object) {
		if (!player.getRandomEvent().hasObjectOption4(object))
			return;
	}

}
