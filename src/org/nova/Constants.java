package org.nova;

import org.nova.game.map.Location;

/**
 * Holds server constants.
 */
public final class Constants {

	public static final String SERVER_NAME = "Nova";
	public static final String CACHE_PATH = "data/cache/";//"C:/Users/Karimshan/Documents/cache/";
	
	public static final int PORT_ID = 43594;
	public static final int RECEIVE_DATA_LIMIT = 7500;
	public static final int PACKET_SIZE_LIMIT = 7500;
	public static final int CLIENT_BUILD = 667;
	public static final int CUSTOM_CLIENT_BUILD = 1;
	
	public static final boolean DEVELOPER_MODE = true;
	
	public static final Location START_PLAYER_LOCATION = new Location(5504, 4442, 0);
	public static final Location RESPAWN_PLAYER_LOCATION = new Location(5504, 4442, 0); 
	
	public static final int WORLD_CYCLE_TIME = 600;
	public static final long MAX_PACKETS_DECODER_PING_DELAY = 30000;
	
	public static final int PLAYERS_LIMIT = 2000;
	public static final int LOCAL_PLAYERS_LIMIT = 250;
	public static final int NPCS_LIMIT = Short.MAX_VALUE - 1;
	public static final int LOCAL_NPCS_LIMIT = 1000;
	public static final int MIN_FREE_MEM_ALLOWED = 30000000;
	
	public static final int[] GRAB_SERVER_KEYS = { 1362, 77448, 44880, 39771,
			24563, 363672, 44375, 0, 1614, 0, 5340, 142976, 741080, 188204,
			358294, 416732, 828327, 19517, 22963, 16769, 1244, 11976, 10, 15,
			119, 817677, 1624243};

}
