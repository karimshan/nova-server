package org.nova.kshan.content.skills.construction;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.map.GlobalObject;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class RemoveRoomObject extends Dialogue {
	
	GlobalObject o;
	
	@Override
	public void start() {
		o = (GlobalObject) data[0];
		sendOptions("Really remove it?", "Yes", "No");
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		end();
		if(buttonId == 1) {
			player.setCantMove(true);
			player.animate(3685);
			if(player.getHouse().getCurrentRoom(o) == null) { // If room somehow nulls
				player.getHouse().getConstruction().removeObject(o); // then the object will still go away
				return;
			}
			Game.submit(new GameTick(.5) {
				public void run() {
					player.setCantMove(false);
					player.getHouse().getConstruction().removeObject(o);
					stop();
				}
			});
			return;
		}
	}

	@Override
	public void finish() { 
		
	}
	
}
