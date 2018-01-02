package org.nova.game.player.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
/**
 * 
 * @author Fuzen Seth
 * @information This is used to handle resource dungeons over the game.
 * @since 16.3.2014
 */
public class ResourceDungeons {
	
	/**
	 * Handles the resource dungeons.
	 */
	public static final boolean handleDungeons(Player player, GlobalObject gameObject) {
		if (!(player.getSkills().getLevel(Skills.DUNGEONEERING) >= checkDungeoneering(player, gameObject))) {
			player.sendMessage("You need a Dungeoneering level of "+checkDungeoneering(player, gameObject)+" to enter "+gameObject.defs().name.toLowerCase()+".");
			return false;
		}
		switch (gameObject.getId()) {
		case 52853:
			teleport(player, new Location(3200,3200,0));
			return true;
		}
		return false;
	}
	
	public static int checkDungeoneering(Player player, GlobalObject object) {
		if (object.getId() == 52853)
		return 5;
		else
		return 0;
	}
	
	/**
	 * Teleports players to a resource dungeon.
	 */
	public static final void teleport(final Player player, final Location destination) {
	
			WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {		
				if (loop == 0) {
                   player.setNextAnimation(new Animation(13652));
				player.setNextGraphics(new Graphics(2602));
				} else if (loop == 5) {
				player.setLocation(destination);
				player.sendMessage("workzzzzzz gj nex0n");
				} else if (loop == 6) {
				player.setNextAnimation(new Animation(13654));
				player.setNextGraphics(new Graphics(2603));
				stop();
				}
				loop++;
				}
			}, 0, 1);
		
	}
	
}
