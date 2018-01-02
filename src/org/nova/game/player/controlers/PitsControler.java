package org.nova.game.player.controlers;

import java.util.LinkedList;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;


/**
 * Controlls the fightpits minigames.
 * 
 * @author Dj Khaled :troll:
 * 
 */
public class PitsControler extends Controller {

	private LinkedList<Player> playing = new LinkedList<Player>();

	private LinkedList<Player> lobby = new LinkedList<Player>();

	private Location[] GAME_TELEPORTS = {
			new Location(2384 + Misc.random(28), 5133 + Misc.random(3), 0),
			new Location(2410 + Misc.random(3), 5140 + Misc.random(17), 0),
			new Location(2392 + Misc.random(10), 5141 + Misc.random(25), 0),
			new Location(2383 + Misc.random(2), 5141 + Misc.random(14), 0),
			new Location(2392 + Misc.random(11), 5145 + Misc.random(19), 0) };

	private Location[] ORB_TELEPORTS = { new Location(2399, 5171, 0),
			new Location(2398, 5150, 0), new Location(2384, 5157, 0),
			new Location(2409, 5158, 0), new Location(2411, 5137, 0),
			new Location(2388, 5138, 0) };

	private Location LOBBY_TELEPORT = new Location(2395 + Misc.random(8),
			5170 + Misc.random(3), 0);

	private int reward;
	private String champion;

	private boolean checkAll() {
		if (!player.isRunning())
			return false;
		if (lobby.contains(player)) {
			lobby.remove(player);
			return false;
		} else if (playing.contains(player)) {
			playing.remove(player);
			return false;
		}
		player.setInfiniteStopDelay();
		sendInterfaces();
		return true;
	}

	@Override
	public void start() {
		if (!checkAll()) {
			return;
		}
	}

	@Override
	public void sendInterfaces() {
		updatePlayerConfigs(player);
		player.interfaces()
				.sendTab(
						player.interfaces().isFullScreen() ? 11
								: 8, 373);
	}

	private void updatePlayerConfigs(Player player) {
		if (playing.size() >= 2) {
			player.packets().sendConfig(560, playing.size());
		} else {
			player.packets().sendIComponentText(373, 0,
					"Current Champion: JaLYt-Ket-" + champion);
			player.packets().sendConfig(560, -1);
		}
	}

	@Override
	public boolean processMagicTeleport(Location toTile) {
		if (!isInFightPits(player))
			removeControler();
		else
			player.getMatrixDialogues().startDialogue("SimpleMessage",
					"A magical force prevents you from moving...");
		return true;
	}

	@Override
	public boolean processItemTeleport(Location toTile) {
		if (!isInFightPits(player))
			removeControler();
		else
			player.getMatrixDialogues().startDialogue("SimpleMessage",
					"A magical force prevents you from teleporting...");
		return true;
	}

	public void startFight(final Player player) {
		playing.add(player);// Maybe this
		lobby.remove(player);
		if (player.getTemporaryAttributtes().get("viewingOrb") != null
				&& (Boolean) player.getTemporaryAttributtes().get("viewingOrb")) {
			resetOrb();
		}
		player.reset();
		player.stopAll();
		player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 2618,
				"Please wait for my signal before fighting.");
		for (Player players : playing) {
			players.setLocation(new Location(GAME_TELEPORTS[Misc
					.getRandom(GAME_TELEPORTS.length)]));
		}
		WorldTasksManager.schedule(new WorldTask() {
			int count = 3;

			@Override
			public void run() {
				if (count == 0) {
					player.getMatrixDialogues().startDialogue(
							"SimpleNPCMessage", 2618, "You may begin.");
					player.setCanPvp(true);
					player.resetStopDelay();
					reward *= (playing.size() * 2.40);
					this.stop();
				}
				count--;
			}
		}, 0, 2);
	}

	private void sendOrb() {
		player.getAppearance().switchHidden();
		player.packets().sendBlackOut(5);
		player.getTemporaryAttributtes().put("viewingOrb", true);
		player.setInfiniteStopDelay();
	}

	private void resetOrb() {
		player.getAppearance().switchHidden();
		player.packets().sendBlackOut(0);
		player.getTemporaryAttributtes().put("viewingOrb", false);
		player.resetStopDelay();
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (interfaceId == 374) {
			switch (componentId) {
			case 5:
				resetOrb();
				player.setLocation(new Location(LOBBY_TELEPORT));
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				for (int i = 0; i < 4; i++) {
					if (i + 11 == componentId) {
						sendOrb();
						player.setLocation(ORB_TELEPORTS[i]);
					}
				}
				break;
			}
		}
		return true;
	}

	@Override
	public boolean processObjectClick1(GlobalObject object) {
		if (object.getId() == 6917)
			return true;
		return false;
	}

	private void remainingPlayersCheck(Player player) {
		if (player == null)
			return;
		if (playing.size() == 1) {
			playing.clear();
			lobby.add(player);
			player.setCanPvp(false);
			champion = player.getDisplayName();
			player.getInventory().addItem(new Item(6529, reward));
			player.setLocation(LOBBY_TELEPORT);
			player.packets().sendMessage(
					"Congratulations! You have received approximatly " + reward
							+ " tokkul.", true);
		}
	}

	@Override
	public boolean sendDeath() {
		player.addStopDelay(7);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.stopAll();
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.packets().sendMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(-1));
					player.reset();
					player.setCanPvp(false);
					player.setLocation(LOBBY_TELEPORT);
					playing.remove(player);
					lobby.add(player);
					for (Player player : lobby)
						updatePlayerConfigs(player);
					for (Player player : playing)
						updatePlayerConfigs(player);
					remainingPlayersCheck(playing.get(0));
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean login() {
		player.setLocation(new Location(LOBBY_TELEPORT));
		return false; // so doesnt remove script
	}

	@Override
	public boolean logout() {
		if (lobby.contains(player)) {
			lobby.remove(player);
			return false;
		} else if (playing.contains(player)) {
			playing.remove(player);
			return false;
		}
		return false; // so doesnt remove script
	}

	public static boolean isInFightPits(Player player) {
		return player.getX() >= 2374 && player.getY() >= 5129
				&& player.getX() <= 2424 && player.getY() <= 5168;
	}
}
