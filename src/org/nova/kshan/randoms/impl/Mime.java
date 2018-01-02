package org.nova.kshan.randoms.impl;

import org.nova.cache.loaders.AnimationDefinition;
import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.entity.Entity;
import org.nova.game.map.Directions;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.randoms.RandomEvent;
import org.nova.utility.misc.Misc;

/**
 * Hopefully this doesn't take too long LOL
 * Update: only took an hour to get all animations and ids and
 * fabricate everything together
 * 
 * @author shan
 *
 */
public class Mime extends RandomEvent {

	// Animation for the spotlight fading out on the non-performer
	private final int SPOTLIGHT_FADE = 1135;

	// Animation for shining the spotlight on the current performer
	private final int SPOTLIGHT_SHINE = 1136;

	// Animation for when the mime bows
	private final int BOW = 858;

	// the id for the mime emotes chatbox
	private final int MIME_CHATBOX = 188;

	// The MIME_ANIMATIONS for the event
	private final int[] mimeAnimations = { 857, 860, 861, 866, 1128, 1129, 1130, 1131 };

	// The animation the mime just did
	private int currentAnim = -1;
	
	// The animation that the player guessed
	private int guessed = -1;

	// The animations if the player gets the action right or wrong
	private final int CRY = 860, CHEER = 862;

	// The animation that corresponds with the correct button on inter 188
	private final int[][] mimeChatboxAnims = { 
			{ 5, 866 }, { 3, CRY }, { 9, 1128 }, { 7, 1129 },
			{ 2, 857 }, { 4, 861 }, { 6, 1130 }, { 8, 1131 }
	};

	// Mime object
	private NPC mime;

	// Strange watchers
	private NPC[] strangeWatchers;

	// The amount of emotes remaining that the player has to do to leave.
	// If they get one wrong, the counter goes up by 2 and then they don't get the reward.
	private int emoteCounter = 5;

	// The spotlight objects
	GlobalObject playerSpotlight;
	GlobalObject mimeSpotlight;

	// returns if the spotlights have been spawned yet or not.
	private boolean spawnedSpotlights;

	@Override
	public void startEvent() {
		spawnEventNPCs();
		player.sm("You need to copy the mime's performance, then you'll return to where you were.");
		player.setRun(false);
		player.interfaces().setKeepChatboxOpen(true);
		interactionStage = 1;
		spawnSpotlights();
		player.getDialogue().start(getDialogue());
	}

	@Override
	public Integer getNPCId() {
		return 410;
	}

	@Override
	public String[] getNPCMessages() {
		return new String[] { "Ah, "+player.getDisplayName()+", just the person I need!",
				getPlayer().getDisplayName()+"! I require your assistance.", 
				"Ah, you'll do, "+getPlayer().getDisplayName()+"!" };
	}

	@Override
	public boolean checkLogin() {
		emoteCounter = (int) getData().get("emoteCounter");
		if(interactionStage == 1) {
			player.setLocation(getEventLocation());
			startEvent();
		} else if(interactionStage > 1 && emoteCounter > 0) {
			player.sm("You need to copy the mime's performance, then you'll return to where you were.");
			if(!spawnedSpotlights)
				spawnSpotlights();
			spawnEventNPCs();
			Game.submit(new GameTick(1) {
				public void run() {
					playerSpotlight.animate(SPOTLIGHT_FADE);
					sendNewEmote();
					stop();
				}
			});
		} else if(emoteCounter == 0) {
			player.getRandomEvent().fullyStop();
			player.sm("Congratulations on completing the mime event! Here's your reward: TODO");
		}
		return false;
	}

	@Override
	public boolean checkLogout() {
		if(emoteCounter == 0)
			return true;
		getData().put("emoteCounter", this.emoteCounter);
		return false;
	}

