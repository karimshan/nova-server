package org.nova.kshan.dialogues.testscript;

import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public class DialogueScriptManager {

	private Player player;
	private DialogueScript dialogue;

	public DialogueScriptManager(Player player) {
		this.player = player;
	}

	public void open(String dialogueName) {
		if(dialogue != null)
			terminate();
		dialogue = DialogueScripts.getDialogue(dialogueName);
		if(dialogue == null)
			return;
		dialogue.setPlayer(player);
		dialogue.open();
	}

	public void nextStage(int interfaceId, int buttonId) {
		try {
			if (dialogue == null) {
				player.interfaces().closeChatBoxInterface();
				return;
			}
			dialogue.nextStage(interfaceId, buttonId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void terminate() {
		dialogue = null;
		player.interfaces().closeChatBoxInterface();
	}

	public DialogueScript getCurrent() {
		return dialogue;
	}

}
