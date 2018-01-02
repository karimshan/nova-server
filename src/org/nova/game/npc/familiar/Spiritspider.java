package org.nova.game.npc.familiar;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.actions.Summoning.Pouches;
import org.nova.utility.misc.Misc;

public class Spiritspider extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5995661005749498978L;

	public Spiritspider(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Egg Spawn";
	}

	@Override
	public String getSpecialDescription() {
		return "Spawns a random amount of red eggs around the familiar.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		setNextAnimation(new Animation(8267));
		player.setNextAnimation(new Animation(7660));
		player.setNextGraphics(new Graphics(1316));
		Location tile = this;
		// attemps to randomize tile by 4x4 area
		for (int trycount = 0; trycount < Misc.getRandom(10); trycount++) {
			tile = new Location(this, 2);
			if (Game.canMoveNPC(this.getZ(), tile.getX(), tile.getY(),
					player.getSize()))
				return true;
			for (Entity entity : this.getPossibleTargets()) {
				if (entity instanceof Player) {
					Player players = (Player) entity;
					players.packets().sendGraphics(new Graphics(1342), tile);
				}
				Game.addGroundItem(new Item(223, 1), tile, player, false, 120,
						true);
			}
		}
		return true;
	}
}