	@Override
	public boolean canTeleport() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasNPCOption1(NPC npc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasObjectOption1(GlobalObject obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canAttackEntity(Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canClickButtons(int interfaceId, int buttonId, int slotId, int packetId) {
		switch(interfaceId) {
		case MIME_CHATBOX:
			guessEmote(buttonId);
			break;
		}
		return true;
	}

	@Override
	public Dialogue getDialogue() {
		return new Dialogue() {

			@Override
			public void start() {
				npcDialogue(410, 2, "Mysterious Old Man", new String[] { "You need to copy the mime's performance, then you'll",
				"be returned to where you were." });
			}

			@Override
			public void process(int interfaceId, int buttonId) {
				// initial click to continue stage for dialogue
				if(interactionStage == 1) {
					player.interfaces().setKeepChatboxOpen(false);
					player.addWalkSteps(player.getX(), player.getY() - 2, 5, false);
					end();
				}
			}

			@Override
			public void finish() {
				interactionStage = 2;
				sendNewEmote(); // This is where the event starts
			}

		};
	}

	@Override
	public Location getEventLocation() {
		return new Location(baseX + 8, baseY + 20, 0); // In the spotlight for now
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
	public Integer[] getDynamicMapVariables() {
		return new Integer[] { 250, 593, 3, 3 };
	}

	/**
	 * Sends the options for the emote
	 */
	private void sendEmoteOptions() {
		player.interfaces().sendChatBoxInterface(MIME_CHATBOX);
		// Gotta send the strings too smh
		player.packets().sendString("Dance", 188, 5);
		player.packets().sendString("Cry", 188, 3);
		player.packets().sendString("Glass Wall", 188, 9);
		player.packets().sendString("Lean on Air", 188, 7);
		player.packets().sendString("Think", 188, 2);
		player.packets().sendString("Laugh", 188, 4);
		player.packets().sendString("Climb Rope", 188, 6);
		player.packets().sendString("Glass Box", 188, 8);
		player.interfaces().setKeepChatboxOpen(true);
	}

	/**
	 * Closes the chatbox interface
	 */
	private void closeChatbox() {
		player.interfaces().setKeepChatboxOpen(false);
		player.interfaces().closeChatBoxInterface();
	}

	/**
	 * Spawns the spotlights which shine on both the mime and player
	 */
	private void spawnSpotlights() {
		GlobalObject playerSpotlight = new GlobalObject(3644, 10, 0, (baseX + 7), (baseY + 17), 0);
		GlobalObject mimeSpotlight = new GlobalObject(3644, 10, 2, (baseX + 10), (baseY + 17), 0);
		Game.spawnReplacedObject(playerSpotlight, true);
		Game.spawnReplacedObject(mimeSpotlight, true);
		this.playerSpotlight = playerSpotlight;
		this.mimeSpotlight = mimeSpotlight;
		spawnedSpotlights = true;
	}

	/**
	 * Sends a new emote from the mime to the player lol *foolproof*
	 */
	private void sendNewEmote() {
		playerSpotlight.animate(SPOTLIGHT_FADE);
		if(interactionStage == 2 || emoteCounter != 5)
			mimeSpotlight.animate(SPOTLIGHT_SHINE);
		mime.faceDirection(Directions.SOUTH);
		currentAnim = mimeAnimations[Misc.random(mimeAnimations.length)];
		mime.animate(currentAnim);
		double time = AnimationDefinition.get(currentAnim).getSeconds();
		Game.submit(new GameTick(time) {

			@Override
			public void run() {
				mime.face(player);
				mime.animate(BOW);
				mimeSpotlight.animate(SPOTLIGHT_FADE);
				playerSpotlight.animate(SPOTLIGHT_SHINE);
				sendEmoteOptions();
				stop();
			}

		});
	}

	/**
	 * The player guesses the emote, and if it is correct, 
	 * they cheer and then another one is sent if the emoteCounter is
	 * not at zero yet.
	 */
	private void guessEmote(int buttonId) {
		for(int i = 0; i < mimeChatboxAnims.length; i++)
			if(buttonId == mimeChatboxAnims[i][0])
				this.guessed = mimeChatboxAnims[i][1];
		closeChatbox();
		player.animate(guessed);
		double guessedAnimTime = AnimationDefinition.get(guessed).getSeconds();
		Game.submit(new GameTick(guessedAnimTime) {

			@Override
			public void run() {
				if(currentAnim == guessed) {
					emoteCounter--;
					player.animate(CHEER);
					double time = AnimationDefinition.get(CHEER).getSeconds();
					Game.submit(new GameTick(time) {
						public void run() {
							if(emoteCounter == 0) {
								fullyStop();
								player.sm("Congratulations on completing the mime event! Here's your reward: TODO");
							} else
								sendNewEmote();
							stop();
						}
					});
				} else {
					emoteCounter += 2; // Increases required emotes by 2 if the player messes up (lol)
					player.animate(CRY);
					double time = AnimationDefinition.get(CRY).getSeconds();
					Game.submit(new GameTick(time) {
						public void run() {
							sendNewEmote();
							stop();
						}
					});
				}
				stop();
			}

		});
	}

	/**
	 * Spawns the mime and strange watchers
	 */
	private void spawnEventNPCs() {
		mime = new NPC(1056, new Location(baseX + 11, baseY + 18, 0), false);
		mime.faceDirection(Directions.WEST);
		strangeWatchers = new NPC[] { 
				new NPC(1057, Location.create(baseX + 15, baseY + 12, 0), false),
				new NPC(1058, Location.create(baseX + 12, baseY + 10, 0), false),
				new NPC(1059, Location.create(baseX + 6, baseY + 11, 0), false)
		};
		for(NPC n : strangeWatchers)
			n.faceDirection(Directions.SOUTH);
	}

}
