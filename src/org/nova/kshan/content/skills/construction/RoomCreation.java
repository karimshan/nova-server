package org.nova.kshan.content.skills.construction;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.player.Skills;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class RoomCreation extends Dialogue {
	
	RoomChunk nextRoom;

	@Override
	public void start() {
		nextRoom = (RoomChunk) data[0];
		sendOptions(TITLE, "Rotate clockwise.", "Rotate counterclockwise.", "Build.", "Cancel.");
		player.getTemporaryAttributtes().put("viewingRoom", nextRoom);
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		switch(buttonId) {
			case OPTION1:
				player.getHouse().viewRoom(nextRoom, true);
				if(nextRoom.getRotation() == 3)
					nextRoom.setRotation((-1) & 0x3);
				nextRoom.setRotation((nextRoom.getRotation() + 1) & 0x3);
				player.getHouse().viewRoom(nextRoom, false);
				start();
				break;
			case OPTION2:
				player.getHouse().viewRoom(nextRoom, true);
				if(nextRoom.getRotation() == 0)
					nextRoom.setRotation((4) & 0x3);
				nextRoom.setRotation((nextRoom.getRotation() - 1) & 0x3);
				player.getHouse().viewRoom(nextRoom, false);
				start();
				break;
			case OPTION3:
				if(player.getSkills().getLevel(Skills.CONSTRUCTION) < nextRoom.getRoom().getLevelRequired()
						&& !player.getHouse().hasCheatMode()) {
					end();
					player.interfaces().closeScreenInterface();
					player.sm("You need a Construction level of "+nextRoom.getRoom().getLevelRequired()+" in order to build this room.");
					return;
				}
				if(!player.getInventory().containsItem(995, nextRoom.getRoom().getCost())
						&& !player.getHouse().hasCheatMode()) {
					end();
					player.interfaces().closeScreenInterface();
					player.sm("You need "+nextRoom.getRoom().getCost()+" coins to build this room.");
					return;
				}
				player.getInventory().deleteItem(995, nextRoom.getRoom().getCost());
				player.getHouse().viewRoom(nextRoom, true);
				end();
				player.getHouse().getHouseData().addData(nextRoom);
				player.packets().sendWindowsPane(399, 0);
				player.getHouse().resetVariables();
				player.getHouse().setDefaults();
				player.getHouse().parseLandscape();
				player.getTemporaryAttributtes().remove("viewingRoom");
				Game.submit(new GameTick(2.4) {
					public void run() {
						stop();
						player.interfaces().sendDefaultPane();
						player.getHouse().refreshConfigs();
					}
				});
				break;
			case OPTION4:
				end();
				player.getHouse().viewRoom(nextRoom, true);
				break;
		}
	}

	@Override
	public void finish() { 
		
	}

}
