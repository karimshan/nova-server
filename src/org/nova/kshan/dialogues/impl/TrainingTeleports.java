package org.nova.kshan.dialogues.impl;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.map.Location;
import org.nova.game.player.HintIcon.HintDirections;
import org.nova.game.player.content.Magic;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class TrainingTeleports extends Dialogue {
	
	@Override
	public void start() {
		sendOptions("Where would you like to train?", new String[] { 
				"Rock crabs", "Lumbridge cows", "Hell hounds", "Death Wings and Ogres", "Next Page" });
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		if(stage == 0) {
			if(buttonId == OPTION1)
				Magic.specialTele(player, new Location(2673, 3711, 0), true);
			else if(buttonId == OPTION2)
				Magic.specialTele(player, new Location(3254, 3267, 0), true);
			else if(buttonId == OPTION3)
				Magic.specialTele(player, new Location(2870, 9845, 0), true);
			else if(buttonId == OPTION4) {
				Magic.specialTele(player, new Location(2584, 3379, 0), true);
				Game.submit(new GameTick(4) {
					public void run() {
						player.hints().sendHint(2562, 3356, 30, 10, HintDirections.CENTER);
						player.sm("Follow the yellow arrow indicated on the minimap to find the ladder.");
						stop();
					}
				});
			}
		}
		end();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
