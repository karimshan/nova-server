package org.nova.kshan.content.pets;

import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public class BossPets {
	
	/**
	 * Gives the player a boss pet.
	 * @param p
	 * @param id
	 */
	public static boolean createBossPet(Player p, int id) {
		if(p.getPet() != null) {
			p.sm("You already have "+p.getPet().getName()+" following you.");
			return false;
		}
		p.setPet(new PetManager(id, p));
		return true;
	}

}
