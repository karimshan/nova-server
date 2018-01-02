package org.nova.game.player.actions;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class Milestone extends Action {

	@Override
	public boolean start(Player player) {
		for (int skill = 0; skill < 25; skill++) {
			int skillLevel = 0;
			final boolean highestSkill = (player.getSkills().getLevel(skill) > skillLevel - 1);
		
		switch (skillLevel) {
		case 10:
			
				
			return true;
		}
	}
		return false;
	}

	@Override
	public boolean process(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int processWithDelay(Player player) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void stop(Player player) {
		// TODO Auto-generated method stub
		
	}

}
