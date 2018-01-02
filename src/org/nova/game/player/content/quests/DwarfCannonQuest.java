package org.nova.game.player.content.quests;

import org.nova.game.map.GlobalObject;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * 
 * @author Fuzen Seth
 * @information Represents the Recipe for Disaster quest.
 */
public class DwarfCannonQuest {
	
	private static final int CHEST_ID = -1;
	
	private int railingsFixed;
	
	public static final void traceStages(Player player) {
		QuestMessage.setTitle(player, "Dwarf's Multicannon");
		switch (player.getQuestStage()) {
		case 0:
			new QuestMessage(player, "Go to the coal trucks near the Fishing guild, you will be seeing a short dwarf called Captain Lawgof. Speak with him to start this quest.");
			break;
		case 1:
			new QuestMessage(player, "Next we're going to fix railings to stay safe from the Goblins, fix all railings and return to Lawgof.");
			break;
		}
	}
	
	public static final boolean handleNPCTalk(Player player, NPC npc) {
		switch (npc.getId()) {
		
		}
		
		return false;
	}
	/**
	 * Fixes a railing.
	 * @param player
	 * @param object
	 */
	public static void fixRailing(Player player, GlobalObject object) {
		DwarfCannonQuest cannon = new DwarfCannonQuest();
		player.sendMessage("You succesfully fix the railing.");
		cannon.setRailingsFixed(cannon.getRailingsFixed() + 1);
	}
	
	public static final boolean handleChest(Player player, GlobalObject gameObject) {
		if (gameObject.getId() == CHEST_ID) {
			switch (player.getRecipeStage()) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
				
			}
		}
		return false;
	}

	public int getRailingsFixed() {
		return railingsFixed;
	}

	public void setRailingsFixed(int railingsFixed) {
		this.railingsFixed = railingsFixed;
	}
	
}
