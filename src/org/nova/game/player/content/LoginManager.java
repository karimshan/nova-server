package org.nova.game.player.content;

import org.nova.game.player.Player;

public class LoginManager {

	public static void sendNews(Player player) {
		player.packets().sendIComponentText(827, 5, "Welcome to Nova.");
		player.packets().sendIComponentText(827, 6, "It's awesome to see you back, if you need any sort of help you can visit our forums at 'www.Nova-rs.com'.");
	}
}
