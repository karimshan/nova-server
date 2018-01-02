package org.nova.game.npc.normal;

import org.nova.game.map.Location;
import org.nova.game.masks.Hit;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * 
 * @author Josh'
 *
 */
public class SkeletonMonkey extends NPC {

	private static final long serialVersionUID = 188692144944819705L;
	
	private Hit hit;
	
	private Player player;
	
	public SkeletonMonkey(int id, Location tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setCapDamage(200);
	}
}