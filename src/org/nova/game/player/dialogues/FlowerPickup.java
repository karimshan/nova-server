package org.nova.game.player.dialogues;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.masks.Animation;


public class FlowerPickup extends MatrixDialogue {

	GlobalObject flowerObject;
	
	public int getFlowerId(int objectId) {
		return 2460+((objectId-2980)*2);
	}
	
	@Override
	public void start() {
		flowerObject = (GlobalObject) parameters[0];
		sendDialogue(SEND_2_OPTIONS, "Select a Option", "Pick the flowers.", "Leave the flowers into ground.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == 1) {
				player.setNextAnimation(new Animation(827));
				player.getInventory().addItem(getFlowerId(flowerObject.getId()), 1);
				player.getInventory().refresh();
				Game.removeObject(flowerObject, false);
			}
			end();
		}
	}

	@Override
	public void finish() {

	}
}