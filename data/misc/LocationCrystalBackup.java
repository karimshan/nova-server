package org.nova.game.player.kshan.dialogues.impl;

import org.nova.game.Location;
import org.nova.game.player.content.Magic;
import org.nova.game.player.kshan.dialogues.Dialogue;
import org.nova.game.player.kshan.runscripts.type.string.StringScript;

/**
 * 
 * @author K-Shan
 *
 */
public class LocationCrystal extends Dialogue {
	
	int currentIndex = 0;
	int lastIndex = 3;
	int lastInterfaceId = FIVE_OPTIONS;
	boolean specialStage = false;
	int page = 1;
	
	@Override
	public void start() {
		 int currentSize = player.getLocationNames().isEmpty() ? 0 : player.getLocationNames().size();
			int optionsFilled = 0;
			if(currentSize == 0) {
				sendLines(false, "Your location crystal is currently empty,", 
						"but it doesn't have to be! Click on the button below to",
						"add your current location to the crystal.");
				player.packets().sendString("Click here to add your location to the crystal.", 212, 4);
				stage = 2;
			} else if(currentSize == 1) {
				sendOptions("Where would you like to teleport?", "", "1). "+player.getLocationNames().get(0)+": "
						+ ""+player.getPhysicalLocations().get(0).toString(), "");
				player.packets().sendHideIComponent(THREE_OPTIONS, 2, true);
				player.packets().sendHideIComponent(THREE_OPTIONS, 4, true);
				stage = 4;
			} else if(currentSize <= 5 && currentSize > 1) {
				for(int i = 0; i < currentSize; i++)
					optionsFilled++;
				for(int i = 0; i < optionsFilled; i++) {
					player.packets().sendString(player.getLocationNames().indexOf(player.getLocationNames().get(i)) + 1+"). "+player.getLocationNames().get(i)+": "
							+ ""+player.getPhysicalLocations().get(i).toString(), checkOptions(optionsFilled), 
							checkOptions(optionsFilled) == THREE_OPTIONS ? i + 2 : i + 1);
				}
				player.interfaces().sendChatBoxInterface(checkOptions(optionsFilled));
				player.packets().sendString("Where would you like to go?", 
					checkOptions(optionsFilled), checkOptions(optionsFilled) == THREE_OPTIONS ? 1 : 0);
				stage = 1;
			} else if(currentSize > 5) {
				for(int i = 0; i < 4; i++)
					player.packets().sendString(player.getLocationNames().indexOf(player.getLocationNames().get(i)) + 1+"). "+player.getLocationNames().get(i)+": "
							+ ""+player.getPhysicalLocations().get(i).toString(), checkOptions(5), i + 1);
				player.packets().sendString("<col=0000ff>Next Page ("+(page + 1)+")", checkOptions(5), 5);
				player.interfaces().sendChatBoxInterface(checkOptions(5));
				player.packets().sendString("Where would you like to go?", checkOptions(5), 0);
				stage = 3;
			}
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		if(interfaceId == FIVE_OPTIONS && buttonId == 5 && stage != 1 && stage != 6) {
			page++;
			int lastIndexInArray = player.getLocationNames().isEmpty() ? 0 : player.getLocationNames().size();
			int count = 0;
			int trueCount = 0;
			for(int i = (lastIndex + 1); i < lastIndexInArray; i++) {
				count++;
				trueCount++;
			}
			if(count > 3)
				count = 3;
			for(int i = 0; i < count; i++) {
				player.packets().sendString(player.getLocationNames().indexOf(player.getLocationNames().get(lastIndex + i + 1)) + 1+"). "
						+ ""+player.getLocationNames().get(lastIndex + i + 1)+": "
					+ ""+player.getPhysicalLocations().get(lastIndex + i + 1).toString(), checkOptions(trueCount > 3 ? count + 2 : count + 1), 
						checkOptions(trueCount > 3 ? count + 2 : count + 1) == THREE_OPTIONS ? i + 2 : i + 1);
			}
			player.interfaces().sendChatBoxInterface(checkOptions(trueCount > 3 ? count + 2 : count + 1));
			player.packets().sendString("<col=0000ff>Previous Page: ("+(page - 1)+")", checkOptions(trueCount > 3 ? count + 2 : count + 1), 
					checkOptions(trueCount > 3 ? count + 2 : count + 1) == THREE_OPTIONS ? count + 2 : count + 1);
			if(trueCount > 3)
				player.packets().sendString("<col=0000ff>Next Page: ("+(page + 1)+")", checkOptions(count + 2), count + 2);
			player.packets().sendString("Where would you like to go?", checkOptions(count + 1), checkOptions(count + 1) == THREE_OPTIONS ? 1 : 0);
			lastIndex = (lastIndex + count);
			stage = 7;
		}
		switch(stage) {
			case 1:
				currentIndex = interfaceId == THREE_OPTIONS ? buttonId - 2 : buttonId - 1;
				sendOptions(TITLE, "Teleport to: <col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col>: "
					+ ""+player.getPhysicalLocations().get(currentIndex).toString(), "Make changes to this location.", "Go back to the page you were on.");
				stage = 5;
				break;
			case 2:
				end();
				player.scripts().startScript("AddToLocationCrystal", "Enter in a name for this location", false);
				break;
			case 3:
				if(buttonId != 5) {
					currentIndex = interfaceId == THREE_OPTIONS ? buttonId - 2 : buttonId - 1;
					sendOptions(TITLE, "Teleport to: <col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col>: "
						+ ""+player.getPhysicalLocations().get(currentIndex).toString(), "Make changes to this location.", "Go back to the page you were on.");
					stage = 5;
				}
				break;
			case 4:
				sendOptions(TITLE, "Teleport to: <col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col>: "
						+ ""+player.getPhysicalLocations().get(currentIndex).toString(), "Make changes to this location.", "Go back to the page you were on.");
				stage = 5;
				break;
			case 5:
				if(buttonId == OPTION1_OTHER) {		
					player.sm("Teleporting to: <col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col> at coordinates: <col=ff0000>"
						+ ""+player.getPhysicalLocations().get(currentIndex).toString());
					Magic.specialTele(player, (Location) player.getPhysicalLocations().get(currentIndex), false);
					end();
				} else if(buttonId == OPTION2_OTHER) {
					sendOptions("What change would you like to make?", 
						"Change the name only. (<col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col>)",
							"Change only the location to your current location.", "Change both the name and location.",
								"Delete this location.", "Go back to the options list.");
					specialStage = false;
					stage = 6;
				} else
					start();
				break;
			case 6:
				if(buttonId == OPTION1) {
					end();
					player.scripts().startScript(new StringScript() {
						@Override
						public void process(String input) {
							if(player.getLocationNames().contains(input))
								player.sm("<col=ff0000>"+input+"</col> already exists in your crystal.");
							else {
								player.sm("Current name: <col=0000ff>"+player.getLocationNames().get(currentIndex)+"</col> has been replaced with: <col=ff0000>"+input);
								player.getLocationNames().set(currentIndex, input);
							}
						}
					}, "Enter in a new name to replace: <col=ff0000>"+player.getLocationNames().get(currentIndex));
				} else if(buttonId == OPTION2) {
					end();
					player.getPhysicalLocations().set(currentIndex, player.getLocation());
					player.sm("<col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col> has been updated with a new location: "
							+ ""+player.getPhysicalLocations().get(currentIndex).toString());
				} else if(buttonId == OPTION3) {
					end();
					player.scripts().startScript("AddToLocationCrystal", "Enter in the new name for this location", true, currentIndex);
				} else if(buttonId == OPTION4) { 
					sendLines(false, "Are you sure you would like to delete the location:", "<col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col>"
							+ ": <col=ff0000>"+ ""+player.getPhysicalLocations().get(currentIndex).toString()+"</col> ?", 
								"(This location will be permanently deleted from the location crystal)");
					stage = 9;
				} else if(buttonId == OPTION5) {
					sendOptions(TITLE, "Teleport to: <col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col>: "
							+ ""+player.getPhysicalLocations().get(currentIndex).toString(), "Make changes to this location.", "Go back to the page you were on.");
					stage = specialStage ? 8 : 5;
				}
				break;
			case 7:
				if(interfaceId == TWO_OPTIONS && buttonId != 2 || (interfaceId == FOUR_OPTIONS || interfaceId == THREE_OPTIONS) && buttonId != 4 || stage != 3 && buttonId != 5 && buttonId != 4 && interfaceId == FIVE_OPTIONS) {
					currentIndex = interfaceId == THREE_OPTIONS ? buttonId + lastIndex - 3 : interfaceId == TWO_OPTIONS ? buttonId + lastIndex - 1 : buttonId + lastIndex - 3;
					System.out.println(currentIndex);
					sendOptions(TITLE, "Teleport to: <col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col>: "
							+ ""+player.getPhysicalLocations().get(currentIndex).toString(), "Make changes to this location.", "Go back to the page you were on.");
					stage = 8;
					lastInterfaceId = interfaceId;
				} else if(interfaceId == TWO_OPTIONS && buttonId == 2) {
					page--;
					lastIndex = lastIndex - 4;
					int lastIndexInArray = player.getLocationNames().isEmpty() ? 0 : player.getLocationNames().size();
					int count = 0;
					int trueCount = 0;
					for(int i = (lastIndex + 1); i < lastIndexInArray; i++) {
						count++;
						trueCount++;
					}
					if(count > 3)
						count = 3;
					for(int i = 0; i < count; i++) {
						player.packets().sendString(player.getLocationNames().indexOf(player.getLocationNames().get(lastIndex + i + 1)) + 1+"). "
								+ ""+player.getLocationNames().get(lastIndex + i + 1)+": "
							+ ""+player.getPhysicalLocations().get(lastIndex + i + 1).toString(), checkOptions(trueCount > 3 ? count + 2 : count + 1), 
								checkOptions(trueCount > 3 ? count + 2 : count + 1) == THREE_OPTIONS ? i + 2 : i + 1);
					}
					player.interfaces().sendChatBoxInterface(checkOptions(trueCount > 3 ? count + 2 : count + 1));
					player.packets().sendString("<col=0000ff>Previous Page: ("+(page - 1)+")", checkOptions(trueCount > 3 ? count + 2 : count + 1), 
							checkOptions(trueCount > 3 ? count + 2 : count + 1) == THREE_OPTIONS ? count + 2 : count + 1);
					if(trueCount > 3)
						player.packets().sendString("<col=0000ff>Next Page: ("+(page + 1)+")", checkOptions(count + 2), count + 2);
					player.packets().sendString("Where would you like to go?", checkOptions(count + 1), checkOptions(count + 1) == THREE_OPTIONS ? 1 : 0);
					lastIndex = (lastIndex + count);
					stage = 7;
				} else if((interfaceId == THREE_OPTIONS || interfaceId == FOUR_OPTIONS || interfaceId == FIVE_OPTIONS) && buttonId == 4) {
					page--;
					if(page == 1) {
						lastIndex = 3;
						currentIndex = 0;
						start();
					} else {
						lastIndex = interfaceId == THREE_OPTIONS ? lastIndex - 5 : lastIndex - 6;
						int lastIndexInArray = player.getLocationNames().isEmpty() ? 0 : player.getLocationNames().size();
						int count = 0;
						int trueCount = 0;
						for(int i = (lastIndex + 1); i < lastIndexInArray; i++) {
							count++;
							trueCount++;
						}
						if(count > 3)
							count = 3;
						for(int i = 0; i < count; i++) {
							player.packets().sendString(player.getLocationNames().indexOf(player.getLocationNames().get(lastIndex + i + 1)) + 1+"). "
									+ ""+player.getLocationNames().get(lastIndex + i + 1)+": "
								+ ""+player.getPhysicalLocations().get(lastIndex + i + 1).toString(), checkOptions(trueCount > 3 ? count + 2 : count + 1), 
									checkOptions(trueCount > 3 ? count + 2 : count + 1) == THREE_OPTIONS ? i + 2 : i + 1);
						}
						player.interfaces().sendChatBoxInterface(checkOptions(trueCount > 3 ? count + 2 : count + 1));
						player.packets().sendString("<col=0000ff>Previous Page: ("+(page - 1)+")", checkOptions(trueCount > 3 ? count + 2 : count + 1), 
								checkOptions(trueCount > 3 ? count + 2 : count + 1) == THREE_OPTIONS ? count + 2 : count + 1);
						if(trueCount > 3)
							player.packets().sendString("<col=0000ff>Next Page: ("+(page + 1)+")", checkOptions(count + 2), count + 2);
						player.packets().sendString("Where would you like to go?", checkOptions(count + 1), checkOptions(count + 1) == THREE_OPTIONS ? 1 : 0);
						lastIndex = (lastIndex + count);
						stage = 7;
					}
				}
				break;
			case 8:
				if(buttonId == OPTION1_OTHER) {		
					player.sm("Teleporting to: <col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col> at coordinates: <col=ff0000>"
						+ ""+player.getPhysicalLocations().get(currentIndex).toString());
					Magic.specialTele(player, (Location) player.getPhysicalLocations().get(currentIndex), false);
					end();
				} else if(buttonId == OPTION2_OTHER) {
					sendOptions("What change would you like to make?", 
						"Change the name only. (<col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col>)",
							"Change only the location to your current location.", "Change both the name and location.", 
								"Delete this location.", "Go back to the options list.");
					stage = 6;
					specialStage = true;
				} else {
					switch(lastInterfaceId) {
						case TWO_OPTIONS:
							lastIndex = lastIndex - 1;
							break;
						case THREE_OPTIONS:
							lastIndex = lastIndex - 2;
							break;
						default:
							lastIndex = lastIndex - 3;
							break;
					}
					int lastIndexInArray = player.getLocationNames().isEmpty() ? 0 : player.getLocationNames().size();
					int count = 0;
					int trueCount = 0;
					for(int i = (lastIndex + 1); i < lastIndexInArray; i++) {
						count++;
						trueCount++;
					}
					if(count > 3)
						count = 3;
					for(int i = 0; i < count; i++) {
						player.packets().sendString(player.getLocationNames().indexOf(player.getLocationNames().get(lastIndex + i + 1)) + 1+"). "
								+ ""+player.getLocationNames().get(lastIndex + i + 1)+": "
							+ ""+player.getPhysicalLocations().get(lastIndex + i + 1).toString(), checkOptions(trueCount > 3 ? count + 2 : count + 1), 
								checkOptions(trueCount > 3 ? count + 2 : count + 1) == THREE_OPTIONS ? i + 2 : i + 1);
					}
					player.interfaces().sendChatBoxInterface(checkOptions(trueCount > 3 ? count + 2 : count + 1));
					player.packets().sendString("<col=0000ff>Previous Page: ("+(page - 1)+")", checkOptions(trueCount > 3 ? count + 2 : count + 1), 
							checkOptions(trueCount > 3 ? count + 2 : count + 1) == THREE_OPTIONS ? count + 2 : count + 1);
					if(trueCount > 3)
						player.packets().sendString("<col=0000ff>Next Page: ("+(page + 1)+")", checkOptions(count + 2), count + 2);
					player.packets().sendString("Where would you like to go?", checkOptions(count + 1), checkOptions(count + 1) == THREE_OPTIONS ? 1 : 0);
					lastIndex = (lastIndex + count);
					stage = 7;
				}
				break;
			case 9:
				sendOptions("Delete this location?", "Yes, delete the location: <col=ff0000>"+player.getLocationNames().get(currentIndex), "No, go back to where I was.");
				stage = 10;
				break;
			case 10:
				if(buttonId == 1) {
					end();
					player.sm("Deleted location: <col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col> at coordinates: <col=ff0000>"
							+ ""+player.getPhysicalLocations().get(currentIndex).toString());
					player.getLocationNames().remove(currentIndex);
					player.getPhysicalLocations().remove(currentIndex);
				} else {
					sendOptions("What change would you like to make?", 
						"Change the name only. (<col=ff0000>"+player.getLocationNames().get(currentIndex)+"</col>)",
							"Change only the location to your current location.", "Change both the name and location.",
								"Delete this location.", "Go back to the options list.");
					stage = 6;
				}
				break;
		}
	}

	@Override
	public void finish() {
		
	}
		
}