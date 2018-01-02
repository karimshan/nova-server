package org.nova.game.player.controlers;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.npc.NPC;
import org.nova.game.npc.wguild.AnimatedArmour;
import org.nova.game.player.Player;

public class WGuildControler extends Controller {
	
	private int tick;
	private static NPC animatedArmour;

	@Override
	public void start() {
		sendInterfaces();
	}
	
	@Override
	public void process() {
		sendInterfaces();
		if(player.getTemporaryAttributtes().get("killednpc") != null) {
			player.setWGuildTokens((player.getWGuildTokens() + (Integer) player.getTemporaryAttributtes().get("killednpc")));
			player.getTemporaryAttributtes().remove("killednpc");
			sendInterfaces();
			return;
		}
		if(player.inClopsRoom()) {
			if(player.getWGuildTokens() <= 20) {
				player.packets().sendMessage("Your time is about to run out. Please leave the cyclops room.");
			}
			if(player.getWGuildTokens() <= 10) {
				player.setLocation(new Location(2843, 3536, 2));
				player.packets().sendMessage("Your time has run out.");
				player.setInClopsRoom(false);
				return;
			}
			if(tick == 20) {
				player.setWGuildTokens((player.getWGuildTokens() - 10));
				sendInterfaces();
				player.packets().sendMessage("Your tokens reduce by 10.", true);
				tick = 0;
			}
			tick++;
		}
	}
	
	@Override
	public boolean processMagicTeleport(Location toTile) {
		if(player.inClopsRoom()) {
			player.getMatrixDialogues()
			.startDialogue("SimpleMessage",
					new Object[] {"A magical force prevents you from teleporting." });
			return false;
		} else {
			stop();
			return true;
		}
	}
	
	@Override
	public boolean processItemTeleport(Location toTile) {
		if(player.inClopsRoom()) {
			player.getMatrixDialogues()
			.startDialogue("SimpleMessage",
					new Object[] {"A magical force prevents you from teleporting." });
			return false;
		} else {
			stop();
			return true;
		}
	}
	
