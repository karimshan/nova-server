package org.nova.game.player.content.itemactions;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.game.player.controlers.Duelarena;
import org.nova.utility.misc.Misc;


public class Flower {
	
	private final static int[] FLOWERS = {2980, 2986, 2987, 2988, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2982, 2982, 2982, 2982, 2982, 2982, 2982, 2982,
	2983, 2983, 2983, 2983, 2983, 2983, 2983, 2983, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2982, 2982, 2982, 2982, 2982, 2982, 2982, 2982,
	2983, 2983, 2983, 2983, 2983, 2983, 2983, 2983, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2985  };
	
	public static void PlantFlower(Player player) {
		final Location tile = new Location(player);
		 GlobalObject Flower4 = new GlobalObject(FLOWERS[Misc.random(FLOWERS.length)], 10, 0, player);
		if (!Game.canMoveNPC(player.getZ(), player.getX(), player.getY(),
				1)
				|| Game.getRegion(player.getRegionId()).getSpawnedObject(
						player) != null
						|| player.getControllerManager().getControler() instanceof Duelarena) { 
			player.packets().sendMessage("You can't plant flowers here.");
		} else {
	if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
		if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
			if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
				player.addWalkSteps(player.getX(), player.getY() - 1, 1);
	player.getInventory().deleteItem(299, 1);
	Game.spawnObject(Flower4, false);
	player.getMatrixDialogues().startDialogue("FlowerGame");
	player.setNextAnimation(new Animation(827));
		}
	}
}