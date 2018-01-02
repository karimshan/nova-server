package org.nova.game.npc.dragons;

import org.nova.game.map.Location;
import org.nova.game.npc.NPC;

@SuppressWarnings("serial")
public class KingBlackDragon extends NPC {

	public KingBlackDragon(int id, Location tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
	}

	public static boolean atKBD(Location tile) {
		if ((tile.getX() >= 2250 && tile.getX() <= 2292)
				&& (tile.getY() >= 4675 && tile.getY() <= 4710))
			return true;
		return false;
	}

}