	public static void handleItemOnObject(final Player player, GlobalObject object, Item item) {
		player.getControllerManager().startController("WGuildControler", new Object[] { });
		if(isArmour(item.getId())) {
			if(player.getY() != 3533) {
				player.packets().sendMessage("You cannot add the armour from this side.");
				return;
			}
			int npcId = 0;
			int armourSlot = 0;
			switch(getArmour(item)) {
			case "bronze":
				npcId = 4278;
				armourSlot = 1;
				break;
			case "iron":
				npcId = 4279;
				armourSlot = 0;
				break;
			case "steel":
				npcId = 4280;
				armourSlot = 2;
				break;
			case "mithril":
				npcId = 4282;
				armourSlot = 3;
				break;
			case "adamant":
				npcId = 4283;
				armourSlot = 4;
				break;
			case "rune":
				npcId = 4284;
				armourSlot = 5;
				break;
			}
			final int finalNpc = npcId;
			if(!contains(player, ARMOURS[armourSlot])) {
				player.packets().sendMessage("You do not have all the required armour pieces.");
				return;
			}
			player.setNextAnimation(new Animation(827));
			player.packets().sendMessage("You place the armour on the animator and it starts to glow");
			deleteItems(player, ARMOURS[armourSlot]);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.addWalkSteps(player.getX(), player.getY()+5);
					stop();
				}
			}, 3);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					animatedArmour = new AnimatedArmour(finalNpc, new Location(player.getX(), player.getY()-5, player.getZ()), -1, true, true);
					animatedArmour.setNextForceTalk(new ForceTalk("I'm ALIVE!"));
					animatedArmour.addWalkSteps(animatedArmour.getX(), animatedArmour.getY()+4);
					animatedArmour.setTarget(player);
					((AnimatedArmour) animatedArmour).setPlayerCon(player);
					animatedArmour.setAttackedBy(player);
					player.hints().addHintIcon(animatedArmour, 0, false, false);
					stop();
				}
			}, 6);
		}
	}
	
	@Override
	public boolean canAttack(Entity target) {
		if(target instanceof AnimatedArmour) {
			AnimatedArmour n = (AnimatedArmour) target;
			if(player != n.getPlayerCon()) {
				player.packets().sendMessage("It's not after you.");
				return false;
			}
		}
		return true;
	}
	
	public static void deleteItems(Player player, int[] items) {
		for(int i = 0; i < items.length; i++) 
			player.getInventory().deleteItem(items[i], 1);
	}
	
	public static boolean isArmour(int itemId) {
		for(int i = 0; i < ARMOURS.length; i++) {
			for(int i2 = 0; i2 < ARMOURS[i].length; i2++) {
				if(itemId == ARMOURS[i][i2]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static String getArmour(Item item) {
		String name = item.defs().getName().toLowerCase();
		if(name.contains("bronze")) {
			return "bronze";
		} else if(name.contains("steel")) {
			return "steel";
		} else if(name.contains("iron")) {
			return "iron";
		} else if(name.contains("mithril")) {
			return "mithril";
		} else if(name.contains("adamant")) {
			return "adamant";
		} else if(name.contains("rune")) {
			return "rune";
		}
		return null;
	}
	
	public static boolean contains(Player player, int[] item) {
		for(int i = 0; i < item.length; i++) {
			if(!player.getInventory().containsItem(item[i], 1)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean processObjectTeleport(Location tile) {
		if(player.inClopsRoom()) {
			player.getMatrixDialogues()
			.startDialogue("SimpleMessage",
					new Object[] {"A magical force prevents you from teleporting." });
			return false;
		} else {
			stop();
			return true;
		}
	}
	
	public void stop() {
		if(animatedArmour != null) {
			animatedArmour.finish();
		}
		player.packets().closeInterface(player.interfaces().isFullScreen() ? 10 : 8);
		removeControler();
	}
	
	@Override
	public boolean processObjectClick1(GlobalObject object) {
		start();
		if(object.getId() == 66599 || object.getId() == 66601) {
			if(player.getX() == 2846) {
				enterClopsRoom();
			} else if(player.getX() == 2847) {
				leaveClopsRoom();
			}
			return false;
		}
		if(animatedArmour != null) {
			animatedArmour.finish();
		}
		return true;
	}
	
	@Override
	public boolean logout() {
		return false;
	}
	
	@Override
	public boolean login() {
		start();
		return false;
	}
	
	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if(interfaceId == 271) {
			if(player.getPrayer().isAncientCurses()) {
				player.packets().sendMessage("The warriors of this guild forbid such dark prayers.");
				return false;
			}
		}
		return true;
	}
	
	public void enterClopsRoom() {
		if(player.getWGuildTokens() < 100) {
			player.packets().sendMessage("You must have over 100 tokens to enter.");
			return;
		}
		player.setInClopsRoom(true);
		player.setLocation(new Location(player.getX()+1, player.getY(), player.getZ()));
	}
	
	public void leaveClopsRoom() {
		player.setInClopsRoom(false);
		player.setLocation(new Location(player.getX()-1, player.getY(), player.getZ()));
	}
	
	public void sendInterfaces() {
		player.interfaces().sendTab(player.interfaces().isFullScreen() ? 10 : 8, 1057);
		player.packets().sendConfig(2030, player.getWGuildTokens());
	}
	
	public static void dropDefender(Player player, NPC npc) {
		int itemId = 8844;
		for(int i = 0; i < DEFENDERS.length; i++) {
			if(player.getInventory().containsItem(DEFENDERS[i], 1) || player.getBank().containsItem(DEFENDERS[i], 1)) {
				if(DEFENDERS[i] == 20072) {
					itemId = 20072;
				} else {
					itemId = DEFENDERS[i + 1];
				}
			}
		}
		Game.addGroundItem(new Item(itemId, 1), new Location(
				npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), npc.getZ()), player,
				false, 180, true);
	}
	//								Iron					Bronze					Steel
	public static int[][] ARMOURS = { {1153, 1115, 1067 }, { 1155, 1117, 1075 }, { 1157, 1119, 1069 }, 
		//Mithril				Adamant				Rune
		{ 1159, 1121, 1071 }, { 1161, 1123, 1073 }, { 1163, 1127, 1079 } };
	public static int[] DEFENDERS = { 8844, 8845, 8846, 8847, 8848, 8849, 8850, 20072 };

}