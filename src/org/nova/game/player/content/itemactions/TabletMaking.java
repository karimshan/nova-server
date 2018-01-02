package org.nova.game.player.content.itemactions;

import org.nova.game.player.Player;
import org.nova.game.player.Skills;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class TabletMaking {

	/**
	 * Tablet's data.
	 * @author JazzyYaYaYa
	 *
	 */
	private enum Tablets {
		VARROCK(1,70,12,15,233), 
		FALADOR(2,70,15,12,233),
		CAMELOT(3,70,15,23,3),
		LUMBRIDGE(4,50,15,2,33);
		
		private int id;
		
		private static int requirement;
		
		private static int itemrequirement;
		
		private static int amount;
	
		private int buttonId;
		
		private Tablets(int id, int requirement, int amount, int buttonId, int itemrequirement)  {
			this.id = id;
		}	
		
	}
	
	public static void makeTablets(Player player) {
	if (player.getSkills().getLevel(Skills.CONSTRUCTION) >= Tablets.requirement) {
	player.sendMessage("You cannot create this teleportation tablet. You must have construction level of "+Tablets.requirement+".");
		return;
	}
	if (player.getInventory().containsItem(Tablets.itemrequirement, Tablets.amount)) {
		player.sendMessage("You don't have enough "+Tablets.itemrequirement+" to create this teleportation tablet.");
		return;
	}
	//TODO Caller
	}
	}
