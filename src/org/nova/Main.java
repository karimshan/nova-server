package org.nova;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.nova.cache.Cache;
import org.nova.cache.definition.ItemDefinition;
import org.nova.cache.definition.NPCDefinition;
import org.nova.cache.definition.ObjectDefinition;
import org.nova.cache.files.Index;
import org.nova.cache.loaders.ItemEquipIds;
import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.map.Region;
import org.nova.game.map.RegionBuilder;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScriptsHandler;
import org.nova.game.player.Player;
import org.nova.game.player.content.FishingSpotsHandler;
import org.nova.game.player.content.FriendChatsManager;
import org.nova.game.player.controlers.ControllerHandler;
import org.nova.game.player.dialogues.MatrixDialogueHandler;
import org.nova.kshan.bot.action.BotActionHandler;
import org.nova.kshan.content.areas.Area;
import org.nova.kshan.content.cutscenes.Cutscenes;
import org.nova.kshan.content.keystroke.KeyStrokeData;
import org.nova.kshan.content.skills.construction.RoomList;
import org.nova.kshan.content.skills.construction.RoomObjects;
import org.nova.kshan.dialogues.DialogueManager;
import org.nova.kshan.gameworld.DeletedObjects;
import org.nova.kshan.gameworld.GlobalObjectSpawns;
import org.nova.kshan.input.InputEvents;
import org.nova.kshan.randoms.RandomEvents;
import org.nova.kshan.randoms.dependencies.DynamicMapList;
import org.nova.kshan.utilities.Punishments;
import org.nova.kshan.utilities.TimeUtils;
import org.nova.network.ServerChannelHandler;
import org.nova.utility.Censor;
import org.nova.utility.ShopsHandler;
import org.nova.utility.TasksManager;
import org.nova.utility.huffman.Huffman;
import org.nova.utility.loading.Logger;
import org.nova.utility.loading.items.ItemBonuses;
import org.nova.utility.loading.items.ItemExamines;
import org.nova.utility.loading.map.MapAreas;
import org.nova.utility.loading.map.MapContainersXteas;
import org.nova.utility.loading.map.ObjectExamines;
import org.nova.utility.loading.map.ObjectSpawns;
import org.nova.utility.loading.npcs.NCDLoader;
import org.nova.utility.loading.npcs.NPCBonuses;
import org.nova.utility.loading.npcs.NPCDrops;
import org.nova.utility.loading.npcs.NPCExamines;
import org.nova.utility.loading.npcs.NPCHoverMessages;
import org.nova.utility.loading.npcs.NPCSpawning;
import org.nova.utility.loading.npcs.NPCSpawns;
import org.nova.utility.loading.playerutils.DominionTowerRank;
import org.nova.utility.loading.playerutils.SFiles;
import org.nova.utility.misc.Misc;

public final class Main {

