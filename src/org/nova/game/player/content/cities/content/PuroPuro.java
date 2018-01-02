package org.nova.game.player.content.cities.content;

import java.util.List;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class PuroPuro {

	public PuroPuro() {
		setPlayer(player);
	}
	
	private int[] countedImps = new int [10];
	
	private final int BABY_IMPLING = 1028;
	private final int YOUNG_IMPLING = 1029;
	private final int GOURMET_IMPLING = 1030;
	private final int EARTH_IMPLING = 1031;
	private final int ESSENCE_IMPLING = 1032;
	private final int ECLECTIC_IMPLING = 1033;
	private final int NATURE_IMPLING = 1034;
	private final int MAGPIE_IMPLING = 1035;
	private final int PURO_OVERLAY = 169;
	
	private Player player;
	/**
	 * Is player in puro-puro?
	 */
	private List <Player> puroUser;
	
	/**
	 * Starts the puro puro.
	 * @param player
	 */
	public void initPuroPuro() {
		puroUser.add(player);
		updateEffects();
		player.setTeleport(true);
	}
	/**
	 * enterings = Is the player going into puro puro? true if yes.
	 * @param x
	 * @param y
	 * @param h
	 * @param entering
	 */
	public void enterPuroPuro(final int x, final int y, final int h, final boolean entering) {
				WorldTasksManager.schedule(new WorldTask() {
				int loop;
				boolean entering;
				@Override
				public void run() {		
					switch (loop) {
					case 0:
                        player.setNextAnimation(new Animation(13652));
                        player.setNextGraphics(new Graphics(2602));
						break;
					case 4:
						player.setLocation(new Location(x,y,h));
						if (entering)
							entering = false;
						initPuroPuro();
						stop();
						break;
					}
					loop++;
					}
				}, 0, 1);
	}
	/**
	 * Leaves the puro-puro.
	 */
	public void leavePuroPuro() {
		removeAll();
		player.setTeleport(false);
	}
	
	public void removeAll() {
		player.closeInterfaces();
		player.interfaces().closeOverlay(false);
	}
	/**
	 * Starts catching a impling.
	 * @param player
	 * @param npc
	 */
	public void catchImplings(Player player, NPC npc) {
		switch (npc.getId()) {
		case BABY_IMPLING:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 16) {
			
			} else {
				player.sendMessage("You must have Hunter level of 17 atleast to catch a " + npc.getName().toLowerCase() + ".");
			}
			break;
		case YOUNG_IMPLING:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 21) {
				
			} else {
				player.sendMessage("You must have Hunter level of 22 atleast to catch a " + npc.getName().toLowerCase() + ".");
			}
			break;
		case GOURMET_IMPLING:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 27) {
				
			} else {
				player.sendMessage("You must have Hunter level of 28 atleast to catch a " + npc.getName().toLowerCase() + ".");
			}
			break;
		case EARTH_IMPLING:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 35) {
				
			} else {
				player.sendMessage("You must have Hunter level of 36 atleast to catch a " + npc.getName().toLowerCase() + ".");
			}
			break;
		case ESSENCE_IMPLING:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 41) {
				
			} else {
				player.sendMessage("You must have Hunter level of 42 atleast to catch a " + npc.getName().toLowerCase() + ".");
			}
			break;
		case ECLECTIC_IMPLING:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 49) {
				
			} else {
				player.sendMessage("You must have Hunter level of 50 atleast to catch a " + npc.getName().toLowerCase() + ".");
			}
			break;
		case NATURE_IMPLING:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 57) {
				
			} else {
				player.sendMessage("You must have Hunter level of 58 atleast to catch a " + npc.getName().toLowerCase() + ".");
			}
			break;
		case MAGPIE_IMPLING:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 64) {
				
			} else {
				player.sendMessage("You must have Hunter level of 65 atleast to catch a " + npc.getName().toLowerCase() + ".");
			}
			break;
		}
	}
	/**
	 * Adds the overlay for player, shows also how many imps are catched.
	 */
	public void updateEffects() {
	player.interfaces().sendOverlay(PURO_OVERLAY, false);
	player.packets().sendIComponentText(PURO_OVERLAY, 3, ""+getCountedImps()[1]+"");
	player.packets().sendIComponentText(PURO_OVERLAY, 5, ""+getCountedImps()[2]+"");
	player.packets().sendIComponentText(PURO_OVERLAY, 7, ""+getCountedImps()[3]+"");
	player.packets().sendIComponentText(PURO_OVERLAY, 9, ""+getCountedImps()[4]+"");
	player.packets().sendIComponentText(PURO_OVERLAY, 11, ""+getCountedImps()[5]+"");
	player.packets().sendIComponentText(PURO_OVERLAY, 13, ""+getCountedImps()[6]+"");
	player.packets().sendIComponentText(PURO_OVERLAY, 15, ""+getCountedImps()[7]+"");
	player.packets().sendIComponentText(PURO_OVERLAY, 17, ""+getCountedImps()[8]+"");
	player.packets().sendIComponentText(PURO_OVERLAY, 19, ""+getCountedImps()[9]+"");
	player.packets().sendIComponentText(PURO_OVERLAY, 21, ""+getCountedImps()[10]+"");
	}
	/**
	 * Does the player have the requirements?
	 * @return
	 */
	private boolean containsRequirements() {
		return false;
	}
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int[] getCountedImps() {
		updateEffects();
		return countedImps;
	}

	public void setCountedImps(int[] countedImps) {
		this.countedImps = countedImps;
	}
}
