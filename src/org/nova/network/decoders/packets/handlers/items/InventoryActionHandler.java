package org.nova.network.decoders.packets.handlers.items;

import java.util.HashMap;
import java.util.List;

import org.nova.Constants;
import org.nova.cache.definition.ItemDefinition;
import org.nova.cache.definition.NPCDefinition;
import org.nova.game.Game;
import org.nova.game.engine.cores.WorldThread;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.familiar.Familiar.SpecialAttack;
import org.nova.game.player.ClueScrolls;
import org.nova.game.player.Equipment;
import org.nova.game.player.Inventory;
import org.nova.game.player.Pets;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Firelighting;
import org.nova.game.player.actions.Firemaking;
import org.nova.game.player.actions.Fletching;
import org.nova.game.player.actions.Fletching.Fletch;
import org.nova.game.player.actions.GemCutting;
import org.nova.game.player.actions.GemCutting.Gem;
import org.nova.game.player.actions.HerbCleaning;
import org.nova.game.player.actions.Herblore;
import org.nova.game.player.actions.Hunter;
import org.nova.game.player.actions.Hunter.HunterEquipment;
import org.nova.game.player.actions.LeatherCrafting;
import org.nova.game.player.actions.Summoning;
import org.nova.game.player.actions.Summoning.Pouches;
import org.nova.game.player.content.BonesBurying.Bone;
import org.nova.game.player.content.Caskets;
import org.nova.game.player.content.DiceGame;
import org.nova.game.player.content.Dicing;
import org.nova.game.player.content.Foods;
import org.nova.game.player.content.ItemConstants;
import org.nova.game.player.content.ItemEffects;
import org.nova.game.player.content.ItemOnItemHandler;
import org.nova.game.player.content.ItemOnItemHandler.ItemOnItem;
import org.nova.game.player.content.ItemSets;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.Pots;
import org.nova.game.player.content.PrayerScroll;
import org.nova.game.player.content.Runecrafting;
import org.nova.game.player.content.SkillCapeCustomizer;
import org.nova.game.player.content.TeleportManager;
import org.nova.game.player.content.ToyHorsey;
import org.nova.game.player.content.cities.ApeToll;
import org.nova.game.player.content.cities.content.ItemEmptying;
import org.nova.game.player.content.handlers.XPLamp;
import org.nova.game.player.content.itemactions.AncientEffigies;
import org.nova.game.player.content.itemactions.AshScattering.Ash;
import org.nova.game.player.content.itemactions.ItemPoisoning;
import org.nova.game.player.content.itemactions.RottenPotato;
import org.nova.game.player.content.itemactions.SpiritShard;
import org.nova.game.player.controlers.Barrows;
import org.nova.kshan.content.pets.BossPets;
import org.nova.kshan.content.skills.magic.MagicOnItem;
import org.nova.kshan.utilities.Logs;
import org.nova.network.decoders.packets.handlers.ButtonPackets;
import org.nova.network.stream.InputStream;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;


public class InventoryActionHandler {

