package org.nova.game.player.content.itemactions;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class SpecialSmithing {

	/**
	 *Creates a dragonfire shield.
	 */
	public static final void createDragonfireShield(final Player player) {
		if (!(player.getSkills().getLevel(Skills.SMITHING) >= 89)) {
			player.sendMessage("You must have smithing level of 90 to create a Dragonfire Shield.");
			return;
		}
		if (!player.getInventory().containsItem(1540, 1)) {
			player.sendMessage("You must have a anti-dragon shield with you.");
			return;
		}
		if (!player.getInventory().containsItem(2347, 1)) {
			player.sendMessage("You must have hammer in your inventory to create a Dragonfire Shield.");
			return;
		}
		WorldTasksManager.schedule(new WorldTask() {
			
			int loop;
		
		@Override
		public void run() {
		player.addStopDelay(8);
		switch (loop) {
		case 0:
			player.setNextAnimation(new Animation(898));
			break;
		case 1:
			player.getSkills().addXp(Skills.SMITHING, 2000);
			player.setNextAnimation(new Animation(898));
			break;
		case 3:
			player.setNextAnimation(new Animation(898));
			addDragonfireShield(player);
			player.getMatrixDialogues().startDialogue("DragonfireFinish");
			stop();
			break;
		}
			loop++;
			}
		}, 0, 1);
	}
	
	private static void addDragonfireShield(Player player) {
		player.getInventory().deleteItem(11286, 1);
		player.getInventory().deleteItem(1540, 1);
		player.getInventory().addItem(11283, 1);
	}
	
}
