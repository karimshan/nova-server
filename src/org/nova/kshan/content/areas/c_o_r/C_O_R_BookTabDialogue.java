package org.nova.kshan.content.areas.c_o_r;

import org.nova.game.map.GlobalObject;
import org.nova.game.player.content.Magic;
import org.nova.kshan.dialogues.Dialogue;

/**
 * The dialogue that pops up if the player clicks the cavern of remembrance option
 * in the book tab.
 * 
 * @author K-Shan
 *
 */
public class C_O_R_BookTabDialogue extends Dialogue {

	@Override
	public void start() {
		sendOptions("What would you like to do?", "View challenges' progress.", "Teleport to the Cavern of Remembrance.");
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		end();
		if(buttonId == 2) {
			Magic.telePlayer(player, C_O_R_Data.TELEPORT_TILE, false);
			player.sm("In order to fight the data bosses, you must complete the challenges.");
		} else {
			player.interfaces().sendInterface(275);
			player.packets().sendRunScript(1207, 12);
			player.packets().sendString("<col=000000><shad=9932CC>Cavern of Remembrance Boss Kills", 275, 2);
			for(int i = 12; i < 316; i++)
				player.packets().sendString("", 275, i);
			int index = 0;
			for(GlobalObject o : C_O_R_Data.getBossObjects()) {
				String name = o	.getName();
				if(player.getCavernOfRemembrance().getOriginalBosses().containsKey(name)) {
					int kills = player.getCavernOfRemembrance().getOriginalBosses().get(name);
					int killsNeeded = C_O_R_Data.getKillsNeeded(name);
					String extraString = player.getCavernOfRemembrance().hasKills(name) ? 
						"<col=000000><shad=00ff00><str>" : "<col=000000><shad=ff0000>";
					player.packets().sendString(extraString + name+" - "+kills+"/"+killsNeeded, 275, 16 + index);
				} else
					player.packets().sendString("<col=000000><shad=ff0000>"+name+" - 0/"+C_O_R_Data.getKillsNeeded(name), 275, 16 + index);
				index++;
			}
		}
	}

	@Override
	public void finish() {
		
	}

}
