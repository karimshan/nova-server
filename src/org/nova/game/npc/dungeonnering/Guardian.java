package org.nova.game.npc.dungeonnering;

import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.content.dungeoneering.DungeonManager;
import org.nova.game.player.content.dungeoneering.RoomReference;

@SuppressWarnings("serial")
public class Guardian extends NPC {

	private DungeonManager manager;
	private RoomReference reference;

	public Guardian(int id, Location tile, DungeonManager manager,
			RoomReference reference) {
		super(id, tile, -1, true, true);
		this.manager = manager;
		this.reference = reference;
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		manager.updateGuardian(reference);
	}

}
