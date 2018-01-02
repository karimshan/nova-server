package org.nova.game.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.nova.Constants;
import org.nova.cache.definition.ObjectDefinition;
import org.nova.cache.loaders.AnimationDefinition;
import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Directions;
import org.nova.game.map.DynamicRegion;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceMovement;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.NPC;
import org.nova.game.npc.familiar.Familiar;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.Poison;
import org.nova.kshan.content.bodyglow.BodyGlow;
import org.nova.utility.misc.Misc;

public abstract class Entity extends Location {

	private static final long serialVersionUID = -3372926325008880753L;

	private transient int index;
	private transient int lastRegionId;
	private transient Location lastLoadedMapRegionTile;
	private transient CopyOnWriteArrayList<Integer> mapRegionsIds;
	private transient int direction;
	private transient Location lastLocation;
	private transient Location nextLocation;
	private transient int nextWalkDirection;
	private transient int nextRunDirection;
	private transient Location nextFaceLocation;
	private transient boolean teleported;
	private transient ConcurrentLinkedQueue<int[]> walkSteps;
	private transient ConcurrentLinkedQueue<Hit> receivedHits;
	private transient ConcurrentHashMap<Entity, Integer> receivedDamage;
	private transient boolean finished;
	private transient long freezeDelay;
	private transient boolean isUnderAttack;

	//private transient HoverMessage hoverMessage;
	private transient BodyGlow bodyGlow;
	private transient Animation nextAnimation;
	private transient Graphics nextGraphics1;
	private transient Graphics nextGraphics2;
	private transient Graphics nextGraphics3;
	private transient Graphics nextGraphics4;
	private transient ArrayList<Hit> nextHits;
	private transient ForceMovement nextForceMovement;
	private transient ForceTalk nextForceTalk;
	private transient int nextFaceEntity;
	private transient int lastFaceEntity;
	private transient Entity attackedBy;
	private transient long attackedByDelay;
	private transient boolean multiArea;
	private transient boolean isAtDynamicRegion;
	private transient long lastAnimationEnd;
	private transient boolean forceMultiArea;
	private transient long frozenBlocked;
	private transient long findTargetDelay;
	private transient ConcurrentHashMap<Object, Object> temporaryAttributes;

	private int hitpoints;
	private int mapSize;
	private boolean run;
	private Poison poison;

	public Entity(Location tile) {
		super(tile);
		poison = new Poison();
	}

	public boolean isFamiliar() {
		return this instanceof Familiar;
	}

	public boolean inArea(int a, int b, int c, int d) {
		return getX() >= a && getY() >= b && getX() <= c && getY() <= d;
	}

	public final void initEntity() {
		mapRegionsIds = new CopyOnWriteArrayList<Integer>();
		walkSteps = new ConcurrentLinkedQueue<int[]>();
		receivedHits = new ConcurrentLinkedQueue<Hit>();
		receivedDamage = new ConcurrentHashMap<Entity, Integer>();
		temporaryAttributes = new ConcurrentHashMap<Object, Object>();
		nextHits = new ArrayList<Hit>();
		nextWalkDirection = nextRunDirection - 1;
		lastFaceEntity = -1;
		nextFaceEntity = -2;
		poison.setEntity(this);
	}

	public int getClientIndex() {
		return index + (this instanceof Player ? 32768 : 0);
	}

	public void applyHit(Hit hit) {
		if (isDead())
			return;
		receivedHits.add(hit);
		handleIngoingHit(hit);
	}

	public abstract void handleIngoingHit(Hit hit);

	public void reset() {
		setHitpoints(getMaxHitpoints());
		receivedHits.clear();
		resetCombat();
		walkSteps.clear();
		poison.reset();
		resetReceivedDamage();
		temporaryAttributes.clear();
	}

	public void move(Location nextLocation) {
		this.nextLocation = nextLocation;
	}

	public void resetCombat() {
		attackedBy = null;
		attackedByDelay = 0;
		freezeDelay = 0;
	}

	public void processReceivedHits() {
		if (this instanceof Player) {
			if (((Player) this).getEmotesManager().getNextEmoteEnd() >= Misc
					.currentTimeMillis())
				return;
		}
		Hit hit;
		int count = 0;
		while ((hit = receivedHits.poll()) != null && count++ < 10)
			processHit(hit);
	}

	private void processHit(Hit hit) {
		if (isDead())
			return;
		removeHitpoints(hit);
		nextHits.add(hit);
	}

