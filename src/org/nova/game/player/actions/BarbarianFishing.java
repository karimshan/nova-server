package org.nova.game.player.actions;

import java.util.HashMap;
import java.util.Map;

import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.content.FishingSpotsHandler;
import org.nova.utility.misc.Misc;
/**
 * 
 * @author Fuzen Seth
 *
 */
	public class BarbarianFishing extends Action {

		public static boolean handleSpots(Player player, int id) {
			switch (id) {
			
			}
			return false;
		}
		
		
		public enum Fish {
			LEAPING_TROUT(11328,48,120),
			LEAPING_SALMON(11330,58,120),
			LEAPING_SURGEON(11330,70,120);
		
			private final int id, level;
			private final double xp;

			private Fish(int id, int level, double xp) {
				this.id = id;
				this.level = level;
				this.xp = xp;
			}

			public int getId() {
				return id;
			}

			public int getLevel() {
				return level;
			}

			public double getXp() {
				return xp;
			}
		}

		public enum FishingSpots {

			LURE2(329, 1, 309, 314, new Animation(622), Fish.LEAPING_SALMON, Fish.LEAPING_TROUT);

			private final Fish[] fish;
			private final int id, option, tool, bait;
			private final Animation animation;

			static final Map<Integer, FishingSpots> spot = new HashMap<Integer, FishingSpots>();

			public static FishingSpots forId(int id) {
				return spot.get(id);
			}

			static {
				for (FishingSpots spots : FishingSpots.values())
					spot.put(spots.id | spots.option << 24, spots);
			}

			private FishingSpots(int id, int option, int tool, int bait,
					Animation animation, Fish... fish) {
				this.id = id;
				this.tool = tool;
				this.bait = bait;
				this.animation = animation;
				this.fish = fish;
				this.option = option;
			}

			public Fish[] getFish() {
				return fish;
			}

			public int getId() {
				return id;
			}

			public int getOption() {
				return option;
			}

			public int getTool() {
				return tool;
			}

			public int getBait() {
				return bait;
			}

			public Animation getAnimation() {
				return animation;
			}
		}

		private FishingSpots spot;

		private NPC npc;
		private Location tile;
		private int fishId;

		private final int[] BONUS_FISH = { };

		private boolean multipleCatch;
		
		public BarbarianFishing(FishingSpots spot, NPC npc) {
			this.spot = spot;
			this.npc = npc;
			tile = new Location(npc);
		}

		@Override
		public boolean start(Player player) {
			if (!checkAll(player))
				return false;
			fishId = getRandomFish(player);

			player.packets().sendMessage("You attempt to capture a fish...",  true);
			setActionDelay(player, getFishingDelay(player));
			return true;
		}

		@Override
		public boolean process(Player player) {
			player.setNextAnimation(spot.getAnimation());
			return checkAll(player);
		}

		private int getFishingDelay(Player player) {
			int playerLevel = player.getSkills().getLevel(Skills.FISHING);
			int fishLevel = spot.getFish()[fishId].getLevel();
			int modifier = spot.getFish()[fishId].getLevel();
			int randomAmt = Misc.random(4);
			double cycleCount = 1, otherBonus = 0;
			if (player.getFamiliar() != null)
				otherBonus = getSpecialFamiliarBonus(player.getFamiliar().getId());
			cycleCount = Math.ceil(((fishLevel + otherBonus) * 50 - playerLevel * 10) / modifier * 0.25 - randomAmt * 4);
			if (cycleCount < 1) 
				cycleCount = 1;
			int delay = (int) cycleCount + 1;
			delay /= player.getAuraManager().getFishingAccurayMultiplier();
			return delay;

		}

		private int getSpecialFamiliarBonus(int id) {
			switch (id) {
			case 6796:
			case 6795:
				return 1;
			}
			return -1;
		}

		private int getRandomFish(Player player) {
			int random = Misc.random(spot.getFish().length);
			int difference = player.getSkills().getLevel(Skills.FISHING)
					- spot.getFish()[random].getLevel();
			if (difference < -1)
				return random = 0;
			if (random < -1)
				return random = 0;
			return random;
		}

		@Override
		public int processWithDelay(Player player) {
			addFish(player);
			return getFishingDelay(player);
		}

		private void addFish(Player player) {
			Item fish = new Item(spot.getFish()[fishId].getId(), multipleCatch ? 2
					: 1);
			player.packets().sendMessage(getMessage(fish),  true);
			player.getInventory().deleteItem(spot.getBait(), 1);
			double totalXp = spot.getFish()[fishId].getXp();
			if (hasFishingSuit(player))
				totalXp *= 1.025;
			player.getSkills().addXp(Skills.FISHING, totalXp);
			player.getInventory().addItem(fish);
			if (player.getFamiliar() != null) {
				if (Misc.getRandom(50) == 0 && getSpecialFamiliarBonus(player.getFamiliar().getId()) > 0) {
					player.getInventory().addItem(new Item(BONUS_FISH[Misc.random(BONUS_FISH.length)]));
					player.getSkills().addXp(Skills.FISHING, 5.5);
				}
			}
			fishId = getRandomFish(player);
			if (Misc.getRandom(50) == 0 && FishingSpotsHandler.moveSpot(npc))
				player.setNextAnimation(new Animation(-1));
		}
		
		private boolean hasFishingSuit(Player player) {
			if (player.getEquipment().getHatId() == 24427 && player.getEquipment().getChestId() == 24428
					&& player.getEquipment().getLegsId() == 24429 && player.getEquipment().getBootsId() == 24430)
				return true;
			return false;
		}

		private String getMessage(Item fish) {
		if (multipleCatch)
				return "Your quick reactions allow you to catch two " + fish.defs().getName().toLowerCase() + ".";
			else
			
				return "You manage to catch a " + fish.defs().getName().toLowerCase() + ".";
		}

		private boolean checkAll(Player player) {
			if (player.getSkills().getLevel(Skills.FISHING) < spot.getFish()[fishId].getLevel()) {
				player.getMatrixDialogues().startDialogue("SimpleMessage","You need a fishing level of " + spot.getFish()[fishId].getLevel() + " to fish here.");
				return false;
			}
			if (!player.getInventory().containsOneItem(spot.getTool())) {
				player.packets().sendMessage("You need a "+ new Item(spot.getTool()).defs().getName().toLowerCase() + " to fish here.");
				return false;
			}
			if (!player.getInventory().containsOneItem(spot.getBait()) && spot.getBait() != -1) {
				player.packets().sendMessage("You don't have " + new Item(spot.getBait()).defs().getName().toLowerCase() + " to fish here.");
				return false;
			}
			if (!player.getInventory().hasFreeSlots()) {
				player.setNextAnimation(new Animation(-1));
				player.getMatrixDialogues().startDialogue("SimpleMessage", "You don't have enough inventory space.");
				return false;
			}
			if (tile.getX() != npc.getX() || tile.getY() != npc.getY())
				return false;
			return true;
		}

		@Override
		public void stop(final Player player) {
			setActionDelay(player, 3);
		}
	}
