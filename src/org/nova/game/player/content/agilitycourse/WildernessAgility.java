package org.nova.game.player.content.agilitycourse;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceMovement;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Agility;

public class WildernessAgility {
	
	public static void GateWalk(final Player player, final GlobalObject object) {
		if(!Agility.hasLevel(player, 35))
			return;
		if(player.getY() != object.getY()) {
			player.addWalkSteps(2998, 3916, 0, false);
			player.lock(2);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					GateWalkEnd(player, object);
				}
			}, 1);
		}else
			GateWalkEnd(player, object);
	}

	private static void GateWalkEnd(final Player player, GlobalObject object) {
		player.packets().sendMessage("You walk carefully across the path...", true);
		player.lock(17);
		player.setNextAnimation(new Animation(9908));
		final Location toTile = new Location(object.getX(), 3931, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 16, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setLocation(toTile);
				player.packets().sendMessage("... and make it safely to the other side.", true);
			}	

		}, 15);
	}

	public static void enterObstaclePipe(final Player player, int objectX, int objectY) {
		if(!Agility.hasLevel(player, 35))
			return;
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(objectX, objectY == 3938 ? 3950 : 3937, -1, false);
		player.packets().sendMessage(
				"You pulled yourself through the pipes.", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(295);
				} else {
					player.getAppearance().setRenderEmote(-1);
					setWildernessStage(player, 0);
					player.setRunHidden(running);
					player.getSkills().addXp(Skills.AGILITY, 7);
					stop();
				}
			}
		}, 0, 6);
	}

	public static void swingOnRopeSwing(final Player player, GlobalObject object) {
		if(!Agility.hasLevel(player, 35))
			return;
		if(player.getY() != 3953)
			player.addWalkSteps(3005, 3953, 0, false);
		player.lock(4);
		player.setNextAnimation(new Animation(751));
		Game.sendObjectAnimation(player, object, new Animation(497));
		final Location toTile = new Location(object.getX(), 3958, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.NORTH));
		player.getSkills().addXp(Skills.AGILITY, 22);
		player.packets().sendMessage("You skilfully swing across.", true);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setLocation(toTile);
				if (getWildernessStage(player) == 0)
					setWildernessStage(player, 1);
				player.getSkills().addXp(Skills.AGILITY, 15.5);
			}	

		}, 1);
	}

	/*
	 * Stepping Stone Method by Cjay0091
	 */
	public static void crossSteppingPalletes(final Player player, final GlobalObject object) {
		if (player.getY() != object.getY())
			return;
		player.lock(6);
		WorldTasksManager.schedule(new WorldTask() {

			int x;

			@Override
			public void run() {
				if (x++ == 6) {
					stop();
					return;
				}
				final Location toTile = new Location(3002 - x, player.getY(), player.getZ());
				player.setNextForceMovement(new ForceMovement(toTile, 1, ForceMovement.WEST));
				player.setNextAnimation(new Animation(741));
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						player.setLocation(toTile);
					}
				}, 0);
			}	
		}, 2, 1);
		player.getSkills().addXp(Skills.AGILITY, 20);
		if (getWildernessStage(player) == 1)
			setWildernessStage(player, 2);
	}

	public static void walkLog(final Player player) {
		if (player.getX() != 3002 || player.getY() != 3945)
			player.addWalkSteps(3002, 3945, -1, false);
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(2994, 3945, -1, false);
		player.packets().sendMessage(
				"You walk carefully across the log...", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					player.getSkills().addXp(Skills.AGILITY, 7.5);
					player.packets().sendMessage(
							"... and make it safely to the other side.", true);
					stop();
					if (getWildernessStage(player) == 2)
						setWildernessStage(player, 3);
				}
			}
		}, 0, 6);
	}

	public static void climbCliff(final Player player, GlobalObject object) {
		if (player.getY() != 3939) {
			return;
		}
		player.setNextAnimation(new Animation(3378));
		final Location toTile = new Location(player.getX(), 3935, 0);

		player.packets().sendMessage("You climb up the rock.", true);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setLocation(toTile);
				player.setNextAnimation(new Animation(-1));
				player.getAppearance().setRenderEmote(-1);
				stop();
				if (getWildernessStage(player) == 3) {
					removeWildernessStage(player);
					player.getSkills().addXp(Skills.AGILITY, 50.5);

				}
			}	
		}, 6);
	}

	public static void removeWildernessStage(Player player) {
		player.getTemporaryAttributtes().remove("WildernessCourse");
	}

	public static void setWildernessStage(Player player, int stage) {
		player.getTemporaryAttributtes().put("WildernessCourse", stage);
	}

	public static int getWildernessStage(Player player) {
		Integer stage = (Integer) player.getTemporaryAttributtes().get(
				"WildernessCourse");
		if (stage == null)
			return -1;
		return stage;
	}
}