	public void removeHitpoints(Hit hit) {
		if (isDead() || hit.getLook() == HitLook.ABSORB_DAMAGE)
			return;
		if (hit.getLook() == HitLook.HEALED_DAMAGE) {
			heal(hit.getDamage());
			return;
		}
		if (hit.getDamage() > hitpoints)
			hit.setDamage(hitpoints);
		addReceivedDamage(hit.getSource(), hit.getDamage());
		hitpoints -= hit.getDamage();
		if (hitpoints <= 0)
			sendDeath(hit.getSource());
		else if (this instanceof Player) {
			Player player = (Player) this;
			if (player.getEquipment().getRingId() == 2550) {
				if (hit.getSource() != null && hit.getSource() != player)
					hit.getSource().applyHit(
							new Hit(player, (int) (hit.getDamage() * 0.1),
									HitLook.REFLECTED_DAMAGE));
			}
			if (player.getPrayer().hasPrayersOn()) {
				if ((hitpoints < player.getMaxHitpoints() * 0.1)
						&& player.getPrayer().usingPrayer(0, 23)) {
					setNextGraphics(new Graphics(436));
					hitpoints += player.getSkills().getLevelFromXP(Skills.PRAYER) * 2.5;
					player.getSkills().set(Skills.PRAYER, 0);
					player.getPrayer().setPrayerpoints(0);
				} else if (player.getEquipment().getAmuletId() != 11090
						&& player.getEquipment().getRingId() == 11090
						&& player.getHitpoints() <= player.getMaxHitpoints() * 0.1) {
					Magic.sendNormalTeleportSpell(player, 1, 0,
							Constants.RESPAWN_PLAYER_LOCATION);
					player.getEquipment().deleteItem(11090, 1);
					player.packets()
							.sendMessage(
									"Your ring of life saves you, but is destroyed in the process.");
				}
			}
			if (player.getEquipment().getAmuletId() == 11090
					&& player.getHitpoints() <= player.getMaxHitpoints() * 0.2) { // priority
																					// over
																					// rol
				player.heal((int) (player.getMaxHitpoints() * 0.3));
				player.getEquipment().deleteItem(11090, 1);
				player.packets()
						.sendMessage(
								"Your pheonix necklace heals you, but is destroyed in the process.");
			}
		}
	}

	public void resetReceivedDamage() {
		receivedDamage.clear();
	}

	public void removeDamage(Entity entity) {
		receivedDamage.remove(entity);
	}

	public Player getMostDamageReceivedSourcePlayer() {
		Player player = null;
		NPC npc = null;
		Familiar familiar = null;
		int damage = -1;
		for (Entity source : receivedDamage.keySet()) {
			if (source instanceof NPC) {
				npc = (NPC) source;
				if (npc.isFamiliar())
					familiar = (Familiar) npc;
			}
			if (familiar == null && npc != null)
				continue;
			Integer d = receivedDamage.get(source);
			if (d == null) {
				receivedDamage.remove(source);
				continue;
			}
			if (familiar != null) {
				d += receivedDamage.get(familiar.getOwner()) != null ? receivedDamage
						.get(familiar.getOwner()) : 0;
				player = familiar.getOwner();
			} else if (familiar == null) {
				player = (Player) source;
			}
			if (d > damage)
				damage = d;

		}
		return player;
	}

	public void addReceivedDamage(Entity source, int amount) {
		if (source == null)
			return;
		Integer damage = receivedDamage.get(source);
		damage = damage == null ? amount : damage + amount;
		receivedDamage.put(source, damage);
	}

	public void heal(int ammount) {
		heal(ammount, 0);
	}

	public void heal(int ammount, int extra) {
		hitpoints = hitpoints + ammount >= getMaxHitpoints() + extra ? getMaxHitpoints()
				+ extra
				: hitpoints + ammount;
	}

	public boolean hasWalkSteps() {
		return !walkSteps.isEmpty();
	}

	public abstract void sendDeath(Entity source);

