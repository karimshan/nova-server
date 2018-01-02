package org.nova.kshan.content.skills.construction;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.content.Magic;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.randoms.RandomEvent;
import org.nova.utility.loading.playerutils.SFiles;

/**
 * The {@link RandomEvent} that takes place when a person clicks on the house portal
 * 
 * @author K-Shan
 *
 * (Using my RandomEvent class because it's much cleaner and easier to use)
 * 
 */
public class EnterHouse extends RandomEvent {
	
	Player host;

	@Override
	public void startEvent() {
		host = temporaryData.length >= 1 ? (Player) temporaryData[0] : null;
		if(isMyHouse()) {
			if(player.getHouse().hasGuests())
				player.getHouse().addPlayer(player);
			else {
				player.getHouse().setConstructors();
				player.setLocation(player.getHouse().getHouseLocation());
				player.packets().sendWindowsPane(399, 0);
				player.getHouse().enterHouse(player.getHouse().inBuildingMode());
				Game.submit(new GameTick(2.4) {
					int ticks = 0;
					@Override
					public void run() {
						if(ticks == 0) {
							player.getHouse().teleportToHouse();
							player.packets().sendWindowsPane(399, 0);
							setDelay(2.4);
							player.getHouse().getHouseData().refreshSpawnedData();
						} else if(ticks == 1) {
							player.interfaces().sendDefaultPane();
							player.getHouse().setAtHouse(true);
							stop();
						}
						ticks++;
					}
				});		
			}
		} else {
			if(host.hasFinished()
					|| !Game.containsPlayer(host.getUsername()) 
					|| host == null 
					|| host.getHouse().getMapChunks() == null) {
				player.sm(host.getDisplayName()+" has just left "+(player.getAppearance().isMale() ? "his" : "her")+" house");
				player.getRandomEvent().fullyStop(false);
				player.setLocation(player.getHouse().getHouseLocation());
				player.getHouse().setAtHouse(false);
				return;
			} else
				host.getHouse().addPlayer(player);
		}
	}

	@Override
	public Integer getNPCId() {
		return null;
	}

	@Override
	public String[] getNPCMessages() {
		return null;
	}

	@Override
	public boolean checkLogin() {
		if(getData().get("host") != null) {
			player.setLocation(player.getHouse().getHouseLocation());
			return true;
		}
		player.getHouse().setConstructors();
		player.packets().sendWindowsPane(399, 0);
		player.setLocation(player.getHouse().getHouseLocation());
		player.getHouse().enterHouse(player.getHouse().inBuildingMode());
		Game.submit(new GameTick(2.4) {
			int ticks = 0;
			@Override
			public void run() {
				if(ticks == 0) {
					int[] previousLocationArray = (int[]) getData().get("previousLocation");
					if(previousLocationArray == null)
						player.getHouse().teleportToHouse();
					else {
						int offsetX = previousLocationArray[0];
						int offsetY = previousLocationArray[1];
						int z = previousLocationArray[2];
						Location previousLocation = new Location(player.getHouse().getBaseX() + offsetX, 
							player.getHouse().getBaseY() + offsetY, z);
						player.setLocation(previousLocation);
					}
					setDelay(.4);
				} else if(ticks == 1) {
					player.getHouse().getHouseData().refreshSpawnedData();
					setDelay(2.4);
				} else if(ticks == 2) {
					player.interfaces().sendDefaultPane();
					player.getHouse().setAtHouse(true);
					stop();
				}
				ticks++;
			}
		});	
		return false;
	}

	@Override
	public boolean checkLogout() {
		if(isMyHouse()) {
			getData().put("previousLocation", new int[] { player.getHouse().getOffsetX(player.getX()), 
				player.getHouse().getOffsetY(player.getY()), player.getZ() });
			player.getHouse().destroyHouse();
		} else {
			if(host != null)
				getData().put("host", host);
			host.packets().sendMessage(player.getDisplayName()+" has just left your house.");
			if(!isMyHouse()) {
				if(!(host.getX() <= 127) && Game.containsPlayer(host.getUsername()) && 
					host.getHouse().getCachedGuests().size() == 1) {
					host.getHouse().destroyHouse();
					host.getHouse().setLoadedHouse(false);
					SFiles.savePlayer(host);
					player.getRandomEvent().fullyStop(false);
					host.sm("Since "+player.getDisplayName()+" was the last guest in your house, it has been closed.");
				}
			}
			player.getRandomEvent().fullyStop(false);
			player.setCoords(player.getHouse().getHouseLocation());
			host.getHouse().getCachedGuests().remove(player);
			return true;
		}
		return false;
	}

