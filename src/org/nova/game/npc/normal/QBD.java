package org.nova.game.npc.normal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.npc.Drop;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;
import org.nova.utility.loading.npcs.NPCDrops;
import org.nova.utility.misc.Misc;


@SuppressWarnings("serial")
public class QBD extends NPC {

	public QBD(int id, Location tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		this.setCantFollowUnderCombat(true);
		this.setForceMultiArea(true);
		this.setForceTargetDistance(10);
		this.setForceMultiAttacked(true);
		this.setAtMultiArea(true);
		this.setRandomWalk(false);
		this.setCapDamage(1050);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = Game.getRegion(regionId).getPlayerIndexes();
			if (playerIndexes != null) {
				for (int npcIndex : playerIndexes) {
					Player player = Game.getPlayers().get(npcIndex);
					if (player == null
							|| player.isDead()
							|| player.hasFinished()
							|| !player.isRunning()
							|| !player.withinDistance(this, 84)
							|| ((!isAtMultiArea() || !player.isAtMultiArea())
									&& player.getAttackedBy() != this && player
									.getAttackedByDelay() > Misc
									.currentTimeMillis())
							|| !clipedProjectile(player, false))
						continue;	possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}

	/*
	 * gotta override else setRespawnTask override doesnt work
	 */
	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		Game.removeNPC(this);
	
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
					Game.sendMessage("NEWS: The QBD has recently been slayed, we have a hero amongst us!");
				} else if (loop >= defs.getDeathDelay()) {
					dropQBD();
					reset();
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	public void dropQBD() {
		try {
			Drop[] drops = NPCDrops.getDrops(getId());
			if (drops == null)
				return;
			Player killer = getMostDamageReceivedSourcePlayer();
			if (killer == null)
				return;
		int [] charms = {12159, 12160, 12158, 12163};
		int li = Misc.random(3);
		Drop dr = new Drop(charms[li],85,11,31,false);
		sendDropQBD(killer,dr);
			Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (Drop drop : drops) {
				if (drop.getRate() == 100)
					sendDropQBD(killer, drop);
				else {
					if ((Misc.getRandomDouble(99) + 1) <= drop.getRate() * 1.5)
						possibleDrops[possibleDropsCount++] = drop;
				}
			}
			if (possibleDropsCount > 0)
		sendDropQBD(killer,
						possibleDrops[Misc.getRandom(possibleDropsCount - 1)]);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}

	public void sendDropQBD(Player player, Drop drop) {
		int size = getSize();
		Game.addGroundItem(new Item(drop.getItemId(), drop.getMinAmount()
				+ Misc.getRandom(drop.getExtraAmount())), new Location(
				3536, 5191, getZ()), player,
				false, 180, false);
	}
	@Override
	public void setRespawnTask() {
		if (!hasFinished()) {
			reset();
			setCoords(getRespawnTile());
			finish();
		}CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					respawn();
				} catch (Exception e) {
					e.printStackTrace();
				} catch (Error e) {
					e.printStackTrace();
				}
			}
		}, getCombatDefinitions().getRespawnDelay() * 600,
				TimeUnit.MILLISECONDS);
	}

	public void respawn() {
		setFinished(false);
		Game.addNPC(this);
		setLastRegionId(0);
		Game.updateEntityRegion(this);
		loadMapRegions();
		this.setAtMultiArea(true);
		Game.sendMessage("NEWS: The QBD has respawned, lets see if it can be killed again!");
	}

}
