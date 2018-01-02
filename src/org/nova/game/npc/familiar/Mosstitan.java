package org.nova.game.npc.familiar;

import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Summoning.Pouches;

public class Mosstitan extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2779054495849433214L;

	public Mosstitan(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Titan's Constitution ";
	}

	@Override
	public String getSpecialDescription() {
		return "Defence by 12.5%, and it can also increase a player's Life Points 80 points higher than their max Life Points.";
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
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		int newLevel = getOwner().getSkills().getLevel(Skills.DEFENCE)
				+ (getOwner().getSkills().getLevelFromXP(Skills.DEFENCE) / (int) 12.5);
		if (newLevel > getOwner().getSkills().getLevelFromXP(Skills.DEFENCE)
				+ (int) 12.5)
			newLevel = getOwner().getSkills().getLevelFromXP(Skills.DEFENCE)
					+ (int) 12.5;
		getOwner().setNextGraphics(new Graphics(2011));
		getOwner().setNextAnimation(new Animation(7660));
		getOwner().getSkills().set(Skills.DEFENCE, newLevel);
		return true;
	}
}