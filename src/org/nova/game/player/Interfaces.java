package org.nova.game.player;

import java.util.concurrent.ConcurrentHashMap;

import org.nova.game.Game;
import org.nova.kshan.content.PlayTime;

public class Interfaces {

	public static final int FIXED_WINDOW_ID = 548;
	public static final int RESIZABLE_WINDOW_ID = 746;
	public static final int CHAT_BOX_TAB = 13;
	public static final int FIXED_SCREEN_TAB_ID = 9;
	public static final int FIXED_SCREEN2_TAB_ID = 11;
	public static final int RESIZABLE_SCREEN_TAB_ID = 12;
	public static final int FIXED_INV_TAB_ID = 199; // wrong
	public static final int RESIZABLE_INV_TAB_ID = 87;
	private Player player;

	private final ConcurrentHashMap<Integer, int[]> openedinterfaces = new ConcurrentHashMap<Integer, int[]>();

	private boolean fullScreen;
	private int windowsPane;

	public Interfaces(Player player) {
		this.player = player;
	}

	public void sendTeamOverlay() {
		player.packets().sendOverlay(489);
	}
	
	public boolean fullScreen() {
		return fullScreen;
	}
	
	private transient boolean keepChatboxOpen;
	
	public void setKeepChatboxOpen(boolean b) {
		this.keepChatboxOpen = b;
	}
	
	public boolean chatboxKeptOpen() {
		return keepChatboxOpen;
	}
	
	public void sendTab(int tabId, int interfaceId) {
		player.closeInterfaces();
		player.packets().sendHideIComponent(489, 7, true);
		player.packets().sendHideIComponent(489, 8, true);
		player.packets().sendHideIComponent(489, 9, true);
		player.packets().sendHideIComponent(489, 10, true);
		player.packets().sendHideIComponent(489, 11, true);
		player.interfaces().closeChatBoxInterface();
		player.packets().sendInterface(true,
				fullScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, tabId,
				interfaceId);
	}

	public void sendChatBoxInterface(int interfaceId) {
		player.packets().sendInterface(true, 752, CHAT_BOX_TAB, interfaceId);
	}

	public void closeChatBoxInterface() {
		player.packets().closeInterface(CHAT_BOX_TAB);
	}
	
	public void sendOverlay(int interfaceId, boolean fullScreen) {
		sendTab(fullScreen ? fullScreen ? 1 : 11 : 0, interfaceId);
	}

	public void closeOverlay(boolean fullScreen) {
		player.packets().closeInterface(
				fullScreen ? fullScreen ? 1 : 11 : 0);
	}
	public void sendInterface(int interfaceId) {
		player.packets()
				.sendInterface(
						false,
						fullScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID,
						fullScreen ? RESIZABLE_SCREEN_TAB_ID
								: FIXED_SCREEN_TAB_ID, interfaceId);
	}

	public void sendInventoryInterface(int childId) {
		player.packets().sendInterface(false,
				fullScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID,
				fullScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID,
				childId);
	}

	public final void sendInterfaces() {
		if (player.getDisplayMode() == 2 || player.getDisplayMode() == 3) {
			fullScreen = true;
			sendFullScreenInterfaces();
		} else {
			fullScreen = false;
			sendFixedInterfaces();
		}
		player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		player.getMusicsManager().unlockMusicPlayer();
		player.getInventory().unlockInventoryOptions();
		player.getPrayer().unlockPrayerBookButtons();
		if (player.getFamiliar() != null && player.isRunning())
			player.getFamiliar().unlock();
		player.getControllerManager().sendInterfaces();
	}

	public void replaceRealChatBoxInterface(int interfaceId) {
		player.packets().sendInterface(true, 752, 12, interfaceId);
	}

	public void closeReplacedRealChatBoxInterface() {
		player.packets().closeInterface(752, 12);
	}
	
	public void sendFullScreenInterfaces() {
		player.packets().sendWindowsPane(746, 0);
		sendTab(15, 745);
		sendTab(19, 751);
		sendTab(73, 752);
		sendTab(177, 748); // 177
		sendTab(178, 749);
		sendTab(179, 750);
		sendTab(180, 747); // summoning
		player.packets().sendInterface(true, 752, 9, 137);
		sendTab(90, 884);
		sendTab(92, 320);
		//sendMsPortal();
		sendBook();// 259); // Achievement tab
		sendControlPanel(); // control panel 
		sendInventory();
		sendEquipment();
		sendPrayerBook();
		sendMagicBook();
		sendTab(99, 550); // friend list
		sendTab(100, 1109); // 551 ignore now friendchat
		sendTab(101, 1110); // 589 old clan chat now new clan chat
		sendSettings();
		sendTab(103, 590); // emote, 590 in sendFullScreenInterfaces
		sendTab(104, 187); // music
		sendTab(105, 1150);//34); // notes / item search inter (custom)
		sendTab(108, 182); // logout
	}
	
	public void sendControlPanel() {
		sendTab(fullScreen ? 93 : 207, 506);
	}
	
