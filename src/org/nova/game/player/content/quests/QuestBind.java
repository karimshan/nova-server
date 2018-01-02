package org.nova.game.player.content.quests;

import org.nova.game.player.Player;

/**
 * @author Nexon / Fuzen Seth
 * This file will handle the quest binding.
 * 
 */
public class QuestBind {
private static boolean resizableScreen;

public static int 
QUEST_BIND = 1024,
MAIN_TITLE = 82,
MAIN_TEXT = 7,
QUEST_POINTS = 5,
QUEST_INTERFACE = 72,
BIND_TAB = 174
;

	public static void unBind(Player player) {
		CleanTexts(player);
		player.sm("<col=ff0033>Quest System: You have unbind your current quest.");
		player.interfaces().removeTab(resizableScreen ? 93 : 207);
	}

	public static void CleanTexts(Player player) {
		player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
				"Binded Quest: None.");
		player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
				"My current quest is: none.");
		player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
				"Quest points: "+player.questpoints+".");
	}
	
	public boolean hasRezizableScreen() {
		return resizableScreen;
	}
	
	public static void ShowBindforHoarfrost(Player player) {
		if (player.getHoarfrostdepthsStage() == 3) {
			player.closeInterfaces();
			CleanTexts(player);
			player.interfaces().sendBindingTab();
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Hoarfrost Hollow");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"This will be the final fight. This battle is not going to be easy. I must find an Ice Demon from the icy caves of Crandor. Head to top of crandor the cave will be located there, after that try to find an Ice Demon and beat it, once this is done you'll see a tunnel. Pick the mysterious orb from Ice Demon and enter in to the tunnel.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
		}
		else if (player.getHoarfrostStage() == 2) {
			player.closeInterfaces();
			CleanTexts(player);
			player.interfaces().sendBindingTab();
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Hoarfrost Hollow");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"Frank has given me the passport. I should find ned from ship located in Port Sarim. I should get prepared to fight the mysterious ice demon.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
		}
		else if (player.getHoarfrostStage() == 1) {
			CleanTexts(player);
			player.closeInterfaces();
			player.interfaces().sendBindingTab();
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Hoarfrost Hollow");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"Ned has asked me to speak with Frank, he will be located inside of Port Sarim, near the bar.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
		} else {
				forceCloseInterfaces(player);
				player.interfaces().sendBindingTab();
				player.sm("Game: You have binded a new quest: Hoarfrost Hollow.");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Hoarfrost Hollow");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"I should start the quest by speaking to Ned. Ned is located inside the houses of Draynor Village.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
		}
	}
	
	public static void forceCloseInterfaces(Player player) {
		player.closeInterfaces();
		player.interfaces().closeChatBoxInterface();
	}
	
}
