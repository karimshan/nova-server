package org.nova.game.player.dialogues;

import org.nova.game.map.GlobalObject;
import org.nova.game.player.actions.Cooking;
import org.nova.game.player.actions.Cooking.Cookables;
import org.nova.game.player.content.SkillsDialogue;

public class CookingD extends MatrixDialogue {

	private Cookables cooking;
	private GlobalObject object;

	@Override
	public void start() {
		this.cooking = (Cookables) parameters[0];
		this.object = (GlobalObject) parameters[1];

		SkillsDialogue
				.sendSkillsDialogue(
						player,
						SkillsDialogue.COOK,
						"Choose how many you wish to cook,<br>then click on the item to begin.",
						player.getInventory().getItems()
								.getNumberOf(cooking.getRawItem()),
						new int[] { cooking.getProduct().getId() }, null);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setSkill(
				new Cooking(object, cooking.getRawItem(), SkillsDialogue
						.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {

	}

}
