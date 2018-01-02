package org.nova.kshan.content.pets;

import java.io.Serializable;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * Personal pets.
 * 
 * @author K-Shan <infamous575@yahoo.com> <skype>k-shan904</skype>
 * 
 */
public class PetManager extends NPC implements Serializable {

	/**
	 * familiar id
	 */
	public int familiarId = -1;

	/**
	 * the owner.
	 */
	public Player owner = null;

	/**
	 * @serial
	 */
	private static final long serialVersionUID = -5061899652357310565L;

	/**
	 * instance of npc
	 * 
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 */
	public PetManager(int famId, Player owner) {
		super(famId, new Location(owner.getX() + 1, owner.getY() + 1, owner.getZ()), -1, true, false);
		this.owner = owner;
		this.familiarId = famId;
		sendFollow();
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		sendFollow();
		if(!withinDistance(owner, 8))
			call(true);
	}

	/**
	 * dismissing the familiar
	 * 
	 * @param logged
	 */
	public void dismiss(boolean logged) {
		if (!logged) {
			owner.packets().sendConfig(1174, -1);
			owner.setPet(null);
			owner.packets().closeInterface(owner.interfaces().fullScreen() ? 37 : 158);
			owner.packets().sendIComponentSettings(747, 16, 0, 0, 0);
			owner.packets().sendRunScript(2471);
			owner.packets().sendRunScript(655);
		}
		finish();
	}

	/**
	 * returning if dead or not.
	 */
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
				} else if (loop >= defs.getDeathDelay()) {
					dismiss(false);
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

	/**
	 * calling the familiar.
	 */
	public void call() {
		if (getAttackedBy() != null && getAttackedByDelay() > Misc.currentTimeMillis()) {
			return;
		}
		setNextFaceEntity(owner);
		call(false);
	}

	/**
	 * the area around.
	 */
	private transient int[][] checkNearDirs;

	/**
	 * requesting move.
	 */
	private transient boolean sentRequestMoveMessage;

	/**
	 * calling the familiar.
	 * 
	 * @param login
	 */
	public void call(boolean login) {
		int size = getSize();
		if (login) {
			checkNearDirs = Misc.getCoordOffsetsNear(size);
		} else
			removeTarget();
		Location teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			final Location tile = new Location(new Location(owner.getX() + checkNearDirs[0][dir], owner.getY() + checkNearDirs[1][dir], owner.getZ()));
			if (Game.canMoveNPC(tile.getZ(), tile.getX(), tile.getY(), size)) {
				teleTile = tile;
				break;
			}
		}
		if (login || teleTile != null)
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					setNextGraphics(new Graphics(189));
				}
			}, 1);
		if (teleTile == null) {
			if (!sentRequestMoveMessage) {
				sentRequestMoveMessage = true;
			}
			return;
		}
		sentRequestMoveMessage = false;
		setLocation(teleTile);
	}

	/**
	 * 
	 * @param target
	 * @return
	 */
	public boolean canAttack(Entity target) {
		if (target instanceof Player) {
			Player player = (Player) target;
			if (!owner.isCanPvp() || !player.isCanPvp())
				return false;
		}
		return !target.isDead() && owner.isAtMultiArea() && isAtMultiArea() && target.isAtMultiArea() && owner.getControllerManager().canAttack(target);
	}

	/**
	 * following the player.
	 */
	protected void sendFollow() {
		setNextFaceEntity(owner);
		if (getFreezeDelay() >= Misc.currentTimeMillis())
			return;
		int size = getSize();

		int distanceX = owner.getX() - getX();
		int distanceY = owner.getY() - getY();
		if (distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1 && !owner.hasWalkSteps()) {
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
		if ((!clipedProjectile(owner, true)) || distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
			resetWalkSteps();
			addWalkStepsInteract(owner.getX(), owner.getY(), 2, size, true);
			return;
		} else
			resetWalkSteps();

	}

	/**
	 * 
	 * @return
	 */
	public int getFamiliarId() {
		return familiarId;
	}

	/**
	 * 
	 * @return
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.familiarId = id;
	}

	/**
	 * 
	 * @param owner
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}

}
