package org.nova.kshan.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.nova.cache.Cache;
import org.nova.game.npc.Drop;
import org.nova.utility.loading.npcs.NPCDrops;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class DropsUnpacker {
	
	public static void main(String[] args) {
		try {
			Cache.load();
			print("Loaded Cache.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		NPCDrops.init();
		print("Loaded NPC Drops.");
		for (int npcId = 0; npcId < Misc.getNPCsSize(); npcId++) {
			Drop[] drops = NPCDrops.getDrops(npcId);
			File file = new File("data/npcs/drops.txt");
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
				if(drops != null) {
					writer.write(npcId+" - ");
					int count = 0;
					for(Drop drop : drops) {
						count++;
						writer.write(drop.getItemId()+", "+drop.getRate()+", "+drop.getMinAmount()+", "
							+ ""+drop.getMaxAmount()+", "+drop.isFromRareTable()+""
							+ ""+(count == drops.length ? "" : " - "));
					}
					writer.newLine();
				}
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		print("Finished Unpacking NPC Drops.");
	}
	
	private static void print(String s) {
		System.out.println(s);
	}

}
