package org.nova.game.player.content.minigames.clanwars;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.game.player.actions.PlayerCombat;
import org.nova.game.player.content.Foods.Food;
import org.nova.game.player.content.Pots.Pot;
import org.nova.game.player.content.minigames.clanwars.ClanWars.Rules;
import org.nova.game.player.controlers.Controller;

/**
 * A controler subclass handling players in the clan wars activity.
 * @author Emperor
 *
 */
public final class WarControler extends Controller {

	/**
	 * The clan wars instance.
	 */
	private transient ClanWars clanWars;
	
	/**
	 * Constructs a new {@code WarControler} {@code Object}.
	 */
	public WarControler() {
		/*
		 * empty.
		 */
	}
	
	@Override
	public void start() {
		this.clanWars = (ClanWars) super.getArguments()[0];
		player.setCanPvp(true);
		player.packets().sendPlayerOption("Attack", 1, true);
		moved();
	}
	
	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.packets().sendMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					if (clanWars.get(Rules.ITEMS_LOST)) {
						Player killer = player.getMostDamageReceivedSourcePlayer();
						if (killer != null) {
							killer.removeDamage(player);
							player.sendItemsOnDeath(killer);
						}
						player.getEquipment().init();
						player.getInventory().init();
					}
					player.reset();
					if (clanWars.getFirstTeam() == player.getCurrentFriendChat()) {
						player.setLocation(clanWars.getBaseLocation().transform(clanWars.getAreaType().getFirstDeathOffsetX(), clanWars.getAreaType().getFirstDeathOffsetY(), 0));
						clanWars.getFirstPlayers().remove(player);
						clanWars.getFirstViewers().add(player);
						int firstKills = clanWars.getKills() & 0xFFFF;
						int secondKills = (clanWars.getKills() >> 24 & 0xFFFF) + 1;
						clanWars.setKills(firstKills | (secondKills << 24));
					} else {
						Location northEast = clanWars.getBaseLocation().transform(clanWars.getAreaType().getNorthEastTile().getX() - clanWars.getAreaType().getSouthWestTile().getX(), 
								clanWars.getAreaType().getNorthEastTile().getY() - clanWars.getAreaType().getSouthWestTile().getY(), 0);
						player.setLocation(northEast.transform(clanWars.getAreaType().getSecondDeathOffsetX(), clanWars.getAreaType().getSecondDeathOffsetY(), 0));
						clanWars.getSecondPlayers().remove(player);
						clanWars.getSecondViewers().add(player);
						int firstKills = (clanWars.getKills() & 0xFFFF) + 1;
						int secondKills = clanWars.getKills() >> 24 & 0xFFFF;
						clanWars.setKills(firstKills | (secondKills << 24));
					}
					clanWars.updateWar();
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.packets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}
	
	@Override
	public boolean canEat(Food food) {
		if (clanWars.get(Rules.NO_FOOD)) {
			player.packets().sendMessage("Food has been disabled during this war.");
			return false;
		}
		return true;
	}

	@Override
	public boolean canPot(Pot pot) {
		if (clanWars.get(Rules.NO_POTIONS)) {
			player.packets().sendMessage("Potions has been disabled during this war.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean processMagicTeleport(Location toTile) {
		player.packets().sendMessage("You can't teleport out of a clan war!");
		return false;
	}

	@Override
	public boolean processItemTeleport(Location toTile) {
		player.packets().sendMessage("You can't teleport out of a clan war!");
		return false;
	}
	
	@Override
	public boolean processObjectClick1(GlobalObject object) {
		switch (object.getId()) {
		case 38697:
		case 28140:
			clanWars.getFirstViewers().remove(player);
			clanWars.getSecondViewers().remove(player);
		case 38696:
		case 38695:
		case 28139:
		case 38694:
		case 28214:
			clanWars.leave(player, true);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canAttack(Entity target) {
		if (!clanWars.getFirstPlayers().contains(player) && !clanWars.getSecondPlayers().contains(player)) {
			return false;
		}
		if (clanWars.getFirstPlayers().contains(player) && clanWars.getFirstPlayers().contains(target)) {
			player.packets().sendMessage("You can't attack players in your own team.");
			return false;
		}
		if (clanWars.getSecondPlayers().contains(player) && clanWars.getSecondPlayers().contains(target)) {
			player.packets().sendMessage("You can't attack players in your own team.");
			return false;
		}
		if (!clanWars.getTimer().isStarted()) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean keepCombating(Entity victim) {
		boolean isRanging = PlayerCombat.isRanging(player) != 0;
		if (player.getCombatDefinitions().getSpellId() > 0) {
			switch (clanWars.getMagicRuleCount()) {
			case 1: //Standard spells only.
				if (player.getCombatDefinitions().getSpellBook() != 0) {
					player.packets().sendMessage("You can only use modern spells during this war!");
					return false;
				}
				break;
			case 2: //Bind/Snare/Entangle only.
				if (player.getCombatDefinitions().getSpellBook() != 0) {
					player.packets().sendMessage("You can only use binding spells during this war!");
					return false;
				}
				switch (player.getCombatDefinitions().getSpellId()) {
				case 36:
				case 55:
				case 81:
					break;
				default:
					player.packets().sendMessage("You can only use binding spells during this war!");
					return false;
				}
				break;
			case 3: //No magic at all.
				player.packets().sendMessage("Magic combat is not allowed during this war!");
				return false;
			}
		}
		if (isRanging && clanWars.get(Rules.NO_RANGE)) {
			player.packets().sendMessage("Ranged combat is not allowed during this war!");
			return false;
		}
		if (!isRanging && clanWars.get(Rules.NO_MELEE) && player.getCombatDefinitions().getSpellId() <= 0) {
			player.packets().sendMessage("Melee combat is not allowed during this war!");
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void moved() {
		switch (clanWars.getAreaType()) {
		case PLATEAU:
		case TURRETS:
			player.setForceMultiArea(true);
			break;
		case FORSAKEN_QUARRY:
			Location northEast = clanWars.getBaseLocation().transform(clanWars.getAreaType().getNorthEastTile().getX() - clanWars.getAreaType().getSouthWestTile().getX(), 
				clanWars.getAreaType().getNorthEastTile().getY() - clanWars.getAreaType().getSouthWestTile().getY(), 0)
				.transform(-16, -16, 0);
			Location southWest = clanWars.getBaseLocation().transform(16, 16, 0);
			player.setForceMultiArea(player.getX() >= southWest.getX() && player.getY() >= southWest.getY() && player.getX() <= northEast.getX() && player.getY() <= northEast.getY());
			break;
		}
	}
	
	@Override
	public void forceClose() {
		player.setCanPvp(false);
		player.packets().sendPlayerOption("null", 1, true);
	}
	
	@Override
	public boolean logout() {
		player.setLocation(new Location(2992, 9676, 0));
		return true;
	}

}