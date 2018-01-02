package org.nova.game.player.content;

import org.nova.game.player.Player;
import org.nova.network.stream.InputStream;

public class ClanChatManager {

	public static void handleButtons(Player player, int buttonId, int packetId,
			InputStream stream) {
		String username = player.getDisplayName();
		String clanName = "Nova";

		if (buttonId == 85 && packetId == 61) {
			player.packets().sendJoinClanChat(username = "Lucifer", clanName);
		}

		if (buttonId == 80 && packetId == 61) {
			if (player.inClanChat == false) {
				player.packets().sendMessage(
						"You need to be in a clan chat to view the settings");
			} else if (player.inClanChat == true) {
				player.interfaces().sendInterface(1096);
			}
		}

	}

}
