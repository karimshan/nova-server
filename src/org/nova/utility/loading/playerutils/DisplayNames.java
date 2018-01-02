package org.nova.utility.loading.playerutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.nova.game.player.Player;
import org.nova.game.player.content.FriendChatsManager;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;


public final class DisplayNames {
	
	private static ArrayList<String> cachedNames;
	
	

	private static final String PATH = "data/playerdata/displayNames.ser";
	
	private DisplayNames() {
		
	}
	
	@SuppressWarnings("unchecked")
	public static void init() {
		File file = new File(PATH);
		if (file.exists())
			try {
				cachedNames = (ArrayList<String>) SFiles.loadSerializedFile(file);
				return;
			} catch (Throwable e) {
				Logger.handle(e);
			}
		cachedNames = new ArrayList<String>();
	}

	
	public static void save() {
		try {
			SFiles.storeSerializableClass(cachedNames, new File(PATH));
		} catch (IOException e) {
			Logger.handle(e);
		}
	}
	
	public static boolean setDisplayName(Player player, String displayName) {
		synchronized (cachedNames) {
			if((SFiles.containsPlayer(Misc.formatPlayerNameForProtocol(displayName)) || cachedNames.contains(displayName) || !cachedNames.add(displayName)))
				return false;
			if(player.hasDisplayName())
				cachedNames.remove(player.getDisplayName());
		}
		player.setDisplayName(displayName);
		FriendChatsManager.refreshChat(player);
		player.getAppearance().generateAppearanceData();
		return true;
	}
	
	public static boolean removeDisplayName(Player player) {
		if(!player.hasDisplayName())
			return false;
		synchronized (cachedNames) {
			cachedNames.remove(player.getDisplayName());
		}
		player.setDisplayName(null);
		player.getAppearance().generateAppearanceData();
		return true;
	}
}
