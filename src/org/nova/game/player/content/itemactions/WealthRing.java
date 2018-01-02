package org.nova.game.player.content.itemactions;

import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 * @information Represents handling for Ring of wealth.
 * @since 13.4.2014
 */
public class WealthRing {

	private static WealthRing wealthRing;
	
	public int chance;
	/**
	 * Array holding the rare drops.
	 */
	public static final int[] WEALTH_DROPS = {1079,1712,1149,1129,11118,1731};
	/**
	 * Init wealth ring
	 */
	public static boolean addWealthRingEffect(Player player, NPC npc) {
		int RINGS[] = {2572,20653,20655,20657,20659};
		for (int rings : RINGS) {
		if (player.getEquipment().getRingId() == rings)
			WealthRing.getWealthRing().handleRingBrigthness(player, npc);
		return false;
		}
		return false;
	}
	
	public  void handleRingBrigthness(Player player, NPC npc) {
		switch (Misc.getRandom(5)) {
		case 0:
			return;
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			shineBright(player, npc);
			break;
			
		}
	}
	
	/**
	 * Ring of wealth chance and shining.
	 * @param player
	 */
	public void shineBright(Player player, NPC npc) {
		if (chance == -1 || chance == 100) {
			chance = 1;
		return;
		}
		//#FFFF00
		player.sendMessage("<col=FFFF00>Your ring of wealth shines more brightly!");
		chance++;
		if (chance > 10) {
			switch (Misc.getRandom(2)) {
			case 1:
			case 2:
				
			case 0:
				drop(player, npc);
				break;
			}
		}
		else if (chance > 20) {
			switch (Misc.getRandom(4)) {
			case 1:
			case 2:
				break;
			case 4:
			case 3:
			case 0:
				drop(player, npc);
				break;
		}
			
		}		
		else if (chance > 35) {
			switch (Misc.getRandom(4)) {
			case 1:
			case 2:
			case 0:
				drop(player, npc);
				break;
		}	
	}
	}
	/**
	 * The dropping.
	 * @param player
	 * @param npc
	 * @return
	 */
		public final void drop(Player player, NPC npc) {	
			final int size = npc.getSize();
			int itemId = WEALTH_DROPS[Misc.getRandom(5)];
			Game.addGroundItem(new Item(itemId), new Location(npc.getCoordFaceX(size), npc.getCoordFaceY(size), npc.getZ()), player, false, 180, true);
		}
		/**
		 * Gets ring of wealth singleton.
		 * @return
		 */
		public static WealthRing getWealthRing() {
			return wealthRing;
		}
	
}

