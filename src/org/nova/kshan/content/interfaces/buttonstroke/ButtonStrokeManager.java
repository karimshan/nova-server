package org.nova.kshan.content.interfaces.buttonstroke;

import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public class ButtonStrokeManager {
	
	private Player player;
	private ButtonStroke buttonStroke;

	public ButtonStrokeManager(Player player) {
		this.player = player;
	}

	public void run(ButtonStroke key, final Object... data) {
		if(buttonStroke != null)
			buttonStroke.terminate();
		this.setButtonStroke(key);
		if (buttonStroke == null)
			return;
		buttonStroke.data = data;
		buttonStroke.setPlayer(player);
	}
	
	public void terminate() {
		setButtonStroke(null);
	}
	
	public ButtonStroke getButtonStroke() {
		return buttonStroke;
	}

	public void setButtonStroke(ButtonStroke buttonStroke) {
		this.buttonStroke = buttonStroke;
		
	}

}
