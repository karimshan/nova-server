package org.nova.game.player.controlers;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

public class CrabControler extends Controller {
	
	private static CrabControler singleton = new CrabControler();
	
	@Override
	public void start() {
		if (inCrabArea(player))
		displayInterfaces(player, true);
	}
	public void handleShopButtonInteract(Player player, int buttonId) {
		
	}
	
	public static final boolean inCrabArea(Player player) {
		int destX = player.getX();
		int destY = player.getY();
		return  (destX >= 2300 && destX <=2737 && destY >= 3600 && destY <= 3900);
	}
	public void closeInterface() {
		
	}
	@Override
	public void forceClose() {
		if (player.interfaces().containsInterface(player.interfaces().isFullScreen() ? 10 : 19, 837))
		   player.packets().closeInterface(player.interfaces().isFullScreen() ? 10 : 19);
	}
	public void displayInterfaces(final Player player, boolean sendPoints) {
		player.packets().sendIComponentText(837, 2, "");	
		player.packets().sendIComponentText(837, 3, "");	
		player.packets().sendIComponentText(837, 4, "");	
		player.packets().sendIComponentText(837, 5, "");	
		player.packets().sendIComponentText(837, 6, "");	
		player.packets().sendIComponentText(837, 7, "");	
		player.packets().sendIComponentText(837, 8, "");	
		player.packets().sendIComponentText(837, 9, "");	
		
		player.packets().sendIComponentText(837, 8, "<col=#FFFFFF>Crab points");
			player.packets().sendIComponentText(837, 2, "<col=#FF8370>"+player.getCrabPoints());
			 player.interfaces().sendTab(player.interfaces().isFullScreen() ? 10 : 19, 837);

				player.packets().sendIComponentText(837, 9, "<col=#FFFFFF>EXP Boost unactive.");
				
				WorldTasksManager.schedule(new WorldTask() {
					boolean active;
					int loop;
					
					@Override
					public void run() {
					switch (loop) {
					case 0:
						if (active) {
						stop();
						return;
						}
						active = true;
						player.packets().sendIComponentText(837, 4, "");
						player.packets().sendIComponentText(837, 5, "");
						player.packets().sendIComponentText(837, 4, "Updating");	
						player.packets().sendIComponentText(837, 5, "points...");	
						break;
					case 3:
						player.packets().sendIComponentText(837, 4, "");
						player.packets().sendIComponentText(837, 5, "");
						player.packets().sendIComponentText(837, 4, "Points");
						player.packets().sendIComponentText(837, 5, "updated!");
						player.setCrabPoints(player.getCrabPoints() + Misc.getRandom(10));
						player.packets().sendIComponentText(837, 2, "<col=#FF8370>"+player.getCrabPoints());
						break;
					case 4:
						player.packets().sendIComponentText(837, 4, "");
						player.packets().sendIComponentText(837, 5, "");
						break;

					}
						loop++;
						}
					}, 0, 1);
	}
	
	public void setXPBoost(Player players) {
		player.sendMessage("Your combat experiences are boosted up for one hour.");
		player.xpModifier = 150;
	}
	
	@Override
	public boolean logout() {
		return false;
	}
	
	@Override
	public void processNPCDeath(int id) {
		if (id == 1265)
		displayInterfaces(player, true);
	}
	
	public void openShop() {
		
	}
	
	@Override
	public boolean login() {
		start();
		return false;
	}

	public static CrabControler getSingleton() {
		return singleton;
	}

	public static void setSingleton(CrabControler singleton) {
		CrabControler.singleton = singleton;
	}
	
}
