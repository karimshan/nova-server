package org.nova.game.player.content.minigames;

import java.util.ArrayList;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.map.RegionBuilder;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;


public class ClanWars {

	private static int kills;

	private int portalIndex;

	private long fightTime;

	private boolean[][] rules;

	private boolean allowedEnter, whiteTeam;

	private Player player, other;

	/**
	 * Holds the team's members.
	 */
	private ArrayList<Player> team;

	/**
	 * Arranges and separates the teleports of each team via north, south, in
	 * the following order Platue, Turrets, Quarry, Forest & Classic.
	 */
	public static Location[][] ARENA_TELEPORTS = {
			{ new Location(2883, 5941, 0), new Location(2883, 5900, 0) },
			{ new Location(2815, 5511, 0), new Location(2891, 5515, 0) },
			{ new Location(2993, 5558, 0), new Location(2891, 5515, 0) },
			{ new Location(2930, 5668, 0), new Location(2892, 5641, 0) },
			{ new Location(2883, 5941, 0), new Location(2883, 5900, 0) } };

	// x, x, y
	/**
	 * Arranges the center coordinate of the arena, the corrdinates are arranged
	 * in the following order. X, X, Y.
	 */
	public static int[] ARENA_CENTER = { 2882, 2911, 5663 };

	/**
	 * Arranges and separates the teleports of each team via north, south, in
	 * the following order Platue, Turrets, Quarry, Forest, Classic.
	 */
	public static Location[][] DEATH_TELEPORTS = {
			{ new Location(2851, 5931, 0), new Location(2851, 5909, 0) },
			{ new Location(2815, 5511, 0), new Location(2891, 5515, 0) }, // done
			{ new Location(2908, 5686, 0), new Location(2915, 5537, 0) },
			{ new Location(2929, 5667, 0), new Location(2892, 5661, 0) },
			{ new Location(2883, 5941, 0), new Location(2883, 5900, 0) } };

	public static int[] KILLS_REQUIRED = { 25, 50, 100, 200, 400, 750, 1000,
			2000, 5000, 10000 };
	// seconds
	public static double[] FIGHT_TIME = { 0.05, 0.10, 0.30, 2.00, 2.30, 3.00,
			4.00, 5.00, 6.00, 8.00, 8.00 };

	public static Location CLANWARS_LOBBY = new Location(1, 1, 0);// TODO

	public ClanWars(Player player, Player other) {
		this.player = player;
		this.other = other;
		kills = 0;
		rules = new boolean[12][10];
		team = new ArrayList<Player>();
		rules[0][0] = true;
		ClanChallengeInterface.openInterface(player);
		ClanChallengeInterface.openInterface(other);
	}

	private void swapRule(Player target, int rule, int index) {
		boolean current = rules[rule][index];
		if (current)
			current = false;
		else
			current = true;
		if (player != target)
			other.getClanWars().swapRule(target, rule, index);
	}

	protected long calculateFightingTime() {
		for (int index = 60; index < 93; index += 3) {// time
			int calculatedHash = (60 - index) / 3;
			if (calculatedHash < 0)
				calculatedHash = 0;
			if (getRules(2, calculatedHash))
				return (long) FIGHT_TIME[calculatedHash];
		}
		return 0;
	}

	public long getRequiredKills() {
		return KILLS_REQUIRED[getKillsHash()];
	}

	public int getKillsHash() {
		for (int i = 28; i < 52; i += 3) {// Victory
			int calculatedHash = (31 - i) / 3;
			if (calculatedHash < 0)
				calculatedHash = 0;
			if (getRules(0, calculatedHash))
				return calculatedHash;
		}
		return 0;
	}

	public boolean getRules(int rule, int index) {
		return rules[rule][index];
	}

