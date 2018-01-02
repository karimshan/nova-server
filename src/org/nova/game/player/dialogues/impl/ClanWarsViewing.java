package org.nova.game.player.dialogues.impl;

import org.nova.game.player.dialogues.MatrixDialogue;

/**
 * Handles the clan wars viewing dialogue.
 * 
 * @author Emperor
 * 
 */
public final class ClanWarsViewing extends MatrixDialogue {

    @Override
    public void start() {
	// TITLE: "Your clan does not appear to be in a war."
	sendOptionsDialogue("Select an option", "I want to watch a friend's clan war.", "Show me a battle - any battle.", "Oh, forget it.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
	end();
	player.packets().sendMessage("There are no clan wars going on currently.");
    }

    @Override
    public void finish() {
    }

}