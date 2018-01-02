package org.nova.game.player.content.quests;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class DraynorMyth {
	
	/**
	 * Data
	 */
	private static boolean stage1;
	private static boolean stage2;
	private static boolean stage3;
	private static boolean stage4;
	private static boolean stage5;
	private static boolean stage6;
	private static boolean resizableScreen;

	public static int 
	QUEST_BIND = 1024,
	MAIN_TITLE = 82,
	MAIN_TEXT = 7,
	QUEST_POINTS = 5,
	QUEST_INTERFACE = 72,
	BIND_TAB = 174
	;
	
	public static void ShowBindforDraynorMyths(Player player) {
		if (isStage3() == true) {
			 refreshStages();
			DraynorMyth.setStage3(true);
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Myths of Draynor");
						player.packets().sendHideIComponent(1024, 5, true);
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"You have went in correct cave. Now you should find Ice Elemental. Once you kill him and achieve the book go speak with Meteora, only Meteora can read the book and understand it. Meteora was located in Lunar Isle.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			}
		else if (isStage2() == true) {
		 refreshStages();
		DraynorMyth.setStage2(true);
			QuestBind.CleanTexts(player);
			player.packets().sendHideIComponent(1024, 5, true);
			player.closeInterfaces();
			player.interfaces().sendBindingTab();
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Myths of Draynor");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"Meteora has heard some wierd noises and sounds coming from cave at Lunar Isle. I should get my fighting equipment and move in to cave and check out if something is wrong in there. Food will be needed.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
		}
		else if (isStage1() == true) {
			 refreshStages();
			DraynorMyth.setStage1(true);
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				player.interfaces().sendBindingTab();
				player.packets().sendHideIComponent(1024, 5, true);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Myths of Draynor");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"I should move straight to Lunar Isle, the Quest Rewarder should teleport me there. Maybe I should speak with him and find Meteora inside of the Lunar Isle.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			} else {
					QuestBind.forceCloseInterfaces(player);
					player.interfaces().sendBindingTab();
					player.sm("<col=ff0033>Quest System: You have binded a new quest: Myths of Draynor.");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
							"Myths of Draynor");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
							"Get into Draynor Village, find a witch called Aggie, speak with her for some information. Remember that you have to give her 3 cabbages before you get Aggie speak with you. The cabbages can be picked, or bought from the Grand Exchange.");
					player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
							"Quest points: "+player.questpoints+".");
			}
	}
	
	public static void refreshStages() {
		setStage1(false);
		setStage2(false);
		setStage3(false);
		setStage5(false);
		setStage6(false);
	}
/**
 * 
 * @return
 */
	public static boolean isStage1() {
		return stage1;
	}

/**
 * 
 * @return
 */
	public boolean hasRezizableScreen() {
		return resizableScreen;
	}

/**
 * 
 * @param stage1
 */
	public static void setStage1(boolean stage1) {
		DraynorMyth.stage1 = stage1;
	}



/**
 * 
 * @return
 */
	public static boolean isStage2() {
		return stage2;
	}
/**
 * 
 * @param stage2
 */
	public static void setStage2(boolean stage2) {
		DraynorMyth.stage2 = stage2;
	}
/**
 * 
 * @return
 */
	public static boolean isStage3() {
		return stage3;
	}
/**
 * 
 * @param stage3
 */
	public static void setStage3(boolean stage3) {
		DraynorMyth.stage3 = stage3;
	}
/**
 * 
 * @return
 */
	public boolean isStage4() {
		return stage4;
	}
/**
 * 
 * @param stage4
 */
	public void setStage4(boolean stage4) {
		DraynorMyth.stage4 = stage4;
	}
/**
 * 
 * @return
 */
	public boolean isStage6() {
		return stage6;
	}
/**
 * 
 * @param stage6
 */
	public static void setStage6(boolean stage6) {
		DraynorMyth.stage6 = stage6;
	}
/**
 * 
 * @return
 */
	public boolean isStage5() {
		return stage5;
	}
/**
 * 
 * @param stage5
 */
	public static void setStage5(boolean stage5) {
		DraynorMyth.stage5 = stage5;
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