	@Override
	public boolean canTeleport() {
		if(isMyHouse()) {
			final PlayerHouse house = player.getHouse();
			if(!house.hasGuests()) {
				Game.submit(new GameTick(2.4) {
					
					@Override
					public void run() {
						stop();
						player.getRandomEvent().fullyStop(false);
						house.destroyHouse();
						player.sm("You've left your house.");
						player.setCantMove(false);
						player.getHouse().setAtHouse(false);
						return;
					}
					
				});
				return true;
			} else {
				player.getHouse().setAtHouse(false);
				player.getRandomEvent().fullyStop(false);
				player.sm("You leave your house.");
				return true;
			}
		} else {
			Game.submit(new GameTick(.01) {
				
				@Override
				public void run() {
					if(!isMyHouse()) {
						if((!(host.getX() <= 127)) && Game.containsPlayer(host.getUsername()) && 
							host.getHouse().getCachedGuests().size() <= 1) {
							host.getHouse().destroyHouse();
							host.getHouse().setLoadedHouse(false);
							SFiles.savePlayer(host);
							player.getHouse().setAtHouse(false);
							player.sm("You were the last guest in "+host.getDisplayName()+"'s house and it has been closed after you left.");
							host.sm("Since "+player.getDisplayName()+" was the last guest in your house, it has been closed.");
							player.getRandomEvent().fullyStop(false);
						}
					}
					stop();
				}
				
			});
			player.getHouse().setAtHouse(false);
			player.getRandomEvent().fullyStop(false);
			host.getHouse().getCachedGuests().remove(player);
			host.packets().sendMessage(player.getDisplayName()+" has just left your house.");
		}
		return true;
	}

	@Override
	public boolean hasNPCOption1(NPC npc) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean hasObjectOption4(final GlobalObject o) {
		if(player.getRights() > 1) {
			player.sm("Object: "+o.getId()+" - "+o.toString()+" "
				+ "Offsets: ["+(host != null ? host : player).getHouse().getOffsetX(o.getX())+", "+(host != null ? host : player).getHouse().getOffsetY(o.getY())+"] "
				+ "In room: ["+(host != null ? host : player).getHouse().getRoomX(o.getChunkX())+", "+(host != null ? host : player).getHouse().getRoomY(o.getChunkY())+"]");
		}
//		if(o.isGhost())
//			return false;
		if(isMyHouse()) {
			if(!player.getHouse().inBuildingMode()) {
				player.sm("You can only do that in building mode.");
				return true;
			}
			if(player.getHouse().isBuildingObject()) {
				player.sm("Finish constructing your "+((String)
					player.getTemporaryAttributtes().get("tempConObject"))+" before doing that.");
				player.resetWalkSteps();
				return false;
			}
			switch(o.getId()) {
				
				default:
					if(o.getId() == 13405) {
						if(player.getHouse().getPortalCount() == 1)
							player.sm("Your house must contain at least one exit portal.");
						else
							player.getDialogue().start(new RemoveRoomObject(), o);
						return false;
					}
					if(o.defs().containsOption("Build"))
						if(o.getName().toLowerCase().contains("door hotspot"))
							player.getHouse().getConstruction().buildRoom(o);
						else
							player.getHouse().getConstruction().sendBuild(o);
					else
						player.getDialogue().start(new RemoveRoomObject(), o);
					break;
			}
			return true;
		} else
			player.sm("This is not your house.");
		return false;
	}

	@Override
	public boolean hasObjectOption1(GlobalObject obj) {
		HouseConstruction.handleObjectClick(player, obj, host, isMyHouse()); // Had to make static for visitors
		return false;
	}
	
