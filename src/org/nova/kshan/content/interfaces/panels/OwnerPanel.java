package org.nova.kshan.content.interfaces.panels;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.npc.NPC;
import org.nova.kshan.content.interfaces.BookTab;
import org.nova.kshan.content.interfaces.buttonstroke.ButtonStroke;
import org.nova.kshan.content.keystroke.KeyStroke;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.dialogues.impl.AddObjectSpawn;
import org.nova.kshan.gameworld.GlobalObjectSpawns;
import org.nova.kshan.input.type.integer.IntegerInput;
import org.nova.kshan.input.type.string.StringInput;
import org.nova.utility.loading.npcs.NPCSpawns;

/**
 * 
 * @author K-Shan
 *
 */
public class OwnerPanel extends Dialogue {
	
	private String s = "first";
	private boolean didObjectAction = false;

	@Override
	public void start() {
		sendOptions("Welcome, <img=1>"+player.getDisplayName()+".", 
			"Player options", "In-Game options", "Stuff just for you");
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		switch(s) {
			case "first":
				if(buttonId == OPTION1_OTHER) {
					sendOptions("What would you like to do?", 
						"Mute a player", "Kick a player", "Ban a player", "IP-Ban a player", "Next...");
					s = "punishment";
				} else if(buttonId == OPTION2_OTHER) {
					sendOptions(TITLE, "Add Global Object spawn", "Add Global NPC spawn", 
						"Reset NPC Spawns", "Add Ground Item spawn", "Next...");
					s = "spawn_options";
				} else {
					sendOptions(TITLE, "Fuck with people", "Say CHEESE!");
				}
				break;
			case "punishment":
				end();
				if(buttonId == 1) {
					// TODO
				} else if(buttonId == 2) {
					// TODO
				} else if(buttonId == 3) {
					// TODO
				} else if(buttonId == 4) {
					// TODO
				} else {
					// TODO
				}
				break;
			case "spawn_options":
				if(buttonId == 1) {
					sendOptions(TITLE, "Add the game object by using a dialogue", "Add the game object by using key strokes", "Cancel");
					s = "object_spawn_options";
				} else if(buttonId == 2) {
					end();
					player.getInputEvent().run(new StringInput() {

						@Override
						public void process(String input) {
							String[] tokens = input.split(" ");
							int id = Integer.parseInt(tokens[0]);
							boolean walks = tokens.length > 1 ? Boolean.parseBoolean(tokens[1]) : true;
							NPCSpawns.addRealSpawn(player.getUsername(), id, player, walks);
							Game.spawnNPC(id, player, walks);
							player.sm("Added new npc spawn: "+id+" - "+player.getLocation().toString()+" - "+walks);
						}

						@Override
						public void whileTyping(int key, char keyChar, boolean shiftHeld) {
							
						}
						
					}, "Enter the NPC's ID: (Location: "+player.getLocation().toString()+")");
				} else if(buttonId == 3) {
					Game.getSpawnedNPCs().clear();
					player.sm("Reset npcs.");
					for(NPC n : Game.getNPCs())
						n.sendDeath(n);
					end();
				} else if(buttonId == 4) {
					player.sm("Add ground item spawn...");
					end();
				} else {
					// TODO
				}
				break;
			case "object_spawn_options":
				if(buttonId == 2) {
					end();
					player.getInputEvent().run(new IntegerInput() {

						@Override
						public void process(int input) {
							GlobalObject object = new GlobalObject(input, 10, 0, player);
							player.getDialogue().start(new AddObjectSpawn(), object);
						}

						@Override
						public void whileTyping(int key, char keyChar, boolean shiftHeld) {
							
						}
						
					}, "Enter the Object's ID: (Location: "+player.getLocation().toString()+")");
				} else if(buttonId == 3) {
					end();
					addObjectWithKeyStrokes();
				} else
					end();
				break;
		}
	}

	@Override
	public void finish() {
		
	}
	
