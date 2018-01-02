package org.nova.game.player.content;

import org.nova.game.player.Player;
import org.nova.game.player.content.quests.QuestNPCActions;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class SpellSelection {

	public static final int MENU = 1127;
	
	
	/**
	 * Sends the spellbook's menu.
	 * @param player
	 */
	public static void sendMenu(Player player) {
		player.interfaces().closeChatBoxInterface();
	refreshInterface(player);
	player.interfaces().sendInterface(MENU);
	}
	/**
	 * Handles the interface buttons.
	 * @param player
	 * @param componentId
	 */
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 25:  //Modern
			QuestNPCActions.switchModerns(player);
			player.closeInterfaces();
			break;
		case 38: //Ancient
			if (!player.isCompletedHoarfrost()) {
				player.closeInterfaces();
				player.sendMessage("You must have completed 'Hoarfrost Hollow' -battle quest to use this spellbook.");
				return;
			}
			player.closeInterfaces();
			QuestNPCActions.swichAncients(player);
			break;
		case 51: //Lunar
			if (!player.isCompletedThrone()) {
				player.closeInterfaces();
				player.sendMessage("You must have completed 'Throne of Miscellania' -quest to use this spellbook.");
				return;
			}
			player.closeInterfaces();
			QuestNPCActions.switchLunars(player);
			break;
		}
	}
	/**
	 * Refresh (interface)
	 * @param player
	 */
	public static void refreshInterface(Player player) {
		player.packets().sendIComponentText(MENU, 18, "Select a Spell");
		player.packets().sendIComponentText(MENU, 17, "Remember that some spellbooks requires a quest to be completed.");
		player.packets().sendIComponentText(MENU, 12, "Modern Magic");
		player.packets().sendIComponentText(MENU, 13, "Ancient Magic");
		player.packets().sendIComponentText(MENU, 14, "Lunar Magic");
	}
	
}
