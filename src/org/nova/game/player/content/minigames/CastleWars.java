package org.nova.game.player.content.minigames;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.others.CastleWarBarricade;
import org.nova.game.player.Equipment;
import org.nova.game.player.Player;
import org.nova.game.player.controlers.CastleWarsPlaying;
import org.nova.game.player.dialogues.CastleWarsScoreboard;


public final class CastleWars {
	public static final int CW_TICKET = 4067;
	public static final int SARADOMIN = 0;
	public static final int ZAMORAK = 1;
	private static final int GUTHIX = 2;
	@SuppressWarnings("unchecked")
	private static final List<Player>[] waiting = new List[2];
	@SuppressWarnings("unchecked")
	private static final List<Player>[] playing = new List[2];
	private static int[] seasonWins = new int[2];
	public static final Location LOBBY = new Location(2442, 3090, 0),
			SARA_WAITING = new Location(2381, 9489, 0),
			ZAMO_WAITING = new Location(2421, 9523, 0),
			SARA_BASE = new Location(2426, 3076, 1),
			ZAMO_BASE = new Location(2373, 3131, 1);

	private static PlayingGame playingGame;

	static {
		init();
	}

	public static void init() {
		for (int i = 0; i < waiting.length; i++)
			waiting[i] = Collections.synchronizedList(new LinkedList<Player>());
		for (int i = 0; i < playing.length; i++)
			playing[i] = Collections.synchronizedList(new LinkedList<Player>());
	}

	public static void viewScoreBoard(Player player) {
		player.interfaces().sendChatBoxInterface(55);
		player.packets().sendIComponentText(55, 1,
				"Saradomin: " + seasonWins[SARADOMIN]);
		player.packets().sendIComponentText(55, 2,
				"Zamorak: " + seasonWins[ZAMORAK]);
	}

	public static int getPowerfullestTeam() {
		int zamorak = waiting[ZAMORAK].size() + playing[ZAMORAK].size();
		int saradomin = waiting[SARADOMIN].size() + playing[SARADOMIN].size();
		if (saradomin == zamorak)
			return GUTHIX;
		if (zamorak > saradomin)
			return ZAMORAK;
		return SARADOMIN;
	}

	public static void joinPortal(Player player, int team) {
		if (player.getEquipment().getHatId() != -1
				|| player.getEquipment().getCapeId() != -1) {
			player.packets().sendMessage(
					"You cannot wear hats, capes or helms in the arena.");
			return;
		}
		int powerfullestTeam = getPowerfullestTeam();
		if (team == GUTHIX) {
			team = powerfullestTeam == ZAMORAK ? SARADOMIN : ZAMORAK;
		} else if (team == powerfullestTeam) {
			if (team == ZAMORAK)
				player.packets()
						.sendMessage(
								"The Zamorak team is powerful enough already! Guthix demands balance - join the Saradomin team instead!");
			else if (team == SARADOMIN)
				player.packets()
						.sendMessage(
								"The Saradomin team is powerful enough already! Guthix demands balance - join the Zamorak team instead!");
			return;
		}
		player.addStopDelay(2);
		waiting[team].add(player);
		setCape(player, new Item(team == ZAMORAK ? 4042 : 4041));
		player.getControllerManager().startController("CastleWarsWaiting", team);
		player.setLocation(new Location(team == ZAMORAK ? ZAMO_WAITING
				: SARA_WAITING, 1));
		player.getMusicsManager().playMusic(318); // temp testing else 5
		if (playingGame == null && waiting[team].size() >= 5) // at least
																// 9players to
																// start
			createPlayingGame();
		else
			refreshTimeLeft(player);
		// You cannot take non-combat items into the arena
	}

	public static void setCape(Player player, Item cape) {
		player.getEquipment().getItems().set(Equipment.SLOT_CAPE, cape);
		player.getEquipment().refresh(Equipment.SLOT_CAPE);
		player.getAppearance().generateAppearanceData();
	}

