package org.nova.game.player.dialogues;

import org.nova.game.player.content.itemactions.CrystalKeyRewards;
import org.nova.game.player.content.quests.QuestNPCActions;

public class Switcher extends MatrixDialogue {
	@Override
	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Select a option", "Modern spellbook",
				"Ancient spellbook", "Lunar spellbook", "Switch prayers",
				"Exchange Crystal key"); // Change options maybe?
	}

	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_5_OPTIONS) {
			switch (componentId) {
			case 1:
				QuestNPCActions.switchModerns(player);
				break;
			case 2:
				QuestNPCActions.swichAncients(player);
				break;
			case 3:
				QuestNPCActions.switchLunars(player);
				break;
			case 4:
				if (player.isCompletedRuinsofUzer() == true) {
					QuestNPCActions.switchCurses(player);
				} else {
					player.interfaces().closeChatBoxInterface();
					player.sm("You have to complete battle quest: Ruins of Uzer to use curses.");

				}
				break;
			case 5:
				player.interfaces().closeChatBoxInterface();
				CrystalKeyRewards.getCrystalKeyRewards().sendReward(player);
				break;

			}
		}
	}

	@Override
	public void finish() {

	}

}