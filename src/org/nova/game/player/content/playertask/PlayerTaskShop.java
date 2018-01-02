package org.nova.game.player.content.playertask;

import org.nova.game.player.Player;

public class PlayerTaskShop {
	
static int store = 825;
	
	public static void openStore(Player player) {
		player.closeInterfaces();
		player.interfaces().closeChatBoxInterface();
		player.interfaces().sendInterface(store);
		sendDetails(player);
	}
	/**
	 *  This will handle the shop buttons.
	 * @param player
	 * @param componentId
	 */
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 133:
			if (!player.getInventory().hasFreeSlots()) {
				player.sm("You don't have enough space for this purchase.");
			}
			else if (player.taskpoints >= 18) {
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
				player.taskpoints -= 18;
				TaskManager.removeTask(player);
				player.closeInterfaces();
			} else {
				player.sm("You don't have enough task points for this purchase. Current points: "+player.taskpoints+".");
			}
			break;
		case 136:
			if (!player.getInventory().hasFreeSlots()) {
				player.sm("You don't have enough space for this purchase.");
			}
			else if (player.taskpoints >= 50) {
				if (player.isBoostedtasks()) {
					player.sm("You have already purchased boosted task points.");
					return;
				}
				player.taskpoints -= 50;
				player.setBoostedtasks(true);
				player.closeInterfaces();
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful. Your new tasks are boosted.");
			} else {
				player.sm("You don't have enough task points for this purchase. Current points: "+player.taskpoints+".");
			}
			break;
		case 129:
			if (!player.getInventory().hasFreeSlots()) {
				player.sm("You don't have enough space for this purchase.");
			}
			else if (player.taskpoints >= 30) {
				player.taskpoints -= 30;
				player.getInventory().addItem(22340,1);
				player.closeInterfaces();
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
				
			} else {
				player.sm("You don't have enough task points for this purchase. Current points: "+player.taskpoints+".");
			}
			break;
			case 125:
			if (!player.getInventory().hasFreeSlots()) {
				player.sm("You don't have enough space for this purchase.");
			}
			else if (player.taskpoints >= 690) {
				player.taskpoints -= 690;
				player.getInventory().addItem(15492,1);
				player.closeInterfaces();
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
				
			} else {
				player.sm("You don't have enough task points for this purchase. Current points: "+player.taskpoints+".");
			}
			break;
			case 121:
			if (!player.getInventory().hasFreeSlots()) {
				player.sm("You don't have enough space for this purchase.");
			}
			else if (player.taskpoints >= 490) {
				player.taskpoints -= 490;
				player.getInventory().addItem(13263,1);
				player.closeInterfaces();
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
				
			} else {
				player.sm("You don't have enough task points for this purchase. Current points: "+player.taskpoints+".");
			}
			break;
		case 117:
			if (!player.getInventory().hasFreeSlots()) {
				player.sm("You don't have enough space for this purchase.");
			}
			else if (player.taskpoints >= 60) {
				player.taskpoints -= 60;
				player.getInventory().addItem(7409,1);
				player.closeInterfaces();
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
				
			} else {
				player.sm("You don't have enough task points for this purchase. Current points: "+player.taskpoints+".");
			}
			break;
		case 113:
			if (!player.getInventory().hasFreeSlots()) {
				player.sm("You don't have enough space for this purchase.");
			}
			else if (player.taskpoints >= 290) {
				player.taskpoints -= 290;
				player.getInventory().addItem(10887,1);
				player.closeInterfaces();
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
				
			} else {
				player.sm("You don't have enough task points for this purchase. Current points: "+player.taskpoints+".");
			}
			break;
		case 109:
			if (!player.getInventory().hasFreeSlots()) {
				player.sm("You don't have enough space for this purchase.");
			}
			else if (player.taskpoints >= 680) {
				player.taskpoints -= 680;
				player.getInventory().addItem(22369,1);
				player.closeInterfaces();
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
				
			} else {
				player.sm("You don't have enough task points for this purchase. Current points: "+player.taskpoints+".");
			}
			break;
			case 105:
			if (!player.getInventory().hasFreeSlots()) {
				player.sm("You don't have enough space for this purchase.");
			}
			else if (player.taskpoints >= 730) {
				player.taskpoints -= 730;
				player.getInventory().addItem(22365,1);
				player.closeInterfaces();
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
				
			} else {
				player.sm("You don't have enough task points for this purchase. Current points: "+player.taskpoints+".");
			}
			break;
		case 101:
			if (!player.getInventory().hasFreeSlots()) {
				player.sm("You don't have enough space for this purchase.");
			}
			else if (player.taskpoints >= 860) {
				player.taskpoints -= 860;
				player.getInventory().addItem(22361,1);
				player.closeInterfaces();
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
				
			} else {
				player.sm("You don't have enough task points for this purchase. Current points: "+player.taskpoints+".");
			}
			break;
		case 93: //Gold mining set
			if (!player.getInventory().hasFreeSlots()) {
				player.sm("You don't have enough space for this purchase.");
			}
			else if (player.taskpoints >= 460) {
				player.taskpoints -= 460;
				player.getInventory().addItem(20787,1);
				player.getInventory().addItem(20788,1);
				player.getInventory().addItem(20789,1);
				player.getInventory().addItem(20790,1);
				player.getInventory().addItem(20791,1);
				player.getInventory().addItem(20792,1);
				player.closeInterfaces();
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
				
			} else {
				player.sm("You don't have enough task points for this purchase. Current points: "+player.taskpoints+".");
			}
			break;
		case 97: 
			 if (player.taskpoints >= 350) {
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("Costumes");
			} else {
				player.sm("You must have atleast 350 task points to view costumes. Current points: "+player.taskpoints+".");
			}
			break;
		
		}
		
	}
	/*
 * Shop texts
 */
	private static void sendDetails(Player player) {
		player.packets().sendHideIComponent(825, 29, true);
		player.packets().sendIComponentText(store, 95,"Gold Mining Set");
		player.packets().sendIComponentText(store, 96,"460 Points");

		player.packets().sendIComponentText(store, 99,"Costumes");
		player.packets().sendIComponentText(store, 100,"350 Points");

		player.packets().sendIComponentText(store, 103,"Goliath Gloves");
		player.packets().sendIComponentText(store, 104,"830 Points");
		
		player.packets().sendIComponentText(store, 107,"Swift Gloves");
		player.packets().sendIComponentText(store, 108,"730 Points");
		
		player.packets().sendIComponentText(store, 111,"Spellcaster Gloves");
		player.packets().sendIComponentText(store, 112,"680 Points");
		
		player.packets().sendIComponentText(store, 115,"Barrelchest's Anchor");
		player.packets().sendIComponentText(store, 116,"290 Points");
		
		player.packets().sendIComponentText(store, 119,"Magic Secateurs");
		player.packets().sendIComponentText(store, 120,"60 Points");
		
		player.packets().sendIComponentText(store, 123,"Slayer Helmet");
		player.packets().sendIComponentText(store, 124,"490 Points");
		
		player.packets().sendIComponentText(store, 127,"Full Slayer Helmet");
		player.packets().sendIComponentText(store, 128,"670 Points");
		
		player.packets().sendIComponentText(store, 131,"XP Book");
		player.packets().sendIComponentText(store, 132,"30 Points");
		
		player.packets().sendIComponentText(store, 134,"Cancel Current Task");
		player.packets().sendIComponentText(store, 135,"18 Points");
	
		player.packets().sendIComponentText(store, 138,"Boosted Task Points");
		player.packets().sendIComponentText(store, 139,"50 Points");
		
		
		player.packets().sendIComponentText(store, 54,"My current task points:");
		player.packets().sendIComponentText(store, 55,""+player.taskpoints+"");
		player.packets().sendIComponentText(store,66,"Task System");
		player.packets().sendIComponentText(store, 67,"Complete tasks, gain points and purchase awesome goodies from the tasks store.");
		player.packets().sendIComponentText(store, 69,"Task: None");
		
	}

}
