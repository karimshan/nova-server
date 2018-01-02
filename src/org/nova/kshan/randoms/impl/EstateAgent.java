package org.nova.kshan.randoms.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.entity.Entity;
import org.nova.game.item.FloorItem;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.PlayerFollow;
import org.nova.game.player.content.Magic;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.randoms.RandomEvent;

/**
 * 
 * @author K-Shan
 *
 */
public class EstateAgent extends RandomEvent {
	
	NPC estateAgent;
	List<FloorItem> materials;
	List<GlobalObject> chairs;
	private int minsRemaining;
	private int secRemaining;
	private double rewardRemaining;
	private byte chairsFinished;
	List<GlobalObject> finishedChairs;

	@Override
	public void startEvent() {
		estateAgent = new NPC(getNPCId(), new Location(baseX + 11, baseY + 4, 0), false);
		getPlayer().getDialogue().start(getDialogue());
		estateAgent.face(player);
	}
	
	@Override
	public Integer getNPCId() {
		return 4247; // Estate Agent
	}

	@Override
	public String[] getNPCMessages() {
		return new String[] { getPlayer().getDisplayName()+"! I require your assistance.", 
			"I need your help building these chairs!", "Ah, you'll do, "+getPlayer().getDisplayName()+"!" };
	}

	@Override
	public boolean canTeleport() {
		if(interactionStage < 3)
			getPlayer().sm("You cannot do that at the moment.");
		else {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkLogin() {
		if(interactionStage == 0) {
			player.setLocation(getEventLocation());
			Game.submit(new GameTick(0.5) { // Small delay so npc faces player or else the npc won't lol
			
				@Override
				public void run() {
					startEvent();
					stop();
				}
				
			});
		} else if(interactionStage == 1) {
			Game.submit(new GameTick(1) {
				int ticks;
				@Override
				public void run() {
					if(player.hasFinished() || !Game.containsPlayer(player.getUsername()) 
							|| player.getRandomEvent().getCurrent() == null) {
						stop();
						player.packets().sendResetCamera();
						player.packets().sendRemoveOverlay();
						return;
					}
					if(isStopped())
						return;
					else
						ticks++;
					if(ticks == 1) {
						npcDialogue(getNPCId(), -1, "Estate Agent", new String[] { 
						"The materials that you need can be found right here."});
					materials = (List<FloorItem>) getData().get("materials");
					for(FloorItem items : materials)
						Game.addGroundItem(items, items.getLocation());
					player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 19), 
							Game.getCutsceneY(player, (baseY + 20 - 3)), 2000, 10, 10);
					player.packets().sendCameraLook(Game.getCutsceneX(player, baseX + 22), 
							Game.getCutsceneY(player, (baseY + 20 - 7)), 0, 10, 10);
					} else if(ticks == 6) {
						interactionStage = 2;
						getPlayer().packets().sendResetCamera();
						stop();
						player.setCantMove(false);
						player.unlock();
						
						if(Game.getRegion(player.getRegionId(), true).getSpawnedObjects() != null)
							Game.getRegion(player.getRegionId(), true).getSpawnedObjects().clear();
						finishedChairs = new ArrayList<GlobalObject>();
						finishedChairs.add(Game.getRegion(player.getRegionId(), true).getObject(26076, 
							new Location(baseX + 11, baseY + 13, 0)));
						finishedChairs.get(0).setCoords(new Location(baseX + 11, baseY + 13, 0));
						finishedChairs.add(Game.getRegion(player.getRegionId(), true).getObject(26075, 
							new Location(baseX + 11, baseY + 15, 0)));
						finishedChairs.get(1).setCoords(new Location(baseX + 11, baseY + 15, 0));
						finishedChairs.add(Game.getRegion(player.getRegionId(), true).getObject(26075, 
							new Location(baseX + 13, baseY + 11, 0)));
						finishedChairs.get(2).setCoords(new Location(baseX + 13, baseY + 11, 0));
						finishedChairs.add(Game.getRegion(player.getRegionId(), true).getObject(26076, 
							new Location(baseX + 15, baseY + 11, 0)));
						finishedChairs.get(3).setCoords(new Location(baseX + 15, baseY + 11, 0));
						finishedChairs.add(Game.getRegion(player.getRegionId(), true).getObject(26075, 
							new Location(baseX + 17, baseY + 13, 0)));
						finishedChairs.get(4).setCoords(new Location(baseX + 17, baseY + 13, 0));
						finishedChairs.add(Game.getRegion(player.getRegionId(), true).getObject(26076, 
							new Location(baseX + 17, baseY + 15, 0)));
						finishedChairs.get(5).setCoords(new Location(baseX + 17, baseY + 15, 0));
						finishedChairs.add(Game.getRegion(player.getRegionId(), true).getObject(26075, 
							new Location(baseX + 15, baseY + 17, 0)));
						finishedChairs.get(6).setCoords(new Location(baseX + 15, baseY + 17, 0));
						finishedChairs.add(Game.getRegion(player.getRegionId(), true).getObject(26076, 
							new Location(baseX + 13, baseY + 17, 0)));
						finishedChairs.get(7).setCoords(new Location(baseX + 13, baseY + 17, 0));
						getData().put("finishedChairs", finishedChairs);
			
						chairs = new ArrayList<GlobalObject>(8);
						chairs.add(new GlobalObject(15410, 10, 3, 
								new Location(baseX + 11, baseY + 13, 0)));
						chairs.add(new GlobalObject(15410, 10, 3, 
								new Location(baseX + 11, baseY + 15, 0)));
						chairs.add(new GlobalObject(15410, 10, 2, 
								new Location(baseX + 13, baseY + 11, 0)));
						chairs.add(new GlobalObject(15410, 10, 2, 
								new Location(baseX + 15, baseY + 11, 0)));
						chairs.add(new GlobalObject(15410, 10, 1, 
								new Location(baseX + 17, baseY + 13, 0)));
						chairs.add(new GlobalObject(15410, 10, 1, 
								new Location(baseX + 17, baseY + 15, 0)));
						chairs.add(new GlobalObject(15410, 10, 4, 
								new Location(baseX + 15, baseY + 17, 0)));
						chairs.add(new GlobalObject(15410, 10, 4, 
								new Location(baseX + 13, baseY + 17, 0)));
						for(GlobalObject o : chairs)
							Game.spawnReplacedObject(o, true);
						getData().put("chairs", chairs);
						player.packets().sendString("<col=00ff00>Time Left: 5:00<br><col=00ff00>Chairs: "
								+ "0/8<br>"+ "<col=00ff00>Reward left: 100%", 532, 0);
						player.packets().sendString("", 532, 1);
						player.packets().sendOverlay(532);
						getData().put("minsRemaining", 5);
						getData().put("secRemaining", 0);
						getData().put("rewardRemaining", 100.0);
						minsRemaining = (int) getData().get("minsRemaining");
						secRemaining = (int) getData().get("secRemaining");
						rewardRemaining = (double) getData().get("rewardRemaining");
						Game.submit(player, gameTimer);
						npcDialogue(4247, .1, "Estate Agent", new String[] { 
								"Time starts.... Now!", "Good luck, and beat the clock!" });
						if(player.getInventory().containsItems()) {
							for(Item item : player.getInventory().getItems().getItems())
								if(item != null)
									player.getBank().addItem(item.getId(), item.getAmount(), true);
							player.sm("You had items in your inventory. They have been put in your bank.");
							player.getInventory().reset();
						}
					}
				}
			});
		} else if(interactionStage == 2) {
			player.packets().sendString("", 532, 1);
			player.packets().sendOverlay(532);
			finishedChairs = (List<GlobalObject>) getData().get("finishedChairs");
			minsRemaining = (int) getData().get("minsRemaining");
			secRemaining = (int) getData().get("secRemaining");
			rewardRemaining = (double) getData().get("rewardRemaining");
			chairsFinished = (byte) getData().get("chairsFinished");
			materials = (List<FloorItem>) getData().get("materials");
			if(!materials.isEmpty())
				for(FloorItem items : materials)
					Game.addGroundItem(items, items.getLocation());
			Game.submit(player, gameTimer);
			chairs = (List<GlobalObject>) getData().get("chairs");
			if(chairs != null && !chairs.isEmpty())
				for(GlobalObject o : chairs)
					Game.spawnReplacedObject(o, true);
		} else if(interactionStage == 3) {
			player.getDialogue().start("EntityDialogue", 4247, (Object) new String[] { 
				"Well done, you made all of the chairs!", 
				"My client is going to be very pleased, thanks to you!",
				"You may now leave through the portal."
			});
			Game.spawnReplacedObject(new GlobalObject(2156, 10, 1, new Location(baseX + 17, baseY + 19, player.getZ())), true);
		}
		return false;
	}

