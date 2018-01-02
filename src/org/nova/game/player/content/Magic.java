package org.nova.game.player.content;

import org.nova.Constants;
import org.nova.cache.definition.ItemDefinition;
import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.content.cities.ApeToll;
import org.nova.game.player.controlers.Kalaboss;
import org.nova.game.player.controlers.Wilderness;
import org.nova.utility.misc.Misc;

/*
 * content package used for static stuff
 */
public class Magic {

	public static final int MAGIC_TELEPORT = 0, ITEM_TELEPORT = 1,
			OBJECT_TELEPORT = 2;

	@SuppressWarnings("unused")
	private static final int AIR_RUNE = 556, WATER_RUNE = 555,
			EARTH_RUNE = 557, FIRE_RUNE = 554, MIND_RUNE = 558,
			NATURE_RUNE = 561, CHAOS_RUNE = 562, DEATH_RUNE = 560,
			BLOOD_RUNE = 565, SOUL_RUNE = 566, ASTRAL_RUNE = 9075,
			LAW_RUNE = 563, STEAM_RUNE = 4694, MIST_RUNE = 4695,
			DUST_RUNE = 4696, SMOKE_RUNE = 4697, MUD_RUNE = 4698,
			LAVA_RUNE = 4699, ARMADYL_RUNE = 21773;

	public static final boolean hasInfiniteRunes(int runeId, int weaponId,
			int shieldId) {
		if (runeId == AIR_RUNE) {
			if (weaponId == 1381 || weaponId == 21777) // air staff
				return true;
		} else if (runeId == WATER_RUNE) {
			if (weaponId == 1383 || shieldId == 18346) // water staff
				return true;
		} else if (runeId == EARTH_RUNE) {
			if (weaponId == 1385) // earth staff
				return true;
		} else if (runeId == FIRE_RUNE) {
			if (weaponId == 1387) // fire staff
				return true;
		}
		return false;
	}

	public static boolean useTabTeleport(final Player player, final int itemId) {
		if (itemId < 8007 || itemId > 8007 + TABS.length - 1)
			return false;
		if (useTeleTab(player, TABS[itemId - 8007]))
			player.getInventory().deleteItem(itemId, 1);
		return true;
	}

