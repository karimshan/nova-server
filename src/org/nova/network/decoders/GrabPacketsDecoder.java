package org.nova.network.decoders;

import org.nova.cache.Cache;
import org.nova.network.Session;
import org.nova.network.stream.InputStream;

public final class GrabPacketsDecoder extends Decoder {

	public GrabPacketsDecoder(Session connection) {
		super(connection);
	}

	@Override
	public final void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected()) {
			int packetId = stream.readUnsignedByte();
			if (packetId == 0 || packetId == 1)
				decodeRequestCacheContainer(stream, packetId == 1);
			else
				decodeOtherPacket(stream, packetId);
		}
	}

	private final void decodeRequestCacheContainer(InputStream stream,
			boolean priority) {
		int indexId = stream.readUnsignedByte();
		int archiveId = stream.readInt();
		if (archiveId < 0)
			return;
		if (indexId != 255) {
			if (Cache.INSTANCE.getIndices().length <= indexId
					|| Cache.INSTANCE.getIndices()[indexId] == null
					|| !Cache.INSTANCE.getIndices()[indexId]
							.archiveExists(archiveId))
				return;
		} else if (archiveId != 255)
			if (Cache.INSTANCE.getIndices().length <= archiveId
					|| Cache.INSTANCE.getIndices()[archiveId] == null)
				return;
		session.getGrabPackets().sendCacheArchive(indexId, archiveId, priority);
	}

	private final void decodeOtherPacket(InputStream stream, int packetId) {
		if (packetId == 7) {
			session.getChannel().close();
			return;
		}
		if (packetId == 4) {
			session.getGrabPackets().setEncryptionValue(
					stream.readUnsignedByte());
			if (stream.readUnsignedShort() != 0)
				session.getChannel().close();
		} else
			stream.skip(3);
	}
}
