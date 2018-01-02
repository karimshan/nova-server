package org.nova.game.player.content.cities.content.barbarianassault;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the barbarian assault minigame.
 * @since 11.4.2014
 */
public class BarbarianAssault {
	
	private boolean healer;
	private boolean attacker;
	private boolean defender;
	private boolean collector;
	
	/**
	 * Barbarian assault singleton.
	 */
	private static BarbarianAssault barbarianAssault = new BarbarianAssault();
	
	
	public static final boolean handleGameObject(Player player, GlobalObject object) {
		
		switch (object.getId()) {
		
		}
		return false;
	}
	
	public void loadGame(Player player, NPC npc) {
	switch(player.getAssaultStage()) {
	case 0:

		break;
	case 1:
		break;
	}
	}
	
	public void finishGame(Player player, NPC npc) {
		
	}
	
	
	/**
	 * Can the player teleport?
	 * @param player
	 * @param disabled
	 */
	public void teleportState(Player player, boolean disabled) {
		if (disabled)
			player.setTeleport(true);
		else
			player.setTeleport(true);
	}
	
	public static BarbarianAssault getBarbarianAssault() {
		return barbarianAssault;
	}
	
	/**
	 * Spawns a NPC.
	 * @param npcId
	 * @param nextTile
	 */
	public void spawnAssaultNPC(int npcId, Location nextTile) {
		Game.spawnNPC(npcId, nextTile, -1, false);
	}
	
}
