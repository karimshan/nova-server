package org.nova.kshan.dialogues.impl;

import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.dialogues.listoptions.impl.LocationCrystal;

/**
 * 
 * @author K-Shan
 *
 */
public class LocationCrystalIntro extends Dialogue {

	@Override
	public void start() {
		sendOptions("Location Crystal", "Open the crystal", 
			"Add your current location to the crystal.", "Wipe the crystal's memory");
	}
	
	@Override
	public void process(int i, int b) {
		if(stage == 0 && b == 2) {
			end();
			player.animate(4070);
			player.graphics(306);
			player.getDialogue().start(new LocationCrystal());
		} else if(stage == 0 && b == 3) {
			end();
			player.getInputEvent().run("AddLocation", "Enter in a name for this location", false);
		} else if(stage == 0 && b == 4) {
			sendLines(false, "<col=ff0000>WARNING: ONCE DELETED, YOUR SAVED LOCATIONS",
				"<col=ff0000>CANNOT BE RECOVERED. ARE YOU SURE YOU", "<col=ff0000>WANT TO DELETE ALL SAVED LOCATIONS?");
			stage = 1;
		} else if(stage == 1) {
			sendOptions(TITLE, "Yes, delete my saved locations", "No, I want to keep my locations!");
			stage = 2;
		} else if(stage == 2) {
			end();
			if(b == 1) {
				player.getLC().clearCrystal();
				player.graphics(307);
				player.animate(4069);
				player.sm("The crystal trembles as it wipes out the last of your saved locations.");
			}
		}
			
	}
	
	@Override
	public void finish() {
		
	}
}
