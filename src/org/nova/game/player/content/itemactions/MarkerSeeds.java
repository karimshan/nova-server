package org.nova.game.player.content.itemactions;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 */
public class MarkerSeeds {
	
	private Player player;
	
	public static int MARKER_SEED = 15075;
	
	private ItemDefinition definitions;
	
	private boolean activeMarker;
	
	private transient NPC npc;
	
	public MarkerSeeds(Player player, ItemDefinition definitions, NPC npc) {
		this.player = player;
		this.definitions = definitions;
		this.npc = npc;
	}
	
	public void plantSeed() {
		if (isActiveMarker()) { 
		player.sendMessage("You have already active marker plant, pick it up first.");
		return;
		}
		player.getInventory().deleteItem(15075,1);
		player.sendMessage("You succesfully plant the marker seed.");
		player.setNextAnimation(new Animation(827));
		Game.spawnNPC(9150, new Location(player.getX() +1, player.getY(), player.getZ()), -1, true, true);
		setActiveMarker(true);
	}
	
	public void pickUp() {
		if (npc != null) {
			player.sendMessage("This is not your marker plant, you cannot pick it up.");
			return;
		}
			Game.getNPCs().remove(npc.getIndex() == 9150);
	}
	
	public boolean plantMarkerSeed(Player player, Item item){
		if (definitions.getName().equals("Marker seeds")) {
			plantSeed();
			return true;
		}
		return false;
	}

	public boolean isActiveMarker() {
		return activeMarker;
	}

	public void setActiveMarker(boolean activeMarker) {
		this.activeMarker = activeMarker;
	}
	
}
