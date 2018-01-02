package org.nova.network.decoders.packets.handlers;

import java.util.HashMap;
import java.util.TimerTask;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.npc.familiar.Familiar;
import org.nova.game.npc.familiar.Familiar.SpecialAttack;
import org.nova.game.player.CombatDefinitions;
import org.nova.game.player.Equipment;
import org.nova.game.player.Inventory;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Rest;
import org.nova.game.player.actions.Smithing.ForgingInterface;
import org.nova.game.player.actions.Summoning;
import org.nova.game.player.actions.summoning.SummonTrain;
import org.nova.game.player.actions.summoning.SummoningScroll;
import org.nova.game.player.content.ClanChatManager;
import org.nova.game.player.content.FairyRing;
import org.nova.game.player.content.GiftBox;
import org.nova.game.player.content.ItemConstants;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.MagicAbility;
import org.nova.game.player.content.PlayerLook;
import org.nova.game.player.content.Runecrafting;
import org.nova.game.player.content.Shop;
import org.nova.game.player.content.SkillCapeCustomizer;
import org.nova.game.player.content.SkillsDialogue;
import org.nova.game.player.content.SpellSelection;
import org.nova.game.player.content.Warnings;
import org.nova.game.player.content.cities.ApeToll;
import org.nova.game.player.content.cities.content.artisan.ArtisanWorkshop;
import org.nova.game.player.content.dungeoneering.DungeonPartyManager;
import org.nova.game.player.content.exchange.GrandExchangeConstants;
import org.nova.game.player.content.handlers.SpiritTree;
import org.nova.game.player.content.handlers.XPLamp;
import org.nova.game.player.content.itemactions.BoltEnchanting;
import org.nova.game.player.content.itemactions.DungeoneeringShop;
import org.nova.game.player.content.playertask.PlayerTaskShop;
import org.nova.game.player.content.playertask.TaskManager;
import org.nova.game.player.content.quests.QuestList;
import org.nova.game.player.content.tutorial.Start;
import org.nova.game.player.dialogues.LevelUp;
import org.nova.game.player.dialogues.Transportation;
import org.nova.kshan.content.interfaces.BookTab;
import org.nova.kshan.content.interfaces.ItemSearch;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.network.decoders.packets.PacketDecoder;
import org.nova.network.decoders.packets.handlers.items.InventoryActionHandler;
import org.nova.network.stream.InputStream;
import org.nova.utility.ShopsHandler;
import org.nova.utility.loading.Logger;
import org.nova.utility.loading.items.ItemExamines;
import org.nova.utility.misc.Misc;

public class ButtonPackets {
	
	public long buttonDelay;
	
	public static void handleButtons(final Player player, InputStream stream,
			int packetId) {
		int interfaceHash = stream.readIntV2();
		int interfaceId = interfaceHash >> 16;
		Warnings warnings = new Warnings();
		BoltEnchanting enchanting = new BoltEnchanting();
		if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
			return;
		}
		
		if (player.isDead()
				|| !player.interfaces().containsInterface(interfaceId))
			return;
		final int buttonId = interfaceHash - (interfaceId << 16);
		if (buttonId != 65535
				&& Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= buttonId) {
	
			return;
		}
		final int slotId2 = stream.readUnsignedShortLE128();
		final int slotId = stream.readUnsignedShort();
		Item item = player.getInventory().getItem(slotId);
		if(player.getRights() > 1)
			System.out.println("Interface Id: " + interfaceId + " Button Id: " + buttonId + " Packet: " + packetId +" Slot: "+slotId);
		if(player.getButtonStrokes().getButtonStroke() != null 
			&& !player.getButtonStrokes().getButtonStroke().press(interfaceId, buttonId, slotId, packetId))
			return;
		if (!player.getControllerManager().processButtonClick(interfaceId,
				buttonId, slotId, packetId))
			return;
		if(!player.getRandomEvent().canClickButtons(interfaceId, buttonId, slotId, packetId))
			return;
		if(player.tasks().interfaceState() != null)
			player.tasks().buttons(interfaceId, buttonId);
	
		
		
		switch(interfaceId) {
		
		case 679: // Inventory sending examine
			if(buttonId == 0) {
				if(packetId == 25)
					player.getInventory().sendExamine(slotId);
				else if(packetId == 18) { // Button 3 or 4 idk
					switch(item.getId()) {
						case 22440:
							player.sm("Your location crystal has "+player.getLC().getCharges()+" charge(s) left.");
							if(player.getLC().getSize() == player.getLC().getMaxLocationCount())
								player.sm("Furthermore, you have reached the limit on the number of locations you can add.");
							else
								player.sm("Furthermore, you may add up to "+(
									player.getLC().getMaxLocationCount() - 
										player.getLC().getNames().size())+" more location(s) to your crystal.");
							break;
					}
				}
			}
			break;
			
		case 1150: // Item search
			ItemSearch.searchItem(player, buttonId, slotId, packetId);
			break;
		
		case 261:
			if(buttonId == 8) { // Opens up house options and shows room count
				player.interfaces().sendTab(player.interfaces().isFullScreen() ? 102 : 216, 398);
				player.packets().sendGlobalConfig(944, player.getHouse().getSavedRooms().size());
				player.getHouse().refreshConfigs();
			}
			break;
		case 398:
			switch(buttonId) {
				case 19:
					player.interfaces().sendSettings();
					break;
				case 15: // Toggle build mode on
					if(player.getHouse().inBuildingMode()) {
						player.sm("Building mode is already on.");
						break;
					}
					player.sm("Building mode is now on.");
					player.getHouse().setInBuildingMode(true);
					player.getHouse().refreshConfigs();
					break;
				case 1: // Toggle build mode off
					if(!player.getHouse().inBuildingMode()) {
						player.sm("Building mode is already off.");
						break;
					}
					player.sm("Building mode is now off.");
					player.getHouse().setInBuildingMode(false);
					player.getHouse().refreshConfigs();
					break;
				case 26: // Arrive at house
					player.getHouse().setArrivingAtPortal(false);
					player.getHouse().refreshConfigs();
					break;
				case 25: // Arrive at portal
					player.getHouse().setArrivingAtPortal(true);
					player.getHouse().refreshConfigs();
					break;
			}
			break;
		
		case 496:
			switch(buttonId) {
				case 4:
					player.getDialogue().start(new Dialogue() {
						public void start() {
							sendOptions("Select an option", "Melee weapons", "Melee helmets", "Armour");
						}
						public void process(int i, int b) {
							end();
							if(b == 2)
								ShopsHandler.openShop(player, 2);
							else if(b == 3)
								ShopsHandler.openShop(player, 6);
							else 
								ShopsHandler.openShop(player, 15);
						}
						public void finish() { }
					});
					break;
				case 5:
					ShopsHandler.openShop(player, 10);
					break;
				case 6:
					ShopsHandler.openShop(player, 14);
					break;
				case 7:
					player.getDialogue().start(new Dialogue() {
						public void start() {
							sendOptions("Select an option", 
								"Fishing and food", "Woodcutting and mining", "Construction", "Firemaking and Fletching", "Next..");
							stage = 1;
						}
						public void process(int i, int b) {
							if(stage == 1) {
								if(b == 1) {
									end();
									ShopsHandler.openShop(player, 22);
								} else if(b == 2) {
									end();
									ShopsHandler.openShop(player, 21);
								} else if(b == 3) {
									end();
									ShopsHandler.openShop(player, 36);
								} else if(b == 4) {
									end();
									ShopsHandler.openShop(player, 62);
								} else {
									sendOptions("Select an option:", 
										"Crafting", "Hunter", "Herblore", "Runecrafting", "Back..");
									stage = 2;
								}
							} else if(stage == 2) {
								if(b == 1) {
									end();
									ShopsHandler.openShop(player, 19);
								} else if(b == 2) {
									end();
									ShopsHandler.openShop(player, 26);
								} else if(b == 3) {
									end();
									ShopsHandler.openShop(player, 20);
								} else if(b == 4) {
									end();
									ShopsHandler.openShop(player, 9);
								} else {
									start();
									stage = 1;
								}
							}
						}
						public void finish() { }
					});
					break;
				case 8:
					ShopsHandler.openShop(player, 61);
					break;
				case 9:
					ShopsHandler.openShop(player, 31);
					break;
				case 10:
					ShopsHandler.openShop(player, 4);
					break;
			}
			break;
	
		
			
		case 751:
			if (buttonId == 25) {
				if (packetId == 64)
					player.getFriendsIgnores().setPrivateStatus(0);
				else if (packetId == 4)
					player.getFriendsIgnores().setPrivateStatus(1);
				else if (packetId == 52)
					player.getFriendsIgnores().setPrivateStatus(2);
				else if (buttonId == 13)
					player.sm("Add this");
			}
			else if (buttonId == 13) 
				player.sm("Add this");
			else if (buttonId == 25)
				if (packetId == 64)
					player.setFilterGame(false);
				else if (packetId == 52)
					player.setFilterGame(true);
			break;
		
		case 506: // Nova Panel
			BookTab.processButtonClick(player, buttonId);
			break;
		
		
	}
		
		
		
		
		
		
		
		
		
		 if (interfaceId == 309) 
				PlayerLook.handleHairdresserSalonButtons(player, buttonId, slotId);
			else if (interfaceId == 729) 
				PlayerLook.handleThessaliasMakeOverButtons(player, buttonId, slotId);
			else if (interfaceId == 900)
				PlayerLook.handleMageMakeOverButtons(player, buttonId);
			if (player.isInTutorial())
				return;
			
