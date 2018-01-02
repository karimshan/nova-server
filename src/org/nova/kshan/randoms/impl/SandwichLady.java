package org.nova.kshan.randoms.impl;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.player.content.Magic;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.dialogues.impl.EntityDialogue;
import org.nova.kshan.randoms.RandomEvent;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class SandwichLady extends RandomEvent {
	
	/**
	 * player's current refreshment (Set randomly)
	 */
	private String currentRefreshment;
	
	/**
	 * Sandwich Lady
	 */
	NPC sandwichLady;
	
	/**
	 * An array of refreshments
	 */
	private Object[][] refreshments = {
		{ "Baguette", 6961 }, 
		{ "Square sandwich", 6965 }, 
		{ "Triangle sandwich", 6962 },
		{ "Roll", 6963 }, 
		{ "Meat pie", 2327 }, 
		{ "Doughnut", 14665 }, 
		{ "Chocolate bar", 1973 }
	};
	
	@Override
	public boolean isTraditional() {
		return true;
	}

	@Override
	public void startEvent() {
		sandwichLady = findNPC(8629);
		if(sandwichLady == null) {
			player.getRandomEvent().fullyStop();
			player.sm("An unexpected error has occurred.");
			return;
		}
		player.faceEntity(sandwichLady);
		sandwichLady.faceEntity(player);
		randomRefreshment();
		player.getDialogue().start(getDialogue(), "Start");
	}

	@Override
	public Integer getNPCId() {
		return 8631;
	}

	@Override
	public String[] getNPCMessages() {
		return new String[] { "Sandwiches, " + getPlayer().getDisplayName() + "!",
				"Hey, you look hungry, " + getPlayer().getDisplayName() + "! Come with me!", 
				"Sandwiches! Get your sandwiches here!" };
	}

	@Override
	public boolean canTeleport() {
		if(interactionStage < 1) {
			getPlayer().sm("A magical force prevents you from teleporting.");
			return false;
		} else
			return true;
	}

	@Override
	public boolean checkLogin() {
		NPC npc = findNPC(8629);
		currentRefreshment = (String) getData().get("currentRefreshment");
		if(interactionStage == 0) {
			getPlayer().face(npc);
			npc.face(getPlayer());
			Game.submit(new GameTick(.5) {
				public void run() {
					stop();
					getPlayer().getDialogue().start(getDialogue(), "Start");
				}
			});
		} else if(interactionStage == 1) {
			Game.submit(new GameTick(.5) {
				public void run() {
					stop();
					getPlayer().getDialogue().start(new EntityDialogue(), 8629, (Object)
						new String[] { "The exit portal's just over there, dearie, whenever", "you're ready to leave." });
				}
			});
			npc.faceLocation(new Location(77, 70, 0));
			getPlayer().face(npc);
			npc.forceTalk("Just over there, dearie!");
		}
		return false;
	}

	@Override
	public boolean checkLogout() {
		return false;
	}
	
	@Override
	public boolean hasHiddenMiniMap() {
		return false;
	}

	@Override
	public boolean hasNPCOption1(final NPC npc) {
		if(getPlayer().getTemporaryAttribute("tempSandwichLady") != null)
			return false;
		getPlayer().setNextFaceEntity(npc);
		Game.submit(new GameTick(.5) {
			public void run() {
				if(npc.getId() == 8629 && getPlayer().withinDistance(npc, 2)) {
					stop();
					getPlayer().face(npc);
					npc.face(getPlayer());
					switch(interactionStage) {
						case 0:
							getPlayer().getDialogue().start(getDialogue(), "Start");
							break;
						case 1:
							getPlayer().getDialogue().start(new EntityDialogue(), 8629, (Object)
								new String[] { "The exit portal's just over there, dearie, whenever", "you're ready to leave." });
							sandwichLady = findNPC(8629);
							sandwichLady.faceLocation(new Location(77, 70, 0));
							sandwichLady.forceTalk("Just over there, dearie!");
							break;
					}
				}
			}
		});
		return false;
	}

	@Override
	public boolean hasObjectOption1(final GlobalObject obj) {
		Game.submit(new GameTick(.5) {
			public void run() {
				if(obj.defs().name.toLowerCase().contains("exit portal")) {
					getPlayer().faceObject(obj);
					if(interactionStage == 1) {
						Magic.telePlayer(getPlayer(), getOriginalLocation(), false);
						Game.submit(new GameTick(2.1) {
							public void run() {
								getPlayer().getRandomEvent().fullyStop();
								stop();
							}
						});
						currentRefreshment = null;
						stop();
					} else {
						getPlayer().sendMessage("You can't do that at the moment.");
						stop();
					}
				}
			}
		});
		return true;
	}

	@Override
	public boolean canAttackEntity(Entity entity) {
		return true;
	}

	@Override
	public boolean canClickButtons(int interfaceId, int buttonId, int slotId, int packetId) {
		if(interfaceId == 297) {
			if(current().equals(sandwichForButtonId(buttonId)))
				getPlayer().getDialogue().start(getDialogue(), "Correct");
			else if(!current().equals(sandwichForButtonId(buttonId)) && buttonId != 47 && buttonId != 49 && buttonId != 44)
				getPlayer().getDialogue().start(getDialogue(), "Wrong");
				
		}
		return true;
	}
	
	private String sandwichForButtonId(int button) {
		switch(button) {
			case 18:
				return "Meat pie";
			case 22:
				return "Chocolate bar";
			case 20:
				return "Doughnut";
			case 10:
				return "Baguette";
			case 12:
				return "Triangle sandwich";
			case 14:
				return "Square sandwich";
			case 16:
				return "Roll";
		}
		return null;
	}

	@Override
	public Dialogue getDialogue() {
		return new Dialogue() {
			String var;
			@Override
			public void start() {
				var = (String) data[0];
				if(var.equals("Correct")) {
					interactionStage = 1;
					getPlayer().interfaces().closeScreenInterface();
					if (getPlayer().getInventory().hasFreeSlots())
						getPlayer().getInventory().addItem(sandwichId(), 1);
					else {
						getPlayer().sm("Your " + current() + " has been placed on the ground.");
						Game.addGroundItem(new Item(sandwichId(), 1), new Location(player), player, false, 180, true);
					}
					sendEntityDialogue(NPC, TALKING_ALOT, 8629, "Sandwich Lady", false,
							new String[] { "That's right. Enjoy your snack, dearie." });
					
				} else if(var.equals("Wrong")) {
					end();
					wrongSnack();
				} else if(var.equalsIgnoreCase("Start")) {
					sendEntityDialogue(NPC, TALKING_ALOT, 8629, "Sandwich Lady", false,
						new String[] { "I'm sure you're a busy boy, but you look hungry. I", 
						"tell you what -", 
						"have a "+current().toLowerCase()+" for free."});
				}
			}

			@Override
			public void process(int interfaceId, int buttonId) {
				if(var.equals("Correct")) {
					if(stage == 0) {
						stage = 1;
						sendPlayerDialogue(false, new String[] { "Thanks! Can I go now?" });
					} else if(stage == 1) {
						sendEntityDialogue(NPC, CALM_TALK, 8629, "Sandwich Lady", false,
								new String[] { "Of course, dearie.", "The exit portal's just over there.", });
						sandwichLady = findNPC(8629);
						sandwichLady.faceLocation(new Location(77, 70, 0));
						sandwichLady.forceTalk("Just over there, dearie!");
						stage = 2;
					} else
						end();
				} else {
					end();
					getPlayer().interfaces().closeScreenInterface();
					getPlayer().interfaces().sendInterface(297);
					getPlayer().packets().sendString("Have a "+current().toLowerCase()+" for free!", 297, 48);
					getPlayer().sendMessage("Have a "+current().toLowerCase()+" for free!");
				}
					
			}

			@Override
			public void finish() {
				
			}
			
		};
	}

	@Override
	public Location getEventLocation() {
		return new Location(75, 70, 0);
	}
	
	@Override
	public Integer[] getTabsRemoved() {
		return selectTabs();
	}

	/**
	 * Returns the current refreshment
	 * @return
	 */
	public String current() {
		return currentRefreshment;
	}

	/**
	 * Sets the refreshment randomly
	 */
	public void randomRefreshment() {
		currentRefreshment = (String) refreshments[Misc.random(refreshments.length)][0];
		getData().put("currentRefreshment", currentRefreshment);
	}
	
	/**
	 * Returns the sandwich id by matching up the two variables
	 * @return
	 */
	public Integer sandwichId() {
		for (Object[] id : refreshments) {
			if (current().equalsIgnoreCase((String) id[0]))
				return (Integer) id[1];
		}
		return -1;
	}
	
	public void wrongSnack() {
		getPlayer().packets().sendCloseInterface();
		getPlayer().setCantMove(true);
		canLogout(false);
		getPlayer().getDialogue().start(new EntityDialogue(), 8629,
				new String[] { "Hey, that's not what I offered you!", "Thief! Thief! Thief!" }, 
				true, Dialogue.VERY_ANGRY);
		for (final NPC n : Game.getLocalNPCs(getPlayer())) {
			if (n.getId() == 8629 && n.withinDistance(new Location(getPlayer()), 7)) {
				getPlayer().setTemporaryAttribute("tempSandwichLady", n);
			}
		}
		final NPC n = (NPC) getPlayer().getTemporaryAttribute("tempSandwichLady");
		n.setNextForceTalk(new ForceTalk("Thief!"));
		Game.submit(new GameTick(3) {
			@Override
			public void run() {
				if(getPlayer().getRandomEvent().getCurrent() == null)
					stop();
				n.setNextFaceEntity(getPlayer());
				n.setLocation(new Location(getPlayer().getX() - 1, getPlayer().getY() - 1, getPlayer().getZ()));
				this.stop();
			}
		});
		Game.submit(new GameTick(3.58) {
			@Override
			public void run() {
				if(getPlayer().getRandomEvent().getCurrent() == null)
					stop();
				n.animate(3301);
				n.setNextForceTalk(new ForceTalk("Take that, " + getPlayer().getDisplayName() + "!"));
				n.setNextGraphics(new Graphics(189));
				getPlayer().face(n);
				getPlayer().setNextAnimation(new Animation(2304));
				this.stop();
			}
		});
		Game.submit(new GameTick(6.35) {
			@Override
			public void run() {
				if(getPlayer().getRandomEvent().getCurrent() == null)
					stop();
				n.setLocation(n.getRespawnTile());
				getPlayer().packets().sendOverlay(120);
				this.stop();
			}
		});
		Game.submit(new GameTick(8.84) {
			@Override
			public void run() {
				if(getPlayer().getRandomEvent().getCurrent() == null)
					stop();
				toWrongLocation = true;
				getPlayer().setLocation(RandomEvent.WRONG_LOCATIONS[Misc.random(RandomEvent.WRONG_LOCATIONS.length)]);
				getPlayer().packets().sendOverlay(170);
				getPlayer().sm("The sandwich lady hits you with a baguette.");
				getPlayer().getRandomEvent().fullyStop();
				getPlayer().setCantMove(false);
				getPlayer().animate(-1);
				this.stop();
			}
		});
		Game.submit(new GameTick(9) {
			@Override
			public void run() {
				if(getPlayer().getRandomEvent().getCurrent() == null)
					stop();
				getPlayer().getDialogue().finishDialogue();
				getPlayer().interfaces().closeChatBoxInterface();
				getPlayer().packets().sendCloseInterface();
				getPlayer().removeTemporaryAttribute("tempSandwichLady");
				canLogout(true);
				this.stop();
			}
		});
	}

	@Override
	public Integer[] getDynamicMapVariables() {
		return null;
	}
}
