package org.nova.game.player.content;

import java.io.Serializable;

import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Josh'
 * 
 */
public class ShatteredHeart implements Serializable {

	/**
	 * The unique serializable serial version UID
	 */
	private static final long serialVersionUID = 8623701940653286235L;
	
	/**
	 * The private instance of the player class
	 */
	private Player player;

	/**
	 * Sets the player
	 * @param player - the player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * The constructor
	 */
	public ShatteredHeart(Player player) {
		this.player = player;
		rocksAdded = new boolean[Misc.getItemsSize()];
	}
	
	/**
	 * The boolean[] of the rock pieces
	 */
	private boolean[] rocksAdded;
	
	/**
	 * Checks if the rocks have been added to the plinth
	 * @param rocks - the rocks added to the plinth
	 * @return rocksAdded
	 */
	public boolean hasAddedRock(int rocks) {
		return rocksAdded[rocks] || player.getInventory().containsItem(rocks, 1);
	}
	
	/**
	 * Gets the tools held in the boolean
	 * @return
	 */
	public boolean[] getRocks() {
		return rocksAdded;
	}

	/**
	 * Refreshes the plinth configs
	 */
	public void refreshPlinthConfigs() {
		player.packets().sendConfigByFile(7203, player.getRocksAddedToPlinth());
	}
	
	/**
	 * An integer[] containing all of the rock id(s)
	 */
	public int[] ROCK = { 15522, 15523, 15524, 15525, 15526, 15527, 15528, 15529, 15530, 15531, 15532, 15533, 15534,
		15535, 15536, 15537, 15538, 15539, 15540, 15541, 15542, 15543, 15544, 15545, 15546, 15547, 15548, 15549,
		15550, 15551};
	
	
	/**
	 * Checks if the players inventory has any rocks to be added to the plinth
	 * @param player - the player
	 * @return
	 */
	private boolean containsRocksToBeAdded(Player player) {
		for (int i : ROCK) {
			if (player.getInventory().containsOneItem(i)) 
				return true;
			}
			return false;
	}
	
	/**
	 * Handles adding the rocks to the plinth
	 * @param player - the player adding rocks to the plinth
	 */
	public void addRocksToPlinth(Player player) {
		if (player.getRocksAddedToPlinth() == 30) {
			//do the cutscene for the rocks here or a reward or whatever the fuck u want
		}
		if (!containsRocksToBeAdded(player)) {
			player.packets().sendMessage("You don't have any rocks to add to the plinth.");
			return;
		}
		player.packets().sendConfigByFile(7203, player.getRocksAddedToPlinth());
		player.rocksAddedToPlinth ++;
		refreshPlinthConfigs();
		player.packets().sendMessage("You add the rock to the plinth");
	}
}