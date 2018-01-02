package org.nova.cache.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.nova.cache.Cache;
import org.nova.cache.stream.OutputStream;

public final class CacheUtils {

	public static byte[] cryptRSA(byte[] data, BigInteger exponent, BigInteger modulus) {
		return new BigInteger(data).modPow(exponent, modulus).toByteArray();
	}
	
	public static byte[] getArchivePacketData(int indexId, int archiveId,
			byte[] archive) {
		OutputStream stream = new OutputStream(archive.length + 4);
		stream.writeByte(indexId);
		stream.writeShort(archiveId);
		stream.writeByte(0); // priority, no compression
		stream.writeInt(archive.length);
		int offset = 8;
		for (int index = 0; index < archive.length; index++) {
			if (offset == 512) {
				stream.writeByte(-1);
				offset = 1;
			}
			stream.writeByte(archive[index]);
			offset++;
		}
		byte[] packet = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.setBytes(packet, 0, packet.length);
		return packet;
	}

	public static int getNameHash(String name) {
		return name.toLowerCase().hashCode();
	}

	public static final int getInterfaceDefinitionsSize(Cache store) {
		return store.getIndices()[3].getLastArchiveId();
	}

	public static final int getInterfaceDefinitionsComponentsSize(Cache store,
			int interfaceId) {
		return store.getIndices()[3].getLastFileId(interfaceId);
	}

	
	public static final int getItemsSize(Cache store) {
		int lastArchiveId = store.getIndices()[19].getLastArchiveId();
		return lastArchiveId * 256 + store.getIndices()[19].getValidFilesCount(lastArchiveId);
				
	}
	
	public static final int getModelsSize(Cache store) {
		return store.getIndices()[7].getLastArchiveId() + 1;
	}
	
	private static List<Integer> nulledModels;
	
	/**
	 * Returns the list of nulled models in the cache.
	 * 
	 * @return
	 */
	public static final List<Integer> getNulledModels() {
		return nulledModels;
	}
	
	/**
	 * Checks for any nulled models in the current cache.
	 */
	public static void checkModels() {
		Cache store = Cache.INSTANCE;//MainFrame.STORE; TODO
		if(nulledModels == null)
			nulledModels = new ArrayList<Integer>();
		int lastArchiveId = getModelsSize();
		for(int i = 0; i < lastArchiveId; i++)
			if(store.getIndices()[7].getFile(i, 0) == null)
				nulledModels.add(i);
	}

	private CacheUtils() {

	}
	
	public static final int getNPCsSize() {
		return getNPCsSize(Cache.INSTANCE);
	}
	
	public static final int getObjectsSize() {
		return getObjectsSize(Cache.INSTANCE);
	}
	
	public static final int getItemsSize() {
		return getItemsSize(Cache.INSTANCE);
	}
	
	public static final int getModelsSize() {
		return getModelsSize(Cache.INSTANCE);
	}

	public static final int getNPCsSize(Cache store) {
		int lastArchiveId = store.getIndices()[18].getLastArchiveId();
		return lastArchiveId * 128 + store.getIndices()[18].getValidFilesCount(lastArchiveId);
	}

	public static final int getObjectsSize(Cache STORE) {
		int lastArchiveId = STORE.getIndices()[16].getLastArchiveId();
		return lastArchiveId
				* 256
				+ STORE.getIndices()[16]
						.getValidFilesCount(lastArchiveId);
	}
	
	@SuppressWarnings("resource")
	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > 2147483647L) {
			System.out.println("The file length is un-real.");
		}
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while ((offset < bytes.length)
				&& ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
			offset += numRead;
		}
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file: "+file.getName());
		}
		is.close();
		return bytes;
	}

}
