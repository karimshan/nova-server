package org.nova.game.player.content.quests;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @since 30.12.2013
 * @information We handle the miscellanious stuff for quests.
 */
public class QuestSystem {
	/**
	 * Sends notification to player.
	 */
	public static final void sendUpdate(final Player player) {
		player.packets().sendIComponentText(1009, 0, "Quest progress updated!");
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				switch (loop) {
				case 0:
					
					break;
				case 1:
					player.packets().sendIComponentText(1009, 0, "Quest progress updated!");
					player.interfaces().sendTab(player.interfaces().isFullScreen() ? 10 : 19, 1009);
					player.packets().sendIComponentText(1009, 0, "Quest progress updated!");
					break;
				case 13:
			player.packets()
					.closeInterface(
							player.interfaces().isFullScreen() ? 10
									: 19);
			player.packets().sendWindowsPane(
					player.interfaces().isFullScreen() ? 746
							: 548, 0);
					stop();
					break;
				}
				loop++;
				}
			}, 0, 1);
	}

}
