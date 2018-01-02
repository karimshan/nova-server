package org.nova.game.player.actions;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.item.Item;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;

public class PlankCutting extends Action {

	/**
	 * Enum for Wood
	 * 
	 * @author Humid
	 * 
	 */
	public enum Wood {
		REGULAR(1511, 960, 15.0, 1, 9031), 
		MAGIC(1513, 15295, 107, 75, 9031), 
		YEW(1515, 15293, 85, 60, 9031), 
		MAPLE(1517, 15292, 67, 45, 9031), 
		WILLOW(1519, 15294, 25, 30, 9031), 
		OAK(1521, 8778, 20, 15, 9031), 
		TEAK(6333, 8780, 50, 35, 9031), 
		MOHAGANY(6332, 8782, 73, 50, 9031);

		private double experience;
		private int levelRequired;
		private int uncut, cut;

		private int emote;

		private Wood(int uncut, int cut, double experience, int levelRequired,
				int emote) {
			this.uncut = uncut;
			this.cut = cut;
			this.experience = experience;
			this.levelRequired = levelRequired;
			this.emote = emote;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperience() {
			return experience;
		}

		public int getUncut() {
			return uncut;
		}

		public int getCut() {
			return cut;
		}

		public int getEmote() {
			return emote;
		}

	}

	public static void cut(Player player, Wood wood) {
		if (player.getInventory().getItems()
				.getNumberOf(new Item(wood.getUncut(), 1)) <= 1) // contains just
																// 1 lets start
			player.getActionManager().setSkill(new PlankCutting(wood, 1));
		else
			player.getMatrixDialogues().startDialogue("PlankCuttingD", wood);
	}

	private Wood wood;
	private int quantity;

	public PlankCutting(Wood wood, int quantity) {
		this.wood = wood;
		this.quantity = quantity;
	}

	public boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.CONSTRUCTION) < wood
				.getLevelRequired()) {
			player.getMatrixDialogues().startDialogue(
					"SimpleMessage",
					"You need a Construction level of " + wood.getLevelRequired()
							+ " to cut that wood.");
			return false;
		}
		if (!player.getInventory().containsOneItem(wood.getUncut())) {
			player.getMatrixDialogues().startDialogue(
					"SimpleMessage",
					"You don't have any "
							+ ItemDefinition
									.get(wood.getUncut())
									.getName().toLowerCase() + " to cut.");
			return false;
		}
		return true;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			setActionDelay(player, 1);
			player.setNextAnimation(new Animation(9031));
			return true;
		}
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		player.getInventory().deleteItem(wood.getUncut(), 1);
		player.getInventory().addItem(wood.getCut(), 1);
		player.getSkills().addXp(Skills.CONSTRUCTION, wood.getExperience());
		player.packets().sendMessage(
				"You cut the "
						+ ItemDefinition.get(wood.getUncut())
								.getName().toLowerCase() + ".", true);
		quantity--;
		if (quantity <= 0)
			return -1;
		player.setNextAnimation(new Animation(9031)); // start the
																// emote and add
																// 2 delay
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}
}