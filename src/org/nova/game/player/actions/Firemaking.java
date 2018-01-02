package org.nova.game.player.actions;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.FloorItem;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.npc.familiar.Familiar;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.controlers.Duelarena;
import org.nova.network.decoders.packets.handlers.items.InventoryActionHandler;
import org.nova.utility.misc.Misc;

public class Firemaking extends Action {

	public static enum Fire {
		NORMAL(1511, 1, 300, 2732, 40, 20)
		, ACHEY(2862, 1, 300, 2732, 40, 1)
		, OAK(1521, 15, 450, 2732, 60, 1)
		, WILLOW(1519, 30, 450, 2732, 90, 1)
		, TEAK(6333, 35, 450, 2732, 105, 1)
		, ARCTIC_PINE(10810, 42, 500, 2732, 125, 1)
		, MAPLE(1517, 45, 500, 2732, 135, 1)
		, MAHOGANY(6332, 50,700, 2732, 157.5, 1)
		, EUCALYPTUS(12581, 58, 700, 2732, 193.5, 1)
		, YEW(1515, 60, 800, 2732, 202.5, 1)
		, MAGIC(1513, 75, 900, 2732, 303.8,1)
		, CURSED_MAGIC(13567, 82, 1000, 2732, 303.8, 1);

		private int logId;
		private int level;
		private int life;
		private int fireId;
		private int time;
		private double xp;

		Fire(int logId, int level, int life, int fireId, double xp, int time) {
			this.logId = logId;
			this.level = level;
			this.life = life;
			this.fireId = fireId;
			this.xp = xp;
			this.time = time;
		}

		public int getLogId() {
			return logId;
		}

		public int getLevel() {
			return level;
		}

		public int getLife() {
			return (life * 600);
		}

		public int getFireId() {
			return fireId;
		}

		public double getExperience() {
			return xp;
		}

		public int getTime() {
			return time;
		}
	}

	private Fire fire;

	public Firemaking(Fire fire) {
		this.fire = fire;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.packets().sendMessage("You attempt to light the logs.",
				true);
		player.getInventory().deleteItem(fire.getLogId(), 1);
		Game.addGroundItem(new Item(fire.getLogId(), 1),
				new Location(player), player, false, 180, true);
		Long time = (Long) player.getTemporaryAttributtes().remove("Fire");
		boolean quickFire = time != null && time > Misc.currentTimeMillis();
		setActionDelay(player, quickFire ? 1 : 2);
		if (!quickFire)
			player.setNextAnimation(new Animation(733));
		return true;
	}

	public static boolean isFiremaking(Player player, Item item1, Item item2) {
		Item log = InventoryActionHandler.contains(590, item1, item2);
//		Item redFirelight = InventoryActionHandler.contains(7329, item1, item2);
//		Item greenFirelight = InventoryActionHandler.contains(7330, item1, item2);
//		Item blueFirelight = InventoryActionHandler.contains(7331, item1, item2);
//		Item purpleFirelight = InventoryActionHandler.contains(10326, item1, item2);
//		Item whiteFirelight = InventoryActionHandler.contains(10327, item1, item2);
		if (log == null)
			return false;
		return isFiremaking(player, log.getId());
	}

	public static boolean isFiremaking(Player player, int logId) {
		for (Fire fire : Fire.values()) {
			if (fire.getLogId() == logId) {
				player.getActionManager().setAction(new Firemaking(fire));
				return true;
			}
		}
		return false;

	}

	public static void startFamiliarFire(Player player, Familiar familiar,
			Fire fire) {
		if (player.getFamiliar().getId() == 7378
				|| player.getFamiliar().getId() == 7377) {
		}
	}

	public boolean checkAll(Player player) {
		if (!player.getInventory().containsItem(590, 1)) {
			player.packets().sendMessage(
					"You do not have the required items to light this.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.FIREMAKING) < fire.getLevel()) {
			player.packets().sendMessage(
					"You do not have the required level to light this.");
			return false;
		}
		if (!Game.canMoveNPC(player.getZ(), player.getX(), player.getY(),
				1) // cliped
				|| Game.getRegion(player.getRegionId()).getSpawnedObject(
						player) != null
						|| player.getControllerManager().getControler() instanceof Duelarena) { // contains
			// object
			player.packets().sendMessage("You can't light a fire here.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	public static double increasedExperience(Player player, double totalXp) {
		if (player.getEquipment().getGlovesId() == 13660)
			totalXp *= 1.025;
		if (player.getEquipment().getRingId() == 13659)
			totalXp *= 1.025;
		return totalXp;
	}

	@Override
	public int processWithDelay(final Player player) {
		final Location tile = new Location(player);
		if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
			if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
				if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
					player.addWalkSteps(player.getX(), player.getY() - 1, 1);
		if(!player.tasks().hasFinished(player.tasks().ldTasks(), (byte) player.tasks().indexFromName("HotStuff"))) {
			player.tasks().taskCompletion("HotStuff");
			player.getSkills().addRSXP(Skills.FIREMAKING, 1500);
			player.getSkills().addRSXP(Skills.WOODCUTTING, 500);
		}
		player.packets().sendMessage(
				"The fire catches and the logs begin to burn.", true);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				final FloorItem item = Game.getRegion(tile.getRegionId())
						.getGroundItem(fire.getLogId(), tile, player);
				if (item == null)
					return;
				if (!Game.removeGroundItem(player, item, false))
					return;
				//player.getPackets().sendSound(2594, 0, 1); //TODO find fire sound
				Game.spawnTempGroundObject(new GlobalObject(fire.getFireId(),
						10, 0, tile.getX(), tile.getY(), tile.getZ()), 592,
						fire.getLife());
				player.getSkills().addXp(Skills.FIREMAKING, increasedExperience(player, fire.getExperience()));
				player.faceLocation(tile);
			}
		}, 1);
		player.getTemporaryAttributtes().put("Fire",
				Misc.currentTimeMillis() + 1800);
		return -1;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

}
