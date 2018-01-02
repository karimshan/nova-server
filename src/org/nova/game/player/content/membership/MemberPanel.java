package org.nova.game.player.content.membership;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class MemberPanel {
	
	private transient Player player;
	
	public MemberPanel(Player player) {
		this.player = player;
	}
	
	public void openPanel() {
		
	}
	
	public void addSpecial() {
		player.sendMessage("Your special attack has been restored.");
	}
	
}
