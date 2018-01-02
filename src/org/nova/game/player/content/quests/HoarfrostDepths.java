package org.nova.game.player.content.quests;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class HoarfrostDepths {
	private static boolean resizableScreen;

	public static int 
	QUEST_BIND = 1024,
	MAIN_TITLE = 82,
	MAIN_TEXT = 7,
	QUEST_POINTS = 5,
	QUEST_INTERFACE = 72,
	BIND_TAB = 174
	;
	
	public static void ShowBindforHoarfrostDepths(Player player) {
		if (player.getHoarfrostdepthsStage() == 3) {
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				player.packets().sendHideIComponent(1024, 5, true);
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Hoarfrost Depths");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"You have went in correct cave. Now you should find Ice Elemental. Once you kill him and achieve the book go speak with Meteora, only Meteora can read the book and understand it. Meteora was located in Lunar Isle.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			}
		else if (player.getHoarfrostdepthsStage() == 2) {
			QuestBind.CleanTexts(player);
			player.closeInterfaces();
			player.packets().sendHideIComponent(1024, 5, true);
			player.interfaces().sendBindingTab();
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Hoarfrost Depths");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"Meteora has heard some wierd noises and sounds coming from cave at Lunar Isle. I should get my fighting equipment and move in to cave and check out if something is wrong in there. Food will be needed.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
		}
		else if (player.getHoarfrostdepthsStage() == 1) {
			player.setHoarfrostStage(1);
				QuestBind.CleanTexts(player);
				player.packets().sendHideIComponent(1024, 5, true);
				player.closeInterfaces();
				player.interfaces().sendBindingTab();
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Hoarfrost Depths");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"I should move straight to Lunar Isle, the Quest Rewarder should teleport me there. Maybe I should speak with him and find Meteora inside of the Lunar Isle.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			} else {
					QuestBind.forceCloseInterfaces(player);
					player.packets().sendHideIComponent(1024, 5, true);
					player.interfaces().sendBindingTab();
					player.sm("<col=ff0033>Quest System: You have binded a new quest: Hoarfrost Depths.");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
							"Hoarfrost Depths");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
							"I should start the quest by speaking to Osman, which is located near of Al-Kharid Kingdom. I should go speak with him and ask does he need help for the first.");
					player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
							"Quest points: "+player.questpoints+".");
			}
	}
	public boolean hasRezizableScreen() {
		return resizableScreen;
	}




/**
 * Sends reward once quest is complete.
 * @param player
 */
public static void sendReward(Player player) {
	if (player.isCompletedHoarfrostDepths())
		return;
	if (player.getInventory().containsItem(600, 1)) {
		player.getInventory().deleteItem(600, 1);
		player.getInventory().refresh();
		QuestBind.CleanTexts(player);
		player.questpoints = +16; 
		player.setCompletedHoarfrostDepths(); 
		player.getInventory().addItem(995, 200000);
		player.sm("<col=ff0033>Quest System: Battle Quest Hoarfrost Depths has been completed.");
		player.packets().sendIComponentText(277, 3,
				"Hoarfrost Depths Completed!");
		player.packets().sendIComponentText(277, 4,
				"Reward(s):");
		player.packets().sendIComponentText(277, 9,
				"New store unlocked");
		player.packets().sendIComponentText(277, 10,
				"Received 200,000 coins");
		player.packets().sendIComponentText(277, 11,
				"Ability to teleport Jatizso");
		player.packets().sendIComponentText(277, 12,
				"");
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
