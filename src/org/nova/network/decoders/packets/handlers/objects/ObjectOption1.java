package org.nova.network.decoders.packets.handlers.objects;

import org.nova.Constants;
import org.nova.cache.definition.ObjectDefinition;
import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceMovement;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.player.ClueScrolls;
import org.nova.game.player.OwnedObjectManager;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Agility;
import org.nova.game.player.actions.EssenceMining;
import org.nova.game.player.actions.EssenceMining.EssenceDefinitions;
import org.nova.game.player.actions.Farming;
import org.nova.game.player.actions.Hunter.HunterEquipment;
import org.nova.game.player.actions.Hunter.HunterNPC;
import org.nova.game.player.actions.Mining;
import org.nova.game.player.actions.Mining.RockDefinitions;
import org.nova.game.player.actions.PlayerCombat;
import org.nova.game.player.actions.Summoning;
import org.nova.game.player.actions.Woodcutting;
import org.nova.game.player.actions.Woodcutting.TreeDefinitions;
import org.nova.game.player.content.ArtisanWorkshop;
import org.nova.game.player.content.DungeoneeringDungeons;
import org.nova.game.player.content.FairyRing;
import org.nova.game.player.content.Gods;
import org.nova.game.player.content.HoarfrostHollow;
import org.nova.game.player.content.Ladders.LadderBase;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.MaxoutPlayer;
import org.nova.game.player.content.OuraniaCrafting;
import org.nova.game.player.content.Pickables;
import org.nova.game.player.content.ResourceDungeons;
import org.nova.game.player.content.Runecrafting;
import org.nova.game.player.content.Staircase;
import org.nova.game.player.content.agilitycourse.BarbarianOutpostAgility;
import org.nova.game.player.content.agilitycourse.WildernessAgility;
import org.nova.game.player.content.cities.Karamja;
import org.nova.game.player.content.cities.Keldagrim;
import org.nova.game.player.content.cities.PortPhasmatys;
import org.nova.game.player.content.cities.Underwater;
import org.nova.game.player.content.cities.content.ApeTollDungeon;
import org.nova.game.player.content.dungeoneering.DungeonPartyManager;
import org.nova.game.player.content.guilds.impl.CookingGuild;
import org.nova.game.player.content.itemactions.Beehive;
import org.nova.game.player.content.itemactions.Flax;
import org.nova.game.player.content.itemactions.RottenTomato;
import org.nova.game.player.content.minigames.CastleWars;
import org.nova.game.player.content.minigames.pestcontrol.Lander;
import org.nova.game.player.content.newuser.CharacterCreation;
import org.nova.game.player.content.quests.HoarfrostDepths;
import org.nova.game.player.content.quests.UzerRuins;
import org.nova.game.player.controlers.TowersPkControler;
import org.nova.game.player.controlers.Wilderness;

/**
 * 
 * @author Karimshan Nawaz
 *
 */
public class ObjectOption1 {
	
