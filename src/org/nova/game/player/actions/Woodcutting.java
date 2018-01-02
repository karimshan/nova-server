package org.nova.game.player.actions;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.content.ItemEffects;
import org.nova.utility.misc.Misc;

public final class Woodcutting extends Action {

	public static enum TreeDefinitions {

		NORMAL(1, 25, 1511, 20, 4, 1341, 8, 0), // TODO
		DEAD(1, 25, 1511, 20, 4, 12733, 8, 0),

		OAK(15, 37.5, 1521, 30, 4, 1341, 15, 15), // TODO

		WILLOW(30, 67.5, 1519, 60, 4, 1341, 51, 15), // TODO

		MAPLE(45, 100, 1517, 83, 16, 31057, 72, 10),

		YEW(60, 175, 1515, 120, 17, 1341, 94, 10), // TODO

		IVY(68, 332.5, -1, 120, 17, 46319, 58, 10),

		MAGIC(75, 250, 1513, 150, 21, 37824, 121, 10),

		CURSED_MAGIC(82, 250, 1513, 150, 21, 37822, 121, 10),
		
		MUTATED_JADE(95, 375.5, 21358, 185, 15, -1, 199, 10);
		private int level;
		private double xp;
		private int logsId;
		private int logBaseTime;
		private int logRandomTime;
		private int stumpId;
		private int respawnDelay;
		private int randomLifeProbability;

		private TreeDefinitions(int level, double xp, int logsId,
				int logBaseTime, int logRandomTime, int stumpId,
				int respawnDelay, int randomLifeProbability) {
			this.level = level;
			this.xp = xp;
			this.logsId = logsId;
			this.logBaseTime = logBaseTime;
			this.logRandomTime = logRandomTime;
			this.stumpId = stumpId;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getLogsId() {
			return logsId;
		}

		public int getLogBaseTime() {
			return logBaseTime;
		}

		public int getLogRandomTime() {
			return logRandomTime;
		}

		public int getStumpId() {
			return stumpId;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int getRandomLifeProbability() {
			return randomLifeProbability;
		}
	}

	private GlobalObject tree;
	private TreeDefinitions definitions;

	private int emoteId;
	private boolean usingBeaver = false;
	private int axeTime;

	public Woodcutting(GlobalObject tree, TreeDefinitions definitions) {
		this.tree = tree;
		this.definitions = definitions;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.packets()
				.sendMessage(
						usingBeaver ? "Your beaver uses its strong teeth to chop down the tree..."
								: "You swing your hatchet at the "
										+ (TreeDefinitions.IVY == definitions ? "ivy"
												: "tree") + "...");
		setActionDelay(player, getWoodcuttingDelay(player));
		return true;
	}

	private int getWoodcuttingDelay(Player player) {
		int summoningBonus = player.getFamiliar() != null ? (player
				.getFamiliar().getId() == 6808 || player.getFamiliar().getId() == 6807) ? 10
				: 0
				: 0;
		int wcTimer = definitions.getLogBaseTime()
				- (player.getSkills().getLevel(8) + summoningBonus)
				- Misc.getRandom(axeTime);
		if (wcTimer < 1 + definitions.getLogRandomTime())
			wcTimer = 1 + Misc.getRandom(definitions.getLogRandomTime());
		wcTimer /= player.getAuraManager().getWoodcuttingAccurayMultiplier();
		return wcTimer;
	}

	private boolean checkAll(Player player) {
		if (!hasAxe(player)) {
			player.packets().sendMessage(
					"You need a hatchet to chop down this tree.");
			return false;
		}
		if (!setAxe(player)) {
			player.packets().sendMessage(
					"You dont have the required level to use that axe.");
			return false;
		}
		if (!hasWoodcuttingLevel(player))
			return false;
		if (!player.getInventory().hasFreeSlots()) {
			player.packets().sendMessage(
					"Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	private boolean hasWoodcuttingLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(8)) {
			player.packets().sendMessage(
					"You need a woodcutting level of " + definitions.getLevel()
							+ " to chop down this tree.");
			return false;
		}
		return true;
	}

	private boolean setAxe(Player player) {
		int level = player.getSkills().getLevel(8);
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId != -1) {
			switch (weaponId) {
			case 6739: // dragon axe
				if (level >= 61) {
					emoteId = 2846;
					axeTime = 13;
					return true;
				}
				break;
			case 1359: // rune axe
				if (level >= 41) {
					emoteId = 867;
					axeTime = 10;
					return true;
				}
				break;
			case 1357: // adam axe
				if (level >= 31) {
					emoteId = 869;
					axeTime = 7;
					return true;
				}
				break;
			case 1355: // mit axe
				if (level >= 21) {
					emoteId = 871;
					axeTime = 5;
					return true;
				}
				break;
			case 1361: // black axe
				if (level >= 11) {
					emoteId = 873;
					axeTime = 4;
					return true;
				}
				break;
			case 1353: // steel axe
				if (level >= 6) {
					emoteId = 875;
					axeTime = 3;
					return true;
				}
				break;
			case 1349: // iron axe
				emoteId = 877;
				axeTime = 2;
				return true;
			case 1351: // bronze axe
				emoteId = 879;
				axeTime = 1;
				return true;
			case 13661: // Inferno adze
				if (level >= 61) {
					emoteId = 10251;
					axeTime = 13;
					return true;
				}
				break;
			}
		}
		if (player.getInventory().containsOneItem(6739)) {
			if (level >= 61) {
				emoteId = 2846;
				axeTime = 13;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1359)) {
			if (level >= 41) {
				emoteId = 867;
				axeTime = 10;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1357)) {
			if (level >= 31) {
				emoteId = 869;
				axeTime = 7;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1355)) {
			if (level >= 21) {
				emoteId = 871;
				axeTime = 5;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1361)) {
			if (level >= 11) {
				emoteId = 873;
				axeTime = 4;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1353)) {
			if (level >= 6) {
				emoteId = 875;
				axeTime = 3;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1349)) {
			emoteId = 877;
			axeTime = 2;
			return true;
		}
		if (player.getInventory().containsOneItem(1351)) {
			emoteId = 879;
			axeTime = 1;
			return true;
		}
		if (player.getInventory().containsOneItem(13661)) {
			if (level >= 61) {
				emoteId = 10251;
				axeTime = 13;
				return true;
			}
		}
		return false;

	}

	private boolean hasAxe(Player player) {
		if (player.getInventory().containsOneItem(1351, 1349, 1353, 1355, 1357,
				1361, 1359, 6739, 13661))
			return true;
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1)
			return false;
		switch (weaponId) {
		case 1351:// Bronze Axe
		case 1349:// Iron Axe
		case 1353:// Steel Axe
		case 1361:// Black Axe
		case 1355:// Mithril Axe
		case 1357:// Adamant Axe
		case 1359:// Rune Axe
		case 6739:// Dragon Axe
		case 13661: // Inferno adze
			return true;
		default:
			return false;
		}

	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(usingBeaver ? 1 : emoteId));
		return checkTree(player);
	}

