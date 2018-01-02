package org.nova.kshan.bot.action.impl;

import org.nova.game.engine.GameTick;
import org.nova.kshan.bot.action.BotAction;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class BotLevelUp extends BotAction {
	
	String messageType, skillName;
	int level;

	@Override
	public GameTick getGameTick() {
		
		messageType = (String) data[0];
		level = (Integer) data[1];
		skillName = (String) data[2];
		skillName = skillName.toLowerCase();
		
		final String[][] messages = { 
			{ "W00t, I just got "+level+" "+skillName+"!",
				"Yay, now I have "+level+" "+skillName+"!!", 
				"Yesssss! "+level+" "+skillName+"!",
				"Finally got "+level+" "+skillName+", lol."  },
			{ "I finally got the completionist cape!!!!",
				"After getting the comp cape, my life is now complete lol", 
				"woooooot!!!!!!!!!! I finally got the completionist cape!",
				"Got dat completionist cape, finally haha :)" },
			{ "Yay!!! I have all 99s now!",
				"Finally got everything 99 lol", 
				"Took long enough to get all 99s!",
				"W0000000000t!!!!! I'm so happy that I finally got all 99s!!!" }
		};
		
		return new GameTick(2.5) {

			@Override
			public void run() {
				stopAction();
				switch(messageType) {
					case "All 99s":
						bot.publicChat(messages[2][Misc.random(messages[2].length)]);
						break;
					case "Completionist Cape":
						bot.publicChat(messages[1][Misc.random(messages[1].length)]);
						break;
					default:
						bot.publicChat(messages[0][Misc.random(messages[0].length)]);
						break;
				}
			}
			
		};
	}

	@Override
	public String getActionType() {
		return "LEVEL_UP";
	}

}
