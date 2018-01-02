package org.nova.kshan.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.nova.cache.Cache;
import org.nova.cache.definition.ItemDefinition;
import org.nova.game.npc.Drop;
import org.nova.game.npc.NPC;
import org.nova.utility.loading.npcs.NPCDrops;
import org.nova.utility.misc.Misc;

/**
 * Generates drops for an npc
 * 
 * @author K-Shan
 *
 */
public class DropGenerator {

	private static final Map<Integer, Integer> map = new HashMap<>();

	public static void log(Object o) {
		System.out.println(o);
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(System.in);
		char answer = 'N';
		do {
			Cache.load();
			NPCDrops.init();
			System.out.print("Enter the npc id: ");
			int npcId = s.nextInt();
			System.out.print("Enter the amount of kills: ");
			int kills = s.nextInt();
			for (int i = 0; i < kills; i++) {
				Drop[] drops = NPCDrops.getDrops(npcId);
				if (drops == null) {
					return;
				}
				Drop[] possibleDrops = new Drop[drops.length];
				int possibleDropsCount = 0;
				for (Drop drop : drops) {
					if (drop.getRate() == 100) {
						int previousValue = map.get(drop.getItemId()) != null ? map
								.get(drop.getItemId()) : 0;
						map.put(drop.getItemId(),
								drop.getMinAmount()
										+ Misc.getRandom(drop.getExtraAmount())
										+ previousValue);
					} else {
						if (Misc.getRandomDouble(100) <= drop.getRate()) {
							possibleDrops[possibleDropsCount++] = drop;
						}
					}
				}
				if (possibleDropsCount > 0) {
					Drop drop2 = possibleDrops[Misc
							.getRandom(possibleDropsCount - 1)];
					int previousValue = map.get(drop2.getItemId()) != null ? map
							.get(drop2.getItemId()) : 0;
					map.put(drop2.getItemId(),
							drop2.getMinAmount()
									+ Misc.getRandom(drop2.getExtraAmount())
									+ previousValue);
				}
			}
			log("NPC: "+NPC.createBlankNPC(npcId));
			log("Kills: " + kills);
			for (Entry<Integer, Integer> entry : map.entrySet()) {
				int formatted = entry.getValue() < 0 || entry.getValue() > Integer.MAX_VALUE ? Integer.MAX_VALUE : entry.getValue();
				log(ItemDefinition.get(entry.getKey()).name+" - "+formatted);
			}
			System.out.print("\nWould you like to check another NPC? (Y / N): ");
			answer = s.next().charAt(0);
		} while(answer == 'Y' || answer == 'y');
		s.close();
	}

}