	private boolean usedDeplateAurora;

	@Override
	public int processWithDelay(Player player) {
		addLog(player);
		if (!usedDeplateAurora
				&& (1 + Math.random()) < player.getAuraManager()
						.getChanceNotDepleteMN_WC()) {
			usedDeplateAurora = true;
		} else if (Misc.getRandom(definitions.getRandomLifeProbability()) <= 0) {
			long time = definitions.respawnDelay * 600;
			Game.spawnTemporaryObject(
					new GlobalObject(definitions.getStumpId(), tree.getType(),
							tree.getRotation(), tree.getX(), tree.getY(), tree
									.getZ()), time);
			if (tree.getZ() < 3) {
				GlobalObject object = Game.getObject(new Location(
						tree.getX() - 1, tree.getY() - 1, tree.getZ() + 1));
				/*
				 * if(object == null) { object = World.getObject(new Location(
				 * tree.getX(), tree.getY() - 1, tree.getPlane() + 1));
				 * if(object == null) { object = World.getObject(new Location(
				 * tree.getX() - 1, tree.getY(), tree.getPlane() + 1));
				 * if(object == null) { object = World.getObject(new Location(
				 * tree.getX(), tree.getY(), tree.getPlane() + 1)); } } }
				 */
				if (object != null)
					Game.removeTemporaryObject(object, time, false);
			}
			player.setNextAnimation(new Animation(-1));
			return -1;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.setNextAnimation(new Animation(-1));
			player.packets().sendMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		return getWoodcuttingDelay(player);
	}

	private void addLog(Player player) {
		double xpBoost = 1.00;
		if (player.getEquipment().getChestId() == 10939)
			xpBoost += 0.008;
		if (player.getEquipment().getLegsId() == 10940)
			xpBoost += 0.006;
		if (player.getEquipment().getHatId() == 10941)
			xpBoost += 0.004;
		if (player.getEquipment().getBootsId() == 10933)
			xpBoost += 0.002;
		if (player.getEquipment().getChestId() == 10939
				&& player.getEquipment().getLegsId() == 10940
				&& player.getEquipment().getHatId() == 10941
				&& player.getEquipment().getBootsId() == 10933)
			xpBoost += 0.005;
		if (player.getItembonusactions().hasHatiHead()) 
			xpBoost += 0.120;
		player.getSkills().addXp(8, definitions.getXp() * xpBoost);
		player.getInventory().addItem(definitions.getLogsId(), 1);
		ItemEffects.sendNestRandomly(player);
		
		if (definitions == TreeDefinitions.IVY) {
			player.packets().sendMessage(
					"You succesfully cut an ivy vine.", true);
			// todo gfx
		} else {
			String logName = ItemDefinition
					.get(definitions.getLogsId()).getName()
					.toLowerCase();
			if(!player.tasks().hasFinished(player.tasks().ldTasks(), (byte) player.tasks().indexFromName("Lumberjack"))) {
				player.tasks().taskCompletion("Lumberjack");
				player.getSkills().addRSXP(Skills.WOODCUTTING, 1000);
			}
			player.packets().sendMessage(
					"You get some " + logName + ".", true);
			// todo infernal adze
		}
	}

	private boolean checkTree(Player player) {
		return Game.getRegion(tree.getRegionId()).containsObject(tree.getId(),
				tree);
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
