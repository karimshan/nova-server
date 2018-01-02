package org.nova.game.player.controlers;

import org.nova.Constants;
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
import org.nova.game.player.Player;
import org.nova.game.player.content.Pots;
import org.nova.utility.misc.Misc;

public class TowersPkControler extends Controller {

	private static Location[] RESPAWN_PLACES = {
			new Location(10008, 10009, 0), new Location(10064, 10009, 0),
			new Location(10008, 10065, 0), new Location(10064, 10065, 0) };

	private static Location[] ENTRACE_PLACES = {
			new Location(10015, 10054, 0), new Location(10015, 9998, 0),
			new Location(10071, 9998, 0), new Location(10071, 10054, 0) };

	private static int[] WIN_MATCH_SOUND_EFFECTS = { 121, 122, 123, 124 };

	/*
	 * climbed tower musics: Waste Defaced Slain to Waste
	 */
	private static int[] CLIMBED_MUSICS = new int[] { 426, 543, 856 };

	@Override
	public void start() {
		Wilderness.checkBoosts(player);
	}

	@Override
	public boolean canAttack(Entity target) {
		return canHit(target);
	}

	@Override
	public boolean canHit(Entity target) {
		if (target instanceof NPC || player.getZ() != 1)
			return true;
		Player p2 = (Player) target;
		if (Math.abs(player.getSkills().getCombatLevel()
				- p2.getSkills().getCombatLevel()) > 10)
			return false;
		return true;
	}

	@Override
	public boolean login() {
		if (player.getZ() != 0)
			player.setCanPvp(true);
		return false;
	}

	private static void yellChilds(String text) {
		for (NPC n : Game.getNPCs()) {
			if (n == null || n.getId() < 6334 || n.getId() > 6337)
				continue;
			n.setNextForceTalk(new ForceTalk(text));
		}
	}

	@Override
	public boolean processObjectClick1(final GlobalObject object) {
		if (object.getId() == 31529 && object.getZ() == 0) {
			climbTower(object);
			return false;
		} else if (object.getId() == 31530 && object.getZ() == 1) {
			player.getMatrixDialogues().startDialogue("SimpleMessage",
					"You're not allowed to climb these stairs.");
		} else if (object.getId() == 31529 && object.getZ() == 1) {
			player.useStairs(-1,
					new Location(player.getX(), player.getY(), 2), 0, 1);
			return false;
		} else if (object.getId() == 31530 && object.getZ() == 2) {
			player.useStairs(-1,
					new Location(player.getX(), player.getY(), 1), 0, 1);
			return false;
		} else if (object.getId() == 31651) {
			leaveTower();
			return false;
		}
		return true;
	}

