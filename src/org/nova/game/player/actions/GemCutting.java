package org.nova.game.player.actions;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.item.Item;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.utility.misc.Misc;

	public class GemCutting extends Action {

		/**
		 * Enum for gems
		 * 
		 * @author Raghav
		 * 
		 */
		public enum Gem {
			OPAL(1625, 1609, 15.0, 1, 886),

			JADE(1627, 1611, 20, 13, 886),

			RED_TOPAZ(1629, 1613, 25, 16, 887),

			SAPPHIRE(1623, 1607, 50, 20, 888),

			EMERALD(1621, 1605, 67, 27, 889),

			RUBY(1619, 1603, 85, 34, 887),

			DIAMOND(1617, 1601, 107.5, 43, 890),

			DRAGONSTONE(1631, 1615, 137.5, 55, 885),

			ONYX(6571, 6573, 167.5, 67, 2717);

			private double experience;
			private int levelRequired;
			private int uncut, cut;

			private int emote;

			private Gem(int uncut, int cut, double experience, int levelRequired,
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
		
		private Gem gem;
		private int quantity;

		public GemCutting(Gem gem, int quantity) {
			this.gem = gem;
			this.quantity = quantity;
		}
		public boolean checkAll(Player player) {
			if (player.getSkills().getLevel(Skills.CRAFTING) < gem
					.getLevelRequired()) {
				player.packets().sendMessage(
						"You need a crafting level of " + gem.getLevelRequired()
						+ " to cut that gem.");
				return false;
			}
			if (!player.getInventory().containsOneItem(gem.getUncut())) {
				player.packets().sendMessage("You don't have any "
								+ ItemDefinition
								.get(gem.getUncut())
								.getName().toLowerCase() + " to cut.");
				return false;
			}
			return true;
		}
		
		public static void cut(Player player, Gem gem) {
			int amount = player.getInventory().getItems().getNumberOf(new Item(gem.getUncut(), 1));
			if (amount <= 1)
				player.getActionManager().setAction(new GemCutting(gem, 1));
			else {
				if (new GemCutting(gem, amount).checkAll(player))
					player.getMatrixDialogues().startDialogue("GemCuttingD", gem);
			}	
		}
		
		
		private boolean isSuccesfull(Player player) {
			int craftingLevel = player.getSkills().getLevel(Skills.CRAFTING);
			int level = Misc.getRandom(craftingLevel);
			double ratio = level / (Misc.random(gem.getLevelRequired() / 2) + 1);
			if (Math.round(ratio * craftingLevel) < gem.getLevelRequired())
				return false;
			return true;
		}

		@Override
		public boolean start(Player player) {
			if (checkAll(player)) {
				setActionDelay(player, 1);
				player.setNextAnimation(new Animation(gem.getEmote()));
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
			player.getInventory().deleteItem(gem.getUncut(), 1);
			if (!isSuccesfull(player)) {
				player.getInventory().addItem(1633 , 1);
				player.getSkills().addXp(Skills.CRAFTING, 1);
				player.packets().sendMessage(
						"You accidentally crushed the gem upon attempting to cut it." , true);
			} else {
			player.getInventory().addItem(gem.getCut(), 1);
			player.getSkills().addXp(Skills.CRAFTING, gem.getExperience());
			player.packets().sendMessage(
					"You cut the "
							+ ItemDefinition.get(gem.getUncut())
							.getName().toLowerCase() + ".", true);
			}
			quantity--;
			if (quantity <= 0)
				return -1;
			player.setNextAnimationNoPriority(new Animation(gem.getEmote())); // start the
			return 1;
		}

		@Override
		public void stop(final Player player) {
			setActionDelay(player, 3);
		}
	}