package org.nova.kshan.dialogues.listoptions.impl;

import java.util.ArrayList;

import org.nova.game.map.Location;
import org.nova.game.player.content.Magic;
import org.nova.kshan.dialogues.listoptions.ListOptionsDialogue;
import org.nova.kshan.input.impl.AddLocation;
import org.nova.kshan.input.type.string.StringInput;

/**
 * The user's location crystal that stores saved locations.
 * There will be a limit added for certain users
 * with different membership ranks, etc.
 * 
 * @author K-Shan
 *
 */
public class LocationCrystal extends ListOptionsDialogue {
	
	boolean goBackToPage;
	
	@Override
	public void start() {
		if(getList().isEmpty()) {
			sendLines(false, "Your location crystal is currently empty,", 
				"but it doesn't have to be! Click on the button below to",
					"add your current location to the crystal.");
			player.packets().sendString("Click here to add your location to the crystal.", 212, 4);
			stage = 9;
		} else
			super.start();
	}

	@Override
	public ArrayList<?> getList() {
		return (ArrayList<?>) player.getLC().getNames();
	}

	@Override
	public String getTitle() {
		return "Where would you like to teleport?";
	}

	@Override
	public void processStage(int s) {
		String name = s == 9 ? "" : (String) getSelectedElement();
		Location location = s == 9 ? null : player.getLC().getLocations().get(currentIndex);
		if(stage == 4) {
			goBackToPage = true;
			specialStage = true;
		}
		switch(stage) {
			case 1:
			case 2:
			case 3:
			case 4:
				sendOptions(TITLE, "Teleport to: <col=ff0000>"+name+"</col> "+location.toString(), 
					"Make changes to this location.", 
					"Go back to the page you were on.");
				stage = 5;
				break;
			case 5:
				if(buttonId == OPTION1_OTHER) {
					if(player.getLC().getCharges() < 1) {
						end();
						player.getDialogue().sendMsg("You do not have any charges remaining.", 
							"Please see (INSERT NPC) to recharge your crystal. (For Shan)");
						return;
					} else if(player.getLC().getCharges() == 1) {
						player.sm("You use the final charge on your location crystal. Please see (INSERT NPC) to recharge your crystal.");
						player.getLC().setCharges(player.getLC().getCharges() - 1);
					} else {
						player.getLC().setCharges(player.getLC().getCharges() - 1);
						player.sm("You have "+player.getLC().getCharges()+" charge(s) left on your crystal.");
					}
					player.sm("Teleporting to: <col=ff0000>"+name+"</col> at coordinates: <col=ff0000>"+location.toString());
					Magic.specialTele(player, location, false);
					end();
				} else if(buttonId == OPTION2_OTHER) {
					sendOptions("What change would you like to make?", 
						"Change the name only. (<col=ff0000>"+name+"</col>)",
						"Change only the location to your current location.", 
						"Change both the name and location.",
						"Delete this location.", 
						"Go back to the options list.");
					specialStage = false;
					stage = 6;
				} else {
					if(goBackToPage) {
						lastIndex = lastInterfaceId == TWO_OPTIONS ? lastIndex - 1 : 
							lastInterfaceId == THREE_OPTIONS ? lastIndex - 2 : lastIndex - 3;
						stage = 4;
						sendPageOptions();
						goBackToPage = false;
						specialStage = false;
					} else
						start();
				}
				break;
			case 6:
				if(buttonId == OPTION1) {
					end();
					player.getInputEvent().run(new StringInput() {
						@Override
						public void process(String input) {
							if(player.getLC().getNames().contains(input))
								player.sm("<<col=col=ff0000>"+input+"</col> already exists in your crystal.");
							else {
								player.sm("Current name: <col=0000ff>"+name+"</col> has been replaced with: <col=ff0000>"+input);
								player.getLC().getNames().set(currentIndex, input);
							}
						}

						@Override
						public void whileTyping(int key, char keyChar, boolean shiftHeld) {
							
						}
						
					}, "Enter in a new name to replace: <col=ff0000>"+name);
				} else if(buttonId == OPTION2) {
					end();
					player.getLC().getLocations().set(currentIndex, Location.create(player));
					player.sm("<col=ff0000>"+name+"</col> has been updated with a new location: "
							+ ""+player.getLC().getLocations().get(currentIndex).toString());
				} else if(buttonId == OPTION3) {
					end();
					player.getInputEvent().run(new AddLocation(), 
						"Enter in the new name for this location:", true, currentIndex);
				} else if(buttonId == OPTION4) { 
					sendLines(false, "Are you sure you want to delete the location:", 
						"<col=ff0000>"+name+"</col> - "+location.toString()+" ?", 
						"(This location will be permanently deleted from the location crystal)");
					stage = 7;
				} else if(buttonId == OPTION5) {
					sendOptions(TITLE, "Teleport to: <col=ff0000>"+name+"</col> "+location.toString(), 
						"Make changes to this location.", 
						"Go back to the page you were on.");
					stage = 5;
					goBackToPage = specialStage ? true : goBackToPage;
				}
				break;
			case 7:
				sendOptions("Delete this location?", 
					"Yes, delete the location: <col=ff0000>"+name, "No, go back to where I was.");
				stage = 8;
				break;
			case 8:
				if(buttonId == 1) {
					end();
					player.sm("Deleted location: <col=ff0000>"+name+"</col> at coordinates: <col=ff0000>"+location.toString());
					player.getLC().deleteLocation(currentIndex);
				} else {
					sendOptions("What change would you like to make?", 
						"Change the name only. (<col=ff0000>"+name+"</col>)",
						"Change only the location to your current location.", 
						"Change both the name and location.",
						"Delete this location.", 
						"Go back to the options list.");
					stage = 6;
				}
				break;
			case 9:
				end();
				player.getInputEvent().run(new AddLocation(), "Enter the name for this location", false);
				break;
		}
	}
	
	@Override
	public <V> String getEachLine(V value) {
		return value+" - "+getLocation((String) value).toString();
	}
	
	@Override
	public int[] getButton5Overrides() {
		return new int[] { 6 };
	}
	
	/**
	 * Returns the location in the location crystal that corresponds
	 * with the specified name parameter.
	 * @param name
	 * @return
	 */
	public Location getLocation(String name) {
		int count = 0;
		Location location = null;
		for(String s : player.getLC().getNames()) {
			if(s.equals(name))
				location = player.getLC().getLocations().get(count);
			count++;
		}
		return location;
	}
		
}