	@Override
	public boolean hasObjectOption2(GlobalObject o) {
		if(player.getRights() > 1)
			player.sm("Object: "+o.getId()+" - "+o.toString()+" "
				+ "Offsets: ["+(host != null ? host : player).getHouse().getOffsetX(o.getX())+", "+(host != null ? host : player).getHouse().getOffsetY(o.getY())+"] "
				+ "In room: ["+(host != null ? host : player).getHouse().getRoomX(o.getChunkX())+", "+(host != null ? host : player).getHouse().getRoomY(o.getChunkY())+"]");
		if(!isMyHouse()) {
			player.sm("This is not your house.");
			return true;
		}
		switch(o.getId()) {
			case 13405:
				player.getHouse().setPrivateHouse(!player.getHouse().isPrivateHouse());
				player.sm(player.getHouse().isPrivateHouse() ? "You have locked your house to further guests." : 
					"You unlocked your house. Guests may now enter.");
				break;
		}
		return true;
	}
	
	@Override
	public void processEvent() { // Gotta have all these checks to avoid bugs :)
		if(!isMyHouse()) {
			if(host.hasFinished() || !Game.containsPlayer(host.getUsername()) || host == null) {
				String username = host.getUsername(); // Only doing this check so that region is destroyed either way.
				host = SFiles.loadPlayer(username);
				host.setUsername(username);
				host.getHouse().destroyHouse();
				host.getHouse().setLoadedHouse(false);
				SFiles.savePlayer(host);
				player.sm(host.getDisplayName()+" has just gone offline.");
				player.getRandomEvent().fullyStop(false);
				player.setLocation(player.getHouse().getHouseLocation());
				player.getHouse().setAtHouse(false);
				return;
			}
			if((!(host.getX() <= 127)) && Game.containsPlayer(host.getUsername()) &&
				host.getHouse().getMapChunks() == null) {
				player.sm(host.getDisplayName()+" has just left his house.");
				player.getRandomEvent().fullyStop(false);
				player.setLocation(player.getHouse().getHouseLocation());
				player.getHouse().setAtHouse(false);
				return;
			}
			if(!(host.getX() <= 127) && host.getRights() > 1 && host.getHouse().getMapChunks() != null) {
				player.sm(host.getDisplayName()+" has teleported away.");
				player.getRandomEvent().fullyStop(false);
				player.setLocation(player.getHouse().getHouseLocation());
				player.getHouse().setAtHouse(false);
				host.sm("You've left your house and "+(host.getHouse().getCachedGuests().size() > 0 ? 
					"all guests have been kicked." : "it has been closed."));
				host.getHouse().getCachedGuests().remove(player);
				host.getHouse().destroyHouse();
				host.getRandomEvent().fullyStop();
				return;
			}
		}
	}
	
	@Override
	public boolean canDropItem(Item item) {
		if(isMyHouse() && player.getHouse().inBuildingMode()) {
			player.sm("You cannot drop items while in building mode.");
			return false;
		}
		return true;
	}
	
	@Override
	public void processMovement() {
		if(player.getTemporaryAttributtes().get("viewingRoom") != null) { // To remove the view room objects if the player moves
			RoomChunk room = (RoomChunk) player.getTemporaryAttributtes().get("viewingRoom");
			if(room != null)
				player.getHouse().viewRoom(room, true);
			player.getTemporaryAttributtes().remove("viewingRoom");
			player.getTemporaryAttributtes().remove("viewingObjects");
		}
	}

