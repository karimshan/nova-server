package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.controlers.NewHomeControler;

public class NewHomeGuide extends MatrixDialogue {

	private NewHomeControler controler;
	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		controler = (NewHomeControler) parameters[1];
		int s = controler.getStage();
		if (s == 0) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinition.get(npcId).name,
							"Greetings! I see you are a new arrival in this land. My",
							"job is welcome all new visitors. So Welcome!" },
					IS_NPC, npcId, 9827);
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinition.get(npcId).name,
							"Anyways, you might be wondering, who made this server.",
							"The Owner and Developer is Emanuel" },
					IS_NPC, npcId, 9827);
		}
	}

	@Override
	public void finish() {

	}

}
