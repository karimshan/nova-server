package org.nova.kshan.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.nova.game.npc.Drop;

public class DropTester {
	
	public static Map<Integer, ArrayList<Drop>> getDrops() {
		Map<Integer, ArrayList<Drop>> drops = new HashMap<Integer, ArrayList<Drop>>();
		try {
			BufferedReader r = new BufferedReader(new FileReader(new File("data/npcs/drops.txt")));
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith("//") || line.equals(""))
					continue;
				String[] tokens = line.split(" - ");
				int npcId = Integer.parseInt(tokens[0]);
				for(int i = 1; i < tokens.length; i++) {
					String[] dropTokens = tokens[i].split(", ");
					int itemId = Integer.parseInt(dropTokens[0]);
					double rate = Double.parseDouble(dropTokens[1]);
					int minAmount = Integer.parseInt(dropTokens[2]);
					int maxAmount = Integer.parseInt(dropTokens[3]);
					boolean rare = Boolean.parseBoolean(dropTokens[4]);
					ArrayList<Drop> toUse = new ArrayList<Drop>();
					toUse.add(new Drop(itemId, rate, minAmount, maxAmount, rare));
					drops.put(npcId, toUse);
				}
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		for(Drop d : drops.get(14677))
			System.out.println(d.getItemId());
		return drops;
	}
	
	public static void main(String args[]) {
		getDrops();
	}

}
