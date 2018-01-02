package org.nova.kshan.content;

import java.io.Serializable;

import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public class Membership implements Serializable {

	private static final long serialVersionUID = 8265102569984133227L;
	
	private Player player;
	
	public Player player() {
		return player;
	}
	
	public void player(Player p) {
		this.player = p;
	}
	
	public transient static final byte REGULAR = 1, EXTREME = 2, SUPREME = 3, 
			LEGENDARY = 4, DIVINE = 5, ULTIMATE = 6;
		
	private byte rankID;
	
	public byte getRankID() {
		return rankID;
	}
	
	public boolean isMember() {
		return getRankID() > 0;
	}
	
	public boolean isRegular() {
		return getRankID() == REGULAR;
	}

	public boolean isExtreme() {
		return getRankID() == EXTREME;
	}
	
	public boolean isSupreme() {
		return getRankID() == SUPREME;
	}
	
	public boolean isLegendary() {
		return getRankID() == LEGENDARY;
	}

	public boolean isDivine() {
		return getRankID() == DIVINE;
	}
	
	public boolean isUltimate() {
		return getRankID() == ULTIMATE;
	}
	
	public void setRank(byte rank) {
		this.rankID = rank;
	}
	
	public String getRank() {
		return !isMember() ? "Inactive" : getNameFromRank(getRankID());
	}
	
	public String getNameFromRank(int rank) {
		switch(rank) {
			case 1:
				return "Regular";
			case 2:
				return "Extreme";
			case 3:
				return "Supreme";
			case 4:
				return "Legendary";
			case 5:
				return "DIVINE";
			case 6:
				return "ULTIMATE";
		}
		return "Inactive";
	}

}