	public static boolean useTeleTab(final Player player, final Location tile) {
		if (!player.getControllerManager().processItemTeleport(tile))
			return false;
		if (!player.getRandomEvent().canTeleport())
			return false;
		player.lock();
		player.setNextAnimation(new Animation(9597));
		player.setNextGraphics(new Graphics(1680));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 0) {
					player.setNextAnimation(new Animation(4731));
					stage = 1;
				} else if (stage == 1) {
					Location teleTile = tile;
					// attemps to randomize tile by 4x4 area
					for (int trycount = 0; trycount < 10; trycount++) {
						teleTile = new Location(tile, 2);
						if (Game.canMoveNPC(tile.getZ(), teleTile.getX(),
								teleTile.getY(), player.getSize()))
							break;
						teleTile = tile;
					}
					player.setLocation(teleTile);
					player.getControllerManager().magicTeleported(ITEM_TELEPORT);
					if (player.getControllerManager().getControler() == null)
						teleControlersCheck(player, teleTile);
					player.faceLocation(new Location(teleTile.getX(), teleTile
							.getY() - 1, teleTile.getZ()));
					player.setDirection(6);
					player.setNextAnimation(new Animation(9598));
					stage = 2;
				} else if (stage == 2) {
					player.resetReceivedDamage();
					player.unlock();
					stop();
				}

			}
		}, 2, 1);
		return true;
	}

	private final static Location[] TABS = { new Location(3217, 3426, 0),
			new Location(3222, 3218, 0), new Location(2965, 3379, 0),
			new Location(2758, 3478, 0), new Location(2660, 3306, 0) };

	public static final boolean checkCombatSpell(Player player, int spellId,
			int set, boolean delete) {
		switch (player.getCombatDefinitions().getSpellBook()) {
		case 193:
			switch (spellId) {
			case 28:
				if (!checkSpellRequirements(player, 50, delete, CHAOS_RUNE, 2,
						DEATH_RUNE, 2, FIRE_RUNE, 1, AIR_RUNE, 1))
					return false;
				break;
			case 32:
				if (!checkSpellRequirements(player, 52, delete, CHAOS_RUNE, 2,
						DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE, 1))
					return false;
				break;
			case 24:
				if (!checkSpellRequirements(player, 56, delete, CHAOS_RUNE, 2,
						DEATH_RUNE, 2, BLOOD_RUNE, 1))
					return false;
				break;
			case 20:
				if (!checkSpellRequirements(player, 58, delete, CHAOS_RUNE, 2,
						DEATH_RUNE, 2, WATER_RUNE, 2))
					return false;
				break;
			case 30:
				if (!checkSpellRequirements(player, 62, delete, CHAOS_RUNE, 4,
						DEATH_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE, 2))
					return false;
				break;
			case 34:
				if (!checkSpellRequirements(player, 64, delete, CHAOS_RUNE, 4,
						DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE, 2))
					return false;
				break;
			case 26:
				if (!checkSpellRequirements(player, 68, delete, CHAOS_RUNE, 4,
						DEATH_RUNE, 2, BLOOD_RUNE, 2))
					return false;
				break;
			case 22:
				if (!checkSpellRequirements(player, 70, delete, CHAOS_RUNE, 4,
						DEATH_RUNE, 2, WATER_RUNE, 4))
					return false;
				break;
			case 29:
				if (!checkSpellRequirements(player, 74, delete, DEATH_RUNE, 2,
						BLOOD_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE, 2))
					return false;
				break;
			case 33:
				if (!checkSpellRequirements(player, 76, delete, DEATH_RUNE, 2,
						BLOOD_RUNE, 2, AIR_RUNE, 2, SOUL_RUNE, 2))
					return false;
				break;
			case 25:
				if (!checkSpellRequirements(player, 80, delete, DEATH_RUNE, 2,
						BLOOD_RUNE, 4))
					return false;
				break;
			case 21:
				if (!checkSpellRequirements(player, 82, delete, DEATH_RUNE, 2,
						BLOOD_RUNE, 2, WATER_RUNE, 3))
					return false;
				break;
			case 31:
				if (!checkSpellRequirements(player, 86, delete, DEATH_RUNE, 4,
						BLOOD_RUNE, 2, FIRE_RUNE, 4, AIR_RUNE, 4))
					return false;
				break;
			case 35:
				if (!checkSpellRequirements(player, 88, delete, DEATH_RUNE, 4,
						BLOOD_RUNE, 2, AIR_RUNE, 4, SOUL_RUNE, 3))
					return false;
				break;
			case 27:
				if (!checkSpellRequirements(player, 92, delete, DEATH_RUNE, 4,
						BLOOD_RUNE, 4, SOUL_RUNE, 1))
					return false;
				break;
			case 23:
				if (!checkSpellRequirements(player, 94, delete, DEATH_RUNE, 4,
						BLOOD_RUNE, 2, WATER_RUNE, 6))
					return false;
				break;
			case 36: // Miasmic Rush
				if (!ItemDefinition.get(player.getEquipment().getWeaponId()).
						getName().toLowerCase().startsWith("zuriel's staff")) {
					player.packets().sendMessage(
						"You need to be wielding Zuriel's Staff in order to cast this spell.", true);
					return false;
				}
				if (!checkSpellRequirements(player, 61, delete, CHAOS_RUNE, 2,
						EARTH_RUNE, 1, SOUL_RUNE, 1))
					return false;
				break;
			case 38: // Miasmic Burst
				if (!ItemDefinition.get(player.getEquipment().getWeaponId()).
						getName().toLowerCase().startsWith("zuriel's staff")) {
					player.packets().sendMessage(
						"You need to be wielding Zuriel's Staff in order to cast this spell.", true);
					return false;
				}
				if (!checkSpellRequirements(player, 73, delete, CHAOS_RUNE, 4,
						EARTH_RUNE, 2, SOUL_RUNE, 2))
					return false;
				break;
			case 37: // Miasmic Blitz
				if (!ItemDefinition.get(player.getEquipment().getWeaponId()).
						getName().toLowerCase().startsWith("zuriel's staff")) {
					player.packets().sendMessage(
						"You need to be wielding Zuriel's Staff in order to cast this spell.", true);
					return false;
				}
				if (!checkSpellRequirements(player, 85, delete, BLOOD_RUNE, 2,
						EARTH_RUNE, 3, SOUL_RUNE, 3))
					return false;
				break;
			case 39: // Miasmic Barrage
				if (!ItemDefinition.get(player.getEquipment().getWeaponId()).
						getName().toLowerCase().startsWith("zuriel's staff")) {
					player.packets().sendMessage(
						"You need to be wielding Zuriel's Staff in order to cast this spell.", true);
					return false;
				}
				if (!checkSpellRequirements(player, 97, delete, BLOOD_RUNE, 4,
						EARTH_RUNE, 4, SOUL_RUNE, 4))
					return false;
				break;
			default:
				return false;
			}
			break;
		case 192:
			switch (spellId) {
			case 25:
				if (!checkSpellRequirements(player, 1, delete, AIR_RUNE, 1,
						MIND_RUNE, 1))
					return false;
				break;
			case 28:
				if (!checkSpellRequirements(player, 5, delete, WATER_RUNE, 1,
						AIR_RUNE, 1, MIND_RUNE, 1))
					return false;
				break;
			case 30:
				if (!checkSpellRequirements(player, 9, delete, EARTH_RUNE, 2,
						AIR_RUNE, 1, MIND_RUNE, 1))
					return false;
				break;
			case 32:
				if (!checkSpellRequirements(player, 13, delete, FIRE_RUNE, 3,
						AIR_RUNE, 2, MIND_RUNE, 1))
					return false;
				break;
			case 34: // air bolt
				if (!checkSpellRequirements(player, 17, delete, AIR_RUNE, 2,
						CHAOS_RUNE, 1))
					return false;
				break;
			case 36:// bind
				if (!checkSpellRequirements(player, 20, delete, EARTH_RUNE, 3,
						WATER_RUNE, 3, NATURE_RUNE, 2))
					return false;
				break;
			case 55: // snare
				if (!checkSpellRequirements(player, 50, delete, EARTH_RUNE, 4,
						WATER_RUNE, 4, NATURE_RUNE, 3))
					return false;
				break;
			case 81:// entangle
				if (!checkSpellRequirements(player, 79, delete, EARTH_RUNE, 5,
						WATER_RUNE, 5, NATURE_RUNE, 4))
					return false;
				break;
			case 39: // water bolt
				if (!checkSpellRequirements(player, 23, delete, WATER_RUNE, 2,
						AIR_RUNE, 2, CHAOS_RUNE, 1))
					return false;
				break;
			case 42: // earth bolt
				if (!checkSpellRequirements(player, 29, delete, EARTH_RUNE, 3,
						AIR_RUNE, 2, CHAOS_RUNE, 1))
					return false;
				break;
			case 45: // fire bolt
				if (!checkSpellRequirements(player, 35, delete, FIRE_RUNE, 4,
						AIR_RUNE, 3, CHAOS_RUNE, 1))
					return false;
				break;
			case 49: // air blast
				if (!checkSpellRequirements(player, 41, delete, AIR_RUNE, 3,
						DEATH_RUNE, 1))
					return false;
				break;
			case 52: // water blast
				if (!checkSpellRequirements(player, 47, delete, WATER_RUNE, 3,
						AIR_RUNE, 3, DEATH_RUNE, 1))
					return false;
				break;
			case 58: // earth blast
				if (!checkSpellRequirements(player, 53, delete, EARTH_RUNE, 4,
						AIR_RUNE, 3, DEATH_RUNE, 1))
					return false;
				break;
			case 63: // fire blast
				if (!checkSpellRequirements(player, 59, delete, FIRE_RUNE, 5,
						AIR_RUNE, 4, DEATH_RUNE, 1))
					return false;
				break;
			case 70: // air wave
				if (!checkSpellRequirements(player, 62, delete, AIR_RUNE, 5,
						BLOOD_RUNE, 1))
					return false;
				break;
			case 73: // water wave
				if (!checkSpellRequirements(player, 65, delete, WATER_RUNE, 7,
						AIR_RUNE, 5, BLOOD_RUNE, 1))
					return false;
				break;
			case 77: // earth wave
				if (!checkSpellRequirements(player, 70, delete, EARTH_RUNE, 7,
						AIR_RUNE, 5, BLOOD_RUNE, 1))
					return false;
				break;
			case 80: // fire wave
				if (!checkSpellRequirements(player, 75, delete, FIRE_RUNE, 7,
						AIR_RUNE, 5, BLOOD_RUNE, 1))
					return false;
				break;
			case 84:
				if (!checkSpellRequirements(player, 81, delete, AIR_RUNE, 7,
						DEATH_RUNE, 1, BLOOD_RUNE, 1))
					return false;
				break;
			case 87:
				if (!checkSpellRequirements(player, 85, delete, WATER_RUNE, 10,
						AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1))
					return false;
				break;
			case 89:
				if (!checkSpellRequirements(player, 85, delete, EARTH_RUNE, 10,
						AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1))
					return false;
				break;
			case 74: // // the Id is wrong
				if (!checkSpellRequirements(player, 85, delete, EARTH_RUNE, 11,
						DEATH_RUNE, 3, ASTRAL_RUNE, 4))
					return false;
				break;
			case 66: // Sara Strike
				if (player.getEquipment().getWeaponId() != 2415) {
					player.packets()
							.sendMessage(
									"You need to be equipping a Saradomin staff to cast this spell.",
									true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4,
						FIRE_RUNE, 1, BLOOD_RUNE, 2))
					return false;
				break;
			case 67: // Guthix Claws
				if (player.getEquipment().getWeaponId() != 2416) {
					player.packets()
							.sendMessage(
									"You need to be equipping a Guthix Staff or Void Mace to cast this spell.",
									true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4,
						FIRE_RUNE, 1, BLOOD_RUNE, 2))
					return false;
				break;
			case 68: // Flame of Zammy
				if (player.getEquipment().getWeaponId() != 2417) {
					player.packets()
							.sendMessage(
									"You need to be equipping a Zamorak Staff to cast this spell.",
									true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4,
						FIRE_RUNE, 4, BLOOD_RUNE, 2))
					return false;
				break;
			case 91:
				if (!checkSpellRequirements(player, 85, delete, FIRE_RUNE, 10,
						AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1))
					return false;
				break;
			case 86: // teleblock
				if (!checkSpellRequirements(player, 85, delete, CHAOS_RUNE, 1,
						LAW_RUNE, 1, DEATH_RUNE, 1))
					return false;
				break;
			case 99: // Storm of Armadyl
				if (!checkSpellRequirements(player, 77, delete, ARMADYL_RUNE, 1))
					return false;
				break;
			default:
				return false;
			}
			break;
		default:
			return false;
		}
		if (set >= 0) {
			if (set == 0)
				player.getCombatDefinitions().setAutoCastSpell(spellId);
			else
				player.getTemporaryAttributtes().put("tempCastSpell", spellId);
		}
		return true;
	}

	public static final void setCombatSpell(Player player, int spellId) {
		if (player.getCombatDefinitions().getAutoCastSpell() == spellId)
			player.getCombatDefinitions().resetSpells(true);
		else
			checkCombatSpell(player, spellId, 0, false);
		player.stopAll();
	}

	public static final void processLunarSpell(Player player, int spellId,
			int packetId) {
		switch (spellId) {
		case 37:
			if (player.getSkills().getLevel(Skills.MAGIC) < 94) {
				player.packets().sendMessage(
						"Your Magic level is not high enough for this spell.");
				return;
			}
			if (player.getLastVeng() > 0) {
				player.packets().sendMessage(
						"You can only cast vengeance once every 30 seconds.");
				return;
			}
			if (!checkRunes(player, true, ASTRAL_RUNE, 4, DEATH_RUNE, 2,
					EARTH_RUNE, 10))
				return;
			player.setNextGraphics(new Graphics(726, 0, 100));
			player.setNextAnimation(new Animation(4410));
			player.setCastVeng(true);
			player.setLastVeng(50);
			player.packets().sendMessage("You cast a vengeance.");
			break;
		case 39:
			if (player.isTeleport()) {
				player.sm("You cannot teleport currently to anywhere.");
				return;
			}
			if (!player.getRandomEvent().canTeleport())
				return;
			if (player.isTransformed())
				ApeToll.untransformPlayer(player);
			player.stopAll();
			player.getControllerManager().forceStop();
			Magic.sendLunarTeleport(player, 0, 0, new Location(
					Constants.RESPAWN_PLAYER_LOCATION));
			break;
		}
	}

	public static final void processAncientSpell(Player player, int spellId,
			int packetId) {
		switch (spellId) {
		case 28:
		case 32:
		case 24:
		case 20:
		case 30:
		case 34:
		case 26:
		case 22:
		case 29:
		case 33:
		case 25:
		case 21:
		case 31:
		case 35:
		case 27:
		case 23:
		case 36:
		case 37:
		case 38:
		case 39:
			setCombatSpell(player, spellId);
			break;
		case 40:
			sendAncientTeleportSpell(player, 54, 64,
					new Location(3099, 9882, 0), LAW_RUNE, 2, FIRE_RUNE, 1,
					AIR_RUNE, 1);
			break;
		case 41:
			sendAncientTeleportSpell(player, 60, 70,
					new Location(3222, 3336, 0), LAW_RUNE, 2, SOUL_RUNE, 1);
			break;
		case 42:
			sendAncientTeleportSpell(player, 66, 76,
					new Location(3492, 3471, 0), LAW_RUNE, 2, BLOOD_RUNE, 1);

			break;
		case 43:
			sendAncientTeleportSpell(player, 72, 82,
					new Location(3006, 3471, 0), LAW_RUNE, 2, WATER_RUNE, 4);
			break;
		case 44:
			sendAncientTeleportSpell(player, 78, 88,
					new Location(2990, 3696, 0), LAW_RUNE, 2, FIRE_RUNE, 3,
					AIR_RUNE, 2);
			break;
		case 45:
			sendAncientTeleportSpell(player, 84, 94,
					new Location(3217, 3677, 0), LAW_RUNE, 2, SOUL_RUNE, 2);
			break;
		case 46:
			sendAncientTeleportSpell(player, 90, 100, new Location(3288, 3886,
					0), LAW_RUNE, 2, BLOOD_RUNE, 2);
			break;
		case 47:
			sendAncientTeleportSpell(player, 96, 106, new Location(2977, 3873,
					0), LAW_RUNE, 2, WATER_RUNE, 8);
			break;
		case 48:
			sendAncientTeleportSpell(player, 0, 0, new Location(
					Constants.RESPAWN_PLAYER_LOCATION));
			break;
		}
	}

	public static final void processNormalSpell(final Player player,
			int spellId, int packetId) {
		switch (spellId) {
		case 25: // air strike
		case 28: // water strike
		case 30: // earth strike
		case 32: // fire strike
		case 34: // air bolt
		case 39: // water bolt
		case 42: // earth bolt
		case 45: // fire bolt
		case 49: // air blast
		case 52: // water blast
		case 58: // earth blast
		case 63: // fire blast
		case 70: // air wave
		case 73: // water wave
		case 77: // earth wave
		case 80: // fire wave
		case 99:
		case 84:
		case 87:
		case 89:
		case 91:
		case 36:
		case 55:
		case 81:
		case 66:
		case 67:
		case 68:
			setCombatSpell(player, spellId);
			break;
		case 27: // crossbow bolt enchant
			if (player.getSkills().getLevel(Skills.MAGIC) < 4) {
				player.packets()
						.sendMessage(
								"You need a Magic level of 4 in order to cast this spell.");
				return;
			}
			player.stopAll();
			player.interfaces().sendInterface(432);
			break;
		case 24:
			useHomeTele(player);
			break;
		case 37: // mobi
			sendNormalTeleportSpell(player, 10, 19, new Location(
					/* 2413, 2848, 0 */5888, 4621, 0), LAW_RUNE, 1, WATER_RUNE,
					1, AIR_RUNE, 1);
			break;
		case 40: // varrock
			sendNormalTeleportSpell(player, 25, 19,
					new Location(3212, 3424, 0), FIRE_RUNE, 1, AIR_RUNE, 3,
					LAW_RUNE, 1);
			break;
		case 43: // lumby
			sendNormalTeleportSpell(player, 31, 41,
					new Location(3222, 3218, 0), EARTH_RUNE, 1, AIR_RUNE, 3,
					LAW_RUNE, 1);
			break;
		case 46: // fally
			sendNormalTeleportSpell(player, 37, 48,
					new Location(2964, 3379, 0), WATER_RUNE, 1, AIR_RUNE, 3,
					LAW_RUNE, 1);
			break;
		case 51: // camelot
			sendNormalTeleportSpell(player, 45, 55.5, new Location(2757, 3478,
					0), AIR_RUNE, 5, LAW_RUNE, 1);
			break;
		case 57: // ardy
			sendNormalTeleportSpell(player, 51, 61,
					new Location(2664, 3305, 0), WATER_RUNE, 2, LAW_RUNE, 2);
			break;
		case 62: // watch
			sendNormalTeleportSpell(player, 58, 68,
					new Location(2547, 3113, 2), EARTH_RUNE, 2, LAW_RUNE, 2);
			break;
		case 69: // troll
			sendNormalTeleportSpell(player, 61, 68,
					new Location(2888, 3674, 0), FIRE_RUNE, 2, LAW_RUNE, 2);
			break;
		case 72: // ape
			sendNormalTeleportSpell(player, 64, 76,
					new Location(2776, 9103, 0), FIRE_RUNE, 2, WATER_RUNE, 2,
					LAW_RUNE, 2, 1963, 1);
			break;
		case 48:
			if (player.getHouse().isArrivingAtPortal()) {
				if (!player.getHouse().hasPurchasedHouse()) {
					player.sm("You must purchase a house before doing this.");
					return;
				}
				sendNormalTeleportSpell(player, 40, 59, player.getHouse()
						.getHouseLocation(), LAW_RUNE, 1, EARTH_RUNE, 1,
						AIR_RUNE, 1);
			} else {
				if (player.getHouse().hasGuests())
					player.getHouse().addPlayer(player);
				else {
					if (player.getRandomEvent().getCurrent() != null
							&& player.getRandomEvent().getCurrent().getClass()
									.getSimpleName().equals("EnterHouse")) {
						player.sm("You're already at your house.");
						return;
					}
					if (!player.getHouse().hasPurchasedHouse()) {
						player.sm("You must purchase a house before doing this.");
						return;
					}
					sendNormalTeleportSpell(player, 0, 0, player.getHouse()
							.getHouseLocation());
					Game.submit(new GameTick(2.4) {
						@Override
						public void run() {
							stop();
							player.getRandomEvent().start("EnterHouse");
						}
					});
				}
			}
			break;
		}
	}

	private static void useHomeTele(Player player) {
		if (player.isTransformed())
			ApeToll.untransformPlayer(player);
		player.stopAll();
		Magic.specialTele(player, Constants.START_PLAYER_LOCATION, false);
		player.getControllerManager().forceStop();
	}

	public static final boolean checkSpellRequirements(Player player,
			int level, boolean delete, int... runes) {
		if (player.getSkills().getLevelFromXP(Skills.MAGIC) < level) {
			player.packets().sendMessage(
					"You need a Magic level of " + level
							+ " in order to cast this spell.");
			return false;
		}
		return checkRunes(player, delete, runes);
	}

	public static final boolean checkSpellRequirements2(Player player,
			int level, boolean delete, int... runes) {
		if (player.getSkills().getLevelFromXP(Skills.MAGIC) < level) {
			player.packets().sendMessage(
					"You need a Magic level of " + level
							+ " in order to cast this spell.");
			return false;
		}
		return checkRunes(player, delete, runes);
	}

	public static boolean hasStaffOfLight(int weaponId) {
		if (weaponId == 15486 || weaponId == 22207 || weaponId == 22209
				|| weaponId == 22211 || weaponId == 22213)
			return true;
		return false;
	}

	public static final boolean checkRunes(Player player, boolean delete,
			int... runes) {
		int weaponId = player.getEquipment().getWeaponId();
		int shieldId = player.getEquipment().getShieldId();
		int runesCount = 0;
		while (runesCount < runes.length) {
			int runeId = runes[runesCount++];
			int ammount = runes[runesCount++];
			if (hasInfiniteRunes(runeId, weaponId, shieldId))
				continue;
			if (hasStaffOfLight(weaponId) && Misc.getRandom(8) == 0
					&& runeId != 21773)// 1 in eight chance of keeping runes
				continue;
			if (!player.getInventory().containsItem(runeId, ammount)) {
				player.packets().sendMessage(
						"You do not have enough "
								+ ItemDefinition.get(runeId).getName()
										.replace("rune", "Rune")
								+ "s to cast this spell.");
				return false;
			}
		}
		if (delete) {
			runesCount = 0;
			while (runesCount < runes.length) {
				int runeId = runes[runesCount++];
				int ammount = runes[runesCount++];
				if (hasInfiniteRunes(runeId, weaponId, shieldId))
					continue;
				player.getInventory().deleteItem(runeId, ammount);
			}
		}
		return true;
	}

	public static final void sendAncientTeleportSpell(Player player, int level,
			double xp, Location tile, int... runes) {
		if (!player.getRandomEvent().canTeleport())
			return;
		if (player.isTeleport() == false) {
			sendTeleportSpell(player, 9599, 8941, 1681, -1, level, xp, tile, 5,
					true, MAGIC_TELEPORT, runes);
			player.addStopDelay(5);
		} else {
			player.sm("You cannot teleport anywhere currently.");
		}
	}

	public static final void sendLunarTeleport(Player player, int level,
			double xp, Location tile, int... runes) {
		if (!player.getRandomEvent().canTeleport())
			return;
		if (player.isTeleport() == false) {
			sendTeleportSpell(player, 9606, 9013, 1685, -1, level, xp, tile, 6,
					true, MAGIC_TELEPORT, runes);
		} else {
			player.sm("You cannot teleport anywhere currently.");
		}
	}

	public static final void sendNormalTeleportSpell(Player player, int level,
			double xp, Location tile, int... runes) {
		player.interfaces().closeChatBoxInterface();
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, level, xp, tile, 3,
				true, MAGIC_TELEPORT, runes);
	}

	public static final boolean sendItemTeleportSpell(Player player,
			boolean randomize, int upEmoteId, int upGraphicId, int delay,
			Location tile) {
		if (!player.getRandomEvent().canTeleport())
			return false;
		return sendTeleportSpell(player, upEmoteId, -2, upGraphicId, -1, 0, 0,
				tile, delay, randomize, ITEM_TELEPORT);
	}

	public static void pushLeverTeleport(final Player player,
			final Location tile) {
		if (!player.getControllerManager().processObjectTeleport(tile))
			return;
		if (!player.getRandomEvent().canTeleport())
			return;
		player.setNextAnimation(new Animation(2140));
		player.setInfiniteStopDelay();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.resetStopDelay();
				Magic.sendObjectTeleportSpell(player, false, tile);
			}
		}, 1);
	}

	public static final void sendObjectTeleportSpell(Player player,
			boolean randomize, Location tile) {
		if (!player.getRandomEvent().canTeleport())
			return;
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, 0, 0, tile, 3,
				randomize, OBJECT_TELEPORT);
	}

	public static final void sendDelayedObjectTeleportSpell(Player player,
			int delay, boolean randomize, Location tile) {
		if (!player.getRandomEvent().canTeleport())
			return;
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, 0, 0, tile, delay,
				randomize, OBJECT_TELEPORT);
	}

	public static final void VecnaSkull(Player player) {
		player.stopAll(false);
		Long lastVecna = (Long) player.getTemporaryAttributtes().get(
				"LAST_VECNA");
		if (lastVecna != null && lastVecna + 420000 > Misc.currentTimeMillis()) {
			player.packets()
					.sendMessage(
							"The skull has not yet regained "
									+ "its mysterious aura. You will need to wait another "
									+ (lastVecna != null
											&& lastVecna + 60000 > Misc
													.currentTimeMillis() ? "7"
											: (lastVecna != null
													&& lastVecna + 120000 > Misc
															.currentTimeMillis() ? "6"
													: (lastVecna != null
															&& lastVecna + 180000 > Misc
																	.currentTimeMillis() ? "5"
															: (lastVecna != null
																	&& lastVecna + 240000 > Misc
																			.currentTimeMillis() ? "4"
																	: (lastVecna != null
																			&& lastVecna + 300000 > Misc
																					.currentTimeMillis() ? "3"
																			: (lastVecna != null
																					&& lastVecna + 360000 > Misc
																							.currentTimeMillis() ? "2"
																					: "1"))))))
									+ " minutes.");
			return;
		}
		player.getTemporaryAttributtes().put("LAST_VECNA",
				Misc.currentTimeMillis());
		player.setNextGraphics(new Graphics(738, 0, 100));
		player.setNextAnimation(new Animation(10530));
		player.packets()
				.sendMessage(
						"The skull feeds off the life around you, boosting your magical ability.");
		int actualLevel = player.getSkills().getLevel(Skills.MAGIC);
		int realLevel = player.getSkills().getLevelFromXP(Skills.MAGIC);
		int level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.MAGIC, (int) (level + 6));
	}

	public static final boolean sendTeleportSpell(final Player player,
			int upEmoteId, final int downEmoteId, int upGraphicId,
			final int downGraphicId, int level, final double xp,
			final Location tile, int delay, final boolean randomize,
			final int teleType, int... runes) {// TODO, fix wilderness
		long currentTime = Misc.currentTimeMillis();
		if (player.getStopDelay() > currentTime)
			return false;
		if (player.getSkills().getLevel(Skills.MAGIC) < level) {
			player.packets().sendMessage(
					"You need a Magic level of " + level
							+ " in order to cast this spell.");
			return false;
		}
		if (!checkRunes(player, true, runes))
			return false;
		if (teleType == MAGIC_TELEPORT) {
			if (!player.getControllerManager().processMagicTeleport(tile))
				return false;
		} else if (teleType == ITEM_TELEPORT) {
			if (!player.getControllerManager().processItemTeleport(tile))
				return false;
		} else if (teleType == OBJECT_TELEPORT) {
			if (!player.getControllerManager().processObjectTeleport(tile))
				return false;
		}
		if (!player.getRandomEvent().canTeleport())
			return false;
		player.stopAll();
		player.hints().sendHint(0, 0, 0, 0);
		if (upEmoteId != -1)
			player.setNextAnimation(new Animation(upEmoteId));
		if (upGraphicId != -1)
			player.setNextGraphics(new Graphics(upGraphicId));
		if (teleType == MAGIC_TELEPORT)
			player.packets().sendSound(5527, 0, 2);
		player.addStopDelay(3 + delay);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				Location teleTile = tile;
				if (randomize) {
					// attemps to randomize tile by 4x4 area
					for (int trycount = 0; trycount < 10; trycount++) {
						teleTile = new Location(tile, 2);
						if (Game.canMoveNPC(tile.getZ(), teleTile.getX(),
								teleTile.getY(), player.getSize()))
							break;
						teleTile = tile;
					}
				}
				player.setLocation(teleTile);
				player.getControllerManager().magicTeleported(teleType);
				if (player.getControllerManager().getControler() == null)
					teleControlersCheck(player, teleTile);
				if (xp != 0)
					player.getSkills().addXp(Skills.MAGIC, xp);
				if (downEmoteId != -1)
					player.setNextAnimation(new Animation(
							downEmoteId == -2 ? -1 : downEmoteId));
				if (downGraphicId != -1)
					player.setNextGraphics(new Graphics(downGraphicId));
				if (teleType == MAGIC_TELEPORT) {
					player.packets().sendSound(5524, 0, 2);
					player.faceLocation(new Location(teleTile.getX(), teleTile
							.getY() - 1, teleTile.getZ()));
					player.setDirection(6);
				}
			}
		}, delay);
		return true;
	}

	public static void teleControlersCheck(Player player, Location teleTile) {
		if (Kalaboss.isAtKalaboss(teleTile))
			player.getControllerManager().startController("Kalaboss");
		else if (Wilderness.isAtWild(teleTile))
			player.getControllerManager().startController("Wilderness");
	}

	private Magic() {

	}

	public static void processLunarSpell(Player player, int componentId, NPC npc) {
		// TODO Auto-generated method stub

	}

	public static final boolean telePlayer(final Player player, Location l,
			final boolean randomizeTile) {
		int x = l.getX();
		int y = l.getY();
		int plane = l.getZ();
		if (!player.getControllerManager().processMagicTeleport(
				new Location(x, y, plane)))
			return false;
		if (!player.getRandomEvent().canTeleport())
			return false;
		final Location tile = new Location(x, y, plane);
		long currentTime = Misc.currentTimeMillis();
		if (player.getStopDelay() > currentTime)
			return false;
		if (player.getX() >= 2956
				&& player.getX() <= 3067
				&& player.getY() >= 5512
				&& player.getY() <= 5630
				|| (player.getX() >= 2756 && player.getX() <= 2875
						&& player.getY() >= 5512 && player.getY() <= 5627)) {
			player.packets().sendMessage(
					"A magical force is blocking you from teleporting.");
			return false;
		}

		player.hints().sendHint(0, 0, 0, 0);
		player.stopAll();
		player.setNextAnimation(new Animation(8939));
		player.setNextGraphics(new Graphics(1576));
		player.packets().sendSound(200, 0, 1);
		player.lock(3 + 1);
		WorldTasksManager.schedule(new WorldTask() {

			boolean removeDamage;

			@Override
			public void run() {
				if (!removeDamage) {
					Location teleTile = tile;
					if (randomizeTile) {
						// attemps to randomize tile by 4x4 area
						for (int trycount = 0; trycount < 10; trycount++) {
							teleTile = new Location(tile, 2);
							if (Game.canMoveNPC(tile.getZ(), teleTile.getX(),
									teleTile.getY(), player.getSize()))
								break;
							teleTile = tile;
						}
					}
					player.setLocation(teleTile);
					player.getControllerManager().magicTeleported(0);
					if (player.getControllerManager().getControler() == null)
						teleControlersCheck(player, teleTile);

					player.setNextAnimation(new Animation(8941));
					player.setNextGraphics(new Graphics(1577));
					// if (teleType == MAGIC_TELEPORT) {
					player.packets().sendSound(201, 0, 1);
					player.faceLocation(new Location(teleTile.getX(), teleTile
							.getY() - 1, teleTile.getZ()));
					player.setDirection(6);
					// }
					removeDamage = true;
				} else {
					player.resetReceivedDamage();
					stop();
				}
			}
		}, 3, 0);
		return true;
	}

	public static final void specialTele(final Player player, final Location l,
			final boolean randomize) {
		// if(!player.randomEvent().teleport())
		// return;
		// if(!player.getControlerManager().processMagicTeleport(l))
		// return;
		// long currentTime = Misc.currentTimeMillis();
		// if (player.getStopDelay() > currentTime)
		// return;
		// if (player.getX() >= 2956 && player.getX() <= 3067 && player.getY()
		// >= 5512 && player.getY() <= 5630
		// || (player.getX() >= 2756 && player.getX() <= 2875 && player.getY()
		// >= 5512 && player.getY() <= 5627)) {
		// player.packets().sendMessage("A magical force is blocking you from teleporting.");
		// return;
		// }
		// player.stopAll();
		// player.lock(3 + 1);
		// player.setCantMove(true);
		// player.animate(3256);
		// player.graphics(2009);
		// player.hints().sendHint(0, 0, 0, 0);
		// //player.sm("You gather energy...");
		// World.submit(new GameTick(1.6) {
		// int ticks = 0;
		// public void run() {
		// ticks++;
		// if(ticks == 1) {
		// player.animate(3248);
		// player.graphics(188);
		// //player.sm("Your encapsulated energy allows you to take flight!");
		// } else if(ticks == 2) {
		// Location teleLocation = l;
		// if (randomize) {
		// for (int trycount = 0; trycount < 10; trycount++) {
		// teleLocation = new Location(l, 2);
		// if (World.canMoveNPC(l.getZ(), l.getX(), l.getY(), player.getSize()))
		// break;
		// teleLocation = l;
		// }
		// }
		// player.setLocation(teleLocation);
		// player.faceLocation(new Location(teleLocation.getX(),
		// teleLocation.getY() - 1, teleLocation.getZ()));
		// player.setDirection(6);
		// player.setCantMove(false);
		// player.unlock();
		// }
		// else if(ticks == 3) {
		// player.animate(331);
		// stop();
		// World.submit(new GameTick(.5) {
		// int ticks = 0;
		// public void run() {
		// ticks++;
		// if(ticks == 3)
		// player.graphics(188);
		// else if(ticks == 4) {
		// player.animate(-1);
		// player.resetReceivedDamage();
		// stop();
		// }
		// }
		// });
		// }
		// }
		// });
		telePlayer(player, l, randomize);
	}
}
