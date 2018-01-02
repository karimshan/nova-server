package org.nova.game.npc.familiar;

import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.game.player.actions.Summoning.Pouches;
import org.nova.game.player.actions.Woodcutting;
import org.nova.game.player.actions.Woodcutting.TreeDefinitions;

public class Beaver extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9181393770444014076L;

	public Beaver(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Multichop";
	}

	@Override
	public String getSpecialDescription() {
		return "Chops a tree, giving the owner its logs. There is also a chance that random logs may be produced.";
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
		return SpecialAttack.OBJECT;
	}

	@Override
	public boolean submitSpecial(Object context) {
		GlobalObject object = (GlobalObject) context;
		getOwner().getActionManager().setSkill(
				new Woodcutting(object, TreeDefinitions.NORMAL));
		return true;
	}
}
