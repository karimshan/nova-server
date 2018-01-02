package org.nova.cache.files;

import org.nova.cache.Cache;
import org.nova.cache.stream.InputStream;
import org.nova.cache.stream.OutputStream;
import org.nova.cache.utility.CacheConstants;
import org.nova.cache.utility.CacheUtils;
import org.nova.cache.utility.crc32.CRC32HGenerator;
import org.nova.cache.utility.whirlpool.Whirlpool;

public final class Index {

	private MainFile mainFile;
	private MainFile index255;
	private ReferenceTable table;
	private byte[][][] cachedFiles;
	private int crc;
	private byte[] whirlpool;

	public Index(MainFile index255, MainFile mainFile, int[] keys) {
		this.mainFile = mainFile;
		this.index255 = index255;
		byte[] archiveData = index255.getArchiveData(getId());
		if (archiveData == null)
			return;
		crc = CRC32HGenerator.getHash(archiveData);
		whirlpool = Whirlpool.getHash(archiveData, 0, archiveData.length);
		Archive archive = new Archive(getId(), archiveData, keys);
		table = new ReferenceTable(archive);
		resetCachedFiles();
	}

	public void resetCachedFiles() {
		cachedFiles = new byte[getLastArchiveId() + 1][][];
	}

	public int getLastFileId(int archiveId) {
		if (!archiveExists(archiveId))
			return -1;
		return table.getArchives()[archiveId].getFiles().length - 1;
	}

	public int getLastArchiveId() {
		return table.getArchives().length - 1;
	}

	public int getValidArchivesCount() {
		return table.getValidArchiveIds().length;
	}

	public int getValidFilesCount(int archiveId) {
		if (!archiveExists(archiveId))
			return -1;
		return table.getArchives()[archiveId].getValidFileIds().length;
	}

	public boolean archiveExists(int archiveId) {
		if(archiveId < 0)
			return false;
		ArchiveReference[] archives = table.getArchives();
		return archives.length > archiveId && archives[archiveId] != null;
	}

	public boolean fileExists(int archiveId, int fileId) {
		if (!archiveExists(archiveId))
			return false;
		FileReference[] files = table.getArchives()[archiveId].getFiles();
		return files.length > fileId && files[fileId] != null;
	}

	public int getArchiveId(String name) {
		int nameHash = CacheUtils.getNameHash(name);
		ArchiveReference[] archives = table.getArchives();
		int[] validArchiveIds = table.getValidArchiveIds();
		for (int archiveId : validArchiveIds) {
			if (archives[archiveId].getNameHash() == nameHash)
				return archiveId;
		}
		return -1;
	}

	public int getFileId(int archiveId, String name) {
		if (!archiveExists(archiveId))
			return -1;
		int nameHash = CacheUtils.getNameHash(name);
		FileReference[] files = table.getArchives()[archiveId].getFiles();
		int[] validFileIds = table.getArchives()[archiveId].getValidFileIds();
		for (int index = 0; index < validFileIds.length; index++) {
			int fileId = validFileIds[index];
			if (files[fileId].getNameHash() == nameHash)
				return fileId;
		}
		return -1;
	}

	public byte[] getFile(int archiveId) {
		if (!archiveExists(archiveId))
			return null;
		return getFile(archiveId,
				table.getArchives()[archiveId].getValidFileIds()[0]);
	}

	public byte[] getFile(int archiveId, int fileId) {
		return getFile(archiveId, fileId, null);
	}

