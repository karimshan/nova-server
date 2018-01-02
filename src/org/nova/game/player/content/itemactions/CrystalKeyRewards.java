package org.nova.game.player.content.itemactions;

import org.nova.game.Game;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the crystal key rewards handling.
 * @since 11.4.2014
 */
public class CrystalKeyRewards {
	/**
	 * The random rewards.
	 */
	public static final int REWARD_ITEMS[] = {1712,11730,11732,1725,6585,3751,6737,6731,405,4587,1305,6889,6920,1333,1434,4087,1079,1127,11118,1149,1139};
	
	private static CrystalKeyRewards crystalKeyRewads = new CrystalKeyRewards();

	
	private boolean checkAll(Player player) {
		if (!player.getInventory().containsItem(989, 1))  {
			player.sendMessage("You must have crystal key in order to receive a reward.");
		player.getMatrixDialogues().startDialogue("SimpleMessage", "You don't have a crystal key to offer.");
		return false;
		} else {
			return true;
		}		
	}
	
	/**
	 * Sends the reward to player.
	 */
	public void sendReward(Player player) {
		final int selectedReward = REWARD_ITEMS[Misc.getRandom(20)];
		if (!checkAll(player))
			return;	
		for (NPC npc : Game.getNPCs()) {
				if (npc.getId()  == 456) {
					npc.setNextForceTalk(new ForceTalk("Thanks for the Crystal key, "+player.getDisplayName()+"!"));
					npc.setNextGraphics(new Graphics(1993));
				}
			}
		player.setNextAnimation(new Animation(487));
			player.addStopDelay(3);
		player.getInventory().deleteItem(989, 1);
		player.getInventory().addItem(selectedReward, 1);
		player.getMatrixDialogues().startDialogue("ItemMessage", "Congratulations, you have received a item!", selectedReward);
	}
	
	
	public static CrystalKeyRewards getCrystalKeyRewards() {
		return crystalKeyRewads;
	}


	
}
