package org.nova.game.player.content;

import java.io.Serializable;

import org.nova.game.entity.Entity;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

public final class Poison implements Serializable {

	private static final long serialVersionUID = -6324477860776313690L;

	private transient Entity entity;
	private int poisonDamage;
	private int poisonCount;

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

	public void makePoisoned(int startDamage) {
		if (poisonDamage > startDamage)
			return;
		if (entity instanceof Player) {
			Player player = ((Player) entity);
			if (player.getPoisonImmune() > Misc.currentTimeMillis())
				return;
			if (poisonDamage == 0)
				player.packets().sendMessage("You are poisoned.");
		}
		poisonDamage = startDamage;
		refresh();
	}

	public void processPoison() {
		if (!entity.isDead() && isPoisoned()) {
			if (poisonCount > 0) {
				poisonCount--;
				return;
			}
			boolean heal = false;
			if (entity instanceof Player) {
				Player player = ((Player) entity);
				// inter opened we dont poison while inter opened like at rs
				if (player.interfaces().containsScreenInter())
					return;
				if (player.getAuraManager().hasPoisonPurge())
					heal = true;
			}
			entity.applyHit(new Hit(entity, poisonDamage,
					heal ? HitLook.HEALED_DAMAGE : HitLook.POISON_DAMAGE));
			poisonDamage -= 2;
			if (isPoisoned()) {
				poisonCount = 30;
				return;
			}
			reset();
		}
	}

	public void reset() {
		poisonDamage = 0;
		poisonCount = 0;
		refresh();
	}

	public void refresh() {
		if (entity instanceof Player) {
			Player player = ((Player) entity);
			player.packets().sendConfig(102, isPoisoned() ? 1 : 0);
		}
	}

	public boolean isPoisoned() {
		return poisonDamage >= 1;
	}
}
