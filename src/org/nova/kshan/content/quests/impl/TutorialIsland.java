package org.nova.kshan.content.quests.impl;

import org.nova.Constants;
import org.nova.cache.loaders.ObjectDefinition;
import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.FloorItem;
import org.nova.game.item.Item;
import org.nova.game.map.Directions;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.player.CoordsEvent;
import org.nova.game.player.HintIcon.HintDirections;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.controlers.Controller;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.dialogues.impl.InfoText;
import org.nova.network.decoders.packets.handlers.objects.Passages;

/**
 * 
 * @author K-Shan
 *
 */
public class TutorialIsland extends Controller {
	
	public static final int RUNESCAPE_GUIDE = 945;
	public static final int SURVIVAL_EXPERT = 943;
	public static final int FISHING_SPOT = 952;
	public static final int MASTER_CHEF = 942;
	public static final int QUEST_GUIDE = 949;
	public static final int BROTHER_BRACE = 954;
	public static final int MINING_INSTRUCTOR = 948;
	public static final int COMBAT_INSTRUCTOR = 944;
	public static final int GIANT_RAT = 950;
	public static final int FINANCIAL_ADVISOR = 947;
	public static final int BANKER = 44;
	public static final int CHICKEN = 951;
	public static final int MAGIC_INSTRUCTOR = 946;
	
	public static final int NORMAL_LOGS = 1511;
	public static final int TINDERBOX = 590;
	public static final int BUCKET_OF_WATER = 1929;
	public static final int POT_OF_FLOUR = 1933;
	public static final int EMPTY_BUCKET = 3727;
	public static final int EMPTY_POT = 1931;
	public static final int BREAD_DOUGH = 2307;

	public static final Animation WOODCUTTING_ANIMATION = new Animation(879);
	public static final Animation FIREMAKING_ANIMATION = new Animation(733);
	public static final Animation FISHING_ANIMATION = new Animation(621);
	public static final Animation FIRE_COOKING_ANIMATION = new Animation(897);
	public static final Animation RANGE_COOKING_ANIMATION = new Animation(883);
	public static final Animation LADDER_ANIMATION = new Animation(828);
	public static final Location MINING_TUTORIAL_LOCATION = new Location(3088, 9520, 0);
	
	private boolean clicked = false;
	
	@Override
	public boolean processNPCAttack(NPC npc) {
		if(player.getTutorialStage() == 47 || player.getTutorialStage() == 48) {
			if(npc.getId() == GIANT_RAT) {
				player.setTemporaryAttribute("FIRST_RAT", npc);
			sendTutLines("", 48, new String[] { 
				"<col=0000ff>Sit back and watch.</col>",
				"While you are fighting, you will see a bar over your head. The bar",
				"shows how much health you have left. Your opponent will have one too.",
				"You will continue to attack the rat until it's dead or you do something",
				"else.", "" });
				refreshAll();	
			}
		} else if(player.getTutorialStage() == 50) {
			if(npc.getId() == GIANT_RAT) {
				player.setTemporaryAttribute("SECOND_RAT", npc);
				refreshAll();	
			}
		} else if(player.getTutorialStage() == 67) {
			if(npc.getId() == CHICKEN || npc.getId() == 41) {
				player.setTemporaryAttribute("CHICKEN", npc);
				refreshAll();
			}
		}

		return true;
	}

	@Override
	public void start() {
		if(!player.hasCustomizedCharacter()) {
			CharacterDesign.openCharacterDesign(player);
			player.packets().sendBlackOut(2);
			player.setCantMove(true);
		}
		if(player.getTutorialStage() == 0 || player.getTutorialStage() == 1)
			player.packets().sendBConfig(168, 16); // Sets no selected tab
		else if(player.getTutorialStage() >= 2 && player.getTutorialStage() < 6)
			player.packets().sendBConfig(168, 12); // Sets selected tab as settings
		refreshFaceDirections();
		refreshAll();
		resetClicked(); // Resets if clicked an object, every 7 seconds.
		if(player.getTutorialStage() == 22
				|| player.getTutorialStage() == 23
					|| player.getTutorialStage() == 21)
			player.setCantMove(true);
		player.packets().sendUnlockIComponentOptionSlots(190, 18, 0, 201, 0, 1, 2, 3);
		if(player.getTutorialStage() > 19)
			player.interfaces().sendMusicPlayer();
		if(player.getTutorialStage() > 26) {
			player.interfaces().sendQuestTab();
			player.packets().sendConfig(281, 1);
		}
	}
	
	public void refreshFaceDirections() {
		for(NPC n : Game.getLocalNPCs(player)) {
			switch(n.getId()) {
				case SURVIVAL_EXPERT:
					n.faceDirection(Directions.WEST);
					break;
					
				case BANKER:
					n.faceDirection(Directions.SOUTH);
					break;
			}
		}
	}
	
	public void resetClicked() {
		Game.submit(new GameTick(7000) { // 7 Secs
			@Override
			public void run() {
				if(player.getTutorialStage() < 70 && clicked)
					clicked = false;
				else if(player.getTutorialStage() > 70)
					stop();
			}
		});
	}
	
	public void sendPercentage() {
		if(player.getTutorialStage() >= 5)
			player.packets().sendConfig(406, 2); // About 5%
		if(player.getTutorialStage() >= 8)
			player.packets().sendConfig(406, 3); // About 10%
		if(player.getTutorialStage() >= 15)
			player.packets().sendConfig(406, 4); // About 15%
		if(player.getTutorialStage() >= 18)
			player.packets().sendConfig(406, 5); // About 20%
		if(player.getTutorialStage() >= 21)
			player.packets().sendConfig(406, 6); // About 25%
		if(player.getTutorialStage() >= 25)
			player.packets().sendConfig(406, 7); // About 30%
		if(player.getTutorialStage() >= 29)
			player.packets().sendConfig(406, 8); // About 35%
		if(player.getTutorialStage() >= 35)
			player.packets().sendConfig(406, 9); // About 40%
		if(player.getTutorialStage() >= 39)
			player.packets().sendConfig(406, 10); // About 45%
		if(player.getTutorialStage() >= 46)
			player.packets().sendConfig(406, 11); // About 50%
		if(player.getTutorialStage() >= 50)
			player.packets().sendConfig(406, 12); // About 55%
		if(player.getTutorialStage() >= 52)
			player.packets().sendConfig(406, 14); // About 65%
		if(player.getTutorialStage() >= 54)
			player.packets().sendConfig(406, 15); // About 75%
		if(player.getTutorialStage() >= 56)
			player.packets().sendConfig(406, 16); // About 80%
		if(player.getTutorialStage() >= 62)
			player.packets().sendConfig(406, 17); // About 85%
		if(player.getTutorialStage() >= 64)
			player.packets().sendConfig(406, 18); // About 90%
		if(player.getTutorialStage() >= 66)
			player.packets().sendConfig(406, 20); // About 95%
	}
	
	public void refreshAll() {
		try {
			refreshHintIcons();
			refreshProgress();
			sendInterfaces();
			sendPercentage();
		} catch(Exception e) {
			e.printStackTrace();
		}
		player.getCombatDefinitions().sendUnlockAttackStylesButtons();
	}
	
	public void refreshHintIcons() {
		int stage = player.getTutorialStage();
		if(stage >= 0 && stage < 4) {
			NPC guide = findGlobalNPC(RUNESCAPE_GUIDE);
				if(guide != null)
					player.hints().addHintIcon(guide, 0, false, false);
		} else if(stage == 4) {
			player.hints().removeAll();
			player.hints().doorHint(3097, 3107, HintDirections.EAST);
		} else if(stage == 5) {
			player.hints().removeAll();
			NPC survivalExpert = findGlobalNPC(SURVIVAL_EXPERT);
			if(survivalExpert != null)
				player.hints().addHintIcon(survivalExpert, 0, false, false);
		} else if(stage == 7 && player.getInventory().containsItem(NORMAL_LOGS, 1)) {
			player.hints().removeAll();
			player.hints().doorHint(0, 0, (byte) 0);
		} else if(stage == 7) {
			player.hints().doorHint(3099, 3095, HintDirections.EAST);
		} else if(stage == 10) {
			player.hints().removeAll();
			NPC survivalExpert = findGlobalNPC(SURVIVAL_EXPERT);
			if(survivalExpert != null)
				player.hints().addHintIcon(survivalExpert, 0, false, false);
		} else if(stage == 11) {
			player.hints().removeAll();
			player.hints().addHintIcon(3101, 3092, 25, 45, HintDirections.CENTER, 0, false, false);
		} else if(stage == 14) {
			player.hints().removeAll();
			player.hints().addHintIcon(3089, 3092, 50, 75, HintDirections.EAST, 0, false, false);
		} else if(stage == 15) {
			player.hints().removeAll();
			player.hints().doorHint(3079, 3084, HintDirections.WEST);
		} else if(stage == 16) {
			player.hints().removeAll();
			NPC chef = findGlobalNPC(MASTER_CHEF);
			if(chef != null)
				player.hints().addHintIcon(chef, 0, false, false);
		} else if(stage == 18) { 
			player.hints().removeAll();
			player.hints().sendHint(3075, 3081, 45, HintDirections.CENTER);
		} else if(stage == 20) {
			player.hints().removeAll();
			player.hints().doorHint(3073, 3090, HintDirections.WEST);
		} else if(stage == 24) {
			player.hints().removeAll();
			player.hints().doorHint(3086, 3126, HintDirections.SOUTH);
		} else if(stage == 25) {
			player.hints().removeAll();
			NPC questGuide = findGlobalNPC(QUEST_GUIDE);
			if(questGuide != null)
				player.hints().addHintIcon(questGuide, 0, false, false);
		} else if(stage == 27) {
			player.hints().removeAll();
			NPC questGuide = findGlobalNPC(QUEST_GUIDE);
			if(questGuide != null)
				player.hints().addHintIcon(questGuide, 0, false, false);
		} else if(stage == 28) {
			player.hints().removeAll();
			player.hints().sendHint(3088, 3119, 0, HintDirections.CENTER);
		} else if(stage == 29) {
			player.hints().removeAll();
			NPC miningInstructor = findGlobalNPC(MINING_INSTRUCTOR);
			if(miningInstructor != null)
				player.hints().addHintIcon(miningInstructor, 0, false, false);
		} else if(stage == 30 || stage == 33) {
			player.hints().removeAll();
			player.hints().sendHint(3076, 9504, 45, HintDirections.CENTER);
		} else if(stage == 31 || stage == 34) {
			player.hints().removeAll();
			player.hints().sendHint(3086, 9501, 45, HintDirections.CENTER);
		} else if(stage == 32) {
			player.hints().removeAll();
			NPC miningInstructor = findGlobalNPC(MINING_INSTRUCTOR);
			if(miningInstructor != null)
				player.hints().addHintIcon(miningInstructor, 0, false, false);
		} else if(stage == 35) {
			player.hints().removeAll();
			player.hints().sendHint(3079, 9497, 127, HintDirections.SOUTH);
		} else if(stage == 36) {
			player.hints().removeAll();
			NPC miningInstructor = findGlobalNPC(MINING_INSTRUCTOR);
			if(miningInstructor != null)
				player.hints().addHintIcon(miningInstructor, 0, false, false);
		} else if(stage == 37) {
			player.hints().removeAll();
			player.hints().sendHint(3083, 9499, 45, HintDirections.CENTER);
		} else if(stage == 38) {
			player.hints().removeAll();
			player.hints().sendHint(3094, 9502, 127, HintDirections.EAST);
		} else if(stage == 39) {
			player.hints().removeAll();
			NPC combatInstructor = findGlobalNPC(COMBAT_INSTRUCTOR);
			if(combatInstructor != null)
				player.hints().addHintIcon(combatInstructor, 0, false, false);
		} else if(stage == 43) {
			player.hints().removeAll();
			NPC combatInstructor = findGlobalNPC(COMBAT_INSTRUCTOR);
			if(combatInstructor != null)
				player.hints().addHintIcon(combatInstructor, 0, false, false);
		} else if(stage == 46) {
			player.hints().removeAll();
			player.hints().doorHint(3111, 9518, HintDirections.WEST);
		} else if(stage == 47 || stage == 48) {
			player.hints().removeAll();
			NPC rat = findGlobalNPC(GIANT_RAT);
			if(rat != null)
				player.hints().addHintIcon(rat, 0, false, false);
		} else if(stage == 49) {
			player.hints().removeAll();
			NPC combatInstructor = findGlobalNPC(COMBAT_INSTRUCTOR);
			if(combatInstructor != null)
				player.hints().addHintIcon(combatInstructor, 0, false, false);
		} else if(stage == 50) {
			player.hints().removeAll();
			NPC rat = findGlobalNPC(GIANT_RAT); // Rat
			if(rat != null)
				player.hints().addHintIcon(rat, 0, false, false);
		} else if(stage == 51) {
			player.hints().removeAll();
			player.hints().sendHint(3111, 9526, 135, HintDirections.CENTER);
		} else if(stage == 52) {
			player.hints().removeAll();
			player.hints().sendHint(3122, 3124, 135, HintDirections.CENTER);
		} else if(stage == 53) {
			player.hints().removeAll();
			player.hints().doorHint(3125, 3124, HintDirections.WEST);
		} else if(stage == 54) {
			player.hints().removeAll();
			NPC adv = findGlobalNPC(FINANCIAL_ADVISOR);
			if(adv != null)
				player.hints().addHintIcon(adv, 0, false, false);
		} else if(stage == 55) {
			player.hints().removeAll();
			player.hints().doorHint(3130, 3124, HintDirections.WEST);
		} else if(stage == 56) {
			player.hints().removeAll();
			NPC brace = findGlobalNPC(BROTHER_BRACE);
			if(brace != null)
				player.hints().addHintIcon(brace, 0, false, false);
		} else if(stage == 58) {
			player.hints().removeAll();
			NPC brace = findGlobalNPC(BROTHER_BRACE);
			if(brace != null)
				player.hints().addHintIcon(brace, 0, false, false);
		} else if(stage == 61 || stage == 62) {
			player.hints().removeAll();
			NPC brace = findGlobalNPC(BROTHER_BRACE);
			if(brace != null)
				player.hints().addHintIcon(brace, 0, false, false);
		} else if(stage == 63) {
			player.hints().removeAll();
			player.hints().doorHint(3122, 3102, HintDirections.NORTH);
		} else if(stage == 64) {
			player.hints().removeAll();
			NPC magic = findGlobalNPC(MAGIC_INSTRUCTOR);
			if(magic != null)
				player.hints().addHintIcon(magic, 0, false, false);
		} else if(stage == 66) {
			player.hints().removeAll();
			NPC magic = findGlobalNPC(MAGIC_INSTRUCTOR);
			if(magic != null)
				player.hints().addHintIcon(magic, 0, false, false);
		} else if(stage == 67) {
			player.hints().removeAll();
			NPC chicken = findGlobalNPC(CHICKEN);
			if(chicken != null)
				player.hints().addHintIcon(chicken, 0, false, false);
		} else if(stage == 68) {
			player.hints().removeAll();
			NPC magic = findGlobalNPC(MAGIC_INSTRUCTOR);
			if(magic != null)
				player.hints().addHintIcon(magic, 0, false, false);
		} else {
			player.hints().removeAll();
			player.hints().addHintIcon(0, 0, 0, 0, 0, 0, false, false);
			player.hints().removeAll();
		}
	}
	
	public void refreshLTRStatus() {
		player.interfaces().sendInterface(275);
		for (int i = 0; i < 316; i++)
			player.packets().sendString("", 275, i);
		player.packets().sendRunScript(1207, 10);
		player.packets().sendString("<col=C20006>Learning the Ropes", 275, 2);
		player.packets().sendString("<str>I have learned about the following: using a hatchet to cut", 275, 17);
		player.packets().sendString("<str>trees; using a tinderbox and logs to make a fire; fishing", 275, 18);
		player.packets().sendString("<str>with a net at a Fishing spot; cooking fish on a fire; baking", 275, 19);
		player.packets().sendString("<str>bread on a range. I've also learned how to access my Stats", 275, 20);
		player.packets().sendString("<str>and Music Player interface tabs, and how to run and", 275, 21);
		player.packets().sendString("<str>perform emotes.", 275, 22);
		if(player.getTutorialStage() == 27) {
			player.packets().sendString("I can learn more about the Quest Journal by talking to the", 275, 23);
			player.packets().sendString("instructor indicated by the arrow.", 275, 24);
		} else if(player.getTutorialStage() == 28) {
			player.packets().sendString("<str>I can learn more about the Quest Journal by talking to the", 275, 23);
			player.packets().sendString("<str>instructor indicated by the arrow.", 275, 24);
			player.packets().sendString("<col=330099>I can climb down the</col> <col=660000>ladder</col> <col=330099>indicated by the</col> <col=660000>arrow.", 275, 25);
			player.packets().sendRunScript(1207, 11);
		} else if(player.getTutorialStage() >= 29) {
			player.packets().sendString("<str>I can learn more about the Quest Journal by talking to the", 275, 23);
			player.packets().sendString("<str>instructor indicated by the arrow.", 275, 24);
			player.packets().sendString("<str>I can climb down the ladder indicated by the arrow.", 275, 25);
			player.packets().sendString("<col=330099>I can go talk to the</col> <col=660000>Mining Instructor</col> <col=330099>indicated by the</col> <col=660000>arrow.", 275, 26);
			player.packets().sendRunScript(1207, 12);
		}
	}
	
	public NPC findGlobalNPC(int id) {
		for (NPC npc : Game.getLocalNPCs(player))
			if (npc.getId() == id)
				return npc;
		return null;
	}
	
