package org.nova.game.player.content.dungeoneering.rooms;

import org.nova.game.player.content.dungeoneering.DungeonManager;
import org.nova.game.player.content.dungeoneering.RoomReference;
import org.nova.utility.misc.Misc;

public final class StartRoom extends HandledRoom {

	private int[] complexitys;

	public StartRoom(int[] complexitys, int chunkX, int chunkY, int musicId,
			int... doorsDirections) {
		super(chunkX, chunkY, new RoomEvent() {
			@Override
			public void openRoom(DungeonManager dungeon, RoomReference reference) {
				dungeon.telePartyToRoom(reference);
				dungeon.spawnNPC(reference, 11226, 6 + Misc.random(3),
						6 + Misc.random(3)); // smoother
				dungeon.linkPartyToDungeon();
			}
		}, doorsDirections);
		this.complexitys = complexitys;

	}

	@Override
	public boolean isComplexity(int complexity) {
		for (int c : complexitys)
			if (c == complexity)
				return true;
		return false;
	}

}
