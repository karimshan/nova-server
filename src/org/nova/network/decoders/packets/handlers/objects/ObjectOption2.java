package org.nova.network.decoders.packets.handlers.objects;

import org.nova.cache.definition.ObjectDefinition;
import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.player.Player;
import org.nova.game.player.actions.CowMilking;
import org.nova.game.player.content.ArtisanWorkshop;
import org.nova.game.player.content.FairyRing;
import org.nova.game.player.content.Pickables;
import org.nova.game.player.content.ShootingStar;
import org.nova.game.player.content.Thieving;
import org.nova.game.player.content.itemactions.Beehive;
import org.nova.game.player.content.itemactions.Flax;

/**
 * 
 * @author Karimshan Nawaz
 *
 */
public class ObjectOption2 {
	
	/**
	 * 
	 * @param player
	 * @param object
	 */
	public static void process(final Player player, final GlobalObject object) {
		int id = object.getId();
		ObjectDefinition objectDef = object.defs();
		if (!player.getRandomEvent().hasObjectOption2(object))
			return;
		if (!player.getControllerManager().processObjectClick2(object))
			return;
		switch (id) {
			case 68:
				Beehive.take(player, object, true);
				break;
		}
		if (id == 36786 || id == 42378 || id == 42377 || id == 42217
				|| id == 27663)
			player.getBank().openBank();
		else if (object.defs().name.equalsIgnoreCase("furnace"))
			player.getMatrixDialogues().startDialogue("SmeltingD",
					object);
		if (Pickables.pick(player, object))
			return;
		if (id == 24823)
			ArtisanWorkshop.TakeSteelIngots(player);
		if (objectDef.name.equals("Dairy cow"))
			player.getActionManager().setAction(new CowMilking());
		if (id == 24822)
			ArtisanWorkshop.TakeIronIngots(player);
		if (id == 2646)
			Flax.pickFlax(player, object);
		if (objectDef.name.toLowerCase().contains("bank booth")
				|| objectDef.name.toLowerCase().contains("bank")) {
			player.closeInterfaces();
			player.interfaces().closeChatBoxInterface();
			player.getBank().openBank();
		} else if (object.defs().name.equalsIgnoreCase("Crashed star"))
			player.packets().sendMessage(
					"The current size of the star is "
							+ (ShootingStar.stage) + ".");
		if (objectDef.name.contains("fairy ring")) {
			FairyRing.refresh(player);
			FairyRing.openFairyRing(player);
		}
		if (id == 29394)
			ArtisanWorkshop.DepositIngots(player);
		if (id == 29395)
			ArtisanWorkshop.DepositIngots(player);
		if (id == 61)
			player.getMatrixDialogues().startDialogue("LunarAltar");
		else if (id == 62677)
			player.getDominionTower().openRewards();
		else if (id == 6)
			player.getDwarfcannon().pickUpDwarfCannon(0, object);
		else if (id == 29358)
			player.getDwarfcannon().pickUpDwarfCannon(0, object);
		else if (id == 29403)
			player.getDwarfcannon().pickUpDwarfCannon(0, object);
		else if (id == 62688)
			player.getMatrixDialogues().startDialogue(
				"SimpleMessage",
					"You have a Dominion Factor of "
						+ player.getDominionTower()
							.getDominionFactor() + ".");
		else if (id == 34384 || id == 34383 || id == 14011
				|| id == 7053 || id == 34387 || id == 34386
				|| id == 34385)
			Thieving.handleStalls(player, object);
		
		switch (objectDef.name.toLowerCase()) {
			case "bank deposit box":
				if (objectDef.containsOption(0, "Deposit"))
					player.getBank().openDepositBox();
				break;
			case "gate":
			case "metal door":
				if(object.getType() == 0
					&& objectDef.containsOption(1, "Open"))
					Passages.passGate(player, object);
				break;
			case "door":
				if (id == 21507) {
					Game.removeObject(object, true);
					return;
				}
				if (object.getType() == 0
						&& objectDef.containsOption(1, "Open"))
					Passages.passDoor(player, object);
				break;
			case "bank":
			case "bank chest":
			case "bank booth":
			case "counter":
				if (objectDef.containsOption(1, "Bank"))
					player.getBank().openBank();
				break;
			case "ladder":
				if (id == 21512)
					return;
				Passages.climbLadder(player, object, 2);
				break;
			case "staircase":
				Passages.navigateStaircase(player, object, 2);
				break;
			default:
				break;
		}
	}	
}
