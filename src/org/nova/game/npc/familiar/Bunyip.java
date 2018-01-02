package org.nova.game.npc.familiar;

import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Fishing.Fish;
import org.nova.game.player.actions.Summoning.Pouches;
import org.nova.game.player.content.Foods.Food;

public class Bunyip extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7203440350875823581L;

	public Bunyip(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Swallow Whole";
	}

	@Override
	public String getSpecialDescription() {
		return "Eat an uncooked fish and gain the correct number of life points corresponding to the fish eaten if you have the cooking level to cook the fish.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 7;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ITEM;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Item item = getOwner().getInventory().getItem((Integer) object);
		for (Fish fish : Fish.values()) {
			if (fish.getId() == item.getId()) {
				if (getOwner().getSkills().getLevel(Skills.COOKING) < fish
						.getLevel()) {
					getOwner()
							.packets()
							.sendMessage(
									"Your cooking level is not high enough for the bunyip to eat this fish.");
					return false;
				} else {
					getOwner().setNextGraphics(new Graphics(1316));
					getOwner().setNextAnimation(new Animation(7660));
					getOwner().heal(Food.forId(item.getId()).getHeal());
					getOwner().getInventory().deleteItem(item.getId(),
							item.getAmount());
					return true;// stop here
				}
			} else {
				getOwner().packets().sendMessage(
						"Your bunyip cannot eat this.");
				return false;
			}
		}
		return true;
	}
}
