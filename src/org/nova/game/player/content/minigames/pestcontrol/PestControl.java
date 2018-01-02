package org.nova.game.player.content.minigames.pestcontrol;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.map.MapBuilder;
import org.nova.game.npc.NPC;
import org.nova.game.npc.others.PestMonsters;
import org.nova.game.npc.pest.PestPortal;
import org.nova.game.npc.pest.Shifter;
import org.nova.game.npc.pest.Spinner;
import org.nova.game.npc.pest.Splatter;
import org.nova.game.player.Player;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;

public class PestControl {

    private final static int[][] PORTAL_LOCATIONS = { { 4, 56, 45, 21, 32 }, { 31, 28, 10, 9, 32 } };
    private final static int[] KNIGHT_IDS = { 3782, 3784, 3785 };

    private int[] boundChunks;
    private int[] pestCounts = new int[5];

    private List<Player> team;
    private List<NPC> brawlers = new LinkedList<NPC>();
    private PestPortal[] portals = new PestPortal[4];
    
    private PestPortal knight;
    private PestData data;

    private byte portalCount = 5;

    private class PestGameTimer extends TimerTask {

	int seconds = 1200;

	@Override
	public void run() {
	    try {
		updateTime(seconds / 60);
		if (seconds == 0 || canFinish()) {
		    endGame();
		    cancel();
		    System.out.println("Finished at: " + seconds);
		    return;
		}
		if (seconds % 10 == 0)
		    sendPortalInterfaces();
		seconds--;
	    }
	    catch (Exception e) {
		Logger.handle(e);
	    }
	}
    }

    public PestControl(List<Player> team, PestData data) {
	this.team = Collections.synchronizedList(team);
	this.data = data;
    }

    public PestControl create() {
	final PestControl instance = this;
	CoresManager.slowExecutor.execute(new Runnable() {
	    @Override
	    public void run() {
		boundChunks = MapBuilder.findEmptyChunkBound(8, 8);
		MapBuilder.copyAllPlanesMap(328, 320, boundChunks[0], boundChunks[1], 8);
		sendBeginningWave();
		unlockPortal();
		for (Player player : team) {
		    player.getControllerManager().removeControllerWithoutCheck();
		    player.useStairs(-1, getLocation(35 - Misc.random(4), 54 - (Misc.random(3))), 1, 2);
		    player.getControllerManager().startController("PestControlGame", instance);
		}
		CoresManager.fastExecutor.schedule(new PestGameTimer(), 1000, 1000);
	    }
	});
	return instance;
    }

    private void sendBeginningWave() {
	knight = new PestPortal(KNIGHT_IDS[Misc.random(KNIGHT_IDS.length)], true, getLocation(32, 32), this);
	knight.unlock();
	for (int index = 0; index < portals.length; index++) {
	    PestPortal portal = portals[index] = new PestPortal(6146 + index, true, getLocation(PORTAL_LOCATIONS[0][index], PORTAL_LOCATIONS[1][index]), this);
	    portal.setHitpoints(data.ordinal() == 0 ? 2000 : 2500);
	}
    }

    public boolean createPestNPC(int index) {
	if (pestCounts[index] >= (index == 4 ? 4 : (portals[index] != null && portals[index].isLocked()) ? 5 : 15))
	    return false;
	pestCounts[index]++;
	Location baseTile = getLocation(PORTAL_LOCATIONS[0][index], PORTAL_LOCATIONS[1][index]);
	Location teleTile = baseTile;
	int npcId = index == 4 ? data.getShifters()[Misc.random(data.getShifters().length)] : data.getPests()[Misc.random(data.getPests().length)];
	NPCDefinition defs = NPCDefinition.get(npcId);
	for (int trycount = 0; trycount < 10; trycount++) {
	    teleTile = new Location(baseTile, 5);
	    if (Game.canMoveNPC(baseTile.getZ(), teleTile.getX(), teleTile.getY(), defs.size))
		break;
	    teleTile = baseTile;
	}
	String name = defs.getName().toLowerCase();
	if (name.contains("shifter"))
	    new Shifter(npcId, teleTile, -1, true, true, index, this);
	else if (name.contains("splatter"))
	    new Splatter(npcId, teleTile, -1, true, true, index, this);
	else if (name.contains("spinner"))
	    new Spinner(npcId, teleTile, -1, true, true, index, this);
	else if (name.contains("brawler"))
	    brawlers.add(new PestMonsters(npcId, teleTile, -1, true, true, index, this));
	else
	    new PestMonsters(npcId, teleTile, -1, true, true, index, this);
	return true;
    }

    public void endGame() {
	final List<Player> team = new LinkedList<Player>();
	team.addAll(this.team);
	this.team.clear();
	for (final Player player : team) {
	    final int knightZeal = (int) ((PestControlGame) player.getControllerManager().getControler()).getPoints();
	    player.getControllerManager().forceStop();
	    WorldTasksManager.schedule(new WorldTask() {

		@Override
		public void run() {
		    sendFinalReward(player, knightZeal);
		}
	    }, 1);
	}
	CoresManager.slowExecutor.schedule(new Runnable() {
	    @Override
	    public void run() {
		MapBuilder.destroyMap(boundChunks[0], boundChunks[1], 8, 8);
		boundChunks = null;
	    }
	}, 6000, TimeUnit.MILLISECONDS);
    }

