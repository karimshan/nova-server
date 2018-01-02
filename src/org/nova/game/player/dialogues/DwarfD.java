package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.map.Location;
import org.nova.game.player.content.cities.Keldagrim;

public class DwarfD extends MatrixDialogue {

	private int npcId;

	
	
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
		sendDialogue(SEND_5_OPTIONS, "Choose a Option", "I would like to go Keldagrim, I came from far away.", "What are you doing here?", "Nothing sir, nevermind.", "", "");
		stage = 2;
		break;
	case 2:
		switch (componentId) {
		case 1://Takes to keldagrim.
			Keldagrim.shipTeleport(player, 0, 0, new Location(2939,10197,0));
			break;
		case 2:
			sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name, 
				"I'm here to take people to Keldagrim city;",
				"The home of dwarfs, "+player.getDisplayName()+"!"}, IS_NPC, npcId, 9827);
			stage = -1;
			break;
		case 3:
		case 4:
			player.interfaces().closeChatBoxInterface();
			break;
	case 5:
		player.interfaces().closeChatBoxInterface();
		end();
		break;
		}
			}
	}
	@Override
	public void finish() {

	}
	
}
