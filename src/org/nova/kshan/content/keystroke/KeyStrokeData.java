package org.nova.kshan.content.keystroke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds data pertaining to key strokes.
 * 
 * @author K-Shan
 *
 */
public class KeyStrokeData {
	
	private static final Map<Integer, String> KEYS = new HashMap<Integer, String>();
	
	public static final void addKeys() {
		KEYS.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader(new File("./data/playerdata/keyStrokes.txt")));
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith("//") || line.equals(""))
					continue;
				String[] tokens = line.split(" - ");
				int key = Integer.parseInt(tokens[0]);
				String name = tokens[1];
				KEYS.put(key, name);
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final Map<Integer, String> getKeys() {
		return KEYS;
	}

	public static String getKey(int key) {
		if(KEYS.get(key) == null)
			return "No string for key: "+key;
		return KEYS.get(key);	
	}

}
