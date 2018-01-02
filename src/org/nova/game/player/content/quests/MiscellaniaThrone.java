package org.nova.game.player.content.quests;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class MiscellaniaThrone {
	
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
		if (player.getMiscellaniaThrone() == 4) {
			QuestBind.CleanTexts(player);
			player.closeInterfaces();
			player.packets().sendHideIComponent(1024, 5, true);
			 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Throne of Miscellania");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"All I need to do now is only to give king Percival the sir Bolren's contract. Return to Etceteria.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
		}
	else if (player.getMiscellaniaThrone() == 3) {
				QuestBind.CleanTexts(player);
				player.packets().sendHideIComponent(1024, 5, true);
				player.closeInterfaces();
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Throne of Miscellania");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"King Bolren sent me to ask king Percival what he wants to be in peace. I should go back to Etceteria and ask king Percival himself what he wants.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			}
		else if (player.getMiscellaniaThrone() == 2) {
			QuestBind.CleanTexts(player);
			player.closeInterfaces();
			player.packets().sendHideIComponent(1024, 5, true);
			player.interfaces().sendBindingTab();
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Throne of Miscellania");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"I have found out that the king in Etceteria is King percival. Now I got the required information. I should return back to Miscellania and tell king Bolren what I know.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
		}
		else if (player.getMiscellaniaThrone() == 1) {
			player.setHoarfrostStage(1);
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				player.packets().sendHideIComponent(1024, 5, true);
				player.interfaces().sendBindingTab();
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Throne of Miscellania");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"King Bolren told me to go find out whos the king at Etceteria. Once I have found the king I should speak with him.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
			} else {
					QuestBind.forceCloseInterfaces(player);
					player.interfaces().sendBindingTab();
					player.packets().sendHideIComponent(1024, 5, true);
					player.sm("<col=ff0033>Quest System: You have binded a new quest: Throne of Miscellania.");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
							"Throne of Miscellania");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
							"To start this quest, I must go to Miscellania and speak with King Bolren, he will be telling me what to do. Bolren is located inside Miscellania castle.");
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
	if (player.isCompletedThrone()) {
		return;
	}
	player.setCompletedThrone(true);
	player.getInventory().addItem(995, 832000);
		player.getInventory().refresh();
		QuestBind.CleanTexts(player);
		player.questpoints = +7; 
		player.setCompletedHoarfrostDepths(); 
		player.getInventory().addItem(995, 200000);
		player.sm("<col=ff0033>Quest System: Quest Throne of Miscellania has been completed.");
		player.packets().sendIComponentText(277, 3,
				"Throne of Miscellania Completd!");
		player.packets().sendIComponentText(277, 4,
				"Reward(s):");
		player.packets().sendIComponentText(277, 9,
				"New store unlocked");
		player.packets().sendIComponentText(277, 10,
				"Ability to switch item colours at Miscellania.");
		player.packets().sendIComponentText(277, 11,
				"Received 832,000 coins.");
		player.packets().sendIComponentText(277, 12,
				"Ability to use Dwarfs multicannon.");
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
