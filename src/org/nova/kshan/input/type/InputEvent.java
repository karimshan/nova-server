package org.nova.kshan.input.type;

import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public abstract class InputEvent {

	protected Player player;
	public String message;
	public Object[] data;
	public abstract void whileTyping(int key, char keyChar, boolean shiftHeld);
	
	public InputEvent() {
		
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void sendScript(String message) {
		this.message = message;
	}
}
