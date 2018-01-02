package org.nova.game.player.content.guilds.impl;

import org.nova.game.map.GlobalObject;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.content.guilds.AbstractGuild;

public class ChampionsGuild implements AbstractGuild {

	@Override
	public String getGuildName() {
		return "Champion's Guild";
	}

	@Override
	public int requestedLevel() {
		return 0;
	}

	@Override
	public void enterGuild(Player player) {
	if (!player.isCompletedHoarfrost() && !player.isCompletedHoarfrostDepths()) {
		player.sendMessage("You must have completed Hoarfrost depths and Hoarfrost hollow to enter here.");
		return;
	}
		
	}

	public void handleDoor(Player player, GlobalObject object) {
		
	}
	@Override
	public boolean handleNPCInteract(Player player, NPC npc) {
		switch (npc.getId()) {
		case 198: //Guildmaster
			
			break;
		}
		return false;
	}

	@Override
	public boolean handleObjectInteract(Player player, GlobalObject gameObject) {
		switch (gameObject.getId()) {
		case 1805:
			enterGuild(player);
			gameObject.moveLocation(0, 3, 0);
			break;
			
		}
		return false;
	}

}
