package org.nova.game.npc.dungeonnering;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.content.dungeoneering.DungeonManager;
import org.nova.game.player.content.dungeoneering.RoomReference;

@SuppressWarnings("serial")
public final class Glutenus extends NPC {

	private int meleeNPCId;
	private int switchPrayersDelay;
	private int spawnedSpiders;
	public DungeonManager dungeon;
	public RoomReference reference;


	public Glutenus(int id, Location tile, DungeonManager dungeonManager,
			RoomReference reference) {
		super(id, tile, -1, true, true);
		this.reference = reference;
		this.dungeon = dungeonManager;
		meleeNPCId = id;
		Game.spawnObject(new GlobalObject(49283, 10, 3,
				reference.getX() - 7, reference.getY() + 4, 0), true);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead())
			return;
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		dungeon.stairs(this.reference, 7, 0);
		dungeon.stairs(this.reference, 7, 15);
	}

}
