package org.nova.game.player.dialogues;

import org.nova.game.map.GlobalObject;
import org.nova.game.player.content.BonesOnAltar;
import org.nova.game.player.content.BonesOnAltar.Bones;
import org.nova.game.player.content.SkillsDialogue;

public class PrayerD extends MatrixDialogue {

		private Bones bones;
		private GlobalObject object;

		@Override
		public void start() {
			this.bones = (Bones) parameters[0];
			this.object = (GlobalObject) parameters[1];

			SkillsDialogue
					.sendSkillsDialogue(
							player,
							SkillsDialogue.OFFER,
							"How many would you like to offer?",
							player.getInventory().getItems()
									.getNumberOf(bones.getBone()),
							new int[] { bones.getBone().getId() }, null);
		}

		@Override
		public void run(int interfaceId, int componentId) {
			player.getActionManager().setAction(
					new BonesOnAltar(object, bones.getBone(), SkillsDialogue
							.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {

	}

}