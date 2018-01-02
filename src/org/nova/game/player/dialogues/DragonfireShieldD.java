package org.nova.game.player.dialogues;

import org.nova.game.player.content.itemactions.SpecialSmithing;
/**
 * 
 * @author Fuzen Seth
 *
 */
public class DragonfireShieldD extends MatrixDialogue {
	@Override
	public void start() {
		sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {"", "You set to work, trying to attach the ancient draconic", "visage to your anti-dragonbreath shield. It's not easy to", "work with the ancient artifact and it takes all of your", "skill as a master smith."}, IS_ITEM, 11286, 1);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			player.interfaces().closeChatBoxInterface();
			SpecialSmithing.createDragonfireShield(player);
		} 
	}

	@Override
	public void finish() {

	}
}