	/**
	 * Adds a new object using key strokes.
	 */
	private void addObjectWithKeyStrokes() {
		/*player.interfaces().sendInventoryInterface(114);
		player.getInputEvent().run(new IntegerInput() {

			@Override
			public void process(int input) {
				GlobalObject object = new GlobalObject(input, 10, 0, player);
				player.packets().sendSpawnedObject(object);
				// TODO and have configs working: cfgs 261-266
			}
		}, "Enter the new Object's ID: (Location: "+player.getLocation().toString()+")");
		
		*/
		 
		 
		 player.getInputEvent().run(new IntegerInput() {

			@Override
			public void process(int input) {
				GlobalObject object = new GlobalObject(input, 10, 0, player);
				player.packets().sendSpawnedObject(object);
				player.interfaces().setKeepChatboxOpen(true);
				player.packets().sendHideIComponent(214, 6, true);
				sendLines("<col=ff0000>"+object.getInformation(), 
					"A panel has opened up in your book tab. Controls:", 
					"E - Decrement rotation | R - Increment Rotation", 
					"A - Decrement X-Coord | D - Increment X-Coord", 
					"W - Decrement Y-Coord | S - Increment Y-Coord");
				player.packets().sendString("<col=ff0000>"+object.getName(), 506, 0);
				player.packets().sendString("<col=ffffff>Add Object", 506, 2);
				player.packets().sendString("<col=ffffff>Change ID", 506, 4);
				player.packets().sendString("<col=ffffff>Modify Type", 506, 6);
				player.packets().sendString("<col=ffffff>Cancel", 506, 8);
				player.packets().sendHideIComponent(506, 10, true);
				player.packets().sendHideIComponent(506, 12, true);
				player.packets().sendHideIComponent(506, 14, true);
				player.getKeyStrokes().run(new KeyStroke() {

					@Override
					public void press(int key, char keyChar, boolean shiftHeld) {
						if(didObjectAction) {
							terminate();
							return;
						}
						player.packets().sendHideIComponent(214, 6, true);
						String[] keys = { "e", "r", "w", "a", "s", "d" };
						for(String k : keys)
							if(getKey(key).equals(k))
								player.packets().sendDestroyObject(object);
						switch(getKey(key)) {
							case "e":
								object.setRotation(object.getRotation() - 1);
								break;
							case "r":
								object.setRotation(object.getRotation() + 1);
								break;
							case "a":
								object.setCoords(object.getX() - 1, object.getY(), player.getZ());
								break;
							case "d":
								object.setCoords(object.getX() + 1, object.getY(), player.getZ());
								break;
							case "s":
								object.setCoords(object.getX(), object.getY() - 1, player.getZ());
								break;
							case "w":
								object.setCoords(object.getX(), object.getY() + 1, player.getZ());
								break;
						}
						for(String k : keys)
							if(getKey(key).equals(k))
								player.packets().sendSpawnedObject(object);
						sendLines("<col=ff0000>"+object.getInformation(), 
							"A panel has opened up in your book tab. Controls:", 
							"E - Decrement rotation | R - Increment Rotation", 
							"A - Decrement X-Coord | D - Increment X-Coord", 
							"W - Decrement Y-Coord | S - Increment Y-Coord");
					}
					
				});
				player.getButtonStrokes().run(new ButtonStroke() {

					@Override
					public boolean press(int interfaceId, int buttonId, int slotId, int packetId) {
						if(interfaceId == 506) {
							switch(buttonId) {
								case 2:
									didObjectAction = true;
									try {
										BufferedWriter w = new BufferedWriter(new FileWriter(GlobalObjectSpawns.OBJECT_SPAWNS_FILE, true));
										w.write(object.getId()+" : "+object.getX()+", "+object.getY()+", "+object.getZ()+", "+object.getType()+""
											+ ", "+object.getRotation()+", "+object.defs().isProjectileClipped());
										w.newLine();
										w.close();
										player.sm("Added new object spawn: "+object.getInformation());
									} catch(Exception e) {
										e.printStackTrace();
									}
									didObjectAction = true;
									player.packets().sendDestroyObject(object);
									player.interfaces().setKeepChatboxOpen(false);
									BookTab.sendPanel(player);
									player.interfaces().closeChatBoxInterface();
									terminate();
									player.packets().sendHideIComponent(506, 10, false);
									player.packets().sendHideIComponent(506, 12, false);
									player.packets().sendHideIComponent(506, 14, false);
									Game.spawnObject(object, object.defs().isProjectileClipped());
									break;
								case 4:
									player.packets().sendDestroyObject(object);
									player.packets().sendSpawnedObject(object);
									player.getInputEvent().run(new IntegerInput() {

										@Override
										public void process(int input) {
											object.setId(input);
											player.packets().sendDestroyObject(object);
											player.packets().sendSpawnedObject(object);
											player.packets().sendString("<col=ff0000>"+object.getName(), 506, 0);
											sendLines("<col=ff0000>"+object.getInformation(), 
												"A panel has opened up in your book tab. Controls:", 
												"I - Decrement rotation | O - Increment Rotation", 
												"J - Decrement X-Coord | K - Increment X-Coord", 
												"N - Decrement Y-Coord | M - Increment Y-Coord");
										}

										@Override
										public void whileTyping(int key, char keyChar, boolean shiftHeld) {
											
										}
										
									}, "Enter the new object id:");
									break;
								case 6:
									player.packets().sendDestroyObject(object);
									player.packets().sendSpawnedObject(object);
									player.getInputEvent().run(new IntegerInput() {

										@Override
										public void process(int input) {
											object.setType(input);
											player.packets().sendDestroyObject(object);
											player.packets().sendSpawnedObject(object);
											sendLines("<col=ff0000>"+object.getInformation(), 
												"A panel has opened up in your book tab. Controls:", 
												"I - Decrement rotation | O - Increment Rotation", 
												"J - Decrement X-Coord | K - Increment X-Coord", 
												"N - Decrement Y-Coord | M - Increment Y-Coord");
										}

										@Override
										public void whileTyping(int key, char keyChar, boolean shiftHeld) {
											
										}
										
									}, "Enter new type: ("+object.getInformation()+")");
									break;
								case 8:
									didObjectAction = true;
									player.packets().sendDestroyObject(object);
									player.interfaces().setKeepChatboxOpen(false);
									BookTab.sendPanel(player);
									player.interfaces().closeChatBoxInterface();
									terminate();
									player.packets().sendHideIComponent(506, 10, false);
									player.packets().sendHideIComponent(506, 12, false);
									player.packets().sendHideIComponent(506, 14, false);
									break;
							}
							return false;
						}
						return true;
					}
					
				});
			}

			@Override
			public void whileTyping(int key, char keyChar, boolean shiftHeld) {
				
			}
			
		}, "Enter the new Object's ID: (Location: "+player.getLocation().toString()+")");
		 
		 
		 
	}

}
