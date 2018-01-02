package org.nova.kshan.bot;

import java.util.ArrayList;
import java.util.List;

import org.nova.game.Game;
import org.nova.game.player.Player;

public class BotList {
	
	public static List<Bot> getBots() {
		List<Bot> bots = new ArrayList<Bot>();
		for(Player p : Game.getPlayers()) {
			if(p == null)
				continue;
			if(p.isBot())
				if(!bots.contains(p))
					bots.add((Bot) p);
		}
		return bots;
	}
	
	public static Bot get(String username) {
		for(Bot bot : getBots()) {
			if(bot == null)
				continue;
			if(bot.getUsername().equalsIgnoreCase(username.replace("_", " ")))
				return bot;
		}
		return null;
	}

}
