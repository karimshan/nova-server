package org.nova.game.npc.dungeonnering;

import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.content.Dungeoneering.Dungeon;

@SuppressWarnings("serial")
public class DungeonBoss extends NPC {

	private Dungeon dungeon;

	public DungeonBoss(int id, Location tile, Dungeon dungeon) {
		super(id, tile, dungeon.getDungeonBossRoomHash(), false, true);
		this.dungeon = dungeon;
		setForceMultiArea(true);
		setForceAgressive(true);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		dungeon.openStairs();
	}

	public Dungeon getDungeon() {
		return dungeon;
	}
}
