package org.nova.kshan.dialogues.impl;

import java.util.Arrays;

import org.nova.cache.definition.NPCDefinition;
import org.nova.kshan.content.skills.slayer.SlayerTask;
import org.nova.kshan.content.skills.slayer.SlayerTask.Master;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class SlayerMaster extends Dialogue {

	private int npcId;
	private String name;
	
	@Override
	public void start() {
		npcId = (Integer) data[0];
		name = NPCDefinition.get(npcId).name;
		sendNPCDialogue(npcId, name, false, "Hello brave warrior, What can I do for you today?" );
	}

	@Override
	public void process(int interfaceId, int button) {
		if (stage == 0) {
			sendOptions(TITLE, new String[] { 
				"I'm looking for a slayer task.", "I would like to view the slayer shop.", 
					"I would like to reset my Slayer Task." });
			stage = 1;
		} else if (stage == 1) {
			if (button == OPTION1_OTHER) {
				sendPlayerDialogue(false,
					"Hey there, "+name+".", "I'm looking for a slayer task." );
				stage = 2;
			} else if (button == OPTION2_OTHER) {
				sendPlayerDialogue(false,
					"Hey there, "+name+".", "I would like to view the slayer points shop." );
				stage = 3;
			} else if(button == OPTION3_OTHER) {
				sendPlayerDialogue(false,
					"Hey there, "+name+".", "I would like to reset my Slayer Task." );
				stage = 4;
			}
		} else if (stage == 2) {
			String[] task = null;
			if(player.slayerTask().completed()) {
				player.slayerTask(SlayerTask.assignRandomTask(player, Master.forId(npcId)));
				task = new String[] { "Your task is to kill " + player.slayerTask().taskTotal() + " " + 
					player.slayerTask().monsterName().toLowerCase() + "s." }; 
			} else {
				task = new String[] { "You already have a Slayer Task: " + 
					player.slayerTask().monsterName() + "s.", "You have "+player.slayerTask().amountLeft()+" left to kill." };
			}
			player.sm(""+Arrays.toString(task).replace("[", "").replace("]", "").replace(".,", "."));
			sendNPCDialogue(npcId, name, false, task);
			stage = 10;
		} else if (stage == 3) {
			player.interfaces().closeChatBoxInterface();
			player.interfaces().sendInterface(164);
			player.packets().sendIComponentText(164, 20, "" + player.slayerPoints() + "");
			stage = 10;
		} else if(stage == 4) {
			if(player.slayerTask().completed()) {
				sendNPCDialogue(npcId, name, false, 
					"You currently don't have a slayer task.", "Would you like one?" );
				stage = 9;
			} else {
				sendNPCDialogue(npcId, name, false, 
					"Certainly, but at a price of 250,000 coins.", "Are you sure you want to do that?" );
				stage = 5;
			}
		} else if(stage == 5) {
			sendOptions("Pay 250K to reset your task?", new String[] { 
				"Yes, pay 250K to reset my task!", "No thanks!" });
			stage = 6;
		} else if(stage == 6) {
			if(button == OPTION1) {
				boolean hasMoney = player.getInventory().containsItem(995, 250000);
				if(hasMoney) {
					player.getInventory().deleteItem(995, 250000);
					sendLines(false, new String[] { 
						"You paid "+player.slayerTask().currentMaster()+" to reset your current task." });
					player.slayerTask().amountKilled(player.slayerTask().taskTotal());
					player.slayerTask().completed(true);
					stage = 8;
				} else {
					sendLines(false,
						"You don't have enough coins to pay "+player.slayerTask().currentMaster()+".");
					stage = 10;
				}
			} else if(button == OPTION2) {
				sendNPCDialogue(npcId, name, false, 
					"Come talk to me again if you would like a slayer task." );
				stage = 10;
			}
		} else if(stage == 8) {
			sendNPCDialogue(npcId, name, false, 
				"Let me know if you need anything else, "+player.getDisplayName()+"!",
				"Have a nice day." );
			stage = 10;
		} else if(stage == 9) {
			sendOptions("Do you want a slayer task?", new String[] { "Yes, please.", "No thanks." });
			stage = 11;
		} else if(stage == 11) {
			if(button == OPTION1) {
				sendPlayerDialogue(false, "Yes, please." );
				stage = 2;
			} else {
				sendNPCDialogue(npcId, name, false, 
					"Come talk to me again if you would like a slayer task." );
				stage = 10;
			}
		}
		else
			end();
	
	}
	@Override
	public void finish() {

	}
}