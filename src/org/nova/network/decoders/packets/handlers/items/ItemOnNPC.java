package org.nova.network.decoders.packets.handlers.items;

import org.nova.game.item.Item;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * 
 * @author Karimshan Nawaz
 *
 */
public class ItemOnNPC {
		
	/**
	 * 
	 * @param player
	 * @param stream
	 */
	public static void process(Player player, NPC npc, Item item) {
		player.sm("Item: ["+item.getId()+" - "+item.getName()+"] used on NPC: ["+npc.getId()+" - "+npc.getName()+"]");
		if(!player.getRandomEvent().canUseItemOnNPC(npc, item))
			return;
	}
}
