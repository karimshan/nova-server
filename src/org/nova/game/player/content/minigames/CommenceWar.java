package org.nova.game.player.content.minigames;

import org.nova.game.player.Player;

/**
 * Commence the war between 2 clans.
 * @author Own4g3
 *
 */
public class CommenceWar {

	/**
	 * Constructs a new instance.
	 * 
	 * @param player The player who's challenging.
	 * @param other The player who's opponent.
	 * @param war The war.
	 */
	public CommenceWar(Player player, Player other, War war) {
		sendInterface(player);
		sendInterface(other);
	}

	/**
	 * Sends the setup screen.
	 * @param player The player.
	 */
	public void sendInterface(final Player player) {
		Player other = (Player) player.getTemporaryAttributtes().get("challenger");
		player.packets().sendIComponentText(791, 14, "Clan Wars Options: Challenging " + other.getDisplayName());
		player.interfaces().sendInterface(791);
		player.interfaces().sendInventoryInterface(792);
		player.packets().sendUnlockIComponentOptionSlots(791, 141, 0, 63, 0);
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				end(player);
			}
		});
	}


	private void end(Player player) {
		Player other = (Player) player.getTemporaryAttributtes().get("challenger");
		if (other != null) {
			other.getTemporaryAttributtes().remove("challenger");
			other.setCloseInterfacesEvent(null);
			other.closeInterfaces();
		}
		player.getTemporaryAttributtes().remove("challenger");
		player.setCloseInterfacesEvent(null);
		player.closeInterfaces();
	}

}
