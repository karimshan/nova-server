package org.nova.game.player.content;

import java.util.HashMap;
import java.util.Map;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.Item;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.utility.loading.Logger;

public class GildedAltar {

	public enum bonestoOffer {

		NORMAL(526, 200),

		BIG(532, 300),

		OURG(4834, 500),

		DRAGON(536, 520),

		FROST_DRAGON(18830, 750);

		private int id;
		private double experience;

		private static Map<Integer, bonestoOffer> bones = new HashMap<Integer, bonestoOffer>();

		static {
			for (bonestoOffer bone : bonestoOffer.values()) {
				bones.put(bone.getId(), bone);
			}
		}

		public static bonestoOffer forId(int id) {
			return bones.get(id);
		}

		private bonestoOffer(int id, double experience) {
			this.id = id;
			this.experience = experience;
		}

		public int getId() {
			return id;
		}

		public double getExperience() {
			return experience;
		}

		public static boolean stopOfferGod = false;
		private static boolean imfuckingprayingfortheGods = false;

		public static void offerprayerGod(final Player player, Item item) {
			final int itemId = item.getId();
			final ItemDefinition itemDef = new ItemDefinition(item.getId());
			final bonestoOffer bone = bonestoOffer.forId(item.getId());
			WorldTasksManager.schedule(new WorldTask() {
				public void run() {
					try {
						if (!player.getInventory().containsItem(itemId, 1)) {
							stop();
							return;
						}
						player.packets()
								.sendMessage(
										"The gods are very pleased with your offering.");
						player.setNextAnimation(new Animation(896));
						player.setNextGraphics(new Graphics(624));
						player.getInventory().deleteItem(new Item(itemId, 1));
						double xp = bone.getExperience()
								* player.getAuraManager().getPrayerMultiplier();
						player.getSkills().addXp(Skills.PRAYER, xp);
						player.getInventory().refresh();
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, 0, 3);
		}
	}
}