	public static void main(String[] args) {
		long currentTime = Misc.currentTimeMillis();
		Game.initing = true;
		Logger.log("Server", "Starting up the server...");
		Cache.load();
		ItemEquipIds.init();
		Huffman.init();
		MapContainersXteas.init();
		MapAreas.init();
		ObjectSpawns.init();
		NPCSpawns.init();
		NCDLoader.init();
		NPCBonuses.init();
		NPCDrops.init();
		Censor.initialize();
		ShopsHandler.init();
		FishingSpotsHandler.init();
		CombatScriptsHandler.init();
		MatrixDialogueHandler.init();
		ControllerHandler.init();
		FriendChatsManager.init();
		CoresManager.init();
		tasks = Executors.newScheduledThreadPool(1);
		ObjectExamines.loadExamines();
		ItemExamines.loadExamines();
		NPCExamines.loadExamines();
		Logger.log("Examines", "Loaded all NPC, Object, and Item Examines.");
		ItemBonuses.loadBonuses();
		Logger.log("Item Bonuses", "Loaded all Item Bonuses.");
		Game.init();
		RandomEvents.addAll();
		Cutscenes.addAll();
		BotActionHandler.addAll();
		KeyStrokeData.addKeys();
		DialogueManager.addAll();
		RegionBuilder.init();
		Game.instantSendGroundItems();
		InputEvents.addAll();
		Area.initialize();
		submitGlobalTasks();
		RoomObjects.loadRoomObjects();
		RoomObjects.loadBuildables();
		RoomList.loadRooms();
		Logger.log("Construction", "Loaded construction data.");
		//DialogueScripts.load();
		//Logger.log("Dialogue Scripts", "Loaded all dialogue scripts.");
		// TODO this maybe never
		TasksManager.loadLDTasks();
		//Logger.log("Construction", "Constructing homes, please wait...");
		//constructHomes();
		Punishments.loadPermanentPunishments();
		Logger.log("Punishments", "Loaded permanent punishment lists.");
		try {
			ServerChannelHandler.init();
		} catch (Exception e) {
			Logger.log("Server", "Couldn't load the Server Channel.");
			e.printStackTrace();
			System.exit(1);
			return;
		}
		Logger.log("Server", "Nova launched in " + ((double) (Misc.currentTimeMillis() - currentTime) / 1000)+ " seconds.");
		addAccountsSavingTask();
		addCleanMemoryTask();
		createDynamicMaps();
		NPCSpawning.spawnNPCS();
		NPCSpawns.loadSpawns();
		for(NPC n : Game.getNPCs())
			if(n.getCombatLevel() < 0 && n.getName().equals("null"))
				n.finish();
		DeletedObjects.delete();
		GlobalObjectSpawns.spawnObjects();
		NPCHoverMessages.load();
	}
	
	private static void createDynamicMaps() {
		DynamicMapList.EXCLUSIVE_MAPS.clear();
		DynamicMapList.MAPS.clear();
		DynamicMapList.addExclusiveMap("Sandwich Lady", 2, 2, 238, 640);
		DynamicMapList.addExclusiveMap("Cavern of Remembrance", 7, 5, 248, 632);
		Game.getRegion(Game.getRegions().size() - 1, true);
	}
	
	@SuppressWarnings("unused")
	private static void constructHomes() {
		boolean empty = true;
		try {
			BufferedReader r = new BufferedReader(new FileReader("data/playerdata/homesToConstruct.txt"));
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith("//") || line.equals(""))
					continue;
				String name = line.split(" - ")[0];
				long lastPut = Long.parseLong(line.split(" - ")[1]);
				Player p = SFiles.loadPlayer(name);
				if(p != null)
					empty = false;
				if(!empty) {
					p.setUsername(name);
					if((lastPut + TimeUtils.getDays(3)) < System.currentTimeMillis()) {
					//	p.getHouse().setKeptOpen(false);
					//	p.getHouse().addToHomesToConstructQueue(true);
						Logger.log("Construction", p.getDisplayName()+" has been inactive and his house will not be constructed.");
						SFiles.savePlayer(p);
					}
					//if(p.getHouse().isKeptOpen()) {
						p.getHouse().parseLandscape();
						Logger.log("Construction", "Successfully created "+p.getDisplayName()+"'s house.");
				//	}
				}
			}
			r.close();
			if(!empty) {
				Thread.sleep(2400); // To wait for region to finish building
				Logger.log("Construction:", "Finished constructing homes in the queue.");
			} else
				Logger.log("Construction", "Finished scanning. There were no homes to construct.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void setWebsitePlayersOnline(int amount) throws IOException {
		URL url = new URL(
				"http://Nova-rs.com/register.php?amount=" + amount);
		url.openStream().close();
	}

