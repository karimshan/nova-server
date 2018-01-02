package org.nova.game.player.content.exchange;

import org.nova.game.player.Player;
import org.nova.game.player.content.exchange.GrandExchange.Type;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author 'Mystic Flow
 * @author `Discardedx2
 */
public final class Offer {

	private final String owner;
	private final Type type;
	private final int box;
	private final int amount;
	private final int price;
	
	private boolean aborted = false;
	private int currentAmount;
	private int id;
	private transient Player player;

	public int coins = 0;
	
	public Offer(String owner, int box, int id, int amount, int price) {
		this.owner = owner;
		this.type = Type.BUY;
		this.box = box;
		this.id = id;
		this.amount = amount;
		this.currentAmount = amount;
		this.price = price;
	}

	public String getOwner() {
		return owner;
	}

	public int getBox() {
		return box;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public int getPrice() {
		return price;
	}

	public boolean isAborted() {
		return aborted;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	public Player getPlayer() {
		if(player == null) player = Misc.player(owner);
		return player;
	}

	public Type getType() {
		return type;
	}
	
	public void setAborted(boolean b) {
		this.aborted = b;
	}
	
	public void setId(int id) {
		this.id = id;
	}

}