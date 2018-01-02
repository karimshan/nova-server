package org.nova.game.player.content;

import java.util.ArrayList;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @since 9.12.2013
 * @category Represents player lending a item to other player.
 */
public class ItemLending {
	/**
	 * We construct Item assisting and we set {@code <player>}.
	 */
	private ItemLending() {
		setPlayer(player);
	}
	
	public static final String LENDABLE_ITEMS[] = {"whip", "dragon claw"};
	
	/**
	 * The loaner and owner of the item.
	 */
	private ArrayList<Player> loaner, owner;
	
	/**
	 * The current item which is being lended.
	 */
	private int lendedItem;
	/**
	 * 
	 * @author Fuzen Seth
	 * @since 9.12.2013
	 * @category LendState represents a lending type.
	 */
	public enum LendState {
	NOT_LENDING,
	PENDING_LEND,
	LENDING,
	FINISHED_LENDING;
	
	public static LendState forId(int lendStateId) {
		switch (lendStateId) {
		/**
		 * Not lending currently.
		 */
		case 0:
			return NOT_LENDING;
			/**
			 * Nearly lending, pending lend.
			 */
		case 1:
			return PENDING_LEND;
			/**
			 * Lending a item.
			 */
		case 2:
			return LENDING;
			/**
			 * Has finished lending.
			 */
		case 3:
			return FINISHED_LENDING;
		}
		return null;
	}
}
	/**
	 * Adds a lend item in trade screen.
	 */
	public void addLendItem() {
		Item item = new Item(lendedItem);
		for (String s : LENDABLE_ITEMS) {
		if (!item.getName().toLowerCase().contains(s)) {
			player.sendMessage("" + item.getName() + " cannot be lended.");
			return;
		}
		/**
		 * TODO
		 */
		}
	}
	
	/**
	 * Starts the actual lending.
	 */
	public void startLendingSession() {
		owner.add(player);
		loaner.add(player);
	Item item = new Item(lendedItem);
	}
	/**
	 * The player.
	 */
	private Player player;
	
	/**
	 * Gets the loaner.
	 * @return
	 */
	public ArrayList<Player> getloaner() {
		return loaner;
	}
	/**
	 * Sets a player as a loaner.
	 * @param loaner
	 */
	public void setloaner(ArrayList<Player> loaner) {
		this.loaner = loaner;
	}
	/**
	 * We get {@code owner}
	 * @return
	 */
	public ArrayList<Player> getOwner() {
		return owner;
	}
	/**
	 * Sets player as a lend item owner.
	 * @param owner
	 */
	public void setOwner(ArrayList<Player> owner) {
		this.owner = owner;
	}
	/**
	 * Get player.
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}
	/**
	 * We set the player.
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	/**
	 * Gets the Lendable item.
	 * @return
	 */
	public int getLendedItem() {
		return lendedItem;
	}
	/**
	 * Adds the lendable item.
	 * @param lendedItem
	 */
	public void addLendableItem(int lendedItem) {
		this.lendedItem = lendedItem;
	}
}
