package org.nova.game.player.content.cities.content;

import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;

public class Fillables {
	
	private transient Player player;
	
	private GlobalObject worldObject;
	
	private Animation BUCKET_FILL_ANIMATION = new Animation(833);
	
	public Fillables(Player player) {
		this.player = player;
		setWorldObject(worldObject);
	}

	public boolean fillBuckets(Item itemId, GlobalObject object) {
		player.addStopDelay(3);
		if (itemId.getName().equalsIgnoreCase("Bucket") && object.defs().name.contains("Fountain") || object.defs().name.contains("Well") || object.defs().name.contains("Sink")) {
			player.getInventory().deleteItem(1925,1);
			player.getInventory().addItem(1929,1);
			player.setNextAnimation(BUCKET_FILL_ANIMATION);
			player.sendMessage("You take some water from " + object.defs().name.toLowerCase() + " and fill the " + itemId.getName().toLowerCase() + ".");
			return true;
		}
		if (itemId.getName().equalsIgnoreCase("vial") && object.defs().name.contains("Fountain") || object.defs().name.contains("Well")) {
			player.getInventory().deleteItem(229,1);
			player.getInventory().addItem(227,1);
			player.setNextAnimation(BUCKET_FILL_ANIMATION);
			player.sendMessage("You take some water from " + object.defs().name.toLowerCase() + " and fill the " + itemId.getName().toLowerCase() + ".");
			return true;
		}
		return false;
	}
	
	public GlobalObject getWorldObject() {
		return worldObject;
	}

	public void setWorldObject(GlobalObject worldObject) {
		this.worldObject = worldObject;
	}
	
}

