package org.nova.kshan.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.utility.loading.playerutils.SFiles;

/**
 * Stores temporary or permanent data that the player uses.
 * 
 * @author K-Shan
 *
 */
public class TemporaryData implements Serializable {
	
	private static final long serialVersionUID = -5502973949324819662L;
	
	private transient Player player;
	
	private Map<String, Object> savedData;
	private transient Map<String, Object> runtimeData;
	private ArrayList<Item> cachedTradeItems;
	
	// Other variables that I don't want to cram in Player.java
	
	public void setFields(Player player) {
		this.player = player;
		if(this.savedData == null)
			savedData = new HashMap<String, Object>();
		this.runtimeData = new HashMap<String, Object>();
		if(cachedTradeItems == null)
			cachedTradeItems = new ArrayList<Item>();
	}
	
	public void login() {
		if(!getCachedTradeItems().isEmpty()) { // Fixes trade "glitch" lmfao
			for(Item item : getCachedTradeItems())
				player.getInventory().addItem(item);
			player.packets().sendMessage("The server disconnected while you were trading and your items have been returned.");
			getCachedTradeItems().clear();
			SFiles.savePlayer(player);
		}
	}
	
	public Map<String, Object> getSavedData() {
		return savedData;
	}
	
	public Map<String, Object> getRuntimeData() {
		return runtimeData;
	}

	public ArrayList<Item> getCachedTradeItems() {
		return cachedTradeItems;
	}

}