	public void sendBook() {
		sendTab(fullScreen ? 91 : 205, 930); 
	}
	
	public void sendBookInfo() {
		player.packets().sendString("<col=c12006>Let us know what else to add.", 930, 10);
		player.packets().sendString("<col=ff0000>Nova"
			+ "<br><br>Online: <col=ffffff>"+Game.getPlayers().size()+"<br>Wilderness:"
			+ " <col=ffffff>"+Game.playersInWilderness.size()+""
			+ "<br><br><col=ff0000>Player<br><br>Play Time: <col=ffffff>"
			+ ""+player.playTime().getPlayTime(PlayTime.DAYS)+" days "+player.playTime().getPlayTime(PlayTime.HOURS)+""
			+ " hrs "+player.playTime().getPlayTime(PlayTime.MINUTES)+" mins "
			+ ""+player.playTime().getPlayTime(PlayTime.SECONDS)+" secs"
			+ "<br>Combat XP Boost: <col=ffffff>x"+player.combatXPBoost()+"<br>"
			+ "Skilling XP Boost: <col=ffffff>x"+player.skillXPBoost()+"<br>"
			+ "Game Mode: <col=ffffff>"+player.gameMode()+"<br>Membership: <col=ffffff>"+player.getMembership().getRank()+""
			+ "<br><br><col=ff0000>Slayer<br><br>Task: <col=ffffff>"+(player.slayerTask().amountLeft() < 1 ? "Speak to <col=00ff00>"+
			player.slayerTask().currentMaster()+", <col=ffffff>who<br>"
			+ "<col=ffffff>is located in "+player.slayerTask().currentMasterLocation()+"." : 
			player.slayerTask().monsterName()+"s<col=ffffff><br>Complete: "
			+ "<col=ffffff>"+player.slayerTask().amountKilled()+"/"+player.slayerTask().taskTotal())
			+ "<br><br><col=ff0000>Wilderness<br><br>Kills: <col=ffffff>"+(int) player.getKills()+"<br>"
			+ "Deaths: <col=ffffff>"+(int)player.getDeaths()+""+ "<br>KDR: <col=ffffff>"+player.getKDR()+"<br>Streak: "
			+ "<col=ffffff>"+(int) player.getStreak()+"<br>Points: <col=ffffff>"+player.getPkPoints(), 930, 16);
	}

	public void sendEquipment() {
		sendTab(fullScreen ? 95 : 209, 387);
	}

	public void closeEquipment() {
		player.packets().closeInterface(fullScreen ? 95 : 209);
	}

	public void sendInventory() {
		sendTab(fullScreen ? 94 : 208, Inventory.INVENTORY_INTERFACE);
	}

	public void closeInventory() {
		player.packets().closeInterface(fullScreen ? 94 : 208);
	}

	public void sendSkills() {
		sendTab(fullScreen ? 30 : 151, 320);
	}

	public void sendSettings() {
		sendSettings(261);
	}

	public void sendSettings(int interfaceId) {
		sendTab(fullScreen ? 102 : 216, interfaceId);
	}

	public void sendPrayerBook() {
		sendTab(fullScreen ? 96 : 210, 271);
	}

	public void sendMagicBook() {
		sendTab(fullScreen ? 97 : 211, player.getCombatDefinitions()
				.getSpellBook());
	}
	
	public void sendFixedInterfaces() {
		player.packets().sendWindowsPane(548, 0);
		sendTab(15, 745);
		sendTab(68, 751); // Chat options
		sendTab(192, 752); // Chatbox
		player.packets().sendInterface(true, 752, 9, 137);
		sendTab(17, 754);
		sendTab(183, 748); // HP bar
		sendTab(185, 749); // Prayer bar
		sendTab(186, 750); // Energy bank
		sendTab(188, 747);
		sendTab(204, 884); // Attack tab
		sendTab(206, 320); // Skill tab
		//sendMsPortal();// Quest tab 190
		sendTab(205, 1056);// Task tab
		sendControlPanel();
		//sendTab(207, 506); // Quest tab (Custom)
		//TabsManager.sendNoticeboard(player);
		sendBook();
		sendInventory(); // Inventory tab
		sendEquipment();
		sendPrayerBook();
		sendMagicBook();
		sendTab(213, 550); // Friend tab
		sendTab(214, 1109); // friendchat
		sendTab(215, 1110); // clan chat
		sendSettings();
		sendTab(217, 590); // Emote tab
		sendTab(218, 187); // Music tab
		sendTab(219, 1150);//34); // Notes tab / item search
		sendTab(222, 182); // Logout tab
	}
	
	public boolean addInterface(int windowId, int tabId, int childId) {
		if (openedinterfaces.containsKey(tabId))
			player.packets().closeInterface(tabId);
		openedinterfaces.put(tabId, new int[] { childId, windowId });
		return openedinterfaces.get(tabId)[0] == childId;
	}