	public ArrayList<Player> getTeam() {
		return team;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void incrementKills(Player player, Player other) {
		this.kills++;
		ClanChallengeInterface.refreshClanInterface(player);
		if (!player.getClanWars().getRules(0, 0)) {
			if (player.getClanWars().getRules(0, 0) ? other.getClanWars()
					.getTeam().size() == 5 : other.getClanWars().getTeam()
					.size() == 0)
				ClanChallengeInterface.sendWinningClan(player);
		} else if (player.getClanWars().getKills() == player.getClanWars()
				.getRequiredKills())
			ClanChallengeInterface.sendWinningClan(player);

	}

	public void decrementKills() {
		this.kills--;
	}

	public long getFightTime() {
		return fightTime;
	}

	public void setFightTime(int fightTime) {
		this.fightTime = fightTime;
	}

	public void addFightTime(long fightTime) {
		this.fightTime = fightTime + Misc.currentTimeMillis();
	}

	public boolean isAllowedEnter() {
		return allowedEnter;
	}

	public void setAllowedEnter(boolean allowedEnter) {
		this.allowedEnter = allowedEnter;
	}

	public boolean isWhiteTeam() {
		return whiteTeam;
	}

	public void setWhiteTeam(boolean whiteTeam) {
		this.whiteTeam = whiteTeam;
	}

	public int getPortalIndex() {
		return portalIndex;
	}

	public void setPortalIndex(int portalIndex) {
		System.out.println(portalIndex);
		if (portalIndex < 0)
			portalIndex = 0;
		this.portalIndex = portalIndex;
	}

	public Location getTeleportedArea() {
		return ARENA_TELEPORTS[portalIndex][whiteTeam ? 0 : 1];
	}

	public Player getOther() {
		return other;
	}

	public static class ClanChallengeInterface {

		/**
		 * Opens the challenge interface.
		 * 
		 * @param player
		 *            - The player that is challenging TODO add support for two
		 *            players
		 */
		public static void openInterface(Player player) {
			if (player.getCurrentFriendChatOwner() == null) {
				player.packets().sendMessage(
						"You need to be in a friend's chat to start a war.");
				return;
			}
			player.interfaces().sendInterface(791);
			player.packets().sendUnlockIComponentOptionSlots(791, 141, 0,
					63, 0);
		}

		/**
		 * Start the battle based off regulated rules.
		 * 
		 * @param player
		 *            The white team's challenger.
		 * @param other
		 *            The black team's challengee.
		 */
		public static void beginBattle(final Player player, Player other) {
			player.getTemporaryAttributtes().put("hasChallenged", true);
			if (other.getTemporaryAttributtes().get("hasChallenged") == null)
				return;
			player.getCurrentFriendChat().sendMessage(
					player,
					"The battle will begin in two minutes between "
							+ player.getCurrentFriendChat().getChannelName()
							+ " and "
							+ other.getCurrentFriendChat().getChannelName()
							+ ".");
			other.getCurrentFriendChat().sendMessage(
					player,
					"The battle will begin in two minutes between "
							+ other.getCurrentFriendChat().getChannelName()
							+ " and "
							+ player.getCurrentFriendChat().getChannelName()
							+ ".");
			player.stopAll();
			for (Player teamPlayer : player.getCurrentFriendChat().getPlayers())
				teamPlayer.getClanWars().setAllowedEnter(true);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (!player.getClanWars().getRules(0, 0))
						player.getClanWars().setAllowedEnter(false);
					player.getClanWars().addFightTime(
							player.getClanWars().calculateFightingTime());
					createDynamicArea(player);
					enterPortal(player, player);
				}
			});
		}

		protected static void createDynamicArea(Player player) {
			RegionBuilder.createDynamicRegion(player.getClanWars()
					.getTeleportedArea().getRegionId());
		}

		public static void enterPortal(final Player player, Player leader) {// TODO
			sendClanInterface(player);
			refreshClanInterface(player);
			for (Player team : leader.getClanWars().getTeam()) {
				if (team != null
						&& !leader.getClanWars().getTeam().contains(player))
					team.getClanWars().getTeam().add(player);
			}
			player.loadMapRegions();
			player.setLocation(leader.getClanWars().getTeleportedArea());
		}

		public static boolean processButtonClick(Player player, Player other,
				int interfaceId, int componentId, int slotId, int packetId) {
			if (interfaceId == 791) {
				for (int i = 25; i < 52; i += 3) {// Victory
					if (componentId == i)
						player.getClanWars().swapRule(other, 0, (28 - i) / 3);
				}
				for (int i = 60; i < 93; i += 3) {
					// time
					if (componentId == i)
						player.getClanWars().swapRule(other, 2, (60 - i) / 3);
				}
				for (int i = 120; i < 136; i += 2) {
					if (i != 122) {
						// rulesThreeSwap();
						continue;
					}
					if (componentId == i)
						player.getClanWars().swapRule(other, (i / 2) - 4, 0);
				}
				if (componentId == 141) {
					for (int i = 3; i < 19; i += 4) {
						if (i == slotId)
							player.getClanWars().setPortalIndex((i - 3) / 4);// need
																				// to
																				// save
																				// idx
																				// for
																				// death
					}
				}
				if (componentId == 116)// items on death
					player.getClanWars().swapRule(other, 3, 0);
				else if (componentId == 143)// accept
					beginBattle(player, other);
				return true;
			}
			return false;
		}

		public static void sendWinningClan(Player leader) {// TODO red code tags
			for (Player player : leader.getClanWars().getTeam()) {
				player.packets().sendMessage("Your clan is victorious!");
			}
			Player other = leader.getClanWars().getOther();// used for loser
															// interface,
			for (Player player : other.getClanWars().getTeam()) {
				player.packets().sendMessage("Your clan has lost!");
			}
		}

		public static void sendClanInterface(Player player) {
			for (Player teamPlayer : player.getClanWars().getTeam()) {
				teamPlayer.packets().sendConfigByFile(5280,
						player.getClanWars().getKillsHash());
			}
		}

		public static void refreshClanInterface(Player player) {
			/*
			 * file: 5280, 3, 0 - Kills, index easy :p file: 5281, 7, 4 file:
			 * 5282, 8, 8 file: 5283, 9, 9 file: 5284, 10, 10 file: 5285, 11, 11
			 * file: 5286, 13, 12 file: 5287, 14, 14 file: 5288, 15, 15 file:
			 * 5289, 16, 16 file: 5290, 17, 17 file: 5291, 17, 0 file: 5292, 30,
			 * 26 file: 5293, 31, 31
			 */
		}

		public static void sendDrawInterface(Player leader) {
			for (Player player : leader.getClanWars().getTeam()) {
				player.packets().sendMessage(
						"The war has ended in a draw!");
			}
			Player other = leader.getClanWars().getOther();// used for loser
															// interface,
			for (Player player : other.getClanWars().getTeam()) {
				player.packets().sendMessage(
						"The war has ended in a draw!");
			}
		}
	}
}