	public static void setWeapon(Player player, Item weapon) {
		player.getEquipment().getItems().set(Equipment.SLOT_WEAPON, weapon);
		player.getEquipment().refresh(Equipment.SLOT_WEAPON);
		player.getAppearance().generateAppearanceData();
	}

	public static void createPlayingGame() {
		playingGame = new PlayingGame();
		CoresManager.fastExecutor
				.scheduleAtFixedRate(playingGame, 60000, 60000);
		refreshAllPlayersTime();
	}

	public static void destroyPlayingGame() {
		playingGame.cancel();
		playingGame = null;
		refreshAllPlayersTime();
		leavePlayersSafely();
	}

	public static void leavePlayersSafely() {
		leavePlayersSafely(-1);
	}

	public static void leavePlayersSafely(final int winner) {
		for (int i = 0; i < playing.length; i++) {
			for (final Player player : playing[i]) {
				player.addStopDelay(7);
				player.stopAll();
			}
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				for (int i = 0; i < playing.length; i++)
					for (Player player : playing[i]
							.toArray(new Player[playing[i].size()])) {
						forceRemovePlayingPlayer(player);
						if (winner != -1) {
							if (winner == -2) {
								player.packets()
										.sendMessage("You draw.");
								player.getInventory().addItem(CW_TICKET, 1);
							} else if (winner == i) {
								player.packets().sendMessage("You won.");
								player.getInventory().addItem(CW_TICKET, 2);
							} else
								player.packets()
										.sendMessage("You lost.");
						}
					}
			}
		}, 6);
	}

	// unused
	public static void forceRemoveWaitingPlayer(Player player) {
		player.getControllerManager().forceStop();
	}

	public static void removeWaitingPlayer(Player player, int team) {
		waiting[team].remove(player);
		setCape(player, null);
		player.setLocation(new Location(LOBBY, 2));
		if (playingGame != null && waiting[team].size() == 0
				&& playing[team].size() == 0)
			destroyPlayingGame(); // cancels if 0 players playing/waiting on any
									// of the tea
	}

	public static void refreshTimeLeft(Player player) {
		player.packets()
				.sendConfig(
						380,
						playingGame == null ? 0
								: playingGame.minutesLeft
										- (player.getControllerManager()
												.getControler() instanceof CastleWarsPlaying ? 5
												: 0));
	}

	public static void startGame() {
		for (int i = 0; i < waiting.length; i++) {
			for (Player player : waiting[i].toArray(new Player[waiting[i]
					.size()])) {
				joinPlayingGame(player, i);
			}
		}
	}

	public static void forceRemovePlayingPlayer(Player player) {
		player.getControllerManager().forceStop();
	}

	public static void removePlayingPlayer(Player player, int team) {
		playing[team].remove(player);
		player.reset();
		player.setCanPvp(false);

		// remove the items
		setCape(player, null);
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == 4037 || weaponId == 4039) {
			CastleWars.setWeapon(player, null);
			CastleWars.dropFlag(player.getLastLocation(),
					weaponId == 4037 ? CastleWars.SARADOMIN
							: CastleWars.ZAMORAK);
		}
		player.getInventory().deleteItem(4049, Integer.MAX_VALUE); // bandages
		player.getInventory().deleteItem(4053, Integer.MAX_VALUE); // barricades

		player.hints().removeUnsavedHintIcon();
		player.getMusicsManager().reset();
		player.setLocation(new Location(LOBBY, 2));
		if (playingGame != null && waiting[team].size() == 0
				&& playing[team].size() == 0)
			destroyPlayingGame(); // cancels if 0 players playing/waiting on any
									// of the tea
	}

	public static void joinPlayingGame(Player player, int team) {
		playingGame.refresh(player);
		waiting[team].remove(player);
		player.getControllerManager().removeControllerWithoutCheck();
		playing[team].add(player);
		player.setCanPvp(true);
		player.getControllerManager().startController("CastleWarsPlaying", team);
		player.setLocation(new Location(team == ZAMORAK ? ZAMO_BASE
				: SARA_BASE, 1));
	}

	public static void endGame(int winner) {
		if (winner != -2)
			seasonWins[winner]++;
		leavePlayersSafely(winner);
	}

	public static void refreshAllPlayersTime() {
		for (int i = 0; i < waiting.length; i++)
			for (Player player : waiting[i])
				refreshTimeLeft(player);
		for (int i = 0; i < playing.length; i++)
			for (Player player : playing[i]) {
				player.getMusicsManager().playMusic(i == ZAMORAK ? 845 : 314);
				refreshTimeLeft(player);
			}
	}

	public static void refreshAllPlayersPlaying() {
		for (int i = 0; i < playing.length; i++)
			for (Player player : playing[i])
				playingGame.refresh(player);
	}

	public static void addHintIcon(int team, Player target) {
		for (Player player : playing[team]) {
			player.hints().addHintIcon(target, 0, false, false);
		}
	}

	public static void removeHintIcon(int team) {
		for (Player player : playing[team]) {
			player.hints().removeUnsavedHintIcon();
		}
	}

	public static void addScore(Player player, int team, int flagTeam) {
		if (playingGame == null)
			return;
		playingGame.addScore(player, team, flagTeam);
	}

	public static void takeFlag(Player player, int team, int flagTeam,
			GlobalObject object, boolean droped) {
		if (playingGame == null)
			return;
		playingGame.takeFlag(player, team, flagTeam, object, droped);
	}

	public static void dropFlag(Location tile, int flagTeam) {
		if (playingGame == null)
			return;
		playingGame.dropFlag(tile, flagTeam);
	}

	public static void removeBarricade(int team, CastleWarBarricade npc) {
		if (playingGame == null)
			return;
		playingGame.removeBarricade(team, npc);
	}

	public static void addBarricade(int team, Player player) {
		if (playingGame == null)
			return;
		playingGame.addBarricade(team, player);
	}

	public static boolean isBarricadeAt(Location tile) {
		if (playingGame == null)
			return false;
		return playingGame.isBarricadeAt(tile);
	}

	private static class PlayingGame extends TimerTask {

		private static final int SAFE = 0, TAKEN = 1, DROPPED = 2;
		private int minutesLeft;
		private int[] score;
		private int[] flagStatus;
		private int[] barricadesCount;
		private final LinkedList<GlobalObject> spawnedObjects = new LinkedList<GlobalObject>();
		private final LinkedList<CastleWarBarricade> barricades = new LinkedList<CastleWarBarricade>();

		public PlayingGame() {
			reset();
		}

		public void reset() {
			minutesLeft = 5; // temp testing else 5
			score = new int[2];
			flagStatus = new int[2];
			barricadesCount = new int[2];
			for (GlobalObject object : spawnedObjects)
				Game.destroySpawnedObject(object, object.getId() == 4377
						|| object.getId() == 4378 ? false : true);
			spawnedObjects.clear();
			for (CastleWarBarricade npc : barricades)
				npc.finish();
			barricades.clear();
		}

		public boolean isBarricadeAt(Location tile) {
			for (Iterator<CastleWarBarricade> it = barricades.iterator(); it
					.hasNext();) {
				CastleWarBarricade npc = it.next();
				if (npc.isDead() || npc.hasFinished()) {
					it.remove();
					continue;
				}
				if (npc.getX() == tile.getX() && npc.getY() == tile.getY()
						&& tile.getZ() == tile.getZ())
					return true;
			}
			return false;
		}

		public void addBarricade(int team, Player player) {
			if (barricadesCount[team] >= 10) {
				player.packets()
						.sendMessage(
								"Each team in the activity can have a maximum of 10 barricades set up.");
				return;
			}
			player.getInventory().deleteItem(new Item(4053, 1));
			barricadesCount[team]++;
			barricades.add(new CastleWarBarricade(team, new Location(player)));
		}

		public void removeBarricade(int team, CastleWarBarricade npc) {
			barricadesCount[team]--;
			barricades.remove(npc);
		}

		public void takeFlag(Player player, int team, int flagTeam,
				GlobalObject object, boolean droped) {
			if (!droped && team == flagTeam)
				return;
			if (droped && flagStatus[flagTeam] != DROPPED)
				return;
			else if (!droped && flagStatus[flagTeam] != SAFE)
				return;

			if (flagTeam != team
					&& (player.getEquipment().getWeaponId() != -1 || player
							.getEquipment().getShieldId() != -1)) {
				// TODO no space message
				player.packets()
						.sendMessage(
								"You can't take flag while wearing something in your hands.");
				return;
			}
			if (!droped) {
				GlobalObject flagStand = new GlobalObject(
						flagTeam == SARADOMIN ? 4377 : 4378, object.getType(),
						object.getRotation(), object.getX(), object.getY(),
						object.getZ());
				spawnedObjects.add(flagStand);
				Game.spawnObject(flagStand, false);
			} else {
				spawnedObjects.remove(object);
				Game.destroySpawnedObject(object, true);
				if (flagTeam == team) {
					makeSafe(flagTeam);
					return;
				}
			}
			addHintIcon(flagTeam, player);
			flagStatus[flagTeam] = TAKEN;
			setWeapon(player, new Item(flagTeam == SARADOMIN ? 4037 : 4039, 1));
			refreshAllPlayersPlaying();
		}

		public void addScore(Player player, int team, int flagTeam) {
			setWeapon(player, null);
			score[team] += 1;
			makeSafe(flagTeam);
		}

		private void makeSafe(int flagTeam) {
			GlobalObject flagStand = null;
			for (GlobalObject object : spawnedObjects) {
				if (object.getId() == (flagTeam == SARADOMIN ? 4377 : 4378)) {
					flagStand = object;
					break;
				}
			}
			if (flagStand == null)
				return;
			Game.destroySpawnedObject(flagStand, false);
			flagStatus[flagTeam] = SAFE;
			refreshAllPlayersPlaying();
		}

		public void dropFlag(Location tile, int flagTeam) {
			removeHintIcon(flagTeam);
			GlobalObject flagDroped = new GlobalObject(
					flagTeam == SARADOMIN ? 4900 : 4901, 10, 0, tile.getX(),
					tile.getY(), tile.getZ());
			spawnedObjects.add(flagDroped);
			Game.spawnObject(flagDroped, true);
			flagStatus[flagTeam] = DROPPED;
			refreshAllPlayersPlaying();
		}

		public void refresh(Player player) {
			player.packets().sendConfigByFile(143, flagStatus[SARADOMIN]);
			player.packets().sendConfigByFile(145, score[SARADOMIN]);
			player.packets().sendConfigByFile(153, flagStatus[ZAMORAK]);
			player.packets().sendConfigByFile(155, score[ZAMORAK]);
		}

		@Override
		public void run() {
			minutesLeft--;
			if (minutesLeft == 5) {
				endGame(score[SARADOMIN] == score[ZAMORAK] ? -2
						: score[SARADOMIN] > score[ZAMORAK] ? SARADOMIN
								: ZAMORAK);
				reset();
			} else if (minutesLeft == 0) {
				minutesLeft = 25;
				startGame();
			} else if (minutesLeft > 6) // adds ppl waiting on lobby
				startGame();
			refreshAllPlayersTime();
		}
	}

	public static void handleInterfaces(Player player, int interfaceId,
			int componentId) {
		if (interfaceId == 55) {
			if (componentId == 9)
				player.closeInterfaces();
		}
	}

	public static boolean handleObjects(Player player, int objectId) {
		if (objectId == 4484) { // scoreboard
			player.getMatrixDialogues().startDialogue(
					new CastleWarsScoreboard());
			return true;
		}
		if (objectId == 4388) {
			joinPortal(player, ZAMORAK);
			return true;
		}
		if (objectId == 4408) {
			joinPortal(player, GUTHIX);
			return true;
		}
		if (objectId == 4387) {
			joinPortal(player, SARADOMIN);
			return true;
		}
		return false;
	}

	public static List<Player>[] getPlaying() {
		return playing;
	}
}