		if (interfaceId == 548 || interfaceId == 746) {
			if ((interfaceId == 548 && buttonId == 180)
					|| (interfaceId == 746 && buttonId == 182)) {
				if (player.interfaces().containsScreenInter()
						|| player.interfaces()
								.containsInventoryInter()) {
					player.packets()
							.sendMessage(
									"Please finish what you're doing before opening the world map.");
					return;
				} else
				if (interfaceId == 746 && buttonId == 175) {
					if (player.getControllerManager().getControler() != null) {
						player.sendMessage("You cannot contact the Advisor over here.");
						return;
					}
					player.getMatrixDialogues().startDialogue("Advisor", 6307);
				}
				// world map open
				player.setNextAnimation(new Animation(840));
				player.packets().sendWindowsPane(755, 0);
				int posHash = player.getX() << 14 | player.getY();
				player.packets().sendGlobalConfig(622, posHash); // map open
				// center
				// pos
				player.packets().sendGlobalConfig(674, posHash); // player
				
				player.setNextAnimation(new Animation(840));
				// position
			}
				if (MagicAbility.handleMagicAbility(player, buttonId))

				return;
				else if (interfaceId == 619)
					System.out.println(""+buttonId+"");
			else if ((interfaceId == 548)
					|| (interfaceId == 746 && buttonId == 229)) {
				if (packetId == PacketDecoder.ACTION_BUTTON7_PACKET)
					player.getSkills().resetXpCounter();
			}
			
			if (buttonId == 184) {
				if (player.getControllerManager().getControler() != null) {
					player.sendMessage("You cannot contact the Advisor over here.");
					return;
				}
				player.getMatrixDialogues().startDialogue("Advisor", 6307);
			}
		}
		switch (interfaceId) {
		case 1028:
			PlayerLook.handleCharacterCustomizingButtons(player, buttonId, slotId);
			break;
		case 622:
			break;
		case 940:
		DungeoneeringShop.handleButtons(player, buttonId, slotId, packetId);
			break;
		
		case 429:
			MagicAbility.handleInterfaceInteractions(player, buttonId);
			break;
		case 432:
			enchanting.handleEnchantingInterface(player, buttonId);
			break;
		case 1143:
			player.getLoyaltyprogramme().handleButtons(player, buttonId, slotId);
			break;
		}
	
	 if (interfaceId == 1127) {
			SpellSelection.handleButtons(player, buttonId);
		}
		else if (interfaceId == 827) {
			switch (buttonId) {
			case 6:
			case 7:
			case 20:
				player.closeInterfaces();
				player.interfaces().closeOverlay(true);
				player.interfaces().closeScreenInterface();
				player.interfaces().sendOverlay(812, false);
				break;
			}
		}
	 if (interfaceId == 523) {
		 if (buttonId == 105)
			 player.packets().closeInterface(player.interfaces().isFullScreen() ? 97 : 211);
		 	player.interfaces().sendMagicBook();
	 }
		else if (interfaceId == 164) {
			if (buttonId == 24) {	
				if (player.slayerPoints() < 400) {
					player.packets().sendMessage("You don't have enough slayer points to buy Slayer XP.");
			} else {
					player.packets().sendMessage("You spend 400 slayer points on Slayer XP.");
					player.getSkills().addXp(Skills.SLAYER, 150000);
					player.setSlayerPoints(player.slayerPoints() - 400);
				}
			}
			if (buttonId == 26) {
				if (player.slayerPoints() < 75) {
					player.packets().sendMessage("You don't have enough slayer points to buy a Ring Of Slaying (8).");
			} else {
					player.packets().sendMessage("You spend 75 slayer points on a Ring Of Slaying (8).");
					player.getInventory().addItem(13281, 1);
					player.setSlayerPoints(player.slayerPoints() - 75);
				}
			}
			if (buttonId == 28) {
				if (player.slayerPoints() < 35) {
					player.packets().sendMessage("You don't have enough slayer points to buy 250 casts of Slayer Dart.");
			} else {
					player.packets().sendMessage("You spend 35 slayer points on 250 casts of Slayer Dart.");
					player.getInventory().addItem(560, 250);
					player.getInventory().addItem(558, 1000);
					player.setSlayerPoints(player.slayerPoints() - 35);
				}
			}
			if (buttonId == 37) {
				if (player.slayerPoints() < 35) {
					player.packets().sendMessage("You don't have enough slayer points to buy 250 Broad-Tipped Bolts.");
			} else {
					player.packets().sendMessage("You spend 35 slayer points on 250 Broad-Tipped Bolts.");
					player.getInventory().addItem(13280, 250);
					player.setSlayerPoints(player.slayerPoints() - 35);
				}
			}
			if (buttonId == 39) {
				if (player.slayerPoints() < 35) {
					player.packets().sendMessage("You don't have enough slayer points to buy 250 Broad Arrows.");
			} else {
					player.packets().sendMessage("You spend 35 slayer points on 250 Broad Arrows.");
					player.getInventory().addItem(4160, 250);
					player.setSlayerPoints(player.slayerPoints() - 35);
				}
			}
			if (buttonId == 16) {
				player.interfaces().sendInterface(163);
				player.packets().sendIComponentText(163, 18, "" + player.slayerPoints() + "");
			}
			if (buttonId == 17) {
				player.interfaces().sendInterface(161);
				player.packets().sendIComponentText(161, 19, "" + player.slayerPoints() + "");
				player.packets().sendIComponentText(161, 23, "Cancel your current mission");
			}
		
		}	// Clan Stuff
	else if (interfaceId == 1110) { // TODO
		ClanChatManager
				.handleButtons(player, buttonId, packetId, stream);
	}
else if (interfaceId == 163) {
			if (buttonId == 15) {
				player.interfaces().sendInterface(164);
				player.packets().sendIComponentText(164, 20, "" + player.slayerPoints() + "");
			}
			if (buttonId == 14) {
				player.interfaces().sendInterface(161);
				player.packets().sendIComponentText(161, 19, "" + player.slayerPoints() + "");
				player.packets().sendIComponentText(161, 23, "Cancel your current mission");
			}
		} else if (interfaceId == 161) {
			if (buttonId == 15) {
				player.interfaces().sendInterface(164);
				player.packets().sendIComponentText(164, 20, "" + player.slayerPoints() + "");
			}
			if (buttonId == 14) {
				player.interfaces().sendInterface(163);
				player.packets().sendIComponentText(163, 18, "" + player.slayerPoints() + "");
			}
			if (buttonId == 23) {
				if (player.slayerPoints() < 30) {
					player.packets().sendMessage("You need 30 slayer points to cancel your current slayer mission.");
				} else {
					player.packets().sendMessage("You ask kuradal to remove your current slayer task for 30 points.");
					player.setSlayerPoints(player.slayerPoints() - 30);
				}
			}
		} else if (interfaceId == 182) {
			if (player.interfaces().containsInventoryInter())
				return;
			if (buttonId == 6 || buttonId == 13)
				if (!player.hasFinished()) {
					player.loggedOutUsingButton = true;
					player.logout();
				}
			if (interfaceId == 947) {
				if (buttonId == 766) {
					player.closeInterfaces();
					player.interfaces().sendInterface(938);
				}
			}
		} else if (interfaceId == 578) { // Fairy Ring Warning
	        if (buttonId == 15) {
	        FairyRing.warningInterface(player);
	        }
	        else if (buttonId == 16) {
	        	player.closeInterfaces();
	        }
		}  else if (interfaceId == 1072) {
			   ArtisanWorkshop.handleButtons(player, buttonId);
			}
		else if (interfaceId == 897) {
			Start.handleButtons(player, buttonId);
		}
		else if (interfaceId == 734) { //Fairy rings                                
	        if (buttonId == 23) { //1st Plus
                if (player.firstColumn == 2) {
                    player.firstColumn = 4;
            } else if (player.firstColumn == 3) {
                player.firstColumn = 3;
        }    else if (player.firstColumn >= 4) {
            player.firstColumn = 1;
    }
        else{
            player.firstColumn++;
        }
	        } else if (buttonId == 24) { //1st Subtract
	                if (player.firstColumn <= 1) {
	                        player.firstColumn = 4;
	                } else {
	                        player.firstColumn--;
	                }
	        } else if (buttonId == 25) { //2nd Plus
                if (player.secondColumn == 2) {
                    player.secondColumn = 4;
            } else if (player.secondColumn == 3) {
                player.secondColumn = 3;
        }    else if (player.secondColumn >= 4) {
            player.secondColumn = 1;
    }
            else{
                        player.secondColumn++;
                }
	        } else if (buttonId == 26) { //2nd Subtract
	                if (player.secondColumn <= 1) {
	                        player.secondColumn = 4;
	                } else {
	                        player.secondColumn--;
	                }
	        } else if (buttonId == 27) { //3rd Plus
                if (player.thirdColumn == 2) {
                        player.thirdColumn = 4;
                } else if (player.thirdColumn == 3) {
                    player.thirdColumn = 3;
            }    else if (player.thirdColumn >= 4) {
                player.thirdColumn = 1;
        }
                else{
	                        player.thirdColumn++;
	                }
	        } else if (buttonId == 28) { //3rd Subtract
	                if (player.thirdColumn <= 1) {
	                        player.thirdColumn = 4;
	                } else {
	                        player.thirdColumn--;
	                }
	        } else if (buttonId == 21) { //Confirm
	                FairyRing.ringTele(player, player.firstColumn, player.secondColumn, player.thirdColumn);
	        }
			else if (interfaceId == 938) {
				if (buttonId == 39) {
					player.closeInterfaces();
					new DungeonPartyManager(player);
					player.dungtime = 800;
				}
			}
		} else  if (interfaceId == 1139) {
			if(buttonId == 2) {
				XPLamp.appendExperience(player);
			} else {
				XPLamp.handleButtons(player, buttonId);
			}
		} 
		else if (warnings.handleButtons(player, interfaceId, buttonId))
			return;
		else if (interfaceId == 797) {
			SpiritTree.handleButtons(player, buttonId);
		
		}
		 else if (interfaceId == 6) {
				player.getAgilityrewardsHandler().handleButtons(player, buttonId);
			
			}
		
