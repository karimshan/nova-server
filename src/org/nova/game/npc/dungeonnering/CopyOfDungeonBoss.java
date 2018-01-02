package org.nova.game.npc.dungeonnering;

import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.content.dungeoneering.Dungeon;
import org.nova.game.player.content.dungeoneering.DungeonManager;
import org.nova.game.player.content.dungeoneering.RoomReference;

@SuppressWarnings("serial")
public class CopyOfDungeonBoss extends NPC {

	private DungeonManager dungeon;
	private RoomReference room;

	public CopyOfDungeonBoss(int id, Location tile, DungeonManager dungeon) {
		super(id, tile, -1, false, true);
		this.dungeon = dungeon;
		this.room = room;
		setForceMultiArea(true);
		setForceAgressive(true);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
	//	dungeon.openStairs(player);
	}

	public Dungeon getDungeon() {
		return dungeon.getDungeon();
	}
}
