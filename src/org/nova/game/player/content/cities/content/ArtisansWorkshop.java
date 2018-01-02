package org.nova.game.player.content.cities.content;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 * Artisan's Workshop minigame, located in Falador.
 * Created 6.7.2013
 */
public class ArtisansWorkshop {
	//Player instance
	private transient Player player;
	//Which ingot
	private String product; 
	//Ore's name used for checking
	private String oreName;
	
	private transient Item item;
	
	public ArtisansWorkshop(Item item) {
		this.item = item;
	}
	public ArtisansWorkshop(Player player) {
		this.player = player;
	}
	//This will basically open the main interface
	public void openShop() {
		player.interfaces().sendInterface(1);
	}
	/**
	 * Ore storing
	 */
	public void storeOres() {
		int oreAmount = item.getAmount();
	switch (this.getOreName()) {
	case "Bronze Ingot":
		if (player.getInventory().containsItem(1,1)) {
			player.getInventory().deleteItem(1, oreAmount); //<- Gets automaticly amount
			player.oresStored += oreAmount; 
			player.sm("You have succesfully stored "+oreAmount+".");
		}
		break;
	case "Iron Ingot":
		
		break;
	case "Steel Ingot":
		break;
		case "Mithril Ingot":
			break;
		case "Adamant Ingot":
			break;
		case "Rune Ingot":
			break;
	}
}
	
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	public String getOreName() {
		return oreName;
	}
	public void setOreName(String oreName) {
		this.oreName = oreName;
	}
	
}
