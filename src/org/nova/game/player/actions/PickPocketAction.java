package org.nova.game.player.actions;

import org.nova.game.item.Item;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.NPC;
import org.nova.game.player.Equipment;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.utility.misc.Misc;

/**
 * Handels the pick pocketing.
 * 
 * @author Raghav/Own4g3
 * 
 */
public class PickPocketAction extends Action {

	/**
	 * Pick pocketing npc.
	 */
	private NPC npc;

	/**
	 * Data of an npc.
	 */
	private PickPocketableNPC npcData;

	/**
	 * The npc stun animation.
	 */
	private static final Animation STUN_ANIMATION = new Animation(422),

	/**
	 * The pick pocketing animation.
	 */
	PICKPOCKETING_ANIMATION = new Animation(881),

	/**
	 * The double loot animation.
	 */
	DOUBLE_LOOT_ANIMATION = new Animation(5074),

	/**
	 * The triple loot animation.
	 */
	TRIPLE_LOOT_ANIMATION = new Animation(5075),

	/**
	 * The quadruple loot animation.
	 */
	QUADRUPLE_LOOT_ANIMATION = new Animation(5078);

	/**
	 * The double loot gfx.
	 */
	private static final Graphics DOUBLE_LOOT_GFX = new Graphics(873),

	/**
	 * The triple loot gfx.
	 */
	TRIPLE_LOOT_GFX = new Graphics(874),

	/**
	 * The quadruple loot gfx.
	 */
	QUADRUPLE_LOOT_GFX = new Graphics(875);

	/**
	 * The index to use in the levels required arrays.
	 */
	private int index;

	/**
	 * Constructs a new {@code PickpocketAction} {@code Object}.
	 * 
	 * @param npc
	 *            The npc to whom the player is pickpocketing.
	 * @param npcData
	 *            Data of an npc.
	 */
	public PickPocketAction(NPC npc, PickPocketableNPC npcData) {
		this.npc = npc;
		this.npcData = npcData;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
			int agilityLevel = player.getSkills().getLevel(Skills.AGILITY);
			if (Misc.getRandom(50) < 5) { // Possibility of multiple
											// pickpocket.
				for (int i = 0; i < 4; i++) {
					if (npcData.getThievingLevels()[i] <= thievingLevel
							&& npcData.getAgilityLevels()[i] <= agilityLevel)
						index = i;
				}
			}
			player.faceEntity(npc);
			player.setNextAnimation(getAnimation());
			player.setNextGraphics(getGraphics());
			player.packets().sendMessage(
					"You attempt to pick the "
							+ npc.defs().name.toLowerCase()
							+ "'s pocket...");
			setActionDelay(player, 3);
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
		if (!isSuccesfull(player)) {
			player.packets().sendMessage(
					"You fail to pick the "
							+ npc.defs().name.toLowerCase()
							+ "'s pocket.");
			npc.setNextAnimation(STUN_ANIMATION);
			npc.faceEntity(player);
			player.setNextAnimation(new Animation(424));
			player.setNextGraphics(new Graphics(80, 5, 60));
			player.packets().sendMessage("You've been stuned.");
			player.applyHit(new Hit(player, npcData.getStunDamage(),
					HitLook.REGULAR_DAMAGE));
			if (npcData.equals(PickPocketableNPC.MASTER_FARMER)
					|| npcData.equals(PickPocketableNPC.FARMER))
				npc.setNextForceTalk(new ForceTalk(
						"Cor blimey mate, what are ye doing in me pockets?"));
			else
				npc.setNextForceTalk(new ForceTalk(
						"What do you think you're doing?"));
			player.addStopDelay(npcData.getStunTime());
			stop(player);
		} else {
			player.packets().sendMessage("" + getMessage(player));
			player.getSkills().addXp(Skills.THIEVING, npcData.getExperience());
			for (int i = 0; i <= index; i++) {
				Item item = npcData.getLoot()[Misc
						.random(npcData.getLoot().length)];
				player.getInventory().addItem(item.getId(), item.getAmount());
			}
		}
		return -1;
	}

