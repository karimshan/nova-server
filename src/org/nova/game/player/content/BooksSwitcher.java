package org.nova.game.player.content;

import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 * 
 */
public class BooksSwitcher {
/**
 * 
 * @param player
 */
	public static void checkforAncients(Player player) {
	if (player.questpoints <= 13) {
		player.addStopDelay(2);
		player.setNextAnimation(new Animation(9568));
		/*
		 * Add gfx and some sexy animation
		 * here also make npc do animation
		 */
		player.getCombatDefinitions().setSpellBook(1);
		player.interfaces().closeChatBoxInterface();
		} else {
			player.sm("You must have atleast 13 quest points to meet the power of Ancient magic.");
			player.interfaces().closeChatBoxInterface();
		}
	}
	/**
	 * 
	 * @param player
	 */
	public static void setRegularSpells(Player player) {
		player.sm("You haven taken back the power of regular magic spells.");
		player.getCombatDefinitions().setSpellBook(0);
	player.interfaces().closeChatBoxInterface();
	}
	
	/**
	 * 
	 * @param player
	 */
	
	public static void checkforLunars(Player player) {
		if (player.questpoints <= 20) {
			/*
			 * Add gfx and some sexy animation
			 * here also make npc do animation
			 */
			player.addStopDelay(2);
			player.setNextAnimation(new Animation(9568));
			player.sm("You feel holy lunar magic coming in to your body.");
			player.getCombatDefinitions().setSpellBook(2);
			player.interfaces().closeChatBoxInterface();
		} else {
			player.interfaces().closeChatBoxInterface();
			player.sm("You will need atleast 20 quest points to use Lunar magic.");
		}
	}
	/**
	 * 
	 * @param player
	 */
	public static void setRegularPrayers(Player player) {
		player.getPrayer().setPrayerBook(true);
		player.addStopDelay(2);
		player.setNextAnimation(new Animation(9568));
		player.interfaces().closeChatBoxInterface();
	}
	/**
	 * 
	 * @param player
	 */
	public static void checkforCursePrayers(Player player) {
		if (player.questpoints <= 36) {
			player.addStopDelay(2);
			player.setNextAnimation(new Animation(9568));
			player.getPrayer().setPrayerBook(true);
		} else {
			player.interfaces().closeChatBoxInterface();
			player.sm("You will need atleast 36 quest points to use power of Curse.");
		}
	}
}
