package org.nova.game.npc.others;

import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.content.minigames.CastleWars;
import org.nova.utility.misc.Misc;

@SuppressWarnings("serial")
public class CastleWarBarricade extends NPC {

	private int team;

	public CastleWarBarricade(int team, Location tile) {
		super(1532, tile, -1, true, true);
		setCantFollowUnderCombat(true);
		this.team = team;
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		cancelFaceEntityNoCheck();
		if (getId() == 1533 && Misc.getRandom(20) == 0)
			sendDeath(this);
	}

	public void litFire() {
		transformIntoNPC(1533);
		sendDeath(this);
	}

	public void explode() {
		sendDeath(this);
	}

	@Override
	public void sendDeath(Entity killer) {
		resetWalkSteps();
		getCombat().removeTarget();
		if (this.getId() != 1533) {
			setNextAnimation(null);
			reset();
			setCoords(getRespawnTile());
			finish();
		} else {
			super.sendDeath(killer);
		}
		CastleWars.removeBarricade(team, this);
	}

}
