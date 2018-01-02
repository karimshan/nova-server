package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class SandwichLady extends MatrixDialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_3_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hello, I want to give you a snack for a free.",
						"Please select a snack and you will be free!",
						"I want you to pick a baguette."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Sure, let me have a look please." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			player.interfaces().sendInterface(297);
			player.sm("Sandwich Lady wants you to pick a baguette.");
			player.interfaces().closeChatBoxInterface();
		} 
	}

	@Override
	public void finish() {

	}
}
