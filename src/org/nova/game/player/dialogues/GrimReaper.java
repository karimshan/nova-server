package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.map.Location;
import org.nova.game.player.content.Magic;

public class GrimReaper extends MatrixDialogue {

	private int npcId;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_3_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Hello, I am the Grim Reaper, Mentios's newcomers guide.",
						"Mentios was created by Laake from a very crappy base!",
						"Here are some helpfull tips to get you started;"}, IS_NPC, npcId, 12379);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
			new String[] { player.getDisplayName(), "I'm hoping this is worth the time Reaper." },
			IS_PLAYER, player.getIndex(), 12379);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "Oh please, Mentios is the Best.",
								"You can view our range of teleports and commands,",
								"by clicking the quest tab and clicking any button.",
								"Only donate to Laake/King Mentios!"}, IS_NPC, npcId, 12379);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "Rules of Mentios!",
								"Advertising other servers is a direct ban!",
								"Bug/glitch abuse will result with a talking to.",
								"Any offensive language will result in a possible mute. "}, IS_NPC, npcId, 12379);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "NO Duping what so ever!",
								"Begging for staff or pestering staff is forbiden.",
								"This could all result in your account getting ban.",
								""}, IS_NPC, npcId, 12379);
			stage = 4;
		} else if (stage == 4) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "Please View the rest of the rules,",
								"and other infomation indebth on our forums.",
								"",
								""}, IS_NPC, npcId, 12379);
			stage = 5;
		} else if (stage == 5) { //
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinition.get(npcId).name, "",
								"We hope you enjoy your stay on Mentios!",
								".",
								""}, IS_NPC, npcId, 12379);
			stage = 6;


		} else if (stage == 6) {
				sendDialogue(SEND_2_OPTIONS, "Wana Play Mentios Now?",
				"Sure :D","Hell Yah");
              		if (componentId == 1) {
			Magic.sendNormalTeleportSpell(player, 0, 0,
					new Location(2847, 10219, 0));
		} else if (componentId == 2) {
		Magic.sendNormalTeleportSpell(player, 0, 0,
					new Location(2847, 10219, 0));
		} else
			end();
                }
        }

    @Override
    public void finish() {
    }
}


