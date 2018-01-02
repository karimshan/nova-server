package org.nova.game.player.dialogues;

/**
 * 
 * @author Fuzen Seth
 *
 */
public class DragonfireFinish extends MatrixDialogue {
	@Override
	public void start() {
		sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] { "",
				"Even for an expert armourer it is not an easy task,",
				"but eventually it is ready. You have crafted the",
				"draconic visage and anti-dragonbreath shield into a",
				"dragonfire shield."}, IS_ITEM, 11283, 1);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			player.interfaces().closeChatBoxInterface();
		} 
	}

	@Override
	public void finish() {

	}
}