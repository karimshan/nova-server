package org.nova.game.player;

import java.util.HashMap;
import java.util.Map;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.utility.misc.Misc;

public class Pets extends NPC {
	/**
	 * Objects
	 */
	private static final long serialVersionUID = 1325133366821220445L;
	private transient Player owner;
	public boolean spawnedPet = false;
	private int npcId;

	public static enum Pet {
		SPARKLES(2267, 22973),
		JAD(3603, 21512);
		private static final Map<Integer, Pet> item = new HashMap<Integer, Pet>();

		static {
			for (Pet itemId : Pet.values()) {
				item.put(itemId.itemId, itemId);
			}
		}

		public static Pet forId(int id) {
			return item.get(id);
		}

		private int npcId;
		private int itemId;

		private Pet(int npcId, int itemId) {
			this.npcId = npcId;
			this.itemId = itemId;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getItemId() {
			return itemId;
		}

		public static Map<Integer, Pet> getItem() {
			return item;
		}
	}

	public Pets(int npcId, Player owner, Location tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(npcId, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setRun(true);
		this.setOwner(owner);
		this.npcId = npcId;
		spawnPet(npcId, owner);
		call(true);
	}

	public void spawnPet(int npcId, Player owner) {
		if (owner.getRealPet() != null) {
			return;
		}
		spawnedPet = true;
		Game.getNPCs().add(this);
		setNextGraphics(new Graphics(defs().size <= 1 ? 1314 : 1315));
		owner.setPetFollow(owner.getIndex());
		owner.setRealPet(this);
	}

	public void processNPC() {
		if (isDead())
			return;
		if (!withinDistance(getOwner(), 4)) {
			call(false);
			return;
		}
		trackTimer++;
		if (trackTimer == 50) {
			trackTimer = 0;
			ticks--;
			if (ticks == 2)
				getOwner().packets().sendMessage(
						"You have 1 minute before your familiar vanishes.");
			else if (ticks == 1)
				getOwner().packets().sendMessage(
						"You have 30 seconds before your familiar vanishes.");
			else if (ticks == 0) {
				dissmissPet(false);
				return;
			}
		}
		sendFollow();
	}

	public void call() {
		if (getAttackedBy() != null
				&& getAttackedByDelay() > System.currentTimeMillis()) {
			getOwner().packets().sendMessage(
					"You cant call your familiar while it under combat.");
			return;
		} else {
			call(false);
			return;
		}
	}

	public void call(boolean login) {
		int size = getSize();
		if (login) {
			checkNearDirs = Misc.getCoordOffsetsNear(size);
		} else {
			removeTarget();
		}
		Location teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			Location tile = new Location(new Location(getOwner().getX()
					+ checkNearDirs[0][dir], getOwner().getY()
					+ checkNearDirs[1][dir], getOwner().getZ()));
			if (!Game.canMoveNPC(tile.getZ(), tile.getX(), tile.getY(),
					size))
				continue;
			teleTile = tile;
			break;
		}

		if (login || teleTile != null)
			WorldTasksManager.schedule(new WorldTask() {

				public void run() {
					setNextGraphics(new Graphics(
							defs().size <= 1 ? 1314 : 1315));
				}

			});
		if (teleTile == null) {
			if (!sentRequestMoveMessage) {
				getOwner().packets().sendMessage(
						"Theres not enough space for your familiar appear.");
				sentRequestMoveMessage = true;
			}
			return;
		} else {
			sentRequestMoveMessage = false;
			setLocation(teleTile);
			return;
		}
	}

	private void sendFollow() {
		if (getLastFaceEntity() != getOwner().getClientIndex())
			setNextFaceEntity(getOwner());
		if (getFreezeDelay() >= System.currentTimeMillis())
			return;
		int size = getSize();
		int distanceX = getOwner().getX() - getX();
		int distanceY = getOwner().getY() - getY();
		if (distanceX < size && distanceX > -1 && distanceY < size
				&& distanceY > -1 && !getOwner().hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(getOwner().getX() + 1, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(getOwner().getX() - size - 1, getY())) {
					resetWalkSteps();
					if (!addWalkSteps(getOwner().getX(), getY() + 1)) {
						resetWalkSteps();
						if (!addWalkSteps(getOwner().getX(), getY() - size - 1))
							return;
					}
				}
			}
			return;
		}
		if (!clipedProjectile(getOwner(), true) || distanceX > size
				|| distanceX < -1 || distanceY > size || distanceY < -1) {
			resetWalkSteps();
			addWalkStepsInteract(getOwner().getX(), getOwner().getY(), 2, size, true);
			return;
		} else {
			resetWalkSteps();
			return;
		}
	}

	public void respawnFamiliar(Player owner) {
		this.setOwner(owner);
		initEntity();
		deserialize();
		owner.setPetFollow(owner.getIndex());
		call(true);
	}

	public void dissmissPet(boolean loggedOut) {
		if (!loggedOut) {
			getOwner().getInventory().addItem(getOwner().getPetId(), 1);
			getOwner().setPetId(0);
			getOwner().setRealPet(null);
			finish();
		}
		finish();
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	private int ticks;
	private int trackTimer;
	private transient boolean sentRequestMoveMessage;
	private transient int checkNearDirs[][];
}