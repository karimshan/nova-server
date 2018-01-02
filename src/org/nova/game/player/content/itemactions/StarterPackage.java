package org.nova.game.player.content.itemactions;
/**
 * 
 * @author Fuzen Seth
 * @information Represents starter package.
 * @since 9.3.2014
 */
public class StarterPackage {

	
	public static enum ItemHolder {
		IRON_SCIMITAR(1, 1);	
		
		private int itemId;
		private int amount;
		
		private ItemHolder(int itemId, int amount) {
		this.itemId = itemId;
		this.amount = amount;
		}
	}
	
	
}
