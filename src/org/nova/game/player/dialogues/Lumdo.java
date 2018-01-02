package org.nova.game.player.dialogues;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;


public class Lumdo extends MatrixDialogue {

//	private DungControler dungeon;

	@Override
	public void start() {
		sendDialogue(SEND_2_LARGE_OPTIONS,
				"Would you like to go back to Ape Atoll?", "Yes.",
				"No.");
		stage = 0;

	}

	@Override
	public void run(int interfaceId, int componentId) {
	switch (stage) {
	case 0:
		player.interfaces().closeChatBoxInterface();
		player.addStopDelay(5);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				if (loop == 2) {
				player.setLocation(new Location(2802,2704,0));
				} else if (loop == 3) {
				player.closeInterfaces();
				stop();
				}
				loop++;
				}
			}, 0, 1);
		break;
	case 1:
		end();
		player.interfaces().closeChatBoxInterface();
		break;
	}
		
	}

	@Override
	public void finish() {

	}

}
