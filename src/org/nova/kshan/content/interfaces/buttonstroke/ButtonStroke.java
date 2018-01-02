package org.nova.kshan.content.interfaces.buttonstroke;

import org.nova.game.player.Player;

/**
 * A class that represents a single interface button press.
 * 
 * @author K-Shan
 *
 */
public abstract class ButtonStroke {
	
	protected Player player;
	protected int stage = 0;
	protected Object[] data;
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public abstract boolean press(int interfaceId, int buttonId, int slotId, int packetId);
	
	public void terminate() {
		player.getButtonStrokes().terminate();
	}

}
