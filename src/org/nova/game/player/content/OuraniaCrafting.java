package org.nova.game.player.content;

import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
/**
 * 
 * @author Fuzen Seth
 * @information Represents Ourania runecrafting.
 * @since 23.6.2014
 */
public final class OuraniaCrafting {

	private OuraniaCrafting() {

		}
	/**
	 * Holds the interface id of the bank charge.
	 */
	public static final int INTERFACE = 619;
	
	@SuppressWarnings("unused")
	private static final int AIR_RUNE = 556, WATER_RUNE = 555,
			EARTH_RUNE = 557, FIRE_RUNE = 554, MIND_RUNE = 558,
			NATURE_RUNE = 561, CHAOS_RUNE = 562, DEATH_RUNE = 560,
			BLOOD_RUNE = 565, SOUL_RUNE = 566, ASTRAL_RUNE = 9075,
			LAW_RUNE = 563, STEAM_RUNE = 4694, MIST_RUNE = 4695,
			DUST_RUNE = 4696, SMOKE_RUNE = 4697, MUD_RUNE = 4698,
			LAVA_RUNE = 4699, ARMADYL_RUNE = 21773;
		/**
		 * Holds the item id of the runes.
		 */
	public static final int[] RUNES = {556,555,557,554,558,561,562,560,565,566,9075,563,4694,4695,4696,4697,4698,4698};
	/**
	 * Loads the singleton.
	 */
	private static OuraniaCrafting singleton = new OuraniaCrafting();
	
	/**
	 * Holds the item id of rune and pure essence.
	 */
	public final static int RUNE_ESSENCE = 1436, PURE_ESSENCE = 7936;
	
	/**
	 * Handles the bank charge buttons.
	 * @param player
	 * @param component
	 * @return
	 */
	public static final boolean handleBankChargeButtons(Player player, int component) {
		switch (component) {
		
		}
		return false;
	}
	
	/**
	 * Opens bank, player needs to charge first.
	 * @param player
	 */
	public static final void openBank(Player player) {
		player.getBank().openBank();
	/**
	 * TODO Bank paying
	 * * Find configs
	 */
		//player.getInterfaceManager().sendInterface(INTERFACE);
		for (int i = 0; i < 45; i++)
		player.packets().sendUnlockIComponentOptionSlots(619, i, 0, 58, 0);
//		player.getPackets().sendConfig(id, value);
	}
	
	/**
	 * Crafts the player's essences.
	 * @param player
	 * @param pureEssOnly
	 */
	public void craftEssence(Player player, boolean pureEssOnly) {
		int runes = player.getInventory().getItems().getNumberOf(PURE_ESSENCE);
		if (runes > 0)
			player.getInventory().deleteItem(PURE_ESSENCE, runes);
		if (!pureEssOnly) {
			int normalEss = player.getInventory().getItems()
					.getNumberOf(RUNE_ESSENCE);
			if (normalEss > 0) {
				player.getInventory().deleteItem(RUNE_ESSENCE, normalEss);
				runes += normalEss;
			}
		}
		if (runes == 0) {
			player.getMatrixDialogues().startDialogue(
					"SimpleMessage",
					"You don't have " + (pureEssOnly ? "pure" : "rune")
							+ " essence.");
			return;
		}
		player.getSkills().addXp(20, 103);
		player.setNextGraphics(new Graphics(186));
		player.setNextAnimation(new Animation(791));
		player.addStopDelay(5);
		player.packets().sendMessage(
				"You bind the temple's power into runes.");
		/**
		 * Now we add the extra EXP if skill level is high enough.
		 */
		if (player.getSkills().getLevel(Skills.RUNECRAFTING) > 89) {
		player.getSkills().addXp(Skills.RUNECRAFTING, 35 * runes);
		return;
		}
		else if (player.getSkills().getLevel(Skills.RUNECRAFTING) > 79) {
			player.getSkills().addXp(Skills.RUNECRAFTING, 50 * runes);
		return;
		}
		else if (player.getSkills().getLevel(Skills.RUNECRAFTING) > 69) {
			player.getSkills().addXp(Skills.RUNECRAFTING, 75 * runes);
			return;
		
		}
		
		for (int rune : RUNES)
	player.getInventory().addItem(rune, runes / 3);
	}

	public static OuraniaCrafting getSingleton() {
		return singleton;
	}


	public static void setSingleton(OuraniaCrafting singleton) {
		OuraniaCrafting.singleton = singleton;
	}
	
}