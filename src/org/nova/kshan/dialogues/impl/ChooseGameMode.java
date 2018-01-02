package org.nova.kshan.dialogues.impl;

import org.nova.game.Game;
import org.nova.game.npc.NPC;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class ChooseGameMode extends Dialogue {
	
		@Override
		public void start() {
			player.setCantMove(true);
			sendOptions("Select your XP rate:", new String[] { 
				"Easy: [x195 <col=c12006>combat</col> - x30 <col=0000ff>skilling]",
				"Normal: [x75 <col=c12006>combat</col> - x18 <col=0000ff>skilling]",
				"Advanced: [x20 <col=c12006>combat</col> - x6.85 <col=0000ff>skilling]",
				"Extreme: [x3.8 <col=c12006>combat</col> - x1.75 <col=0000ff>skilling]"});
		}
		
		@Override
		public void process(int i, int b) {
			if(b == OPTION1) {
				end();
				player.combatXPBoost(195);
				player.skillXPBoost(30);
				player.sm("Your game mode: <col=00ff00>Easy</col>. [x195 <col=c12006>combat</col> - x30 <col=0000ff>skilling]");
			} else if(b == OPTION2) {
				end();
				player.combatXPBoost(75);
				player.skillXPBoost(18);
				player.sm("Your game mode: <col=ffff00>Normal</col>. [x75 <col=c12006>combat</col> - x18 <col=0000ff>skilling]");
			} else if(b == OPTION3) {
				end();
				player.combatXPBoost(20);
				player.skillXPBoost(6.85);
				player.sm("Your game mode: <col=0000ff>Advanced</col>. [x20 <col=c12006>combat</col> - x6.85 <col=0000ff>skilling]");
			} else if(b == OPTION4) {
				end();
				player.combatXPBoost(3.8);
				player.skillXPBoost(1.75);
				player.sm("Your game mode: <col=ff0000>EXTREME</col>. [x3.8 <col=c12006>combat</col> - x1.75 <col=0000ff>skilling]");
			}
			if(player.getStarterStage() == 0) {
				player.setStarterStage(1);
				player.setCantMove(false);
				player.unlock();
				for(NPC n : Game.getLocalNPCs(player)) {
					if(n.defs().getName().contains("Guide")) {
						player.sm("Follow the yellow arrow indicated on the minimap to reach the guide, then talk to him.");
						player.hints().addHintIcon(n, 1, true, false);
					}
				}
			}
			player.setCantMove(false);
			player.unlock();
		}
		
		@Override
		public void finish() { }
}
