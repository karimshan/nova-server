package org.nova.kshan.content.keystroke;

import org.nova.game.player.Player;

/**
 * A class that represents a single key stroke.
 * 
 * @author K-Shan
 *
 */
public abstract class KeyStroke {
	
	protected Player player;
	protected int stage = 0;
	protected Object[] data;
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public abstract void press(int key, char keyChar, boolean shiftHeld);
	
	public void terminate() {
		player.getKeyStrokes().terminate();
	}

	protected String getKey(int key) {
		return KeyStrokeData.getKey(key);
	}

}
