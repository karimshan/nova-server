package org.nova.game.player;

import org.nova.network.stream.InputStream;

public class LogicPacket {

	private int id;
	byte[] data;

	public LogicPacket(int id, int size, InputStream stream) {
		this.id = id;
		data = new byte[size];
		stream.getBytes(data, 0, size);
	}

	public int getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}

	public short readShort() {
		return (short) ((short) ((data[id++] & 0xff) << 8) | (short) (data[id++] & 0xff));
	}

}