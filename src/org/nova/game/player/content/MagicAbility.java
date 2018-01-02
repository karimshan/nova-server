package org.nova.game.player.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.Region;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.network.stream.InputStream;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class MagicAbility {

	private static final int CHARGE = 83;
	
	private static final int BONES_TO_PEACHES = 33;
	
	public static final int GROUP_VENGEANCE = 74;
	
	private static final int DISRUPTION_SHIELD = -1;
	
	private static final int NPC_CONTACT = 26;
	
	protected static final int AIR_RUNE = 556, WATER_RUNE = 555,
			EARTH_RUNE = 557, FIRE_RUNE = 554, MIND_RUNE = 558,
			NATURE_RUNE = 561, CHAOS_RUNE = 562, DEATH_RUNE = 560,
			BLOOD_RUNE = 565, SOUL_RUNE = 566, ASTRAL_RUNE = 9075,
			LAW_RUNE = 563, STEAM_RUNE = 4694, MIST_RUNE = 4695,
			DUST_RUNE = 4696, SMOKE_RUNE = 4697, MUD_RUNE = 4698,
			LAVA_RUNE = 4699, ARMADYL_RUNE = 21773;
	
	public static Entity[] getMultiAttackTargets(Player player, Player target, int maxDistance, int maxAmtTargets) {
		InputStream stream = new InputStream(0);
		int playerIndex = stream.readUnsignedShortLE();
		Player p2 = Game.getPlayers().get(playerIndex);
		p2 = target;
		List<Entity> possibleTargets = new ArrayList<Entity>();
		possibleTargets.add(target);
			y: for (int regionId : target.getMapRegionsIds()) {
				Region region = Game.getRegion(regionId);
				
					List<Integer> playerIndexes = region.getPlayerIndexes();
					if(playerIndexes == null)
						continue;
					for (int playerIndex1 : playerIndexes) {
						Player p21 = Game.getPlayers().get(playerIndex1);
						if (p21 == null || p21 == player || p21 == target
								|| p21.isDead() 
								|| !p21.withinDistance(target, maxDistance)
								|| !player.getControllerManager().canHit(p21))
							continue;
						possibleTargets.add(p21);
						if(possibleTargets.size() == maxAmtTargets)
							break y;
				}
		}
		return possibleTargets.toArray(new Entity[possibleTargets.size()]);
	}
	/**
	 * Gets the actual targets.
	 * @param player
	 * @param target
	 * @return
	 */
	public static Entity[] getMultiAttackTargets(Player player, Player target) {
		return getMultiAttackTargets(player, target, 1, 9);
	}
	 /**
	  * 
	  * @param player
	  * @param interfaceId
	  * @param componentId
	  * @return
	  */
	public static final boolean handleInterfaceInteractions(Player player,  int componentId) {

			switch (componentId) {
			case 13:
			case 41:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SlayerD", 8273);
				break;
			case 27:
			case 42:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SlayerD", 7780);
				break;
			case 18:
			case 44:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SlayerD", 8274);
				break;
			case 23:
			case 45:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SlayerD", 8275);
				break;
			case 28:
			case 46:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SlayerD", 1597);
				break;
			case 29:
				player.closeInterfaces();
				player.getMatrixDialogues().startDialogue("SlayerD", 1598);
			break;
			/**
			 * This is the random contact options.
			 */
			case 35:
			player.closeInterfaces();
		Random randomNPC = new Random();
		switch (randomNPC.nextInt(5)) {
		case 1:
			player.getMatrixDialogues().startDialogue("SlayerD", 1598);
			break;
		case 2:
			player.getMatrixDialogues().startDialogue("SlayerD", 1597);
			break;
		case 3:
			player.getMatrixDialogues().startDialogue("SlayerD", 8275);
			break;
		case 4:
			player.getMatrixDialogues().startDialogue("SlayerD", 8274);
			break;
		case 5:
			player.getMatrixDialogues().startDialogue("SlayerD", 8273);
			break;
		}
				break;
			case 59:
				player.closeInterfaces();
				break;
				
			default:
				player.sendMessage("You cannot contact this NPC.");
			}
			return false;
	}
	
	/**
	 * We handle the magic abilities.
	 * @param player
	 * @param componentId
	 * @return
	 */
	public static final boolean handleMagicAbility(Player player, int componentId) {
		
		switch (componentId) {
		
		case NPC_CONTACT:
			if (!(player.getSkills().getLevel(Skills.MAGIC) >= 66)) {
				player.sendMessage("You must have magic level of 67 to use this spell.");
				return false;
			}
			long currentTime = Misc.currentTimeMillis();
			if (player.getAttackedByDelay() + 10000 > currentTime) {
				player.packets()
						.sendMessage(
								"You can't use NPC Contact until 10 seconds after the end of combat.");
				return false;
			}
			if (!checkRunes(player, true, ASTRAL_RUNE, 1,  564, 1, AIR_RUNE, 2))
				return false;
			MagicAbility.useNPCContact(player);
			return true;
			
		case DISRUPTION_SHIELD:
			if (!(player.getSkills().getLevel(Skills.MAGIC) >= 89)) {
				player.sendMessage("You must have magic level of 90 to use this spell.");
				return false;
			
			}
			return true;
		
		case GROUP_VENGEANCE:
			Player p2 = Game.getPlayers().get(player.getIndex());
			Player target = p2;
			if (player.isVeangeanceOther()) {
				player.packets().sendMessage("Players may only cast vengeance once every 30 seconds.");
				return false;
			}
			if (!(player.getSkills().getLevel(Skills.MAGIC) >= 94)) {
			player.sendMessage("You must have magic level of 95 to use this spell.");
			return false;
		}		
			if (!p2.isAcceptAid()) {
			player.sendMessage("You must have accept aid on to use this spell.");
				return false;
			}
			
			if (!checkRunes(player, true, ASTRAL_RUNE, 4,  DEATH_RUNE,3, EARTH_RUNE, 11))
				return false;
			MagicAbility.getMultiAttackTargets(player, target, 4, 5);
			MagicAbility.castGroupVengeance(player, target, MagicAbility.getMultiAttackTargets(player, target));
			return true;
			
		case CHARGE:
			if (!(player.getSkills().getLevel(6) >= 79)) {
				player.sendMessage("Your magic level is not enough to cast this spell.");
				return false;
			}
			if (!containsRequirements(player, 0)) {
				player.sendMessage("You don't have enough runes to cast this spell.");
				return false;
			}
			if (player.isCharge()) {
			player.sendMessage("You have already activated the charge spell.");
				return false;
			}
			player.getInventory().deleteItem(FIRE_RUNE,3);
			player.getInventory().deleteItem(BLOOD_RUNE,3);
			player.getInventory().deleteItem(AIR_RUNE,3);
			preformCharge(player);
			return true;
			
	case BONES_TO_PEACHES:
		Item item = new Item(526);
			if (!containsRequirements(player, BONES_TO_PEACHES)) {
				player.sendMessage("You don't have enough runes to cast this spell.");
				return false;
			}
			if (!player.getInventory().containsItem(526, item.getId())) {
				player.sendMessage("You don't have any BONES in your inventory.");
				return false;
			}
			preformBonestoPeaches(player, item);
			return true;
			
		}
		return false;
	}
	
	private static void useNPCContact(final Player player) {
		player.sendMessage("Select a NPC that you wish to contact.");
				WorldTasksManager.schedule(new WorldTask() {
					int loop;
					@Override
					public void run() {
						switch (loop) {
						case 0:
							player.setNextAnimation(new Animation(4413));
							player.setNextGraphics(new Graphics(728, 0, 100));
							player.getSkills().addSkillXpRefresh(Skills.MAGIC, 250 + 60);
							break;
						case 3:
							player.interfaces().sendInterface(429);
							stop();
							break;
							
						}
						loop++;
						}
					}, 0, 1);
	}
	/**
	 * Preforms the stats spy lunar spell.
	 */
	public static final void preformStatsSpy(final Player player, final Player target) {
		for (int i = 0; i < 107; i++) {
			player.packets().sendIComponentText(523, i, "");
		}
		player.packets().sendIComponentText(523, 103, "Player: "+target.getDisplayName()+"");
		player.packets().sendIComponentText(523, 1, ""+target.getSkills().getLevel(Skills.ATTACK)+"/");
		player.packets().sendIComponentText(523, 2, ""+target.getSkills().getLevel(Skills.ATTACK)+"");
		player.packets().sendIComponentText(523, 9, ""+target.getSkills().getLevel(Skills.MINING)+"/");
		player.packets().sendIComponentText(523, 11,""+target.getSkills().getLevel(Skills.MINING)+"");
		player.packets().sendIComponentText(523, 17, ""+target.getSkills().getLevel(Skills.AGILITY)+"/");
		player.packets().sendIComponentText(523, 19, ""+target.getSkills().getLevel(Skills.AGILITY)+"");
		player.packets().sendIComponentText(523, 25, ""+target.getSkills().getLevel(Skills.DEFENCE)+"/");
		player.packets().sendIComponentText(523, 27, ""+target.getSkills().getLevel(Skills.DEFENCE)+"");
		player.packets().sendIComponentText(523, 33, ""+target.getSkills().getLevel(Skills.FISHING)+"/");
		player.packets().sendIComponentText(523, 35, ""+target.getSkills().getLevel(Skills.FISHING)+"");
		player.packets().sendIComponentText(523, 41, ""+target.getSkills().getLevel(Skills.THIEVING)+"/");
		player.packets().sendIComponentText(523, 43, ""+target.getSkills().getLevel(Skills.THIEVING)+"");
		player.packets().sendIComponentText(523, 49, ""+target.getSkills().getLevel(Skills.PRAYER)+"/");
		player.packets().sendIComponentText(523, 51, ""+target.getSkills().getLevel(Skills.PRAYER)+"");
		player.packets().sendIComponentText(523, 57, ""+target.getSkills().getLevel(Skills.FIREMAKING)+"/");
		player.packets().sendIComponentText(523, 59, ""+target.getSkills().getLevel(Skills.FIREMAKING)+"");
		player.packets().sendIComponentText(523, 65, ""+target.getSkills().getLevel(Skills.FLETCHING)+"/");
		player.packets().sendIComponentText(523, 67, ""+target.getSkills().getLevel(Skills.FLETCHING)+"");
		player.packets().sendIComponentText(523, 73, ""+target.getSkills().getLevel(Skills.RUNECRAFTING)+"/");
		player.packets().sendIComponentText(523, 75, ""+target.getSkills().getLevel(Skills.RUNECRAFTING)+"");
		player.packets().sendIComponentText(523, 81, ""+target.getSkills().getLevel(Skills.FARMING)+"/");
		player.packets().sendIComponentText(523, 83, ""+target.getSkills().getLevel(Skills.FARMING)+"");
		player.packets().sendIComponentText(523, 89, ""+target.getSkills().getLevel(Skills.HUNTER)+"/");
		player.packets().sendIComponentText(523, 92, ""+target.getSkills().getLevel(Skills.HUNTER)+"");
		player.packets().sendIComponentText(523, 97, ""+target.getSkills().getLevel(Skills.DUNGEONEERING)+"/");
		player.packets().sendIComponentText(523, 99, ""+target.getSkills().getLevel(Skills.DUNGEONEERING)+"");

		player.packets().sendIComponentText(523, 5, ""+target.getSkills().getLevel(Skills.HITPOINTS)+"/");
		player.packets().sendIComponentText(523, 6, ""+target.getSkills().getLevel(Skills.HITPOINTS)+"");
		player.packets().sendIComponentText(523, 13, ""+target.getSkills().getLevel(Skills.STRENGTH)+"/");
		player.packets().sendIComponentText(523, 14, ""+target.getSkills().getLevel(Skills.STRENGTH)+"");
		player.packets().sendIComponentText(523, 21, ""+target.getSkills().getLevel(Skills.SMITHING)+"/");
		player.packets().sendIComponentText(523, 22, ""+target.getSkills().getLevel(Skills.SMITHING)+"");
		player.packets().sendIComponentText(523, 29, ""+target.getSkills().getLevel(Skills.HERBLORE)+"/");
		player.packets().sendIComponentText(523, 30, ""+target.getSkills().getLevel(Skills.HERBLORE)+"");
		player.packets().sendIComponentText(523, 37, ""+target.getSkills().getLevel(Skills.RANGE)+"/");
		player.packets().sendIComponentText(523, 38, ""+target.getSkills().getLevel(Skills.RANGE)+"");
		player.packets().sendIComponentText(523, 45, ""+target.getSkills().getLevel(Skills.COOKING)+"/");
		player.packets().sendIComponentText(523, 46, ""+target.getSkills().getLevel(Skills.COOKING)+"");
		player.packets().sendIComponentText(523, 53, ""+target.getSkills().getLevel(Skills.CRAFTING)+"/");
		player.packets().sendIComponentText(523, 54, ""+target.getSkills().getLevel(Skills.CRAFTING)+"");
		player.packets().sendIComponentText(523, 61, ""+target.getSkills().getLevel(Skills.MAGIC)+"/");
		player.packets().sendIComponentText(523, 62, ""+target.getSkills().getLevel(Skills.MAGIC)+"");
		player.packets().sendIComponentText(523, 69, ""+target.getSkills().getLevel(Skills.WOODCUTTING)+"/");
		player.packets().sendIComponentText(523, 70, ""+target.getSkills().getLevel(Skills.WOODCUTTING)+"");
		player.packets().sendIComponentText(523, 77, ""+target.getSkills().getLevel(Skills.SLAYER)+"/");
		player.packets().sendIComponentText(523, 78, ""+target.getSkills().getLevel(Skills.SLAYER)+"");
		player.packets().sendIComponentText(523, 85, ""+target.getSkills().getLevel(Skills.CONSTRUCTION)+"/");
		player.packets().sendIComponentText(523, 86, ""+target.getSkills().getLevel(Skills.CONSTRUCTION)+"");
		player.packets().sendIComponentText(523, 93, ""+target.getSkills().getLevel(Skills.SUMMONING)+"/");
		player.packets().sendIComponentText(523, 94, ""+target.getSkills().getLevel(Skills.SUMMONING)+"");
		player.packets().sendIComponentText(523, 106, "Total level: "+Skills.getTotalLevel(target)+"");
		player.interfaces().sendTab(player.interfaces().fullScreen() ? 97 : 211, 523);
		
	}
	
	/**
	 * Casts the group vengeance spell.
	 * @param player
	 * @param target
	 * @param multiAttackTargets
	 */
	private static void castGroupVengeance(final Player player, final Player target, Entity[] multiAttackTargets) {
		for (final Player nearbyPlayers : Game.getPlayers()) {
			nearbyPlayers.setCastVeng(true);
			player.addVengeanceOther(30);
			
			if (player.withinDistance(nearbyPlayers, 2)) {
				WorldTasksManager.schedule(new WorldTask() {
					int loop;
					@Override
					public void run() {
						switch (loop) {
						case 0:
							player.setNextAnimation(new Animation(4410));
							nearbyPlayers.setNextGraphics(new Graphics(725, 0, 100));
							player.getSkills().addSkillXpRefresh(Skills.MAGIC, 250 + 140);
							break;
						case 1:
							nearbyPlayers.sendMessage(""+player.getDisplayName()+" has casted Group vengeance spell. You have received a vengenace.");
							stop();
							break;
							
						}
						loop++;
						}
					}, 0, 1);
			
			}
		}
	}
	/**
	 * Preforms the tele grab spell.
	 * @param player
	 * @param item
	 */
	public static final void preformTeleGrab(Player player, Item item) {
		
	}
	
	
	public static final void preformStatSpy(Player player, Player target) {
		player.packets().sendIComponentText(523, 104, ""+target.getDisplayName());
		player.packets().sendIComponentText(523, 106, ""+target.getSkills().getCombatLevelWithSummoning());
	}
	/**
	 * Preforms the monster examine lunar spell.
	 * @param player
	 * @param npc
	 */
	public static final void preformMonsterExamine(final Player player, NPC incomingNPC) {
	incomingNPC = Game.getNPCs().get(incomingNPC.getIndex());
	player.packets().sendIComponentText(522, 0, "Monster name: " + incomingNPC.getName() + "");
	player.packets().sendIComponentText(522, 1, "Combat level: " + incomingNPC.getCombatLevel() + "");
	player.packets().sendIComponentText(522, 1, "Life points: " + incomingNPC.getHitpoints() + "");
	player.packets().sendIComponentText(522, 1, "Creature's max hit: " + incomingNPC.getMaxHit() + "");
}
	
	public static final void preformHealOther(final Player player, final Player target, final int healingAmount) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				switch (loop) {
				case 0:
					player.setNextFaceEntity(target);
					player.setNextAnimation(new Animation(4411));
					target.setNextGraphics(new Graphics(734, 0, 100));
					player.getSkills().addSkillXpRefresh(Skills.MAGIC, 250 * 0.2);
					player.applyHit(new Hit(player, player.getHitpoints() / 2, HitLook.REGULAR_DAMAGE));
					break;
				case 1:
					player.sendMessage("You have healed " + target.getDisplayName() + ".");
					target.sendMessage("You have been healed.");
					player.setNextFaceEntity(null);
					stop();
					break;
				}
				loop++;
				}
			}, 0, 1);
	}
	/**
	 * Cures the other player who's diseased.
	 */
	public static final void cureOther(final Player player, final Player target) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				switch (loop) {
				case 0:
					player.setNextFaceEntity(target);
					player.setNextAnimation(new Animation(4411));
					target.setNextGraphics(new Graphics(734, 0, 100));
					player.getSkills().addSkillXpRefresh(Skills.MAGIC, 250 / 1.9541);
					break;
				case 1:
					player.sendMessage("You have healed " + target.getDisplayName() + " from diseases.");
					target.sendMessage("You have been healed by poison and other diseases.");
					player.setNextFaceEntity(null);
					stop();
					break;
				}
				loop++;
				}
			}, 0, 1);
	}
	/**
	 * Requests a vengance to cast to target.
	 * @param player
	 * @param target
	 * @param active
	 */
	public static final void requestVengeanceOther(final Player player, final Player target, final boolean active) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				switch (loop) {
				case 0:
					player.setNextFaceEntity(target);
					player.setNextAnimation(new Animation(4411));
					target.setNextGraphics(new Graphics(725, 0, 100));
					player.getSkills().addSkillXpRefresh(Skills.MAGIC, 250 + 100);

					player.setNextFaceEntity(null);
					break;
				case 1:
					player.sendMessage("You have given a vengeance.");
					target.sendMessage("You have received a vengeance.");
					stop();
					break;
					
				}
				loop++;
				}
			}, 0, 1);
	}
	
	/**
	 * Preforms special transfer spell to specific target.
	 * @param player
	 */
	public static final void preformSpecialTransfer(final Player player, final Player target, final boolean active) {
		WorldTasksManager.schedule(new WorldTask() {
		int loop;
		@Override
		public void run() {
			switch (loop) {
			case 0:
				player.setNextFaceEntity(target);
				player.setNextAnimation(new Animation(4411));
				target.setNextGraphics(new Graphics(751, 0, 100));
				player.getSkills().addSkillXpRefresh(Skills.MAGIC, 250 + 75);
				break;
			case 1:
				player.sendMessage("You have restored " + target.getDisplayName() + "'s special attack.");
				player.applyHit(new Hit(player, 100, HitLook.REGULAR_DAMAGE));
				player.setNextFaceEntity(null);
				stop();
				break;
				
			}
			loop++;
			}
		}, 0, 1);
	
	}
	/**
	 * Preforms the bones to peaches spell.
	 * @param player
	 */
	private static final void preformBonestoPeaches(Player player, Item item) {
		boolean active = false;
		if (active)
			return;
		active = true;
		int BONES = 526;
		int BoneSlots = 0;
		player.addStopDelay(4);
		player.getInventory().deleteItem(NATURE_RUNE, 1);
		player.getInventory().deleteItem(WATER_RUNE, 2);
		player.getInventory().deleteItem(EARTH_RUNE, 2);
		sendSpellAction(player, BONES_TO_PEACHES);
		player.getInventory().deleteItem(526, new Item(item.getAmount())); //We delete the item and get the amount-
		BoneSlots += item.getAmount(); //We get grab the amount of bones.
		player.getInventory().addItem(526, BoneSlots); //Now we hadd the bones that are stored in BoneSlots
		BoneSlots = 0; //And in the end we ofcourse reset it.
		active = false;
	}

	
	/**
	 * We send animation + graphics at the same time.
	 */
	private static final void sendSpellAction(Player player, int spellId) {
		switch (spellId) {
		case BONES_TO_PEACHES:
			player.setNextAnimation(new Animation(722));
			player.setNextGraphics(new Graphics(141));
			break;
		}
	}
	/**
	 * Requirements for spells. int id
	 * @param player
	 * @param id
	 * @return
	 */
	private static final boolean containsRequirements(Player player, int id) {
		switch (id) {
		case 0: //Charge
			return player.getInventory().containsItem(FIRE_RUNE, 3) && player.getInventory().containsItem(BLOOD_RUNE, 3) && player.getInventory().containsItem(AIR_RUNE, 3);
		case BONES_TO_PEACHES:
			return player.getInventory().containsItem(EARTH_RUNE, 2) && player.getInventory().containsItem(NATURE_RUNE, 1) && player.getInventory().containsItem(WATER_RUNE, 2);
		}
		return false;
	}
	
	/**
	 * Preform charge spell.
	 * @param player
	 */
	public static void preformCharge(final Player player) {
		player.setNextAnimation(new Animation(811));
		player.getSkills().addSkillXpRefresh(Skills.MAGIC, 250 / 0.8544);
		player.sendMessage("You feel charged with magic power.");
		player.addChargeSpell(420);
	}
	
	/**
	 * Checks the runes.
	 * @param player
	 * @param delete
	 * @param runes
	 * @return
	 */
	private static final boolean checkRunes(Player player, boolean delete,
			int... runes) {
		int weaponId = player.getEquipment().getWeaponId();
		int shieldId = player.getEquipment().getShieldId();
		int runesCount = 0;
		while (runesCount < runes.length) {
			int runeId = runes[runesCount++];
			int ammount = runes[runesCount++];
			if (Magic.hasInfiniteRunes(runeId, weaponId, shieldId))
				continue;
			if (Magic.hasStaffOfLight(weaponId) && Misc.getRandom(8) == 0
					&& runeId != 21773)// 1 in eight chance of keeping runes
				continue;
			if (!player.getInventory().containsItem(runeId, ammount)) {
				player.packets().sendMessage(
						"You do not have enough "
								+ ItemDefinition.get(runeId)
										.getName().replace("rune", "Rune")
								+ "s to cast this spell.");
				return false;
			}
		}
		if (delete) {
			runesCount = 0;
			while (runesCount < runes.length) {
				int runeId = runes[runesCount++];
				int ammount = runes[runesCount++];
				if (Magic.hasInfiniteRunes(runeId, weaponId, shieldId))
					continue;
				player.getInventory().deleteItem(runeId, ammount);
			}
		}
		return true;
	}
}
