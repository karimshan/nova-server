package org.nova.game.player.content.guilds.impl;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.content.guilds.AbstractGuild;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the Cooking guild.
 * @since 10.4.2014
 */
public class CookingGuild implements AbstractGuild {
	
	public CookingGuild() {
		super();
	}
	
	/**
	 * The cooking guild Singleton.
	 */
	private static CookingGuild cookingGuild = new CookingGuild();
	/**
	 * Holds the doors object id.
	 */
	public static final int DOOR = 2712;
	/**
	 * The guild's name.
	 */
	@Override
	public String getGuildName() {
		return "Cooking guild";
	}
	/**
	 * Level requested to enter to the guild.
	 */
	@Override
	public int requestedLevel() {
		return 34;
	}
	/**
	 * Enters to the guild.
	 */
	@Override
	public void enterGuild(Player player) {
		
	}
	/**
	 * The NPC interact.
	 */
	@Override
	public boolean handleNPCInteract(Player player, NPC npc) {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * We handle the object interactions.
	 */
	@Override
	public boolean handleObjectInteract(final Player player, GlobalObject gameObject) {
	switch (gameObject.getId()) {
	case DOOR:
		if (player.getX() == 3143 && gameObject.getX() == 3143) {
			if (!(player.getSkills().getLevel(Skills.COOKING) > requestedLevel())) {
				player.sendMessage("You need a Cooking level of "+requestedLevel()+" to enter "+getGuildName()+".");
			return false;
			}
			final GlobalObject openedDoor = new GlobalObject(gameObject.getId(),
					gameObject.getType(), gameObject.getRotation() + 1, gameObject.getX(),
					gameObject.getY(), gameObject.getZ());
			WorldTasksManager.schedule(new WorldTask() {
				int loop;
				
				@Override
				public void run() {
					player.addStopDelay(2);
					switch (loop) {
					case 0:
						openedDoor.moveLocation(-1, 0, 0);
						openedDoor.setRotation(1);
						player.addWalkSteps(3143, 3444, -1, false);
						break;
					case 1:
						openedDoor.moveLocation(-1, 0, 0);
						openedDoor.setRotation(3);
						break;
					case 2:
						player.sendMessage("You enter to the "+getGuildName()+".");
						stop();
						break;
					}
					loop++;
					}
				}, 0, 1);
		}
		return true;
	}
		
		return false;
	}

	public static CookingGuild getCookingGuild() {
		return cookingGuild;
	}

}
