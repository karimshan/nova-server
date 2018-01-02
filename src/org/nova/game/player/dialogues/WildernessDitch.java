package org.nova.game.player.dialogues;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceMovement;

public class WildernessDitch extends MatrixDialogue {

	private GlobalObject ditch;

	@Override
	public void start() {
		ditch = (GlobalObject) parameters[0];
		player.interfaces().sendInterface(382);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (interfaceId == 382 && componentId == 19) {
			player.stopAll();
			player.lock(4);
			player.setNextAnimation(new Animation(6132));
			final Location toTile = new Location(ditch.getRotation() == 3
					|| ditch.getRotation() == 1 ? ditch.getX() - 1
					: player.getX(), ditch.getRotation() == 0
					|| ditch.getRotation() == 2 ? ditch.getY() + 2
					: player.getY(), ditch.getZ());
			player.setNextForceMovement(new ForceMovement(new Location(player),
					1, toTile, 2, ditch.getRotation() == 0
							|| ditch.getRotation() == 2 ? ForceMovement.NORTH
							: ForceMovement.WEST));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setLocation(toTile);
					player.faceObject(ditch);
					player.getControllerManager().startController("Wilderness");
					player.resetReceivedDamage();
				}
			}, 2);
		} else
			player.closeInterfaces();
		end();
	}

	@Override
	public void finish() {

	}

}
