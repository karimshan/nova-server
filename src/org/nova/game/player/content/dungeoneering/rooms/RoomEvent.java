package org.nova.game.player.content.dungeoneering.rooms;

import org.nova.game.player.content.dungeoneering.DungeonManager;
import org.nova.game.player.content.dungeoneering.RoomReference;

public interface RoomEvent {

	public void openRoom(DungeonManager dungeon, RoomReference reference);
}
