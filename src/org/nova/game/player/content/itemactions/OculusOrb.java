package org.nova.game.player.content.itemactions;



import java.awt.event.KeyEvent;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class OculusOrb {

	
	public static void startOrb(final Player player) {
	if (player.getControllerManager().getControler() != null) {
		player.sendMessage("You can not use the orb here.");
		return;	
	}
	if (player.isDead()) {
		player.sendMessage("You cannot view the orb currently.");
		return;
	}
	player.packets().sendWindowsPane(475, 0);
	player.packets().sendInterface(true, 475, 57, 751);
	player.packets().sendInterface(true, 475, 55, 752);
	player.setCloseInterfacesEvent(new Runnable() {

		@SuppressWarnings("null")
		@Override
		public void run() {
			@SuppressWarnings("deprecation")
			KeyEvent key = null;
			player.packets().sendWindowsPane(player.interfaces().isFullScreen() ? 746 : 548, 0);
			player.packets().sendResetCamera();
		if (key.equals(KeyEvent.VK_ESCAPE)) {
			player.packets().sendResetCamera();
		}
		}
		
	});
	}
}
