package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.map.Location;
import org.nova.game.player.content.Enchanting;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.TeleportManager;
import org.nova.game.player.content.minigames.CastleWars;
import org.nova.game.player.content.quests.QuestNPCActions;
import org.nova.utility.ShopsHandler;

public class Rewardsman extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hey there, "+player.getDisplayName()+".",
								" Is there anything you want to buy or do?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Let's see..." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendDialogue(SEND_5_OPTIONS, "What you want to do?", 
					"Hoarfrost Hollow Rewards", "Hoarfrost Depths Rewards", "Ruins of Uzer Rewards", "Knight of Camelot Rewards", "Next page");
			stage = 2;
		} 
		else if (stage == 2) {
			if(componentId == 1) {
				if (player.isCompletedHoarfrost()) {
				stage = 3;
				sendDialogue(SEND_4_OPTIONS, "Hoarfrost Hollow Rewards",
						"View store.", "Teleport to Lunar Island", "Buy Korasi's Sword for 500,000.", "Nevermind");
			} else {
				player.interfaces().closeChatBoxInterface();
				player.sm("You have to complete Battle Quest: Hoarfrost Hollow to view this section.");
			}
			}
			else if(componentId == 2) {
				if (player.isCompletedHoarfrostDepths()) {
				stage = 5;
				sendDialogue(SEND_4_OPTIONS, "Hoarfrost Depths Rewards",
						"View store.", "Teleport to Jatizso.", "Teleport to Picatoris Fishing Colony.", "Nevermind");
			} else {
				player.interfaces().closeChatBoxInterface();
				player.sm("You have to complete Battle Quest: Hoarfrost Depths to view this section.");
			}
			}
			else if(componentId == 3) {
				if (player.isCompletedRuinsofUzer()) {
				stage = 99;
				sendDialogue(SEND_4_OPTIONS, "Ruins of Uzer Rewards",
						"View store.", "Enchant my Salve amulet.", "Purchase Fire cape for 500,000 coins.", "Nevermind");
			} else {
				player.interfaces().closeChatBoxInterface();
				player.sm("You have to complete Battle Quest: Ruins of Uzer to view this section.");
			}
			}
			else if (componentId == 4) {
				if (player.isCamelotKnight()) {
				stage = 65;
			sendDialogue(SEND_4_OPTIONS, "Knight of Camelot Rewards",
					"View store.", "Purchase Prayer book for 500,000 coins.", "Teleport to Relleka Rock crabs.", "Nevermind");
			} else {
				player.sm("You have to complete Battle Quest: Knight's of Camelot to view this section.");
			}
		}
			else if (componentId == 5) {
				sendDialogue(SEND_5_OPTIONS, "What you want to do?", 
						"Wanted!", "Nevermind.", "", "", "");
				stage = 81;
			}
		}
		
		else if (stage == 81) {
		switch (componentId) {
		case 1:
			if (!player.isCompletedWanted()) {
				player.interfaces().closeChatBoxInterface();
				player.sendMessage("You must have completed quest 'Wanted!' in order to view this section.");
				return;
			}
			sendDialogue(SEND_4_OPTIONS, "Wanted! Rewards",
					"View store.", "Purchase a Dwarven rock cake for 50,000 gold coins.", "Teleport to Keldagrim.", "Nevermind.");
				stage = 100;
			break;
		case 2:
			player.interfaces().closeChatBoxInterface();
			break;
		
	}
	}
		else if (stage == 100) {
			switch (componentId) {
			case 1:
				player.interfaces().closeChatBoxInterface();
				ShopsHandler.openShop(player, 51);
				break;
			case 2:
				if (!player.getInventory().containsItem(995, 50000)) {
				player.interfaces().closeChatBoxInterface();
				player.sendMessage("You don't have enough money to purchase this item.");
					return;
				}
				if (player.getInventory().containsItem(995,50000)) {
					player.getInventory().deleteItem(995,50000);
					if (player.getInventory().hasFreeSlots()) {
					player.getInventory().addItem(7509,1);
					player.interfaces().closeChatBoxInterface();
					player.sendMessage("You have succesfully purchased a dwarven rock cake for 50,000 gold.");
					} else {
						player.getBank().addItem(7509, 1, true);
						player.sendMessage("You did not have any space in your inventory, this item was added to your bank account.");
						player.interfaces().closeChatBoxInterface();
					}
				}
				break;
			case 3:
				player.interfaces().closeChatBoxInterface();
				TeleportManager.SpiritTeleport(player, 0, 0, new Location(2775,10162,0));
				break;
			case 4:
				player.interfaces().closeChatBoxInterface();
				break;
			}
}
		else if (stage == 65) {
			if (componentId == 1) {
			player.interfaces().closeChatBoxInterface();
			ShopsHandler.openShop(player, 103);
			} else if (componentId == 2)
				if (player.getInventory().containsItem(995, 500000)) {
					player.getInventory().addItem(10890, 1);
					player.getInventory().deleteItem(995, 500000);
					player.interfaces().closeChatBoxInterface();
					player.sm("You have bought a Prayer book.");
				} else {
					player.sm("You don't have enough coins.");
					player.interfaces().closeChatBoxInterface();
				}
			else if (componentId == 3)

				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3484,
						3091, 0));
			else if (componentId == 4) {
			player.interfaces().closeChatBoxInterface();
			}
		}
		else if (stage == 99) {
			if (componentId == 1) {
			player.interfaces().closeChatBoxInterface();
			ShopsHandler.openShop(player, 102);
			} else if (componentId == 2)
				Enchanting.enchantSalveAmulet(player);
			else if (componentId == 3)
				if (player.getInventory().containsItem(995, 500000)) {
					player.getInventory().addItem(6570, 1);
					player.interfaces().closeChatBoxInterface();
					player.sm("You have bought a Fire cape.");
				} else {
					player.sm("You don't have enough coins.");
					player.interfaces().closeChatBoxInterface();
				}
			else if (componentId == 4) {
			player.interfaces().closeChatBoxInterface();
			}
		}
		else if (stage == 5) {
			if (componentId == 1) {
			player.interfaces().closeChatBoxInterface();
			ShopsHandler.openShop(player, 101);
			} else if (componentId == 2)
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2412,
						3810, 0));
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2331,
						3810, 0));
			else if (componentId == 4) {
			player.interfaces().closeChatBoxInterface();
			}
		}
		else if (stage == 3) {
			if (componentId == 1) {
				QuestNPCActions.openStore(player);
			} else if (componentId == 2)
			QuestNPCActions.TeleportLunar(player);
			else if (componentId == 3)
				QuestNPCActions.buyKorasi(player);
			else if (componentId == 4) {
			player.interfaces().closeChatBoxInterface();
			}
		}else if (stage == 3) {
			if (componentId == 1) {
				QuestNPCActions.openStore(player);
			} else if (componentId == 2)
			QuestNPCActions.TeleportLunar(player);
			else if (componentId == 3)
				QuestNPCActions.buyKorasi(player);
			else if (componentId == 4) {
			player.interfaces().closeChatBoxInterface();
			}
		} else if (stage == 4) {
			if (componentId == 1)
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2539,
						4712, 0));
			else if (componentId == 2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3240,
						3611, 0));
				player.getControllerManager().startController("Wilderness");
			} else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2399,
						5177, 0));
			else if (componentId == 4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2984,
						3596, 0));
				player.getControllerManager().startController("Wilderness");
			} else if (componentId == 5) {
				stage = 5;
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Easts (PvP)", "Brimhaven", "Corporeal Beast", "Feldip hills",
						"More Options");
			}
		} else if (stage == 5) {
			if (componentId == 1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3360,
						3658, 0));
				player.getControllerManager().startController("Wilderness");
			} else if (componentId == 2)
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2709,
						9464, 0));
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2966,
						4383, 0));
			else if (componentId == 4) {
				player.packets().sendMessage("Disabled.");
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2570,
						2916, 0));
			} else if (componentId == 5) {
				stage = 6;
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Rock Crabs", "King Black Dragon", "Dragons", "Abyss",
						"More Options");
			}
		} else if (stage == 6) {
			if (componentId == 1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2708,
						3709, 0));
			} else if (componentId == 2)
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2285,
						4694, 0));
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2892,
						9784, 0));
			else if (componentId == 4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3040,
						4834, 0));
			} else if (componentId == 5) {
				stage = 7;
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Mine Essence", "Pure Training", "Dominion Tower", "Barrows",
						"More Options");
			}
		} else if (stage == 7) {
			if (componentId == 1)
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Location(
						2911, 4832, 0), new int[0]);
			else if (componentId == 2)
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Location(
						3561, 9948, 0), new int[0]);
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Location(
						3366, 3083, 0), new int[0]);
			else if (componentId == 4)
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Location(
						3565, 3289, 0), new int[0]);
			else if (componentId == 5) {
				stage = 8;
				sendDialogue((short) 238, new String[] {
						"Where would you like to go?", "Zamorak",
						"Armadyl", "Castle Wars", "",
						"More Options" });
			}
		} else if (stage == 8) {
			if (componentId == 1)
				teleportPlayer(2925, 5330, 2);
			else if (componentId == 2)
				teleportPlayer(2838, 5297, 2);
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, CastleWars.LOBBY);
			else if (componentId == 5) {
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Nex", "Bandos", "Saradomin", "Tormented Demons",
						"Beginning Options");
				stage = 2;
			}
		}
	}

	private void teleportPlayer(int x, int y, int z) {
		player.setLocation(new Location(x, y, z));
		player.stopAll();
		player.getControllerManager().startController("GodWars");
	}

	@Override
	public void finish() {

	}
}
