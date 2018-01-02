package org.nova.game.npc.familiar;

import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Summoning.Pouches;

public class Wartortoise extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5092434230714486203L;

	public Wartortoise(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Testudo";
	}

	@Override
	public String getSpecialDescription() {
		return "Increases defence by nine points.";
	}

	@Override
	public int getBOBSize() {
		return 18;
	}

	@Override
	public int getSpecialAmount() {
		return 20;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		int newLevel = player.getSkills().getLevel(Skills.DEFENCE) + 9;
		if (newLevel > player.getSkills().getLevelFromXP(Skills.DEFENCE) + 9)
			newLevel = player.getSkills().getLevelFromXP(Skills.DEFENCE) + 9;
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		player.getSkills().set(Skills.DEFENCE, newLevel);
		return true;
	}
}
