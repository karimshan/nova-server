package org.nova.game.item;

import org.nova.game.map.Location;
import org.nova.game.player.Player;
@SuppressWarnings("serial")
public class FloorItem extends Item {

	public Location tile;
	public Player owner;
	private boolean invisible;
	private boolean grave;

	public FloorItem(int id) {
		super(id);
	}

	@Override
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public FloorItem(Item item, Location tile, Player owner,
			boolean underGrave, boolean invisible) {
		super(item.getId(), item.getAmount());
		this.tile = tile;
		this.owner = owner;
		grave = underGrave;
		this.invisible = invisible;
	}

	public Location getLocation() {
		return tile;
	}

	public boolean isGrave() {
		return grave;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public Player getOwner() {
		return owner;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

}
