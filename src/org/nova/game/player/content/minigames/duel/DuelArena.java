package org.nova.game.player.content.minigames.duel;
import org.nova.cache.definition.ItemDefinition;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.player.Equipment;
import org.nova.game.player.actions.PlayerCombat;
import org.nova.game.player.content.Foods.Food;
import org.nova.game.player.content.Pots.Pot;
import org.nova.game.player.controlers.Controller;

public class DuelArena extends Controller {

	private final Item[] FUN_WEAPONS = {};

	@Override
	
	public void start() {
		player.setOverloadDelay(0);
		player.stopAll();
		player.addStopDelay(2); // fixes mass click steps
		player.getTemporaryAttributtes().put("startedDuel", true);
		player.getTemporaryAttributtes().put("canFight", false);
		player.reset();
		player.setCanPvp(true);
		player.hints().addHintIcon(
				player.getDuelConfigurations().getOther(player), 1, false, false);
		WorldTasksManager.schedule(new WorldTask() {
			int count = 3;

			@Override
			public void run() {
				if (count > 0)
					player.setNextForceTalk(new ForceTalk("" + count));

				if (count == 0) {
					player.getTemporaryAttributtes().put("canFight", true);
					player.setNextForceTalk(new ForceTalk("FIGHT!"));
					this.stop();
				}
				count--;
			}
		}, 0, 2);
	}

	@Override
	public boolean canEat(Food food) {
		if (player.getDuelConfigurations().getRule(4)) {
			player.packets().sendMessage(
					"You cannot eat during this duel.", true);
			return false;
		}
		return true;
	}

	@Override
	public boolean canPot(Pot pot) {
		if (player.getDuelConfigurations().getRule(3)) {
			player.packets().sendMessage(
					"You cannot drink during this duel.", true);
			return false;
		}
		return true;
	}

	@Override
	public boolean canMove(int dir) {
		if (player.getDuelConfigurations().getRule(25)) {
			player.packets().sendMessage(
					"You cannot move during this duel!", true);
			return false;
		}
		return true;
	}

	@Override
	public boolean processMagicTeleport(Location toTile) {
		player.getMatrixDialogues()
				.startDialogue("SimpleMessage",
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processItemTeleport(Location toTile) {
		player.getMatrixDialogues()
				.startDialogue("SimpleMessage",
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		switch (interfaceId) {
		case 271:
			if (player.getDuelConfigurations().getRule(5)) {
				player.packets().sendMessage(
						"You can't use prayers in this duel.");
				return false;
			}
			return true;
		case 193:
		case 430:
		case 192:
			if (player.getDuelConfigurations().getRule(2))
				return false;
			return true;
		case 884:
			switch (componentId) {
			case 4:
				if (player.getDuelConfigurations().getRule(9)) {
					player.packets().sendMessage(
							"You can't use special attacks in this duel.");
					return false;
				}
				return true;
			}
			return true;
		}
		return true;
	}

	@Override
	public boolean processObjectClick1(GlobalObject object) {
		player.getMatrixDialogues().startDialogue("ForfeitDialouge");
		return true;
	}

	@Override
	public boolean sendDeath() {
		player.addStopDelay(7);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.stopAll();
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.packets().sendMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(-1));
					player.setCanPvp(false);
					player.getDuelConfigurations().reward(
							player.getDuelConfigurations().getOther(player));
					// player.getDuelConfigurations().addSpoils(player);
					player.getDuelConfigurations()
							.endDuel(player, false, false);
					player.getDuelConfigurations().endDuel(
							player.getDuelConfigurations().getOther(player),
							false, false);
					removeControler();
					player.getControllerManager()
							.startController("DuelControler");
					player.getDuelConfigurations().getOther(player)
							.getControllerManager()
							.startController("DuelControler");
					player.getDuelConfigurations().getOther(player).duelWins++;
					this.stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean login() {
		return true;
	}

	@Override
	public boolean logout() {
		player.getDuelConfigurations().endDuel(player, false, true);
		player.getDuelConfigurations().endDuel(
				player.getDuelConfigurations().getOther(player), false, false); // other
																				// player
		return false;
	}

	@Override
	public boolean keepCombating(Entity victim) {
		boolean isRanging = PlayerCombat.isRanging(player) != 0;
		if (player.getTemporaryAttributtes().get("canFight") == Boolean.FALSE) {
			player.packets().sendMessage("The duel hasn't started yet.",
					true);
			return false;
		}
		if (player.getDuelConfigurations().getOther(player) != victim)
			return false;
		if (player.getCombatDefinitions().getSpellId() > 0
				&& player.getDuelConfigurations().getRule(2)) {
			player.packets().sendMessage(
					"You cannot use Magic in this duel!", true);
			return false;
		} else if (isRanging && player.getDuelConfigurations().getRule(0)) {
			player.packets().sendMessage(
					"You cannot use Range in this duel!", true);
			return false;
		} else if (!isRanging && player.getDuelConfigurations().getRule(1)
				&& player.getCombatDefinitions().getSpellId() <= 0) {
			player.packets().sendMessage(
					"You cannot use Melee in this duel!", true);
			return false;
		} else {
			for (Item item : FUN_WEAPONS) {
				if (player.getDuelConfigurations().getRule(4)
						&& !player.getInventory().containsItem(item.getId(),
								item.getAmount())) {
					player.packets().sendMessage(
							"You can only use fun weapons in this duel!");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean canEquip(int slotId, int itemId) {
		if (player.getDuelConfigurations().getRule(10 + slotId)) {
			player.packets().sendMessage(
					"You can't equip "
							+ ItemDefinition.get(itemId)
									.getName().toLowerCase()
							+ " during this duel.");
			return false;
		}
		if (slotId == 3 && Equipment.isTwoHandedWeapon(new Item(itemId))
				&& player.getDuelConfigurations().getRule(15)) {
			player.packets().sendMessage(
					"You can't equip "
							+ ItemDefinition.get(itemId)
									.getName().toLowerCase()
							+ " during this duel.");
			return false;
		}
		return true;
	}
}
