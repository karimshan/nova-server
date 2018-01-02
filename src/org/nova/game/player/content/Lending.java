package org.nova.game.player.content;

import java.util.ArrayList;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class Lending {

	public Lending() {
		setPlayer(player);
	}
	/**
	 * Lend items container.
	 */
	public static final int LEND_ITEMS[] = {/* TODO All lend items here.*/};
	
	/**
	 * Owner of the item
	 */
	private ArrayList<Player> owners;
	/**
	 * The lended item.
	 */
	private ArrayList<Item> lendables;
	/**
	 * Gets @ item
	 */
	private Item item;
	
	private int lendableItem = item.getId();
	/**
	 * The player whos lending.
	 */
	private ArrayList<Player> lenders;
	/**
	 * @Player - Instance
	*/
	private Player player;
	/**
	 * Lends a item.
	 */
	public final void lend() {
	if (!player.isLending()) {
	player.sendMessage("You are already lending your "+lendables.toString());
		return;
	}
	owners.add(player);
	lendables.add(new Item(lendableItem));
	lenders.add(player);
	
}
	/**
	 * Submits a lending task @ world.
	 */
	public void submitLendingTask() {
		
	}
	
	/**
	 * How long a item can be lended.
	 * @return
	 */
	public long getLendTime() {
		return player.getLendingTill();
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
