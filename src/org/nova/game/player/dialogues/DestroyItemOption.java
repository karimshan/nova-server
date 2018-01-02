package org.nova.game.player.dialogues;

import org.nova.game.item.Item;

public class DestroyItemOption extends MatrixDialogue {

	int slotId;
	Item item;

	@Override
	public void start() {
		slotId = (Integer) parameters[0];
		item = (Item) parameters[1];
		player.interfaces().sendChatBoxInterface(94);
		player.packets().sendIComponentText(94, 8,
				item.defs().getName());
		player.packets().sendIComponentText(94, 7,
				"If you destroy this item, you will have to earn it again.");
		player.packets().sendItemOnIComponent(94, 9, item.getId(), 1);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (interfaceId == 94 && componentId == 3) {
			player.getInventory().deleteItem(slotId, item);
			player.getCharges().degradeCompletly(item);
			player.packets().sendSound(4500, 0, 1);
		}
		end();
	}

	@Override
	public void finish() {

	}

}
