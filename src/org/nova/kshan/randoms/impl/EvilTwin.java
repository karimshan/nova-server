package org.nova.kshan.randoms.impl;

import java.util.ArrayList;
import java.util.List;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.entity.Entity;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.ForceMovement;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.randoms.RandomEvent;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class EvilTwin extends RandomEvent {
	
	NPC molly;
	
	List<NPC> mollys = new ArrayList<NPC>();
	
	int[][] mollyIds = { 
		{ 3896, 3900, 3904, 3908, 4277 }, 
		{ 3897, 3901, 3905, 3909, 7445 }, 
		{ 3898, 3902, 3906, 3910, 7446 }, 
		{ 3899, 3903, 3907, 3911, 8636 } 
	};
	
	final int randomIndex = Misc.random(4);
	final int randomMolly = Misc.random(5);
	
	GlobalObject claw;
	GlobalObject target;
	
	boolean spawnedClaw;
	boolean failedTask;
	
	int currentCamLookX;
	int tries = 0;

	@Override
	public void startEvent() {
		molly = new NPC(getNPCId(), new Location(baseX + 4, baseY + 15, 0), false);
		molly.face(player);
		player.getDialogue().start(getDialogue());
		spawnMollys();
	}
	
	@Override
	public Integer getNPCId() {
		return mollyIds[randomIndex][randomMolly];
	}

	@Override
	public String[] getNPCMessages() {
		return new String[] { "Help me "+getPlayer().getDisplayName()+"! I've been accused!", 
				"I need your help, "+getPlayer().getDisplayName()+"!", "My sister needs to be stopped!" };
	}

	@Override
	public boolean canTeleport() {
		getPlayer().packets().sendMessage("Molly has asked you to do something!");
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkLogin() {
		NPC npc = (NPC) getData().get("molly");
		if(interactionStage < 1 || interactionStage == 3) {
			molly = new NPC(npc.getId(), npc, false);
			Game.submit(new GameTick(1.1) {

				@Override
				public void run() {
					stop();
					molly.face(player);
				}	
				
			});
		}
		player.face(molly);
		player.getDialogue().start(getDialogue());
		player.setCantMove(false);
		tries = (int) getData().get("tries");
		if(interactionStage < 3) {
			List<NPC> savedMollys = (List<NPC>) getData().get("mollys");
			this.mollys = new ArrayList<NPC>();
			for(NPC n : savedMollys) {
				NPC sM = new NPC(n.getId(), n.getLocation(), true);
				mollys.add(sM);
			}
		} else if(interactionStage == 3) {
			failedTask = (boolean) getData().get("failedTask");
			if(failedTask) {
				Game.submit(new GameTick(.7) {

					@Override
					public void run() {
						failedTask(true);
						stop();
					}
					
				});
			} else {
				Game.submit(new GameTick(.7) {

					@Override
					public void run() {
						player.getDialogue().start(getSuccessDialogue());
						stop();
					}
					
				});
			}
		}
		return false;
	}

	@Override
	public boolean checkLogout() {
		getData().put("molly", new NPC(molly.getId(), Location.create(molly), false, false, null, null));
		if(interactionStage < 3)
			getData().put("mollys", mollys);
		molly = findNPC(getNPCId());
		if(molly != null)
			molly.finish();
		for(int i = 0; i < mollys.size(); i++) 
			if(mollys.get(i) != null)
				mollys.get(i).finish();
		getData().put("tries", tries);
		getData().put("failedTask", failedTask);
		return false;
	}

	@Override
	public boolean hasNPCOption1(NPC npc) {
		if(interactionStage <= 1 && npc.getId() == molly.getId())
			player.getDialogue().start(getDialogue());
		else {
			if(failedTask) {
				player.getDialogue().open(new Dialogue() {

					@Override
					public void start() {
						npc(molly.getId(), "I am extremely disappointed in the fact that", 
							"you couldn't get the job done, but it doesn't", 
							"matter now because you basically have just now", 
							"caused my life to go down the drain!");
					}

					@Override
					public void process(int interfaceId, int buttonId) {
						if(stage == 0)
							npc(1, molly.getId(), 
								"Now I'm going to be the one to get arrested for", "crimes that I didn't commit!");
						else if(stage == 1)
							npc(2, molly.getId(), "I am going to banish you away from here.", 
								"I don't want to speak to you anymore,", "nor will I give you the reward",
								"that you would've gotten if you had caught her.");
						else if(stage == 2) {
							end();
							player.sm("You start feeling groggy...");
							player.getScreenFade().fadeWhite(() -> {
								toWrongLocation = true;
								player.getRandomEvent().fullyStop();
							}, () -> {
								player.sm("You wake up banished away from molly's area.");
							});
						}
					}

					@Override
					public void finish() { }
					
				});
			} else {
				player.getDialogue().start(getFinalSuccessDialogue());
			}
		}
		return true;
	}
	
	@Override
	public boolean hasObjectOption1(GlobalObject object) {
		if(interactionStage == 0)
			return false;
		if(object.getId() == 14982) {
			if(interactionStage > 1 && interactionStage < 3) {
				player.sm("Molly has asked you to do something!");
				return false;
			} else if(interactionStage == 3)
				return true;
			player.setLocation((baseX + 9), (baseY + 17), 0);
			interactionStage = 2;
			Game.sendGraphics(molly, new Graphics(189), molly);
			molly.finish();
			tries = 2;
			if(!spawnedClaw)
				spawnClaw();
		} else if(object.getId() == 14978) {
			if(tries == 0)
				return false;
			if(interactionStage == 3)
				return false;
			if(!spawnedClaw)
				spawnClaw();
			player.interfaces().sendTab(tab(FRIENDS_LIST), 240);
			player.setCantMove(true);
			player.packets().sendString("Tries = "+tries, 240, 27);
			currentCamLookX = currentCamLookX != baseX + 14 && currentCamLookX != 0 ? currentCamLookX : baseX + 14;
			player.camPos(baseX + 14, baseY + 19, 2000, -1);
			player.camLook(currentCamLookX, baseY + 6, 0, -1);
			return true;
		}
		return false;
	}

	@Override
	public boolean canAttackEntity(Entity entity) {
		return false;
	}

	@Override
	public boolean canClickButtons(int interfaceId, int buttonId, int slotId,
			int packetId) {
		if(interactionStage == 0 && interfaceId != 182 && interfaceId != 750)
			return false;
		if(interfaceId == 240) {
			int nextX = 0;
			int nextY = 0;
			switch(buttonId) {
				case 33:
					player.setCantMove(false);
					player.packets().sendResetCamera();
					player.interfaces().sendTab(tab(FRIENDS_LIST), 550);
					break;
				case 29:
				case 30:
				case 31:
				case 32:
					if(tries == 0)
						return false;
					nextX = buttonId == 31 ? 1 : buttonId == 32 ? -1 : 0;
					nextY = buttonId == 29 ? -1 : buttonId == 30 ? 1 : 0;
					if(nextY == 1 && claw.getY() == (baseY + 13)) {
						player.sm("You can't move the camera outside of the gallery.");
						break;
					}
					if(nextX == 1 && claw.getX() == (baseX + 19)) {
						player.sm("You can't move the camera outside of the gallery.");
						break;
					}
					if(nextX == -1 && claw.getX() == (baseX + 9)) {
						player.sm("You can't move the camera outside of the gallery.");
						break;
					}
					if(nextY == -1 && claw.getX() >= (baseX + 9) && claw.getX() <= (baseX + 11) && claw.getY() == (baseY + 6)) {
						player.sm("You can't move the camera outside of the gallery.");
						break;
					}
					if(nextX == -1 && claw.getX() == (baseX + 12) && (claw.getY() == (baseY + 4) || claw.getY() == (baseY + 5))) {
						player.sm("You can't move the camera outside of the gallery.");
						break;
					}
					if(nextY == -1 && claw.getY() == (baseY + 4)) {
						player.sm("You can't move the camera outside of the gallery.");
						break;
					}
					currentCamLookX = nextX == 1 ? currentCamLookX + 1 : nextX == -1 ? currentCamLookX - 1 : currentCamLookX;
					player.camPos(baseX + 14, baseY + 19, 2000, 2);
					player.camLook(currentCamLookX, baseY + 6, 0, 2);
					player.packets().sendDestroyObject(claw);
					player.packets().sendDestroyObject(target);
					claw.setCoords(claw.getX() + (buttonId == 31 ? 1 : buttonId == 32 ? -1 : 0), 
						claw.getY() + (buttonId == 29 ? -1 : buttonId == 30 ? 1 : 0), 0);
					player.packets().sendSpawnedObject(claw);
					target.setCoords(claw);
					player.packets().sendSpawnedObject(target);
					break;
				case 28:
					if(tickDelay > 0)
						return true;
					tickDelay = 8;
					boolean displayFailMessage = false;
					for(final NPC n : mollys) {
						if(n.matches(claw)) {
							if(n.getId() != molly.getId()) {
								n.setRandomWalk(false);
								n.animate(4001);
								claw.animate(4000);
								player.sm("You caught an innocent civilian!");
								mollys.remove(n);
								tries--;
								player.packets().sendString("Tries = "+tries, 240, 27);
								if(tries == 0)
									failedTask(false);
								Game.submit(new GameTick(3.28) {

									@Override
									public void run() {
										stop();
										n.finish();
									}
									
								});
								return true;
							} else {
								success(n);
								return true;
							}
						} else {
							claw.animate(4000);
							displayFailMessage = true;
						}
					}
					if(displayFailMessage)
						player.sm("You have no target!");
					player.packets().sendString("Tries = "+tries, 240, 27);
					break;
			}
		}
		return true;
	}

	@Override
	public Dialogue getDialogue() {
		return new Dialogue() {
			
			int npcId = molly.getId();
			
			@Override
			public void start() {
				if(interactionStage == 0)
					sendNPCDialogue(0, npcId, "Thanks for coming!");
				else if(interactionStage == 1) {
					sendNPCDialogue(16, npcId, "Would you like me to run over the controls?");
					stage = 16;
				}
			}
			
			@Override
			public void process(int i, int b) {
				if(interactionStage == 0) {
					switch(stage) {
					 	case 0:
					 		sendPlayerDialogue(1, "It's not like I had a lot of choice you know!"); break;
					 	case 1:
					 		sendNPCDialogue(2, npcId, "I'm sorry for abducting you like that, but I really need", 
					 			"your help "+player.getDisplayName()+"."); break;
					 	case 2:
					 		sendPlayerDialogue(3, "What's the problem then?"); break;
					 	case 3:
					 		sendNPCDialogue(4, npcId, "It's my evil twin sister! She's been galavanting around", 
					 			"Nova committing crimes and now I'm getting the", "blame!"); break;
					 	case 4:
					 		sendPlayerDialogue(5, "Well what's all this got to do with me then?"); break;
					 	case 5:
					 		sendNPCDialogue(6, npcId, "I'm glad you asked!"); break;
					 	case 6:
					 		sendNPCDialogue(7, npcId, "Through that door is a room with a cage and a control", 
					 			"panel that operates a giant mechanical claw."); break;
					 	case 7:
					 		sendNPCDialogue(8, npcId, "I lured my sister into the room so I could imprison her", 
					 			"in the case by using the claw. The problem is my sister", 
					 			"managed to herd some innocent civilians in there with", "her."); break;
					 	case 8:
					 		sendPlayerDialogue(9, "So what do you need me to do?"); break;
					 	case 9:
					 		sendNPCDialogue(10, npcId, "I need you to go next door and use the claw to catch", "my sister."); break;
					 	case 10:
					 		sendNPCDialogue(11, npcId, "Once she's in prison, she won't be causing me anymore", "bother!"); break;
					 	case 11:
					 		sendPlayerDialogue(12, "Sounds easy enough to me."); break;
					 	case 12:
					 		sendNPCDialogue(13, npcId, "Fabulous! Now take a good long look at me because the",
					 			"door will be locked behind you. My twin looks exactly", "like me, even her clothes!"); break;
					 	case 13:
					 		sendNPCDialogue(14, npcId, "One more thing to make your life difficult; the magic", 
					 			"powering the claw is running low so you'll only have", "two attempts to catch her."); break;
					 	case 14:
					 		sendPlayerDialogue(15, "I'll do my best then!"); break;
					 	case 15:
					 		sendNPCDialogue(16, npcId, "By the way, would you like me to run through the",
					 			"controls for you, or do you think you'll manage?"); break;
					 	case 16:
					 		sendOptions(TITLE, "Yes please.", "No, thanks."); stage = 17; break;
					 	case 17:
					 		if(b == OPTION1) {
					 			sendPlayerDialogue(18, "Yes please. I mean, it's always best to be prepared,", "right?");
					 			break;
					 		} else {
					 			end();
					 			sendNPCDialogue(0, npcId, "Good luck!");
					 			interactionStage = 1;
					 			break;
					 		}
					 	case 18:
					 		sendNPCDialogue(19, npcId, "Ok, when you turn the machine on you'll see the", 
					 			"glowing mark on the floor where the claw is currently", 
					 			"aiming, and you'll see a lever and button on the right", "hand side of your screen."); break;
					 	case 19:
					 		sendNPCDialogue(20, npcId, "To move the claw's current location, click on the", 
					 			"direction you want it to move in, as indicated on the", "right hand panel."); break;
					 	case 20:
					 		sendNPCDialogue(21, npcId, "You'll know which way the claw will go as the lever will", 
					 			"point that way to show you."); break;
					 	case 21:
					 		sendNPCDialogue(22, npcId, "When you see someone on top of the glowing mark", 
					 			"then hit the button on the right hand panel, as this will", "send the claw down to pick them up."); break;
					 	case 22:
					 		sendNPCDialogue(23, npcId, "Do be careful and make sure that there is someone on", 
					 			"the mark, and not just walking past it."); break;
					 	case 23:
					 		sendNPCDialogue(24, npcId, "Oh, and make sure that the person is my sister, so you", 
					 			"don't end up catching a random civilian."); break;
					 	case 24:
					 		sendNPCDialogue(25, npcId, "Does that help?"); break;
					 	case 25:
					 		sendPlayerDialogue(26, "Yes, that covers everything. Thanks!"); break;
					 	case 26:
					 		end();
					 		sendNPCDialogue(0, npcId, "Good luck!");
					 		interactionStage = 1;
					 		break;
					}
				} else if(interactionStage == 1) {
					switch(stage) {
						case 16:
					 		sendOptions(TITLE, "Yes please.", "No, thanks."); stage = 17; break;
					 	case 17:
					 		if(b == OPTION1) {
					 			sendPlayerDialogue(18, "Yes please. I mean, it's always best to be prepared,", "right?");
					 			break;
					 		} else {
					 			end();
					 			sendNPCDialogue(0, npcId, "Good luck!");
					 			interactionStage = 1;
					 			break;
					 		}
					 	case 18:
					 		sendNPCDialogue(19, npcId, "Ok, when you turn the machine on you'll see the", 
					 			"glowing mark on the floor where the claw is currently", 
					 			"aiming, and you'll see a lever and button on the right", "hand side of your screen."); break;
					 	case 19:
					 		sendNPCDialogue(20, npcId, "To move the claw's current location, click on the", 
					 			"direction you want it to move in, as indicated on the", "right hand panel."); break;
					 	case 20:
					 		sendNPCDialogue(21, npcId, "You'll know which way the claw will go as the lever will", 
					 			"point that way to show you."); break;
					 	case 21:
					 		sendNPCDialogue(22, npcId, "When you see someone on top of the glowing mark", 
					 			"then hit the button on the right hand panel, as this will", "send the claw down to pick them up."); break;
					 	case 22:
					 		sendNPCDialogue(23, npcId, "Do be careful and make sure that there is someone on", 
					 			"the mark, and not just walking past it."); break;
					 	case 23:
					 		sendNPCDialogue(24, npcId, "Oh, and make sure that the person is my sister, so you", 
					 			"don't end up catching a random civilian."); break;
					 	case 24:
					 		sendNPCDialogue(25, npcId, "Does that help?"); break;
					 	case 25:
					 		sendPlayerDialogue(26, "Yes, that covers everything. Thanks!"); break;
					 	case 26:
					 		end();
					 		sendNPCDialogue(0, npcId, "Good luck!");
					 		interactionStage = 1;
					 		break;
					}
				} else if(interactionStage == 2) {
					
				}
			}

			@Override
			public void finish() {
				
			}
		};
	}
	
	int tickDelay = 0;
	
	@Override
	public void processEvent() {
		if(tickDelay > 0)
			tickDelay--;
	}

	@Override
	public Location getEventLocation() {
		return new Location(baseX + 4, baseY + 16, 0);
	}

	@Override
	public boolean hasHiddenMiniMap() {
		return true;
	}

	@Override
	public Integer[] getTabsRemoved() {
		return new Integer[] {
			tab(ATTACK_STYLES), 
			tab(QUESTS), 
			tab(ACHIEVEMENTS),
			tab(SKILLS),
			tab(EQUIPMENT),
			tab(INVENTORY),
			tab(PRAYER),
			tab(MAGIC_BOOK), 
			tab(EMOTES),
			tab(NOTES),
			tab(GAME_SETTINGS)
		};
	}

	@Override
	public boolean isTraditional() {
		return true;
	}
	
	@Override
	public boolean forceStop() {
		molly = findNPC(getNPCId());
		if(molly != null)
			molly.finish();
		for(int i = 0; i < mollys.size(); i++) 
			if(mollys.get(i) != null)
				mollys.get(i).finish();
		return false;
		
	}
	
	public void spawnMollys() {
		for(int i = 0; i < mollyIds[0].length; i++) {
			mollys.add(new NPC(mollyIds[randomIndex][i], new Location((baseX + 14) + Misc.random(5), 
				(baseY + 8) - Misc.random(5), 0), true));
		}
	}

	@Override
	public Integer[] getDynamicMapVariables() {
		return new Integer[] { 232, 640, 3, 3  };
	}
	
	private void failedTask(boolean onlyDialogue) {
		Dialogue failedDialogue = new Dialogue() {
			@Override
			public void start() {
				npc(molly.getId(), "You know, I didn't ask much of you, and you", 
					"managed to mess up the one thing that I did", "ask of you!");
			}

			@Override
			public void process(int interfaceId,
					int buttonId) {
				if(stage == 0) {
					end();
					npc(molly.getId(), "Come next door and talk to me.");
				}
			}

			@Override
			public void finish() { 
				
			}
		};
		if(onlyDialogue) {
			player.getDialogue().start(failedDialogue);
			return;
		}
		player.setCantMove(false);
		player.packets().sendResetCamera();
		player.interfaces().sendTab(tab(FRIENDS_LIST), 550);
		for(NPC npc : mollys) {
			npc.setRandomWalk(false);
			Game.sendGraphics(npc, new Graphics(189), npc);
			npc.finish();
			interactionStage = 3;
		}
		molly = new NPC(molly.getId(), molly, false);
		molly.face(player);
		player.face(molly);
		player.getDialogue().start(failedDialogue);
		failedTask = true;
	}
	
	/**
	 * What happens when the player catches the evil twin
	 */
	private void success(NPC n) {
		n.setRandomWalk(false);
		player.sm("You caught the evil twin!");
		n.animate(4001);
		claw.animate(4000);
		player.camPos(baseX + 14, baseY + 19, 3200, -1);
		player.camLook(currentCamLookX, baseY + 6, 0, -1);
		tries--;
		player.interfaces().sendTab(tab(FRIENDS_LIST), 550);
		Game.submit(new GameTick(3) {

			int ticks = 0;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.packets().sendDestroyObject(claw);
					player.packets().sendDestroyObject(target);
					player.camPos(baseX + 14, baseY + 19, 3200, 1);
					player.camLook(baseX + 10, baseY + 4, 0, 1);
					NPC npc = new NPC(n.getId() - 20, n, false);
					npc.setNextForceMovement(new ForceMovement(new Location(baseX + 10, baseY + 4, player.getZ()), 5, 3));
					Game.submit(new GameTick(1) {
						int ticks = 0;
						NPC newnpc;
						public void run() {
							if(ticks == 3) {
								npc.setLocation(new Location(baseX + 10, baseY + 4, player.getZ()));
								npc.animate(4003);
								newnpc = new NPC(npc.getId() + 20, Location.create(baseX + 10, baseY + 4, player.getZ()), false);
							} else if(ticks == 4) {
								npc.finish();
								player.camPos(baseX + 13, baseY + 7, 2400, 11);
								player.camLook(baseX + 10, baseY + 4, 0, 11);
								newnpc.faceLocation(Location.create(baseX + 14, baseY + 8, 0));
								for(NPC np : mollys) {
									if(np == n)
										continue;
									np.setRandomWalk(false);
									Game.sendGraphics(np, new Graphics(189), np);
									np.finish();
								}
							} else if(ticks == 7)
								newnpc.animate(859);
							else if(ticks == 10) {
								stop();
								molly = new NPC(molly.getId(), molly, false);
								molly.face(player);
								player.face(molly);
								player.packets().sendResetCamera();
								player.setCantMove(false);
								player.getDialogue().start(getSuccessDialogue());
								npc.finish();
								newnpc.finish();
							}
							ticks++;
						}
					});
					n.finish();
				} else if(ticks == 2) {
					stop();
					
				}
				ticks++;
			}
			
		});
		interactionStage = 3;
	}
	
	/**
	 * Dialogue that opens if the player caught the evil twin.
	 * @return
	 */
	private Dialogue getSuccessDialogue() {
		
		return new Dialogue() {

			@Override
			public void start() {
				npc(molly.getId(), "Well done, you managed to catch my sister!");
			}

			@Override
			public void process(int interfaceId, int buttonId) {
				if(stage == 0) {
					end();
					npc(molly.getId(), "Come next door and talk to me.");
				}
			}

			@Override
			public void finish() { 
				
			}
			
		};
	}
	
	/**
	 * Opens the final dialogue.
	 * @return
	 */
	private Dialogue getFinalSuccessDialogue() {
		
		return new Dialogue() {

			@Override
			public void start() {
				player("Well, I've managed to get her into the cage.");
			}

			@Override
			public void process(int interfaceId, int buttonId) {
				if(stage == 0)
					npc(1, molly.getId(), 
						"Fantastic! For so many years I've had to put up with", 
						"her and now she's locked up for good.");
				else if(stage == 1)
					npc(2, molly.getId(), 
						"Thank you for all your help. Take this as a reward.");
				else if(stage == 2) {
					end();
					player.getRandomEvent().fullyStop();
					player.sm("Molly thanks you for your help and gives you a small reward.");
				}
			}

			@Override
			public void finish() { 
				
			}
			
		};
	}
	
	private void spawnClaw() {
		GlobalObject claw = new GlobalObject(14976, 10, 0, (baseX + 14), (baseY + 12), 0);
		GlobalObject target = new GlobalObject(14977, 22, 0, (baseX + 14), (baseY + 12), 0);
		Game.spawnReplacedObject(claw, true);
		Game.spawnReplacedObject(target, true);
		this.claw = claw;
		this.target = target;
		spawnedClaw = true;
	}

}
