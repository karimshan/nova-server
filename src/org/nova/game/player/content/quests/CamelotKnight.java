package org.nova.game.player.content.quests;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class CamelotKnight {
	
	/**
	 * Data
	 */
	int rewardxp = 500;
	private static boolean resizableScreen;

	public static int 
	QUEST_BIND = 1024,
	MAIN_TITLE = 82,
	MAIN_TEXT = 7,
	QUEST_POINTS = 5,
	QUEST_INTERFACE = 72,
	BIND_TAB = 174
	;
	
	public static void ShowBind(Player player) {
		
		 if (player.getCamelotknightStage() == 4) {
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Knight of Camelot");
						player.packets().sendHideIComponent(1024, 5, true);
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"Now it's time to find and kill the sea troll queen, I must range it, if I don't have I can leave the underwater by using anchor. And if I want to go back I simply speak with King Arthur again.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
				QuestSystem.sendUpdate(player);
			}
		else if (player.getCamelotknightStage() == 3) {
				QuestBind.CleanTexts(player);
				player.packets().sendHideIComponent(1024, 5, true);
				player.closeInterfaces();
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Knight of Camelot");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"I have earned the permission, now I should head back to Camelot castle. Once i am there I should tell King Arthurt that I am ready to go.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
				QuestSystem.sendUpdate(player);
			}
		else if (player.getCamelotknightStage() == 2) {
			QuestBind.CleanTexts(player);
			player.closeInterfaces();
			player.packets().sendHideIComponent(1024, 5, true);
			player.interfaces().sendBindingTab();
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Knight of Camelot");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"I have to go and cook 5 cooked chickens for Sigli the Huntsman, maybe he will be giving me the permission then.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
			QuestSystem.sendUpdate(player);
		}
		else if (player.getCamelotknightStage() == 1) {
				QuestBind.CleanTexts(player);
				player.packets().sendHideIComponent(1024, 5, true);
				player.closeInterfaces();
				player.interfaces().sendBindingTab();
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Knight of Camelot");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"King Arthur has told me to go Relleka and speak with Sigli the Huntsman, I must ask his permission to defeat the sea troll queen.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
				QuestSystem.sendUpdate(player);
			} else {
					QuestBind.forceCloseInterfaces(player);
					player.packets().sendHideIComponent(1024, 5, true);
					player.interfaces().sendBindingTab();
					player.sm("<col=ff0033>Quest System: You have binded a new quest: Knight of Camelot.");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
							"Knight of Camelot");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
							"Head straight in to Camelot, there will be King Arthur inside Camelot's castle. He will be telling you what to do.");
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
	if (player.isCamelotKnight() == true) {
	return;	
	}
	else {
		player.getInventory().refresh();
		QuestBind.CleanTexts(player);
		player.questpoints = +6; 
		player.setCamelotKnight(true);
		player.getInventory().addItem(10890, 1);
		player.sm("<col=ff0033>Quest System: Battle Quest Knight of Camelot has been completed.");
		player.packets().sendIComponentText(277, 3,
				"Knight of Camelot Completed!");
		player.packets().sendIComponentText(277, 4,
				"Reward(s):");
		player.packets().sendIComponentText(277, 9,
				"New store unlocked.");
		player.packets().sendIComponentText(277, 10,
				"Received Prayer book.");
		player.packets().sendIComponentText(277, 11,
				"Ability to quick-teleport to Relleka.");
		player.packets().sendIComponentText(277, 12,
				"Ability to cure poisons.");
		player.packets().sendIComponentText(277,13,
				"");
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
	
}
}
