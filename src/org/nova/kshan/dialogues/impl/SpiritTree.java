package org.nova.kshan.dialogues.impl;

import org.nova.Constants;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.PlayerLook;
import org.nova.kshan.content.PlayTime;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class SpiritTree extends Dialogue {
	
	NPC npc;

	@Override
	public void start() {
		npc = (NPC) data[0];
		sendEntityDialogue(NPC, 9827, npc.getId(), npc.getName(), false, 
			"Good day, "+player.getDisplayName()+"!", "I am your source of information on "+Constants.SERVER_NAME+"!", 
				"Tell me, what would you like to do?");
	}

	@Override
	public void process(int interfaceId, int b) {
		if(stage == 0) {
			sendOptions("What would you like to do?", "Player settings", "Teleports", "Help");
			stage = 1;
		} else if(stage == 1) {
			if(b == 2) {
				sendOptions("Player settings", "Change XP Rate", "View current slayer task", 
						"View time played", "Change Player Look", "More coming soon...");
				stage = 2;
			} else if(b == 3) {
				sendOptions("What would you like to view?", 
						"Training teleports", "Telport to a slayer master", "Bosses", "Skilling Teleports", "City teleports");
				stage = 3;
			} else {
				sendEntityDialogue(NPC, 9827, npc.getId(), npc.getName(), false, 
					"Currently, I cannot assist you as I don't have that function.", 
						"However, please ask a staff member for help and", "i'm sure they'd be more than willing to help you!");
				stage = 10;
			}
		} else if(stage == 2) {
			if(b == 1) {
				end();
				player.getDialogue().start("ChooseGameMode");
			} else if(b == 2) {
				end();
				if(player.slayerTask().amountLeft() > 0)
					player.sm("Your current slayer task is: "+player.slayerTask().monsterName()+". Only "+player.slayerTask().amountLeft()+" left to kill!");
				else
					player.sm("You currently don't have a slayer task. Speak to "+player.slayerTask().currentMaster()+" in "+player.slayerTask().currentMasterLocation()+" for a task.");
			} else if(b == 3) {
				end();
				player.sm("Total Play Time (Days:Hours:Minutes:Seconds): "+player.playTime().getPlayTime(PlayTime.DAYS)+":"+player.playTime().getPlayTime(PlayTime.HOURS)+""
					+ ":"+player.playTime().getPlayTime(PlayTime.MINUTES)+":"
						+ ""+player.playTime().getPlayTime(PlayTime.SECONDS)+"");
			} else if(b == 4) {
				end();
				if(player.getEquipment().wearingArmour())
					player.sm("Please unequip your armour for a better view of what you're editing.");
				else
					PlayerLook.openMageMakeOver(player);
			} else {
				end();
				player.sm("More player settings will be added soon, please be patient!");
			}
		} else if(stage == 3) {
			if(b == 1) {
				end();
				player.getDialogue().start("TrainingTeleports");
			} else if(b == 2) {
				end();
				player.sm("Teleporting to Spria, who is located in Taverly.");
				Magic.specialTele(player, new Location(2890, 3431, 0), true);
			} else if(b == 3) {
				end();
				player.getDialogue().start("BossTeleports");
			} else if(b == 4) {
				end();
				player.getDialogue().start("SkillingTeleports");
			} else {
				end();
				player.getDialogue().start("CityTeleports");
			}
		} else
			end();
	}

	@Override
	public void finish() {
		
	}

}
