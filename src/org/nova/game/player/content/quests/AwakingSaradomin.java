package org.nova.game.player.content.quests;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class AwakingSaradomin {
	
	private static boolean resizableScreen;
	private static boolean foundbook;
	
	public static int QUEST_BIND = 1024,MAIN_TITLE = 82,MAIN_TEXT = 7,
	QUEST_POINTS = 5,QUEST_INTERFACE = 72,BIND_TAB = 174;
	
	
	/**
	 * 10k player i know but this must be very goodly handled lol
	 * @param player
	 */
	public static void introduceQuest(final Player player) {
		player.stopAll();
		breakScene(player);	
		breakScene(player);	
		player.teleportPlayer(3240, 3451, 1);
		player.sendMessage("And so the sleep starts. Zzzz...");
		player.packets().sendCutscene(0);
		WorldTasksManager.schedule(new WorldTask() {
		int loop;
		
		@Override
		public void run() {
			if (loop == 40) {
			breakScene(player);	
			player.setSaradominStage(1);
			player.setNextForceTalk(new ForceTalk("What a dream... Where am I?"));
			}
			else if (loop == 41) {
				player.setNextAnimation(new Animation(3));
			}
			loop++;
			}
		}, 0, 1);
	}
	
	public static void breakScene(Player player) {
	player.packets().sendBlackOut(0);
	player.packets().sendMapRegion(false);
	}
	
	public static void ShowBind(Player player) {
		if (player.getRuinsofuzerStage() == 5) {
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				player.packets().sendHideIComponent(1024, 5, true);
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Awaking the Saradomin");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"I have now enough knowledge of the Lucien. I should head now inside cave which is at Awaking the Saradomin. This fight will be not easy, so I should be prepared with some good equipment, including food and potions. Once you kill Lucien, bring the Strange Book to Locks and traps trainer, which is located at Nova home. He'll be rewarding you.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			}
		else if (player.getRuinsofuzerStage() == 4) {
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				player.packets().sendHideIComponent(1024, 5, true);
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Awaking the Saradomin");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"I have to visit Varrock museum now, I have been given a security code: MUFPAH. Once I reach Varrock museum I should talk to Azzandra for information of this Lucien.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			}
		else if (player.getRuinsofuzerStage() == 3) {
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				player.packets().sendHideIComponent(1024, 5, true);
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Awaking the Saradomin");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"I have to visit Varrock museum now, I have been given a security code: MUFPAH. Once I reach Varrock museum I should talk to Azzandra for information of this Lucien.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			}
		else if (player.getRuinsofuzerStage() == 2) {
			QuestBind.CleanTexts(player);
			player.closeInterfaces();
			player.packets().sendHideIComponent(1024, 5, true);
			player.interfaces().sendBindingTab();
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Awaking the Saradomin");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"This is an easy part. Find a book inside Varrock Castle, from the library, the one that Reldo is in. Once you find the book show it to Reldo and he'll convert the book for you.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
		}
		else if (player.getRuinsofuzerStage() == 1) {
				QuestBind.CleanTexts(player);
				player.packets().sendHideIComponent(1024, 5, true);
				player.closeInterfaces();
				player.interfaces().sendBindingTab();
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Awaking the Saradomin");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"You have woken up back from the mysterious dream, I should get back to museum and talk with Curator Haig Halen again for more instructions.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			} else {
					QuestBind.forceCloseInterfaces(player);
					player.packets().sendHideIComponent(1024, 5, true);
					player.interfaces().sendBindingTab();
					player.sm("<col=ff0033>Quest System: You have binded a new quest: Awaking the Saradomin.");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
							"Awaking the Saradomin");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
							"This quest can be started in Varrock's Museum, by speaking to Curator Haig Halen.");
					player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
							"Quest points: "+player.questpoints+".");
			}
	}
	
/**
 * 
 * @return
 */
	public boolean hasRezizableScreen() {
		return resizableScreen;
	}
/**
 * Sends reward once quest is complete.
 * @param player
 */
public static void sendReward(Player player) {
	if (player.isCompletedRuinsofUzer())
		return;
	player.teleportPlayer(3483, 3089, 0);
		player.getInventory().refresh();
		QuestBind.CleanTexts(player);
		player.questpoints = +29; 
		player.setCompletedRuinsofUzer();
		player.getInventory().addItem(995, 250000);
		player.getInventory().addItem(6570, 1);
		player.sm("<col=ff0033>Quest System: Battle Quest: Awaking the Saradomin has been completed.");
		player.packets().sendIComponentText(277, 3,
				"Awaking the Saradomin Completed!");
		player.packets().sendIComponentText(277, 4,
				"Reward(s):");
		player.packets().sendIComponentText(277, 9,
				"New store unlocked");
		player.packets().sendIComponentText(277, 10,
				"Received 250,000 coins");
		player.packets().sendIComponentText(277, 11,
				"Awaking the Saradomin Services unlocked.");
		player.packets().sendIComponentText(277, 12,
				"Ability for Curse Prayers unlocked.");
		player.packets().sendIComponentText(277,13,
				"Received  1x Firecape.");
		player.packets().sendIComponentText(277, 14,
				"");
		player.packets().sendIComponentText(277, 6,
				"Quest Points; "+player.questpoints+"/350");
		player.packets().sendIComponentText(277, 7,
				"");
		player.packets().sendIComponentText(277, 15,
				"");
		player.packets().sendIComponentText(277, 16,
				"");
		player.packets().sendIComponentText(277, 17,
				"");
		player.interfaces().sendInterface(277);
}

public static boolean isFoundbook() {
	return foundbook;
}

public static void setFoundbook(boolean foundbook) {
	AwakingSaradomin.foundbook = foundbook;
}
}