	public static void enter(final Player player) {
		player.setLocation(ENTRACE_PLACES[Misc
				.random(ENTRACE_PLACES.length)]);
		player.getControllerManager().startController("TowersPkControler");
		player.packets().sendMessage(
				"You find yourself on a beach near a giant abandoned tower.");
		player.packets()
				.sendMessage(
						"<col=ff0000>WARNING! IF YOU CLIMB THE TOWER YOU WILL BE ON A PVP AREA!");
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
			
			}
		}, 1);
	}

	public void leaveTower() {
		player.setLocation(Constants.RESPAWN_PLAYER_LOCATION);
		player.packets().sendMessage(
				"You start swimming and find yourself at home.");
		removeControler();
	}

	@Override
	public void magicTeleported(int type) {
		if (player.isCanPvp())
			player.setCanPvp(false);
		removeControler();
	}

	public void climbTower(GlobalObject object) {
		if (player.getFamiliar() != null) {
			player.packets().sendMessage(
					"You can't familiars into field.");
			return;
		}
		if (containsItem("pouch")) {
			player.packets().sendMessage(
					"You can't take pouches into field.");
			return;
		}
		if (containsItem("extreme", "overload", "super prayer",
				"recover special")) {
			player.packets().sendMessage(
					"You can't take extreme potions into field.");
			return;
		}
		if (containsItem("spirit shield")) {
			player.packets().sendMessage(
					"You can't take spirit shields into field.");
			return;
		}
		if (containsItem("torva", "virtus", "pernix")) {
			player.packets().sendMessage(
					"You can't take nex armors into field.");
			return;
		}
		if (containsItem("statius", "vesta", "morrigan", "zuriel")) {
			player.packets().sendMessage(
					"You can't take pvp armors into field.");
			return;
		}
		if (containsItem("chaotic", "primal", "celestial", "sagittarian",
				"promethium", "spiritbloom", "tyrannoleather", "gorgonite",
				"runic", "megaleather")
				|| containsItem(19669, 20820)) {
			player.packets()
					.sendMessage(
							"You can't take overpowered dungeoneering items into field.");
			return;
		}
		if (containsItem("swift gloves", "spellcaster gloves", "goliath gloves")) {
			player.packets().sendMessage(
					"You can't take overpowered gloves into field.");
			return;
		}
		if (containsItem("ragefire boots", "glaiven boots", "steadfast boots")) {
			player.packets().sendMessage(
					"You can't take overpowered boots into field.");
			return;
		}
		if (containsItem(20769, 20771)) {
			player.packets().sendMessage(
					"You can't take overpowered capes into field.");
			return;
		}
		if (containsItem("ganodermic", "polypore")) {
			player.packets().sendMessage(
					"You can't take overpowered magic gear into field.");
			return;
		}
		if (containsItem("vanguard", "trickster", "battle-mage")) {
			player.packets().sendMessage(
					"You can't take hybrid minigame armors into field.");
			return;
		}

		if (player.getOverloadDelay() > 0)
			Pots.resetOverLoadEffect(player);
		player.setLocation(new Location(object.getX() + 2, object.getY(),
				1));
		player.getMusicsManager().playMusic(
				CLIMBED_MUSICS[Misc.random(CLIMBED_MUSICS.length)]);
		player.addStopDelay(2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setCanPvp(true);
			}
		});
	}

	public boolean containsItem(String... names) {
		for (Item item : player.getEquipment().getItems().getItems()) {
			if (item == null)
				continue;
			String itemName = item.defs().getName().toLowerCase();
			for (String name : names)
				if (itemName.contains(name))
					return true;
		}
		for (Item item : player.getInventory().getItems().getItems()) {
			if (item == null)
				continue;
			String itemName = item.defs().getName().toLowerCase();
			for (String name : names)
				if (itemName.contains(name))
					return true;
		}
		return false;
	}

	public boolean containsItem(int... ids) {
		for (Item item : player.getEquipment().getItems().getItems()) {
			if (item == null)
				continue;
			int itemId = item.getId();
			for (int id : ids)
				if (itemId == id)
					return true;
		}
		for (Item item : player.getInventory().getItems().getItems()) {
			if (item == null)
				continue;
			int itemId = item.getId();
			for (int id : ids)
				if (itemId == id)
					return true;
		}
		return false;
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

	@Override
	public boolean sendDeath() {
		player.addStopDelay(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.packets().sendMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					Player killer = player.getMostDamageReceivedSourcePlayer();
					if (killer != null) {
						killer.packets()
								.sendMusicEffect(
										WIN_MATCH_SOUND_EFFECTS[Misc
												.random(WIN_MATCH_SOUND_EFFECTS.length)]);
						yellChilds(killer.getDisplayName() + " killed "
								+ player.getDisplayName() + "!");
					}
					player.reset();
					player.setCanPvp(false);
					player.setLocation(new Location(RESPAWN_PLACES[Misc
							.random(RESPAWN_PLACES.length)], 1));
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.packets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean processItemTeleport(Location toTile) {
		if (player.getTeleBlockDelay() > Misc.currentTimeMillis()) {
			player.packets().sendMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectTeleport(Location toTile) {
		if (player.getTeleBlockDelay() > Misc.currentTimeMillis()) {
			player.packets().sendMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processMagicTeleport(Location toTile) {
		if (player.getTeleBlockDelay() > Misc.currentTimeMillis()) {
			player.packets().sendMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}

}
