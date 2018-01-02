package org.nova.game.npc.familiar;

import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.actions.HerbCleaning.Herbs;
import org.nova.game.player.actions.Summoning.Pouches;
import org.nova.utility.misc.Misc;

public class Macaw extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7805271915467121215L;

	public Macaw(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Herbcall";
	}

	@Override
	public String getSpecialDescription() {
		return "Creates a random herb.";
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
		Herbs herb;
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		// TODO too lazy to find anims and gfx
		if (Misc.getRandom(100) == 0)
			herb = Herbs.values()[Misc.random(Herbs.values().length)];
		else
			herb = Herbs.values()[Misc.getRandom(3)];
		Game.addGroundItem(new Item(herb.getHerbId(), 1), player);
		return true;
	}
}
