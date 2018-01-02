package org.nova.game.player.content;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.masks.Animation;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class Enchanting {
public static int NPC_ANIM = 713,NPC_GFX = 1897;

	
public static void sendEnchantAction(Player player) {
	player.setNextAnimation(new Animation(713));
	player.sm("Pikkupstix is currently enchanting...");
	player.setNextGraphics(new org.nova.game.masks.Graphics(1897));
}
public static void enchantSalveAmulet(Player player) {
	player.interfaces().closeChatBoxInterface();
	if (player.getInventory().containsItem(4081, 1) && player.getInventory().containsItem(995, 10000000)) {
		player.getInventory().deleteItem(995, 10000000);
		player.getInventory().deleteItem(4081,1);
		player.getInventory().addItem(10588,1);
		player.setNextAnimation(new Animation(713));
		player.setNextGraphics(new org.nova.game.masks.Graphics(1897));
		player.getMatrixDialogues().startDialogue("SimpleMessage", "Your amulet has been enchanted to: Salve amulet (e).");
	} else {
		player.sm("Enchanting was not complete. You must have Salve amulet and 10 million coins.");
	}
}



	public static void enchantBerserker(final Player player) {
		if (player.getInventory().containsItem(3751, 1)) {
			player.interfaces().closeChatBoxInterface();
			BerserkerEnchant(player);
			} else {
				player.interfaces().closeChatBoxInterface();
				player.sm("You must have completed Battle Quest Hoarfrost Hollow. Also you must have berserker helm.");
			}
		}

	public static void enchantArcher(final Player player) {
		if (player.getInventory().containsItem(3749, 1)) {

			player.interfaces().closeChatBoxInterface();
			ArcherEnchant(player);
			} else {
				player.interfaces().closeChatBoxInterface();
				player.sm("You must have completed Battle Quest Hoarfrost Hollow. Also you must have Archer helm.");
			}
		}
	
	public static void enchantFarseer(final Player player) {
		if (player.getInventory().containsItem(3755, 1)) {
			player.interfaces().closeChatBoxInterface();
			FarseerEnchant(player);
			} else {
				player.interfaces().closeChatBoxInterface();
				player.sm("You must have completed Battle Quest Hoarfrost Hollow. Also you must have Farseer helm.");
			}
		}
	
	public static void enchantNeitiznot(final Player player) {
		if (!player.getInventory().containsItem(10828, 1)) {
			player.interfaces().closeChatBoxInterface();
			NeitiznotEnchant(player);
			} else {
				player.interfaces().closeChatBoxInterface();
				player.sm("You must have completed Battle Quest Hoarfrost Hollow. Also you must have Farseer helm.");
			}
		}
	
	
	
	
	public static void FarseerEnchant(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				player.lock();
				player.interfaces().closeChatBoxInterface();					
				if (loop == 0) {
					player.getInventory().deleteItem(3755, 1);
					player.lock();
					player.sm("Pikkupstix is getting ready for enchanting...");
				} else if (loop == 3) {
					sendEnchantAction(player);
					player.sm("You are currently enchanting...");
				} else if (loop == 4) {
				player.getInventory().refresh();
				player.getInventory().addItem(12679, 1);
				stop();	
				player.unlock();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "Your Farseer helm has been enchanted.");
				}
				loop++;
				}
			}, 0, 1);
	}
	public static void ArcherEnchant(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				player.lock();
				player.interfaces().closeChatBoxInterface();					
				if (loop == 0) {
					player.getInventory().deleteItem(3749, 1);
					player.lock();
					player.sm("Pikkupstix is getting ready for enchanting...");
				} else if (loop == 3) {
					sendEnchantAction(player);
					player.sm("You are currently enchanting...");
				} else if (loop == 4) {
				player.getInventory().refresh();
				player.getInventory().addItem(12673, 1);
				stop();	
				player.unlock();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "Your Archer helm has been enchanted.");
				}
				loop++;
				}
			}, 0, 1);
	}
	public static void NeitiznotEnchant(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				player.lock();
				player.interfaces().closeChatBoxInterface();					
				if (loop == 0) {
					player.getInventory().deleteItem(10828, 1);
					player.lock();
					player.sm("Pikkupstix is getting ready for enchanting...");
				} else if (loop == 3) {
					sendEnchantAction(player);
					player.sm("You are currently enchanting...");
				} else if (loop == 4) {
				player.getInventory().refresh();
				player.getInventory().addItem(12681, 1);
				stop();	
				player.unlock();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "Your Helm of Neitiznot has been enchanted.");
				}
				loop++;
				}
			}, 0, 1);
	}
	public static void BerserkerEnchant(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				player.lock();
				player.interfaces().closeChatBoxInterface();					
				if (loop == 0) {
					player.getInventory().deleteItem(3751, 1);
					player.lock();
					player.sm("Pikkupstix is getting ready for enchanting...");
				} else if (loop == 3) {
					sendEnchantAction(player);
					player.sm("You are currently enchanting...");
				} else if (loop == 4) {
				player.getInventory().refresh();
				player.getInventory().addItem(12675, 1);
				stop();	
				player.unlock();
				player.getMatrixDialogues().startDialogue("SimpleMessage", "Your Berserker helm has been enchanted.");
				}
				loop++;
			}			
			}, 0, 1);
	}
	
	public NPC findNPC(int id) {
		for (NPC npc : Game.getNPCs()) {
			if (npc == null || npc.getId() != id)
				continue;
			return npc;
		}
		return null;
	}
	}
