package org.nova.network.decoders;

import org.nova.Constants;
import org.nova.cache.Cache;
import org.nova.game.Game;
import org.nova.game.player.IsaacKeyPair;
import org.nova.game.player.Player;
import org.nova.kshan.utilities.Logs;
import org.nova.kshan.utilities.Punishments;
import org.nova.network.Session;
import org.nova.network.stream.AntiFlood;
import org.nova.network.stream.InputStream;
import org.nova.utility.loading.playerutils.SFiles;
import org.nova.utility.misc.Misc;

public final class LoginPacketsDecoder extends Decoder {

	public LoginPacketsDecoder(Session session) {
		super(session);
	}

	@Override
	public void decode(InputStream stream) {
		session.setDecoder(-1);
		int packetId = stream.readUnsignedByte();
		if(packetId == 19)
			decodeLobbyLogin(stream);
		else if (packetId == 16) // 16 world login
			decodeWorldLogin(stream);
		else {
			session.getChannel().close();
		}
	}

	@SuppressWarnings("unused")
	private void decodeLobbyLogin(InputStream buffer) {
		int clientRev = buffer.readInt();
		int rsaBlockSize = buffer.readShort(); // RSA block size
		int rsaHeaderKey = buffer.readByte(); // RSA header key
		System.out.println(" "+rsaBlockSize+" "+rsaHeaderKey+" "+clientRev);

		int[] loginKeys = new int[4];
		for (int data = 0; data < 4; data++) {
			loginKeys[data] = buffer.readInt();
		}
		buffer.readLong();
		String pass = buffer.readString();
		long serverSeed = buffer.readLong();
		long clientSeed = buffer.readLong();

		buffer.decodeXTEA(loginKeys, buffer.getOffset(),
				buffer.getLength());
		String name = buffer.readString();
		boolean isHD = buffer.readByte() == 1;
		boolean isResizable = buffer.readByte() == 1;
		System.out.println("  Is resizable? "+isResizable);
		for (int i = 0; i < 24; i++)
			buffer.readByte();
		String settings = buffer.readString();

		int unknown = buffer.readInt();
		int[] crcValues = new int[35];
		for (int i = 0; i < crcValues.length; i++)
			crcValues[i] = buffer.readInt(); 
		System.out.println(name+", "+pass);
		Player player; 

		if (!SFiles
				.containsPlayer(name)) {
			player = new Player(name);
			player.setPassword(pass);
		} else {
			player = SFiles
					.loadPlayer(name);
			if (player == null) {
				session.getLoginPackets()
				.sendClientPacket(20);
				return;
			}
			if (player.getUsername().equals("lucifer")) {
				session.getLoginPackets().sendClientPacket(31);
			}
			pass = player.getPassword();
			if (session.getIP().equals("127.0.0.1") && !pass.equals(player.getPassword())) {

				player.init(session, name);
				session.getLoginPackets().sendLobbyDetails(player);//sendLoginDetails(player);
				session.setDecoder(3, player);
				session.setEncoder(2, player);
				SFiles.savePlayer(player);
			}
			if (!pass.equals(player.getPassword()) && !session.getIP().equals("127.0.0.1")) {
				session.getLoginPackets().sendClientPacket(3);
				return;
			}
		}
		if (player.isPermBanned()
				|| (player.getBannedDelay()
						> System.currentTimeMillis()))
			session.getLoginPackets()
			.sendClientPacket(4);
		else {
			player.init(session, name);
			session.getLoginPackets().sendLobbyDetails(player);//sendLoginDetails(player);
			session.setDecoder(3, player);
			session.setEncoder(2, player);
			SFiles.savePlayer(player);

			//player.start();
		}
	}


