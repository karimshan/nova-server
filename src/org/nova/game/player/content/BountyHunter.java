package org.nova.game.player.content;

import java.util.ArrayList;
import java.util.TimerTask;

import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.player.Player;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;

public class BountyHunter {
	
	/**
	 * Gets the bounty hunter singleton.
	 */
	private static BountyHunter bountyHunter = new BountyHunter();

	private ArrayList<Player> targets;
	
	private boolean targeted;
	
	/**
	 * Adds a character to the bounty hunter.
	 * @param player
	 */
	public void addBountyHunterPlayer(Player player) {
		targets.add(player);
		updateInterface(player);
	}
	
	public void calculateTargets(Player player, Player target) {
	if (!isTargeted()) 
		targets.set(Misc.getRandom(targets.size()), target);
		targets.set(Misc.getRandom(targets.size()), player);
		player.packets().sendIComponentText(506, -1, target.getDisplayName());
		player.packets().sendIComponentText(506, -1, player.getDisplayName());
		targets.remove(targets.indexOf(target));
		targets.remove(targets.indexOf(player));
		updateInterface(player);
	}
	
	/**
	 * Leaves from the bounty hunter.
	 * @param player
	 */
	public void leaveBountyHunter(Player player) {
		targets.remove(player);
	}
	
	public static final void startTargetFindingTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : Game.getPlayers()) {
						if (player == null)
							continue;
						//player.restoreRunEnergy();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 1000);
	}
	
	public void updateInterface(Player player) {
		
	}
	
	public boolean inBountyHunter(Player player){
		return false;
	}

	public static BountyHunter getBountyHunter() {
		return bountyHunter;
	}

	public boolean isTargeted() {
		return targeted;
	}

	public void setTargeted(boolean targeted) {
		this.targeted = targeted;
	}
}