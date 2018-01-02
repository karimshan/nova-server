package org.nova.cache.definition;

import java.awt.Image;
import java.awt.Toolkit;

import org.nova.cache.Cache;
import org.nova.cache.utility.CacheConstants;

public class LoaderImageArchive {

	private byte[] data;

	public LoaderImageArchive(byte[] data) {
		this.data = data;
	}

	public LoaderImageArchive(Cache cache, int archiveId) {
		this(cache, CacheConstants.SPRITES_INDEX, archiveId, 0);
	}

	private LoaderImageArchive(Cache cache, int idx, int archiveId, int fileId) {
		decodeArchive(cache, idx, archiveId, fileId);
	}

	private void decodeArchive(Cache cache, int idx, int archiveId, int fileId) {
		byte[] data = cache.getIndices()[idx].getFile(archiveId, fileId);
        if(data == null)
            return;
        this.data = data;
	}
	
	public Image getImage() {
		return Toolkit.getDefaultToolkit().createImage(data);
	}
	
	public byte[] getImageData() {
		return data;
	}

}
