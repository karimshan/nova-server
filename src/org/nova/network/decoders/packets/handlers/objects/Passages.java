package org.nova.network.decoders.packets.handlers.objects;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * Represents all the different passages a player may take when interacting with certain objects.
 * 
 * @author Karimshan Nawaz
 *
 */
public class Passages {
	
	/**
	 * 
	 * @param player
	 * @param object
	 */
	public static void slashWeb(Player player, GlobalObject object) {
		if (Misc.getRandom(1) == 0) {
			Game.spawnTemporaryObject(new GlobalObject(object.getId() + 1,
				object.getType(), object.getRotation(), object.getX(),
					object.getY(), object.getZ()), 60000, true);
			player.packets().sendMessage("You slash through the web!");
		} else
			player.packets().sendMessage("You fail to cut through the web.");
	}
	
	/**
	 * 
	 * @param player
	 * @param object
	 * @return
	 */
	public static boolean passGate(Player player, GlobalObject object) {
		if (Game.isSpawnedObject(object))
			return false;
		if (object.getRotation() == 0) {

			boolean south = true;
			GlobalObject otherDoor = Game.getObject(new Location(
					object.getX(), object.getY() + 1, object.getZ()), object
					.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.defs().name
							.equalsIgnoreCase(object.defs().name)) {
				otherDoor = Game.getObject(
						new Location(object.getX(), object.getY() - 1, object
								.getZ()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.defs().name.equalsIgnoreCase(object
								.defs().name))
					return false;
				south = false;
			}
			GlobalObject openedDoor1 = new GlobalObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getZ());
			GlobalObject openedDoor2 = new GlobalObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getZ());
			if (south) {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor1.setRotation(3);
				openedDoor2.moveLocation(-1, 0, 0);
			} else {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor2.moveLocation(-1, 0, 0);
				openedDoor2.setRotation(3);
			}

			if (Game.removeTemporaryObject(object, 3000, true)
					&& Game.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				Game.spawnTemporaryObject(openedDoor1, 3000, true);
				Game.spawnTemporaryObject(openedDoor2, 3000, true);
				return true;
			}
		} else if (object.getRotation() == 2) {

			boolean south = true;
			GlobalObject otherDoor = Game.getObject(new Location(
					object.getX(), object.getY() + 1, object.getZ()), object
					.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.defs().name
							.equalsIgnoreCase(object.defs().name)) {
				otherDoor = Game.getObject(
						new Location(object.getX(), object.getY() - 1, object
								.getZ()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.defs().name.equalsIgnoreCase(object
								.defs().name))
					return false;
				south = false;
			}
			GlobalObject openedDoor1 = new GlobalObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getZ());
			GlobalObject openedDoor2 = new GlobalObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getZ());
			if (south) {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor2.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			} else {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor1.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			}
			if (Game.removeTemporaryObject(object, 3000, true)
					&& Game.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				Game.spawnTemporaryObject(openedDoor1, 3000, true);
				Game.spawnTemporaryObject(openedDoor2, 3000, true);
				return true;
			}
		} else if (object.getRotation() == 3) {

			boolean right = true;
			GlobalObject otherDoor = Game.getObject(new Location(
					object.getX() - 1, object.getY(), object.getZ()), object
					.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.defs().name
							.equalsIgnoreCase(object.defs().name)) {
				otherDoor = Game.getObject(new Location(object.getX() + 1,
						object.getY(), object.getZ()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.defs().name.equalsIgnoreCase(object
								.defs().name))
					return false;
				right = false;
			}
			GlobalObject openedDoor1 = new GlobalObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getZ());
			GlobalObject openedDoor2 = new GlobalObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getZ());
			if (right) {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor2.setRotation(0);
				openedDoor1.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			} else {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			}
			if (Game.removeTemporaryObject(object, 3000, true)
					&& Game.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				Game.spawnTemporaryObject(openedDoor1, 3000, true);
				Game.spawnTemporaryObject(openedDoor2, 3000, true);
				return true;
			}
		} else if (object.getRotation() == 1) {

			boolean right = true;
			GlobalObject otherDoor = Game.getObject(new Location(
					object.getX() - 1, object.getY(), object.getZ()), object
					.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.defs().name
							.equalsIgnoreCase(object.defs().name)) {
				otherDoor = Game.getObject(new Location(object.getX() + 1,
						object.getY(), object.getZ()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.defs().name.equalsIgnoreCase(object
								.defs().name))
					return false;
				right = false;
			}
			GlobalObject openedDoor1 = new GlobalObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getZ());
			GlobalObject openedDoor2 = new GlobalObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getZ());
			if (right) {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			} else {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor2.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			}
			if (Game.removeTemporaryObject(object, 3000, true)
					&& Game.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				Game.spawnTemporaryObject(openedDoor1, 3000, true);
				Game.spawnTemporaryObject(openedDoor2, 3000, true);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param player
	 * @param object
	 * @return
	 */
	public static boolean passDoor(Player player, GlobalObject object, int time) {
		if (Game.isSpawnedObject(object))
			return false;
		GlobalObject openedDoor = new GlobalObject(object.getId(),
				object.getType(), object.getRotation() + 1, object.getX(),
				object.getY(), object.getZ());
		if (object.getRotation() == 0)
			openedDoor.moveLocation(-1, 0, 0);
		else if (object.getRotation() == 1)
			openedDoor.moveLocation(0, 1, 0);
		else if (object.getRotation() == 2)
			openedDoor.moveLocation(1, 0, 0);
		else if (object.getRotation() == 3)
			openedDoor.moveLocation(0, -1, 0);
		if (Game.removeTemporaryObject(object, time, true)) {
			player.faceObject(openedDoor);
			Game.spawnTemporaryObject(openedDoor, time, true);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param player
	 * @param object
	 * @return
	 */
	public static boolean passDoor(Player player, GlobalObject object) {
		return passDoor(player, object, 3000);
	}

	/**
	 * 
	 * @param player
	 * @param object
	 * @param optionId
	 * @return
	 */
	public static boolean navigateStaircase(Player player, GlobalObject object,
			int optionId) {
		String option = object.defs().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getZ() == 3)
				return false;
			player.useStairs(-1, new Location(player.getX(), player.getY(),
					player.getZ() + 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getZ() == 0)
				return false;
			player.useStairs(-1, new Location(player.getX(), player.getY(),
					player.getZ() - 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getZ() == 3 || player.getZ() == 0)
				return false;
			player.getMatrixDialogues().startDialogue(
					"ClimbNoEmoteStairs",
					new Location(player.getX(), player.getY(),
							player.getZ() + 1),
					new Location(player.getX(), player.getY(),
							player.getZ() - 1), "Go up the stairs.",
					"Go down the stairs.");
		} else
			return false;
		return false;
	}

	/**
	 * 
	 * @param player
	 * @param object
	 * @param optionId
	 * @return
	 */
	public static boolean climbLadder(Player player, GlobalObject object, int optionId) {
		String option = object.defs().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getZ() == 3)
				return false;
			player.useStairs(828, new Location(player.getX(), player.getY(),
					player.getZ() + 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getZ() == 0)
				return false;
			player.useStairs(828, new Location(player.getX(), player.getY(),
					player.getZ() - 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getZ() == 3 || player.getZ() == 0)
				return false;
			player.getMatrixDialogues().startDialogue(
					"ClimbEmoteStairs",
					new Location(player.getX(), player.getY(),
							player.getZ() + 1),
					new Location(player.getX(), player.getY(),
							player.getZ() - 1), "Climb up the ladder.",
					"Climb down the ladder.", 828);
		} else
			return false;
		return true;
	}

}
