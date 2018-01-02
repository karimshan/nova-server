package org.nova.game.player.content;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class Warnings {

/*
 * 
::interface 327 //Warning! Cold area
::interface 382 //Warning! For Wilderness
::interface 560 //Warning to low lvl players about spiders and goblins
::interface 561 //Kalphite Queen Warning
::interface 562 //Very dark area Warning
::interface 563 //Entering Building mode warning
::interface 564 //Warning for archers on top of tower
::interface 565 //High temperatures, robbers, etc warnings
::interface 566 //Do I need to collect any dropped items? Warning
::interface 567 //Very dark - you sure you wanna go in w/o light source? Warning
::interface 568 //Very dark - don't extinguish light source Warning
::interface 569 //Very Dark - sure you wanna go in w/o light source? Warning
::interface 570 //Very dark cave warning
::interface 571 //Tunnel is very dark warning
::interface 572 //Light source goes out warning
::interface 573 //Run past but dangerous area follows warning
::interface 574 //Very dangerous area warning
::interface 575 //Portals with question marks map
::interface 576 //This way leads one way, to Wildy warning
::interface 577 //Tunnel very dark warning
::interface 578 //Fairy Ring code leads to dark area warning
::interface 579 //Sure you want to climb down warning
::interface 580 //Mort Myre warning
::interface 581 //Death Plateau warning
::interface 582 //Shows grass around Edge bank
::interface 583 //Reset warning messages
::interface 600 //This way Leads into Wilderness Warning
::interface 627 //Warning! Duelling is an honorable pasttime...
::interface 650 //The Beasts's Warning
::interface 676 //Dangers and death is more than possibility warning
::interface 677 //Dangers and death is more than possibility warning
::interface 678 //Dangers and death is more than possibility warning
::interface 793 //Free-For-All (Safe) Warning
::interface 798 //WARNING you are carrying a lot of items
inter 892 (warning message)
 */
	public Warnings() {
		setPlayer(player);
	}
	//What's the current warning to give?
	private String currentWarning;

	private Player player;
	
	public void detectCurrentWarning() {
	switch (currentWarning.toString()) {
	case "DARK_FAIRY_RING":
		player.interfaces().sendInterface(578);
		break;
	}
}
	
	public boolean handleButtons(Player player, int interfaceId, int componentId) {
		switch (interfaceId) {
		case 578:
			switch (componentId) {
			case 17:
				player.sendMessage("To remove warnings you can talk to the Doomsayer located in Lumbridge.");
				player.interfaces().closeChatBoxInterface();
				return true;
			case 18:
				player.interfaces().closeChatBoxInterface();
				return false;
			}
			break;
	
		
		}
		return false;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getCurrentWarning() {
		return currentWarning;
	}


	public void setCurrentWarning(String currentWarning) {
		this.currentWarning = currentWarning;
		detectCurrentWarning();
	}
	
	
}