	public byte[] getFile(int archiveId, int fileId, int[] keys) {
		try {
			if (!fileExists(archiveId, fileId))
				return null;
			if (cachedFiles[archiveId] == null
					|| cachedFiles[archiveId][fileId] == null)
				cacheArchiveFiles(archiveId, keys);
			byte[] file = cachedFiles[archiveId][fileId];
			cachedFiles[archiveId][fileId] = null;
			return file;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean packIndex(Cache originalCache) {
		return packIndex(originalCache, false);
	}

	public boolean packIndex(Cache originalCache, boolean checkCRC) {
		try {
			return packIndex(getId(), originalCache, checkCRC);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return packIndex(getId(), originalCache, checkCRC);
	}

	public boolean packIndex(int id, Cache originalCache, boolean checkCRC) {
		try {
			Index originalIndex = originalCache.getIndices()[id];
			for (int archiveId : originalIndex.table.getValidArchiveIds()) {
				if (checkCRC
						&& archiveExists(archiveId)
						&& originalIndex.table.getArchives()[archiveId]
								.getCRC() == table.getArchives()[archiveId]
										.getCRC())
					continue;
				if (!putArchive(id, archiveId, originalCache, false, false))
					return false;
			}
			if (!rewriteTable())
				return false;
			resetCachedFiles();
			return true;
		} catch (Exception e) {

		}
		return true;
	}

	public boolean putArchive(int archiveId, Cache originalCache) {
		return putArchive(getId(), archiveId, originalCache, true, true);
	}
	public boolean putArchive(int archiveId, Cache originalCache,
			boolean rewriteTable, boolean resetCache) {
		return putArchive(getId(), archiveId, originalCache, rewriteTable, resetCache);
	}


	public boolean putArchive(int id, int archiveId, Cache originalCache,
			boolean rewriteTable, boolean resetCache) {
		try {
			Index originalIndex = originalCache.getIndices()[id];
			byte[] data = originalIndex.getMainFile().getArchiveData(archiveId);
			if (data == null) {
				System.out.println("The original index did not contain any data for "+archiveId+".");
				return false;
			}
			if (!archiveExists(archiveId)) {
				table.addEmptyArchiveReference(archiveId);
				System.out.println("Archive did not exist, creating new archive...");
			}
			ArchiveReference reference = table.getArchives()[archiveId];
			reference.updateRevision();
			ArchiveReference originalReference = originalIndex.table.getArchives()[archiveId];
			reference.copyHeader(originalReference);
			int revision = reference.getRevision();
			data[data.length - 2] = (byte) (revision >> 8);
			data[data.length - 1] = (byte) revision;
			if (!mainFile.putArchiveData(archiveId, data))
				return false;
			if (rewriteTable && !rewriteTable())
				return false;
			if (resetCache)
				resetCachedFiles();
			return true;
		} catch (Exception e) {

		}
		return true;
	}




	public boolean putFile(int archiveId, int fileId, byte[] data) {
		return putFile(archiveId, fileId, CacheConstants.GZIP_COMPRESSION, data,
				null, true, true, -1, -1);
	}

	public boolean removeFile(int archiveId, int fileId) {
		return removeFile(archiveId, fileId, CacheConstants.GZIP_COMPRESSION, null);
	}

	public boolean removeFile(int archiveId, int fileId, int compression,
			int[] keys) {
		if (!fileExists(archiveId, fileId))
			return false;
		cacheArchiveFiles(archiveId, keys);
		ArchiveReference reference = table.getArchives()[archiveId];
		reference.removeFileReference(fileId);
		int filesCount = getValidFilesCount(archiveId);
		byte[] archiveData;
		if (filesCount == 1)
			archiveData = getFile(archiveId, reference.getValidFileIds()[0],
					keys);
		else {
			int[] filesSize = new int[filesCount];
			OutputStream stream = new OutputStream();
			for (int index = 0; index < filesCount; index++) {
				int id = reference.getValidFileIds()[index];
				byte[] fileData = getFile(archiveId, id, keys);
				filesSize[index] = fileData.length;
				stream.writeBytes(fileData);
			}
			for (int index = 0; index < filesSize.length; index++) {
				int offset = filesSize[index];
				if (index != 0)
					offset -= filesSize[index - 1];
				stream.writeInt(offset);
			}
			stream.writeByte(1); // 1loop
			archiveData = new byte[stream.getOffset()];
			stream.setOffset(0);
			stream.setBytes(archiveData, 0, archiveData.length);
		}
		reference.updateRevision();
		Archive archive = new Archive(archiveId, compression,
				reference.getRevision(), archiveData);
		byte[] closedArchive = archive.compress();
		reference.setCrc(CRC32HGenerator.getHash(closedArchive, 0,
				closedArchive.length - 2));
		reference.setWhirpool(Whirlpool.getHash(closedArchive, 0,
				closedArchive.length - 2));
		if (!mainFile.putArchiveData(archiveId, closedArchive))
			return false;
		if (!rewriteTable())
			return false;
		resetCachedFiles();
		return true;
	}

	public boolean putFile(int archiveId, int fileId, int compression,
			byte[] data, int[] keys, boolean rewriteTable, boolean resetCache,
			int archiveName, int fileName) {
		if (!archiveExists(archiveId)) {
			table.addEmptyArchiveReference(archiveId);
			resetCachedFiles();
			cachedFiles[archiveId] = new byte[1][];
		}else
			cacheArchiveFiles(archiveId, keys);
		ArchiveReference reference = table.getArchives()[archiveId];
		if (!fileExists(archiveId, fileId))
			reference.addEmptyFileReference(fileId);
		reference.sortFiles();
		int filesCount = getValidFilesCount(archiveId);
		byte[] archiveData;
		if (filesCount == 1)
			archiveData = data;
		else {
			int[] filesSize = new int[filesCount];
			OutputStream stream = new OutputStream();
			for (int index = 0; index < filesCount; index++) {
				int id = reference.getValidFileIds()[index];
				byte[] fileData;
				if (id == fileId)
					fileData = data;
				else
					fileData = getFile(archiveId, id, keys);
				filesSize[index] = fileData.length;
				stream.writeBytes(fileData);
			}
			for (int index = 0; index < filesCount; index++) {
				int offset = filesSize[index];
				if (index != 0)
					offset -= filesSize[index - 1];
				stream.writeInt(offset);
			}
			stream.writeByte(1); // 1loop
			archiveData = new byte[stream.getOffset()];
			stream.setOffset(0);
			stream.setBytes(archiveData, 0, archiveData.length);
		}
		reference.updateRevision();
		Archive archive = new Archive(archiveId, compression,
				reference.getRevision(), archiveData);
		byte[] closedArchive = archive.compress();
		reference.setCrc(CRC32HGenerator.getHash(closedArchive, 0,
				closedArchive.length - 2));
		reference.setWhirpool(Whirlpool.getHash(closedArchive, 0,
				closedArchive.length - 2));
		if (archiveName != -1)
			reference.setNameHash(archiveName);
		if (fileName != -1)
			reference.getFiles()[fileId].setNameHash(fileName);
		if (!mainFile.putArchiveData(archiveId, closedArchive))
			return false;
		if (rewriteTable && !rewriteTable())
			return false;
		if (resetCache)
			resetCachedFiles();
		return true;
	}

	public boolean encryptArchive(int archiveId, int[] keys) {
		return encryptArchive(archiveId, null, keys, true, true);
	}

	public boolean encryptArchive(int archiveId, int[] oldKeys, int[] keys, boolean rewriteTable, boolean resetCache) {
		if (!archiveExists(archiveId))
			return false;
		Archive archive = mainFile.getArchive(archiveId, oldKeys);
		if (archive == null)
			return false;
		ArchiveReference reference = table.getArchives()[archiveId];
		if(reference.getRevision() != archive.getRevision()) 
			throw new RuntimeException("ERROR REVISION");
		reference.updateRevision();
		archive.setRevision(reference.getRevision());
		archive.setKeys(keys);
		byte[] closedArchive = archive.compress();
		reference.setCrc(CRC32HGenerator.getHash(closedArchive, 0,
				closedArchive.length - 2));
		reference.setWhirpool(Whirlpool.getHash(closedArchive, 0,
				closedArchive.length - 2));
		if (!mainFile.putArchiveData(archiveId, closedArchive))
			return false;
		if (rewriteTable && !rewriteTable())
			return false;
		if (resetCache)
			resetCachedFiles();
		return true;

	}

	public boolean rewriteTable() {
		table.updateRevision();
		table.sortTable();
		Object[] hashes = table.encodeHeader(index255);
		if (hashes == null)
			return false;
		//crc = (int) hashes[0];
		whirlpool = (byte[]) hashes[1];
		return true;
	}

	public void setKeys(int[] keys) {
		table.setKeys(keys);
	}

	public int[] getKeys() {
		return table.getKeys();
	}

	private void cacheArchiveFiles(int archiveId, int[] keys) {
		Archive archive = getArchive(archiveId, keys);
		int lastFileId = getLastFileId(archiveId);
		cachedFiles[archiveId] = new byte[lastFileId + 1][];
		if (archive == null)
			return;
		byte[] data = archive.getData();
		if (data == null)
			return;
		int filesCount = getValidFilesCount(archiveId);
		if (filesCount == 1)
			cachedFiles[archiveId][lastFileId] = data;
		else {
			int readPosition = data.length;
			int amtOfLoops = data[--readPosition] & 0xff;
			readPosition -= amtOfLoops * (filesCount * 4);
			InputStream stream = new InputStream(data);
			stream.setOffset(readPosition);
			int filesSize[] = new int[filesCount];
			for (int loop = 0; loop < amtOfLoops; loop++) {
				int offset = 0;
				for (int i = 0; i < filesCount; i++)
					filesSize[i] += offset += stream.readInt();
			}
			byte[][] filesData = new byte[filesCount][];
			for (int i = 0; i < filesCount; i++) {
				filesData[i] = new byte[filesSize[i]];
				filesSize[i] = 0;
			}
			stream.setOffset(readPosition);
			int sourceOffset = 0;
			for (int loop = 0; loop < amtOfLoops; loop++) {
				int dataRead = 0;
				for (int i = 0; i < filesCount; i++) {
					dataRead += stream.readInt();
					System.arraycopy(data, sourceOffset, filesData[i],
							filesSize[i], dataRead);
					sourceOffset += dataRead;
					filesSize[i] += dataRead;
				}
			}
			int count = 0;
			for (int fileId : table.getArchives()[archiveId].getValidFileIds())
				cachedFiles[archiveId][fileId] = filesData[count++];
		}
	}

	public int getId() {
		return mainFile.getId();
	}

	public ReferenceTable getTable() {
		return table;
	}

	public MainFile getMainFile() {
		return mainFile;
	}

	public Archive getArchive(int id) {
		return mainFile.getArchive(id, null);
	}

	public Archive getArchive(int id, int[] keys) {
		return mainFile.getArchive(id, keys);
	}

	public int getCRC() {
		return crc;
	}

	public byte[] getWhirlpool() {
		return whirlpool;
	}
}