	public static boolean equipItem(Player player, int slotId, int itemId) {
		if (player.hasFinished() || player.isDead())
			return false;
		player.stopAll(false, false);
		Item item = player.getInventory().getItem(slotId);
		if (item == null || item.getId() != itemId)
			return false;
		if (item.defs().isNoted() || !item.defs().isWearItem(player.getAppearance().isMale())) {
			player.packets().sendMessage("You can't wear that.");
			return false;
		}
		int targetSlot = Equipment.getItemSlot(itemId);
		if (targetSlot == -1) {
			player.packets().sendMessage("You can't wear that.");
			return false;
		}
		if (!ItemConstants.canWear(item, player))
			return false;
		boolean isTwoHandedWeapon = targetSlot == 3 && Equipment.isTwoHandedWeapon(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			player.packets().sendMessage("Not enough free space in your inventory.");
			return false;
		}
		HashMap<Integer, Integer> requiriments = item.defs().getWearingSkillRequirements();
		boolean hasRequiriments = true;
		if (requiriments != null) {
			for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0)
					continue;
				int level = requiriments.get(skillId);
				if (level < 0 || level > 120)
					continue;
				if (player.getSkills().getLevelFromXP(skillId) < level) {
					String name = Skills.SKILL_NAME[skillId].toLowerCase();
					player.packets().sendMessage(
							"You need to have a"
									+ (name.startsWith("a") ? "n" : "")
									+ " " + name + " level of " + level
									+ " in order to wear this item.");
					hasRequiriments = false;
				}
			}
		}
		if(!hasRequiriments)
			return false;
		if(!player.getRandomEvent().canWearItem(item))
			return false;
		if (!player.getControllerManager().canEquip(targetSlot, itemId))
			return false;
		player.getInventory().getItems().remove(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(5))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null && Equipment.isTwoHandedWeapon(player.getEquipment().getItem(3))) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(3))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(3, null);
			}

		}
		if (player.getEquipment().getItem(targetSlot) != null && (itemId != player.getEquipment().getItem(targetSlot).getId() || !item.defs().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			} else
				player.getInventory().getItems().add(new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			player.getEquipment().getItems().set(targetSlot, null);
		}
		if (targetSlot == Equipment.SLOT_AURA)
			player.getAuraManager().removeAura();
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
		if (targetSlot == 3)
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		if(itemId == 22447)
			player.packets().sendConfig(43, 4);
		player.getCharges().wear(targetSlot);
		return true;
	}  

	public static void handleItemOption2(final Player player, final int slotId,
			final int itemId, Item item) {
		if (Firemaking.isFiremaking(player, itemId))
			return;
		if (Firelighting.isFiremaking(player, itemId))
			return;
		if (itemId == 10890) 
			ItemEffects.recitePrayer(player);
		if (itemId == 15484)
			player.interfaces().gazeOrbOfOculus();
		if (item.getName().contains("Bird's nest")) 
			ItemEffects.openNest(player, item);

		else if (itemId >= 15086 && itemId <= 15100) {
			Dicing.handleRoll(player, itemId, true);
			return;
		}
		if (ItemEmptying.emptyingItem(player, item)) 
			return;
		if (ApeToll.handleItems(player, item))
			return;
		if (itemId == 4251) 
			player.getEctophial().ProcessTeleportation(player);
		for (int i: ClueScrolls.ScrollIds){
			if (itemId == i){
				if (ClueScrolls.Scrolls.getMap(itemId) != null){
					ClueScrolls.showMap(player, ClueScrolls.Scrolls.getMap(itemId));
					return;
				}
				if (ClueScrolls.Scrolls.getObjMap(itemId) != null){
					ClueScrolls.showObjectMap(player, ClueScrolls.Scrolls.getObjMap(itemId));
					return;
				}
				if (ClueScrolls.Scrolls.getRiddles(itemId) != null){
					ClueScrolls.showRiddle(player, ClueScrolls.Scrolls.getRiddles(itemId));
					return;
				}

			}

		}
		if (itemId == 2717){
			ClueScrolls.giveReward(player);
		}
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509)
				pouch = 0;
			if (itemId == 5510)
				pouch = 1;
			if (itemId == 5512)
				pouch = 2;
			if (itemId == 5514)
				pouch = 3;
			Runecrafting.emptyPouch(player, pouch);
			player.stopAll(false);
			if (itemId == 15707) {
				TeleportManager.DaemonheimTeleport(player, 1, 1, new Location(3450, 3694, 0));
			}  	if (itemId == 600) {
				player.sm("I cannot read this book. I should show the book to Meteora located in Lunar Isle.");
			}  
		} else if (itemId == 15098) {
			// DiceGame.handleItem(player, Rolls.FRIENDS_ROLL);
			return;
		} else {
			if (player.isEquipDisabled())
				return;
			long passedTime = Misc.currentTimeMillis()
					- WorldThread.LAST_CYCLE_CTM;
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					List<Integer> slots = player.getSwitchItemCache();
					int[] slot = new int[slots.size()];
					for (int i = 0; i < slot.length; i++)
						slot[i] = slots.get(i);
					player.getSwitchItemCache().clear();
					ButtonPackets.sendWear(player, slot);
					player.stopAll(false, true);
				}
			}, passedTime >= 1200 ? 0 : passedTime > 330 ? 1 : 0);
			if (player.getSwitchItemCache().contains(slotId))
				return;
			player.getSwitchItemCache().add(slotId);
		}
	}

	public static void handleItemOption1(Player player, final int slotId,
			final int itemId, Item item) {
		long time = Misc.currentTimeMillis();
		if (player.getStopDelay() >= time
				|| player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		if (Foods.eat(player, item, slotId))
			return;
		if (PrayerScroll.getSingleton().learnPrayer(player,item))
			return;

		if (itemId == 4251) 
			player.getEctophial().ProcessTeleportation(player);
		else if (itemId == 14057) // broomstick
			player.setNextAnimation(new Animation(10532));

		if (Caskets.lootCasket(player, item)) {

			return;
		}
		if (SpiritShard.openShards(player, item))
			return;
		if (player.getDwarvenrockCake().eatCake(player, itemId)) {
			return;
		}	
		Ash ash = Ash.forId(itemId);
		if (ash != null) {
			Ash.scatter(player, slotId);
			return;
		}
		if (itemId == 20667) {
			Magic.VecnaSkull(player);
		}

		switch (itemId) {
		case 5733:
			RottenPotato.addPotatoAction(player, "eat");
			break;
		case 22440:
			player.getDialogue().start("LocationCrystalIntro");
			break;

		case 15484:
			player.interfaces().gazeOrbOfOculus();
			break;
		case 299:
			if (player.isLocked())
				return;
			if (Game.getObject(new Location(player), 10) != null) {
				player.packets().sendMessage("You cannot plant flowers here..");
				return;
			}
			final Player thisman = player;
			final double random = Misc.getRandomDouble(100);
			final Location tile = new Location(player);
			int flower = Misc.random(2980, 2987);
			if (random < 0.2) {
				flower = Misc.random(2987, 2989);
			}
			if (player.getUsername().equals("lucifer"))
				flower = 2987;
			if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
				if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
					if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
						player.addWalkSteps(player.getX(), player.getY() - 1, 1);
			player.getInventory().deleteItem(299, 1);
			final GlobalObject flowerObject = new GlobalObject(flower, 10, Misc.getRandom(4), tile.getX(), tile.getY(), tile.getZ());
			Game.spawnTemporaryObject(flowerObject, 45000);
			player.addStopDelay(3);
			thisman.faceLocation(new Location(flowerObject.getCoordFaceX(flowerObject.defs().getSizeX(), flowerObject.defs().getSizeY(),
					flowerObject.getRotation()), flowerObject.getCoordFaceY(
							flowerObject.defs().getSizeX(), flowerObject.defs().getSizeY(),
							flowerObject.getRotation()), flowerObject.getZ()));
			WorldTasksManager.schedule(new WorldTask() {
				int step;

				@Override
				public void run() {
					if (thisman == null || thisman.hasFinished())
						stop();
					if (step == 1) {
						thisman.getMatrixDialogues().startDialogue("FlowerPickup", flowerObject);
						thisman.unlock();
						stop();
					}
					step++;
				}
			}, 0, 0);


			break;
		}


		if (itemId == 6) 
			player.getDwarfcannon().cannonSetup();

		if (itemId == 29398) 
			player.getDwarfcannon().cannonSetup();

		if (itemId == 29403) 
			player.getDwarfcannon().cannonSetup();

		if (itemId == 9025) {
			player.getMatrixDialogues().startDialogue("SummerBook");
		}
		if (item.getName().contains("Bird's nest")) {
			player.getInventory().deleteItem(item.getId(), 1);
			ItemEffects.openNest(player, item);
		}
		else if (itemId == 15262)
			ItemSets.openSkillPack(player, itemId, 12183, 5000, player.getInventory().getAmountOf(itemId));
		else if (itemId == 15362)
			ItemSets.openSkillPack(player, itemId, 230, 50, player.getInventory().getAmountOf(itemId));
		else if (itemId == 15363)
			ItemSets.openSkillPack(player, itemId, 228, 50, player.getInventory().getAmountOf(itemId));
		else if (itemId == 15364)
			ItemSets.openSkillPack(player, itemId, 222, 50, player.getInventory().getAmountOf(itemId));
		else if (itemId == 15365)
			ItemSets.openSkillPack(player, itemId, 9979, 50, player.getInventory().getAmountOf(itemId));

		if (item.getName().equals("Lamp") && item.defs().containsOption("Rub")) {
			XPLamp.sendLamp(player);	
		}

		if (itemId == 14664) {
			player.getInventory().deleteItem(14664,1);
			ItemEffects.OpenEventBox(player);
		}
		for (int i: ClueScrolls.ScrollIds){
			if (itemId == i){
				if (ClueScrolls.Scrolls.getMap(itemId) != null){
					ClueScrolls.showMap(player, ClueScrolls.Scrolls.getMap(itemId));
					return;
				}
				if (ClueScrolls.Scrolls.getObjMap(itemId) != null){
					ClueScrolls.showObjectMap(player, ClueScrolls.Scrolls.getObjMap(itemId));
					return;
				}
				if (ClueScrolls.Scrolls.getRiddles(itemId) != null){
					ClueScrolls.showRiddle(player, ClueScrolls.Scrolls.getRiddles(itemId));
					return;
				}
			}

		}
		if (itemId == 2717){
			ClueScrolls.giveReward(player);
		}
		if (itemId == 12855) {
			player.getMatrixDialogues().startDialogue("OculusOrb");
		}
		if (itemId == 22340) {
			player.getMatrixDialogues().startDialogue("XPBook");
		}
		if (itemId == 6099) {
			if (player.getControllerManager().getControler() != null) {
				player.sendMessage("You cannot use teleport crystal here.");
				return;
			} else {
				player.getMatrixDialogues().startDialogue("TeleportCrystalD");	
			}
		}
		if (itemId == 15098) {
			DiceGame.rollDice8(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2075));
			return;
		}
		if (itemId == 2520) {
			ToyHorsey.play(player);
		}
		if (itemId == 2522) {
			ToyHorsey.play(player);
		}
		if (itemId == 2524) {
			ToyHorsey.play(player);
		}
		if (itemId == 2526) {
			ToyHorsey.play(player);
		}
		if (itemId == 15086) {
			DiceGame.rollDice2(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2072));
			return;
		}
		if (itemId == 15088) {
			DiceGame.rollDice3(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2074));
			return;
		}
		if (itemId == 15090) {
			DiceGame.rollDice4(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2071));
			return;
		}
		if (itemId == 15092) {
			DiceGame.rollDice5(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2070));
			return;
		}
		if (itemId == 15094) {
			DiceGame.rollDice5(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2073));
			return;
		}

		if (itemId == 15096) {
			DiceGame.rollDice7(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2068));
			return;
		}
		if (itemId == 15098) {
			DiceGame.rollDice8(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2075));
			return;
		}
		if (itemId == 15100) {
			DiceGame.rollDice1(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2069));
			return;
		}
		if (!player.getControllerManager().handleItemOption1(player, slotId, itemId, item))
			return;
		if (Pots.pot(player, item, slotId))
			return;
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509)
				pouch = 0;
			if (itemId == 5510)
				pouch = 1;
			if (itemId == 5512)
				pouch = 2;
			if (itemId == 5514)
				pouch = 3;
			Runecrafting.fillPouch(player, pouch);
			return;
		}
		if (itemId == 6199) {
			int[] RandomItems = {11732, 4151, 11283, 385, 2347, 1712, 1712, 6585, 1712, 6585, 11732, 11732, 3105, 6918, 6920, 6922, 6924, 6570, 10828, 1079, 1127, 20072, 20072, 8850, 10551, 10548, 4087, 15332, 15332, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 6585, 5698, 1704, 7378, 7370, 7390, 6737, 6731, 6733, 11716, 6199, 6199, 7386, 7394, 11846, 11850, 11852, 2673, 2669, 2671, 6889, 6914, 2653, 2655, 2657, 1837, 10330, 11848, 11854, 11856, 10332, 10334, 10336, 542, 4087, 4585, 6568, 6568, 10338, 10340, 10342, 10344, 10346, 10348, 10350, 10352, 2581, 13736, 6916, 6918, 6920, 6922, 6924, 3481, 3483, 3486, 2577, 2665, 10452, 10454, 10456, 9470, 2661, 10450, 10446, 10448, 1037, 14595, 14603, 1050, 23679, 23680, 23681, 23682, 23683, 23684, 23685, 23686, 23687, 23688, 23689, 23690, 23691, 23692, 23693, 23694, 23695, 23696, 23697, 23698, 23699, 23700, 1040, 1042, 1044, 1046, 1048, 1050, 1053, 1055, 1057, 11732, 3105, 1712, 		1704, 1706, 1079, 1127, 6585, 6570, }; //Other ids go in there as well
			player.getInventory().deleteItem(6199, 1);
			for (int i = 0; i < RandomItems.length; i++) 
				player.getInventory().addItem(RandomItems[i], 1);
			player.packets().sendMessage("You've recieved an item from the Mystery Box!");
			return;
		}
		if (itemId == 952) {// spade
			player.resetWalkSteps();

			if (Barrows.digToBrother(player)){
				player.getControllerManager().startController("Barrows");
				return;
			}
			if (ClueScrolls.digSpot(player)){
				return;
			}
			player.setNextAnimation(new Animation(830));
			player.packets().sendMessage("You find nothing.");
			return;
		}
		/**
		 * Author @ fuzenseth
		 */
		switch (item.getId()) {
		case TeleportManager.BANDIT_CAMP_TELEPORT:
			TeleportManager.preformScrollTeleportation(player, 0,0, new Location(3177,2981,0));
			player.getInventory().deleteItem(TeleportManager.BANDIT_CAMP_TELEPORT, 1);
			break;
		case TeleportManager.NARDAH_TELEPORT:
			TeleportManager.preformScrollTeleportation(player, 0,0, new Location(3436,2889,0));
			player.getInventory().deleteItem(item.getId(), 1);
			break;
		case TeleportManager.MISCELLANIA_TELEPORT:
			TeleportManager.preformScrollTeleportation(player,0,0, new Location(3112,2525,3859));
			player.getInventory().deleteItem(item.getId(), 1);
			break;
		case TeleportManager.PHOENIX_LAIR_TELEPORT:
			TeleportManager.preformScrollTeleportation(player,0,0, new Location(3535,5188,0));
			player.getInventory().deleteItem(item.getId(), 1);
			break;
		case TeleportManager.TAI_BWO_WANNAI_TELEPORT:
			TeleportManager.preformScrollTeleportation(player,0,0, new Location(2795,3066,0));
			player.getInventory().deleteItem(item.getId(), 1);
			break;
		case TeleportManager.LUMBER_YARD_TELEPORT:
			TeleportManager.preformScrollTeleportation(player, 0,0,new Location(3307,3490,0));
			player.getInventory().deleteItem(item.getId(), 1);
			break;
		}
		if (HerbCleaning.clean(player, item, slotId))
			return;
		Bone bone = Bone.forId(itemId);
		if (bone != null) {
			Bone.bury(player, slotId);
			return;
		}
		if (Magic.useTabTeleport(player, itemId))
			return;
		if (itemId == AncientEffigies.SATED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.GORGED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.NOURISHED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.STARVED_ANCIENT_EFFIGY)
			player.getMatrixDialogues().startDialogue("AncientEffigiesD",
					itemId);
		else if (itemId == 4155)
			player.getMatrixDialogues().startDialogue("EnchantedGemDialouge");
		else if (itemId == 1856) {
			player.interfaces().sendInterface(275);
			player.packets()
			.sendIComponentText(275, 2, Constants.SERVER_NAME);
			player.packets().sendIComponentText(275, 16,
					"Welcome to " + Constants.SERVER_NAME + ".");
			player.packets().sendIComponentText(275, 17,
					"If want some an item use command ::item id.");
			player.packets().sendIComponentText(275, 18,
					"If you don't have an item list you can find ids");
			player.packets().sendIComponentText(275, 19,
					"at http://itemdb.biz");
			player.packets().sendIComponentText(275, 20,
					"You can change your prayers and spells at home.");
			player.packets().sendIComponentText(275, 21,
					"If you need any help, do ::ticket. (Don't abuse it)");
			player.packets().sendIComponentText(275, 22,
					"at start of your message on public chat.");
			player.packets().sendIComponentText(275, 22,
					"By the way you can compare your ::score with your mates.");
			player.packets().sendIComponentText(275, 23,
					"Oh and ye, don't forget to ::vote and respect rules.");
			player.packets().sendIComponentText(275, 24, "");
			player.packets().sendIComponentText(275, 25,
					"Forums: Coming soon!");
			player.packets().sendIComponentText(275, 26, "");
			player.packets().sendIComponentText(275, 27,
					"Enjoy your time on " + Constants.SERVER_NAME + ".");
			player.packets().sendIComponentText(275, 28,
					"<img=1> Staff Team");
			player.packets().sendIComponentText(275, 29, "");
			player.packets().sendIComponentText(275, 30, "");
			player.packets().sendIComponentText(275, 14,
					"<u>Visit Website</u>");
			for (int i = 31; i < 300; i++)
				player.packets().sendIComponentText(275, i, "");
		} else if (itemId == HunterEquipment.BOX.getId())
			player.getActionManager().setSkill(new Hunter(HunterEquipment.BOX));
		else if (itemId == HunterEquipment.BRID_SNARE.getId())
			player.getActionManager().setSkill(
					new Hunter(HunterEquipment.BRID_SNARE));
	}

	/*
	 * returns the other
	 */
	public static Item contains(int id1, Item item1, Item item2) {
		if (item1.getId() == id1)
			return item2;
		if (item2.getId() == id1)
			return item1;
		return null;
	}

	public static boolean contains(int id1, int id2, Item... items) {
		boolean containsId1 = false;
		boolean containsId2 = false;
		for (Item item : items) {
			if (item.getId() == id1)
				containsId1 = true;
			else if (item.getId() == id2)
				containsId2 = true;
		}
		return containsId1 && containsId2;
	}

	public static void handleItemOnItem(final Player player, InputStream stream) {

		int interfaceId = stream.readIntV1() >> 16;
			int itemUsedId = stream.readUnsignedShort128();
			int fromSlot = stream.readUnsignedShortLE128();
			int interfaceId2 = stream.readIntV2() >> 16;
		int itemUsedWithId = stream.readUnsignedShort128();
		int toSlot = stream.readUnsignedShortLE();
		if ((interfaceId2 == 747 || interfaceId2 == 662)
				&& interfaceId == Inventory.INVENTORY_INTERFACE) {
			if (player.getFamiliar() != null) {
				player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.ITEM) {
					if (player.getFamiliar().hasSpecialOn())
						player.getFamiliar().submitSpecial(toSlot);
				}
			}
			return;
		}
		if (interfaceId == 679 && interfaceId2 == 192 && !player.interfaces().containsInventoryInter()) {
			int hash = stream.readInt();
			int magicId = hash & 0xFFFF;
			MagicOnItem.handleMagic(player, magicId, player.getInventory().getItem(toSlot));
			return;
		}

		if (interfaceId == Inventory.INVENTORY_INTERFACE
				&& interfaceId == interfaceId2
				&& !player.interfaces().containsInventoryInter()) {
			if (toSlot >= 28 || fromSlot >= 28)
				return;
			Item usedWith = player.getInventory().getItem(toSlot);
			Item itemUsed = player.getInventory().getItem(fromSlot);
			if (itemUsed == null || usedWith == null
					|| itemUsed.getId() != itemUsedId
					|| usedWith.getId() != itemUsedWithId)
				return;
			player.stopAll();
			if(!player.getRandomEvent().canUseItemOnItem(itemUsed, usedWith))
				return;
			if (!player.getControllerManager().canUseItemOnItem(itemUsed,
					usedWith))
				return;
			if (Pots.mixPot(player, itemUsed, usedWith, fromSlot, toSlot))
				return;

			Fletch fletch = Fletching.isFletching(usedWith, itemUsed);
			if (fletch != null) {
				player.getMatrixDialogues().startDialogue("FletchingD", fletch);
				return;
			}
			if (ItemPoisoning.handleItemInteract(player, itemUsed, usedWith))
				return;
			int herblore = Herblore.isHerbloreSkill(itemUsed, usedWith);
			if (herblore > -1) {
				player.getMatrixDialogues().startDialogue("HerbloreD",
						herblore, itemUsed, usedWith);
				return;
			}
			if (itemUsedId == 15086 && itemUsedWithId == 15086) {
				DiceGame.rollDice2(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2072));
				return;
			}
			/*        if (itemUsedId == itemUsed.getId() && itemUsedWithId == 10025) {
			 * 
			 * 		if (itemUsedId == 995) {
			 * 		player.sm("You cannot bank coins.");
			 * return;
				        	  if (!(player.magicboxTime < 1)) {
				        		  player.sm("You can bank an item once in every 5 minute.");
				        		  return;
				        	  } else {
				        		  player.magicboxTime += 50;
					        		player.getInventory().deleteItem(10025,1);
					        		player.getInventory().deleteItem(itemUsed.getId(), 1);
					        		player.getBank().addItem(itemUsed.getId(), itemUsed.getAmount(), true);
					        		player.sm("The imp teleports away, taking the item to your bank account.");  
				        	  }
				          }
				          if (itemUsedId == 10025 && itemUsedWithId == itemUsed.getId()) {
				        		player.getInventory().deleteItem(10025,1);
				        		player.getInventory().deleteItem(itemUsed.getId(), 1);
				        		player.getBank().addItem(itemUsed.getId(), itemUsed.getAmount(), true);
				        		player.sm("The imp teleports away, taking the item to your bank account.");
				          }*/
			if (itemUsedId == 985 && itemUsedWithId == 987) {
				player.getCrystalChest().makeKey(player);
			}
			if (itemUsedId == 15088 && itemUsedWithId == 15088) {
				DiceGame.rollDice3(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2074));
				return;
			}
			if (itemUsedId == 15090 && itemUsedWithId == 15090) {
				DiceGame.rollDice4(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2071));
				return;
			}
			if (itemUsedId == 15092 && itemUsedWithId == 15092) {
				DiceGame.rollDice5(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2070));
				return;
			}
			if (itemUsedId == 15094 && itemUsedWithId == 15094) {
				DiceGame.rollDice5(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2073));
				return;
			}
			if(itemUsedId == 15096 && itemUsedWithId == 15096) {
				DiceGame.rollDice7(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2068));
				return;
			}
			if(itemUsedId == 15098 && itemUsedWithId == 15098) {
				DiceGame.rollDice8(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2075));
				return;
			}
			if (itemUsedId == 15100 && itemUsedWithId == 15100) {
				DiceGame.rollDice1(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2069));
				return;
			}
			if (itemUsed.getId() == LeatherCrafting.NEEDLE.getId()
					|| usedWith.getId() == LeatherCrafting.NEEDLE.getId()) {
				if (LeatherCrafting
						.handleItemOnItem(player, itemUsed, usedWith)) {
					return;
				}
			}
			/*	Sets set = ArmourSets.getArmourSet(itemUsedId, itemUsedWithId);
				if (set != null) {
					ArmourSets.exchangeSets(player, set);
					return;
				}*/
			ItemOnItem itemOnItem = ItemOnItem.forId(itemUsedId);
			if (itemOnItem != null) {
				if (itemUsedWithId == itemOnItem.getItem2())
					ItemOnItemHandler.handleItemOnItem(player, itemOnItem, usedWith.getId(), itemUsed.getId());
				return;
			}
			if (Firemaking.isFiremaking(player, itemUsed, usedWith))
				return;
			if (Firelighting.isFiremaking(player, itemUsed, usedWith))
				return;
			else if (contains(1755, Gem.OPAL.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.OPAL);
			else if (contains(1755, Gem.JADE.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.JADE);
			else if (contains(1755, Gem.RED_TOPAZ.getUncut(), itemUsed,
					usedWith))
				GemCutting.cut(player, Gem.RED_TOPAZ);
			else if (contains(1755, Gem.SAPPHIRE.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.SAPPHIRE);
			else if (contains(1755, Gem.EMERALD.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.EMERALD);
			else if (contains(1755, Gem.RUBY.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.RUBY);
			else if (contains(1755, Gem.DIAMOND.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.DIAMOND);
			else if (contains(1755, Gem.DRAGONSTONE.getUncut(), itemUsed,
					usedWith))
				GemCutting.cut(player, Gem.DRAGONSTONE);
			else if (itemUsed.getId() == 21369 && usedWith.getId() == 4151){
				player.getInventory().deleteItem(21369, 1);
				player.getInventory().deleteItem(4151, 1);
				player.getInventory().addItem(21371, 1);
				player.sm("Good job, you have succesfully combined a whip and vine into a vine whip.");
			}
			else if (itemUsed.getId() == 987 && usedWith.getId() == 985 || itemUsed.getId() == 985 && usedWith.getId() == 987) {
				player.getInventory().deleteItem(987,1);
				player.getInventory().deleteItem(985,1);
				player.getInventory().addItem(989, 1);
				player.sendMessage("You attach the keys together, you have gotten a Crystal key!");
			}

			else if (contains(1755, Gem.ONYX.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.ONYX);
			else

				player.packets().sendMessage("Nothing interesting happens.");
			if (Constants.DEVELOPER_MODE)
				Logger.log("ItemHandler", "Used:" + itemUsed.getId()
				+ ", With:" + usedWith.getId());
		} 
	} 

	public static void handleItemOption3(Player player, int slotId, int itemId,
			Item item) {
		long time = Misc.currentTimeMillis();
		if (player.getStopDelay() >= time
				|| player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		if (itemId == 20767 || itemId == 20769 || itemId == 20771)
			SkillCapeCustomizer.startCustomizing(player, itemId);
		else if (Equipment.getItemSlot(itemId) == Equipment.SLOT_AURA)
			player.getAuraManager().sendTimeRemaining(itemId);
		if (itemId == 15707) {
			TeleportManager.DaemonheimTeleport(player, 1, 1, new Location(3450, 3694, 0));
		}  
		if (ItemEmptying.emptyingItem(player, item)) {
			return;
		}
		if (itemId == 20801) {
			player.getMatrixDialogues().startDialogue("WildyHats");
		}		if (ItemEmptying.emptyingItem(player, item)) {
			return;
		}
		if (itemId == 20802) {
			player.getMatrixDialogues().startDialogue("WildyHats");
		}
		if (itemId == 20803) {
			player.getMatrixDialogues().startDialogue("WildyHats");
		}	
		if (itemId == 20804) {
			player.getMatrixDialogues().startDialogue("WildyHats");
		}
		if (itemId == 20805) {
			player.getMatrixDialogues().startDialogue("WildyHats");
		}
		if (itemId == 20806) {
			player.getMatrixDialogues().startDialogue("WildyHats");
		}

		switch (itemId) {
		case 5733:
			RottenPotato.addPotatoAction(player, "open_cm");
			break;
		}

	}

	public static void handleItemOption4(Player player, int slotId, int itemId,
			Item item) {

		if (itemId == 10890) {
			ItemEffects.recitePrayer(player);
		}
		if (ItemEmptying.emptyingItem(player, item)) {
			return;
		}
		switch (itemId) {
		case 5733:
			RottenPotato.getSingleton().addAction(player, "open_cm");
			break;
		case 20801:
		case 20802:
		case 20803:
		case 20804:
		case 20805:
		case 20806:
			player.getMatrixDialogues().startDialogue("WildyHats");
			break;
		}
		System.out.println("Option 4");
		if (itemId == 15707) {
			TeleportManager.DaemonheimTeleport(player, 1, 1, new Location(3450, 3694, 0));
		}  
	}

	public static void handleItemOption5(Player player, int slotId, int itemId,
			Item item) {
		if (ItemEmptying.emptyingItem(player, item)) {
			return;
		}
		System.out.println("Option 5");
	}

	public static void handleItemOption6(Player player, int slotId, int itemId,
			Item item) {
		long time = Misc.currentTimeMillis();
		if (player.getStopDelay() >= time
				|| player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		Pouches pouches = Pouches.forId(itemId);
		if (ItemEmptying.emptyingItem(player, item)) {
			return;
		}
		if (itemId == 15707) {
			TeleportManager.DaemonheimTeleport(player, 1, 1, new Location(3450, 3694, 0));
		}  
		if (pouches != null) {
			Summoning.spawnFamiliar(player, pouches);
		}
		else if (itemId == 1438)
			Runecrafting.locate(player, 3127, 3405);
		else if (itemId == 1440)
			Runecrafting.locate(player, 3306, 3474);
		else if (itemId == 1442)
			Runecrafting.locate(player, 3313, 3255);
		else if (itemId == 1444)
			Runecrafting.locate(player, 3185, 3165);
		else if (itemId == 1446)
			Runecrafting.locate(player, 3053, 3445);
		else if (itemId == 1448)
			Runecrafting.locate(player, 2982, 3514);
		else if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354
				&& itemId <= 10362)
			player.getMatrixDialogues().startDialogue("Transportation",
					"Edgeville", new Location(3087, 3496, 0), "Karamja",
					new Location(2918, 3176, 0), "Draynor Village",
					new Location(3105, 3251, 0), "Al Kharid",
					new Location(3293, 3163, 0), itemId);
		else if (itemId >= 2552 && itemId <= 2566)
			player.getMatrixDialogues().startDialogue("Transportation",
					"Duel Arena", new Location(3315, 3234, 0),
					"Castle Wars", new Location(2442, 3088, 0),
					"Mobilising Armies", new Location(2413, 2848, 0),
					"Fist of Guthix", new Location(1679, 5599, 0), itemId);
		else if (itemId == 1704 || itemId == 10352)
			player.packets()
			.sendMessage(
					"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
		else if (itemId >= 3853 && itemId <= 3867)
			player.getMatrixDialogues().startDialogue("Transportation",
					"Burthrope Games Room", new Location(2880, 3559, 0),
					"Barbarian Outpost", new Location(2519, 3571, 0),
					"Gamers' Grotto", new Location(2970, 9679, 0),
					"Corporeal Beast", new Location(2886, 4377, 0), itemId);
	}

	public static void dropItem(Player player, int slotId, int itemId,
			Item item) {
		long time = System.currentTimeMillis();
		if (player.getStopDelay() >= time
				|| player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		if(!player.getRandomEvent().canDropItem(item))
			return;
		player.stopAll(false);
		if (item.defs().isDestroyItem()) {
			player.getMatrixDialogues().startDialogue("DestroyItemOption",
				new Object[] { Integer.valueOf(slotId), item });
			return;
		}
		if (player.getCharges().degradeCompletly(item)) {
			return;
		}
		
		// Boss Pets
		if(itemId >= 22746 && itemId <= 22757) { // Change this value and it will auto update and always work
			String name = ItemDefinition.get(itemId).name.toLowerCase();
			for(int i = 0; i < Misc.getNPCsSize(); i++) {
				String nName = NPCDefinition.get(i).name.toLowerCase();
				if(name.equals(nName)) {
					if(BossPets.createBossPet(player, i)) {
						player.getInventory().deleteItem(slotId, item);
					}
				}
			}
			return;
		}

		if (IsPet(itemId)){
			for(int i = 0; i < itempets.length; i++){ 
				if (itemId == itempets[i]){
					if (player.getRealPet() != null) {
						player.sendMessage("You already have a pet spawned, please dismiss it to spawn another.");
						return;
					}
					player.setPetId(itempets[i]);
					new Pets(npcpets[i], player, new Location(player.getX() + 1,
							player.getY() + 1, player.getZ()), 0, false);
					player.getInventory().deleteItem(slotId, item);
				}
			}
		} else {
			int x = player.getX(), y = player.getY();
			Logs.write(player.getUsername()+" dropped an item: ["+
						item.getId()+", "+item.getName()+" - x"+item.getAmount()+"] at location: ["+
							x+", "+y+", "+player.getZ()+"]", "DroppedItems", player, true);
			player.getInventory().deleteItem(slotId, item);
			Game.addGroundItem(item, new Location(player), player, false, 180, true);
			player.packets().sendSound(2739, 0, 1);
		}
	}
	public static int[] itempets = {22973, 12196, 21512, 22992, 22993, 22994, 22995, 12469, 12470, 12471, 12472, 12473, 12474, 12475, 12476, 12481, 12482, 12484, 12485, 12487, 12488, 12489, 12490, 12492, 12493, 12496, 12497, 12498, 12499, 12500, 12501, 12502, 12503, 12505, 12506, 12507, 12508, 12509, 12510, 12511, 12512, 12513, 12514, 12515, 12516, 12517, 12518, 12519, 12520, 12521, 12523, 14627, 14626, 7581, 7582, 7583, 7584, 7585};
	public static int[] npcpets  = {2267,  6969,  3604,  14832, 14768, 14769, 14770,  6900,  6901,  6902,  6903,  6904,  6905,  6906,  6907,  6908,  6909,  6911,  6912,  6914,  6915,  6916,  6919,  6920,  6923,  6942,  6943,  6945,  6946,  6947,  6948,  6949,  6950,  6951,  6952,  6953,  6954,  6955,  6956,  6957,  6958,  6959,  6960,  6961,  6962,  6963,  6964,  6965,  6966,  6967,  6968,  8550,  8551, 3503, 3504, 3505, 3506, 3507};

	public static boolean IsPet(int j){
		for(int i : itempets){
			if (i != j){
				continue;
			}
			return true;

		}
		return false;
	}
}