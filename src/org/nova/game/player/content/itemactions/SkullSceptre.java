package org.nova.game.player.content.itemactions;
/**
 * 
 * @author Fuzen Seth
 * @information Represents the skull sceptre item.
 * @since 21.6.2014
 */
public class SkullSceptre {
	/**
	 * The singleton.
	 */
	private static final SkullSceptre singleton = new SkullSceptre();

	
	public void teleport() {
		
	}
	
	
	public static SkullSceptre getSingleton() {
		return singleton;
	}
}
