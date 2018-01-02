package org.nova.game.player.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.nova.Constants;
import org.nova.game.Game;
import org.nova.game.player.FriendsIgnores;
import org.nova.game.player.Player;
import org.nova.game.player.QuickChatMessage;
import org.nova.game.player.content.minigames.clanwars.ClanWars;
import org.nova.network.stream.OutputStream;
import org.nova.utility.loading.playerutils.SFiles;
import org.nova.utility.misc.Misc;

public class FriendChatsManager {

	private String owner;
	private String ownerDisplayName;
	private FriendsIgnores settings;
	private CopyOnWriteArrayList<Player> players;
	private ConcurrentHashMap<String, Long> bannedPlayers;
	private byte[] dataBlock;

	/**
	 * The clan wars instance (if the clan is in a war).
	 */
	private ClanWars clanWars;

	private static HashMap<String, FriendChatsManager> cachedFriendChats;

	public static void init() {
		cachedFriendChats = new HashMap<String, FriendChatsManager>();
	}

	public int getRank(int rights, String username) {
		if (rights == 2)
			return 127;
		if (username.equals(owner))
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
		return ownerDisplayName;
	}

	public String getOwnerName() {
		return owner;
	}

	public String getChannelName() {
		return settings.getChatName().replaceAll("<img=", "");
	}

	private void joinChat(Player player) {
		synchronized (this) {
			if (!player.getUsername().equals(owner) && !settings.hasRankToJoin(player.getUsername()) && player.getRights() < 2) {
				player.packets().sendMessage("You do not have a enough rank to join this friends chat channel.");
				return;
			}
			if (players.size() >= 100) {
				player.packets().sendMessage("This chat is full.");
				return;
			}
			Long bannedSince = bannedPlayers.get(player.getUsername());
			if (bannedSince != null) {
				if (bannedSince + 3600000 > Misc.currentTimeMillis()) {
					player.packets().sendMessage("You have been banned from this channel.");
					return;
				}
				bannedPlayers.remove(player.getUsername());
			}
			joinChatNoCheck(player);
		}
	}

	public void leaveChat(Player player, boolean logout) {
		synchronized (this) {
			player.setCurrentFriendChat(null);
			players.remove(player);
			if (players.size() == 0) { // no1 at chat so uncache it
				synchronized (cachedFriendChats) {
					cachedFriendChats.remove(owner);
				}
			} else
				refreshChannel();
			if (!logout) {
				player.setCurrentFriendChatOwner(null);
				player.toggleLootShare();
				player.closeInterfaces();
				player.packets().sendMessage("You have left the channel.");
				player.packets().sendFriendsChatChannel();
			}
			if (clanWars != null) {
				clanWars.leave(player, false);
			}
		}
	}

