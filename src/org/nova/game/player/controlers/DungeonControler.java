package org.nova.game.player.controlers;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.dungeoneering.DungeonManager;
import org.nova.game.player.content.dungeoneering.DungeonPartyPlayer;
import org.nova.game.player.content.dungeoneering.DungeonUtils;
import org.nova.game.player.content.dungeoneering.RoomReference;
import org.nova.utility.misc.Misc;

public class DungeonControler extends Controller {

	private DungeonManager dungeon;

	@Override
	public void start() {
		dungeon = (DungeonManager) getArguments()[0];
		setArguments(null); 
		showDeaths();
	}

	public void showDeaths() {
		player.interfaces()
				.sendTab(
						player.interfaces().isFullScreen() ? 10
								: 8, 945);
	}

	@Override
	public void sendInterfaces() {
		showDeaths();
	}

	@Override
	public boolean sendDeath() {
		player.addStopDelay(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.packets().sendMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.reset();
					player.setLocation(dungeon.getHomeTile());
					player.setNextAnimation(new Animation(-1));
					stop();
					DungeonPartyPlayer dp = dungeon.getDPlayer(player);
					if (dp != null)
						dp.increaseDeaths();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	public boolean processMagicTeleport(Location toTile) {
		if (dungeon == null
				|| !player.getCombatDefinitions().isDungeonneringSpellBook()
				|| !dungeon.hasStarted())
			return false;
		return true;
	}

	public boolean processItemTeleport(Location toTile) {
		return false;
	}

	@Override
	public boolean processObjectClick1(final GlobalObject object) {
		if (dungeon == null || !dungeon.hasStarted())
			return false;
		if (DungeonUtils.isDoor(object.getId())) {
			openDoor(object);
			return false;
		} else if (DungeonUtils.isExit(object.getId())) {
			leaveDungeon(false);
			return false;
		} else if (object.getId() == 3784){
			int dl = player.getSkills().getLevel(24);
			int ia = 0;
			int dt = 0;
			int fn = 0;
			if (dl >= 110){
			ia = 80000;
			} else if (dl >= 90){
			ia = 70000;
			} else if (dl >= 75){
			ia = 55000;
			} else if (dl >= 50){
			ia = 50000;
			} else if (dl >= 25){
			ia = 45000;
			} else if (dl >= 10){
			ia = 15000;
			} else {
			ia = 5000;
			}
			if (player.dungtime > 0) {
			dt = (int)(player.dungtime*2);
			fn = (ia+dt);
			}else{
			int lol = Misc.random(1,5000);
			fn = (ia)+lol;
			}
			player.setDungeoneeringPoints(player.getDungeoneeringPoints() + fn/10);
			player.getSkills().addXp(24, fn);
			player.setLocation(new Location(3449, 3725, 0));
			player.dungtime = 0;
			player.dungeon = null;
			player.reset();
			player.stopAll();
			dungeon.removePlayer(player);
			player.getCombatDefinitions().removeDungeonneringBook();
			player.setMapSize(0);
			player.setForceMultiArea(false);
			removeControler();
			player.packets().closeInterface(
					player.interfaces().isFullScreen() ? 7 : 17);
			player.packets().sendMessage("You have finished the dungeon, and gained "+(fn)+" Xp and "+fn/10+" Tokens.");
			player.packets().sendMusicEffect(415);
			dungeon.destroyDungeon();
			return true;
		}
		return true;
	}

	public void openDoor(GlobalObject object) {
		RoomReference roomReference = dungeon.getCurrentRoomReference(player);
		if (object.getRotation() == 3)
			dungeon.enterRoom(player, roomReference.getX(),
					roomReference.getY() - 1, object);
		else if (object.getRotation() == 1)
			dungeon.enterRoom(player, roomReference.getX(),
					roomReference.getY() + 1, object);
		else if (object.getRotation() == 2)
			dungeon.enterRoom(player, roomReference.getX() + 1,
					roomReference.getY(), object);
		else
			dungeon.enterRoom(player, roomReference.getX() - 1,
					roomReference.getY(), object);
	}

	@Override
	public boolean processObjectClick2(final GlobalObject object) {
		if (dungeon == null || !dungeon.hasStarted())
			return false;
		return true;
	}

	/*
	 * return process normaly
	 */
	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (dungeon == null || !dungeon.hasStarted())
			return false;
		if (interfaceId == 950 && componentId == 24) {
			Magic.sendNormalTeleportSpell(player, 0, 0, dungeon.getHomeTile());
			return false;
		}
		return true;
	}

	@Override
	public void forceClose() {
		leaveDungeon(false);
	}

	@Override
	public boolean logout() {
		leaveDungeon(true);
		return false;
	}

	public void leaveDungeon(boolean logout) {
		if (dungeon == null || !dungeon.hasStarted()) {
			if (logout)
				player.setCoords(new Location(new Location(3449, 3729, 0),
						2));
			else
				player.setLocation(new Location(new Location(3449, 3729, 0), 2));
			return;
		}
		player.stopAll();
		dungeon.removePlayer(player);
		player.getCombatDefinitions().removeDungeonneringBook();
		player.setMapSize(0);
		player.setForceMultiArea(false);
		if (logout)
			player.setCoords(new Location(new Location(3449, 3729, 0), 2));
		else
			player.setCoords(new Location(new Location(3449, 3729, 0),
					2));

		removeControler();
		player.packets().closeInterface(
				player.interfaces().isFullScreen() ? 7 : 17);
	}

}
