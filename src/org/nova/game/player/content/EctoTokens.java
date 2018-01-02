package org.nova.game.player.content;

import org.nova.game.item.Item;
import org.nova.game.item.ItemsContainer;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @infora
 */
public class EctoTokens {
	/**
	 * Tokens
	 */
    private ItemsContainer<Item> tokens;
    
    /**
     * Holds ecto token's item id.
     */
    public static final int ECTO_TOKEN = 4278;
	/**
	 * Removes ecto tokens.
	 * @param player
	 * @param amount
	 */
	public void deleteEctoTokens(Player player, int amount) {
		player.getInventory().deleteItem(ECTO_TOKEN, amount);
	}
	
	/**
	 * Adds ecto tokens.
	 * @param player
	 * @param amount
	 */
	public void addEctoTokens(Player player, int amount) {
		player.getInventory().addItem(ECTO_TOKEN, amount);
	}
	
	/**
	 * Remove tokens.
	 * @param player
	 * @param list
	 * @return
	 */
    public boolean removeTokens(Player player, Item... list) {
        if (list == null || list.length == 0) {
                return false;
        }
        for (Item item : list) {
                if (item != null)
                 deleteEctoTokens(player, tokens.getSize());
        }
        refresh(player);
        return true;
    }
    /**
     * Refresh tokens.
     * @param player
     * @param slots
     */
    public void refresh(Player player, int... slots) {
        player.packets().sendUpdateItems(93, tokens, slots);
    }
    /**
     * Does the player contain one token?
     * @param player
     * @return
     */
    public boolean containsOneToken(Player player) {
    return player.getInventory().containsItem(ECTO_TOKEN, 1);	
    }
    
	/**
	 * Gets the ecto-tokens from player's inventory.
	 * @param player
	 * @param amount
	 * @return
	 */
	@SuppressWarnings("null")
	public int getEctotokens(Player player, int amount) {
		final boolean tokens = player.getInventory().containsItem(ECTO_TOKEN, amount);
		if (tokens)
		return amount;
		else
			return (Integer) null;
	}
	
}
