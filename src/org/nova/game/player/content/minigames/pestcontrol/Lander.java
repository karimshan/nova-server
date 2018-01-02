package org.nova.game.player.content.minigames.pestcontrol;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.nova.Constants;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.game.player.content.minigames.pestcontrol.PestControl.PestData;

public class Lander {

    public static Lander[] landers = new Lander[3];
    private static final int AUTO_GAME = Constants.DEVELOPER_MODE ? 2 : 15;
    private static final int TIME = 150;

    private List<Player> lobby = Collections.synchronizedList(new LinkedList<Player>());
    private LobbyTimer timer;
    private LanderRequirement landerRequirement;

    public Lander(LanderRequirement landerRequirement) {
	this.landerRequirement = landerRequirement;
    }

    public class LobbyTimer extends TimerTask {

	private int seconds = TIME;

	@Override
	public void run() {
	    if (seconds == 0 && lobby.size() >= 5 || lobby.size() >= AUTO_GAME)
		passPlayersToGame();
	    else if (seconds == 0)
		seconds = TIME;
	    else if (lobby.size() == 0) {
		cancel();
		return;
	    }
	    seconds--;
	    if (seconds % 30 == 0)
		refreshLanderInterface();
	}

	public int getMinutes() {
	    return seconds / 60;
	}
    }

    private void passPlayersToGame() {
	final List<Player> playerList = new LinkedList<Player>();
	playerList.addAll(Collections.synchronizedList(lobby));
	lobby.clear();
	if (playerList.size() > AUTO_GAME) {
	    for (int index = AUTO_GAME; index < playerList.size(); index++) {
		Player player = playerList.get(index);
		if (player == null) {
		    playerList.remove(index);
		    continue;
		}
		player.packets().sendMessage("You have received priority over other players.");
		playerList.remove(index);
		lobby.add(player);
	    }
	}
	new PestControl(playerList, PestData.valueOf(landerRequirement.name())).create();
    }

    public void enterLander(Player player) {
	if (lobby.size() == 0)
	    CoresManager.fastExecutor.schedule(timer = new LobbyTimer(), 1000, 1000);
	player.getControllerManager().startController("PestControlLobby", landerRequirement.getId());
	add(player);
	player.useStairs(-1, landerRequirement.getLocation(), 1, 2, "You board the lander.");
    }

    public void exitLander(Player player) {
	player.useStairs(-1, landerRequirement.getExitTile(), 1, 2, "You leave the lander.");
	remove(player);
    }

    public void add(Player player) {
	lobby.add(player);
	refreshLanderInterface();
    }

    private void refreshLanderInterface() {
	for (Player teamPlayer : lobby)
	    teamPlayer.getControllerManager().getControler().sendInterfaces();
    }

    public void remove(Player player) {
	lobby.remove(player);
	refreshLanderInterface();
    }

    public List<Player> getPlayers() {
	return lobby;
    }

    public static enum LanderRequirement {

	NOVICE(0, 40, new Location(2661, 2639, 0), new Location(2657, 2639, 0)),

	INTERMEDIATE(1, 70, new Location(2641, 2644, 0), new Location(2644, 2644, 0)),

	VETERAN(2, 100, new Location(2635, 2653, 0), new Location(2638, 2653, 0));

	private static Map<Integer, LanderRequirement> landers = new HashMap<Integer, LanderRequirement>();

	public static LanderRequirement forId(int id) {
	    return landers.get(id);
	}

	static {
	    for (LanderRequirement lander : LanderRequirement.values())
		landers.put(lander.getId(), lander);
	}

	int id, requirement, reward;
	int[] pests;
	Location tile, exit;

	private LanderRequirement(int id, int requirement, Location tile, Location exit) {
	    this.id = id;
	    this.requirement = requirement;
	    this.tile = tile;
	    this.exit = exit;
	}

	public int getId() {
	    return id;
	}

	public int getRequirement() {
	    return requirement;
	}

	public Location getLocation() {
	    return tile;
	}

	public Location getExitTile() {
	    return exit;
	}
    }

    public static Lander[] getLanders() {
	return landers;
    }

    public LanderRequirement getLanderRequierment() {
	return landerRequirement;
    }

    static {
	for (int i = 0; i < landers.length; i++)
	    landers[i] = new Lander(LanderRequirement.forId(i));
    }

    @Override
    public String toString() {
	return landerRequirement.name().toLowerCase();
    }

    public static boolean canEnter(Player player, int landerIndex) {
	Lander lander = landers[landerIndex];
	if (player.getSkills().getCombatLevelWithSummoning() < lander.getLanderRequierment().requirement) {
	    player.getMatrixDialogues().startDialogue("SimpleMessage", "You need a combat level of " + lander.getLanderRequierment().getRequirement() + " or more to enter in boat.");
	    return false;
	} else if (player.getRealPet() != null || player.getFamiliar() != null) {
	    player.packets().sendMessage("You can't take a follower into the lander, there isn't enough room!");
	    return false;
	}
	lander.enterLander(player);
	return true;
    }

    public LobbyTimer getTimer() {
	return timer;
    }
}
