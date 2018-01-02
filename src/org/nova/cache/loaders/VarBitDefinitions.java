package org.nova.cache.loaders;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.nova.cache.Cache;
import org.nova.network.stream.InputStream;

public final class VarBitDefinitions {

	private static final ConcurrentHashMap<Integer, VarBitDefinitions> varpbitDefs = new ConcurrentHashMap<Integer, VarBitDefinitions>();

	public static final VarBitDefinitions getClientVarpBitDefinitions(int id) {
		VarBitDefinitions script = varpbitDefs.get(id);
		if (script != null)// open new txt document
			return script;
		byte[] data = Cache.INSTANCE.getIndices()[22].getFile(id >>> 1416501898,
				id & 0x3ff);
		script = new VarBitDefinitions();
		script.id = id;
		if (data != null)
			script.readValueLoop(new InputStream(data));
		varpbitDefs.put(id, script);
		return script;

	}

	public static final void main(String[] args) throws IOException {
		Cache.load();
		System.out.println("There are currently: "
				+ Cache.INSTANCE.getIndices()[22].getLastArchiveId() * 0x3ff
				+ " bitConfigs.");
		// List<BitConfigDefinitions> configs = new
		// ArrayList<BitConfigDefinitions>();
		for (int i = 0; i < Cache.INSTANCE.getIndices()[22].getLastArchiveId() * 0x3ff; i++) {
			VarBitDefinitions cd = getClientVarpBitDefinitions(i);
			if (cd.baseVar == 563) {
				System.out.println("BitConfig: " + i + ", from bitshift:"
						+ cd.startBit + ", till bitshift: " + cd.endBit + ", "
						+ cd.baseVar);
			}
		}
	}

	public int baseVar;
	public int endBit;

	public int id;

	public int startBit;

	private VarBitDefinitions() {

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
		if (opcode == 1) {
			baseVar = stream.readUnsignedShort();
			startBit = stream.readUnsignedByte();
			endBit = stream.readUnsignedByte();
		}
	}
}
