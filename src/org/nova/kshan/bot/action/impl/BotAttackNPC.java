package org.nova.kshan.bot.action.impl;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.actions.PlayerCombat;
import org.nova.kshan.bot.action.BotAction;

/**
 * 
 * @author K-Shan
 *
 */
public class BotAttackNPC extends BotAction {
	
	String replaceFrom, replaceWith;

	@Override
	public GameTick getGameTick() {
		
		replaceFrom = (String) data[1];
		replaceWith = (String) data[2];
		
		return new GameTick(1.875) {

			@Override
			public void run() {
				Player p = bot;
				String npcName = "";
				String charsUpToNPCName = replaceFrom.replace(" @npcName@", "");
				npcName = replaceWith.replace(charsUpToNPCName+" ", "");
				int npcId = -1;
				for(NPC npc : Game.getLocalNPCs(p))
					if(npc != null && npc.getName().toLowerCase().contains(npcName.toLowerCase()) && npcName.length() >= 3)
						npcId = npc.getId();
				NPC n = Game.findNPC(npcId);
				if(npcName.length() < 4 && !npcName.contains("man")) {
					stopAction();
					p.publicChat("You need to be more specific.");
					return;
				}
				if(n == null || !n.withinDistance(p, 14)) {
					stopAction();
					p.publicChat("What "+npcName+"...?");
					return;
				}
				if(!n.defs().hasAttackOption()) {
					stopAction();
					p.publicChat("I cannot attack the "+n.getName().toLowerCase()+".");
					return;
				}
				if(!p.isUnderAttack() && !n.isUnderAttack() && n != null) {
					p.stopAll(false);
					p.addWalkSteps(n.getX(), n.getY());
					p.face(n);
					if(p.withinDistance(n)) {
						stopAction();
						p.stopAll();
						p.getActionManager().setAction(new PlayerCombat(n));
					}
				} else if(p.isUnderAttack()) {
					stopAction();
					p.publicChat("I'm already under attack.");
					return;
				}
			}
			
		};
		
	}

	@Override
	public String getActionType() {
		return "ATTACK_NPC";
	}

}
