package org.nova.utility.loading.map;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map;

import org.nova.cache.definition.ObjectDefinition;
import org.nova.game.map.GlobalObject;
import org.nova.kshan.utilities.FormatUtils;

/**
 * 
 * @author K-Shan
 *
 */
public class ObjectExamines {

	private static final Map<Integer, String> EXAMINES = new HashMap<Integer, String>();
	
	private static final String UNPACKED = "data/map/objects/unpackedExamines.txt";
	private static final String PACKED = "data/map/objects/packedExamines.e";

	public static void loadExamines() {
		if (new File(PACKED).exists())
			loadPacked();
		else
			loadUnpacked();
	}

	public static String getExamine(int objectId) {
		String message = "";
		if (EXAMINES.get(objectId) != null) {
			message = EXAMINES.get(objectId);
			formatMessage(message);
		} else {
			message = FormatUtils.formatForExamine(ObjectDefinition.get(objectId).name);
		}
		return message;
	}

	public static String getExamine(GlobalObject o) {
		return getExamine(o.getId());
	}

	private static void formatMessage(String message) {
		message = message.replace("<i>", "");
		message = message.replace("</i>", "");
		message = message.replace("<b>", "");
		message = message.replace("<br>", "");
		message = message.replace("</b>", "");
		message = message.replace("</br>", "");
	}

	private static void loadPacked() {
		EXAMINES.clear();
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining())
				EXAMINES.put(buffer.getShort() & 0xffff, readString(buffer));
			channel.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadUnpacked() {
		EXAMINES.clear();
		String line = "";
		try {
		BufferedReader in = new BufferedReader(new FileReader(UNPACKED));
		DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED));
		while (true) {
			line = in.readLine();
			if (line == null)
				break;
			if (line.startsWith("//"))
				continue;
			line = line.replace("﻿", "");
			String[] split = line.split(" - ");
			int npcId = Integer.parseInt(split[0]);
			/*if (split[1].length() > 255)
				continue;*/
			EXAMINES.put(npcId, split[1]);
			out.writeShort(npcId);
			writeString(out, split[1]);
		}
		in.close();
		out.flush();
		out.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}
	}

	public static String readString(ByteBuffer buffer) {
		int count = buffer.get() & 0xff;
		byte[] bytes = new byte[count];
		buffer.get(bytes, 0, count);
		return new String(bytes);
	}

	public static void writeString(DataOutputStream out, String string) throws IOException {
		byte[] bytes = string.getBytes();
		out.writeByte(bytes.length);
		out.write(bytes);
	}

}