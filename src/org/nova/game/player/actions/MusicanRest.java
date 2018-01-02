package org.nova.game.player.actions;

import org.nova.game.masks.Animation;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

public class MusicanRest extends Action {

	private static int[][] REST_DEFS = { { 5713, 1549, 5748 },
			{ 11786, 1550, 11788 }, { 5713, 1551, 2921 }

	};
	private NPC npc;
	private int index;
	
	@Override
	public boolean start(Player player) {
		if (!process(player))
			return false;
		index = Misc.random(REST_DEFS.length);
		player.setMusicianResting(true);
		player.setNextAnimation(new Animation(REST_DEFS[index][0]));
		player.getAppearance().setRenderEmote(REST_DEFS[index][1]);
		player.interfaces().closeChatBoxInterface();
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		if (player.getPoison().isPoisoned()) {
			player.packets().sendMessage(
					"You can't rest while you're poisoned.");
			return false;
		}
		if (player.getAttackedByDelay() + 10000 > Misc.currentTimeMillis()) {
			player.packets().sendMessage(
					"You can't rest until 10 seconds after the end of combat.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		return 0;
	}

	@Override
	public void stop(Player player) {
		player.setResting(false);
		player.setMusicianResting(false);
		player.setNextAnimation(new Animation(REST_DEFS[index][2]));
		player.getEmotesManager().setNextEmoteEnd();
		player.getAppearance().setRenderEmote(-1);
	}

}
