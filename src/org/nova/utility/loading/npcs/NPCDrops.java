package org.nova.utility.loading.npcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.nova.game.npc.Drop;
import org.nova.utility.loading.Logger;

/**
 * 
 * @author K-Shan
 *
 */
public class NPCDrops {

	private final static String PACKED_PATH = "data/npcs/packedDrops.d";
	private static HashMap<Integer, Drop[]> npcDrops;

	public static final void init() {
		loadDrops();
	}
	
	private static void loadDrops() {
		if(npcDrops == null)
			npcDrops = new HashMap<Integer, Drop[]>();
		npcDrops.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader(new File("data/npcs/drops.txt")));
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith("//") || line.equals(""))
					continue;
				String[] tokens = line.split(" - ");
				int npcId = Integer.parseInt(tokens[0]);
				Drop[] toUse = new Drop[tokens.length - 1];
				int count = 0;
				for(int i = 1; i < tokens.length; i++) {
					String[] dropTokens = tokens[i].split(", ");
					int itemId = Integer.parseInt(dropTokens[0]);
					double rate = Double.parseDouble(dropTokens[1]);
					int minAmount = Integer.parseInt(dropTokens[2]);
					int maxAmount = Integer.parseInt(dropTokens[3]);
					boolean rare = Boolean.parseBoolean(dropTokens[4]);
					toUse[count] = new Drop(itemId, rate, minAmount, maxAmount, rare);
					npcDrops.put(npcId, toUse);
					count++;
				}
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static Drop[] getDrops(int npcId) {
		return npcDrops.get(npcId);
	}

	private Map<Integer, ArrayList<Drop>> dropMapx = null;

	public Map<Integer, ArrayList<Drop>> getDropArray() {

		if (dropMapx == null)
			dropMapx = new LinkedHashMap<Integer, ArrayList<Drop>>();
		for (int i : npcDrops.keySet()) {
			int npcId = i;
			ArrayList<Drop> temp = new ArrayList<Drop>();
			for (Drop mainDrop : npcDrops.get(npcId)) {
				temp.add(mainDrop);
			}
			dropMapx.put(i, temp);
		}

		return dropMapx;
	}

	public void insertDrop(int npcID, Drop d) {
		loadPackedNPCDrops();
		Drop[] oldDrop = npcDrops.get(npcID);
		if (oldDrop == null) {
			npcDrops.put(npcID, new Drop[] { d });
		} else {
			int length = oldDrop.length;
			Drop destination[] = new Drop[length + 1];
			System.arraycopy(oldDrop, 0, destination, 0, length);
			destination[length] = d;
			npcDrops.put(npcID, destination);
		}
	}

	private static void loadPackedNPCDrops() {
		npcDrops.clear();
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			int dropSize = buffer.getShort() & 0xffff;
			npcDrops = new HashMap<Integer, Drop[]>(dropSize);
			for (int i = 0; i < dropSize; i++) {
				int npcId = buffer.getShort() & 0xffff;
				Drop[] drops = new Drop[buffer.getShort() & 0xffff];
				for (int d = 0; d < drops.length; d++) {
					if (buffer.get() == 0)
						drops[d] = new Drop(buffer.getShort() & 0xffff,
								buffer.getDouble(), buffer.getInt(),
								buffer.getInt(), false);
					else
						drops[d] = new Drop(0, 0, 0, 0, true);

				}
				npcDrops.put(npcId, drops);
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public HashMap<Integer, Drop[]> getDropMap() {
		return npcDrops;
	}

}