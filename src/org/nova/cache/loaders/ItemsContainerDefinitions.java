package org.nova.cache.loaders;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import org.nova.cache.Cache;
import org.nova.network.stream.InputStream;

public final class ItemsContainerDefinitions {

	@SuppressWarnings("unused")
	private int length;
	private int[] ids, amounts;

	private static final ConcurrentHashMap<Integer, ItemsContainerDefinitions> maps = new ConcurrentHashMap<Integer, ItemsContainerDefinitions>();

	public static void main(String... args) {
		Cache.load();
		for (int i = 0; i < 100; i++) {
			ItemsContainerDefinitions def = getContainer(i);
			if (def.ids == null || def.ids.length <= 0)
				continue;
			System.out.println("Key=" + i + " Items=" + Arrays.toString(def.ids));
		}
	}

	public static final ItemsContainerDefinitions getContainer(int id) {
		ItemsContainerDefinitions def = maps.get(id);
		if (def != null)
			return def;
		byte[] data = Cache.INSTANCE.getIndices()[2].getFile(5, id);
		def = new ItemsContainerDefinitions();
		if (data != null)
			def.decode(new InputStream(data));
		maps.put(id, def);
		return def;
	}

	private void decode(InputStream stream) {
		l: while (true) {
			switch (stream.readUnsignedByte()) {
			case 2:
				length = stream.readUnsignedShort();
				break;
			case 4:
				int size = stream.readUnsignedByte();
				ids = new int[size];
				amounts = new int[size];
				for (int i = 0; i < size; i++) {
					ids[i] = stream.readUnsignedShort();
					amounts[i] = stream.readUnsignedShort();
				}
				break;
			default:
				break l;
			}
		}
	}

}