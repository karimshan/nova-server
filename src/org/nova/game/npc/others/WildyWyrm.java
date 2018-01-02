package org.nova.game.npc.others;

import org.nova.game.Game;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

public class WildyWyrm {

	private static final Location[] locations = { new Location(3348, 3749, 0),	new Location(3083, 3650, 0), new Location(2970, 3841, 0), new Location(3200, 3876, 0), new Location(2982, 3785, 0),
			new Location(3055, 3665, 0), new Location(3203, 3680, 0), new Location(3269, 3649, 0), new Location(3265, 3681, 0) };

	private static NPC npc = null;

	public static boolean spawned = false;
	
	public WildyWyrm(NPC npc) {
		npc(npc);
	}

	public WildyWyrm npc(NPC npc) {
		WildyWyrm.npc = npc;
		return this;
	}
	
	public static NPC npc() {
		return npc;
	}
	
	public static void setNpc(NPC n) {
		npc = n;
	}
	
	public static void update() {
		try {
			int a = 0;
			a = Misc.random(0, (locations.length - 1));
			Location newLocation = new Location(locations[a]);
			if (!spawned || (Game.findNPC(3334) == null)) {
				Game.spawnNPC(3334, newLocation, -1, true);
				setSpawned(true);
			}
			setNpc(Game.findNPC(3334));
			npc().setLocation(newLocation);
			for (Player players : Game.getPlayers()) {
				players.packets().sendMessage("<img=6><col=ff0000>A new WildyWyrm has emerged from the ground in the wilderness.");
			}
		} catch (Exception e) {
			System.out.println("Couldn't update WildyWyrm.");
		}
	}
	
	public static void setSpawned(boolean b) {
		spawned = b;
	}
	
}