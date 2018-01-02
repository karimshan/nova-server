package org.nova.game.player.controlers;

import java.util.LinkedList;
import java.util.List;

import org.nova.game.entity.Entity;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.ForceTalk;
import org.nova.game.npc.godwars.barrows.BarrowsBrother;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.utility.misc.Misc;


/**
 * 
 * @author Raghav/Cjay
 * 
 */
public class Barrows extends Controller {

	private BarrowsBrother brother;
	private Brothers brotherInform;
	private int barrowsFaces;
	private int resetFace = -1;

	/**
	 * Handling of barrows brothers
	 */
	public static enum Brothers {
		AHRIM(2025, 0, 66017, 6702, new Location(3565, 3289, 0),
				new Location(3561, 3286, 0), new Location(3567, 3292, 0),
				new Location(3557, 9703, 0)),

		VERAC(2030, 1, 66016, 6707, new Location(3556, 3297, 0),
				new Location(3554, 3294, 0), new Location(3559, 3300, 0),
				new Location(3578, 9706, 0)),

		DHAROK(2026, 2, 63177, 6703, new Location(3574, 3297, 0),
				new Location(3570, 3293, 0), new Location(3576, 3299, 0),
				new Location(3556, 9718, 0)),

		GUTHAN(2027, 3, 66020, 6704, new Location(3576, 3281, 0),
				new Location(3574, 3279, 0), new Location(3579, 3284, 0),
				new Location(3534, 9704, 0)),

		TORAG(2029, 4, 66019, 6706, new Location(3553, 3283, 0),
				new Location(3550, 3280, 0), new Location(3555, 3285, 0),
				new Location(3568, 9683, 0)),

		KARIL(2028, 5, 66018, 6705, new Location(3565, 3276, 0),
				new Location(3563, 3273, 0), new Location(3568, 3279, 0),
				new Location(3546, 9684, 0));

		private int npcId, coffinId, stairsId, index;
		private Location exitLocation, moundArea, moundArea2, playerEntry;

		Brothers(int npcId, int index, int coffinId, int stairsId,
				Location exitLocation, Location moundArea,
				Location moundArea2, Location playerEntry) {
			this.npcId = npcId;
			this.index = index;
			this.coffinId = coffinId;
			this.stairsId = stairsId;
			this.exitLocation = exitLocation;
			this.moundArea = moundArea;
			this.moundArea2 = moundArea2;
			this.playerEntry = playerEntry;
		}/*
		public void HandleBrother(String brother){
			int io = Misc.random(3);
			switch(brother){
				case "ahrim":
					switch(io){
					case 0:
						break;
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					}
				break;
			
			
			}		
		}*/
		
		public int getNpcId() {
			return npcId;
		}

		public int getCoffinId() {
			return coffinId;
		}

		public int getStairsId() {
			return stairsId;
		}

		public Location getExitLocation() {
			return exitLocation;
		}

		public Location getMoundArea() {
			return moundArea;
		}

		public Location getMoundArea2() {
			return moundArea2;
		}

		public Location getplayerEntry() {
			return playerEntry;
		}

		public int getIndex() {
			return index;
		}
	}

	@Override
	public boolean login() {
		start();
		player.getTemporaryAttributtes().put("lootedChest", Boolean.FALSE);
		return false;
	}

	@Override
	public boolean logout() {
		if (brother != null)
			brother.finish();
		if (player.getTemporaryAttributtes().get("lootedChest") == Boolean.TRUE) { // if
																					// looted
																					// chest
																					// and
																					// logout,
																					// send
																					// him
																					// back
																					// to
																					// barrows
																					// top.
			teleport();
			player.setLocation(new Location(3565, 3289, 0));
		}
		return false;
	}

	@Override
	public void forceClose() {
		if (brother != null)
			brother.finish();
		player.packets().sendBlackOut(0);
	}

	@Override
	public void sendInterfaces() {
		player.interfaces().sendTab(
				player.interfaces().isFullScreen() ? 5 : 17, 24);
		player.packets().sendConfig(453, slayedBrothers());
	}

	@Override
	public void start() {
		player.packets().sendBlackOut(2);
		this.sendInterfaces();
		this.giveHiddenBrother();
		barrowsFaces = 10 + Misc.random(10);
	}

	@Override
	public boolean canAttack(Entity target) {
		if (target != brother) {
			player.packets().sendMessage("This isn't your target.");
			return false;
		}
		return true;
	}

	public void magicTeleported(int type) {
		teleport();
	}

