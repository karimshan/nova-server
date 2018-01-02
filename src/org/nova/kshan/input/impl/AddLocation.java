package org.nova.kshan.input.impl;

import org.nova.game.map.Location;
import org.nova.kshan.input.type.string.StringInput;

/**
 * 
 * @author K-Shan
 *
 */
public class AddLocation extends StringInput {
	
	boolean replaceCurrentLocation;
	int currentIndex;

	@Override
	public void process(String input) {
		if(player.getLC().getNames().size() >= player.getLC().getMaxLocationCount()) {
			player.getDialogue().sendMsg("You cannot add any more locations to your crystal.");
			return;
		}
		replaceCurrentLocation = (Boolean) data[0];
		if(data.length > 1)
			currentIndex = (Integer) data[1];
		if(player.getLC().getNames().contains(input))
			player.sm("<col=ff0000>"+input+"</col> already exists in your crystal.");
		else {
			if(input.length() > 25) {
				player.getDialogue().start("InfoText", (Object) new String[] { "The name of a location cannot exceed 25 characters." });
				return;
			}
			if(replaceCurrentLocation) {
				player.getLC().replace(currentIndex, input, new Location(player.getX(), player.getY(), player.getZ()));
				player.sm("Saved location replaced with: "+input+": "+player.getLocation().toString()+"");
			} else {
				player.getLC().addLocation(input, new Location(player.getX(), player.getY(), player.getZ()));
				player.sm("Location saved!: "+input+": "+player.getLocation().toString()+"");
			}
		}
	}

	@Override
	public void whileTyping(int key, char keyChar, boolean shiftHeld) {
		
	}
	

}
