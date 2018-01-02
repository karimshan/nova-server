package org.nova.kshan.content.skills.construction;

import java.util.ArrayList;

import org.nova.game.player.Player;
import org.nova.kshan.dialogues.listoptions.ListOptionsDialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class ExpelGuests extends ListOptionsDialogue {
	
	@Override
	public void start() {
		if(getList().isEmpty()) {
			end();
			sendLines("You have no guests in your house.");
		} else
			super.start();
	}

	@Override
	public ArrayList<?> getList() {
		return (ArrayList<?>) player.getHouse().getCachedGuests();
	}

	@Override
	public String getTitle() {
		return "Who do you wish to expel?";
	}

	@Override
	public void processStage(int s) {
		end();
		Player guest = ((Player) getList().get(currentIndex));
		guest.getRandomEvent().fullyStop(false);
		guest.setLocation(guest.getHouse().getHouseLocation());
		player.getHouse().getCachedGuests().remove(guest);
		player.sm("You've expelled "+guest.getDisplayName()+".");
		guest.packets().sendMessage("You've been expelled from "+player.getDisplayName()+"'s house.");
	}
	
	@Override
	public <E> String getEachLine(E value) {
		return ((Player) value).getDisplayName();
	}
	
}
