package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.quests.DraynorMyth;

public class Aggie extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (player.getInventory().containsItem(1965, 3)) {
			player.getInventory().deleteItem(1965, 3);
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Thanks for the cabbages, " +player.getDisplayName()+ "."}, IS_NPC, npcId, 9827);
		}
		else if (player.getInventory().containsItem(1966, 3)) {
			player.getInventory().deleteItem(1966, 3);
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
					"Thanks for the cabbages, " +player.getDisplayName()+ "."}, IS_NPC, npcId, 9827);
			}
		else if (player.getInventory().containsItem(1967, 3)) {
			player.getInventory().deleteItem(1967, 3);
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
					"Thanks for the cabbages, " +player.getDisplayName()+ "."}, IS_NPC, npcId, 9827);
			}
		else if (player.getInventory().containsItem(1968, 3)) {
			player.getInventory().deleteItem(1968, 3);
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
					"Thanks for the cabbages, " +player.getDisplayName()+ "."}, IS_NPC, npcId, 9827);
			}
		
		else {
			player.interfaces().closeChatBoxInterface();
			player.getMatrixDialogues().startDialogue("SimpleMessage", "You have to get atleast three cabbages, to discuss with Aggie.");
			
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Basically the Draynor is in need of your help.", "We need you to defeat an insane monster." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, 
					"But for the first we need knowledge.",
					"You should talk to Wise old Man maybe he knows something."
					,"Wise old Man doesn't give out knowledge for free tho.",
					"You must offer him atleast 3,000 coins."}, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {

			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "Alright, Aggie. I'll start searching." },
			IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
		end();
		DraynorMyth.setStage1(true);
		DraynorMyth.ShowBindforDraynorMyths(player);
			} else
			end();
	}

	@Override
	public void finish() {

	}
}