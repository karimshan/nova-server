package org.nova.game.player.content;

import java.util.HashMap;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information This is a file for the PVP Statuettes.
 * @since 24.3.2014
 */
public class Statuettes {

    private static HashMap<Integer, Artefacts> artefactItem = new HashMap<Integer, Artefacts>();
    /**
     * 
     * @author Fuzen Seth
     * @information Data holder for artifacts.
     */
	public static enum Artefacts {
	    BROKEN_STATIUE_HEADDRESS(14892, 5000),
	    THIRD_AGE_CARAFE(14891, 10000),
	    BRONZED_RAGON_CLAW(14890, 20000),
	    ANCIENT_PSALTERY_BRIDGE(14889, 30000),
	    BANDOS_SCRIMSHAW(14888, 40000),
	    SARADOMIN_AMPHORA(14888, 50000),
	    SARADOMIN_CARVING(14886,75000),
	    ZAMORAK_MEDALLION(14885,100000),
	    ARMADYL_TOTEM(14884,150000),
	    GUTHIXIAN_BRAZIER(14883,200000),
	    RUBY_CHALICE(14882,250000),
	    BANDOS_STATUETTE(14881,300000),
	    SARADOMIN_STATUETTE(14880,400000),
	    ZAMORAK_STATUETTE(14879,500000),
	    ARMADYL_STATUETTE(14878,750000),
	    SEREN_STATUETTE(14877,1000000),
	    ANCIENT_STATIUETTE(14876, 5000000);
	    
	        private int itemId, coins;

	        public int getId() {
	            return itemId;
	        }

	        private Artefacts(int id, int coins) {
	            this.itemId = id;
	            this.coins = coins;
	        }

			public int getCoins() {
				return coins;
			}
	    }
	    /**
		   * Finds the artefacts.
		   * @param player, int itemId, int amount, @param item
		   */
		public static void findArtefacts(Player player) {
		  final Artefacts artefacts = artefactItem.get(artefactItem.size());
			  if (!player.getInventory().hasFreeSlots()) {
				player.sendMessage("You don't have enough space in your inventory to exchange artefacts.");
				  return;
			  }
			  if (!player.getInventory().containsItem(artefacts.getId(), 1)) {
				  player.sendMessage("You don't have any artefacts to exchange.");
				  return;
			  }
			  if (player.getSkills().getCombatLevelWithSummoning() <= 29) {
				  player.sendMessage("You must have combat level atleast of 30 to exchange artefacts.");
			  }
			  if (containsArtefacts(player, artefacts.getId()) && player.getSkills().getCombatLevelWithSummoning() >= 30) {
				  player.getInventory().deleteItem(artefacts.getId(), player.getInventory().getAmountOf(artefacts.getId()));
				  player.addStopDelay(3);
				  player.getInventory().addItem(995, artefacts.getCoins());
				  player.getMatrixDialogues().startDialogue("SimpleMessage", "You have succesfully exchanged your artefacts.");
			  }
		  }
		
		/**
		 * Does the player have artefacts?
		 * @param player
		 * @param itemId
		 * @return
		 */
		public static boolean containsArtefacts(Player player, int itemId) {
		  final Artefacts artefacts = artefactItem.get(itemId);
			return (player.getInventory().containsItem(artefacts.getId(), 1));
		}
}
