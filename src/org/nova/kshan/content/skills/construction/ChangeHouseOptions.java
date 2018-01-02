package org.nova.kshan.content.skills.construction;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.player.content.Magic;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class ChangeHouseOptions extends Dialogue {

	@Override
	public void start() {
		sendOptions("Select an Option", "Leave your house.", "");//, "Keep your house open after you leave.");
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		if(buttonId == 1) {
			end();
			final PlayerHouse house = player.getHouse();
			if(!house.hasGuests()) {
				Magic.telePlayer(player, player.getHouse().getHouseLocation(), true);
				Game.submit(new GameTick(2.4) {
					
					@Override
					public void run() {
						stop();
						player.getRandomEvent().fullyStop(false);
						house.destroyHouse();
						player.sm("You have left your house."); // Past tense because you already left lol
					}
					
				});
			} else {
				player.getRandomEvent().fullyStop(false);
				player.setLocation(player.getHouse().getHouseLocation());
				player.sm("You leave your house.");
			}
		}
	}

	@Override
	public void finish() {
		
	}

}
