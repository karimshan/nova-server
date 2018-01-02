package org.nova.game.player.dialogues;

import org.nova.game.player.content.itemactions.DungeoneeringShop;
import org.nova.game.player.content.itemactions.DungeoneeringShop.DungeonReward;

public class DungRewardConfirm extends MatrixDialogue {

	DungeonReward item;

  @Override
	public void start() {
		item = (DungeonReward) parameters[0];
		player.interfaces().sendChatBoxInterface(1183);
		player.packets().sendIComponentText(1183, 12, "It will cost "+ item.getCost() +" for "+item.getName()+".");
		player.packets().sendItemOnIComponent(1183, 13, item.getId(), 1);
		player.packets().sendIComponentText(1183, 7, "Are you sure you want to buy this?");
		player.packets().sendIComponentText(1183, 22, "Confirm Purchase");
	}

  @Override
	public void run(int interfaceId, int componentId) {
		player.packets().sendMessage("COMPONENTID: "+componentId);
		if (componentId == 9) {
			if (player.getInventory().getFreeSlots() >= 1) {
				player.setDungeoneeringPoints(player.getDungeoneeringPoints() - item.getCost());
				player.getInventory().addItem(item.getId(), 1);
			DungeoneeringShop.refresh(player);
			} else {
				player.packets().sendMessage("You need more inventory space.");
			}
		}
		end();
	}

  @Override
	public void finish() {

	}

}