		else if (interfaceId == 309) 
			PlayerLook.handleHairdresserSalonButtons(player, buttonId, slotId);
		else if (interfaceId == 729) 
			PlayerLook.handleThessaliasMakeOverButtons(player, buttonId, slotId);
		else if (interfaceId == 1027) {
			TaskManager.handleButtons(player, buttonId);
		}else if (interfaceId == 1123) {
				GiftBox.handleButtons(player, buttonId);	
		}else if (interfaceId == 95) {
			player.getShipcharter().handleButtons(player, buttonId);
		} else if (interfaceId == 825) {
				PlayerTaskShop.handleButtons(player, buttonId);
		} else if (interfaceId == 1066) {
			player.sm("Add this");
		} 	 	
		else if (interfaceId == 666) {
			switch (buttonId) {
			case 18:
				Summoning.sendInterface(player);
				break;
			case 16:
				switch (packetId) {
				case 14:
					SummoningScroll.createScroll(player, slotId2, 1);
					break;
				case 67: // 5
					SummoningScroll.createScroll(player, slotId2, 5);
					break;
				case 5: // 10
					SummoningScroll.createScroll(player, slotId2, 10);
					break;
				case 55: // all
					SummoningScroll.createScroll(player, slotId2, 28);
					break;
				case 68: // x
					player.getTemporaryAttributtes().put("item1", slotId2);
					player.getTemporaryAttributtes().put("sum1",
							Integer.valueOf(0));
					player.packets()
							.sendRunScript(
									108,
									new Object[] { "How many would you like to make? " });
					break;
				}
				break;
			}
		}
		else if (interfaceId == 672){
			if (buttonId == 19)
				SummoningScroll.sendInterface(player);
			
			if (buttonId == 16) {
				switch(slotId) {
				case 2: //				level,shards,Charm,Item,2nd item (-1 = null),Pouch,xp (times by 5)
					SummonTrain.CreatePouch(player, 1, 7,12158,2859,-1,12047,48);
					return;
				case 7:
					SummonTrain.CreatePouch(player, 4, 8,12158,2138,-1,12043,93);
					break;
				case 12:
					SummonTrain.CreatePouch(player, 10, 8,12158,6291,-1,12059,126);
					break;
				case 17:
					SummonTrain.CreatePouch(player, 13, 9,12158,3363,-1,12019,126);
					break;
				case 22:
					SummonTrain.CreatePouch(player, 16, 7,12158,440,-1,12009,216);
					break;
				case 27:
					SummonTrain.CreatePouch(player, 17, 1,12158,6319,-1,12778,465);
					break;
				case 32:
					SummonTrain.CreatePouch(player, 18, 45,12159,1783,-1,12049,312);
					break;
				case 37:
					SummonTrain.CreatePouch(player, 19, 57,12160,3095,-1,12055,832);
					break;
				case 42:
					SummonTrain.CreatePouch(player, 22, 64,12160,12168,-1,12808,968);
					break;
				case 47:
					SummonTrain.CreatePouch(player, 23, 75,12163,2134,-1,12067,1024);
					break;
				case 52:
					SummonTrain.CreatePouch(player, 25, 51,12163,3138,-1,12063,500);
					break;
				case 57:
					SummonTrain.CreatePouch(player, 28, 47,12159,6032,-1,12091,498);
					break;
				case 62:
					SummonTrain.CreatePouch(player, 29, 84,12163,9976,-1,12800,552);
					break;
				case 67:
					SummonTrain.CreatePouch(player, 31, 81,12160,3325,-1,12053,1360);
					break;
				case 72:
					SummonTrain.CreatePouch(player, 32, 84,12160,12156,-1,12065,1408);
					break;
				case 77:
					SummonTrain.CreatePouch(player, 33, 72,12159,1519, -1,12021,576);
					break;
				case 82:
					SummonTrain.CreatePouch(player, 34, 74,12159,12164,-1,12818,596);
					break;
				case 87:
					SummonTrain.CreatePouch(player, 34, 74, 12163,12166,-1, 12780,1096);
					break;
				case 92:
					SummonTrain.CreatePouch(player, 34, 74, 12163, 12167,-1,12798,1096);
					break;
				case 97:
					SummonTrain.CreatePouch(player, 34, 74, 12163,12165,-1,12814,2096);
					break;
				case 107:
					SummonTrain.CreatePouch(player, 40, 11, 12158, 6010,-1,12086,528);
					break;
				case 117:
					SummonTrain.CreatePouch(player, 42, 104, 12160, 12134, -1, 12051, 1848);
					break;
				case 122:
					SummonTrain.CreatePouch(player, 43, 88, 12159, 12109, -1, 12095, 752);
					break;
				case 127:
					SummonTrain.CreatePouch(player, 43, 88, 12159, 12111, -1, 12097, 752);
					break;
				case 132:
					SummonTrain.CreatePouch(player, 43, 88, 12159, 12113, -1, 12099, 752);
					break;
				case 137:
					SummonTrain.CreatePouch(player, 43, 88, 12159, 12115, -1, 12101, 752);
					break;
				case 347:
					SummonTrain.CreatePouch(player, 85, 150, 12160, 10149, -1, 12776, 4500);
					break;
//Custom adds	@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
					
				case 387:
					SummonTrain.CreatePouch(player, 99, 200, 12160, 1119, -1, 12790, 10000);//Steel titan
					break; 
	
				case 102:
					SummonTrain.CreatePouch(player, 36, 102, 12163, 2349, -1, 12073, 2300);//Bronze mino
					break; 
					
				case 112:
					SummonTrain.CreatePouch(player, 41, 78, 12159, 249, -1, 12071, 1000);//Macaw
					break; 
				
					
				case 142:
					SummonTrain.CreatePouch(player, 43, 88, 12159, 12117, -1, 12103, 1000);//sp pengatrece
					break; 
					
				case 147:
					SummonTrain.CreatePouch(player, 43, 88, 12159, 12119, -1, 12105, 1000);//sp coraxatrice
					break; 
					
					
				case 152:
					SummonTrain.CreatePouch(player, 43, 88, 12159, 12121, -1, 12107, 1000);//sp vulatrice
					break; 
					
				case 157:
					SummonTrain.CreatePouch(player, 46, 125, 12163, 2351, -1, 12075, 2900);//iron mino
					break; 
					
				case 162:
					SummonTrain.CreatePouch(player, 46, 111, 12160, 590, -1, 12816, 1030);//Pyrelord
					break; 
					
				case 167:
					SummonTrain.CreatePouch(player, 47, 88, 12159, 1635, -1, 12041, 1020);//Magpie
					break;

				case 172:
					SummonTrain.CreatePouch(player, 49, 117, 12160, 2132, -1, 12061, 1040);//Bloated leech
					break; 
					
				case 177:
					SummonTrain.CreatePouch(player, 52, 12, 12158, 9978, -1, 12007, 2000);//terror bird
					break; 
					
				case 182:
					SummonTrain.CreatePouch(player, 54, 106, 12159, 12161, -1, 12035, 2000);//abyssal parasite
					break; 
					
				case 187:
					SummonTrain.CreatePouch(player, 55, 151, 12163, 1937, -1, 12027, 3200);//Spirit Jelly
					break; 
					
				case 192:
					SummonTrain.CreatePouch(player, 56, 141, 12163, 2353, -1, 12077, 3400);//steel mino
					break; 
					
				case 197:
					SummonTrain.CreatePouch(player, 56, 109, 12159, 311, -1, 12531, 2500);//ibis
					break; 
					
				case 202:
					SummonTrain.CreatePouch(player, 57, 153, 12163, 10103, -1, 12812, 3700);//spirit kyatt
					break; 
					
				case 207:
					SummonTrain.CreatePouch(player, 57, 155, 12163, 10095, -1, 12784, 3700);//spirit larupia
					break;

				case 212:
					SummonTrain.CreatePouch(player, 57, 153, 12163, 10099, -1, 12810, 3700);//spirit garaahk
					break; 
					
				case 217:
					SummonTrain.CreatePouch(player, 58, 144, 12163, 6667, -1, 12023, 4000);//karamthulu overlord
					break;

				case 222:
					SummonTrain.CreatePouch(player, 61, 141, 12160, 9736, -1, 12085, 3000);//dust devil
					break; 
					
				case 227:
					SummonTrain.CreatePouch(player, 62, 119, 12159, 12161, -1, 12037, 3100);//abysal lurker
					break;
					
				case 232:
					SummonTrain.CreatePouch(player, 63, 116, 12160, 6287, -1, 12015, 3200);//spirit cobra
					break; 
					
				case 237:
					SummonTrain.CreatePouch(player, 64, 128, 12160, 8431, -1, 12045, 3250);//stranger plant
					break; 
					
				case 242:
					SummonTrain.CreatePouch(player, 66, 152, 12163, 2359, -1, 12079, 4500);//mithril mino
					break; 
					
				case 247:
					SummonTrain.CreatePouch(player, 66, 11, 12158, 2150, -1, 12123, 3400);//barker toad
					break; 
					
				case 252:
					SummonTrain.CreatePouch(player, 67, 1, 12158, 7939, -1, 12031, 3600);//war tortise
					break;
					
				case 257:
					SummonTrain.CreatePouch(player, 68, 110, 12159, 383, -1, 12029, 3700);//bunyip
					break;
					
				case 262:
					SummonTrain.CreatePouch(player, 69, 130, 12159, 1963, -1, 12033, 3800);//fruit bat
					break;
					
				case 267:
					SummonTrain.CreatePouch(player, 70, 130, 12160, 1933, -1, 12820, 3900);//ravenging locust
					break;
					
				case 272:
					SummonTrain.CreatePouch(player, 71, 14, 12158, 10117, -1, 12057, 4000);//arctic bear
					break;
					
				case 277:
					SummonTrain.CreatePouch(player, 72, 165, 12160, 14616, -1, 14623, 4100);//pheonix pouch
					break;
					
				case 282:
					SummonTrain.CreatePouch(player, 73, 195, 12163, 12168, -1, 12792, 6000);//obsidion golem
					break;
					
				case 287:
					SummonTrain.CreatePouch(player, 74, 166, 12160, 6983, -1, 12069, 4300);//granite lobster
					break;
					
				case 292:
					SummonTrain.CreatePouch(player, 75, 168, 12160, 2460, -1, 12011, 4400);//mantis
					break;
					
				case 297:
					SummonTrain.CreatePouch(player, 76, 141, 12159, 10020, -1, 12782, 4500);//forge regent
					break;
					
				case 302:
					SummonTrain.CreatePouch(player, 76, 144, 12163, 2361, -1, 12081, 5400);//ady mino
					break;
				
				case 307:
					SummonTrain.CreatePouch(player, 77, 174, 12160, 12162, -1, 12794, 4700);//talon beast
					break;
					
				case 312:
					SummonTrain.CreatePouch(player, 78, 124, 12159, 5933, -1, 12013, 4800);//giant ent
					break;
					
				case 317:
					SummonTrain.CreatePouch(player, 79, 198, 12163, 1442, -1, 12802, 6000);//fire titan
					break;
					
				case 322:
					SummonTrain.CreatePouch(player, 79, 202, 12163, 1440, -1, 12804, 6500);//moss titan
					break;
					
				case 327:
					SummonTrain.CreatePouch(player, 79, 198, 12163, 1440, 1438, 1444, 7000);//ice titan
					break;
					
				case 332:
					SummonTrain.CreatePouch(player, 80, 128, 12159, 571, -1, 12025, 5100);//hydra
					break;
					
				case 337:
					SummonTrain.CreatePouch(player, 83, 1, 12160, 6155, -1, 12017, 5200);//spirit dagonoth
					break;
					
				case 342:
					SummonTrain.CreatePouch(player, 83, 219, 12160, 12168, -1, 12788, 5300);//lava titan
					break;
				
					
				case 352:
					SummonTrain.CreatePouch(player, 86, 1, 12163, 2363, -1, 12083, 7000);//rune mino
					break;
					
				case 357:
					SummonTrain.CreatePouch(player, 88, 140, 12159, 237, -1, 12039, 5500);//unicorn stallion
					break;
					
				case 362:
					SummonTrain.CreatePouch(player, 89, 222, 12163, 1444, -1, 12786, 7600);//geyser titan
					break;
					
				case 367:
					SummonTrain.CreatePouch(player, 92, 203, 12160, 3226, 2859, 12089, 7000);//wolpertinger
					break;
				
				case 372:
					SummonTrain.CreatePouch(player, 93, 113, 12159, 12161, -1, 12796, 7500);//abyss titan
					break;
					
				case 377:
					SummonTrain.CreatePouch(player, 95, 198, 12160, 1115, -1, 12822, 9000);//iron titan
					break;
					
				case 382:
					SummonTrain.CreatePouch(player, 96, 211, 12160, 10818, -1, 12093, 9500);//pack yak
					break;


					//				level,shards,Charm,Item,2nd item (-1 = null),Pouch,xp (times by 5)
				default:
					player.sm("This pouch is going to be added soon.");
					//logger.debug("summonButton: "+buttonId2+".");
					break;
				}
				
			if (buttonId == 19) {
				player.closeInterfaces();
				}	
			} 
		} else if (interfaceId == 105) {
			System.out.println("Component: " + buttonId + " Packet: "
					+ stream.getId());
			switch (buttonId) {
			case 190:
				player.packets().setGeSearch(GrandExchangeConstants.SEARCH);
				break;
			default:
				Game.getGrandExchange().handleButtons(player, buttonId,
						stream.getId());
				break;
			}
		} else if (interfaceId == 880) {
			if (buttonId >= 7 && buttonId <= 19)
				Familiar.setLeftclickOption(player, (buttonId - 7) / 2);
			else if (buttonId == 21)
				Familiar.confirmLeftOption(player);
			else if (buttonId == 25)
				Familiar.setLeftclickOption(player, 7);
		} else if (interfaceId == 662) {
			if (player.getFamiliar() == null)
				return;
			if (buttonId == 49)
				player.getFamiliar().call();
			else if (buttonId == 51)
				player.getMatrixDialogues().startDialogue("DismissD");
			else if (buttonId == 67)
				player.getFamiliar().takeBob();
			else if (buttonId == 69)
				player.getFamiliar().renewFamiliar();
			else if (buttonId == 74) {
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK)
					player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().hasSpecialOn())
					player.getFamiliar().submitSpecial(player);
			}
		} else if (interfaceId == 747) {
			if (buttonId == 7) {
				Familiar.selectLeftOption(player);
			} else if (player.getFamiliar() == null)
				return;
			if (buttonId == 10 || buttonId == 19)
				player.getFamiliar().call();
			else if (buttonId == 11 || buttonId == 20)
				player.getMatrixDialogues().startDialogue("DismissD");
			else if (buttonId == 12 || buttonId == 21)
				player.getFamiliar().takeBob();
			else if (buttonId == 13 || buttonId == 22)
				player.getFamiliar().renewFamiliar();
			else if (buttonId == 18 || buttonId == 18)
				player.getFamiliar().sendFollowerDetails();
			else if (buttonId == 17) {
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK)
					player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().hasSpecialOn())
					player.getFamiliar().submitSpecial(player);
			}
		
		} else if (interfaceId == 187) {
			if (buttonId == 1) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					player.getMusicsManager().playAnotherMusic(slotId / 2);
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					player.getMusicsManager().addToPlayList(slotId / 2);
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					player.getMusicsManager().removeFromPlayList(slotId / 2);
			} else if (buttonId == 4)
				player.getMusicsManager().addPlayingMusicToPlayList();
			else if (buttonId == 10)
				player.getMusicsManager().switchPlayListOn();
			else if (buttonId == 11)
				player.getMusicsManager().clearPlayList();
			else if (buttonId == 13)
				player.getMusicsManager().switchShuffleOn();
		} else if (interfaceId == 275) {
			if (buttonId == 14) {
				player.sm("Coming soon.");
			}
		} else if (interfaceId == 590 || interfaceId == 494) {
			player.getEmotesManager().useBookEmote(slotId);
		} else if (interfaceId == 192) {
			if (player.isDeveloperMode()) 
			Logger.log("ButtonHandler", "InterfaceId " + interfaceId
					+ ", componentId " + buttonId + ", slotId " + slotId
					+ ", slotId2 " + slotId2 + ", PacketId: " + packetId);
			MagicAbility.handleMagicAbility(player, buttonId);
			if (buttonId == 2)
				player.getCombatDefinitions().switchDefensiveCasting();
			else if (buttonId == 7)
				player.getCombatDefinitions().switchShowCombatSpells();
			else if (buttonId == 9)
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			else if (buttonId == 11)
				player.getCombatDefinitions().switchShowMiscallaneousSpells();
			else if (buttonId == 13)
				player.getCombatDefinitions().switchShowSkillSpells();
			else if (buttonId >= 15 & buttonId <= 17)
				player.getCombatDefinitions()
						.setSortSpellBook(buttonId - 15);
			else
				Magic.processNormalSpell(player, buttonId, packetId);
			if (player.isDeveloperMode()) 
				Logger.log("ButtonHandler", "InterfaceId " + interfaceId
						+ ", componentId " + buttonId + ", slotId " + slotId
						+ ", slotId2 " + slotId2 + ", PacketId: " + packetId);
		} else if (interfaceId == 336) {
			if (buttonId == 0)
				if (packetId == 81) {
					player.getTemporaryAttributtes().put("offerX",
							Integer.valueOf(slotId));
					player.packets().sendRunScript(108,
							new Object[] { "Enter Amount:" });
				}
			if (packetId == 61)
				player.getTrade().addItem(player, slotId, 1);
			else if (packetId == 64)
				player.getTrade().addItem(player, slotId, 5);
			else if (packetId == 4)
				player.getTrade().addItem(player, slotId, 10);
			else if (packetId == 52)
				player.getTrade().addItem(player, slotId, 0x7fffffff);
			else if (packetId == 91)
				player.packets().sendMessage(
						(new StringBuilder("A "))
								.append((new Item(slotId2, 1)).defs()
										.getName().toLowerCase())
								.append("'s value is ")
								.append(ItemDefinition.get(
										slotId2).getValue(slotId2))
								.append(" gold.").toString());
		} else if (interfaceId == 335) {
			if (buttonId == 34)
				if (packetId == 25)
					player.getBank().sendExamine(slotId2);
				else if (packetId == 61)
					player.packets().sendMessage(
							(new StringBuilder("A "))
									.append((new Item(slotId2, 1))
											.defs().getName()
											.toLowerCase())
									.append("'s value is ")
									.append(ItemDefinition.get(
											slotId2).getValue(slotId2))
									.append(" gold.").toString());
			if (buttonId == 31)
				if (packetId == 61)
					player.getTrade().removeItem(player, slotId, 1);
				else if (packetId == 64)
					player.getTrade().removeItem(player, slotId, 5);
				else if (packetId == 4)
					player.getTrade().removeItem(player, slotId, 10);
				else if (packetId == 52)
					player.getTrade().removeItem(player, slotId, 0x7fffffff);
				else if (packetId == 25) {
					player.getBank().sendExamine(slotId);
					return;
		}
				else if (packetId == 91)
					player.packets().sendMessage(
							(new StringBuilder("A "))
									.append((new Item(slotId2, 1))
											.defs().getName()
											.toLowerCase())
									.append("'s value is ")
									.append(ItemDefinition.get(
											slotId2).getValue(slotId2))
									.append(" gold.").toString());
				else if (packetId == 81) {
					player.getTemporaryAttributtes().put("removeX",
							Integer.valueOf(slotId));
					player.packets().sendRunScript(108,
							new Object[] { "Enter Amount:" });
				}
			if (buttonId == 18 || buttonId == 12) {
				player.getTrade().tradeFailed();
			} else if (buttonId == 16)
				player.getTrade().acceptPressed(player);
		} else if (interfaceId == 334) {
			if (buttonId == 22) {
				player.getTrade().tradeFailed();
			} else if (buttonId == 21)
				player.getTrade().acceptPressed(player);
		} else if (interfaceId == 300) {
			ForgingInterface.handleIComponents(player, buttonId);
		}
		
		else if (interfaceId == 1083) {
			if (player.isInTutorial()) {
				player.sendMessage("Cannot use noticeboard during the introduction.");
				return;
			}
			QuestList.handleInterfaceButtons(player, buttonId);
		}
			else if (interfaceId == 72) {
				if (player.isInTutorial()) {
					player.sendMessage("Cannot use noticeboard during the introduction.");
					return;
				}
				QuestList.handleButtons(player, buttonId);
		}	else if (interfaceId == 1019) {
				
		} else if (interfaceId == 206) {
			if (buttonId == 13) {
				player.closeInterfaces();
				player.interfaces().closeInventoryInterface();
			}
			else if (buttonId == 15) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					player.getPriceCheckManager().removeItem(slotId, 1);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					player.getPriceCheckManager().removeItem(slotId, 5);
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					player.getPriceCheckManager().removeItem(slotId, 10);
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					player.getPriceCheckManager().removeItem(slotId,
							Integer.MAX_VALUE);
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("pc_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().put("pc_isRemove",
							Boolean.TRUE);
					player.packets().sendRunScript(108,
							new Object[] { "Enter Amount:" });
				}
			}
			/*
			 * } else if (interfaceId == 672) { if (componentId == 16) { if
			 * (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
			 * Summoning.sendCreatePouch(player, slotId2, 1); else if (packetId
			 * == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
			 * Summoning.sendCreatePouch(player, slotId2, 5); else if (packetId
			 * == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
			 * Summoning.sendCreatePouch(player, slotId2, 10); else if (packetId
			 * == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
			 * Summoning.sendCreatePouch(player, slotId2, Integer.MAX_VALUE);
			 * else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
			 * Summoning.sendCreatePouch(player, slotId2, 28);// x else if
			 * (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
			 * player.getPackets().sendGameMessage( "You currently need " +
			 * ItemDefinitions.getItemDefinitions( slotId2)
			 * .getCreateItemRequirements()); } }
			 */
		} else if (interfaceId == 207) {
			if (buttonId == 0) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					player.getPriceCheckManager().addItem(slotId, 1);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					player.getPriceCheckManager().addItem(slotId, 5);
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					player.getPriceCheckManager().addItem(slotId, 10);
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					player.getPriceCheckManager().addItem(slotId,
							Integer.MAX_VALUE);
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("pc_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().remove("pc_isRemove");
					player.packets().sendRunScript(108,
							new Object[] { "Enter Amount:" });
				} else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == 665) {
			if (player.getFamiliar() == null
					|| player.getFamiliar().getBob() == null)
				return;
			if (buttonId == 0) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					player.getFamiliar().getBob().addItem(slotId, 1);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					player.getFamiliar().getBob().addItem(slotId, 5);
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					player.getFamiliar().getBob().addItem(slotId, 10);
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					player.getFamiliar().getBob()
							.addItem(slotId, Integer.MAX_VALUE);
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().remove("bob_isRemove");
					player.packets().sendRunScript(108,
							new Object[] { "Enter Amount:" });
				} else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == 671) {
			if (player.getFamiliar() == null
					|| player.getFamiliar().getBob() == null)
				return;
			if (buttonId == 27) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					player.getFamiliar().getBob().removeItem(slotId, 1);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					player.getFamiliar().getBob().removeItem(slotId, 5);
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					player.getFamiliar().getBob().removeItem(slotId, 10);
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					player.getFamiliar().getBob()
							.removeItem(slotId, Integer.MAX_VALUE);
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().put("bob_isRemove",
							Boolean.TRUE);
					player.packets().sendRunScript(108,
							new Object[] { "Enter Amount:" });
				}
			} else if (buttonId == 29)
				player.getFamiliar().takeBob();
		} else if (interfaceId == 916) {
			SkillsDialogue.handleSetQuantityButtons(player, buttonId);
		} else if (interfaceId == 193) {
			if (buttonId == 5)
				player.getCombatDefinitions().switchShowCombatSpells();
			else if (buttonId == 7)
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			else if (buttonId >= 9 && buttonId <= 11)
				player.getCombatDefinitions().setSortSpellBook(buttonId - 9);
			else if (buttonId == 18)
				player.getCombatDefinitions().switchDefensiveCasting();
			else
				Magic.processAncientSpell(player, buttonId, packetId);
		} else if (interfaceId == 430) {
			MagicAbility.handleMagicAbility(player, buttonId);
			if (buttonId == 5)
				player.getCombatDefinitions().switchShowCombatSpells();
			else if (buttonId == 7)
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			else if (buttonId == 9)
				player.getCombatDefinitions().switchShowMiscallaneousSpells();
			else if (buttonId >= 11 & buttonId <= 13)
				player.getCombatDefinitions()
						.setSortSpellBook(buttonId - 11);
			else if (buttonId == 20)
				player.getCombatDefinitions().switchDefensiveCasting();
			else
				Magic.processLunarSpell(player, buttonId, packetId);
		} else if (interfaceId == 261) {
			if (player.interfaces().containsInventoryInter())
				return;
			if (buttonId == 14) {
				if (player.interfaces().containsScreenInter()) {
					player.packets()
							.sendMessage(
									"Please close the interface you have open before setting your graphic options.");
					return;
				}
				player.stopAll();
				player.interfaces().sendInterface(742);
			}
			else if (buttonId == 7) {
				if (player.isAcceptAid()) 
					player.setAcceptAid(false);
				else
					player.setAcceptAid(true);
				player.packets().sendAcceptAidStatus();
			}
			else if (buttonId == 4)
				player.switchAllowChatEffects();
			else if (buttonId == 5) {
				player.interfaces().sendSettings(982);
			} else if (buttonId == 6)
				player.switchMouseButtons();
			else if (buttonId == 16) {
				if (player.interfaces().containsScreenInter()) {
					player.packets()
							.sendMessage(
									"Please close the interface you have open before setting your audio options.");
					return;
				}
				player.stopAll();
				player.interfaces().sendInterface(743);
			}
		} else if (interfaceId == 982) {
			if (buttonId == 5)
				player.interfaces().sendSettings();
			else if (buttonId == 42)
				player.setPrivateChatSetup(player.getPrivateChatSetup() == 0 ? 1
						: 0);
			else if (buttonId >= 49 && buttonId <= 61)
				player.setPrivateChatSetup(buttonId - 48);
		} else if (interfaceId == 271) {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (buttonId == 8 || buttonId == 42)
						player.getPrayer().switchPrayer(slotId);

					else if (buttonId == 43
							&& player.getPrayer().isUsingQuickPrayer())
						player.getPrayer().switchSettingQuickPrayer();
				}
			});
		} else if (interfaceId == 320) {
			player.stopAll();
			int lvlupSkill = -1;
			int skillMenu = -1;
			switch (buttonId) {
			case 200: // Attack
				skillMenu = 1;
				if (player.getTemporaryAttributtes().remove("leveledUp[0]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 1);
				} else {
					lvlupSkill = 0;
					player.packets().sendConfig(1230, 10);
				}
				break;
			case 11: // Strength
				skillMenu = 2;
				if (player.getTemporaryAttributtes().remove("leveledUp[2]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 2);
				} else {
					lvlupSkill = 2;
					player.packets().sendConfig(1230, 20);
				}
				break;
			case 28: // Defence
				skillMenu = 5;
				if (player.getTemporaryAttributtes().remove("leveledUp[1]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 5);
				} else {
					lvlupSkill = 1;
					player.packets().sendConfig(1230, 40);
				}
				break;
			case 52: // Ranged
				skillMenu = 3;
				if (player.getTemporaryAttributtes().remove("leveledUp[4]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 3);
				} else {
					lvlupSkill = 4;
					player.packets().sendConfig(1230, 30);
				}
				break;
			case 76: // Prayer
				if (player.getTemporaryAttributtes().remove("leveledUp[5]") != Boolean.TRUE) {
					skillMenu = 7;
					player.packets().sendConfig(965, 7);
				} else {
					lvlupSkill = 5;
					player.packets().sendConfig(1230, 60);
				}
				break;
			case 93: // Magic
				if (player.getTemporaryAttributtes().remove("leveledUp[6]") != Boolean.TRUE) {
					skillMenu = 4;
					player.packets().sendConfig(965, 4);
				} else {
					lvlupSkill = 6;
					player.packets().sendConfig(1230, 33);
				}
				break;
			case 110: // Runecrafting
				if (player.getTemporaryAttributtes().remove("leveledUp[20]") != Boolean.TRUE) {
					skillMenu = 12;
					player.packets().sendConfig(965, 12);
				} else {
					lvlupSkill = 20;
					player.packets().sendConfig(1230, 100);
				}
				break;
			case 134: // Construction
				skillMenu = 22;
				if (player.getTemporaryAttributtes().remove("leveledUp[21]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 22);
				} else {
					lvlupSkill = 21;
					player.packets().sendConfig(1230, 698);
				}
				break;
			case 193: // Hitpoints
				skillMenu = 6;
				if (player.getTemporaryAttributtes().remove("leveledUp[3]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 6);
				} else {
					lvlupSkill = 3;
					player.packets().sendConfig(1230, 50);
				}
				break;
			case 19: // Agility
				skillMenu = 8;
				if (player.getTemporaryAttributtes().remove("leveledUp[16]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 8);
				} else {
					lvlupSkill = 16;
					player.packets().sendConfig(1230, 65);
				}
				break;
			case 36: // Herblore
				skillMenu = 9;
				if (player.getTemporaryAttributtes().remove("leveledUp[15]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 9);
				} else {
					lvlupSkill = 15;
					player.packets().sendConfig(1230, 75);
				}
				break;
			case 60: // Thieving
				skillMenu = 10;
				if (player.getTemporaryAttributtes().remove("leveledUp[17]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 10);
				} else {
					lvlupSkill = 17;
					player.packets().sendConfig(1230, 80);
				}
				break;
			case 84: // Crafting
				skillMenu = 11;
				if (player.getTemporaryAttributtes().remove("leveledUp[12]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 11);
				} else {
					lvlupSkill = 12;
					player.packets().sendConfig(1230, 90);
				}
				break;
			case 101: // Fletching
				skillMenu = 19;
				if (player.getTemporaryAttributtes().remove("leveledUp[9]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 19);
				} else {
					lvlupSkill = 9;
					player.packets().sendConfig(1230, 665);
				}
				break;
			case 118: // Slayer
				skillMenu = 20;
				if (player.getTemporaryAttributtes().remove("leveledUp[18]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 20);
				} else {
					lvlupSkill = 18;
					player.packets().sendConfig(1230, 673);
				}
				break;
			case 142: // Hunter
				skillMenu = 23;
				if (player.getTemporaryAttributtes().remove("leveledUp[22]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 23);
				} else {
					lvlupSkill = 22;
					player.packets().sendConfig(1230, 689);
				}
				break;
			case 186: // Mining
				skillMenu = 13;
				if (player.getTemporaryAttributtes().remove("leveledUp[14]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 13);
				} else {
					lvlupSkill = 14;
					player.packets().sendConfig(1230, 110);
				}
				break;
			case 179: // Smithing
				skillMenu = 14;
				if (player.getTemporaryAttributtes().remove("leveledUp[13]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 14);
				} else {
					lvlupSkill = 13;
					player.packets().sendConfig(1230, 115);
				}
				break;
			case 44: // Fishing
				skillMenu = 15;
				if (player.getTemporaryAttributtes().remove("leveledUp[10]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 15);
				} else {
					lvlupSkill = 10;
					player.packets().sendConfig(1230, 120);
				}
				break;
			case 68: // Cooking
				skillMenu = 16;
				if (player.getTemporaryAttributtes().remove("leveledUp[7]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 16);
				} else {
					lvlupSkill = 7;
					player.packets().sendConfig(1230, 641);
				}
				break;
			case 172: // Firemaking
				skillMenu = 17;
				if (player.getTemporaryAttributtes().remove("leveledUp[11]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 17);
				} else {
					lvlupSkill = 11;
					player.packets().sendConfig(1230, 649);
				}
				break;
			case 165: // Woodcutting
				skillMenu = 18;
				if (player.getTemporaryAttributtes().remove("leveledUp[8]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 18);
				} else {
					lvlupSkill = 8;
					player.packets().sendConfig(1230, 660);
				}
				break;
			case 126: // Farming
				skillMenu = 21;
				if (player.getTemporaryAttributtes().remove("leveledUp[19]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 21);
				} else {
					lvlupSkill = 19;
					player.packets().sendConfig(1230, 681);
				}
				break;
			case 150: // Summoning
				skillMenu = 24;
				if (player.getTemporaryAttributtes().remove("leveledUp[23]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 24);
				} else {
					lvlupSkill = 23;
					player.packets().sendConfig(1230, 705);
				}
				break;
			case 158: // Dung
				skillMenu = 25;
				if (player.getTemporaryAttributtes().remove("leveledUp[24]") != Boolean.TRUE) {
					player.packets().sendConfig(965, 25);
				} else {
					lvlupSkill = 24;
					player.packets().sendConfig(1230, 705);
				}
				break;
			}

			player.interfaces().sendInterface(
					lvlupSkill != -1 ? 741 : 499);
			if (lvlupSkill != -1)
				LevelUp.switchFlash(player, lvlupSkill, false);
			if (skillMenu != -1)
				player.getTemporaryAttributtes().put("skillMenu", skillMenu);
		} else if (interfaceId == 499) {
			int skillMenu = -1;
			if (player.getTemporaryAttributtes().get("skillMenu") != null)
				skillMenu = (Integer) player.getTemporaryAttributtes().get(
						"skillMenu");
			switch (buttonId) {
			case 10:
				player.packets().sendConfig(965, skillMenu);
				break;
			case 11:
				player.packets().sendConfig(965, 1024 + skillMenu);
				break;
			case 12:
				player.packets().sendConfig(965, 2048 + skillMenu);
				break;
			case 13:
				player.packets().sendConfig(965, 3072 + skillMenu);
				break;
			case 14:
				player.packets().sendConfig(965, 4096 + skillMenu);
				break;
			case 15:
				player.packets().sendConfig(965, 5120 + skillMenu);
				break;
			case 16:
				player.packets().sendConfig(965, 6144 + skillMenu);
				break;
			case 17:
				player.packets().sendConfig(965, 7168 + skillMenu);
				break;
			case 18:
				player.packets().sendConfig(965, 8192 + skillMenu);
				break;
			case 19:
				player.packets().sendConfig(965, 9216 + skillMenu);
				break;
			case 20:
				player.packets().sendConfig(965, 10240 + skillMenu);
				break;
			case 21:
				player.packets().sendConfig(965, 11264 + skillMenu);
				break;
			case 22:
				player.packets().sendConfig(965, 12288 + skillMenu);
				break;
			case 23:
				player.packets().sendConfig(965, 13312 + skillMenu);
				break;
			case 29: // close inter
				player.stopAll();
				break;
			}
		} else if (interfaceId == 387) {
			if (player.interfaces().containsInventoryInter())
				return;
			if (buttonId == 8)
				ButtonPackets.sendRemove(player, Equipment.SLOT_HAT);
			else if (buttonId == 11) {
				if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20769 || capeId == 20771)
						SkillCapeCustomizer.startCustomizing(player, capeId);
				} else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20767)
						SkillCapeCustomizer.startCustomizing(player, capeId);
				} else if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					ButtonPackets.sendRemove(player, Equipment.SLOT_CAPE);
				else if (packetId == PacketDecoder.ACTION_BUTTON8_PACKET)
					player.getEquipment().sendExamine(Equipment.SLOT_CAPE);
			} else if (buttonId == 14) {
				int ringId = player.getEquipment().getRingId();
				int amuletId = player.getEquipment().getAmuletId();
				if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706) {
						if (Magic.sendItemTeleportSpell(player, true,
								Transportation.EMOTE, Transportation.GFX, 4,
								new Location(3087, 3496, 0))) {
							Item amulet = player.getEquipment().getItem(
									Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(
										Equipment.SLOT_AMULET);
							}
						}
						else if (ringId <= 2567 && ringId >= 2552) {
							if (Magic.sendItemTeleportSpell(player, true,
									Transportation.EMOTE, Transportation.GFX, 4,
									new Location(3315, 3234, 0))) {
								Item ring = player.getEquipment().getItem(
										Equipment.SLOT_RING);
								if (ring != null) {
									ring.setId(ring.getId() + 2);
									player.getEquipment().refresh(
											Equipment.SLOT_RING);
								}
							}
						}
					} else if (amuletId == 1704 || amuletId == 10352)
						player.packets()
								.sendMessage(
										"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
				} else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706) {
						if (Magic.sendItemTeleportSpell(player, true,
								Transportation.EMOTE, Transportation.GFX, 4,
								new Location(2918, 3176, 0))) {
							Item amulet = player.getEquipment().getItem(
									Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(
										Equipment.SLOT_AMULET);
							}
						}else if (ringId <= 2567 && ringId >= 2552) {
							if (Magic.sendItemTeleportSpell(player, true,
									Transportation.EMOTE, Transportation.GFX, 4,
									new Location(2458, 3093, 0))) {
								Item ring = player.getEquipment().getItem(
										Equipment.SLOT_RING);
								if (ring != null) {
									ring.setId(ring.getId() + 2);
									player.getEquipment().refresh(
											Equipment.SLOT_RING);
								}
							}
						}
					}
				} else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706) {
						if (Magic.sendItemTeleportSpell(player, true,
								Transportation.EMOTE, Transportation.GFX, 4,
								new Location(3105, 3251, 0))) {
							Item amulet = player.getEquipment().getItem(
									Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(
										Equipment.SLOT_AMULET);
							}
						}else if (ringId <= 2567 && ringId >= 2552) {
							if (Magic.sendItemTeleportSpell(player, true,
									Transportation.EMOTE, Transportation.GFX, 4,
									new Location(2458, 3093, 0))) {
								Item ring = player.getEquipment().getItem(
										Equipment.SLOT_RING);
								if (ring != null) {
									ring.setId(ring.getId() + 2);
									player.getEquipment().refresh(
											Equipment.SLOT_RING);
								}
							}
						}
					}
				} else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706) {
						if (Magic.sendItemTeleportSpell(player, true,
								Transportation.EMOTE, Transportation.GFX, 4,
								new Location(3293, 3163, 0))) {
							Item amulet = player.getEquipment().getItem(
									Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(
										Equipment.SLOT_AMULET);
							}
						}
						 else if (ringId <= 2567 && ringId >= 2552) {
								if (Magic.sendItemTeleportSpell(player, true,
										Transportation.EMOTE, Transportation.GFX, 4,
										new Location(2458, 3093, 0))) {
									Item ring = player.getEquipment().getItem(
											Equipment.SLOT_RING);
									if (ring != null) {
										ring.setId(ring.getId() + 2);
										player.getEquipment().refresh(
												Equipment.SLOT_RING);
									}
								}
							}
					}
				} else if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					ButtonPackets.sendRemove(player, Equipment.SLOT_AMULET);
				else if (packetId == PacketDecoder.ACTION_BUTTON8_PACKET)
					player.getEquipment().sendExamine(Equipment.SLOT_AMULET);
			} else if (buttonId == 17)
				ButtonPackets.sendRemove(player, Equipment.SLOT_WEAPON);
			else if (buttonId == 20)
				ButtonPackets.sendRemove(player, Equipment.SLOT_CHEST);
			else if (buttonId == 23)
				ButtonPackets.sendRemove(player, Equipment.SLOT_SHIELD);
			else if (buttonId == 26)
				ButtonPackets.sendRemove(player, Equipment.SLOT_LEGS);
			else if (buttonId == 29)
				ButtonPackets.sendRemove(player, Equipment.SLOT_HANDS);
			else if (buttonId == 32)
				ButtonPackets.sendRemove(player, Equipment.SLOT_FEET);
			else if (buttonId == 35)
				ButtonPackets.sendRemove(player, Equipment.SLOT_RING);
			else if (buttonId == 38)
				ButtonPackets.sendRemove(player, Equipment.SLOT_ARROWS);
			else if (buttonId == 50) {
				if (packetId == 61 || packetId == 52) { //remove
					ButtonPackets.sendRemove(player, Equipment.SLOT_AURA);
					player.getAuraManager().removeAura();
				} else if (packetId == 25) // examine
					player.getEquipment().sendExamine(Equipment.SLOT_AURA);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET) {
					if(player.getEquipment().getItem(Equipment.SLOT_AURA).defs().name.toLowerCase().contains("aura")) {
						player.sm("TODO: add boost?");
						return;
					}
					player.getAuraManager().activate();
				} else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					player.getAuraManager().sendAuraRemainingTime();
			} else if (buttonId == 45) {
				player.stopAll();
				player.interfaces().sendInterface(17);
			} else if (buttonId == 39) {
				player.stopAll();
				player.packets().sendItems(93,
						player.getInventory().getItems());
				player.interfaces().sendInventoryInterface(670);
				player.interfaces().sendInterface(667);
				player.packets().sendInterSetItemsOptionsScript(670, 0, 93,
						4, 7, "Equip", "Compare", "Stats", "Examine");
				player.packets().sendUnlockIComponentOptionSlots(670, 0, 0,
						27, 0, 1, 2, 3);
				refreshEquipBonuses(player);
				for(int i = 0; i < 20; i++)
					player.packets().sendUnlockIComponentOptionSlots(667, i, 0, 14, 0);
			} else if (buttonId == 42) {
				if (player.interfaces().containsScreenInter()) {
					player.packets()
							.sendMessage(
									"Please finish what you're doing before opening the price checker.");
					return;
				}
				player.stopAll();
				player.getPriceCheckManager().initPriceCheck();
			}
		} else if (interfaceId == 449) {
			if (buttonId == 1) {
				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
				if (shop == null)
					return;
				shop.sendInventory(player);
			} else if (buttonId == 21) {
				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
				if (shop == null)
					return;
				Integer slot = (Integer) player.getTemporaryAttributtes().get(
						"ShopSelectedSlot");
				if (slot == null)
					return;
				if (shop.id == 89) {
					if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
						shop.buyDung(player, slot, 1);
					else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
						shop.buyDung(player, slot, 5);
					else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
						shop.buyDung(player, slot, 10);
					else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
						shop.buyDung(player, slot, 50);
					return;
				}
				if (shop.id == 76) {
					if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
						shop.buyVote(player, slot, 1);
					else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
						shop.buyVote(player, slot, 5);
					else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
						shop.buyVote(player, slot, 10);
					else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
						shop.buyVote(player, slot, 50);
					return;
				}
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					shop.buy(player, slot, 1);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					shop.buy(player, slot, 5);
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					shop.buy(player, slot, 10);
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					shop.buy(player, slot, 50);

			}
		} else if (interfaceId == 620) {
			if (buttonId == 25) {
				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
				if (shop == null)
					return;
				if (shop.id == 89) {
					if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
						shop.sendDungInfo(player, slotId);
					else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
						shop.buyDung(player, slotId, 1);
					else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
						shop.buyDung(player, slotId, 5);
					else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
						shop.buyDung(player, slotId, 10);
					else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET)
						shop.buyDung(player, slotId, 50);
					else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
						shop.buyDung(player, slotId, 500);
					else if (packetId == PacketDecoder.ACTION_BUTTON8_PACKET)
						shop.sendExamine(player, slotId);
					return;
				}
				if (shop.id == 76) {

					if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
						shop.sendVoteInfo(player, slotId);
					else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
						shop.buyVote(player, slotId, 1);
					else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
						shop.buyVote(player, slotId, 5);
					else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
						shop.buyVote(player, slotId, 10);
					else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET)
						shop.buyVote(player, slotId, 50);
					else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
						shop.buyVote(player, slotId, 500);
					else if (packetId == PacketDecoder.ACTION_BUTTON8_PACKET)
						shop.sendExamine(player, slotId);
					return;
				}
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					shop.sendInfo(player, slotId);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					shop.buy(player, slotId, 1);
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					shop.buy(player, slotId, 5);
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					shop.buy(player, slotId, 10);
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET)
					shop.buy(player, slotId, 50);
				else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
					shop.buy(player, slotId, 500);
				else if (packetId == PacketDecoder.ACTION_BUTTON8_PACKET)
					shop.sendExamine(player, slotId);
			}
		} else if (interfaceId == 621) {
			if (buttonId == 0) {

				if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
					player.getInventory().sendExamine(slotId);
				else {
					Shop shop = (Shop) player.getTemporaryAttributtes().get(
							"Shop");
					if (shop.id == 76 || shop.id == 89) {
						player.sm("You cannot sell to this shop.");
						return;
					}
					if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
						shop.sendValue(player, slotId);
					else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
						shop.sell(player, slotId, 1);
					else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
						shop.sell(player, slotId, 5);
					else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
						shop.sell(player, slotId, 10);
					else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET)
						shop.sell(player, slotId, 500);
				}
			}
		} else if (interfaceId == 640 || interfaceId == 631 || interfaceId == 628
				|| interfaceId == 626 || interfaceId == 637
				|| interfaceId == 639) {
			//Player other = (Player) player.getData().getRuntimeData().get("requestingDuel");
			//Duel.handleDuelRequestButtons(player, other, interfaceId, buttonId);
		} else if (interfaceId == 650) {
			if (buttonId == 17) {
				player.stopAll();
				player.setLocation(new Location(2974, 4384, 0));
				player.getControllerManager().startController(
						"CorpBeastControler");
			} else if (buttonId == 18)
				player.closeInterfaces();
		} else if (interfaceId == 667) {
			if(buttonId == 7) {
				if (slotId >= 14)
					return;
				item = player.getEquipment().getItem(slotId);
				if (item == null)
					return;
				if(packetId == 3)
					player.sm(ItemExamines.getExamine(item));
				if (!player.getControllerManager().canEquip(slotId, item.getId()))
					return;
				 if (packetId == 3)
				 player.getInventory().sendExamine(slotId);//TODO Examines?
				if (packetId == 61) {
					sendRemove(player, slotId);
					refreshEquipBonuses(player);
				}
			} else if(buttonId == 75) {
				player.interfaces().closeScreenInterface();
				player.unlock();
				player.resetStopDelay();
				player.interfaces().closeInventoryInterface();
			}
		} else if (interfaceId == 670) {
			if (buttonId == 0) {
				if (slotId >= player.getInventory().getItemsContainerSize())
					return;
				item = player.getInventory().getItem(slotId);
				if (item == null)
					return;
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET) {
					if (sendWear(player, slotId, item.getId()))
						ButtonPackets.refreshEquipBonuses(player);
				} else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == Inventory.INVENTORY_INTERFACE) { // inventory
			if (buttonId == 0) {
				if (slotId > 27
						|| player.interfaces()
								.containsInventoryInter())
					return;
				item = player.getInventory().getItem(slotId);
				if (item == null || item.getId() != slotId2)
					return;
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					InventoryActionHandler.handleItemOption1(player, slotId,
							slotId2, item);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					InventoryActionHandler.handleItemOption2(player, slotId,
							slotId2, item);
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					InventoryActionHandler.handleItemOption3(player, slotId,
							slotId2, item);
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					InventoryActionHandler.handleItemOption4(player, slotId,
							slotId2, item);
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET)
					InventoryActionHandler.handleItemOption5(player, slotId,
							slotId2, item);
				else if (packetId == PacketDecoder.ACTION_BUTTON6_PACKET)
					InventoryActionHandler.handleItemOption6(player, slotId,
							slotId2, item);
				else if (packetId == PacketDecoder.ACTION_BUTTON7_PACKET)
					InventoryActionHandler.dropItem(player, slotId,
							slotId2, item);
			}
		} else if (interfaceId == 742) {
			if (buttonId == 46) // close
				player.stopAll();
		} else if (interfaceId == 743) {
			if (buttonId == 20) // close
				player.stopAll();
		} else if (interfaceId == 741) {
			if (buttonId == 9) // close
				player.stopAll();
		} else if (interfaceId == 749) {
			if (buttonId == 1) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET) { // activate
					if (player.isDead() || player.isDueling()) {
						return;
					}
					
					player.getPrayer().switchQuickPrayers();
				}
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET) {// switch
					if (player.isDead() || player.isDueling()) {
						return;
					}
					player.getPrayer().switchSettingQuickPrayer();
			}
			}
		} else if (interfaceId == 750) {
			if (buttonId == 1) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET) {
					if(!player.tasks().hasFinished(player.tasks().ldTasks(), (byte) player.tasks().indexFromName("OnTheRun")))
						player.tasks().taskCompletion("OnTheRun");
					player.toogleRun(player.isResting() ? false : true);
					if (player.isResting())
						player.stopAll();
				} else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET) {
					if (player.isResting()) {
						player.stopAll();
						return;
					}
					long currentTime = Misc.currentTimeMillis();
					if (player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
						player.packets().sendMessage(
								"You can't rest while perfoming an emote.");
						return;
					}
					if (player.isTransformed()) {
						player.sendMessage("You cannot rest while you're in a form.");
						return;
					}
					if (player.getStopDelay() >= currentTime) {
						player.packets().sendMessage(
								"You can't rest while perfoming an action.");
						return;
					}
					player.stopAll();
					player.getActionManager().setSkill(new Rest());
				}
			}
		} else if (interfaceId == 11) {
			if (buttonId == 17) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					player.getBank().depositItem(slotId, 1, false);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					player.getBank().depositItem(slotId, 5, false);
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					player.getBank().depositItem(slotId, 10, false);
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					player.getBank().depositItem(slotId, Integer.MAX_VALUE,
							false);
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().remove("bank_isWithdraw");
					player.packets().sendRunScript(108,
							new Object[] { "Enter Amount:" });
				} else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
					player.getInventory().sendExamine(slotId);
			} else if (buttonId == 18)
				player.getBank().depositAllInventory(false);
			else if (buttonId == 20)
				player.getBank().depositAllEquipment(false);
		} else if (interfaceId == 762) {
			if (buttonId == 119)
					return;
			else if(buttonId == 117) {
				player.stopAll();
					player.packets().sendItems(93,
							player.getInventory().getItems());
					player.interfaces().sendInventoryInterface(670);
					player.interfaces().sendInterface(667);
					player.packets().sendInterSetItemsOptionsScript(670, 0, 93,
							4, 7, "Equip", "Compare", "Stats", "Examine");
					player.packets().sendUnlockIComponentOptionSlots(670, 0, 0,
							27, 0, 1, 2, 3);
					refreshEquipBonuses(player);
					for(int i = 0; i < 20; i++)
						player.packets().sendUnlockIComponentOptionSlots(667, i, 0, 14, 0);
			
			}
			else if (buttonId == 43) {
				player.closeInterfaces();
			return;
			}
			else if (buttonId == 15)
				player.getBank().switchInsertItems();
			else if (buttonId == 19)
				player.getBank().switchWithdrawNotes();
			else if (buttonId == 33)
				player.getBank().depositAllInventory(true);
			else if (buttonId == 35)
				player.getBank().depositAllEquipment(true);
			else if (buttonId == 46) {
				player.closeInterfaces();
				player.interfaces().sendInterface(767);
				player.setCloseInterfacesEvent(new Runnable() {
					@Override
					public void run() {
						player.getBank().openBank();
					}
				});
			}
		else if (buttonId >= 46 && buttonId <= 64) {
			int tabId = 9 - ((buttonId - 46) / 2);
			if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
				player.getBank().setCurrentTab(tabId);
			else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
				player.getBank().collapse(tabId);
		}
				else if (buttonId == 44) {
				player.closeInterfaces();
				player.interfaces().sendInterface(767);
				player.setCloseInterfacesEvent(new Runnable() {
					@Override
					public void run() {
						player.getBank().openBank();
					}
				});
			} else if (buttonId >= 44 && buttonId <= 62) {
				int tabId = 9 - ((buttonId - 44) / 2);
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					player.getBank().setCurrentTab(tabId);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					player.getBank().collapse(tabId);
			} else if (buttonId == 93) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					player.getBank().withdrawItem(slotId, 1);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					player.getBank().withdrawItem(slotId, 5);
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					player.getBank().withdrawItem(slotId, 10);
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					player.getBank().withdrawLastAmount(slotId);
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().put("bank_isWithdraw",
							Boolean.TRUE);
					player.packets().sendRunScript(108,
							new Object[] { "Enter Amount:" });
				} else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
					player.getBank().withdrawItem(slotId, Integer.MAX_VALUE);
				else if (packetId == PacketDecoder.ACTION_BUTTON6_PACKET)
					player.getBank().withdrawItemButOne(slotId);
				else if (packetId == PacketDecoder.ACTION_BUTTON8_PACKET)
					player.getBank().sendExamine(slotId);

			}
		} else if (interfaceId == 763) {
			if (buttonId == 0) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					player.getBank().depositItem(slotId, 1, true);
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					player.getBank().depositItem(slotId, 5, true);
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					player.getBank().depositItem(slotId, 10, true);
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					player.getBank().depositLastAmount(slotId);
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot",
							slotId);
					player.getTemporaryAttributtes().remove("bank_isWithdraw");
					player.packets().sendRunScript(108,
							new Object[] { "Enter Amount:" });
				} else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
					player.getBank().depositItem(slotId, Integer.MAX_VALUE,
							true);
				else if (packetId == PacketDecoder.ACTION_BUTTON8_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		} else if (interfaceId == 767) {
			if (buttonId == 10)
				player.getBank().openBank();
		}  else if (interfaceId == 884) {
			if (buttonId == 4) {
				if (buttonId == 4) {
					int weaponId = player.getEquipment().getWeaponId();
					if (player.hasInstantSpecial(weaponId)) {
						player.performInstantSpecial(weaponId);
						return;
					}
					submitSpecialRequest(player);
				}
			} else if (buttonId >= 11 && buttonId <= 14) {
				if(player.getEquipment().getWeaponId() == 22447) {
					player.sm("You cannot perform melee attacks with this weapon.");
					player.packets().sendConfig(43, 4);
					return;
				}
				player.getCombatDefinitions().setAttackStyle(buttonId - 11);
			}
			else if (buttonId == 15)
				player.getCombatDefinitions().switchAutoRetaliate();
		} else if (interfaceId == 755) {
			if (buttonId == 44) {
				player.packets().sendWindowsPane(
						player.interfaces().isFullScreen() ? 746
								: 548, 2);
			player.setNextAnimation(new Animation (-1));
			}
		} else if (interfaceId == 20)
			SkillCapeCustomizer.handleSkillCapeCustomizer(player, buttonId);
		else if (interfaceId == 1056) {
			if (buttonId == 102)
				player.interfaces().sendInterface(917);
		}
		else if (interfaceId == 1163 || interfaceId == 1164
				|| interfaceId == 1168 || interfaceId == 1170
				|| interfaceId == 1173)
			player.getDominionTower().handleButtons(interfaceId, buttonId);
	
		else if (interfaceId == 1108 || interfaceId == 1109)
			player.getFriendsIgnores().handleFriendChatButtons(interfaceId,
					buttonId, packetId);
		else if (interfaceId == 1079)
			player.closeInterfaces();
		if (player.isDeveloperMode()) 
			Logger.log("ButtonHandler", "InterfaceId " + interfaceId
					+ ", componentId " + buttonId + ", slotId " + slotId
					+ ", slotId2 " + slotId2 + ", PacketId: " + packetId);
	}
	
	public static void refreshEquipBonuses(Player player) {
		int[] bonus = player.getCombatDefinitions().getBonuses();
		player.packets().sendIComponentText(667, 31, "Stab: "+(bonus[0] < 0 ? bonus[0] : "+"+bonus[0]));
		player.packets().sendIComponentText(667, 32, "Slash: "+(bonus[1] < 0 ? bonus[1] : "+"+bonus[1]));
		player.packets().sendIComponentText(667, 33, "Crush: "+(bonus[2] < 0 ? bonus[2] : "+"+bonus[2]));
		player.packets().sendIComponentText(667, 34, "Magic: "+(bonus[3] < 0 ? bonus[3] : "+"+bonus[3]));
		player.packets().sendIComponentText(667, 35, "Range: "+(bonus[4] < 0 ? bonus[4] : "+"+bonus[4]));
		player.packets().sendIComponentText(667, 36, "Stab: "+(bonus[5] < 0 ? bonus[5] : "+"+bonus[5]));
		player.packets().sendIComponentText(667, 37, "Slash: "+(bonus[6] < 0 ? bonus[6] : "+"+bonus[6]));
		player.packets().sendIComponentText(667, 38, "Crush: "+(bonus[7] < 0 ? bonus[7] : "+"+bonus[7]));
		player.packets().sendIComponentText(667, 39, "Magic: "+(bonus[8] < 0 ? bonus[8] : "+"+bonus[8]));
		player.packets().sendIComponentText(667, 40, "Range: "+(bonus[9] < 0 ? bonus[9] : "+"+bonus[9]));
		player.packets().sendIComponentText(667, 41, "Summoning: "+(bonus[10] < 0 ? bonus[10] : "+"+bonus[10]));
		player.packets().sendIComponentText(667, 42, "Absorb Melee: "+player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORB_MELEE_BONUS]+"%");
		player.packets().sendIComponentText(667, 43, "Absorb Magic: "+player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORB_MAGE_BONUS]+"%");
		player.packets().sendIComponentText(667, 44, "Absorb Ranged: "+player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORB_RANGE_BONUS]+"%");
		player.packets().sendIComponentText(667, 45, "Strength: "+(bonus[14] < 0 ? bonus[14] : "+"+bonus[14]));
		player.packets().sendIComponentText(667, 46, "Ranged Str: "+(bonus[15] < 0 ? bonus[15] : "+"+bonus[15]));
		player.packets().sendIComponentText(667, 47, "Prayer: "+(bonus[16] < 0 ? bonus[16] : "+"+bonus[16]));
		player.packets().sendIComponentText(667, 48, "Magic Damage: +"+bonus[17]+"%");
	}

	public static void sendRemove(Player player, int slotId) {
		if (slotId >= 15)
			return;
		Item item = player.getEquipment().getItem(slotId);
		if (item == null
				|| !player.getInventory().addItem(item.getId(),
						item.getAmount()))
			return;
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getAppearance().generateAppearanceData();
		if (Runecrafting.isTiara(item.getId()))
			player.packets().sendConfig(491, 0);
		if (item.getName().contains("greegree"))
			ApeToll.untransformPlayer(player);
		if (slotId == 3)
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
	}

	public static void submitSpecialRequest(final Player player) {
		CoresManager.fastExecutor.schedule(new TimerTask() {
		    @Override
		    public void run() {
			try {
			    WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
				    if (player.isDead())
					return;
				    player.getCombatDefinitions().switchUsingSpecialAttack();
				}
			    }, 0);
			}
			catch (Throwable e) {
			    Logger.handle(e);
			}
		    }
		}, 300);
	    }
	 public static boolean sendWear(Player player, int slotId, int itemId) {
			player.stopAll(false, false);
			Item item = player.getInventory().getItem(slotId);
			if (item == null || item.getId() != itemId)
			    return false;
			if (item.defs().isNoted() || !item.defs().isWearItem(player.getAppearance().isMale())) {
			    player.packets().sendMessage("You can't wear that.");
			    return true;
			}
			int targetSlot = Equipment.getItemSlot(itemId);
			if (targetSlot == -1) {
			    player.packets().sendMessage("You can't wear that.");
			    return true;
			}
			if (!ItemConstants.canWear(item, player))
			    return true;
			boolean isTwoHandedWeapon = targetSlot == 3 && Equipment.isTwoHandedWeapon(item);
			if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			    player.packets().sendMessage("Not enough free space in your inventory.");
			    return true;
			}
			HashMap<Integer, Integer> requiriments = item.defs().getWearingSkillRequirements();
			boolean hasRequiriments = true;
			if (requiriments != null) {
			    for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0)
				    continue;
				int level = requiriments.get(skillId);
				if (level < 0 || level > 120)
				    continue;
				if (player.getSkills().getLevelFromXP(skillId) < level) {
					String name = Skills.SKILL_NAME[skillId]
							.toLowerCase();
					player.packets().sendMessage(
							"You need to have a"
									+ (name.startsWith("a") ? "n" : "")
									+ " " + name + " level of " + level
									+ " in order to wear this item.");
					hasRequiriments = false;
				}

			    }
			}
			if (!hasRequiriments)
			    return true;
			if (!player.getControllerManager().canEquip(targetSlot, itemId))
			    return false;
			player.stopAll(false, false);
			player.getInventory().deleteItem(slotId, item);
			if (targetSlot == 3) {
			    if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().addItem(player.getEquipment().getItem(5).getId(), player.getEquipment().getItem(5).getAmount())) {
				    player.getInventory().getItems().set(slotId, item);
				    player.getInventory().refresh(slotId);
				    return true;
				}
				player.getEquipment().getItems().set(5, null);
			    }
			} else if (targetSlot == 5) {
			    if (player.getEquipment().getItem(3) != null && Equipment.isTwoHandedWeapon(player.getEquipment().getItem(3))) {
				if (!player.getInventory().addItem(player.getEquipment().getItem(3).getId(), player.getEquipment().getItem(3).getAmount())) {
				    player.getInventory().getItems().set(slotId, item);
				    player.getInventory().refresh(slotId);
				    return true;
				}
				player.getEquipment().getItems().set(3, null);
			    }

			}
			if (player.getEquipment().getItem(targetSlot) != null && (itemId != player.getEquipment().getItem(targetSlot).getId() || !item.defs().isStackable())) {
			    if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
				player.getInventory().refresh(slotId);
			    } else
				player.getInventory().addItem(new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			    player.getEquipment().getItems().set(targetSlot, null);
			}
			if (targetSlot == Equipment.SLOT_AURA)
			    player.getAuraManager().removeAura();
			int oldAmt = 0;
			if (player.getEquipment().getItem(targetSlot) != null) {
			    oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
			}
			Item item2 = new Item(itemId, oldAmt + item.getAmount());
			player.getEquipment().getItems().set(targetSlot, item2);
			player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
			player.getAppearance().generateAppearanceData();
			player.packets().sendSound(2240, 0, 1);
			if (targetSlot == 3)
			    player.getCombatDefinitions().desecreaseSpecialAttack(0);
			player.getCharges().wear(targetSlot);
			return true;
		    }

		   
		    public static void sendWear(Player player, int[] slotIds) {
		    	if (player.hasFinished() || player.isDead())
		    	    return;
		    	boolean worn = false;
		    	Item[] copy = player.getInventory().getItems().getItemsCopy();
		    	for (int slotId : slotIds) {
		    	    Item item = player.getInventory().getItem(slotId);
		    	    if (item == null)
		    		continue;
		    	    if (InventoryActionHandler.equipItem(player, slotId, item.getId()))
		    		worn = true;
		    	}
		    	player.getInventory().refreshItems(copy);
		    	if (worn) {
		    	    player.getAppearance().generateAppearanceData();
		    	    player.packets().sendSound(2240, 0, 1);
		    	}
		        }
}
