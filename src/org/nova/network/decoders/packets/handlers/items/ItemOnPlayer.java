package org.nova.network.decoders.packets.handlers.items;

import org.nova.game.player.CoordsEvent;
import org.nova.game.player.Player;

/**
 * 
 * @author Karimshan Nawaz
 *
 */
public class ItemOnPlayer {
	
	/**
	 * 
	 * @param player
	 * @param usedOn
	 * @param itemId
	 */
	public static void process(final Player player, final Player usedOn, final int itemId) {
		player.setCoordsEvent(new CoordsEvent(usedOn, new Runnable() {
			public void run() {
				player.faceEntity(usedOn);
				if(!player.getRandomEvent().canUseItemOnPlayer(usedOn, itemId))
					return;
				if(usedOn.interfaces().containsScreenInter()) {
					player.sendMessage(usedOn.getDisplayName()+" is busy at the moment.");
					return;
				}
				switch (itemId) {
					case 962:// Christmas cracker
						if (player.getInventory().getFreeSlots() < 3
							|| usedOn.getInventory().getFreeSlots() < 3) {
							player.sendMessage((player.getInventory()
								.getFreeSlots() < 3 ? "You do"
									: "The other player does")
										+ " not have enough inventory space to open this cracker.");
							return;
						}
						player.getMatrixDialogues().startDialogue(
							"ChristmasCrackerD", usedOn, itemId);
						break;
					default:
						player.sendMessage("Nothing interesting happens.");
						break;
				}
			}
		}, usedOn.getSize()));
	}

}