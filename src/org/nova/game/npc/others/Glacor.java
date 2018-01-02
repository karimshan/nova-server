package org.nova.game.npc.others;

import java.util.concurrent.TimeUnit;

import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Hit;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;


@SuppressWarnings("serial")
public final class Glacor extends NPC {

	private boolean[] demonPrayer;
	private int fixedCombatType;
	private int[] cachedDamage;
	private int shieldTimer;
	private int fixedAmount;
	private int prayerTimer;

	public Glacor(int id, Location tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		demonPrayer = new boolean[3];
		cachedDamage = new int[3];
		shieldTimer = 0;
		switchPrayers(0);
		this.setCombatLevel(650);
		this.setForceAgressive(true);
		this.setRandomWalk(true);
	}

	public void switchPrayers(int type) {
		resetPrayerTimer();
	}

	private void resetPrayerTimer() {
		prayerTimer = 16;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead())
			return;
		
		if (getCombat().process()) {// no point in processing
				for (int i = 0; i < cachedDamage.length; i++) {
					if (cachedDamage[i] >= 310) {
						cachedDamage = new int[3];
					}
				}
			}
			for (int i = 0; i < cachedDamage.length; i++) {
				if (cachedDamage[i] >= 310) {
					cachedDamage = new int[3];
				}
			}
		}

	@Override
	public void handleIngoingHit(final Hit hit) {
		int type = 0;
		super.handleIngoingHit(hit);
		if (hit.getSource() instanceof Player) {// Armadyl Battlestaff
			Player player = (Player) hit.getSource();
		}
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		shieldTimer = 0;
		final NPC n = this;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					drop(n);
					reset();
					setCoords(getRespawnTile());
					finish();
					setRespawnTask();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}


	@Override
	public void setRespawnTask() {
		if (!hasFinished()) {
			reset();
			setCoords(getRespawnTile());
			finish();
		}
		final NPC npc = this;
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				setFinished(false);
				Game.addNPC(npc);
				npc.setLastRegionId(0);
				Game.updateEntityRegion(npc);
				loadMapRegions();
				checkMultiArea();
				shieldTimer = 0;
				fixedCombatType = 0;
				fixedAmount = 0;
			}
		}, getCombatDefinitions().getRespawnDelay() * 600,
				TimeUnit.MILLISECONDS);
	} //Your re-spawn time on them.


	public int getFixedCombatType() {
		return fixedCombatType;
	}

	public void setFixedCombatType(int fixedCombatType) {
		this.fixedCombatType = fixedCombatType;
	}

	public int getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(int fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

}