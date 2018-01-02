package org.nova.game.player.controlers;

import org.nova.game.map.GlobalObject;
import org.nova.game.player.Player;
/**
 * 
 * @author Fuzen Seth
 * Represents a handler for RuneScape's 2011 new tutorial.
 */
public class Tutorial extends Controller {
	
	
	/**
	 * Adds the starter to player.
	 */
	public static final void addStarter(Player player) {
	//Melee
	player.getInventory().addItem(1323, 1);
	player.getInventory().addItem(1067, 1);
	player.getInventory().addItem(1115, 1);
	player.getInventory().addItem(1153, 1);
	player.getInventory().addItem(1191, 1);
	player.getInventory().addItem(11118, 1);
	//Ranged
	player.getInventory().addItem(841, 1);
	player.getInventory().addItem(884, 500);
	//Magic
	player.getInventory().addItem(1381, 1);
	player.getInventory().addItem(558, 500);
	
	//Misc
	player.getInventory().addItem(7947, 100);
	player.getInventory().addItem(2441, 100);
	player.getInventory().addItem(2437, 100);
	player.getInventory().addItem(6686, 100);
	player.getInventory().addItem(3025, 100);
	
	player.getInventory().addItem(1712, 1);
	player.getInventory().addItem(995, 500000);
	player.getInventory().addItem(405, 1);
	player.getInventory().addItem(2552, 1);
	player.getInventory().addItem(8007, 150);
	player.getInventory().addItem(8008, 150);
	player.getInventory().addItem(8009, 150);
	player.getInventory().addItem(8010, 150);
	player.getInventory().addItem(8011, 150);
	player.getInventory().addItem(22340, 1);
	player.getAppearance().generateAppearanceData();
	}

	@Override
	public void start() {
		player.interfaces().sendOverlay(1024, false);
	}
	
	@Override
	public boolean processObjectClick1(GlobalObject object) {
		return false;
	}
	
	
	
	public void refreshStages() {
	
	}
	
}
