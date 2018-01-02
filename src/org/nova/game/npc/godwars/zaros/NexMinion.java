package org.nova.game.npc.godwars.zaros;

import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.content.minigames.ZarosGodwars;

@SuppressWarnings("serial")
public class NexMinion extends NPC {

	private boolean hasNoBarrier;

	public NexMinion(int id, Location tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setCantFollowUnderCombat(true);
		setCapDamage(0);
	}

	public void breakBarrier() {
		setCapDamage(-1);
		hasNoBarrier = true;
	}

	@Override
	public void processNPC() {
		if (isDead() || !hasNoBarrier)
			return;
		if (!getCombat().process())
			checkAgressivity();
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		ZarosGodwars.moveNextStage();
	}

}
