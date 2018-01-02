package org.nova.game.player.content;

import org.nova.game.player.Player;
import org.nova.utility.loading.playerutils.SFiles;
import org.nova.utility.misc.Misc;


/**
 * @author Fuzen Seth
 * 
 */
public class DisplayNameAction {
	

	
	public static void RemoveDisplay (Player player) {
		player.setDisplayName(Misc.formatPlayerNameForDisplay(player.getUsername()));
		player.interfaces().closeChatBoxInterface();
		SFiles.savePlayer(player);
		player.getAppearance().generateAppearanceData();
		player.sm("Display name removed, if you don't see your original name please relog.");
	}


	
}