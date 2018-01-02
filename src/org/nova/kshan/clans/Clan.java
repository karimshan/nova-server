package org.nova.kshan.clans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.nova.game.Game;
import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public class Clan implements Serializable {

	private static final long serialVersionUID = -6212323029663482168L;
	
	@SuppressWarnings("unused")
	private Player player;
	private LinkedList<String> members;
	private List<String> bannedMembers;
	private ClanBank clanBank;
	private String name;
	private String leaderName = null;
	
	public Clan(Player player) {
		this.player = player;
		this.setClanBank(new ClanBank(player));
		this.members = new LinkedList<String>();
		this.bannedMembers = new ArrayList<String>();
		members.add(player.getUsername());
	}
	
	public ArrayList<Player> getOnlineMembers() {
		ArrayList<Player> online = new ArrayList<Player>();
		for(Player p : Game.getPlayers()) {
			if(p == null)
				continue;
			for(int i = 0; i < members.size(); i++)
				if(p.getUsername().equals(members.get(i)))
					if(!online.contains(p))
						online.add(p);
		}
		return online;
	}
	
	public LinkedList<String> getMembers() {
		return members;
	}
	
	public void setClanBank(ClanBank bank) {
		this.clanBank = bank;
	}
	
	public ClanBank getBank() {
		return clanBank;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getLeader() {
		return leaderName;
	}

	public void setLeader(String leader) {
		this.leaderName = leader;
	}

	public List<String> getBannedMembers() {
		return bannedMembers;
	}

}
