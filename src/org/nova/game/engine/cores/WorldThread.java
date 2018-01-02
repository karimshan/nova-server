package org.nova.game.engine.cores;

import org.nova.Constants;
import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.kshan.bot.Bot;
import org.nova.utility.misc.Misc;

public final class WorldThread extends Thread {

	protected WorldThread() {
		setPriority(Thread.MAX_PRIORITY);
		setName("World Thread");
	}

	@Override
	public final void run() {
		while (!CoresManager.shutdown) {
			long currentTime = Misc.currentTimeMillis();
			try {
				// long debug = Utils.currentTimeMillis();
				WorldTasksManager.processTasks();
				// System.out.print("TASKS: "+(Utils.currentTimeMillis()-debug));
				// debug = Utils.currentTimeMillis();
				for (Player player : Game.getPlayers()) {
					if ((player == null || !player.hasStarted()
							|| player.hasFinished()) && (!(player instanceof Bot)))
						continue;
					if (!(player instanceof Bot)
							&& (currentTime - player.getPacketsDecoderPing() > Constants.MAX_PACKETS_DECODER_PING_DELAY
							&& player.getSession().getChannel().isOpen()))
						player.getSession().getChannel().close();
					player.processEntity();
				}
				// System.out.print(" ,PLAYERS PROCESS: "+(Utils.currentTimeMillis()-debug));
				// debug = Utils.currentTimeMillis();
				for (NPC npc : Game.getNPCs()) {
					if (npc == null || npc.hasFinished())
						continue;
					npc.processEntity();
				}
			} catch (Exception e) {
				System.out.println("Error occurred in WorldThread.");
				e.printStackTrace();
			}
			try {
				// System.out.print(" ,NPCS PROCESS: "+(Utils.currentTimeMillis()-debug));
				// debug = Utils.currentTimeMillis();

				for (Player player : Game.getPlayers()) {
					if (player == null || !player.hasStarted()
							|| player.hasFinished())
						continue;
					player.packets().sendLocalPlayersUpdate();
					player.packets().sendLocalNPCsUpdate();
				}
				// System.out.print(" ,PLAYER UPDATE: "+(Utils.currentTimeMillis()-debug)+", "+World.getPlayers().size()+", "+World.getNPCs().size());
				// debug = Utils.currentTimeMillis();
				for (Player player : Game.getPlayers()) {
					if (player == null || !player.hasStarted()
							|| player.hasFinished()) {
						continue;
					}
					player.resetMasks();
				}
				for (NPC npc : Game.getNPCs()) {
					if (npc == null || npc.hasFinished())
						continue;
					npc.resetMasks();
				}
			} catch (Exception e) {
				System.out.println("Error occurred in WorldThread.");
				e.printStackTrace();
			}
			// System.out.println(" ,TOTAL: "+(Utils.currentTimeMillis()-currentTime));
			LAST_CYCLE_CTM = Misc.currentTimeMillis();
			long sleepTime = Constants.WORLD_CYCLE_TIME + currentTime
					- LAST_CYCLE_CTM;
			if (sleepTime <= 0)
				continue;
			try {
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				System.out.println("Error occurred in WorldThread.");
				e.printStackTrace();
			}
		}
	}

	public static long LAST_CYCLE_CTM;

}
