package org.nova.game.player.content;

import java.util.HashMap;
import java.util.Map;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @since 22.2.2014
 * @information Created this file to
 * manage most of the ladders in-game.
 */
public class Ladders {

	/**
	 * 
	 * @author Fuzen Seth
	 * This enum represents the handled ladders.
	 * @since 22.2.2014
	 */
	public enum LadderBase {
		VARROCK_BANK(24360, new Location(3190, 9833, 0)),
		VARROCK_BANK_UNDERGROUND(24360, new Location(3188,3433,0));
		
		private int id;
		//private double experience;
		private Location nextLocation;

		private static Map<Integer, LadderBase> ladders = new HashMap<Integer, LadderBase>();

		static {
			for (LadderBase ladder : LadderBase.values()) {
				ladders.put(ladder.getId(), ladder);
			}
		}

		public static LadderBase forId(int id) {
			return ladders.get(id);
		}

		private LadderBase(int id, Location nextLocation) {
			this.id = id;
			this.setNextLocation(nextLocation);
		}

		public int getId() {
			return id;
		}

	public static boolean manageLadders(final Player player, int object, GlobalObject gameObject) {

		if (gameObject == null || LadderBase.forId(gameObject.getId()) == null)
			return false;
		
		final LadderBase ladders = LadderBase.forId(gameObject.getId());
		
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (ladders.getNextLocation().matches(new Location(-1, -1, -1))) {
					System.out.println(""+player.getDisplayName()+"'s ladder is nulled. "+ladders.getId()+", "+ladders.getNextLocation()+".");
					stop();	
					return;
				}
				player.setLocation(new Location(ladders.getNextLocation()));
				stop();
			}

		}, 2);
		return false;
	}

	public Location getNextLocation() {
		return nextLocation;
	}

	public void setNextLocation(Location nextLocation) {
		this.nextLocation = nextLocation;
	}
	}
}
