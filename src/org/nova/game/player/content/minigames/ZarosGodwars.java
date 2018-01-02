package org.nova.game.player.content.minigames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.godwars.zaros.Nex;
import org.nova.game.npc.godwars.zaros.NexMinion;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;


public final class ZarosGodwars {

	private static final List<Player> playersOn = Collections
			.synchronizedList(new ArrayList<Player>());
	// private static final Object LOCK = new Object();

	public static Nex nex;
	public static NexMinion fumus;
	public static NexMinion umbra;
	public static NexMinion cruor;
	public static NexMinion glacies;

	public static int getPlayersCount() {
		return playersOn.size();
	}

	public static void breakFumusBarrier() {
		// synchronized(LOCK) {
		if (fumus == null)
			return;
		fumus.breakBarrier();
		// }
	}

	public static void breakUmbraBarrier() {
		// synchronized(LOCK) {
		if (umbra == null)
			return;
		umbra.breakBarrier();
		// }
	}

	public static void breakCruorBarrier() {
		// synchronized(LOCK) {
		if (cruor == null)
			return;
		cruor.breakBarrier();
		// }
	}

	public static void breakGlaciesBarrier() {
		// synchronized(LOCK) {
		if (glacies == null)
			return;
		glacies.breakBarrier();
		// }
	}

	public static void addPlayer(Player player) {
		// synchronized(LOCK) {
		if (playersOn.contains(player)) {
			// System.out.println("ERROR DOUBLE ENTRY!");
			return;
		}
		playersOn.add(player);
		startWar();
		// }
	}

	public static void removePlayer(Player player) {
		// synchronized(LOCK) {
		playersOn.remove(player);
		cancelWar();
		// }
	}

	public static void deleteNPCS() {
		if (nex != null) {
			nex.killBloodReavers();
			nex.finish();
			nex = null;
		}
		if (fumus != null) {
			fumus.finish();
			fumus = null;
		}
		if (umbra != null) {
			umbra.finish();
			umbra = null;
		}
		if (cruor != null) {
			cruor.finish();
			cruor = null;
		}
		if (glacies != null) {
			glacies.finish();
			glacies = null;
		}
	}

	private static void cancelWar() {
		if (getPlayersCount() == 0)
			deleteNPCS();
	}

	public static ArrayList<Entity> getPossibleTargets() {
		// synchronized(LOCK) {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>(
				playersOn.size());
		for (Player player : playersOn) {
			if (player == null || player.isDead() || player.hasFinished()
					|| !player.isRunning())
				continue;
			possibleTarget.add(player);
			/*
			 * if (player.getFamiliar() != null &&
			 * player.getFamiliar().isAgressive())
			 * possibleTarget.add(player.getFamiliar());
			 */
		}
		return possibleTarget;
		// }
	}

	public static void moveNextStage() {
		// synchronized(LOCK) {
		if (nex == null)
			return;
		nex.moveNextStage();
		// }
	}

	public static void endWar() {
		// synchronized(LOCK) {
		deleteNPCS();
		CoresManager.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				startWar();
			}

		}, 1, TimeUnit.MINUTES);
		// }
	}

	private static void startWar() {
		if (getPlayersCount() >= 1) {
			if (nex == null) {
				Game.spawnNPC(13447, new Location(2924, 5202, 0), -1, true,
						true);
				WorldTasksManager.schedule(new WorldTask() {
					private int count = 0;

					@Override
					public void run() {
						// synchronized(LOCK) {
						if (nex == null) {
							stop();
							return;
						}
						if (count == 1) {
							nex.setNextForceTalk(new ForceTalk("AT LAST!"));
							nex.setNextAnimation(new Animation(6355));
							nex.setNextGraphics(new Graphics(1217));
							nex.playSound(3295, 2);
						} else if (count == 3) {
							Game.spawnNPC(13451, new Location(2912, 5216, 0),
									-1, true, true);
							fumus.setDirection(Misc.getFaceDirection(1, -1));
							nex.faceLocation(new Location(fumus
									.getCoordFaceX(fumus.getSize()), fumus
									.getCoordFaceY(fumus.getSize()), 0));
							nex.setNextForceTalk(new ForceTalk("Fumus!"));
							nex.setNextAnimation(new Animation(6987));
							Game.sendProjectile(fumus, nex, 2244, 18, 18, 60,
									30, 0, 0);
							nex.playSound(3325, 2);
						} else if (count == 5) {
							Game.spawnNPC(13452, new Location(2937, 5216, 0),
									-1, true, true);
							umbra.setDirection(Misc.getFaceDirection(-1, -1));
							nex.faceLocation(new Location(umbra
									.getCoordFaceX(umbra.getSize()), umbra
									.getCoordFaceY(umbra.getSize()), 0));
							nex.setNextForceTalk(new ForceTalk("Umbra!"));
							nex.setNextAnimation(new Animation(6987));
							Game.sendProjectile(umbra, nex, 2244, 18, 18, 60,
									30, 0, 0);
							nex.playSound(3313, 2);
						} else if (count == 7) {
							Game.spawnNPC(13453, new Location(2937, 5190, 0),
									-1, true, true);
							cruor.setDirection(Misc.getFaceDirection(-1, 1));
							nex.faceLocation(new Location(cruor
									.getCoordFaceX(cruor.getSize()), cruor
									.getCoordFaceY(cruor.getSize()), 0));
							nex.setNextForceTalk(new ForceTalk("Cruor!"));
							nex.setNextAnimation(new Animation(6987));
							Game.sendProjectile(cruor, nex, 2244, 18, 18, 60,
									30, 0, 0);
							nex.playSound(3299, 2);
						} else if (count == 9) {
							Game.spawnNPC(13454, new Location(2912, 5190, 0),
									-1, true, true);
							glacies.faceLocation(new Location(glacies
									.getCoordFaceX(glacies.getSize()), glacies
									.getCoordFaceY(glacies.getSize()), 0));
							glacies.setDirection(Misc.getFaceDirection(1, 1));
							nex.faceLocation(new Location(glacies
									.getCoordFaceX(glacies.getSize()), glacies
									.getCoordFaceY(glacies.getSize()), 0));
							nex.setNextForceTalk(new ForceTalk("Glacies!"));
							nex.setNextAnimation(new Animation(6987));
							Game.sendProjectile(glacies, nex, 2244, 18, 18,
									60, 30, 0, 0);
							nex.playSound(3304, 2);
						} else if (count == 11) {
							nex.setNextForceTalk(new ForceTalk(
									"Fill my soul with smoke!"));
							Game.sendProjectile(fumus, nex, 2244, 18, 18, 60,
									30, 0, 0);

							nex.playSound(3310, 2);
						} else if (count == 13) {
							nex.setCantInteract(false);
							stop();
							return;
						}
						count++;
					}
					// }
				}, 0, 1);
			}
		}
	}

	private ZarosGodwars() {

	}
}
