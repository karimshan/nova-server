package org.nova.game.player;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.utility.misc.Misc;


public class OwnedObjectManager {

	public static final AtomicLong keyMaker = new AtomicLong();

	private static final Map<String, OwnedObjectManager> ownedObjects = new ConcurrentHashMap<String, OwnedObjectManager>();

	private Player player;
	private GlobalObject[] objects;
	private int count;
	private long cycleTime;
	private long lifeTime;
	private String managerKey;

	public static void processAll() {
		for (OwnedObjectManager object : ownedObjects.values())
			object.process();
	}

	public static boolean isPlayerObject(Player player, GlobalObject object) {
		for (Iterator<String> it = player.getOwnedObjectManagerKeys()
				.iterator(); it.hasNext();) {
			OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			if (manager.getCurrentObject().getX() == object.getX()
					&& manager.getCurrentObject().getY() == object.getY()
					&& manager.getCurrentObject().getZ() == object
							.getZ()
					&& manager.getCurrentObject().getId() == object.getId())
				return true;
		}
		return false;
	}

	public static interface ConvertEvent {

		public boolean canConvert(Player player);

	}

	public static boolean convertIntoObject(GlobalObject object,
			GlobalObject toObject, ConvertEvent event) {
		for (OwnedObjectManager manager : ownedObjects.values()) {
			if (manager.getCurrentObject().getX() == toObject.getX()
					&& manager.getCurrentObject().getY() == toObject.getY()
					&& manager.getCurrentObject().getZ() == toObject
							.getZ()
					&& manager.getCurrentObject().getId() == object.getId()) {
				if (event != null && !event.canConvert(manager.player))
					return false;
				manager.convertIntoObject(toObject);
				return true;
			}
		}
		return false;
	}

	public static boolean removeObject(Player player, GlobalObject object) {
		for (Iterator<String> it = player.getOwnedObjectManagerKeys()
				.iterator(); it.hasNext();) {
			OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			if (manager.getCurrentObject().getX() == object.getX()
					&& manager.getCurrentObject().getY() == object.getY()
					&& manager.getCurrentObject().getZ() == object
							.getZ()
					&& manager.getCurrentObject().getId() == object.getId()) {
				manager.delete();
				return true;
			}
		}
		return false;
	}

	public static void linkKeys(Player player) {
		for (Iterator<String> it = player.getOwnedObjectManagerKeys()
				.iterator(); it.hasNext();) {
			OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			manager.player = player;
		}
	}

	public static void addOwnedObjectManager(Player player,
			GlobalObject[] objects, long cycleTime) {
		new OwnedObjectManager(player, objects, cycleTime);
	}

	private OwnedObjectManager(Player player, GlobalObject[] objects,
			long cycleTime) {
		managerKey = player.getUsername() + "_" + keyMaker.getAndIncrement();
		this.cycleTime = cycleTime;
		this.objects = objects;
		this.player = player;
		spawnObject();
		player.getOwnedObjectManagerKeys().add(managerKey);
		ownedObjects.put(managerKey, this);
	}

	public void reset() {
		for (OwnedObjectManager object : ownedObjects.values())
			object.delete();
	}

	public void resetLifeTime() {
		this.lifeTime = Misc.currentTimeMillis() + cycleTime;
	}

	public boolean forceMoveNextStage() {
		if (count != -1)
			destroyObject(objects[count]);
		count++;
		if (count == objects.length) {
			remove();
			return false;
		}
		spawnObject();
		return true;
	}

	private void spawnObject() {
		Game.spawnObject(objects[count], true);
		resetLifeTime();
	}

	public void convertIntoObject(GlobalObject object) {
		destroyObject(objects[count]);
		objects[count] = object;
		spawnObject();
	}

	private void remove() {
		ownedObjects.remove(managerKey);
		if (player != null)
			player.getOwnedObjectManagerKeys().remove(managerKey);
	}

	public void delete() {
		destroyObject(objects[count]);
		remove();
	}

	public void process() {
		if (Misc.currentTimeMillis() > lifeTime)
			forceMoveNextStage();
	}

	public GlobalObject getCurrentObject() {
		return objects[count];
	}

	public void destroyObject(GlobalObject object) {
		int regionId = object.getRegionId();
		int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		Game.getRegion(regionId).removeObject(object);
		Game.getRegion(regionId).removeMapObject(object, baseLocalX,
				baseLocalY);
		for (Player p2 : Game.getPlayers()) {
			if (p2 == null || !p2.hasStarted() || p2.hasFinished()
					|| !p2.getMapRegionsIds().contains(regionId))
				continue;
			p2.packets().sendDestroyObject(object);
		}
	}

}
