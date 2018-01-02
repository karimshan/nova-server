package org.nova.game.player.controlers;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceMovement;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.utility.misc.Misc;

public class Wilderness extends Controller {

	private boolean showingSkull;

	@Override
	public void start() {
		if(!Game.playersInWilderness.contains(player))
			Game.playersInWilderness.add(player);
		checkBoosts(player);
	}

	public static void checkBoosts(Player player) {
		boolean changed = false;
		int level = player.getSkills().getLevelFromXP(Skills.ATTACK);
		int maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.ATTACK)) {
			player.getSkills().set(Skills.ATTACK, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelFromXP(Skills.STRENGTH);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.STRENGTH)) {
			player.getSkills().set(Skills.STRENGTH, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelFromXP(Skills.DEFENCE);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.DEFENCE)) {
			player.getSkills().set(Skills.DEFENCE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelFromXP(Skills.RANGE);
		maxLevel = (int) (level + 5 + (level * 0.1));
		if (maxLevel < player.getSkills().getLevel(Skills.RANGE)) {
			player.getSkills().set(Skills.RANGE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelFromXP(Skills.MAGIC);
		maxLevel = level + 5;
		if (maxLevel < player.getSkills().getLevel(Skills.MAGIC)) {
			player.getSkills().set(Skills.MAGIC, maxLevel);
			changed = true;
		}
		if (changed)
			player.packets().sendMessage(
					"Your extreme potion bonus has been reduced.");
	}

	@Override
	public boolean login() {
		moved();
		if(!Game.playersInWilderness.contains(player))
			Game.playersInWilderness.add(player);
		return false;
	}

	@Override
	public boolean keepCombating(Entity target) {
		if (target instanceof NPC)
			return true;
		if (!canAttack(target))
			return false;
		if (target.getAttackedBy() != player
				&& player.getAttackedBy() != target)
			player.setWildernessSkull();
		if (player.getCombatDefinitions().getSpellId() <= 0 && Misc.inCircle(new Location(3105, 3933, 0), target, 24)) {
			player.packets().sendMessage("You can only use magic in the arena.");
			return false;
		}
		return true;
	}

	@Override
	public boolean canAttack(Entity target) {
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (player.isCanPvp() && !p2.isCanPvp()) {
				player.packets().sendMessage(
						"That player is not in the wilderness.");
				return false;
			}
			if (canHit(target))
				return true;
			return false;
		}
		return true;
	}

	@Override
	public boolean canHit(Entity target) {
		if (target instanceof NPC)
			return true;
		Player p2 = (Player) target;
		if (Math.abs(player.getSkills().getCombatLevel()
				- p2.getSkills().getCombatLevel()) > getWildLevel())
			return false;
		return true;
	}

	@Override
	public boolean processMagicTeleport(Location toTile) {
		if (getWildLevel() > 20) {
			player.packets().sendMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		} else
		if (player.getTeleBlockDelay() > Misc.currentTimeMillis()) {
			player.packets().sendMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		} else
		Game.playersInWilderness.remove(player);
		return true;

	}

	@Override
	public boolean processItemTeleport(Location toTile) {
		if (getWildLevel() > 20) {
			player.packets().sendMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		} else
		if (player.getTeleBlockDelay() > Misc.currentTimeMillis()) {
			player.packets().sendMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		} else
			Game.playersInWilderness.remove(player);
		return true;
	}

	@Override
	public boolean processObjectTeleport(Location toTile) {
		if (player.getTeleBlockDelay() > Misc.currentTimeMillis()) {
			player.packets().sendMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		} else
			Game.playersInWilderness.remove(player);
		return true;
	}

	 public void showSkull()
	    {
	        player.interfaces().sendTab(player.interfaces().isFullScreen() ? 10 : 19, 381);
	    }
	 
	public static boolean isDitch(int id) {
		return id >= 1440 && id <= 1444 || id >= 65076 && id <= 65087;
	}
    public void removeIcon()
    {
        if(showingSkull)
        {
            showingSkull = false;
            player.setCanPvp(false);
            player.packets().closeInterface(player.interfaces().isFullScreen() ? 10 : 19);
            player.getAppearance().generateAppearanceData();
            player.getEquipment().refresh(null);
        }
    }
	@Override
	public boolean processObjectClick1(final GlobalObject object) {
		if (isDitch(object.getId())) {
			player.lock();
			player.setNextAnimation(new Animation(6132));
			final Location toTile = new Location(object.getRotation() == 1 || object.getRotation() == 3 ? object.getX() + 2 : player.getX(),
					object.getRotation() == 0 || object.getRotation() == 2 ? object.getY() - 1 : player.getY(), object.getZ());
			
			player.setNextForceMovement(new ForceMovement(
					new Location(player), 1, toTile, 2, object.getRotation() == 0 || object.getRotation() == 2 ? ForceMovement.SOUTH : ForceMovement.EAST));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setLocation(toTile);
					player.faceObject(object);
					removeIcon();
					removeControler();
					if(Game.playersInWilderness.contains(player))
						Game.playersInWilderness.remove(player);
					player.resetReceivedDamage();
					player.unlock();
				}
			}, 2);
			return false;
		} else if (object.getId() == 2557 || object.getId() == 65717) {
			player.packets().sendMessage("It seems it is locked, maybe you should try something else.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean processObjectClick2(final GlobalObject object) {
		if (object.getId() == 2557 || object.getId() == 65717) {
			return false;
		}
		return true;
	}

	@Override
	public void sendInterfaces() {
		if (isAtWild(player))
			showSkull();
	}

	@Override
	public boolean sendDeath() {
		
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 3) {
					player.packets().sendMessage("Oh dear, you have died.");
					Player killer = player.getMostDamageReceivedSourcePlayer();
					if (killer != null) {
						killer.removeDamage(player);
						player.sendItemsOnDeath(killer);
						if (!killer.getSession().getIP().equals(player.getSession().getIP())) {
							killer.setPkPoints(killer.getPkPoints() + 1);
							killer.setKills(killer.getKills() + 1);
							killer.setStreak(killer.getStreak() + 1);
							killer.sm("You received a pk point for killing "+player.getDisplayName()+"! You now have "+killer.getPkPoints()+".");
						} else
							killer.sm("You will not receive rewards for killing somebody on your own IP.");
					}
					player.setStreak(0);
					player.setDeaths(player.getDeaths() + 1);
					player.getEquipment().init();
					player.getInventory().init();
					player.reset();
					player.setLocation(new Location(3086, 3494, 0));
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					removeIcon();
					removeControler();
					if(Game.playersInWilderness.contains(player))
						Game.playersInWilderness.remove(player);
					player.packets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false; 
	}

	@Override
	public void moved() {
		boolean isAtWild = isAtWild(player);
		boolean isAtWildSafe = isAtWildSafe();
		if (!showingSkull && isAtWild && !isAtWildSafe) {
			showingSkull = true;
			player.setCanPvp(true);
			showSkull();
			player.getAppearance().generateAppearanceData();
		} else if (showingSkull && (isAtWildSafe || !isAtWild)) {
			removeIcon();
		} else if (!isAtWildSafe && !isAtWild) {
			player.setCanPvp(false);
			removeIcon();
			removeControler();
			
		} else if (Kalaboss.isAtKalaboss(player)) {
			removeIcon();
			player.setCanPvp(false);
			removeControler();
			
			player.getControllerManager().startController("Kalaboss");
		}
	}

	@Override
	public boolean logout() {
		if(Game.playersInWilderness.contains(player))
			Game.playersInWilderness.remove(player);
		return false; // so doesnt remove script
	}

	@Override
	public void forceClose() {
		if(Game.playersInWilderness.contains(player))
			Game.playersInWilderness.remove(player);
		removeIcon();
	}

	public static final boolean isAtWild(Location tile) {//TODO fix this
		return (tile.getX() >= 3011 && tile.getX() <= 3132
				&& tile.getY() >= 10052 && tile.getY() <= 10175) //fortihrny dungeon
				|| 	(tile.getX() >= 2940 && tile.getX() <= 3395
				&& tile.getY() >= 3525 && tile.getY() <= 4000)
				|| (tile.getX() >= 3264 && tile.getX() <= 3279
						&& tile.getY() >= 3279 && tile.getY() <= 3672)
				|| (tile.getX() >= 2756 && tile.getX() <= 2875
						&& tile.getY() >= 5512 && tile.getY() <= 5627)
				|| (tile.getX() >= 3158 && tile.getX() <= 3181
						&& tile.getY() >= 3679 && tile.getY() <= 3697)
				|| (tile.getX() >= 3280 && tile.getX() <= 3183
						&& tile.getY() >= 3885 && tile.getY() <= 3888) || (tile.getX() >= 3012 && tile.getX() <= 3059
						&& tile.getY() >= 10303 && tile.getY() <= 10351);
	}

	public boolean isAtWildSafe() {
		return (player.getX() >= 2940 && player.getX() <= 3395
				&& player.getY() <= 3524 && player.getY() >= 3523);
	}

	public int getWildLevel() {
		if(player.getY() > 9900)
			return (player.getY() - 9912) / 8 + 1;
		return (player.getY() - 3520) / 8 + 1;
	}

}
