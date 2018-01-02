package org.nova.kshan.content.interfaces;

import org.nova.Constants;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.game.player.content.Magic;
import org.nova.kshan.content.areas.c_o_r.C_O_R_Data;
import org.nova.kshan.content.interfaces.panels.AdminPanel;
import org.nova.kshan.content.interfaces.panels.ModPanel;
import org.nova.kshan.content.interfaces.panels.OwnerPanel;
import org.nova.kshan.content.interfaces.panels.UserPanel;

/**
 * 
 * @author K-Shan
 *
 */
public class BookTab {
	
	/**
	 * Processes the clicking of buttons in the book tab.
	 * @param player
	 * @param buttonId
	 */
	public static void processButtonClick(final Player player, int buttonId) {
		if(buttonId == 2)
			player.setPanelId(player.getPanelId() == 1 ? 0 : player.getPanelId() + 1);
		sendPanel(player);
		int panelId = player.getPanelId();
		switch(buttonId) {
			case 4:
				switch(panelId) {
					case 0:
						player.sm("Coming soon!");
						break;
					case 1:
						player.camPos(player.getX(), player.getY() - 3, 1000, 9);
						player.camLook(player.getX(), player.getY() + 2, 0, 9);
						player.faceLocation(new Location(player.getX(), player.getY() - 1, player.getZ()));
						break;
				}
				break;
			case 6:
				switch(panelId) {
					case 0:
						player.getDialogue().start("TrainingTeleports");
						break;
					case 1:
						player.camPos(player.getX() + 3, player.getY(), 1000, 7);
						player.camLook(player.getX(), player.getY(), 500, 7);
						break;
				}
				break;
			case 8:
				switch(panelId) {
					case 0:
						C_O_R_Data.handleBookTabAction(player);
						break;
					case 1:
						player.camPos(player.getX(), player.getY() + 3, 1000, 7);
						player.camLook(player.getX(), player.getY(), 500, 7);
						break;
				}
				break;
			case 10:
				switch(panelId) {
					case 0:
						boolean mod = player.getRights() == 1;
						boolean admin = player.getRights() == 2;
						boolean owner = player.isOwner();
						player.getDialogue().start(owner ? new OwnerPanel() : admin ? new AdminPanel() 
							: mod ? new ModPanel() : new UserPanel());
						break;
					case 1:
						player.camPos(player.getX() - 3, player.getY(), 1000, 7);
						player.camLook(player.getX(), player.getY(), 500, 7);
						break;
				}
				break;
			case 12:
				switch(panelId) {
					case 0:
						Magic.telePlayer(player, Constants.START_PLAYER_LOCATION, false);
						break;
					case 1:
						player.packets().sendResetCamera();
						break;
				}
				break;
			case 14:
				switch(panelId) {
					case 0:
						player.sm("Slaves and their information will be available soon.");
						break;
					case 1:
						// Blank button
						break;
				}
				break;
		}
	}
	
	/**
	 * Sends the book panel.
	 * @param player
	 */
	public static void sendPanel(Player player) {
		player.packets().sendString("<col=ffffff>" + Constants.SERVER_NAME + "", 506, 0);
		player.packets().sendString("<col=FFFFFF>Next Page..", 506, 2);
		switch(player.getPanelId()) {
			case 0:
				player.packets().sendString("Your Information", 506, 4);
				player.packets().sendString("Training Teleports", 506, 6);
				player.packets().sendString("Cavern of Remembrance", 506, 8);
				player.packets().sendString(player.isOwner() ? "Owner Panel" : player.getRights() == 1 ? 
					"Moderator Panel" : player.getRights() == 2 ? "Admin Panel" : "User Panel", 506, 10);
				player.packets().sendString("Teleport Home", 506, 12);
				player.packets().sendString("Slave Options", 506, 14);
				break;
			case 1:
				player.packets().sendString("<col=ffffff>View Character from..<br><col=ffffff>Next Page..", 506, 2);
				player.packets().sendString("North", 506, 4);
				player.packets().sendString("East", 506, 6);
				player.packets().sendString("South", 506, 8);
				player.packets().sendString("West", 506, 10);
				player.packets().sendString("Reset <br>Camera", 506, 12);
				player.packets().sendString("", 506, 14);
				break;
		}
	}

}
