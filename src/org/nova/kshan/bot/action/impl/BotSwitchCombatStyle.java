package org.nova.kshan.bot.action.impl;

import org.nova.game.engine.GameTick;
import org.nova.kshan.bot.action.BotAction;

/**
 * 
 * @author K-Shan
 *
 */
public class BotSwitchCombatStyle extends BotAction {
	
	private String playerMessage;

	@Override
	public GameTick getGameTick() {
		
		playerMessage = (String) data[0];
		
		return new GameTick(1.875) {

			@Override
			public void run() {
				bot.publicChat(playerMessage);
				bot.publicChat("Got it, thank you! Training ");
				bot.getCombatDefinitions().setAttackStyle(1);
				stopAction();
			}
			
		};
	}

	@Override
	public String getActionType() {
		return "SWITCH_COMBAT_STYLE";
	}

}
