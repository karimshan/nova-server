package org.nova.game.player;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.nova.Constants;
import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.network.stream.OutputStream;
import org.nova.utility.loading.playerutils.SFiles;
import org.nova.utility.misc.Misc;

public class ClanChat {
	
	private String clanowner;
	private String displayName;
	private FriendsIgnores settings;
	private CopyOnWriteArrayList<Player> players;
	private ConcurrentHashMap<String, Long> bannedPlayers;
	private byte[] dataBlock;

	private static HashMap<String, ClanChat> cachedClanChats;

	public static void init() {
		cachedClanChats = new HashMap<String, ClanChat>();
	}

	private ClanChat(Player player) {
		clanowner = player.getUsername();
		displayName = player.getDisplayName();
		settings = player.getFriendsIgnores();
		players = new CopyOnWriteArrayList<Player>();
		bannedPlayers = new ConcurrentHashMap<String, Long>();
	}

	public int getRank(int rights, String username) {
		if (rights == 2)
			return 127;
		if (username.equals(clanowner))
			return 7;
		return settings.getRank(username);
	}

	public CopyOnWriteArrayList<Player> getPlayers() {
		return players;
	}

	public int getWhoCanKickOnChat() {
		return settings.getWhoCanKickOnChat();
	}

	public String getOwnerDisplayName() {
		return displayName;
	}

	public String getOwnerName() {
		return clanowner;
	}

	public String getChannelName() {
		return settings.getChatName().replaceAll("<img=", "");
	}

	public void enter(Player player) {
		boolean hasWar = player.getCurrentFriendChat() != null
				&& player.getClanChat() != null;
		final ClanChat c = hasWar ? player.getClanChat() : (ClanChat) player
				.getTemporaryAttributtes().get("view_clan_chat");
		if (c == null) {
			return;
		}

		player.getControllerManager().startController("clan_chat", c);
		CoresManager.slowExecutor.submit(new Runnable() {
			@Override
			public void run() {

			}
		});
		return;
	}

	public void accept(final Player player) {
		final Player other = (Player) player.getTemporaryAttributtes().get(
				"clan_request");
		if (other != null
				&& (Boolean) other.getTemporaryAttributtes().get(
						"accepted_clan") == Boolean.TRUE) {
			CoresManager.slowExecutor.submit(new Runnable() {
				@Override
				public void run() {
					player.getTemporaryAttributtes().remove("accepted_clan");
					other.getTemporaryAttributtes().remove("accepted_clan");
					player.interfaces().closeScreenInterface();
					other.interfaces().closeScreenInterface();

				}
			});
			return;
		}
		player.getTemporaryAttributtes().put("accepted_clan", true);
	}

	public void leave(Player p, boolean ingame) {
		if (players.contains(p)) {
			players.remove(p);
		} else if (!ingame) {
			return;
		}
		boolean resized = p.interfaces().isFullScreen();
		p.packets().closeInterface(resized ? 746 : 548, resized ? 11 : 27);
		p.getControllerManager().startController("clan_chat_request");
		updateClan();
	}

	public void updateClan() {

	}

	private void joinChat(Player player) {
		synchronized (this) {
			if (!player.getUsername().equals(clanowner)
					&& !settings.hasRankToJoin(player.getUsername())
					&& player.getRights() < 2) {
				player.packets().sendMessage(
						"You must be invited to join the clan chat channel.");
				return;
			}
			if (players.size() >= 100) {
				player.packets().sendMessage("This chat is full.");
				return;
			}
			Long bannedSince = bannedPlayers.get(player.getUsername());
			if (bannedSince != null) {
				if (bannedSince + 3600000 > Misc.currentTimeMillis()) {
					player.packets().sendMessage(
							"You have been removed from this clan chat.");
					return;
				}
				bannedPlayers.remove(player.getUsername());
			}
			joinChatNoCheck(player);
		}
	}