	/**
	 * 
	 * @param player
	 * @param object
	 */
	public static void process(final Player player, final GlobalObject object) {
		int id = object.getId();
		ObjectDefinition objectDef = object.defs();
		if(player.inArea("Cavern of Remembrance")) {
			player.getCavernOfRemembrance().interact(object);
			return;
		}
		if (!player.getRandomEvent().hasObjectOption1(object))
			return;
		if (LadderBase.forId(id) != null) {
			LadderBase.manageLadders(player, id, object);
			return;
		}
		if (RottenTomato.handleCrates(player, object))
			return;
		if (!player.getControllerManager().processObjectClick1(object))
			return;
		if (player.getRockPlinth().handleObjectInteractions(player,
				object))
			return;
		if (CastleWars.handleObjects(player, id))
			return;
		if (CookingGuild.getCookingGuild().handleObjectInteract(player,
				object))
			return;
		if (PortPhasmatys.processObjects(player, object))
			return;
		if (CharacterCreation.handleObjects(player, id))
			return;
		if (ApeTollDungeon.progressObjectInteract(player, object))
			return;
		else if (id == 9311 || id == 9312) {
			if (!Agility.hasLevel(player, 21))
				return;
			WorldTasksManager.schedule(new WorldTask() {

				int ticks = 0;

				@Override
				public void run() {
					boolean withinGE = id == 9312;
					Location tile = withinGE ? new Location(3139, 3516,
							0) : new Location(3143, 3514, 0);
					player.lock();
					ticks++;
					if (ticks == 1) {
						player.setNextAnimation(new Animation(2589));
						player.setNextForceMovement(new ForceMovement(
								object, 1,
								withinGE ? ForceMovement.WEST
										: ForceMovement.EAST));
					} else if (ticks == 3) {
						player.setLocation(new Location(3141, 3515, 0));
						player.setNextAnimation(new Animation(2590));
					} else if (ticks == 5) {
						player.setNextAnimation(new Animation(2591));
						player.setLocation(tile);
					} else if (ticks == 6) {
						player.setLocation(new Location(tile.getX()
								+ (withinGE ? -1 : 1), tile.getY(),
								tile.getZ()));
						player.unlock();
						stop();
					}
				}
			}, 0, 0);
		}
		if (ResourceDungeons.handleDungeons(player, object))
			return;
		if (Pickables.pickBananas(player, id))
			return;
		if (Staircase.handleGameObject(player, object))
			return;
		if (DungeoneeringDungeons.handleGameObject(player, object))
			return;
		if (Pickables.pick(player, object)) {
			return;
		}
		if (id == 2406)
			player.getZanarisHouse().openHouse();
		if (Karamja.handleObjects(player, id))
			return;
		HunterNPC hunterNpc = HunterNPC.forObjectId(id);
		if (hunterNpc != null) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(hunterNpc.getEquipment()
						.getPickUpAnimation());
				player.getInventory().addItem(hunterNpc.getItem(), 1);
				player.getInventory().addItem(
						hunterNpc.getEquipment().getId(), 1);
				player.getSkills().addXp(Skills.HUNTER, hunterNpc.getXp());
				player.setTrapAmount(player.getTrapAmount() - 1);
			} else {
				player.packets().sendMessage("This isn't your trap.");
			}
		}
		if (objectDef.name.equals("Furnace")) {
			player.getMatrixDialogues().startDialogue("SmeltingD",
					object);
		}
		if (objectDef.name.toLowerCase().contains("spinning")) {
			player.getMatrixDialogues().startDialogue("SpinningD");
		}

		if (objectDef.name.equals("Anvil")
				&& player.getInventory().containsItem(11286, 1)) {
			player.getMatrixDialogues().startDialogue(
					"DragonfireShieldD");
			// SpecialSmithing.createDragonfireShield(player);
		}

		if (id == 2646) {
			Flax.pickFlax(player, object);
		} else if (id == 26194) {
			player.getMatrixDialogues().startDialogue("PartyRoomLever");
		}
		if (id == 5008) { // Keldagrim entrance
			Keldagrim.enter(player);
		}
		if (Keldagrim.handleObjectInteractions(player, object))
			return;

		switch (id) {

		/**
		 * House portal
		 */
		case 15477: // Taverly
			player.getDialogue().start("HousePortal", "Taverly");
			break;
		case 15478: // Rimmington
			player.getDialogue().start("HousePortal", "Rimmington");
			break;
		case 15479: // Pollniveach
			player.getDialogue().start("HousePortal", "Pollniveach");
			break;
		case 15480: // Rellekka
			player.getDialogue().start("HousePortal", "Rellekka");
			break;
		case 15481: // Brimhaven
			player.getDialogue().start("HousePortal", "Brimhaven");
			break;
		case 15482: // Yanille
			player.getDialogue().start("HousePortal", "Yanille");
			break;

		case 1968: // Grand tree door
			Game.removeObject(object, true);
			break;
		case 68:
			Beehive.take(player, object, false);
			break;
		case 2562:
			MaxoutPlayer.scanGameProgress(player);
			break;
		case 4995:
			player.setLocation(new Location(3417, 3540, 2));
			break;
		case 25338:
			player.setLocation(new Location(1772, 5366, 0));
			break;
		case 25336:
			player.setLocation(new Location(1768, 5366, 1));
			break;
		case 2874:
		case 2873:
		case 2875:
			Gods.handleObjects(player, object);
			break;
		case 5013:
			player.setLocation(new Location(2838, 10124, 0));
			break;
		case 5998:
			player.setLocation(new Location(2799, 10134, 0));
			break;
		case 5973:
			if (player.getSkills().getLevel(Skills.AGILITY) >= 65) {
				player.sendMessage("You succesfully pass the entrance.");
			} else {
				player.sendMessage("You must have agility level of 66 to enter to this obstacle.");
			}
			break;
		case 5014: // Leaving the cave.
			player.setLocation(new Location(2731, 3714, 0));
			player.sendMessage("You are being hurt from the glowing sun's light after being in a dark cave.");
			player.applyHit(new Hit(player, 70, HitLook.REGULAR_DAMAGE));
			player.setNextForceTalk(new ForceTalk(
					"Ouch, my eyes hurts so much!"));
			break;
		default:
			if (player.isDeveloperMode())
				player.sendMessage("Object id: " + object.getId());
		}
		if (ClueScrolls.objectSpot(player, object)) {
			return;
		}

		if (id == 5282) {
			player.getEctophial().refillEctophial(player);
		}
		if (id == 34978) {
			if (player.isCompletedRuinsofUzer()) {
				player.sm("I have already saved the Ruins of Uzer.");
			} else {
				player.teleportPlayer(2763, 9315, 0);
			}
		}
		if (id == 24821) {
			ArtisanWorkshop.GiveBronzeIngots(player);
		}
		if (id == 24822) {
			ArtisanWorkshop.GiveIronIngots(player);
		} else if (object.getX() == 3287 && object.getY() == 5448
				&& object.getZ() == 0) {
			player.setLocation(new Location(3283, 5448, 0));

		} else if (object.getX() == 3283 && object.getY() == 5448
				&& object.getZ() == 0) {
			player.setLocation(new Location(3287, 5448, 0));

		} else if (object.getX() == 3307 && object.getY() == 5496
				&& object.getZ() == 0) {
			player.setLocation(new Location(3317, 5496, 0));

		} else if (object.getX() == 3317 && object.getY() == 5496
				&& object.getZ() == 0) {
			player.setLocation(new Location(3307, 5496, 0));

		} else if (object.getX() == 3318 && object.getY() == 5481
				&& object.getZ() == 0) {
			player.setLocation(new Location(3322, 5480, 0));

		} else if (object.getX() == 3322 && object.getY() == 5480
				&& object.getZ() == 0) {
			player.setLocation(new Location(3318, 5481, 0));

		} else if (object.getX() == 3299 && object.getY() == 5484
				&& object.getZ() == 0) {
			player.setLocation(new Location(3303, 5477, 0));

		} else if (object.getX() == 3303 && object.getY() == 5477
				&& object.getZ() == 0) {
			player.setLocation(new Location(3299, 5484, 0));

		} else if (object.getX() == 3286 && object.getY() == 5470
				&& object.getZ() == 0) {
			player.setLocation(new Location(3285, 5474, 0));

		} else if (object.getX() == 3285 && object.getY() == 5474
				&& object.getZ() == 0) {
			player.setLocation(new Location(3286, 5470, 0));

		} else if (object.getX() == 3290 && object.getY() == 5463
				&& object.getZ() == 0) {
			player.setLocation(new Location(3302, 5469, 0));

		} else if (object.getX() == 3302 && object.getY() == 5469
				&& object.getZ() == 0) {
			player.setLocation(new Location(3290, 5463, 0));

		} else if (object.getX() == 3296 && object.getY() == 5455
				&& object.getZ() == 0) {
			player.setLocation(new Location(3299, 5450, 0));

		} /* Waterfall */
		else if (id == 1987) {
			if (object.getX() == 2509 && object.getY() == 3493) {
				player.packets().sendMessage(
						"You hop on the log raft..");
				player.useStairs(-1, new Location(2512, 3481, 0), 3, 4);
				player.packets().sendMessage(
						"..and crash on a small island!");
			}
		} else if (id == 10283) {
			if (object.getX() == 2512 && object.getY() == 3475) {
				if (player.getInventory().containsItem(954, 1)) {
					player.useStairs(-1, new Location(2511, 3467, 0),
							3, 4);
				} else {
					player.useStairs(-1, new Location(2527, 3413, 0),
							3, 4);
					player.packets()
							.sendMessage(
									"The waterfall washes you down to the river.");
					player.packets().sendMessage(
							"Be glad you're still in one piece.");
				}
			}
		} else if (id == 2020) {
			if (object.getX() == 2512 && object.getY() == 3465) {
				if (player.getInventory().containsItem(954, 1)) {
					player.useStairs(-1, new Location(2511, 3463, 0),
							3, 4);
				} else {
					player.useStairs(-1, new Location(2527, 3413, 0),
							3, 4);
					player.packets()
							.sendMessage(
									"The waterfall washes you down to the river.");
					player.packets().sendMessage(
							"Be glad you're still in one piece.");
				}
			}
		} else if (id == 2022) {
			if (object.getX() == 2512 && object.getY() == 3463) {
				player.packets().sendMessage(
						"You get inside the barrel..");
				player.useStairs(-1, new Location(2527, 3413, 0), 3, 4);
				player.packets().sendMessage(
						"The waterfall washes you down to the river.");
				player.packets().sendMessage(
						"Be glad you're still in one piece.");
			}
		} else if (id == 37247) {
			if (object.getX() == 2511 && object.getY() == 3464)
				player.useStairs(-1, new Location(2575, 9861, 0), 1, 2);
		} else if (id == 32711) {
			if (object.getX() == 2574 && object.getY() == 9860)
				player.useStairs(-1, new Location(2511, 3463, 0), 1, 2);
		} else if (object.getX() == 3299 && object.getY() == 5450
				&& object.getZ() == 0) {
			player.setLocation(new Location(3296, 5455, 0));

		} else if (object.getX() == 3280 && object.getY() == 5501
				&& object.getZ() == 0) {
			player.setLocation(new Location(3285, 5508, 0));

		} else if (object.getX() == 3285 && object.getY() == 5508
				&& object.getZ() == 0) {
			player.setLocation(new Location(3280, 5501, 0));

		} else if (object.getX() == 3300 && object.getY() == 5514
				&& object.getZ() == 0) {
			player.setLocation(new Location(3297, 5510, 0));

		} else if (object.getX() == 3297 && object.getY() == 5510
				&& object.getZ() == 0) {
			player.setLocation(new Location(3300, 5514, 0));

		} else if (object.getX() == 3289 && object.getY() == 5533
				&& object.getZ() == 0) {
			player.setLocation(new Location(3288, 5536, 0));

		} else if (object.getX() == 3288 && object.getY() == 5536
				&& object.getZ() == 0) {
			player.setLocation(new Location(3289, 5533, 0));

		} else if (object.getX() == 3285 && object.getY() == 5527
				&& object.getZ() == 0) {
			player.setLocation(new Location(3282, 5531, 0));

		} else if (object.getX() == 3282 && object.getY() == 5531
				&& object.getZ() == 0) {
			player.setLocation(new Location(3285, 5527, 0));

		} else if (object.getX() == 3325 && object.getY() == 5518
				&& object.getZ() == 0) {
			player.setLocation(new Location(3323, 5531, 0));

		} else if (object.getX() == 3323 && object.getY() == 5531
				&& object.getZ() == 0) {
			player.setLocation(new Location(3325, 5518, 0));

		} else if (object.getX() == 3299 && object.getY() == 5533
				&& object.getZ() == 0) {
			player.setLocation(new Location(3297, 5536, 0));

		} else if (object.getX() == 3297 && object.getY() == 5536
				&& object.getZ() == 0) {
			player.setLocation(new Location(3299, 5533, 0));

		} else if (object.getX() == 3321 && object.getY() == 5554
				&& object.getZ() == 0) {
			player.setLocation(new Location(3315, 5552, 0));

		} else if (object.getX() == 3315 && object.getY() == 5552
				&& object.getZ() == 0) {
			player.setLocation(new Location(3321, 5554, 0));

		} else if (object.getX() == 3291 && object.getY() == 5555
				&& object.getZ() == 0) {
			player.setLocation(new Location(3285, 5556, 0));

		} else if (object.getX() == 3285 && object.getY() == 5556
				&& object.getZ() == 0) {
			player.setLocation(new Location(3291, 5555, 0));

		} else if (object.getX() == 3266 && object.getY() == 5552
				&& object.getZ() == 0) {
			player.setLocation(new Location(3262, 5552, 0));

		} else if (object.getX() == 3262 && object.getY() == 5552
				&& object.getZ() == 0) {
			player.setLocation(new Location(3266, 5552, 0));

		} else if (object.getX() == 3256 && object.getY() == 5561
				&& object.getZ() == 0) {
			player.setLocation(new Location(3253, 5561, 0));

		} else if (object.getX() == 3253 && object.getY() == 5561
				&& object.getZ() == 0) {
			player.setLocation(new Location(3256, 5561, 0));

		} else if (object.getX() == 3249 && object.getY() == 5546
				&& object.getZ() == 0) {
			player.setLocation(new Location(3252, 5543, 0));

		} else if (object.getX() == 3252 && object.getY() == 5543
				&& object.getZ() == 0) {
			player.setLocation(new Location(3249, 5546, 0));

		} else if (object.getX() == 3261 && object.getY() == 5536
				&& object.getZ() == 0) {
			player.setLocation(new Location(3268, 5534, 0));

		} else if (object.getX() == 3268 && object.getY() == 5534
				&& object.getZ() == 0) {
			player.setLocation(new Location(3261, 5536, 0));

		} else if (object.getX() == 3243 && object.getY() == 5526
				&& object.getZ() == 0) {
			player.setLocation(new Location(3241, 5529, 0));

		} else if (object.getX() == 3241 && object.getY() == 5529
				&& object.getZ() == 0) {
			player.setLocation(new Location(3243, 5526, 0));

		} else if (object.getX() == 3230 && object.getY() == 5547
				&& object.getZ() == 0) {
			player.setLocation(new Location(3226, 5553, 0));

		} else if (object.getX() == 3226 && object.getY() == 5553
				&& object.getZ() == 0) {
			player.setLocation(new Location(3230, 5547, 0));

		} else if (object.getX() == 3206 && object.getY() == 5553
				&& object.getZ() == 0) {
			player.setLocation(new Location(3204, 5546, 0));

		} else if (object.getX() == 3204 && object.getY() == 5546
				&& object.getZ() == 0) {
			player.setLocation(new Location(3206, 5553, 0));

		} else if (object.getX() == 3211 && object.getY() == 5533
				&& object.getZ() == 0) {
			player.setLocation(new Location(3214, 5533, 0));

		} else if (object.getX() == 3214 && object.getY() == 5533
				&& object.getZ() == 0) {
			player.setLocation(new Location(3211, 5533, 0));

		} else if (object.getX() == 3208 && object.getY() == 5527
				&& object.getZ() == 0) {
			player.setLocation(new Location(3211, 5523, 0));

		} else if (object.getX() == 3211 && object.getY() == 5523
				&& object.getZ() == 0) {
			player.setLocation(new Location(3208, 5527, 0));

		} else if (object.getX() == 3201 && object.getY() == 5531
				&& object.getZ() == 0) {
			player.setLocation(new Location(3197, 5529, 0));

		} else if (object.getX() == 3197 && object.getY() == 5529
				&& object.getZ() == 0) {
			player.setLocation(new Location(3201, 5531, 0));

		} else if (object.getX() == 3202 && object.getY() == 5515
				&& object.getZ() == 0) {
			player.setLocation(new Location(3196, 5512, 0));

		} else if (object.getX() == 3196 && object.getY() == 5512
				&& object.getZ() == 0) {
			player.setLocation(new Location(3202, 5515, 0));

		} else if (object.getX() == 3190 && object.getY() == 5515
				&& object.getZ() == 0) {
			player.setLocation(new Location(3190, 5519, 0));

		} else if (object.getX() == 3190 && object.getY() == 5519
				&& object.getZ() == 0) {
			player.setLocation(new Location(3190, 5515, 0));

		} else if (object.getX() == 3185 && object.getY() == 5518
				&& object.getZ() == 0) {
			player.setLocation(new Location(3181, 5517, 0));

		} else if (object.getX() == 3181 && object.getY() == 5517
				&& object.getZ() == 0) {
			player.setLocation(new Location(3185, 5518, 0));

		} else if (object.getX() == 3187 && object.getY() == 5531
				&& object.getZ() == 0) {
			player.setLocation(new Location(3182, 5530, 0));

		} else if (object.getX() == 3182 && object.getY() == 5530
				&& object.getZ() == 0) {
			player.setLocation(new Location(3187, 5531, 0));

		} else if (object.getX() == 3169 && object.getY() == 5510
				&& object.getZ() == 0) {
			player.setLocation(new Location(3159, 5501, 0));

		} else if (object.getX() == 3159 && object.getY() == 5501
				&& object.getZ() == 0) {
			player.setLocation(new Location(3169, 5510, 0));

		} else if (object.getX() == 3165 && object.getY() == 5515
				&& object.getZ() == 0) {
			player.setLocation(new Location(3173, 5530, 0));

		} else if (object.getX() == 3173 && object.getY() == 5530
				&& object.getZ() == 0) {
			player.setLocation(new Location(3165, 5515, 0));

		} else if (object.getX() == 3156 && object.getY() == 5523
				&& object.getZ() == 0) {
			player.setLocation(new Location(3152, 5520, 0));

		} else if (object.getX() == 3152 && object.getY() == 5520
				&& object.getZ() == 0) {
			player.setLocation(new Location(3156, 5523, 0));

		} else if (object.getX() == 3148 && object.getY() == 5533
				&& object.getZ() == 0) {
			player.setLocation(new Location(3153, 5537, 0));

		} else if (object.getX() == 3153 && object.getY() == 5537
				&& object.getZ() == 0) {
			player.setLocation(new Location(3148, 5533, 0));

		} else if (object.getX() == 3143 && object.getY() == 5535
				&& object.getZ() == 0) {
			player.setLocation(new Location(3147, 5541, 0));

		} else if (object.getX() == 3147 && object.getY() == 5541
				&& object.getZ() == 0) {
			player.setLocation(new Location(3143, 5535, 0));

		} else if (object.getX() == 3168 && object.getY() == 5541
				&& object.getZ() == 0) {
			player.setLocation(new Location(3171, 5542, 0));

		} else if (object.getX() == 3171 && object.getY() == 5542
				&& object.getZ() == 0) {
			player.setLocation(new Location(3168, 5541, 0));

		} else if (object.getX() == 3190 && object.getY() == 5549
				&& object.getZ() == 0) {
			player.setLocation(new Location(3190, 5554, 0));

		} else if (object.getX() == 3190 && object.getY() == 5554
				&& object.getZ() == 0) {
			player.setLocation(new Location(3190, 5549, 0));

		} else if (object.getX() == 3180 && object.getY() == 5557
				&& object.getZ() == 0) {
			player.setLocation(new Location(3174, 5558, 0));

		} else if (object.getX() == 3174 && object.getY() == 5558
				&& object.getZ() == 0) {
			player.setLocation(new Location(3180, 5557, 0));

		} else if (object.getX() == 3162 && object.getY() == 5557
				&& object.getZ() == 0) {
			player.setLocation(new Location(3158, 5561, 0));

		} else if (object.getX() == 3158 && object.getY() == 5561
				&& object.getZ() == 0) {
			player.setLocation(new Location(3162, 5557, 0));

		} else if (object.getX() == 3166 && object.getY() == 5553
				&& object.getZ() == 0) {
			player.setLocation(new Location(3162, 5545, 0));

		} else if (object.getX() == 3162 && object.getY() == 5545
				&& object.getZ() == 0) {
			player.setLocation(new Location(3166, 5553, 0));

		} else if (object.getX() == 3273 && object.getY() == 5460
				&& object.getZ() == 0) {
			player.setLocation(new Location(3280, 5460, 0));

		} else if (object.getX() == 3280 && object.getY() == 5460
				&& object.getZ() == 0) {
			player.setLocation(new Location(3273, 5460, 0));

			/**
			 * /*Chaos Tunnels Exits
			 */

		} else if (object.getX() == 3142 && object.getY() == 5545
				&& object.getZ() == 0) {
			player.setLocation(new Location(3115, 5528, 0));
		} else if (object.getX() == 3183 && object.getY() == 5470
				&& object.getZ() == 0) {
			player.setLocation(new Location(3059, 3549, 0));

		} else if (object.getX() == 3248 && object.getY() == 5490
				&& object.getZ() == 0) {
			player.setLocation(new Location(3120, 3571, 0));

		} else if (object.getX() == 3292 && object.getY() == 5479
				&& object.getZ() == 0) {
			player.setLocation(new Location(3166, 3561, 0));

		} else if (object.getX() == 3291 && object.getY() == 5538
				&& object.getZ() == 0) {
			player.setLocation(new Location(3166, 3618, 0));

		} else if (object.getX() == 3234 && object.getY() == 5559
				&& object.getZ() == 0) {
			player.setLocation(new Location(3107, 3640, 0));

			/**
			 * /*Bork Entrance
			 */
		} else if (object.getX() == 3115 && object.getY() == 5528
				&& object.getZ() == 0) {
			player.setLocation(new Location(3142, 5545, 0));

			/**
			 * /*
			 */
		} else if (object.getX() == 3120 && object.getY() == 3571
				&& object.getZ() == 0) {
			player.setLocation(new Location(3248, 5490, 0));

		} else if (object.getX() == 3129 && object.getY() == 3587
				&& object.getZ() == 0) {
			player.setLocation(new Location(3234, 5559, 0));
		}
		if (id == 24823) {
			ArtisanWorkshop.GiveSteelIngots(player);
		}
		if (id == 29395) {
			player.interfaces()
					.sendInterface(ArtisanWorkshop.INGOTWITH);
		}

		if (id == 29394) {
			player.interfaces()
					.sendInterface(ArtisanWorkshop.INGOTWITH);
		}

		if (id == 29396) {
			ArtisanWorkshop.DepositArmour(player);
			player.getInventory().refresh();
		}
		if (id == 1111111) {
			player.getCrystalChest().openChest();
		}

		if (id == 45008) {
			player.sm("I can not enter here currently.");
		}
		switch (object.defs().name.toLowerCase()) {
		case "crashed star":
			player.getActionManager().setAction(
					new Mining(object, RockDefinitions.CRASHED_STAR));
			break;
		}
		/**
		 * Random event cages
		 */

		switch (id) {
		case 18342:
			player.getControllerManager().startController("Wilderness");
			player.teleportPlayer(3071, 3648, 0);
			break;
		case 20600:
			player.getControllerManager().startController("Wilderness");
			player.teleportPlayer(3077, 10058, 0);
			break;
		}

		if (objectDef.name.contains("Fairy ring")) {
			FairyRing.refresh(player);
			FairyRing.openFairyRing(player);
		}
		if (id == 6 || id == 29358 || id == 29403) {
			player.getDwarfcannon().preRotationSetup(object);
		}

		if (id == 1317) {
			player.getMatrixDialogues().startDialogue("SpiritTree",
					3636);
		}
		if (objectDef.name.contains("Small obelisk")) {
			player.lock(3);
			player.setNextAnimation(new Animation(8502));
			player.setNextGraphics(new Graphics(1308));
			player.sendMessage("You renew your summoning points.");
			player.getSkills().getLevelFromXP(Skills.SUMMONING);
			player.getSkills().set(Skills.SUMMONING,
					player.getSkills().getLevelFromXP(Skills.SUMMONING));
		}
		// Raking for every patch
		if (id == 8150 && !player.getFaladorHerbPatchRaked()) {
			Farming.startRake(player, id);
		}

		if (id == 7847 && !player.getFaladorFlowerPatchRaked()) {
			Farming.startRake(player, id);
		}

		if (id == 8550 && !player.getFaladorNorthAllotmentPatchRaked()) {
			Farming.startRake(player, id);
		}

		if (id == 8551 && !player.getFaladorSouthAllotmentPatchRaked()) {
			Farming.startRake(player, id);
		}

		// Falador herb patch harvesting
		/*
		 * if (id == 8150 && player.getFaladorHerbPatchRaked()) {
		 * Farming.harvestHerb(player, id,
		 * player.getFaladorHerbPatch()); }
		 */
		if (id == 12475) {
			Underwater.leaveUnderwater(player);
			if (player.getCamelotknightStage() == 4) {
				player.getInventory().addItem(769, 1);
			}
		}
		// Falador north allotment patch harvesting
		if (id == 8550 && player.getFaladorNorthAllotmentPatchRaked()) {
			Farming.harvestAllotment(player, id,
					player.getFaladorNorthAllotmentPatch());
		}

		// Falador south allotment patch harvesting
		if (id == 8551 && player.getFaladorSouthAllotmentPatchRaked()) {
			Farming.harvestAllotment(player, id,
					player.getFaladorSouthAllotmentPatch());
		}

		// Falador flower patch harvesting
		if (id == 7847 && player.getFaladorFlowerPatchRaked()) {
			Farming.harvestFlower(player, id,
					player.getFaladorFlowerPatch());
		}
		/* END FARMING */
		if (id == 31833) {
			player.teleportPlayer(3491, 3090, 0);
			player.sm("The ancient door has taken you back in to the sunlight.");
		}

		if (id == 36306) {
			if (player.isCompletedHoarfrostDepths())
				return;

			/*
			 * Quest binding refresh, moving to stage 3
			 */
			player.setHoarfrostStage(3);
			HoarfrostDepths.ShowBindforHoarfrostDepths(player);
			player.teleportPlayer(3021, 5210, 0);
		}
		if (id == 13932) {
			player.teleportPlayer(2141, 3944, 0);
		}
		if (id == 6481) {
			player.sm("You enter to the tunnel.");
		}
		if (id == 6547) {
			if (id == 6545) {
				player.sm("I can not enter from this side to the pyramid.");
			}
		}

		if (id == 15543 || id == 15545 || id == 15545 || id == 15543
				|| id == 15546 || id == 15547 || id == 15548
				|| id == 15542 || id == 15541 || id == 15539
				|| id == 15544) {
			player.sm("I have searched the bookcase, but there's nothing that I would need.");
		}
		if (id == 35763) {
			if (UzerRuins.isFoundbook()) {
				return;
			}
			if (!player.getInventory().containsItem(757, 1)) {
				player.getInventory().addItem(757, 1);
				player.getInventory().refresh();
				player.getMatrixDialogues()
						.startDialogue("SimpleMessage",
								"You have found the book, show it to Reldo now.");
			} else {
				player.sm("You have the book already, you can show it to Reldo now.");
			}

		}
		if (id == 25154) {
			HoarfrostHollow.enterBossRoom(player);
		}
		if (id == 28716) {
			Summoning.infusePouches(player);
		}
		if (id == 30065) {
			HoarfrostHollow.finish(player);

		}
		// BarbarianOutpostAgility start
		else if (id == 20210)
			BarbarianOutpostAgility.enterObstaclePipe(player, object);
		else if (id == 43526)
			BarbarianOutpostAgility.swingOnRopeSwing(player, object);
		else if (id == 43595 && object.getX() == 2550
				&& object.getY() == 3546)
			BarbarianOutpostAgility
					.walkAcrossLogBalance(player, object);
		else if (id == 20211 && object.getX() == 2538
				&& object.getY() == 3545)
			BarbarianOutpostAgility.climbObstacleNet(player, object);
		else if (id == 2302 && object.getX() == 2535
				&& object.getY() == 3547)
			BarbarianOutpostAgility.walkAcrossBalancingLedge(player,
					object);
		else if (id == 1948)
			BarbarianOutpostAgility.climbOverCrumblingWall(player,
					object);
		else if (id == 43533)
			BarbarianOutpostAgility.runUpWall(player, object);
		else if (id == 43597)
			BarbarianOutpostAgility.climbUpWall(player, object);
		else if (id == 43587)
			BarbarianOutpostAgility.fireSpringDevice(player, object);
		else if (id == 43527)
			BarbarianOutpostAgility.crossBalanceBeam(player, object);
		else if (id == 43531)
			BarbarianOutpostAgility.jumpOverGap(player, object);
		else if (id == 43532)
			BarbarianOutpostAgility.slideDownRoof(player, object);
		else if (id == 43529 && object.getX() >= 2484
				&& object.getY() >= 3417 && object.getX() <= 2487
				&& object.getY() <= 3422 && player.getZ() == 3)
			Agility.PreSwing(player, object);
		else if (id == 69514)
			Agility.RunGnomeBoard(player, object);
		else if (id == 69389)
			Agility.JumpDown(player, object);
		else if (id == 43527)
			BarbarianOutpostAgility.crossBalanceBeam(player, object);
		else if (id == 43531)
			BarbarianOutpostAgility.jumpOverGap(player, object);
		else if (id == 43532)
			BarbarianOutpostAgility.slideDownRoof(player, object);
		else if (id == 65365)
			WildernessAgility.GateWalk(player, object);
		else if (id == 65734)
			WildernessAgility.climbCliff(player, object);
		else if (id == 65362)
			WildernessAgility.enterObstaclePipe(player, object.getX(),
					object.getY());
		else if (id == 64696)
			WildernessAgility.swingOnRopeSwing(player, object);
		else if (id == 64698)
			WildernessAgility.walkLog(player);
		else if (id == 64699)
			WildernessAgility.crossSteppingPalletes(player, object);

		else if (id == HunterEquipment.BOX.getObjectId()) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(19192));
				player.getInventory().addItem(
						HunterEquipment.BOX.getId(), 1);
				player.setTrapAmount(player.getTrapAmount() - 1);
			} else
				player.packets().sendMessage("This isn't your trap.");
		} else if (id == 59463) { // works now
			player.getMatrixDialogues().startDialogue("Crate");
		} else if (id == 4277) {
			// player.sendMessage("You successfully thieve from the stall");
			player.addStopDelay(4);
			player.getInventory().addItem(995, 1270);
			player.setNextAnimation(new Animation(881));
			player.getSkills().addXp(17, 100);
		} else if (id == 2878) { // works now
			player.getMatrixDialogues().startDialogue("Pool");
		} else if (id == HunterEquipment.BRID_SNARE.getObjectId()) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(19192));
				Game.getRegion(object.getRegionId()).removeObject(object);
				player.getInventory().addItem(
						HunterEquipment.BRID_SNARE.getId(), 1);
				player.setTrapAmount(player.getTrapAmount() - 1);
			} else
				player.packets().sendMessage("This isn't your trap.");

		} else if (id == 48496) {
			// player.getInterfaceManager().sendInterface(947);
			new DungeonPartyManager(player);
			player.dungtime = 800;
		} else if (id == 57225) {
			if (player.getX() == 2907) {
				player.getDialogue().start("NexEntrance");
			} else {
				return;
			}
		} else if (id == 46935 && object.getX() == 3090
				&& object.getY() == 3498) {
			TowersPkControler.enter(player);
		}

		if (objectDef.name.contains("fairy ring")) {
			FairyRing.refresh(player);
			FairyRing.openFairyRing(player);
		} else if (object.defs().name.equalsIgnoreCase("Obelisk")
				&& object.getY() > 3527) {
			player.getControllerManager().startController(
					"ObeliskControler", object);
		} else if (id == 2350
				&& (object.getX() == 3352 && object.getY() == 3417 && object
						.getZ() == 0))
			player.useStairs(832, new Location(3177, 5731, 0), 1, 2);
		else if (id == 2353
				&& (object.getX() == 3177 && object.getY() == 5730 && object
						.getZ() == 0))
			player.useStairs(828, new Location(3353, 3416, 0), 1, 2);
		else if (id == 10949 || id == 18994 || id == 18995
				|| id == 18996 || id == 3038 || id == 3245
				|| id == 11933 || id == 11934 || id == 11935
				|| id == 11957 || id == 11958 || id == 11959)
			player.getActionManager().setSkill(
					new Mining(object, RockDefinitions.Tin_Ore));
		else if (id == 37312 || id == 11952 || id == 37310) // gold ore
			player.getActionManager().setSkill(
					new Mining(object, RockDefinitions.Gold_Ore));
		else if (id == 19000 || id == 19001 || id == 19002
				|| id == 37309 || id == 37307 || id == 11954
				|| id == 11955 || id == 11956) // iron ore
			player.getActionManager().setSkill(
					new Mining(object, RockDefinitions.Iron_Ore));
		else if (id == 37306 || id == 2311 || id == 37304
				|| id == 37305) // silver ore
			player.getActionManager().setSkill(
					new Mining(object, RockDefinitions.Silver_Ore));
		else if (id == 10948 || id == 18997 || id == 18998
				|| id == 18999 || id == 14850 || id == 14851
				|| id == 3233 || id == 3032 || id == 11930
				|| id == 11931 || id == 11932) // coal ore
			player.getActionManager().setSkill(
					new Mining(object, RockDefinitions.Coal_Ore));
		else if (id == 18991 || id == 18992 || id == 18993
				|| id == 3027 || id == 3229 || id == 11936
				|| id == 11937 || id == 11938 || id == 11960
				|| id == 11961 || id == 11962) // copper
			player.getActionManager().setSkill(
					new Mining(object, RockDefinitions.Copper_Ore));
		else if (id == 3041 || id == 3280 || id == 11942 || id == 11944) // mithril
			// ore
			player.getActionManager().setSkill(
					new Mining(object, RockDefinitions.Mithril_Ore));
		else if (id == 3273 || id == 3040 || id == 11939 || id == 11941) // adamant
			// ore
			player.getActionManager().setSkill(
					new Mining(object, RockDefinitions.Adamant_Ore));
		else if (id == 14860 || id == 14861)
			player.getActionManager().setSkill(
					new Mining(object, RockDefinitions.Runite_Ore));
		else if (id == 10947)
			player.getActionManager().setSkill(
					new Mining(object, RockDefinitions.Granite_Ore));
		else if (id == 10946)
			player.getActionManager().setSkill(
					new Mining(object, RockDefinitions.Sandstone_Ore));
		else if (id == 11554 || id == 11552)
			player.packets().sendMessage(
					"That rock is currently unavailable.");
		else if (id == 2491)
			player.getActionManager()
					.setSkill(
							new EssenceMining(
									object,
									player.getSkills().getLevel(
											Skills.MINING) < 30 ? EssenceDefinitions.Rune_Essence
											: EssenceDefinitions.Pure_Essence));
		else if (id == 2478)
			Runecrafting.craftEssence(player, 556, 1, 5, false, 11, 2,
					22, 3, 34, 4, 44, 5, 55, 6, 66, 7, 77, 88, 9, 99,
					10);
		else if (id == 26847)
			OuraniaCrafting.getSingleton().craftEssence(player, false);
		else if (id == 2479)
			Runecrafting.craftEssence(player, 558, 2, 5.5, false, 14,
					2, 28, 3, 42, 4, 56, 5, 70, 6, 84, 7, 98, 8);
		else if (id == 2480)
			Runecrafting.craftEssence(player, 555, 5, 6, false, 19, 2,
					38, 3, 57, 4, 76, 5, 95, 6);
		else if (id == 2481)
			Runecrafting.craftEssence(player, 557, 9, 6.5, false, 26,
					2, 52, 3, 78, 4);
		else if (id == 2482)
			Runecrafting.craftEssence(player, 554, 14, 7, false, 35, 2,
					70, 3);
		else if (id == 2483)
			Runecrafting.craftEssence(player, 559, 20, 7.5, false, 46,
					2, 92, 3);
		else if (id == 2484)
			Runecrafting.craftEssence(player, 564, 27, 8, true, 59, 2);
		else if (id == 2487)
			Runecrafting
					.craftEssence(player, 562, 35, 8.5, true, 74, 2);
		else if (id == 17010)
			Runecrafting.craftEssence(player, 9075, 40, 8.7, true, 82,
					2);
		else if (id == 2486)
			Runecrafting.craftEssence(player, 561, 45, 9, true, 91, 2);
		else if (id == 2485)
			Runecrafting.craftEssence(player, 563, 50, 9.5, true);
		else if (id == 2488)
			Runecrafting.craftEssence(player, 560, 65, 10, true);
		else if (id == 30624)
			Runecrafting.craftEssence(player, 565, 77, 10.5, true);
		else if (id == 2452) {
			int hatId = player.getEquipment().getHatId();
			if (hatId == Runecrafting.AIR_TIARA
					|| hatId == Runecrafting.OMNI_TIARA)
				Runecrafting.enterAirAltar(player);
		} else if (id == 2455) {
			int hatId = player.getEquipment().getHatId();
			if (hatId == Runecrafting.EARTH_TIARA
					|| hatId == Runecrafting.OMNI_TIARA)
				Runecrafting.enterEarthAltar(player);
		} else if (id == 2456) {
			int hatId = player.getEquipment().getHatId();
			if (hatId == Runecrafting.FIRE_TIARA
					|| hatId == Runecrafting.OMNI_TIARA)
				Runecrafting.enterFireAltar(player);
		} else if (id == 2454) {
			int hatId = player.getEquipment().getHatId();
			if (hatId == Runecrafting.WATER_TIARA
					|| hatId == Runecrafting.OMNI_TIARA)
				Runecrafting.enterWaterAltar(player);
		} else if (id == 2457) {
			int hatId = player.getEquipment().getHatId();
			if (hatId == Runecrafting.BODY_TIARA
					|| hatId == Runecrafting.OMNI_TIARA)
				Runecrafting.enterBodyAltar(player);
		} else if (id == 2453) {
			int hatId = player.getEquipment().getHatId();
			if (hatId == Runecrafting.MIND_TIARA
					|| hatId == Runecrafting.OMNI_TIARA)
				Runecrafting.enterMindAltar(player);
		} else if (id == 36972) {
			player.setNextAnimation(new Animation(712));
			player.setNextGraphics(new org.nova.game.masks.Graphics(624));
			player.packets().sendMessage("You pray to the gods");
			player.getInventory().deleteItem(536, 1);
			// player.getSkills().addXp(Skills.PRAYER, 300);
		} else if (id == 36972) {
			player.setNextAnimation(new Animation(712));
			player.setNextGraphics(new org.nova.game.masks.Graphics(624));
			player.packets().sendMessage("You pray to the gods");
			player.getInventory().deleteItem(18830, 1);
			// player.getSkills().addXp(Skills.PRAYER, 600);
		} else if (id == 47120) { // zaros altar
			// recharge if needed
			if (player.getPrayer().getPrayerpoints() < player.getSkills()
					.getLevelFromXP(Skills.PRAYER) * 10) {
				player.addStopDelay(12);
				player.setNextAnimation(new Animation(12563));
				player.getPrayer().setPrayerpoints(
						(int) ((player.getSkills().getLevelFromXP(
								Skills.PRAYER) * 10) * 1.15));
				player.getPrayer().refreshPrayerPoints();
			}
			player.getMatrixDialogues().startDialogue("ZarosAltar");
		}
		/*
		 * else if (id == 9369) { if (player.getX() == 2399 &&
		 * player.getY() == 5177) {
		 * FightPitsControler.enterWaitRoom(player);
		 * player.getControlerManager
		 * ().startControler("FightPitsControler"); } else if
		 * (player.getX() == 2399 && player.getY() == 5175)
		 * player.addWalkSteps(2399, 5175, -1, false); }
		 */
		else if (id == 36786)
			player.getMatrixDialogues().startDialogue("Banker", 4907);
		else if (id == 42377 || id == 42378)
			player.getMatrixDialogues().startDialogue("Banker", 2759);
		else if (id == 42217 || id == 782 || id == 34752 || id == 4369)
			player.getMatrixDialogues().startDialogue("Banker", 553);

		else if (id == 42425 && object.getX() == 3220
				&& object.getY() == 3222) { // zaros portal
			player.useStairs(10256, new Location(3353, 3416, 0), 4, 5,
					"And you find yourself into a digsite.");
			player.addWalkSteps(3222, 3223, -1, false);
			player.packets().sendMessage(
					"You examine portal and it aborbs you...");
		} else if (id == 46500 && object.getX() == 3351
				&& object.getY() == 3415) { // zaros portal
			player.useStairs(-1, new Location(
					Constants.RESPAWN_PLAYER_LOCATION.getX(),
					Constants.RESPAWN_PLAYER_LOCATION.getY(),
					Constants.RESPAWN_PLAYER_LOCATION.getZ()), 2, 3,
					"You found your way back to home.");
			player.addWalkSteps(3351, 3415, -1, false);
		} else if (id == 9293) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 70) {
				player.packets()
						.sendMessage(
								"You need an Agility level of 70 in order to use this obstacle.",
								true);
				return;
			}
			int x = player.getX() == 2886 ? 2892 : 2886;
			WorldTasksManager.schedule(new WorldTask() {
				int count = 0;

				@Override
				public void run() {
					player.setNextAnimation(new Animation(844));
					if (count++ == 1)
						stop();
				}

			}, 0, 0);
			player.setNextForceMovement(new ForceMovement(new Location(
					x, 9799, 0), 3, player.getX() == 2886 ? 1 : 3));
			player.useStairs(-1, new Location(x, 9799, 0), 3, 4);
		} else if (id == 29370
				&& (object.getX() == 3150 || object.getX() == 3153)
				&& object.getY() == 9906) { // edgeville dungeon cut
			if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
				player.packets()
						.sendMessage(
								"You need an agility level of 53 to use this obstacle.");
				return;
			}
			final boolean running = player.getRun();
			player.setRunHidden(false);
			player.lock(8);
			player.addWalkSteps(object.getX() == 3150 ? 3155 : 3149,
					9906, -1, false);
			player.packets().sendMessage(
					"You pulled yourself through the pipes.", true);
			WorldTasksManager.schedule(new WorldTask() {
				boolean secondloop;

				@Override
				public void run() {
					if (!secondloop) {
						secondloop = true;
						player.getAppearance().setRenderEmote(295);
					} else {
						player.getAppearance().setRenderEmote(-1);
						player.setRunHidden(running);
						player.getSkills().addXp(Skills.AGILITY, 7);
						stop();
					}
				}
			}, 0, 5);
		}

		else if (id == 2295)
			Agility.walkGnomeLog(player);
		else if (id == 2285)
			Agility.climbGnomeObstacleNet(player);
		else if (id == 35970)
			Agility.climbUpGnomeTreeBranch(player);
		else if (id == 2312)
			Agility.walkGnomeRope(player);
		else if (id == 4059)
			Agility.walkBackGnomeRope(player);
		else if (id == 2314)
			Agility.climbDownGnomeTreeBranch(player);
		else if (id == 2286)
			Agility.climbGnomeObstacleNet2(player);
		else if (id == 43543 || id == 43544)
			Agility.enterGnomePipe(player, object.getX(), object.getY());
		else if (Wilderness.isDitch(id)) {// wild ditch
			player.getMatrixDialogues().startDialogue(
					"WildernessDitch", object);
		} else if (id == 42611) {// Magic Portal
			player.getMatrixDialogues().startDialogue("MagicPortal");
		} else if (id == 27254) {// Edgeville portal
			player.packets().sendMessage("You enter the portal...");
			player.useStairs(10584, new Location(3087, 3488, 0), 2, 3,
					"..and are transported to Edgeville.");
			player.addWalkSteps(1598, 4506, -1, false);
		} else if (id == 15522) {// portal sign
			if (player.withinDistance(new Location(1598, 4504, 0), 1)) {// PORTAL
				// 1
				player.interfaces().sendInterface(327);
				player.packets().sendIComponentText(327, 13,
						"Edgeville");
				player.packets()
						.sendIComponentText(
								327,
								14,
								"This portal will take you to edgeville. There "
										+ "you can multi pk once past the wilderness ditch.");
			}
			if (player.withinDistance(new Location(1598, 4508, 0), 1)) {// PORTAL
				// 2
				player.interfaces().sendInterface(327);
				player.packets().sendIComponentText(327, 13,
						"Mage Bank");
				player.packets()
						.sendIComponentText(
								327,
								14,
								"This portal will take you to the mage bank. "
										+ "The mage bank is a 1v1 deep wilderness area.");
			}
			if (player.withinDistance(new Location(1598, 4513, 0), 1)) {// PORTAL
				// 3
				player.interfaces().sendInterface(327);
				player.packets().sendIComponentText(327, 13,
						"Magic's Portal");
				player.packets()
						.sendIComponentText(
								327,
								14,
								"This portal will allow you to teleport to areas that "
										+ "will allow you to change your magic spell book.");
			}
		} else if (id == 37929) {// corp beast
			if (object.getX() == 2971 && object.getY() == 4382
					&& object.getZ() == 0)
				player.interfaces().sendInterface(650);
			else if (object.getX() == 2918 && object.getY() == 4382
					&& object.getZ() == 0) {
				player.stopAll();
				player.setLocation(new Location(
						player.getX() == 2921 ? 2917 : 2921, player
								.getY(), player.getZ()));
			}
		} else if (id == 37928 && object.getX() == 2883
				&& object.getY() == 4370 && object.getZ() == 0) {
			player.stopAll();
			player.setLocation(new Location(3214, 3782, 0));
			player.getControllerManager().startController("Wilderness");
		} else if (id == 38815 && object.getX() == 3209
				&& object.getY() == 3780 && object.getZ() == 0) {
			if (player.getSkills().getLevelFromXP(Skills.WOODCUTTING) < 37
					|| player.getSkills().getLevelFromXP(Skills.MINING) < 45
					|| player.getSkills().getLevelFromXP(Skills.SUMMONING) < 23
					|| player.getSkills().getLevelFromXP(Skills.FIREMAKING) < 47
					|| player.getSkills().getLevelFromXP(Skills.PRAYER) < 55) {
				player.packets()
						.sendMessage(
								"You need 23 Summoning, 37 Woodcutting, 45 Mining, 47 Firemaking and 55 Prayer to enter this dungeon.");
				return;
			}
			player.stopAll();
			player.setLocation(new Location(2885, 4372, 0));
			player.getControllerManager().forceStop();
		} else if (id == 9369) {
			player.getControllerManager().startController("FightPits");
		} else if (id == 1817 && object.getX() == 2273
				&& object.getY() == 4680) { // kbd lever
			Magic.pushLeverTeleport(player,
					new Location(3067, 10254, 0));
		} else if (id == 1816 && object.getX() == 3067
				&& object.getY() == 10252) { // kbd out lever
			Magic.pushLeverTeleport(player, new Location(2273, 4681, 0));
		} else if (id == 9356) {
			player.getMatrixDialogues().startDialogue("JadEnter");

		} else if (id == 28698) {
			player.getMatrixDialogues().startDialogue("LunarAltar");
		} else if (id == 32015 && object.getX() == 3069
				&& object.getY() == 10256) { // kbd stairs
			player.useStairs(828, new Location(3017, 3848, 0), 1, 2);
			player.getControllerManager().startController("Wilderness");
		} else if (id == 1765 && object.getX() == 3017
				&& object.getY() == 3849) { // kbd out stairs
			player.stopAll();
			player.setLocation(new Location(3069, 10255, 0));
			player.getControllerManager().forceStop();
		} else if (id == 14315) {
			player.getControllerManager().startController(
					"PestControlLobby", 1);
		} else if (id == 5959) {
			Magic.pushLeverTeleport(player, new Location(2539, 4712, 0));
		} else if (id == 5960) {
			Magic.pushLeverTeleport(player, new Location(3089, 3957, 0));
		} else if (id == 62681)
			player.getDominionTower().viewScoreBoard();
		else if (id == 2273) {
			player.setLocation(new Location(2851, 5933, 0));
			player.sm("Use your fire cape on the floating orb to bring out Har'Arken.");
			player.sm("WARNING     WARNING     WARNING     WARNING     WARNING     WARNING     WARNING");
			player.sm("You will lose your fire cape and not be able to get it back, but gain the kiln cape if you win!");
		} else if (id == 62678 || id == 62679)
			player.getDominionTower().openModes();
		else if (id == 62688)
			player.getMatrixDialogues().startDialogue("DTClaimRewards");
		else if (id == 62677)
			player.getDominionTower().talkToFace();
		else if (id == 62680)
			player.getDominionTower().openBankChest();
		else if (id == 62676) { // dominion exit
			player.useStairs(-1, new Location(3374, 3093, 0), 0, 1);
		} else if (id == 62674) { // dominion entrance
			player.useStairs(-1, new Location(3744, 6405, 0), 0, 1);

		} else if (id == 14315) {
			if (Lander.canEnter(player, 0))
				return;
		} else if (id == 25631) {
			if (Lander.canEnter(player, 1))
				return;
		} else if (id == 25632) {
			if (Lander.canEnter(player, 2))
				return;
		} else if (id == 2878 || id == 2879) {
			player.getMatrixDialogues().startDialogue("SimpleMessage",
					"You feel the energy rush through your veins.");
			final boolean isLeaving = id == 2879;
			final Location tile = isLeaving ? new Location(2509, 4687,
					0) : new Location(2542, 4720, 0);
			player.setNextForceMovement(new ForceMovement(player, 1,
					tile, 2, isLeaving ? ForceMovement.SOUTH
							: ForceMovement.NORTH));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.setNextAnimation(new Animation(13842));
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							player.setNextAnimation(new Animation(-1));
							player.setLocation(isLeaving ? new Location(
									2542, 4718, 0) : new Location(2509,
									4689, 0));
						}
					}, 2);
				}
			});
		} else if (id == 24991) {
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.getControllerManager().startController(
							"PuroPuro");
				}
			}, 10);
			Magic.sendTeleportSpell(player, 6601, -1, 1118, -1, 0, 0,
					new Location(2591, 4320, 0), 9, false,
					Magic.OBJECT_TELEPORT);

		} else if (id == 18050) {
			return;
		} else if (id == 77574 || id == 77573) {
			boolean back = id == 77573;
			player.lock(4);
			final Location tile = back ? new Location(2687, 9506, 0)
					: new Location(2682, 9506, 0);
			final boolean isRun = player.isRunning();
			player.setRun(false);
			player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.setRun(isRun);
				}
			}, 4);
		} else {
			switch (objectDef.name.toLowerCase()) {
			case "web":
				if (objectDef.containsOption(0, "Slash")) {
					player.setNextAnimation(new Animation(PlayerCombat
							.getWeaponAttackEmote(player.getEquipment()
									.getWeaponId(), player
									.getCombatDefinitions()
									.getAttackStyle())));
					Passages.slashWeb(player, object);
				}
				break;
			case "bank booth":
				if (objectDef.containsOption(0, "Bank")
						|| objectDef.containsOption(1, "Use-quickly"))
					player.getBank().openBank();
				if (objectDef.containsOption(2, "Colect")) {
					player.interfaces().sendInterface(109);
				}
				break;

			case "bank chest":
				if (objectDef.containsOption(0, "Use"))
					player.getBank().openBank();
				// case "bank deposit box":
				// if (objectDef.containsOption(0, "Deposit"))
				// player.getBank().openDepositBox();
				// break;
			case "bank":
				player.getBank().openBank();
				break;
			// Woodcutting start
			case "tree":
				if (objectDef.containsOption(0, "Chop down"))
					player.getActionManager().setSkill(
							new Woodcutting(object,
									TreeDefinitions.NORMAL));
				break;
			case "dead tree":
				if (objectDef.containsOption(0, "Chop down"))
					player.getActionManager().setSkill(
							new Woodcutting(object,
									TreeDefinitions.DEAD));
				break;
			case "oak":
				if (objectDef.containsOption(0, "Chop down"))
					player.getActionManager()
							.setSkill(
									new Woodcutting(object,
											TreeDefinitions.OAK));
				break;
			case "willow":
				if (objectDef.containsOption(0, "Chop down"))
					player.getActionManager().setSkill(
							new Woodcutting(object,
									TreeDefinitions.WILLOW));
				break;
			case "maple tree":
				if (objectDef.containsOption(0, "Chop down"))
					player.getActionManager().setSkill(
							new Woodcutting(object,
									TreeDefinitions.MAPLE));
				break;
			case "ivy":
				if (objectDef.containsOption(0, "Chop"))
					player.getActionManager()
							.setSkill(
									new Woodcutting(object,
											TreeDefinitions.IVY));
				break;
			case "yew":
				if (objectDef.containsOption(0, "Chop down"))
					player.getActionManager()
							.setSkill(
									new Woodcutting(object,
											TreeDefinitions.YEW));
				break;
			case "magic tree":
				if (objectDef.containsOption(0, "Chop down"))
					player.getActionManager().setSkill(
							new Woodcutting(object,
									TreeDefinitions.MAGIC));
				break;
			case "cursed magic tree":
				if (objectDef.containsOption(0, "Chop down"))
					player.getActionManager().setSkill(
							new Woodcutting(object,
									TreeDefinitions.CURSED_MAGIC));
				break;
			// Woodcutting end
			case "gate":
			case "large door":
			case "tree door":
			case "metal door":
				if (id == 21600) {
					Game.removeObject(object, true);
					return;
				}

				if (object.getType() == 0
						&& objectDef.containsOption(0, "Open"))
					Passages.passGate(player, object);
				break;
			case "door":
				if (id == 21507 || id == 21505) {
					Game.removeObject(object, true);
					return;
				}

				if (object.getType() == 0
						&& (objectDef.containsOption(0, "Open") || objectDef
								.containsOption(0, "Unlock")))
					Passages.passDoor(player, object);
				break;
			case "ladder":
				if (id == 21512 || id == 21514)
					return;
				if (id == 32015) {
					player.useStairs(828, new Location(2562, 3357, 0),
							1, 2);
					player.hints().remove();
					break;
				} else if (id == 1759) {
					player.useStairs(828, new Location(2562, 9755, 0),
							1, 2);
					player.hints().remove();
					break;
				} else
					Passages.climbLadder(player, object, 1);
				break;
			case "staircase":
				Passages.navigateStaircase(player, object, 1);
				break;
			case "altar":
				if (objectDef.containsOption(0, "Pray-at")) {
					final int maxPrayer = player.getSkills()
							.getLevelFromXP(Skills.PRAYER) * 10;
					if (player.getPrayer().getPrayerpoints() < maxPrayer) {
						player.addStopDelay(5);
						player.packets().sendMessage(
								"You pray to the gods...", true);
						player.setNextAnimation(new Animation(645));
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								player.getPrayer().restorePrayer(
										maxPrayer);
								player.packets()
										.sendMessage(
												"...and recharged your prayer.",
												true);
							}
						}, 2);
					} else {
						player.packets().sendMessage(
								"You already have full prayer.", true);
					}
					if (id == 6552)
						player.getMatrixDialogues().startDialogue(
								"AncientAltar");
				}
				break;
			default:

				break;
			}
		}
	}

}