	public void refreshProgress() {
		switch(player.getTutorialStage()) {
			case 0:
				sendTutLines("Getting Started", 0, new String[] { "", "", 
						"Please take a moment to design your character.", "", "", "" });
				break;
				
			case 1:
				sendTutLines("Getting Started", 1, new String[] { 
						"", "To start the tutorial, use your left mouse button to click on the",
							"RuneScape Guide in this room. He is indicated by a flashing yellow",
								"arrow above his head. If you can't see him, use your keyboard's arrow",
									"keys to rotate the view.", "" });
				break;
				
			case 2:
				sendTutLines("Game Options", 2, new String[] { "", "",
							"Please click on the flashing spanner icon found at the bottom-right of",
								"your screen. This will display your game options.", "", "" });
				break;
				
			case 3:
				player.packets().sendConfig(1021, 0);
				sendTutLines("Game Options", 3, new String[] {
						"In the interface, you can now see a variety of options such as screen",
						"brightness, sound and music volume and whether you want to accept", 
						"aid from other players or not. Don't worry about these too much for ",
						"now; they will become clearer as you explore the game. Talk to the",
						"RuneScape Guide to continue.", ""});
				break;
				
			case 4:
				sendTutLines("Interacting with scenery", 4, new String[] { "",
						"You can interact with many items of scenery by simply clicking",
						"on them. Right clicking will also give you more options. Feel free to",
						"try it with the things in the room, then click on the door indicated",
						"with the yellow arrow to go through to the next instructor.", 
						"", });
				break;
				
			case 5:
				sendTutLines("Moving around", 5, new String[] {
						"Follow the path to find the next instructor. Clicking on the ground will",
						"walk you to that point. You can also navigate by clicking on the", 
						"minimap in the top-right corner of your screen. Talk to the Survival", 
						"Expert by the pond to continue the tutorial. Remember, you can", 
						"rotate the view by pressing the arrow keys.", "" });
				break;
			
			case 6:
				sendTutLines("", 6, 
						new String[] { "<col=0000FF>Viewing the items that you were given",
						"Click on the flashing backpack icon to the right-hand side of",
						"the main window to view your inventory. Your inventory is a list",
						"of everything you have in your backback.", "", "" });
				break;
				
			case 7:
				sendTutLines("", 7, new String[] { "<col=0000FF>Cut down a tree</col>",
						"You can click on the backpack icon at any time to view the", 
						"items that you currently have in your inventory. You will see", 
						"that you now have a hatchet in your inventory. Use this to get", 
						"some logs by clicking on one of the trees in the area.", 
						"" });
				break;
				
			case 8:
				sendTutLines("", 8, new String[] { 
						"<col=0000FF>Making a fire</col>",
						"Well done! You managed to cut some logs from the tree! Next,",
						"use the tinderbox in your inventory to light the logs.",
						"First click on the tinderbox to 'use' it.",
						"Then click on the logs in your inventory to light them.",
						"" });
				break;
				
			case 9:
				sendTutLines("", 9, new String[] { "", "You gained some experience.",
						"Click on the flashing bar graph icon near the inventory button",
						"to see your skill stats.", "", "" });
				break;
				
			case 10:
				sendTutLines("Your skill stats", 10, new String[] { 
						"Here you will see how good your skills are. As you move your",
						"mouse over any of the icons in this tab, the small yellow popup", 
						" box will show you the exact amount of experience you have", 
						" and how much is needed to get to the next level. Speak to the", 
						" Survival Expert to continue.", "" 
						});
				break;
				
			case 11:
				sendTutLines("", 11, new String[] { 
						"<col=0000ff>Catch some shrimp</col>",
						"Click on the bubbling fishing spot, indicated by the flashing",
						"arrow. Remember, you can check your inventory by clicking the",
						"backpack icon.", "", "" });
				break;
				
			case 12:
				sendTutLines("", 12, new String[] { 
						"<col=0000ff>Cooking your shrimp</col>", 
						"Now that you've caught some shrimp, let's cook it. First light a",
						"fire: chop down a tree and then use the tinderbox on the logs.",
						"If you've lost your hatchet or tinderbox, Brynaa will give you", 
						"another.", "" });
				break;
				
			case 13:
				sendTutLines("", 13, new String[] { 
						"<col=0000ff>Burning your shrimp</col>",
						"You have just burnt your first shrimp. This is normal. As you",
						"get more experience in Cooking, you will burn stuff less often.",
						"Let's try cooking without burning it this time. First catch some",
						"more shrimp, then use them on a fire.", "" });
				break;
				
			case 14:
				sendTutLines("", 14, new String[] { 
						"<col=0000ff>Well done, you've just cooked your first RuneScape meal.</col>",
						"If you'd like a recap on anything you've learnt so far, speak to",
						"the Survival Expert. You can now move on to the next",
						"instructor. Click on the gate shown and follow the path.",
						"Remember, you can move the camera with the arrow keys.", "" });
				break;
				
			case 15:
				sendTutLines("", 15, new String[] { 
						"<col=0000ff>Find your next instructor</col>",
						"Follow the path until you get to the door with the yellow arrow",
						"above it. Click on the door to open it. Notice the mini map in the",
						"top right; This shows a top down view of the area around you.",
						"This can also be used for navigation.", "" });
				break;
				
			case 16:
				sendTutLines("", 16, new String[] {
						"<col=0000ff>Find your next instructor</col>",
						"Talk to the chef indicated. He will teach you the more advanced",
						"aspects of Cooking such as combining ingredients. He will also",
						"teach you about your Music Player.", "", ""});
				break;
				
			case 17:
				sendTutLines("", 17, new String[] { 
						"<col=0000ff>Making dough</col>", 
						"This is the base for many of the meals. To make dough we must",
						"mix flour and water. First, right click the bucket of water and",
						"select use, then left click on the pot of flour.", "", "" });
				break;
				
			case 18:
				sendTutLines("", 18, new String[] { 
						"<col=0000ff>Cooking dough</col>",
						"Now you have made dough, you can cook it. To cook the dough,",
						"use it with the range shown by the arrow. If you lose your",
						"dough, talk to Lev - he will give you more ingredients.", "", "" });
				break;
				
			case 19:
				sendTutLines("Cooking dough", 19, new String[] {
						"Well done! Your first loaf of bread. As you gain experience in",
						"Cooking, you will be able to make other things like pies, cakes",
						"and even kebabs. Now you've got the hang of cooking, let's",
						"move on. Click on the flashing icon in the bottom right to see",
						"the Music Player.", "" });
				break;
				
			case 20:
				sendTutLines("The Music Player", 20, new String[] {
						"From this interface you can control the music that is played. As you",
						"explore the world and complete quests, more of the tunes will become", 
						"unlocked. Once you've examined this menu, use the next door to", 
						"continue. If you need a recap on anything covered here, talk to Lev.", 
						"", "" });
				break;
				
			case 21:
				sendTutLines("", 21, new String[] {
						"<col=0000ff>Emotes</col>",
						"",
						"Now, how about showing some feelings? You will see a flashing",
							"icon in the shape of two masks. Click on that to access your", 
							"emotes.", ""});
				break;
				
			case 22:
				sendTutLines("Emotes", 22, new String[] { 
						"For those situations where words don't quite describe how you feel, try",
						"an emote. Go ahead, try one out! You might notice that some of the",
						"emotes are grey and cannot be used now. Don't worry! As you",
						"progress further into the game, you'll gain accesss to all sorts of things,",
						"including more fun emotes like these.", "" });
				break;
				
			case 23:
				sendTutLines("", 23, new String[] { 
						"<col=0000ff>Running</col>", 
						"It's only a short distance to the next guide.",
						"Why not try running there? To do this, click on the run icon next to",
						"the minimap.", "", "" });
				break;
				
			case 24:
				sendTutLines("Run to the next guide", 24, new String[] { "",
						"Now that you have the run button turned on, follow the path until",
						"you come to the end. You may notice that the number on the button",
						"goes down. This is your run energy. If your run energy reaches",
						"zero, you'll stop running. Click on the door to pass through it.", ""});
					break;
					
			case 25:
				sendTutLines("Talk with the Quest Guide.", 25, new String[] {
						"", "", 
						"He will tell you all about quests.", "", "", "" });
				break;
				
			case 26:
				sendTutLines("", 26, new String[] { "",
						"<col=0000ff>Open the Quest Journal.</col>",
						"Click on the flashing icon next to your inventory.",
						"", "", "" });
				break;
				
			case 27:
				sendTutLines("", 27, new String[] {
						"<col=0000ff>Your Quest Journal", "",
						"This is your Quest Journal, a list of all the quests in the game.",
						"Talk to the Quest Guide again for an explanation.", "", "" });
				break;
				
			case 28:
				sendTutLines("", 28, new String[] { "",
						"<col=0000ff>Moving on</col>", 
						"It's time to enter some caves. Click on the ladder to go down to",
						"the next area.", "", "", "" });
				break;
				
			case 29:
				sendTutLines("", 29, new String[] { 
						"<col=0000ff>Mining and Smithing</col>",
						"Next let's get you a weapon, or more to the point, you can",
						"make your first weapon yourself. Don't panic, the Mining",
						"Instructor will help you. Talk to him and he'll tell you all about it.",
						"", "" });
				break;
				
			case 30:
				sendTutLines("", 30, new String[] { 
						"<col=0000ff>Prospecting</col>",
						"To prospect a mineable rock, just right click it and select the",
						"'prospect rock' option. This will tell you the type of ore you can",
						"mine from it. Try it now on one of the rocks indicated.", "", "" });
				break;
				
			case 31:
				sendTutLines("", 31, new String[] { 
						"<col=0000ff>It's tin.", "",
						"So now you know there's tin in the grey rocks, try prospecting",
						"the brown ones next.", "", "" });
				break;
				
			case 32:
				sendTutLines("", 32, new String[] { 
						"<col=0000ff>It's copper.", 
						"Talk to the Mining Instructor to find out about these types of",
						"ore and how you can mine them. He'll even give you the",
						"required tools.", "", "" });
				break;
				
			case 33:
				sendTutLines("", 33, new String[] { 
						"<col=0000ff>Mining</col>",
						"It's quite simple really. All you need to do is right click on the",
						"rock and select 'mine'. You can only mine when you have a",
						"pickaxe. So give it a try: first mine one tin ore.", "", "" });
				break;
				
			case 34:
				sendTutLines("", 34, new String[] { 
						"<col=0000ff>Mining</col>",
						"Now you have some tin ore you just need some copper ore.",
						"then you'll have all you need to create a bronze bar. As you",
						"did before, right click on the copper rock and select 'mine'.", "", "" });
				break;
				
			case 35:
				sendTutLines("", 35, new String[] { 
						"<col=0000ff>Smelting</col>",
						"You should now have both some copper and tin ore. So let's",
						"smelt them to make a bronze bar. To do this, right click on",
						"either tin or copper ore and select use then left click on the", 
						"furnace. Try it now.", "" });
				break;
				
			case 36:
				sendTutLines("", 36, new String[] {
						"<col=0000ff>You've made a bronze bar!</col>",
						"",
						"Speak to the Mining Instructor and he'll show you how to make",
						"it into a weapon.",
						"", "" });
				break;
				
			case 37:
				sendTutLines("", 37, new String[] { 
						"<col=0000ff>Smithing a dagger</col>",
						"To smith you'll need a hammer - like the one you were given by", 
						"Dezzick - access to an anvil like the one with the arrow over it",
						"and enough metal bars to make what you are trying to smith.",
						"To start the process, use the bar on one of the anvils.", "" });
				break;
				
			case 38:
				sendTutLines("", 38, new String[] { 
						"<col=0000ff>You've finished in this area.</col>",
						"So let's move on. Go through the gates shown by the arrow.",
						"Remember, you may need to move the camera to see your",
						"surroundings. Speak to the guide for a recap at any time.", "", "" });
				break;
				
			case 39:
				sendTutLines("", 39, new String[] { 
						"<col=0000ff>Combat</col>", "",
						"In this area you will find out about combat with swords and bows.",
						"Speak to the guide and he will tell you all about it.", "", "" });
				break;
				
			case 40:
				sendTutLines("", 40, new String[] { 
						"<col=0000ff>Wielding weapons</col>", "",
						"You now have access to a new interface. Click on the flashing icon of",
						"a helmet, the one to the right of your backpack icon.", "", "" });
				break;
				
			case 41:
				sendTutLines("", 41, new String[] { 
						"<col=0000ff>This your worn equipment.</col>",
						"From here you can see what items you have equipped. You will notice", 
						"the button 'Show Equipment Stats'. Click on this now to display the", 
						"details of what you have equipped.", 
						"", "" });
				break;
				
			case 42:
				sendTutLines("", 42, new String[] { 
						"<col=0000ff>Worn interface</col>", 
						"You can see what items you are wearing in the worn equipment to",
						"the left of the screen, with their combined statistics on the right. Let's",
						"add something. Left click your dagger to 'wield' it.", "", "" });
					break;
					
			case 43:
				sendTutLines("", 43, 
						new String[] {  
						"<col=0000ff>You're now holding your dagger.</col>",
						"Clothes, armour, weapons and many other items are equipped like this.",
						"You can unequip items by clicking on the item in the worn equipment.",
						"You can close this window by clicking on the small 'x' in the top-right",
						"hand corner. Speak to the Combat Instructor to continue.", "" });
				break;
				
			case 44:
				sendTutLines("", 44, 
						new String[] { 
						"<col=0000ff>Unequipping items</col>",
						"In your worn equipment tab, right click on the dagger and select the",
						"remove option from the drop down list. After you've unequipped the",
						"dagger, wield the sword and shield. As you pass the mouse over an",
						"item, you will see its name appear at the top left of the screen.", "" });
				break;
				
			case 45:
				sendTutLines("Combat styles", 45, new String[] { "", "", 
						"Click on the flashing crossed swords icon to see your combat styles.", "", "", "" });
				break;
				
			case 46:
				sendTutLines("These are your combat styles.", 46, new String[] { 
						"From this interface you can select the type of attack your character",
						"will use, and whether or not to auto-retaliate. Different monsters have",
						"different weaknesses. If you hover your mouse over the buttons, you",
						"will see the type of XP you will receive when using each type of", 
						"attack. Now you have the tools needed for battle, why not slay some", 
						"rats? Click on the gates indicated to continue." });
				break;
				
			case 47:
				sendTutLines("", 47, new String[] { "<col=0000FF>Attacking</col>",
						"To attack the rat, right click it and select the attack option. You will",
						"then walk over to it and start hitting it.",
						"", "", "" });
				break;
				
			case 48:
				sendTutLines("", 48, new String[] { 
						"<col=0000ff>Sit back and watch.</col>",
						"While you are fighting, you will see a bar over your head. The bar",
						"shows how much health you have left. Your opponent will have one too.",
						"You will continue to attack the rat until it's dead or you do something",
						"else.", "" });
				break;
				
			case 49:
				sendTutLines("Well done, you've made your first kill!",  49, 
						new String[] { 
						"", "", "Pass through the gate and talk to the Combat Instructor; he will give",
						"you your next task.", "", "" });
				break;
				
			case 50:
				sendTutLines("Rat ranging", 50, new String[] { 
						"",
						"Now you have a bow and some arrows. Before you can use them",
						"you'll need to equip them. Once equipped with the ranging gear, try",
						"killing another rat. Remember: to attack, right click on the monster",
						"and select attack.", "" });
					break;
					
			case 51:
				sendTutLines("Moving on",  51, 
						new String[] { 
						"",
						"You have completed the tasks here. To move on, click on the ladder", 
						"shown. If you need to go over any of what you learnt here, just talk",
						"to the Combat Instructor and he'll tell you what he can.", "", "" });
				break;
				
			case 52:
				sendTutLines("Banking", 52, new String[] { "",
						"Follow the path and you will come to the front of the building. This is",
						"the Bank of RuneScape, where you can store all your most valued",
						"items. To open your bank box, just right click on an open booth",
						"indicated and select 'use'.",
						"", "" });
				break;
				
			case 53:
				sendTutLines("This is your bank box.", 53, new String[] { 
						"", 
						"You can store stuff in here for safekeeping. If you die, anything in your",
						"bank will be saved. To deposit something, right click it and select",
						"'Deposit-1'. Once you've had a good look, close the window and move",
						"on through the door indicated.", "" });
				break;
				
			case 54:
				sendTutLines("Financial advice", 54, new String[] {
						"", "", 
						"The guide here will tell you all about making cash. Just click on him to", 
						"hear what he's got to say.", "", "" });
				break;
				
			case 55:
				sendTutLines("", 55, new String[] { "", "", 
						"Continue through the next door.", "", "", "" });
				break;
				
			case 56:
				sendTutLines("Prayer", 56, new String[] {
						"", "", 
						"Follow the path to the chapel and enter it.", 
						"Once inside talk to the monk. He'll tell you all about the Prayer skill.", 
						"", "" });
				break;
				
			case 57:
				sendTutLines("Your Prayer List", 57, new String[] { 
						"", "", "Click on the flashing icon to open your Prayer List.", "", "", "" });
				break;
				
			case 58:
				sendTutLines("Your Prayer List", 58, new String[] {
						"",
						"", 
						"Talk with Brother Brace and he'll tell you all about prayers.",
						"",
						"", ""});
				break;
				
			case 59:
				sendTutLines("Friends List", 59, new String[] { 
						"", "", "You should now see another new icon. Click on the flashing green", 
						"face to open your Friends List.", "", "" });
				break;
				
			case 60:
				sendTutLines("This is your Friends List.", 60, new String[] {
						"",
						"", 
						"This will be explained by Brother Brace shortly, but first click on the",
						"other flashing face in the interface.",
						"", ""});
				break;
				
			case 61:
			case 62:
				sendTutLines("This is your Ignore List.", 61, new String[] {
						"",
						"The two lists - Friends and Ignores - can be very helpful for keeping", 
						"track of when your friends are online or for blocking messages from",
						"people you simply don't like. Speak with Brother Brace and he will tell",
						"you more.", 
						""});
				break;
				
			case 63:
				sendTutLines("Your final Instructor!", 63, new String[] { 
						"",
						"",
						"You're almost finished on tutorial island. Pass through the door to find",
						"the path leading to your final instructor.",
						"",
						"" });
				break;
				
			case 64:
				sendTutLines("Your final instructor!", 64, new String[] {
						"", "", 
						"Just follow the path to the Wizard's house, where you will be shown", 
						"how to cast spells. Just talk with the mage indicated to find out more.", 
						"", "" });
				break;
				
			case 65:
				sendTutLines("Open up your final tab.", 65, new String[] { 
						"", "", "Open up the Magic Spellbook tab by clicking on the flashing icon next",
						"to the Prayer List tab you just learned about.", "", "" });
				break;
				
			case 66:
				player.getDialogue().start(new Dialogue() {
					public void start() {
						npc(MAGIC_INSTRUCTOR, "Good. This is a list of your spells. Currently you can",
							"only cast one offensive spell called Wind Strike. Let's",
							"try it out one of those chickens.");
					}
					public void process(int i, int b) {
						if(stage == 0) {
							player.setTutorialStage(67);
							sendItems(new int[] { 556, 5, 558, 5 }, 
								"Terrova gives you five <col=0000ff>air runes</col> "
								+ "and five <col=0000ff>mind runes</col!");
							player.getInventory().add(556, 5);
							player.getInventory().add(558, 5);
							stage = 1;
						} else if(stage == 1) {
							end();
							sendTutLines("Cast Wind Strike at a chicken.", 67, new String[] { "",
								"Now you have runes you should see the Wind Strike icon at the top-",
								"left of your spellbook, second in from the left. Walk over to the caged",
								"chickens, click on the Wind Strike icon and then select one of the chickens",
								"to cast it on. It may take several tries. If you need more runes ask",
								"Terrova." });
							refreshAll();
						}
					}
					public void finish() { }
				});
				break;
				
			case 67:
				sendTutLines("Cast Wind Strike at a chicken.", 67, new String[] { "",
						"Now you have runes you should see the Wind Strike icon at the top-",
						"left of your spellbook, second in from the left. Walk over to the caged",
						"chickens, click on the Wind Strike icon and then select one of the chickens",
						"to cast it on. It may take several tries. If you need more runes ask",
						"Terrova." });
				break;
				
			case 68:
				sendTutLines("You have almost completed the tutorial!", 68, 
						new String[] { 
						"", "",
						"All you need to do now is teleport to the mainland. Just speak with", 
						"Terrova and he'll tell you how to do that.", 
						"", "" });
				break;
				
			case 69:
				sendTutLines("You have almost completed the tutorial!", 69, 
						new String[] { 
						"", "Just click on the first spell, Home Teleport in your Magic Spellbook.",
						"This spell doesn't require any runes, but can only be cast once every",
						"30 minutes.", "", "" });
				break;
		}
	}
	
