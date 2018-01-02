package org.nova.network.decoders;

import org.nova.Constants;
import org.nova.network.Session;
import org.nova.network.stream.InputStream;

public final class ClientPacketsDecoder extends Decoder {

	public ClientPacketsDecoder(Session connection) {
		super(connection);
	}

	@Override
	public final void decode(InputStream stream) {
		session.setDecoder(-1);
		int packetId = stream.readUnsignedByte();
		switch (packetId) {
		case 14:
			decodeLogin(stream);
			break;
		case 15:
			decodeGrab(stream);
			break;
		default:
			session.getChannel().close();
			break;
		}
	}

	private final void decodeLogin(InputStream stream) {
		if (stream.getRemaining() != 0) {
			session.getChannel().close();
			return;
		}
		session.setDecoder(2);
		session.setEncoder(1);
		session.getLoginPackets().sendStartUpPacket();
	}

	private final void decodeGrab(InputStream stream) {
		if (stream.getRemaining() != 8) {
			System.out.println(" "+stream.getRemaining());
			session.getChannel().close();
			return;
		}
		session.setEncoder(0);
		if (stream.readInt() != Constants.CLIENT_BUILD
				|| stream.readInt() != Constants.CUSTOM_CLIENT_BUILD) {
			session.setDecoder(-1);
			session.getGrabPackets().sendOutdatedClientPacket();
			return;
		}
		session.setDecoder(1);
		session.getGrabPackets().sendStartUpPacket();
	}
}
