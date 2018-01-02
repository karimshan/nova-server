
package org.nova.game.player.content;

import java.util.ArrayList;
import java.util.List;

import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class ItemEffects {
	
private transient Player player;

private static transient Item item;

private static String nestRewardName;

final static int[] gifts = {14664};

public static int EMOTE = 9603, GFX = 1684;


	public static void processMagicBoxAction(Player player) {
		player.getInventory().deleteItem(10025,1);
		player.getInventory().deleteItem(item.getId(), 1);
		player.getBank().addItem(item.getId(), item.getAmount(), true);
		player.sm("The imp teleports away, taking the item to your bank account.");
	}

	public static void processRingofLife(Player player) {
	player.getEquipment().deleteItem(player.getEquipment().getRingId(), 1);
	Magic.sendItemTeleportSpell(player, true, EMOTE, GFX, 4, (new Location(3221, 3218, 0)));
	player.sm("The ring of life has saved your life.");
		
	}
	public static void handlePhoenixNecklaceEffect(Player player) {
			player.heal(350);
		
	}
	/**
	 * Gotta do this better using int[]
	 * @param player
	 */
	public static void openNest(Player player, Item item) {
		randomLoot(player);
	}

	/**
	 * This will get the random loot for nests,
	 * another method will scan the String and handle the rewards.
	 * @param player
	 */
	public static void randomLoot(Player player) {
		int Random = Misc.getRandom(3);
		switch (Random) {
		case 0:
			nestRewardName="Uncut rubys and some sapphires.";
			checkRewards(player);
			break;
		case 1:
			nestRewardName="Uncut rubys and some sapphires.";
			checkRewards(player);
			break;
		case 2:
			nestRewardName="Uncut emeralds and some diamonds.";
			checkRewards(player);
			break;
		case 3:
			nestRewardName="Uncut dragonstones!";
			checkRewards(player);
			break;
		}
	}
	/**
	 *  This will basically check which reward is given.
	 * @param player
	 */
	public static void checkRewards(Player player) {
		if (!player.getInventory().hasFreeSlots())
			return;
		if (getNestRewardName().equals("Uncut rubys and some sapphires.")) {
			player.getInventory().addItem(1620, Misc.getRandom(30));
			player.getInventory().addItem(1624, Misc.getRandom(30));
			player.sm("You open the nest, your loot is: "+getNestRewardName()+"");
		}
		if (getNestRewardName().equals("Uncut emeralds and some diamonds.")) {
				player.getInventory().addItem(1622, Misc.getRandom(20));
				player.getInventory().addItem(1618, Misc.getRandom(30));
				player.sm("You have open the nest, your loot is: "+getNestRewardName()+"");
		}
		if (getNestRewardName().equals("Uncut dragonstones!")) {
		player.getInventory().addItem(1632, Misc.getRandom(5));
		player.sm("You have open the nest, your loot is: "+getNestRewardName()+"");
		}
	}
	
	public static void sendNestRandomly(Player player) {
		int random = Misc.getRandom(150);
		switch (random) {
		case 2:
			dropNest(player);
			break;
		case 30:
			dropNest(player);
			break;
		case 40:
			dropNest(player);
			break;
		case 70:
			dropNest(player);
			break;
		case 122:
			dropNest(player);
			break;
		}
	}

	public static void dropNest(Player player) {
	  List<int[]> positions = new ArrayList<int[]>();
	  for (int x = player.getX() - 2; x <= player.getX() + 2; x++) {
	    for (int y = player.getY() - 2; y <= player.getY() + 2; y++) {
	         positions.add(new int[] { x, y });
	    }
	  }
	  if (positions.isEmpty()) {
	      positions = null;
	      return;
	  }
	  Game.addGroundItem(new Item(5073, 1),new Location(player.getCoordFaceX(player.getSize()),
						player.getCoordFaceY(player.getSize()),player.getZ()),player,false,180,true);
	  player.packets().sendMessage("A bird nest falls from the tree!");
	  positions.clear();
	  positions = null;
	} 
	
	public static boolean inNexController(Location tile) {
		return ((tile.getX() >= 2904 && tile.getX() <= 2941) && (tile.getY() >= 5184 && tile.getY() <= 5220));
	}

	public static void savecurrentLocation(Player player) {
		
		if (inNexController(player)) {
			player.packets().sendMessage("You can't save your current location here!");
			return;
		}
		player.setSaveX(player.getX());
		player.setSaveY(player.getY());
		player.setSaveplane(player.getZ());
		player.interfaces().closeChatBoxInterface();
		player.sm("Your location to the teleport crystal has been saved.");
	}
	
	public static void teleportSavedLocation(Player player) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new Location(
				player.getSaveX(), player.getSaveY(), player.getSaveplane()));
		player.sm("You have teleported to the last saved location.");
		player.interfaces().closeChatBoxInterface();
	}
	
	public void openSextant(){
		player.interfaces().sendInterface(365);
	}
	
	public void handleCrackers() {
		String username="";
		Player other = Game.getPlayerByDisplayName(username);
	other.sm(" "+player.getDisplayName()+" has pulled a cracker on you.");
	player.sm("You have pulled the cracker on "+other.getDisplayName()+".");
	player.interfaces().sendInterface(518);
	
	}

	public static void OpenEventBox(final Player player) {
		GiftBox.openGift(player);
		player.addStopDelay(3);
	
	}

	public int[] getgifts() {
		return gifts;
	}
	
	public static void recitePrayer(Player player) {
	if (player.getPoison().isPoisoned()) {
	player.lock(3);
	player.getPoison().makePoisoned(0);
	player.getPrayer().drainPrayer(110);
	player.getPoison().reset();
	player.sm("You have cured your poison away, with the power of Saradomin.");
	player.setNextAnimation(new Animation(5864));
	player.setLastRecite(30);
	} else {
		player.sm("You are not poisoned, there's nothing to cure.");
	}
}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public static String getNestRewardName() {
		return nestRewardName;
	}
	
	public void setNestRewardName(String nestRewardName) {
		ItemEffects.nestRewardName = nestRewardName;
	}
	
	
}
