package org.nova.cache.loaders;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.nova.cache.Cache;
import org.nova.network.stream.InputStream;
import org.nova.utility.misc.Misc;

public final class ClientScriptMap {

	private static final ConcurrentHashMap<Integer, ClientScriptMap> interfaceScripts = new ConcurrentHashMap<Integer, ClientScriptMap>();

	public static final ClientScriptMap getMap(int scriptId) {
		ClientScriptMap script = interfaceScripts.get(scriptId);
		if (script != null)
			return script;
		byte[] data = Cache.INSTANCE.getIndices()[17].getFile(
				scriptId >>> 0xba9ed5a8, scriptId & 0xff);
		script = new ClientScriptMap();
		if (data != null)
			script.readValueLoop(new InputStream(data));
		interfaceScripts.put(scriptId, script);
		return script;

	}

	public static void main(String[] args) throws IOException {
		Cache.load();
	}

	@SuppressWarnings("unused")
	private char aChar6337;
	@SuppressWarnings("unused")
	private char aChar6345;

	private int defaultIntValue;

	private String defaultStringValue;

	private HashMap<Long, Object> values;

	private ClientScriptMap() {
		defaultStringValue = "null";
	}

	public int getIntValue(long key) {
		if (values == null)
			return defaultIntValue;
		Object value = values.get(key);
		if (value == null || !(value instanceof Integer))
			return defaultIntValue;
		return (Integer) value;
	}

	public long getKeyForValue(Object value) {
		for (Long key : values.keySet()) {
			if (values.get(key).equals(value))
				return key;
		}
		return -1;
	}

	public int getSize() {
		if (values == null)
			return 0;
		return values.size();
	}

	public String getStringValue(long key) {
		if (values == null)
			return defaultStringValue;
		Object value = values.get(key);
		if (value == null || !(value instanceof String))
			return defaultStringValue;
		return (String) value;
	}

	public Object getValue(long key) {
		if (values == null)
			return null;
		return values.get(key);
	}

	public HashMap<Long, Object> getValues() {
		return values;
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1)
			aChar6337 = Misc.method2782((byte) stream.readByte());
		else if (opcode == 2)
			aChar6345 = Misc.method2782((byte) stream.readByte());
		else if (opcode == 3)
			defaultStringValue = stream.readString();
		else if (opcode == 4)
			defaultIntValue = stream.readInt();
		else if (opcode == 5 || opcode == 6 || opcode == 7 || opcode == 8) {
			int count = stream.readUnsignedShort();
			int loop = opcode == 7 || opcode == 8 ? stream.readUnsignedShort()
					: count;
			values = new HashMap<Long, Object>(Misc.getHashMapSize(count));
			for (int i = 0; i < loop; i++) {
				int key = opcode == 7 || opcode == 8 ? stream
						.readUnsignedShort() : stream.readInt();
				Object value = opcode == 5 || opcode == 7 ? stream.readString()
						: stream.readInt();
				values.put((long) key, value);
			}
		}
	}
}