	public void leaveChat(Player player, boolean logout) {
		synchronized (this) {
			player.setClanchat(null);
			players.remove(player);
			if (players.size() == 0) { // no1 at chat so uncache it
				synchronized (cachedClanChats) {
					cachedClanChats.remove(clanowner);
				}
			} else
				refreshChannel();
			if (!logout) {
				player.setCurrentFriendChatOwner(null);
				player.packets().sendMessage("You have left the clan.");
				player.packets().sendClanChatChannel();
			}

		}
	}

	public Player getPlayerByDisplayName(String username) {
		String formatedUsername = Misc.formatPlayerNameForProtocol(username);
		for (Player player : players) {
			if (player.getUsername().equals(formatedUsername)
					|| player.getDisplayName().equals(username))
				return player;
		}
		return null;
	}

	public void kickPlayerFromChat(Player player, String username) {
		String name = "";
		for (char character : username.toCharArray()) {
			name += Misc.containsInvalidCharacter(character) ? " " : character;
		}
		synchronized (this) {
			int rank = getRank(player.getRights(), player.getUsername());
			if (rank < getWhoCanKickOnChat())
				return;
			Player kicked = getPlayerByDisplayName(name);
			if (kicked == null) {
				player.packets().sendMessage(
						"This player is not in the clan.");
				return;
			}
			if (rank <= getRank(kicked.getRights(), kicked.getUsername()))
				return;
			kicked.setClanchat(null);
			kicked.setCurrentFriendChatOwner(null);
			players.remove(kicked);
			bannedPlayers.put(kicked.getUsername(), Misc.currentTimeMillis());
			kicked.packets().sendClanChatChannel();
			kicked.packets().sendMessage(
					"You have been kicked from the clan chat.");
			player.packets().sendMessage(
					"You have kicked " + kicked.getUsername()
							+ " from clan chat channel.");
			refreshChannel();

		}
	}

	private void joinChatNoCheck(Player player) {
		synchronized (this) {
			players.add(player);
			player.setClanchat(this);
			player.setCurrentFriendChatOwner(clanowner);
			player.packets().sendMessage(
					"You are now talking in the clan chat "
							+ settings.getChatName());
			refreshChannel();
		}
	}

	public void destroyChat() {
		synchronized (this) {
			for (Player player : players) {
				player.setClanchat(null);
				player.setCurrentFriendChatOwner(null);
				player.packets().sendClanChatChannel();
				player.packets().sendMessage(
						"You have been removed from this clan!");
			}
		}
		synchronized (cachedClanChats) {
			cachedClanChats.remove(clanowner);
		}

	}

	public void sendQuickMessage(Player player, QuickChatMessage message) {
		synchronized (this) {
			if (!player.getUsername().equals(clanowner)
					&& !settings.canTalk(player) && player.getRights() < 2) {
				player.packets()
						.sendMessage(
								"You do not have a enough rank to talk on this clan chat channel.");
				return;
			}
			String formatedName = Misc.formatPlayerNameForDisplay(player
					.getUsername());
			String displayName = player.getDisplayName();
			int rights = player.getRights();
			for (Player p2 : players)
				p2.packets().receiveFriendChatQuickMessage(formatedName,
						displayName, rights, settings.getChatName(), message);
		}
	}

	public void sendMessage(Player player, String message) {
		synchronized (this) {
			if (!player.getUsername().equals(clanowner)
					&& !settings.canTalk(player) && player.getRights() < 2) {
				player.packets()
						.sendMessage(
								"You do not have a enough rank to talk on this clan chat channel.");
				return;
			}
			String formatedName = Misc.formatPlayerNameForDisplay(player
					.getUsername());
			String displayName = player.getDisplayName();
			int rights = player.getRights();
			for (Player p2 : players)
				p2.packets().receiveFriendChatMessage(formatedName,
						displayName, rights, settings.getChatName(), message);
		}
	}

