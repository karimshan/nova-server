package org.nova.game.player.content.handlers;

import org.nova.game.player.Player;
import org.nova.game.player.Skills;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class AgilityRewardsHandler {

	private transient Player player;
	
	public AgilityRewardsHandler(Player player) {
	this.player = player;
	}
	
	public void openShop(Player player) {
		player.packets().sendIComponentText(6, 43, "Raptor's Agility Exchange)");
		//Agile's Set
		player.packets().sendIComponentText(6, 7, "Agile set (135)");
		player.packets().sendItemOnIComponent(6, 8, 14936, 1);
		//Gas mask 1506
		player.packets().sendItemOnIComponent(6, 11, 1506, 1);
		player.packets().sendIComponentText(6, 10, "Gas mask (125)");

		player.packets().sendItemOnIComponent(6, 10, 1506, 1);
		player.packets().sendIComponentText(6, 7, "Gas mask (125)");
		//11282 - Beret Mask
		player.packets().sendItemOnIComponent(6, 5, 11282, 1);
		player.packets().sendIComponentText(6, 9, "Beret & Mask (70)");
		
		player.interfaces().sendInterface(6);
	}
	
	/**
	 * Here we handle the exchanging session.
	 * @return
	 */
	public void handleButtons(Player player, int componentId) {
		
	switch (componentId) {
	case 24:
	if (player.agilityPoints >= 1) {
		if (!(player.agilityPoints >= 1)) {
			player.sendMessage("You don't have enough points to buy this.");
			player.closeInterfaces();
			return;
		}
		player.agilityPoints -= 1;
		player.getSkills().addXp(Skills.AGILITY, 5);
		player.sendMessage("Your purchase has been completed, current points: "+player.agilityPoints+".");
	}
		break;
	case 25:
		if (!(player.agilityPoints >= 9)) {
			player.sendMessage("You don't have enough points to buy this.");
			player.closeInterfaces();
			return;
		}
	if (player.agilityPoints >= 9) {
		player.agilityPoints -= 10;
		player.getSkills().addXp(Skills.AGILITY, 30);
		player.sendMessage("Your purchase has been completed, current points: "+player.agilityPoints+".");
	}
		break;
	case 26:

		if (!(player.agilityPoints >= 24)) {
			player.sendMessage("You don't have enough points to buy this.");
			player.closeInterfaces();
			return;
		}
	if (player.agilityPoints >= 24) {
		player.agilityPoints -= 25;
		player.getSkills().addXp(Skills.AGILITY, 75);
		player.sendMessage("Your purchase has been completed, current points: "+player.agilityPoints+".");
	}
		break;
	case 27:
	if (player.agilityPoints >= 99) {
		if (!(player.agilityPoints >= 99)) {
			player.sendMessage("You don't have enough points to buy this.");
			player.closeInterfaces();
			return;
		}
		player.agilityPoints -= 100;
		player.getSkills().addXp(Skills.AGILITY, 430);
		player.sendMessage("Your purchase has been completed, current points: "+player.agilityPoints+".");
	}
		break;
	case 28:
	if (player.agilityPoints >= 999) {
		if (!(player.agilityPoints >= 999)) {
			player.sendMessage("You don't have enough points to buy this.");
			player.closeInterfaces();
			return;
		}
		player.agilityPoints -= 1000;
		player.getSkills().addXp(Skills.AGILITY, 4300);
		player.sendMessage("Your purchase has been completed, current points: "+player.agilityPoints+".");
	}
		break;
		//Item Exchange
		//Agile's set
	case 8:
		if (!(player.agilityPoints >= 69)) {
			player.sendMessage("You don't have enough points to buy this.");
			player.closeInterfaces();
			return;
		}
		if (!(player.agilityPoints >= 69)) {
			player.sendMessage("You don't have enough points to buy this.");
			player.closeInterfaces();
			return;
		}
	if (player.agilityPoints >= 134) {
		player.agilityPoints -= 135;
		player.sendMessage("Your purchase has been completed, current points: "+player.agilityPoints+".");
		if (!player.getInventory().hasFreeSlots()) {
			player.getBank().addItem(14936, 1, true);
			player.getBank().addItem(14938, 1, true);
			player.sendMessage("Mentios has detected that you did not have space, reward has been added to your bank account.");
		} else {
		player.getInventory().addItem(14936,1);
		player.getInventory().addItem(14938,1);
	}
	}
		break;
		//Gas mask
	case 10:
		if (!(player.agilityPoints >= 124)) {
			player.sendMessage("You don't have enough points to buy this.");
			player.closeInterfaces();
			return;
		}
	if (player.agilityPoints >= 124) {
		player.agilityPoints -= 125;
		player.sendMessage("Your purchase has been completed, current points: "+player.agilityPoints+".");
		if (!player.getInventory().hasFreeSlots()) {
			player.getBank().addItem(1506,1,true);
			player.sendMessage("Mentios has detected that you did not have space, reward has been added to your bank account.");
		} else {
		player.getInventory().addItem(1506,1);
	}
}
		break;
	
	case 9:
		if (!(player.agilityPoints >= 69)) {
			player.sendMessage("You don't have enough points to buy this.");
			player.closeInterfaces();
			return;
		}
	if (player.agilityPoints >= 69) {
		player.agilityPoints -= 70;
		player.sendMessage("Your purchase has been completed, current points: "+player.agilityPoints+".");
		if (!player.getInventory().hasFreeSlots()) {
			player.getBank().addItem(11282,1,true);
			player.sendMessage("Mentios has detected that you did not have space, reward has been added to your bank account.");
		} else {
		player.getInventory().addItem(11282,1);
	}
}
		break;
	
	}
}
}
