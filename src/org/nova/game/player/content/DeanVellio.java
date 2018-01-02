package org.nova.game.player.content;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @inforamation This file represents an experienced traveller (NPC) that locates perfect training locations.
 */
public class DeanVellio {
	/**
	 * Holds the Vellio's NPC id.
	 */
	public static final int VELLIO = 14012;
	
	/**
	 * Dean vellio's singleton.
	 */
	private static DeanVellio deanVellio = new DeanVellio();

	public void locate(Player player) {
		player.interfaces().closeChatBoxInterface();
	if (player.getSkills().getCombatLevelWithSummoning() >= 29) 
		player.interfaces().closeChatBoxInterface();
		player.getMatrixDialogues().startDialogue("VellioLow", VELLIO);
	 if (player.getSkills().getCombatLevelWithSummoning() >= 69) 
		player.getMatrixDialogues().startDialogue("VellioMid", VELLIO);
	if (player.getSkills().getCombatLevelWithSummoning() >= 99) 
		player.getMatrixDialogues().startDialogue("VellioHard", VELLIO);
}
	/**
	 * 
	 * @param player
	 */
	public final void vellioTeleport(final Player player, final Location tile) {
		player.interfaces().closeChatBoxInterface();
		player.setNextGraphics(new Graphics(2128));
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
			switch (loop) {
			case 0:
				player.addStopDelay(4);
		for (NPC n : Game.getNPCs()) {
			if (n.getId() == VELLIO)
				n.setNextForceTalk(new ForceTalk("Let's begin the adventure, "+player.getDisplayName()+"!"));
		}
				/**
				 * Next we send black fading screen.
				 */
			player.setDirection(2);
			player.setNextGraphics(new Graphics(2128));
				player.packets().sendHideIComponent(241, 5, true);
				player.getMatrixDialogues().startDialogue("ItemMessage",
						"You feel the ancient power coming in to your body...", 4675);
				break;
			case 3:
				player.setLocation(tile);
				break;
			case 4:
				player.closeInterfaces();
				stop();
				break;
			}
				loop++;
				}
			}, 0, 1);
	}
	/**
	 * Gets dean vellio @instance
	 * @return
	 */
	public static DeanVellio getDeanVellio() {
		return deanVellio;
	}

}
