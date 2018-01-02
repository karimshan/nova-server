package org.nova.network.decoders;

import org.nova.network.Session;
import org.nova.network.stream.InputStream;

public abstract class Decoder {

	protected Session session;

	public Decoder(Session session) {
		this.session = session;
	}

	public abstract void decode(InputStream stream);

}
