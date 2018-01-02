package org.nova.game.player.content.itemactions;

import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information PvP artifacts exchanging system.
 * @since 28.3.2014
 */
public class Artifacts {
    /**
     * A int holding the artifact item ids.
     */
	public static final int[] ARTIFACTS = {14892,14891,14890,14889,14888,14886,14885,14884,14883,14882,14881,14880,14879,14878,14877,14876};
	
	/**
	 * 
	 * @author Fuzen Seth
	 * @information Holds data about the artifacts.
	 * @since 29.3.2014
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
		        static Artefacts artifacts;
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
				
				public static Artefacts getArtefacts() {
					return artifacts;
				}
		    }
	 
	 public static final void exchangeArtifacts(Player player) {
		 for (Artefacts artefact : Artefacts.values()) {
			 int amount = player.getInventory().getAmountOf(artefact.getId());
			 final int calculatedAmount = amount * artefact.getCoins();
			 if (player.getInventory().containsItem(artefact.getId(), amount)) {
		 player.getInventory().deleteItem(artefact.getId(), amount);
		 player.getInventory().addItem(995, calculatedAmount);
		 player.getMatrixDialogues().startDialogue("SimpleNPCMessage",6537, "Thank you! I have given you "+calculatedAmount+" gold coins.");
		 return;
			 } else {
				 player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 6537, "You don't have any statuettes to exchange.");
			 }
		 
	 }
}
}

