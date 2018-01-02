package org.nova.game.npc.familiar;

import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Summoning.Pouches;

public class Granitecrab extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 649164679697311630L;

	public Granitecrab(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Stony Shell";
	}

	@Override
	public String getSpecialDescription() {
		return "Increases your restance to all attacks by four.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 12;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		int newLevel = player.getSkills().getLevel(Skills.DEFENCE) + 4;
		if (newLevel > player.getSkills().getLevelFromXP(Skills.DEFENCE) + 4)
			newLevel = player.getSkills().getLevelFromXP(Skills.DEFENCE) + 4;
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		setNextGraphics(new Graphics(8108));
		setNextAnimation(new Animation(1326));
		player.getSkills().set(Skills.DEFENCE, newLevel);
		return true;
	}

}