    private void sendFinalReward(Player player, int knightZeal) {
	if (knight.isDead())
	    player.getMatrixDialogues().startDialogue("SimpleMessage", "You failed to protect the void knight and have not been awarded any points.");
	else if (knightZeal < 750)
	    player.getMatrixDialogues().startDialogue("SimpleMessage", "The knights notice your lack of zeal in that battle and have not presented you with any points.");
	else {
	    int coinsAmount = player.getSkills().getCombatLevel() * 100;
	    int pointsAmount = data.getReward();
	    player.getMatrixDialogues().startDialogue("SimpleMessage", "Congradulations! You have successfully kept the lander safe and have been awarded: " + coinsAmount + " gold coins and " + pointsAmount + " commendation points.");
	    player.getInventory().addItem(new Item(995, coinsAmount));
	    player.setPestPoints(player.getPestPoints() + pointsAmount);
	}
    }

    private void sendPortalInterfaces() {
	for (Player player : team) {
	    for (int i = 13; i < 17; i++) {
		PestPortal npc = portals[i - 13];
		if (npc != null)
		    player.packets().sendIComponentText(408, i, npc.getHitpoints() + "");
	    }
	    player.packets().sendIComponentText(408, 1, "" + knight.getHitpoints());
	}
    }

    public void unlockPortal() {
	if (portalCount == 0)
	    return;
	else if (portalCount == 1) {
	    portalCount--;
	    return;
	}
	final int index = Misc.random(portals.length);
	if (portals[index] == null || portals[index].isDead()) {
	    unlockPortal();
	} else {
	    portalCount--;
	    WorldTasksManager.schedule(new WorldTask() {

		@Override
		public void run() {
		    portals[index].unlock();
		}
	    }, 30);
	}
    }

    public boolean isBrawlerAt(Location tile) {
	for (Iterator<NPC> it = brawlers.iterator(); it.hasNext();) {
	    NPC npc = it.next();
	    if (npc.isDead() || npc.hasFinished()) {
		it.remove();
		continue;
	    }
	    if (npc.getX() == tile.getX() && npc.getY() == tile.getY() && tile.getZ() == tile.getZ())
		return true;
	}
	return false;
    }

    private void updateTime(int minutes) {
	for (Player player : team)
	    player.packets().sendIComponentText(408, 0, minutes + " min");
    }

    public void sendTeamMessage(String message) {
	for (Player player : team)
	    player.packets().sendMessage(message, true);
    }

    private boolean canFinish() {
	if (knight == null || knight.isDead())
	    return true;
	return portalCount == 0;
    }

    public Location getLocation(int mapX, int mapY) {
	return new Location(boundChunks[0] * 8 + mapX, boundChunks[1] * 8 + mapY, 0);
    }

    public PestPortal[] getPortals() {
	return portals;
    }

    public List<Player> getPlayers() {
	return team;
    }

    public NPC getKnight() {
	return knight;
    }

    public enum PestData {

	NOVICE(new int[] { /*Shifters*/3732, 3733, 3734, 3735, /*Ravagers*/3742, 3743, 3744, /*Brawler*/3772, 3773, /*Splatter*/3727, 3728, 3729, /*Spinner*/3747, 3748, 3749, /*Torcher*/3752, 3753, 3754, 3755, /*Defiler*/3762, 3763, 3764, 3765 }, new int[] { 3732, 3733, 3734, 3735 }, 3),

	INTERMEDIATE(new int[] { /*Shifters*/3734, 3735, 3736, 3737, 3738, 3739/*Ravagers*/, 3744, 3743, 3745, /*Brawler*/3773, 3775, 3776, /*Splatter*/3728, 3729, 3730, /*Spinner*/3748, 3749, 3750, 3751, /*Torcher*/3754, 3755, 3756, 3757, 3758, 3759, /*Defiler*/3764, 3765, 3766, 3768, 3769 }, new int[] { 3734, 3735, 3736, 3737, 3738, 3739 }, 5),

	VETERAN(new int[] { /*Shifters*/3736, 3737, 3738, 3739, 3740, 3741 /*Ravagers*/, 3744, 3745, 3746, /*Brawler*/3776, 3774,/*Splatter*/3729, 3730, 3731, /*Spinner*/3749, 3750, 3751, /*Torcher*/3758, 3759, 3760, 3761,/*Defiler*/3770, 3771 }, new int[] { 3736, 3737, 3738, 3739, 3740, 3741 }, 7);

	private int[] pests, shifters;
	private int reward;

	private PestData(int[] pests, int[] shifters, int reward) {
	    this.pests = pests;
	    this.shifters = shifters;
	    this.reward = reward;
	}

	public int[] getShifters() {
	    return shifters;
	}

	public int[] getPests() {
	    return pests;
	}

	public int getReward() {
	    return reward;
	}
    }

    public int[] getPestCounts() {
	return pestCounts;
    }

    public PestData getPestData() {
	return data;
    }

    public int getPortalCount() {
	return portalCount;
    }
}