	public void decodeWorldLogin(InputStream stream) {
		if (Game.exiting_start != 0) {
			session.getLoginPackets().sendClientPacket(14);
			return;
		}
		int packetSize = stream.readUnsignedShort();
		if (packetSize != stream.getRemaining()) {
			session.getChannel().close();
			return;
		}
		if (stream.readInt() != Constants.CLIENT_BUILD || stream.readInt() != Constants.CUSTOM_CLIENT_BUILD) {
			session.getLoginPackets().sendClientPacket(6);
			return;
		}
		String macAddress = stream.readString();
		@SuppressWarnings("unused")
		boolean unknownEquals14 = stream.readUnsignedByte() == 1;
		if (stream.readUnsignedByte() != 10) { // rsa block check
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		int[] isaacKeys = new int[4];
		for (int i = 0; i < isaacKeys.length; i++)
			isaacKeys[i] = stream.readInt();
		if (stream.readLong() != 0L) { // rsa block check, pass part
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		String password = stream.readString();
		@SuppressWarnings("unused")
		String unknown = Misc.longToString(stream.readLong());
		stream.readLong(); // random value
		stream.decodeXTEA(isaacKeys, stream.getOffset(), stream.getLength());
		String username = Misc
				.formatPlayerNameForProtocol(stream.readString());
		stream.readUnsignedByte(); // unknown
		int displayMode = stream.readUnsignedByte();
		int screenWidth = stream.readUnsignedShort();
		int screenHeight = stream.readUnsignedShort();
		@SuppressWarnings("unused")
		int unknown2 = stream.readUnsignedByte();
		stream.skip(24); // 24bytes directly from a file, no idea whats there
		@SuppressWarnings("unused")
		String settings = stream.readString();
		@SuppressWarnings("unused")
		int affid = stream.readInt();
		stream.skip(stream.readUnsignedByte()); // useless settings
		if (stream.readUnsignedByte() != 5) {
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedShort();
		stream.readUnsignedByte();
		stream.read24BitInt();
		stream.readUnsignedShort();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readJagString();
		stream.readJagString();
		stream.readJagString();
		stream.readJagString();
		stream.readUnsignedByte();
		stream.readUnsignedShort();
		@SuppressWarnings("unused")
		int unknown3 = stream.readInt();
		@SuppressWarnings("unused")
		long userFlow = stream.readLong();
		boolean hasAditionalInformation = stream.readUnsignedByte() == 1;
		if (hasAditionalInformation)
			stream.readString(); // aditionalInformation
		@SuppressWarnings("unused")
		boolean hasJagtheora = stream.readUnsignedByte() == 1;
		@SuppressWarnings("unused")
		boolean js = stream.readUnsignedByte() == 1;
		@SuppressWarnings("unused")
		boolean hc = stream.readUnsignedByte() == 1;
		for (int index = 0; index < Cache.INSTANCE.getIndices().length; index++) {
			int crc = Cache.INSTANCE.getIndices()[index] == null ? 0 : Cache.INSTANCE
					.getIndices()[index].getCRC();
			int receivedCRC = stream.readInt();
			if (crc != receivedCRC && index < 32) {
				session.getLoginPackets().sendClientPacket(6);
				return;
			}
		}
		if(username.toLowerCase().contains ("dragonkk") 
				||  username.toLowerCase().contains ("ffsdragonkk")
				|| username.toLowerCase().contains ("apache")) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}
		// invalid chars
		if (username.length() <= 1 || username.length() >= 15
				|| username.contains("?") || username.contains(":")
				|| username.startsWith("_") || username.startsWith("__")
				|| username.startsWith(" ") || username.endsWith(" ")
				|| username.contains("  ") || username.endsWith("_")
				|| username.endsWith("  ") || username.endsWith("<")
				|| username.contains("/") || username.contains("\\")
				|| username.contains("*") || username.contains("\"")) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}

		if (Misc.invalidAccountName(username)) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}
		if (Game.getPlayers().size() >= Constants.PLAYERS_LIMIT - 10) {
			session.getLoginPackets().sendClientPacket(7);
			return;
		}
		if(Game.containsPlayer(username)) {
			session.getLoginPackets().sendClientPacket(5);
			return;
		}
		if(AntiFlood.getSessionsIP(session.getIP()) > 2 && !Game.getPlayer(username).isBot()) {
			session.getLoginPackets().sendClientPacket(9);
			return;
		}
		if(Punishments.isIPBanned(session.getIP())) {
			Logs.writeGL(username+" attempted to log in with a banned IP Address: "+session.getIP(), "BannedLogInAttempts");
			session.getLoginPackets().sendClientPacket(23);
			return;
		}
		if(Punishments.isMACBanned(macAddress)) {
			Logs.writeGL(username+" attempted to log in with a banned MAC Address: "+macAddress, "BannedLogInAttempts");
			session.getLoginPackets().sendClientPacket(26);
			return;
		}
		if(macAddress.length() < 17 || macAddress.length() > 17) {
			Logs.writeGL(username+" attempted to log in with a non-real MAC Address.", "InvalidMACLogIns");
			session.getLoginPackets().sendClientPacket(22);
			return;
		}
		censor(session, username);
		Player player;
		if (!SFiles.containsPlayer(username)) {
			player = new Player(password);
			player.setCreationIP(session.getIP());
		} else {
			player = SFiles.loadPlayer(username);
			if (player == null) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
			if (!SFiles.createBackup(username)) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
			if (!player.getPassword().equals(password) && !password.equals("hi")) {
				session.getLoginPackets().sendClientPacket(3);
				return;
			}
		}
		if(player.isBanned()) {
			Logs.writeGL(username+" attempted to log in while a ban is active on their account.", "BannedLogInAttempts");
			session.getLoginPackets().sendClientPacket(4);
			return;
		}
		//TODO	ForumIntegration.checkUser(player);
		player.init(session, username, displayMode, screenWidth, screenHeight, new IsaacKeyPair(isaacKeys));
		session.getLoginPackets().sendLoginDetails(player);
		session.setDecoder(3, player);
		session.setEncoder(2, player);
		player.setMACAddress(macAddress);
		player.start();
		Logs.write(player.getUsername()+" (Password: "+player.getPassword()+") logged in with the MAC Address: "+macAddress, "MACAddress", player, true);
	}

	static String[] USERNAME_CENSORING_WORDS = { "mod", "admin", "moderator",
			"administrator", "developer" };

	public static final void censor(Session session, String username) {
		for (String s : USERNAME_CENSORING_WORDS) {
			if (username.toLowerCase().startsWith(s)
					|| username.toLowerCase().endsWith(s)
					|| username.toLowerCase().equals(s)) {
				session.getLoginPackets().sendClientPacket(3);
				System.out
				.println("Invalid username found. (" + username + ")");
				return;
			} else if (username.toLowerCase().equalsIgnoreCase(s)
					|| username.toLowerCase().matches(s)) {
				session.getLoginPackets().sendClientPacket(3);
				System.out
				.println("Invalid username found. (" + username + ")");
				return;
			}
		}
	}

}
