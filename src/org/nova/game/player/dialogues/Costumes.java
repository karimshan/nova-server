package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;
import org.nova.game.player.content.playertask.PlayerTaskShop;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class Costumes extends MatrixDialogue {

	public Costumes() {
	}

	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Task Shop Costumes", "Witchdoctor's Set","Void Knight's Set", "Skeletal Outfit", "Lederhosen Outift", "Nevermind, return back to rewards.");
		stage = 2;
		}

	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == 1) {	
				if (player.getInventory().hasFreeSlots()) {
			player.taskpoints -= 350;
			player.getInventory().addItem(20044, 1);
			player.getInventory().addItem(20045, 1);
			player.getInventory().addItem(20046, 1);
			player.interfaces().closeChatBoxInterface();
			player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
			} else {
				player.interfaces().closeChatBoxInterface();
				player.sm("You don't have enough space in your inventory.");
			}
		}
			else if (componentId == 2) {
				if (player.getInventory().hasFreeSlots()) {
				player.taskpoints -= 350;
				player.interfaces().closeChatBoxInterface();
				player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
				player.getInventory().addItem(8839, 1);
				player.getInventory().addItem(8840, 1);
				player.getInventory().addItem(8841, 1);
				player.getInventory().addItem(8842, 1);
				player.getInventory().addItem(11665, 1);
				player.getInventory().addItem(11663, 1);
				player.getInventory().addItem(11664, 1);
				} else {
					player.interfaces().closeChatBoxInterface();
					player.sm("You don't have enough space in your inventory.");
				}
			}
			else if (componentId == 3) {
				if (player.getInventory().hasFreeSlots()) {
					player.taskpoints -= 350;
					player.interfaces().closeChatBoxInterface();
					player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
					player.getInventory().addItem(9922, 1);
					player.getInventory().addItem(9923, 1);
					player.getInventory().addItem(9924, 1);
					player.getInventory().addItem(9925, 1);
					} else {
						player.interfaces().closeChatBoxInterface();
						player.sm("You don't have enough space in your inventory.");
					}
			}
			else if (componentId == 4) {
				if (player.getInventory().hasFreeSlots()) {
					player.taskpoints -= 350;
					player.interfaces().closeChatBoxInterface();
					player.sm("<col=ff0033>Task Shop: Your current purchase was succesful.");
					player.getInventory().addItem(6180, 1);
					player.getInventory().addItem(6181, 1);
					player.getInventory().addItem(6182, 1);
					} else {
						player.interfaces().closeChatBoxInterface();
						player.sm("You don't have enough space in your inventory.");
					}
			}
			else if (componentId == 5) {
			player.closeInterfaces();
			player.interfaces().closeChatBoxInterface();
			PlayerTaskShop.openStore(player);
			}
		}
			else if (stage == 39) {
				if (componentId == 1) 
					TeleportManager.SpiritTeleport(player, 0, 0, new Location(2710,
							9485, 0));		
			else if (componentId == 2) 
					TeleportManager.SpiritTeleport
					(player, 0, 0, new Location(1737,
							5312, 0));
					
			else if (componentId == 3) 
					TeleportManager.SpiritTeleport(player, 0, 0, new Location(3005,
							9548, 0)); 
			
				else if (componentId == 4) 
					TeleportManager.SpiritTeleport(player, 0, 0, new Location(2548,
							10151, 0));
				
				else if (componentId == 5) {
				player.interfaces().closeChatBoxInterface();
				}
		} 
		}

	public void finish() {
	}

	private int npcId;
}
