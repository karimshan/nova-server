package org.nova.kshan.content.skills.magic;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.MagicAbility;
import org.nova.utility.misc.Misc;

/**
 * @author Fuzen Seth
 * @since 13.12.2013
 * @category This represents a player casting a spell on player. 
 * <info> - Non-combat spells like special energy transfers.
 */
public class MagicOnPlayerPacketHandler {

	@SuppressWarnings("unused")
	private static final int AIR_RUNE = 556, WATER_RUNE = 555,
			EARTH_RUNE = 557, FIRE_RUNE = 554, MIND_RUNE = 558,
			NATURE_RUNE = 561, CHAOS_RUNE = 562, DEATH_RUNE = 560,
			BLOOD_RUNE = 565, SOUL_RUNE = 566, ASTRAL_RUNE = 9075,
			LAW_RUNE = 563, STEAM_RUNE = 4694, MIST_RUNE = 4695,
			DUST_RUNE = 4696, SMOKE_RUNE = 4697, MUD_RUNE = 4698,
			LAVA_RUNE = 4699, ARMADYL_RUNE = 21773;
	/**
	 * Energy transfer spell id.
	 */
	private static final int SPECIAL_TRANSFER = 27;
	
	public static final int STAT_SPY = 31;
	
	/**
	 * Vengeance other spell id.
	 */
	private static final int VENGEANCE_OTHER = 42;
	/**
	 * Cure other spell id.
	 */
	private static final int CURE_OTHER = 23;
	/**
	 * Heal other spell id.
	 */
	private static final int HEAL_OTHER = 52;
	
