package org.nova.game.player.content;

import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

public class EasterBunny {

	private static EasterBunny singleton = new EasterBunny();

	private int bunnyId;
	
	
	public static final void transfrom(Player player) {
		
	}
	
	public static final void untransform(Player player) {
		
	}
	/**
	 * Player can select a bunny they want to.
	 */
	public void displayInterface(Player player, NPC npc) {
		player.interfaces().sendInterface(625);
		//player.getPackets().sendNPCOnIComponent(interfaceId, componentId, npcId);
	}
	
	public static EasterBunny getSingleton() {
		return singleton;
	}

	public static void setSingleton(EasterBunny singleton) {
		EasterBunny.singleton = singleton;
	}
	
}
