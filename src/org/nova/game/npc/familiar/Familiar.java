package org.nova.game.npc.familiar;

import java.io.Serializable;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;
import org.nova.game.player.actions.Summoning.Pouches;
import org.nova.utility.misc.Misc;


public abstract class Familiar extends NPC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3255206534594320406L;

	private transient Player owner;
	private int ticks;
	private int trackTimer;
	private int specialEnergy;
	private boolean trackDrain;

	private BeastOfBurden bob;
	private Pouches pouch;

	public Familiar(Player owner, Pouches pouch, Location tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(pouch.getNpcId(), tile, mapAreaNameHash,
				canBeAttackFromOutOfArea, false);
		setRun(true);
		this.owner = owner;
		this.pouch = pouch;
		resetTickets();
		specialEnergy = 60;
		if (getBOBSize() > 0)
			bob = new BeastOfBurden(getBOBSize());
		call(true);
	}

	public void store() {
		if (bob == null)
			return;
		bob.open();
	}

	public boolean canStoreEssOnly() {
		return pouch.getNpcId() == 6818;
	}

	public int getOriginalId() {
		return pouch.getNpcId();
	}

	public void resetTickets() {
		ticks = (int) (pouch.getTime() / 1000 / 30);
		trackTimer = 0;
	}

	private void sendFollow() {
		if (getLastFaceEntity() != owner.getClientIndex())
			setNextFaceEntity(owner);
		if (getFreezeDelay() >= Misc.currentTimeMillis())
			return; // if freeze cant move ofc
		int size = getSize();

		int distanceX = owner.getX() - getX();
		int distanceY = owner.getY() - getY();
		// if is under
		if (distanceX < size && distanceX > -1 && distanceY < size
				&& distanceY > -1 && !owner.hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(owner.getX() + 1, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(owner.getX() - size - 1, getY())) {
					resetWalkSteps();
					if (!addWalkSteps(owner.getX(), getY() + 1)) {
						resetWalkSteps();
						if (!addWalkSteps(owner.getX(), getY() - size - 1)) {
							return;
						}
					}
				}
			}
			return;
		}
		if ((!clipedProjectile(owner, true)) || distanceX > size
				|| distanceX < -1 || distanceY > size || distanceY < -1) {
			resetWalkSteps();
			addWalkStepsInteract(owner.getX(), owner.getY(), 2, size, true);
			return;
		} else
			resetWalkSteps();

	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		unlockOrb();
		trackTimer++;
		if (trackTimer == 50) {
			trackTimer = 0;
			ticks--;
			if (trackDrain)
				owner.getSkills().drainSummoning(1);
			trackDrain = !trackDrain;
			if (ticks == 2)
				owner.packets().sendMessage(
						"You have 1 minute before your familiar vanishes.");
			else if (ticks == 1)
				owner.packets().sendMessage(
						"You have 30 seconds before your familiar vanishes.");
			else if (ticks == 0) {
				dissmissFamiliar(false);
				return;
			}
			sendTimeRemaining();
		}
		if (owner.isCanPvp() && getId() != pouch.getNpcId()) {
			transformIntoNPC(pouch.getNpcId());
			call(false);
			return;
		} else if (!owner.isCanPvp() && getId() == pouch.getNpcId()) {
			transformIntoNPC(pouch.getNpcId() - 1);
			call(false);
			return;
		} else if (!withinDistance(owner, 12)) {
			call(false);
			return;
		}
		if (!getCombat().process()) {
			if (isAgressive() && owner.getAttackedBy() != null
					&& owner.getAttackedByDelay() > Misc.currentTimeMillis()
					&& canAttack(owner.getAttackedBy())
					&& Misc.getRandom(25) == 0)
				getCombat().setTarget(owner.getAttackedBy());
			else
				sendFollow();
		}
	}

	public boolean canAttack(Entity target) {
		if (target instanceof Player) {
			Player player = (Player) target;
			if (!owner.isCanPvp() || !player.isCanPvp())
				return false;
		}
		return !target.isDead() && owner.isAtMultiArea() && isAtMultiArea()
				&& target.isAtMultiArea()
				&& owner.getControllerManager().canAttack(target);
	}

	public boolean renewFamiliar() {
		if (ticks > 5) {
			owner.packets()
					.sendMessage(
							"You need to have at least two minutes and fifty seconds remaining before you can renew your familiar.",
							true);
			return false;
		} else if (!owner.getInventory().getItems()
				.contains(new Item(pouch.getPouchId(), 1))) {
			owner.packets().sendMessage(
					"You need a "
							+ ItemDefinition
									.get(pouch.getPouchId())
									.getName().toLowerCase()
							+ " to renew your familiar's timer.");
			return false;
		}
		resetTickets();
		owner.getInventory().deleteItem(pouch.getPouchId(), 1);
		call(true);
		owner.packets().sendMessage(
				"You use your remaining pouch to renew your familiar.");
		return true;
	}

	public void takeBob() {
		if (bob == null)
			return;
		bob.takeBob();
	}

	public void sendTimeRemaining() {
		owner.packets().sendConfig(1176, ticks * 65);
	}

	public void sendMainConfigs() {
		switchOrb(true);
		owner.packets().sendConfig(448, pouch.getPouchId());// configures
																// familiar type
																// based on
																// pouch?
		owner.packets().sendConfig(1160, 243269632); // sets npc emote
		refreshSpecialEnergy();
		sendTimeRemaining();
		owner.packets().sendConfig(1175, getSpecialAmount() << 23);// check
		owner.packets().sendGlobalString(204, getSpecialName());
		owner.packets().sendGlobalString(205, getSpecialDescription());
		owner.packets().sendGlobalConfig(1436,
				getSpecialAttack() == SpecialAttack.CLICK ? 1 : 0);
		unlockOrb(); // temporary
	}

	public void sendFollowerDetails() {
		boolean res = owner.interfaces().isFullScreen();
		owner.packets().sendInterface(true, res ? 746 : 548, res ? 98 : 212,
				662);
		owner.packets().sendHideIComponent(662, 44, true);
		owner.packets().sendHideIComponent(662, 45, true);
		owner.packets().sendHideIComponent(662, 46, true);
		owner.packets().sendHideIComponent(662, 47, true);
		owner.packets().sendHideIComponent(662, 48, true);
		owner.packets().sendHideIComponent(662, 71, false);
		owner.packets().sendHideIComponent(662, 72, false);
		unlock();
		owner.packets().sendGlobalConfig(168, 8);// tab id
	}

	public void switchOrb(boolean on) {
		owner.packets().sendConfig(1174, on ? -1 : 0);
		if (on)
			unlock();
		else
			lockOrb();
	}

	public void unlockOrb() {
		owner.packets().sendHideIComponent(747, 8, false);
		sendLeftClickOption(owner);
	}

	public static void selectLeftOption(Player player) {
		boolean res = player.interfaces().isFullScreen();
		player.packets().sendInterface(true, res ? 746 : 548,
				res ? 98 : 212, 880);
		sendLeftClickOption(player);
		player.packets().sendGlobalConfig(168, 8);// tab id
	}

	public static void confirmLeftOption(Player player) {
		player.packets().sendGlobalConfig(168, 4);// inv tab id
		boolean res = player.interfaces().isFullScreen();
		player.packets().closeInterface(res ? 98 : 212);
	}

	public static void setLeftclickOption(Player player,
			int summoningLeftClickOption) {
		if (summoningLeftClickOption == player.getSummoningLeftClickOption())
			return;
		player.setSummoningLeftClickOption(summoningLeftClickOption);
		sendLeftClickOption(player);
	}

	public static void sendLeftClickOption(Player player) {
		player.packets().sendConfig(1493,
				player.getSummoningLeftClickOption());
		player.packets().sendConfig(1494,
				player.getSummoningLeftClickOption());
	}

	public void unlock() {
		switch (getSpecialAttack()) {
		case CLICK:
			owner.packets().sendIComponentSettings(747, 17, 0, 0, 2);
			owner.packets().sendIComponentSettings(662, 74, 0, 0, 2);
			break;
		case ENTITY:
			owner.packets().sendIComponentSettings(747, 17, 0, 0, 20480);
			owner.packets().sendIComponentSettings(662, 74, 0, 0, 20480);
			break;
		case OBJECT:
		case ITEM:
			owner.packets().sendIComponentSettings(747, 17, 0, 0, 65536);
			owner.packets().sendIComponentSettings(662, 74, 0, 0, 65536);
			break;
		}
		owner.packets().sendHideIComponent(747, 8, false);
	}

	public void lockOrb() {
		owner.packets().sendHideIComponent(747, 8, true);
	}

	private transient int[][] checkNearDirs;
	private transient boolean sentRequestMoveMessage;

	public void call() {
		if (getAttackedBy() != null
				&& getAttackedByDelay() > Misc.currentTimeMillis()) {
			// TODO or something as this
			owner.packets().sendMessage(
					"You cant call your familiar while it under combat.");
			return;
		}
		call(false);
	}

	public void call(boolean login) {
		int size = getSize();
		if (login) {
			if (bob != null)
				bob.setEntitys(owner, this);
			checkNearDirs = Misc.getCoordOffsetsNear(size);
			sendMainConfigs();
		} else
			removeTarget();
		Location teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			final Location tile = new Location(new Location(owner.getX()
					+ checkNearDirs[0][dir], owner.getY()
					+ checkNearDirs[1][dir], owner.getZ()));
			if (Game.canMoveNPC(tile.getZ(), tile.getX(), tile.getY(),
					size)) { // if found done
				teleTile = tile;
				break;
			}
		}
		if (login || teleTile != null)
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					setNextGraphics(new Graphics(
							defs().size > 1 ? 1315 : 1314));
				}
			});
		if (teleTile == null) {
			if (!sentRequestMoveMessage) {
				owner.packets().sendMessage(
						"Theres not enough space for your familiar appear.");
				sentRequestMoveMessage = true;
			}
			return;
		}
		sentRequestMoveMessage = false;
		setLocation(teleTile);
	}

	public void dissmissFamiliar(boolean logged) {
		if (!logged) {
			owner.setFamiliar(null);
			switchOrb(false);
			owner.packets()
					.closeInterface(
							owner.interfaces().isFullScreen() ? 98
									: 212);
			owner.packets().sendIComponentSettings(747, 17, 0, 0, 0);
			//if (bob != null)
			//	bob.dropBob(); // can cause a dup method?
		}
		finish();
	}

	private transient boolean dead;

	@Override
	public void sendDeath(Entity source) {
		if (dead)
			return;
		dead = true;
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		setCantInteract(true);
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
					owner.packets().sendMessage(
							"Your familiar slowly begins to fade away..");
				} else if (loop >= defs.getDeathDelay()) {
					dissmissFamiliar(false);
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	public void respawnFamiliar(Player owner) {
		this.owner = owner;
		initEntity();
		deserialize();
		call(true);
	}

	public abstract String getSpecialName();

	public abstract String getSpecialDescription();

	public abstract int getBOBSize();

	public abstract int getSpecialAmount();

	public abstract SpecialAttack getSpecialAttack();

	public abstract boolean submitSpecial(Object object);

	public boolean isAgressive() {
		return true;
	}

	public static enum SpecialAttack {
		ITEM, ENTITY, CLICK, OBJECT
	}

	public BeastOfBurden getBob() {
		return bob;
	}

	public void refreshSpecialEnergy() {
		owner.packets().sendConfig(1177, specialEnergy);
	}

	public void restoreSpecialAttack(int energy) {
		if (specialEnergy >= 60)
			return;
		specialEnergy = energy + specialEnergy >= 60 ? 60 : specialEnergy
				+ energy;
		refreshSpecialEnergy();
	}

	public void setSpecial(boolean on) {
		if (!on)
			owner.getTemporaryAttributtes().remove("FamiliarSpec");
		else {
			if (specialEnergy < getSpecialAmount()) {
				owner.packets().sendMessage(
						"You familiar doesn't have enough special energy.");
				return;
			}
			owner.getTemporaryAttributtes().put("FamiliarSpec", Boolean.TRUE);
		}
	}

	public void drainSpecial(int specialReduction) {
		specialEnergy -= specialReduction;
		if (specialEnergy < 0) {
			specialEnergy = 0;
		}
		refreshSpecialEnergy();
	}

	public void drainSpecial() {
		specialEnergy -= getSpecialAmount();
		refreshSpecialEnergy();
	}

	public boolean hasSpecialOn() {
		if (owner.getTemporaryAttributtes().remove("FamiliarSpec") != null) {
			if (!owner.getInventory().containsItem(pouch.getScrollId(), 1)) {
				owner.packets().sendMessage(
						"You don't have the scrolls to use this move.");
				return false;
			}
			owner.getInventory().deleteItem(pouch.getScrollId(), 1);
			drainSpecial();
			return true;
		}
		return false;
	}

	public Player getOwner() {
		return owner;
	}
}
