package org.nova.kshan.content.cutscenes.impl;

import org.nova.game.Game;
import org.nova.game.npc.NPC;
import org.nova.kshan.content.cutscenes.Cutscene;

/**
 * 
 * @author K-Shan
 *
 */
public class TestCutscene extends Cutscene {
	
	NPC npc;

	@Override
	public double getTickDelay() {
		return 0.6;
	}

	@Override
	public void processActions() {
		move(3222, 3222, 0, 5);
		cam(0, -3, 1200, 17, false, -1);
		cam(0, 3, 0, 17, true, 5);
		faceDirection("South", 0);
		animate(382, -1);
		graphics(2795, -1);
		forceTalk("Oh you know, just f**king shit up with my blades.", 6);
		createNPC(1, new int[] { 1, 0 }, false, "South", "Blue man", 5);
		delay(1); // Counts as a stage
	}
	
	@Override
	public void extraActions() {
		if(stage == 9) {
			npc = Game.getLocalNPC(((NPC) getStoredData().get(0)).getName(), player);
			npc.graphics(2795);
			forceTalk("Cunt", 6, npc);
		}
	}

	@Override
	public boolean hasHiddenMinimap() {
		return false;
	}

	@Override
	public boolean hasScreenZoom() {
		return false;
	}

	@Override
	public boolean teleportBack() {
		return true;
	}

}
