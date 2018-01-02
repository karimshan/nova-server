package org.nova.game.npc.arena;

import org.nova.game.map.Location;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * 
 * @author Josh'
 *
 */
public class KolodionMage extends NPC {

	private static final long serialVersionUID = 188692144944819705L;
	
	private Hit hit;
	
	private Player player;
	
	public KolodionMage(int id, Location tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setCapDamage(450);
	}
	
	@Override
	public void handleIngoingHit(Hit hit) {
		if (hit.getLook() == HitLook.MELEE_DAMAGE
				|| hit.getLook() == HitLook.RANGE_DAMAGE) {
			hit.setDamage(0);
			return;
		}
		super.handleIngoingHit(hit);
	}
}
	
	