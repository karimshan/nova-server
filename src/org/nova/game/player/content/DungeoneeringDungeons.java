package org.nova.game.player.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the resource dungeons.
 * @since 13.4.2014
 */
public class DungeoneeringDungeons {

	/**
	 * 
	 * @author Fuzen Seth
	 * Holds data for resource dungeons.
	 */
	public static enum Dungeons {
		BAXTORIAN_WATERFALL_DUNGEON(36,  52853, new Location(1256, 4591, 0)),
		AL_KHARID_DUNGEON(76,  52860, new Location(1181,4515,0)),
		VARROCK_HILL_GIANTS(6,  52853, new Location(1134,4589,0)),
		DWARVEN_RESOURCE_DUNGEON(14,52855, new Location(1041,4575,0)),
		DWARVEN_RESOURCE_EXIT(0, 52864, new Location(3034,9772,0)),
		VARROCK_HILL_GIANTS_RESOURCE_EXIT(0, 52868, new Location(3104,9826,0)),
		AL_KHARID_RESOURCE_EXIT(0, 52872, new Location(3298,3307,0));
		
		private int levelReq;
		private int objectId;
		private Location nextTile;
		
		private Dungeons(int levelReq, int objectId, Location nextTile) {
			this.levelReq = levelReq;
			this.objectId = objectId;
			this.nextTile = nextTile;
		}
		
		public int getLevelReq() {
			return levelReq;
		}
		
		public int getObjectId() {
			return objectId;
		}
		
		public Location getNextTile() {
			return nextTile;
		}
		
	}
	
	/**
	 * @param player
	 * @param gameObject
	 * @return
	 */
	public static final boolean handleGameObject(Player player, GlobalObject gameObject) {
		for (Dungeons dungeon : Dungeons.values()) {
			if (gameObject.getId() == dungeon.getObjectId()) {
				if (!(player.getSkills().getLevel(Skills.DUNGEONEERING) > dungeon.getLevelReq())) {
					player.sendMessage("You need a Dungeoneering level of "+dungeon.getLevelReq() + 1+" to enter "+gameObject.defs().name.toLowerCase()+".");
					return false;
	 }
	teleport(player, dungeon.getNextTile());
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Teleports the player to the resource dungeon.
	 */
	public static final void teleport(final Player player, final Location destination) {
			WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {		
				player.addStopDelay(3);
				if (loop == 0) {
                   player.setNextAnimation(new Animation(13288));
				player.setNextGraphics(new Graphics(2516));
				} else if (loop == 1) {
				player.setLocation(destination);
				player.setNextAnimation(new Animation(13285));
				player.setNextGraphics(new Graphics(2517));
				} else if (loop == 2) {
				stop();
				}
				loop++;
				}
			}, 0, 1);
		
	}
}