	@Override
	public boolean checkLogout() {
		if(interactionStage == 1)
			getData().put("materials", materials);
		else if(interactionStage == 2) {
			getData().put("minsRemaining", minsRemaining);
			getData().put("secRemaining", secRemaining);
			getData().put("rewardRemaining", rewardRemaining);
			getData().put("chairsFinished", chairsFinished);
			if(finishedChairs != null && !finishedChairs.isEmpty())
				getData().put("finishedChairs", finishedChairs);
			if(materials != null && !materials.isEmpty())
				getData().put("materials", materials);
			if(chairs != null && !chairs.isEmpty())
				getData().put("chairs", chairs);
		}
		return false;
	}
	

	@Override
	public boolean hasNPCOption1(NPC npc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasObjectOption1(GlobalObject obj) {
		if(obj.getId() == 2156) {
			Magic.telePlayer(getPlayer(), getOriginalLocation(), false);
			Game.submit(new GameTick(2.5) {
				public void run() {
					getPlayer().getRandomEvent().fullyStop();
					stop();
				}
			});
			return true;
		}
		return false;
	}

	@Override
	public boolean canAttackEntity(Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean canClickButtons(int interfaceId, int buttonId, int slotId,
			int packetId) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean canDropItem(Item item) {
		getPlayer().sm("You cannot do that at the moment.");
		return false;
	}
	
	@Override
	public boolean canPickupItem(Item item) {
		for(FloorItem fItem : materials)
			if(fItem != null) {
				if(fItem.getId() == item.getId()) {
					materials.remove(fItem);
					updateSavedVariables();
					return true;
				}
			}
		return true;
	}
	
	@Override
	public boolean hasObjectOption4(final GlobalObject object) {
		if(object.getId() == 15410) {
			getPlayer().getDialogue().start(new Dialogue() {
				
				@Override
				public void start() {
					if(!checkRequirements()) {
						end();
						sendLines(false, "You do not have the materials needed to make this chair.");
						return;
					}
					player.packets().sendChatBoxInterface(131);
					player.packets().sendItemOnIComponent(131, 0, 8782, 1);
					player.packets().sendItemOnIComponent(131, 2, 2347, 1);
					player.packets().sendString("You use your knowledge in construction...", 131, 1);
				}
				
				@Override
				public void process(int i, int b) {
					if(stage == 0) {
						end();
						player.animate(898);
						player.getSkills().addRSXP(Skills.CONSTRUCTION, 5000);
						player.getInventory().deleteItem(8782, 1);
						player.getInventory().deleteItem(6822, 1);
						sendLines(false, "...and successfully make an armchair.");
						chairs.remove(object);
						for(GlobalObject o : finishedChairs)
							if(o != null)
								if(o.matches(object))
									Game.spawnReplacedObject(o, true);
						chairsFinished += 1;
						updateSavedVariables();
					}
				}
				
				@Override
				public void finish() { 
					
				}
				
				private boolean checkRequirements() {
					if(player.getInventory().containsItems(8782, 2347, 8794, 6822))
						return true;
					return false;
				}
				
			});
		}
		return true;
	}
	

	@Override
	public Dialogue getDialogue() {
		return new Dialogue() {
			public void start() {
				getPlayer().lock();
				getPlayer().setCantMove(true);
				getPlayer().packets().sendCameraPos(Game.getCutsceneX(getPlayer(), baseX + 11), 
						Game.getCutsceneY(getPlayer(), baseY + 2), 1200);
				getPlayer().packets().sendCameraLook(Game.getCutsceneX(getPlayer(), baseX + 11), 
						Game.getCutsceneY(getPlayer(), baseY + 5), 0);
				estateAgent.resetMasks();
				estateAgent.resetCombat();
				estateAgent.face(player);
				estateAgent.resetWalkSteps();
				npcDialogue(getNPCId(), 2, "Estate Agent", new String[] { "There you are, "+getPlayer().getDisplayName()+"!" });
				estateAgent.face(player);
			}
			public void process(int interfaceId, int buttonId) {
				if(stage == 0) {
					npcDialogue(getNPCId(), 2, "Estate Agent", new String[] { 
						"I need you to help me build chairs", "for a very picky client of mine.", 
						"She's been talking about how she needs specific chairs", "for a birthday party." });
					stage = 1;
					estateAgent.face(player);
				} else if(stage == 1) {
					getPlayer().packets().sendCameraPos(Game.getCutsceneX(getPlayer(), baseX + 12), 
							Game.getCutsceneY(getPlayer(), baseY), 2400);
					getPlayer().packets().sendCameraLook(Game.getCutsceneX(getPlayer(), baseX + 10), 
							Game.getCutsceneY(getPlayer(), baseY + 3), 0);
					getPlayer().face(estateAgent);
					playerDialogue(new String[] { 
						"Oh, I didn't see you there, agent.", "And yeah, I'd be glad to help!"
					}, 2);
					stage = 2;
				} else if(stage == 2) {
					npcDialogue(getNPCId(), 2, "Estate Agent", new String[] { 
						"Excellent, follow me for further instructions." });
					stage = 3;
				} else if(stage == 3) {
					end();
					getPlayer().interfaces().closeChatBoxInterface();
					player.setRun(false);
					estateAgent.resetWalkSteps();
					estateAgent.resetMasks();
					estateAgent.setNextFaceEntity(null);
					Game.submit(new GameTick(1.0) {
						int ticks = 0;
						boolean set;
						public void run() {
							if(player.hasFinished() || !Game.containsPlayer(player.getUsername()) 
									|| player.getRandomEvent().getCurrent() == null) {
								stop();
								player.packets().sendResetCamera();
								player.packets().sendRemoveOverlay();
								return;
							}
							if(isStopped())
								return;
							else
								ticks++;
							if(ticks >= 1 && ticks < 6) {
								getPlayer().packets().sendCameraPos(Game.getCutsceneX(getPlayer(), getPlayer().getX() + 6), 
										Game.getCutsceneY(getPlayer(), getPlayer().getY() + 2), 2750, 9, 9);
								getPlayer().packets().sendCameraLook(Game.getCutsceneX(getPlayer(), player.getX() - 2), 
										Game.getCutsceneY(getPlayer(), player.getY() + 2), 0, 16, 16);
								if(!set) {
									player.getActionManager().setAction(new PlayerFollow(estateAgent));
									set = true;
								}
								estateAgent.addWalkSteps(estateAgent.getX() + 2, estateAgent.getY() + 3);
							} else if(ticks == 6) {
								estateAgent.face(getPlayer());
								estateAgent.forceTalk("Right through this door.");
							} else if(ticks == 8) {
								GlobalObject toFace = new GlobalObject(1, 0, 10, baseX + 13, baseY + 11, 0);
								estateAgent.faceObject(toFace);
							} else if(ticks == 9) {
								GlobalObject o = Game.getRegion(player.getRegionId()).getObject(25638, 
										new Location(baseX + 13, baseY + 7, 0));
								GlobalObject replace = new GlobalObject(o.getId(), o);
								o.setCoords(new Location(baseX + 13, baseY + 7, 0));
								replace.setCoords(new Location(baseX + 13, baseY + 7, 0));
								replace.setRotation(0);
								replace.moveLocation(0, 1, 0);
								player.packets().sendSpawnedObject(o);
								player.packets().sendSpawnedObject(replace);
								Game.removeTemporaryObject(o, 3000, true);
								Game.spawnTemporaryObject(replace, 3000, true);
								estateAgent.resetWalkSteps();
								estateAgent.addWalkSteps(estateAgent.getX(), estateAgent.getY() + 2);
							} else if(ticks == 13) {
								player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 13), 
										Game.getCutsceneY(player, baseY + 21), 2800, 12, 12);
								player.packets().sendCameraLook(Game.getCutsceneX(player, player.getX()), 
										Game.getCutsceneY(player, player.getY()), 0, 12, 12);
								npcDialogue(getNPCId(), -1, "Estate Agent", new String[] { 
									"This is how the chairs are set up.", 
									"There are a total of 8 chairs that need to be made and", 
									"you will be timed on how fast you complete this task.",
									"If you don't finish within the given time, you won't<br> be given the good portion of reward."});
							} else if(ticks == 19) {
								stop();
								player.interfaces().closeChatBoxInterface();
								player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 14), 
										Game.getCutsceneY(player, baseY + 20), 2800, 20, 20);
								player.packets().sendCameraLook(Game.getCutsceneX(player, baseX + 14), 
										Game.getCutsceneY(player, baseY + 14), 0, 20, 20);
								Game.submit(new GameTick(2) {
									int tick = 0;
									@Override
									public void run() {
										if(player.hasFinished() || !Game.containsPlayer(player.getUsername()) 
												|| player.getRandomEvent().getCurrent() == null) {
											stop();
											player.packets().sendResetCamera();
											player.packets().sendRemoveOverlay();
											return;
										}
										if(isStopped())
											return;
										else
											tick++;
										if(tick == 1) {
											npcDialogue(getNPCId(), -1, "Estate Agent", new String[] { 
												"The birthday party for my client will require very fancy", 
												"chairs made from expensive materials that will be",
												"provided for you."});
											player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 12), 
												Game.getCutsceneY(player, baseY + 20), 1100, 11, 11);
											player.packets().sendCameraLook(Game.getCutsceneX(player, baseX + 14), 
												Game.getCutsceneY(player, (baseY + 20 - 6)), 0, 11, 11);
										} else if(tick == 2) {
											player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 8), 
													Game.getCutsceneY(player, (baseY + 20 - 4)), 1100, 11, 11);
											player.packets().sendCameraLook(Game.getCutsceneX(player, baseX + 14), 
													Game.getCutsceneY(player, (baseY + 20 - 6)), 0, 11, 11);
										} else if(tick == 3) {
											player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 8), 
													Game.getCutsceneY(player, (baseY + 20 - 8)), 1100, 11, 11);
											player.packets().sendCameraLook(Game.getCutsceneX(player, baseX + 14), 
													Game.getCutsceneY(player, (baseY + 20 - 6)), 0, 11, 11);
										} else if(tick == 4) {
											player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 12), 
													Game.getCutsceneY(player, (baseY + 20 - 12)), 1100, 11, 11);
											player.packets().sendCameraLook(Game.getCutsceneX(player, baseX + 14), 
													Game.getCutsceneY(player, (baseY + 20 - 6)), 0, 11, 11);
										} else if(tick == 5) {
											player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 16), 
													Game.getCutsceneY(player, (baseY + 20 - 12)), 1100, 11, 11);
											player.packets().sendCameraLook(Game.getCutsceneX(player, baseX + 14), 
													Game.getCutsceneY(player, (baseY + 20 - 6)), 0, 11, 11);
										} else if(tick == 6) {
											player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 20), 
													Game.getCutsceneY(player, (baseY + 20 - 8)), 1100, 11, 11);
											player.packets().sendCameraLook(Game.getCutsceneX(player, baseX + 14), 
													Game.getCutsceneY(player, (baseY + 20 - 6)), 0, 11, 11);
										} else if(tick == 7) {
											player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 20), 
													Game.getCutsceneY(player, (baseY + 20 - 4)), 1100, 11, 11);
											player.packets().sendCameraLook(Game.getCutsceneX(player, baseX + 14), 
													Game.getCutsceneY(player, (baseY + 20 - 6)), 0, 11, 11);
										} else if(tick == 8) {
											player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 16), 
													Game.getCutsceneY(player, (baseY + 20)), 1100, 11, 11);
											player.packets().sendCameraLook(Game.getCutsceneX(player, baseX + 14), 
													Game.getCutsceneY(player, (baseY + 20 - 6)), 0, 11, 11);
										} else if(tick == 10) {
											interactionStage = 1;
											npcDialogue(getNPCId(), -1, "Estate Agent", new String[] { 
												"The materials that you need can be found right here."});
											materials = new ArrayList<FloorItem>(18);
											materials.add(new FloorItem(new Item(8782, 1), new Location(baseX + 22, 
													baseY + 14, 0), player, false, false));
											materials.add(new FloorItem(new Item(8782, 1), new Location(baseX + 22,
													baseY + 14, 0), player, false, false));
											materials.add(new FloorItem(new Item(8782, 1), new Location(baseX + 22, 
													baseY + 14, 0), player, false, false));
											materials.add(new FloorItem(new Item(8782, 1), new Location(baseX + 22, 
													baseY + 14, 0), player, false, false));
											materials.add(new FloorItem(new Item(8782, 1), new Location(baseX + 22, 
													baseY + 14, 0), player, false, false));
											materials.add(new FloorItem(new Item(8782, 1), new Location(baseX + 22,
													baseY + 14, 0), player, false, false));
											materials.add(new FloorItem(new Item(8782, 1), new Location(baseX + 22, 
													baseY + 14, 0), player, false, false));
											materials.add(new FloorItem(new Item(8782, 1), new Location(baseX + 22, 
													baseY + 14, 0), player, false, false));
											materials.add(new FloorItem(new Item(2347, 1), new Location(baseX + 22, 
													baseY + 15, 0), player, false, false));
											materials.add(new FloorItem(new Item(8794, 1), new Location(baseX + 21, 
													baseY + 15, 0), player, false, false));
											materials.add(new FloorItem(new Item(6822, 1), new Location(baseX + 22, 
													baseY + 13, 0), player, false, false));
											materials.add(new FloorItem(new Item(6822, 1), new Location(baseX + 22, 
													baseY + 13, 0), player, false, false));
											materials.add(new FloorItem(new Item(6822, 1), new Location(baseX + 22, 
													baseY + 13, 0), player, false, false));
											materials.add(new FloorItem(new Item(6822, 1), new Location(baseX + 22, 
													baseY + 13, 0), player, false, false));
											materials.add(new FloorItem(new Item(6822, 1), new Location(baseX + 22, 
													baseY + 13, 0), player, false, false));
											materials.add(new FloorItem(new Item(6822, 1), new Location(baseX + 22, 
													baseY + 13, 0), player, false, false));
											materials.add(new FloorItem(new Item(6822, 1), new Location(baseX + 22, 
													baseY + 13, 0), player, false, false));
											materials.add(new FloorItem(new Item(6822, 1), new Location(baseX + 22, 
													baseY + 13, 0), player, false, false));
											for(FloorItem items : materials)
												Game.addGroundItem(items, items.getLocation());
											player.packets().sendCameraPos(Game.getCutsceneX(player, baseX + 19), 
													Game.getCutsceneY(player, (baseY + 20 - 3)), 2000, 10, 10);
											player.packets().sendCameraLook(Game.getCutsceneX(player, baseX + 22), 
													Game.getCutsceneY(player, (baseY + 20 - 7)), 0, 10, 10);
											getData().put("materials", materials);
										} else if(tick == 13) {
											interactionStage = 2;
											getPlayer().packets().sendResetCamera();
											stop();
											player.setCantMove(false);
											player.unlock();
											
											finishedChairs = new ArrayList<GlobalObject>();
											finishedChairs.add(Game.getRegion(player.getRegionId()).getObject(26076, 
												new Location(baseX + 11, baseY + 13, 0)));
											finishedChairs.get(0).setCoords(new Location(baseX + 11, baseY + 13, 0));
											finishedChairs.add(Game.getRegion(player.getRegionId()).getObject(26075, 
												new Location(baseX + 11, baseY + 15, 0)));
											finishedChairs.get(1).setCoords(new Location(baseX + 11, baseY + 15, 0));
											finishedChairs.add(Game.getRegion(player.getRegionId()).getObject(26075, 
												new Location(baseX + 13, baseY + 11, 0)));
											finishedChairs.get(2).setCoords(new Location(baseX + 13, baseY + 11, 0));
											finishedChairs.add(Game.getRegion(player.getRegionId()).getObject(26076, 
												new Location(baseX + 15, baseY + 11, 0)));
											finishedChairs.get(3).setCoords(new Location(baseX + 15, baseY + 11, 0));
											finishedChairs.add(Game.getRegion(player.getRegionId()).getObject(26075, 
												new Location(baseX + 17, baseY + 13, 0)));
											finishedChairs.get(4).setCoords(new Location(baseX + 17, baseY + 13, 0));
											finishedChairs.add(Game.getRegion(player.getRegionId()).getObject(26076, 
												new Location(baseX + 17, baseY + 15, 0)));
											finishedChairs.get(5).setCoords(new Location(baseX + 17, baseY + 15, 0));
											finishedChairs.add(Game.getRegion(player.getRegionId()).getObject(26075, 
												new Location(baseX + 15, baseY + 17, 0)));
											finishedChairs.get(6).setCoords(new Location(baseX + 15, baseY + 17, 0));
											finishedChairs.add(Game.getRegion(player.getRegionId()).getObject(26076, 
												new Location(baseX + 13, baseY + 17, 0)));
											finishedChairs.get(7).setCoords(new Location(baseX + 13, baseY + 17, 0));
											getData().put("finishedChairs", finishedChairs);
											
											chairs = new ArrayList<GlobalObject>(8);
											chairs.add(new GlobalObject(15410, 10, 3, 
													new Location(baseX + 11, baseY + 13, 0)));
											chairs.add(new GlobalObject(15410, 10, 3, 
													new Location(baseX + 11, baseY + 15, 0)));
											chairs.add(new GlobalObject(15410, 10, 2, 
													new Location(baseX + 13, baseY + 11, 0)));
											chairs.add(new GlobalObject(15410, 10, 2, 
													new Location(baseX + 15, baseY + 11, 0)));
											chairs.add(new GlobalObject(15410, 10, 1, 
													new Location(baseX + 17, baseY + 13, 0)));
											chairs.add(new GlobalObject(15410, 10, 1, 
													new Location(baseX + 17, baseY + 15, 0)));
											chairs.add(new GlobalObject(15410, 10, 4, 
													new Location(baseX + 15, baseY + 17, 0)));
											chairs.add(new GlobalObject(15410, 10, 4, 
													new Location(baseX + 13, baseY + 17, 0)));
											for(GlobalObject o : chairs)
												Game.spawnReplacedObject(o, true);
											getData().put("chairs", chairs);
											player.packets().sendString("<col=00ff00>Time Left: 5:00<br><col=00ff00>Chairs: "
													+ "0/8<br>"+ "<col=00ff00>Reward left: 100%", 532, 0);
											player.packets().sendString("", 532, 1);
											player.packets().sendOverlay(532);
											getData().put("minsRemaining", 5);
											getData().put("secRemaining", 0);
											getData().put("rewardRemaining", 100.0);
											minsRemaining = (int) getData().get("minsRemaining");
											secRemaining = (int) getData().get("secRemaining");
											rewardRemaining = (double) getData().get("rewardRemaining");
											Game.submit(player, gameTimer);
											npcDialogue(4247, .1, "Estate Agent", new String[] { 
													"Time starts.... Now!", "Good luck, and beat the clock!" });
											estateAgent.finish();
											if(player.getInventory().containsItems()) {
												for(Item item : player.getInventory().getItems().getItems())
													if(item != null)
														player.getBank().addItem(item.getId(), item.getAmount(), true);
												player.sm("You had items in your inventory. They have been put in your bank.");
												player.getInventory().reset();
											}
										}
									}
								});
							}
						}
					});
				}
			}
			public void finish() { }
		};
	}
	
	GameTick gameTimer = new GameTick("Estate Agent", 1.0) {
		
		boolean receivedMessage = false;
		
		@Override
		public void run() {
			if(secRemaining == 0 && minsRemaining > 0) {
				minsRemaining -= 1;
				secRemaining = 60;
			}
			if(secRemaining > 0)
				secRemaining--;
			rewardRemaining -= (100.0 / (5 * 60));
			String secs = secRemaining == 60 ? "00" : secRemaining < 10 ? "0"+secRemaining : ""+secRemaining;
			String rewardRemain = new DecimalFormat("#.##").format(rewardRemaining <= 0 ? 0 : rewardRemaining);
			String color = minsRemaining == 2 && secRemaining <= 30 ? "ffff00" : minsRemaining < 2 ? "ff0000" 
				: (minsRemaining == 2 && secRemaining > 0) 
						|| (minsRemaining == 1 && secRemaining > 15) ? "ffff00": "00ff00";
			getPlayer().packets().sendString("<col="+color+">Time Left: "+minsRemaining+":"+secs+"<br><col="+color+">Chairs: "
				+ ""+chairsFinished+"/8<br>"+ "<col="+color+">Reward left: "+rewardRemain+"%", 532, 0);
			getPlayer().packets().sendString("", 532, 1);
			if(!receivedMessage && minsRemaining == 0 && secRemaining == 0 && chairsFinished < 8) {
				receivedMessage = true;
				getData().put("partialReward", true);
				player.sm("You will no longer receive the good portion of the reward.");
			}
			if(chairsFinished == 8) {
				stop();
				player.packets().sendString("<col=00ffff>Time Left: COMPLETE<br><col=00ffff>"
					+ "Chairs: COMPLETE<br><col=00ffff>Reward left: "+rewardRemain+"%", 532, 0);
				Game.submit(new GameTick(3) {
					
					@Override
					public void run() {
						if(player.interfaces().containsChatBoxInter())
							player.interfaces().closeChatBoxInterface();
						interactionStage = 3;
						player.packets().sendRemoveOverlay();
						player.getDialogue().start("EntityDialogue", 4247, (Object) new String[] { 
							"Well done, you made all of the chairs!", 
							"My client is going to be very pleased, thanks to you!",
							"You may now leave through the portal."
						});
						Game.spawnReplacedObject(new GlobalObject(2156, 10, 1, new Location(baseX + 17, baseY + 19, player.getZ())), true);
						stop();
					}
					
				});
			}
		}
		
	};

	@Override
	public Location getEventLocation() {
		return new Location(baseX + 10, baseY + 1, 0);
	}

	@Override
	public boolean hasHiddenMiniMap() {
		return true;
	}
	
	@Override
	public boolean forceStop() {
		if(player.getGameTickFromCache("Estate Agent") != null && !player.getGameTickFromCache("Estate Agent").isStopped())
			player.getGameTickFromCache("Estate Agent").stop();
		if(estateAgent != null)
			estateAgent.finish();
		return true;
	}

	@Override
	public Integer[] getTabsRemoved() {
		return selectTabs();
	}

	@Override
	public boolean isTraditional() {
		return true;
	}

	@Override
	public Integer[] getDynamicMapVariables() {
		return new Integer[] { 343, 437, 4, 3 };
	}
	
	private void updateSavedVariables() {
		getData().put("minsRemaining", minsRemaining);
		getData().put("secRemaining", secRemaining);
		getData().put("rewardRemaining", rewardRemaining);
		getData().put("chairsFinished", chairsFinished);
		if(materials != null && !materials.isEmpty())
			getData().put("materials", materials);
		if(chairs != null && !chairs.isEmpty())
			getData().put("chairs", chairs);
	}

}
