package org.nova.kshan.bot.action.impl;

import org.nova.game.engine.GameTick;
import org.nova.kshan.bot.action.BotAction;

/**
 * 
 * @author K-Shan
 *
 */
public class BotToggleRun extends BotAction {
	
	String response;

	@Override
	public GameTick getGameTick() {
		
		response = (String) data[0];
		
		return new GameTick(1.875) {

			@Override
			public void run() {
				bot.publicChat(response);
				stopAction();
			}
			
		};
	}

	@Override
	public String getActionType() {
		return "RESPONSE";
	}

}
