package org.nova.kshan.dialogues.impl;

import org.nova.game.map.Location;
import org.nova.game.player.content.Magic;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class CityTeleports extends Dialogue {
	
	@Override
	public void start() {
		sendOptions("Where do you want to go?", new String[] { 
				"Lumbridge", "Edgeville", "Falador", "Varrock", "Ardougne" });
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		if(interfaceId == FIVE_OPTIONS) {
			if(buttonId == 1) {
				end();
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3222, 3222, 0));
			} else if(buttonId == 2) {
				end();
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3093, 3493, 0));
			} else if(buttonId == 3) {
				end();
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2964, 3379, 0));
			} else if(buttonId == 4) {
				end();
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3212, 3422, 0));
			} else if(buttonId == 5) {
				end();
				Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2665, 3306, 0));
			} else
				end();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
