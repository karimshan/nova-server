package org.nova.game.player.content.cities.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 * Ship Charter - 28.7.2013
 */
public class ShipCharter {
	/**
	 * Constructor, player
	 * @param player
	 */
	public ShipCharter(Player player) {
		this.player = player;
	}	
	/**
	 * The travel price.
	 */
	private int travelFee;
	
	/**
	 * Player instance
	 */
	private transient Player player;
	/**
	 * Location's name.
	 */
	private String locationName;
	
	/**
	 * Opens the ship charter.
	 */
	public void openShipCharter() {
		refresh();
		closeAll();
		player.interfaces().sendInterface(95);
	}
	public void closeAll() {
		player.interfaces().closeChatBoxInterface();
		player.closeInterfaces();
	}
	/**
	 * Unfinished, it would put you inside ship and this would move the player.
	 * @param player
	 * @param id
	 * @return
	 */
	public boolean handleGangplanks(Player player, int id) {
		switch (id) {
		case 17395: //Catherby
			if (player.getX() == 2792 && player.getY() == 3417 && player.getZ() == 1) {
				player.teleportPlayer(2792, 3413, 0);
			}
		return true;
		case 17394: //Catherby
			if (player.getX() == 2792 && player.getY() == 3414 && player.getZ() == 0) {
				player.teleportPlayer(2792, 3417, 1);
			}
		return true;
		}
		return false;
	}
	/**
	 * The actual teleport.s
	 * 
	 */
	public void charterTeleport(final Player player, int i, int j, final Location tile) {
		if (!player.getControllerManager().processItemTeleport(tile))
			return;
			player.addStopDelay(6);
			WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
			player.interfaces().closeChatBoxInterface();
			Location teleTile = tile;
			teleTile = new Location(tile, 2);
			teleTile = tile;						
				if (loop == 0) {
				player.interfaces().sendInterface(120);
				} else if (loop == 2) {
				player.setLocation(teleTile);
				} else if (loop == 3) {
				player.closeInterfaces();
				stop();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You have succesfully arrived to: "+getLocationName()+".");
				}
				loop++;
				}
			}, 0, 1);
		}
	/**
	 *	Gets the given location and teleports.
	 */
	public void handleTeleportation() {
		player.getInventory().deleteItem(995, getTravelFee());
		player.interfaces().closeChatBoxInterface();
		player.closeInterfaces();
		if (locationName.equals("Karamja")) {
			charterTeleport(player, 0, 0, new Location(2954, 3158, 0));
		}
		else if (locationName.equals("Catherby")) {
			charterTeleport(player, 0, 0, new Location(2792, 3414, 0));
		}
		else if (locationName.equals("Oo'glog")) {
			charterTeleport(player, 0, 0, new Location(2623, 2857, 0));
		}
		else if (locationName.equals("Mos Le'Harmless")) {
			charterTeleport(player, 0, 0, new Location(3671, 2931, 0));
		}
		else if (locationName.equals("Port Phasmatys")) {
			charterTeleport(player, 0, 0, new Location(3702, 3503, 0));
		}
		else if (locationName.equals("Port Tyras")) {
			charterTeleport(player, 0, 0, new Location(2142, 3122, 0));
		}
		else if (locationName.equals("Brimhaven")) {
			charterTeleport(player, 0, 0, new Location(2760, 3238, 0));
		}
		else if (locationName.equals("Shipyard")) {
			charterTeleport(player, 0, 0, new Location(3001, 3032, 0));
		}
		else if (locationName.equals("Port Khazard")) {
			charterTeleport(player, 0, 0, new Location(2674, 3144, 0));
		}
		else if (locationName.equals("Port Sarim")) {
			charterTeleport(player, 0, 0, new Location(3038, 3192, 0));
		}
	}
	/**
	 * Button handling @interface
	 * @return
	 */
	public void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 25:
			setTravelFee(850);
			if (player.getInventory().containsItem(995, getTravelFee())) {
				putDestination("Catherby");
			} else {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You must have "+getTravelFee()+" coins to travel in this location.");
			}
			break;
		case 29:
			setTravelFee(4811);
			if (player.getInventory().containsItem(995, getTravelFee())) {
				putDestination("Port Khazard");
			} else {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You must have "+getTravelFee()+" coins to travel in this location.");
			}
			break;
		case 32:
			setTravelFee(1128);
			if (player.getInventory().containsItem(995, getTravelFee())) {
				putDestination("Crandor");
			} else {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You must have "+getTravelFee()+" coins to travel in this location.");
			}
			break;
		case 31:
			setTravelFee(9511);
			if (player.getInventory().containsItem(995, getTravelFee())) {
				putDestination("Mos Le'Harmless");
			} else {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You must have "+getTravelFee()+" coins to travel in this location.");
			}
			break;
		case 28:
			setTravelFee(5444);
			if (player.getInventory().containsItem(995, getTravelFee())) {
				putDestination("Brimhaven");
			} else {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You must have "+getTravelFee()+" coins to travel in this location.");
			}
			break;
		case 23:
			setTravelFee(6741);
			if (player.getInventory().containsItem(995, getTravelFee())) {
				putDestination("Port Tyras");
			} else {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You must have "+getTravelFee()+" coins to travel in this location.");
			}
			break;
		case 33:
			setTravelFee(3211);
			if (player.getInventory().containsItem(995, getTravelFee())) {
				putDestination("Oo'glog");
			} else {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You must have "+getTravelFee()+" coins to travel in to this location.");
			}
			break;
		case 26:
			setTravelFee(8472);
			if (player.getInventory().containsItem(995, getTravelFee())) {
				putDestination("Shipyard");
			} else {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You must have "+getTravelFee()+" coins to travel in to this location.");
			}
			break;
		case 17:
			setTravelFee(2908);
			if (player.getInventory().containsItem(995, getTravelFee())) {
				putDestination("Karamja");
			} else {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You must have "+getTravelFee()+" coins to travel in to this location.");
			}
			break;
		case 24:
			setTravelFee(7008);
			if (player.getInventory().containsItem(995, getTravelFee())) {
				putDestination("Port Phasmatys");
			} else {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You must have "+getTravelFee()+" coins to travel in to this location.");
			}
			break;
		case 30:
			setTravelFee(2085);
			if (player.getInventory().containsItem(995, getTravelFee())) {
				putDestination("Port Sarim");
			} else {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You must have "+getTravelFee()+" coins to travel in to this location.");
			}
			break;
		}
	}
	/**
	 * Refreshes ship charter.
	 */
	public void refresh() {
		setTravelFee(0);
		resetDestination(null);
	}
	/**
	 * Here we get the location's name.
	 * @return
	 */
	public String getLocationName() {
		return locationName;
	}
	/**
	 * Simply sets a destination to travel.
	 * @param locationName
	 */
	public void putDestination(String locationName) {
		this.locationName = locationName;
		handleTeleportation();
	}
	
	public void resetDestination(String locationName) {
		this.locationName = locationName;
	}
	
	public int getTravelFee() {
		return travelFee;
	}
	
	public void setTravelFee(int travelFee) {
		this.travelFee = travelFee;
	}
	
}
