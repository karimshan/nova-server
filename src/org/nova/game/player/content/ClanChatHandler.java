package org.nova.game.player.content;

import org.nova.game.player.Player;
import org.nova.network.Session;
import org.nova.network.stream.OutputStream;

/**
 * 
 * @author Josh'
 *
 */
public class ClanChatHandler {

	/**
	 * Handles the joining of the clans
	 * @param username - the players user name
	 * @param clanDetails - the clan details
	 */
	public void joinClan(Player player, Session session, String username, String clanDetails) {
		OutputStream stream = new OutputStream();
		stream.writeByte(1);
		stream.writePacketVarShort(7);
		if (!player.isInClanChat()) {
			int clans = 2;
			int chats = 1;
			/**
			 * Bytes
			 * - the clans
			 */
			stream.writeByte(clans);
			stream.writeByte(0);
			stream.writeByte(0);
			stream.writeByte(-1);
			/**
			 * Longs
			 * - the data of the clans
			 */
			stream.writeLong(4062231702422979939L);
			stream.writeLong(0L);
			/**
			 * String
			 * - the clan details
			 */
			stream.writeString(clanDetails);
			/**
			 * Short
			 * - the chats
			 */
			stream.writeShort(chats);
			if (chats > 0) {
				for(int i = 0; i < chats; i++) {
					stream.writeByte(126);
					stream.writeShort(1);
				}
			}
			player.packets().sendMessage(!player.isInClanChat() ? "You join the clan chat." : "You leave the clan chat.");
			if (player.isInClanChat()) {
				player.inChat = false;
			}
			player.inChat = true;
		}
		stream.endPacketVarShort();
		session.write(stream);
	}
}
			
			
