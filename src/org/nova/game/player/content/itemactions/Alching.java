package org.nova.game.player.content.itemactions;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class Alching {
	
	public Alching(Player player) {
		this.player = player;
		getItem();
	}
	
	private transient Player player;
	
	private transient Item item;
	
	/**
	 * Alches the item
	 */
	public void alchItems() {
		player.addStopDelay(3);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					//alch action emote + gfx
				} else {
		/**
		 * Adds the money, removes the coin
		 */
					stop();
				}
			}
		}, 0, 3);
	}
	

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
}
