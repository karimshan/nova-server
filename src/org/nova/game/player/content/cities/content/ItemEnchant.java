package org.nova.game.player.content.cities.content;

import org.nova.game.player.Player;
/**
 * 
 * @author Fuzen Seth
 * 7.11.2013 Nova II
 */
public class ItemEnchant {

	public ItemEnchant() {
	setPlayer(player);	
	}
	
	private String enchantable;
	
	private void enchant() {
		/**
		 * Looped method
		 */
	}
	/**
	 * Data of enchantables.
	 * @author Fuzen Seth
	 *
	 */
	public static enum ENCHANTABLES {
		NEITIZNOT_HELM("NEITIZNOT_HELMET"),
		EXCALIBUR("EXCALBUR");
		
		private String name;
		
	ENCHANTABLES(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
	public void testEnchantation() {
		this.setEnchantTask(ENCHANTABLES.NEITIZNOT_HELM.getName());
	}
	
	private void getItemToEnchant() {
		switch (enchantable.toString()) {
		case "EXCALBUR":
			break;
		case "NEITIZNOT_HELMET":
			enchant(1,1);
			break;
			default:
				player.sendMessage("There was problem while enchanting an item.");;
		
		}
	}
	
	private void enchant(int itemId, int enchantedItem) {
		player.getInventory().addItem(enchantedItem, 1);
		player.getInventory().deleteItem(itemId, 1);
	}
	
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getEnchantable() {
		return enchantable;
	}

	/**
	 * You give task, item you want to enchant.
	 * @param enchantable
	 */
	public void setEnchantTask(String enchantable) {
		this.enchantable = enchantable;
		enchant();
	}
	private Player player;
	
}