	public boolean containsInterface(int tabId, int childId) {
		if (childId == windowsPane)
			return true;
		if (!openedinterfaces.containsKey(tabId))
			return false;
		return openedinterfaces.get(tabId)[0] == childId;
	}

	public int getTabWindow(int tabId) {
		if (!openedinterfaces.containsKey(tabId))
			return FIXED_WINDOW_ID;
		return openedinterfaces.get(tabId)[1];
	}

	public boolean containsInterface(int childId) {
		if (childId == windowsPane)
			return true;
		for (int[] value : openedinterfaces.values())
			if (value[0] == childId)
				return true;
		return false;
	}

	public boolean containsTab(int tabId) {
		return openedinterfaces.containsKey(tabId);
	}

	public void removeAll() {
		openedinterfaces.clear();
	}

	public boolean containsScreenInter() {
		return containsTab(fullScreen ? RESIZABLE_SCREEN_TAB_ID
				: FIXED_SCREEN_TAB_ID);
	}

	public void closeScreenInterface() {
		player.packets()
				.closeInterface(
						fullScreen ? RESIZABLE_SCREEN_TAB_ID
								: FIXED_SCREEN_TAB_ID);
	}

	public boolean containsInventoryInter() {
		return containsTab(fullScreen ? RESIZABLE_INV_TAB_ID
				: FIXED_INV_TAB_ID);
	}

	public void closeInventoryInterface() {
		player.packets().closeInterface(
				fullScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID);
	}

	public boolean containsChatBoxInter() {
		return containsTab(CHAT_BOX_TAB);
	}

	public boolean removeTab(int tabId) {
		return openedinterfaces.remove(tabId) != null;
	}

	public boolean removeInterface(int tabId, int childId) {
		if (!openedinterfaces.containsKey(tabId))
			return false;
		if (openedinterfaces.get(tabId)[0] != childId)
			return false;
		return openedinterfaces.remove(tabId) != null;
	}

	public void sendScreenInterface(int backgroundInterface, int interfaceId) {
		player.interfaces().closeScreenInterface();

		if (isFullScreen()) {
			player.packets().sendInterface(false, RESIZABLE_WINDOW_ID, 9,
					backgroundInterface);
			player.packets().sendInterface(false, RESIZABLE_WINDOW_ID, 11,
					interfaceId);
		} else
			player.packets().sendWindowsPane(interfaceId, 0);

		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				if (isFullScreen()) {
					player.packets().closeInterface(9);
					player.packets().closeInterface(11);
				} else
					player.packets().sendWindowsPane(FIXED_WINDOW_ID, 0);
			}
		});
	}

	public boolean isFullScreen() {
		return fullScreen;
	}
public void sendBindingTab() {
	player.packets().sendHideIComponent(1024, 5, true);
	sendTab(fullScreen ? 93 : 207, 1024);
}

	public void setWindowsPane(int windowsPane) {
		this.windowsPane = windowsPane;
	}

	public int getWindowsPane() {
		return windowsPane;
	}
	
	public void gazeOrbOfOculus() {
		player.packets().sendWindowsPane(475, 0);
		player.packets().sendInterface(true, 475, 57, 751);
		player.packets().sendInterface(true, 475, 55, 752);
		player.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
			player.packets().sendBlackOut(0);
				sendTab(player.interfaces().isFullScreen() ? 746 : 548, 0);
				player.packets().sendResetCamera();
			
			}
			
		});
	}
	/*
	 * returns lastGameTab
	 */
	public int openGameTab(int tabId) {
		player.packets().sendGlobalConfig(168, tabId);
		int lastTab = 4; // tabId
		// tab = tabId;
		return lastTab;
	}

	public void refreshAllTabs() {
		if(player.interfaces().fullScreen())
			sendFullScreenInterfaces();
		else
			sendFixedInterfaces();
		player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		player.getMusicsManager().unlockMusicPlayer();
		player.getInventory().unlockInventoryOptions();
		player.getPrayer().unlockPrayerBookButtons();
		if (player.getFamiliar() != null && player.isRunning())
			player.getFamiliar().unlock();
	}

	public void sendLogoutTab() {
		if(!fullScreen) {
			sendTab(15, 745);
			sendTab(68, 751); // Chat options
			sendTab(17, 754);
			sendTab(222, 182); // Logout tab
		} else {
			sendTab(15, 745);
			sendTab(19, 751);
			sendTab(73, 752);
			sendTab(177, 748); // 177
			sendTab(178, 749);
			sendTab(179, 750);
			sendTab(180, 747); // summoning
			player.packets().sendInterface(true, 752, 9, 137);
			sendTab(108, 182); // logout	
		}
	}

	public void sendDefaultPane() {
		player.packets().sendWindowsPane(player.interfaces().isFullScreen() ? 
			RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, 0);
	}
	
	public void sendBlankItemOnInterface(int inter, int buttonId) {
		player.packets().sendItemOnIComponent(inter, buttonId, 22456, 1);
	}

	public void sendItemSearchTab() {
		sendTab(fullScreen ? 105 : 219, 1150);
	}

}