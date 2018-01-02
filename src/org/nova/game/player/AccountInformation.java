package org.nova.game.player;

public class AccountInformation {
	
private Player player;

	public AccountInformation(Player player) {
		this.player = player;
	}
	
	public void viewInfo() {
	if (player.getMuteReason() == null) {
		//player.sendMessage("Congratulations, you don't have an active mute.");
	} else {
		
	}
	}
}
