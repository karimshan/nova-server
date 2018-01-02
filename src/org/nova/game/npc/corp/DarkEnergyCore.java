package org.nova.game.npc.corp;

import java.util.ArrayList;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;


@SuppressWarnings("serial")
public class DarkEnergyCore extends NPC {

	private CorporealBeast beast;
	private Entity target;

	public DarkEnergyCore(CorporealBeast beast) {
		super(8127, beast, -1, true, true);
		setForceMultiArea(true);
		this.beast = beast;
		changeTarget = 2;
	}

	private int changeTarget;
	private int delay;

	@Override
	public void processNPC() {
		if (isDead() || hasFinished())
			return;
		if (delay > 0) {
			delay--;
			return;
		}
		if (changeTarget > 0) {
			if (changeTarget == 1) {
				ArrayList<Entity> possibleTarget = beast.getPossibleTargets();
				if (possibleTarget.isEmpty()) {
					finish();
					beast.removeDarkEnergyCore();
					return;
				}
				target = possibleTarget.get(Misc.getRandom(possibleTarget
						.size() - 1));
				setLocation(new Location(target));
				Game.sendProjectile(this, this, target, 1828, 0, 0, 40, 40,
						20, 0);
			}
			changeTarget--;
			return;
		}
		if (target == null || target.getX() != getX()
				|| target.getY() != getY() || target.getZ() != getZ()) {
			changeTarget = 3;
			return;
		}
		int damage = Misc.getRandom(50) + 50;
		target.applyHit(new Hit(this, damage, HitLook.REGULAR_DAMAGE));
		beast.heal(damage);
		delay = getPoison().isPoisoned() ? 10 : (int) 0.5D;
		if (target instanceof Player) {
			Player player = (Player) target;
			player.packets()
					.sendMessage(
							"The dark core creature steals some life from you for its master.");
		}
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		beast.removeDarkEnergyCore();
	}

}