	public void processMovement() {
		lastLocation = new Location(this);
		if (lastFaceEntity >= 0) {
			Entity target = lastFaceEntity >= 32768 ? Game.getPlayers().get(
					lastFaceEntity - 32768) : Game.getNPCs().get(
					lastFaceEntity);
			if (target != null)
				direction = Misc.getFaceDirection(
						target.getCoordFaceX(target.getSize()) - getX(),
						target.getCoordFaceY(target.getSize()) - getY());
		}
		nextWalkDirection = nextRunDirection = -1;
		if (nextLocation != null) {
			int lastPlane = getZ();
			setCoords(nextLocation);
			nextLocation = null;
			teleported = true;
			if (this instanceof Player)
				((Player) this).setTemporaryMoveType(Player.TELE_MOVE_TYPE);
			Game.updateEntityRegion(this);
			if (needMapUpdate())
				loadMapRegions();
			else if (this instanceof Player && lastPlane != getZ())
				((Player) this).setClientHasntLoadedMapRegion();
			resetWalkSteps();
			return;
		}
		teleported = false;
		if (walkSteps.isEmpty())
			return;
		if (this instanceof Player) {
			if (((Player) this).getEmotesManager().getNextEmoteEnd() >= Misc
					.currentTimeMillis())
				return;
		}
		nextWalkDirection = getNextWalkStep();
		if (nextWalkDirection != -1) {
			if (this instanceof Player) {
				if (!((Player) this).getControllerManager().canMove(
						nextWalkDirection)) {
					nextWalkDirection = -1;
					resetWalkSteps();
					return;
				}
			}
			moveLocation(Misc.DIRECTION_DELTA_X[nextWalkDirection],
					Misc.DIRECTION_DELTA_Y[nextWalkDirection], 0);
			if (run) {
				if (this instanceof Player
						&& ((Player) this).getRunEnergy() <= 0)
					setRun(false);
				else {
					nextRunDirection = getNextWalkStep();
					if (nextRunDirection != -1) {
						if (this instanceof Player) {
							Player player = (Player) this;
							if (!player.getControllerManager().canMove(
									nextRunDirection)) {
								nextRunDirection = -1;
								resetWalkSteps();
								return;
							}
							player.drainRunEnergy();
						}
						moveLocation(Misc.DIRECTION_DELTA_X[nextRunDirection],
								Misc.DIRECTION_DELTA_Y[nextRunDirection], 0);
					} else if (this instanceof Player)
						((Player) this)
								.setTemporaryMoveType(Player.WALK_MOVE_TYPE);
				}
			}
		}
		Game.updateEntityRegion(this);
		if(this instanceof Player) {
			Player p = (Player) this;
			p.getRandomEvent().processMovement();
		}
		if (needMapUpdate())
			loadMapRegions();
	}

	@Override
	public void moveLocation(int xOffset, int yOffset, int planeOffset) {
		super.moveLocation(xOffset, yOffset, planeOffset);
		direction = Misc.getFaceDirection(xOffset, yOffset);
	}

	private boolean needMapUpdate() {
		int lastMapRegionX = lastLoadedMapRegionTile.getChunkX();
		int lastMapRegionY = lastLoadedMapRegionTile.getChunkY();
		int regionX = getChunkX();
		int regionY = getChunkY();
		int size = ((VIEWPORT_SIZES[mapSize] >> 3) / 2) - 1;
		return Math.abs(lastMapRegionX - regionX) >= size
				|| Math.abs(lastMapRegionY - regionY) >= size;
	}

	public boolean addWalkSteps(int destX, int destY) {
		return addWalkSteps(destX, destY, -1);
	}

	/*
	 * returns if cliped
	 */
	public boolean clipedProjectile(Location tile, boolean checkClose) {
		return clipedProjectile(tile, checkClose, 1); // size 1 thats arrow
														// size, the tile has to
														// be target center
														// coord not base
	}

