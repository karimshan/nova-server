package org.nova.game.player.actions;

import org.nova.game.entity.Entity;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

public class PlayerFollow extends Action {

	private Entity target;

	public PlayerFollow(Entity target) {
		this.target = target;
	}

	@Override
	public boolean start(Player player) {
		player.setNextFaceEntity(target);
		if (checkAll(player))
			return true;
		player.setNextFaceEntity(null);
		return false;
	}

	private boolean checkAll(Player player) {
		if (player.isDead() || player.hasFinished() || target.isDead()
				|| target.hasFinished())
			return false;
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		int size = target.getSize();
		int maxDistance = 16;
		if (target.getDirection() == 0) {//north
			player.addWalkStepsInteract(target.getX(), target.getY() + 1,
					target.getRun() ? 2 : 1, size, true);
      	}
      	if (target.getDirection() == 2048) {//north-east
      		player.addWalkStepsInteract(target.getX() + 1, target.getY() + 1,
      				target.getRun() ? 2 : 1, size, true);
      	}
      	if (target.getDirection() == 4096) {//east
      		player.addWalkStepsInteract(target.getX() + 1, target.getY(),
      				target.getRun() ? 2 : 1, size, true);
      	}
      	if (target.getDirection() == 6144) {//south-east
      		player.addWalkStepsInteract(target.getX() + 1, target.getY() - 1,
      				target.getRun() ? 2 : 1, size, true);
         	}
      	if (target.getDirection() == 8192) {//south
      		player.addWalkStepsInteract(target.getX(), target.getY() - 1,
      				target.getRun() ? 2 : 1, size, true);
        	}
      	if (target.getDirection() == 10240) {//south-west
      		player.addWalkStepsInteract(target.getX() - 1, target.getY() - 1,
      				target.getRun() ? 2 : 1, size, true);
       	}
      	if (target.getDirection() == 12288) {//west
      		player.addWalkStepsInteract(target.getX() - 1, target.getY(),
      				target.getRun() ? 2 : 1, size, true);
      	}
      	if (target.getDirection() == 14336) {//north-west
      		player.addWalkStepsInteract(target.getX() - 1, target.getY() + 1,
      				target.getRun() ? 2 : 1, size, true);
        }
		if (player.getZ() != target.getZ()
				|| distanceX > size + maxDistance
				|| distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance)
			return false;
		if (player.getFreezeDelay() >= Misc.currentTimeMillis())
			return true;
		maxDistance = 0;
		if ((!player.clipedProjectile(target, true))
				|| distanceX > size + maxDistance
				|| distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance) {
			
			if (player.hasWalkSteps())
				player.resetWalkSteps();
			player.addWalkStepsInteract(target.getX(), target.getY(),
					player.getRun() ? 2 : 1, size, true);
			// }
			return true;
		} else {
			player.resetWalkSteps();
			int lastFaceEntity = target.getLastFaceEntity();
			if (lastFaceEntity >= 32768) {
				lastFaceEntity -= 32768;
				if (lastFaceEntity == player.getIndex())
					player.addWalkSteps(target.getLastLocation().getX(),
							target.getLastLocation().getY(), size, true);
			}
		}

		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		return 0;
	}

	@Override
	public void stop(Player player) {
		player.setNextFaceEntity(null);

	}

}
