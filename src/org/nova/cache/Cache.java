package org.nova.cache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.Arrays;

import org.nova.Constants;
import org.nova.cache.files.Archive;
import org.nova.cache.files.Index;
import org.nova.cache.files.MainFile;
import org.nova.cache.stream.OutputStream;
import org.nova.cache.utility.CacheConstants;
import org.nova.cache.utility.CacheUtils;
import org.nova.cache.utility.whirlpool.Whirlpool;


public final class Cache {

	private Index[] indices;
	private MainFile index255;
	private String path;
	private RandomAccessFile data;
	private boolean newProtocol;
	
	public static Cache INSTANCE;
	
	public static void load() {
		INSTANCE = new Cache(Constants.CACHE_PATH);
	}
	
	public static void setInstance(String path) {
		if(path == null || path.equals(null))
			INSTANCE = null;
		else
			INSTANCE = new Cache(path+"/");
	}

	public Cache(String path) {
		this(path, CacheConstants.CLIENT_BUILD >= 704);
	}

	public Cache(String path, boolean newProtocol) {
		this(path, newProtocol, null);
	}

	public Cache(String path, boolean newProtocol, int[][] keys) {
		try {
			this.path = path;
			this.newProtocol = newProtocol;
			data = new RandomAccessFile(path + "main_file_cache.dat2", "rw");
			index255 = new MainFile(255, data, new RandomAccessFile(path + "main_file_cache.idx255", "rw"), newProtocol);
			int idxsCount = index255.getArchivesCount();
			indices = new Index[idxsCount];
			for(int id = 0; id < idxsCount; id++) {
				Index index = new Index(index255, new MainFile(id, data, new RandomAccessFile(path + "main_file_cache.idx"+id, "rw" )
						, newProtocol), keys == null ? null : keys[id]);
				if(index.getTable() == null)
					continue;
				indices[id] = index;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public final byte[] generateIndex255Archive255Current(BigInteger grab_server_private_exponent, BigInteger grab_server_modulus) {
		OutputStream stream = new OutputStream();
		stream.writeByte(getIndices().length);
		for (int index = 0; index < getIndices().length; index++) {
			if (getIndices()[index] == null) {
				stream.writeInt(0);
				stream.writeInt(0);
				stream.writeBytes(new byte[64]);
				continue;
			}
			stream.writeInt(getIndices()[index].getCRC());
			stream.writeInt(getIndices()[index].getTable().getRevision());
			stream.writeBytes(getIndices()[index].getWhirlpool());
			if(CacheConstants.ENCRYPTED_CACHE) {
				//custom protection, encryption of tables addition, by me dragonkk ofc
				if(getIndices()[index].getKeys() != null)
					for(int key : getIndices()[index].getKeys()) 
						stream.writeInt(key);
				else for(int i = 0; i < 4; i++)
					stream.writeInt(0);
			}
		}
		byte[] archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.setBytes(archive, 0, archive.length);

		OutputStream hashStream = new OutputStream(65);
		hashStream.writeByte(0);
		hashStream.writeBytes(Whirlpool.getHash(archive, 0, archive.length));
		byte[] hash = new byte[hashStream.getOffset()];
		hashStream.setOffset(0);
		hashStream.setBytes(hash, 0, hash.length);
		if(grab_server_private_exponent != null && grab_server_modulus != null)
			hash = CacheUtils.cryptRSA(hash, grab_server_private_exponent, grab_server_modulus);
		stream.writeBytes(hash);
		archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.setBytes(archive, 0, archive.length);
		return archive;
	}

	@SuppressWarnings("unused")
	public byte[] generateIndex255Archive255() {
		return CacheConstants.CLIENT_BUILD < 614 ? generateIndex255Archive255Outdated() : generateIndex255Archive255Current(null, null);
	}

	/*
	 * old code
	 */
	public byte[] generateIndex255Archive255Outdated() {
		OutputStream stream = new OutputStream(indices.length * 8);
		for(int index = 0; index < indices.length; index++) {
			if(indices[index] == null) {
				stream.writeInt(0);
				stream.writeInt(0);
				continue;
			}
			stream.writeInt(indices[index].getCRC());
			stream.writeInt(indices[index].getTable().getRevision());
		}
		byte[] archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.setBytes(archive, 0, archive.length);
		return archive;
	}

	public String getPath() {
		return path;
	}

	public Index[] getIndices() {
		return indices;
	}

	public MainFile getIndex255() {
		return index255;
	}
	
	/*
	 * returns index
	 */
	public int addIndex(boolean named, boolean usesWhirpool, int tableCompression) throws IOException {
		int id = indices.length;
		Index[] newIndexes = Arrays.copyOf(indices, indices.length+1);
		resetIndex(id, newIndexes, named, usesWhirpool, tableCompression);
		indices = newIndexes;
		return id;
	}

	public void resetIndex(int id, boolean named, boolean usesWhirpool, int tableCompression) throws FileNotFoundException, IOException {
		resetIndex(id, indices, named, usesWhirpool, tableCompression);
	}
	
	public static final byte[] generateUkeysFile() {
		OutputStream stream = new OutputStream();
		stream.writeByte(INSTANCE.getIndices().length);
		for (int index = 0; index < INSTANCE.getIndices().length; index++) {
			if (INSTANCE.getIndices()[index] == null) {
				stream.writeInt(0);
				stream.writeInt(0);
				stream.writeBytes(new byte[64]);
				continue;
			}
			stream.writeInt(INSTANCE.getIndices()[index].getCRC());
			stream.writeInt(INSTANCE.getIndices()[index].getTable().getRevision());
			stream.writeBytes(INSTANCE.getIndices()[index].getWhirlpool());
		}
		byte[] archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.setBytes(archive, 0, archive.length);
		OutputStream hashStream = new OutputStream(65);
		hashStream.writeByte(0);
		hashStream.writeBytes(Whirlpool.getHash(archive, 0, archive.length));
		byte[] hash = new byte[hashStream.getOffset()];
		hashStream.setOffset(0);
		hashStream.setBytes(hash, 0, hash.length);
		stream.writeBytes(hash);
		archive = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.setBytes(archive, 0, archive.length);
		return archive;
	}

	public void resetIndex(int id, Index[] indices, boolean named, boolean usesWhirpool, int tableCompression) throws FileNotFoundException, IOException {
		OutputStream stream = new OutputStream(4);
		stream.writeByte(5);
		stream.writeByte((named ? 0x1 : 0) | (usesWhirpool ? 0x2 : 0));
		stream.writeShort(0);
		byte[] archiveData = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.setBytes(archiveData, 0, archiveData.length);
		Archive archive = new Archive(id, tableCompression, -1, archiveData);
		index255.putArchiveData(id, archive.compress());
		indices[id] = new Index(index255, new MainFile(id, data, new RandomAccessFile(path + "main_file_cache.idx"+id, "rw"), newProtocol), null);
	}
	
	public static boolean archiveCorrupt(int index, int archiveId) {
		return INSTANCE.getIndices()[index].getFile(archiveId, 0) == null || INSTANCE.getIndices()[index].getArchive(archiveId) == null;
	}

	public static void deleteArchive(int i, int widgetId) {
		INSTANCE.getIndices()[i].getTable().deleteArchive(i, widgetId);
	}

}
