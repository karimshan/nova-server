package org.nova.game.player.content.quests;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class Wanted {
	
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
		if (player.getWantedStage() == 6) {
			QuestBind.CleanTexts(player);
			player.packets().sendHideIComponent(1024, 5, true);
			player.closeInterfaces();
			 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Wanted!");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"Buy a beer to Thak inside bar. Even though he is already little a bit drunk I must do what he says.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
			QuestSystem.sendUpdate(player);
		}
		
		else if (player.getWantedStage() == 5) {
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				player.packets().sendHideIComponent(1024, 5, true);
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Wanted!");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"Now I have arrived to Keldagrim, I must find a dwarf which has a black armour. The dwarf's name is Thak and he should be located in one of the bars at Keldagrim.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
				QuestSystem.sendUpdate(player);
			}
		else if (player.getWantedStage() == 4) {
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				player.packets().sendHideIComponent(1024, 5, true);
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Wanted!");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"Find your way to the Keldagrim city, you can move to there by crossing tunnels. In some part there could be some friendly dwarfs which will help you pass a river, if it's needed. Once you're in Keldagrim talk with a dwarf in blue.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
				QuestSystem.sendUpdate(player);
			}
		else if (player.getWantedStage() == 3) {
				QuestBind.CleanTexts(player);
				player.closeInterfaces();
				player.packets().sendHideIComponent(1024, 5, true);
				 player.interfaces().sendTab(resizableScreen ? 93 : 207, QUEST_BIND);
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Wanted!");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"I have gotten a permission to enter Keldagrim. I will see a 'Keldagrim Entrance' -cave near Rolad, I should enter to Keldagrim fast as possible!");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
				QuestSystem.sendUpdate(player);
			}
		else if (player.getWantedStage() == 2) {
			QuestBind.CleanTexts(player);
			player.closeInterfaces();
			player.packets().sendHideIComponent(1024, 5, true);
			player.interfaces().sendBindingTab();
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
					"Wanted!");
			player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
					"You have given Rolad the chocolate cake, you can now enter to the Keldagrim from the cave entrance.");
			player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
					"Quest points: "+player.questpoints+".");
			player.sm("<col=ff0033>Quest System: Progress has been updated.");
			QuestSystem.sendUpdate(player);
		}
		else if (player.getWantedStage() == 1) {
				QuestBind.CleanTexts(player);
				player.packets().sendHideIComponent(1024, 5, true);
				player.closeInterfaces();
				player.interfaces().sendBindingTab();
						player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
						"Wanted!");
				player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
						"Rolad wants you to bring him a chocolate cake, you can buy this from the Grand Exchange for an example. Once you got the cake, come back and speak with Rolad again.");
				player.packets().sendIComponentText(QUEST_BIND, QUEST_POINTS,
						"Quest points: "+player.questpoints+".");
				player.sm("<col=ff0033>Quest System: Progress has been updated.");
				QuestSystem.sendUpdate(player);
			} else {
					QuestBind.forceCloseInterfaces(player);
					player.interfaces().sendBindingTab();
					player.packets().sendHideIComponent(1024, 5, true);
					player.sm("<col=ff0033>Quest System: You have binded a new quest: Wanted!");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TITLE,
							"Wanted!");
					player.packets().sendIComponentText(QUEST_BIND, MAIN_TEXT,
							"I can start this quest by speaking to a dwarf located in Keldagrim's entrance. This entrance is located in Relleka. Speak the dwarf for more instructions.");
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
	if (player.isCompletedWanted())
		return;
		player.getInventory().deleteItem(1917,1);
		player.getInventory().refresh();
		QuestBind.CleanTexts(player);
		player.questpoints = +4; 
		player.setCompletedwanted(true);
		if (!player.getInventory().hasFreeSlots()) {
			player.getBank().addItem(21773, 118, true);
			player.sendMessage("You didn't have enough space in your inventory. Quest reward was added to your bank account.");
			return;
		}
		player.getInventory().addItem(21773,118);
		player.sm("<col=ff0033>Quest System: Battle Quest: Wanted! has been completed.");
		player.packets().sendIComponentText(277, 3,
				"'Wanted!' Completed!");
		player.packets().sendIComponentText(277, 4,
				"Reward(s):");
		player.packets().sendIComponentText(277, 9,
				"New store unlocked");
		player.packets().sendIComponentText(277, 10,
				"Ability of item recolour.");
		player.packets().sendIComponentText(277, 11,
				"Ability of imbueing rings.");
		player.packets().sendIComponentText(277, 12,
				"Storm of Armadyl unlocked.");
		player.packets().sendIComponentText(277,13,
				"118 Armadyl runes.");
		player.packets().sendIComponentText(277, 14,
				"");
		player.packets().sendIComponentText(277, 6,
				"Quest Points: "+player.questpoints+"/350");
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
	Wanted.foundbook = foundbook;
}
}
