package org.nova.kshan.dialogues.impl;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.kshan.content.skills.slayer.SlayerTask;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class StarterGuide extends Dialogue {
	
	NPC npc;
	
	@Override
	public void start() {
		npc = (NPC) data[0];
		sendNPCDialogue(npc.getId(), npc.getName(), false, 
			"Hello there, "+player.getDisplayName()+"!", "Welcome to Nova! I am your guide for today,", "so let's get started!");
		player.hints().removeAll();
		player.hints().addHintIcon(0, 0, 1, 1, 1, 1, false, false);
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		if(stage == 0) {
			sendPlayerDialogue(false, "Oh great, another guide...");
			stage = 1;
		} else if(stage == 1) {
			sendNPCDialogue(npc.getId(), npc.getName(), false, 
				"It's okay, I will not take much of your", "time. I will be short, sweet and to the point!", 
					"I will show you a few things here at home", "that will help in your adventure!");
			stage = 2;
		} else if(stage == 2) {
			player.interfaces().closeChatBoxInterface();
			player.setCantMove(true);
			Game.submit(new GameTick(1.0) {
				int ticks;
				public void run() {
					ticks++;
					switch(ticks) {
						case 1:
							player.camPos(5504, 4452, 1000, 13);
							player.camLook(5504, 4455, 1000, 12);
							sendNPCDialogue(npc.getId(), npc.getName(), true, 
								"Over here is the ancient altar where you can", "switch between normal, ancient, and lunar magicks!");
							break;
						case 6:
							player.camPos(5498, 4455, 1000, 13);
							player.camLook(5497, 4458, 1000, 12);
							sendNPCDialogue(npc.getId(), npc.getName(), true, 
								"This is the marvelous prayer altar, which", 
								"can help replenish your prayer after", "you've lost all your prayer points.");
							break;
						case 11:
							player.camPos(5510, 4457, 2000, 12);
							player.camLook(5510, 4459, 0, 12);
							sendNPCDialogue(npc.getId(), npc.getName(), true, 
								"To the right of the fountain is the zaros altar", "which lets you switch from normal prayers",
								"to curses. This can be particularily useful in", "fighting bosses or other players.");
							break;
						case 17:
							player.camPos(5519, 4453, 2000, 12);
							player.camLook(5519, 4457, 1500, 12);
							sendNPCDialogue(npc.getId(), npc.getName(), true, 
								"Over to the far right, you'll see the spectacular", 
								"Spirit tree which can help you move around Nova", 
								"and adjust the settings of your character to your", "liking!");
							break;
						case 19:
							for(NPC n : Game.getLocalNPCs(player))
								if(n.getName().equalsIgnoreCase("spirit tree"))
									n.forceTalk("Need to get somewhere? Come see me for teleports!");
							break;
						case 23:
							sendNPCDialogue(npc.getId(), npc.getName(), true, 
								"Over here is surgeon general tafani.", "You can go talk to her if you are in need of healing.");
							for(NPC n : Game.getLocalNPCs(player))
								if(n.getName().equalsIgnoreCase("surgeon general tafani")) {
									n.faceLocation(new Location(n.getX(), n.getY() - 1, player.getZ()));
									player.packets().sendCameraPos(Game.getCutsceneX(player, n.getX()), 
											Game.getCutsceneY(player, n.getY() - 3), 1200, 13, 13);
									player.packets().sendCameraLook(Game.getCutsceneX(player, n.getX()), 
											Game.getCutsceneY(player, n.getY() + 2), 0, 12, 12);
								}
							break;
						case 25:
							for(NPC n : Game.getLocalNPCs(player))
								if(n.getName().equalsIgnoreCase("surgeon general tafani")) {
									n.animate(863);
									n.forceTalk("Hey there! Come talk to me if you need to be healed!");
								}
							break;
						case 28:
							sendNPCDialogue(npc.getId(), npc.getName(), true, 
								"Behind the brilliant Tafani is the shopkeeper.", 
								"He's a big help, because he will buy all those",
								"great loots for some great cash!");
							for(NPC n : Game.getLocalNPCs(player))
								if(n.getName().equalsIgnoreCase("shopkeeper")) {
									n.faceLocation(new Location(n.getX(), n.getY() - 1, player.getZ()));
									player.packets().sendCameraPos(Game.getCutsceneX(player, n.getX()), 
											Game.getCutsceneY(player, n.getY() - 3), 1200, 13, 13);
									player.packets().sendCameraLook(Game.getCutsceneX(player, n.getX()), 
											Game.getCutsceneY(player, n.getY() + 2), 0, 12, 12);
								}
							break;
						case 31:
							for(NPC n : Game.getLocalNPCs(player))
								if(n.getName().equalsIgnoreCase("shopkeeper")) {
									n.animate(863);
									n.forceTalk("If you need to sell your loots, come talk to me!");
								}
							break;
						case 34:
							sendNPCDialogue(npc.getId(), npc.getName(), true, 
								"To the right of the fountain is the banker.", "Your items are safe with him.");
							for(NPC n : Game.getLocalNPCs(player))
								if(n.getName().equalsIgnoreCase("banker")) {
									n.faceLocation(new Location(n.getX(), n.getY() - 1, player.getZ()));
									player.packets().sendCameraPos(Game.getCutsceneX(player, n.getX()), 
											Game.getCutsceneY(player, n.getY() - 3), 1200, 13, 13);
									player.packets().sendCameraLook(Game.getCutsceneX(player, n.getX()), 
											Game.getCutsceneY(player, n.getY() + 2), 0, 12, 12);
								}
							break;
						case 36:
							for(NPC n : Game.getLocalNPCs(player))
								if(n.getName().equalsIgnoreCase("banker")) {
									n.animate(863);
									n.forceTalk("Ran out of space in your backpack? Come talk to me and i'll help you!");
								}
							break;
						case 39:
							sendNPCDialogue(npc.getId(), npc.getName(), true, 
								"Finally, in front of the banker is fairy nuff.", "She is your source for purchasing",
								"items needed in skilling, and combat!");
							for(NPC n : Game.getLocalNPCs(player))
								if(n.getId() == 4431) {
									n.faceLocation(new Location(n.getX(), n.getY() - 1, player.getZ()));
									player.packets().sendCameraPos(Game.getCutsceneX(player, n.getX()), 
											Game.getCutsceneY(player, n.getY() - 3), 1200, 13, 13);
									player.packets().sendCameraLook(Game.getCutsceneX(player, n.getX()), 
											Game.getCutsceneY(player, n.getY() + 2), 0, 12, 12);
								}
							break;
						case 41:
							for(NPC n : Game.getLocalNPCs(player))
								if(n.getId() == 4431) {
									n.graphics(2793);
									n.forceTalk("Need to buy an item? I'm your girl!");
								}
							break;
						case 44:
							stop();
							player.packets().sendResetCamera();
							sendNPCDialogue(npc.getId(), npc.getName(), false, 
								"See? I told you that wouldn't take long!");
							stage = 3;
							break;
					}
				}
			});
		} else if(stage == 3) {
			sendPlayerDialogue(false, "That was pretty helpful!", "Hey, I had another question. What if",
				"i'm in need of help again?");
			stage = 4;
		} else if(stage == 4) {
			sendNPCDialogue(npc.getId(), npc.getName(), false, 
				"If you ever need help again, you can come talk", "to me or click on the '?' button next to", 
				"the logout button.", "I hope you enjoy your time here at Nova!");
			stage = 5;
		} else if(stage == 5) {
			sendNPCDialogue(npc.getId(), npc.getName(), false, 
				"Oh! Before I forget, I am going to give you", 
					"a small reward for being patient with me", "and finishing the whole tutorial. Enjoy!" );
			stage = 6;
		} else if(stage == 6) {
			sendItemDialogue(new Item(22443, 1), "The guide gives you a cape of introduction for finishing the tutorial!");
			stage = 7;
		} else if(stage == 7) {
			end();
			player.setStarterStage(2);
			player.setCantMove(false);
			player.slayerTask(SlayerTask.assignBeginnerTask(player));
			player.setCantChangeLocation(false);
			player.sm("Well done, you have completed the tutorial! Here is your starter pack.");
			player.getInventory().addItem(995, 2500000);  //coins
			player.getInventory().addItem(1153, 1);  //Iron Full Helm
			player.getInventory().addItem(1115, 1);  //Iron Platebody.
			player.getInventory().addItem(1067, 1);  //Iron Platelegs.
			player.getInventory().addItem(1191, 1);  //Iron Kiteshield.
			player.getInventory().addItem(1323, 1);  //Iron Scimitar.
			player.getInventory().addItem(1169, 1);  //Coif.
			player.getInventory().addItem(1129, 1);  //Leather body.			
			player.getInventory().addItem(1095, 1);  //Leather chaps.
			player.getInventory().addItem(882, 50);  //50 Bronze Arrows.
			player.getInventory().addItem(841, 1);  //Shortbow.
			player.getInventory().addItem(1333, 1);  //Rune Scimitar.
			player.getInventory().addItem(4587, 1);  //Dragon Scimitar.
			player.getInventory().addItem(1712, 1);  //Amulet Of Glory.
			player.getInventory().addItem(7459, 1);  //Addy Gloves.
			player.getInventory().addItem(22443, 1); //Cape of introduction
		}
	}

	@Override
	public void finish() {

	}

}
