package org.nova.game.player.content.guilds;

import org.nova.game.map.GlobalObject;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information Represents a single guild handled.
 * @since 10.4.2014
 */
public interface AbstractGuild {
	/**
	 * The guild's name.
	 * @param guild
	 * @return
	 */
	public abstract String getGuildName();
	/**
	 * Required level to enter the guild.
	 * @param level
	 * @return
	 */
	public abstract int requestedLevel();
	/**
	 * Enters to the guild.
	 * @param player
	 */
	public abstract void enterGuild(Player player);
	/**
	 * NPC interact handling.
	 * @param player
	 * @param npc
	 * @return
	 */
	public abstract boolean handleNPCInteract(Player player, NPC npc);
	/**
	 * Object interact handling.
	 * @param player
	 * @param gameObject
	 * @return
	 */
	public abstract boolean handleObjectInteract(Player player, GlobalObject gameObject);
	
}
