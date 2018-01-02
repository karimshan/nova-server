package org.nova.game.player.content;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class TeleportManager {

	public static final int BANDIT_CAMP_TELEPORT = 19476;
	public static final int NARDAH_TELEPORT = 19475;
	public static final int MISCELLANIA_TELEPORT = 19477;
	public static final int PHOENIX_LAIR_TELEPORT = 19478;
	public static final int TAI_BWO_WANNAI_TELEPORT = 19479;
	public static final int LUMBER_YARD_TELEPORT = 19480;

	public static void wiseOldManTeleport(final Player player, int i, int j,
			final Location tile) {
		player.addStopDelay(8);
		if (!player.getControllerManager().processItemTeleport(tile))
			return;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				for (NPC wiseMan : Game.getNPCs()) {
					player.faceEntity(wiseMan);
				}
				player.interfaces().closeChatBoxInterface();
				Location teleTile = tile;
				teleTile = new Location(tile, 2);
				teleTile = tile;
				switch (loop) {
				case 0:
					for (NPC wiseMan : Game.getNPCs()) {
						if (wiseMan.getId() == 2253) {
							if (wiseMan.getX() == 3086)
								wiseMan.setNextForceTalk(new ForceTalk(
										"May Saradomin be with you, "
												+ player.getDisplayName() + "!"));
							wiseMan.setNextAnimation(new Animation(1818));
							wiseMan.setNextGraphics(new Graphics(343));

						}
					}
					break;
				case 1:
					player.sendMessage("You feel ancient power going trough your body.");
					player.setNextAnimation(new Animation(1816));
					player.setNextGraphics(new Graphics(342));
					break;
				case 2:
					player.setLocation(WorldTeleport.getSingleton()
							.getNextTile());
					for (NPC n : Game.getNPCs()) {
						if (n.getId() == 2253)
							player.faceLocation(new Location(player.getX(),
									player.getY() - 1, player.getZ()));
						player.faceLocation(new Location(player.getX(), player
								.getY() - 1, player.getZ()));
						player.resetWalkSteps();
						player.setNextAnimation(new Animation(8941));
					}
					stop();
					break;
				}
				loop++;
			}
		}, 0, 1);
	}

	public static void SpiritTeleport(final Player player, int i, int j,
			final Location tile) {
		if (!player.getControllerManager().processItemTeleport(tile))
			return;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.addStopDelay(2);
				player.interfaces().closeChatBoxInterface();
				Location teleTile = tile;
				teleTile = new Location(tile, 2);
				teleTile = tile;
				if (loop == 0) {
					player.interfaces().sendInterface(120);
					player.setNextAnimation(new Animation(7082));
					player.setNextGraphics(new Graphics(1228));
				} else if (loop == 2) {
					player.setLocation(teleTile);
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(7084));
					player.setNextGraphics(new Graphics(1229));
					player.closeInterfaces();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	public static void preformScrollTeleportation(final Player player, int i,
			int j, final Location tile) {
		if (!player.getControllerManager().processItemTeleport(tile))
			return;
		player.addStopDelay(5);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				Location teleTile = tile;
				teleTile = new Location(tile, 2);
				teleTile = tile;
				switch (loop) {
				case 0:
					player.setNextAnimation(new Animation(14293));
					player.setNextGraphics(new Graphics(94));
					break;
				case 2:
					player.setNextAnimation(new Animation(9598));
					player.resetWalkSteps();
					player.setLocation(new Location(tile));
					stop();
					break;
				}
				loop++;
			}
		}, 0, 1);
	}

	public static void WaydarTeleport(final Player player, int i, int j,
			final Location tile) {
		player.interfaces().closeChatBoxInterface();
		if (!player.getControllerManager().processItemTeleport(tile))
			return;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.addStopDelay(2);
				Location teleTile = tile;
				teleTile = new Location(tile, 2);
				teleTile = tile;
				if (loop == 0) {
					player.interfaces().sendInterface(138);
					// player.getInterfaceManager().sendScreenInterface(120,
					// 138);
				}
				if (loop == 1) {
					player.packets().sendConfig(153, 8);
				}
				if (loop == 3) {
					player.addStopDelay(2);
					player.interfaces().sendInterface(170);
					player.setLocation(teleTile);
				}
				if (loop == 5) {
					player.interfaces().closeChatBoxInterface();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	public static void DaeroTeleport(final Player player, int i, int j,
			final Location tile) {
		if (!player.getControllerManager().processItemTeleport(tile))
			return;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.addStopDelay(2);
				Location teleTile = tile;
				teleTile = new Location(tile, 2);
				teleTile = tile;
				if (loop == 0) {
					player.packets().sendBlackOut(2);
					player.getMatrixDialogues().startDialogue("ItemMessage",
							"You wear the blindfold Daero hands you.", 2649);
					player.packets().sendHideIComponent(241, 5, true);
					if (!player.interfaces().isFullScreen())
						player.interfaces().sendInterface(120);
					else
						player.interfaces().sendInterface(120);
				} else if (loop == 2) {
					player.setLocation(teleTile);
				} else if (loop == 5) {
					player.interfaces().sendInterface(170);
					player.addStopDelay(3);
				} else if (loop == 7) {
					player.interfaces().closeChatBoxInterface();
					player.closeInterfaces();
					player.packets().sendBlackOut(0);
					player.closeInterfaces();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	public static void DaemonheimTeleport(final Player player, int i, int j,
			final Location tile) {
		if (!player.getControllerManager().processItemTeleport(tile))
			return;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				Location teleTile = tile;
				teleTile = new Location(tile, 2);
				teleTile = tile;
				player.addStopDelay(2);
				if (loop == 0) {
					player.setNextAnimation(new Animation(13652));
					player.setNextGraphics(new Graphics(2602));
				} else if (loop == 5) {
					player.setLocation(teleTile);
					// player.setNextLocation(new Location(3449, 3698, 0));
				} else if (loop == 6) {
					player.setNextAnimation(new Animation(13654));
					player.setNextGraphics(new Graphics(2603));
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
}
