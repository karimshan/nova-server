package org.nova.game.npc.familiar;

import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.game.player.actions.Summoning.Pouches;

public class Spiritmosquito extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3249731229258558109L;

	public Spiritmosquito(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Pester";
	}

	@Override
	public String getSpecialDescription() {
		return "Sends a mosquito to your opponent.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 3;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return false;
	}

}
