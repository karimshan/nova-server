package org.nova.kshan.content.skills.construction;

import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class RoomDeletion extends Dialogue {
	
	GlobalObject object;
	RoomChunk nextRoom;

	@Override
	public void start() {
		RoomChunk room = player.getHouse().getCurrentRoom(player);
		object = (GlobalObject) data[1];
		RoomChunk oRoom = player.getHouse().getCurrentRoom(object);
		int nextX = player.getXInChunk() == 0 ? player.getX() - 1 : 
			player.getXInChunk() == 7 ? player.getX() + 1 : player.getX();
		int nextY = player.getYInChunk() == 0 ? player.getY() - 1 : 
			player.getYInChunk() == 7 ? player.getY() + 1 : player.getY();
		int z = player.getZ();
		nextRoom = player.getHouse().getCurrentRoom(new Location(nextX, nextY, z));
		if(object == null) {
			end();
			return;
		}
		if(room != oRoom) {
			for(GlobalObject object : player.getHouse().getTemporaryObjects()) {
				if(object.getId() == 13405 && player.getHouse().getCurrentRoom(object) == oRoom) {
					if(oRoom.getRoom().getName().toLowerCase().equals("garden") && player.getHouse().getPortalCount() == 1) {
						end();
						sendLines("Your house must contain at least one exit portal.");
						return;
					}
				}
			}
			sendOptions("Remove the "+Misc.upper(oRoom.getRoom().getName().toLowerCase())+"?", "Yes", "No");
		} else {
			if(nextRoom != null) {
				for(GlobalObject object : player.getHouse().getTemporaryObjects()) {
					if(object.getId() == 13405 && player.getHouse().getCurrentRoom(object) == nextRoom) {
						if(nextRoom.getRoom().getName().toLowerCase().equals("garden") && player.getHouse().getPortalCount() == 1) {
							end();
							sendLines("Your house must contain at least one exit portal.");
							return;
						}
					}
				}
				sendOptions("Remove the "+Misc.upper(nextRoom.getRoom().getName().toLowerCase())+"?", "Yes", "No");
			} else {
				end();
				player.interfaces().sendInterface(402); // Create a room
			}
		}
		
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		end();
		if(buttonId == OPTION1)
			player.getHouse().getConstruction().deleteRoom(nextRoom);
	}

	@Override
	public void finish() { 
		
	}

}