	//private static final int GROUP_VENGANCE = -1;
	/**
	 * Checks the runes.
	 * @param player
	 * @param delete
	 * @param runes
	 * @return
	 */
	private static final boolean checkRunes(Player player, boolean delete,
			int... runes) {
		int weaponId = player.getEquipment().getWeaponId();
		int shieldId = player.getEquipment().getShieldId();
		int runesCount = 0;
		while (runesCount < runes.length) {
			int runeId = runes[runesCount++];
			int ammount = runes[runesCount++];
			if (Magic.hasInfiniteRunes(runeId, weaponId, shieldId))
				continue;
			if (Magic.hasStaffOfLight(weaponId) && Misc.getRandom(8) == 0
					&& runeId != 21773)// 1 in eight chance of keeping runes
				continue;
			if (!player.getInventory().containsItem(runeId, ammount)) {
				player.packets().sendMessage(
						"You do not have enough "
								+ ItemDefinition.get(runeId)
										.getName().replace("rune", "Rune")
								+ "s to cast this spell.");
				return false;
			}
		}
		if (delete) {
			runesCount = 0;
			while (runesCount < runes.length) {
				int runeId = runes[runesCount++];
				int ammount = runes[runesCount++];
				if (Magic.hasInfiniteRunes(runeId, weaponId, shieldId))
					continue;
				player.getInventory().deleteItem(runeId, ammount);
			}
		}
		return true;
	}
	/**
	 * Gets the music id for player which is sending a spell.
	 * @param player
	 * @param componentId
	 * @return
	 */
	public static int getOutgoingSounds(Player player, int componentId) {
		switch (componentId) {
		case VENGEANCE_OTHER:
		case MagicAbility.GROUP_VENGEANCE:
			return 2907;
		}
		return 1000000;
	}
	/**
	 * Gets the incoming music id for player that is receiving a spell.
	 * @param target
	 * @param componentId
	 * @return
	 */
	public static int getIncomingSounds(Player target, int componentId) {
	switch (componentId) {
	case VENGEANCE_OTHER:
			return 2908;
	}
		return 1000000;
}
	/**
	 * We handle the packet.
	 */
	public static final void handlePacket(int componentId, Player player, Player target) {
		player.faceEntity(target);
		if (!target.isAcceptAid() && !(componentId == STAT_SPY)) {
			player.sendMessage(""+target.getDisplayName()+" doesn't have accept aid enabled.");
			return;
		}
		/**
		 * None of these spells would be allowed in duel so it would be
		 * more smarter to just get the checks here.
		 * @player
		 * @target
		 */
		if (player.isDueling())
			return;
		if (target.isDueling())
			return;
		if (!player.withinDistance(target, 8)) {
			player.addWalkSteps(target.getY() + 1, target.getX(), -1, true);
			return;
		}
	switch (componentId) {
	/**
	 * Heals the targets lifepoints meanwhile takes away players.
	 */
	case HEAL_OTHER:
		if (!(player.getSkills().getLevel(Skills.MAGIC) > 91)) {
			player.sendMessage("You must have magic level of 92 to cast this spell.");
			return;
		}
		if (!checkRunes(player, true, ASTRAL_RUNE, 3,  BLOOD_RUNE, 1, LAW_RUNE, 3))
			return;
		if (target.getHitpoints() > target.getHitpoints() / 0.7) {
			player.sendMessage("" + target.getDisplayName() + " doesn't need healing so far. He looks healthy.");
			return;
		}
		if (player.getHitpoints() < 199) {
			player.sendMessage("You must have atleast 200 lifepoints to cast this spell.");
			return;
		}
		int healingAmount = player.getHitpoints();
		target.heal((int) (target.getHitpoints() * 0.4));
		MagicAbility.preformHealOther(player, target, healingAmount);
		break;
	case STAT_SPY:
		if (!(player.getSkills().getLevel(6) >= 74)) {
			player.sendMessage("You need a magic level of 74 to cast this spell.");
			return;
		}
		if (!checkRunes(player, true, ASTRAL_RUNE, 2,  564, 2, 559, 5))
		return;
		MagicAbility.preformStatsSpy(player, target);
	break;
	/**
	 * Cures other player from poison and diseases. 
	 */
	case CURE_OTHER:
		if (!checkRunes(player, true, ASTRAL_RUNE, 1,  EARTH_RUNE, 10))
		return;
		if (!(player.getSkills().getLevel(Skills.MAGIC) >= 67)) {
			player.sendMessage("You need a magic level of 68 to cast this spell.");
			return;
		}	
		if (!target.getPoison().isPoisoned()) {
			player.sendMessage("You cannot heal someone who doesn't have poison or a disease.");
			return;
		}
		target.getPoison().reset();
		MagicAbility.cureOther(player, target);
		break;
		
		
	/**
	 * Vengeance other spell.
	 */
	case VENGEANCE_OTHER:
		if (!checkRunes(player, true, ASTRAL_RUNE, 3, DEATH_RUNE, 2,
				EARTH_RUNE, 10))
			return;
		if (player.isVeangeanceOther()) {
			player.packets().sendMessage("Players may only cast vengeance once every 30 seconds.");
			return;
		}
		if (!(player.getSkills().getLevel(Skills.MAGIC) >= 92)) {
			player.sendMessage("You need a magic level of 93 to cast this spell.");
			return;
		}
		target.setCastVeng(true);
		player.addVengeanceOther(30);
		MagicAbility.requestVengeanceOther(player, target, true);
			break;
		/**
		 * Energy transfer spell.
		 */
	case SPECIAL_TRANSFER:
		if (!checkRunes(player, true, ASTRAL_RUNE, 3, LAW_RUNE, 2,
				NATURE_RUNE, 1))
		if (!(player.getSkills().getLevel(Skills.MAGIC) >= 90)) {
			player.sendMessage("You must have magic level of 91 to use this spell.");
			return;
		}
		if (!(player.getCombatDefinitions().getSpecialAttackPercentage() >= 99)) {
			player.sendMessage("You must have full special attack to use this spell.");
			return;
		}
		/**
		 * TODO accept aid
		 */
		if (!player.isAcceptAid()) {
			player.sendMessage("This player haven't enabled accept aid.");
		}
		if (!(player.getHitpoints() >= 101)) {
			player.sendMessage("You must have atleast 100 lifepoints to cast this spell.");
			return;
		}
		player.faceLocation(target);
		/**
		 * The special comes up first like in actual RuneScape. (Supposed to come instantly)
		 */
		player.getCombatDefinitions().setSpecialAttack(0);
		target.getCombatDefinitions().setSpecialAttack(100);
		target.sendMessage("Your special attack has been restored by " + player.getDisplayName() + ".");
		MagicAbility.preformSpecialTransfer(player, target, true);
		break;
		default:
		System.out.println("Component id: "+componentId);
		}	
	getIncomingSounds(target, componentId);
	getOutgoingSounds(player, componentId);
	target.getSize();
	
	}
	
	
		
}