	public Player getPlayerByDisplayName(String username) {
		String formatedUsername = Misc.formatPlayerNameForProtocol(username);
		for (Player player : players) {
			if (player.getUsername().equals(formatedUsername) || player.getDisplayName().equals(username))
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
				player.packets().sendMessage("This player is not this channel.");
				return;
			}
			if (rank <= getRank(kicked.getRights(), kicked.getUsername()))
				return;
			kicked.setCurrentFriendChat(null);
			kicked.setCurrentFriendChatOwner(null);
			player.toggleLootShare();
			players.remove(kicked);
			bannedPlayers.put(kicked.getUsername(), Misc.currentTimeMillis());
			kicked.packets().sendFriendsChatChannel();
			kicked.packets().sendMessage("You have been kicked from the friends chat channel.");
			player.packets().sendMessage("You have kicked " + kicked.getUsername() + " from friends chat channel.");
			refreshChannel();

		}
	}

	private void joinChatNoCheck(Player player) {
		synchronized (this) {
			players.add(player);
			player.setCurrentFriendChat(this);
			player.setCurrentFriendChatOwner(owner);
			player.packets().sendMessage("You are now talking in the friends chat channel " + settings.getChatName());
			refreshChannel();
		}
	}

	public void destroyChat() {
		synchronized (this) {
			for (Player player : players) {
				player.setCurrentFriendChat(null);
				player.setCurrentFriendChatOwner(null);
				player.toggleLootShare();
				player.packets().sendFriendsChatChannel();
				player.packets().sendMessage("You have been removed from this channel!");
			}
		}
		synchronized (cachedFriendChats) {
			cachedFriendChats.remove(owner);
		}

	}

	public void sendQuickMessage(Player player, QuickChatMessage message) {
		synchronized (this) {
			if (!player.getUsername().equals(owner) && !settings.canTalk(player) && player.getRights() < 2) {
				player.packets().sendMessage("You do not have a enough rank to talk on this friends chat channel.");
				return;
			}
			String formatedName = Misc.formatPlayerNameForDisplay(player.getUsername());
			String displayName = player.getDisplayName();
			for (Player p2 : players)
				p2.packets().receiveFriendChatQuickMessage(formatedName, displayName, player.getRights(), settings.getChatName(), message);
		}
	}

	public void sendMessage(Player player, String message) {
		synchronized (this) {
			if (!player.getUsername().equals(owner) && !settings.canTalk(player) && player.getRights() < 2) {
				player.packets().sendMessage("You do not have a enough rank to talk on this friends chat channel.");
				return;
			}
			String formatedName = Misc.formatPlayerNameForDisplay(player.getUsername());
			String displayName = player.getDisplayName();
			for (Player p2 : players)
				p2.packets().receiveFriendChatMessage(formatedName,
						displayName, player.getRights(),
						settings.getChatName(), message);	}
	}

	public void sendDiceMessage(Player player, String message) {
		synchronized (this) {
			if (!player.getUsername().equals(owner) && !settings.canTalk(player) && player.getRights() < 2) {
				player.packets().sendMessage("You do not have a enough rank to talk on this friends chat channel.");
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
			stream.writeString(ownerDisplayName);
			String ownerName = Misc.formatPlayerNameForDisplay(owner);
			stream.writeByte(getOwnerDisplayName().equals(ownerName) ? 0 : 1);
			if (!getOwnerDisplayName().equals(ownerName))
				stream.writeString(ownerName);
			stream.writeLong(Misc.stringToLong(getChannelName()));
			int kickOffset = stream.getOffset();
			stream.writeByte(0);
			stream.writeByte(getPlayers().size());
			for (Player player : getPlayers()) {
				String displayName = player.getDisplayName();
				String name = Misc.formatPlayerNameForDisplay(player.getUsername());
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
				dataBlock[kickOffset] = (byte) (player.getUsername().equals(owner) ? 0 : getWhoCanKickOnChat());
				player.packets().sendFriendsChatChannel();
			}
		}
	}

	public byte[] getDataBlock() {
		return dataBlock;
	}

	public FriendChatsManager(Player player) {
		owner = player.getUsername();
		ownerDisplayName = player.getDisplayName();
		settings = player.getFriendsIgnores();
		players = new CopyOnWriteArrayList<Player>();
		bannedPlayers = new ConcurrentHashMap<String, Long>();
	}

	public static void destroyChat(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.destroyChat();
			player.packets().sendMessage("Your friends chat channel has now been disabled!");
		}
	}

	public static void linkSettings(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.settings = player.getFriendsIgnores();
		}
	}

	public static void refreshChat(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.refreshChannel();
		}
	}

	public static List<Player> getLootSharingPeople(Player player) {
		//if (!player.toggleLootShare())
		//  return null;
		FriendChatsManager chat = player.getCurrentFriendChat();
		if (chat == null)
			return null;
		List<Player> players = new ArrayList<Player>();
//		for (Player p2 : player.getCurrentFriendChat().getPlayers()) {
//			//  if (p2.toggleLootShare() && p2.withinDistance(player))
//			//	players.add(p2);
//		} // TODO
		return players;

	}

	public static void toogleLootShare(Player player) { // TODO
		if (player.getCurrentFriendChat() == null) {
			player.packets().sendMessage("You need to be in a Friends Chat channel to activate LootShare.");
			return;
		}
		if (!player.getUsername().equals(player.getCurrentFriendChat().getOwnerName()) && !player.getCurrentFriendChat().settings.hasRankToLootShare(player.getUsername())) {
			player.packets().sendMessage("You must be on the channel owner's friend list to use LootShare on this channel.");
			return;
		}
		player.toggleLootShare();
	}

	public static void joinChat(String ownerName, Player player) {
		synchronized (cachedFriendChats) {
			if (player.getCurrentFriendChat() != null)
				return;
			player.packets().sendMessage("Attempting to join channel...");
			String formatedName = Misc.formatPlayerNameForProtocol(ownerName);
			FriendChatsManager chat = cachedFriendChats.get(formatedName);
			if (chat == null) {
				Player owner = Game.getPlayerByDisplayName(ownerName);
				if (owner == null) {
					if (!SFiles.containsPlayer(formatedName)) {
						player.packets().sendMessage("The channel you tried to join does not exist.");
						return;
					}
					owner = SFiles.loadPlayer(formatedName);
					if (owner == null) {
						player.packets().sendMessage("The channel you tried to join does not exist.");
						return;
					}
					owner.setUsername(formatedName);
				}
				FriendsIgnores settings = owner.getFriendsIgnores();
				if (!settings.hasFriendChat()) {
					player.packets().sendMessage("The channel you tried to join does not exist.");
					return;
				}
				if (!player.getUsername().equals(ownerName) && !settings.hasRankToJoin(player.getUsername()) && player.getRights() < 2) {
					player.packets().sendMessage("You do not have a enough rank to join this friends chat channel.");
					return;
				}
				chat = new FriendChatsManager(owner);
				cachedFriendChats.put(chat.owner, chat);
				chat.joinChatNoCheck(player);
			} else
				chat.joinChat(player);
		}

	}

	/**
	 * Gets the clanWars.
	 * 
	 * @return The clanWars.
	 */
	public ClanWars getClanWars() {
		return clanWars;
	}

	/**
	 * Sets the clanWars.
	 * 
	 * @param clanWars
	 *            The clanWars to set.
	 */
	public void setClanWars(ClanWars clanWars) {
		this.clanWars = clanWars;
	}
}
