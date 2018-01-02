package org.nova.game.player.content.quests;

import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.content.TeleportManager;
import org.nova.utility.ShopsHandler;

/**
 * 
 * @author JazzyYaYaYa | Nexon  | Fuzen Seth
 *
 */
public class QuestNPCActions {
	
	public static void TeleportLunar(Player player) {
		player.interfaces().closeChatBoxInterface();
		TeleportManager.SpiritTeleport(player, 0, 0, new Location(2085, 3914, 0));
	}
	public static void openStore(Player player) {
		player.interfaces().closeChatBoxInterface();
	ShopsHandler.openShop(player, 100);
	}
	
	public static void buyKorasi(Player player) {
	if (player.getInventory().containsItem(995, 500000)) {
		player.interfaces().closeChatBoxInterface();
		player.getInventory().deleteItem(995, 500000);
		player.getInventory().addItem(19784, 1);
		player.getInventory().refresh();
		player.sm("Payments: You have bought an Korasi' s sword for 500,000 coins.");
		player.getMatrixDialogues().startDialogue(
		"SimpleMessage", "You have bought Korasi's sword for 500,000 coins.");
	} else {
		player.interfaces().closeChatBoxInterface();
		player.sm("You don't have enough money to buy Korasi's Sword.");
	}
	}
	public static void switchModerns(Player player) {
		player.addStopDelay(2);
		player.setNextAnimation(new Animation(9568));
		player.sm("You have switched your spellbook.");
		player.getCombatDefinitions().setSpellBook(0);
		player.interfaces().closeChatBoxInterface();
	}
	public static void swichAncients(Player player) {
		player.addStopDelay(2);
		player.setNextAnimation(new Animation(9568));
		player.sm("You have switched your spellbook.");
		player.getCombatDefinitions().setSpellBook(1);
		player.setNextGraphics(new Graphics(2593));
		player.interfaces().closeChatBoxInterface();
	}
	public static void switchLunars(Player player) {
		player.addStopDelay(2);
		player.setNextAnimation(new Animation(9568));
		player.sm("You have switched your spellbook.");
		player.getCombatDefinitions().setSpellBook(2);
		player.setNextGraphics(new Graphics(2593));
		player.interfaces().closeChatBoxInterface();
	}
	public static void switchCurses(Player player) {
		if (!player.getPrayer().isAncientCurses()) {
			player.addStopDelay(2);
			player.setNextAnimation(new Animation(9568));
			player.setNextGraphics(new Graphics(2593));
			player.sm("You have switched succesfully your Prayers book.");
			player.interfaces().closeChatBoxInterface();
			player.getPrayer().setPrayerBook(true);
		} else {
			player.sm("You have switched your Prayers book.");
			player.interfaces().closeChatBoxInterface();
			player.getPrayer().setPrayerBook(false);
		}
	}

}
