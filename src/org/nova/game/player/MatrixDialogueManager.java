package org.nova.game.player;

import org.nova.game.player.dialogues.MatrixDialogue;
import org.nova.game.player.dialogues.MatrixDialogueHandler;

public class MatrixDialogueManager {

	private Player player;
	MatrixDialogue lastDialogue;

	public MatrixDialogueManager(Player player) {
		this.player = player;
	}

	public void startDialogue(Object key, Object... parameters) {
		if (!player.getControllerManager().useDialogueScript(key))
			return;
		if (lastDialogue != null)
			lastDialogue.finish();
		lastDialogue = MatrixDialogueHandler.getDialogue(key);
		if (lastDialogue == null)
			return;
		lastDialogue.parameters = parameters;
		lastDialogue.setPlayer(player);
		lastDialogue.start();
	}

	public void continueDialogue(int interfaceId, int componentId) {
		if (lastDialogue == null) {
			player.interfaces().closeChatBoxInterface();
			return;
		}
		lastDialogue.run(interfaceId, componentId);
	}

	public void finishDialogue() {
		if (lastDialogue == null)
			return;
		lastDialogue.finish();
		lastDialogue = null;
		if (player.interfaces().containsChatBoxInter())
			player.interfaces().closeChatBoxInterface();
	}

}
