package org.nova.game.player.dialogues;

import org.nova.game.player.actions.GemCutting;
import org.nova.game.player.actions.GemCutting.Gem;
import org.nova.game.player.content.SkillsDialogue;

public class GemCuttingD extends MatrixDialogue {

	private Gem gem;

	@Override
	public void start() {
		this.gem = (Gem) parameters[0];
		SkillsDialogue
				.sendSkillsDialogue(
						player,
						SkillsDialogue.CUT,
						"Choose how many you wish to cut,<br>then click on the item to begin.",
						player.getInventory().getItems()
								.getNumberOf(gem.getUncut()),
						new int[] { gem.getUncut() }, null);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setSkill(
				new GemCutting(gem, SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {

	}

}
