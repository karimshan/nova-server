package org.nova.game.player.content.quests;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.map.RegionBuilder;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the Recipe for Disaster quest.
 */
public class RecipeForDisaster {
	
	/**
	 * Holds the chest's id.
	 */
	private static final int CHEST_ID = -1;
	
	private static RecipeForDisaster singleton = new RecipeForDisaster();
	/**
	 * The current wave.
	 */
	private int wave;
	
	private boolean inBattle;
	
	/**
	 * Monsters
	 */
 public static final int[] MONSTERS = {14544,14544,14544,14544,14544,14544,14544,14544,14544,14544,};
	/**
	 * Region chucks.
	 */
	private int[] regionChucks;
	/**
	 * Stage
	 */
	public static enum Stage {
		STARTING,
		LOADING,
		FINISHED;
	}
	
	/**
	 * Starts the quest.
	 * @param player
	 */
	public void start(final Player player) {
		player.addStopDelay(12);
		setInBattle(true);
		player.setTeleport(true);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				switch (loop) {
				case 0:
					loadMap(player);
					break;
				case 3:
					player.setLocation(getLocation(10, 19) );
					break;
				case 4:
					if (player.getRecipeStage() == 0)
					player.sendMessage("You are not allowed to use prayer during this battle.");
					break;
				case 5:
					break;
				case 9:
					addMonster(player);
					stop();
					if (player.getRecipeStage() == 0)
					player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 3387, "You will regret following me!");
					break;
				}
				loop++;
				}
			}, 0, 1);
		
	}
	/**
	 * Adds the correct monster.
	 * @param player
	 */
	public void addMonster(Player player) {
		switch (player.getRecipeStage()) {
		case 0:
			Game.spawnNPC(MONSTERS[0], new Location(player.getX() + 3, player.getY(), player.getZ()), -1, true);
			for (NPC n : Game.getNPCs()) {
				if (n.getId() == 14544)
					n.setForceAgressive(true);
			}
			break;
		case 1:
			Game.spawnNPC(MONSTERS[1], new Location(player.getX() + 3, player.getY(), player.getZ()), -1, false);
			break;
		case 2:
			Game.spawnNPC(MONSTERS[2], new Location(player.getX() + 3, player.getY(), player.getZ()), -1, false);
			break;
		case 3:
			Game.spawnNPC(MONSTERS[3], new Location(player.getX() + 3, player.getY(), player.getZ()), -1, false);
			break;
		case 4:
			Game.spawnNPC(MONSTERS[4], new Location(player.getX() + 3, player.getY(), player.getZ()), -1, false);
			break;
		case 5:
			Game.spawnNPC(MONSTERS[5], new Location(player.getX() + 3, player.getY(), player.getZ()), -1, false);
			break;
		}
		for (NPC n : Game.getNPCs())
			if (n.getId() == MONSTERS[0]) {
			player.hints().addHintIcon(n, 0, false, false);
			}
	}
	
	
	/**
	 * Loads the private map for player.
	 */
	public void loadMap(Player player) {
	regionChucks = RegionBuilder.findEmptyChunkBound(8, 8); 
	RegionBuilder.copyAllPlanesMap(235, 667, regionChucks[0], regionChucks[1], 8);
	
	}
	/**
	 * Settings
	 */
	private boolean prayerAllowed;
	
	public static final void traceStages(Player player) {
		QuestMessage.setTitle(player, "Recipe for Disaster");
		switch (player.getRecipeStage()) {
		case 0:
			new QuestMessage(player, "To start this quest head to Lumbridge. From the first floor you'll find a chef, the chef is a very respected person who cooks for the Lumbridge castle members. Your mission is to speak with him.");
			break;
		case 1:
			new QuestMessage(player, "You have defeated the first mystical monster, congratulations. Speak to the cook again to continue this battle quest.");
			break;
		}
	}
	/**
	 * Exits from the quest.
	 */
	public void exit(Player player) {
		setInBattle(false);
		player.setTeleport(false);
		player.setRemovedPrayers(false);
		player.setLocation(new Location(3207,3216,0));
		player.hints().removeAll();
		player.reset();
		RegionBuilder.destroyMap(regionChucks[0], regionChucks[1], 8, 8);
	}
	
	public static final boolean handleChest(Player player, GlobalObject gameObject) {
		if (gameObject.getId() == CHEST_ID) {
			switch (player.getRecipeStage()) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
				
			}
		}
		return false;
	}
	
	public Location getSpawnTile() {
		return getLocation(15, 19);
		}
	
	public int getWave() {
		return wave;
	}
	public Location getLocation(int mapX, int mapY) {
		return new Location(regionChucks[0]*8 + mapX, regionChucks[1]*8 + mapY, 2);
	}

	public void setWave(int wave) {
		this.wave = wave;
	}
	public boolean isInBattle() {
		return inBattle;
	}
	public void setInBattle(boolean inBattle) {
		this.inBattle = inBattle;
	}
	public static RecipeForDisaster getSingleton() {
		return singleton;
	}
	
}
