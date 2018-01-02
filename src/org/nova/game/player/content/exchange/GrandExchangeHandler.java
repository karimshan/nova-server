package org.nova.game.player.content.exchange;

import org.nova.game.Game;
import org.nova.game.player.Player;
import org.nova.network.stream.Stream;

/**
 * 
 * @author 'Mystic Flow
 * @author `Discardedx2
 */
public class GrandExchangeHandler{

	public void decodeLogicPacket(Player player, Stream packet) {
		System.out.println("Packet: "+packet.getId());
		switch(packet.getId()) {
		case 139:
			int id = packet.readShortl();
			Game.getGrandExchange().buyItem(player, id);
			break;
		}
	}

}
