package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.map.Location;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.cities.Keldagrim;
import org.nova.game.player.content.minigames.CastleWars;
import org.nova.game.player.content.playertask.PlayerTaskHandler;
import org.nova.game.player.content.playertask.PlayerTaskShop;
import org.nova.game.player.content.playertask.TaskManager;
import org.nova.game.player.content.quests.QuestNPCActions;
import org.nova.utility.ShopsHandler;

public class TicketDwarf extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { player.getDisplayName(), "Can you take me to the other side with ship?", "I couldn't find any other routes to the Keldagrim." },
				IS_PLAYER, player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Can you take me to the other side with ship?", "I couldn't find any other routes to the Keldagrim." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} 
		
		if (stage == 1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
							"I can but it will cost you money some money.",
									"You have to pay 1358 coin fee." }, IS_NPC, npcId, 9827);
			stage = 2;
		} 
		
		else if (stage == 2) {
			sendDialogue(SEND_5_OPTIONS, "What you want to do?", 
					"Alright I can pay to you the 1358 coin fee.", "I currently have no money, i'll be back later.", "Nevermind.", "", "");
			stage = 3;
		} 
		else if (stage == 3) {
			if(componentId == 1) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "Alright I can pay to you the 1358 coins -fee."},
						IS_PLAYER, player.getIndex(), 9827);
				stage = 21;
			}
			else if(componentId == 2) {
			player.interfaces().closeChatBoxInterface();
			}
			else if(componentId == 3) {
				player.interfaces().closeChatBoxInterface();
			}
		}
		
		else if (stage == 21) {
			if (!player.getInventory().containsItem(995, 1358)) {
				player.sendMessage("You don't have enough money to use dwarf's ship. It would cost you 1358 coins.");
				player.interfaces().closeChatBoxInterface();
				return;
			}player.interfaces().closeChatBoxInterface();
			Keldagrim.shipTeleport(player, 0, 0, new Location(2837,10141,0));
		}
		
		else if (stage == 39) {
			if (componentId == 1) 
			PlayerTaskHandler.searchforTask(player);
			else if (componentId == 2) 
			PlayerTaskShop.openStore(player);	
			else if (componentId == 3) 
				TaskManager.searhforCompletedTask(player);
				else if (componentId == 4) 
				player.interfaces().closeChatBoxInterface();
				else if (componentId == 5) 
					player.interfaces().closeChatBoxInterface();
		} 
		else if (stage == 99) {
			if (componentId == 1) {
			player.interfaces().closeChatBoxInterface();
			ShopsHandler.openShop(player, 102);
			} else if (componentId == 2)
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3484,
						3091, 0));
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
