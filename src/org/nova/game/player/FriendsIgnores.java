package org.nova.game.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.nova.game.Game;
import org.nova.game.player.content.FriendChatsManager;
import org.nova.network.decoders.packets.PacketDecoder;
import org.nova.utility.misc.Misc;

public class FriendsIgnores implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 39693097250367467L;

	// friends chat
	private String chatName;
	private HashMap<String, Integer> friendsChatRanks;
	private byte whoCanEnterChat;
	private byte whoCanTalkOnChat;
	private byte whoCanKickOnChat;
	private byte whoCanShareloot;
	private byte friendsChatStatus;
	@SuppressWarnings("unused")
	private boolean coinshare;

	// friends list
	private ArrayList<String> friends;
	@SuppressWarnings("unused")
	private ArrayList<String> ignores;
	private byte privateStatus;

	private transient Player player;

	public HashMap<String, Integer> getFriendsChatRanks() {
		if (friendsChatRanks == null) {// temporary
			whoCanKickOnChat = 7;
			whoCanShareloot = -1;
			friendsChatRanks = new HashMap<String, Integer>(200);
			for (String friend : friends)
				friendsChatRanks.put(friend, 0);
		}
		return friendsChatRanks;
	}

	public boolean canTalk(Player player) {
		return getRank(player.getUsername()) >= whoCanTalkOnChat;
	}

	public int getRank(String username) {
		Integer rank = getFriendsChatRanks().get(username);
		if (rank == null)
			return -1;
		return rank;
	}

	public int getWhoCanKickOnChat() {
		return whoCanKickOnChat;
	}

	public boolean hasRankToJoin(String username) {
		return getRank(username) >= whoCanEnterChat;
	}

	public String getChatName() {
		return chatName == null ? "" : chatName;
	}

	public boolean hasFriendChat() {
		return chatName != null;
	}

	public FriendsIgnores() {
		friends = new ArrayList<String>(200);
		ignores = new ArrayList<String>(100);
		friendsChatRanks = new HashMap<String, Integer>(200);
		whoCanKickOnChat = 7;
		whoCanShareloot = -1;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void changeFriendStatus(Player p2, boolean online) {
		if (!friends.contains(p2.getUsername()))
			return;
		if (online && !isOnline(p2))
			online = false;
		player.packets().sendFriend(
				Misc.formatPlayerNameForDisplay(p2.getUsername()),
				p2.getDisplayName(), 1, online, true);
	}

	public void sendFriendsMyStatus(boolean online) {
		for (Player p2 : Game.getPlayers()) {
			if (p2 == null || !p2.hasStarted() || p2.hasFinished())
				continue;
			p2.getFriendsIgnores().changeFriendStatus(player, online);
		}
	}

	public void sendMessage(Player p2, String message) {
		if (privateStatus == 2) {// off
			privateStatus = 0;
			sendFriendsMyStatus(true);
			player.packets().sendPrivateGameBarStage();
		}
		player.packets().sendPrivateMessage(p2.getDisplayName(), message);
		p2.packets().receivePrivateMessage(
				Misc.formatPlayerNameForDisplay(player.getUsername()),
				player.getDisplayName(), player.getRights(), message);
	}

	public void sendQuickChatMessage(Player p2,
			QuickChatMessage quickChatMessage) {
		player.packets().sendPrivateQuickMessageMessage(p2.getDisplayName(),
				quickChatMessage);
		p2.packets().receivePrivateChatQuickMessage(
				Misc.formatPlayerNameForDisplay(player.getUsername()),
				player.getDisplayName(), player.getRights(), quickChatMessage);

	}

	public void changeRank(String username, int rank) {
		if (rank < 0 || rank > 6)
			return;
		String formatedUsername = Misc.formatPlayerNameForProtocol(username);
		if (!friends.contains(formatedUsername))
			return;
		getFriendsChatRanks().put(formatedUsername, rank);
		String displayName;
		Player p2 = Game.getPlayerByDisplayName(username);
		if (p2 != null)
			displayName = p2.getDisplayName();
		else
			displayName = Misc.formatPlayerNameForDisplay(username);
		boolean online = p2 != null && isOnline(p2);
		player.packets().sendFriend(
				Misc.formatPlayerNameForDisplay(username), displayName, 1,
				online, true);
		FriendChatsManager.refreshChat(player);
	}

	public void handleFriendChatButtons(int interfaceId, int componentId,
			int packetId) {
		if (interfaceId == 1109) {
			if (componentId == 27) {
				if (player.getCurrentFriendChat() != null)
					player.getCurrentFriendChat().leaveChat(player, false);
			} else if (componentId == 33) {
				if (player.interfaces().containsScreenInter()) {
					player.packets()
							.sendMessage(
									"Please close the interface you have opened before using Friends Chat setup.");
					return;
				}
				player.stopAll();
				openFriendChatSetup();
			}
		} else if (interfaceId == 1108) {
			if (componentId == 22) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET) {
					player.packets().sendRunScript(109,
							new Object[] { "Enter chat prefix:" });
				} else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET) {
					if (chatName != null) {
						chatName = null;
						refreshChatName();
						FriendChatsManager.destroyChat(player);
					}
				}
			} else if (componentId == 23) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					whoCanEnterChat = -1;
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					whoCanEnterChat = 0;
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					whoCanEnterChat = 1;
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					whoCanEnterChat = 2;
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET)
					whoCanEnterChat = 3;
				else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
					whoCanEnterChat = 4;
				else if (packetId == PacketDecoder.ACTION_BUTTON6_PACKET)
					whoCanEnterChat = 5;
				else if (packetId == PacketDecoder.ACTION_BUTTON7_PACKET)
					whoCanEnterChat = 6;
				else if (packetId == PacketDecoder.ACTION_BUTTON10_PACKET)
					whoCanEnterChat = 7;
				refreshWhoCanEnterChat();
			} else if (componentId == 24) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					whoCanTalkOnChat = -1;
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					whoCanTalkOnChat = 0;
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					whoCanTalkOnChat = 1;
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					whoCanTalkOnChat = 2;
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET)
					whoCanTalkOnChat = 3;
				else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
					whoCanTalkOnChat = 4;
				else if (packetId == PacketDecoder.ACTION_BUTTON6_PACKET)
					whoCanTalkOnChat = 5;
				else if (packetId == PacketDecoder.ACTION_BUTTON7_PACKET)
					whoCanTalkOnChat = 6;
				else if (packetId == PacketDecoder.ACTION_BUTTON10_PACKET)
					whoCanTalkOnChat = 7;
				refreshWhoCanTalkOnChat();
			} else if (componentId == 25) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					whoCanKickOnChat = -1;
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					whoCanKickOnChat = 0;
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					whoCanKickOnChat = 1;
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					whoCanKickOnChat = 2;
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET)
					whoCanKickOnChat = 3;
				else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
					whoCanKickOnChat = 4;
				else if (packetId == PacketDecoder.ACTION_BUTTON6_PACKET)
					whoCanKickOnChat = 5;
				else if (packetId == PacketDecoder.ACTION_BUTTON7_PACKET)
					whoCanKickOnChat = 6;
				else if (packetId == PacketDecoder.ACTION_BUTTON10_PACKET)
					whoCanKickOnChat = 7;
				refreshWhoCanKickOnChat();
				FriendChatsManager.refreshChat(player);
			} else if (componentId == 26) {
				if (packetId == PacketDecoder.ACTION_BUTTON1_PACKET)
					whoCanShareloot = -1;
				else if (packetId == PacketDecoder.ACTION_BUTTON2_PACKET)
					whoCanShareloot = 0;
				else if (packetId == PacketDecoder.ACTION_BUTTON3_PACKET)
					whoCanShareloot = 1;
				else if (packetId == PacketDecoder.ACTION_BUTTON4_PACKET)
					whoCanShareloot = 2;
				else if (packetId == PacketDecoder.ACTION_BUTTON5_PACKET)
					whoCanShareloot = 3;
				else if (packetId == PacketDecoder.ACTION_BUTTON9_PACKET)
					whoCanShareloot = 4;
				else if (packetId == PacketDecoder.ACTION_BUTTON6_PACKET)
					whoCanShareloot = 5;
				else if (packetId == PacketDecoder.ACTION_BUTTON7_PACKET)
					whoCanShareloot = 6;
				refreshWhoCanShareloot();
			}
		}
	}

	public void setChatPrefix(String name) {
		if (name.length() < 1 || name.length() > 20)
			return;
		this.chatName = name;
		refreshChatName();
		FriendChatsManager.refreshChat(player);
	}

	public void refreshChatName() {
		player.packets().sendIComponentText(1108, 22,
				chatName == null ? "Chat disabled" : chatName);
	}

	public void refreshWhoCanShareloot() {
		String text;
		if (whoCanShareloot == 0)
			text = "Any friends";
		else if (whoCanShareloot == 1)
			text = "Recruit+";
		else if (whoCanShareloot == 2)
			text = "Corporal+";
		else if (whoCanShareloot == 3)
			text = "Sergeant+";
		else if (whoCanShareloot == 4)
			text = "Lieutenant+";
		else if (whoCanShareloot == 5)
			text = "Captain+";
		else if (whoCanShareloot == 6)
			text = "General+";
		else
			text = "No-one";
		player.packets().sendIComponentText(1108, 26, text);
	}

	public void refreshWhoCanKickOnChat() {
		String text;
		if (whoCanKickOnChat == 0)
			text = "Any friends";
		else if (whoCanKickOnChat == 1)
			text = "Recruit+";
		else if (whoCanKickOnChat == 2)
			text = "Corporal+";
		else if (whoCanKickOnChat == 3)
			text = "Sergeant+";
		else if (whoCanKickOnChat == 4)
			text = "Lieutenant+";
		else if (whoCanKickOnChat == 5)
			text = "Captain+";
		else if (whoCanKickOnChat == 6)
			text = "General+";
		else if (whoCanKickOnChat == 7)
			text = "Only Me";
		else
			text = "Anyone";
		player.packets().sendIComponentText(1108, 25, text);
	}

	public void refreshWhoCanTalkOnChat() {
		String text;
		if (whoCanTalkOnChat == 0)
			text = "Any friends";
		else if (whoCanTalkOnChat == 1)
			text = "Recruit+";
		else if (whoCanTalkOnChat == 2)
			text = "Corporal+";
		else if (whoCanTalkOnChat == 3)
			text = "Sergeant+";
		else if (whoCanTalkOnChat == 4)
			text = "Lieutenant+";
		else if (whoCanTalkOnChat == 5)
			text = "Captain+";
		else if (whoCanTalkOnChat == 6)
			text = "General+";
		else if (whoCanTalkOnChat == 7)
			text = "Only Me";
		else
			text = "Anyone";
		player.packets().sendIComponentText(1108, 24, text);
	}

	public void refreshWhoCanEnterChat() {
		String text;
		if (whoCanEnterChat == 0)
			text = "Any friends";
		else if (whoCanEnterChat == 1)
			text = "Recruit+";
		else if (whoCanEnterChat == 2)
			text = "Corporal+";
		else if (whoCanEnterChat == 3)
			text = "Sergeant+";
		else if (whoCanEnterChat == 4)
			text = "Lieutenant+";
		else if (whoCanEnterChat == 5)
			text = "Captain+";
		else if (whoCanEnterChat == 6)
			text = "General+";
		else if (whoCanEnterChat == 7)
			text = "Only Me";
		else
			text = "Anyone";
		player.packets().sendIComponentText(1108, 23, text);
	}

	public void openFriendChatSetup() {
		player.interfaces().sendInterface(1108);
		refreshChatName();
		refreshWhoCanEnterChat();
		refreshWhoCanTalkOnChat();
		refreshWhoCanKickOnChat();
		refreshWhoCanShareloot();
	}

	public void addFriend(String username) {
		if (friends.size() >= 200) {
			player.packets().sendMessage("Your friends list is full.");
			return;
		}
		if (username.equals(player.getUsername())) {
			player.packets().sendMessage("You can't add yourself.");
			return;
		}
		String displayName;
		Player p2 = Game.getPlayerByDisplayName(username);
		if (p2 != null)
			displayName = p2.getDisplayName();
		else
			displayName = Misc.formatPlayerNameForDisplay(username);
		String formatedUsername = Misc.formatPlayerNameForProtocol(username);
		if (friends.contains(formatedUsername)) {
			player.packets().sendMessage(
					(username) + " is already on your friends list.");
			return;
		}
		friends.add(formatedUsername);
		getFriendsChatRanks().put(formatedUsername, 0);
		FriendChatsManager.refreshChat(player);
		boolean online = p2 != null && isOnline(p2);
		player.packets().sendFriend(
				Misc.formatPlayerNameForDisplay(username), displayName, 1,
				online, online);
		if (privateStatus == 1 && p2 != null)
			p2.getFriendsIgnores().changeFriendStatus(player, true);
	}

	public void removeFriend(String username) {
		String formatedUsername = Misc.formatPlayerNameForProtocol(username);
		Player p2 = Game.getPlayerByDisplayName(username);
		if (!friends.remove(formatedUsername)) {
			if (p2 == null)
				return;
			friends.remove(p2.getUsername());
			getFriendsChatRanks().remove(p2.getUsername());
			FriendChatsManager.refreshChat(player);
		} else {
			getFriendsChatRanks().remove(formatedUsername);
			FriendChatsManager.refreshChat(player);
		}
		if (privateStatus == 1 && p2 != null)
			p2.getFriendsIgnores().changeFriendStatus(player, true);
	}

	public boolean isOnline(Player p2) {
		if (p2.getFriendsIgnores().privateStatus == 2)
			return false;
		if (p2.getFriendsIgnores().privateStatus == 1
				&& !p2.getFriendsIgnores().friends.contains(player
						.getUsername()))
			return false;
		return true;
	}

	public void init() {
		player.packets().sendUnlockFriendList();
		for (String username : friends) {
			if (username == null) // shouldnt happen
				continue;
			String displayName;
			Player p2 = Game.getPlayerByDisplayName(username);
			if (p2 != null)
				displayName = p2.getDisplayName();
			else
				displayName = Misc.formatPlayerNameForDisplay(username);
			player.packets().sendFriend(
					Misc.formatPlayerNameForDisplay(username), displayName, 1,
					p2 != null && isOnline(p2), false);
		}
		if (privateStatus != 2)
			sendFriendsMyStatus(true);
		if (hasFriendChat())
			FriendChatsManager.linkSettings(player);
	}
	public int getFriendsChatStatus() {
		return friendsChatStatus;
	}

	public void setFriendsChatStatus(int friendsChatStatus) {
		this.friendsChatStatus = (byte) friendsChatStatus;
	}

	public byte getPrivateStatus() {
		return privateStatus;
	}

	public void setPrivateStatus(int privateStatus) {
		this.privateStatus = (byte) privateStatus;
		sendFriendsMyStatus(true);
	}

	public boolean hasRankToLootShare(String username) {
		// TODO Auto-generated method stub
		return false;
	}


}