	public void teleport() {
		remove();
		if (sentEarthquake)
			player.packets().sendStopCameraShake();
		player.packets().closeInterface(
				player.interfaces().isFullScreen() ? 5 : 17);
	}

	@Override
	public boolean sendDeath() {
		remove();
		return true;
	}

	public void remove() {
		player.packets().sendBlackOut(0);
		player.hints().removeUnsavedHintIcon();
		if (brother != null) {
			brother.finish();
			brother = null;
		}
		removeControler();
		player.packets().closeInterface(
				player.interfaces().isFullScreen() ? 5 : 17);
		player.packets().sendConfig(1270, 0);
		barrowsFaces = -1;
		resetFace = -1;
	}

	@Override
	public boolean processObjectClick1(final GlobalObject object) {
		switch (object.getId()) {

		case 6775:
			player.packets().sendMessage("You found nothing.");
			return false;
		case 10284: // Chest
			if (player.getKilledBarrowBrothers()[player.getHiddenBrother()])
				player.getTemporaryAttributtes().put("canLoot", Boolean.TRUE);
			if (brother != null) {
				player.packets().sendMessage("You found nothing.");
				return false;
			}
			this.handleChest();
			return false;
		case 6703: // Barrows
		case 6704:
		case 6702:
		case 6705:
		case 6707:
		case 6706:
			for (Brothers bro : Brothers.values()) {
				if (object.getId() == bro.getStairsId()) {
					player.setLocation(new Location(bro.getExitLocation()
							.getX(), bro.getExitLocation().getY(), 0));
					remove();
					return false;
				}
			}
			return false;
		case 66016:
		case 66017:
		case 63177:
		case 66018:
		case 66019:
		case 66020:
			if (brother != null) {
				player.packets().sendMessage("You found nothing.");
				return false;
			} else {
				for (Brothers bro : Brothers.values()) {
					if (object.getId() == bro.getCoffinId()) {
						if (player.getHiddenBrother() == bro.getIndex()) {
							player.getMatrixDialogues().startDialogue(
									"BarrowsD");
							return false;
						}
						if (player.getKilledBarrowBrothers()[bro.getIndex()]) {
							player.packets().sendMessage(
									"You found nothing.");
							return false;
						}
						brotherInform = bro;
						brother = new BarrowsBrother(bro.getNpcId(),
								new Location(player.getX(), player.getY(),
										player.getZ()), this);
						brother.setTarget(player);
						brother.setAttackedBy(player);
						brother.getCombat().setTarget(player); //siit pitäs toimii juu
						brother.setNextForceTalk(new ForceTalk(
								"You dare disturb my rest!"));
						player.hints().addHintIcon(brother, 1,
								false, false);
						return false;
					}
				}
			}
			return false;
		default:
			/*
			 * int x = object.getX(); int y = object.getY(); if
			 * (object.getRotation() == 0 && player.getX() >= x) { x--; } else
			 * if (object.getRotation() == 2 && player.getX() <= x) { x++; }
			 * else if (object.getRotation() == 1 && player.getY() <= y) { y++;
			 * } else if (object.getRotation() == 3 && player.getY() >= y) {
			 * y--; } player.addWalkSteps(x, y, -1, false); if
			 * (Utils.getRandom(3) == 1) this.createLiveBrother(); return false;
			 */
			return true;
		}
	}

	public int slayedBrothers() {
		int config = 0;
		for (int i = 0; i < player.getKilledBarrowBrothers().length; i++) {
			if (!player.getKilledBarrowBrothers()[i]) {
				continue;
			}
			config |= 1 << i;
		}
		int killCount = player.getBarrowsKillCount();
		return (killCount << 1) << 16 | config;
	}

	private void reset() {
		for (int i = 0; i < player.getKilledBarrowBrothers().length; i++) {
			player.getKilledBarrowBrothers()[i] = false;
		}
		player.setHiddenBrother(0);
		player.setBarrowsKillCount(0);
		if (brother != null) {
			brother.finish();
			brother = null;
		}
		player.packets().sendConfig(453, slayedBrothers());
	}

	public void killedBrother() {
		if (brother != null) {
			player.hints().removeUnsavedHintIcon();
			if (brotherInform != null) {
				player.getKilledBarrowBrothers()[brotherInform.getIndex()] = true;
				player.setBarrowsKillCount(player.getBarrowsKillCount() + 1);
				player.packets().sendConfig(453, slayedBrothers());
			}
			brother = null;
		}
	}

