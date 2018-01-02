package org.nova.kshan.content.minigames.duelarena;

import java.io.Serializable;
import java.util.ArrayList;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * Represents the duel
 * 
 * @author K-Shan
 *
 */
public class Duel implements Serializable {

	private static final long serialVersionUID = -7899171010854711315L;
	
	private transient Player player;
	private transient Player opponent;
	private transient ArrayList<Item> playerStakes;
	private transient ArrayList<Item> opponentStakes;
	private transient DuelType duelType;
	private transient DuelState duelState;
	
	public Duel(Player player, Player opponent, DuelType duelType) {
		this.player = player;
		this.opponent = opponent;
		this.duelType = duelType;
		setDuelState(DuelState.REQUESTING);
		player.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
				declined();
			}
			
		});
		opponent.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
				declined();
			}
			
		});
	}
	
	public static void sendDuelStart(Player player, Player other) {
		player.getData().getRuntimeData().put("requestingDuel", other);
		player.interfaces().sendInterface(640);
	}
	
	public static void handleDuelRequestButtons(Player player, Player other, int interfaceId, int buttonId) {
		if(interfaceId == 640) {
			boolean staked = false;
			if(buttonId == 18 || buttonId == 22) {
				staked = false;
				player.packets().sendConfig(283, 67108864);
			} else if (buttonId == 19 || buttonId == 21) {
				staked = true;
				player.packets().sendConfig(283, 134217728);
			} else if (buttonId == 20) {
				requestDuel(player, other, staked);
			}
		}
	}
	
	public static void requestDuel(Player player, Player other, boolean staked) {
		player.setDuel(new Duel(player, other, !staked ? DuelType.FUN : DuelType.STAKED));
		player.sm("Sending "+other.getUsername()+" a duel request...");
		other.packets().sendDuelRequest(player, !staked);
	}
	
	public void declined() {
		player.getData().getRuntimeData().remove("requestingDuel");
		player.setDuel(null);
		opponent.setDuel(null);
	}
	
	public void sendDuelConfigurations() {
		
	}
	
	public ArrayList<Item> getPlayerStakes() {
		return playerStakes;
	}
	
	public ArrayList<Item> getOpponentStakes() {
		return opponentStakes;
	}

	public Player getOpponent() {
		return opponent;
	}
	
	public DuelType getDuelType() {
		return duelType;
	}
	
	public DuelState getDuelState() {
		return duelState;
	}
	
	public void setDuelState(DuelState duelState) {
		this.duelState = duelState;
	}

}
