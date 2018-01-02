package org.nova.game.player.content.quests;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class UzerRuins {
	
	private static boolean resizableScreen;
	private static boolean foundbook;
	
	public static int 
	QUEST_BIND = 1024,
	MAIN_TITLE = 82,
	MAIN_TEXT = 7,
	QUEST_POINTS = 5,
	QUEST_INTERFACE = 72,
	BIND_TAB = 174
	;
	
	public static void ShowBind(Player player) {
		if (player.getRuinsofuzerStage() == 5) {
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				player.packets().sendHideIComponent(1024, 5, true);
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Ruins of Uzer");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"I have now enough knowledge of the Lucien. I should head now inside cave which is at Ruins of Uzer. This fight will be not easy, so I should be prepared with some good equipment, including food and potions. Once you kill Lucien, bring the Strange Book to Locks and traps trainer, which is located at Nova home. He'll be rewarding you.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			}
		else if (player.getRuinsofuzerStage() == 4) {
				QuestBind.CleanTexts(player);
				player.packets().sendHideIComponent(1024, 5, true);
				player.closeInterfaces();
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Ruins of Uzer");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"I have to visit Varrock museum now, I have been given a security code: MUFPAH. Once I reach Varrock museum I should talk to Azzandra for information of this Lucien.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			}
		else if (player.getRuinsofuzerStage() == 3) {
				QuestBind.CleanTexts(player);
				player.packets().sendHideIComponent(1024, 5, true);
				player.closeInterfaces();
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Ruins of Uzer");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"I have to visit Varrock museum now, I have been given a security code: MUFPAH. Once I reach Varrock museum I should talk to Azzandra for information of this Lucien.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			}
		else if (player.getRuinsofuzerStage() == 2) {
			QuestBind.CleanTexts(player);
			player.packets().sendHideIComponent(1024, 5, true);
			player.closeInterfaces();
			player.interfaces().sendBindingTab();
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Ruins of Uzer");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"This is an easy part. Find a book inside Varrock Castle, from the library, the one that Reldo is in. Once you find the book show it to Reldo and he'll convert the book for you.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
		}
		else if (player.getRuinsofuzerStage() == 1) {
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				player.packets().sendHideIComponent(1024, 5, true);
				player.interfaces().sendBindingTab();
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Ruins of Uzer");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"Sir Owen has told me to go inside the Varrock castle, there will be library where Reldo is. He told me to speak with Reldo to receive more information about the monster.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			} else {
					QuestBind.forceCloseInterfaces(player);
					player.packets().sendHideIComponent(1024, 5, true);
					player.interfaces().sendBindingTab();
					player.sm("<col=ff0033>Quest System: You have binded a new quest: Ruins of Uzer.");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
							"Ruins of Uzer");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
							"I can start this quest by speaking to: Sir Owen, which will be located Ruins of Uzer. Ruins of Uzer is deep inside the deserts of Al-Kharid. If I don't know where to go, I should check the location from world map.");
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
		player.sm("<col=ff0033>Quest System: Battle Quest: Ruins of Uzer has been completed.");
		player.packets().sendIComponentText(277, 3,
				"Ruins of Uzer Completed!");
		player.packets().sendIComponentText(277, 4,
				"Reward(s):");
		player.packets().sendIComponentText(277, 9,
				"New store unlocked");
		player.packets().sendIComponentText(277, 10,
				"Received 250,000 coins");
		player.packets().sendIComponentText(277, 11,
				"Ruins of Uzer Services unlocked.");
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
	UzerRuins.foundbook = foundbook;
}
}