	@Override
	public boolean canAttackEntity(Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canClickButtons(int interfaceId, int buttonId, int slotId, int packetId) {
		if(isMyHouse()) {
			switch(interfaceId) {
			
			case 394: // Build options menu
			case 396:
				player.getHouse().getConstruction().handleBuildButtons(interfaceId, slotId);
				break;
				
			case 402: // Room creation menu
				player.getHouse().getConstruction().handleRoomCreationButtons(buttonId);
				break;
			
			case 261:
				if(buttonId == 8) { // Opens up house options and shows room count
					player.interfaces().sendTab(player.interfaces().isFullScreen() ? 102 : 216, 398);
					player.getHouse().refreshConfigs();
					return false;
				}
				return true;
			case 398:
				switch(buttonId) {
					case 19: // The X button
						player.interfaces().sendSettings();
						break;
					case 29: // Leave house
						if(player.getHouse().isBuildingObject()) {
							player.sm("Finish constructing your "+((String)
								player.getTemporaryAttributtes().get("tempConObject"))+" before doing this.");
							break;
						}
						if(player.getTemporaryAttributtes().get("viewingRoom") != null)
							break;
						Magic.telePlayer(player, player.getHouse().getHouseLocation(), true);
						break;
					case 27: // Expel guests
						if(player.getTemporaryAttributtes().get("viewingRoom") != null)
							break;
						if(player.getHouse().isBuildingObject()) {
							player.sm("Finish constructing your "+((String)
								player.getTemporaryAttributtes().get("tempConObject"))+" before doing this.");
							break;
						}
						player.getDialogue().start("ExpelGuests");
						break;
					case 15: // Toggle build mode on
						long current = System.currentTimeMillis();
						if(player.getHouse().hasGuests()) {
							player.sm("You can't switch between building modes with guests at home!");
							break;
						}
						if(player.getHouse().inBuildingMode()) {
							player.sm("Building mode is already on.");
							break;
						}
						if(player.getTemporaryAttributtes().get("viewingRoom") != null)
							break;
						if(player.interfaces().containsScreenInter()) {
							player.sm("Please close the interface you have open before doing this.");
							break;
						}
						if(player.getHouse().isBuildingObject()) {
							player.sm("Finish constructing your "+((String)
								player.getTemporaryAttributtes().get("tempConObject"))+" before doing this.");
							break;
						}
						player.sm("Building mode is now on.");
						player.getHouse().setInBuildingMode(true);
						player.getHouse().refreshConfigs();
						if(player.getRights() < 2)
							player.packets().sendWindowsPane(399, 0);
						player.getHouse().getHouseData().refreshDataNoDelay();
						if(player.getRights() < 2)
							Game.submit(new GameTick(.5) {

								@Override
								public void run() {
									stop();
									player.interfaces().sendDefaultPane();
								}
							
							});
						if(player.getRights() > 1)
							player.sm("Took "+((double) (System.currentTimeMillis() - current) / 1000)+" seconds to refresh the build mode.");
						break;
					case 1: // Toggle build mode off
						current = System.currentTimeMillis();
						if(player.getHouse().hasGuests()) {
							player.sm("You can't switch between building modes with guests at home!");
							break;
						}
						if(!player.getHouse().inBuildingMode()) {
							player.sm("Building mode is already off.");
							break;
						}
						if(player.getTemporaryAttributtes().get("viewingRoom") != null)
							break;
						if(player.interfaces().containsScreenInter()) {
							player.sm("Please close the interface you have open before doing this.");
							break;
						}
						if(player.getHouse().isBuildingObject()) {
							player.sm("Finish constructing your "+((String)
								player.getTemporaryAttributtes().get("tempConObject"))+" before doing this.");
							break;
						}
						player.sm("Building mode is now off.");
						player.getHouse().setInBuildingMode(false);
						if(player.getRights() < 2)
							player.packets().sendWindowsPane(399, 0);
						player.getHouse().getHouseData().refreshDataNoDelay();
						if(player.getRights() < 2)
							Game.submit(new GameTick(.5) {

								@Override
								public void run() {
									stop();
									player.interfaces().sendDefaultPane();
								}
							
							});
						if(player.getRights() > 1)
							player.sm("Took "+((double) (System.currentTimeMillis() - current) / 1000)+" seconds to refresh the build mode.");
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
				return false;
			}
		}
		return true;
	}

	@Override
	public Dialogue getDialogue() {
		return null;
	}

	@Override
	public Location getEventLocation() {
		return null;
	}

	@Override
	public boolean hasHiddenMiniMap() {
		return false;
	}

	@Override
	public Integer[] getTabsRemoved() {
		return null;
	}

	@Override
	public boolean isTraditional() {
		return false;
	}

	@Override
	public Integer[] getDynamicMapVariables() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean forceStop() {
		return false;
	}
	
	private boolean isMyHouse() {
		return host == null;
	}

}
