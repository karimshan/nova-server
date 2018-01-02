package org.nova.network.encoders;

import org.nova.network.Session;

public abstract class Encoder {

	protected Session session;

	public Encoder(Session session) {
		this.session = session;
	}

}
