package org.nova.kshan.dialogues.impl;

import org.nova.game.npc.NPC;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class SurgeonGeneralTafani extends Dialogue {
	
	NPC npc;
	
	public void start() {
		npc = (NPC) data[0];
		sendNPCDialogue(npc.getId(), npc.getName(), false, "Hello there, would you like to get healed?");
	}
	
	public void process(int i, int b) {
		if(stage == 0) {
			sendOptions("Get healed?", "Yes, please heal me!", "No thanks.");
			stage = 1;
		} else if(stage == 1) {
			end();
			if(b == 1) {
				if(player.getHitpoints() == player.getMaxHitpoints())
					player.sm("You are not in need of healing");
				else {
					if(player.getPoison().isPoisoned()) {
						player.sm("The surgeon heals you and cures your poison.");
						player.getPoison().reset();
						player.heal(player.getMaxHitpoints());
					} else {
						player.sm("The surgeon heals you to a healthy state.");
						player.heal(player.getMaxHitpoints());
					}
				}
			}
		}
	}
	
	public void finish() { 
		
	}

}
