package org.nova.game.npc.familiar;

import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.actions.Summoning.Pouches;

public class Unicornstallion extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1291968400159646829L;

	public Unicornstallion(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Healing Aura";
	}

	@Override
	public String getSpecialDescription() {
		return "Heals 15% of your health points.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 20;
	}

	@Override
	public boolean isAgressive() {
		return false;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		if (player.getHitpoints() == player.getMaxHitpoints()) {
			player.packets()
					.sendMessage(
							"You need to have at least some damage before being able to heal yourself.");
			return false;
		} else {
			player.setNextAnimation(new Animation(7660));
			player.setNextGraphics(new Graphics(1300));
			int percentHealed = player.getMaxHitpoints() / 15;
			player.heal(percentHealed);
		}
		return true;
	}

}
