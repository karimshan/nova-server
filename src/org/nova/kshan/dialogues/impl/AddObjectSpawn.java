package org.nova.kshan.dialogues.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.nova.game.map.GlobalObject;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.gameworld.GlobalObjectSpawns;
import org.nova.kshan.input.type.integer.IntegerInput;

/**
 * The dialogue I use to add object spawns. It's quite handy :3
 * 
 * @author K-Shan
 *
 */
public class AddObjectSpawn extends Dialogue {
	
	GlobalObject object;
	
	@Override
	public void start() {
		object = (GlobalObject) data[0];
		sendOptions(object.getInformation(), "Increment the object's X-coordinate", 
			"Decrement the object's X-coordinate", "Increment the object's Y-coordinate", 
				"Decrement the object's Y-coordinate", "Next...");
		player.packets().sendSpawnedObject(object);
	}
	
	@Override
	public void process(int i, int b) {
		if(stage == 0) {
			if(b == 5) {
				sendOptions(object.getInformation(), "Increment the object's rotation", "Decrement the object's rotation", 
					"Modify the object's type", "Go back to the first page...", "Next...");
				stage = 1;
			} else {
				player.packets().sendDestroyObject(object);
				if(b == 1) {
					object.setCoords(object.getX() + 1, object.getY(), player.getZ());
					player.packets().sendSpawnedObject(object);
				} else if(b == 2) {
					object.setCoords(object.getX() - 1, object.getY(), player.getZ());
					player.packets().sendSpawnedObject(object);
				} else if(b == 3) {
					object.setCoords(object.getX(), object.getY() + 1, player.getZ());
					player.packets().sendSpawnedObject(object);
				} else {
					object.setCoords(object.getX(), object.getY() - 1, player.getZ());
					player.packets().sendSpawnedObject(object);
				}
				start();
			}
		} else if(stage == 1) {
			if(b == 5) {
				sendOptions(object.getInformation(), "Change the Object's ID", "Add Object", 
					"Go back to the previous page", "Cancel");
				stage = 2;
			} else {
				player.packets().sendDestroyObject(object);
				if(b == 1) {
					object.setRotation(object.getRotation() + 1);
					player.packets().sendSpawnedObject(object);
				} else if(b == 2) {
					object.setRotation(object.getRotation() - 1);
					player.packets().sendSpawnedObject(object);
				} else if(b == 3) {
					player.packets().sendDestroyObject(object);
					player.packets().sendSpawnedObject(object);
					player.getInputEvent().run(new IntegerInput() {

						@Override
						public void process(int input) {
							object.setType(input);
							player.packets().sendDestroyObject(object);
							player.packets().sendSpawnedObject(object);
							sendOptions(object.getInformation(), "Increment the object's rotation", "Decrement the object's rotation", 
								"Modify the object's type", "Go back to the first page...", "Next...");
							stage = 1;
						}

						@Override
						public void whileTyping(int key, char keyChar, boolean shiftHeld) {
							
						}
						
					}, "Enter new type: ("+object.getInformation()+")");
				} else if(b == 4) {	
					stage = 0;
					start();
				}
				if(stage == 1)
					sendOptions(object.getInformation(), "Increment the object's rotation", "Decrement the object's rotation", 
						"Modify the object's type", "Go back to the first page...", "Next...");
			}
		} else if(stage == 2) {
			if(b == 2) {
				end();
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
				return;
			} else if(b == 1) {
				player.packets().sendDestroyObject(object);
				player.packets().sendSpawnedObject(object);
				player.getInputEvent().run(new IntegerInput() {

					@Override
					public void process(int input) {
						object.setId(input);
						player.packets().sendDestroyObject(object);
						player.packets().sendSpawnedObject(object);
						sendOptions(object.getInformation(), "Change the Object's ID", "Add Object", 
							"Go back to the previous page", "Cancel");
						stage = 2;
					}

					@Override
					public void whileTyping(int key, char keyChar, boolean shiftHeld) {
						
					}
					
				}, "Enter the new object id:");
			} else if(b == 3) {
				sendOptions(object.getInformation(), "Increment the object's rotation", "Decrement the object's rotation", 
					"Modify the object's type", "Go back to the first page...", "Next...");
				stage = 1;
			} else {
				player.packets().sendDestroyObject(object);
				end();
			}
		}
	}
	
	@Override
	public void finish() { 
		
	}

}