	public static boolean digToBrother(final Player player) {
		int coordX = player.getX();
		int coordY = player.getY();
		for (Brothers bro : Brothers.values()) {
			if (coordX >= bro.getMoundArea().getX()
					&& coordX <= bro.getMoundArea2().getX()
					&& coordY >= bro.getMoundArea().getY()
					&& coordY <= bro.getMoundArea2().getY()) {
				player.useStairs(830, new Location(
						bro.getplayerEntry().getX(), bro.getplayerEntry()
								.getY(), 3), 2, 1,
						"You've broken into a crypt.");
				player.getControllerManager().startController("Barrows");
				return true;
			}
		}
		return false;
	}

	private void giveHiddenBrother() {
		if (player.getHiddenBrother() <= 0)
			player.setHiddenBrother(Misc.getRandom(5));
	}

	@SuppressWarnings("unused")
	private void createLiveBrother() {
		List<Brothers> live = new LinkedList<Brothers>();
		for (Brothers bro : Brothers.values()) {
			if (player.getKilledBarrowBrothers()[bro.getIndex()] == false
					&& player.getHiddenBrother() != bro.getIndex()) {
				live.add(bro);
			}
		}
		if (brother != null && brother.isUnderCombat()) {
			brother.finish();
			brother = null;
			return;
		}
		if (live.size() > 0 || brother != null && brother.isUnderCombat()) {
			Brothers bro = live.get((Misc.random(live.size())));
			brotherInform = bro;
			brother = new BarrowsBrother(bro.getNpcId(), new Location(
					player.getX(), player.getY(), player.getZ()), this);
			brother.setTarget(player);
			brother.setNextForceTalk(new ForceTalk(
					"You think you will escape that easily?!"));
			player.hints().addHintIcon(brother, 1, false, false);
			live.clear();
		}
	}

	private void spawnLastBrother() {
		if (player.getKilledBarrowBrothers()[player.getHiddenBrother()])
			return;
		for (Brothers bro : Brothers.values()) {
			if (player.getHiddenBrother() == bro.getIndex()) {
				brotherInform = bro;
				brother = new BarrowsBrother(bro.getNpcId(), new Location(
						player.getX(), player.getY(), player.getZ()), this);
				brother.setTarget(player);
				brother.setNextForceTalk(new ForceTalk(
						"You dare disturb my rest!"));
				player.hints().addHintIcon(brother, 1, false, false);
			}
		}
	}

	private void drainPrayer() {
		int activeLevel = player.getPrayer().getPrayerpoints();
		if (activeLevel > 0) {
			int level = player.getSkills().getLevelFromXP(Skills.PRAYER) * 10;
			player.getPrayer().drainPrayer(level / 6);
		}
	}

	private boolean sentEarthquake;
	private int headShot;

	private void sendHeadConfigurations() {
		int[] array = player.getZ() == 0 ? HEAD_INDEXES[1]
				: HEAD_INDEXES[0];
		int head = array[(int) (Math.random() * array.length)];
		if (resetFace == 0) {
			player.packets().sendItemOnIComponent(24, headShot, -1, 0);
			player.packets().sendIComponentAnimation(-1, 24, headShot);
			return;
		}
		headShot = Misc.random(5);
		if (headShot == 0)
			headShot = 1;
		drainPrayer();
		player.packets().sendItemOnIComponent(24, headShot, head, 0);
		player.packets().sendIComponentAnimation(9810, 24, headShot);
	}

	@Override
	public void process() {
		if (barrowsFaces > 0)
			barrowsFaces--;
		if (barrowsFaces == 0) {
			sendHeadConfigurations();
			barrowsFaces = -1;
			resetFace = 3;
		}
		if (resetFace > 0)
			resetFace--;
		if (resetFace == 0) {
			sendHeadConfigurations();
			resetFace = -1;
			barrowsFaces = 19 + Misc.random(4);
		}
	}

	private int[] BARROW_REWARDS = { 4708, 4710, 4712, 4714, 4716, 4718, 4720,
			4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747,
			4749, 4751, 4753, 4755, 4757, 4749, };

	private int[] NOTED_LOOT = { 1080, 1114, 1148, 1164, 1202, 1214, 1276,
			1304, 1320, 1334, 1402, 1404, 562, 563, 564, 565, };

private final int[] COMMON_LOOT = {  558, 560, 562, 565, 4740, 995 };
			
	private final int[] UNCOMMON_LOOT = { 165, 159, 141, 129, 6969};
	
	private final int[] RARE_LOOT = { 4708, 4710, 4712, 4714, 4716, 4718, 4720,
			4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747,
			4749, 4751, 4753, 4755, 4757, 4749, };
	
	private final int[] VERYRARE_LOOT = { 1149, 985, 987, };

