package org.nova.utility.loading.items;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.nova.game.item.Item;

/**
 * 
 * @author K-Shan
 *
 */
public class ItemExamines {

	private final static HashMap<Integer, String> itemExamines = new HashMap<Integer, String>();
	
	public static final void loadExamines() {
		itemExamines.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader(new File("data/items/examines.txt")));
			String line;
			while((line = r.readLine()) != null) {
				if(line.equals("") || line.startsWith("//"))
					continue;
				line = line.replace("﻿", "");
				String[] split = line.split(" - ");
				int itemId = Integer.parseInt(split[0]);
				String examine = split.length == 1 ? "ADD EXAMINE FOR ITEM: "+itemId : split[1];
				itemExamines.put(itemId, examine);
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String getExamine(int id) {
		return itemExamines.get(id);
	}
	
	public static String getExamine(Item item) {
		return itemExamines.get(item.getId());
	}
	
}
