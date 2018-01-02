package org.nova.game.npc;

import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 * @information This file handles charm random dropping.
 * @since 13.4.2014
 */
public class Charms {
	
	/**
	 * Charm singleton.
	 */
	private static Charms charms = new Charms();
	
	/**
	 * Array hold charm ids.
	 */
	public static final int[] CHARMS = {12158,12159,12160,12161,12162,12162,12164,12165,12166,12167,12168};
	
	private int charm;
	
	/**
	 * Grabs the charm.
	 * @param player
	 * @return
	 */
	public int sendDrop(NPC npc) {
		switch (Misc.getRandom(2)) {
		/**
		 * The most common charm, gold charm.
		 */
		case 0:
			charm = 12158;
			break;
		case 1:
		case 2:
			charm = CHARMS[Misc.getRandom(9)];
			break;
		}
		return sendDrop(npc);
	}

	public static Charms getCharms() {
		return charms;
	}

	public int getCharm() {
		return charm;
	}

}
