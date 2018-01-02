package org.nova.game.npc.dungeonnering;

import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.npc.NPC;
import org.nova.game.player.content.dungeoneering.DungeonManager;
import org.nova.game.player.content.dungeoneering.RoomReference;

@SuppressWarnings("serial")
public final class Tokash extends NPC {

	public DungeonManager dungeon;
	public RoomReference reference;


	public Tokash(int id, Location tile, DungeonManager dungeonManager,
			RoomReference reference) {
		super(id, tile, -1, true, true);
		this.reference = reference;
		this.dungeon = dungeonManager;
		this.setHitpoints((this.getCombatLevel()*10)+200);
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
		this.setNextAnimation(new Animation(14369));
		super.sendDeath(source);
		dungeon.stairs(this.reference, 7, 0);
		dungeon.stairs(this.reference, 7, 15);
	}

}