	/*
	 * return added all steps
	 */
	public boolean checkWalkStepsInteract(int fromX, int fromY,
			final int destX, final int destY, int maxStepsCount, int size,
			boolean calculate) {
		int[] lastTile = new int[] { fromX, fromY };
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			int myRealX = myX;
			int myRealY = myY;

			if (myX < destX)
				myX++;
			else if (myX > destX)
				myX--;
			if (myY < destY)
				myY++;
			else if (myY > destY)
				myY--;
			if (!checkWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
				if (!calculate)
					return false;
				myX = myRealX;
				myY = myRealY;
				int[] myT = checkcalculatedStep(myRealX, myRealY, destX, destY,
						lastTile[0], lastTile[1], size);
				if (myT == null)
					return false;
				myX = myT[0];
				myY = myT[1];
			}
			int distanceX = myX - destX;
			int distanceY = myY - destY;
			if (!(distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1))
				return true;
			if (stepCount == maxStepsCount)
				return true;
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY)
				return true;
		}
	}

	public int[] checkcalculatedStep(int myX, int myY, int destX, int destY,
			int lastX, int lastY, int size) {
		if (myX < destX) {
			myX++;
			if (!checkWalkStep(myX, myY, lastX, lastY, true))
				myX--;
			else if (!(myX - destX > size || myX - destX < -1
					|| myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		} else if (myX > destX) {
			myX--;
			if (!checkWalkStep(myX, myY, lastX, lastY, true))
				myX++;
			else if (!(myX - destX > size || myX - destX < -1
					|| myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		}
		if (myY < destY) {
			myY++;
			if (!checkWalkStep(myX, myY, lastX, lastY, true))
				myY--;
			else if (!(myX - destX > size || myX - destX < -1
					|| myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		} else if (myY > destY) {
			myY--;
			if (!checkWalkStep(myX, myY, lastX, lastY, true)) {
				myY++;
			} else if (!(myX - destX > size || myX - destX < -1
					|| myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		}
		if (myX == lastX || myY == lastY)
			return null;
		return new int[] { myX, myY };
	}

	/*
	 * returns if cliped
	 */
	public boolean clipedProjectile(Location tile, boolean checkClose, int size) {
		int myX = getX();
		int myY = getY();
		int destX = tile.getX();
		int destY = tile.getY();
		int lastTileX = myX;
		int lastTileY = myY;
		while (true) {
			if (myX < destX)
				myX++;
			else if (myX > destX)
				myX--;
			if (myY < destY)
				myY++;
			else if (myY > destY)
				myY--;
			int dir = Misc.getMoveDirection(myX - lastTileX, myY - lastTileY);
			if (dir == -1)
				return false;
			if (checkClose) {
				if (!Game.checkWalkStep(getZ(), lastTileX, lastTileY, dir,
						size))
					return false;
			} else if (!Game.checkProjectileStep(getZ(), lastTileX, lastTileY,
					dir, size))
				return false;
			lastTileX = myX;
			lastTileY = myY;
			if (lastTileX == destX && lastTileY == destY)
				return true;
		}
	}

	public boolean addWalkStepsInteract(int destX, int destY,
			int maxStepsCount, int size, boolean calculate) {
		return addWalkStepsInteract(destX, destY, maxStepsCount, size, size,
				calculate);
	}

	/*
	 * return added all steps
	 */
	public boolean addWalkStepsInteract(final int destX, final int destY,
			int maxStepsCount, int sizeX, int sizeY, boolean calculate) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			int myRealX = myX;
			int myRealY = myY;

			if (myX < destX)
				myX++;
			else if (myX > destX)
				myX--;
			if (myY < destY)
				myY++;
			else if (myY > destY)
				myY--;
			if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
				if (!calculate)
					return false;
				myX = myRealX;
				myY = myRealY;
				int[] myT = calculatedStep(myRealX, myRealY, destX, destY,
						lastTile[0], lastTile[1], sizeX, sizeY);
				if (myT == null)
					return false;
				myX = myT[0];
				myY = myT[1];
			}
			int distanceX = myX - destX;
			int distanceY = myY - destY;
			if (!(distanceX > sizeX || distanceX < -1 || distanceY > sizeY || distanceY < -1))
				return true;
			if (stepCount == maxStepsCount)
				return true;
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY)
				return true;
		}
	}

	public int[] calculatedStep(int myX, int myY, int destX, int destY,
			int lastX, int lastY, int sizeX, int sizeY) {
		if (myX < destX) {
			myX++;
			if (!addWalkStep(myX, myY, lastX, lastY, true))
				myX--;
			else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		} else if (myX > destX) {
			myX--;
			if (!addWalkStep(myX, myY, lastX, lastY, true))
				myX++;
			else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		}
		if (myY < destY) {
			myY++;
			if (!addWalkStep(myX, myY, lastX, lastY, true))
				myY--;
			else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		} else if (myY > destY) {
			myY--;
			if (!addWalkStep(myX, myY, lastX, lastY, true)) {
				myY++;
			} else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		}
		if (myX == lastX || myY == lastY)
			return null;
		return new int[] { myX, myY };
	}

	/*
	 * return added all steps
	 */
	public boolean addWalkSteps(final int destX, final int destY,
			int maxStepsCount) {
		return addWalkSteps(destX, destY, -1, true);
	}

	/*
	 * return added all steps
	 */
	public boolean addWalkSteps(final int destX, final int destY,
			int maxStepsCount, boolean check) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			if (myX < destX)
				myX++;
			else if (myX > destX)
				myX--;
			if (myY < destY)
				myY++;
			else if (myY > destY)
				myY--;
			if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], check)) // clipped stop
				return false;
			if (stepCount == maxStepsCount)
				return true;
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY)
				return true;
		}
	}

	public int[] getLastWalkTile() {
		Object[] objects = walkSteps.toArray();
		if (objects.length == 0)
			return new int[] { getX(), getY() };
		int step[] = (int[]) objects[objects.length - 1];
		return new int[] { step[1], step[2] };
	}

	// return cliped step
	public boolean checkWalkStep(int nextX, int nextY, int lastX, int lastY,
			boolean check) {
		int dir = Misc.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1)
			return false;

		if (check && !Game.checkWalkStep(getZ(), lastX, lastY, dir, getSize())) {
			return false;
		}
		return true;
	}

	// return cliped step
	public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY,
			boolean check) {
		int dir = Misc.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1)
			return false;

		if (check) {
			if (!Game.checkWalkStep(getZ(), lastX, lastY, dir, getSize()))
				return false;
			if (this instanceof Player) {
				if (!((Player) this).getControllerManager().checkWalkStep(lastX,
						lastY, nextX, nextY))
					return false;
			}
		}
		walkSteps.add(new int[] { dir, nextX, nextY });
		return true;
	}

	public ConcurrentLinkedQueue<int[]> getWalkSteps() {
		return walkSteps;
	}

	public void resetWalkSteps() {
		walkSteps.clear();
	}

	private int getNextWalkStep() {
		int step[] = walkSteps.poll();
		if (step == null)
			return -1;
		return step[0];

	}

	public boolean restoreHitPoints() {
		int maxHp = getMaxHitpoints();
		if (hitpoints > maxHp) {
			if (this instanceof Player) {
				Player player = (Player) this;
				if (player.getPrayer().usingPrayer(1, 5)
						&& Misc.getRandom(100) <= 15)
					return false;
			}
			hitpoints -= 1;
			return true;
		} else if (hitpoints < maxHp) {
			hitpoints += 1;
			if (this instanceof Player) {
				Player player = (Player) this;
				if (player.getPrayer().usingPrayer(0, 9) && hitpoints < maxHp)
					hitpoints += 1;
				else if (player.getPrayer().usingPrayer(0, 26)
						&& hitpoints < maxHp)
					hitpoints += hitpoints + 4 > maxHp ? maxHp - hitpoints : 4;

			}
			return true;
		}
		return false;
	}

	public boolean needMasksUpdate() {
		if(this instanceof NPC)
			return ((NPC) this).hasValidHoverMessage();
		return nextFaceEntity != -2 || nextAnimation != null
			|| nextGraphics1 != null || nextGraphics2 != null
			|| nextGraphics3 != null || nextGraphics4 != null
			|| (nextWalkDirection == -1 && nextFaceLocation != null)
			|| !nextHits.isEmpty() || nextForceMovement != null
			|| nextForceTalk != null || bodyGlow != null;
	}

	public boolean isDead() {
		return hitpoints == 0;
	}

	public void resetMasks() {
		nextAnimation = null;
		nextGraphics1 = null;
		nextGraphics2 = null;
		nextGraphics3 = null;
		nextGraphics4 = null;
		if (nextWalkDirection == -1)
			nextFaceLocation = null;
		nextForceMovement = null;
		nextForceTalk = null;
		nextFaceEntity = -2;
		bodyGlow = null;
		nextHits.clear();
	}

	public abstract void finish();

	public abstract int getMaxHitpoints();

	public void processEntity() {
		poison.processPoison();
		processMovement();
		processReceivedHits();
	}

	public void loadMapRegions() {
		mapRegionsIds.clear();
		isAtDynamicRegion = false;
		int regionX = getChunkX();
		int regionY = getChunkY();
		int mapHash = VIEWPORT_SIZES[mapSize] >> 4;
		for (int xCalc = (regionX - mapHash) / 8; xCalc <= ((regionX + mapHash) / 8); xCalc++)
			for (int yCalc = (regionY - mapHash) / 8; yCalc <= ((regionY + mapHash) / 8); yCalc++) {
				int regionId = yCalc + (xCalc << 8);
				if (Game.getRegion(regionId, this instanceof Player) instanceof DynamicRegion)
					isAtDynamicRegion = true;
				mapRegionsIds.add(yCalc + (xCalc << 8));
			}
		lastLoadedMapRegionTile = new Location(this); // creates a immutable
														// copy of this
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;

	}

	public void setLastRegionId(int lastRegionId) {
		this.lastRegionId = lastRegionId;
	}

	public int getLastRegionId() {
		return lastRegionId;
	}

	public int getMapSize() {
		return mapSize;
	}

	public void setMapSize(int size) {
		this.mapSize = size;
	}

	public CopyOnWriteArrayList<Integer> getMapRegionsIds() {
		return mapRegionsIds;
	}

	public void setNextAnimation(Animation nextAnimation) {
		if (nextAnimation != null && nextAnimation.getIds()[0] >= 0)
			lastAnimationEnd = Misc.currentTimeMillis()
					+ AnimationDefinition.get(
							nextAnimation.getIds()[0]).getEmoteTime();
		this.nextAnimation = nextAnimation;
	}

	public void animate(int animId) {
		Animation anim = new Animation(animId);
		if (anim != null && anim.getIds()[0] >= 0)
			lastAnimationEnd = Misc.currentTimeMillis()
					+ AnimationDefinition.get(
							anim.getIds()[0]).getEmoteTime();
		this.nextAnimation = anim;
	}

	public void setNextAnimationNoPriority(Animation nextAnimation) {
		if (lastAnimationEnd > Misc.currentTimeMillis())
			return;
		setNextAnimation(nextAnimation);
	}

	public Animation getNextAnimation() {
		return nextAnimation;
	}

	public void setNextGraphics(Graphics nextGraphics) {
		nextGraphics1 = null;
		nextGraphics1 = nextGraphics;
	}

	public void graphics(int g) {
		nextGraphics1 = null;
		nextGraphics1 = new Graphics(g, 0, 0);
	}

	public Graphics getNextGraphics1() {
		return nextGraphics1;
	}

	public Graphics getNextGraphics2() {
		return nextGraphics2;
	}

	public Graphics getNextGraphics3() {
		return nextGraphics3;
	}

	public Graphics getNextGraphics4() {
		return nextGraphics4;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean hasFinished() {
		return finished;
	}

	public void setLocation(int x, int y, int z) {
		setLocation(new Location(x, y, z));
	}

	public void setLocation(Location nextLocation) {
		if (this instanceof Player) {
			Player p = (Player) this;
			if ((p.cantChangeLocation() && p.getRights() < 2 && !p.isOwner()) ||
				(!p.getRandomEvent().canChangeLocation(nextLocation))) {
				if (p.getStarterStage() == 1)
					for (NPC n : Game.getLocalNPCs(this))
						if (n.defs().getName().contains("Guide"))
							p.hints().addHintIcon(n, 1, true, false);
				return;
			} else
				this.nextLocation = nextLocation;
		} else
			this.nextLocation = nextLocation;
		if (this instanceof Player) {
			Player p = (Player) this;
			p.getRegion().forceGetRegionMap();
			p.getRegion().forceGetFloorItems();
			p.getRegion().forceGetRegionMapClipedOnly();
		}
	}

	public Location getNextLocation() {
		return nextLocation;
	}

	public boolean hasTeleported() {
		return teleported;
	}

	public Location getLastLoadedMapRegionTile() {
		return lastLoadedMapRegionTile;
	}

	public int getNextWalkDirection() {
		return nextWalkDirection;
	}

	public int getNextRunDirection() {
		return nextRunDirection;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public boolean getRun() {
		return run;
	}

	public Location getNextFaceLocation() {
		return nextFaceLocation;
	}

	public void faceLocation(Location nextFaceLocation) {
		if (nextFaceLocation.getX() == getX()
				&& nextFaceLocation.getY() == getY())
			return;
		this.nextFaceLocation = nextFaceLocation;
		if (nextLocation != null)
			direction = Misc.getFaceDirection(nextFaceLocation.getX()
					- nextLocation.getX(), nextFaceLocation.getY()
					- nextLocation.getY());
		else
			direction = Misc.getFaceDirection(nextFaceLocation.getX() - getX(),
					nextFaceLocation.getY() - getY());
	}

	public abstract int getSize();

	public void cancelFaceEntityNoCheck() {
		nextFaceEntity = -2;
		lastFaceEntity = -1;
	}

	public void setNextFaceEntity(Entity entity) {
		if (entity == null) {
			nextFaceEntity = -1;
			lastFaceEntity = -1;
		} else {
			nextFaceEntity = entity.getClientIndex();
			lastFaceEntity = nextFaceEntity;
		}
	}

	public int getNextFaceEntity() {
		return nextFaceEntity;
	}

	public long getFreezeDelay() {
		return freezeDelay; // 2500 delay
	}

	public int getLastFaceEntity() {
		return lastFaceEntity;
	}

	public long getFrozenBlockedDelay() {
		return frozenBlocked;
	}

	public void setFrozeBlocked(int time) {
		this.frozenBlocked = time;
	}

	public void setFreezeDelay(int time) {
		this.freezeDelay = time;
	}

	public void addFrozenBlockedDelay(int time) {
		frozenBlocked = time + Misc.currentTimeMillis();
	}

	public void addFreezeDelay(long time) {
		addFreezeDelay(time, false);
	}

	public void addFreezeDelay(long time, boolean entangleMessage) {
		long currentTime = Misc.currentTimeMillis();
		if (currentTime > freezeDelay) {
			resetWalkSteps();
			freezeDelay = time + currentTime;
			if (this instanceof Player) {
				Player p = (Player) this;
				if (!entangleMessage)
					p.packets().sendMessage("You have been frozen.");
			}
		}
	}

	public abstract double getMagePrayerMultiplier();

	public abstract double getRangePrayerMultiplier();

	public abstract double getMeleePrayerMultiplier();

	public Entity getAttackedBy() {
		return attackedBy;
	}

	public void setAttackedBy(Entity attackedBy) {
		this.attackedBy = attackedBy;
	}

	public long getAttackedByDelay() {
		return attackedByDelay;
	}

	public void setAttackedByDelay(long attackedByDelay) {
		this.attackedByDelay = attackedByDelay;
	}

	public void checkMultiArea() {
		multiArea = forceMultiArea ? true : Game.isMultiArea(this);
	}

	public boolean isAtMultiArea() {
		return multiArea;
	}

	public void setAtMultiArea(boolean multiArea) {
		this.multiArea = multiArea;
	}

	public boolean isAtDynamicRegion() {
		return isAtDynamicRegion;
	}

	public ForceMovement getNextForceMovement() {
		return nextForceMovement;
	}

	public void setNextForceMovement(ForceMovement nextForceMovement) {
		this.nextForceMovement = nextForceMovement;
	}

	public Poison getPoison() {
		return poison;
	}

	public ForceTalk getNextForceTalk() {
		return nextForceTalk;
	}

	public void setNextForceTalk(ForceTalk nextForceTalk) {
		this.nextForceTalk = nextForceTalk;
	}

	public void forceTalk(String force) {
		this.nextForceTalk = new ForceTalk(force);
	}

	public void face(Entity target) {
		faceLocation(new Location(target.getCoordFaceX(target.getSize()),
				target.getCoordFaceY(target.getSize()), target.getZ()));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				nextFaceEntity = -2;
				this.stop();
			}
		}, 1, 0);
	}

	public void faceEntity(Entity target) {
		faceLocation(new Location(target.getCoordFaceX(target.getSize()),
				target.getCoordFaceY(target.getSize()), target.getZ()));
	}

	public void faceObject(GlobalObject object) {
		ObjectDefinition objectDef = object.defs();
		faceLocation(new Location(
			object.getCoordFaceX(objectDef.getSizeX(),
			objectDef.getSizeY(), object.getRotation()),
			object.getCoordFaceY(objectDef.getSizeX(),
			objectDef.getSizeY(), object.getRotation()),
			object.getZ()));
	}

	public long getLastAnimationEnd() {
		return lastAnimationEnd;
	}

	public ConcurrentHashMap<Object, Object> getTemporaryAttributtes() {
		return temporaryAttributes;
	}

	public ConcurrentHashMap<Object, Object> getAttributtes() {
		return temporaryAttributes;
	}

	public boolean isForceMultiArea() {
		return forceMultiArea;
	}

	public void setForceMultiArea(boolean forceMultiArea) {
		this.forceMultiArea = forceMultiArea;
		checkMultiArea();
	}

	public Location getLastLocation() {
		return lastLocation;
	}

	public ArrayList<Hit> getNextHits() {
		return nextHits;
	}

	public void playSound(int soundId, int type) {
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = Game.getRegion(regionId)
					.getPlayerIndexes();
			if (playerIndexes != null) {
				for (int playerIndex : playerIndexes) {
					Player player = Game.getPlayers().get(playerIndex);
					if (player == null || !player.isRunning()
							|| player.hasFinished() || !withinDistance(player))
						continue;
					player.packets().sendSound(soundId, 0, type);
				}
			}
		}
	}

	public long getFindTargetDelay() {
		return findTargetDelay;
	}

	public void setFindTargetDelay(long findTargetDelay) {
		this.findTargetDelay = findTargetDelay;
	}

	public boolean calcFollow(Location target, boolean inteligent) {
		return calcFollow(target, true);
	}

	public boolean calcFollow(Location target, int maxStepsCount,
			boolean calculate, boolean inteligent) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		int size = getSize();
		int destX = target.getX();
		int destY = target.getY();
		int sizeX = target instanceof GlobalObject ? ((GlobalObject) target)
				.defs().getSizeX() : ((Entity) target).getSize();
		int sizeY = target instanceof GlobalObject ? ((GlobalObject) target)
				.defs().getSizeY() : sizeX;
		while (true) {
			stepCount++;
			int myRealX = myX;
			int myRealY = myY;
			if (Misc.isOnRange(myX, myY, size, destX, destY, sizeX, 0))
				return true;
			if (myX < destX)
				myX++;
			else if (myX > destX)
				myX--;
			if (myY < destY)
				myY++;
			else if (myY > destY)
				myY--;
			if ((this instanceof NPC && !canWalkNPC(myX, myY))
					|| !addWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
				if (!calculate)
					return false;
				myX = myRealX;
				myY = myRealY;
				int[] myT = calculatedStep(myRealX, myRealY, destX, destY,
						lastTile[0], lastTile[1], sizeX, sizeY);
				if (myT == null)
					return false;
				myX = myT[0];
				myY = myT[1];
			}
			if (stepCount == maxStepsCount)
				return true;
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY)
				return true;
		}
	}

	public boolean canWalkNPC(int toX, int toY) {
		int id = ((NPC) this).getId();
		// godwar npcs / corp can walk throught minions
		return id == 6260 || id == 6222 || id == 6203 || id == 6247
				|| id == 8133 || canWalkNPC(toX, toY, false);
	}

	public boolean canWalkNPC(int toX, int toY, boolean checkUnder) {
		if (!isAtMultiArea() /*
							 * || (!checkUnder && !canWalkNPC(getX(), getY(),
							 * true))
							 */)
			return true;
		int size = getSize();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> npcIndexes = Game.getRegion(regionId)
					.getNPCsIndexes();
			if (npcIndexes != null && npcIndexes.size() < 50) {
				for (int npcIndex : npcIndexes) {
					NPC target = Game.getNPCs().get(npcIndex);
					if (target == null
							|| target == this
							|| target.isDead()
							|| target.hasFinished()
							|| target.getZ() != getZ()
							|| !target.isAtMultiArea()
							|| (!(this instanceof Familiar) && target instanceof Familiar))
						continue;
					int targetSize = target.getSize();
					if (!checkUnder && target.getNextWalkDirection() == -1) { // means
						// the
						// walk
						// hasnt
						// been
						// processed
						// yet
						int previewDir = getPreviewNextWalkStep();
						if (previewDir != -1) {
							Location tile = target.transform(
									Misc.DIRECTION_DELTA_X[previewDir],
									Misc.DIRECTION_DELTA_Y[previewDir], 0);
							if (Misc.colides(tile.getX(), tile.getY(),
									targetSize, getX(), getY(), size))
								continue;

							if (Misc.colides(tile.getX(), tile.getY(),
									targetSize, toX, toY, size))
								return false;
						}
					}
					if (Misc.colides(target.getX(), target.getY(), targetSize,
							getX(), getY(), size))
						continue;
					if (Misc.colides(target.getX(), target.getY(), targetSize,
							toX, toY, size))
						return false;
				}
			}
		}
		return true;
	}

	private int getPreviewNextWalkStep() {
		int[] step = walkSteps.poll();
		if (step == null)
			return -1;
		return (int) step[0];
	}
	
	public BodyGlow getBodyGlow() {
		return bodyGlow;
	}

	public void glow(BodyGlow newGlow) {
		if(bodyGlow != null)
			bodyGlow = null;
		this.bodyGlow = newGlow;
		if(this instanceof Player)
			((Player) this).getAppearance().generateAppearanceData();
	}
	
	public void glow(int duration, int[] colors) {
		if(bodyGlow != null)
			bodyGlow = null;
		this.bodyGlow = new BodyGlow(duration, colors);
		if(this instanceof Player)
			((Player) this).getAppearance().generateAppearanceData();
	}

