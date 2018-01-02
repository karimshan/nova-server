package org.nova.game.player.dialogues;

import org.nova.game.player.actions.LeatherCrafting;
import org.nova.game.player.actions.LeatherCrafting.LeatherData;
import org.nova.game.player.content.SkillsDialogue;

public class LeatherCraftingD extends MatrixDialogue {

	@Override
	public void start() {
		int[] items = new int[parameters.length];
		for (int i = 0; i < items.length; i++)
			items[i] = ((LeatherData) parameters[i]).getFinalProduct();

		SkillsDialogue
				.sendSkillsDialogue(
						player,
						SkillsDialogue.MAKE,
						"Choose how many you wish to make,<br>then click on the item to begin.",
						28, items, null);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		int option = SkillsDialogue.getItemSlot(componentId);
		if (option > parameters.length) {
			end();
			return;
		}
		LeatherData data = (LeatherData) parameters[option];
		int quantity = SkillsDialogue.getQuantity(player);
		int invQuantity = player.getInventory().getItems()
				.getNumberOf(data.getLeatherId());
		if (quantity > invQuantity)
			quantity = invQuantity;
		player.getActionManager().setSkill(new LeatherCrafting(data, quantity));
		end();
	}

	@Override
	public void finish() {

	}

}