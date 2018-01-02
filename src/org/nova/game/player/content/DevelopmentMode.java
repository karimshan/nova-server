package org.nova.game.player.content;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @since 31.1.2014
 * @information A file dedicated to handle world-wide developer actions.
 */
public class DevelopmentMode {
	/**
	 * The interface used all the time for developers.
	 */
	private static final int DEVELOPER_INTERFACE = 837;
	
	/**
	 * Loads the interface upon login.
	 * @return
	 */
	public static final boolean load(Player player) {
		if (player.getRights() == 2 && player.isDeveloperMode()) {
			player.sendMessage("Welcome back, "+player.getDisplayName()+".");
			loadInterfaces(player);
		}
		return false;
	}
	/**
	 * Loads the developer interfaces.
	 * 
	 */
	private static void loadInterfaces(Player player) {
		for (int i = 4; i < 10; i++) 
			player.packets().sendIComponentText(DEVELOPER_INTERFACE, i, "");
		player.interfaces().sendTab(player.interfaces().isFullScreen() ? 11
				: 19, DEVELOPER_INTERFACE);

		player.packets().sendIComponentText(DEVELOPER_INTERFACE, 8, "Welcome!");
		player.packets().sendIComponentText(DEVELOPER_INTERFACE, 2, "X: "+player.getX()+"");
		player.packets().sendIComponentText(DEVELOPER_INTERFACE, 3, "Y: "+player.getY()+"");
		
	}
	
	public static final void close(Player player) {
	player.setDeveloperMode(false);
	player.sendMessage("You close the developer mode.");
	closeInterfaces(player);
	}
	
	public static final void updatePosition(Player player) {	
	player.packets().sendIComponentText(DEVELOPER_INTERFACE, 8, "Welcome!");
	player.packets().sendIComponentText(DEVELOPER_INTERFACE, 2, ""+player.getX()+"");
	player.packets().sendIComponentText(DEVELOPER_INTERFACE, 3, ""+player.getY()+"");
		player.interfaces().sendTab(player.interfaces().isFullScreen() ? 11
				: 19, DEVELOPER_INTERFACE);
	}
	
	private static void closeInterfaces(Player player) {
		player.packets()
		.closeInterface(
				player.interfaces().isFullScreen() ? 11
						: 0, DEVELOPER_INTERFACE);
	}
}

