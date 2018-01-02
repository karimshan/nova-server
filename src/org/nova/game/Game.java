package org.nova.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.nova.Constants;
import org.nova.Main;
import org.nova.game.engine.GameTick;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.entity.EntityList;
import org.nova.game.item.FloorItem;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.map.Region;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.SlayerHelp;
import org.nova.game.npc.arena.KolodionMage;
import org.nova.game.npc.corp.CorporealBeast;
import org.nova.game.npc.dragons.KingBlackDragon;
import org.nova.game.npc.godwars.GodWarMinion;
import org.nova.game.npc.godwars.armadyl.KreeArra;
import org.nova.game.npc.godwars.bandos.GeneralGraardor;
import org.nova.game.npc.godwars.saradomin.CommanderZilyana;
import org.nova.game.npc.godwars.saradomin.GodwarsSaradominFaction;
import org.nova.game.npc.godwars.zammorak.GodwarsZammorakFaction;
import org.nova.game.npc.godwars.zammorak.KrilTstsaroth;
import org.nova.game.npc.godwars.zaros.Nex;
import org.nova.game.npc.godwars.zaros.NexMinion;
import org.nova.game.npc.jad.TzTokJad;
import org.nova.game.npc.kalph.KalphiteQueen;
import org.nova.game.npc.normal.Jadinko;
import org.nova.game.npc.normal.Polypore;
import org.nova.game.npc.normal.QBD;
import org.nova.game.npc.normal.SkeletonMonkey;
import org.nova.game.npc.normal.Slayer;
import org.nova.game.npc.others.Glacor;
import org.nova.game.npc.others.HuntNPC;
import org.nova.game.npc.others.Lucien;
import org.nova.game.npc.others.Nomad;
import org.nova.game.npc.others.SeaTroll;
import org.nova.game.npc.others.TormentedDemon;
import org.nova.game.npc.others.ZMINPC;
import org.nova.game.player.OwnedObjectManager;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Hunter.HunterNPC;
import org.nova.game.player.content.ItemConstants;
import org.nova.game.player.content.ShootingStar;
import org.nova.game.player.content.exchange.GrandExchange;
import org.nova.game.player.content.minigames.GodWarsBosses;
import org.nova.game.player.content.minigames.ZarosGodwars;
import org.nova.game.player.content.minigames.clanwars.FfaZone;
import org.nova.game.player.content.minigames.clanwars.RequestController;
import org.nova.game.player.controlers.PuroPuro;
import org.nova.game.player.controlers.Wilderness;
import org.nova.kshan.bot.Bot;
import org.nova.kshan.content.areas.Area;
import org.nova.network.stream.AntiFlood;
import org.nova.utility.ShopsHandler;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;
import org.nova.utility.misc.Misc.EntityDirection;

/**
 * Represents the class that holds the game world constants
 * 
 * @author K-Shan
 *
 */
public final class Game {

	public static int exiting_delay;
	public static long exiting_start;
	public static boolean initing = false;

	private static final EntityList<Player> players = new EntityList<Player>(
			Constants.PLAYERS_LIMIT);
	private static final EntityList<NPC> npcs = new EntityList<NPC>(
			Constants.NPCS_LIMIT);

	private static final Map<Integer, Region> regions = Collections
			.synchronizedMap(new HashMap<Integer, Region>());

	private static final EntityList<NPC> spawnedNPCS = new EntityList<NPC>(
			32000);

	public static final EntityList<NPC> getSpawnedNPCs() {
		return spawnedNPCS;
	}

	public static final EntityList<Player> playersInWilderness = new EntityList<Player>(
			Constants.PLAYERS_LIMIT);

