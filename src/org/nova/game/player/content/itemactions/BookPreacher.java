package org.nova.game.player.content.itemactions;

import org.nova.game.masks.ForceTalk;
import org.nova.game.player.Player;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class BookPreacher {

	public BookPreacher(Player player) {
		this.player = player;
	}

	/**
	 * Player instance.
	 */
	private transient Player player;
	/**
	 * Which book player has?
	 */
	private String currentBook;
	int preachType;
	private int bookType;
	
	public void detectCurrentBook() {
		if (player.getInventory().containsItem(3842, 1) || player.getEquipment().getShieldId() == 3842) {
			putCurrentBook("Zamorak book");
			setBookType(1);
		}
		else if (player.getInventory().containsItem(3844, 1) || player.getEquipment().getShieldId() == 3844) {
			putCurrentBook("Book of balance");
		}
	}
	
	public void findPreach() {
	player.interfaces().closeChatBoxInterface();
	player.closeInterfaces();
	
	switch (getBookType()) {
	case 0:
		if (preachType == 1)
		player.setNextForceTalk(new ForceTalk ("preach number one"));
		else if (preachType == 2)
		player.setNextForceTalk(new ForceTalk ("preach number one"));
		break;
	case 1:
		player.setNextForceTalk(new ForceTalk ("preach number 2"));
		break;
	case 2:
		player.setNextForceTalk(new ForceTalk ("preach number 3"));
		break;
	case 3:
		break;
		}
	}
	
	public String getCurrentBook() {
		return currentBook;
	}
	
	public void putCurrentBook(String currentBook) {
		this.currentBook = currentBook;
	}
	
	public int getBookType() {
		return bookType;
	}
	
	public void setBookType(int bookType) {
		this.bookType = bookType;
		findPreach();
	}
}