//	public void setHoverMessage(String message) {
//		if(hoverMessage != null)
//			hoverMessage = null;
//		this.hoverMessage = new HoverMessage(message);
//		if(this instanceof Player)
//			((Player) this).getAppearance().generateAppearanceData();
//	}

	public boolean isUnderAttack() {
		return isUnderAttack;
	}

	public void setUnderAttack(boolean isUnderAttack) {
		this.isUnderAttack = isUnderAttack;
	}
	
	public void changeDirection(String spawnDirection) {
		if(spawnDirection.equals(null))
			spawnDirection = "NORTH";
		int offsetX = 0;
		int offsetY = 0;
		for(Directions dir : Directions.values()) {
			if(dir != null) {
				if(dir.name().toLowerCase().equals(spawnDirection.toLowerCase())) {
					offsetX = getSize() == 1 ? dir.getOffsetX() : (dir.getOffsetX() < 0 ? dir.getOffsetX() - getSize() : dir.getOffsetX() + getSize());
					offsetY = getSize() == 1 ? dir.getOffsetY() : (dir.getOffsetY() < 0 ? dir.getOffsetY() - getSize() : dir.getOffsetY() + getSize());
				}
			}
		}
		resetMasks();
		faceLocation(new Location(getX() + offsetX, getY() + offsetY, getZ()));
	}
	
	private transient int[] lastBodyGlowColors;
	
	public void setLastBodyGlowColors() {
		this.lastBodyGlowColors = bodyGlow.getColors();
	}
	
	public int[] getLastBodyGlowColors() {
		return lastBodyGlowColors;
	}

}
