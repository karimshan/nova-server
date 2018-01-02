package org.nova.game.player.content.guilds;

import org.nova.game.player.content.guilds.impl.CookingGuild;

public class GuildSingleton {
	
	private static CookingGuild cookingGuild = new CookingGuild();

	public static CookingGuild getCookingGuild() {
		return cookingGuild;
	}
	
}
