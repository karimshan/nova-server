package org.nova.game.player.content.itemactions;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 * @since 11.12.2013
 * @category Represents player's own rock plinth.
 */
public class RockPlinth {
	/**
	 * Position of the rock.
	 */
	Location plinthTile = new Location(3254, 3445, 0);
	/**
	 * A container holding all the strange rocks.
	 */
	public static final int[] STRANGE_ROCKS = {15522,15523,15524,15525,15526,15527,15528,15529,15530,15531};
	
	/**
	 * Plinth's object ids.
	 */
	public static final int DEFAULT_STAGE = 48627;
	public static final int SECOND_STAGE = 48628;
	public static final int THIRD_STAGE = 48629;
	public static final int FOURTH_STAGE = 48630;
	public static final int FIVETH_STAGE = 48631;
	public static final int SIXTH_STAGE = 48627;
	public static final int FINAL_STAGE = 48627;
	/**
	 * The final statue.
	 */
	private static final GlobalObject FINAL_STATUE = null;
	/**
	 * Construct's the rock plinths.
	 * @param player
	 */
	public RockPlinth(Player player) {
		this.player = player;
	}

	/**
	 * We will load the plinth.
	 * @param player
	 */
	public void loadPlinth() {
		switch (player.getPlinthStage()) {
		case 0:
			//None
			break;
		case 1:
			//World.spawnObject(new WorldObject(48627, 10, 3254, 3445, 0, true);
		Game.spawnObject(new GlobalObject(48627, 10,0, 3254, 3445,0), true); 
			//	World.spawnTemporaryObject(new WorldObject(SECOND_STAGE, 10, 0, plinthTile), Short.MAX_VALUE, true);
			break;
		case 2:
			Game.spawnObject(new GlobalObject(48628, 10,0, 3254, 3445,0), true); 
			//World.spawnTemporaryObject(new WorldObject(SECOND_STAGE, 10, 0, plinthTile), Short.MAX_VALUE, true);
			break;
		}
	}
	/**
	 * Checks the statue progress.
	 */
	public void checkStatue() {
		
	}
	/**
	 * Adds a strange rock.
	 */
	public void addStrangeRock() {
		checkAll();
	/*	for (int i : STRANGE_ROCKS) {
			Item statue = new Item(i);
			player.getInventory().deleteItem(statue.getId(), 1);
			player.sendMessage(getMessage());
			increasePlinth();
			loadPlinth();
		}*/
		
	}
	public void checkAll() {
	//	for (int i : STRANGE_ROCKS) {
		//	if (!player.getInventory().containsItem(i, 1)) {
			//	player.sendMessage("You dont have any strange rocks with you.");
				//return;
			//}
	//	}
		
	}
	/**
	 * Adds a random amount of xp.
	 * @param number
	 * @param toNumber
	 */
	public void addRandomXPAmount(int number, int toNumber) {
	
	}
	/**
	 * Finishes a statue.
	 * <reset> as well.
	 */
	public final void finishStatue() {
	double skillXp;
		/**
		 * If theres double xp weekend we ofc * 2 (skillXp x 2)
		 */
		//	skillXp = Constants.ROCKPLINTH_XP_REWARD * 2;
		skillXp = 250.0D + Misc.getRandomDouble(600.0D);;
		player.sendMessage("Congratulations, you have finished the statue. You have been awarded with some skilling experience.");
		for (int skills = Integer.lowestOneBit(7); skills < Integer.highestOneBit(Misc.getRandom(13)); skills ++) {
			player.getSkills().addSkillXpRefresh(skills, skillXp);
			reset();
		}
	}
	/**
	 * Resets the plinth.
	 */
	private void reset() {
		for (int ids = 48627; ids < 48635; ids++)
		Game.spawnTempGroundObject(new GlobalObject(DEFAULT_STAGE, 10, 0, plinthTile), ids, Short.MAX_VALUE);
	//	World.removeObject(new WorldObject(FINAL_STATUE)), 0);
		player.setPlinthStage(0);
	}
	/**
	 * Gets the message when player adds a strange rock.
	 * @return message
	 */
	public String getMessage() {
		int index = player.getPlinthStage();
		switch (index) {
		case 0:
			return "You add a strange rock to the statue.";
		case 1:
			return "You add a strange rock to the statue. It seems to be some kind of statuette.";
		case 2:
			return "";
		case 3:
			return "";
		case 4:
			return "";
		case 5:
			return "";
		}
		return null;
	}
	/**
	 * Sets the plinth to next stage.
	 * @return
	 */
	public int increasePlinth() {
		int index = player.getPlinthStage();
		
		switch (index) {
		case 0:
		return player.setPlinthStage(1);
		case 1:
		return player.setPlinthStage(2);
		case 2:
		return player.setPlinthStage(3);
		case 3:
		return player.setPlinthStage(4);
		case 4:
		return player.setPlinthStage(5);
		case 5:
		return player.setPlinthStage(6);
		}
		return increasePlinth();
	}
	/**
	 * Handles the plinth interactions.
	 * @param player
	 * @param gameObject
	 * @return
	 */
	public boolean handleObjectInteractions(Player player, GlobalObject gameObject) {
		switch (gameObject.getId()) {
		case DEFAULT_STAGE:
			addStrangeRock();
		break;
		}
		return false;
	}
	/**
	 * Player instance.
	 */
	private Player player;
	
	
	/**
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}
	/**
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}
