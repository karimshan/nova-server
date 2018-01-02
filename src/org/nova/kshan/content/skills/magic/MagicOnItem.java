package org.nova.kshan.content.skills.magic;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.Item;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.utility.EconomyPrices;

public class MagicOnItem {
	
    public static final int LOW_ALCHEMY = 38;
    public static final int HIGH_ALCHEMY = 59;
    public static final int SUPER_HEAT = 50;
    public static final int LV1_ENCHANT = 29;
    public static final int LV2_ENCHANT = 41;
    public static final int LV3_ENCHANT = 53;
    public static final int LV4_ENCHANT = 61;
    public static final int LV5_ENCHANT = 76;
    public static final int LV6_ENCHANT = 88;

    public static void handleMagic(Player player, int magicId, Item item) {
        if (magicId == 59) {
            processAlchemy(player, new Item(item.getId()), false);
        }
          switch (magicId) {
        
            case LOW_ALCHEMY:
                processAlchemy(player, item, true);
                break;
                
            case HIGH_ALCHEMY:
                processAlchemy(player, item, false);
                break;
        default: 
            processAlchemy(player, new Item(item.getId()), false);
            
        }
        
    }
    public static void processAlchemy(final Player player, Item item, boolean low) {
        if (player.alch == true) {
      	  return;  
        }
        player.alch = true;
        if (player.getSkills().getLevel(Skills.MAGIC) < (low == true ? 21 : 55)) {
            player.packets().sendMessage("You do not have the required level to cast this spell.");
            player.alch = false;
            return;
        }
        
        if (item.getId() == 995) {
            player.packets().sendMessage("You can't alch this!");
            player.alch = false;
            return;
        }
        
        if (hasFireStaff(player)) {
            if (!player.getInventory().containsItem(561, 1) || !player.getInventory().containsItem(554, (low == true ? 3 : 5)) && !player.getInventory().containsItem(1387, 1)) {
                player.packets().sendMessage("You do not have the required runes to cast this spell.");
                player.alch = false;
                return;
            }
        } else {
            if (!player.getInventory().containsItem(554, (low == true ? 3 : 5)) && !player.getInventory().containsItem(1387, 1)) {
                player.packets().sendMessage("You do not have the required runes to cast this spell.");
                player.alch = false;
                return;
            }
        }
        if (!hasFireStaff(player)) {
            player.setNextAnimation(new Animation(low == true ? 712 : 713));
            player.setNextGraphics(new Graphics(low == false ? 113 : 1692));
        } else {
            player.setNextAnimation(new Animation(low == true ? 9625 : 9633));
            player.setNextGraphics(new Graphics(low == false ? 113 : 1693));
        }
        player.getInventory().deleteItem(561, 1);
        
        if (!hasFireStaff(player)) {
            player.getInventory().deleteItem(554, (low == true ? 3 : 5));
        }
        player.getInventory().deleteItem(item.getId(), 1);
        int baseValue = EconomyPrices.getPrice(item.getId());
        int value = baseValue / (low == true ? 10 : 5);
        player.getInventory().addItem(995, value);
        player.getSkills().addXp(Skills.MAGIC, (low == true ? 10 : 15));
        WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				if (loop == 2) {
				player.interfaces().openGameTab(7);
				player.alch = false;
					stop();
				}
				loop++;
				}
			}, 0, 1);
    }
 
    private static boolean hasFireStaff(Player player) {
    	return player.getEquipment().getWeaponId() == 1387 || player.getEquipment().getWeaponId() == 1393;
    }
}