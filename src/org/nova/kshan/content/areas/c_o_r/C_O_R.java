package org.nova.kshan.content.areas.c_o_r;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.map.GlobalObject;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * This class contains information about the Cavern of Remembrance.
 * 
 * @author K-Shan
 *
 */
public class C_O_R implements Serializable /* The Cavern of Remembrance */ {
	
	/**
	 * @serialField
	 */
	private static final long serialVersionUID = 5604595744462435069L;
	
	/**
	 * The Player
	 */
	private Player player;
	
	/**
	 * Sets the player object
	 * @param player
	 */
	public C_O_R(Player player) {
		this.player = player;
		this.originalBosses = new HashMap<String, Integer>();
		this.dataBosses = new HashMap<String, Integer>();
	}
	
	/**
	 * Map contains all of the original bosses defeated by the player.
	 */
	private Map<String, Integer> originalBosses;
	
	/**
	 * The list that contains all of the data bosses defeated by the player.
	 */
	private Map<String, Integer> dataBosses;
	
	/**
	 * Returns the list of original bosses defeated by the player.
	 * @return
	 */
	public Map<String, Integer> getOriginalBosses() {
		return originalBosses;
	}
	
	/**
	 * Returns the list of the data bosses defeated by the player.
	 * @return
	 */
	public Map<String, Integer> getDataBosses() {
		return dataBosses;
	}
	
	/**
	 * Returns true if the player has the amount of kills needed to pass a barrier.
	 * @param bossName
	 * @return
	 */
	public boolean hasKills(String bossName) {
		return (originalBosses.containsKey(bossName)) && (originalBosses.get(bossName) >= C_O_R_Data.getKillsNeeded(bossName));
	}
	
	/**
	 * Returns the name of the boss with the word "the" if it needs it.
	 * @param bossName
	 * @return
	 */
	private String getFormattedName(String bossName) {
		String[] startingLetters = { "corporeal", "king black", "nomad", "tormented", "avatar" };
		for(int i = 0; i < startingLetters.length; i++)
			if(bossName.toLowerCase().contains(startingLetters[i].toLowerCase()))
				return "the "+bossName;
		return bossName;
	}
	
	/**
	 * Returns true if the player is on the other side of the barrier
	 * and is walking back to the teleport tile.
	 * @param object
	 * @return
	 */
	public boolean isWalkingBack(GlobalObject object) {
		if(player.getX() < object.getX() && player.getY() >= object.getY() || player.getY() > object.getY())
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param object
	 */
	private void passBarrier(GlobalObject object, boolean isWalkingBack) {
		if(player.getTickDelay() > 0)
			return;
		player.setTickDelay(5);
		player.addWalkSteps(player.getX(), player.getY());
		player.setCantMove(true);
		Game.submit(new GameTick(.7) {
				
			int ticks = 0;
			
			@Override
			public void run() {
				if(ticks == 0)
					player.animate(3067);
				else if(ticks == 1) {
					int nextX = object.getX() >= 103 ? player.getX() + (isWalkingBack ? 2 : -2) : player.getX();
					int nextY = !(object.getX() >= 103) ? player.getY() - (isWalkingBack ? 2 : -2) : player.getY();
					int z = player.getZ();
					int size = player.getSize();
					int addOffsetX = 0;
					if(Game.isTileClipped(nextX, nextY, z, size)) {
						if(!(object.getX() >= 103)) {
							int tileCount = -10;
							boolean stop = false;
							while(tileCount <= 10 && !stop) {
								if(!Game.isTileClipped(nextX + tileCount, nextY, z, size)) {
									addOffsetX = tileCount;
									stop = true;
								}
								tileCount++;
							}
						}
					}
					nextX += addOffsetX;
					player.setLocation(nextX, nextY, player.getZ());
					player.setCantMove(false);
					String bossName = object.getName().replace("Barrier (", "").replace(")", "");
					int kills = originalBosses.get(bossName);
					if(isWalkingBack)
						player.sm("You land on the other side of the barrier.");
					else {
						player.sm("Congratulations on defeating <col=ff0000>"+getFormattedName(bossName)+"</col> "+kills+" times!");
						if(bossName.equals("Nex"))
							player.sm("<col=000000><shad=9932CC>Welcome to the Cavern of Remembrance.");
						else if(bossName.equals("King Black Dragon"))
							player.sm("<col=000000><shad=9932CC>Congratulations on making it this far!");
						else if(bossName.equals("Corporeal Beast"))
							player.sm("<col=000000><shad=9932CC>You're almost there, just one more to go.");
					}
					player.graphics(86);
					stop();
				}
				ticks++;
			}
			
		});
			
	}
	
	// Boss list: Objects 64088-64099
	
	/**
	 * Interacts with the boss objects.
	 * @param player
	 * @param object
	 */
	public void interact(final GlobalObject object) {
		switch(object.defs().getOptions()[0]) {
			case "Challenge": // TODO
				player.sm("<col=ff0000>WARNING: "+object.getName()+" is much stronger than the last time you fought it.");
				player.sm("<col=ff0000>It will take all of your skills to defeat it. Are you ready?");
				break;
			case "Pass": // Barriers
				String bossName = object.getName().replace("Barrier (", "").replace(")", "");
				if(!hasKills(bossName))
					player.sm("You need to kill "+getFormattedName(bossName)+" "
						+ ""+C_O_R_Data.getKillsNeeded(bossName)+" times in order to pass this barrier.");
				else
					passBarrier(object, isWalkingBack(object));
				break;
		}
	}
	
	/**
	 * Adds the boss to the list of normal bosses defeated.
	 * @param npc
	 */
	public void addBossKilled(final NPC npc) {
		if(!originalBosses.containsKey(npc.getName()))
			originalBosses.put(npc.getName(), 1);
		else
			originalBosses.put(npc.getName(), originalBosses.get(npc.getName()) + 1);
		if(originalBosses.get(npc.getName()) == C_O_R_Data.getKillsNeeded(npc.getName()))
			player.packets().sendMessage("You've unlocked the <col=ff0000>"+npc.getName()+"</col> barrier at the Cavern of Remembrance.");
	}

}
