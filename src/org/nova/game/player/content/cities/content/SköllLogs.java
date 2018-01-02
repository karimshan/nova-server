package org.nova.game.player.content.cities.content;

import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class SköllLogs {
	/**
	 * Logs that can be stored.
	 */
	private static final int[] LOGS = {1511,1513,1515,1517,1519,1521,2511,2862,3438,3440,3442,3444,3446,3448,6211};
	
	public static final int STORE_LOGS_ANIM = -1;
	/**
	 * How many logs are stored. Static because it's for everyone.
	 */
	private static int logsStored;
	/**
	 * Stores a log(s).
	 * @param player
	 * @param item
	 */
	public static void storeLogs(Player player, Item item) {
		player.addStopDelay(3);
	for (int i : LOGS) {
		if (!player.getInventory().containsItem(i, 1)) {
			player.sendMessage("I should add some more logs to this bonfire.");
			return;
		}
		player.setNextAnimation(new Animation(STORE_LOGS_ANIM));
		player.getInventory().deleteItem(item.getId(), item.getAmount());
		logsStored += item.getAmount();
		player.sendMessage("You have stored "+item.getAmount()+" "+item.getName()+" to the bonfire.");
		//If the bonfire contains more than 120 logs it will spawn a Sköll instantly and reset the counts.
		if (logsStored >= 119) {
			addSköll(player);
			logsStored = 0;
		}
	}
}
	/**
	 * Checks the stored logs amount.
	 * @param player
	 */
	public static void checkLogs(Player player) {
		player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 111, "The bonfire contains currently "+logsStored+" logs, "+player.getDisplayName()+".");
	}
	/**
	 * Adds a Sköll.
	 * @param player
	 */
	public static void addSköll(Player player) {
		Location tile = new Location(player.getX(),player.getY(),player.getZ());
		GlobalObject gameObject = new GlobalObject((GlobalObject) new Location(1,1,1));
		Game.spawnNPC(13461, new Location(gameObject.getX() +3, gameObject.getY(), 0), -1, true);
		if (containsArea(tile)) {
			player.sendMessage("The Sköll Wolf has appeared, prepare yourself for a fight!");
		}
		spawnMinions(player.getX(), player.getY(), player.getZ());
	}
	
	public static void spawnMinions(int tileX, int tileY, int height) {
		tileX = Misc.getRandom(3);
		tileY = Misc.getRandom(3);
		height = 0;
		//Wolf ID TODO
		Game.spawnNPC(-1, new Location(tileX, tileY, height), -1, true);
		Game.spawnNPC(-1, new Location(tileX, tileY, height), -1, true);
		Game.spawnNPC(-1, new Location(tileX, tileY, height), -1, true);
		Game.spawnNPC(-1, new Location(tileX, tileY, height), -1, true);
		Game.spawnNPC(-1, new Location(tileX, tileY, height), -1, true);
	}
	/**
	 * Does player contain the Sköll -area?
	 */
	public static boolean containsArea(Location tile) {
	int destX = tile.getX();
	int destY = tile.getY();
	return (destX >= 2329 && destX <= 2355 && destY >= 3682 && destY <= 3701);
	}
}
