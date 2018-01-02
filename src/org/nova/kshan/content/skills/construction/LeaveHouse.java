package org.nova.kshan.content.skills.construction;

import org.nova.game.player.Player;
import org.nova.game.player.content.Magic;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class LeaveHouse extends Dialogue {
	
	Player host;
	boolean myHouse;
	
	@Override
	public void start() {
		host = (Player) data[0];
		myHouse = (boolean) data[1];
		sendOptions("Leave "+(myHouse ? "your" : ""+host.getDisplayName()+"'s")+" house?", "Yes", "No");
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		if(buttonId == 1)
			Magic.telePlayer(player, player.getHouse().getHouseLocation(), true);
		end();
	}

	@Override
	public void finish() {
		
	}
	
}