	@Override
	public boolean processObjectExamine(GlobalObject object) {
		if(player.getTutorialStage() >= 4)
			player.sm(object.getExamine());
		else
			player.sm("You can't do that at the moment.");
		return false;
	}
	
	@Override
	public boolean canDropItem(Item item) {
		if(player.getTutorialStage() < 17) {
			player.sm("You can't do that at the moment.");
			return false;
		}
		return true;
	}

	@Override
	public boolean canEquip(int slotId, int itemId) {
		if(player.getTutorialStage() < 42)
			player.sm("You'll be told how to equip items later.");
		else {
			if(player.getTutorialStage() == 42) {
				sendTutLines("", 43, 
					new String[] {  
					"<col=0000ff>You're now holding your dagger.</col>",
					"Clothes, armour, weapons and many other items are equipped like this.",
					"You can unequip items by clicking on the item in the worn equipment.",
					"You can close this window by clicking on the small 'x' in the top-right",
					"hand corner. Speak to the Combat Instructor to continue.", "" });
				refreshAll();
				return true;
			} else if(player.getTutorialStage() == 43)
				return true; 
			else if(player.getTutorialStage() == 44) {
				sendTutLines("Combat styles", 45, new String[] { "", "", 
						"Click on the flashing crossed swords icon to see your combat styles.", "", "", "" });
				player.interfaces().sendAttackTab();
				player.packets().sendConfig(1021, 1); // Flashing attack styles icon
				return true;
			} else if(player.getTutorialStage() > 44)
				return true;
			else
				return false;
		}
		return false;
	}
	
	@Override
	public boolean processItemOnNPC(NPC npc, Item item) {
		return false;
	}
	
	@Override
	public boolean processObjectClick2(final GlobalObject object) {
		ObjectDefinition def = object.defs();
		if(def.containsOption("prospect")) {
			if(player.getTutorialStage() == 30) {
				player.addWalkSteps(object.getX(), object.getY());
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.withinDistance(object, 1)) {
							player.faceObject(object);
							stop();
							sendTutLines("", player.getTutorialStage(), new String[] { 
								"<col=0000ff>Please wait.</col>", "", 
								"Your character is now attempting to prospect the rock. This",
								"should only take a few seconds.", "", "" });
							try {
								Thread.sleep(4100);
								player.getDialogue().start(new InfoText(), 
									(Object) new String[] { 
									"This rock contains "+(object.getId() == 3043 ? "tin." : 
										object.getId() == 3042 ? "copper." : "an unknown mineral.") });
								sendTutLines("", 31, new String[] { 
									"<col=0000ff>It's tin.", "",
									"So now you know there's tin in the grey rocks, try prospecting",
									"the brown ones next.", "", "" });
								refreshAll();
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				});
			} else if(player.getTutorialStage() == 31) {
					player.addWalkSteps(object.getX(), object.getY());
					player.faceObject(object);
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.withinDistance(object, 1)) {
								player.faceObject(object);
								stop();
								sendTutLines("", player.getTutorialStage(), new String[] { 
									"<col=0000ff>Please wait.</col>", "", 
									"Your character is now attempting to prospect the rock. This",
									"should only take a few seconds.", "", "" });
								try {
									Thread.sleep(4100);
									player.getDialogue().start(new InfoText(), 
										(Object) new String[] { 
										"This rock contains "+(object.getId() == 3043 ? "tin." : 
											object.getId() == 3042 ? "copper." : "an unknown mineral.") });
									player.setTutorialStage(32);
									refreshHintIcons();
									sendTutLines("", 32, new String[] { 
										"<col=0000ff>It's copper.", 
										"Talk to the Mining Instructor to find out about these types of",
										"ore and how you can mine them. He'll even give you the",
										"required tools.", "", "" });
									refreshAll();
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					});
				} else {
					player.addWalkSteps(object.getX(), object.getY());
					player.faceObject(object);
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.withinDistance(object, 1)) {
								player.faceObject(object);
								stop();
								sendTutLines("", player.getTutorialStage(), new String[] { 
									"<col=0000ff>Please wait.</col>", "", 
									"Your character is now attempting to prospect the rock. This",
									"should only take a few seconds.", "", "" });
								try {
									Thread.sleep(3400);
									player.getDialogue().start(new InfoText(), 
										(Object) new String[] { 
										"This rock contains "+(object.getId() == 3043 ? "tin." : 
											object.getId() == 3042 ? "copper." : "an unknown mineral.") });
									refreshAll();
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
			}
			return false;
	}
	
	public void sendBronzeSmithingInterface() {
		player.interfaces().sendInterface(300);
		player.packets().sendString("Bronze Smithing", 300, 14);
		player.packets().sendString("<col=ffffff>Dagger", 300, 19);
		player.packets().itemOnInterface(300, 18, 1, 1205); // Bronze Dagger
		player.packets().itemOnInterface(300, 26, 1, 1351); // Bronze Hatchet
		player.packets().itemOnInterface(300, 34, 1, 1422); // Bronze Mace
		player.packets().itemOnInterface(300, 42, 1, 1139); // Bronze Med Helm
		player.packets().itemOnInterface(300, 50, 10, 877); // Bronze bolts
		player.packets().itemOnInterface(300, 58, 1, 1277); // Bronze Sword
		player.packets().itemOnInterface(300, 74, 15, 4819); // Bronze Nails
		player.packets().itemOnInterface(300, 106, 15, 39); // Bronze Arrow Tips
		player.packets().itemOnInterface(300, 114, 1, 1321); // Bronze Dagger
		player.packets().itemOnInterface(300, 122, 1, 9420); // Bronze Cross-bow Limbs
		player.packets().itemOnInterface(300, 130, 1, 1291); // Bronze Long-sword
		player.packets().itemOnInterface(300, 138, 5, 864); // Bronze Throwing knife
		player.packets().itemOnInterface(300, 146, 1, 1155); // Bronze Full Helm
		player.packets().itemOnInterface(300, 154, 1, 1173); // Bronze Square shield
		player.packets().itemOnInterface(300, 178, 1, 1337); // Bronze War-hammer
		player.packets().itemOnInterface(300, 186, 1, 1375); // Bronze Battle-axe
		player.packets().itemOnInterface(300, 194, 1, 1103); // Bronze Chain-body
		player.packets().itemOnInterface(300, 202, 1, 1189); // Bronze Kite-shield
		player.packets().itemOnInterface(300, 218, 1, 1307); // Bronze 2h Sword
		player.packets().itemOnInterface(300, 226, 1, 1087); // Bronze Plate-skirt
		player.packets().itemOnInterface(300, 234, 1, 1075); // Bronze Plate-legs
		player.packets().itemOnInterface(300, 242, 1, 1117); // Bronze Plate-body
	}
	
	@Override
	public boolean processItemOnObject(final Item item, final GlobalObject object) {
		
		if(item.getId() == 2349 && object.getId() == 2783) {
			if(clicked)
				return false;
			clicked = true;
			if(player.getTutorialStage() == 37) {
				if(!player.getInventory().containsItem(2349, 1))
					return false;
				player.addWalkSteps(object.getX(), object.getY());
				Game.submit(new GameTick(600) {
					@Override
					public void run() {
						if(player.withinDistance(object, 1)) {
							player.faceObject(object);
							stop();
							try {
								Thread.sleep(1300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							sendBronzeSmithingInterface();
							clicked = false;
						}
					}
					
				});
			}
		}
		
		if(item.getId() == 438 && object.getId() == 3044 
				|| item.getId() == 436 && object.getId() == 3044) {
			if(clicked)
				return false;
			clicked = true;
			if(player.getTutorialStage() == 35) {
				if(!player.getInventory().containsItem(436, 1) || !player.getInventory().containsItem(438, 1)) {
					player.sm("You need tin and copper ore to smelt into a bronze bar.");
					return false;
				}
				player.addWalkSteps(object.getX() + 1, object.getY() + 2);
				Game.submit(new GameTick(600) {
					@Override
					public void run() {
						if(player.getX() == object.getX() + 1
								&& player.getY() == object.getY() + 2) {
							player.faceObject(object);
							stop();
							try {
								Thread.sleep(1100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							player.setNextAnimation(new Animation(899));
							player.sm("You smelt the copper and tin together in the furnace.");
							Game.submit(new GameTick(2500) {
								@Override
								public void run() {
									stop();
									clicked = false;
									player.getInventory().deleteItem(436, 1); // Copper Ore
									player.getInventory().deleteItem(438, 1); // Tin Ore
									player.getInventory().add(2349, 1);
									player.getSkills().addXp(Skills.SMITHING, 35);
									sendTutLines("", 36, new String[] {
										"<col=0000ff>You've made a bronze bar!</col>",
										"",
										"Speak to the Mining Instructor and he'll show you how to make",
										"it into a weapon.",
										"", "" });
									player.sm("You retrieve a bar of bronze.");
									refreshAll();
								}
							});
						}
					}
					
				});
			}
		}
		
		if(item.getId() == BREAD_DOUGH && object.getId() == 3039) {
			if(clicked)
				return false;
			clicked = true;
			if(player.getTutorialStage() == 18) {
				if(!player.getInventory().containsItem(BREAD_DOUGH, 1))
					return false;
				player.addWalkSteps(object.getX(), object.getY() + 1);
				Game.submit(new GameTick(600) {
					@Override
					public void run() {
						if(player.getX() == object.getX() 
								&& player.getY() == object.getY() + 1) {
							player.faceObject(object);
							stop();
							try {
								Thread.sleep(1400);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							player.setNextAnimation(RANGE_COOKING_ANIMATION);
							Game.submit(new GameTick(2000) {
								@Override
								public void run() {
									stop();
									clicked = false;
									int slot = player.getInventory().lookupSlot(BREAD_DOUGH);
									player.getInventory().getItems().set(slot, new Item(2309, 1));
									player.getInventory().refresh();
									player.getSkills().addXp(Skills.COOKING, 50);
									sendTutLines("Cooking dough", 19, new String[] {
										"Well done! Your first loaf of bread. As you gain experience in",
										"Cooking, you will be able to make other things like pies, cakes",
										"and even kebabs. Now you've got the hang of cooking, let's",
										"move on. Click on the flashing icon in the bottom right to see",
										"the Music Player.", "" });
									player.interfaces().sendMusicPlayer();
									player.packets().sendConfig(1021, 15); // Flashing Music Player
									refreshAll();
								}
							});
						}
					}
					
				});
			}
		}
		if(item.getId() == 317 && object.getId() == 2732) {
			if(player.getTutorialStage() == 12) {
				player.addWalkSteps(object.getX() + 1, object.getY());
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.withinDistance(object, 1)) {
							stop();
							player.setNextAnimation(FIRE_COOKING_ANIMATION);
							Game.submit(new GameTick(1500) {
								@Override
								public void run() {
									clicked = false;
									player.faceObject(object);
									player.getInventory().deleteItem(317, 1);
									player.getInventory().addItem(7954, 1);
									sendTutLines("", 13, new String[] { 
										"<col=0000ff>Burning your shrimp</col>",
										"You have just burnt your first shrimp. This is normal. As you",
										"get more experience in Cooking, you will burn stuff less often.",
										"Let's try cooking without burning it this time. First catch some",
										"more shrimp, then use them on a fire.", "" });
									refreshAll();
									stop();
								}
							});
						}
					}
				});
			} else if(player.getTutorialStage() == 13) {
				player.addWalkSteps(object.getX() - 1, object.getY());
				player.faceObject(object);
				Game.submit(new GameTick(600) {
					@Override
					public void run() {
						if(player.withinDistance(object)) {
							player.setNextAnimation(FIRE_COOKING_ANIMATION);
							stop();
							Game.submit(new GameTick(1500) {
								@Override
								public void run() {
									clicked = false;
									player.getInventory().deleteItem(317, 1);
									player.getInventory().addItem(315, 1);
									player.getSkills().addXp(Skills.COOKING, 25);
									sendTutLines("", 14, new String[] { 
										"<col=0000ff>Well done, you've just cooked your first RuneScape meal.</col>",
										"If you'd like a recap on anything you've learnt so far, speak to",
										"the Survival Expert. You can now move on to the next",
										"instructor. Click on the gate shown and follow the path.",
										"Remember, you can move the camera with the arrow keys.", "" });
									refreshAll();
									stop();
								}
								
							});
						}
					}
				});
			} else if(player.getTutorialStage() >= 14) {
				player.addWalkSteps(object.getX() - 1, object.getY());
				player.faceObject(object);
				if(player.getInventory().containsOneItem(315, 2)) {
					player.sm("You cannot carry any more shrimp.");
					return false;
				}
				Game.submit(new GameTick(600) {
					@Override
					public void run() {
						if(player.getX() == object.getX() - 1) {
							player.setNextAnimation(FIRE_COOKING_ANIMATION);
							stop();
							Game.submit(new GameTick(1500) {
								@Override
								public void run() {
									player.getInventory().deleteItem(317, 1);
									player.getInventory().addItem(315, 1);
									player.getSkills().addXp(Skills.COOKING, 25);
									refreshAll();
									stop();
								}
								
							});
						}
					}
				});
			}
			return false;
		}
		return false;
	}
	
	@Override
	public boolean canUseItemOnItem(Item i1, Item i2) {
		if(i1.getId() == BUCKET_OF_WATER && i2.getId() == POT_OF_FLOUR
				|| i1.getId() == POT_OF_FLOUR && i2.getId() == BUCKET_OF_WATER) {
			if(player.getTutorialStage() < 17) {
				player.sm("You can't do that at the moment.");
				return false;
			} else if(player.getTutorialStage() == 17) {
				int slot = player.getInventory().lookupSlot(BUCKET_OF_WATER);
				player.getInventory().getContainer().set(slot, new Item(EMPTY_BUCKET, 1));
				int slot2 = player.getInventory().lookupSlot(POT_OF_FLOUR);
				player.getInventory().getContainer().set(slot2, new Item(EMPTY_POT, 1));
				player.getInventory().add(BREAD_DOUGH, 1);
				player.getInventory().refresh();
				sendTutLines("", 18, new String[] { 
					"<col=0000ff>Cooking dough</col>",
					"Now you have made dough, you can cook it. To cook the dough,",
					"use it with the range shown by the arrow. If you lose your",
					"dough, talk to Lev - he will give you more ingredients.", "", "" });
				refreshAll();
			} else if(player.getTutorialStage() >= 17) {
				int slot = player.getInventory().lookupSlot(BUCKET_OF_WATER);
				player.getInventory().getItems().set(slot, new Item(EMPTY_BUCKET, 1));
				int slot2 = player.getInventory().lookupSlot(POT_OF_FLOUR);
				player.getInventory().getItems().set(slot2, new Item(EMPTY_POT, 1));
				player.getInventory().add(BREAD_DOUGH, 1);
				player.getInventory().refresh();
				refreshAll();
			}
		}
		if(i1.getId() == NORMAL_LOGS && i2.getId() == TINDERBOX 
				|| i1.getId() == TINDERBOX && i2.getId() == NORMAL_LOGS) {
			if(player.getTutorialStage() == 8) {
				if (!Game.canMoveNPC(player.getZ(), player.getX(), player.getY(), 1)
						|| Game.getRegion(player.getRegionId()).getSpawnedObject(player) != null) {
					player.packets().sendMessage("You can't light a fire here.");
					return false;
				}
				player.getInventory().deleteItem(NORMAL_LOGS, 1);
				Game.addGroundItem(new Item(NORMAL_LOGS, 1), new Location(player), player, false, 180, true);
				player.setNextAnimation(FIREMAKING_ANIMATION);
				sendTutLines("", 8, new String[] { 
						"<col=0000FF>Please wait.</col>", "", "Your character is now attempting to light the fire.", 
						"This should only take a few seconds.", "", "" });
				Game.submit(new GameTick(7540) {
					@Override
					public void run() {
						final Location tile = new Location(player);
						if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
							if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
								if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
									player.addWalkSteps(player.getX(), player.getY() - 1, 1);
							final FloorItem item = Game.getRegion(tile.getRegionId()).getGroundItem(NORMAL_LOGS, tile, player);
							if (item == null)
								return;
							if (!Game.removeGroundItem(player, item, false))
								return;
							player.faceLocation(tile);
							sendTutLines("You gained some experience.", 9, new String[] { "", 
								"",
								"Click on the flashing bar graph icon near the inventory button to see",
								"your skill stats.", "", "" });
							player.interfaces().sendSkills();
							player.packets().sendConfig(1021, 2); // Skills flashing
							Game.spawnTempGroundObject(new GlobalObject(2732, 10, 0, tile.getX(), tile.getY(), tile.getZ()), 592, 120000);
							player.getSkills().addXp(Skills.FIREMAKING, 25);
							stop();
						}
				});
			} else if(player.getTutorialStage() >= 12) {
				if (!Game.canMoveNPC(player.getZ(), player.getX(), player.getY(), 1)
						|| Game.getRegion(player.getRegionId()).getSpawnedObject(player) != null) {
					player.packets().sendMessage("You can't light a fire here.");
					return false;
				}
				player.getInventory().deleteItem(NORMAL_LOGS, 1);
				Game.addGroundItem(new Item(NORMAL_LOGS, 1), new Location(player), player, false, 180, true);
				player.setNextAnimation(FIREMAKING_ANIMATION);
				sendTutLines("", player.getTutorialStage(), new String[] { 
						"<col=0000FF>Please wait.</col>", "", "Your character is now attempting to light the fire.", 
						"This should only take a few seconds.", "", "" });
				Game.submit(new GameTick(7540) {
					@Override
					public void run() {
						final Location tile = new Location(player);
						if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
							if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
								if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
									player.addWalkSteps(player.getX(), player.getY() - 1, 1);
							final FloorItem item = Game.getRegion(tile.getRegionId()).getGroundItem(NORMAL_LOGS, tile, player);
							if (item == null)
								return;
							if (!Game.removeGroundItem(player, item, false))
								return;
							player.faceLocation(tile);
							player.interfaces().sendSkills();
							Game.spawnTempGroundObject(new GlobalObject(2732, 10, 0, tile.getX(), tile.getY(), tile.getZ()), 592, 60000);
							if(player.getSkills().getLevelForXp(Skills.FIREMAKING) < 3)
								player.getSkills().addXp(Skills.FIREMAKING, 25);
							refreshAll();
							stop();
						}
				});
			}
			return false;
		}
		return false;
	}
	
	@Override
	public boolean processNPCExamine(NPC npc) {
		if(player.getTutorialStage() >= 4)
			player.sm(npc.getExamine());
		else
			player.sm("You can't do that at the moment.");
		return false;
	}
	
	@Override
	public boolean processMagicTeleport(Location tile) {
		return false;
	}


	@Override
	public void trackXP(int skillId, int addedXp) {
		
	}

	@Override
	public boolean canAddInventoryItem(int itemId, int amount) {
		return true;
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		switch(interfaceId) {
		
		case 192:
			switch(componentId) {
				case 25:
					if(player.getTutorialStage() >= 67)
						return true;
					return false;
				case 24: // TODO Finish - Updated - Lumbridge Teleport :) Finally DONE.
					if(player.getTutorialStage() == 69) {
						player.stopAll();
						player.getControllerManager().removeControllerWithoutCheck();
						player.setCompletedTutorial(true);
						player.interfaces().refreshAllTabs();
						player.setNextAnimation(new Animation(8939));
						player.setNextGraphics(new Graphics(1576));
						player.packets().sendSound(200, 0, 1);
						player.lock(3 + 1);
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								Location tile = new Location(3233, 3230, 0);
								for (int trycount = 0; trycount < 10; trycount++) {
									tile = new Location(tile, 2);
										if (Game.canMoveNPC(tile.getZ(), tile.getX(), tile.getY(), player.getSize()))
											break;
									}
								player.getBank().reset();
								player.getBank().addItem(new Item(995, 25), true);
								player.getInventory().reset();
								player.getEquipment().reset();
								player.refreshEquipment();
								player.getAppearance().generateAppearanceData();
								player.getInventory().add(1351, 1); // Bronze hatchet
								player.getInventory().add(TINDERBOX, 1); // Tinder box
								player.getInventory().add(303, 1); // Small Fishing Net
								player.getInventory().add(315, 1); // Shrimps
								player.getInventory().add(EMPTY_BUCKET, 1); // Empty Bucket
								player.getInventory().add(EMPTY_POT, 1); // Empty Pot
								player.getInventory().add(2309, 1); // Bread
								player.getInventory().add(1265, 1); // Bronze pickaxe
								player.getInventory().add(1205, 1); // Bronze dagger
								player.getInventory().add(1291, 1); // Bronze longsword
								player.getInventory().add(1171, 1); // Wooden shield
								player.getInventory().add(841, 1); // Shortbow
								player.getInventory().add(882, 25); // Bronze arrow
								player.getInventory().add(556, 25); // Air runes
								player.getInventory().add(558, 15); // Mind runes
								player.getInventory().add(555, 6); // Water runes
								player.getInventory().add(557, 4); // Earth runes
								player.getInventory().add(559, 2); // Bronze hatchet
								player.setLocation(new Location(3233, 3230, 0)); // Lumbridge
								player.closeAllInterfaces();
								player.getDialogue().start(new InfoText(), (Object) new String[] { 
									"Welcome to Lumbridge! To get more help, simply click on the",
									"Lumbridge Guide or one of the Tutors - these can be found by",
									"looking for the question mark icon on your minimap. If you find you",
									"are lost at any time, look for a signpost or use the Lumbridge Home",
									"Teleport spell." });
								player.getControllerManager().magicTeleported(0);
								player.setNextAnimation(new Animation(8941));
								player.setNextGraphics(new Graphics(1577));
								player.packets().sendSound(201, 0, 1);
								player.faceLocation(new Location(tile.getX(), tile.getY() - 1, tile.getZ()));
								player.setDirection(6);
								stop();
							}
						}, 3, 0);
						return false;
					}
					return false;
			}
			return false;
		
