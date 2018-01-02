package org.nova.game.player.content.guilds.impl;

import org.nova.game.map.GlobalObject;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.content.guilds.AbstractGuild;

public class FishingGuild implements AbstractGuild {

	@Override
	public String getGuildName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int requestedLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void enterGuild(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleNPCInteract(Player player, NPC npc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleObjectInteract(Player player, GlobalObject gameObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
