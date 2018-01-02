package org.nova.kshan.bot.action.impl;

import java.util.Arrays;

import org.nova.game.engine.GameTick;
import org.nova.kshan.bot.action.BotAction;

/**
 * 
 * @author K-Shan
 *
 */
public class BotResponse extends BotAction {
	
	String response;

	@Override
	public GameTick getGameTick() {
	
		response = (String) data[1];
		
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
