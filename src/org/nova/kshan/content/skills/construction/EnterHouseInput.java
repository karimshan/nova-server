package org.nova.kshan.content.skills.construction;

import java.io.File;

import org.nova.game.Game;
import org.nova.game.player.Player;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.input.type.string.StringInput;
import org.nova.utility.misc.Misc;

public class EnterHouseInput extends StringInput {

	@Override
	public void process(String input) {
		input = input.toLowerCase();
		Player target = Game.getPlayer(input);
		if(target == null) {
			sendLines(Misc.formatPlayerNameForDisplay(input)+" is not online.");
			return;
		}
		if (target.getUsername().equals(
				player.getUsername())) {
			sendLines("You can't enter your own house this way.");
			return;
		}
		if(!target.getHouse().hasPurchasedHouse()) {//!new File("data/playerdata/construction/saves/"+target.getUsername()).exists()) {
			sendLines(target.getDisplayName()+" doesn't have a house.");
			return;
		}
		if(target.getHouse().getMapChunks() == null) {
			sendLines(target.getDisplayName()+" is not at his house.");
			return;
		}
		if(target.getHouse().inBuildingMode()) {
			sendLines(target.getDisplayName()+"'s house is currently under construction.");
			return;
		}
		if(!target.getHouse().hasLoadedHouse()) {
			sendLines(target.getDisplayName()+" has just entered his house. Try again in a few seconds.");
			return;
		}
		if(target.getHouse().isPrivateHouse()) {
			sendLines(target.getDisplayName()+"'s has privacy mode enabled.");
			return;
		}
		if (target.getHouse().getMapChunks() != null) {
			player.getRandomEvent().start("EnterHouse", target);
			sendLines("Entering "
					+ target.getDisplayName()
					+ "'s house...");
		}
	}
	
	public void sendLines(String... lines) {
		Dialogue.sendLines(player, lines);
	}

	@Override
	public void whileTyping(int key, char keyChar, boolean shiftHeld) {
		
	}

}
