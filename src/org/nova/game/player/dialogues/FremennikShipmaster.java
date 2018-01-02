package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.map.Location;
import org.nova.game.player.Player;

public class FremennikShipmaster extends MatrixDialogue {

	int npcId;
	boolean backing;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		backing = (Boolean) parameters[1];
		if (backing) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinition.get(npcId).name,
							"Do you want a lift back to the south?" }, IS_NPC,
					npcId, 9827);
		} else {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinition.get(npcId).name,
							"You want passage to Daemonheim?" }, IS_NPC, npcId,
					9827);
		}

	}

	@Override
	public void run(int interfaceId, int componentId) {
		// TODO Auto-generated method stub
		if (backing) {
			if (stage == -1) {
				stage = 0;
				sendDialogue(SEND_3_LARGE_OPTIONS, SEND_DEFAULT_OPTIONS_TITLE,
						"Yes, please.", "Not right now, thanks.",
						"You look happy.");
			} else if (stage == 0) {
				if (componentId == 2) {
					stage = 1;
					sendEntityDialogue(SEND_1_TEXT_CHAT,
							new String[] { player.getDisplayName(),
									"Yes, please." }, IS_PLAYER,
							player.getIndex(), 9827);
				} else
					// not coded options
					end();
			} else if (stage == 1) {
				stage = 2;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						NPCDefinition.get(npcId).name,
						"All aboard, then." }, IS_NPC, npcId, 9827);
			} else if (stage == 2) {
				sail(player, backing);
				end();
			}
		} else {
			if (stage == -1) {
				stage = 0;
				sendDialogue(SEND_4_OPTIONS, SEND_DEFAULT_OPTIONS_TITLE,
						"Yes, please.", "Not right now, thanks.",
						"Daemonheim?", "Why are you so grumpy?");
			} else if (stage == 0) {
				if (componentId == 1) {
					stage = 1;
					sendEntityDialogue(SEND_1_TEXT_CHAT,
							new String[] { player.getDisplayName(),
									"Yes, please." }, IS_PLAYER,
							player.getIndex(), 9827);
				} else
					// not coded options
					end();
			} else if (stage == 1) {
				stage = 2;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						NPCDefinition.get(npcId).name,
						"Well, don't stand arround. Get on board." }, IS_NPC,
						npcId, 9827);
			} else if (stage == 2) {
				sail(player, backing);
				end();
			}
		}

	}

	public static void sail(Player player, boolean backing) {
		player.useStairs(-1, backing ? new Location(3254, 3171, 0)
				: new Location(3511, 3692, 0), 2, 3);
		if (backing)
			player.getControllerManager().forceStop();
		else
			player.getControllerManager().startController("Kalaboss");
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}