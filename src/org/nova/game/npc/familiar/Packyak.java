package org.nova.game.npc.familiar;

import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.actions.Summoning.Pouches;
import org.nova.game.player.controlers.Duelarena;

public class Packyak extends Familiar {

	/**
	 * @author Fuzen Seth
	 */
	private static final long serialVersionUID = -1397015887332756680L;

	public Packyak(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, false);
	}

	@Override
	public int getSpecialAmount() {
		return 12;
	}

	@Override
	public String getSpecialName() {
		return "Winter Storage";
	}

	@Override
	public String getSpecialDescription() {
		return "Use special move on an item in your inventory to send it to your bank.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ITEM;
	}

	@Override
	public int getBOBSize() {
		return 30;
	}

	@Override
	public boolean isAgressive() {
		return false;
	}

	@Override
	public boolean submitSpecial(Object object) {
		if (getOwner().isDead() || getOwner().getControllerManager().getControler() instanceof Duelarena) {
			return false;
		}
		if (!getOwner().getBank().hasBankSpace()) {
		getOwner().sendMessage("You don't have enough space in your bank account.");
			return false;
		}
		int slotId = (Integer) object;
		if (getOwner().getBank().hasBankSpace()) {
			getOwner().getBank().depositItem(slotId, 1, false);
			getOwner().packets().sendMessage(
					"The pack yak has sent an item to your bank.");
			getOwner().setNextGraphics(new Graphics(1316));
			getOwner().setNextAnimation(new Animation(7660));
			getOwner().getBank().refreshItems();
		}
		return true;
	}
}
