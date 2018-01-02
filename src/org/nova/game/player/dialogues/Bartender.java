package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;

public class Bartender extends MatrixDialogue {

	private int npcId;
	public static final int FINISH  = 42;
	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Good day, how can I help you?"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
	switch (stage) {
	case -1:
				sendEntityDialogue(SEND_2_TEXT_CHAT,
						new String[] { player.getDisplayName(), "Oh hey, i'm just wondering that", "what are you doing over here?" },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 2;
		break;
	case 2:
		case 1://Takes to keldagrim.
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
							"I sell beer, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
			stage = 3;
			break;
	
		case 3:
			sendDialogue(SEND_4_OPTIONS, "What do you want to do?","I would like to purchase a beer please.", "How much does one beer cost?", "Nevermind, I don't drink.", "I will go pick up some money and come back.");
			stage = 4;
		break;
		case 4:
			switch (componentId) {
			case 1:
				sendEntityDialogue(SEND_1_TEXT_CHAT,new String[] { player.getDisplayName(), "I would like to purchase a beer please."},IS_PLAYER, player.getIndex(), 9827);
				stage = 5;
				break;
			case 2:
				sendEntityDialogue(SEND_1_TEXT_CHAT,new String[] { player.getDisplayName(), "How much does one beer cost?"},IS_PLAYER, player.getIndex(), 9827);
				stage = 6;
				break;
			case 3:
				sendEntityDialogue(SEND_1_TEXT_CHAT,new String[] { player.getDisplayName(), "Nevermind, I don't drink."},IS_PLAYER, player.getIndex(), 9827);
				stage = FINISH;
				break;
			case 4:
				sendEntityDialogue(SEND_1_TEXT_CHAT,new String[] { player.getDisplayName(), "I will go pick up some money and come back."},IS_PLAYER, player.getIndex(), 9827);
				stage = FINISH;
				break;
			}
			break;
		case 5:
			if (player.getInventory().containsItem(995, 10)) {
				player.getInventory().deleteItem(995,10);
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
							"Enjoy the beer, "+player.getDisplayName()+" it's delicious!"}, IS_NPC, npcId, 9827);
			player.getInventory().addItem(1917, 1);
			stage = FINISH;
			} else
				sendEntityDialogue(SEND_2_TEXT_CHAT,
						new String[] { NPCDefinition.get(npcId).name,
								"I can't sell you any beer, you must have", "10 gold coins in your inventory to buy a beer."}, IS_NPC, npcId, 9827);
			stage = FINISH;
			break;	
		case FINISH:
			player.interfaces().closeChatBoxInterface();
			break;
		case 6:
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name,
							"I will sell you one super-delicious beer for", "only 10 gold coins, cheap deal isn't it!"}, IS_NPC, npcId, 9827);
			stage = 3;
			break;
		}
	}
	@Override
	public void finish() {

	}
	
}