	public void sendDiceMessage(Player player, String message) {
		synchronized (this) {
			if (!player.getUsername().equals(clanowner)
					&& !settings.canTalk(player) && player.getRights() < 2) {
				player.packets()
						.sendMessage(
								"You do not have a enough rank to talk on this clan chat channel.");
				return;
			}
			for (Player p2 : players) {
				p2.packets().sendMessage(message);
			}
		}
	}

	private void refreshChannel() {
		synchronized (this) {
			OutputStream stream = new OutputStream();
			stream.writeString(displayName);
			String ownerName = Misc.formatPlayerNameForDisplay(clanowner);
			stream.writeByte(getOwnerDisplayName().equals(ownerName) ? 0 : 1);
			if (!getOwnerDisplayName().equals(ownerName))
				stream.writeString(ownerName);
			stream.writeLong(Misc.stringToLong(getChannelName()));
			int kickOffset = stream.getOffset();
			stream.writeByte(0);
			stream.writeByte(getPlayers().size());
			for (Player player : getPlayers()) {
				String displayName = player.getDisplayName();
				String name = Misc.formatPlayerNameForDisplay(player
						.getUsername());
				stream.writeString(displayName);
				stream.writeByte(displayName.equals(name) ? 0 : 1);
				if (!displayName.equals(name))
					stream.writeString(name);
				stream.writeShort(1);
				int rank = getRank(player.getRights(), player.getUsername());
				stream.writeByte(rank);
				stream.writeString(Constants.SERVER_NAME);
			}
			dataBlock = new byte[stream.getOffset()];
			stream.setOffset(0);
			stream.getBytes(dataBlock, 0, dataBlock.length);
			for (Player player : players) {
				dataBlock[kickOffset] = (byte) (player.getUsername().equals(
						clanowner) ? 0 : getWhoCanKickOnChat());
				// player.getPackets().sendClanChatChannel();
			}
		}
	}

	public byte[] getDataBlock() {
		return dataBlock;
	}

	public static void destroyChat(Player player) {
		synchronized (cachedClanChats) {
			ClanChat chat = cachedClanChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.destroyChat();
			player.packets().sendMessage(
					"Your clan chat channel has now been disabled!");
		}
	}

	public static void linkSettings(Player player) {
		synchronized (cachedClanChats) {
			ClanChat chat = cachedClanChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.settings = player.getFriendsIgnores();
		}
	}

	public static void refreshChat(Player player) {
		synchronized (cachedClanChats) {
			ClanChat chat = cachedClanChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.refreshChannel();
		}
	}

	public static void joinChat(String ownerName, Player player) {
		synchronized (cachedClanChats) {
			if (player.getClanChat() != null)
				return;
			player.packets()
					.sendMessage("Attempting to join channel...");
			String formatedName = Misc.formatPlayerNameForProtocol(ownerName);
			ClanChat chat = cachedClanChats.get(formatedName);
			if (chat == null) {
				Player owner = Game.getPlayerByDisplayName(ownerName);
				if (owner == null) {
					if (!SFiles.containsPlayer(formatedName)) {
						player.packets()
								.sendMessage(
										"The channel you tried to join does not exist.");
						return;
					}
					owner = SFiles.loadPlayer(formatedName);
					if (owner == null) {
						player.packets()
								.sendMessage(
										"The channel you tried to join does not exist.");
						return;
					}
					owner.setUsername(formatedName);
				}
				FriendsIgnores settings = owner.getFriendsIgnores();
				if (!settings.hasFriendChat()) {
					player.packets().sendMessage(
							"The channel you tried to join does not exist.");
					return;
				}
				if (!player.getUsername().equals(ownerName)
						&& !settings.hasRankToJoin(player.getUsername())
						&& player.getRights() < 2) {
					player.packets().sendMessage(
							"You need to be invited to join this clan chat.");
					return;
				}
				chat = new ClanChat(player);
				cachedClanChats.put(ownerName, chat);
				chat.joinChatNoCheck(player);
			} else
				chat.joinChat(player);
		}

	}

}