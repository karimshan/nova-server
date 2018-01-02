package org.nova.utility.loading.npcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author K-Shan
 *
 */
public class NPCExamines {

	private static final Map<Integer, String> examines = new HashMap<Integer, String>();

	public static void loadExamines() {
		examines.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader(new File("data/npcs/examines.txt")));
			String line;
			while((line = r.readLine()) != null) {
				if(line.equals("") || line.startsWith("//"))
					continue;
				line = line.replace("<i>", "");
				line = line.replace("</i>", "");
				line = line.replace("<b>", "");
				line = line.replace("﻿", "");
				String[] split = line.split(" - ");
				int itemId = Integer.parseInt(split[0]);
				examines.put(itemId, split[1]);
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getExamine(int id) {
		return examines.get(id);
	}

}