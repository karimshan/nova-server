package org.nova.network.decoders.packets.handlers.items;

import org.nova.Constants;
import org.nova.cache.definition.ObjectDefinition;
import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.CoordsEvent;
import org.nova.game.player.Inventory;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Cooking;
import org.nova.game.player.actions.Farming;
import org.nova.game.player.actions.PlayerCombat;
import org.nova.game.player.actions.Cooking.Cookables;
import org.nova.game.player.actions.Farming.Allotments;
import org.nova.game.player.actions.Farming.Flowers;
import org.nova.game.player.actions.Farming.Herbs;
import org.nova.game.player.actions.Smithing.ForgingBar;
import org.nova.game.player.actions.Smithing.ForgingInterface;
import org.nova.game.player.content.BonesOnAltar;
import org.nova.game.player.content.Runecrafting;
import org.nova.game.player.content.BonesOnAltar.Bones;
import org.nova.game.player.content.itemactions.FountainOfHeroes;
import org.nova.game.player.content.itemactions.SpecialItems;
import org.nova.game.player.controlers.WGuildControler;
import org.nova.network.decoders.packets.handlers.objects.Passages;
import org.nova.network.stream.InputStream;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Karimshan Nawaz
 *
 */
public class ItemOnObject {
	
	/**
	 * 
	 * @param player
	 * @param stream
	 */
	public static void process(final Player player, InputStream stream) {
		if(player.cantInteract())
			return;
		@SuppressWarnings("unused")
		final int forceRun = stream.readUnsignedByteC();
		final int y = stream.readUnsignedShortLE();
		final int itemSlot = stream.readUnsignedShortLE();
		final int interfaceHash = stream.readIntLE();
		final int interfaceId = interfaceHash >> 16;
		final int itemId = stream.readUnsignedShortLE128();
		final int x = stream.readUnsignedShortLE();
		final int id = stream.readInt();
		final Location tile = new Location(x, y, player.getZ());
		int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		GlobalObject mapObject = Game.getRegion(regionId).getObject(id, tile);
		if (mapObject == null)
			mapObject = Game.getRegion(regionId).getSpawnedObject(tile);
		if(mapObject == null)
			player.sm("This object is null. "+id+" - ["+x+", "+y+", "+player.getZ()+"]");
		final GlobalObject object = !player.isAtDynamicRegion() ? mapObject
			: new GlobalObject(id, mapObject.getType(),
				mapObject.getRotation(), x, y, player.getZ());
		final Item item = player.getInventory().getItem(itemSlot);
		if (player.isDead()
				|| Misc.getInterfaceDefinitionsSize() <= interfaceId)
			return;
		if (player.getStopDelay() > Misc.currentTimeMillis())
			return;
		if (!player.interfaces().containsInterface(interfaceId))
			return;
		if (item == null || item.getId() != itemId)
			return;
		player.stopAll(false);
		final ObjectDefinition objectDef = object.defs();
		player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
			@Override
			public void run() {
				player.faceLocation(new Location(object.getCoordFaceX(
						objectDef.getSizeX(), objectDef.getSizeY(),
						object.getRotation()), object.getCoordFaceY(
						objectDef.getSizeX(), objectDef.getSizeY(),
						object.getRotation()), object.getZ()));
				if(Constants.DEVELOPER_MODE)
					System.out.println("Item: ["+item.getId()+", "+item.getName()+" - x"+item.getAmount()+"] used on object: "+object.getInformation());
				if(player.isOwner())
					player.sm("Item: ["+item.getId()+" - "+item.getName()+"] used on object: ["+
						object.getId()+" - "+object.getName()+"]");
				if(!player.getRandomEvent().canUseItemOnObject(item, object))
					return;
				if (interfaceId == Inventory.INVENTORY_INTERFACE) { // inventory
					if (object.defs().name.equals("Anvil")) {
						player.getTemporaryAttributtes()
								.put("itemUsed", itemId);
						ForgingBar bar = ForgingBar.forId(itemId);
						if (bar != null)
							ForgingInterface.sendSmithingInterface(player, bar);
					}
					if (FountainOfHeroes.handleFountain(player, item, object))
						return;
					else if (itemId == 1079 || itemId == 1127 || itemId == 1163
							|| itemId == 1153 || itemId == 1115
							|| itemId == 1067 || itemId == 1155
							|| itemId == 1117 || itemId == 1075
							|| itemId == 1157 || itemId == 1119
							|| itemId == 1069 || itemId == 1159
							|| itemId == 1121 || itemId == 1071
							|| itemId == 1161 || itemId == 1123
							|| itemId == 1073 && object.getId() == 15621) {
						WGuildControler
								.handleItemOnObject(player, object, item);
					} else if (item.defs().getName().equals("Empty ectophial")
							&& object.getId() == 5282) {
						player.getEctophial().refillEctophial(player);
					}

					else if (itemId == 2 && object.getId() == 6) {
						player.getDwarfcannon().loadDwarfCannon(object);
					} else if (object.defs().name.toLowerCase().contains(
							"altar")) {
						Bones bone = BonesOnAltar.isGood(item);
						if (bone != null) {
							player.getMatrixDialogues().startDialogue(
									"PrayerD", bone, object);
							return;
						}
					}

					else if (itemId == 2 && object.getId() == 29358) {
						player.getDwarfcannon().loadDwarfCannon(object);
					} else if (itemId == 1675 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 7)) {
							player.sendMessage("You need a magic level of 7 to charge this amulet.");
							return;
						}
						if (player.getInventory().containsItem(1675, 1))
							player.getInventory().deleteItem(1675, 1);
						player.getInventory().addItem(1727, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Sapphire Amulet is now fully charged.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 1677 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 27)) {
							player.sendMessage("You need a magic level of 27 to charge this amulet.");
							return;
						}
						if (player.getInventory().containsItem(1677, 1))
							player.getInventory().deleteItem(1677, 1);
						player.getInventory().addItem(1729, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Emerald Amulet is now fully charged.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 1679 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 49)) {
							player.sendMessage("You need a magic level of 49 to charge this amulet.");
							return;
						}
						if (player.getInventory().containsItem(1679, 1))
							player.getInventory().deleteItem(1679, 1);
						player.getInventory().addItem(1725, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Ruby Amulet is now fully charged.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 1681 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 57)) {
							player.sendMessage("You need a magic level of 57 to charge this amulet.");
							return;
						}
						if (player.getInventory().containsItem(1681, 1))
							player.getInventory().deleteItem(1681, 1);
						player.getInventory().addItem(1731, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Diamond Amulet is now fully charged.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 1704 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 68)) {
							player.sendMessage("You need a magic level of 68 to charge this amulet.");
							return;
						}
						if (player.getInventory().containsItem(1704, 1))
							player.getInventory().deleteItem(1704, 1);
						player.getInventory().addItem(1712, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Amulet of Glory is now fully charged.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 6585 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 99)) {
							player.sendMessage("You need a magic level of 99 to charge this amulet.");
							return;
						}
						if (player.getInventory().containsItem(6585, 1))
							player.getInventory().deleteItem(6585, 1);
						player.getInventory().addItem(19335, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Amulet of Fury has taken on a new appearence.");
						player.setNextGraphics(new Graphics(2742));

					} else if (itemId == 1637 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 7)) {
							player.sendMessage("You need a magic level of 7 to enchant this ring.");
							return;
						}
						if (player.getInventory().containsItem(1637, 1))
							player.getInventory().deleteItem(1637, 1);
						player.getInventory().addItem(2550, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Sapphire Ring has now been enchanted.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 1639 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 27)) {
							player.sendMessage("You need a magic level of 27 to enchant this ring.");
							return;
						}
						if (player.getInventory().containsItem(1639, 1))
							player.getInventory().deleteItem(1639, 1);
						player.getInventory().addItem(2552, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Emerald Ring has now been enchanted.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 1641 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 49)) {
							player.sendMessage("You need a magic level of 49 to enchant this ring.");
							return;
						}
						if (player.getInventory().containsItem(1641, 1))
							player.getInventory().deleteItem(1641, 1);
						player.getInventory().addItem(2568, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Ruby Ring has now been enchanted.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 1643 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 57)) {
							player.sendMessage("You need a magic level of 57 to enchant this ring.");
							return;
						}
						if (player.getInventory().containsItem(1643, 1))
							player.getInventory().deleteItem(1643, 1);
						player.getInventory().addItem(2570, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Diamond Ring has now been enchanted.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 1645 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 68)) {
							player.sendMessage("You need a magic level of 68 to enchant this ring.");
							return;
						}
						if (player.getInventory().containsItem(1645, 1))
							player.getInventory().deleteItem(1645, 1);
						player.getInventory().addItem(2572, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Dragonstone Ring has now been enchanted.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 6575 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 85)) {
							player.sendMessage("You need a magic level of 85 to enchant this ring.");
							return;
						}
						if (player.getInventory().containsItem(6575, 1))
							player.getInventory().deleteItem(6575, 1);
						player.getInventory().addItem(15017, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Onyx Ring has now been enchanted.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 6731 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 95)) {
							player.sendMessage("You need a magic level of 95 to enchant this ring.");
							return;
						}
						if (player.getInventory().containsItem(6731, 1))
							player.getInventory().deleteItem(6731, 1);
						player.getInventory().addItem(15018, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Seers Ring has now been enchanted.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 6733 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 95)) {
							player.sendMessage("You need a magic level of 95 to enchant this ring.");
							return;
						}
						if (player.getInventory().containsItem(6733, 1))
							player.getInventory().deleteItem(6733, 1);
						player.getInventory().addItem(15019, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Archers Ring has now been enchanted.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 6735 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 95)) {
							player.sendMessage("You need a magic level of 95 to enchant this ring.");
							return;
						}
						if (player.getInventory().containsItem(6735, 1))
							player.getInventory().deleteItem(6735, 1);
						player.getInventory().addItem(15020, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Warriors Ring has now been enchanted.");
						player.setNextGraphics(new Graphics(2742));
					} else if (itemId == 6737 && object.getId() == 36695) {
						if (!player.getSkills().hasRequiriments(Skills.MAGIC, 95)) {
							player.sendMessage("You need a magic level of 95 to enchant this ring.");
							return;
						}
						if (player.getInventory().containsItem(6737, 1))
							player.getInventory().deleteItem(6737, 1);
						player.getInventory().addItem(15220, 1);
						player.setNextAnimation(new Animation(899));
						player.sm("<col=FF0000>Your Berserkers Ring has now been enchanted.");
						player.setNextGraphics(new Graphics(2742));
					} else if (player.getFillables().fillBuckets(item, object))
						return;
					else if (SpecialItems.handleItemOnObject(player, itemId,
							object))
						return;
					else if (itemId == 2 && object.getId() == 29403) {
						player.getDwarfcannon().loadDwarfCannon(object);
					} else if (Herbs.herbs.containsKey(itemId)
							&& player.getFaladorHerbPatchRaked()) {
						Farming.plantHerbSeed(player, itemId, object.getId());
					} else if (Allotments.allotments.containsKey(itemId)
							&& player.getFaladorNorthAllotmentPatchRaked()) {
						Farming.plantAllotmentSeed(player, itemId,
								object.getId());
					} else if (Allotments.allotments.containsKey(itemId)
							&& player.getFaladorSouthAllotmentPatchRaked()) {
						Farming.plantAllotmentSeed(player, itemId,
								object.getId());
					} else if (Flowers.flowers.containsKey(itemId)
							&& player.getFaladorFlowerPatchRaked()) {
						Farming.plantFlowerSeed(player, itemId, object.getId());
					} else if (itemId == 1438 && object.getId() == 2452) {
						Runecrafting.enterAirAltar(player);
					} else if (itemId == 1440 && object.getId() == 2455) {
						Runecrafting.enterEarthAltar(player);
					} else if (itemId == 1442 && object.getId() == 2456) {
						Runecrafting.enterFireAltar(player);
					} else if (itemId == 1444 && object.getId() == 2454) {
						Runecrafting.enterWaterAltar(player);
					} else if (itemId == 1446 && object.getId() == 2457) {
						Runecrafting.enterBodyAltar(player);
					} else if (itemId == 1448 && object.getId() == 2453) {
						Runecrafting.enterMindAltar(player);
					} else if (itemId == 536
							&& object.defs().name.equals("Altar")) {
						player.packets().sendMessage("You pray at the gods");
						player.getInventory().deleteItem(new Item(536, 1));
						player.getSkills().addXp(Skills.PRAYER, 650);
						player.packets().sendSound(2738, 0, 1);
						player.setNextAnimation(new Animation(896));
						player.setNextGraphics(new Graphics(624));
						player.getInventory().refresh();
					} else if (object.getId() == 733 || object.getId() == 64729) {
						player.setNextAnimation(new Animation(PlayerCombat
								.getWeaponAttackEmote(-1, 0)));
						Passages.slashWeb(player, object);
					} else if (objectDef.name.toLowerCase().contains("range")
							|| objectDef.name.toLowerCase().contains("stove")
							|| id == 2732) {
						Cookables cook = Cooking.isCookingSkill(item);
						if (cook != null) {
							player.getMatrixDialogues().startDialogue(
									"CookingD", cook, object);
						}
					}
				}
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

}
