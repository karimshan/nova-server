package org.nova.game.player.content.quests;

import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class QuestList {
	
public static int LIST = 72, QUESTBOARD = 1083;

public static void showComponent(Player player) {
	for (int i = 0; i < 567; i++) {
		player.packets().sendIComponentText(QUESTBOARD, i,  "child: " + i);
		
	}
}
	public static void openQuests(Player player) {
		sendData(player);
		player.interfaces().sendInterface(QUESTBOARD);
	}
	

	
public static void handleInterfaceButtons(Player player, int componentId) {
	if (player.isInTutorial()) {
		player.sendMessage("Cannot use noticeboard during the introduction.");
		return;
	}
	switch (componentId) {
	case 387:
		sendHoarfrostHollow(player);
		break;
	case 74:
		sendHoarfrostDepths(player);
		break;
	case 69:
		sendUzerRuins(player);
		break;
		
		case 281:
		if (!player.isCompletedRuinsofUzer()) {
		player.closeInterfaces();
		UzerRuins.ShowBind(player);
		} else {
			player.sm("You cannot bind this quest, it's already completed.");
		}
		break;
	case 272:
		if (!player.isCompletedHoarfrostDepths()) {
		player.closeInterfaces();
		HoarfrostDepths.ShowBindforHoarfrostDepths(player);
		} else {
			player.sm("You cannot bind this quest, it's already completed.");
		}
		break;
	case 263:
		if (!player.isCompletedHoarfrost()) {
		player.closeInterfaces();
		QuestBind.ShowBindforHoarfrost(player);
		} else {
			player.sm("You cannot bind this quest, it's already completed.");
		}
		break;
	}
	}
private static void sendHoarfrostHollow(Player player) {
	player.packets().sendIComponentText(QUESTBOARD, 85,
			"Hoarfrost Hollow");
	player.packets().sendIComponentText(QUESTBOARD, 87,
			"There's an mysterious Ice Demon. Ned is in need of your help you must travel all over Draynor Village and Port Sarim.");
	player.packets().sendIComponentText(QUESTBOARD, 89,
			"This is an easy Battle Quest.");
	player.packets().sendIComponentText(QUESTBOARD, 265,
			"Bind Quest");
	player.packets().sendHideIComponent(QUESTBOARD,265, true);
player.packets().sendIComponentText(QUESTBOARD, 265,
"Bind Quest");

}
private static void sendHoarfrostDepths(Player player) {
	player.packets().sendIComponentText(QUESTBOARD, 85,
			"Hoarfrost Depths");
	player.packets().sendIComponentText(QUESTBOARD, 87,
			"You must travel all over Lunar island, speak with Meteora and she will be telling you what to do.");
	player.packets().sendIComponentText(QUESTBOARD, 89,
			"Difficulty of this battle quest is: medium.");
	player.packets().sendIComponentText(QUESTBOARD, 274,
			"Bind Quest");
	player.packets().sendHideIComponent(QUESTBOARD,274, true);
player.packets().sendIComponentText(QUESTBOARD, 274,
"Bind Quest");
}
private static void sendUzerRuins(Player player) {
	player.packets().sendIComponentText(QUESTBOARD, 85,
			"Ruins of Uzer");
	player.packets().sendIComponentText(QUESTBOARD, 87,
			"This is grand master battle quest, you must travell all over al-kharid, varrock. The boss battle is very hard also. Remember to be very well prepared for this battle.");
	player.packets().sendIComponentText(QUESTBOARD, 89,
			"Difficulty of this battle quest is hard.");
	player.packets().sendIComponentText(QUESTBOARD, 233,
			"Bind Quest");
	player.packets().sendHideIComponent(QUESTBOARD,233, true);
player.packets().sendIComponentText(QUESTBOARD, 233,
"Bind Quest");
}


public static void handleButtons(Player player, int componentId) {
	if (player.isInTutorial()) {
		player.sendMessage("Cannot use noticeboard during the introduction.");
		return;
	}
	/*
	 * Button handling
	 */
	switch (componentId) {
	case 66: 
		if (player.isCompletedRuinsofUzer()) {
			player.sm("You have already completed this battle quest.");
			player.closeInterfaces();
		} else {
		UzerRuins.ShowBind(player);
		}
		break;
	case 68: 
		if (player.isCompletedHoarfrost()) {
			player.sm("You have already completed this battle quest.");
			player.closeInterfaces();
		} else {
		QuestBind.ShowBindforHoarfrost(player);
		}
		break;
	case 65:
		if (player.isCamelotKnight()) {
			player.sm("You have already completed this battle quest.");
			player.closeInterfaces();
		} else {
		
		CamelotKnight.ShowBind(player);
		player.setCamelotKnight(false);
		}
		break;
	case 67: 
		if (player.isCompletedHoarfrostDepths()) {
			player.sm("You have completed already this battle quest.");
			player.closeInterfaces();
		}
		else if (player.isCompletedHoarfrost()) {
			HoarfrostDepths.ShowBindforHoarfrostDepths(player);
			player.closeInterfaces();
		} else {
		player.closeInterfaces();
		player.sm("You have to complete Hoarfrost Hollow to access Season 2: Hoarfrost Depths.");
		}
		break;
case 73: 
		if (player.isCompletedWanted()) {
			player.sm("You have completed already this battle quest.");
			player.closeInterfaces();
		} else {
	player.closeInterfaces();
	Wanted.ShowBind(player);
		}
		break;
	case 64: 
		if (player.isCompletedThrone()) {
			player.sm("You have completed already this battle quest.");
			player.closeInterfaces();
		}else {
			MiscellaniaThrone.ShowBind(player);
			player.closeInterfaces();
		}
		break;
		case 72: 
			if (player.isCompletedRecipeforDisaster()) {
				player.sendMessage("You have completed already this battle quest.");
				player.closeInterfaces();
			} else {
				RecipeForDisaster.traceStages(player);
				player.closeInterfaces();
			}
			break;
	}
}
public static void refreshMenuandButtons(Player player) {
	player.packets().sendHideIComponent(QUESTBOARD,85, true);
	player.packets().sendHideIComponent(QUESTBOARD,87, true);
	player.packets().sendHideIComponent(QUESTBOARD,89, true);
	player.packets().sendHideIComponent(QUESTBOARD,233, true);
	
}
/**
 * 
 * @param player
 */
	public static void sendList(Player player) {
		if (player.isInTutorial()) {
			player.sendMessage("Cannot use noticeboard during the introduction.");
			return;
		}
		player.packets().sendIComponentText(LIST, 55,
				"Nova Battle Quests");
		player.packets().sendIComponentText(LIST, 37, "<col=ff0033>Recipe for Disaster");
		player.packets().sendIComponentText(LIST, 31,
						"<col=ff0033>Hoarfrost Hollow");
		player.packets().sendIComponentText(LIST, 35,
				"<col=ff0033>Throne of Miscellania");
player.packets().sendIComponentText(LIST, 36,
				"<col=ff0033>Wanted!");
		player.packets().sendIComponentText(LIST, 32,
				"<col=ff0033>Hoarfrost Depths");
		player.packets().sendIComponentText(LIST, 33,
				"<col=ff0033>Ruins of Uzer");
		player.packets().sendIComponentText(LIST, 34,
				"<col=ff0033>Knights of Camelot");
		if (player.isCamelotKnight()) {
			player.packets().sendIComponentText(LIST, 34,
					"<col=008000>Knights of Camelot");
		}
		if (player.isCompletedRuinsofUzer()) {
			player.packets().sendIComponentText(LIST, 33,
					"<col=008000>Ruins of Uzer");
		}
		if (player.isCompletedThrone()) {
			player.packets().sendIComponentText(LIST, 35,
					"<col=008000>Throne of Miscellania");
		}
		if (player.isCompletedHoarfrost()) {
			player.packets().sendIComponentText(LIST, 31,
					"<col=008000>Hoarfrost Hollow");
		}
		if (player.isCompletedHoarfrostDepths()) {
			player.packets().sendIComponentText(LIST, 32,
					"<col=008000>Hoarfrost Depths");
		}
		if (player.isCompletedThrone()) {
			player.packets().sendIComponentText(LIST, 35,
					"<col=008000>Throne of Miscellania");
		}
	if (player.isCompletedWanted()) {
			player.packets().sendIComponentText(LIST, 36,
					"<col=008000>Wanted!");
		}
	if (player.isCompletedRecipeforDisaster())
		player.packets().sendIComponentText(LIST, 37, "<col=008000>Recipe for Disaster");
		player.packets().sendIComponentText(LIST, 38,
				"None");
		player.packets().sendIComponentText(LIST, 39,
				"None");
		player.packets().sendIComponentText(LIST, 40,
				"None");
		player.interfaces().sendInterface(LIST);
	}
	public static void sendHidedComponents(Player player) {
		player.packets().sendIComponentText(QUESTBOARD, 397, "Status");
		player.packets().sendIComponentText(QUESTBOARD, 256, "Rewards");
		player.packets().sendIComponentText(QUESTBOARD, 246, "Quests");
		player.packets().sendHideIComponent(QUESTBOARD,246, true);
		player.packets().sendHideIComponent(QUESTBOARD,256, true);
		player.packets().sendHideIComponent(QUESTBOARD,121, true);
		player.packets().sendHideIComponent(QUESTBOARD,397, true);
		player.packets().sendHideIComponent(QUESTBOARD,457, true);
		player.packets().sendHideIComponent(QUESTBOARD,457, true);
		player.packets().sendHideIComponent(QUESTBOARD,457, true);
		player.packets().sendHideIComponent(QUESTBOARD,457, true);
		player.packets().sendHideIComponent(QUESTBOARD,195, true);
		player.packets().sendHideIComponent(QUESTBOARD,133, true);
		player.packets().sendHideIComponent(QUESTBOARD,129, true);
		player.packets().sendHideIComponent(QUESTBOARD,125, true);
		player.packets().sendHideIComponent(QUESTBOARD,125, true);
		player.packets().sendHideIComponent(QUESTBOARD,137, true);
		player.packets().sendHideIComponent(QUESTBOARD,117, true);
		player.packets().sendHideIComponent(QUESTBOARD,113, true);
		player.packets().sendHideIComponent(QUESTBOARD,109, true);
		player.packets().sendHideIComponent(QUESTBOARD,435, true);
		player.packets().sendHideIComponent(QUESTBOARD,446, true);
		player.packets().sendHideIComponent(QUESTBOARD,457, true);
	}
	public static void sendData(Player player) {
		sendHidedComponents(player);
		player.packets().sendIComponentText(QUESTBOARD, 397, "Status");
		player.packets().sendIComponentText(QUESTBOARD, 256, "Rewards");
		player.packets().sendIComponentText(QUESTBOARD, 246, "Quests");
		player.packets().sendIComponentText(QUESTBOARD, 85, "Battle Quests");
		player.packets().sendIComponentText(QUESTBOARD, 87, "From battle quests you can receive untradeabe items, or items that are boosting your character up.");
		player.packets().sendIComponentText(QUESTBOARD, 89, "Select an Battle Quest to begin.");
			player.packets().sendIComponentText(QUESTBOARD, 152, "My Quest Points:");
		player.packets().sendIComponentText(QUESTBOARD, 152, "My Quest Points:");
		player.packets().sendIComponentText(QUESTBOARD, 201, " "+player.questpoints+".");
		player.packets().sendIComponentText(QUESTBOARD, 195, "Nova Battle Quests");
		player.packets().sendIComponentText(QUESTBOARD, 136, "Hoarfrost Hollow");
		player.packets().sendIComponentText(QUESTBOARD, 132, "Hoarfrost Depths");
		player.packets().sendIComponentText(QUESTBOARD, 128, "Ruins of Uzer");
		player.packets().sendIComponentText(QUESTBOARD, 124, "None");
		player.packets().sendIComponentText(QUESTBOARD, 120, "None");
		player.packets().sendIComponentText(QUESTBOARD, 116, "None");
		player.packets().sendIComponentText(QUESTBOARD, 112, "None");
		player.packets().sendIComponentText(QUESTBOARD, 108, "None");
		player.packets().sendIComponentText(QUESTBOARD, 434, "None");
		player.packets().sendIComponentText(QUESTBOARD, 445, "None");
		player.packets().sendIComponentText(QUESTBOARD, 456, "None");
	
	}
}
