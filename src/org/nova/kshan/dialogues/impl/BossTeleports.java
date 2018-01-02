package org.nova.kshan.dialogues.impl;

import org.nova.game.map.Location;
import org.nova.game.player.content.Magic;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class BossTeleports extends Dialogue {

	@Override
	public void start() {
		stage = 1;
		if (stage == 1) {
			sendOptions("Pvm Teleports", "Bandos", "Armadyl", "Saradomin",
					"Zamorak", "More Options...");
			stage = 1;
		}
	}

	public void process(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == OPTION1) {
				if (!player.canSpawn()) {
					player.packets()
							.sendMessage(
									"<col=ff0000>You can't teleport while you're in this area.</col>");
					end();
				} else {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Location(
							2864, 5354, 2));
					player.getControllerManager().startController("GodWars");
					end();
				}
			}
			if (componentId == OPTION2) {
				if (!player.canSpawn()) {
					player.packets()
							.sendMessage(
									"<col=ff0000>You can't teleport while you're in this area.</col>");
					end();
				} else {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Location(
							2839, 5296, 2));
					player.getControllerManager().startController("GodWars");
					end();
				}
			}
			if (componentId == OPTION3) {
				if (!player.canSpawn()) {
					player.packets()
							.sendMessage(
									"<col=ff0000>You can't teleport while you're in this area.</col>");
					end();
				} else {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Location(
							2907, 5265, 0));
					player.getControllerManager().startController("GodWars");
					end();
				}
			}
			if (componentId == OPTION4) {
				if (!player.canSpawn()) {
					player.packets()
							.sendMessage(
									"<col=ff0000>You can't teleport while you're in this area.</col>");
					end();
				} else {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Location(
							2924, 5330, 2));
					player.getControllerManager().startController("GodWars");
					end();
				}
			}
			if (componentId == OPTION5) {
				stage = 2;
				sendOptions("Pvm Teleports", "King Black Dragon",
						"Tormented Demons", "Nex",
						"Corporeal Beast", "More Options...");
			}
		} else if (stage == 2) {
			if (componentId == OPTION1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3067,
						10254, 0));
				player.packets()
						.sendMessage(
								"<col=ff0000>Make sure you equip an anti-dragonshield with you. You will need it!</col>");
				end();
			}
			if (componentId == OPTION2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2562,
						5739, 0));
				end();
			}
			if (componentId == OPTION3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2862,
						5219, 0));
				end();
			}
			if (componentId == OPTION4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2976,
						4384, 2));
				end();
			}
			if (componentId == OPTION5) {
				stage = 3;
				sendOptions("Pvm Teleports", "Dagganoth Kings",
						"Yk'lagor the Thunderous", "Blink", "Nomad(Quest)",
						"More Options...");
			}
		} else if (stage == 3) {
			if (componentId == OPTION1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2900,
						4449, 0));
				player.packets().sendMessage("Watch your feet.");
				end();
			}
			if (componentId == OPTION2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2844,
						9636, 0));
				player.packets()
						.sendMessage(
								"<col=ff0000>Jump over the obstacle to fight him!</col>");
				end();
			}
			if (componentId == OPTION3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2722,
						9513, 0));
				player.packets().sendMessage(
						"<col=ff0000>Walk east to fight this boss.</col>");
				end();
			}
			if (componentId == OPTION4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3081,
						3476, 0));
				player.packets()
						.sendMessage(
								"<col=ff0000>Enter the portal and investigate the tent inside.</col>");
				end();
			}
			if (componentId == OPTION5) {
				sendOptions("Pvm Teleports", "Avatar", "Wolverine",
						"Ice Troll King", "Mutant Tarn", "More Options...");
				stage = 4;
			}
		} else if (stage == 4) {
			if (componentId == OPTION1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(4568,
						5092, 0));
				end();
			}
			if (componentId == OPTION2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2693,
						9482, 0));
				end();
			}
			if (componentId == OPTION3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3047,
						9580, 0));
				end();
			}
			if (componentId == OPTION4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3209,
						5481, 0));
				end();
			}
			if (componentId == OPTION5) {
				sendOptions("Pvm Teleports", "Giant Mole", "Giant Roc",
						"Kraken", "Coming soon", "Back...");
				stage = 5;
			}
		} else if (stage == 5) {
			if (componentId == OPTION1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(1759,
						5198, 0));
				end();
			}
			if (componentId == OPTION2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3390,
						2975, 0));
				player.packets()
						.sendMessage(
								"<col=ff0000>Activate your ranged prayer once the bird shakes his wings.</col>");
				end();
			}
			if (componentId == OPTION3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2014,
						4825, 0));
				end();
			}
			if (componentId == OPTION4) {
				end();
			}
			if (componentId == OPTION5) {
				sendOptions("Pvm Teleports", "Bandos", "Armadyl", "Saradomin",
						"Zamorak", "More Options...");
				stage = 1;
			}
		}
	}

	@Override
	public void finish() {

	}

}