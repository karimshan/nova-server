package org.nova.utility.loading.items;

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
public class ItemBonuses {
	
	private static final Map<Integer, int[]> bonuses = new HashMap<Integer, int[]>();
	
	public static void loadBonuses() {
		bonuses.clear();
		try {
			@SuppressWarnings("resource")
			BufferedReader r = new BufferedReader(new FileReader(new File("data/items/bonuses.txt")));
			while (true) {
				String line = r.readLine();
				if (line == null)
					break;
				if (line.startsWith("//") || line.equals(""))
					continue;
				String[] lineSplit = line.split(" - ");
				int itemId = Integer.parseInt(lineSplit[0]);
				String[] bonusesFromFile = lineSplit[1].split(", ");
				int[] bonusesArray = new int[18];
				if(bonusesFromFile.length < 18 || bonusesFromFile.length > 18)
					throw new RuntimeException("Error: the item bonus array's size can only be 18. Line: "+line);
				for(int i = 0; i < 18; i++)
					bonusesArray[i] = Integer.parseInt(bonusesFromFile[i]);
				bonuses.put(itemId, bonusesArray);
			}	
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final int[] getItemBonuses(int itemId) {
		return bonuses.get(itemId);
	}
	
}