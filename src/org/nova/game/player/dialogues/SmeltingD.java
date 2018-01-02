package org.nova.game.player.dialogues;

import org.nova.game.map.GlobalObject;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Smelting;
import org.nova.game.player.actions.Smelting.SmeltingBar;
import org.nova.game.player.content.SkillsDialogue;
import org.nova.game.player.content.SkillsDialogue.ItemNameFilter;

public class SmeltingD extends MatrixDialogue {

	private GlobalObject object;

	@Override
	public void start() {
		object = (GlobalObject) parameters[0];
		int[] ids = new int[SmeltingBar.values().length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = SmeltingBar.values()[i].getProducedBar().getId();
		SkillsDialogue
				.sendSkillsDialogue(
						player,
						SkillsDialogue.MAKE,
						"How many bars you would like to smelt?<br>Choose a number, then click the bar to begin.",
						28, ids, new ItemNameFilter() {
							int count = 0;

							@Override
							public String rename(String name) {
								SmeltingBar bar = SmeltingBar.values()[count++];
								if (player.getSkills()
										.getLevel(Skills.SMITHING) < bar
										.getLevelRequired())
									name = "<col=ff0000>" + name
											+ "<br><col=ff0000>Level "
											+ bar.getLevelRequired();
								return name;

							}
						});
	}

	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setSkill(
				new Smelting(SkillsDialogue.getItemSlot(componentId), object,
						SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {
	}
}
