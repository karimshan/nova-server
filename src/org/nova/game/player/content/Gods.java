package org.nova.game.player.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the three gods in-game.
 * @since 23.3.2014
 */
public class Gods {
	
	private static Animation PRAYING_ANIMATION = new Animation(645);
	
	public static final int[] GOD_CAPES = {2412, 2413, 2414};
	/**
	 * Handles the object actions.
	 * @param player
	 * @param gameObject
	 * @return
	 */
	public static final boolean handleObjects(Player player, GlobalObject gameObject) {
	for (int i : GOD_CAPES)
		if (player.getInventory().containsItem(i, 1) || player.getBank().containsItem(i, 1)) {
			player.sendMessage("You have already a cape from the gods.");
		return false;
		}
	
		switch(gameObject.getId()) {
		case 2874:
			addCape(player, 2414);
			return true;
		case 2873:
			addCape(player, 2412);
			return true;
			
		case 2875:
			addCape(player, 2413);
			return true;
		}
		player.getMatrixDialogues().startDialogue("SimpleMessage", "You receive a mysterious cape.");
		return false;
	}
	/**
	 * Adds the cape to player.
	 * @param player
	 * @param itemId
	 */
	private static final void addCape(final Player player, final int itemId) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
			player.addStopDelay(2);
			switch(loop) {
			case 0:
				player.setNextAnimation(PRAYING_ANIMATION);
				break;
				
			case 2:
				player.getInventory().addItem(itemId, 1);
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You receive a cape from the gods.");
				stop();
				break;
				}
				loop++;
				}
			}, 0, 1);
	}
}