	public static void spawnReplacedObject(GlobalObject object, int slot,
			boolean clip) {
		int regionId = object.getRegionId();
		GlobalObject placement = getRegion(regionId, true).getSpawnedObject(
				object, slot);
		if (placement == null) {
			Game.getRegion(regionId).addObject(object);
			if (clip) {
				int baseLocalX = object.getX() - ((regionId >> 8) * 64);
				int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
				Game.getRegion(regionId).addMapObject(object, baseLocalX,
						baseLocalY);
			}
		} else {
			if (placement.matchesObject(object))
				Game.removeObject(object, clip);
			Game.getRegion(regionId).addObject(object);
			if (clip) {
				int baseLocalX = object.getX() - ((regionId >> 8) * 64);
				int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
				Game.getRegion(regionId).addMapObject(object, baseLocalX,
						baseLocalY);
			}
		}
		synchronized (Game.getPlayers()) {
			for (Player p2 : Game.getPlayers()) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished()
						|| !p2.getMapRegionsIds().contains(regionId))
					continue;
				p2.packets().sendSpawnedObject(object);
			}
		}
	}

	public static void spawnReplacedObject(GlobalObject object, boolean clip) {
		int regionId = object.getRegionId();
		GlobalObject placement = Game
				.getRegion(regionId, true)
				.getSpawnedObject(object, Region.OBJECT_SLOTS[object.getType()]);
		if (placement == null) {
			Game.getRegion(regionId).addObject(object);
			if (clip) {
				int baseLocalX = object.getX() - ((regionId >> 8) * 64);
				int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
				Game.getRegion(regionId).addMapObject(object, baseLocalX,
						baseLocalY);
			}
		} else {
			if (placement.matchesObject(object)) {
				Game.removeObject(placement, clip);
				Game.getRegion(regionId, true).getSpawnedObjects()
						.remove(placement);
			}
			Game.getRegion(regionId).addObject(object);
			if (clip) {
				int baseLocalX = object.getX() - ((regionId >> 8) * 64);
				int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
				Game.getRegion(regionId).addMapObject(object, baseLocalX,
						baseLocalY);
			}
		}
		synchronized (Game.getPlayers()) {
			for (Player p2 : Game.getPlayers()) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished()
						|| !p2.getMapRegionsIds().contains(regionId))
					continue;
				p2.packets().sendSpawnedObject(object);
			}
		}
	}

	public static final void init() {
		addRestoreRunEnergyTask();
		addRestoreHitPointsTask();
		addRestoreSkillsTask();
		addRestoreSpecialAttackTask();
		addRestoreShopItemsTask();
		addSummoningEffectTask();
		addDrainPrayerTask();
		addOwnedObjectsTask();
		// spawnStar();
		addMessageCheckTask();
		PuroPuro.initPuroImplings();
		addGrowingTask();
	}

	public static final void spawnTemporaryObject(final GlobalObject object,
			long time) {
		spawnTemporaryObject(object, time, false);
	}

	public static final void spawnTemporaryObject(final GlobalObject object,
			long time, final boolean clip) {
		final int regionId = object.getRegionId();
		GlobalObject realMapObject = getRegion(regionId).getRealObject(object);
		// remakes object, has to be done because on static region coords arent
		// same of real
		final GlobalObject realObject = realMapObject == null ? null
				: new GlobalObject(realMapObject.getId(),
						realMapObject.getType(), realMapObject.getRotation(),
						object.getX(), object.getY(), object.getZ());
		spawnObject(object, clip);
		final int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		final int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		if (realObject != null && clip)
			getRegion(regionId).removeMapObject(realObject, baseLocalX,
					baseLocalY);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					getRegion(regionId).removeObject(object);
					if (clip) {
						getRegion(regionId).removeMapObject(object, baseLocalX,
								baseLocalY);
						if (realObject != null) {
							int baseLocalX = object.getX()
									- ((regionId >> 8) * 64);
							int baseLocalY = object.getY()
									- ((regionId & 0xff) * 64);
							getRegion(regionId).addMapObject(realObject,
									baseLocalX, baseLocalY);
						}
					}
					for (Player p2 : players) {
						if (p2 == null || !p2.hasStarted() || p2.hasFinished()
								|| !p2.getMapRegionsIds().contains(regionId))
							continue;
						if (realObject != null)
							p2.packets().sendSpawnedObject(realObject);
						else
							p2.packets().sendDestroyObject(object);
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}

	public static final boolean isSpawnedObject(GlobalObject object) {
		final int regionId = object.getRegionId();
		GlobalObject spawnedObject = getRegion(regionId).getSpawnedObject(
				object);
		if (spawnedObject != null && object.getId() == spawnedObject.getId() && object.matchesObject(spawnedObject))
			return true;
		return false;
	}

	public static final boolean removeTemporaryObject(
			final GlobalObject object, long time, final boolean clip) {
		final int regionId = object.getRegionId();
		// remakes object, has to be done because on static region coords arent
		// same of real
		final GlobalObject realObject = object == null ? null
				: new GlobalObject(object.getId(), object.getType(),
						object.getRotation(), object.getX(), object.getY(),
						object.getZ());
		removeObject(object, clip);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					getRegion(regionId).removeRemovedObject(object);
					if (clip) {
						int baseLocalX = object.getX() - ((regionId >> 8) * 64);
						int baseLocalY = object.getY()
								- ((regionId & 0xff) * 64);
						getRegion(regionId).addMapObject(realObject,
								baseLocalX, baseLocalY);
					}
					for (Player p2 : players) {
						if (p2 == null || !p2.hasStarted() || p2.hasFinished()
								|| !p2.getMapRegionsIds().contains(regionId))
							continue;
						p2.packets().sendSpawnedObject(realObject);
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);

		return true;
	}

	/**
	 * Registers an event.
	 * 
	 * @param event
	 */
	public static void submit(final GameTick task) {
		submit(task, task.getDelay());
	}

	public static void submit(Player player, final GameTick tick) {
		player.getGameTicks().add(tick);
		submit(player, tick, tick.getDelay());
	}

	public static void submit(final Player player, final GameTick tick,
			final double delay) {
		Main.scheduleTask(new Runnable() {
			@Override
			public void run() {
				if (!tick.isStopped())
					tick.executeTask();
				else {
					player.getGameTicks().remove(tick);
					return;
				}
				if (!tick.isStopped())
					submit(tick, tick.getDelay());
			}
		}, (long) (delay * 1000), TimeUnit.MILLISECONDS);
	}

	public static void submit(Bot bot, final GameTick task) {
		bot.getBotActionHandler().setCurrentTick(task);
		submit(task, task.getDelay());
	}

	/**
	 * Registers an event.
	 * 
	 * @param event
	 */
	public static void submit(final GameTick tick, final double delay) {
		Main.scheduleTask(new Runnable() {
			@Override
			public void run() {
				if (!tick.isStopped())
					tick.executeTask();
				else
					return;
				if (!tick.isStopped())
					submit(tick, tick.getDelay());
			}
		}, (long) (delay * 1000), TimeUnit.MILLISECONDS);
	}

	public static final void removeObject(GlobalObject object, boolean clip) {
		int regionId = object.getRegionId();
		getRegion(regionId).addRemovedObject(object);
		if (clip) {
			int baseLocalX = object.getX() - ((regionId >> 8) * 64);
			int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
			getRegion(regionId).removeMapObject(object, baseLocalX, baseLocalY);
		}
		synchronized (players) {
			for (Player p2 : players) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished()
						|| !p2.getMapRegionsIds().contains(regionId))
					continue;
				p2.packets().sendDestroyObject(object);
			}
		}
	}

	public static final GlobalObject getObject(Location tile) {
		int regionId = tile.getRegionId();
		int baseLocalX = tile.getX() - ((regionId >> 8) * 64);
		int baseLocalY = tile.getY() - ((regionId & 0xff) * 64);
		return getRegion(regionId).getObject(tile.getZ(), baseLocalX, baseLocalY);
	}

	public static final GlobalObject getObject(Location tile, int type) {
		int regionId = tile.getRegionId();
		int baseLocalX = tile.getX() - ((regionId >> 8) * 64);
		int baseLocalY = tile.getY() - ((regionId & 0xff) * 64);
		return getRegion(regionId).getObject(tile.getZ(), baseLocalX,
				baseLocalY, type);
	}

	public static final void spawnObject(GlobalObject object, boolean clip) {
		int regionId = object.getRegionId();
		getRegion(regionId, true).addObject(object);
		if (clip) {
			int baseLocalX = object.getX() - ((regionId >> 8) * 64);
			int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
			getRegion(regionId, true).addMapObject(object, baseLocalX,
					baseLocalY);
		}
		synchronized (players) {
			for (Player p2 : players) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished()
						|| !p2.getMapRegionsIds().contains(regionId))
					continue;
				p2.packets().sendSpawnedObject(object);
			}
		}
	}

	public static final GlobalObject getSpawnedObject(Location tile) {
		return getRegion(tile.getRegionId()).getSpawnedObject(tile);
	}

	public static final void instantSendGroundItems() {
		try {
			for (Player players : Game.getPlayers()) {
				Game.updateEntityRegion(players);
				drop(players, 946, 3100, 3957); // Mage Bank Knife -not in rs
				drop(players, 2333, 3042, 3952); // half a redberry pie
				drop(players, 125, 3026, 10325); // attack potion 1
				drop(players, 526, 3004, 3923); // bones
				drop(players, 526, 3002, 3922); // bones
				drop(players, 526, 2995, 3922); // bones
				drop(players, 526, 2994, 3924); // bones
				drop(players, 526, 2992, 3920); // bones
				drop(players, 564, 2947, 3899); // cosmic rune
				drop(players, 555, 2961, 3898); // water rune
				drop(players, 526, 3236, 3940); // bones
				drop(players, 526, 3250, 3953); // bones
				drop(players, 526, 3232, 3948); // bones
				drop(players, 53, 3235, 3949); // headless arrows
				drop(players, 53, 3237, 3949); // headless arrows
				drop(players, 53, 3240, 3940); // headess arrows

				drop(players, 444, 3296, 3933); // gold ore
				drop(players, 444, 3292, 3931); // gold ore
				drop(players, 444, 3291, 3927); // gold ore
				drop(players, 444, 3287, 3932); // gold ore
				drop(players, 444, 3283, 3936); // gold ore
				drop(players, 1203, 3279, 3938); // iron dagger
				drop(players, 1173, 3294, 3935); // bronze sq

				drop(players, 528, 3321, 3892); // burnt bones
				drop(players, 528, 3316, 3888); // burnt bones
				drop(players, 528, 3313, 3890); // burnt bones
				drop(players, 528, 3292, 3887); // burnt bones
				drop(players, 528, 3291, 3888); // burnt bones
				drop(players, 528, 3290, 3890); // burnt bones
				drop(players, 528, 3288, 3891); // burnt bones
				drop(players, 528, 3287, 3887); // burnt bones
				drop(players, 565, 3296, 3889); // blood rune

				drop(players, 1607, 3170, 3885); // sapphire

				drop(players, 562, 3151, 3831); // chaos rune
				drop(players, 562, 3137, 3830); // chaos rune
				drop(players, 562, 3144, 3825); // chaos rune
				drop(players, 562, 3149, 3822); // chaos rune
				drop(players, 562, 3145, 3814); // chaos rune
				drop(players, 562, 3139, 3814); // chaos rune

				drop(players, 1153, 3069, 3831); // iron full helm
				drop(players, 995, 3079, 3845); // coins
				drop(players, 995, 3108, 3859); // coins
				drop(players, 1385, 3100, 3862); // staff of earth
				drop(players, 1119, 3085, 3859); // steel platebody
				drop(players, 559, 3091, 3865); // body rune
				drop(players, 1654, 3065, 3864); // gold neclase
				drop(players, 229, 3196, 3849); // vial
				drop(players, 229, 3188, 3839); // vial
				drop(players, 1217, 3180, 3824); // black dagger
				drop(players, 239, 3214, 3809); // white berry
				drop(players, 239, 3218, 3814); // white berry

				drop(players, 528, 3219, 3816); // burnt bones
				drop(players, 528, 3218, 3818); // burnt bones
				drop(players, 528, 3214, 3820); // burnt bones
				drop(players, 528, 3213, 3821); // burnt bones
				drop(players, 995, 3220, 3826); // coins
				drop(players, 995, 3221, 3829); // coins
				drop(players, 1191, 3247, 3795); // iron kiteshield
				drop(players, 561, 3309, 3858); // nature rune
				drop(players, 561, 3311, 3856); // nature rune

				drop(players, 960, 3271, 3767); // plank
				drop(players, 960, 3279, 3764); // plank
				drop(players, 960, 3209, 3681); // plank
				drop(players, 960, 3215, 3680); // plank
				drop(players, 960, 3215, 3669); // plank
				drop(players, 960, 3232, 3690); // plank
				drop(players, 960, 3244, 3679); // plank

				drop(players, 528, 3180, 3853); // burnt bones
				drop(players, 528, 3179, 3852); // burnt bones
				drop(players, 528, 3180, 3850); // burnt bones
				drop(players, 528, 3185, 3857); // burnt bones
				drop(players, 528, 3187, 3857); // burnt bones
				drop(players, 528, 3184, 3849); // burnt bones

				drop(players, 966, 3226, 3670); // tile
				drop(players, 966, 3245, 3682); // tile
				drop(players, 966, 3235, 3698); // tile
				drop(players, 966, 3279, 3767); // tile
				drop(players, 966, 3210, 3684); // tile
				drop(players, 964, 3253, 3764); // skull
				drop(players, 964, 3258, 3733); // skull
				drop(players, 964, 3256, 3744); // skull
				drop(players, 964, 3247, 3718); // skull
				drop(players, 964, 3240, 3750); // skull

				drop(players, 526, 2999, 10341); // bones
				drop(players, 526, 3003, 10340); // bones
				drop(players, 526, 3002, 10343); // bones
				drop(players, 526, 3005, 10343); // bones

				drop(players, 526, 3236, 3740); // bones
				drop(players, 526, 3255, 3761); // bones
				drop(players, 526, 3236, 3750); // bones
				drop(players, 526, 3257, 3743); // bones
				drop(players, 526, 3256, 3733); // bones
				drop(players, 526, 3242, 3722); // bones
				drop(players, 526, 3247, 3726); // bones
				drop(players, 526, 3249, 3732); // bones
				drop(players, 526, 3249, 3742); // bones
				drop(players, 526, 3247, 3751); // bones
				drop(players, 526, 3226, 3751); // bones
				drop(players, 526, 3236, 3728); // bones
				drop(players, 526, 3272, 3690); // bones
				drop(players, 526, 3268, 3686); // bones
				drop(players, 526, 3273, 3688); // bones
				drop(players, 526, 3277, 3688); // bones
				drop(players, 526, 3272, 3683); // bones
				drop(players, 526, 3276, 3682); // bones
				drop(players, 526, 3275, 3680); // bones
				drop(players, 526, 3268, 3679); // bones
				drop(players, 526, 3275, 3675); // bones
				drop(players, 526, 3277, 3676); // bones

				drop(players, 532, 3273, 3676); // big bones
				drop(players, 532, 3267, 3678); // big bones
				drop(players, 532, 3274, 3686); // big bones
				drop(players, 532, 3268, 3689); // big bones
				drop(players, 444, 3230, 3743); // gold ore
				drop(players, 952, 3247, 3735); // spade
				drop(players, 1069, 3249, 3739); // steel platelegs
				drop(players, 29, 3209, 3737); // tinderbox

				drop(players, 1207, 3219, 3682); // steel dagger
				drop(players, 1203, 3217, 3693); // iron dagger
				drop(players, 1137, 3239, 3694); // iron helm
				drop(players, 831, 3235, 3673); // crosbow

				// wildy day 2
				drop(players, 960, 3275, 3664); // plank
				drop(players, 960, 3291, 3652); // plank

				drop(players, 526, 3273, 3664); // bones
				drop(players, 526, 3273, 3660); // bones
				drop(players, 526, 3280, 3657); // bones
				drop(players, 526, 3280, 3658); // bones
				drop(players, 526, 3281, 3658); // bones
				drop(players, 526, 3278, 3657); // bones
				drop(players, 526, 3279, 3659); // bones
				drop(players, 526, 3271, 3658); // bones
				drop(players, 526, 3269, 3660); // bones
				drop(players, 526, 3246, 3615); // bones
				drop(players, 526, 3244, 3611); // bones
				drop(players, 526, 3237, 3611); // bones
				drop(players, 526, 3235, 3607); // bones
				drop(players, 526, 3234, 3608); // bones
				drop(players, 526, 3235, 3605); // bones
				drop(players, 526, 3236, 3604); // bones
				drop(players, 526, 3237, 3604); // bones
				drop(players, 526, 3237, 3603); // bones
				drop(players, 526, 3238, 3603); // bones
				drop(players, 526, 3239, 3604); // bones
				drop(players, 526, 3235, 3603); // bones
				drop(players, 526, 3233, 3605); // bones
				drop(players, 526, 3232, 3605); // bones
				drop(players, 526, 2978, 3763); // bones
				drop(players, 526, 2980, 3764); // bones

				drop(players, 882, 3110, 3611); // bronze arrow
				drop(players, 882, 3109, 3604); //
				drop(players, 882, 3105, 3607); //
				drop(players, 882, 3101, 3606); //
				drop(players, 882, 3103, 3600); //
				drop(players, 882, 3098, 3600); //
				drop(players, 882, 3103, 3596); //
				drop(players, 882, 3098, 3596); //
				drop(players, 882, 3101, 3592); //

				drop(players, 995, 3097, 3578); // coins
				drop(players, 995, 3099, 3572); // coins
				drop(players, 995, 3098, 3564); // coins
				drop(players, 955, 3099, 3557); // coins
				drop(players, 995, 3103, 3548); // coins
				drop(players, 995, 3108, 3534); // coins

				drop(players, 966, 3278, 3664); // tile
				drop(players, 966, 3281, 3663); // tile
				drop(players, 966, 3271, 3656); // tile
				drop(players, 966, 3289, 3651); // tile

				drop(players, 1281, 3283, 3658); // steelsword
				drop(players, 559, 3223, 3583); // body rune
				drop(players, 559, 3232, 3574); // body rune
				drop(players, 559, 3229, 3566); // body rune
				drop(players, 995, 3233, 3556); // coins

				drop(players, 557, 3032, 3637); // earth
				drop(players, 556, 3030, 3637); // air
				drop(players, 555, 3028, 3637); // water
				drop(players, 554, 3026, 3637); // fire
				drop(players, 558, 3023, 3640); // mind
				drop(players, 562, 3021, 3640); // chaos
				drop(players, 559, 3021, 3637); // body

				drop(players, 1985, 3039, 3707); // cheese
				drop(players, 1982, 3039, 3706); // tomato

				drop(players, 5341, 2995, 3707); // rake
				drop(players, 313, 2998, 3705); // fishing bait
				drop(players, 5343, 2997, 3703); // seeddibber
				drop(players, 303, 2999, 3701); // snamm fish net

				drop(players, 966, 2963, 3706); // tile
				drop(players, 960, 2964, 3704); // plank

				drop(players, 1539, 2992, 3688); // steel nails
				drop(players, 2347, 2992, 3686); // hammer
				drop(players, 2347, 2985, 3685); // hammer
				drop(players, 2347, 2957, 3708); // hammer
				drop(players, 313, 2988, 3683); // fish bait
				drop(players, 1887, 2987, 3686); // cake tin
				drop(players, 1539, 2986, 3680); // steel nails
				drop(players, 1539, 2981, 3689); // steel nails
				drop(players, 1925, 2981, 3691); // bucket
				drop(players, 1925, 2970, 3705); // bucket
				drop(players, 2353, 2986, 3681); // steel bar
				drop(players, 966, 2976, 3682); // tile
				drop(players, 960, 2969, 3682); // plank
				drop(players, 966, 2983, 3697); // tile
				drop(players, 960, 2983, 3700); // plank
				drop(players, 966, 2963, 3706); // tile
				drop(players, 960, 2974, 3708); // plank
				drop(players, 960, 2964, 3704); // plank
				drop(players, 526, 2962, 3697); // bones
				drop(players, 960, 2961, 3702); // plank

				drop(players, 966, 2957, 3697); // tile
				drop(players, 966, 2959, 3697); // tile
				drop(players, 960, 2957, 3699); // plank
				drop(players, 966, 2955, 3700); // tile
				drop(players, 966, 2956, 3702); // tile
				drop(players, 960, 2956, 3702); // plank
				drop(players, 960, 2952, 3704); // plank
				drop(players, 995, 2990, 3675); // coins
				drop(players, 995, 2985, 3675); // coins
				drop(players, 995, 2979, 3675); // coins
				// kbd
				drop(players, 1607, 2271, 4695); // sappfire
				drop(players, 1365, 2267, 4700); // gold ring
				drop(players, 995, 2283, 4698); // coins
				drop(players, 995, 2280, 4692); // coins
				drop(players, 995, 2282, 4686); // coins
				drop(players, 995, 2273, 4686); // coins
				drop(players, 995, 2265, 4684); // coins
				drop(players, 995, 2261, 4689); // coins
				drop(players, 995, 2260, 4698); // coins
				drop(players, 995, 2262, 4704); // coins
				drop(players, 995, 2274, 4706); // coins
				drop(players, 995, 2269, 4701); // coins
				drop(players, 995, 2270, 4702); // coins
				drop(players, 995, 2267, 4699); // coins
				drop(players, 995, 2268, 4696); // coins
				drop(players, 995, 2272, 4695); // coins
				drop(players, 995, 2274, 4696); // coins
				drop(players, 995, 2273, 4698); // coins
				drop(players, 995, 2273, 4699); // coins
				drop(players, 995, 2273, 4701); // coins
				// edge
				drop(players, 1420, 3112, 3519); // iron mace
				drop(players, 1059, 3097, 3486); // leather gloves
				// monstary guild
				drop(players, 544, 3059, 3487); // monk top
				drop(players, 542, 3059, 3488); // monk skirt
				// lumbridge
				drop(players, 558, 3206, 3208); // lumbridge mind rune
				drop(players, 882, 3205, 3227); // lumbridge bronze arrow
				drop(players, 1923, 3208, 3214); // lumb cooking bowl
				drop(players, 1931, 3209, 3214); // lumb cooking pot
				drop(players, 1931, 3148, 3211); // lumb lost city pot
				drop(players, 1935, 3211, 3212); // lumb cooking jug
				drop(players, 946, 3205, 3212); // knife
				drop(players, 946, 3224, 3202); // knife
				drop(players, 1265, 3229, 3215); // bronze pickaxe
				drop(players, 1265, 3229, 3223); // bronze pickaxe
				drop(players, 1205, 3213, 3216); // bronze dagger
				drop(players, 1511, 3209, 3224); // logs
				drop(players, 1511, 3208, 3225); // logs
				drop(players, 1511, 3105, 3223); // logs
				drop(players, 1511, 3205, 3225); // logs
				drop(players, 1511, 3087, 3265); // logs
				drop(players, 1511, 3087, 3266); // logs
				drop(players, 1511, 3244, 3159); // small fishing net
				drop(players, 1511, 3245, 3155); // small fishing net
				drop(players, 1939, 3193, 3181); // swamp tar
				drop(players, 1939, 3183, 3179); // swamp tar
				drop(players, 1939, 3179, 3190); // swamp tar
				drop(players, 1939, 3180, 3193); // swamp tar
				drop(players, 1939, 3172, 3190); // swamp tar
				drop(players, 1939, 3163, 3187); // swamp tar
				drop(players, 1939, 3164, 3180); // swamp tar
				drop(players, 1939, 3174, 3182); // swamp tar
				drop(players, 1939, 3176, 3184); // swamp tar
				drop(players, 1939, 3165, 3168); // swamp tar
				drop(players, 1939, 3171, 3168); // swamp tar
				drop(players, 1939, 3176, 3168); // swamp tar
				drop(players, 1939, 3182, 3165); // swamp tar
				drop(players, 1939, 3185, 3161); // swamp tar
				drop(players, 1939, 3189, 3164); // swamp tar
				drop(players, 1939, 3192, 3163); // swamp tar
				drop(players, 1939, 3194, 3169); // swamp tar
				drop(players, 1059, 3206, 3147); // leather gloves
				drop(players, 556, 3149, 3246); // air rune
				drop(players, 1061, 3111, 3159); // leather boots
				// draynor
				drop(players, 1511, 3105, 3159); // logs
				drop(players, 1511, 3106, 3159); // logs
				drop(players, 1511, 3107, 3160); // logs
				drop(players, 1985, 3087, 3262); // cheese
				drop(players, 1982, 3088, 3262); // tomato
				// draynor maynor
				drop(players, 5329, 3126, 3358); // secatears
				drop(players, 1925, 3120, 3360); // bucket
				drop(players, 1139, 3120, 3361); // bronze helm
				drop(players, 952, 3121, 3361); // spade
				drop(players, 276, 3112, 3368); // rubber tube -ersnt the
												// chicken
				drop(players, 273, 3100, 3364); // poision -ernst the chicken
				drop(players, 272, 3107, 3356); // fish food -ernst the chicken
				drop(players, 590, 3112, 3369); // tinderbox
				// alkarid
				drop(players, 1925, 3303, 3189); // bucket
				drop(players, 1061, 3306, 3195); // leather boots
				drop(players, 1422, 3320, 3137); // bronze mace
				drop(players, 1965, 3282, 3172); // cabbage
				drop(players, 1937, 3303, 3165); // jug of water
				// alkarid mining place
				drop(players, 554, 3304, 3311); // fire rune
				drop(players, 555, 3298, 3315); // water rune
				// varrock
				drop(players, 1573, 3152, 3401); // doogle leaves
				drop(players, 1573, 3152, 3399); // doogle leaves
				drop(players, 1573, 3151, 3399); // doogle leaves
				drop(players, 1573, 3157, 3400); // doogle leaves
				drop(players, 1573, 3156, 3401); // doogle leaves
				drop(players, 1973, 3143, 3453); // chocolate bar
				drop(players, 559, 3193, 3494); // body rune
				drop(players, 2313, 3222, 3494); // pie dish

				drop(players, 1171, 3204, 3515); // wooden shield
				drop(players, 557, 3199, 3518); // earth rune
				// ardougne
				drop(players, 1955, 2645, 3363); // apple
				// drop(players, 425, 2618, 3323); //pigeon cage
				// drop(players, 425, 2618, 3324); //pigeon cage
				// drop(players, 425, 2618, 3325); //pigeon cage
				// drop(players, , 2638, 3292); //guide book
				drop(players, 1550, 2646, 3299); // garlic
				drop(players, 1420, 2676, 3302); // iron mace
				drop(players, 2347, 2684, 3318); // hammer
				drop(players, 1755, 2683, 3318); // chisel
				drop(players, 1925, 2616, 3255); // bucket
				// drop(players, 1773, , ); //purple dye
				drop(players, 1137, 2540, 3249); // iron medhelm
				drop(players, 1327, 2571, 3288); // black scimmy
				// drop(players, 1510, 1, 2576, 3334); //picture girl
				// drop(players, 278, 1, 2604, 3358); //cattleprod
				drop(players, 1925, 2564, 3332); // bucket
				drop(players, 952, 2566, 3330); // spade
				// east ardougne
				drop(players, 1755, 2543, 3286); // chisel
				drop(players, 946, 2542, 3286); // knife
				drop(players, 1984, 2535, 3333); // rotten apple
				Game.updateEntityRegion(players);
			}
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	@SuppressWarnings("unused")
	private static final void addGroundItemsTask(Player player) {
		CoresManager.fastExecutor.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					for (Player players : getPlayers()) {
						if (players == null)
							continue;
						if (!players.isXPBoosted()) {
							players.sendMessage("<col=ff0033>Your doubled bonus EXP boost has ran out.");
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, 0, 90000);
	}

	public static final void drop(Player players, final int item, final int x,
			final int y) {
		Game.updateGroundItem(new Item(item), new Location(x, y, 0), players);

	}

	@SuppressWarnings("unused")
	private static final void addArtisanMessageTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (NPC n : getNPCs()) {
						if (n == null || n.isDead())
							continue;
						if (n.getId() == 6654) {
							switch (Misc.getRandom(5)) {
							case 0:
								n.setNextForceTalk(new ForceTalk(
										"Make Gauntlets!"));
								break;
							case 1:
								n.setNextForceTalk(new ForceTalk("Make Boots!"));
								break;
							case 2:
								n.setNextForceTalk(new ForceTalk("Make Boots!"));
								break;
							case 3:
								n.setNextForceTalk(new ForceTalk(
										"Make Helmets!"));
								break;
							}
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, 0, 2500);
	}

	private static final void addDrainPrayerTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead()
								|| !player.isRunning())
							continue;
						player.getPrayer().processPrayerDrain();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 1, 600);
	}

	/**
	 * Spawns The Shooting Star Every 1200 Seconds.
	 */
	public static void spawnStar() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 12) {
					ShootingStar.stage = 8;
					ShootingStar.stardustMined = 0;
					ShootingStar.spawnRandomStar();
				}
				loop++;
			}
		}, 0, 1);
	}

	/**
	 * Removes The Star Sprite After 50 Seconds.
	 */
	public static void removeStarSprite(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 50) {
					for (NPC n : Game.getNPCs()) {
						if (n == null || n.getId() != 8091)
							continue;
						n.sendDeath(n); // Removes the Star Sprite.
						spawnStar(); // Spawns Another Star.
					}
				}
				loop++;
			}
		}, 0, 1);
	}

	public static boolean QBD;
	private static GrandExchange grandExchange = new GrandExchange();

	public static GrandExchange getGrandExchange() {
		return grandExchange;
	}

	public static int ticksPassed;

	private static void addGrowingTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					ticksPassed++;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 600, TimeUnit.MILLISECONDS);
	}

	public static void sendMessage(String s) {
		for (Player p : getPlayers()) {
			if (p == null) {
				continue;
			}
			p.sm(s);
		}
	}

	private static void addMessageCheckTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player.getMessageAmount() == 0)// if message != 0
							continue;
						player.resetMessageAmount();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 11, TimeUnit.SECONDS);
	}

	private static void addOwnedObjectsTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					OwnedObjectManager.processAll();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	private static void addRestoreShopItemsTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					ShopsHandler.restoreShops();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 30, TimeUnit.SECONDS);
	}

	private static final void addSummoningEffectTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player.getFamiliar() == null || player.isDead()
								|| !player.hasFinished())
							continue;
						if (player.getFamiliar().getOriginalId() == 6814) {
							player.heal(20);
							player.setNextGraphics(new Graphics(1507));
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 15, TimeUnit.SECONDS);
	}

	private static final void addRestoreSpecialAttackTask() {

		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead()
								|| !player.isRunning())
							continue;
						player.getCombatDefinitions().restoreSpecialAttack();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 30000);
	}

	private static boolean checkAgility;

	private static final void addRestoreRunEnergyTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null
								|| player.isDead()
								|| !player.isRunning()
								|| (checkAgility && player.getSkills().getLevel(
										Skills.AGILITY) < 70))
							continue;
						player.restoreRunEnergy();
					}
					checkAgility = !checkAgility;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 1000);
	}

	private static final void addRestoreHitPointsTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead()
								|| !player.isRunning())
							continue;
						player.restoreHitPoints();
					}
					for (NPC npc : npcs) {
						if (npc == null || npc.isDead() || npc.hasFinished())
							continue;
						npc.restoreHitPoints();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 6000);
	}

	@SuppressWarnings("unused")
	private static final void addGroundItemSpawnTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead()
								|| !player.isRunning())
							continue;
						player.restoreHitPoints();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 6000);
	}

	private static final void addRestoreSkillsTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || !player.isRunning())
							continue;
						int ammountTimes = player.getPrayer().usingPrayer(0, 8) ? 2
								: 1;
						if (player.isResting())
							ammountTimes += 1;
						if (player.isMusicianResting())
							ammountTimes += 2;
						boolean berserker = player.getPrayer()
								.usingPrayer(1, 5);
						for (int skill = 0; skill < 25; skill++) {
							if (skill == Skills.SUMMONING)
								continue;
							for (int time = 0; time < ammountTimes; time++) {
								int currentLevel = player.getSkills().getLevel(
										skill);
								int normalLevel = player.getSkills()
										.getLevelFromXP(skill);
								if (currentLevel > normalLevel) {
									if (skill == Skills.ATTACK
											|| skill == Skills.STRENGTH
											|| skill == Skills.DEFENCE
											|| skill == Skills.RANGE
											|| skill == Skills.MAGIC) {
										if (berserker
												&& Misc.getRandom(100) <= 15)
											continue;
									}
									player.getSkills()
											.set(skill, currentLevel - 1);
								} else if (currentLevel < normalLevel)
									player.getSkills()
											.set(skill, currentLevel + 1);
								else
									break;
							}
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 30000);

	}

	public static final Map<Integer, Region> getRegions() {
		return regions;
	}

	public static final Region getRegion(int id) {
		return getRegion(id, false);
	}

	public static final Region getRegion(int id, boolean load) {
		Region region = regions.get(id);
		if (region == null) {
			region = new Region(id);
			regions.put(id, region);
		}
		if (load)
			region.checkLoadMap();
		return region;
	}

	public static final void addPlayer(Player player) {
		players.add(player);
		if (!(player instanceof Bot))
			AntiFlood.add(player.getSession().getIP());
	}

	public static void removePlayer(Player player) {
		players.remove(player);
		if (!(player instanceof Bot))
			AntiFlood.remove(player.getSession().getIP());
	}

	public static final void addNPC(NPC npc) {
		npcs.add(npc);
	}

	public static final void removeNPC(NPC npc) {
		npcs.remove(npc);
	}

	public static final NPC spawnFacingNPC(int id, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			EntityDirection faceDirection) {
		NPC returnValue = spawnNPC(id, tile, mapAreaNameHash,
				canBeAttackFromOutOfArea, false);
		returnValue.setDirection(faceDirection.getValue());
		return returnValue;
	}
	
	public static final NPC spawnNPC(int id, Location tile, int mHash, boolean canAttack) {
		return spawnNPC(id, tile, mHash, canAttack, true);
	}

	public static final NPC spawnNPC(int id, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			boolean spawned) {
		NPC n = null;
		HunterNPC hunterNPCs = HunterNPC.forId(id);
		if (hunterNPCs != null) {
			if (id == hunterNPCs.getNpcId())
				n = new HuntNPC(id, tile, mapAreaNameHash,
						canBeAttackFromOutOfArea, spawned);
		}
		// for (int j = 0; j < 57; j++){
		// if (id == 9964+j)
		// n = new AsteaFrostweb(id, tile, mapAreaNameHash,
		// canBeAttackFromOutOfArea, spawned);
		// }
		if (SlayerHelp.isSlayer(id))
			n = new Slayer(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		else if (id == 15507)
			n = new QBD(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);

		else if (id == 14688 || id == 14690 || id == 14692 || id == 14694
				|| id == 14696 || id == 14698 || id == 14700)
			n = new Polypore(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 13820 || id == 13821 || id == 13822)
			n = new Jadinko(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 1158 || id == 1160)
			n = new KalphiteQueen(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 6215)
			n = new GodwarsZammorakFaction(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 2745)
			n = new TzTokJad(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 3847)
			n = new SeaTroll(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 907 || id == 11301 || id == 11302 || id == 911)
			n = new KolodionMage(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 1471)
			n = new SkeletonMonkey(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 14256)
			n = new Lucien(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		else if (id == 6255 || id == 6257)
			n = new GodwarsSaradominFaction(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 8528)
			n = new Nomad(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		else if (id == 6261 || id == 6263 || id == 6265)
			n = GodWarsBosses.graardorMinions[(id - 6261) / 2] = new GodWarMinion(
					id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		else if (id == 6260)
			n = new GeneralGraardor(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 6222)
			n = new KreeArra(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 6223 || id == 6225 || id == 6227)
			n = GodWarsBosses.armadylMinions[(id - 6223) / 2] = new GodWarMinion(
					id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		else if (id == 6203)
			n = new KrilTstsaroth(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 6204 || id == 6206 || id == 6208)
			n = GodWarsBosses.zamorakMinions[(id - 6204) / 2] = new GodWarMinion(
					id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		else if (id == 50 || id == 2642 || id == 12866 || id == 12866)
			n = new KingBlackDragon(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 6363 || id == Integer.valueOf(6371))
			n = new ZMINPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);

		else if (id == 6248 || id == 6250 || id == 6252)
			n = GodWarsBosses.commanderMinions[(id - 6248) / 2] = new GodWarMinion(
					id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		else if (id == 6247)
			n = new CommanderZilyana(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 8133)
			n = new CorporealBeast(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 13447)
			n = ZarosGodwars.nex = new Nex(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 13451)
			n = ZarosGodwars.fumus = new NexMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 13452)
			n = ZarosGodwars.umbra = new NexMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 13453)
			n = ZarosGodwars.cruor = new NexMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 13454)
			n = ZarosGodwars.glacies = new NexMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 8349 || id == 8450 || id == 8451)
			n = new TormentedDemon(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		else if (id == 14301)
			n = new Glacor(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		else
			n = new NPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		if (n.getId() == 4247)
			n.setRandomWalk(false);
		return n;
	}

	/*
	 * check if the entity region changed because moved or teled then we update
	 * it
	 */
	public static final void updateEntityRegion(Entity entity) {
		if (entity.hasFinished()) {
			if (entity instanceof Player)
				getRegion(entity.getLastRegionId()).removePlayerIndex(
						entity.getIndex());
			else
				getRegion(entity.getLastRegionId()).removeNPCIndex(
						entity.getIndex());
			return;
		}
		int regionId = entity.getRegionId();
		if (entity.getLastRegionId() != regionId) { // map region entity at
			// changed
			if (entity instanceof Player) {
				if (entity.getLastRegionId() > 0)
					getRegion(entity.getLastRegionId()).removePlayerIndex(
							entity.getIndex());
				Region region = getRegion(regionId);
				region.addPlayerIndex(entity.getIndex());
				Player player = (Player) entity;
				player.getControllerManager().moved();
				int musicId = region.getMusicId();
				if (musicId != -1)
					player.getMusicsManager().checkMusic(region.getMusicId());
				if (player.isRunning()
						&& player.getControllerManager().getControler() == null)
					checkControlersAtMove(player);
			} else {
				if (entity.getLastRegionId() > 0)
					getRegion(entity.getLastRegionId()).removeNPCIndex(
							entity.getIndex());
				getRegion(regionId).addNPCIndex(entity.getIndex());
			}
			entity.checkMultiArea();
			entity.setLastRegionId(regionId);
		} else {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				player.getControllerManager().moved();
				if (player.isRunning()
						&& player.getControllerManager().getControler() == null)
					checkControlersAtMove(player);
			}
			entity.checkMultiArea();
		}
	}

	private static void checkControlersAtMove(Player player) {
		if (player.getControllerManager().getControler() == null) {
			String control = null;
			if (!(player.getControllerManager().getControler() instanceof RequestController)
					&& RequestController.inWarRequest(player))
				control = "clan_wars_request";
			else if (FfaZone.inArea(player))
				control = "clan_wars_ffa";
			if (control != null)
				player.getControllerManager().startController(control);
		}
	}

	/*
	 * checks clip
	 */
	public static boolean canMoveNPC(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++)
			for (int tileY = y; tileY < y + size; tileY++)
				if (getMask(plane, tileX, tileY) != 0)
					return false;
		return true;
	}

	public static int getMask(int plane, int x, int y) {
		Location tile = new Location(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null)
			return -1;
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getMask(tile.getZ(), baseLocalX, baseLocalY);
	}

	public static void setMask(int plane, int x, int y, int mask) {
		Location tile = new Location(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null)
			return;
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		region.setMask(tile.getZ(), baseLocalX, baseLocalY, mask);
	}

	public static int getRotation(int plane, int x, int y) {
		Location tile = new Location(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null)
			return 0;
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getRotation(tile.getZ(), baseLocalX, baseLocalY);
	}

	private static int getClipedOnlyMask(int plane, int x, int y) {
		Location tile = new Location(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null)
			return -1;
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getMaskClipedOnly(tile.getZ(), baseLocalX, baseLocalY);
	}

	public static final boolean checkProjectileStep(int plane, int x, int y,
			int dir, int size) {
		int xOffset = Misc.DIRECTION_DELTA_X[dir];
		int yOffset = Misc.DIRECTION_DELTA_Y[dir];
		/*
		 * int rotation = getRotation(plane,x+xOffset,y+yOffset); if(rotation !=
		 * 0) { dir += rotation; if(dir >= Utils.DIRECTION_DELTA_X.length) dir =
		 * dir - (Utils.DIRECTION_DELTA_X.length-1); xOffset =
		 * Utils.DIRECTION_DELTA_X[dir]; yOffset = Utils.DIRECTION_DELTA_Y[dir];
		 * }
		 */
		if (size == 1) {
			int mask = getClipedOnlyMask(plane,
					x + Misc.DIRECTION_DELTA_X[dir], y
							+ Misc.DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0)
				return (mask & 0x42240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (mask & 0x60240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (mask & 0x40a40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (mask & 0x48240000) == 0;
			if (xOffset == -1 && yOffset == -1) {
				return (mask & 0x43a40000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & 0x60e40000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & 0x4e240000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & 0x78240000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
			}
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0)
				return (getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (getClipedOnlyMask(plane, x + 2, y) & 0x60e40000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (getClipedOnlyMask(plane, x, y + 2) & 0x4e240000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			if (xOffset == -1 && yOffset == -1)
				return (getClipedOnlyMask(plane, x - 1, y) & 0x4fa40000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) == 0
						&& (getClipedOnlyMask(plane, x, y - 1) & 0x63e40000) == 0;
			if (xOffset == 1 && yOffset == -1)
				return (getClipedOnlyMask(plane, x + 1, y - 1) & 0x63e40000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y - 1) & 0x60e40000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y) & 0x78e40000) == 0;
			if (xOffset == -1 && yOffset == 1)
				return (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4fa40000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0
						&& (getClipedOnlyMask(plane, x, y + 2) & 0x7e240000) == 0;
			if (xOffset == 1 && yOffset == 1)
				return (getClipedOnlyMask(plane, x + 1, y + 2) & 0x7e240000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y + 2) & 0x78240000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) != 0
						|| (getClipedOnlyMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getClipedOnlyMask(plane, x + size, y) & 0x60e40000) != 0
						|| (getClipedOnlyMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) != 0
						|| (getClipedOnlyMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x, y + size) & 0x4e240000) != 0
						|| (getClipedOnlyMask(plane, x + (size - 1), y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0
							|| (getClipedOnlyMask(plane, sizeOffset - 1 + x,
									y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x + size, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + size, sizeOffset
							+ (-1 + y)) & 0x78e40000) != 0
							|| (getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x - 1, y + size) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0
							|| (getClipedOnlyMask(plane, -1 + (x + sizeOffset),
									y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x + size, y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0
							|| (getClipedOnlyMask(plane, x + size, y
									+ sizeOffset) & 0x78e40000) != 0)
						return false;
			}
		}
		return true;
	}

	public static final boolean checkWalkStep(int plane, int x, int y, int dir,
			int size) {
		int xOffset = Misc.DIRECTION_DELTA_X[dir];
		int yOffset = Misc.DIRECTION_DELTA_Y[dir];
		int rotation = getRotation(plane, x + xOffset, y + yOffset);
		if (rotation != 0) {
			for (int rotate = 0; rotate < (4 - rotation); rotate++) {
				int fakeChunckX = xOffset;
				int fakeChunckY = yOffset;
				xOffset = fakeChunckY;
				yOffset = 0 - fakeChunckX;
			}
		}

		if (size == 1) {
			int mask = getMask(plane, x + Misc.DIRECTION_DELTA_X[dir], y
					+ Misc.DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0)
				return (mask & 0x42240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (mask & 0x60240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (mask & 0x40a40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (mask & 0x48240000) == 0;
			if (xOffset == -1 && yOffset == -1) {
				return (mask & 0x43a40000) == 0
						&& (getMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & 0x60e40000) == 0
						&& (getMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & 0x4e240000) == 0
						&& (getMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getMask(plane, x, y + 1) & 0x48240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & 0x78240000) == 0
						&& (getMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getMask(plane, x, y + 1) & 0x48240000) == 0;
			}
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0)
				return (getMask(plane, x - 1, y) & 0x43a40000) == 0
						&& (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (getMask(plane, x + 2, y) & 0x60e40000) == 0
						&& (getMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (getMask(plane, x, y - 1) & 0x43a40000) == 0
						&& (getMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (getMask(plane, x, y + 2) & 0x4e240000) == 0
						&& (getMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			if (xOffset == -1 && yOffset == -1)
				return (getMask(plane, x - 1, y) & 0x4fa40000) == 0
						&& (getMask(plane, x - 1, y - 1) & 0x43a40000) == 0
						&& (getMask(plane, x, y - 1) & 0x63e40000) == 0;
			if (xOffset == 1 && yOffset == -1)
				return (getMask(plane, x + 1, y - 1) & 0x63e40000) == 0
						&& (getMask(plane, x + 2, y - 1) & 0x60e40000) == 0
						&& (getMask(plane, x + 2, y) & 0x78e40000) == 0;
			if (xOffset == -1 && yOffset == 1)
				return (getMask(plane, x - 1, y + 1) & 0x4fa40000) == 0
						&& (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0
						&& (getMask(plane, x, y + 2) & 0x7e240000) == 0;
			if (xOffset == 1 && yOffset == 1)
				return (getMask(plane, x + 1, y + 2) & 0x7e240000) == 0
						&& (getMask(plane, x + 2, y + 2) & 0x78240000) == 0
						&& (getMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getMask(plane, x - 1, y) & 0x43a40000) != 0
						|| (getMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getMask(plane, x + size, y) & 0x60e40000) != 0
						|| (getMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getMask(plane, x, y - 1) & 0x43a40000) != 0
						|| (getMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getMask(plane, x, y + size) & 0x4e240000) != 0
						|| (getMask(plane, x + (size - 1), y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getMask(plane, x - 1, y - 1) & 0x43a40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0
							|| (getMask(plane, sizeOffset - 1 + x, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getMask(plane, x + size, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x + size, sizeOffset + (-1 + y)) & 0x78e40000) != 0
							|| (getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getMask(plane, x - 1, y + size) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0
							|| (getMask(plane, -1 + (x + sizeOffset), y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getMask(plane, x + size, y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0
							|| (getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0)
						return false;
			}
		}
		return true;
	}

	public static final boolean containsPlayer(String username) {
		for (Player p2 : players) {
			if (p2 == null)
				continue;
			if (p2.getUsername().equalsIgnoreCase(username))
				return true;
		}
		return false;
	}

	public static Player getPlayer(String username) {
		for (Player player : getPlayers()) {
			if (player == null)
				continue;
			if(player.getUsername().equals(username.replace("_", " ")))
				return player;
		}
		return null;
	}

	public static final Player getPlayerByDisplayName(String username) {
		String formatedUsername = Misc.formatPlayerNameForProtocol(username);
		for (Player player : getPlayers()) {
			if (player == null)
				continue;
			if (player.getUsername().equals(formatedUsername)
					|| player.getDisplayName().equals(username))
				return player;
		}
		return null;
	}

	public static final EntityList<Player> getPlayers() {
		return players;
	}

	public static final EntityList<NPC> getNPCs() {
		return npcs;
	}

	private Game() {

	}

	public static final void safeShutdown(int delay) {
		if (exiting_start != 0)
			return;
		exiting_start = Misc.currentTimeMillis();
		exiting_delay = (int) delay;
		for (Player player : Game.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished())
				continue;
			player.packets().sendSystemUpdate(delay);
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					for (Player player : Game.getPlayers()) {
						if (player == null || !player.hasStarted())
							continue;
						player.realFinish();
					}
					Main.saveFiles();
					Main.oldrestart();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, delay, TimeUnit.SECONDS);
	}

	public static final void addGroundItem(final Item item,
			final Location tile, final Player owner/* null for default */,
			final boolean underGrave, long hiddenTime/* default 3minutes */,
			boolean invisible) {
		final FloorItem floorItem = new FloorItem(item, tile, owner,
				owner == null ? false : underGrave, invisible);
		final Region region = getRegion(tile.getRegionId());
		region.forceGetFloorItems().add(floorItem);
		if(owner == null)
			return;
		if (invisible && hiddenTime != -1) {
			if (owner != null)
				owner.packets().sendGroundItem(floorItem);
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						if (!region.forceGetFloorItems().contains(floorItem))
							return;
						int regionId = tile.getRegionId();
						if ((owner != null && underGrave)
								|| ItemConstants.isTradeable(floorItem)) {
							region.forceGetFloorItems().remove(floorItem);
							if (owner.getMapRegionsIds().contains(regionId)
									|| owner.getZ() != tile.getZ())
								owner.packets().sendRemoveGroundItem(floorItem);
							return;
						}
						floorItem.setInvisible(false);
						for (Player player : players) {
							if (player == null
									|| player == owner
									|| !player.hasStarted()
									|| player.hasFinished()
									|| player.getZ() != tile.getZ()
									|| !player.getMapRegionsIds().contains(
											regionId))
								continue;
							player.packets().sendGroundItem(floorItem);
						}
						removeGroundItem(floorItem, 180);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, hiddenTime, TimeUnit.SECONDS);
			return;
		}
		int regionId = tile.getRegionId();
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.hasFinished()
					|| player.getZ() != tile.getZ()
					|| !player.getMapRegionsIds().contains(regionId))
				continue;
			player.packets().sendGroundItem(floorItem);
		}
		removeGroundItem(floorItem, 180);
	}

	public static final void updateGroundItem(Item item, final Location tile,
			final Player owner) {
		final FloorItem floorItem = Game.getRegion(tile.getRegionId())
				.getGroundItem(item.getId(), tile, owner);
		if (floorItem == null) {
			addGroundItem(item, tile, owner, false, 360, true);
			return;
		}
		floorItem.setAmount(1);
		owner.packets().sendRemoveGroundItem(floorItem);
		owner.packets().sendGroundItem(floorItem);

	}

	private static final void removeGroundItem(final FloorItem floorItem,
			long publicTime) {

		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					int regionId = floorItem.getLocation().getRegionId();
					Region region = getRegion(regionId);
					if (!region.forceGetFloorItems().contains(floorItem))
						return;
					region.forceGetFloorItems().remove(floorItem);
					for (Player player : Game.getPlayers()) {
						if (player == null
								|| !player.hasStarted()
								|| player.hasFinished()
								|| player.getZ() != floorItem.getLocation().getZ()
								|| !player.getMapRegionsIds()
										.contains(regionId))
							continue;
						player.packets().sendRemoveGroundItem(floorItem);
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, publicTime, TimeUnit.SECONDS);
	}

	public static final boolean removeGroundItem(Player player,
			FloorItem floorItem) {
		return removeGroundItem(player, floorItem, true);
	}

	public static final boolean removeGroundItem(Player player,
			FloorItem floorItem, boolean add) {
		int regionId = floorItem.getLocation().getRegionId();
		Region region = getRegion(regionId);
		if (!region.forceGetFloorItems().contains(floorItem)) {
			return false;
		}
		if (add
				&& player.getInventory().getFreeSlots() == 0
				&& (!floorItem.defs().isStackable() || !player.getInventory()
						.containsItem(floorItem.getId(), 1))) {
			player.packets().sendMessage("Not enough space in your inventory.");
			return false;
		}
		region.forceGetFloorItems().remove(floorItem);
		if (add)
			player.getInventory().addItem(floorItem.getId(),
					floorItem.getAmount());
		if (floorItem.isInvisible() || floorItem.isGrave()) {
			player.packets().sendRemoveGroundItem(floorItem);
			return true;
		} else {
			for (Player p2 : Game.getPlayers()) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished()
						|| p2.getZ() != floorItem.getLocation().getZ()
						|| !p2.getMapRegionsIds().contains(regionId))
					continue;
				p2.packets().sendRemoveGroundItem(floorItem);
			}
			return true;
		}
	}

	public static final void sendGraphics(Entity creator, Graphics graphics,
			Location tile) {
		if (creator == null) {
			for (Player player : Game.getPlayers()) {
				if (player == null || !player.hasStarted()
						|| player.hasFinished() || !player.withinDistance(tile))
					continue;
				player.packets().sendGraphics(graphics, tile);
			}
		} else {
			for (int regionId : creator.getMapRegionsIds()) {
				List<Integer> playersIndexes = getRegion(regionId)
						.getPlayerIndexes();
				if (playersIndexes == null)
					continue;
				for (Integer playerIndex : playersIndexes) {
					Player player = players.get(playerIndex);
					if (player == null || !player.hasStarted()
							|| player.hasFinished()
							|| !player.withinDistance(tile))
						continue;
					player.packets().sendGraphics(graphics, tile);
				}
			}
		}
	}

	public static final void sendProjectile(Entity shooter, Location startTile,
			Location receiver, int gfxId, int startHeight, int endHeight,
			int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId)
					.getPlayerIndexes();
			if (playersIndexes == null)
				continue;
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null
						|| !player.hasStarted()
						|| player.hasFinished()
						|| (!player.withinDistance(shooter) && !player
								.withinDistance(receiver)))
					continue;
				player.packets().sendProjectile(null, startTile, receiver,
						gfxId, startHeight, endHeight, speed, delay, curve,
						startDistanceOffset, 1);
			}
		}
	}

	public static final boolean isMultiArea(Location tile) {
		for (Area area : Area.getAreas()) {
			if (area != null && Area.inArea(tile, area)
					&& area.getAreaType().equals(Area.AreaType.MULTI_PVP)
					|| area != null && Area.inArea(tile, area)
					&& area.getAreaType().equals(Area.AreaType.MULTI))
				return true;
		}
		int destX = tile.getX();
		int destY = tile.getY();
		return (destX >= 3200 && destX <= 3390 && destY >= 3840 && destY <= 3967) // wild
				|| (destX >= 2835 && destX <= 2880 && destY >= 5905 && destY <= 5950)
				|| (destX >= 2500 && destX <= 3000 && destY >= 8990 && destY <= 9400)
				|| (destX >= 2600 && destX <= 2800 && destY >= 9900 && destY <= 11500)
				|| (destX >= 2992 && destX <= 3007 && destY >= 3912 && destY <= 3967)
				|| (destX >= 3060 && destX <= 3207 && destY >= 9800 && destY <= 12000)
				|| (destX >= 2946 && destX <= 2959 && destY >= 3816 && destY <= 3831)
				|| (destX >= 3008 && destX <= 3199 && destY >= 3856 && destY <= 3903)
				|| (destX >= 3008 && destX <= 3071 && destY >= 3600 && destY <= 3711)
				|| (destX >= 3270 && destX <= 3346 && destY >= 3532 && destY <= 3625)
				|| (destX >= 2965 && destX <= 3050 && destY >= 3904 && destY <= 3959) // wild
				|| (destX >= 2815 && destX <= 2966 && destY >= 5240 && destY <= 5375)
				|| (destX >= 2840 && destX <= 2950 && destY >= 5190 && destY <= 5230) // godwars
				|| (destX >= 3547 && destX <= 3555 && destY >= 9690 && destY <= 9699) // zaros
				// godwars
				|| (destX >= 2948 && destY >= 5537 && destX <= 3071 && destY <= 5631) // Risk
				// ffa.
				|| (destX >= 2756 && destY >= 5537 && destX <= 2879 && destY <= 5631) // Safe
				|| (destX >= 1490 && destX <= 1515 && destY >= 4696 && destY <= 4714) // chaos
																						// dwarf
																						// battlefield
				|| KingBlackDragon.atKBD(tile) // King Black Dragon lair
				|| TormentedDemon.atTD(tile) // Tormented demon's area
				|| (destX >= 2970 && destX <= 3000 && destY >= 4365 && destY <= 4400)// corp
				|| (destX >= 3136 && destX <= 3327 && destY >= 3520
						&& destY <= 3970 || (destX >= 2376 && 5127 >= destY
						&& destX <= 2422 && 5168 <= destY))
				|| (destX >= 3000 && destY >= 3600 && destX <= 4600 && destY <= 5000)
				|| (destX >= 2374 && destY >= 5129 && destX <= 2424 && destY <= 5168) // pits
				|| (destX >= 2622 && destY >= 5696 && destX <= 2573 && destY <= 5752) // torms
				|| (destX >= 2368 && destY >= 3072 && destX <= 2431 && destY <= 3135) // castlewars
				|| (destX >= 3086 && destY >= 5536 && destX <= 3315 && destY <= 5530) // Bork
				|| (tile.getX() >= 3526 && tile.getX() <= 3550
						&& tile.getY() >= 5185 && tile.getY() <= 5215) // out
				|| (destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532); // castlewars
																						// in

		// multi
	}

	public static final boolean pvpArea(Entity entity) {
		for (Area area : Area.getAreas())
			if (area != null && Area.inArea(entity, area)
					&& area.getAreaType().equals(Area.AreaType.MULTI_PVP)
					|| area != null && Area.inArea(entity, area)
					&& area.getAreaType().equals(Area.AreaType.PVP))
				return true;
			else
				return Wilderness.isAtWild(entity);
		return false;
	}

	public static final boolean inSafe(Location tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		for (Area area : Area.getAreas())
			if (area != null && !Area.inArea(tile, area)
					&& !area.getAreaType().equals(Area.AreaType.MULTI_PVP)
					&& !Area.inArea(tile, area)
					&& !area.getAreaType().equals(Area.AreaType.PVP))
				return true;
		if ((destX >= 3008 && destX <= 3019 && destY >= 3353 && destY <= 3358)
				|| (destX >= 2943 && destX <= 3949 && destY >= 3367 && destY <= 3373)) {
			return true;
		} else {
			return false;
		}
	}

	public static void destroySpawnedObject(GlobalObject object, boolean clip) {
		int regionId = object.getRegionId();
		int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		GlobalObject realMapObject = getRegion(regionId).getRealObject(object);

		Game.getRegion(regionId).removeObject(object);
		if (clip)
			Game.getRegion(regionId).removeMapObject(object, baseLocalX,
					baseLocalY);
		for (Player p2 : Game.getPlayers()) {
			if (p2 == null || !p2.hasStarted() || p2.hasFinished()
					|| !p2.getMapRegionsIds().contains(regionId))
				continue;
			if (realMapObject != null)
				p2.packets().sendSpawnedObject(realMapObject);
			else
				p2.packets().sendDestroyObject(object);
		}
	}

	public static final void spawnTempGroundObject(final GlobalObject object,
			final int replaceId, long time) {
		final int regionId = object.getRegionId();
		GlobalObject realMapObject = getRegion(regionId).getRealObject(object);
		final GlobalObject realObject = realMapObject == null ? null
				: new GlobalObject(realMapObject.getId(),
						realMapObject.getType(), realMapObject.getRotation(),
						object.getX(), object.getY(), object.getZ());
		spawnObject(object, false);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					getRegion(regionId).removeObject(object);
					addGroundItem(new Item(replaceId), object, null, false,
							180, false);
					for (Player p2 : players) {
						if (p2 == null || !p2.hasStarted() || p2.hasFinished()
								|| p2.getZ() != object.getZ()
								|| !p2.getMapRegionsIds().contains(regionId))
							continue;
						if (realObject != null)
							p2.packets().sendSpawnedObject(realObject);
						else
							p2.packets().sendDestroyObject(object);
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, time, TimeUnit.MILLISECONDS);
	}

	public static int getIdFromName(String playerName) {
		for (Player p : players) {
			if (p == null) {
				continue;
			}
			if (p.getUsername().equalsIgnoreCase(
					Misc.formatPlayerNameForProtocol(playerName))) {
				return p.getIndex();
			}
		}
		return 0;
	}

	public static void sendMessage(String message, boolean forStaff) {
		for (Player p : Game.getPlayers()) {
			if (p == null || !p.isRunning() || (forStaff && p.getRights() == 0))
				continue;
			p.packets().sendMessage(message);
		}
	}

	public static final void addGroundItem(final Item item, final Location tile) {
		final FloorItem floorItem = new FloorItem(item, tile, null, false,
				false);
		final Region region = getRegion(tile.getRegionId());
		region.forceGetFloorItems().add(floorItem);
		int regionId = tile.getRegionId();
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.hasFinished()
					|| player.getZ() != tile.getZ()
					|| !player.getMapRegionsIds().contains(regionId))
				continue;
			player.packets().sendGroundItem(floorItem);
		}
	}

	public static final void addTimedGroundItem(final Item item,
			final Location tile) {
		final FloorItem floorItem = new FloorItem(item, tile, null, false,
				false);
		final Region region = getRegion(tile.getRegionId());
		region.forceGetFloorItems().add(floorItem);
		int regionId = tile.getRegionId();
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.hasFinished()
					|| player.getZ() != tile.getZ()
					|| !player.getMapRegionsIds().contains(regionId))
				continue;
			player.packets().sendGroundItem(floorItem);
		}
	}

	public static final void addGroundItem(final Item item,
			final Location tile, final Player owner/* null for default */,
			final boolean underGrave, long hiddenTime/* default 3minutes */,
			boolean invisible, boolean intoGold) {
		addGroundItem(item, tile, owner, underGrave, hiddenTime, invisible,
				intoGold, 180);
	}

	public static final void addGroundItem(final Item item,
			final Location tile, final Player owner/* null for default */,
			final boolean underGrave, long hiddenTime/* default 3minutes */,
			boolean invisible, boolean intoGold, final int publicTime) {
		if (intoGold) {
			if (!ItemConstants.isTradeable(item)) {
				int price = item.defs().getValue();
				if (price <= 0)
					return;
				item.setId(995);
				item.setAmount(price);
			}
		}
		final FloorItem floorItem = new FloorItem(item, tile, owner,
				owner == null ? false : underGrave, invisible);
		final Region region = getRegion(tile.getRegionId());
		region.forceGetFloorItems().add(floorItem);
		if (invisible && hiddenTime != -1) {
			if (owner != null)
				owner.packets().sendGroundItem(floorItem);
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						if (!region.forceGetFloorItems().contains(floorItem))
							return;
						int regionId = tile.getRegionId();
						if (underGrave || !ItemConstants.isTradeable(floorItem)
								|| item.getName().contains("Dr nabanik")) {
							region.forceGetFloorItems().remove(floorItem);
							if (owner != null) {
								if (owner.getMapRegionsIds().contains(regionId)
										&& owner.getZ() == tile.getZ())
									owner.packets().sendRemoveGroundItem(
											floorItem);
							}
							return;
						}

						floorItem.setInvisible(false);
						for (Player player : players) {
							if (player == null
									|| player == owner
									|| !player.hasStarted()
									|| player.hasFinished()
									|| player.getZ() != tile.getZ()
									|| !player.getMapRegionsIds().contains(
											regionId))
								continue;
							player.packets().sendGroundItem(floorItem);
						}
						removeGroundItem(floorItem, publicTime);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, hiddenTime, TimeUnit.SECONDS);
			return;
		}
		int regionId = tile.getRegionId();
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.hasFinished()
					|| player.getZ() != tile.getZ()
					|| !player.getMapRegionsIds().contains(regionId))
				continue;
			player.packets().sendGroundItem(floorItem);
		}
		removeGroundItem(floorItem, publicTime);
	}

	public static final void sendObjectAnimation(GlobalObject object,
			Animation animation) {
		sendObjectAnimation(null, object, animation);
	}

	public static final void sendObjectAnimation(Entity creator,
			GlobalObject object, Animation animation) {
		if (creator == null) {
			for (Player player : Game.getPlayers()) {
				if (player == null || !player.hasStarted()
						|| player.hasFinished())// ||
												// !player.withinDistance(object))
					continue;
				player.packets().sendObjectAnimation(object, animation);
			}
		} else {
			for (int regionId : creator.getMapRegionsIds()) {
				List<Integer> playersIndexes = getRegion(regionId)
						.getPlayerIndexes();
				if (playersIndexes == null)
					continue;
				for (Integer playerIndex : playersIndexes) {
					Player player = players.get(playerIndex);
					if (player == null || !player.hasStarted()
							|| player.hasFinished()
							|| !player.withinDistance(object))
						continue;
					player.packets().sendObjectAnimation(object, animation);
				}
			}
		}
	}

	public static final void sendProjectile(Location shooter, Entity receiver,
			int gfxId, int startHeight, int endHeight, int speed, int delay,
			int curve, int startDistanceOffset) {
		for (int regionId : receiver.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId)
					.getPlayerIndexes();
			if (playersIndexes == null)
				continue;
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null
						|| !player.hasStarted()
						|| player.hasFinished()
						|| (!player.withinDistance(shooter) && !player
								.withinDistance(receiver)))
					continue;
				player.packets().sendProjectile(receiver, shooter, receiver,
						gfxId, startHeight, endHeight, speed, delay, curve,
						startDistanceOffset, 1);
			}
		}
	}

	public static final void sendProjectile(Entity shooter, Location receiver,
			int gfxId, int startHeight, int endHeight, int speed, int delay,
			int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId)
					.getPlayerIndexes();
			if (playersIndexes == null)
				continue;
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null
						|| !player.hasStarted()
						|| player.hasFinished()
						|| (!player.withinDistance(shooter) && !player
								.withinDistance(receiver)))
					continue;
				player.packets().sendProjectile(null, shooter, receiver, gfxId,
						startHeight, endHeight, speed, delay, curve,
						startDistanceOffset, shooter.getSize());
			}
		}
	}

	public static final void sendProjectile(Entity shooter, Entity receiver,
			int gfxId, int startHeight, int endHeight, int speed, int delay,
			int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId)
					.getPlayerIndexes();
			if (playersIndexes == null)
				continue;
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null
						|| !player.hasStarted()
						|| player.hasFinished()
						|| (!player.withinDistance(shooter) && !player
								.withinDistance(receiver)))
					continue;
				int size = shooter.getSize();
				player.packets().sendProjectile(receiver, shooter, receiver,
						gfxId, startHeight, endHeight, speed, delay, curve,
						startDistanceOffset, size);
			}
		}
	}

	public static final void addGroundItem(final Item item,
			final Location tile, final Player owner/*
													 * null for default
													 */, boolean invisible,
			long hiddenTime/*
							 * default 3 minutes
							 */) {
		addGroundItem(item, tile, owner, invisible, hiddenTime, 2, 150);
	}

	public static final FloorItem addGroundItem(final Item item,
			final Location tile, final Player owner/*
													 * null for default
													 */, boolean invisible,
			long hiddenTime/*
							 * default 3 minutes
							 */, int type) {
		return addGroundItem(item, tile, owner, invisible, hiddenTime, type,
				150);
	}

	private static FloorItem addGroundItem(Item item, Location tile,
			Player owner, boolean invisible, long hiddenTime, int type, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	public static final void addGroundItemForever(Item item, final Location tile) {
		int regionId = tile.getRegionId();
		final FloorItem floorItem = new FloorItem(item.getId());
		final Region region = getRegion(tile.getRegionId());
		region.getGroundItems().add(floorItem);
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.hasFinished()
					|| player.getZ() != floorItem.getLocation().getZ()
					|| !player.getMapRegionsIds().contains(regionId))
				continue;
			player.packets().sendGroundItem(floorItem);
		}
	}

	public static NPC findNPC(int id) {
		NPC npc = null;
		for (NPC n : getNPCs()) {
			if (n == null)
				continue;
			if (n.getId() == id) {
				npc = n;
			}
		}
		return npc;
	}

	public static final GlobalObject getStandardObject(Location tile) {
		return getRegion(tile.getRegionId()).getStandartObject(tile.getZ(),
				tile.getXInRegion(), tile.getYInRegion());
	}

	public static boolean locationAvailable(Location location, int size) {
		int x = location.getX();
		int y = location.getY();
		int plane = location.getZ();
		for (int tileX = x; tileX < x + size; tileX++)
			for (int tileY = y; tileY < y + size; tileY++)
				if (getMask(plane, tileX, tileY) != 0)
					return false;
		return true;
	}

	/**
	 * Grabs only the local players within a 14 tile radius.
	 * 
	 * @param location
	 * @return
	 */
	public static List<Player> getLocalPlayers(Location location) {
		List<Player> localPlayers = new ArrayList<Player>();
		for (Player p : getPlayers())
			if (p != null)
				if (!localPlayers.contains(p))
					if (p.withinDistance(location, 14))
						localPlayers.add(p);
		return localPlayers;
	}

	/**
	 * Grabs only the local npcs within a 14 tile radius.
	 * 
	 * @param location
	 * @return
	 */
	public static List<NPC> getLocalNPCs(Location location) {
		List<NPC> localNPCs = new ArrayList<NPC>();
		for (NPC n : getNPCs())
			if (n != null)
				if (!localNPCs.contains(n))
					if (n.withinDistance(location, 14))
						localNPCs.add(n);
		return localNPCs;
	}

	public static NPC spawnNPC(int npcId, Location tile, boolean walk) {
		return new NPC(npcId, tile, walk, true);
	}

	public static NPC spawnNPC(int npcId, Location tile, boolean walk,
			boolean spawned) {
		return new NPC(npcId, tile, walk, spawned);
	}

	public static int getCutsceneX(Entity player, int x) {
		return new Location(x, 0, 0).getLocalX(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
	}

	public static int getCutsceneY(Entity player, int y) {
		return new Location(0, y, 0).getLocalY(
				player.getLastLoadedMapRegionTile(), player.getMapSize());
	}

	public static final void spawnObject(GlobalObject object) {
		getRegion(object.getRegionId()).spawnObject(object, object.getZ(),
				object.getXInRegion(), object.getYInRegion(), false);
	}

	public static final void removeObject(GlobalObject object) {
		getRegion(object.getRegionId()).removeObject(object, object.getZ(),
				object.getXInRegion(), object.getYInRegion());
	}

	public static List<GlobalObject> getLocalObjects(Location location,
			int radius) {
		if(location == null)
			return new ArrayList<GlobalObject>();
		int regionId = location.getRegionId();
		List<GlobalObject> localObjects = new ArrayList<GlobalObject>(5000);
		if (Game.getRegion(location.getRegionId(), true).getSpawnedObjects() != null) {
			for (GlobalObject obj : Game
					.getRegion(location.getRegionId(), true)
					.getSpawnedObjects()) {
				if (!localObjects.contains(obj)) {
					obj = Game
							.getRegion(regionId, true)
							.getSpawnedObject(
									new Location(obj.getX(), obj.getY(),
											obj.getZ()),
									Game.getRegion(regionId, true)
											.getSpawnedObject(
													new Location(obj.getX(),
															obj.getY(), obj
																	.getZ()), 3) != null ? 3
											: Game.getRegion(regionId, true)
													.getSpawnedObject(
															new Location(obj
																	.getX(),
																	obj.getY(),
																	obj.getZ()),
															1) != null ? 1
													: Game.getRegion(regionId,
															true)
															.getSpawnedObject(
																	new Location(
																			obj.getX(),
																			obj.getY(),
																			obj.getZ()),
																	0) != null ? 0
															: 2);
					if (obj.withinDistance(location, radius))
						localObjects.add(obj);
				}
			}
		}
		return localObjects;
	}

	public static final GlobalObject getSpawnedObject(int x, int y, int plane) {
		Location tile = new Location(x, y, plane);
		int regionId = tile.getRegionId();
		return getRegion(regionId).getSpawnedObject(tile);
	}

	public static final void spawnRemoveObject(GlobalObject object, boolean clip) {
		int regionId = object.getRegionId();
		GlobalObject placement = getRegion(regionId, true).getSpawnedObject(
				object, Region.OBJECT_SLOTS[object.getType()]);
		if (placement == null) {
			getRegion(regionId).addObject(object);
			if (clip) {
				int baseLocalX = object.getX() - ((regionId >> 8) * 64);
				int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
				getRegion(regionId)
						.addMapObject(object, baseLocalX, baseLocalY);
			}
		} else if (placement.matchesObject(object)) {
			placement.setId(object.getId());
			object = placement;
		} else {
			getRegion(regionId).addObject(object);
			if (clip) {
				int baseLocalX = object.getX() - ((regionId >> 8) * 64);
				int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
				getRegion(regionId)
						.addMapObject(object, baseLocalX, baseLocalY);
			}
		}
		synchronized (players) {
			for (Player p2 : players) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished()
						|| !p2.getMapRegionsIds().contains(regionId))
					continue;
				p2.packets().sendSpawnedObject(object);
			}
		}
	}

	public static NPC getLocalNPC(String name, Location location) {
		for (NPC n : getLocalNPCs(location))
			if (n.getName().equals(name))
				return n;
		return null;
	}

	/**
	 * Spawns a new object on the current object's tile
	 * @param object
	 */
	public static void spawnNewObject(GlobalObject object) {
		int regionId = object.getRegionId();
		Region r = getRegion(regionId, true);
		GlobalObject placement = r.getSpawnedObject(object);
		if (placement == null)
			placement = Game.getObject(object);
		if(placement == null || placement.getType() == 22 && object.getType() != 22) {
			r.addObject(object);
			int baseLocalX = object.getX() - ((regionId >> 8) * 64);
			int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
			r.addMapObject(object, baseLocalX, baseLocalY);
		} else {
			placement.setId(object.getId());
			placement.setRotation(object.getRotation());
			placement.setType(object.getType());
			object = placement;
		}
		synchronized (players) {
			for (Player p2 : players) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished()
						|| !p2.getMapRegionsIds().contains(regionId))
					continue;
				p2.packets().sendSpawnedObject(object);
			}
		}
	}
	
	public static boolean isTileClipped(int x, int y, int z, int size) {
		for (int tileX = x; tileX < x + size; tileX++)
			for (int tileY = y; tileY < y + size; tileY++)
				if (getMask(z, tileX, tileY) != 0)
					return true;
		return false;
	}

}