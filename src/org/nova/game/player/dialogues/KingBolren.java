package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.MiscellaniaThrone;

public class KingBolren extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hey there."}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "What is a gnome doing in Miscellania?","Is this your castle, Bolren?" },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"Haha yes it is. I'm here to try to make peace.",
					"with Etceteria king. You must help me because"
					,"if I would go there it would be very dangerous.",
					"Such an brave adventurer like you can..."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "Go and speak with the king there.", "For the first I need to know whos the king", "in Etcetaria, we have been in war for years."}, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"This is why I want this to be solved..."
					}, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
		player.setMiscellaniaThrone(1);
		MiscellaniaThrone.ShowBind(player);
			end();
			} else
			end();
	}

	@Override
	public void finish() {

	}
}