package org.nova.kshan.clans;

import org.nova.game.item.Item;
import org.nova.game.player.Bank;
import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public class ClanBank extends Bank {

	private static final long serialVersionUID = -8407057367296425323L;
	
	private Player player;
	
	public ClanBank(Player player) {
		this.player = player;
	}
	
	public void update() {
		for(Player p : player.getClan().getOnlineMembers())
			p.getClan().getBank().refreshItems();
	}
	
	@Override
	public void addItem(Item item, boolean refresh) {
		super.addItem(item, refresh);
		update();
	}
	
}