		case 884: // Attack tab
			return true;
		
		case 387:
			switch(componentId) {
				case 56:
					if(player.getTutorialStage() == 41) {
						sendTutLines("", 42, new String[] { 
							"<col=0000ff>Worn interface</col>", 
							"You can see what items you are wearing in the worn equipment to",
							"the left of the screen, with their combined statistics on the right. Let's",
							"add something. Left click your dagger to 'wield' it.", "", "" });
						refreshAll();
						return true;
					} else if(player.getTutorialStage() > 41)
						return true;
					else
						return false;
			}
			return false;
		
		case 300:
			if(player.getTutorialStage() == 37) {
				if(componentId != 24) {
					player.sm("You can only make the dagger for now.");
					return false;
				} else {
					player.packets().sendCloseInterface();
					if(!player.getInventory().containsOneItem(2349, 1)) {
						clicked = false;
						return false;
					}
					player.setNextAnimation(new Animation(898)); // Smithing with hammer anim.
					Game.submit(new GameTick(1500) {
						public void run() {
							stop();
							int slot = player.getInventory().lookupSlot(2349);
							player.getInventory().getItems().set(slot, new Item(1205, 1));
							player.getInventory().refresh();
							player.setTutorialStage(38);
							refreshHintIcons();
							sendTutLines("", 38, new String[] { 
								"<col=0000ff>You've finished in this area.</col>",
								"So let's move on. Go through the gates shown by the arrow.",
								"Remember, you may need to move the camera to see your",
								"surroundings. Speak to the guide for a recap at any time.", "", "" });
							refreshAll();
							player.sm("You make a bronze dagger.");
						}
					});
				}
				return false;
			}
			break;
		
		case 190:
			if(componentId == 18) {
				if(slotId == 147) {
					refreshLTRStatus();
					return true;
				}
			} else if(componentId == 10)
				return false;
			break;
	
			case 750: // Orbs
				switch(componentId) {
					case 1: // Toggle run
						if(player.getTutorialStage() < 23) {
							player.setRun(true);
							return true;
						} else if(player.getTutorialStage() == 23) {
							player.setCantMove(false);
							sendTutLines("Run to the next guide", 24, new String[] { "",
									"Now that you have the run button turned on, follow the path until",
									"you come to the end. You may notice that the number on the button",
									"goes down. This is your run energy. If your run energy reaches",
									"zero, you'll stop running. Click on the door to pass through it.", ""});
							refreshAll();
							return true;
						}
				}
				return false;
			
			case 261:
				if(componentId == 3) {
					if(player.getTutorialStage() < 23) {
						player.setRun(true);
						return true;
					} else if(player.getTutorialStage() == 23) {
						player.setCantMove(false);
						sendTutLines("Run to the next guide", 24, new String[] { "",
							"Now that you have the run button turned on, follow the path until",
							"you come to the end. You may notice that the number on the button",
							"goes down. This is your run energy. If your run energy reaches",
							"zero, you'll stop running. Click on the door to pass through it.", ""});
						refreshAll();
						return false;
					}
				}
				return true;
				
			case 464:
				switch(componentId) {
					default:
						if(player.getTutorialStage() == 22) {
							player.setCantMove(true);
							sendTutLines("", 23, new String[] { 
									"<col=0000ff>Running</col>", 
									"It's only a short distance to the next guide.",
									"Why not try running there? To do this, click on the run icon next to",
									"the minimap.", "", "" });
							refreshAll();
						}
						return true;
				}
				
			case 771: // Char Design Interface
				if(componentId == 76) { // Accept Settings
					player.packets().sendRemoveOverlay3();
					CharacterDesign.refresh(player);
					player.setCustomizedCharacter(true);
					player.setCantMove(false);
					player.setTutorialStage(1);
					refreshAll();
					player.packets().sendBlackOut(0);
				}
				return true;
				
			case 548:
			case 746:
				switch(componentId) {
				
				case 81:
				case 120:
					if(componentId == 120 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 45) {
						player.packets().sendConfig(1021, 0);
						sendTutLines("These are your combat styles.", 46, new String[] { 
							"From this interface you can select the type of attack your character",
							"will use, and whether or not to auto-retaliate. Different monsters have",
							"different weaknesses. If you hover your mouse over the buttons, you",
							"will see the type of XP you will receive when using each type of", 
							"attack. Now you have the tools needed for battle, why not slay some", 
							"rats? Click on the gates indicated to continue." });
						refreshAll();
						return false;
					}
					return false;
				
				
				case 55:
				case 132:
					if(componentId == 132 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 2) {
						player.packets().sendConfig(1021, 0);
						sendTutLines("Game Options", 3, new String[] {
								"In the interface, you can now see a variety of options such as screen",
								"brightness, sound and music volume and whether you want to accept", 
								"aid from other players or not. Don't worry about these too much for ",
								"now; they will become clearer as you explore the game. Talk to the",
								"RuneScape Guide to continue.", ""});
						refreshAll();
						return false;
					}
					return false;
					
					
				case 88:
				case 127:
					if(componentId == 127 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 65) {
						player.packets().sendConfig(1021, 0);
						player.setTutorialStage(66);
						player.getDialogue().start(new Dialogue() {
							public void start() {
								sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", false,
									"Good. This is a list of your spells. Currently you can",
									"only cast one offensive spell called Wind Strike. Let's",
									"try it out one of those chickens.");
							}
							public void process(int i, int b) {
								if(stage == 0) {
									player.setTutorialStage(67);
									sendItems(new int[] { 556, 5, 558, 5 }, 
										"Terrova gives you five <col=0000ff>air runes</col> "
										+ "and five <col=0000ff>mind runes</col!");
									player.getInventory().add(556, 5);
									player.getInventory().add(558, 5);
									stage = 1;
								} else if(stage == 1) {
									end();
									sendTutLines("Cast Wind Strike at a chicken.", 67, new String[] { "",
										"Now you have runes you should see the Wind Strike icon at the top-",
										"left of your spellbook, second in from the left. Walk over to the caged",
										"chickens, click on the Wind Strike icon and then select one of the chickens",
										"to cast it on. It may take several tries. If you need more runes ask",
										"Terrova." });
									refreshAll();
								}
							}
							public void finish() { }
						});
						refreshAll();
						return false;
					}
					return false;
					
					
				case 87:
				case 126:
					if(componentId == 126 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 57) {
						player.packets().sendConfig(1021, 0);
						sendTutLines("Your Prayer List", 58, new String[] {
								"",
								"", 
								"Talk with Brother Brace and he'll tell you all about prayers.",
								"",
								"", ""});
						refreshAll();
						return false;
					}
					return false;
					
				case 52:
				case 129:
					if(componentId == 129 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 59) {
						sendTutLines("This is your Friends List.", 60, new String[] {
								"",
								"", 
								"This will be explained by Brother Brace shortly, but first click on the",
								"other flashing face in the interface.",
								"", ""});
						player.packets().sendConfig(1021, 11); // Flashing Ignores.
						player.interfaces().sendIgnoresTab();
						refreshAll();
						return false;
					}
					return false;

				case 53:
				case 130:
					if(componentId == 130 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 60) {
						sendTutLines("This is your Ignore List.", 61, new String[] {
								"",
								"The two lists - Friends and Ignores - can be very helpful for keeping", 
								"track of when your friends are online or for blocking messages from",
								"people you simply don't like. Speak with Brother Brace and he will tell",
								"you more.", 
								""});
						player.packets().sendConfig(1021, 0);
						refreshAll();
						return false;
					}
					return false;
					
				case 83:
				case 122:
					if(componentId == 122 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 26) {
						player.packets().sendConfig(1021, 0);
						sendTutLines("", 27, new String[] {
								"<col=0000ff>Your Quest Journal", "",
								"This is your Quest Journal, a list of all the quests in the game.",
								"Talk to the Quest Guide again for an explanation.", "", "" });
						refreshAll();
						return false;
					}
					return false;
					