	@Override
	public void stop(Player player) {
		npc.setNextFaceEntity(null);
	}

	/**
	 * Checks if the player is succesfull to thiev or not.
	 * 
	 * @param player
	 *            The player.
	 * @return {@code True} if succesfull, {@code false} if not.
	 */
	private boolean isSuccesfull(Player player) {
		int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
		int increasedChance = getIncreasedChance(player);
		int level = Misc.random(thievingLevel + increasedChance) + 1;
		double ratio = level
				/ (Misc.random(npcData.getThievingLevels()[0] + 5) + 1);
		if (Math.round(ratio * thievingLevel) < npcData.getThievingLevels()[0]
				/ player.getAuraManager().getThievingAccurayMultiplier())
			return false;
		return true;
	}

	/**
	 * Gets the increased chance for succesfully pickpocketing.
	 * 
	 * @param player
	 *            The player.
	 * @return The amount of increased chance.
	 */
	private int getIncreasedChance(Player player) {
		int chance = 0;
		if (Equipment.getItemSlot(Equipment.SLOT_HANDS) == 10075)
			chance += 12;
		player.getEquipment();
		if (Equipment.getItemSlot(Equipment.SLOT_CAPE) == 15349)
			chance += 15;
		if (npc.defs().name.contains("H.A.M")) {
			for (Item item : player.getEquipment().getItems().getItems()) {
				if (item != null
						&& item.defs().getName().contains("H.A.M")) {
					chance += 3;
				}
			}
		}
		return chance;
	}

	/**
	 * Gets the message to send when finishing.
	 * 
	 * @param player
	 *            The player.
	 * @return The message.
	 */
	private String getMessage(Player player) {
		switch (index) {
		case 0:
			return "You succesfully pick the "
					+ npc.defs().name.toLowerCase() + "'s pocket.";
		case 1:
			return "Your lighting-fast reactions allow you to steal double loot.";
		case 2:
			return "Your lighting-fast reactions allow you to steal triple loot.";
		case 3:
			return "Your lighting-fast reactions allow you to steal quadruple loot.";
		}
		return null;
	}

	/**
	 * Checks everything before starting.
	 * 
	 * @param player
	 *            The player.
	 * @return
	 */
	private boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.THIEVING) < npcData
				.getThievingLevels()[0]) {
			player.getMatrixDialogues().startDialogue(
					"SimpleMessage",
					"You need a thieving level of "
							+ npcData.getThievingLevels()[0]
							+ "to steal from this npc.");
			return false;
		}
		if (player.getInventory().getFreeSlots() < 1) {
			player.packets().sendMessage(
					"You don't have enough space in your inventory.");
			return false;
		}
		if (player.getAttackedBy() != null
				&& player.getAttackedByDelay() > Misc.currentTimeMillis()) {
			player.packets().sendMessage(
					"You can't do this while you're under combat.");
			return false;
		}
		if (npc.getAttackedBy() != null
				&& npc.getAttackedByDelay() > Misc.currentTimeMillis()) {
			player.packets().sendMessage("The npc is under combat.");
			return false;
		}
		if (npc.isDead()) {
			player.packets().sendMessage("Too late, the npc is dead.");
			return false;
		}
		return true;

	}

	/**
	 * Gets the animation to perform.
	 * 
	 * @param player
	 *            The player.
	 * @return The animation.
	 */
	private Animation getAnimation() {
		switch (index) {
		case 0:
			return PICKPOCKETING_ANIMATION;
		case 1:
			return DOUBLE_LOOT_ANIMATION;
		case 2:
			return TRIPLE_LOOT_ANIMATION;
		case 3:
			return QUADRUPLE_LOOT_ANIMATION;
		}
		return null;
	}

	/**
	 * Gets the graphic to perform.
	 * 
	 * @param player
	 *            The player.
	 * @return The graphic.
	 */
	private Graphics getGraphics() {
		switch (index) {
		case 0:
			return null;
		case 1:
			return DOUBLE_LOOT_GFX;
		case 2:
			return TRIPLE_LOOT_GFX;
		case 3:
			return QUADRUPLE_LOOT_GFX;
		}
		return null;
	}

}
