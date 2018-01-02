package org.nova.kshan.content.skills.construction;

import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class HousePortal extends Dialogue {
	
	String location;

	@Override
	public void start() {
		location = (String) data[0];
		if(player.getHouse().hasPurchasedHouse())
			if(!player.getHouse().getHouseLocationName().equals(location)) {
				end();
				sendLines("This is not the place where your house is located.", "Your house is located in "+player.getHouse().getHouseLocationName()+".");
				return;
			} else
				sendOptions("Select an Option", "Go to your house.", "Go to your house (building mode).", 
					"Go to a friend's house.", "Never mind" );
		else {
			end();
			sendLines("You will need to purchase a house before you can do this.", 
				"Try talking to the Estate Agent who is located in Falador.");
		}
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		if (buttonId == 1) {
			end();
			player.getHouse().setInBuildingMode(false);
			player.getRandomEvent().start("EnterHouse");
		} else if(buttonId == 2) {
			end();
			if(player.getHouse().hasGuests())  {
				player.sm("You cannot enter building mode at this time. There are guests at your house.");
				return;
			}
			player.getHouse().setInBuildingMode(true);
			player.getRandomEvent().start("EnterHouse");
		} else if(buttonId == 3) {
			end();
			if (stage == 0)
				player.getInputEvent().run(new EnterHouseInput(), "Enter the name of the friend whose house you would like to go to:");
		} else
			end();
	}

	@Override
	public void finish() {

	}

}
