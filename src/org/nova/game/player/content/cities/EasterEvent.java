package org.nova.game.player.content.cities;

import org.nova.game.map.GlobalObject;
import org.nova.game.masks.ForceTalk;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the easter event.
 * @since 4.4.2014
 */
public class EasterEvent {

	public static final int BUNNY_NPC = -1;
	
	/**
	 * Handles the game object.
	 * @param player
	 * @param gameObject
	 * @return
	 */
	public static final boolean handleGameObject(Player player, GlobalObject gameObject) {
		switch (gameObject.getId()) {
		
		}
		return false;
	}
	/**
	 * Updates the hint icons during the event.
	 */
	public static void updateHintIcons(Player player) {
		switch (player.getEasterEventStage()) {
		case 0:
			break;
		case 1:
			break;
		case 3:
			break;
		case 4:
			break;
				
		}
		
	}
 	/**
	 * Transforms in to a bunny.
	 * @param player
	 * @param untransform
	 */
	public static final void transform(Player player, boolean untransform) {
		if (!untransform)
			player.getAppearance().transformIntoNPC(-1);
		else {
			
			player.setTeleport(true);
			player.setNextForceTalk(new ForceTalk("What happened?! I'm a rabbit!"));
			player.getAppearance().transformIntoNPC(BUNNY_NPC);
			player.setTransformed(true);
			
		}
	}
	
}