	private final int[][] HEAD_INDEXES = {
			{ 4761, 4763, 4765, 4767, 4769, 4771 },
			{ 4761, 4762, 4763, 4764, 4765, 4766, 4767, 4768, 4769, 4770, 4771,
					4772 }, };

	private void handleChest() {
		if (player.getTemporaryAttributtes().get("lootedChest") == Boolean.TRUE) {
			player.packets().sendMessage("You found nothing.");
			return;
		}
		if (player.getTemporaryAttributtes().get("canLoot") != null) {
			rewards();
		} else {
			spawnLastBrother();
	}
}

	private void addItems(int i, int j, int k, int l) {
			player.getInventory().addItem(i, j);
			player.getInventory().addItem(k, l);
			player.getTemporaryAttributtes().remove("canLoot");
			player.getTemporaryAttributtes().put("lootedChest", Boolean.TRUE);
			player.packets().sendCameraShake(3, 25, 50, 25, 50);
			player.packets().sendSpawnedObject(
					new GlobalObject(6775, 10, 0, 3551, 9695, 0));
			sentEarthquake = true;
			this.reset();
	
	}

	private void rewards() {
			int[] reward = new int[2];
			int[] rewardN = new int[2];
			boolean[] stages = new boolean[2];
			//Reward 1
			int chance = Misc.getRandom(20);
			if (chance <= 6 && !stages[0])  {
				reward[0] = COMMON_LOOT[Misc.getRandom(COMMON_LOOT.length - 1)];
				if (reward[0] == 558) {
					rewardN[0] = Misc.getRandom(1795);
				} else if (reward[0] == 560) {
					rewardN[0] = Misc.getRandom(391);
				} else if (reward[0] == 562) {
					rewardN[0] = Misc.getRandom(773);
				} else if (reward[0] == 565) {
					rewardN[0] = Misc.getRandom(164);
				} else if (reward[0] == 4740) {
					rewardN[0] = Misc.getRandom(188);
				} else if (reward[0] == 995) {
					rewardN[0] = Misc.getRandom(4162);
				}
				stages[0] = true;
			} else if (6 < chance && chance <= 12 && !stages[0]) {
				reward[0] = UNCOMMON_LOOT[Misc.getRandom(UNCOMMON_LOOT.length - 1)];
				if (reward[0] == 6969) {
					rewardN[0] = 4;
				} else {
					rewardN[0] = 1;
				}
				stages[0] = true;
			} else if (12 < chance && chance <= 18) {
				reward[0] = RARE_LOOT[Misc.getRandom(RARE_LOOT.length - 1)];
				rewardN[0] = 1;
				stages[0] = true;
			} else if (18 < chance && chance <= 20) {
				reward[0] = VERYRARE_LOOT[Misc.getRandom(VERYRARE_LOOT.length - 1)];
				rewardN[0] = 1;
				stages[0] = true;
			}
			//Reward 2
			chance = Misc.getRandom(20);
			if (chance <= 6 && !stages[1])  {
				reward[0] = COMMON_LOOT[Misc.getRandom(COMMON_LOOT.length - 1)];
				if (reward[1] == 558) {
					rewardN[1] = Misc.getRandom(1795);
				} else if (reward[1] == 560) {
					rewardN[1] = Misc.getRandom(391);
				} else if (reward[1] == 562) {
					rewardN[1] = Misc.getRandom(773);
				} else if (reward[1] == 565) {
					rewardN[1] = Misc.getRandom(164);
				} else if (reward[1] == 4740) {
					rewardN[1] = Misc.getRandom(188);
				} else if (reward[1] == 995) {
					rewardN[1] = Misc.getRandom(4162);
				}
				stages[1] = true;
			} else if (6 < chance && chance <= 12  && !stages[1]) {
				reward[1] = UNCOMMON_LOOT[Misc.getRandom(UNCOMMON_LOOT.length - 1)];
				if (reward[1] == 6969) {
					rewardN[1] = 4;
				} else {
					rewardN[1] = 1;
				}
				stages[1] = true;
			} else if (12 < chance && chance <= 18 && !stages[1]) {
				reward[1] = RARE_LOOT[Misc.getRandom(RARE_LOOT.length - 1)];
				rewardN[1] = 1;
				stages[1] = true;
			} else if (18 < chance && chance <= 20 && !stages[1]) {
				reward[1] = VERYRARE_LOOT[Misc.getRandom(VERYRARE_LOOT.length - 1)];
				rewardN[1] = 1;
				stages[1] = true;
			}
			
			if (stages[0] && stages[1]) {
				addItems(reward[0], rewardN[0], reward[1], rewardN[1]);
			}
	}
}