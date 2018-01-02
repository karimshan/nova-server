package org.nova.game.player.content.itemactions;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class BoltEnchanting {
	
	public BoltEnchanting() {
	setPlayer(player);	
	}

	private Player player;
	
	
	private int packetId;
	private int enchantAmount;
	
	private static final int FIRST_OPTION = 61;
	private static final int SECOND_OPTION = 64;
	private static final int THIRD_OPTION = 4;
	
	public enum Bolts {
		OPAL(879);
		
	private int boltId;
	
	private Bolts(int boltId) {
		this.boltId = boltId;
	}

	public int getBoltId() {
		return boltId;
	}
}

	public boolean checkRunes(Player player, int... runes) {
	int runesCount = 0;
	
	while (runesCount < runes.length) {
		int runeId = runes[runesCount++];
		int ammount = runes[runesCount++];
		if (player.getInventory().containsItem(runeId, ammount))
		continue;
		else
			player.sendMessage("You don't have enough runes to create these bolts.");
	}
		return false;
	}
	
	public boolean handleEnchantingInterface(Player player, int componentId) {
		switch (componentId) {
		case 14:
			switch (packetId) {
			case FIRST_OPTION:
				if (checkRunes(player, 526)) {
					
				}
				break;
			}
			break;
			
		case 29:
			break;
		case 18:
			break;
		case 22:
			break;
		case 32:
			break;
		case 26:
			break;
		case 35:
			break;
		case 38:
			break;
		case 41:
			break;
		case 44:
			break;
		}
		return false;
	}
	public void enchantBolts() {
	player.interfaces().closeChatBoxInterface();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getEnchantAmount() {
		return enchantAmount;
	}

	public void setEnchantAmount(int enchantAmount) {
		this.enchantAmount = enchantAmount;
	}

	

}