	@SuppressWarnings("unused")
	private static void addUpdatePlayersOnlineTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					setWebsitePlayersOnline(Game.getPlayers().size());
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}, 2, 2, TimeUnit.MINUTES);
	}

	private static void addCleanMemoryTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					cleanMemory(Runtime.getRuntime().freeMemory() < Constants.MIN_FREE_MEM_ALLOWED);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}, 0, 10, TimeUnit.MINUTES);
	}

	private static void addAccountsSavingTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					saveAccounts();
					System.out.println("Saved all players, safe to shutdown or restart.");
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 20, TimeUnit.SECONDS);
	}
	
	public static final void saveAccounts() {
		for (Player player : Game.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished())
				continue;
			SFiles.savePlayer(player);
		}
		Punishments.saveFiles();
		DominionTowerRank.save();
	}

	public static void saveFiles() {
		for (Player player : Game.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished())
				continue;
			SFiles.savePlayer(player);
		}
		Punishments.saveFiles();
		DominionTowerRank.save();
	}

	public static void cleanMemory(boolean force) {
		if (force) {
			ItemDefinition.itemDefinitions.clear();
			NPCDefinition.clearNPCDefinitions();
			ObjectDefinition.clearObjectDefinitions();
			for (Region region : Game.getRegions().values())
				region.removeMapFromMemory();
		}
	for (Index index : Cache.INSTANCE.getIndices())
			index.resetCachedFiles();
		CoresManager.fastExecutor.purge();
		System.gc();
	}

	public static void shutdown() {
		try {
			closeServices();
		} finally {
			System.exit(0);
		}
	}

	public static void closeServices() {
		ServerChannelHandler.shutdown();
		CoresManager.shutdown();
	}

	public static void oldrestart() {
		closeServices();
		System.gc();
		try {
			System.exit(2);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void restartApplication(Runnable runBeforeRestart) {
		try {
			// java binary
			String java = System.getProperty("java.home") + "/bin/java";
			// vm arguments
			List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
			StringBuffer vmArgsOneLine = new StringBuffer();
			for (String arg : vmArguments) {
				// if it's the agent argument : we ignore it otherwise the
				// address of the old application and the new one will be in conflict
				if (!arg.contains("-agentlib")) {
					vmArgsOneLine.append(arg);
					vmArgsOneLine.append(" ");
				}
			}
			// init the command to execute, add the vm args
			final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);

			// program main and program arguments
			String[] mainCommand = System.getProperty("sun.java.command").split(" ");
			// program main is a jar
			if (mainCommand[0].endsWith(".jar")) {
				// if it's a jar, add -jar mainJar
				cmd.append("-jar " + new File(mainCommand[0]).getPath());
			} else {
				// else it's a .class, add the classpath and mainClass
				cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
			}
			// finally add program arguments
			for (int i = 1; i < mainCommand.length; i++) {
				cmd.append(" ");
				cmd.append(mainCommand[i]);
			}
			new Thread() {
				public void run() {
					closeServices();
					System.gc();
				}
			}.start();
			System.out.println((cmd.toString()));
			// execute the command in a shutdown hook, to be sure that all the
			// resources have been disposed before restarting the application
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						Runtime.getRuntime().exec(cmd.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			// execute some custom code before restarting
			if (runBeforeRestart != null) {
				runBeforeRestart.run();
			}
			// exit
			System.exit(0);
		} catch (Exception e) {
			// something went wrong
			System.out.println("Error while trying to restart the application: "+e.getMessage());
		}
	}

	private Main() {

	}
	
	private static final void submitGlobalTasks() {
		Game.submit(new GameTick(1.0) {
			@Override
			public void run() {
				for(Player p : Game.getPlayers()) {
					p.interfaces().sendBookInfo();
					p.playTime().processPlayTime();
					for(NPC n : Game.getNPCs())
						if(n != null && n.getCombatLevel() < 0 && n.getName().equals("null"))
							n.finish();
				}
			}
		});
	}

	/**
	 * @param task
	 */
	private static ScheduledExecutorService tasks;

	/**
	 * 
	 * @param runnable
	 * @param delay
	 * @param unit
	 */
	public static void scheduleTask(final Runnable runnable, long delay, TimeUnit unit) {
		tasks.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					runnable.run();
				} catch (Exception e) {
				} catch (Throwable t) {
				}
			}
		}, delay, unit);
	}

	/**
	 * 
	 * @param task
	 */
	public static void submit(final Runnable task) {
		tasks.submit(new Runnable() {
			@Override
			public void run() {
				try {
					task.run();
				} catch (Exception e) {
				} catch (Throwable t) {
				}
			}
		});
	}

}
