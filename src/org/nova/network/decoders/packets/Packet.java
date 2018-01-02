package org.nova.network.decoders.packets;

import org.nova.network.stream.InputStream;

/**
 * 
 * @author K-Shan
 *
 */
public interface Packet {
	
	void handle(InputStream stream);
	int getListeners();

}
