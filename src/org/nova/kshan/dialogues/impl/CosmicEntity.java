package org.nova.kshan.dialogues.impl;

import org.nova.game.npc.NPC;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class CosmicEntity extends Dialogue {

	NPC npc;
	
	@Override
	public void start() {
		npc = (NPC) data[0];
		sendNPCDialogue(npc.getId(), npc.getName(), false, "Hey there, "+player.getDisplayName()+"!", 
				"Here is a list of available shops you can buy from!");
	}

	@Override
	public void process(int inter, int b) {
		if(stage == 0) {
			end();
			player.interfaces().sendInterface(496);
			for (int i = 4; i <= 14; i++)
				player.packets().sendString("", 496, i);
			player.packets().sendString("Nova Shops", 496, 3);
			player.packets().sendString("Melee shop", 496, 4);
			player.packets().sendString("Magic shop", 496, 5);
			player.packets().sendString("Ranged shop", 496, 6);
			player.packets().sendString("Skilling supplies", 496, 7);
			player.packets().sendString("Food", 496, 8);
			player.packets().sendString("Potions", 496, 9);
			player.packets().sendString("Summoning", 496, 10);
			player.packets().sendString("", 496, 11);
			player.packets().sendString("", 496, 12);
			player.packets().sendString("", 496, 13);
			player.packets().sendItemOnIComponent(496, 1, 22441, 1);
			player.packets().sendItemOnIComponent(496, 2, 22442, 1);
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
