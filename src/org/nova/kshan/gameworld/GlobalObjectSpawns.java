package org.nova.kshan.gameworld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;

/**
 * Spawns global game objects in the game world.
 * 
 * @author K-Shan
 *
 */
public class GlobalObjectSpawns {

	/**
	 * The file containing the object spawns.
	 */
	public static final File OBJECT_SPAWNS_FILE = new File("./data/map/objects/spawns.txt");
	
	/**
	 * Method used to invoke the spawns
	 */
	public static void spawnObjects() {
		try {
			BufferedReader r = new BufferedReader(new FileReader(OBJECT_SPAWNS_FILE));
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith("//") || line.equals(""))
					continue;
				String[] tokens = line.split(" : ");
				int[] objectTokens = new int[tokens[1].split(", ").length - 1];
				for(int i = 0; i < tokens[1].split(", ").length - 1; i++)
					objectTokens[i] = Integer.parseInt(tokens[1].split(", ")[i]);
				int id = Integer.parseInt(tokens[0]);
				int x = objectTokens[0];
				int y = objectTokens[1];
				int z = objectTokens[2];
				int type = objectTokens[3];
				int rotation = objectTokens[4];
				boolean clipped = Boolean.parseBoolean(tokens[1].split(", ")[tokens[1].split(", ").length - 1]);
				Game.spawnObject(new GlobalObject(id, type, rotation, x, y, z), clipped);
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
