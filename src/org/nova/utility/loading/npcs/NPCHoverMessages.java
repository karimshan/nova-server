package org.nova.utility.loading.npcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

/**
 * 
 * @author K-Shan
 *
 */
public class NPCHoverMessages {

	private final static HashMap<Integer, String> messages = new HashMap<Integer, String>();
	
	public static final void load() {
		messages.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader(new File("data/npcs/hoverMessages.txt")));
			String line;
			while((line = r.readLine()) != null) {
				if(line.equals("") || line.startsWith("//"))
					continue;
				line = line.replace("﻿", "");
				String[] split = line.split(" : ");
				int npcId = Integer.parseInt(split[0]);
				String message = split[1];
				messages.put(npcId, message);
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String getMessage(int id) {
		return messages.get(id);
	}

}
