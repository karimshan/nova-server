package org.nova.kshan.content.keystroke;

import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public class KeyStrokeManager {
	
	private Player player;
	private KeyStroke keyStroke;

	public KeyStrokeManager(Player player) {
		this.player = player;
	}

	public void run(KeyStroke key, final Object... data) {
		if(keyStroke != null)
			keyStroke.terminate();
		this.setKeyStroke(key);
		if (keyStroke == null)
			return;
		keyStroke.data = data;
		keyStroke.setPlayer(player);
	}
	
	public void terminate() {
		setKeyStroke(null);
	}
	
	public KeyStroke getKeyStroke() {
		return keyStroke;
	}

	private void setKeyStroke(KeyStroke keyStroke) {
		this.keyStroke = keyStroke;
		
	}

}
