package org.nova.game.player.starter;


import org.nova.game.Game;
import org.nova.game.player.Player;

/**
 * This class handles the giving of a starter kit.
 * 
 * @author Emperial
 * 
 */
public class Starter {

	public static final int MAX_STARTER_COUNT = 10;

	public static void appendStarter(Player player) {
		String ip = player.getSession().getIP();
		int count = StarterMap.getSingleton().getCount(ip);
		player.starter = 1;
		if (count >= MAX_STARTER_COUNT) {
			player.sm("It is apparent you have already claimed a starter on this IP!");
			return;
		}

		player.interfaces().sendInterface(1066);
		player.packets().sendIComponentText(1066, 18, "Welcome to Nova 666!");
		player.packets().sendIComponentText(1066, 19, "Hello " + player.getDisplayName() + ", and welcome to Nova! If you require help, you can use the ::yell command, or the ::help guide. If you want to know what you can do here, use ::commands!");
		player.packets().sendIComponentText(1066, 25, "Okay, great!");
		player.packets().sendIComponentText(1066, 33, "View guides");
		player.softreset = true; 

		player.hints().removeUnsavedHintIcon();
		player.getMusicsManager().reset();
		player.getCombatDefinitions().setAutoRetaliate(true);
		player.getCombatDefinitions().refreshAutoRelatie();
		StarterMap.getSingleton().addIP(ip);
		Game.sendMessage("<col=de4ac0>We welcome " + player.getDisplayName() + " to Nova! Anyone willing to help " + player.getDisplayName() + "?");
	}

}