				case 85:
				case 124:
					if(componentId == 124 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 6) {
						player.packets().sendConfig(1021, 0);
						sendTutLines("", 7, new String[] { "<col=0000FF>Cut down a tree</col>",
								"You can click on the backpack icon at any time to view the", 
								"items that you currently have in your inventory. You will see", 
								"that you now have a hatchet in your inventory. Use this to get", 
								"some logs by clicking on one of the trees in the area.", 
								"" });
						refreshAll();
						return false;
					}
					return false;
					
					
				case 57:
				case 134:
					if(componentId == 134 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 19) {
						player.packets().sendConfig(1021, 0);
						sendTutLines("The Music Player", 20, new String[] { 
								"From this interface you can control the music that is played. As you",
								"explore the world and complete quests, more of the tunes will become", 
								"unlocked. Once you've examined this menu, use the next door to", 
								"continue. If you need a recap on anything covered here, talk to Lev.", 
								"", "" });
						refreshAll();
						return false;
					}
					break;
				case 86:
				case 125:
					if(componentId == 125 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 40) {
						player.packets().sendConfig(1021, 0);
						sendTutLines("", 41, new String[] { 
								"<col=0000ff>This your worn equipment.</col>",
								"From here you can see what items you have equipped. You will notice", 
								"the button 'Show Equipment Stats'. Click on this now to display the", 
								"details of what you have equipped.", 
								"", "" });
						refreshAll();
						return false;
					}
					break;
				case 56:
				case 133:
					if(componentId == 133 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 21) {
						player.packets().sendConfig(1021, 0);
						sendTutLines("Emotes", 22, new String[] { 
							"For those situations where words don't quite describe how you feel, try",
							"an emote. Go ahead, try one out! You might notice that some of the",
							"emotes are grey and cannot be used now. Don't worry! As you",
							"progress further into the game, you'll gain accesss to all sorts of things,",
							"including more fun emotes like these.", "" });
						player.setCantMove(true);
						refreshAll();
						return false;
					}
					return true;
				case 82:
				case 121:
					if(componentId == 121 && !player.interfaces().fullScreen())
						return false;
					if(player.getTutorialStage() == 9) {
						player.packets().sendConfig(1021, 0);
						sendTutLines("Your skill stats", 10, new String[] { 
								"Here you will see how good your skills are. As you move your",
								"mouse over any of the icons in this tab, the small yellow popup", 
								" box will show you the exact amount of experience you have", 
								" and how much is needed to get to the next level. Speak to the", 
								" Survival Expert to continue.", "" 
								});
						refreshAll();
						return false;
					}
					return false;
				}
				return true;
		}
		return true;
	}

	@Override
	public boolean processObjectClick1(final GlobalObject object) {
		switch(object.getId()) {
		
		case 36999:
		case 37002:
			if(clicked)
				return false;
			clicked = true;
			if(object.getX() != 3129) {
				clicked = false;
				return false;
			}
			player.addWalkSteps(object.getX(), object.getY());
			player.faceObject(object);
			Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
				@Override
				public void run() {
					if(player.withinDistance(object, 1)) {
						stop();
						try {
							Thread.sleep(1600);
							Passages.churchDoors(object);
							player.addWalkSteps(object.getX() - 1, object.getY());
							refreshAll();
							clicked = false;
							refreshAll();
							player.setCantMove(true);
							Thread.sleep(1500);
							player.setCantMove(false);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			});
			return false;
			
		case 3026:
			if(clicked)
				return false;
			clicked = true;
			if(player.getTutorialStage() == 63) {
				player.addWalkSteps(object.getX(), object.getY() + 1);
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.getX() == object.getX()
								&& player.getY() == object.getY() + 1) {
							stop();
							try {
								Thread.sleep(1600);
								Passages.passDoor(player, object, 1000);
								player.addWalkSteps(object.getX(), object.getY());
								sendTutLines("Your final instructor!", 64, new String[] {
										"", "", 
										"Just follow the path to the Wizard's house, where you will be shown", 
										"how to cast spells. Just talk with the mage indicated to find out more.", 
										"", "" });
								refreshAll();
								clicked = false;
								player.setCantMove(true);
								Thread.sleep(1500);
								player.setCantMove(false);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
			return false;
		
		case 3024:
			if(clicked)
				return false;
			clicked = true;
			if(player.getTutorialStage() == 53) {
				player.addWalkSteps(object.getX() - 1, object.getY());
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.withinDistance(object, 1)) {
							stop();
							try {
								Thread.sleep(1600);
								Passages.passDoor(player, object, 1000);
								player.addWalkSteps(object.getX(), object.getY());
								player.setCantMove(true);
								sendTutLines("Financial advice", 54, new String[] {
										"", "", 
										"The guide here will tell you all about making cash. Just click on him to", 
										"hear what he's got to say.", "", "" });
								refreshAll();
								clicked = false;
								refreshAll();
								player.setCantMove(true);
								Thread.sleep(1500);
								player.setCantMove(false);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
			return false;
			
		case 3025:
			if(clicked)
				return false;
			clicked = true;
			if(player.getTutorialStage() == 55) {
				player.addWalkSteps(object.getX() - 1, object.getY());
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.getX() == object.getX() - 1
								&& player.getY() == object.getY()) {
							stop();
							try {
								Thread.sleep(1600);
								Passages.passDoor(player, object, 1000);
								player.addWalkSteps(object.getX(), object.getY());
								sendTutLines("Prayer", 56, new String[] {
										"", "", 
										"Follow the path to the chapel and enter it.", 
										"Once inside talk to the monk. He'll tell you all about the Prayer skill.", 
										"", "" });
								refreshAll();
								clicked = false;
								refreshAll();
								player.setCantMove(true);
								Thread.sleep(1500);
								player.setCantMove(false);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
			return false;
		
		case 3045:
			if(clicked)
				return false;
			clicked = true;
			if(player.getTutorialStage() == 52) {
				player.addWalkSteps(object.getX(), object.getY() - 1);
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.getX() == object.getX() 
								&& player.getY() == object.getY() - 1) {
							player.faceObject(object);
							stop();
							Game.submit(new GameTick(1000) {
								@Override
								public void run() {
									stop();
									clicked = false;
									player.getDialogue().start(new Dialogue() {
										public void start() {
											sendNPCDialogue(BANKER, "Banker", false, 
												 "Good day, would you like to access your bank account?");
										}
										public void process(int i, int b) {
											if(stage == 0) {
												sendOptions("Select an Option", new String[] { "Yes", "No thanks." });
												stage = 1;
											} else if(stage == 1) {
												if(b == OPTION1) {
													end();
													player.getBank().addItem(new Item(995, 25), true);
													player.getBank().sendItems();
													sendTutLines("This is your bank box.", 53, new String[] { 
														"", 
											"You can store stuff in here for safekeeping. If you die, anything in your",
											"bank will be saved. To deposit something, right click it and select",
											"'Deposit-1'. Once you've had a good look, close the window and move",
											"on through the door indicated.", "" });
													refreshAll();
												} else if(b == OPTION2) {
													end();
													refreshAll();
												}
											}
										}
										public void finish() { }
									});
									Game.submit(new GameTick(1100) {
										public void run() {
											refreshHintIcons();
											stop();
										}
									});
									
								}
							});
						}
					}
				});
			} else {
				player.addWalkSteps(object.getX(), object.getY() - 1);
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.getX() == object.getX() 
								&& player.getY() == object.getY() - 1) {
							player.faceObject(object);
							stop();
							Game.submit(new GameTick(1000) {
								@Override
								public void run() {
									stop();
									clicked = false;
									player.getDialogue().start(new Dialogue() {
										public void start() {
											npc(BANKER, "Good day, would you like to access your bank account?");
										}
										public void process(int i, int b) {
											if(stage == 0) {
												sendOptions("Select an Option", new String[] { "Yes", "No thanks." });
												stage = 1;
											} else if(stage == 1) {
												if(b == OPTION1) {
													end();
													player.getBank().openBank();
													refreshAll();
												} else if(b == OPTION2) {
													end();
													refreshAll();
												}
											}
										}
										public void finish() { }
									});
									Game.submit(new GameTick(1100) {
										public void run() {
											refreshHintIcons();
											stop();
										}
									});
									
								}
							});
						}
					}
				});
			}
			return false;
		
		case 3030:
			if(clicked)
				return false;
			clicked = true;
			if(player.getTutorialStage() == 51) {
				player.addWalkSteps(object.getX(), object.getY() - 1);
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.getX() == object.getX() 
								&& player.getY() == object.getY() - 1) {
							player.faceObject(object);
							stop();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							player.setNextAnimation(LADDER_ANIMATION);
							Game.submit(new GameTick(1000) {
								@Override
								public void run() {
									stop();
									clicked = false;
									player.setLocation(new Location(3111, 3125, 0)); // Outside of the bank.
									player.setNextAnimation(LADDER_ANIMATION);
									sendTutLines("Banking", 52, new String[] { "",
										"Follow the path and you will come to the front of the building. This is",
										"the Bank of RuneScape, where you can store all your most valued",
										"items. To open your bank box, just right click on an open booth",
										"indicated and select 'use'.",
										"", "" });
									refreshAll();
									Game.submit(new GameTick(1100) {
										public void run() {
											refreshHintIcons();
											stop();
										}
									});
									
								}
							});
						}
					}
				});
			}
			return false;
		
		case 3022:
		case 3023:
			if(clicked)
				return false;
			clicked = true;
			if(player.getTutorialStage() == 46) {
				player.addWalkSteps(object.getX() + 1, object.getY());
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.getX() == object.getX() + 1 && player.getY() == object.getY()) {
							this.stop();
							try {
								Thread.sleep(1500);
								clicked = false;
								Passages.handleTutorialGate2(player, object, 1000);
								player.addWalkSteps(object.getX(), object.getY());
								sendTutLines("", 47, new String[] { "<col=0000FF>Attacking</col>",
										"To attack the rat, right click it and select the attack option. You will",
										"then walk over to it and start hitting it.",
										"", "", "" });
								refreshAll();
								player.setCantMove(true);
								Thread.sleep(2000);
								player.setCantMove(false);
							} catch (Throwable t) {
								t.printStackTrace();
							}
						}
					}
				});		
			} else if(player.getTutorialStage() > 46 && player.getTutorialStage() < 50) {
				player.addWalkSteps(object.getX(), object.getY());
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.getX() == object.getX() + 1 && player.getY() == object.getY()) {
							this.stop();
							try {
								Thread.sleep(1500);
								clicked = false;
								Passages.handleTutorialGate2(player, object, 1000);
								player.addWalkSteps(object.getX(), object.getY());
								refreshAll();
								player.setCantMove(true);
								Thread.sleep(2000);
								player.setCantMove(false);
							} catch (Throwable t) {
								t.printStackTrace();
							}
						} else if(player.getX() == object.getX() && player.getY() == object.getY()) {
							this.stop();
							try {
								Thread.sleep(1500);
								clicked = false;
								Passages.handleTutorialGate2(player, object, 1000);
								player.addWalkSteps(object.getX() + 1, object.getY());
								refreshAll();
								player.setCantMove(true);
								Thread.sleep(2000);
								player.setCantMove(false);
							} catch (Throwable t) {
								t.printStackTrace();
							}
						}
					}
				});
			}
			return false;
		
			case 3021:
			case 3020:
				if(clicked)
					return false;
				clicked = true;
				if(player.getTutorialStage() == 38) {
					player.addWalkSteps(object.getX(), object.getY());
					player.faceObject(object);
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.withinDistance(object, 1)) {
								try {
									Thread.sleep(1500);
									stop();
									clicked = false;
									Passages.handleTutorialGate2(player, object, 1300);
									player.addWalkSteps(object.getX() + 1, object.getY());
									sendTutLines("", 39, new String[] { 
										"<col=0000ff>Combat</col>", "",
										"In this area you will find out about combat with swords and bows.",
										"Speak to the guide and he will tell you all about it.", "", "" });
									refreshAll();
									player.setCantMove(true);
									Thread.sleep(2000);
									player.setCantMove(false);
								} catch (Throwable t) {
									t.printStackTrace();
								}
							}
						}
					});		
				}
				return false;
		
		case 3043: // Tin ore
			if(clicked)
				return false;
			clicked = true;
			if(player.getTutorialStage() == 33) {
			player.addWalkSteps(object.getX(), object.getY());
			player.faceObject(object);
			player.stopAll(false);
			Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
				@Override
				public void run() {
					if(player.withinDistance(object, 1)) {
						player.faceObject(object);
						stop();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						player.setNextAnimation(new Animation(625)); // Bronze pick
						sendTutLines("", player.getTutorialStage(), new String[] { 
							"<col=0000ff>Please wait.</col>", "",
							"Your character is now attempting to mine the rock.",
							"This should only take a few seconds.", "", ""});
						Game.submit(new GameTick(6500) {
							@Override
							public void run() {
								stop();
								clicked = false;
								player.setNextAnimation(new Animation(-1));
								player.getInventory().add(438, 1);
								player.getSkills().addXp(Skills.MINING, 25);
								player.getDialogue().start(new InfoText(), 
									(Object) new String[] { "You manage to mine some tin." });
								player.setTutorialStage(34);
								GlobalObject replace = new GlobalObject(11552, object.getType(),
										object.getRotation(), object.getX(), object.getY(), object.getZ());
								Game.removeTemporaryObject(object, 40000, true);
								Game.spawnTemporaryObject(replace, 40000, true);
								refreshHintIcons();
								sendTutLines("", 34, new String[] { 
									"<col=0000ff>Mining</col>",
									"Now you have some tin ore you just need some copper ore.",
									"then you'll have all you need to create a bronze bar. As you",
									"did before, right click on the copper rock and select 'mine'.", "", "" });
								refreshAll();
							}
						});
					}
				}
			});
			} else if(player.getTutorialStage() > 33){
					player.addWalkSteps(object.getX(), object.getY());
					player.faceObject(object);
					player.stopAll(false);
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.withinDistance(object, 1)) {
								player.faceObject(object);
								stop();
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								player.setNextAnimation(new Animation(625)); // Bronze pick
								sendTutLines("", player.getTutorialStage(), new String[] { 
									"<col=0000ff>Please wait.</col>", "",
									"Your character is now attempting to mine the rock.",
									"This should only take a few seconds.", "", ""});
								Game.submit(new GameTick(6500) {
									@Override
									public void run() {
										stop();
										clicked = false;
										player.setNextAnimation(new Animation(-1));
										player.getInventory().add(438, 1);
										if(player.getSkills().getLevelForXp(Skills.MINING) < 3)
											player.getSkills().addXp(Skills.MINING, 25);
										player.getDialogue().start(new InfoText(), 
											(Object) new String[] { "You manage to mine some tin." });
										GlobalObject replace = new GlobalObject(11552, object.getType(),
												object.getRotation(), object.getX(), object.getY(), object.getZ());
										Game.removeTemporaryObject(object, 40000, true);
										Game.spawnTemporaryObject(replace, 40000, true);
										refreshAll();
									}
								});
							}
						}
					});
			}
			return false;
			
		case 3042: // Copper ore
			if(clicked)
				return false;
			clicked = true;
			if(player.getTutorialStage() == 34) {
			player.addWalkSteps(object.getX(), object.getY());
			player.faceObject(object);
			Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
				@Override
				public void run() {
					if(player.withinDistance(object, 1)) {
						player.faceObject(object);
						stop();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						player.setNextAnimation(new Animation(625)); // Bronze pick
						sendTutLines("", player.getTutorialStage(), new String[] { 
							"<col=0000ff>Please wait.</col>", "",
							"Your character is now attempting to mine the rock.",
							"This should only take a few seconds.", "", ""});
						Game.submit(new GameTick(6500) {
							@Override
							public void run() {
								stop();
								clicked = false;
								GlobalObject replace = new GlobalObject(11552, object.getType(),
										object.getRotation(), object.getX(), object.getY(), object.getZ());
								Game.removeTemporaryObject(object, 40000, true);
								Game.spawnTemporaryObject(replace, 40000, true);
								player.setNextAnimation(new Animation(-1));
								player.getInventory().add(436, 1);
								player.getSkills().addXp(Skills.MINING, 25);
								player.getDialogue().start(new InfoText(), 
									(Object) new String[] { "You manage to mine some copper." });
								player.setTutorialStage(35);
								refreshHintIcons();
								sendPercentage();
								sendTutLines("", 35, new String[] { 
									"<col=0000ff>Smelting</col>",
									"You should now have both some copper and tin ore. So let's",
									"smelt them to make a bronze bar. To do this, right click on",
									"either tin or copper ore and select use then left click on the", 
									"furnace. Try it now.", "" });
								refreshAll();
							}
						});
					}
				}
			});
			} else if(player.getTutorialStage() > 34) {
				player.addWalkSteps(object.getX(), object.getY());
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.withinDistance(object, 1)) {
							player.faceObject(object);
							stop();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							player.setNextAnimation(new Animation(625)); // Bronze pick
							sendTutLines("", player.getTutorialStage(), new String[] { 
								"<col=0000ff>Please wait.</col>", "",
								"Your character is now attempting to mine the rock.",
								"This should only take a few seconds.", "", ""});
							Game.submit(new GameTick(6500) {
								@Override
								public void run() {
									stop();
									clicked = false;
									GlobalObject replace = new GlobalObject(11552, object.getType(),
											object.getRotation(), object.getX(), object.getY(), object.getZ());
									Game.removeTemporaryObject(object, 40000, true);
									Game.spawnTemporaryObject(replace, 40000, true);
									player.setNextAnimation(new Animation(-1));
									player.getInventory().add(436, 1);
									if(player.getSkills().getLevelForXp(Skills.MINING) < 3)
										player.getSkills().addXp(Skills.MINING, 25);
									player.getDialogue().start(new InfoText(), 
										(Object) new String[] { "You manage to mine some copper." });
									refreshAll();
								}
							});
						}
					}
				});
			}
			return false;

		
		
			case 3039:
				if(clicked)
					return false;
				clicked = true;
				if(player.getTutorialStage() == 18) {
					if(!player.getInventory().containsOneItem(BREAD_DOUGH, 1))
						return false;
					player.addWalkSteps(object.getX(), object.getY() + 1);
					Game.submit(new GameTick(600) {
						@Override
						public void run() {
							if(player.getX() == object.getX() 
									&& player.getY() == object.getY() + 1) {
								player.faceObject(object);
								stop();
								try {
									Thread.sleep(1400);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								player.setNextAnimation(RANGE_COOKING_ANIMATION);
								Game.submit(new GameTick(2000) {
									@Override
									public void run() {
										stop();
										clicked = false;
										int slot = player.getInventory().lookupSlot(BREAD_DOUGH);
										player.getInventory().getItems().set(slot, new Item(2309, 1));
										player.getInventory().refresh();
										player.getSkills().addXp(Skills.COOKING, 50);
										sendTutLines("Cooking dough", 19, new String[] {
											"Well done! Your first loaf of bread. As you gain experience in",
											"Cooking, you will be able to make other things like pies, cakes",
											"and even kebabs. Now you've got the hang of cooking, let's",
											"move on. Click on the flashing icon in the bottom right to see",
											"the Music Player.", "" });
										player.interfaces().sendMusicPlayer();
										player.packets().sendConfig(1021, 15); // Flashing Music Player
										refreshAll();
									}
								});
							}
						}
						
					});
				}		
				return false;
				
			case 3029:
				if(clicked)
					return false;
				clicked = true;
				if(player.getTutorialStage() == 28) {
					player.addWalkSteps(object.getX(), object.getY() + 1);
					player.faceObject(object);
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.getX() == object.getX() 
									&& player.getY() == object.getY() + 1) {
								player.faceObject(object);
								stop();
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								player.setNextAnimation(LADDER_ANIMATION);
								Game.submit(new GameTick(1000) {
									@Override
									public void run() {
										stop();
										clicked = false;
										player.setLocation(MINING_TUTORIAL_LOCATION);
										player.setNextAnimation(LADDER_ANIMATION);
										sendTutLines("", 29, new String[] { 
											"<col=0000ff>Mining and Smithing</col>",
											"Next let's get you a weapon, or more to the point, you can",
											"make your first weapon yourself. Don't panic, the Mining",
											"Instructor will help you. Talk to him and he'll tell you all about it.",
											"", "" });
										refreshAll();
										Game.submit(new GameTick(1100) {
											public void run() {
												refreshHintIcons();
												stop();
											}
										});
										
									}
								});
							}
						}
					});
				}
				return false;
				
			case 36832:
				if(clicked)
					return false;
				clicked = true;
				player.addWalkSteps(object.getX(), object.getY() + 1);
				player.faceObject(object);
				Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
					@Override
					public void run() {
						if(player.getX() == object.getX() 
								&& player.getY() == object.getY() + 1) {
							player.faceObject(object);
							stop();
							player.sm("You search the books...");
							try {
								Thread.sleep(2000);
								player.sm("None of them look very interesting.");
								clicked = false;
								refreshAll();
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				});
				return false;
				
			case 3019:
				if(clicked)
					return false;
				clicked = true;
				if(player.getTutorialStage() == 24) {
					player.addWalkSteps(object.getX(), object.getY());
					player.faceObject(object);
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.getX() == object.getX() 
									&& player.getY() == object.getY()) {
								stop();
								try {
									Thread.sleep(1600);
									Passages.passDoor(player, object, 1000);
									player.addWalkSteps(object.getX(), object.getY() - 1);
									sendTutLines("Talk with the Quest Guide.", 25, new String[] {
											"", "", 
											"He will tell you all about quests.", "", "", "" });
									clicked = false;
									refreshAll();
									player.setCantMove(true);
									Thread.sleep(2000);
									player.setCantMove(false);
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
				return false;
				
			case 3018:
				if(clicked)
					return false;
				clicked = true;
				if(player.getTutorialStage() == 20) {
					player.addWalkSteps(object.getX() + 1, object.getY());
					player.faceObject(object);
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.getX() == object.getX() + 1 
									&& player.getY() == object.getY()) {
								stop();
								try {
									Thread.sleep(1600);
									Passages.passDoor(player, object, 1000);
									player.addWalkSteps(object.getX(), object.getY());
									
									clicked = false;
									sendTutLines("", 21, new String[] {
										"<col=0000ff>Emotes</col>",
										"",
										"Now, how about showing some feelings? You will see a flashing",
											"icon in the shape of two masks. Click on that to access your", 
											"emotes.", ""});
									player.interfaces().sendEmotes();
									player.packets().sendConfig(1021, 14); // Flashing Emotes
									refreshAll();
									player.setCantMove(true);
									Thread.sleep(2000);
									player.setCantMove(false);
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
				return false;
				
			case 3017:
				if(clicked)
					return false;
				clicked = true;
				if(player.getTutorialStage() == 15) {
					player.addWalkSteps(object.getX(), object.getY());
					player.faceObject(object);
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.getX() == object.getX() && player.getY() == object.getY()) {
								stop();
								try {
									Thread.sleep(1600);
									clicked = false;
									Passages.passDoor(player, object, 1000);
									player.addWalkSteps(object.getX() - 1, object.getY());
									sendTutLines("", 16, new String[] {
											"<col=0000ff>Find your next instructor</col>",
											"Talk to the chef indicated. He will teach you the more advanced",
											"aspects of Cooking such as combining ingredients. He will also",
											"teach you about your Music Player.", "", ""});
									refreshAll();
									player.setCantMove(true);
									Thread.sleep(1300);
									player.setCantMove(false);
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
				return false;
		
			case 3015:
			case 3016:
				if(clicked)
					return false;
				clicked = true;
				if(player.getTutorialStage() == 14) {
					player.addWalkSteps(object.getX() - 1, object.getY());
					player.faceObject(object);
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.getX() == object.getX() + 1 && player.getY() == object.getY()) {
								try {
									this.stop();
									Thread.sleep(1500);
									clicked = false;
									Passages.handleTutorialGate(player, object, 1000);
									player.addWalkSteps(object.getX(), object.getY());
									sendTutLines("", 15, new String[] { 
											"<col=0000ff>Find your next instructor</col>",
											"Follow the path until you get to the door with the yellow arrow",
											"above it. Click on the door to open it. Notice the mini map in the",
											"top right; This shows a top down view of the area around you.",
											"This can also be used for navigation.", "" });
									refreshAll();
									player.setCantMove(true);
									Thread.sleep(1300);
									player.setCantMove(false);
								} catch (Throwable t) {
									t.printStackTrace();
								}
							}
						}
					});		
				}
				return false;
				
			case 3014: // Door in the first room
				if(clicked)
					return false;
				clicked = true;
				if(player.getTutorialStage() < 4)
					player.sm("Please speak to the RuneScape Guide before attempting this.");
				else if(player.getTutorialStage() == 4) {
					player.addWalkSteps(object.getX() - 1, object.getY());
					player.faceObject(object);
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.getX() == object.getX() - 1 && player.getY() == object.getY()) {
								try {
									Thread.sleep(1600);
									clicked = false;
									Passages.passDoor(player, object, 1000);
									player.addWalkSteps(object.getX(), object.getY());
									sendTutLines("Moving around", 5, new String[] {
										"Follow the path to find the next instructor. Clicking on the ground will",
										"walk you to that point. You can also navigate by clicking on the", 
										"minimap in the top-right corner of your screen. Talk to the Survival", 
										"Expert by the pond to continue the tutorial. Remember, you can", 
										"rotate the view by pressing the arrow keys.", "" });
									refreshFaceDirections();
									refreshHintIcons();
									refreshAll();
									stop();
									player.setCantMove(true);
									Thread.sleep(2000);
									player.setCantMove(false);
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
				return false;
				
			case 3033: // Normal Tree
				if(clicked)
					return false;
				clicked = true;
				player.addWalkSteps(object.getX() - 1, object.getY());
				player.faceObject(object);
				if(player.getTutorialStage() >= 14) {
					player.sm("You have already cut down a tree.");
					clicked = false;
					return false;
				} else if(player.getInventory().containsOneItem(NORMAL_LOGS) && player.getTutorialStage() < 9) {
					player.sm("You already have logs in your inventory.");
					clicked = false;
					return false;
				} else if(player.getTutorialStage() == 7 || player.getTutorialStage() == 8) {
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.withinDistance(object, 1)) {
								stop();
								try {
									player.setNextAnimation(WOODCUTTING_ANIMATION);
									Thread.sleep(1250);
									sendTutLines("", 7, new String[] { 
											"<col=0000FF>Please wait.</col>",
											"", 
											"Your character is attempting to cut down the tree. Sit back",
											"for a moment while "+(player.getAppearance().
													isMale() ? "he" : "she")+" does all the hard work.",
											"", "" });
									player.faceObject(object);
									player.setCantMove(true);
									Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
										int count = 0;
										@Override
										public void run() {
											count++;
											player.setNextAnimation(WOODCUTTING_ANIMATION);
											if(count == 10) {
												player.getDialogue().start(new Dialogue() {
													@Override
													public void start() {
														
														GlobalObject tree = object;
														player.setNextAnimation(WOODCUTTING_ANIMATION);
														Game.spawnTemporaryObject(new GlobalObject(1341, tree.getType(), tree.getRotation(), tree.getX(), tree.getY(), tree.getZ()), 25000);
														if (tree.getZ() < 3) {
															GlobalObject objec = Game.getObject(new Location(tree.getX() - 1, tree.getY() - 1, tree.getZ() + 1));
															if (objec == null) {
																objec = Game.getObject(new Location(tree.getX(), tree.getY() - 1, tree.getZ() + 1));
																if (objec == null) {
																	objec = Game.getObject(new Location(tree.getX() - 1, tree.getY(), tree.getZ() + 1));
																	if (objec == null) {
																		objec = Game.getObject(new Location(tree.getX(), tree.getY(), tree.getZ() + 1));
																	}
																}
															}
															if (objec != null)
																Game.removeTemporaryObject(objec, 25000, false);
														}
														clicked = false;
														player.setNextAnimation(new Animation(-1));
														sendItemDialogue(new Item(NORMAL_LOGS, 1), "You get some logs.");
														player.getInventory().add(NORMAL_LOGS, 1);
														player.setCantMove(false);
														player.getSkills().addXp(Skills.WOODCUTTING, 25);
														sendTutLines("", 8, new String[] { 
															"<col=0000FF>Making a fire</col>",
															"Well done! You managed to cut some logs from the tree! Next,",
															"use the tinderbox in your inventory to light the logs.",
															"First click on the tinderbox to 'use' it.",
															"Then click on the logs in your inventory to light them.",
															"" });
														clicked = false;
														refreshAll();
													}

													@Override
													public void process(int interfaceId,
															int buttonId) {
														if(stage == 0)
														end();
													}

													@Override
													public void finish() {
														
													}
												});
												clicked = false;
												stop();
											}
										}
									});									
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					});
				} else if(player.getTutorialStage() >= 12) {
					if(player.getInventory().containsOneItem(NORMAL_LOGS, 2)) {
						player.sm("You already have more than enough logs in your inventory.");
						clicked = false;
						return false;
					}
					Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
						@Override
						public void run() {
							if(player.withinDistance(object, 1)) {
								stop();
								try {
									Thread.sleep(1250);
									sendTutLines("", player.getTutorialStage(), new String[] { 
											"<col=0000FF>Please wait.</col>",
											"", 
											"Your character is attempting to cut down the tree. Sit back",
											"for a moment while "+(player.getAppearance().
													isMale() ? "he" : "she")+" does all the hard work.",
											"", "" });
									player.faceObject(object);
									player.setCantMove(true);
									Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
										int count = 0;
										@Override
										public void run() {
											count++;
											player.setNextAnimation(WOODCUTTING_ANIMATION);
											if(count == 10) {
												stop();
												player.getDialogue().start(new Dialogue() {
													@Override
													public void start() {
														GlobalObject tree = object;
														player.setNextAnimation(WOODCUTTING_ANIMATION);
														Game.spawnTemporaryObject(new GlobalObject(1341, tree.getType(), tree.getRotation(), tree.getX(), tree.getY(), tree.getZ()), 25000);
														if (tree.getZ() < 3) {
															GlobalObject objec = Game.getObject(new Location(tree.getX() - 1, tree.getY() - 1, tree.getZ() + 1));
															if (objec == null) {
																objec = Game.getObject(new Location(tree.getX(), tree.getY() - 1, tree.getZ() + 1));
																if (objec == null) {
																	objec = Game.getObject(new Location(tree.getX() - 1, tree.getY(), tree.getZ() + 1));
																	if (objec == null) {
																		objec = Game.getObject(new Location(tree.getX(), tree.getY(), tree.getZ() + 1));
																	}
																}
															}
															if (objec != null)
																Game.removeTemporaryObject(objec, 25000, false);
														}
														player.setNextAnimation(new Animation(-1));
														sendItemDialogue(new Item(NORMAL_LOGS, 1), "You get some logs.");
														player.getInventory().add(NORMAL_LOGS, 1);
														player.setCantMove(false);
														if(player.getSkills().getLevelForXp(Skills.WOODCUTTING) < 3)
															player.getSkills().addXp(Skills.WOODCUTTING, 25);
														clicked = false;
														refreshAll();
													}

													@Override
													public void process(int interfaceId,
															int buttonId) {
														if(stage == 0)
														end();
													}

													@Override
													public void finish() {
														
													}
												});
											}
										}
									});					
								} catch (Throwable e) {
								e.printStackTrace();
								}
							}
						}
					});
				}
				return false;
		}
		return false;
	}
	
	@Override
	public boolean processNPCClick1(final NPC npc) {
		player.stopAll(false);
		player.face(npc);
		if(npc.getId() == FISHING_SPOT && player.getTutorialStage() < 11) {
			player.sm("You can't do that at the moment.");
			player.resetMasks();
			return false;
		}
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.faceEntity(player);
				player.faceEntity(npc);
				player.setNextFaceEntity(null);
				if(npc.getId() == RUNESCAPE_GUIDE) {
					clicked = false;
					switch(player.getTutorialStage()) {
						case 1:
							player.getDialogue().start(new Dialogue() {
								@Override
								public void start() {
									sendEntityDialogue(NPC, HAPPY_TALKING, RUNESCAPE_GUIDE, 
										"RuneScape Guide", new String[] { 
										"Greetings! I see you are a new arrival to this land. My", 
										"job is to welcome all new visitors. So welcome!" }, false);
								}
										
								@Override
								public void process(int interfaceId, int buttonId) {
									if(stage == 0) {
										sendEntityDialogue(NPC, HAPPY_TALKING, RUNESCAPE_GUIDE, 
											"RuneScape Guide", new String[] { 
											"You have already learned the first thing needed to", 
											"succeed in the world: talking to other people!" }, false);
											stage = 1;
									} else if(stage == 1) {
										sendEntityDialogue(NPC, HAPPY_TALKING, RUNESCAPE_GUIDE, 
											"RuneScape Guide", new String[] { 
											"You will find many inhabitants of this world have useful", 
											"things to say to you. By clicking on them with your",
											"mouse you can talk to them." }, false);
											stage = 2;
									} else if(stage == 2) {
										sendEntityDialogue(NPC, HAPPY_TALKING, RUNESCAPE_GUIDE, 
											"RuneScape Guide", new String[] { 
											"I would also suggest reading through some of the", 
											"supporting information on the website. There you can",
											"find the Game Guide, which contains all the additional",
											"information you're ever likely to need. It also contains" }, false);
											stage = 3;
									} else if(stage == 3) {
										sendEntityDialogue(NPC, HAPPY_TALKING, RUNESCAPE_GUIDE, 
											"RuneScape Guide", new String[] { 
											"maps and helpful tips to help you on your journey." }, false);
											stage = 4;
									} else if(stage == 4) {
										sendEntityDialogue(NPC, HAPPY_TALKING, RUNESCAPE_GUIDE, 
											"RuneScape Guide", new String[] { 
											"You will notice a flashing icon of a spanner; please click", 
											"on this to continue the tutorial." }, false);
											stage = 5;
									} else if(stage == 5) {
										end();
										sendTutLines("Game Options", 2, new String[] { "", "",
												"Please click on the flashing spanner icon found at the bottom-right of",
													"your screen. This will display your game options.", "", "" });
										refreshAll();
									}
								}

								@Override
								public void finish() {
									refreshAll();
								}
							});
							break;
							
						case 2:
							player.getDialogue().start("InfoTextNPC", RUNESCAPE_GUIDE, new String[] { 
								"You will notice a flashing icon of a spanner; please click", 
								"on this to continue the tutorial." }, false, Dialogue.HAPPY_TALKING);
							break;
							
						case 3:
						case 4:
							player.getDialogue().start(new Dialogue() {

								@Override
								public void start() {
									sendEntityDialogue(NPC, HAPPY_TALKING, RUNESCAPE_GUIDE, 
											"RuneScape Guide", new String[] { 
											"I'm glad you're making progress!" }, false);
								}

								@Override
								public void process(int interfaceId,
										int buttonId) {
									if(stage == 0) {
										sendEntityDialogue(NPC, HAPPY_TALKING, RUNESCAPE_GUIDE, 
												"RuneScape Guide", new String[] { 
												"To continue the tutorial, go through that door over",
												"there and speak to your first instructor!" }, false);
										stage = 1;
									} else if(stage == 1) {
										end();
										sendTutLines("Interacting with scenery", 4, new String[] { "",
												"You can interact with many items of scenery by simply clicking",
												"on them. Right clicking will also give you more options. Feel free to",
												"try it with the things in the room, then click on the door indicated",
												"with the yellow arrow to go through to the next instructor.", 
												"", });
										refreshAll();
									}
								}

								@Override
								public void finish() {
									
								}
								
							});
							break;
					} 
				} else if(npc.getId() == SURVIVAL_EXPERT) {
					clicked = false;
					switch(player.getTutorialStage()) {
					
					case 5:
						player.getDialogue().start(new Dialogue() {
							@Override
							public void start() {
								sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
									npc.getName(), new String[] { 
										"Hello there, newcomer. My name is Brynna. My job is", 
										"to teach you a few survival tips and tricks. First off,", 
										"we're going to start with the most basic survival skill of", 
										"all: making a fire." }, false);
							}
							@Override
							public void process(int interfaceId,
									int buttonId) {
								if(stage == 0) {
								sendItems(new int[] { TINDERBOX, 1, 1351, 1 }, 
									"The Survival Guide gives you a <col=0000FF>tinderbox</col> "
									+ "and a <col=0000FF>bronze hatchet</col>!");
								player.getInventory().addItem(TINDERBOX, 1);
								player.getInventory().addItem(1351, 1);
									stage = 1;
								} else if(stage == 1) {
									end();
									sendTutLines("", 6, 
											new String[] { "<col=0000FF>Viewing the items that you were given",
											"Click on the flashing backpack icon to the right-hand side of",
											"the main window to view your inventory. Your inventory is a list",
											"of everything you have in your backback.", "", "" });
									player.interfaces().sendInventoryTab();
									player.packets().sendConfig(1021, 5); // Flashing Inventory
									refreshAll();
								}
							}

							
							@Override
							public void finish() {
								
							}
							
						});
						break;
					
						case 6:
						case 7:
						case 8:
						case 9:
							player.getDialogue().start(new InfoTextNPC(), SURVIVAL_EXPERT, 
									(Object) new String[] { 
										"Hello again. I'm here to teach you a few survival tips",
										"and tricks. First off we're going to start with the most",
										"basic survival skill of all: making a fire." });
							break;
						
						case 10:
							player.getDialogue().start(new Dialogue() {

								@Override
								public void start() {
									sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
											"Well done! Next we need to get some food in our", 
											"bellies. We'll need something to cook. There are shrimp", 
											"in the pond there, so let's catch and cook some." }, false);
								}

								@Override
								public void process(int interfaceId,
										int buttonId) {
									if(stage == 0) {
										player.setTutorialStage(11);
										sendItemDialogue(new Item(303, 1), // Small Fishing Net
												"The Survival Guide gives you a <col=0000ff>net</col>!");
										player.getInventory().add(303, 1); // Small Fishing Net
										refreshHintIcons();
										stage = 1;
									} else if(stage == 1) {
										end();
										sendTutLines("", 11, new String[] { 
												"<col=0000ff>Catch some shrimp</col>",
												"Click on the bubbling fishing spot, indicated by the flashing",
												"arrow. Remember, you can check your inventory by clicking the",
												"backpack icon.", "", "" });
										refreshAll();
									}
								}

								@Override
								public void finish() {
									
								}
								
							});
							break;
							
						case 11:
						case 12:
							player.getDialogue().start(new InfoTextNPC(), SURVIVAL_EXPERT, 
									(Object) new String[] { 
								"Now that you've made a fire, we need to get some",
								"food in our bellies. We'll need something to cook.", 
								"There are shrimp in the pond there, so use the net",
								"I gave you and let's catch and cook some." });
							
						case 13:
							player.getDialogue().start(new InfoTextNPC(), SURVIVAL_EXPERT, 
									(Object) new String[] { 
								"I see that you've burnt your shrimp!",
								"This is completley normal, however. As you advance",
								"in Cooking, your burn rate will decrease and soon,",
								"be catching and cooking shrimp better than I do!"});
							break;
							
						case 14:
							player.getDialogue().start(new Dialogue() {
								@Override
								public void start() {
									sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
										npc.getName(), false, new String[] { 
										"Well" });
								}
								@Override
								public void process(int interfaceId,
										int buttonId) {
									if(stage == 0) {
										
									}
								}
								@Override
								public void finish() {
									
								}
							});
					}
				} else if(npc.getId() == FISHING_SPOT) {
					if(player.getTutorialStage() == 11) {
						npc.resetMasks();
						if(clicked)
							return;
						clicked = true;
						sendTutLines("", 11, new String[] { 
							"<col=0000ff>Please wait.</col>",
							"This should only take a few seconds.",
							"As you gain Fishing experience, you'll find that there are many",
							"types of fish and many ways to catch them.", "", "" });
						Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
							@Override
							public void run() {
								if(player.withinDistance(npc, 1)) {
									stop();
									Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
										int count = 0;
										@Override
										public void run() {
											count++;
											player.setNextAnimation(FISHING_ANIMATION);
											if(count == 15) {
												stop();
												clicked = false;
												player.getInventory().add(317, 1); // Raw Shrimps
												player.setNextAnimation(new Animation(-1));
												player.getSkills().addXp(Skills.FISHING, 25);
												sendTutLines("", 12, new String[] { 
													"<col=0000ff>Cooking your shrimp</col>", 
													"Now that you've caught some shrimp, let's cook it. First light a",
													"fire: chop down a tree and then use the tinderbox on the logs.",
													"If you've lost your hatchet or tinderbox, Brynaa will give you", 
													"another.", "" });
												refreshAll();
											}
										}
									});
								}
							}
						});
					} else if(player.getTutorialStage() >= 12) {
						npc.resetMasks();
						if(player.getInventory().containsOneItem(317, 2)) {
							player.sm("You can't carry any more shrimp.");
							return;
						}
						sendTutLines("", player.getTutorialStage(), new String[] { 
							"<col=0000ff>Please wait.</col>",
							"This should only take a few seconds.",
							"As you gain Fishing experience, you'll find that there are many",
							"types of fish and many ways to catch them.", "", "" });
						Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
							@Override
							public void run() {
								if(player.withinDistance(npc, 1)) {
									stop();
									Game.submit(new GameTick(Constants.WORLD_CYCLE_TIME) {
										int count = 0;
										@Override
										public void run() {
											count++;
											player.setNextAnimation(FISHING_ANIMATION);
											if(count == 15) {
												stop();
												player.getInventory().add(317, 1); // Raw Shrimps
												player.setNextAnimation(new Animation(-1));
												if(player.getSkills().getLevelForXp(Skills.FISHING) < 3)
													player.getSkills().addXp(Skills.FISHING, 25);
												refreshAll();
											}
										}
									});
								}
							}
						});
					}
				} else if(npc.getId() == MASTER_CHEF) {
					clicked = false;
					if(player.getTutorialStage() == 16) {
						player.getDialogue().start(new Dialogue() {
							@Override
							public void start() {
								sendEntityDialogue(NPC, HAPPY_TALKING, npc.getId(), 
									npc.getName(), new String[] { 
									"Ah! Welcome, newcomer. I am the Master Chef, Lev. It", 
									"is here I will teach you how to cook food truly fit for a", 
									"king." }, false);
							}
							@Override
							public void process(int interfaceId, int buttonId) {
								if(stage == 0) {
									sendPlayerDialogue(new String[] { 
										"I already know how to cook. Brynna taught me just", 
											"now." }, false);
									stage = 1;
								} else if(stage == 1) {
									sendEntityDialogue(NPC, LAUGH_EXCITED, npc.getId(), 
									npc.getName(), new String[] { 
									"Hahahahahaha! You call THAT cooking? Some shrimp", 
									"on an open log fire? Oh, no, no, no. I am going to", 
									"teach you the fine art of cooking bread." }, false);
									stage = 2;
								} else if(stage == 2) {
									sendEntityDialogue(NPC, LAUGH_EXCITED, npc.getId(), 
										npc.getName(), new String[] { 
										"And no fine meal is complete without good music, so", 
										"we'll cover that while you're here too.", }, false);
										stage = 3;
								} else if(stage == 3) {
									sendItems(new int[] { 1929, 1, 1933, 1 }, 
										"The Cooking Guide gives you a <col=0000ff>bucket of water</col> "
										+ "and a <col=0000ff>pot of flour</col>!");
									player.getInventory().add(1929, 1); // Bucket of water
									player.getInventory().add(1933, 1); // Pot of flour
									player.setTutorialStage(17);
									stage = 4;
								} else if(stage == 4) {
									end();
									sendTutLines("", 17, new String[] { 
										"<col=0000ff>Making dough</col>", 
										"This is the base for many of the meals. To make dough we must",
										"mix flour and water. First, right click the bucket of water and",
										"select use, then left click on the pot of flour.", "", "" });
									refreshAll();
								}
							}
							@Override
							public void finish() {
								
							}
						});
					} else if(player.getTutorialStage() == 18) {
						player.getDialogue().start(new InfoTextNPC(), MASTER_CHEF, 
								(Object) new String[] { 
							"Now you have made dough, you can cook it.",
							"To cook the dough, use it with the range over there.",
							"If you manage to lose your dough, come talk to me again",
							"and I will provide you with more ingredients!" });

					} else if(player.getTutorialStage() >= 17 
							&& !player.getInventory().containsOneItem(BUCKET_OF_WATER, 1)
							|| player.getTutorialStage() >= 17 
							&& !player.getInventory().containsOneItem(POT_OF_FLOUR, 1)) {
						player.getDialogue().start(new Dialogue() {
							@Override
							public void start() {
								sendEntityDialogue(NPC, HAPPY_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"I see you've lost the ingredients that i've given you!", 
										"However, there is nothing to worry about because", 
										"I will provide you with more ingredients!"}, false);
							}
							@Override
							public void process(int interfaceId, int buttonId) {
								if(stage == 0) {
									sendItems(new int[] { 1929, 1, 1933, 1 }, 
											"The Cooking Guide gives you a <col=0000ff>bucket of water</col> "
											+ "and a <col=0000ff>pot of flour</col>!");
										player.getInventory().add(1929, 1); // Bucket of water
										player.getInventory().add(1933, 1); // Pot of flour
										stage = 1;
								} else {
									end();
									refreshAll();
								}
							}
							@Override
							public void finish() {
								
							}
						});
					}
				} else if(npc.getId() == QUEST_GUIDE) {
					clicked = false;
					if(player.getTutorialStage() == 25) {
						player.getDialogue().start(new Dialogue() {
							@Override
							public void start() {
								sendEntityDialogue(NPC, HAPPY_TALKING, npc.getId(), 
									npc.getName(), new String[] { 
									"Ah. Welcome, adventurer. I'm here to tell you all about",
									"quests. Let's start by opening the Quest List." }, false);
							}
							@Override
							public void process(int interfaceId, int buttonId) {
								if(stage == 0) {
									end();
									player.packets().sendUnlockIComponentOptionSlots(190, 18, 0, 201, 0, 1, 2, 3);
									player.packets().sendConfig(281, 1);
									sendTutLines("", 26, new String[] { "",
											"<col=0000ff>Open the Quest Journal.</col>",
											"Click on the flashing icon next to your inventory.",
											"", "", "" });
									player.interfaces().sendQuestTab();
									player.packets().sendConfig(1021, 3); // Flashing Quest Tab
									refreshAll();
								}
							}
							@Override
							public void finish() {
								
							}
						});
					} else if(player.getTutorialStage() == 27) {
						player.getDialogue().start(new Dialogue() {
							@Override
							public void start() {
								sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
									npc.getName(), new String[] { 
									"Now that you have the journal open, I'll you a bit",
									"about it. At the moment all the quests are shown in",
									"red, which means you have not started them yet." }, false);
							}
							@Override
							public void process(int interfaceId, int buttonId) {
								if(stage == 0) {
									sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"When you start a quest, it will change colour to yellow,",
										"and to green when you've finished. This is so you can",
										"easily see what's complete, what's started, and what's left",
										"to begin."}, false);
									stage = 1;
								} else if(stage == 1) {
									sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"The start of quests are easy to find. Look out for the",
										"star icons on the minimap, just like the one you should",
										"see marking my house."}, false);
									stage = 2;
								} else if(stage == 2) {
									sendEntityDialogue(NPC, HAPPY_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"The quests themselves can vary greatly from collecting",
										"beads to hunting down dragons. Generally quests are",
										"started by talking to a non-player character like me,",
										"and will involve a series of tasks." }, false);
									stage = 3;
								} else if(stage == 3) {
									sendEntityDialogue(NPC, HAPPY_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"There's not a lot I can tell you about questing.",
										"You have to experience the thrill of it yourself to fully",
										"understand. You may find some adventure in the caves",
										"under my house."}, false);
									stage = 4;
								} else if(stage == 4) {
									end();
									sendTutLines("", 28, new String[] { "",
											"<col=0000ff>Moving on</col>", 
											"It's time to enter some caves. Click on the ladder to go down to",
											"the next area.", "", "", "" });
									refreshAll();
								}
							}
							@Override
							public void finish() {
								
							}
						});
					} else if(player.getTutorialStage() >= 28) {
						player.getDialogue().start(new Dialogue() {
							@Override
							public void start() {
								sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
									npc.getName(), new String[] { 
									"Now that you have the journal open, I'll you a bit",
									"about it. At the moment all the quests are shown in",
									"red, which means you have not started them yet." }, false);
							}
							@Override
							public void process(int interfaceId, int buttonId) {
								if(stage == 0) {
									sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"When you start a quest, it will change colour to yellow,",
										"and to green when you've finished. This is so you can",
										"easily see what's complete, what's started, and what's left",
										"to begin."}, false);
									stage = 1;
								} else if(stage == 1) {
									sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"The start of quests are easy to find. Look out for the",
										"star icons on the minimap, just like the one you should",
										"see marking my house."}, false);
									stage = 2;
								} else if(stage == 2) {
									sendEntityDialogue(NPC, HAPPY_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"The quests themselves can vary greatly from collecting",
										"beads to hunting down dragons. Generally quests are",
										"started by talking to a non-player character like me,",
										"and will involve a series of tasks." }, false);
									stage = 3;
								} else if(stage == 3) {
									sendEntityDialogue(NPC, HAPPY_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"There's not a lot I can tell you about questing.",
										"You have to experience the thrill of it yourself to fully",
										"understand. You may find some adventure in the caves",
										"under my house."}, false);
									stage = 4;
								} else if(stage == 4) {
									end();
									sendTutLines("", player.getTutorialStage(), new String[] { "",
											"<col=0000ff>Moving on</col>", 
											"It's time to enter some caves. Click on the ladder to go down to",
											"the next area.", "", "", "" });
									refreshAll();
								}
							}
							@Override
							public void finish() {
								
							}
						});
					}
				} else if(npc.getId() == MINING_INSTRUCTOR) {
					clicked = false;
					if(player.getTutorialStage() == 29) {
						player.getDialogue().start(new Dialogue() {
							@Override
							public void start() {
								sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
									npc.getName(), new String[] { 
									"Hi there. You must be new around here. So what do I",
									"call you? 'Newcomer' seems so impersonal, and if we're",
									"going to be working together, I'd rather call you by",
									"name."}, false);
							}
							@Override
							public void process(int interfaceId, int buttonId) {
								if(stage == 0) {
									sendPlayerDialogue(new String[] { 
										"You can call me "+player.getDisplayName()+"." }, false);
									stage = 1;
								} else if(stage == 1) {
									sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"Ok then, "+player.getDisplayName()+". My name is Dezzick and I'm a",
										"miner by trade. Let's prospect some of those rocks." }, false);
									stage = 2;
								} else if(stage == 2) {
									end();
									sendTutLines("", 30, new String[] { 
										"<col=0000ff>Prospecting</col>",
										"To prospect a mineable rock, just right click it and select the",
										"'prospect rock' option. This will tell you the type of ore you can",
										"mine from it. Try it now on one of the rocks indicated.", "", "" });
									refreshAll();
								}
							}
							@Override
							public void finish() {
								
							}
						});
					} else if(player.getTutorialStage() == 32) {
						player.getDialogue().start(new Dialogue() {
							@Override
							public void start() {
								sendPlayerDialogue(new String[] { 
									"I prospected both types of rock! One set contains tin",
									"and the other has copper ore inside." }, false);
							}
							@Override
							public void process(int interfaceId, int buttonId) {
								if(stage == 0) {
									sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"Absolutely right, "+player.getDisplayName()+". These two ore types can",
										"smelted together to make bronze." }, false);
									stage = 1;
								} else if(stage == 1) {
									sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"So now you know what ore is in the rocks over there,",
										"why don't you have a go at mining some tin and",
										"copper? Here, you'll need this to start with."}, false);
									stage = 2;
								} else if(stage == 2) {
									player.getInventory().add(1265, 1);
									sendItemDialogue(new Item(1265, 1), 
										"Dezzick gives you a <col=0000ff>bronze pickaxe</col>!");
									player.setTutorialStage(33);
									stage = 3;
								} else if(stage == 3) {
									end();
									sendTutLines("", 33, new String[] { 
										"<col=0000ff>Mining</col>",
										"It's quite simple really. All you need to do is right click on the",
										"rock and select 'mine'. You can only mine when you have a",
										"pickaxe. So give it a try: first mine one tin ore.", "", "" });
									refreshAll();
								}
							}
							@Override
							public void finish() {
								
							}
						});
					} else if(player.getTutorialStage() == 36) {
						player.getDialogue().start(new Dialogue() {
							@Override
							public void start() {
								sendPlayerDialogue(new String[] { 
									"How do I make a weapon out of this?" }, false);
							}
							@Override
							public void process(int interfaceId, int buttonId) {
								if(stage == 0) {
									sendEntityDialogue(NPC, NORMAL_TALKING, npc.getId(), 
										npc.getName(), new String[] { 
										"Okay, I'll show you how to make a dagger out of it.",
										"You'll be needing this..." }, false);
									stage = 1;
								} else if(stage == 1) {
									sendItemDialogue(new Item(2347, 1), 
										"Dezzick gives you a <col=0000ff>hammer</col>!");
									player.getInventory().add(2347, 1);
									player.setTutorialStage(37);
									stage = 2;
								} else if(stage == 2) {
									end();
									sendTutLines("", 37, new String[] { 
										"<col=0000ff>Smithing a dagger</col>",
										"To smith you'll need a hammer - like the one you were given by", 
										"Dezzick - access to an anvil like the one with the arrow over it",
										"and enough metal bars to make what you are trying to smith.",
										"To start the process, use the bar on one of the anvils.", "" });
									refreshAll();
								}
							}
							@Override
							public void finish() {
								
							}
						});
					} else if(player.getTutorialStage() > 36) {
						player.getDialogue().start(new Dialogue() {
							@Override
							public void start() {
								sendOptions("What do you want to say?", 
									new String[] { 
										"Tell me about prospecting again.", 
										"Tell me about Mining again.",
										"Tell me about Smelting again.",
										"Tell me about Smithing again.", 
										"Nope. I'm ready to move on!" });
							}
							@Override
							public void process(int interfaceId, int buttonId) {
								if(stage == 0 && interfaceId == FIVE_OPTIONS) {
									if(buttonId == OPTION1) {
										sendEntityDialogue(NPC, NORMAL_TALKING, 
											npc.getId(), npc.getName(), new String[] { 
											"To prospect a mineable rock, just right click it",
											"and select the 'prospect rock' option. This will tell",
											"you the type of ore you can mine from it." }, false);
											stage = 1;
									} else if(buttonId == OPTION2) {
										sendEntityDialogue(NPC, NORMAL_TALKING, 
												npc.getId(), npc.getName(), new String[] { 
											"To mine a rock, simply right click it",
											"and select the 'mine' option. This will walk your",
											"character to the rock, and start mining it." }, false);
											stage = 1;
									} else if(buttonId == OPTION3) {
										sendEntityDialogue(NPC, NORMAL_TALKING, 
											npc.getId(), npc.getName(), new String[] { 
											"Smelting is quite simple, really. You must have 2",
											"different ore types to smelt. First, select the 'use'",
											"option on one of the ores in your inventory, then click",
											"the furnace to begin smelting." }, false);
											stage = 1;
									} else if(buttonId == OPTION4) {
										sendEntityDialogue(NPC, NORMAL_TALKING, 
											npc.getId(), npc.getName(), new String[] { 
											"In order to smith, you need a bar of some type.",
											"When you have your bar, select the 'use' option",
											"and click on an anvil. This should bring up the Smithing",
											"interface, and then you can choose what to make." }, false);
											stage = 1;
									} else if(buttonId == OPTION5) {
										sendPlayerDialogue(new String[] { "I'm ready to move on!" }, false);
										stage = 2;
									}
								} else if(stage == 1) {
									start();
									stage = 0;
								} else if(stage == 2)
									end();
							}
							@Override
							public void finish() {
								
							}
						});
					}
				} else if(npc.getId() == COMBAT_INSTRUCTOR) {
					if(player.getTutorialStage() == 39) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								player("Hi! My name's "+player.getDisplayName()+".");
							}
							public void process(int interfaceId, int buttonId) {
								if(stage == 0) {
									sendEntityDialogue(NPC, NORMAL_TALKING, 
										npc.getId(), npc.getName(), new String[] { 
										"Do I look like I care? To me you're just another",
										"newcomer who thinks they're ready to fight." }, false);
										stage = 1;
								} else if(stage == 1) {
									sendEntityDialogue(NPC, NORMAL_TALKING, 
										npc.getId(), npc.getName(), new String[] { 
										"I am Vannaka, the greatest swordsman alive." }, false);
										stage = 2;
								} else if(stage == 2) {
									sendEntityDialogue(NPC, NORMAL_TALKING, 
										npc.getId(), npc.getName(), new String[] { 
										"Let's get started by teaching you how to wield a weapon." }, false);
										stage = 3;
								} else if(stage == 3) {
									end();
									sendTutLines("", 40, new String[] { 
										"<col=0000ff>Wielding weapons</col>", "",
										"You now have access to a new interface. Click on the flashing icon of",
										"a helment, the one to the right of your backpack icon.", "", "" });
									player.packets().sendConfig(1021, 6); // Flashing equipment.
									player.interfaces().sendEquipment();
									refreshAll();
								}
							}
							public void finish() {
								
							}
						});
					} else if(player.getTutorialStage() == 42) {
						player.getDialogue().start(new InfoTextNPC(), npc.getId(), (Object) new String[] { 
						"Let's get started by teaching you how to wield a weapon." });
					} else if(player.getTutorialStage() == 43) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								sendNPCDialogue(npc.getId(), npc.getName(), 
									new String[] { 
									"Very good, but that little butter knife isn't going to",
									"protect you much. Here, take these."}, false);
							}
							public void process(int inter, int button) {
								if(stage == 0) {
									sendItems(new int[] { 1277, 1, 1171, 1 }, 
										"The Combat Guide gives you a <col=0000ff>"
										+ "bronze sword</col> and a <col=0000ff>wooden shield</col>!");
									player.setTutorialStage(44);
									player.getInventory().add(1277, 1);
									player.getInventory().add(1171, 1);
									stage = 1;
								} else if(stage == 1) {
									end();
									sendTutLines("", 44, 
									new String[] { 
									"<col=0000ff>Unequipping items</col>",
									"In your worn equipment tab, right click on the dagger and select the",
									"remove option from the drop down list. After you've unequipped the",
									"dagger, wield the sword and shield. As you pass the mouse over an",
									"item, you will see its name appear at the top left of the screen.", "" });
									refreshAll();
								}
							}
							public void finish() { }
						});
					} else if(player.getTutorialStage() == 49) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								sendPlayerDialogue(new String[] { 
									"I did it! I killed a giant rat!"}, false);
							}
							public void process(int inter, int button) {
								if(stage == 0) {
									sendNPCDialogue(npc.getId(), npc.getName(), 
										new String[] { 
										"I saw, "+player.getDisplayName()+". You seem better at this than I",
										"thought."}, false);
									stage = 1;
								} else if(stage == 1) {
									sendNPCDialogue(npc.getId(), npc.getName(), 
										new String[] { 
										"If you took any damage, you can eat food to restore",
										"your health. You will also find that different food will",
										"give you different amounts of health back."}, false);
									stage = 2;
								} else if(stage == 2) {
									sendNPCDialogue(npc.getId(), npc.getName(), 
										new String[] { 
										"Now that you have grasped the basics of combat, let's",
										"move on."}, false);
									stage = 3;
								} else if(stage == 3) {
									sendNPCDialogue(npc.getId(), npc.getName(), 
										new String[] { 
										"Let's try some ranged attacking. With this you can kill",
										"foes from a distance. Also, foes unable to reach you are",
										"as good as dead. You'll be able to attack the rats",
										"without entering the pit."}, false);
									stage = 4;
								} else if(stage == 4) {
									sendItems(new int[] { 882, 50, 841, 1 }, "The Combat Guide gives you some "
											+ "<col=0000ff>bronze arrows</col> and a <col=0000ff>shortbow</col>!");
									player.getInventory().add(882, 50); // bronze arrows
									player.getInventory().add(841, 1); // Short-bow
									player.setTutorialStage(50);
									stage = 5;
								} else if(stage == 5) {
									end();
									sendTutLines("Rat ranging", 50, new String[] { 
										"",
										"Now you have a bow and some arrows. Before you can use them",
										"you'll need to equip them. Once equipped with the ranging gear, try",
										"killing another rat. Remember: to attack, right click on the monster",
										"and select attack.", "" });
									refreshAll();
								}
							}
							public void finish() { }
						});
					}
				} else if(npc.getId() == FINANCIAL_ADVISOR) {
					if(player.getTutorialStage() == 54) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								sendPlayerDialogue(new String[] { 
									"Hello. Who are you?" }, false);
							}
							public void process(int i, int b) {
								if(stage == 0) {
									sendNPCDialogue(npc.getId(), npc.getName(), 
											new String[] { 
											"I'm the Financial Advisor. I'm here to tell people how to",
											"make money."}, false);
									stage = 1;
								} else if(stage == 1) {
									sendPlayerDialogue(new String[] { 
									"Okay. How can I make money then?" }, false);
									stage = 2;
								} else if(stage == 2) {
									sendNPCDialogue(npc.getId(), npc.getName(), 
											new String[] { 
											"How you can make money? Quite." }, false);
									stage = 3;
								} else if(stage == 3) {
									sendNPCDialogue(npc.getId(), npc.getName(), 
											new String[] { 
											"Well, there are three basic ways of making money here:",
											"combat, quests, and trading. I will talk you through each",
											"of them very quickly." }, false);
									stage = 4;
								} else if(stage == 4) {
									sendNPCDialogue(npc.getId(), npc.getName(), 
											new String[] { 
											"Let's start with combat as it is probably still fresh in",
											"your mind. Many enemies, both human and monster",
											"will drop items when they die." }, false);
									stage = 5;
								} else if(stage == 5) {
									sendNPCDialogue(npc.getId(), npc.getName(), 
											new String[] { 
											"Now, the next way to earn money quickly is by quests.",
											"Many people on RuneScape have things they need",
											"doing, which they will reward you for. " }, false);
									stage = 6;
								} else if(stage == 6) {
									sendNPCDialogue(npc.getId(), npc.getName(), 
											new String[] { 
											"By getting a high level in skills such as Cooking, Mining,",
											"Smithing or Fishing, you can create or catch your own",
											"items and sell them for pure profit." }, false);
									stage = 7;
								} else if(stage == 7) {
									sendNPCDialogue(npc.getId(), npc.getName(), 
											new String[] { 
											"Well, that about covers it. Come back if you'd like to go",
											"over this again." }, false);
									stage = 8;
								} else if(stage == 8) {
									end();
									sendTutLines("", 55, new String[] { "", "", 
											"Continue through the next door.", "", "", "" });
									refreshAll();
								}
							}
							public void finish() { }
						});
					}
				} else if(npc.getId() == BROTHER_BRACE) {
					if(player.getTutorialStage() == 56) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								sendPlayerDialogue(new String[] { 
									"Good day, brother, my name's "+player.getDisplayName()+"." }, false);
							}
							public void process(int inter, int button) {
								if(stage == 0) {
									sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
										"Hello, "+player.getDisplayName()+". I'm Brother Brace. I'm here to tell",
										"you all about Prayer." }, false);
									stage = 1;
								} else if(stage == 1) {
									end();
									sendTutLines("Your Prayer List", 57, new String[] { 
										"", "", "Click on the flashing icon to open your Prayer List.", "", "", "" });
									refreshAll();
									player.packets().sendConfig(1021, 7); // Flashing prayer Tab.
									player.interfaces().sendPrayerBook();
								}
							}
							public void finish() { }
						});
					} else if(player.getTutorialStage() == 58) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
									"This is your Prayer List. Prayers can help a lot in",
									"combat. Click on the prayer you wish to use to activate",
									"it, and click it again to deactivate it." }, false);
							}
							public void process(int inter, int button) {
								if(stage == 0) {
									sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
										"Active prayers will drain your Prayer Points which",
										"you can recharge by finding an altar or another holy spot",
										"and praying there." }, false);
									stage = 1;
								} else if(stage == 1) {
									sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
										"As you noticed, most enemies will drop bones when",
										"defeated. Burying bones, by clicking them in your",
										"inventory will gain you Prayer Experience." }, false);
									stage = 2;
								} else if(stage == 2) {
									sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
										"I'm also the community officer 'round here, so it's my",
										"job to tell you about your Friends and Ignore Lists." }, false);
									stage = 3;
								} else if(stage == 3) {
									end();
									sendTutLines("Friends List", 59, new String[] { 
										"", "", "You should now see another new icon. Click on the flashing green", 
										"face to open your Friends List.", "", "" });
									refreshAll();
									player.packets().sendConfig(1021, 10); // Flashing Friends Tab.
									player.interfaces().sendFriendsTab();
								}
							}
							public void finish() { }
						});
					} else if(player.getTutorialStage() == 61 || player.getTutorialStage() == 62) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								player.setTutorialStage(62);
								sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
									"Good. Now you have both menus open I'll tell you a",
									"little about each. You can add people to either list by",
									"clicking the add button then typing their name into the",
									"box that appears." }, false);
							}
							public void process(int inter, int button) {
								if(stage == 0) {
									sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
										"You remove people from the lists in the same way. If",
										"you add someone to your ignore list, they will not be",
										"able to talk to you or send any form of message to you."}, false);
									stage = 1;
								} else if(stage == 1) {
									sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
										"Your Friends List shows the online status of your",
										"friends. Friends in red are offline, friends in green are",
										"online and on the same server and friends in yellow",
										"are online, but on a different server." }, false);
									stage = 2;
								} else if(stage == 2) {
									sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
										"There is also a system that allows you to chat with",
										"your friends and clan-mates in a private channel. This",
										"is called the 'Clan Chat', and will become available to you",
										"later on."}, false);
									stage = 3;
								} else if(stage == 3) {
									sendPlayerDialogue(new String[] { "Are there rules on in-game behaviour?" }, false);
									stage = 4;
								} else if(stage == 4) {
									sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
										"Yes, you should read the rules of conduct on the",
										"website to make sure you do nothing to get yourself",
										"banned." }, false);
									stage = 5;
								} else if(stage == 5) {
									sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
										"But in general, always try to be courteous to other",
										"players - remember the people in the game are real",
										"people with real feelings." }, false);
										stage = 6;
								} else if(stage == 6) {
									sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
										"If you go 'round being abusive or causing trouble, your",
										"character could end up being the one in trouble." }, false);
									stage = 7;
								} else if(stage == 7) {
									sendPlayerDialogue(new String[] { "Okay, thanks. I'll bear 'that in mind. "}, false);
									stage = 8; 
								} else if(stage == 8) {
									end();
									sendTutLines("Your final Instructor!", 63, new String[] { 
										"",
										"",
										"You're almost finished on tutorial island. Pass through the door to find",
										"the path leading to your final instructor.",
										"",
										"" });
									refreshAll();
								}
							}
							public void finish() { }
						});
					}
				} else if(npc.getId() == MAGIC_INSTRUCTOR) {
					if(player.getTutorialStage() == 64) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								sendPlayerDialogue(new String[] { "Hello." }, false);
							}
							public void process(int inter, int button) {
								if(stage == 0) {
									sendNPCDialogue(npc.getId(), npc.getName(), new String[] { 
										"Good day, newcomer. My name is Terrova. I'm here",
										"to tell you about Magic. Let's start by opening your",
										"spellbook." }, false);
									stage = 1;
								} else if(stage == 1) {
									end();
									sendTutLines("Open up your final tab.", 65, new String[] { 
										"", "", "Open up the Magic Spellbook tab by clicking on the flashing icon next",
										"to the Prayer List tab you just learned about.", "", "" });
									refreshAll();
									player.packets().sendConfig(1021, 8); // Flashing Magic tab
									player.interfaces().sendMagicBook();
								}
							}
							public void finish() { }
						});
					} else if(player.getTutorialStage() == 66) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", new String[] { 
									"Good. This is a list of your spells. Currently you can",
									"only cast one offensive spell called Wind Strike. Let's",
									"try it out one of those chickens." }, false);
							}
							public void process(int i, int b) {
								if(stage == 0) {
									player.setTutorialStage(67);
									sendItems(new int[] { 556, 5, 558, 5 }, 
										"Terrova gives you five <col=0000ff>air runes</col> "
										+ "and five <col=0000ff>mind runes</col!");
									player.getInventory().add(556, 5);
									player.getInventory().add(558, 5);
									stage = 1;
								} else if(stage == 1) {
									end();
									sendTutLines("Cast Wind Strike at a chicken.", 67, new String[] { "",
										"Now you have runes you should see the Wind Strike icon at the top-",
										"left of your spellbook, second in from the left. Walk over to the caged",
										"chickens, click on the Wind Strike icon and then select one of the chickens",
										"to cast it on. It may take several tries. If you need more runes ask",
										"Terrova." });
									refreshAll();
								}
							}
							public void finish() { }
						});
					} else if(player.getTutorialStage() == 67 && !player.getInventory().contains(556, 1) 
							|| player.getTutorialStage() == 67 && !player.getInventory().contains(558, 1)) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", new String[] { 
									"Since you have run out of runes, here are some more!" }, false);
							}
							public void process(int i, int b) {
								if(stage == 0) {
									player.setTutorialStage(67);
									sendItems(new int[] { 556, 5, 558, 5 }, 
										"Terrova gives you five <col=0000ff>air runes</col> "
										+ "and five <col=0000ff>mind runes</col!");
									player.getInventory().add(556, 5);
									player.getInventory().add(558, 5);
									stage = 1;
								} else if(stage == 1) {
									end();
									refreshAll();
								}
							}
							public void finish() { }
						});
					} else if(player.getTutorialStage() == 68) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", new String[] { 
									"Well, you're all finished here now. I'll give you a reasonable",
									"number of runes when you leave." }, false);
							}
							public void process(int i, int b) {
								if(stage == 0) {
									sendOptions("Do you want to go to the mainland?", new String[] { 
										"Yes.", "No." });
									stage = 1;
								} else if(stage == 1) {
									if(b == OPTION1) {
										sendPlayerDialogue(new String[] { 
											"Yes, I'm ready to leave." }, false);
										stage = 2;
									} else if(b == OPTION2) {
										end();
										refreshAll();
									}
								} else if(stage == 2) {
									sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", new String[] { 
										"Good good. I've deactivated the protective spells around",
										"the island, so now you can teleport yourself out of here." }, false);
									stage = 3;
								} else if(stage == 3) {
									sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", new String[] { 
										"When you get to the mainland you will find yourself in",
										"the town of Lumbridge. If you want some ideas on",
										"where to go next, talk to my friend Phileas, also known",
										"as the Lumbridge Guide. You can't miss him; he's"}, false);
									stage = 4;
								} else if(stage == 4) {
									sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", new String[] { 
										"holding a big staff with a question mark on the end. He",
										"also has a white beard and carries a rucksack full of",
										"scrolls. There are also tutors willing to teach you about", 
										"the many skills you could learn."}, false);
									stage = 5;
								} else if(stage == 5) {
									player.packets().sendModelOnIComponent(244, 1, 7185); // Question mark lol
									sendNPCDialogue(-1, "", new String[] { 
											"When you get to Lumbridge, look for the '?' icon on your",
											"minimap. The Lumbridge Guide and the other tutors",
											"will be standing near one of these. The Lumbridge",
											"Guide should be standing sligtly to the north-east of" }, false);
									stage = 6;
								} else if(stage == 6) {
									player.packets().sendModelOnIComponent(244, 1, 7185); // Question mark lol
									sendNPCDialogue(-1, "", new String[] { 
											"", "the castle's courtyard and the others you will find",
											"scattered around Lumbridge.", "" }, false);
									stage = 7;
								} else if(stage == 7) {
									sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", new String[] { 
											"If all else fails, visit the RuneScape website for a whole",
											"chestload of information on quests, skills and minigames",
											"as well as a very good starter's guide." }, false);
									stage = 8;
								} else if(stage == 8) {
									end();
									sendTutLines("You have almost completed the tutorial!", 69, 
											new String[] { 
											"", "Just click on the first spell, Home Teleport in your Magic Spellbook.",
											"This spell doesn't require any runes, but can only be cast once every",
											"30 minutes.", "", "" });
									refreshAll();
								}
							}
							public void finish() { }
						});
					} else if(player.getTutorialStage() == 69) {
						player.getDialogue().start(new Dialogue() {
							public void start() {
								sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", new String[] { 
										"Good good. I've deactivated the protective spells around",
										"the island, so now you can teleport yourself out of here." }, false);
							}
							public void process(int i, int b) {
								if(stage == 0) {
									sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", new String[] { 
										"When you get to the mainland you will find yourself in",
										"the town of Lumbridge. If you want some ideas on",
										"where to go next, talk to my friend Phileas, also known",
										"as the Lumbridge Guide. You can't miss him; he's"}, false);
									stage = 4;
								} else if(stage == 4) {
									sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", new String[] { 
										"holding a big staff with a question mark on the end. He",
										"also has a white beard and carries a rucksack full of",
										"scrolls. There are also tutors willing to teach you about", 
										"the many skills you could learn."}, false);
									stage = 5;
								} else if(stage == 5) {
									sendNPCDialogue(-1, "", new String[] { 
											"When you get to Lumbridge, look for the '?' icon on your",
											"minimap. The Lumbridge Guide and the other tutors",
											"will be standing near one of these. The Lumbridge",
											"Guide should be standing sligtly to the north-east of" }, false);
									stage = 6;
								} else if(stage == 6) {
									sendNPCDialogue(-1, "", new String[] { 
											"", "the castle's courtyard and the others you will find",
											"scattered around Lumbridge." }, false);
									stage = 7;
								} else if(stage == 7) {
									sendNPCDialogue(MAGIC_INSTRUCTOR, "Magic instructor", new String[] { 
											"If all else fails, visit the RuneScape website for a whole",
											"chestload of information on quests, skills and minigames",
											"as well as a very good starter's guide." }, false);
									stage = 8;
								} else if(stage == 8) {
									end();
									sendTutLines("You have almost completed the tutorial!", 69, 
										new String[] { 
										"", "Just click on the first spell, Home Teleport in your Magic Spellbook.",
										"This spell doesn't require any runes, but can only be cast once every",
										"30 minutes." });
									refreshAll();
								}
							}
							public void finish() { }
						});
					}
				}
			}
		}, npc.getId() == RUNESCAPE_GUIDE ? 1 : npc.getSize()));
		return false;
	}
	
	@Override
	public void process() {
		player.packets().sendOverlay2(371);
		player.packets().sendHideIComponent(371, 4, true);
	}
	

	@Override
	public void sendInterfaces() {
		int stage = player.getTutorialStage();
		boolean rezi = player.interfaces().hasResizableScreen();
		player.packets().sendOverlay2(371);
		player.packets().sendHideIComponent(371, 4, true);
		player.packets().closeInterface(rezi ? 29 : 150);// Attack tab
		player.packets().closeInterface(rezi ? 30 : 151);// Skill tab
		if(stage < 27)
		player.packets().closeInterface(rezi ? 31 : 152);// Quest tab
		player.packets().closeInterface(rezi ? 32 : 153);// Achievement tab
		player.packets().closeInterface(rezi ? 33 : 154);// Inventory tab
		player.packets().closeInterface(rezi ? 34 : 155);// Equipment tab
		player.packets().closeInterface(rezi ? 35 : 156);// pray tab
		player.packets().closeInterface(rezi ? 36 : 157);// magic tab
		player.packets().closeInterface(rezi ? 38 : 159);// Friend tab
		player.packets().closeInterface(rezi ? 39 : 160);// Ignore tab
		player.packets().closeInterface(rezi ? 40 : 161);// Clan tab
		player.packets().closeInterface(rezi ? 41 : 162);// Settings tab
		player.packets().closeInterface(rezi ? 42 : 163);// Emote tab
		if(stage < 20)
			player.packets().closeInterface(rezi ? 43 : 164);// Music tab
		player.packets().closeInterface(rezi ? 44 : 165);// Notes tab
		if(stage == 2) {
			player.interfaces().sendSettingsTab();
			player.packets().sendConfig(1021, 13); // Settings tab flashing
		} else if(stage >= 3)
			player.interfaces().sendSettingsTab();
		if(stage == 6) {
			player.interfaces().sendInventoryTab();
			player.packets().sendConfig(1021, 5); // Inventory flashing
		} else if(stage >= 7)
			player.interfaces().sendInventoryTab();
		if(stage == 9) {
			player.interfaces().sendSkills();
			player.packets().sendConfig(1021, 2); // Skills flashing
		} else if(stage > 9)
			player.interfaces().sendSkills();
		if(stage == 19) {
			player.interfaces().sendMusicPlayer();
			player.packets().sendConfig(1021, 15); // Flashing Music Player
		}
		if(stage == 21) {
			player.interfaces().sendEmotes();
			player.packets().sendConfig(1021, 14); // Flashing Emotes
		} else if(stage > 21)
			player.interfaces().sendEmotes();
		if(stage == 26) {
			player.interfaces().sendQuestTab();
			player.packets().sendConfig(1021, 3); // Flashing Quest Tab
			player.packets().sendUnlockIComponentOptionSlots(190, 18, 0, 201, 0, 1, 2, 3);
			player.packets().sendConfig(281, 1);
		} else if(stage > 26)
			player.packets().sendUnlockIComponentOptionSlots(190, 18, 0, 201, 0, 1, 2, 3);
		if(stage == 40) {
			player.packets().sendConfig(1021, 6); // Flashing equipment.
			player.interfaces().sendEquipment();
		} else if(stage > 40)
			player.interfaces().sendEquipment();
		if(stage == 45) {
			player.interfaces().sendAttackTab();
			player.packets().sendConfig(1021, 1); // Flashing attack styles icon
			player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		} else if(player.getTutorialStage() > 45) {
			player.interfaces().sendAttackTab();
			player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		}
		if(player.getTutorialStage() == 57) {
			player.packets().sendConfig(1021, 7); // Flashing prayer Tab.
			player.interfaces().sendPrayerBook();
		} else if(player.getTutorialStage() > 57)
			player.interfaces().sendPrayerBook();
		if(player.getTutorialStage() == 59) {
			player.packets().sendConfig(1021, 10); // Flashing Friends Tab.
			player.interfaces().sendFriendsTab();
		} else if(player.getTutorialStage() > 59)
			player.interfaces().sendFriendsTab();
		if(player.getTutorialStage() == 60) {
			player.packets().sendConfig(1021, 11); // Flashing Ignores.
			player.interfaces().sendIgnoresTab();
		} else if(stage > 60)
			player.interfaces().sendIgnoresTab();
		if(stage == 65) {
			player.packets().sendConfig(1021, 8); // Flashing Magic tab
			player.interfaces().sendMagicBook();
		} else if(stage > 65)
			player.interfaces().sendMagicBook();
	}
	
	public void sendTutLines(String title, int stageId, String[] lines) {
		player.packets().sendHideIComponent(371, 4, true);
		player.interfaces().sendNRChatbox(372);
		player.packets().sendIComponentText(372, 0, title);
		player.packets().sendIComponentText(372, 1, lines[0]);
		player.packets().sendIComponentText(372, 2, lines[1]);
		player.packets().sendIComponentText(372, 3, lines[2]);
		player.packets().sendIComponentText(372, 4, lines[3]);
		player.packets().sendIComponentText(372, 5, lines[4]);
		player.packets().sendIComponentText(372, 6, lines[5]);
		player.setTutorialStage(stageId);
	}


	@Override
	public boolean login() {
		start();
		return false;
	}


	@Override
	public boolean logout() {
		return false;
	}

	public static void finishTutorial(final Player player) {
		player.stopAll();
		player.getControllerManager().removeControllerWithoutCheck();
		player.setCompletedTutorial(true);
		player.interfaces().refreshAllTabs();
		player.setNextAnimation(new Animation(8939));
		player.setNextGraphics(new Graphics(1576));
		player.packets().sendSound(200, 0, 1);
		player.lock(3 + 1);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				Location tile = new Location(3233, 3230, 0);
				for (int trycount = 0; trycount < 10; trycount++) {
					tile = new Location(tile, 2);
						if (Game.canMoveNPC(tile.getZ(), tile.getX(), tile.getY(), player.getSize()))
							break;
					}
				player.getBank().reset();
				player.getBank().addItem(new Item(995, 25), true);
				player.getInventory().reset();
				player.packets().sendRemoveOverlay2();
				player.getEquipment().reset();
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				player.getInventory().add(1351, 1); // Bronze hatchet
				player.getInventory().add(TutorialIsland.TINDERBOX, 1); // Tinder box
				player.getInventory().add(303, 1); // Small Fishing Net
				player.getInventory().add(315, 1); // Shrimps
				player.getInventory().add(TutorialIsland.EMPTY_BUCKET, 1); // Empty Bucket
				player.getInventory().add(TutorialIsland.EMPTY_POT, 1); // Empty Pot
				player.getInventory().add(2309, 1); // Bread
				player.getInventory().add(1265, 1); // Bronze pickaxe
				player.getInventory().add(1205, 1); // Bronze dagger
				player.getInventory().add(1291, 1); // Bronze longsword
				player.getInventory().add(1171, 1); // Wooden shield
				player.getInventory().add(841, 1); // Shortbow
				player.getInventory().add(882, 25); // Bronze arrow
				player.getInventory().add(556, 25); // Air runes
				player.getInventory().add(558, 15); // Mind runes
				player.getInventory().add(555, 6); // Water runes
				player.getInventory().add(557, 4); // Earth runes
				player.getInventory().add(559, 2); // Bronze hatchet
				player.setNewLocation(new Location(3233, 3230, 0)); // Lumbridge
				player.closeAllInterfaces();
				player.getDialogue().start(new InfoText(), (Object) new String[] { 
					"Welcome to Lumbridge! To get more help, simply click on the",
					"Lumbridge Guide or one of the Tutors - these can be found by",
					"looking for the question mark icon on your minimap. If you find you",
					"are lost at any time, look for a signpost or use the Lumbridge Home",
					"Teleport spell." });
				player.getControllerManager().magicTeleported(0);
				player.setNextAnimation(new Animation(8941));
				player.setNextGraphics(new Graphics(1577));
				player.packets().sendSound(201, 0, 1);
				player.faceLocation(new Location(tile.getX(), tile.getY() - 1, tile.getZ()));
				player.setDirection(6);
				stop();
			}
		}, 3, 0);
	}
	
}