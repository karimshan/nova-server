package org.nova.kshan.dialogues.impl;

import org.nova.game.map.Location;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.TeleportManager;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class SkillingTeleports extends Dialogue {
	
	@Override
	public void start() {
		stage = 1;
		if (stage == 1) {
		    sendOptions("Skilling Teleports", "Fishing", "Mining", "Agility", "Woodcutting", "More Options...");
			stage = 1; 
		}
	}
	
	public void process(int interfaceId, int componentId) {
		if (stage == 1) {
	    if(componentId == OPTION1) {
            Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2599, 3421, 0));
			end();
		}
		if(componentId == OPTION2) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3299, 3312, 0));
			end();
		}
        	if(componentId == OPTION3) {
			sendOptions("Agility Teleports", "Gnome Agility", "Barbarian Outpost", "Wilderness Agility");
			stage = 3;
		}
		if(componentId == OPTION4) {
		    	sendOptions("Woodcutting Teleports", "Woodcutting", "Ivy's");
			stage = 8;
		}
		if(componentId == OPTION5) {
		    stage = 2;
		    sendOptions("Skilling Teleports", "Runecrafting", "Summoning", "Hunter", "Dungeoneering", "Back...");
		}
		} else if (stage == 2) {
		if(componentId == OPTION1) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3040, 4842, 0));
			end();
		}
		if(componentId == OPTION2) {
			TeleportManager.SpiritTeleport(player, 0, 0, new Location(2207, 5346, 0));	
			end();
		}
		if(componentId == OPTION4) {
		    player.getControllerManager().startController("ZombieMinigame", 1);
			end();
		}
		if(componentId == OPTION3) {
		    sendOptions("Hunter Teleports", "Normal Hunting", "Falconry Hunting");
			stage = 4;
		}
		if(componentId == OPTION5) {
		    stage = 1; 
		    sendOptions("Skilling Teleports", "Fishing", "Mining", "Agility", "Woodcutting", "More Options...");
		}
		} else if (stage == 3) {
		if(componentId == OPTION1) {
            Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2470, 3436, 0));
			end();
		}
		if(componentId == OPTION2) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2552, 3563, 0));
			end();
		}
		if(componentId == OPTION3) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2998, 3916, 0));
			end();
		}
		} else if (stage == 4) {
		if(componentId == OPTION1) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2526, 2916, 0));
			end();
		}
		if(componentId == OPTION2) {
		if(player.getEquipment().wearingArmour()) {
			end();
		player.packets().sendMessage("You can't do this while wearing armour");
			}else{
		player.getControllerManager().startController("Falconry", 1);
			end();
			}
			}
		} else if (stage == 8) {
		if(componentId == OPTION1) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3498, 3620, 0));
			end();
		}
		if(componentId == OPTION2) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3218, 3500, 0));
			end();
		}
	  }
	}

	@Override
	public void finish() {

	}

}