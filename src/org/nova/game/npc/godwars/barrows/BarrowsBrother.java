package org.nova.game.npc.godwars.barrows;

import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.controlers.Barrows;

@SuppressWarnings("serial")
public class BarrowsBrother extends NPC {

	private Barrows barrows;

	public BarrowsBrother(int id, Location tile, Barrows barrows) {
		super(id, tile, -1, true, true);
		this.barrows = barrows;
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		barrows.killedBrother();
	}

}
