package org.nova.game.npc.familiar;

import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Summoning.Pouches;

public class Bullant extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4667052662212699631L;

	public Bullant(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Unburden";
	}

	@Override
	public String getSpecialDescription() {
		return "Restores the owner's run energy by half of their Agility level.";
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
		if (player.getRunEnergy() == 100) {
			player.packets().sendMessage(
					"This wouldn't effect you at all.");
			return false;
		}
		int agilityLevel = getOwner().getSkills().getLevel(Skills.AGILITY);
		int runEnergy = player.getRunEnergy() + (Math.round(agilityLevel / 2));
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		player.setRunEnergy(runEnergy > 100 ? 100 : runEnergy);
		return true;
	}
}
