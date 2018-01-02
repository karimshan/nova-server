package org.nova.kshan.bot.action;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.kshan.bot.Bot;

/**
 * 
 * @author K-Shan
 *
 */
public abstract class BotAction {
	
	protected Bot bot;
	protected Object[] data;
	private boolean responding;

	public abstract GameTick getGameTick();
	public abstract String getActionType();

	public void startAction() {
		if(!isResponding())
			setResponding(true);
		processAction();
	}
	
	public void processAction() {
		if(isResponding())
			Game.submit(bot, getGameTick());
		else
			stopAction();
	}
	
	public void stopAction() {
		bot.getBotActionHandler().stopAction();
	}
	
	public boolean isResponding() {
		return responding;
	}
	
	public void setResponding(boolean responding) {
		this.responding = responding;
	}

}
